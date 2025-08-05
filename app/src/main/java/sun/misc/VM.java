package sun.misc;

import java.io.IOException;
import java.lang.Thread;
import java.util.Properties;
import sun.misc.Launcher;
import sun.security.pkcs11.wrapper.PKCS11Constants;

/* loaded from: rt.jar:sun/misc/VM.class */
public class VM {

    @Deprecated
    public static final int STATE_GREEN = 1;

    @Deprecated
    public static final int STATE_YELLOW = 2;

    @Deprecated
    public static final int STATE_RED = 3;
    private static boolean pageAlignDirectMemory;
    private static final int JVMTI_THREAD_STATE_ALIVE = 1;
    private static final int JVMTI_THREAD_STATE_TERMINATED = 2;
    private static final int JVMTI_THREAD_STATE_RUNNABLE = 4;
    private static final int JVMTI_THREAD_STATE_BLOCKED_ON_MONITOR_ENTER = 1024;
    private static final int JVMTI_THREAD_STATE_WAITING_INDEFINITELY = 16;
    private static final int JVMTI_THREAD_STATE_WAITING_WITH_TIMEOUT = 32;
    private static boolean suspended = false;
    private static volatile boolean booted = false;
    private static final Object lock = new Object();
    private static long directMemory = PKCS11Constants.CKF_EC_CURVENAME;
    private static boolean defaultAllowArraySyntax = false;
    private static boolean allowArraySyntax = defaultAllowArraySyntax;
    private static final Properties savedProps = new Properties();
    private static volatile int finalRefCount = 0;
    private static volatile int peakFinalRefCount = 0;

    public static native ClassLoader latestUserDefinedLoader0();

    private static native void initialize();

    static {
        initialize();
    }

    @Deprecated
    public static boolean threadsSuspended() {
        return suspended;
    }

    public static boolean allowThreadSuspension(ThreadGroup threadGroup, boolean z2) {
        return threadGroup.allowThreadSuspension(z2);
    }

    @Deprecated
    public static boolean suspendThreads() {
        suspended = true;
        return true;
    }

    @Deprecated
    public static void unsuspendThreads() {
        suspended = false;
    }

    @Deprecated
    public static void unsuspendSomeThreads() {
    }

    @Deprecated
    public static final int getState() {
        return 1;
    }

    @Deprecated
    public static void registerVMNotification(VMNotification vMNotification) {
    }

    @Deprecated
    public static void asChange(int i2, int i3) {
    }

    @Deprecated
    public static void asChange_otherthread(int i2, int i3) {
    }

    public static void booted() {
        synchronized (lock) {
            booted = true;
            lock.notifyAll();
        }
    }

    public static boolean isBooted() {
        return booted;
    }

    public static void awaitBooted() throws InterruptedException {
        synchronized (lock) {
            while (!booted) {
                lock.wait();
            }
        }
    }

    public static long maxDirectMemory() {
        return directMemory;
    }

    public static boolean isDirectMemoryPageAligned() {
        return pageAlignDirectMemory;
    }

    public static boolean allowArraySyntax() {
        return allowArraySyntax;
    }

    public static boolean isSystemDomainLoader(ClassLoader classLoader) {
        return classLoader == null;
    }

    public static String getSavedProperty(String str) {
        if (savedProps.isEmpty()) {
            throw new IllegalStateException("Should be non-empty if initialized");
        }
        return savedProps.getProperty(str);
    }

    public static void saveAndRemoveProperties(Properties properties) {
        if (booted) {
            throw new IllegalStateException("System initialization has completed");
        }
        savedProps.putAll(properties);
        String str = (String) properties.remove("sun.nio.MaxDirectMemorySize");
        if (str != null) {
            if (str.equals("-1")) {
                directMemory = Runtime.getRuntime().maxMemory();
            } else {
                long j2 = Long.parseLong(str);
                if (j2 > -1) {
                    directMemory = j2;
                }
            }
        }
        if ("true".equals((String) properties.remove("sun.nio.PageAlignDirectMemory"))) {
            pageAlignDirectMemory = true;
        }
        String property = properties.getProperty("sun.lang.ClassLoader.allowArraySyntax");
        allowArraySyntax = property == null ? defaultAllowArraySyntax : Boolean.parseBoolean(property);
        properties.remove("java.lang.Integer.IntegerCache.high");
        properties.remove("sun.zip.disableMemoryMapping");
        properties.remove("sun.java.launcher.diag");
        properties.remove("sun.cds.enableSharedLookupCache");
    }

    public static void initializeOSEnvironment() {
        if (!booted) {
            OSEnvironment.initialize();
        }
    }

    public static int getFinalRefCount() {
        return finalRefCount;
    }

    public static int getPeakFinalRefCount() {
        return peakFinalRefCount;
    }

    public static void addFinalRefCount(int i2) {
        finalRefCount += i2;
        if (finalRefCount > peakFinalRefCount) {
            peakFinalRefCount = finalRefCount;
        }
    }

    public static Thread.State toThreadState(int i2) {
        if ((i2 & 4) != 0) {
            return Thread.State.RUNNABLE;
        }
        if ((i2 & 1024) != 0) {
            return Thread.State.BLOCKED;
        }
        if ((i2 & 16) != 0) {
            return Thread.State.WAITING;
        }
        if ((i2 & 32) != 0) {
            return Thread.State.TIMED_WAITING;
        }
        if ((i2 & 2) != 0) {
            return Thread.State.TERMINATED;
        }
        if ((i2 & 1) == 0) {
            return Thread.State.NEW;
        }
        return Thread.State.RUNNABLE;
    }

    public static ClassLoader latestUserDefinedLoader() {
        ClassLoader classLoaderLatestUserDefinedLoader0 = latestUserDefinedLoader0();
        if (classLoaderLatestUserDefinedLoader0 != null) {
            return classLoaderLatestUserDefinedLoader0;
        }
        try {
            return Launcher.ExtClassLoader.getExtClassLoader();
        } catch (IOException e2) {
            return null;
        }
    }
}
