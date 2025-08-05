package com.intel.bluetooth;

import java.security.PrivilegedActionException;
import java.util.Properties;
import java.util.Vector;
import org.apache.commons.math3.geometry.VectorFormat;

/* loaded from: bluecove-2.1.1.jar:com/intel/bluetooth/UtilsJavaSE.class */
public class UtilsJavaSE {
    static final boolean javaSECompiledOut = false;
    static JavaSE5Features java5Helper;
    static final boolean canCallNotLoadedNativeMethod;
    static final int javaSpecificationVersion = getJavaSpecificationVersion();
    static boolean java13 = false;
    static boolean java14 = false;
    static boolean detectJava5Helper = true;
    static final boolean ibmJ9midp = detectJ9midp();

    /* loaded from: bluecove-2.1.1.jar:com/intel/bluetooth/UtilsJavaSE$JavaSE5Features.class */
    interface JavaSE5Features {
        void clearProperty(String str);
    }

    /* loaded from: bluecove-2.1.1.jar:com/intel/bluetooth/UtilsJavaSE$StackTraceLocation.class */
    static class StackTraceLocation {
        public String className;
        public String methodName;
        public String fileName;
        public int lineNumber;

        StackTraceLocation() {
        }
    }

    static {
        canCallNotLoadedNativeMethod = !ibmJ9midp;
    }

    private UtilsJavaSE() {
    }

    private static boolean detectJ9midp() {
        try {
            String ibmJ9config = System.getProperty("com.ibm.oti.configuration");
            return (ibmJ9config == null || ibmJ9config.indexOf("midp") == -1) ? false : true;
        } catch (SecurityException e2) {
            return false;
        }
    }

    private static int getJavaSpecificationVersion() {
        try {
            String javaV = System.getProperty("java.specification.version");
            if (javaV == null || javaV.length() < 3) {
                return 0;
            }
            return Integer.valueOf(javaV.charAt(2)).intValue();
        } catch (Throwable th) {
            return 0;
        }
    }

    private static void detectJava5Helper() {
        if (java13 || ibmJ9midp || !detectJava5Helper || javaSpecificationVersion < 5) {
            return;
        }
        detectJava5Helper = false;
        try {
            Class klass = Class.forName("com.intel.bluetooth.UtilsJavaSE5");
            java5Helper = (JavaSE5Features) klass.newInstance();
            DebugLog.debug("Use java 1.5+ features:", vmInfo());
        } catch (Throwable th) {
        }
    }

    static StackTraceLocation getLocation(Vector fqcnSet) {
        if (java13 || ibmJ9midp) {
            return null;
        }
        if (!java14) {
            try {
                Class.forName("java.lang.StackTraceElement");
                java14 = true;
                DebugLog.debug("Java 1.4+ detected:", vmInfo());
            } catch (ClassNotFoundException e2) {
                java13 = true;
                return null;
            }
        }
        try {
            return getLocationJava14(fqcnSet);
        } catch (Throwable th) {
            java13 = true;
            return null;
        }
    }

    static String vmInfo() {
        try {
            return new StringBuffer().append(System.getProperty("java.version")).append(VectorFormat.DEFAULT_SEPARATOR).append(System.getProperty("java.vm.name")).append(VectorFormat.DEFAULT_SEPARATOR).append(System.getProperty("java.vendor")).toString();
        } catch (SecurityException e2) {
            return "";
        }
    }

    private static StackTraceLocation getLocationJava14(Vector fqcnSet) {
        StackTraceElement[] ste = new Throwable().getStackTrace();
        for (int i2 = 0; i2 < ste.length - 1; i2++) {
            if (fqcnSet.contains(ste[i2].getClassName())) {
                String nextClassName = ste[i2 + 1].getClassName();
                if (!nextClassName.startsWith("java.") && !nextClassName.startsWith("sun.") && !fqcnSet.contains(nextClassName)) {
                    StackTraceElement st = ste[i2 + 1];
                    StackTraceLocation loc = new StackTraceLocation();
                    loc.className = st.getClassName();
                    loc.methodName = st.getMethodName();
                    loc.fileName = st.getFileName();
                    loc.lineNumber = st.getLineNumber();
                    return loc;
                }
            }
        }
        return null;
    }

    public static void threadSetDaemon(Thread thread) {
        try {
            if (!ibmJ9midp) {
                thread.setDaemon(true);
            }
        } catch (Throwable th) {
        }
    }

    static boolean runtimeAddShutdownHook(Thread thread) {
        try {
            if (ibmJ9midp) {
                IBMJ9Helper.addShutdownClass(thread);
                return true;
            }
            Runtime.getRuntime().addShutdownHook(thread);
            return true;
        } catch (Throwable th) {
            return false;
        }
    }

    static void runtimeRemoveShutdownHook(Thread thread) {
        try {
            if (!ibmJ9midp) {
                Runtime.getRuntime().removeShutdownHook(thread);
            }
        } catch (Throwable th) {
        }
    }

    static void setSystemProperty(String propertyName, String propertyValue) {
        if (ibmJ9midp) {
            return;
        }
        boolean propertySet = false;
        try {
            Properties props = System.getProperties();
            if (propertyValue != null) {
                props.put(propertyName, propertyValue);
                propertySet = propertyValue.equals(System.getProperty(propertyName));
            } else {
                props.remove(propertyName);
                propertySet = System.getProperty(propertyName) == null;
            }
        } catch (SecurityException e2) {
        }
        if (!propertySet) {
            try {
                if (propertyValue != null) {
                    System.setProperty(propertyName, propertyValue);
                } else {
                    detectJava5Helper();
                    if (java5Helper != null) {
                        java5Helper.clearProperty(propertyName);
                    }
                }
            } catch (Throwable th) {
            }
        }
    }

    public static Throwable initCause(Throwable throwable, Throwable cause) {
        if (!java14 || cause == null) {
            return throwable;
        }
        try {
            return throwable.initCause(cause);
        } catch (Throwable th) {
            return throwable;
        }
    }

    static boolean isCurrentThreadInterrupted() {
        if (ibmJ9midp) {
            return false;
        }
        return Thread.interrupted();
    }

    static Throwable getCause(PrivilegedActionException e2) {
        try {
            return e2.getCause();
        } catch (Throwable th) {
            try {
                return e2.getException();
            } catch (Throwable th2) {
                return null;
            }
        }
    }
}
