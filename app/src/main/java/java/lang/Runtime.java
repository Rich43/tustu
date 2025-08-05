package java.lang;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.StringTokenizer;
import sun.reflect.CallerSensitive;
import sun.reflect.Reflection;

/* loaded from: rt.jar:java/lang/Runtime.class */
public class Runtime {
    private static Runtime currentRuntime = new Runtime();

    public native int availableProcessors();

    public native long freeMemory();

    public native long totalMemory();

    public native long maxMemory();

    public native void gc();

    private static native void runFinalization0();

    public native void traceInstructions(boolean z2);

    public native void traceMethodCalls(boolean z2);

    public static Runtime getRuntime() {
        return currentRuntime;
    }

    private Runtime() {
    }

    public void exit(int i2) {
        SecurityManager securityManager = System.getSecurityManager();
        if (securityManager != null) {
            securityManager.checkExit(i2);
        }
        Shutdown.exit(i2);
    }

    public void addShutdownHook(Thread thread) {
        SecurityManager securityManager = System.getSecurityManager();
        if (securityManager != null) {
            securityManager.checkPermission(new RuntimePermission("shutdownHooks"));
        }
        ApplicationShutdownHooks.add(thread);
    }

    public boolean removeShutdownHook(Thread thread) {
        SecurityManager securityManager = System.getSecurityManager();
        if (securityManager != null) {
            securityManager.checkPermission(new RuntimePermission("shutdownHooks"));
        }
        return ApplicationShutdownHooks.remove(thread);
    }

    public void halt(int i2) {
        SecurityManager securityManager = System.getSecurityManager();
        if (securityManager != null) {
            securityManager.checkExit(i2);
        }
        Shutdown.beforeHalt();
        Shutdown.halt(i2);
    }

    @Deprecated
    public static void runFinalizersOnExit(boolean z2) {
        throw new UnsupportedOperationException();
    }

    public Process exec(String str) throws IOException {
        return exec(str, (String[]) null, (File) null);
    }

    public Process exec(String str, String[] strArr) throws IOException {
        return exec(str, strArr, (File) null);
    }

    public Process exec(String str, String[] strArr, File file) throws IOException {
        if (str.length() == 0) {
            throw new IllegalArgumentException("Empty command");
        }
        StringTokenizer stringTokenizer = new StringTokenizer(str);
        String[] strArr2 = new String[stringTokenizer.countTokens()];
        int i2 = 0;
        while (stringTokenizer.hasMoreTokens()) {
            strArr2[i2] = stringTokenizer.nextToken();
            i2++;
        }
        return exec(strArr2, strArr, file);
    }

    public Process exec(String[] strArr) throws IOException {
        return exec(strArr, (String[]) null, (File) null);
    }

    public Process exec(String[] strArr, String[] strArr2) throws IOException {
        return exec(strArr, strArr2, (File) null);
    }

    public Process exec(String[] strArr, String[] strArr2, File file) throws IOException {
        return new ProcessBuilder(strArr).environment(strArr2).directory(file).start();
    }

    public void runFinalization() {
        runFinalization0();
    }

    @CallerSensitive
    public void load(String str) {
        load0(Reflection.getCallerClass(), str);
    }

    synchronized void load0(Class<?> cls, String str) {
        SecurityManager securityManager = System.getSecurityManager();
        if (securityManager != null) {
            securityManager.checkLink(str);
        }
        if (!new File(str).isAbsolute()) {
            throw new UnsatisfiedLinkError("Expecting an absolute path of the library: " + str);
        }
        ClassLoader.loadLibrary(cls, str, true);
    }

    @CallerSensitive
    public void loadLibrary(String str) {
        loadLibrary0(Reflection.getCallerClass(), str);
    }

    synchronized void loadLibrary0(Class<?> cls, String str) {
        SecurityManager securityManager = System.getSecurityManager();
        if (securityManager != null) {
            securityManager.checkLink(str);
        }
        if (str.indexOf(File.separatorChar) != -1) {
            throw new UnsatisfiedLinkError("Directory separator should not appear in library name: " + str);
        }
        ClassLoader.loadLibrary(cls, str, false);
    }

    @Deprecated
    public InputStream getLocalizedInputStream(InputStream inputStream) {
        return inputStream;
    }

    @Deprecated
    public OutputStream getLocalizedOutputStream(OutputStream outputStream) {
        return outputStream;
    }
}
