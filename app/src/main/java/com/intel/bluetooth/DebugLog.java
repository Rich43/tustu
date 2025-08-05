package com.intel.bluetooth;

import com.intel.bluetooth.UtilsJavaSE;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.Vector;
import jdk.internal.dynalink.CallSiteDescriptor;

/* loaded from: bluecove-2.1.1.jar:com/intel/bluetooth/DebugLog.class */
public abstract class DebugLog {
    private static final boolean debugCompiledOut = false;
    public static final int DEBUG = 1;
    public static final int ERROR = 4;
    private static boolean debugEnabled = false;
    private static boolean initialized = false;
    private static boolean debugInternalEnabled = false;
    private static final String FQCN;
    private static final Vector fqcnSet;
    private static boolean java13;
    private static Vector loggerAppenders;
    static Class class$com$intel$bluetooth$DebugLog;

    /* loaded from: bluecove-2.1.1.jar:com/intel/bluetooth/DebugLog$LoggerAppender.class */
    public interface LoggerAppender {
        void appendLog(int i2, String str, Throwable th);
    }

    /* loaded from: bluecove-2.1.1.jar:com/intel/bluetooth/DebugLog$LoggerAppenderExt.class */
    public interface LoggerAppenderExt extends LoggerAppender {
        boolean isLogEnabled(int i2);
    }

    static {
        Class clsClass$;
        if (class$com$intel$bluetooth$DebugLog == null) {
            clsClass$ = class$("com.intel.bluetooth.DebugLog");
            class$com$intel$bluetooth$DebugLog = clsClass$;
        } else {
            clsClass$ = class$com$intel$bluetooth$DebugLog;
        }
        FQCN = clsClass$.getName();
        fqcnSet = new Vector();
        java13 = false;
        loggerAppenders = new Vector();
        fqcnSet.addElement(FQCN);
    }

    static Class class$(String x0) {
        try {
            return Class.forName(x0);
        } catch (ClassNotFoundException x1) {
            throw new NoClassDefFoundError(x1.getMessage());
        }
    }

    private DebugLog() {
    }

    private static synchronized void initialize() {
        if (initialized) {
            return;
        }
        initialized = true;
        debugEnabled = BlueCoveImpl.getConfigProperty(BlueCoveConfigProperties.PROPERTY_DEBUG, false);
        if (debugEnabled) {
        }
        boolean useStdOut = BlueCoveImpl.getConfigProperty(BlueCoveConfigProperties.PROPERTY_DEBUG_STDOUT, true);
        debugInternalEnabled = useStdOut && debugEnabled;
        boolean useLog4j = BlueCoveImpl.getConfigProperty(BlueCoveConfigProperties.PROPERTY_DEBUG_LOG4J, true);
        if (useLog4j) {
            try {
                LoggerAppenderExt log4jAppender = (LoggerAppenderExt) Class.forName("com.intel.bluetooth.DebugLog4jAppender").newInstance();
                System.out.println("BlueCove log redirected to log4j");
                addAppender(log4jAppender);
                if (log4jAppender.isLogEnabled(1)) {
                    debugEnabled = true;
                }
            } catch (Throwable th) {
            }
        }
    }

    public static boolean isDebugEnabled() {
        if (!initialized) {
            initialize();
        }
        return debugEnabled;
    }

    public static void setDebugEnabled(boolean debugEnabled2) {
        if (!initialized) {
            initialize();
        }
        if (debugEnabled2) {
        }
        BlueCoveImpl.instance().enableNativeDebug(debugEnabled2);
        debugEnabled = debugEnabled2;
        debugInternalEnabled = debugEnabled;
    }

    public static void debug(String message) {
        if (isDebugEnabled()) {
            log(message, null, null);
            printLocation();
            callAppenders(1, message, null);
        }
    }

    public static void debug(String message, String v2) {
        if (isDebugEnabled()) {
            log(message, " ", v2);
            printLocation();
            callAppenders(1, new StringBuffer().append(message).append(" ").append(v2).toString(), null);
        }
    }

    public static void debug(String message, Throwable t2) {
        if (isDebugEnabled()) {
            log(message, " ", t2.toString());
            printLocation();
            if (!UtilsJavaSE.ibmJ9midp) {
                t2.printStackTrace(System.out);
            } else if (debugInternalEnabled) {
                t2.printStackTrace();
            }
            callAppenders(1, message, t2);
        }
    }

    public static void debug(String message, Object obj) {
        if (isDebugEnabled()) {
            log(message, " ", obj.toString());
            printLocation();
            callAppenders(1, new StringBuffer().append(message).append(" ").append(obj.toString()).toString(), null);
        }
    }

    public static void debug(String message, String v2, String v22) {
        if (isDebugEnabled()) {
            log(message, " ", new StringBuffer().append(v2).append(" ").append(v22).toString());
            printLocation();
            callAppenders(1, new StringBuffer().append(message).append(" ").append(v2).append(" ").append(v22).toString(), null);
        }
    }

    public static void debug(String message, long v2) {
        if (isDebugEnabled()) {
            log(message, " ", Long.toString(v2));
            printLocation();
            callAppenders(1, new StringBuffer().append(message).append(" ").append(Long.toString(v2)).toString(), null);
        }
    }

    public static void debug0x(String message, long v2) {
        if (isDebugEnabled()) {
            log(message, " 0x", Utils.toHexString(v2));
            printLocation();
            callAppenders(1, new StringBuffer().append(message).append(" 0x").append(Utils.toHexString(v2)).toString(), null);
        }
    }

    public static void debug0x(String message, String s2, long v2) {
        if (isDebugEnabled()) {
            log(message, new StringBuffer().append(" ").append(s2).append(" 0x").toString(), Utils.toHexString(v2));
            printLocation();
            callAppenders(1, new StringBuffer().append(message).append(" ").append(s2).append(" 0x").append(Utils.toHexString(v2)).toString(), null);
        }
    }

    public static void debug(String message, boolean v2) {
        if (isDebugEnabled()) {
            log(message, " ", String.valueOf(v2));
            printLocation();
            callAppenders(1, new StringBuffer().append(message).append(" ").append(v2).toString(), null);
        }
    }

    public static void debug(String message, byte[] data) {
        debug(message, data, 0, data == null ? 0 : data.length);
    }

    public static void debug(String message, byte[] data, int off, int len) {
        if (isDebugEnabled()) {
            StringBuffer buf = new StringBuffer();
            if (data == null) {
                buf.append(" null byte[]");
            } else {
                buf.append(" [");
                for (int i2 = off; i2 < off + len; i2++) {
                    if (i2 != off) {
                        buf.append(", ");
                    }
                    buf.append(new Byte(data[i2]).toString());
                }
                buf.append("]");
                if (len > 4) {
                    buf.append(" ").append(len);
                }
            }
            log(message, buf.toString(), null);
            printLocation();
            callAppenders(1, new StringBuffer().append(message).append(buf.toString()).toString(), null);
        }
    }

    public static void debug(String message, int[] data) {
        debug(message, data, 0, data == null ? 0 : data.length);
    }

    public static void debug(String message, int[] data, int off, int len) {
        if (isDebugEnabled()) {
            StringBuffer buf = new StringBuffer();
            if (data == null) {
                buf.append(" null int[]");
            } else {
                buf.append(" [");
                for (int i2 = off; i2 < off + len; i2++) {
                    if (i2 != off) {
                        buf.append(", ");
                    }
                    buf.append(Integer.toString(data[i2]));
                }
                buf.append("]");
                if (len > 4) {
                    buf.append(" ").append(len);
                }
            }
            log(message, buf.toString(), null);
            printLocation();
            callAppenders(1, new StringBuffer().append(message).append(buf.toString()).toString(), null);
        }
    }

    public static void nativeDebugCallback(String fileName, int lineN, String message) {
        if (fileName != null) {
            try {
                if (fileName.startsWith(".\\")) {
                    fileName = fileName.substring(2);
                }
            } catch (Throwable e2) {
                try {
                    System.out.println(new StringBuffer().append("Error when calling debug ").append((Object) e2).toString());
                    return;
                } catch (Throwable th) {
                    return;
                }
            }
        }
        debugNative(new StringBuffer().append(fileName).append(CallSiteDescriptor.TOKEN_DELIMITER).append(lineN).toString(), message);
    }

    public static void debugNative(String location, String message) {
        if (isDebugEnabled()) {
            log(message, "\n\t  ", location);
            callAppenders(1, message, null);
        }
    }

    public static void error(String message) {
        if (isDebugEnabled()) {
            log("error ", message, null);
            printLocation();
            callAppenders(4, message, null);
        }
    }

    public static void error(String message, long v2) {
        if (isDebugEnabled()) {
            log("error ", message, new StringBuffer().append(" ").append(v2).toString());
            printLocation();
            callAppenders(4, new StringBuffer().append(message).append(" ").append(v2).toString(), null);
        }
    }

    public static void error(String message, String v2) {
        if (isDebugEnabled()) {
            log("error ", message, new StringBuffer().append(" ").append(v2).toString());
            printLocation();
            callAppenders(4, new StringBuffer().append(message).append(" ").append(v2).toString(), null);
        }
    }

    public static void error(String message, Throwable t2) {
        if (isDebugEnabled()) {
            log("error ", message, new StringBuffer().append(" ").append((Object) t2).toString());
            printLocation();
            if (!UtilsJavaSE.ibmJ9midp) {
                t2.printStackTrace(System.out);
            } else if (debugInternalEnabled) {
                t2.printStackTrace();
            }
            callAppenders(4, message, t2);
        }
    }

    public static void fatal(String message) {
        log("error ", message, null);
        printLocation();
        callAppenders(4, message, null);
    }

    public static void fatal(String message, Throwable t2) {
        log("error ", message, new StringBuffer().append(" ").append((Object) t2).toString());
        printLocation();
        if (!UtilsJavaSE.ibmJ9midp) {
            t2.printStackTrace(System.out);
        } else if (debugInternalEnabled) {
            t2.printStackTrace();
        }
        callAppenders(4, message, t2);
    }

    private static void callAppenders(int level, String message, Throwable throwable) {
        Enumeration iter = loggerAppenders.elements();
        while (iter.hasMoreElements()) {
            LoggerAppender a2 = (LoggerAppender) iter.nextElement2();
            a2.appendLog(level, message, throwable);
        }
    }

    public static void addAppender(LoggerAppender newAppender) {
        loggerAppenders.addElement(newAppender);
    }

    public static void removeAppender(LoggerAppender newAppender) {
        loggerAppenders.removeElement(newAppender);
    }

    private static String d00(int i2) {
        if (i2 > 9) {
            return String.valueOf(i2);
        }
        return new StringBuffer().append("0").append(String.valueOf(i2)).toString();
    }

    private static String d000(int i2) {
        if (i2 > 99) {
            return String.valueOf(i2);
        }
        if (i2 > 9) {
            return new StringBuffer().append("0").append(String.valueOf(i2)).toString();
        }
        return new StringBuffer().append("00").append(String.valueOf(i2)).toString();
    }

    private static void log(String message, String va1, String va2) {
        if (!debugInternalEnabled) {
            return;
        }
        try {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date(System.currentTimeMillis()));
            StringBuffer sb = new StringBuffer();
            sb.append(d00(calendar.get(11))).append(CallSiteDescriptor.TOKEN_DELIMITER);
            sb.append(d00(calendar.get(12))).append(CallSiteDescriptor.TOKEN_DELIMITER);
            sb.append(d00(calendar.get(13))).append(".");
            sb.append(d000(calendar.get(14))).append(" ");
            sb.append(message);
            if (va1 != null) {
                sb.append(va1);
            }
            if (va2 != null) {
                sb.append(va2);
            }
            System.out.println(sb.toString());
        } catch (Throwable th) {
        }
    }

    private static void printLocation() {
        if (java13 || !debugInternalEnabled) {
            return;
        }
        try {
            UtilsJavaSE.StackTraceLocation ste = UtilsJavaSE.getLocation(fqcnSet);
            if (ste != null) {
                System.out.println(new StringBuffer().append("\t  ").append(formatLocation(ste)).toString());
            }
        } catch (Throwable th) {
            java13 = true;
        }
    }

    private static String formatLocation(UtilsJavaSE.StackTraceLocation ste) {
        if (ste == null) {
            return "";
        }
        return new StringBuffer().append(ste.className).append(".").append(ste.methodName).append("(").append(ste.fileName).append(CallSiteDescriptor.TOKEN_DELIMITER).append(ste.lineNumber).append(")").toString();
    }
}
