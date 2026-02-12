package ir.joorjens.common.file;

import ir.joorjens.common.Utility;
import ir.joorjens.jmx.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public final class FileRW {
    private static final Logger log = LoggerFactory.getLogger(FileRW.class);
    private static final String UNICODE_UTF8 = "UTF-8";
    private static final String BASE64_REGEX = "^data:image/[^;]*;base64,?";

    public static String read(String fileAddress, String unicode, boolean isRelativeAddress) {
        final StringBuilder fileStr = new StringBuilder();
        final List<String> lines = readLine(fileAddress, unicode, isRelativeAddress);
        for (String line : lines) {
            fileStr.append(line).append("\n");
        }
        return fileStr.toString();
    }

    public static List<String> readLine(String fileAddress, String unicode, boolean isRelativeAddress) {
        return readLine(fileAddress, unicode, isRelativeAddress, Integer.MAX_VALUE, 0);
    }

    public static List<String> readLine(String fileAddress, String unicode, boolean isRelativeAddress, int lineCount, int fromLine) {
        List<String> lines = new ArrayList<>();
        InputStreamReader inputStreamReader = null;
        BufferedReader inputBufferedReader = null;
        try {
            if (isRelativeAddress) {
                fileAddress = Config.baseFolder + fileAddress;
            }
            File fileDir = new File(fileAddress);
            if (!Utility.isEmpty(unicode)) {
                inputStreamReader = new InputStreamReader(new FileInputStream(fileDir), UNICODE_UTF8);
            } else {
                inputStreamReader = new InputStreamReader(new FileInputStream(fileDir));
            }
            inputBufferedReader = new BufferedReader(inputStreamReader);

            String line;
            int lineC = 0;
            while ((line = inputBufferedReader.readLine()) != null && ++lineC <= lineCount) {
                if (fromLine <= lineC) {
                    lines.add(line);
                }
            }
        } catch (UnsupportedEncodingException e) {
            log.warn("@readLine(UnsupportedEncodingException). Message: " + e.getMessage());
        } catch (IOException e) {
            log.warn("@readLine(IOException). Message: " + e.getMessage());
        } finally {
            try {
                if (inputBufferedReader != null) {
                    inputBufferedReader.close();
                }
                if (inputStreamReader != null) {
                    inputStreamReader.close();
                }
            } catch (IOException e) {
                log.warn("@readLine(IOException) -> closing. Message: " + e.getMessage());
            }
        }

        return lines;
    }

    public static boolean write(String fileAddress, String content, String unicode, boolean isRelativeAddress, boolean append) {
        boolean OK = false;
        OutputStreamWriter outputStreamWriter = null;
        BufferedWriter bufferedWriter = null;
        try {
            if (unicode == null || unicode.trim().isEmpty()) {
                unicode = UNICODE_UTF8;
            }
            if (isRelativeAddress) {
                fileAddress = Config.baseFolder + fileAddress;
            }
            FileOutputStream fileOutputStream = new FileOutputStream(fileAddress, append);
            outputStreamWriter = new OutputStreamWriter(fileOutputStream, unicode);
            bufferedWriter = new BufferedWriter(outputStreamWriter);
            bufferedWriter.write(content);
            OK = true;
        } catch (UnsupportedEncodingException e) {
            log.error("@write(UnsupportedEncodingException). Message: " + e.getMessage());
        } catch (IOException e) {
            log.error("@write(IOException). Message: " + e.getMessage());
        } catch (Exception e) {
            log.error("@write(Exception). Message: " + e.getMessage());
        } finally {
            try {
                if (bufferedWriter != null) {
                    bufferedWriter.close();
                }
                if (outputStreamWriter != null) {
                    outputStreamWriter.close();
                }
            } catch (IOException e) {
                log.warn("@write(IOException) -> closing. Message: " + e.getMessage());
            }
        }
        return OK;
    }

    public static boolean isExist(String fileAddress, boolean isRelativeAddress, boolean fileOrDri) {
        if (isRelativeAddress) {
            fileAddress = Config.baseFolder + fileAddress;
        }
        File f = new File(fileAddress);
        return f.exists() && ((fileOrDri && !f.isDirectory()) || (!fileOrDri && f.isDirectory()));
    }

    public static boolean mkdir(String dirPath, boolean isRelativeAddress, boolean recursive) {
        if (isRelativeAddress) {
            dirPath = Config.baseFolder + dirPath;
        }
        File file = new File(dirPath);
        if (recursive) {
            return file.mkdirs();
        } else {
            return file.mkdir();
        }
    }

    public static void delete(String fileAddress, boolean isRelativeAddress) {
        if (isRelativeAddress) {
            fileAddress = Config.baseFolder + fileAddress;
        }
        File f = new File(fileAddress);
        f.deleteOnExit();
        log.debug("@deleteFile:" + fileAddress);
    }

    public static boolean copy(final InputStream in, final OutputStream out) {
        boolean OK = false;
        try {
            byte[] buffer = new byte[1024];
            int count;
            while ((count = in.read(buffer)) != -1) {
                out.write(buffer, 0, count);
            }
            out.flush(); // Flush out stream, to write any remaining buffered data
            OK = true;
        } catch (IOException e) {
            log.warn("@copy(IOException). Message: " + e.getMessage());
        }
        return OK;
    }

    public static List<String> getDirectoryContents(final String address) {
        final File currentDir = new File(address);
        return getDirectoryContents(currentDir);
    }

    public static List<String> getDirectoryContents(final File dir) {
        final List<String> list = new ArrayList<>();
        final File[] files = dir.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    list.addAll(getDirectoryContents(file));
                } else {
                    try {
                        list.add(file.getCanonicalPath());
                    } catch (IOException e) {
                        log.warn(String.format("IOException@getDirectoryContents. dir: %s, Message: %s", dir.getName(), e.getMessage()));
                    }
                }
            }
        }
        return list;
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
        File oldfile = new File(baseFolder + imageAddressOld);
        File newfile = new File(baseFolder + imageAddressNew);

        if (!imageAddressNew.equals(imageAddressOld) && isExist(imageAddressNew, false, true))
            delete(imageAddressNew, false);

        return oldfile.renameTo(newfile);
    }
    //-------------------------- Image --------------------------
}