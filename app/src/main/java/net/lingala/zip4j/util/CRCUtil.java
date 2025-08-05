package net.lingala.zip4j.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.CRC32;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.progress.ProgressMonitor;

/* loaded from: zip4j_1.3.1.jar:net/lingala/zip4j/util/CRCUtil.class */
public class CRCUtil {
    private static final int BUF_SIZE = 16384;

    public static long computeFileCRC(String inputFile) throws ZipException {
        return computeFileCRC(inputFile, null);
    }

    public static long computeFileCRC(String inputFile, ProgressMonitor progressMonitor) throws ZipException {
        if (!Zip4jUtil.isStringNotNullAndNotEmpty(inputFile)) {
            throw new ZipException("input file is null or empty, cannot calculate CRC for the file");
        }
        InputStream inputStream = null;
        try {
            try {
                Zip4jUtil.checkFileReadAccess(inputFile);
                InputStream inputStream2 = new FileInputStream(new File(inputFile));
                byte[] buff = new byte[16384];
                CRC32 crc32 = new CRC32();
                while (true) {
                    int readLen = inputStream2.read(buff);
                    if (readLen != -1) {
                        crc32.update(buff, 0, readLen);
                        if (progressMonitor != null) {
                            progressMonitor.updateWorkCompleted(readLen);
                            if (progressMonitor.isCancelAllTasks()) {
                                progressMonitor.setResult(3);
                                progressMonitor.setState(0);
                                if (inputStream2 == null) {
                                    return 0L;
                                }
                                try {
                                    inputStream2.close();
                                    return 0L;
                                } catch (IOException e2) {
                                    throw new ZipException("error while closing the file after calculating crc");
                                }
                            }
                        }
                    } else {
                        long value = crc32.getValue();
                        if (inputStream2 != null) {
                            try {
                                inputStream2.close();
                            } catch (IOException e3) {
                                throw new ZipException("error while closing the file after calculating crc");
                            }
                        }
                        return value;
                    }
                }
            } catch (IOException e4) {
                throw new ZipException(e4);
            } catch (Exception e5) {
                throw new ZipException(e5);
            }
        } catch (Throwable th) {
            if (0 != 0) {
                try {
                    inputStream.close();
                } catch (IOException e6) {
                    throw new ZipException("error while closing the file after calculating crc");
                }
            }
            throw th;
        }
    }
}
