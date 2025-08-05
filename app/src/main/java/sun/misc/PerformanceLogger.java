package sun.misc;

import java.io.File;
import java.io.FileWriter;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Vector;
import sun.security.action.GetPropertyAction;

/* loaded from: rt.jar:sun/misc/PerformanceLogger.class */
public class PerformanceLogger {
    private static final int START_INDEX = 0;
    private static final int LAST_RESERVED = 0;
    private static boolean perfLoggingOn;
    private static boolean useNanoTime;
    private static Vector<TimeData> times;
    private static String logFileName;
    private static Writer logWriter;
    private static long baseTime;

    static {
        perfLoggingOn = false;
        useNanoTime = false;
        logFileName = null;
        logWriter = null;
        String str = (String) AccessController.doPrivileged(new GetPropertyAction("sun.perflog"));
        if (str != null) {
            perfLoggingOn = true;
            if (((String) AccessController.doPrivileged(new GetPropertyAction("sun.perflog.nano"))) != null) {
                useNanoTime = true;
            }
            if (str.regionMatches(true, 0, "file:", 0, 5)) {
                logFileName = str.substring(5);
            }
            if (logFileName != null && logWriter == null) {
                AccessController.doPrivileged(new PrivilegedAction<Void>() { // from class: sun.misc.PerformanceLogger.1
                    /* JADX WARN: Can't rename method to resolve collision */
                    @Override // java.security.PrivilegedAction
                    /* renamed from: run */
                    public Void run2() {
                        try {
                            File file = new File(PerformanceLogger.logFileName);
                            file.createNewFile();
                            Writer unused = PerformanceLogger.logWriter = new FileWriter(file);
                            return null;
                        } catch (Exception e2) {
                            System.out.println(((Object) e2) + ": Creating logfile " + PerformanceLogger.logFileName + ".  Log to console");
                            return null;
                        }
                    }
                });
            }
            if (logWriter == null) {
                logWriter = new OutputStreamWriter(System.out);
            }
        }
        times = new Vector<>(10);
        for (int i2 = 0; i2 <= 0; i2++) {
            times.add(new TimeData("Time " + i2 + " not set", 0L));
        }
    }

    public static boolean loggingEnabled() {
        return perfLoggingOn;
    }

    /* loaded from: rt.jar:sun/misc/PerformanceLogger$TimeData.class */
    static class TimeData {
        String message;
        long time;

        TimeData(String str, long j2) {
            this.message = str;
            this.time = j2;
        }

        String getMessage() {
            return this.message;
        }

        long getTime() {
            return this.time;
        }
    }

    private static long getCurrentTime() {
        if (useNanoTime) {
            return System.nanoTime();
        }
        return System.currentTimeMillis();
    }

    public static void setStartTime(String str) {
        if (loggingEnabled()) {
            setStartTime(str, getCurrentTime());
        }
    }

    public static void setBaseTime(long j2) {
        if (loggingEnabled()) {
            baseTime = j2;
        }
    }

    public static void setStartTime(String str, long j2) {
        if (loggingEnabled()) {
            times.set(0, new TimeData(str, j2));
        }
    }

    public static long getStartTime() {
        if (loggingEnabled()) {
            return times.get(0).getTime();
        }
        return 0L;
    }

    public static int setTime(String str) {
        if (loggingEnabled()) {
            return setTime(str, getCurrentTime());
        }
        return 0;
    }

    public static int setTime(String str, long j2) {
        int size;
        if (loggingEnabled()) {
            synchronized (times) {
                times.add(new TimeData(str, j2));
                size = times.size() - 1;
            }
            return size;
        }
        return 0;
    }

    public static long getTimeAtIndex(int i2) {
        if (loggingEnabled()) {
            return times.get(i2).getTime();
        }
        return 0L;
    }

    public static String getMessageAtIndex(int i2) {
        if (loggingEnabled()) {
            return times.get(i2).getMessage();
        }
        return null;
    }

    public static void outputLog(Writer writer) {
        if (loggingEnabled()) {
            try {
                synchronized (times) {
                    for (int i2 = 0; i2 < times.size(); i2++) {
                        TimeData timeData = times.get(i2);
                        if (timeData != null) {
                            writer.write(i2 + " " + timeData.getMessage() + ": " + (timeData.getTime() - baseTime) + "\n");
                        }
                    }
                }
                writer.flush();
            } catch (Exception e2) {
                System.out.println(((Object) e2) + ": Writing performance log to " + ((Object) writer));
            }
        }
    }

    public static void outputLog() {
        outputLog(logWriter);
    }
}
