package jdk.nashorn.internal.codegen;

import com.sun.xml.internal.ws.transport.http.DeploymentDescriptorParser;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.net.URL;
import java.nio.file.FileVisitOption;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.security.AccessController;
import java.security.MessageDigest;
import java.security.PrivilegedAction;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.function.Predicate;
import java.util.stream.Stream;
import jdk.nashorn.internal.codegen.types.Type;
import jdk.nashorn.internal.runtime.Context;
import jdk.nashorn.internal.runtime.RecompilableScriptFunctionData;
import jdk.nashorn.internal.runtime.Source;
import jdk.nashorn.internal.runtime.logging.DebugLogger;
import jdk.nashorn.internal.runtime.options.Options;
import sun.security.validator.Validator;

/* loaded from: nashorn.jar:jdk/nashorn/internal/codegen/OptimisticTypesPersistence.class */
public final class OptimisticTypesPersistence {
    private static final int DEFAULT_MAX_FILES = 0;
    private static final int UNLIMITED_FILES = -1;
    private static final int DEFAULT_CLEANUP_DELAY = 20;
    private static final String DEFAULT_CACHE_SUBDIR_NAME = "com.oracle.java.NashornTypeInfo";
    private static final Object[] locks;
    private static final long ERROR_REPORT_THRESHOLD = 60000;
    private static volatile long lastReportedError;
    private static final AtomicBoolean scheduledCleanup;
    private static final Timer cleanupTimer;
    private static final int MAX_FILES = getMaxFiles();
    private static final int CLEANUP_DELAY = Math.max(0, Options.getIntProperty("nashorn.typeInfo.cleanupDelaySeconds", 20));
    private static final File baseCacheDir = createBaseCacheDir();
    private static final File cacheDir = createCacheDir(baseCacheDir);

    static {
        locks = cacheDir == null ? null : createLockArray();
        if (baseCacheDir == null || MAX_FILES == -1) {
            scheduledCleanup = null;
            cleanupTimer = null;
        } else {
            scheduledCleanup = new AtomicBoolean();
            cleanupTimer = new Timer(true);
        }
    }

    public static Object getLocationDescriptor(Source source, int functionId, Type[] paramTypes) {
        if (cacheDir == null) {
            return null;
        }
        StringBuilder b2 = new StringBuilder(48);
        b2.append(source.getDigest()).append('-').append(functionId);
        if (paramTypes != null && paramTypes.length > 0) {
            b2.append('-');
            for (Type t2 : paramTypes) {
                b2.append(Type.getShortSignatureDescriptor(t2));
            }
        }
        return new LocationDescriptor(new File(cacheDir, b2.toString()));
    }

    /* loaded from: nashorn.jar:jdk/nashorn/internal/codegen/OptimisticTypesPersistence$LocationDescriptor.class */
    private static final class LocationDescriptor {
        private final File file;

        LocationDescriptor(File file) {
            this.file = file;
        }
    }

    public static void store(Object locationDescriptor, final Map<Integer, Type> optimisticTypes) {
        if (locationDescriptor != null && !optimisticTypes.isEmpty()) {
            final File file = ((LocationDescriptor) locationDescriptor).file;
            AccessController.doPrivileged(new PrivilegedAction<Void>() { // from class: jdk.nashorn.internal.codegen.OptimisticTypesPersistence.1
                /* JADX WARN: Can't rename method to resolve collision */
                @Override // java.security.PrivilegedAction
                /* renamed from: run */
                public Void run2() {
                    FileOutputStream out;
                    Throwable th;
                    synchronized (OptimisticTypesPersistence.getFileLock(file)) {
                        if (!file.exists()) {
                            OptimisticTypesPersistence.scheduleCleanup();
                        }
                        try {
                            out = new FileOutputStream(file);
                            th = null;
                        } catch (Exception e2) {
                            OptimisticTypesPersistence.reportError("write", file, e2);
                        }
                        try {
                            try {
                                out.getChannel().lock();
                                DataOutputStream dout = new DataOutputStream(new BufferedOutputStream(out));
                                Type.writeTypeMap(optimisticTypes, dout);
                                dout.flush();
                                if (out != null) {
                                    if (0 != 0) {
                                        try {
                                            out.close();
                                        } catch (Throwable th2) {
                                            th.addSuppressed(th2);
                                        }
                                    } else {
                                        out.close();
                                    }
                                }
                            } catch (Throwable th3) {
                                if (out != null) {
                                    if (th != null) {
                                        try {
                                            out.close();
                                        } catch (Throwable th4) {
                                            th.addSuppressed(th4);
                                        }
                                    } else {
                                        out.close();
                                    }
                                }
                                throw th3;
                            }
                        } finally {
                        }
                    }
                    return null;
                }
            });
        }
    }

    public static Map<Integer, Type> load(Object locationDescriptor) {
        if (locationDescriptor != null) {
            final File file = ((LocationDescriptor) locationDescriptor).file;
            return (Map) AccessController.doPrivileged(new PrivilegedAction<Map<Integer, Type>>() { // from class: jdk.nashorn.internal.codegen.OptimisticTypesPersistence.2
                /* JADX WARN: Can't rename method to resolve collision */
                @Override // java.security.PrivilegedAction
                /* renamed from: run */
                public Map<Integer, Type> run2() {
                    Map<Integer, Type> typeMap;
                    try {
                        if (file.isFile()) {
                            synchronized (OptimisticTypesPersistence.getFileLock(file)) {
                                FileInputStream in = new FileInputStream(file);
                                Throwable th = null;
                                try {
                                    try {
                                        in.getChannel().lock(0L, Long.MAX_VALUE, true);
                                        DataInputStream din = new DataInputStream(new BufferedInputStream(in));
                                        typeMap = Type.readTypeMap(din);
                                        if (in != null) {
                                            if (0 != 0) {
                                                try {
                                                    in.close();
                                                } catch (Throwable th2) {
                                                    th.addSuppressed(th2);
                                                }
                                            } else {
                                                in.close();
                                            }
                                        }
                                    } catch (Throwable th3) {
                                        if (in != null) {
                                            if (th != null) {
                                                try {
                                                    in.close();
                                                } catch (Throwable th4) {
                                                    th.addSuppressed(th4);
                                                }
                                            } else {
                                                in.close();
                                            }
                                        }
                                        throw th3;
                                    }
                                } finally {
                                }
                            }
                            return typeMap;
                        }
                        return null;
                    } catch (Exception e2) {
                        OptimisticTypesPersistence.reportError("read", file, e2);
                        return null;
                    }
                }
            });
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void reportError(String msg, File file, Exception e2) {
        long now = System.currentTimeMillis();
        if (now - lastReportedError > 60000) {
            reportError(String.format("Failed to %s %s", msg, file), e2);
            lastReportedError = now;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void reportError(String msg, Exception e2) {
        getLogger().warning(msg, "\n", exceptionToString(e2));
    }

    private static String exceptionToString(Exception e2) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter((Writer) sw, false);
        e2.printStackTrace(pw);
        pw.flush();
        return sw.toString();
    }

    private static File createBaseCacheDir() {
        if (MAX_FILES == 0 || Options.getBooleanProperty("nashorn.typeInfo.disabled")) {
            return null;
        }
        try {
            return createBaseCacheDirPrivileged();
        } catch (Exception e2) {
            reportError("Failed to create cache dir", e2);
            return null;
        }
    }

    private static File createBaseCacheDirPrivileged() {
        return (File) AccessController.doPrivileged(new PrivilegedAction<File>() { // from class: jdk.nashorn.internal.codegen.OptimisticTypesPersistence.3
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.security.PrivilegedAction
            /* renamed from: run */
            public File run2() {
                File dir;
                String explicitDir = System.getProperty("nashorn.typeInfo.cacheDir");
                if (explicitDir == null) {
                    File systemCacheDir = OptimisticTypesPersistence.getSystemCacheDir();
                    dir = new File(systemCacheDir, OptimisticTypesPersistence.DEFAULT_CACHE_SUBDIR_NAME);
                    if (OptimisticTypesPersistence.isSymbolicLink(dir)) {
                        return null;
                    }
                } else {
                    dir = new File(explicitDir);
                }
                return dir;
            }
        });
    }

    private static File createCacheDir(File baseDir) {
        if (baseDir == null) {
            return null;
        }
        try {
            return createCacheDirPrivileged(baseDir);
        } catch (Exception e2) {
            reportError("Failed to create cache dir", e2);
            return null;
        }
    }

    private static File createCacheDirPrivileged(final File baseDir) {
        return (File) AccessController.doPrivileged(new PrivilegedAction<File>() { // from class: jdk.nashorn.internal.codegen.OptimisticTypesPersistence.4
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.security.PrivilegedAction
            /* renamed from: run */
            public File run2() {
                try {
                    String versionDirName = OptimisticTypesPersistence.getVersionDirName();
                    File versionDir = new File(baseDir, versionDirName);
                    if (OptimisticTypesPersistence.isSymbolicLink(versionDir)) {
                        return null;
                    }
                    versionDir.mkdirs();
                    if (versionDir.isDirectory()) {
                        OptimisticTypesPersistence.getLogger().info("Optimistic type persistence directory is " + ((Object) versionDir));
                        return versionDir;
                    }
                    OptimisticTypesPersistence.getLogger().warning("Could not create optimistic type persistence directory " + ((Object) versionDir));
                    return null;
                } catch (Exception e2) {
                    OptimisticTypesPersistence.reportError("Failed to calculate version dir name", e2);
                    return null;
                }
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static File getSystemCacheDir() {
        String os = System.getProperty("os.name", Validator.VAR_GENERIC);
        if ("Mac OS X".equals(os)) {
            return new File(new File(System.getProperty("user.home"), "Library"), "Caches");
        }
        if (os.startsWith("Windows")) {
            return new File(System.getProperty("java.io.tmpdir"));
        }
        return new File(System.getProperty("user.home"), ".cache");
    }

    public static String getVersionDirName() throws Exception {
        URL url = OptimisticTypesPersistence.class.getResource("anchor.properties");
        String protocol = url.getProtocol();
        if (protocol.equals("jar")) {
            String jarUrlFile = url.getFile();
            String filePath = jarUrlFile.substring(0, jarUrlFile.indexOf(33));
            URL file = new URL(filePath);
            InputStream in = file.openStream();
            Throwable th = null;
            try {
                try {
                    byte[] buf = new byte[131072];
                    MessageDigest digest = MessageDigest.getInstance("SHA-1");
                    while (true) {
                        int l2 = in.read(buf);
                        if (l2 == -1) {
                            break;
                        }
                        digest.update(buf, 0, l2);
                    }
                    String strEncodeToString = Base64.getUrlEncoder().withoutPadding().encodeToString(digest.digest());
                    if (in != null) {
                        if (0 != 0) {
                            try {
                                in.close();
                            } catch (Throwable th2) {
                                th.addSuppressed(th2);
                            }
                        } else {
                            in.close();
                        }
                    }
                    return strEncodeToString;
                } finally {
                }
            } catch (Throwable th3) {
                if (in != null) {
                    if (th != null) {
                        try {
                            in.close();
                        } catch (Throwable th4) {
                            th.addSuppressed(th4);
                        }
                    } else {
                        in.close();
                    }
                }
                throw th3;
            }
        }
        if (protocol.equals(DeploymentDescriptorParser.ATTR_FILE)) {
            String fileStr = url.getFile();
            String className = OptimisticTypesPersistence.class.getName();
            int packageNameLen = className.lastIndexOf(46);
            String dirStr = fileStr.substring(0, (fileStr.length() - packageNameLen) - 1);
            File dir = new File(dirStr);
            return "dev-" + new SimpleDateFormat("yyyyMMdd-HHmmss").format(new Date(getLastModifiedClassFile(dir, 0L)));
        }
        throw new AssertionError();
    }

    private static long getLastModifiedClassFile(File dir, long max) {
        long currentMax = max;
        for (File f2 : dir.listFiles()) {
            if (f2.getName().endsWith(".class")) {
                long lastModified = f2.lastModified();
                if (lastModified > currentMax) {
                    currentMax = lastModified;
                }
            } else if (f2.isDirectory()) {
                long lastModified2 = getLastModifiedClassFile(f2, currentMax);
                if (lastModified2 > currentMax) {
                    currentMax = lastModified2;
                }
            }
        }
        return currentMax;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static boolean isSymbolicLink(File file) {
        if (Files.isSymbolicLink(file.toPath())) {
            getLogger().warning("Directory " + ((Object) file) + " is a symlink");
            return true;
        }
        return false;
    }

    private static Object[] createLockArray() {
        Object[] lockArray = new Object[Runtime.getRuntime().availableProcessors() * 2];
        for (int i2 = 0; i2 < lockArray.length; i2++) {
            lockArray[i2] = new Object();
        }
        return lockArray;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static Object getFileLock(File file) {
        return locks[(file.hashCode() & Integer.MAX_VALUE) % locks.length];
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static DebugLogger getLogger() {
        try {
            return Context.getContext().getLogger(RecompilableScriptFunctionData.class);
        } catch (Exception e2) {
            e2.printStackTrace();
            return DebugLogger.DISABLED_LOGGER;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void scheduleCleanup() {
        if (MAX_FILES != -1 && scheduledCleanup.compareAndSet(false, true)) {
            cleanupTimer.schedule(new TimerTask() { // from class: jdk.nashorn.internal.codegen.OptimisticTypesPersistence.5
                @Override // java.util.TimerTask, java.lang.Runnable
                public void run() {
                    OptimisticTypesPersistence.scheduledCleanup.set(false);
                    try {
                        OptimisticTypesPersistence.doCleanup();
                    } catch (IOException e2) {
                    }
                }
            }, TimeUnit.SECONDS.toMillis(CLEANUP_DELAY));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void doCleanup() throws IOException {
        Path[] files = getAllRegularFilesInLastModifiedOrder();
        int nFiles = files.length;
        int filesToDelete = Math.max(0, nFiles - MAX_FILES);
        int filesDeleted = 0;
        for (int i2 = 0; i2 < nFiles && filesDeleted < filesToDelete; i2++) {
            try {
                Files.deleteIfExists(files[i2]);
                filesDeleted++;
            } catch (Exception e2) {
            }
            files[i2] = null;
        }
    }

    private static Path[] getAllRegularFilesInLastModifiedOrder() throws IOException {
        Stream<Path> filesStream = Files.walk(baseCacheDir.toPath(), new FileVisitOption[0]);
        Throwable th = null;
        try {
            Path[] pathArr = (Path[]) filesStream.filter(new Predicate<Path>() { // from class: jdk.nashorn.internal.codegen.OptimisticTypesPersistence.9
                @Override // java.util.function.Predicate
                public boolean test(Path path) {
                    return !Files.isDirectory(path, new LinkOption[0]);
                }
            }).map(new Function<Path, PathAndTime>() { // from class: jdk.nashorn.internal.codegen.OptimisticTypesPersistence.8
                @Override // java.util.function.Function
                public PathAndTime apply(Path path) {
                    return new PathAndTime(path);
                }
            }).sorted().map(new Function<PathAndTime, Path>() { // from class: jdk.nashorn.internal.codegen.OptimisticTypesPersistence.7
                @Override // java.util.function.Function
                public Path apply(PathAndTime pathAndTime) {
                    return pathAndTime.path;
                }
            }).toArray(new IntFunction<Path[]>() { // from class: jdk.nashorn.internal.codegen.OptimisticTypesPersistence.6
                /* JADX WARN: Can't rename method to resolve collision */
                @Override // java.util.function.IntFunction
                public Path[] apply(int length) {
                    return new Path[length];
                }
            });
            if (filesStream != null) {
                if (0 != 0) {
                    try {
                        filesStream.close();
                    } catch (Throwable th2) {
                        th.addSuppressed(th2);
                    }
                } else {
                    filesStream.close();
                }
            }
            return pathArr;
        } catch (Throwable th3) {
            if (filesStream != null) {
                if (0 != 0) {
                    try {
                        filesStream.close();
                    } catch (Throwable th4) {
                        th.addSuppressed(th4);
                    }
                } else {
                    filesStream.close();
                }
            }
            throw th3;
        }
    }

    /* loaded from: nashorn.jar:jdk/nashorn/internal/codegen/OptimisticTypesPersistence$PathAndTime.class */
    private static class PathAndTime implements Comparable<PathAndTime> {
        private final Path path;
        private final long time;

        PathAndTime(Path path) {
            this.path = path;
            this.time = getTime(path);
        }

        @Override // java.lang.Comparable
        public int compareTo(PathAndTime other) {
            return Long.compare(this.time, other.time);
        }

        private static long getTime(Path path) {
            try {
                return Files.getLastModifiedTime(path, new LinkOption[0]).toMillis();
            } catch (IOException e2) {
                return -1L;
            }
        }
    }

    private static int getMaxFiles() {
        String str = Options.getStringProperty("nashorn.typeInfo.maxFiles", null);
        if (str == null) {
            return 0;
        }
        if ("unlimited".equals(str)) {
            return -1;
        }
        return Math.max(0, Integer.parseInt(str));
    }
}
