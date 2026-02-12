package ir.joorjens.common.file;

import ir.joorjens.common.Utility;
import ir.joorjens.jmx.Config;
import ir.joorjens.model.util.TypeEnumeration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;

public final class ImageRW {
    private static final Logger log = LoggerFactory.getLogger(ImageRW.class);
    private static final String BASE64_PREFIX = "data:image/";
    private static final String BASE64_REGEX = "^data:image/[^;]*;base64,?";

    public static final int lthImageLowPixResizeHeight = 96, lthImageLowPixResizeWidth = 96,
            lthImageSmallPixResizeHeight = 320, lthImageSmallPixResizeWidth = 320,
            lthImageMedPixResizeHeight = 400, lthImageMedPixResizeWidth = 400;
    public static final String PREFIX_TMB_LOW = "tmb_low_";
    public static final String PREFIX_TMB_SMALL = "tmb_small_";
    public static final String PREFIX_TMB_MED = "tmb_med_";

    //-------------------------- Image --------------------------

    public static boolean isImageAddress(String basePath, String imageAddress) {
        return !Utility.isEmpty(imageAddress) && imageAddress.length() <= 100
                && imageAddress.startsWith(basePath);
    }

    /**
     * @param image        is address(from db) or base64(for upsert)
     * @param imageAddress imageAddress(default) without extension
     * @param preLink      previous image link
     * @return new address of image
     */
    public static String saveImage(String basePath, String image, String imageAddress, final String preLink, boolean low, boolean small, boolean med) {
        String link = "";
        try {
            //normalizing imageAddress if it has @2.png
            int index1 = imageAddress.lastIndexOf('@');
            int index2 = imageAddress.lastIndexOf('.');
            if (index1 > 0) {
                imageAddress = imageAddress.substring(0, index1);
            } else if (index2 > 0) {
                imageAddress = imageAddress.substring(0, index2);
            }
            //find version and extension
            final String folderName;
            String extension = ".png";
            int version = 0;
            if (!Utility.isEmpty(preLink)) {
                folderName = getFolderFromImageAddress(preLink);
                String preLinkTmp = preLink;
                index2 = preLinkTmp.lastIndexOf('.');
                if (index2 > 0) {
                    extension = preLinkTmp.substring(index2);
                    preLinkTmp = preLinkTmp.substring(0, index2);
                }
                index1 = preLinkTmp.lastIndexOf('@');
                if (index1 > 0) {
                    try {
                        version = Integer.parseInt(preLinkTmp.substring(++index1));
                    } catch (NumberFormatException ignored) {
                    }
                }
            } else {
                folderName = getFolderFromImageAddress(imageAddress);
            }
            //Saving image
            if (!Utility.isEmpty(image)) {
                if (image.startsWith(BASE64_PREFIX)) {
                    imageAddress = addFolderToImageAddress(imageAddress, folderName);
                    link = imageAddress + '@' + (++version) + '.' + image.substring(image.indexOf(BASE64_PREFIX) + BASE64_PREFIX.length(), image.indexOf(";"));
                    FileRW.saveImage(image, link, true);
                    saveThumbnail(link, extension.replace(".", ""), low, small, med);
                } else if (isImageAddress(basePath, preLink)) {
                    link = preLink;
                } else if (isImageAddress(basePath, image)) {
                    link = image;
                }
            } else if (isImageAddress(basePath, preLink)) {
                //if(!Utility.isEmpty(folderName))
                imageAddress = addFolderToImageAddress(imageAddress, folderName);
                FileRW.delete(imageAddress + '@' + version + extension, true);
            }
        } catch (Exception e) {
            log.debug(String.format("Exception@saveImage. Message: %s", e.getMessage()));
        }
        return link;
    }

    public static String confirmImage(String basePath, String image, String oldImageAddress, boolean low, boolean small, boolean med) {
        String address = "";
        oldImageAddress = oldImageAddress != null ? oldImageAddress : "";
        try {
            if (!Utility.isEmpty(image)) {
                image = image.replaceAll(PREFIX_TMB_LOW, "").replaceAll(PREFIX_TMB_SMALL, "").replaceAll(PREFIX_TMB_MED, "");
                if (image.contains(Config.TEMPORARY_PREFIX)) {
                    address = renameTempImage(basePath, oldImageAddress, low, small, med);
                } else if (image.startsWith(BASE64_PREFIX)) {
                    if (!Utility.isEmpty(oldImageAddress) && oldImageAddress.contains(Config.TEMPORARY_PREFIX)) {
                        address = removeSubString(oldImageAddress.substring(oldImageAddress.indexOf(basePath)), Config.TEMPORARY_PREFIX);
                        deleteTempImage(oldImageAddress);
                    } else {
                        address = oldImageAddress.substring(oldImageAddress.indexOf(basePath));
                    }
                    address = address.substring(0, address.indexOf("."));
                    address = saveImage(basePath, image, address, "", low, small, med);
                } else {
                    address = image.substring(image.indexOf(basePath));
                }
            }
        } catch (Exception e) {
            log.debug(String.format("Exception@confirmImage(%s,%s). Message: %s", image, oldImageAddress, e.getMessage()));
        }
        return address;
    }

    private static String addFolderToImageAddress(String imageAddress, String folderName) {
        String[] addressParts = imageAddress.split("/");
        if (addressParts.length == 2) { //-> image/139137/picName@1.png
            imageAddress = addressParts[0] + '/' + folderName;
            if (!FileRW.isExist(imageAddress, true, false)) {
                FileRW.mkdir(imageAddress, true, true);
            }
            imageAddress += '/' + addressParts[1];
        }
        return imageAddress;
    }

    private static String getFolderFromImageAddress(String imageAddress) {
        String[] addressParts = imageAddress.split("/");
        if (addressParts.length == 3) { //-> image/139137/picName@1.png
            return addressParts[1];
        }
        return "" + Utility.getTimeStamp(Utility.getCurrentTime(), TypeEnumeration.TS_WEEK, true);
    }

    public static String removeSubString(String mainStr, String removalStr) {
        int tempIndex = mainStr.indexOf(removalStr);
        if (tempIndex != -1) {
            String firstPart = mainStr.substring(0, tempIndex);
            String secondPart = mainStr.substring(tempIndex + removalStr.length());
            mainStr = firstPart + secondPart;
        }
        return mainStr;
    }

    public static void deleteTempImage(String imageAddress) {//// TODO: 7/13/17   not work!!!
        if (!Utility.isEmpty(imageAddress) && imageAddress.contains(Config.TEMPORARY_PREFIX)) {
            FileRW.delete(imageAddress, true);

            FileRW.delete(getThumbnailPath(imageAddress, PREFIX_TMB_LOW), true);
            FileRW.delete(getThumbnailPath(imageAddress, PREFIX_TMB_SMALL), true);
            FileRW.delete(getThumbnailPath(imageAddress, PREFIX_TMB_MED), true);
        }
    }

    public static String getThumbnailPath(String path, String prefix) {
        if (!Utility.isEmpty(path)) {
            int index = path.lastIndexOf("/") + 1;
            return path.substring(0, index) + prefix + path.substring(index);
        }
        return null;
    }

    private static String renameTempImage(String basePath, String oldImageAddress, boolean low, boolean small, boolean med) {
        String address = oldImageAddress;
        if (!Utility.isEmpty(oldImageAddress) && oldImageAddress.contains(Config.TEMPORARY_PREFIX)) {
            address = removeSubString(oldImageAddress.substring(oldImageAddress.indexOf(basePath)), Config.TEMPORARY_PREFIX);
            FileRW.renameFile(oldImageAddress, address, true);

            if (low)
                FileRW.renameFile(getThumbnailPath(oldImageAddress, PREFIX_TMB_LOW), getThumbnailPath(address, PREFIX_TMB_LOW), true);
            if (small)
                FileRW.renameFile(getThumbnailPath(oldImageAddress, PREFIX_TMB_SMALL), getThumbnailPath(address, PREFIX_TMB_SMALL), true);
            if (med)
                FileRW.renameFile(getThumbnailPath(oldImageAddress, PREFIX_TMB_MED), getThumbnailPath(address, PREFIX_TMB_MED), true);

        }
        return address;
    }

    public static String getNewPath(String path, String prefix) {
        String thumbnailPath = getThumbnailPath(path, prefix);
        if (!Utility.isEmpty(thumbnailPath)) {
            return Config.baseFolder + thumbnailPath;
        }
        return null;
    }

    //-------------------------- Image --------------------------
    public static boolean saveImage(String imageBase64, String imageAddress, boolean isRelativeAddress) {
        boolean OK = false;
        if (imageBase64 != null && imageAddress != null) {
            if (isRelativeAddress) {
                imageAddress = Config.baseFolder + imageAddress;
            }
            imageBase64 = imageBase64.replaceFirst(BASE64_REGEX, "");
            log.info("@saveImage:" + imageAddress);
            try {
                byte[] imageBytes = Utility.decodeBase64(imageBase64);
                FileOutputStream imageOutFile = new FileOutputStream(imageAddress);
                imageOutFile.write(imageBytes);
                imageOutFile.close();
                OK = true;
            } catch (Exception e) {
                log.error("IOException@saveImage. Message: " + e.getMessage());
            }
        }
        return OK;
    }

    public static String readImage(String imageAddress, boolean isRelativeAddress) {
        String imageBase64 = null;
        if (imageAddress != null) {
            if (isRelativeAddress) {
                imageAddress = Config.baseFolder + imageAddress;
            }
            File file = new File(imageAddress);
            try {
                FileInputStream imageInFile = new FileInputStream(file);
                byte imageData[] = new byte[(int) file.length()];
                imageInFile.read(imageData);
                imageBase64 = Utility.encodeBase64(imageData);
            } catch (Exception e) {
                log.warn("IOException@readImage. Message: " + e.getMessage());
            }
        }
        return imageBase64;
    }

    public static boolean renameFile(String imageAddressOld, String imageAddressNew, boolean isRelativeAddress) {
        String baseFolder = "";
        if (isRelativeAddress) {
            baseFolder = Config.baseFolder;
        }
        File oldFile = new File(baseFolder + imageAddressOld);
        File newFile = new File(baseFolder + imageAddressNew);

        if (!imageAddressNew.equals(imageAddressOld) && FileRW.isExist(imageAddressNew, false, true))
            FileRW.delete(imageAddressNew, false);

        return oldFile.renameTo(newFile);
    }
    //-------------------------- Image --------------------------

    public static void saveThumbnail(String path, String fileExtension, boolean low, boolean small, boolean med) throws IOException {
        try {
            String fullPath = Config.baseFolder + path;
            BufferedImage bufferedImage = ImageUtil.readImage(fullPath);
            if (low) {
                if (ImageUtil.isResizeable(bufferedImage.getHeight(), bufferedImage.getWidth(),//
                        lthImageLowPixResizeHeight, lthImageLowPixResizeWidth)) {
                    ImageIO.write(ImageUtil.resizeScalrHW(bufferedImage, //
                                    lthImageLowPixResizeHeight, lthImageLowPixResizeWidth), //
                            fileExtension, new File(ImageRW.getNewPath(path, PREFIX_TMB_LOW)));
                } else {
                    ImageIO.write(bufferedImage, fileExtension, new File(ImageRW.getNewPath(path, PREFIX_TMB_LOW)));
                }
            }
            if (small) {
                if (ImageUtil.isResizeable(bufferedImage.getHeight(), bufferedImage.getWidth(),//
                        lthImageSmallPixResizeHeight, lthImageSmallPixResizeWidth)) {
                    ImageIO.write(ImageUtil.resizeScalrHW(bufferedImage, //
                                    lthImageSmallPixResizeHeight, lthImageSmallPixResizeWidth), //
                            fileExtension, new File(ImageRW.getNewPath(path, PREFIX_TMB_SMALL)));
                } else {
                    ImageIO.write(bufferedImage, fileExtension, new File(ImageRW.getNewPath(path, PREFIX_TMB_SMALL)));
                }
            }
            if (med) {
                if (ImageUtil.isResizeable(bufferedImage.getHeight(), bufferedImage.getWidth(),//
                        lthImageMedPixResizeHeight, lthImageMedPixResizeWidth)) {
                    ImageIO.write(ImageUtil.resizeScalrHW(bufferedImage, //
                                    lthImageMedPixResizeHeight, lthImageMedPixResizeWidth), //
                            fileExtension, new File(ImageRW.getNewPath(path, PREFIX_TMB_MED)));
                } else {
                    ImageIO.write(bufferedImage, fileExtension, new File(ImageRW.getNewPath(path, PREFIX_TMB_MED)));
                }
            }
        } catch (Exception e) {
            log.debug(String.format("Exception@saveThumbnail(%S). Message: %s", path, e.getMessage()));
        }
    }

    //-------------------------- Image --------------------------
}