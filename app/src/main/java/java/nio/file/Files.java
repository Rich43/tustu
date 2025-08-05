package java.nio.file;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UncheckedIOException;
import java.nio.channels.Channels;
import java.nio.channels.SeekableByteChannel;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.DirectoryStream;
import java.nio.file.attribute.BasicFileAttributeView;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileAttribute;
import java.nio.file.attribute.FileAttributeView;
import java.nio.file.attribute.FileOwnerAttributeView;
import java.nio.file.attribute.FileTime;
import java.nio.file.attribute.PosixFileAttributeView;
import java.nio.file.attribute.PosixFileAttributes;
import java.nio.file.attribute.PosixFilePermission;
import java.nio.file.attribute.UserPrincipal;
import java.nio.file.spi.FileSystemProvider;
import java.nio.file.spi.FileTypeDetector;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.ServiceLoader;
import java.util.Set;
import java.util.Spliterators;
import java.util.function.BiPredicate;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;
import sun.nio.fs.DefaultFileTypeDetector;

/* loaded from: rt.jar:java/nio/file/Files.class */
public final class Files {
    private static final int BUFFER_SIZE = 8192;
    private static final int MAX_BUFFER_SIZE = 2147483639;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !Files.class.desiredAssertionStatus();
    }

    private Files() {
    }

    private static FileSystemProvider provider(Path path) {
        return path.getFileSystem().provider();
    }

    private static Runnable asUncheckedRunnable(Closeable closeable) {
        return () -> {
            try {
                closeable.close();
            } catch (IOException e2) {
                throw new UncheckedIOException(e2);
            }
        };
    }

    public static InputStream newInputStream(Path path, OpenOption... openOptionArr) throws IOException {
        return provider(path).newInputStream(path, openOptionArr);
    }

    public static OutputStream newOutputStream(Path path, OpenOption... openOptionArr) throws IOException {
        return provider(path).newOutputStream(path, openOptionArr);
    }

    public static SeekableByteChannel newByteChannel(Path path, Set<? extends OpenOption> set, FileAttribute<?>... fileAttributeArr) throws IOException {
        return provider(path).newByteChannel(path, set, fileAttributeArr);
    }

    public static SeekableByteChannel newByteChannel(Path path, OpenOption... openOptionArr) throws IOException {
        HashSet hashSet = new HashSet(openOptionArr.length);
        Collections.addAll(hashSet, openOptionArr);
        return newByteChannel(path, hashSet, new FileAttribute[0]);
    }

    /* loaded from: rt.jar:java/nio/file/Files$AcceptAllFilter.class */
    private static class AcceptAllFilter implements DirectoryStream.Filter<Path> {
        static final AcceptAllFilter FILTER = new AcceptAllFilter();

        private AcceptAllFilter() {
        }

        @Override // java.nio.file.DirectoryStream.Filter
        public boolean accept(Path path) {
            return true;
        }
    }

    public static DirectoryStream<Path> newDirectoryStream(Path path) throws IOException {
        return provider(path).newDirectoryStream(path, AcceptAllFilter.FILTER);
    }

    public static DirectoryStream<Path> newDirectoryStream(Path path, String str) throws IOException {
        if (str.equals("*")) {
            return newDirectoryStream(path);
        }
        FileSystem fileSystem = path.getFileSystem();
        final PathMatcher pathMatcher = fileSystem.getPathMatcher("glob:" + str);
        return fileSystem.provider().newDirectoryStream(path, new DirectoryStream.Filter<Path>() { // from class: java.nio.file.Files.1
            @Override // java.nio.file.DirectoryStream.Filter
            public boolean accept(Path path2) {
                return pathMatcher.matches(path2.getFileName());
            }
        });
    }

    public static DirectoryStream<Path> newDirectoryStream(Path path, DirectoryStream.Filter<? super Path> filter) throws IOException {
        return provider(path).newDirectoryStream(path, filter);
    }

    public static Path createFile(Path path, FileAttribute<?>... fileAttributeArr) throws IOException {
        newByteChannel(path, EnumSet.of(StandardOpenOption.CREATE_NEW, StandardOpenOption.WRITE), fileAttributeArr).close();
        return path;
    }

    public static Path createDirectory(Path path, FileAttribute<?>... fileAttributeArr) throws IOException {
        provider(path).createDirectory(path, fileAttributeArr);
        return path;
    }

    public static Path createDirectories(Path path, FileAttribute<?>... fileAttributeArr) throws IOException {
        Path path2;
        try {
            createAndCheckIsDirectory(path, fileAttributeArr);
            return path;
        } catch (FileAlreadyExistsException e2) {
            throw e2;
        } catch (IOException e3) {
            SecurityException securityException = null;
            try {
                path = path.toAbsolutePath();
            } catch (SecurityException e4) {
                securityException = e4;
            }
            Path parent = path.getParent();
            while (true) {
                path2 = parent;
                if (path2 == null) {
                    break;
                }
                try {
                    provider(path2).checkAccess(path2, new AccessMode[0]);
                    break;
                } catch (NoSuchFileException e5) {
                    parent = path2.getParent();
                }
            }
            if (path2 == null) {
                if (securityException == null) {
                    throw new FileSystemException(path.toString(), null, "Unable to determine if root directory exists");
                }
                throw securityException;
            }
            Path pathResolve = path2;
            Iterator<Path> it = path2.relativize(path).iterator();
            while (it.hasNext()) {
                pathResolve = pathResolve.resolve(it.next());
                createAndCheckIsDirectory(pathResolve, fileAttributeArr);
            }
            return path;
        }
    }

    private static void createAndCheckIsDirectory(Path path, FileAttribute<?>... fileAttributeArr) throws IOException {
        try {
            createDirectory(path, fileAttributeArr);
        } catch (FileAlreadyExistsException e2) {
            if (!isDirectory(path, LinkOption.NOFOLLOW_LINKS)) {
                throw e2;
            }
        }
    }

    public static Path createTempFile(Path path, String str, String str2, FileAttribute<?>... fileAttributeArr) throws IOException {
        return TempFileHelper.createTempFile((Path) Objects.requireNonNull(path), str, str2, fileAttributeArr);
    }

    public static Path createTempFile(String str, String str2, FileAttribute<?>... fileAttributeArr) throws IOException {
        return TempFileHelper.createTempFile(null, str, str2, fileAttributeArr);
    }

    public static Path createTempDirectory(Path path, String str, FileAttribute<?>... fileAttributeArr) throws IOException {
        return TempFileHelper.createTempDirectory((Path) Objects.requireNonNull(path), str, fileAttributeArr);
    }

    public static Path createTempDirectory(String str, FileAttribute<?>... fileAttributeArr) throws IOException {
        return TempFileHelper.createTempDirectory(null, str, fileAttributeArr);
    }

    public static Path createSymbolicLink(Path path, Path path2, FileAttribute<?>... fileAttributeArr) throws IOException {
        provider(path).createSymbolicLink(path, path2, fileAttributeArr);
        return path;
    }

    public static Path createLink(Path path, Path path2) throws IOException {
        provider(path).createLink(path, path2);
        return path;
    }

    public static void delete(Path path) throws IOException {
        provider(path).delete(path);
    }

    public static boolean deleteIfExists(Path path) throws IOException {
        return provider(path).deleteIfExists(path);
    }

    public static Path copy(Path path, Path path2, CopyOption... copyOptionArr) throws IOException {
        FileSystemProvider fileSystemProviderProvider = provider(path);
        if (provider(path2) == fileSystemProviderProvider) {
            fileSystemProviderProvider.copy(path, path2, copyOptionArr);
        } else {
            CopyMoveHelper.copyToForeignTarget(path, path2, copyOptionArr);
        }
        return path2;
    }

    public static Path move(Path path, Path path2, CopyOption... copyOptionArr) throws IOException {
        FileSystemProvider fileSystemProviderProvider = provider(path);
        if (provider(path2) == fileSystemProviderProvider) {
            fileSystemProviderProvider.move(path, path2, copyOptionArr);
        } else {
            CopyMoveHelper.moveToForeignTarget(path, path2, copyOptionArr);
        }
        return path2;
    }

    public static Path readSymbolicLink(Path path) throws IOException {
        return provider(path).readSymbolicLink(path);
    }

    public static FileStore getFileStore(Path path) throws IOException {
        return provider(path).getFileStore(path);
    }

    public static boolean isSameFile(Path path, Path path2) throws IOException {
        return provider(path).isSameFile(path, path2);
    }

    public static boolean isHidden(Path path) throws IOException {
        return provider(path).isHidden(path);
    }

    /* loaded from: rt.jar:java/nio/file/Files$FileTypeDetectors.class */
    private static class FileTypeDetectors {
        static final FileTypeDetector defaultFileTypeDetector = createDefaultFileTypeDetector();
        static final List<FileTypeDetector> installeDetectors = loadInstalledDetectors();

        private FileTypeDetectors() {
        }

        private static FileTypeDetector createDefaultFileTypeDetector() {
            return (FileTypeDetector) AccessController.doPrivileged(new PrivilegedAction<FileTypeDetector>() { // from class: java.nio.file.Files.FileTypeDetectors.1
                /* JADX WARN: Can't rename method to resolve collision */
                @Override // java.security.PrivilegedAction
                /* renamed from: run */
                public FileTypeDetector run2() {
                    return DefaultFileTypeDetector.create();
                }
            });
        }

        private static List<FileTypeDetector> loadInstalledDetectors() {
            return (List) AccessController.doPrivileged(new PrivilegedAction<List<FileTypeDetector>>() { // from class: java.nio.file.Files.FileTypeDetectors.2
                /* JADX WARN: Can't rename method to resolve collision */
                @Override // java.security.PrivilegedAction
                /* renamed from: run */
                public List<FileTypeDetector> run2() {
                    ArrayList arrayList = new ArrayList();
                    Iterator it = ServiceLoader.load(FileTypeDetector.class, ClassLoader.getSystemClassLoader()).iterator();
                    while (it.hasNext()) {
                        arrayList.add((FileTypeDetector) it.next());
                    }
                    return arrayList;
                }
            });
        }
    }

    public static String probeContentType(Path path) throws IOException {
        Iterator<FileTypeDetector> it = FileTypeDetectors.installeDetectors.iterator();
        while (it.hasNext()) {
            String strProbeContentType = it.next().probeContentType(path);
            if (strProbeContentType != null) {
                return strProbeContentType;
            }
        }
        return FileTypeDetectors.defaultFileTypeDetector.probeContentType(path);
    }

    public static <V extends FileAttributeView> V getFileAttributeView(Path path, Class<V> cls, LinkOption... linkOptionArr) {
        return (V) provider(path).getFileAttributeView(path, cls, linkOptionArr);
    }

    public static <A extends BasicFileAttributes> A readAttributes(Path path, Class<A> cls, LinkOption... linkOptionArr) throws IOException {
        return (A) provider(path).readAttributes(path, cls, linkOptionArr);
    }

    public static Path setAttribute(Path path, String str, Object obj, LinkOption... linkOptionArr) throws IOException {
        provider(path).setAttribute(path, str, obj, linkOptionArr);
        return path;
    }

    public static Object getAttribute(Path path, String str, LinkOption... linkOptionArr) throws IOException {
        String strSubstring;
        if (str.indexOf(42) >= 0 || str.indexOf(44) >= 0) {
            throw new IllegalArgumentException(str);
        }
        Map<String, Object> attributes = readAttributes(path, str, linkOptionArr);
        if (!$assertionsDisabled && attributes.size() != 1) {
            throw new AssertionError();
        }
        int iIndexOf = str.indexOf(58);
        if (iIndexOf == -1) {
            strSubstring = str;
        } else {
            strSubstring = iIndexOf == str.length() ? "" : str.substring(iIndexOf + 1);
        }
        return attributes.get(strSubstring);
    }

    public static Map<String, Object> readAttributes(Path path, String str, LinkOption... linkOptionArr) throws IOException {
        return provider(path).readAttributes(path, str, linkOptionArr);
    }

    public static Set<PosixFilePermission> getPosixFilePermissions(Path path, LinkOption... linkOptionArr) throws IOException {
        return ((PosixFileAttributes) readAttributes(path, PosixFileAttributes.class, linkOptionArr)).permissions();
    }

    public static Path setPosixFilePermissions(Path path, Set<PosixFilePermission> set) throws IOException {
        PosixFileAttributeView posixFileAttributeView = (PosixFileAttributeView) getFileAttributeView(path, PosixFileAttributeView.class, new LinkOption[0]);
        if (posixFileAttributeView == null) {
            throw new UnsupportedOperationException();
        }
        posixFileAttributeView.setPermissions(set);
        return path;
    }

    public static UserPrincipal getOwner(Path path, LinkOption... linkOptionArr) throws IOException {
        FileOwnerAttributeView fileOwnerAttributeView = (FileOwnerAttributeView) getFileAttributeView(path, FileOwnerAttributeView.class, linkOptionArr);
        if (fileOwnerAttributeView == null) {
            throw new UnsupportedOperationException();
        }
        return fileOwnerAttributeView.getOwner();
    }

    public static Path setOwner(Path path, UserPrincipal userPrincipal) throws IOException {
        FileOwnerAttributeView fileOwnerAttributeView = (FileOwnerAttributeView) getFileAttributeView(path, FileOwnerAttributeView.class, new LinkOption[0]);
        if (fileOwnerAttributeView == null) {
            throw new UnsupportedOperationException();
        }
        fileOwnerAttributeView.setOwner(userPrincipal);
        return path;
    }

    public static boolean isSymbolicLink(Path path) {
        try {
            return readAttributes(path, BasicFileAttributes.class, LinkOption.NOFOLLOW_LINKS).isSymbolicLink();
        } catch (IOException e2) {
            return false;
        }
    }

    public static boolean isDirectory(Path path, LinkOption... linkOptionArr) {
        try {
            return readAttributes(path, BasicFileAttributes.class, linkOptionArr).isDirectory();
        } catch (IOException e2) {
            return false;
        }
    }

    public static boolean isRegularFile(Path path, LinkOption... linkOptionArr) {
        try {
            return readAttributes(path, BasicFileAttributes.class, linkOptionArr).isRegularFile();
        } catch (IOException e2) {
            return false;
        }
    }

    public static FileTime getLastModifiedTime(Path path, LinkOption... linkOptionArr) throws IOException {
        return readAttributes(path, BasicFileAttributes.class, linkOptionArr).lastModifiedTime();
    }

    public static Path setLastModifiedTime(Path path, FileTime fileTime) throws IOException {
        ((BasicFileAttributeView) getFileAttributeView(path, BasicFileAttributeView.class, new LinkOption[0])).setTimes(fileTime, null, null);
        return path;
    }

    public static long size(Path path) throws IOException {
        return readAttributes(path, BasicFileAttributes.class, new LinkOption[0]).size();
    }

    private static boolean followLinks(LinkOption... linkOptionArr) {
        boolean z2 = true;
        for (LinkOption linkOption : linkOptionArr) {
            if (linkOption == LinkOption.NOFOLLOW_LINKS) {
                z2 = false;
            } else {
                if (linkOption == null) {
                    throw new NullPointerException();
                }
                throw new AssertionError((Object) "Should not get here");
            }
        }
        return z2;
    }

    public static boolean exists(Path path, LinkOption... linkOptionArr) {
        try {
            if (followLinks(linkOptionArr)) {
                provider(path).checkAccess(path, new AccessMode[0]);
                return true;
            }
            readAttributes(path, BasicFileAttributes.class, LinkOption.NOFOLLOW_LINKS);
            return true;
        } catch (IOException e2) {
            return false;
        }
    }

    public static boolean notExists(Path path, LinkOption... linkOptionArr) {
        try {
            if (followLinks(linkOptionArr)) {
                provider(path).checkAccess(path, new AccessMode[0]);
                return false;
            }
            readAttributes(path, BasicFileAttributes.class, LinkOption.NOFOLLOW_LINKS);
            return false;
        } catch (NoSuchFileException e2) {
            return true;
        } catch (IOException e3) {
            return false;
        }
    }

    private static boolean isAccessible(Path path, AccessMode... accessModeArr) {
        try {
            provider(path).checkAccess(path, accessModeArr);
            return true;
        } catch (IOException e2) {
            return false;
        }
    }

    public static boolean isReadable(Path path) {
        return isAccessible(path, AccessMode.READ);
    }

    public static boolean isWritable(Path path) {
        return isAccessible(path, AccessMode.WRITE);
    }

    public static boolean isExecutable(Path path) {
        return isAccessible(path, AccessMode.EXECUTE);
    }

    /* JADX WARN: Removed duplicated region for block: B:38:0x010f  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static java.nio.file.Path walkFileTree(java.nio.file.Path r5, java.util.Set<java.nio.file.FileVisitOption> r6, int r7, java.nio.file.FileVisitor<? super java.nio.file.Path> r8) throws java.io.IOException {
        /*
            Method dump skipped, instructions count: 355
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: java.nio.file.Files.walkFileTree(java.nio.file.Path, java.util.Set, int, java.nio.file.FileVisitor):java.nio.file.Path");
    }

    public static Path walkFileTree(Path path, FileVisitor<? super Path> fileVisitor) throws IOException {
        return walkFileTree(path, EnumSet.noneOf(FileVisitOption.class), Integer.MAX_VALUE, fileVisitor);
    }

    public static BufferedReader newBufferedReader(Path path, Charset charset) throws IOException {
        return new BufferedReader(new InputStreamReader(newInputStream(path, new OpenOption[0]), charset.newDecoder()));
    }

    public static BufferedReader newBufferedReader(Path path) throws IOException {
        return newBufferedReader(path, StandardCharsets.UTF_8);
    }

    public static BufferedWriter newBufferedWriter(Path path, Charset charset, OpenOption... openOptionArr) throws IOException {
        return new BufferedWriter(new OutputStreamWriter(newOutputStream(path, openOptionArr), charset.newEncoder()));
    }

    public static BufferedWriter newBufferedWriter(Path path, OpenOption... openOptionArr) throws IOException {
        return newBufferedWriter(path, StandardCharsets.UTF_8, openOptionArr);
    }

    private static long copy(InputStream inputStream, OutputStream outputStream) throws IOException {
        long j2 = 0;
        byte[] bArr = new byte[8192];
        while (true) {
            int i2 = inputStream.read(bArr);
            if (i2 > 0) {
                outputStream.write(bArr, 0, i2);
                j2 += i2;
            } else {
                return j2;
            }
        }
    }

    public static long copy(InputStream inputStream, Path path, CopyOption... copyOptionArr) throws IOException {
        Objects.requireNonNull(inputStream);
        boolean z2 = false;
        for (CopyOption copyOption : copyOptionArr) {
            if (copyOption == StandardCopyOption.REPLACE_EXISTING) {
                z2 = true;
            } else {
                if (copyOption == null) {
                    throw new NullPointerException("options contains 'null'");
                }
                throw new UnsupportedOperationException(((Object) copyOption) + " not supported");
            }
        }
        SecurityException securityException = null;
        if (z2) {
            try {
                deleteIfExists(path);
            } catch (SecurityException th) {
            }
        }
        try {
            OutputStream outputStreamNewOutputStream = newOutputStream(path, StandardOpenOption.CREATE_NEW, StandardOpenOption.WRITE);
            Throwable th2 = null;
            try {
                try {
                    long jCopy = copy(inputStream, outputStreamNewOutputStream);
                    if (outputStreamNewOutputStream != null) {
                        if (0 != 0) {
                            try {
                                outputStreamNewOutputStream.close();
                            } catch (Throwable th3) {
                                th2.addSuppressed(th3);
                            }
                        } else {
                            outputStreamNewOutputStream.close();
                        }
                    }
                    return jCopy;
                } finally {
                }
            } catch (Throwable th4) {
                if (outputStreamNewOutputStream != null) {
                    if (th2 != null) {
                        try {
                            outputStreamNewOutputStream.close();
                        } catch (Throwable th5) {
                            th2.addSuppressed(th5);
                        }
                    } else {
                        outputStreamNewOutputStream.close();
                    }
                }
                throw th4;
            }
        } catch (FileAlreadyExistsException e2) {
            if (securityException != null) {
                throw securityException;
            }
            throw e2;
        }
    }

    public static long copy(Path path, OutputStream outputStream) throws IOException {
        Objects.requireNonNull(outputStream);
        InputStream inputStreamNewInputStream = newInputStream(path, new OpenOption[0]);
        Throwable th = null;
        try {
            long jCopy = copy(inputStreamNewInputStream, outputStream);
            if (inputStreamNewInputStream != null) {
                if (0 != 0) {
                    try {
                        inputStreamNewInputStream.close();
                    } catch (Throwable th2) {
                        th.addSuppressed(th2);
                    }
                } else {
                    inputStreamNewInputStream.close();
                }
            }
            return jCopy;
        } catch (Throwable th3) {
            if (inputStreamNewInputStream != null) {
                if (0 != 0) {
                    try {
                        inputStreamNewInputStream.close();
                    } catch (Throwable th4) {
                        th.addSuppressed(th4);
                    }
                } else {
                    inputStreamNewInputStream.close();
                }
            }
            throw th3;
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:21:0x0074, code lost:
    
        if (r8 != r10) goto L23;
     */
    /* JADX WARN: Code restructure failed: missing block: B:24:0x0081, code lost:
    
        return java.util.Arrays.copyOf(r9, r10);
     */
    /* JADX WARN: Code restructure failed: missing block: B:33:?, code lost:
    
        return r9;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private static byte[] read(java.io.InputStream r6, int r7) throws java.io.IOException {
        /*
            r0 = r7
            r8 = r0
            r0 = r8
            byte[] r0 = new byte[r0]
            r9 = r0
            r0 = 0
            r10 = r0
        L9:
            r0 = r6
            r1 = r9
            r2 = r10
            r3 = r8
            r4 = r10
            int r3 = r3 - r4
            int r0 = r0.read(r1, r2, r3)
            r1 = r0
            r11 = r1
            if (r0 <= 0) goto L24
            r0 = r10
            r1 = r11
            int r0 = r0 + r1
            r10 = r0
            goto L9
        L24:
            r0 = r11
            if (r0 < 0) goto L71
            r0 = r6
            int r0 = r0.read()
            r1 = r0
            r11 = r1
            if (r0 >= 0) goto L36
            goto L71
        L36:
            r0 = r8
            r1 = 2147483639(0x7ffffff7, float:NaN)
            r2 = r8
            int r1 = r1 - r2
            if (r0 > r1) goto L4b
            r0 = r8
            r1 = 1
            int r0 = r0 << r1
            r1 = 8192(0x2000, float:1.148E-41)
            int r0 = java.lang.Math.max(r0, r1)
            r8 = r0
            goto L5e
        L4b:
            r0 = r8
            r1 = 2147483639(0x7ffffff7, float:NaN)
            if (r0 != r1) goto L5b
            java.lang.OutOfMemoryError r0 = new java.lang.OutOfMemoryError
            r1 = r0
            java.lang.String r2 = "Required array size too large"
            r1.<init>(r2)
            throw r0
        L5b:
            r0 = 2147483639(0x7ffffff7, float:NaN)
            r8 = r0
        L5e:
            r0 = r9
            r1 = r8
            byte[] r0 = java.util.Arrays.copyOf(r0, r1)
            r9 = r0
            r0 = r9
            r1 = r10
            int r10 = r10 + 1
            r2 = r11
            byte r2 = (byte) r2
            r0[r1] = r2
            goto L9
        L71:
            r0 = r8
            r1 = r10
            if (r0 != r1) goto L7b
            r0 = r9
            goto L81
        L7b:
            r0 = r9
            r1 = r10
            byte[] r0 = java.util.Arrays.copyOf(r0, r1)
        L81:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: java.nio.file.Files.read(java.io.InputStream, int):byte[]");
    }

    /* JADX WARN: Failed to calculate best type for var: r8v0 ??
    java.lang.NullPointerException
     */
    /* JADX WARN: Failed to calculate best type for var: r9v0 ??
    java.lang.NullPointerException
     */
    /* JADX WARN: Multi-variable type inference failed. Error: java.lang.NullPointerException: Cannot invoke "jadx.core.dex.instructions.args.RegisterArg.getSVar()" because the return value of "jadx.core.dex.nodes.InsnNode.getResult()" is null
    	at jadx.core.dex.visitors.typeinference.AbstractTypeConstraint.collectRelatedVars(AbstractTypeConstraint.java:31)
    	at jadx.core.dex.visitors.typeinference.AbstractTypeConstraint.<init>(AbstractTypeConstraint.java:19)
    	at jadx.core.dex.visitors.typeinference.TypeSearch$1.<init>(TypeSearch.java:376)
    	at jadx.core.dex.visitors.typeinference.TypeSearch.makeMoveConstraint(TypeSearch.java:376)
    	at jadx.core.dex.visitors.typeinference.TypeSearch.makeConstraint(TypeSearch.java:361)
    	at jadx.core.dex.visitors.typeinference.TypeSearch.collectConstraints(TypeSearch.java:341)
    	at java.base/java.util.ArrayList.forEach(Unknown Source)
    	at jadx.core.dex.visitors.typeinference.TypeSearch.run(TypeSearch.java:60)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.runMultiVariableSearch(FixTypesVisitor.java:116)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.visit(FixTypesVisitor.java:91)
     */
    /* JADX WARN: Not initialized variable reg: 8, insn: 0x0087: MOVE (r0 I:??[int, float, boolean, short, byte, char, OBJECT, ARRAY]) = (r8 I:??[int, float, boolean, short, byte, char, OBJECT, ARRAY]) A[TRY_LEAVE], block:B:30:0x0087 */
    /* JADX WARN: Not initialized variable reg: 9, insn: 0x008b: MOVE (r0 I:??[int, float, boolean, short, byte, char, OBJECT, ARRAY]) = (r9 I:??[int, float, boolean, short, byte, char, OBJECT, ARRAY]), block:B:32:0x008b */
    /* JADX WARN: Type inference failed for: r8v0, types: [java.io.InputStream] */
    /* JADX WARN: Type inference failed for: r9v0, types: [java.lang.Throwable] */
    public static byte[] readAllBytes(Path path) throws IOException {
        ?? r8;
        ?? r9;
        SeekableByteChannel seekableByteChannelNewByteChannel = newByteChannel(path, new OpenOption[0]);
        Throwable th = null;
        try {
            try {
                InputStream inputStreamNewInputStream = Channels.newInputStream(seekableByteChannelNewByteChannel);
                Throwable th2 = null;
                long size = seekableByteChannelNewByteChannel.size();
                if (size > 2147483639) {
                    throw new OutOfMemoryError("Required array size too large");
                }
                byte[] bArr = read(inputStreamNewInputStream, (int) size);
                if (inputStreamNewInputStream != null) {
                    if (0 != 0) {
                        try {
                            inputStreamNewInputStream.close();
                        } catch (Throwable th3) {
                            th2.addSuppressed(th3);
                        }
                    } else {
                        inputStreamNewInputStream.close();
                    }
                }
                return bArr;
            } catch (Throwable th4) {
                if (r8 != 0) {
                    if (r9 != 0) {
                        try {
                            r8.close();
                        } catch (Throwable th5) {
                            r9.addSuppressed(th5);
                        }
                    } else {
                        r8.close();
                    }
                }
                throw th4;
            }
        } finally {
            if (seekableByteChannelNewByteChannel != null) {
                if (0 != 0) {
                    try {
                        seekableByteChannelNewByteChannel.close();
                    } catch (Throwable th6) {
                        th.addSuppressed(th6);
                    }
                } else {
                    seekableByteChannelNewByteChannel.close();
                }
            }
        }
    }

    public static List<String> readAllLines(Path path, Charset charset) throws IOException {
        BufferedReader bufferedReaderNewBufferedReader = newBufferedReader(path, charset);
        Throwable th = null;
        try {
            try {
                ArrayList arrayList = new ArrayList();
                while (true) {
                    String line = bufferedReaderNewBufferedReader.readLine();
                    if (line == null) {
                        break;
                    }
                    arrayList.add(line);
                }
                if (bufferedReaderNewBufferedReader != null) {
                    if (0 != 0) {
                        try {
                            bufferedReaderNewBufferedReader.close();
                        } catch (Throwable th2) {
                            th.addSuppressed(th2);
                        }
                    } else {
                        bufferedReaderNewBufferedReader.close();
                    }
                }
                return arrayList;
            } finally {
            }
        } catch (Throwable th3) {
            if (bufferedReaderNewBufferedReader != null) {
                if (th != null) {
                    try {
                        bufferedReaderNewBufferedReader.close();
                    } catch (Throwable th4) {
                        th.addSuppressed(th4);
                    }
                } else {
                    bufferedReaderNewBufferedReader.close();
                }
            }
            throw th3;
        }
    }

    public static List<String> readAllLines(Path path) throws IOException {
        return readAllLines(path, StandardCharsets.UTF_8);
    }

    public static Path write(Path path, byte[] bArr, OpenOption... openOptionArr) throws IOException {
        Objects.requireNonNull(bArr);
        OutputStream outputStreamNewOutputStream = newOutputStream(path, openOptionArr);
        Throwable th = null;
        try {
            try {
                int length = bArr.length;
                int i2 = length;
                while (i2 > 0) {
                    int iMin = Math.min(i2, 8192);
                    outputStreamNewOutputStream.write(bArr, length - i2, iMin);
                    i2 -= iMin;
                }
                if (outputStreamNewOutputStream != null) {
                    if (0 != 0) {
                        try {
                            outputStreamNewOutputStream.close();
                        } catch (Throwable th2) {
                            th.addSuppressed(th2);
                        }
                    } else {
                        outputStreamNewOutputStream.close();
                    }
                }
                return path;
            } finally {
            }
        } catch (Throwable th3) {
            if (outputStreamNewOutputStream != null) {
                if (th != null) {
                    try {
                        outputStreamNewOutputStream.close();
                    } catch (Throwable th4) {
                        th.addSuppressed(th4);
                    }
                } else {
                    outputStreamNewOutputStream.close();
                }
            }
            throw th3;
        }
    }

    public static Path write(Path path, Iterable<? extends CharSequence> iterable, Charset charset, OpenOption... openOptionArr) throws IOException {
        Objects.requireNonNull(iterable);
        CharsetEncoder charsetEncoderNewEncoder = charset.newEncoder();
        OutputStream outputStreamNewOutputStream = newOutputStream(path, openOptionArr);
        Throwable th = null;
        try {
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStreamNewOutputStream, charsetEncoderNewEncoder));
            Throwable th2 = null;
            try {
                try {
                    Iterator<? extends CharSequence> it = iterable.iterator();
                    while (it.hasNext()) {
                        bufferedWriter.append(it.next());
                        bufferedWriter.newLine();
                    }
                    if (bufferedWriter != null) {
                        if (0 != 0) {
                            try {
                                bufferedWriter.close();
                            } catch (Throwable th3) {
                                th2.addSuppressed(th3);
                            }
                        } else {
                            bufferedWriter.close();
                        }
                    }
                    return path;
                } catch (Throwable th4) {
                    if (bufferedWriter != null) {
                        if (th2 != null) {
                            try {
                                bufferedWriter.close();
                            } catch (Throwable th5) {
                                th2.addSuppressed(th5);
                            }
                        } else {
                            bufferedWriter.close();
                        }
                    }
                    throw th4;
                }
            } finally {
            }
        } finally {
            if (outputStreamNewOutputStream != null) {
                if (0 != 0) {
                    try {
                        outputStreamNewOutputStream.close();
                    } catch (Throwable th6) {
                        th.addSuppressed(th6);
                    }
                } else {
                    outputStreamNewOutputStream.close();
                }
            }
        }
    }

    public static Path write(Path path, Iterable<? extends CharSequence> iterable, OpenOption... openOptionArr) throws IOException {
        return write(path, iterable, StandardCharsets.UTF_8, openOptionArr);
    }

    public static Stream<Path> list(Path path) throws IOException {
        DirectoryStream<Path> directoryStreamNewDirectoryStream = newDirectoryStream(path);
        try {
            final Iterator<Path> it = directoryStreamNewDirectoryStream.iterator();
            return (Stream) StreamSupport.stream(Spliterators.spliteratorUnknownSize(new Iterator<Path>() { // from class: java.nio.file.Files.2
                @Override // java.util.Iterator
                public boolean hasNext() {
                    try {
                        return it.hasNext();
                    } catch (DirectoryIteratorException e2) {
                        throw new UncheckedIOException(e2.getCause());
                    }
                }

                /* JADX WARN: Can't rename method to resolve collision */
                @Override // java.util.Iterator
                public Path next() {
                    try {
                        return (Path) it.next();
                    } catch (DirectoryIteratorException e2) {
                        throw new UncheckedIOException(e2.getCause());
                    }
                }
            }, 1), false).onClose(asUncheckedRunnable(directoryStreamNewDirectoryStream));
        } catch (Error | RuntimeException e2) {
            try {
                directoryStreamNewDirectoryStream.close();
            } catch (IOException e3) {
                try {
                    e2.addSuppressed(e3);
                } catch (Throwable th) {
                }
            }
            throw e2;
        }
    }

    public static Stream<Path> walk(Path path, int i2, FileVisitOption... fileVisitOptionArr) throws IOException {
        FileTreeIterator fileTreeIterator = new FileTreeIterator(path, i2, fileVisitOptionArr);
        try {
            Stream stream = StreamSupport.stream(Spliterators.spliteratorUnknownSize(fileTreeIterator, 1), false);
            fileTreeIterator.getClass();
            return stream.onClose(fileTreeIterator::close).map(event -> {
                return event.file();
            });
        } catch (Error | RuntimeException e2) {
            fileTreeIterator.close();
            throw e2;
        }
    }

    public static Stream<Path> walk(Path path, FileVisitOption... fileVisitOptionArr) throws IOException {
        return walk(path, Integer.MAX_VALUE, fileVisitOptionArr);
    }

    public static Stream<Path> find(Path path, int i2, BiPredicate<Path, BasicFileAttributes> biPredicate, FileVisitOption... fileVisitOptionArr) throws IOException {
        FileTreeIterator fileTreeIterator = new FileTreeIterator(path, i2, fileVisitOptionArr);
        try {
            Stream stream = StreamSupport.stream(Spliterators.spliteratorUnknownSize(fileTreeIterator, 1), false);
            fileTreeIterator.getClass();
            return stream.onClose(fileTreeIterator::close).filter(event -> {
                return biPredicate.test(event.file(), event.attributes());
            }).map(event2 -> {
                return event2.file();
            });
        } catch (Error | RuntimeException e2) {
            fileTreeIterator.close();
            throw e2;
        }
    }

    public static Stream<String> lines(Path path, Charset charset) throws IOException {
        BufferedReader bufferedReaderNewBufferedReader = newBufferedReader(path, charset);
        try {
            return (Stream) bufferedReaderNewBufferedReader.lines().onClose(asUncheckedRunnable(bufferedReaderNewBufferedReader));
        } catch (Error | RuntimeException e2) {
            try {
                bufferedReaderNewBufferedReader.close();
            } catch (IOException e3) {
                try {
                    e2.addSuppressed(e3);
                } catch (Throwable th) {
                }
            }
            throw e2;
        }
    }

    public static Stream<String> lines(Path path) throws IOException {
        return lines(path, StandardCharsets.UTF_8);
    }
}
