package gnu.io;

import java.io.RandomAccessFile;
import java.util.logging.Logger;
import net.lingala.zip4j.util.InternalZipConstants;

/* loaded from: RXTXcomm.jar:gnu/io/Zystem.class */
public class Zystem {
    public static final int SILENT_MODE = 0;
    public static final int FILE_MODE = 1;
    public static final int NET_MODE = 2;
    public static final int MEX_MODE = 3;
    public static final int PRINT_MODE = 4;
    public static final int J2EE_MSG_MODE = 5;
    public static final int J2SE_LOG_MODE = 6;
    static int mode = 0;
    private static String target;

    public Zystem(int i2) throws UnSupportedLoggerException {
        mode = i2;
        startLogger("asdf");
    }

    public Zystem() throws UnSupportedLoggerException {
        String property = System.getProperty("gnu.io.log.mode");
        if (property == null || "SILENT_MODE".equals(property)) {
            mode = 0;
        } else if ("FILE_MODE".equals(property)) {
            mode = 1;
        } else if ("NET_MODE".equals(property)) {
            mode = 2;
        } else if ("MEX_MODE".equals(property)) {
            mode = 3;
        } else if ("PRINT_MODE".equals(property)) {
            mode = 4;
        } else if ("J2EE_MSG_MODE".equals(property)) {
            mode = 5;
        } else if ("J2SE_LOG_MODE".equals(property)) {
            mode = 6;
        } else {
            try {
                mode = Integer.parseInt(property);
            } catch (NumberFormatException e2) {
                mode = 0;
            }
        }
        startLogger("asdf");
    }

    public void startLogger() throws UnSupportedLoggerException {
        if (mode == 0 || mode == 4) {
        } else {
            throw new UnSupportedLoggerException("Target Not Allowed");
        }
    }

    public void startLogger(String str) throws UnSupportedLoggerException {
        target = str;
    }

    public void finalize() {
        mode = 0;
        target = null;
    }

    public void filewrite(String str) {
        try {
            RandomAccessFile randomAccessFile = new RandomAccessFile(target, InternalZipConstants.WRITE_MODE);
            randomAccessFile.seek(randomAccessFile.length());
            randomAccessFile.writeBytes(str);
            randomAccessFile.close();
        } catch (Exception e2) {
            System.out.println("Debug output file write failed");
        }
    }

    public boolean report(String str) {
        if (mode != 2) {
            if (mode == 4) {
                System.out.println(str);
                return true;
            }
            if (mode != 3) {
                if (mode == 0) {
                    return true;
                }
                if (mode == 1) {
                    filewrite(str);
                    return false;
                }
                if (mode != 5 && mode == 6) {
                    Logger.getLogger("gnu.io").fine(str);
                    return true;
                }
                return false;
            }
            return false;
        }
        return false;
    }

    public boolean reportln() {
        if (mode != 2) {
            if (mode == 4) {
                System.out.println();
                return true;
            }
            if (mode != 3) {
                if (mode == 0) {
                    return true;
                }
                if (mode == 1) {
                    filewrite("\n");
                    return false;
                }
                if (mode == 5) {
                    return false;
                }
                return false;
            }
            return false;
        }
        return false;
    }

    public boolean reportln(String str) {
        if (mode != 2) {
            if (mode == 4) {
                System.out.println(str);
                return true;
            }
            if (mode != 3) {
                if (mode == 0) {
                    return true;
                }
                if (mode == 1) {
                    filewrite(new StringBuffer().append(str).append("\n").toString());
                    return false;
                }
                if (mode != 5 && mode == 6) {
                    return true;
                }
                return false;
            }
            return false;
        }
        return false;
    }
}
