package com.sun.webkit;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Paths;
import java.nio.file.attribute.FileAttribute;
import java.util.logging.Level;
import java.util.logging.Logger;

/* loaded from: jfxrt.jar:com/sun/webkit/FileSystem.class */
final class FileSystem {
    private static final int TYPE_UNKNOWN = 0;
    private static final int TYPE_FILE = 1;
    private static final int TYPE_DIRECTORY = 2;
    private static final Logger logger = Logger.getLogger(FileSystem.class.getName());

    private FileSystem() {
        throw new AssertionError();
    }

    private static boolean fwkFileExists(String path) {
        return new File(path).exists();
    }

    private static RandomAccessFile fwkOpenFile(String path, String mode) {
        try {
            return new RandomAccessFile(path, mode);
        } catch (FileNotFoundException | SecurityException ex) {
            logger.log(Level.FINE, String.format("Error determining size of file [%s]", path), (Throwable) ex);
            return null;
        }
    }

    private static void fwkCloseFile(RandomAccessFile raf) {
        try {
            raf.close();
        } catch (IOException ex) {
            logger.log(Level.FINE, String.format("Error while closing RandomAccessFile for file [%s]", raf), (Throwable) ex);
        }
    }

    private static int fwkReadFromFile(RandomAccessFile raf, ByteBuffer byteBuffer) {
        try {
            FileChannel fc = raf.getChannel();
            return fc.read(byteBuffer);
        } catch (IOException ex) {
            logger.log(Level.FINE, String.format("Error while reading RandomAccessFile for file [%s]", raf), (Throwable) ex);
            return -1;
        }
    }

    private static void fwkSeekFile(RandomAccessFile raf, long pos) {
        try {
            raf.seek(pos);
        } catch (IOException ex) {
            logger.log(Level.FINE, String.format("Error while seek RandomAccessFile for file [%s]", raf), (Throwable) ex);
        }
    }

    private static long fwkGetFileSize(String path) {
        try {
            File file = new File(path);
            if (file.exists()) {
                return file.length();
            }
            return -1L;
        } catch (SecurityException ex) {
            logger.log(Level.FINE, String.format("Error determining size of file [%s]", path), (Throwable) ex);
            return -1L;
        }
    }

    private static boolean fwkGetFileMetadata(String path, long[] metadataArray) {
        try {
            File file = new File(path);
            if (file.exists()) {
                metadataArray[0] = file.lastModified();
                metadataArray[1] = file.length();
                if (file.isDirectory()) {
                    metadataArray[2] = 2;
                    return true;
                }
                if (file.isFile()) {
                    metadataArray[2] = 1;
                    return true;
                }
                metadataArray[2] = 0;
                return true;
            }
            return false;
        } catch (SecurityException ex) {
            logger.log(Level.FINE, String.format("Error determining Metadata for file [%s]", path), (Throwable) ex);
            return false;
        }
    }

    private static String fwkPathByAppendingComponent(String path, String component) {
        return new File(path, component).getPath();
    }

    private static boolean fwkMakeAllDirectories(String path) {
        try {
            Files.createDirectories(Paths.get(path, new String[0]), new FileAttribute[0]);
            return true;
        } catch (IOException | InvalidPathException ex) {
            logger.log(Level.FINE, String.format("Error creating directory [%s]", path), (Throwable) ex);
            return false;
        }
    }

    private static String fwkPathGetFileName(String path) {
        return new File(path).getName();
    }
}
