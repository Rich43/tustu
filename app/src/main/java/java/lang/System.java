package java.lang;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.Console;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Executable;
import java.nio.channels.Channel;
import java.nio.channels.spi.SelectorProvider;
import java.security.AccessControlContext;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Map;
import java.util.Properties;
import java.util.PropertyPermission;
import jdk.internal.util.StaticProperty;
import sun.misc.JavaLangAccess;
import sun.misc.SharedSecrets;
import sun.misc.VM;
import sun.misc.Version;
import sun.nio.ch.Interruptible;
import sun.reflect.CallerSensitive;
import sun.reflect.ConstantPool;
import sun.reflect.Reflection;
import sun.reflect.annotation.AnnotationType;
import sun.security.util.SecurityConstants;

/* loaded from: rt.jar:java/lang/System.class */
public final class System {
    public static final InputStream in;
    public static final PrintStream out;
    public static final PrintStream err;
    private static volatile SecurityManager security;
    private static volatile Console cons;
    private static Properties props;
    private static String lineSeparator;

    private static native void registerNatives();

    private static native void setIn0(InputStream inputStream);

    private static native void setOut0(PrintStream printStream);

    private static native void setErr0(PrintStream printStream);

    public static native long currentTimeMillis();

    public static native long nanoTime();

    public static native void arraycopy(Object obj, int i2, Object obj2, int i3, int i4);

    public static native int identityHashCode(Object obj);

    private static native Properties initProperties(Properties properties);

    public static native String mapLibraryName(String str);

    static {
        registerNatives();
        in = null;
        out = null;
        err = null;
        security = null;
        cons = null;
    }

    private System() {
    }

    public static void setIn(InputStream inputStream) {
        checkIO();
        setIn0(inputStream);
    }

    public static void setOut(PrintStream printStream) {
        checkIO();
        setOut0(printStream);
    }

    public static void setErr(PrintStream printStream) {
        checkIO();
        setErr0(printStream);
    }

    public static Console console() {
        if (cons == null) {
            synchronized (System.class) {
                cons = SharedSecrets.getJavaIOAccess().console();
            }
        }
        return cons;
    }

    public static Channel inheritedChannel() throws IOException {
        return SelectorProvider.provider().inheritedChannel();
    }

    private static void checkIO() {
        SecurityManager securityManager = getSecurityManager();
        if (securityManager != null) {
            securityManager.checkPermission(new RuntimePermission("setIO"));
        }
    }

    public static void setSecurityManager(SecurityManager securityManager) {
        try {
            securityManager.checkPackageAccess("java.lang");
        } catch (Exception e2) {
        }
        setSecurityManager0(securityManager);
    }

    private static synchronized void setSecurityManager0(final SecurityManager securityManager) {
        SecurityManager securityManager2 = getSecurityManager();
        if (securityManager2 != null) {
            securityManager2.checkPermission(new RuntimePermission("setSecurityManager"));
        }
        if (securityManager != null && securityManager.getClass().getClassLoader() != null) {
            AccessController.doPrivileged(new PrivilegedAction<Object>() { // from class: java.lang.System.1
                @Override // java.security.PrivilegedAction
                public Object run() {
                    securityManager.getClass().getProtectionDomain().implies(SecurityConstants.ALL_PERMISSION);
                    return null;
                }
            });
        }
        security = securityManager;
    }

    public static SecurityManager getSecurityManager() {
        return security;
    }

    public static Properties getProperties() {
        SecurityManager securityManager = getSecurityManager();
        if (securityManager != null) {
            securityManager.checkPropertiesAccess();
        }
        return props;
    }

    public static String lineSeparator() {
        return lineSeparator;
    }

    public static void setProperties(Properties properties) {
        SecurityManager securityManager = getSecurityManager();
        if (securityManager != null) {
            securityManager.checkPropertiesAccess();
        }
        if (properties == null) {
            properties = new Properties();
            initProperties(properties);
        }
        props = properties;
    }

    public static String getProperty(String str) {
        checkKey(str);
        SecurityManager securityManager = getSecurityManager();
        if (securityManager != null) {
            securityManager.checkPropertyAccess(str);
        }
        return props.getProperty(str);
    }

    public static String getProperty(String str, String str2) {
        checkKey(str);
        SecurityManager securityManager = getSecurityManager();
        if (securityManager != null) {
            securityManager.checkPropertyAccess(str);
        }
        return props.getProperty(str, str2);
    }

    public static String setProperty(String str, String str2) {
        checkKey(str);
        SecurityManager securityManager = getSecurityManager();
        if (securityManager != null) {
            securityManager.checkPermission(new PropertyPermission(str, "write"));
        }
        return (String) props.setProperty(str, str2);
    }

    public static String clearProperty(String str) {
        checkKey(str);
        SecurityManager securityManager = getSecurityManager();
        if (securityManager != null) {
            securityManager.checkPermission(new PropertyPermission(str, "write"));
        }
        return (String) props.remove(str);
    }

    private static void checkKey(String str) {
        if (str == null) {
            throw new NullPointerException("key can't be null");
        }
        if (str.equals("")) {
            throw new IllegalArgumentException("key can't be empty");
        }
    }

    public static String getenv(String str) {
        SecurityManager securityManager = getSecurityManager();
        if (securityManager != null) {
            securityManager.checkPermission(new RuntimePermission("getenv." + str));
        }
        return ProcessEnvironment.getenv(str);
    }

    public static Map<String, String> getenv() {
        SecurityManager securityManager = getSecurityManager();
        if (securityManager != null) {
            securityManager.checkPermission(new RuntimePermission("getenv.*"));
        }
        return ProcessEnvironment.getenv();
    }

    public static void exit(int i2) {
        Runtime.getRuntime().exit(i2);
    }

    public static void gc() {
        Runtime.getRuntime().gc();
    }

    public static void runFinalization() {
        Runtime.getRuntime().runFinalization();
    }

    @Deprecated
    public static void runFinalizersOnExit(boolean z2) {
        Runtime.runFinalizersOnExit(z2);
    }

    @CallerSensitive
    public static void load(String str) {
        Runtime.getRuntime().load0(Reflection.getCallerClass(), str);
    }

    @CallerSensitive
    public static void loadLibrary(String str) {
        Runtime.getRuntime().loadLibrary0(Reflection.getCallerClass(), str);
    }

    private static PrintStream newPrintStream(FileOutputStream fileOutputStream, String str) {
        if (str != null) {
            try {
                return new PrintStream((OutputStream) new BufferedOutputStream(fileOutputStream, 128), true, str);
            } catch (UnsupportedEncodingException e2) {
            }
        }
        return new PrintStream((OutputStream) new BufferedOutputStream(fileOutputStream, 128), true);
    }

    private static void initializeSystemClass() {
        props = new Properties();
        initProperties(props);
        VM.saveAndRemoveProperties(props);
        lineSeparator = props.getProperty("line.separator");
        StaticProperty.jdkSerialFilter();
        Version.init();
        FileInputStream fileInputStream = new FileInputStream(FileDescriptor.in);
        FileOutputStream fileOutputStream = new FileOutputStream(FileDescriptor.out);
        FileOutputStream fileOutputStream2 = new FileOutputStream(FileDescriptor.err);
        setIn0(new BufferedInputStream(fileInputStream));
        setOut0(newPrintStream(fileOutputStream, props.getProperty("sun.stdout.encoding")));
        setErr0(newPrintStream(fileOutputStream2, props.getProperty("sun.stderr.encoding")));
        loadLibrary("zip");
        Terminator.setup();
        VM.initializeOSEnvironment();
        Thread threadCurrentThread = Thread.currentThread();
        threadCurrentThread.getThreadGroup().add(threadCurrentThread);
        setJavaLangAccess();
        VM.booted();
    }

    private static void setJavaLangAccess() {
        SharedSecrets.setJavaLangAccess(new JavaLangAccess() { // from class: java.lang.System.2
            @Override // sun.misc.JavaLangAccess
            public ConstantPool getConstantPool(Class<?> cls) {
                return cls.getConstantPool();
            }

            @Override // sun.misc.JavaLangAccess
            public boolean casAnnotationType(Class<?> cls, AnnotationType annotationType, AnnotationType annotationType2) {
                return cls.casAnnotationType(annotationType, annotationType2);
            }

            @Override // sun.misc.JavaLangAccess
            public AnnotationType getAnnotationType(Class<?> cls) {
                return cls.getAnnotationType();
            }

            @Override // sun.misc.JavaLangAccess
            public Map<Class<? extends Annotation>, Annotation> getDeclaredAnnotationMap(Class<?> cls) {
                return cls.getDeclaredAnnotationMap();
            }

            @Override // sun.misc.JavaLangAccess
            public byte[] getRawClassAnnotations(Class<?> cls) {
                return cls.getRawAnnotations();
            }

            @Override // sun.misc.JavaLangAccess
            public byte[] getRawClassTypeAnnotations(Class<?> cls) {
                return cls.getRawTypeAnnotations();
            }

            @Override // sun.misc.JavaLangAccess
            public byte[] getRawExecutableTypeAnnotations(Executable executable) {
                return Class.getExecutableTypeAnnotationBytes(executable);
            }

            @Override // sun.misc.JavaLangAccess
            public <E extends Enum<E>> E[] getEnumConstantsShared(Class<E> cls) {
                return cls.getEnumConstantsShared();
            }

            @Override // sun.misc.JavaLangAccess
            public void blockedOn(Thread thread, Interruptible interruptible) {
                thread.blockedOn(interruptible);
            }

            @Override // sun.misc.JavaLangAccess
            public void registerShutdownHook(int i2, boolean z2, Runnable runnable) {
                Shutdown.add(i2, z2, runnable);
            }

            @Override // sun.misc.JavaLangAccess
            public int getStackTraceDepth(Throwable th) {
                return th.getStackTraceDepth();
            }

            @Override // sun.misc.JavaLangAccess
            public StackTraceElement getStackTraceElement(Throwable th, int i2) {
                return th.getStackTraceElement(i2);
            }

            @Override // sun.misc.JavaLangAccess
            public String newStringUnsafe(char[] cArr) {
                return new String(cArr, true);
            }

            @Override // sun.misc.JavaLangAccess
            public Thread newThreadWithAcc(Runnable runnable, AccessControlContext accessControlContext) {
                return new Thread(runnable, accessControlContext);
            }

            @Override // sun.misc.JavaLangAccess
            public void invokeFinalize(Object obj) throws Throwable {
                obj.finalize();
            }
        });
    }
}
