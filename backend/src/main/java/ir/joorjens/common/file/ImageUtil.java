package ir.joorjens.common.file;

import org.imgscalr.Scalr;
import org.imgscalr.Scalr.Mode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.Raster;
import java.io.*;
import java.util.Iterator;

public class ImageUtil {
    private static final Logger log = LoggerFactory.getLogger(ImageUtil.class);

    public static final int lthImageQualityWHLow = 20, lthImageQualityHorizontalHeight = 10
            , lthImageQualityHorizontalRatio = 20, lthImageQualitySize = 10, lthImageQualityLengthLow = 20;

    // ----------------------------------------------------------------------------------------------------------
    // ImageFeatures

    public static int calcImageLength(int height, int width) {
        return ((int) Math.sqrt(Math.pow(height, 2) + Math.pow(width, 2)));
    }

    public static boolean hasQualityForIndex(int size, int height, int width) {
        if (height < lthImageQualityWHLow || width < lthImageQualityWHLow)
            return false;

        if (height < lthImageQualityHorizontalHeight && (width / height) > lthImageQualityHorizontalRatio)
            return false;

        height = calcImageLength(height, width);
        if (size < lthImageQualitySize && height < lthImageQualityLengthLow)
            return false;

        return true;
    }

    // ----------------------------------------------------------------------------------------------------------
    // Reading
    public static BufferedImage readImage(String imageAddress) throws IOException {
        File file = new File(imageAddress);
        return ImageIO.read(file);
    }

    // ----------------------------------------------------------------------------------------------------------

    // ----------------------------------------------------------------------------------------------------------
    // Resizing

    /**
     * is it good for resizing?
     *
     * @param imageHeight
     * @param imageWidth
     * @param fitHeightTo
     * @param fitWidthTo
     * @return
     */
    public static boolean isResizeable(int imageHeight, int imageWidth, int fitHeightTo, int fitWidthTo) {
        if (imageHeight <= fitHeightTo && imageWidth <= fitWidthTo)
            return false;
        return true;
    }

    public static BufferedImage resizeScalr(final BufferedImage image, int fitHeightTo, int fitWidthTo) {
        return Scalr.resize(image, fitWidthTo, fitHeightTo);
    }

    public static BufferedImage resizeScalr(final BufferedImage image, int targetSize) {
        return Scalr.resize(image, targetSize);
    }

    public static BufferedImage resizeScalrHW(final BufferedImage image, int fitHeightTo, int fitWidthTo) {
        int imageHeight = image.getHeight(), imageWidth = image.getWidth();
        if ((imageHeight * 1.2) < imageWidth)
            return resizeScalrH(image, fitHeightTo);
        else
            return resizeScalrW(image, fitWidthTo);
    }

    private static BufferedImage resizeScalrW(final BufferedImage image, int fitToWidth) {
        return Scalr.resize(image, Mode.FIT_TO_WIDTH, fitToWidth, 0);
    }

    private static BufferedImage resizeScalrH(final BufferedImage image, int fitToheight) {
        return Scalr.resize(image, Mode.FIT_TO_HEIGHT, 0, fitToheight);
    }

    /**
     * This function resize the image file and returns the BufferedImage object that can be saved to file system.
     */
    public static BufferedImage resizeImage(final BufferedImage image, int width, int height) {
        final BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        final Graphics2D graphics2D = bufferedImage.createGraphics();
        graphics2D.setComposite(AlphaComposite.Src);
        graphics2D.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        graphics2D.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphics2D.drawImage(image, 0, 0, width, height, null);
        graphics2D.dispose();
        return bufferedImage;
    }

    // ----------------------------------------------------------------------------------------------------------

    // ----------------------------------------------------------------------------------------------------------
    // Copying

    /**
     * @param source      file name
     * @param destination file name
     * @return
     * @throws java.io.IOException
     */
    public static boolean resizeAndCopyImage(String source, String destination, boolean mkdir, int fitHeightTo, int fitWidthTo) throws IOException, FileNotFoundException, NullPointerException {
        File dfile = new File(destination);
        if (dfile.exists()) {
            // if destination is exists
            return true;
        }
        File sfile = new File(source);
        if (!sfile.isFile()) {
            // if source is not exists or is not a file
            return false;
        }
        String fileName = sfile.getName();
        if (!destination.endsWith(fileName)) {
            // if copying is not exact copy
            return false;
        }
        int indexOf = fileName.indexOf(".");
        if (indexOf == -1) {
            // if file does not have a format
            return false;
        }
        String formatName = fileName.substring(++indexOf);
        if (formatName == null) {
            // if file does not have a format
            return false;
        }
        if (mkdir) {
            String destDir = destination.substring(0, destination.length() - fileName.length());
            FileRW.mkdir(destDir, false, true);
        }
        BufferedImage sImage = ImageIO.read(sfile);
        BufferedImage dImage = null;
        if (fitHeightTo > 0 && fitWidthTo > 0) {
            dImage = resizeScalrHW(sImage, fitHeightTo, fitWidthTo);
        } else if (fitWidthTo > 0) {
            dImage = resizeScalrW(sImage, fitWidthTo);
        } else if (fitHeightTo > 0) {
            dImage = resizeScalrH(sImage, fitHeightTo);
        } else {
            // resize parameters is not valid
            return false;
        }
        ImageIO.write(dImage, formatName, dfile);
        return true;
    }

    // ----------------------------------------------------------------------------------------------------------

    public static String getImageFormat(byte[] bytesContent) throws IOException {
        String fileExtension = null;
        InputStream in = new ByteArrayInputStream(bytesContent);
        ImageInputStream imageInputStream = ImageIO.createImageInputStream(in);
        Iterator<ImageReader> iterator = ImageIO.getImageReaders(imageInputStream);
        if (iterator.hasNext()) {
            ImageReader imageReader = (ImageReader) iterator.next();
            fileExtension = imageReader.getFormatName().toLowerCase();
        }
        imageInputStream.close();
        return fileExtension;
    }

    public static BufferedImage getBufferedImage(byte[] bytesContent) throws IOException {
        InputStream in = new ByteArrayInputStream(bytesContent);
        return ImageIO.read(in);
    }

    @Deprecated
    public static byte[] getByteArray(BufferedImage image) throws IOException {
        Raster raster = image.getRaster();
        if (raster != null) {
            DataBufferByte dataBufferByte = (DataBufferByte) raster.getDataBuffer();
            if (dataBufferByte != null) {
                return dataBufferByte.getData();
            }
        }
        return null;
    }


    public static byte[] getByteArray(BufferedImage image, String formatName) throws IOException {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        ImageIO.write(image, formatName, os);
        return os.toByteArray();
    }

    public static InputStream getInputStream(BufferedImage image, String formatName) throws IOException {
        InputStream is = new ByteArrayInputStream(getByteArray(image, formatName));
        return is;
    }
}