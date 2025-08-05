package jdk.jfr.internal;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.io.Reader;
import java.lang.Thread;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ReflectPermission;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.CopyOption;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.StandardOpenOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileAttribute;
import java.security.AccessControlContext;
import java.security.AccessController;
import java.security.Permission;
import java.security.PrivilegedAction;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.PropertyPermission;
import java.util.concurrent.Callable;
import jdk.jfr.Event;
import jdk.jfr.FlightRecorder;
import jdk.jfr.FlightRecorderListener;
import jdk.jfr.FlightRecorderPermission;
import jdk.jfr.Recording;
import net.lingala.zip4j.util.InternalZipConstants;
import sun.misc.Unsafe;

/* loaded from: jfr.jar:jdk/jfr/internal/SecuritySupport.class */
public final class SecuritySupport {
    private static final Unsafe unsafe = Unsafe.getUnsafe();
    public static final SafePath JFC_DIRECTORY = getPathInProperty("java.home", "lib/jfr");
    static final SafePath USER_HOME = getPathInProperty("user.home", null);
    static final SafePath JAVA_IO_TMPDIR = getPathInProperty("java.io.tmpdir", null);

    /* loaded from: jfr.jar:jdk/jfr/internal/SecuritySupport$CallableWithoutCheckException.class */
    private interface CallableWithoutCheckException<T> {
        T call();
    }

    /* loaded from: jfr.jar:jdk/jfr/internal/SecuritySupport$RunnableWithCheckedException.class */
    private interface RunnableWithCheckedException {
        void run() throws Exception;
    }

    /* loaded from: jfr.jar:jdk/jfr/internal/SecuritySupport$SecureRecorderListener.class */
    static final class SecureRecorderListener implements FlightRecorderListener {
        private final AccessControlContext context;
        private final FlightRecorderListener changeListener;

        SecureRecorderListener(AccessControlContext accessControlContext, FlightRecorderListener flightRecorderListener) {
            this.context = (AccessControlContext) Objects.requireNonNull(accessControlContext);
            this.changeListener = (FlightRecorderListener) Objects.requireNonNull(flightRecorderListener);
        }

        @Override // jdk.jfr.FlightRecorderListener
        public void recordingStateChanged(Recording recording) {
            AccessController.doPrivileged(() -> {
                try {
                    this.changeListener.recordingStateChanged(recording);
                    return null;
                } catch (Throwable th) {
                    Logger.log(LogTag.JFR, LogLevel.WARN, "Unexpected exception in listener " + ((Object) this.changeListener.getClass()) + " at recording state change");
                    return null;
                }
            }, this.context);
        }

        @Override // jdk.jfr.FlightRecorderListener
        public void recorderInitialized(FlightRecorder flightRecorder) {
            AccessController.doPrivileged(() -> {
                try {
                    this.changeListener.recorderInitialized(flightRecorder);
                    return null;
                } catch (Throwable th) {
                    Logger.log(LogTag.JFR, LogLevel.WARN, "Unexpected exception in listener " + ((Object) this.changeListener.getClass()) + " when initializing FlightRecorder");
                    return null;
                }
            }, this.context);
        }

        public FlightRecorderListener getChangeListener() {
            return this.changeListener;
        }
    }

    /* loaded from: jfr.jar:jdk/jfr/internal/SecuritySupport$DirectoryCleaner.class */
    private static final class DirectoryCleaner extends SimpleFileVisitor<Path> {
        private DirectoryCleaner() {
        }

        @Override // java.nio.file.SimpleFileVisitor, java.nio.file.FileVisitor
        public FileVisitResult visitFile(Path path, BasicFileAttributes basicFileAttributes) throws IOException {
            Files.delete(path);
            return FileVisitResult.CONTINUE;
        }

        @Override // java.nio.file.SimpleFileVisitor, java.nio.file.FileVisitor
        public FileVisitResult postVisitDirectory(Path path, IOException iOException) throws IOException {
            if (iOException != null) {
                throw iOException;
            }
            Files.delete(path);
            return FileVisitResult.CONTINUE;
        }
    }

    /* loaded from: jfr.jar:jdk/jfr/internal/SecuritySupport$SafePath.class */
    public static final class SafePath {
        private final Path path;
        private final String text;

        public SafePath(Path path) {
            this.text = path.toString();
            this.path = Paths.get(this.text, new String[0]);
        }

        public SafePath(String str) {
            this(Paths.get(str, new String[0]));
        }

        public Path toPath() {
            return this.path;
        }

        public String toString() {
            return this.text;
        }
    }

    private static <U> U doPrivilegedIOWithReturn(final Callable<U> callable) throws IOException {
        try {
            return (U) AccessController.doPrivileged(new PrivilegedExceptionAction<U>() { // from class: jdk.jfr.internal.SecuritySupport.1
                @Override // java.security.PrivilegedExceptionAction
                public U run() throws Exception {
                    return (U) callable.call();
                }
            }, (AccessControlContext) null);
        } catch (PrivilegedActionException e2) {
            Throwable cause = e2.getCause();
            if (cause instanceof IOException) {
                throw ((IOException) cause);
            }
            throw new IOException("Unexpected error during I/O operation. " + cause.getMessage(), cause);
        }
    }

    private static void doPriviligedIO(RunnableWithCheckedException runnableWithCheckedException) throws IOException {
        doPrivilegedIOWithReturn(() -> {
            runnableWithCheckedException.run();
            return null;
        });
    }

    private static void doPrivileged(final Runnable runnable, Permission... permissionArr) {
        AccessController.doPrivileged(new PrivilegedAction<Void>() { // from class: jdk.jfr.internal.SecuritySupport.2
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.security.PrivilegedAction
            /* renamed from: run */
            public Void run2() {
                runnable.run();
                return null;
            }
        }, (AccessControlContext) null, permissionArr);
    }

    private static void doPrivileged(final Runnable runnable) {
        AccessController.doPrivileged(new PrivilegedAction<Void>() { // from class: jdk.jfr.internal.SecuritySupport.3
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.security.PrivilegedAction
            /* renamed from: run */
            public Void run2() {
                runnable.run();
                return null;
            }
        });
    }

    private static <T> T doPrivilegedWithReturn(final CallableWithoutCheckException<T> callableWithoutCheckException, Permission... permissionArr) {
        return (T) AccessController.doPrivileged(new PrivilegedAction<T>() { // from class: jdk.jfr.internal.SecuritySupport.4
            @Override // java.security.PrivilegedAction
            /* renamed from: run */
            public T run2() {
                return (T) callableWithoutCheckException.call();
            }
        }, (AccessControlContext) null, permissionArr);
    }

    public static List<SafePath> getPredefinedJFCFiles() {
        ArrayList arrayList = new ArrayList();
        try {
            Iterator it = (Iterator) doPrivilegedIOWithReturn(() -> {
                return Files.newDirectoryStream(JFC_DIRECTORY.toPath(), "*").iterator();
            });
            while (it.hasNext()) {
                Path path = (Path) it.next();
                if (path.toString().endsWith(".jfc")) {
                    arrayList.add(new SafePath(path));
                }
            }
        } catch (IOException e2) {
            Logger.log(LogTag.JFR, LogLevel.WARN, "Could not access .jfc-files in " + ((Object) JFC_DIRECTORY) + ", " + e2.getMessage());
        }
        return arrayList;
    }

    static void makeVisibleToJFR(Class<?> cls) {
    }

    static void addHandlerExport(Class<?> cls) {
    }

    public static void registerEvent(Class<? extends Event> cls) {
        doPrivileged(() -> {
            FlightRecorder.register(cls);
        }, new FlightRecorderPermission(Utils.REGISTER_EVENT));
    }

    static boolean getBooleanProperty(String str) {
        return ((Boolean) doPrivilegedWithReturn(() -> {
            return Boolean.valueOf(Boolean.getBoolean(str));
        }, new PropertyPermission(str, "read"))).booleanValue();
    }

    private static SafePath getPathInProperty(String str, String str2) {
        return (SafePath) doPrivilegedWithReturn(() -> {
            String property = System.getProperty(str);
            if (property == null) {
                return null;
            }
            return new SafePath((str2 == null ? new File(property) : new File(property, str2)).getAbsolutePath());
        }, new PropertyPermission("*", "read"));
    }

    static Thread createRecorderThread(ThreadGroup threadGroup, ClassLoader classLoader) {
        Thread thread = (Thread) doPrivilegedWithReturn(() -> {
            return new Thread(threadGroup, "JFR Recorder Thread");
        }, new RuntimePermission("modifyThreadGroup"), new RuntimePermission("modifyThread"));
        doPrivileged(() -> {
            thread.setContextClassLoader(classLoader);
        }, new RuntimePermission("setContextClassLoader"), new RuntimePermission("modifyThread"));
        return thread;
    }

    static void registerShutdownHook(Thread thread) {
        doPrivileged(() -> {
            Runtime.getRuntime().addShutdownHook(thread);
        }, new RuntimePermission("shutdownHooks"));
    }

    static void setUncaughtExceptionHandler(Thread thread, Thread.UncaughtExceptionHandler uncaughtExceptionHandler) {
        doPrivileged(() -> {
            thread.setUncaughtExceptionHandler(uncaughtExceptionHandler);
        }, new RuntimePermission("modifyThread"));
    }

    static void moveReplace(SafePath safePath, SafePath safePath2) throws IOException {
        doPrivilegedIOWithReturn(() -> {
            return Files.move(safePath.toPath(), safePath2.toPath(), new CopyOption[0]);
        });
    }

    static void clearDirectory(SafePath safePath) throws IOException {
        doPriviligedIO(() -> {
            Files.walkFileTree(safePath.toPath(), new DirectoryCleaner());
        });
    }

    static SafePath toRealPath(SafePath safePath) throws Exception {
        return new SafePath((Path) doPrivilegedIOWithReturn(() -> {
            return safePath.toPath().toRealPath(new LinkOption[0]);
        }));
    }

    static boolean existDirectory(SafePath safePath) throws IOException {
        return ((Boolean) doPrivilegedIOWithReturn(() -> {
            return Boolean.valueOf(Files.exists(safePath.toPath(), new LinkOption[0]));
        })).booleanValue();
    }

    static RandomAccessFile createRandomAccessFile(SafePath safePath) throws Exception {
        return (RandomAccessFile) doPrivilegedIOWithReturn(() -> {
            return new RandomAccessFile(safePath.toPath().toFile(), InternalZipConstants.WRITE_MODE);
        });
    }

    public static InputStream newFileInputStream(SafePath safePath) throws IOException {
        return (InputStream) doPrivilegedIOWithReturn(() -> {
            return Files.newInputStream(safePath.toPath(), new OpenOption[0]);
        });
    }

    public static long getFileSize(SafePath safePath) throws IOException {
        return ((Long) doPrivilegedIOWithReturn(() -> {
            return Long.valueOf(Files.size(safePath.toPath()));
        })).longValue();
    }

    static SafePath createDirectories(SafePath safePath) throws IOException {
        return new SafePath((Path) doPrivilegedIOWithReturn(() -> {
            return Files.createDirectories(safePath.toPath(), new FileAttribute[0]);
        }));
    }

    public static boolean exists(SafePath safePath) throws IOException {
        return ((Boolean) doPrivilegedIOWithReturn(() -> {
            return Boolean.valueOf(Files.exists(safePath.toPath(), new LinkOption[0]));
        })).booleanValue();
    }

    public static boolean isDirectory(SafePath safePath) throws IOException {
        return ((Boolean) doPrivilegedIOWithReturn(() -> {
            return Boolean.valueOf(Files.isDirectory(safePath.toPath(), new LinkOption[0]));
        })).booleanValue();
    }

    static void delete(SafePath safePath) throws IOException {
        doPriviligedIO(() -> {
            Files.delete(safePath.toPath());
        });
    }

    static boolean isWritable(SafePath safePath) throws IOException {
        return ((Boolean) doPrivilegedIOWithReturn(() -> {
            return Boolean.valueOf(Files.isWritable(safePath.toPath()));
        })).booleanValue();
    }

    static void deleteOnExit(SafePath safePath) {
        doPrivileged(() -> {
            safePath.toPath().toFile().deleteOnExit();
        });
    }

    static ReadableByteChannel newFileChannelToRead(SafePath safePath) throws IOException {
        return (ReadableByteChannel) doPrivilegedIOWithReturn(() -> {
            return FileChannel.open(safePath.toPath(), StandardOpenOption.READ);
        });
    }

    public static InputStream getResourceAsStream(String str) throws IOException {
        return (InputStream) doPrivilegedIOWithReturn(() -> {
            return SecuritySupport.class.getResourceAsStream(str);
        });
    }

    public static Reader newFileReader(SafePath safePath) throws IOException {
        return (Reader) doPrivilegedIOWithReturn(() -> {
            return Files.newBufferedReader(safePath.toPath());
        });
    }

    static void touch(SafePath safePath) throws IOException {
        doPriviligedIO(() -> {
            new RandomAccessFile(safePath.toPath().toFile(), InternalZipConstants.WRITE_MODE).close();
        });
    }

    static void setAccessible(Method method) {
        doPrivileged(() -> {
            method.setAccessible(true);
        }, new ReflectPermission("suppressAccessChecks"));
    }

    static void setAccessible(Field field) {
        doPrivileged(() -> {
            field.setAccessible(true);
        }, new ReflectPermission("suppressAccessChecks"));
    }

    static void setAccessible(Constructor<?> constructor) {
        doPrivileged(() -> {
            constructor.setAccessible(true);
        }, new ReflectPermission("suppressAccessChecks"));
    }

    static void ensureClassIsInitialized(Class<?> cls) {
        unsafe.ensureClassInitialized(cls);
    }

    static Class<?> defineClass(String str, byte[] bArr, ClassLoader classLoader) {
        return unsafe.defineClass(str, bArr, 0, bArr.length, classLoader, null);
    }

    static Thread createThreadWitNoPermissions(String str, Runnable runnable) {
        return (Thread) doPrivilegedWithReturn(() -> {
            return new Thread(runnable, str);
        }, new Permission[0]);
    }

    static void setDaemonThread(Thread thread, boolean z2) {
        doPrivileged(() -> {
            thread.setDaemon(z2);
        }, new RuntimePermission("modifyThread"));
    }

    public static SafePath getAbsolutePath(SafePath safePath) throws IOException {
        return new SafePath((Path) doPrivilegedIOWithReturn(() -> {
            return safePath.toPath().toAbsolutePath();
        }));
    }
}
