package net.lingala.zip4j.util;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.FileHeader;
import net.lingala.zip4j.model.ZipModel;
import org.icepdf.core.util.PdfOps;

/* loaded from: zip4j_1.3.1.jar:net/lingala/zip4j/util/Zip4jUtil.class */
public class Zip4jUtil {
    public static boolean isStringNotNullAndNotEmpty(String str) {
        if (str == null || str.trim().length() <= 0) {
            return false;
        }
        return true;
    }

    public static boolean checkOutputFolder(String path) throws ZipException {
        if (!isStringNotNullAndNotEmpty(path)) {
            throw new ZipException(new NullPointerException("output path is null"));
        }
        File file = new File(path);
        if (file.exists()) {
            if (!file.isDirectory()) {
                throw new ZipException("output folder is not valid");
            }
            if (!file.canWrite()) {
                throw new ZipException("no write access to output folder");
            }
            return true;
        }
        try {
            file.mkdirs();
            if (!file.isDirectory()) {
                throw new ZipException("output folder is not valid");
            }
            if (!file.canWrite()) {
                throw new ZipException("no write access to destination folder");
            }
            return true;
        } catch (Exception e2) {
            throw new ZipException("Cannot create destination folder");
        }
    }

    public static boolean checkFileReadAccess(String path) throws ZipException {
        if (!isStringNotNullAndNotEmpty(path)) {
            throw new ZipException("path is null");
        }
        if (!checkFileExists(path)) {
            throw new ZipException(new StringBuffer("file does not exist: ").append(path).toString());
        }
        try {
            File file = new File(path);
            return file.canRead();
        } catch (Exception e2) {
            throw new ZipException("cannot read zip file");
        }
    }

    public static boolean checkFileWriteAccess(String path) throws ZipException {
        if (!isStringNotNullAndNotEmpty(path)) {
            throw new ZipException("path is null");
        }
        if (!checkFileExists(path)) {
            throw new ZipException(new StringBuffer("file does not exist: ").append(path).toString());
        }
        try {
            File file = new File(path);
            return file.canWrite();
        } catch (Exception e2) {
            throw new ZipException("cannot read zip file");
        }
    }

    public static boolean checkFileExists(String path) throws ZipException {
        if (!isStringNotNullAndNotEmpty(path)) {
            throw new ZipException("path is null");
        }
        File file = new File(path);
        return checkFileExists(file);
    }

    public static boolean checkFileExists(File file) throws ZipException {
        if (file == null) {
            throw new ZipException("cannot check if file exists: input file is null");
        }
        return file.exists();
    }

    public static boolean isWindows() {
        String os = System.getProperty("os.name").toLowerCase();
        return os.indexOf("win") >= 0;
    }

    public static void setFileReadOnly(File file) throws ZipException {
        if (file == null) {
            throw new ZipException("input file is null. cannot set read only file attribute");
        }
        if (file.exists()) {
            file.setReadOnly();
        }
    }

    public static void setFileHidden(File file) throws ZipException {
        if (file == null) {
            throw new ZipException("input file is null. cannot set hidden file attribute");
        }
        if (isWindows() && file.exists()) {
            try {
                Runtime.getRuntime().exec(new StringBuffer("attrib +H \"").append(file.getAbsolutePath()).append(PdfOps.DOUBLE_QUOTE__TOKEN).toString());
            } catch (IOException e2) {
            }
        }
    }

    public static void setFileArchive(File file) throws ZipException {
        if (file == null) {
            throw new ZipException("input file is null. cannot set archive file attribute");
        }
        if (isWindows() && file.exists()) {
            try {
                if (file.isDirectory()) {
                    Runtime.getRuntime().exec(new StringBuffer("attrib +A \"").append(file.getAbsolutePath()).append(PdfOps.DOUBLE_QUOTE__TOKEN).toString());
                } else {
                    Runtime.getRuntime().exec(new StringBuffer("attrib +A \"").append(file.getAbsolutePath()).append(PdfOps.DOUBLE_QUOTE__TOKEN).toString());
                }
            } catch (IOException e2) {
            }
        }
    }

    public static void setFileSystemMode(File file) throws ZipException {
        if (file == null) {
            throw new ZipException("input file is null. cannot set archive file attribute");
        }
        if (isWindows() && file.exists()) {
            try {
                Runtime.getRuntime().exec(new StringBuffer("attrib +S \"").append(file.getAbsolutePath()).append(PdfOps.DOUBLE_QUOTE__TOKEN).toString());
            } catch (IOException e2) {
            }
        }
    }

    public static long getLastModifiedFileTime(File file, TimeZone timeZone) throws ZipException {
        if (file == null) {
            throw new ZipException("input file is null, cannot read last modified file time");
        }
        if (!file.exists()) {
            throw new ZipException("input file does not exist, cannot read last modified file time");
        }
        return file.lastModified();
    }

    public static String getFileNameFromFilePath(File file) throws ZipException {
        if (file == null) {
            throw new ZipException("input file is null, cannot get file name");
        }
        if (file.isDirectory()) {
            return null;
        }
        return file.getName();
    }

    public static long getFileLengh(String file) throws ZipException {
        if (!isStringNotNullAndNotEmpty(file)) {
            throw new ZipException("invalid file name");
        }
        return getFileLengh(new File(file));
    }

    public static long getFileLengh(File file) throws ZipException {
        if (file == null) {
            throw new ZipException("input file is null, cannot calculate file length");
        }
        if (file.isDirectory()) {
            return -1L;
        }
        return file.length();
    }

    public static long javaToDosTime(long time) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(time);
        int year = cal.get(1);
        if (year < 1980) {
            return 2162688L;
        }
        return ((year - 1980) << 25) | ((cal.get(2) + 1) << 21) | (cal.get(5) << 16) | (cal.get(11) << 11) | (cal.get(12) << 5) | (cal.get(13) >> 1);
    }

    public static long dosToJavaTme(int dosTime) {
        int sec = 2 * (dosTime & 31);
        int min = (dosTime >> 5) & 63;
        int hrs = (dosTime >> 11) & 31;
        int day = (dosTime >> 16) & 31;
        int mon = ((dosTime >> 21) & 15) - 1;
        int year = ((dosTime >> 25) & 127) + 1980;
        Calendar cal = Calendar.getInstance();
        cal.set(year, mon, day, hrs, min, sec);
        cal.set(14, 0);
        return cal.getTime().getTime();
    }

    public static FileHeader getFileHeader(ZipModel zipModel, String fileName) throws ZipException {
        if (zipModel == null) {
            throw new ZipException(new StringBuffer("zip model is null, cannot determine file header for fileName: ").append(fileName).toString());
        }
        if (!isStringNotNullAndNotEmpty(fileName)) {
            throw new ZipException(new StringBuffer("file name is null, cannot determine file header for fileName: ").append(fileName).toString());
        }
        FileHeader fileHeader = getFileHeaderWithExactMatch(zipModel, fileName);
        if (fileHeader == null) {
            String fileName2 = fileName.replaceAll("\\\\", "/");
            fileHeader = getFileHeaderWithExactMatch(zipModel, fileName2);
            if (fileHeader == null) {
                fileHeader = getFileHeaderWithExactMatch(zipModel, fileName2.replaceAll("/", "\\\\"));
            }
        }
        return fileHeader;
    }

    public static FileHeader getFileHeaderWithExactMatch(ZipModel zipModel, String fileName) throws ZipException {
        if (zipModel == null) {
            throw new ZipException(new StringBuffer("zip model is null, cannot determine file header with exact match for fileName: ").append(fileName).toString());
        }
        if (!isStringNotNullAndNotEmpty(fileName)) {
            throw new ZipException(new StringBuffer("file name is null, cannot determine file header with exact match for fileName: ").append(fileName).toString());
        }
        if (zipModel.getCentralDirectory() == null) {
            throw new ZipException(new StringBuffer("central directory is null, cannot determine file header with exact match for fileName: ").append(fileName).toString());
        }
        if (zipModel.getCentralDirectory().getFileHeaders() == null) {
            throw new ZipException(new StringBuffer("file Headers are null, cannot determine file header with exact match for fileName: ").append(fileName).toString());
        }
        if (zipModel.getCentralDirectory().getFileHeaders().size() <= 0) {
            return null;
        }
        ArrayList fileHeaders = zipModel.getCentralDirectory().getFileHeaders();
        for (int i2 = 0; i2 < fileHeaders.size(); i2++) {
            FileHeader fileHeader = (FileHeader) fileHeaders.get(i2);
            String fileNameForHdr = fileHeader.getFileName();
            if (isStringNotNullAndNotEmpty(fileNameForHdr) && fileName.equalsIgnoreCase(fileNameForHdr)) {
                return fileHeader;
            }
        }
        return null;
    }

    public static int getIndexOfFileHeader(ZipModel zipModel, FileHeader fileHeader) throws ZipException {
        if (zipModel == null || fileHeader == null) {
            throw new ZipException("input parameters is null, cannot determine index of file header");
        }
        if (zipModel.getCentralDirectory() == null) {
            throw new ZipException("central directory is null, ccannot determine index of file header");
        }
        if (zipModel.getCentralDirectory().getFileHeaders() == null) {
            throw new ZipException("file Headers are null, cannot determine index of file header");
        }
        if (zipModel.getCentralDirectory().getFileHeaders().size() <= 0) {
            return -1;
        }
        String fileName = fileHeader.getFileName();
        if (!isStringNotNullAndNotEmpty(fileName)) {
            throw new ZipException("file name in file header is empty or null, cannot determine index of file header");
        }
        ArrayList fileHeaders = zipModel.getCentralDirectory().getFileHeaders();
        for (int i2 = 0; i2 < fileHeaders.size(); i2++) {
            FileHeader fileHeaderTmp = (FileHeader) fileHeaders.get(i2);
            String fileNameForHdr = fileHeaderTmp.getFileName();
            if (isStringNotNullAndNotEmpty(fileNameForHdr) && fileName.equalsIgnoreCase(fileNameForHdr)) {
                return i2;
            }
        }
        return -1;
    }

    public static ArrayList getFilesInDirectoryRec(File path, boolean readHiddenFiles) throws ZipException {
        if (path == null) {
            throw new ZipException("input path is null, cannot read files in the directory");
        }
        ArrayList result = new ArrayList();
        File[] filesAndDirs = path.listFiles();
        List filesDirs = Arrays.asList(filesAndDirs);
        if (!path.canRead()) {
            return result;
        }
        for (int i2 = 0; i2 < filesDirs.size(); i2++) {
            File file = (File) filesDirs.get(i2);
            if (file.isHidden() && !readHiddenFiles) {
                return result;
            }
            result.add(file);
            if (file.isDirectory()) {
                result.addAll(getFilesInDirectoryRec(file, readHiddenFiles));
            }
        }
        return result;
    }

    public static String getZipFileNameWithoutExt(String zipFile) throws ZipException {
        if (!isStringNotNullAndNotEmpty(zipFile)) {
            throw new ZipException("zip file name is empty or null, cannot determine zip file name");
        }
        String tmpFileName = zipFile;
        if (zipFile.indexOf(System.getProperty("file.separator")) >= 0) {
            tmpFileName = zipFile.substring(zipFile.lastIndexOf(System.getProperty("file.separator")));
        }
        if (tmpFileName.indexOf(".") > 0) {
            tmpFileName = tmpFileName.substring(0, tmpFileName.lastIndexOf("."));
        }
        return tmpFileName;
    }

    public static byte[] convertCharset(String str) throws ZipException {
        byte[] converted;
        try {
            String charSet = detectCharSet(str);
            if (charSet.equals(InternalZipConstants.CHARSET_CP850)) {
                converted = str.getBytes(InternalZipConstants.CHARSET_CP850);
            } else if (charSet.equals(InternalZipConstants.CHARSET_UTF8)) {
                converted = str.getBytes(InternalZipConstants.CHARSET_UTF8);
            } else {
                converted = str.getBytes();
            }
            return converted;
        } catch (UnsupportedEncodingException e2) {
            return str.getBytes();
        } catch (Exception e3) {
            throw new ZipException(e3);
        }
    }

    public static String decodeFileName(byte[] data, boolean isUTF8) {
        if (isUTF8) {
            try {
                return new String(data, InternalZipConstants.CHARSET_UTF8);
            } catch (UnsupportedEncodingException e2) {
                return new String(data);
            }
        }
        return getCp850EncodedString(data);
    }

    public static String getCp850EncodedString(byte[] data) {
        try {
            String retString = new String(data, InternalZipConstants.CHARSET_CP850);
            return retString;
        } catch (UnsupportedEncodingException e2) {
            return new String(data);
        }
    }

    public static String getAbsoluteFilePath(String filePath) throws ZipException {
        if (!isStringNotNullAndNotEmpty(filePath)) {
            throw new ZipException("filePath is null or empty, cannot get absolute file path");
        }
        File file = new File(filePath);
        return file.getAbsolutePath();
    }

    public static boolean checkArrayListTypes(ArrayList sourceList, int type) throws ZipException {
        if (sourceList == null) {
            throw new ZipException("input arraylist is null, cannot check types");
        }
        if (sourceList.size() <= 0) {
            return true;
        }
        boolean invalidFound = false;
        switch (type) {
            case 1:
                int i2 = 0;
                while (true) {
                    if (i2 >= sourceList.size()) {
                        break;
                    } else if (sourceList.get(i2) instanceof File) {
                        i2++;
                    } else {
                        invalidFound = true;
                        break;
                    }
                }
            case 2:
                int i3 = 0;
                while (true) {
                    if (i3 >= sourceList.size()) {
                        break;
                    } else if (sourceList.get(i3) instanceof String) {
                        i3++;
                    } else {
                        invalidFound = true;
                        break;
                    }
                }
        }
        return !invalidFound;
    }

    public static String detectCharSet(String str) throws ZipException {
        if (str == null) {
            throw new ZipException("input string is null, cannot detect charset");
        }
        try {
            byte[] byteString = str.getBytes(InternalZipConstants.CHARSET_CP850);
            String tempString = new String(byteString, InternalZipConstants.CHARSET_CP850);
            if (str.equals(tempString)) {
                return InternalZipConstants.CHARSET_CP850;
            }
            byte[] byteString2 = str.getBytes(InternalZipConstants.CHARSET_UTF8);
            String tempString2 = new String(byteString2, InternalZipConstants.CHARSET_UTF8);
            if (str.equals(tempString2)) {
                return InternalZipConstants.CHARSET_UTF8;
            }
            return InternalZipConstants.CHARSET_DEFAULT;
        } catch (UnsupportedEncodingException e2) {
            return InternalZipConstants.CHARSET_DEFAULT;
        } catch (Exception e3) {
            return InternalZipConstants.CHARSET_DEFAULT;
        }
    }

    public static int getEncodedStringLength(String str) throws ZipException {
        if (!isStringNotNullAndNotEmpty(str)) {
            throw new ZipException("input string is null, cannot calculate encoded String length");
        }
        String charset = detectCharSet(str);
        return getEncodedStringLength(str, charset);
    }

    public static int getEncodedStringLength(String str, String charset) throws ZipException {
        ByteBuffer byteBuffer;
        if (!isStringNotNullAndNotEmpty(str)) {
            throw new ZipException("input string is null, cannot calculate encoded String length");
        }
        if (!isStringNotNullAndNotEmpty(charset)) {
            throw new ZipException("encoding is not defined, cannot calculate string length");
        }
        try {
            if (charset.equals(InternalZipConstants.CHARSET_CP850)) {
                byteBuffer = ByteBuffer.wrap(str.getBytes(InternalZipConstants.CHARSET_CP850));
            } else if (charset.equals(InternalZipConstants.CHARSET_UTF8)) {
                byteBuffer = ByteBuffer.wrap(str.getBytes(InternalZipConstants.CHARSET_UTF8));
            } else {
                byteBuffer = ByteBuffer.wrap(str.getBytes(charset));
            }
        } catch (UnsupportedEncodingException e2) {
            byteBuffer = ByteBuffer.wrap(str.getBytes());
        } catch (Exception e3) {
            throw new ZipException(e3);
        }
        return byteBuffer.limit();
    }

    public static boolean isSupportedCharset(String charset) throws ZipException {
        if (!isStringNotNullAndNotEmpty(charset)) {
            throw new ZipException("charset is null or empty, cannot check if it is supported");
        }
        try {
            new String("a".getBytes(), charset);
            return true;
        } catch (UnsupportedEncodingException e2) {
            return false;
        } catch (Exception e3) {
            throw new ZipException(e3);
        }
    }

    public static ArrayList getSplitZipFiles(ZipModel zipModel) throws ZipException {
        if (zipModel == null) {
            throw new ZipException("cannot get split zip files: zipmodel is null");
        }
        if (zipModel.getEndCentralDirRecord() == null) {
            return null;
        }
        ArrayList retList = new ArrayList();
        String currZipFile = zipModel.getZipFile();
        String zipFileName = new File(currZipFile).getName();
        if (!isStringNotNullAndNotEmpty(currZipFile)) {
            throw new ZipException("cannot get split zip files: zipfile is null");
        }
        if (!zipModel.isSplitArchive()) {
            retList.add(currZipFile);
            return retList;
        }
        int numberOfThisDisk = zipModel.getEndCentralDirRecord().getNoOfThisDisk();
        if (numberOfThisDisk == 0) {
            retList.add(currZipFile);
            return retList;
        }
        for (int i2 = 0; i2 <= numberOfThisDisk; i2++) {
            if (i2 == numberOfThisDisk) {
                retList.add(zipModel.getZipFile());
            } else {
                String fileExt = ".z0";
                if (i2 > 9) {
                    fileExt = ".z";
                }
                String partFile = zipFileName.indexOf(".") >= 0 ? currZipFile.substring(0, currZipFile.lastIndexOf(".")) : new StringBuffer(String.valueOf(currZipFile)).append(fileExt).append(i2 + 1).toString();
                retList.add(partFile);
            }
        }
        return retList;
    }

    public static String getRelativeFileName(String file, String rootFolderInZip, String rootFolderPath) throws ZipException {
        String fileName;
        String tmpFileName;
        if (!isStringNotNullAndNotEmpty(file)) {
            throw new ZipException("input file path/name is empty, cannot calculate relative file name");
        }
        if (isStringNotNullAndNotEmpty(rootFolderPath)) {
            File rootFolderFile = new File(rootFolderPath);
            String rootFolderFileRef = rootFolderFile.getPath();
            if (!rootFolderFileRef.endsWith(InternalZipConstants.FILE_SEPARATOR)) {
                rootFolderFileRef = new StringBuffer(String.valueOf(rootFolderFileRef)).append(InternalZipConstants.FILE_SEPARATOR).toString();
            }
            String tmpFileName2 = file.substring(rootFolderFileRef.length());
            if (tmpFileName2.startsWith(System.getProperty("file.separator"))) {
                tmpFileName2 = tmpFileName2.substring(1);
            }
            File tmpFile = new File(file);
            if (tmpFile.isDirectory()) {
                tmpFileName = new StringBuffer(String.valueOf(tmpFileName2.replaceAll("\\\\", "/"))).append("/").toString();
            } else {
                String bkFileName = tmpFileName2.substring(0, tmpFileName2.lastIndexOf(tmpFile.getName()));
                tmpFileName = new StringBuffer(String.valueOf(bkFileName.replaceAll("\\\\", "/"))).append(tmpFile.getName()).toString();
            }
            fileName = tmpFileName;
        } else {
            File relFile = new File(file);
            if (relFile.isDirectory()) {
                fileName = new StringBuffer(String.valueOf(relFile.getName())).append("/").toString();
            } else {
                fileName = getFileNameFromFilePath(new File(file));
            }
        }
        if (isStringNotNullAndNotEmpty(rootFolderInZip)) {
            fileName = new StringBuffer(String.valueOf(rootFolderInZip)).append(fileName).toString();
        }
        if (!isStringNotNullAndNotEmpty(fileName)) {
            throw new ZipException("Error determining file name");
        }
        return fileName;
    }

    public static long[] getAllHeaderSignatures() {
        long[] allSigs = {67324752, 134695760, 33639248, 101010256, InternalZipConstants.DIGSIG, InternalZipConstants.ARCEXTDATREC, 134695760, InternalZipConstants.ZIP64ENDCENDIRLOC, InternalZipConstants.ZIP64ENDCENDIRREC, 1, 39169};
        return allSigs;
    }
}
