package com.sun.javafx.tk.quantum;

import com.sun.javafx.tk.Toolkit;
import com.sun.prism.impl.PrismSettings;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.security.AccessController;
import java.security.PrivilegedAction;

/* loaded from: jfxrt.jar:com/sun/javafx/tk/quantum/PerformanceTrackerHelper.class */
abstract class PerformanceTrackerHelper {
    private static final PerformanceTrackerHelper instance = createInstance();

    public abstract void logEvent(String str);

    public abstract void outputLog();

    public abstract boolean isPerfLoggingEnabled();

    public static PerformanceTrackerHelper getInstance() {
        return instance;
    }

    private PerformanceTrackerHelper() {
    }

    private static PerformanceTrackerHelper createInstance() {
        PerformanceTrackerHelper trackerImpl = (PerformanceTrackerHelper) AccessController.doPrivileged(new PrivilegedAction<PerformanceTrackerHelper>() { // from class: com.sun.javafx.tk.quantum.PerformanceTrackerHelper.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.security.PrivilegedAction
            public PerformanceTrackerHelper run() {
                try {
                    if (PrismSettings.perfLog != null) {
                        final PerformanceTrackerHelper trackerImpl2 = new PerformanceTrackerDefaultImpl();
                        if (PrismSettings.perfLogExitFlush) {
                            Runtime.getRuntime().addShutdownHook(new Thread() { // from class: com.sun.javafx.tk.quantum.PerformanceTrackerHelper.1.1
                                @Override // java.lang.Thread, java.lang.Runnable
                                public void run() {
                                    trackerImpl2.outputLog();
                                }
                            });
                        }
                        return trackerImpl2;
                    }
                    return null;
                } catch (Throwable th) {
                    return null;
                }
            }
        });
        if (trackerImpl == null) {
            trackerImpl = new PerformanceTrackerDummyImpl();
        }
        return trackerImpl;
    }

    public final long nanoTime() {
        return Toolkit.getToolkit().getMasterTimer().nanos();
    }

    /* loaded from: jfxrt.jar:com/sun/javafx/tk/quantum/PerformanceTrackerHelper$PerformanceTrackerDefaultImpl.class */
    private static final class PerformanceTrackerDefaultImpl extends PerformanceTrackerHelper {
        private long firstTime;
        private long lastTime;
        private final Method logEventMethod;
        private final Method outputLogMethod;
        private final Method getStartTimeMethod;
        private final Method setStartTimeMethod;

        public PerformanceTrackerDefaultImpl() throws NoSuchMethodException, ClassNotFoundException {
            super();
            Class perfLoggerClass = Class.forName("sun.misc.PerformanceLogger", true, null);
            this.logEventMethod = perfLoggerClass.getMethod("setTime", String.class);
            this.outputLogMethod = perfLoggerClass.getMethod("outputLog", new Class[0]);
            this.getStartTimeMethod = perfLoggerClass.getMethod("getStartTime", new Class[0]);
            this.setStartTimeMethod = perfLoggerClass.getMethod("setStartTime", String.class, Long.TYPE);
        }

        @Override // com.sun.javafx.tk.quantum.PerformanceTrackerHelper
        public void logEvent(String s2) {
            long time = System.currentTimeMillis();
            if (this.firstTime == 0) {
                this.firstTime = time;
            }
            try {
                this.logEventMethod.invoke(null, "JavaFX> " + s2 + " (" + (time - this.firstTime) + "ms total, " + (time - this.lastTime) + "ms)");
            } catch (IllegalAccessException e2) {
            } catch (IllegalArgumentException e3) {
            } catch (InvocationTargetException e4) {
            }
            this.lastTime = time;
        }

        @Override // com.sun.javafx.tk.quantum.PerformanceTrackerHelper
        public void outputLog() {
            logLaunchTime();
            try {
                this.outputLogMethod.invoke(null, new Object[0]);
            } catch (Exception e2) {
            }
        }

        @Override // com.sun.javafx.tk.quantum.PerformanceTrackerHelper
        public boolean isPerfLoggingEnabled() {
            return true;
        }

        private void logLaunchTime() {
            String launchTimeString;
            try {
                if (((Long) this.getStartTimeMethod.invoke(null, new Object[0])).longValue() <= 0 && (launchTimeString = (String) AccessController.doPrivileged(() -> {
                    return System.getProperty("launchTime");
                })) != null && !launchTimeString.equals("")) {
                    long launchTime = Long.parseLong(launchTimeString);
                    this.setStartTimeMethod.invoke(null, "LaunchTime", Long.valueOf(launchTime));
                }
            } catch (Throwable t2) {
                t2.printStackTrace();
            }
        }
    }

    /* loaded from: jfxrt.jar:com/sun/javafx/tk/quantum/PerformanceTrackerHelper$PerformanceTrackerDummyImpl.class */
    private static final class PerformanceTrackerDummyImpl extends PerformanceTrackerHelper {
        private PerformanceTrackerDummyImpl() {
            super();
        }

        @Override // com.sun.javafx.tk.quantum.PerformanceTrackerHelper
        public void logEvent(String s2) {
        }

        @Override // com.sun.javafx.tk.quantum.PerformanceTrackerHelper
        public void outputLog() {
        }

        @Override // com.sun.javafx.tk.quantum.PerformanceTrackerHelper
        public boolean isPerfLoggingEnabled() {
            return false;
        }
    }
}
