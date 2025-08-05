package com.sun.nio.zipfs;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.channels.NonWritableChannelException;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.SeekableByteChannel;
import java.nio.channels.WritableByteChannel;
import java.nio.file.AccessMode;
import java.nio.file.ClosedFileSystemException;
import java.nio.file.CopyOption;
import java.nio.file.DirectoryNotEmptyException;
import java.nio.file.DirectoryStream;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.FileStore;
import java.nio.file.FileSystem;
import java.nio.file.FileSystemException;
import java.nio.file.FileSystemNotFoundException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.NoSuchFileException;
import java.nio.file.NotDirectoryException;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.nio.file.ReadOnlyFileSystemException;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
import java.nio.file.WatchService;
import java.nio.file.attribute.FileAttribute;
import java.nio.file.attribute.FileTime;
import java.nio.file.attribute.PosixFileAttributeView;
import java.nio.file.attribute.PosixFileAttributes;
import java.nio.file.attribute.UserPrincipalLookupService;
import java.nio.file.spi.FileSystemProvider;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.regex.Pattern;
import java.util.zip.CRC32;
import java.util.zip.Deflater;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.Inflater;
import java.util.zip.InflaterInputStream;
import java.util.zip.ZipError;
import java.util.zip.ZipException;
import net.lingala.zip4j.util.InternalZipConstants;

/* loaded from: zipfs.jar:com/sun/nio/zipfs/ZipFileSystem.class */
public class ZipFileSystem extends FileSystem {
    private final ZipFileSystemProvider provider;
    private final ZipPath defaultdir;
    private boolean readOnly;
    private final Path zfpath;
    final ZipCoder zc;
    private final String defaultDir;
    private final String nameEncoding;
    private final boolean useTempFile;
    private final boolean createNew;
    private static final String GLOB_SYNTAX = "glob";
    private static final String REGEX_SYNTAX = "regex";
    private final SeekableByteChannel ch;
    final byte[] cen;
    private END end;
    private long locpos;
    private LinkedHashMap<IndexNode, IndexNode> inodes;
    private IndexNode root;
    private static final boolean isWindows = System.getProperty("os.name").startsWith("Windows");
    private static final Set<String> supportedFileAttributeViews = Collections.unmodifiableSet(new HashSet(Arrays.asList("basic", "zip")));
    private static byte[] ROOTPATH = new byte[0];
    private final int tempFileCreationThreshold = 10485760;
    private Set<InputStream> streams = Collections.synchronizedSet(new HashSet());
    private Set<ExChannelCloser> exChClosers = new HashSet();
    private Set<Path> tmppaths = Collections.synchronizedSet(new HashSet());
    private volatile boolean isOpen = true;
    private final ReadWriteLock rwlock = new ReentrantReadWriteLock();
    private boolean hasUpdate = false;
    private final IndexNode LOOKUPKEY = IndexNode.keyOf(null);
    private final int MAX_FLATER = 20;
    private final List<Inflater> inflaters = new ArrayList();
    private final List<Deflater> deflaters = new ArrayList();

    ZipFileSystem(ZipFileSystemProvider zipFileSystemProvider, Path path, Map<String, ?> map) throws IOException {
        this.readOnly = false;
        this.createNew = "true".equals(map.get("create"));
        this.nameEncoding = map.containsKey("encoding") ? (String) map.get("encoding") : "UTF-8";
        this.useTempFile = Boolean.TRUE.equals(map.get("useTempFile"));
        this.defaultDir = map.containsKey("default.dir") ? (String) map.get("default.dir") : "/";
        if (this.defaultDir.charAt(0) != '/') {
            throw new IllegalArgumentException("default dir should be absolute");
        }
        this.provider = zipFileSystemProvider;
        this.zfpath = path;
        if (Files.notExists(path, new LinkOption[0])) {
            if (this.createNew) {
                OutputStream outputStreamNewOutputStream = Files.newOutputStream(path, StandardOpenOption.CREATE_NEW, StandardOpenOption.WRITE);
                Throwable th = null;
                try {
                    try {
                        new END().write(outputStreamNewOutputStream, 0L);
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
            } else {
                throw new FileSystemNotFoundException(path.toString());
            }
        }
        path.getFileSystem().provider().checkAccess(path, AccessMode.READ);
        if (!Files.isWritable(path)) {
            this.readOnly = true;
        }
        this.zc = ZipCoder.get(this.nameEncoding);
        this.defaultdir = new ZipPath(this, getBytes(this.defaultDir));
        this.ch = Files.newByteChannel(path, StandardOpenOption.READ);
        this.cen = initCEN();
    }

    @Override // java.nio.file.FileSystem
    public FileSystemProvider provider() {
        return this.provider;
    }

    @Override // java.nio.file.FileSystem
    public String getSeparator() {
        return "/";
    }

    @Override // java.nio.file.FileSystem
    public boolean isOpen() {
        return this.isOpen;
    }

    @Override // java.nio.file.FileSystem
    public boolean isReadOnly() {
        return this.readOnly;
    }

    private void checkWritable() throws IOException {
        if (this.readOnly) {
            throw new ReadOnlyFileSystemException();
        }
    }

    @Override // java.nio.file.FileSystem
    public Iterable<Path> getRootDirectories() {
        ArrayList arrayList = new ArrayList();
        arrayList.add(new ZipPath(this, new byte[]{47}));
        return arrayList;
    }

    ZipPath getDefaultDir() {
        return this.defaultdir;
    }

    @Override // java.nio.file.FileSystem
    public ZipPath getPath(String str, String... strArr) {
        String string;
        if (strArr.length == 0) {
            string = str;
        } else {
            StringBuilder sb = new StringBuilder();
            sb.append(str);
            for (String str2 : strArr) {
                if (str2.length() > 0) {
                    if (sb.length() > 0) {
                        sb.append('/');
                    }
                    sb.append(str2);
                }
            }
            string = sb.toString();
        }
        return new ZipPath(this, string);
    }

    @Override // java.nio.file.FileSystem
    public UserPrincipalLookupService getUserPrincipalLookupService() {
        throw new UnsupportedOperationException();
    }

    @Override // java.nio.file.FileSystem
    public WatchService newWatchService() {
        throw new UnsupportedOperationException();
    }

    FileStore getFileStore(ZipPath zipPath) {
        return new ZipFileStore(zipPath);
    }

    @Override // java.nio.file.FileSystem
    public Iterable<FileStore> getFileStores() {
        ArrayList arrayList = new ArrayList(1);
        arrayList.add(new ZipFileStore(new ZipPath(this, new byte[]{47})));
        return arrayList;
    }

    @Override // java.nio.file.FileSystem
    public Set<String> supportedFileAttributeViews() {
        return supportedFileAttributeViews;
    }

    public String toString() {
        return this.zfpath.toString();
    }

    Path getZipFile() {
        return this.zfpath;
    }

    @Override // java.nio.file.FileSystem
    public PathMatcher getPathMatcher(String str) {
        String regexPattern;
        int iIndexOf = str.indexOf(58);
        if (iIndexOf <= 0 || iIndexOf == str.length()) {
            throw new IllegalArgumentException();
        }
        String strSubstring = str.substring(0, iIndexOf);
        String strSubstring2 = str.substring(iIndexOf + 1);
        if (strSubstring.equals(GLOB_SYNTAX)) {
            regexPattern = ZipUtils.toRegexPattern(strSubstring2);
        } else if (strSubstring.equals(REGEX_SYNTAX)) {
            regexPattern = strSubstring2;
        } else {
            throw new UnsupportedOperationException("Syntax '" + strSubstring + "' not recognized");
        }
        final Pattern patternCompile = Pattern.compile(regexPattern);
        return new PathMatcher() { // from class: com.sun.nio.zipfs.ZipFileSystem.1
            @Override // java.nio.file.PathMatcher
            public boolean matches(Path path) {
                return patternCompile.matcher(path.toString()).matches();
            }
        };
    }

    @Override // java.nio.file.FileSystem, java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        beginWrite();
        try {
            if (!this.isOpen) {
                return;
            }
            this.isOpen = false;
            if (!this.streams.isEmpty()) {
                Iterator<E> it = new HashSet(this.streams).iterator();
                while (it.hasNext()) {
                    ((InputStream) it.next()).close();
                }
            }
            beginWrite();
            try {
                sync();
                this.ch.close();
                synchronized (this.inflaters) {
                    Iterator<Inflater> it2 = this.inflaters.iterator();
                    while (it2.hasNext()) {
                        it2.next().end();
                    }
                }
                synchronized (this.deflaters) {
                    Iterator<Deflater> it3 = this.deflaters.iterator();
                    while (it3.hasNext()) {
                        it3.next().end();
                    }
                }
                beginWrite();
                try {
                    this.inodes = null;
                    IOException iOException = null;
                    synchronized (this.tmppaths) {
                        Iterator<Path> it4 = this.tmppaths.iterator();
                        while (it4.hasNext()) {
                            try {
                                Files.deleteIfExists(it4.next());
                            } catch (IOException e2) {
                                if (iOException == null) {
                                    iOException = e2;
                                } else {
                                    iOException.addSuppressed(e2);
                                }
                            }
                        }
                    }
                    this.provider.removeFileSystem(this.zfpath, this);
                    if (iOException != null) {
                        throw iOException;
                    }
                } finally {
                }
            } finally {
            }
        } finally {
            endWrite();
        }
    }

    /* JADX WARN: Type inference failed for: r10v2, types: [com.sun.nio.zipfs.ZipFileSystem$Entry, long] */
    ZipFileAttributes getFileAttributes(byte[] bArr) throws IOException {
        beginRead();
        try {
            ensureOpen();
            Entry entry0 = getEntry0(bArr);
            Entry entry = entry0;
            if (entry0 == null) {
                IndexNode inode = getInode(bArr);
                if (inode == null) {
                    return null;
                }
                ?? entry2 = new Entry(inode.name);
                entry2.method = 0;
                entry2.ctime = -1L;
                entry2.atime = -1L;
                (-1).mtime = entry2;
                entry = entry2;
            }
            endRead();
            return new ZipFileAttributes(entry);
        } finally {
            endRead();
        }
    }

    void setTimes(byte[] bArr, FileTime fileTime, FileTime fileTime2, FileTime fileTime3) throws IOException {
        checkWritable();
        beginWrite();
        try {
            ensureOpen();
            Entry entry0 = getEntry0(bArr);
            if (entry0 == null) {
                throw new NoSuchFileException(getString(bArr));
            }
            if (entry0.type == 1) {
                entry0.type = 4;
            }
            if (fileTime != null) {
                entry0.mtime = fileTime.toMillis();
            }
            if (fileTime2 != null) {
                entry0.atime = fileTime2.toMillis();
            }
            if (fileTime3 != null) {
                entry0.ctime = fileTime3.toMillis();
            }
            update(entry0);
            endWrite();
        } catch (Throwable th) {
            endWrite();
            throw th;
        }
    }

    boolean exists(byte[] bArr) throws IOException {
        beginRead();
        try {
            ensureOpen();
            return getInode(bArr) != null;
        } finally {
            endRead();
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:8:0x0019  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    boolean isDirectory(byte[] r4) throws java.io.IOException {
        /*
            r3 = this;
            r0 = r3
            r0.beginRead()
            r0 = r3
            r1 = r4
            com.sun.nio.zipfs.ZipFileSystem$IndexNode r0 = r0.getInode(r1)     // Catch: java.lang.Throwable -> L21
            r5 = r0
            r0 = r5
            if (r0 == 0) goto L19
            r0 = r5
            boolean r0 = r0.isDir()     // Catch: java.lang.Throwable -> L21
            if (r0 == 0) goto L19
            r0 = 1
            goto L1a
        L19:
            r0 = 0
        L1a:
            r6 = r0
            r0 = r3
            r0.endRead()
            r0 = r6
            return r0
        L21:
            r7 = move-exception
            r0 = r3
            r0.endRead()
            r0 = r7
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.sun.nio.zipfs.ZipFileSystem.isDirectory(byte[]):boolean");
    }

    private ZipPath toZipPath(byte[] bArr) {
        byte[] bArr2 = new byte[bArr.length + 1];
        bArr2[0] = 47;
        System.arraycopy(bArr, 0, bArr2, 1, bArr.length);
        return new ZipPath(this, bArr2);
    }

    Iterator<Path> iteratorOf(byte[] bArr, DirectoryStream.Filter<? super Path> filter) throws IOException {
        beginWrite();
        try {
            ensureOpen();
            IndexNode inode = getInode(bArr);
            if (inode == null) {
                throw new NotDirectoryException(getString(bArr));
            }
            ArrayList arrayList = new ArrayList();
            for (IndexNode indexNode = inode.child; indexNode != null; indexNode = indexNode.sibling) {
                ZipPath zipPath = toZipPath(indexNode.name);
                if (filter == null || filter.accept(zipPath)) {
                    arrayList.add(zipPath);
                }
            }
            Iterator it = arrayList.iterator();
            endWrite();
            return it;
        } catch (Throwable th) {
            endWrite();
            throw th;
        }
    }

    void createDirectory(byte[] bArr, FileAttribute<?>... fileAttributeArr) throws IOException {
        checkWritable();
        byte[] directoryPath = ZipUtils.toDirectoryPath(bArr);
        beginWrite();
        try {
            ensureOpen();
            if (directoryPath.length == 0 || exists(directoryPath)) {
                throw new FileAlreadyExistsException(getString(directoryPath));
            }
            checkParents(directoryPath);
            Entry entry = new Entry(directoryPath, 2);
            entry.method = 0;
            update(entry);
            endWrite();
        } catch (Throwable th) {
            endWrite();
            throw th;
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v23, types: [com.sun.nio.zipfs.ZipFileSystem$Entry, long] */
    /* JADX WARN: Type inference failed for: r3v2, types: [com.sun.nio.zipfs.ZipFileSystem$Entry, long] */
    void copyFile(boolean z2, byte[] bArr, byte[] bArr2, CopyOption... copyOptionArr) throws IOException {
        checkWritable();
        if (Arrays.equals(bArr, bArr2)) {
            return;
        }
        beginWrite();
        try {
            ensureOpen();
            Entry entry0 = getEntry0(bArr);
            if (entry0 == null) {
                throw new NoSuchFileException(getString(bArr));
            }
            if (entry0.isDir()) {
                createDirectory(bArr2, new FileAttribute[0]);
                endWrite();
                return;
            }
            boolean z3 = false;
            boolean z4 = false;
            for (CopyOption copyOption : copyOptionArr) {
                if (copyOption == StandardCopyOption.REPLACE_EXISTING) {
                    z3 = true;
                } else if (copyOption == StandardCopyOption.COPY_ATTRIBUTES) {
                    z4 = true;
                }
            }
            if (getEntry0(bArr2) != null) {
                if (!z3) {
                    throw new FileAlreadyExistsException(getString(bArr2));
                }
            } else {
                checkParents(bArr2);
            }
            ?? entry = new Entry(entry0, 4);
            entry.name(bArr2);
            if (entry0.type == 2 || entry0.type == 3) {
                entry.type = entry0.type;
                if (z2) {
                    entry.bytes = entry0.bytes;
                    entry.file = entry0.file;
                } else if (entry0.bytes != null) {
                    entry.bytes = Arrays.copyOf(entry0.bytes, entry0.bytes.length);
                } else if (entry0.file != null) {
                    entry.file = getTempPathForEntry(null);
                    Files.copy(entry0.file, entry.file, StandardCopyOption.REPLACE_EXISTING);
                }
            }
            if (!z4) {
                ?? CurrentTimeMillis = System.currentTimeMillis();
                entry.ctime = CurrentTimeMillis;
                entry.atime = CurrentTimeMillis;
                CurrentTimeMillis.mtime = entry;
            }
            update(entry);
            if (z2) {
                updateDelete(entry0);
            }
        } finally {
            endWrite();
        }
    }

    OutputStream newOutputStream(byte[] bArr, OpenOption... openOptionArr) throws IOException {
        checkWritable();
        boolean z2 = false;
        boolean z3 = false;
        boolean z4 = false;
        boolean z5 = false;
        for (OpenOption openOption : openOptionArr) {
            if (openOption == StandardOpenOption.READ) {
                throw new IllegalArgumentException("READ not allowed");
            }
            if (openOption == StandardOpenOption.CREATE_NEW) {
                z2 = true;
            }
            if (openOption == StandardOpenOption.CREATE) {
                z3 = true;
            }
            if (openOption == StandardOpenOption.APPEND) {
                z4 = true;
            }
            if (openOption == StandardOpenOption.TRUNCATE_EXISTING) {
                z5 = true;
            }
        }
        if (z4 && z5) {
            throw new IllegalArgumentException("APPEND + TRUNCATE_EXISTING not allowed");
        }
        beginRead();
        try {
            ensureOpen();
            Entry entry0 = getEntry0(bArr);
            if (entry0 == null) {
                if (!z3 && !z2) {
                    throw new NoSuchFileException(getString(bArr));
                }
                checkParents(bArr);
                OutputStream outputStream = getOutputStream(new Entry(bArr, 2));
                endRead();
                return outputStream;
            }
            if (entry0.isDir() || z2) {
                throw new FileAlreadyExistsException(getString(bArr));
            }
            if (!z4) {
                OutputStream outputStream2 = getOutputStream(new Entry(entry0, 2));
                endRead();
                return outputStream2;
            }
            InputStream inputStream = getInputStream(entry0);
            OutputStream outputStream3 = getOutputStream(new Entry(entry0, 2));
            copyStream(inputStream, outputStream3);
            inputStream.close();
            endRead();
            return outputStream3;
        } catch (Throwable th) {
            endRead();
            throw th;
        }
    }

    InputStream newInputStream(byte[] bArr) throws IOException {
        beginRead();
        try {
            ensureOpen();
            Entry entry0 = getEntry0(bArr);
            if (entry0 == null) {
                throw new NoSuchFileException(getString(bArr));
            }
            if (entry0.isDir()) {
                throw new FileSystemException(getString(bArr), "is a directory", null);
            }
            InputStream inputStream = getInputStream(entry0);
            endRead();
            return inputStream;
        } catch (Throwable th) {
            endRead();
            throw th;
        }
    }

    private void checkOptions(Set<? extends OpenOption> set) {
        for (OpenOption openOption : set) {
            if (openOption == null) {
                throw new NullPointerException();
            }
            if (!(openOption instanceof StandardOpenOption)) {
                throw new IllegalArgumentException();
            }
        }
        if (set.contains(StandardOpenOption.APPEND) && set.contains(StandardOpenOption.TRUNCATE_EXISTING)) {
            throw new IllegalArgumentException("APPEND + TRUNCATE_EXISTING not allowed");
        }
    }

    SeekableByteChannel newByteChannel(byte[] bArr, Set<? extends OpenOption> set, FileAttribute<?>... fileAttributeArr) throws IOException {
        Entry entry0;
        checkOptions(set);
        if (set.contains(StandardOpenOption.WRITE) || set.contains(StandardOpenOption.APPEND)) {
            checkWritable();
            beginRead();
            try {
                final WritableByteChannel writableByteChannelNewChannel = Channels.newChannel(newOutputStream(bArr, (OpenOption[]) set.toArray(new OpenOption[0])));
                long j2 = 0;
                if (set.contains(StandardOpenOption.APPEND) && (entry0 = getEntry0(bArr)) != null && entry0.size >= 0) {
                    j2 = entry0.size;
                }
                final long j3 = j2;
                SeekableByteChannel seekableByteChannel = new SeekableByteChannel() { // from class: com.sun.nio.zipfs.ZipFileSystem.2
                    long written;

                    {
                        this.written = j3;
                    }

                    @Override // java.nio.channels.Channel
                    public boolean isOpen() {
                        return writableByteChannelNewChannel.isOpen();
                    }

                    @Override // java.nio.channels.SeekableByteChannel
                    public long position() throws IOException {
                        return this.written;
                    }

                    @Override // java.nio.channels.SeekableByteChannel
                    public SeekableByteChannel position(long j4) throws IOException {
                        throw new UnsupportedOperationException();
                    }

                    @Override // java.nio.channels.SeekableByteChannel, java.nio.channels.ReadableByteChannel
                    public int read(ByteBuffer byteBuffer) throws IOException {
                        throw new UnsupportedOperationException();
                    }

                    @Override // java.nio.channels.SeekableByteChannel
                    public SeekableByteChannel truncate(long j4) throws IOException {
                        throw new UnsupportedOperationException();
                    }

                    @Override // java.nio.channels.SeekableByteChannel, java.nio.channels.WritableByteChannel
                    public int write(ByteBuffer byteBuffer) throws IOException {
                        int iWrite = writableByteChannelNewChannel.write(byteBuffer);
                        this.written += iWrite;
                        return iWrite;
                    }

                    @Override // java.nio.channels.SeekableByteChannel
                    public long size() throws IOException {
                        return this.written;
                    }

                    @Override // java.nio.channels.Channel, java.io.Closeable, java.lang.AutoCloseable
                    public void close() throws IOException {
                        writableByteChannelNewChannel.close();
                    }
                };
                endRead();
                return seekableByteChannel;
            } finally {
            }
        }
        beginRead();
        try {
            ensureOpen();
            Entry entry02 = getEntry0(bArr);
            if (entry02 == null || entry02.isDir()) {
                throw new NoSuchFileException(getString(bArr));
            }
            final ReadableByteChannel readableByteChannelNewChannel = Channels.newChannel(getInputStream(entry02));
            final long j4 = entry02.size;
            return new SeekableByteChannel() { // from class: com.sun.nio.zipfs.ZipFileSystem.3
                long read = 0;

                @Override // java.nio.channels.Channel
                public boolean isOpen() {
                    return readableByteChannelNewChannel.isOpen();
                }

                @Override // java.nio.channels.SeekableByteChannel
                public long position() throws IOException {
                    return this.read;
                }

                @Override // java.nio.channels.SeekableByteChannel
                public SeekableByteChannel position(long j5) throws IOException {
                    throw new UnsupportedOperationException();
                }

                @Override // java.nio.channels.SeekableByteChannel, java.nio.channels.ReadableByteChannel
                public int read(ByteBuffer byteBuffer) throws IOException {
                    int i2 = readableByteChannelNewChannel.read(byteBuffer);
                    if (i2 > 0) {
                        this.read += i2;
                    }
                    return i2;
                }

                @Override // java.nio.channels.SeekableByteChannel
                public SeekableByteChannel truncate(long j5) throws IOException {
                    throw new NonWritableChannelException();
                }

                @Override // java.nio.channels.SeekableByteChannel, java.nio.channels.WritableByteChannel
                public int write(ByteBuffer byteBuffer) throws IOException {
                    throw new NonWritableChannelException();
                }

                @Override // java.nio.channels.SeekableByteChannel
                public long size() throws IOException {
                    return j4;
                }

                @Override // java.nio.channels.Channel, java.io.Closeable, java.lang.AutoCloseable
                public void close() throws IOException {
                    readableByteChannelNewChannel.close();
                }
            };
        } finally {
            endRead();
        }
    }

    FileChannel newFileChannel(byte[] bArr, Set<? extends OpenOption> set, FileAttribute<?>... fileAttributeArr) throws IOException {
        checkOptions(set);
        final boolean z2 = set.contains(StandardOpenOption.WRITE) || set.contains(StandardOpenOption.APPEND);
        beginRead();
        try {
            ensureOpen();
            Entry entry0 = getEntry0(bArr);
            if (z2) {
                checkWritable();
                if (entry0 == null) {
                    if (!set.contains(StandardOpenOption.CREATE) && !set.contains(StandardOpenOption.CREATE_NEW)) {
                        throw new NoSuchFileException(getString(bArr));
                    }
                } else {
                    if (set.contains(StandardOpenOption.CREATE_NEW)) {
                        throw new FileAlreadyExistsException(getString(bArr));
                    }
                    if (entry0.isDir()) {
                        throw new FileAlreadyExistsException("directory <" + getString(bArr) + "> exists");
                    }
                }
                set = new HashSet(set);
                set.remove(StandardOpenOption.CREATE_NEW);
            } else if (entry0 == null || entry0.isDir()) {
                throw new NoSuchFileException(getString(bArr));
            }
            final boolean z3 = entry0 != null && entry0.type == 3;
            final Path tempPathForEntry = z3 ? entry0.file : getTempPathForEntry(bArr);
            final FileChannel fileChannelNewFileChannel = tempPathForEntry.getFileSystem().provider().newFileChannel(tempPathForEntry, set, fileAttributeArr);
            final Entry entry = z3 ? entry0 : new Entry(bArr, tempPathForEntry, 3);
            if (z2) {
                entry.flag = 8;
                entry.method = 8;
            }
            FileChannel fileChannel = new FileChannel() { // from class: com.sun.nio.zipfs.ZipFileSystem.4
                @Override // java.nio.channels.FileChannel, java.nio.channels.SeekableByteChannel, java.nio.channels.WritableByteChannel
                public int write(ByteBuffer byteBuffer) throws IOException {
                    return fileChannelNewFileChannel.write(byteBuffer);
                }

                @Override // java.nio.channels.FileChannel, java.nio.channels.GatheringByteChannel
                public long write(ByteBuffer[] byteBufferArr, int i2, int i3) throws IOException {
                    return fileChannelNewFileChannel.write(byteBufferArr, i2, i3);
                }

                @Override // java.nio.channels.FileChannel, java.nio.channels.SeekableByteChannel
                public long position() throws IOException {
                    return fileChannelNewFileChannel.position();
                }

                @Override // java.nio.channels.FileChannel, java.nio.channels.SeekableByteChannel
                public FileChannel position(long j2) throws IOException {
                    fileChannelNewFileChannel.position(j2);
                    return this;
                }

                @Override // java.nio.channels.FileChannel, java.nio.channels.SeekableByteChannel
                public long size() throws IOException {
                    return fileChannelNewFileChannel.size();
                }

                @Override // java.nio.channels.FileChannel, java.nio.channels.SeekableByteChannel
                public FileChannel truncate(long j2) throws IOException {
                    fileChannelNewFileChannel.truncate(j2);
                    return this;
                }

                @Override // java.nio.channels.FileChannel
                public void force(boolean z4) throws IOException {
                    fileChannelNewFileChannel.force(z4);
                }

                @Override // java.nio.channels.FileChannel
                public long transferTo(long j2, long j3, WritableByteChannel writableByteChannel) throws IOException {
                    return fileChannelNewFileChannel.transferTo(j2, j3, writableByteChannel);
                }

                @Override // java.nio.channels.FileChannel
                public long transferFrom(ReadableByteChannel readableByteChannel, long j2, long j3) throws IOException {
                    return fileChannelNewFileChannel.transferFrom(readableByteChannel, j2, j3);
                }

                @Override // java.nio.channels.FileChannel, java.nio.channels.SeekableByteChannel, java.nio.channels.ReadableByteChannel
                public int read(ByteBuffer byteBuffer) throws IOException {
                    return fileChannelNewFileChannel.read(byteBuffer);
                }

                @Override // java.nio.channels.FileChannel
                public int read(ByteBuffer byteBuffer, long j2) throws IOException {
                    return fileChannelNewFileChannel.read(byteBuffer, j2);
                }

                @Override // java.nio.channels.FileChannel, java.nio.channels.ScatteringByteChannel
                public long read(ByteBuffer[] byteBufferArr, int i2, int i3) throws IOException {
                    return fileChannelNewFileChannel.read(byteBufferArr, i2, i3);
                }

                @Override // java.nio.channels.FileChannel
                public int write(ByteBuffer byteBuffer, long j2) throws IOException {
                    return fileChannelNewFileChannel.write(byteBuffer, j2);
                }

                @Override // java.nio.channels.FileChannel
                public MappedByteBuffer map(FileChannel.MapMode mapMode, long j2, long j3) throws IOException {
                    throw new UnsupportedOperationException();
                }

                @Override // java.nio.channels.FileChannel
                public FileLock lock(long j2, long j3, boolean z4) throws IOException {
                    return fileChannelNewFileChannel.lock(j2, j3, z4);
                }

                @Override // java.nio.channels.FileChannel
                public FileLock tryLock(long j2, long j3, boolean z4) throws IOException {
                    return fileChannelNewFileChannel.tryLock(j2, j3, z4);
                }

                @Override // java.nio.channels.spi.AbstractInterruptibleChannel
                protected void implCloseChannel() throws IOException {
                    fileChannelNewFileChannel.close();
                    if (z2) {
                        entry.mtime = System.currentTimeMillis();
                        entry.size = Files.size(entry.file);
                        ZipFileSystem.this.update(entry);
                        return;
                    }
                    if (!z3) {
                        ZipFileSystem.this.removeTempPathForEntry(tempPathForEntry);
                    }
                }
            };
            endRead();
            return fileChannel;
        } catch (Throwable th) {
            endRead();
            throw th;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public Path getTempPathForEntry(byte[] bArr) throws IOException {
        Path pathCreateTempFileInSameDirectoryAs = createTempFileInSameDirectoryAs(this.zfpath);
        if (bArr != null && getEntry0(bArr) != null) {
            InputStream inputStreamNewInputStream = newInputStream(bArr);
            Throwable th = null;
            try {
                Files.copy(inputStreamNewInputStream, pathCreateTempFileInSameDirectoryAs, StandardCopyOption.REPLACE_EXISTING);
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
        return pathCreateTempFileInSameDirectoryAs;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void removeTempPathForEntry(Path path) throws IOException {
        Files.delete(path);
        this.tmppaths.remove(path);
    }

    private void checkParents(byte[] bArr) throws IOException {
        beginRead();
        do {
            try {
                byte[] parent = getParent(bArr);
                bArr = parent;
                if (parent == null || bArr.length == 0) {
                    return;
                }
            } finally {
                endRead();
            }
        } while (this.inodes.containsKey(IndexNode.keyOf(bArr)));
        throw new NoSuchFileException(getString(bArr));
    }

    private static byte[] getParent(byte[] bArr) {
        int length = bArr.length - 1;
        if (length > 0 && bArr[length] == 47) {
            length--;
        }
        while (length > 0 && bArr[length] != 47) {
            length--;
        }
        if (length <= 0) {
            return ROOTPATH;
        }
        return Arrays.copyOf(bArr, length + 1);
    }

    private final void beginWrite() {
        this.rwlock.writeLock().lock();
    }

    private final void endWrite() {
        this.rwlock.writeLock().unlock();
    }

    private final void beginRead() {
        this.rwlock.readLock().lock();
    }

    private final void endRead() {
        this.rwlock.readLock().unlock();
    }

    final byte[] getBytes(String str) {
        return this.zc.getBytes(str);
    }

    final String getString(byte[] bArr) {
        return this.zc.toString(bArr);
    }

    protected void finalize() throws IOException {
        close();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public long getDataPos(Entry entry) throws IOException {
        if (entry.locoff == -1) {
            Entry entry0 = getEntry0(entry.name);
            if (entry0 == null) {
                throw new ZipException("invalid loc for entry <" + ((Object) entry.name) + ">");
            }
            entry.locoff = entry0.locoff;
        }
        if (readFullyAt(new byte[30], 0, r0.length, entry.locoff) != r0.length) {
            throw new ZipException("invalid loc for entry <" + ((Object) entry.name) + ">");
        }
        return this.locpos + entry.locoff + 30 + ZipConstants.LOCNAM(r0) + ZipConstants.LOCEXT(r0);
    }

    final long readFullyAt(byte[] bArr, int i2, long j2, long j3) throws IOException {
        ByteBuffer byteBufferWrap = ByteBuffer.wrap(bArr);
        byteBufferWrap.position(i2);
        byteBufferWrap.limit((int) (i2 + j2));
        return readFullyAt(byteBufferWrap, j3);
    }

    private final long readFullyAt(ByteBuffer byteBuffer, long j2) throws IOException {
        long j3;
        synchronized (this.ch) {
            j3 = this.ch.position(j2).read(byteBuffer);
        }
        return j3;
    }

    private END findEND() throws IOException {
        byte[] bArr = new byte[128];
        long size = this.ch.size();
        long length = (size - 65557 > 0 ? size - 65557 : 0L) - (bArr.length - 22);
        long j2 = size;
        int length2 = bArr.length;
        while (true) {
            long j3 = j2 - length2;
            if (j3 >= length) {
                int i2 = 0;
                if (j3 < 0) {
                    i2 = (int) (-j3);
                    Arrays.fill(bArr, 0, i2, (byte) 0);
                }
                int length3 = bArr.length - i2;
                if (readFullyAt(bArr, i2, length3, j3 + i2) != length3) {
                    zerror("zip END header not found");
                }
                for (int length4 = bArr.length - 22; length4 >= 0; length4--) {
                    if (bArr[length4 + 0] == 80 && bArr[length4 + 1] == 75 && bArr[length4 + 2] == 5 && bArr[length4 + 3] == 6 && j3 + length4 + 22 + ZipConstants.ENDCOM(bArr, length4) == size) {
                        byte[] bArrCopyOfRange = Arrays.copyOfRange(bArr, length4, length4 + 22);
                        END end = new END();
                        end.endsub = ZipConstants.ENDSUB(bArrCopyOfRange);
                        end.centot = ZipConstants.ENDTOT(bArrCopyOfRange);
                        end.cenlen = ZipConstants.ENDSIZ(bArrCopyOfRange);
                        end.cenoff = ZipConstants.ENDOFF(bArrCopyOfRange);
                        end.comlen = ZipConstants.ENDCOM(bArrCopyOfRange);
                        end.endpos = j3 + length4;
                        if (end.cenlen == 4294967295L || end.cenoff == 4294967295L || end.centot == 65535) {
                            byte[] bArr2 = new byte[20];
                            if (readFullyAt(bArr2, 0, bArr2.length, end.endpos - 20) != bArr2.length) {
                                return end;
                            }
                            long jZIP64_LOCOFF = ZipConstants.ZIP64_LOCOFF(bArr2);
                            byte[] bArr3 = new byte[56];
                            if (readFullyAt(bArr3, 0, bArr3.length, jZIP64_LOCOFF) != bArr3.length) {
                                return end;
                            }
                            end.cenlen = ZipConstants.ZIP64_ENDSIZ(bArr3);
                            end.cenoff = ZipConstants.ZIP64_ENDOFF(bArr3);
                            end.centot = (int) ZipConstants.ZIP64_ENDTOT(bArr3);
                            end.endpos = jZIP64_LOCOFF;
                        }
                        return end;
                    }
                }
                j2 = j3;
                length2 = bArr.length - 22;
            } else {
                zerror("zip END header not found");
                return null;
            }
        }
    }

    private byte[] initCEN() throws IOException {
        this.end = findEND();
        if (this.end.endpos == 0) {
            this.inodes = new LinkedHashMap<>(10);
            this.locpos = 0L;
            buildNodeTree();
            return null;
        }
        if (this.end.cenlen > this.end.endpos) {
            zerror("invalid END header (bad central directory size)");
        }
        long j2 = this.end.endpos - this.end.cenlen;
        this.locpos = j2 - this.end.cenoff;
        if (this.locpos < 0) {
            zerror("invalid END header (bad central directory offset)");
        }
        byte[] bArr = new byte[(int) (this.end.cenlen + 22)];
        if (readFullyAt(bArr, 0, bArr.length, j2) != this.end.cenlen + 22) {
            zerror("read CEN tables failed");
        }
        this.inodes = new LinkedHashMap<>(this.end.centot + 1);
        int i2 = 0;
        int length = bArr.length - 22;
        while (i2 < length) {
            if (!ZipConstants.cenSigAt(bArr, i2)) {
                zerror("invalid CEN header (bad signature)");
            }
            int iCENHOW = ZipConstants.CENHOW(bArr, i2);
            int iCENNAM = ZipConstants.CENNAM(bArr, i2);
            int iCENEXT = ZipConstants.CENEXT(bArr, i2);
            int iCENCOM = ZipConstants.CENCOM(bArr, i2);
            if ((ZipConstants.CENFLG(bArr, i2) & 1) != 0) {
                zerror("invalid CEN header (encrypted entry)");
            }
            if (iCENHOW != 0 && iCENHOW != 8) {
                zerror("invalid CEN header (unsupported compression method: " + iCENHOW + ")");
            }
            if (i2 + 46 + iCENNAM > length) {
                zerror("invalid CEN header (bad header size)");
            }
            IndexNode indexNode = new IndexNode(Arrays.copyOfRange(bArr, i2 + 46, i2 + 46 + iCENNAM), i2);
            this.inodes.put(indexNode, indexNode);
            i2 += 46 + iCENNAM + iCENEXT + iCENCOM;
        }
        if (i2 + 22 != bArr.length) {
            zerror("invalid CEN header (bad header size)");
        }
        buildNodeTree();
        return bArr;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void ensureOpen() throws IOException {
        if (!this.isOpen) {
            throw new ClosedFileSystemException();
        }
    }

    private Path createTempFileInSameDirectoryAs(Path path) throws IOException {
        Path parent = path.toAbsolutePath().getParent();
        Path pathCreateTempFile = Files.createTempFile(parent == null ? path.getFileSystem().getPath(".", new String[0]) : parent, "zipfstmp", null, new FileAttribute[0]);
        this.tmppaths.add(pathCreateTempFile);
        return pathCreateTempFile;
    }

    private void updateDelete(IndexNode indexNode) {
        beginWrite();
        try {
            removeFromTree(indexNode);
            this.inodes.remove(indexNode);
            this.hasUpdate = true;
        } finally {
            endWrite();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void update(Entry entry) {
        beginWrite();
        try {
            IndexNode indexNodePut = this.inodes.put(entry, entry);
            if (indexNodePut != null) {
                removeFromTree(indexNodePut);
            }
            if (entry.type == 2 || entry.type == 3 || entry.type == 4) {
                IndexNode indexNode = this.inodes.get(this.LOOKUPKEY.as(getParent(entry.name)));
                entry.sibling = indexNode.child;
                indexNode.child = entry;
            }
            this.hasUpdate = true;
            endWrite();
        } catch (Throwable th) {
            endWrite();
            throw th;
        }
    }

    private long copyLOCEntry(Entry entry, boolean z2, OutputStream outputStream, long j2, byte[] bArr) throws IOException {
        long jLOCNAM;
        long jLOCNAM2;
        long jWriteLOC;
        long j3 = entry.locoff;
        entry.locoff = j2;
        long j4 = 0;
        if ((entry.flag & 8) != 0) {
            if (entry.size >= 4294967295L || entry.csize >= 4294967295L) {
                j4 = 24;
            } else {
                j4 = 16;
            }
        }
        if (readFullyAt(bArr, 0, 30L, j3) != 30) {
            throw new ZipException("loc: reading failed");
        }
        if (z2) {
            jLOCNAM = j3 + 30 + ZipConstants.LOCNAM(bArr) + ZipConstants.LOCEXT(bArr);
            jLOCNAM2 = j4 + entry.csize;
            jWriteLOC = entry.writeLOC(outputStream) + jLOCNAM2;
        } else {
            outputStream.write(bArr, 0, 30);
            jLOCNAM = j3 + 30;
            jLOCNAM2 = j4 + ZipConstants.LOCNAM(bArr) + ZipConstants.LOCEXT(bArr) + entry.csize;
            jWriteLOC = 30 + jLOCNAM2;
        }
        while (jLOCNAM2 > 0) {
            int fullyAt = (int) readFullyAt(bArr, 0, bArr.length, jLOCNAM);
            int i2 = fullyAt;
            if (fullyAt == -1) {
                break;
            }
            if (jLOCNAM2 < i2) {
                i2 = (int) jLOCNAM2;
            }
            outputStream.write(bArr, 0, i2);
            jLOCNAM2 -= i2;
            jLOCNAM += i2;
        }
        return jWriteLOC;
    }

    /* JADX WARN: Finally extract failed */
    private void sync() throws IOException {
        if (!this.exChClosers.isEmpty()) {
            for (ExChannelCloser exChannelCloser : this.exChClosers) {
                if (exChannelCloser.streams.isEmpty()) {
                    exChannelCloser.ch.close();
                    Files.delete(exChannelCloser.path);
                    this.exChClosers.remove(exChannelCloser);
                }
            }
        }
        if (!this.hasUpdate) {
            return;
        }
        PosixFileAttributes posixAttributes = getPosixAttributes(this.zfpath);
        Path pathCreateTempFileInSameDirectoryAs = createTempFileInSameDirectoryAs(this.zfpath);
        BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(Files.newOutputStream(pathCreateTempFileInSameDirectoryAs, StandardOpenOption.WRITE));
        Throwable th = null;
        try {
            ArrayList arrayList = new ArrayList(this.inodes.size());
            long jWriteCEN = 0;
            byte[] bArr = new byte[8192];
            for (IndexNode indexNode : this.inodes.values()) {
                if (indexNode instanceof Entry) {
                    Entry entry = (Entry) indexNode;
                    try {
                        if (entry.type == 4) {
                            jWriteCEN += copyLOCEntry(entry, true, bufferedOutputStream, jWriteCEN, bArr);
                        } else {
                            entry.locoff = jWriteCEN;
                            jWriteCEN += entry.writeLOC(bufferedOutputStream);
                            if (entry.bytes != null) {
                                bufferedOutputStream.write(entry.bytes);
                                jWriteCEN += entry.bytes.length;
                            } else if (entry.file != null) {
                                InputStream inputStreamNewInputStream = Files.newInputStream(entry.file, new OpenOption[0]);
                                Throwable th2 = null;
                                try {
                                    if (entry.type == 2) {
                                        while (true) {
                                            int i2 = inputStreamNewInputStream.read(bArr);
                                            if (i2 == -1) {
                                                break;
                                            }
                                            bufferedOutputStream.write(bArr, 0, i2);
                                            jWriteCEN += i2;
                                        }
                                    } else if (entry.type == 3) {
                                        EntryOutputStream entryOutputStream = new EntryOutputStream(entry, bufferedOutputStream);
                                        Throwable th3 = null;
                                        while (true) {
                                            try {
                                                try {
                                                    int i3 = inputStreamNewInputStream.read(bArr);
                                                    if (i3 == -1) {
                                                        break;
                                                    } else {
                                                        entryOutputStream.write(bArr, 0, i3);
                                                    }
                                                } catch (Throwable th4) {
                                                    if (entryOutputStream != null) {
                                                        if (th3 != null) {
                                                            try {
                                                                entryOutputStream.close();
                                                            } catch (Throwable th5) {
                                                                th3.addSuppressed(th5);
                                                            }
                                                        } else {
                                                            entryOutputStream.close();
                                                        }
                                                    }
                                                    throw th4;
                                                }
                                            } finally {
                                            }
                                        }
                                        if (entryOutputStream != null) {
                                            if (0 != 0) {
                                                try {
                                                    entryOutputStream.close();
                                                } catch (Throwable th6) {
                                                    th3.addSuppressed(th6);
                                                }
                                            } else {
                                                entryOutputStream.close();
                                            }
                                        }
                                        jWriteCEN += entry.csize;
                                        if ((entry.flag & 8) != 0) {
                                            jWriteCEN += entry.writeEXT(bufferedOutputStream);
                                        }
                                    }
                                    if (inputStreamNewInputStream != null) {
                                        if (0 != 0) {
                                            try {
                                                inputStreamNewInputStream.close();
                                            } catch (Throwable th7) {
                                                th2.addSuppressed(th7);
                                            }
                                        } else {
                                            inputStreamNewInputStream.close();
                                        }
                                    }
                                    Files.delete(entry.file);
                                    this.tmppaths.remove(entry.file);
                                } catch (Throwable th8) {
                                    if (inputStreamNewInputStream != null) {
                                        if (0 != 0) {
                                            try {
                                                inputStreamNewInputStream.close();
                                            } catch (Throwable th9) {
                                                th2.addSuppressed(th9);
                                            }
                                        } else {
                                            inputStreamNewInputStream.close();
                                        }
                                    }
                                    throw th8;
                                }
                            }
                        }
                        arrayList.add(entry);
                    } catch (IOException e2) {
                        e2.printStackTrace();
                    }
                } else if (indexNode.pos != -1) {
                    Entry cen = Entry.readCEN(this, indexNode.pos);
                    try {
                        jWriteCEN += copyLOCEntry(cen, false, bufferedOutputStream, jWriteCEN, bArr);
                        arrayList.add(cen);
                    } catch (IOException e3) {
                        e3.printStackTrace();
                    }
                }
            }
            this.end.cenoff = jWriteCEN;
            while (arrayList.iterator().hasNext()) {
                jWriteCEN += ((Entry) r0.next()).writeCEN(bufferedOutputStream);
            }
            this.end.centot = arrayList.size();
            this.end.cenlen = jWriteCEN - this.end.cenoff;
            this.end.write(bufferedOutputStream, jWriteCEN);
            if (bufferedOutputStream != null) {
                if (0 != 0) {
                    try {
                        bufferedOutputStream.close();
                    } catch (Throwable th10) {
                        th.addSuppressed(th10);
                    }
                } else {
                    bufferedOutputStream.close();
                }
            }
            if (!this.streams.isEmpty()) {
                ExChannelCloser exChannelCloser2 = new ExChannelCloser(createTempFileInSameDirectoryAs(this.zfpath), this.ch, this.streams);
                Files.move(this.zfpath, exChannelCloser2.path, StandardCopyOption.REPLACE_EXISTING);
                this.exChClosers.add(exChannelCloser2);
                this.streams = Collections.synchronizedSet(new HashSet());
            } else {
                this.ch.close();
                Files.delete(this.zfpath);
            }
            if (posixAttributes != null) {
                Files.setPosixFilePermissions(pathCreateTempFileInSameDirectoryAs, posixAttributes.permissions());
            }
            Files.move(pathCreateTempFileInSameDirectoryAs, this.zfpath, StandardCopyOption.REPLACE_EXISTING);
            this.hasUpdate = false;
        } catch (Throwable th11) {
            if (bufferedOutputStream != null) {
                if (0 != 0) {
                    try {
                        bufferedOutputStream.close();
                    } catch (Throwable th12) {
                        th.addSuppressed(th12);
                    }
                } else {
                    bufferedOutputStream.close();
                }
            }
            throw th11;
        }
    }

    private PosixFileAttributes getPosixAttributes(Path path) throws IOException {
        try {
            PosixFileAttributeView posixFileAttributeView = (PosixFileAttributeView) Files.getFileAttributeView(path, PosixFileAttributeView.class, new LinkOption[0]);
            if (posixFileAttributeView == null) {
                return null;
            }
            return posixFileAttributeView.readAttributes();
        } catch (UnsupportedOperationException e2) {
            return null;
        }
    }

    private IndexNode getInode(byte[] bArr) {
        if (bArr == null) {
            throw new NullPointerException("path");
        }
        IndexNode indexNodeKeyOf = IndexNode.keyOf(bArr);
        IndexNode indexNode = this.inodes.get(indexNodeKeyOf);
        if (indexNode == null && (bArr.length == 0 || bArr[bArr.length - 1] != 47)) {
            byte[] bArrCopyOf = Arrays.copyOf(bArr, bArr.length + 1);
            bArrCopyOf[bArrCopyOf.length - 1] = 47;
            indexNode = this.inodes.get(indexNodeKeyOf.as(bArrCopyOf));
        }
        return indexNode;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public Entry getEntry0(byte[] bArr) throws IOException {
        IndexNode inode = getInode(bArr);
        if (inode instanceof Entry) {
            return (Entry) inode;
        }
        if (inode == null || inode.pos == -1) {
            return null;
        }
        return Entry.readCEN(this, inode.pos);
    }

    public void deleteFile(byte[] bArr, boolean z2) throws IOException {
        checkWritable();
        IndexNode inode = getInode(bArr);
        if (inode == null) {
            if (bArr != null && bArr.length == 0) {
                throw new ZipException("root directory </> can't not be delete");
            }
            if (z2) {
                throw new NoSuchFileException(getString(bArr));
            }
            return;
        }
        if (inode.isDir() && inode.child != null) {
            throw new DirectoryNotEmptyException(getString(bArr));
        }
        updateDelete(inode);
    }

    private static void copyStream(InputStream inputStream, OutputStream outputStream) throws IOException {
        byte[] bArr = new byte[8192];
        while (true) {
            int i2 = inputStream.read(bArr);
            if (i2 != -1) {
                outputStream.write(bArr, 0, i2);
            } else {
                return;
            }
        }
    }

    private OutputStream getOutputStream(Entry entry) throws IOException {
        OutputStream outputStreamNewOutputStream;
        if (entry.mtime == -1) {
            entry.mtime = System.currentTimeMillis();
        }
        if (entry.method == -1) {
            entry.method = 8;
        }
        entry.flag = 0;
        if (this.zc.isUTF8()) {
            entry.flag |= 2048;
        }
        if (this.useTempFile || entry.size >= 10485760) {
            entry.file = getTempPathForEntry(null);
            outputStreamNewOutputStream = Files.newOutputStream(entry.file, StandardOpenOption.WRITE);
        } else {
            outputStreamNewOutputStream = new FileRolloverOutputStream(entry);
        }
        return new EntryOutputStream(entry, outputStreamNewOutputStream);
    }

    /* loaded from: zipfs.jar:com/sun/nio/zipfs/ZipFileSystem$FileRolloverOutputStream.class */
    private class FileRolloverOutputStream extends OutputStream {
        private ByteArrayOutputStream baos;
        private final Entry entry;
        private OutputStream tmpFileOS;
        private long totalWritten;

        private FileRolloverOutputStream(Entry entry) {
            this.baos = new ByteArrayOutputStream(8192);
            this.totalWritten = 0L;
            this.entry = entry;
        }

        @Override // java.io.OutputStream
        public void write(int i2) throws IOException {
            if (this.tmpFileOS != null) {
                writeToFile(i2);
            } else if (this.totalWritten + 1 < 10485760) {
                this.baos.write(i2);
                this.totalWritten++;
            } else {
                transferToFile();
                writeToFile(i2);
            }
        }

        @Override // java.io.OutputStream
        public void write(byte[] bArr) throws IOException {
            write(bArr, 0, bArr.length);
        }

        @Override // java.io.OutputStream
        public void write(byte[] bArr, int i2, int i3) throws IOException {
            if (this.tmpFileOS != null) {
                writeToFile(bArr, i2, i3);
            } else if (this.totalWritten + i3 < 10485760) {
                this.baos.write(bArr, i2, i3);
                this.totalWritten += i3;
            } else {
                transferToFile();
                writeToFile(bArr, i2, i3);
            }
        }

        @Override // java.io.OutputStream, java.io.Flushable
        public void flush() throws IOException {
            if (this.tmpFileOS != null) {
                this.tmpFileOS.flush();
            }
        }

        @Override // java.io.OutputStream, java.io.Closeable, java.lang.AutoCloseable
        public void close() throws IOException {
            this.baos = null;
            if (this.tmpFileOS != null) {
                this.tmpFileOS.close();
            }
        }

        private void writeToFile(int i2) throws IOException {
            this.tmpFileOS.write(i2);
            this.totalWritten++;
        }

        private void writeToFile(byte[] bArr, int i2, int i3) throws IOException {
            this.tmpFileOS.write(bArr, i2, i3);
            this.totalWritten += i3;
        }

        private void transferToFile() throws IOException {
            this.entry.file = ZipFileSystem.this.getTempPathForEntry(null);
            this.tmpFileOS = new BufferedOutputStream(Files.newOutputStream(this.entry.file, new OpenOption[0]));
            this.baos.writeTo(this.tmpFileOS);
            this.baos = null;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public byte[] toByteArray() {
            if (this.baos == null) {
                return null;
            }
            return this.baos.toByteArray();
        }
    }

    private InputStream getInputStream(Entry entry) throws IOException {
        InputStream entryInputStream;
        if (entry.type == 2) {
            if (entry.bytes != null) {
                entryInputStream = new ByteArrayInputStream(entry.bytes);
            } else if (entry.file != null) {
                entryInputStream = Files.newInputStream(entry.file, new OpenOption[0]);
            } else {
                throw new ZipException("update entry data is missing");
            }
        } else {
            if (entry.type == 3) {
                return Files.newInputStream(entry.file, new OpenOption[0]);
            }
            entryInputStream = new EntryInputStream(entry, this.ch);
        }
        if (entry.method == 8) {
            long j2 = entry.size + 2;
            if (j2 > 65536) {
                j2 = 8192;
            }
            final long j3 = entry.size;
            entryInputStream = new InflaterInputStream(entryInputStream, getInflater(), (int) j2) { // from class: com.sun.nio.zipfs.ZipFileSystem.5
                private boolean isClosed = false;
                private boolean eof;

                @Override // java.util.zip.InflaterInputStream, java.io.FilterInputStream, java.io.InputStream, java.io.Closeable, java.lang.AutoCloseable
                public void close() throws IOException {
                    if (!this.isClosed) {
                        ZipFileSystem.this.releaseInflater(this.inf);
                        this.in.close();
                        this.isClosed = true;
                        ZipFileSystem.this.streams.remove(this);
                    }
                }

                @Override // java.util.zip.InflaterInputStream
                protected void fill() throws IOException {
                    if (this.eof) {
                        throw new EOFException("Unexpected end of ZLIB input stream");
                    }
                    this.len = this.in.read(this.buf, 0, this.buf.length);
                    if (this.len == -1) {
                        this.buf[0] = 0;
                        this.len = 1;
                        this.eof = true;
                    }
                    this.inf.setInput(this.buf, 0, this.len);
                }

                @Override // java.util.zip.InflaterInputStream, java.io.FilterInputStream, java.io.InputStream
                public int available() throws IOException {
                    if (this.isClosed) {
                        return 0;
                    }
                    long bytesWritten = j3 - this.inf.getBytesWritten();
                    if (bytesWritten > 2147483647L) {
                        return Integer.MAX_VALUE;
                    }
                    return (int) bytesWritten;
                }
            };
        } else if (entry.method != 0) {
            throw new ZipException("invalid compression method");
        }
        this.streams.add(entryInputStream);
        return entryInputStream;
    }

    /* loaded from: zipfs.jar:com/sun/nio/zipfs/ZipFileSystem$EntryInputStream.class */
    private class EntryInputStream extends InputStream {
        private final SeekableByteChannel zfch;
        private long pos;
        protected long rem;
        protected final long size;

        EntryInputStream(Entry entry, SeekableByteChannel seekableByteChannel) throws IOException {
            this.zfch = seekableByteChannel;
            this.rem = entry.csize;
            this.size = entry.size;
            this.pos = ZipFileSystem.this.getDataPos(entry);
        }

        @Override // java.io.InputStream
        public int read(byte[] bArr, int i2, int i3) throws IOException {
            long j2;
            ZipFileSystem.this.ensureOpen();
            if (this.rem == 0) {
                return -1;
            }
            if (i3 <= 0) {
                return 0;
            }
            if (i3 > this.rem) {
                i3 = (int) this.rem;
            }
            ByteBuffer byteBufferWrap = ByteBuffer.wrap(bArr);
            byteBufferWrap.position(i2);
            byteBufferWrap.limit(i2 + i3);
            synchronized (this.zfch) {
                j2 = this.zfch.position(this.pos).read(byteBufferWrap);
            }
            if (j2 > 0) {
                this.pos += j2;
                this.rem -= j2;
            }
            if (this.rem == 0) {
                close();
            }
            return (int) j2;
        }

        @Override // java.io.InputStream
        public int read() throws IOException {
            byte[] bArr = new byte[1];
            if (read(bArr, 0, 1) == 1) {
                return bArr[0] & 255;
            }
            return -1;
        }

        @Override // java.io.InputStream
        public long skip(long j2) throws IOException {
            ZipFileSystem.this.ensureOpen();
            if (j2 > this.rem) {
                j2 = this.rem;
            }
            this.pos += j2;
            this.rem -= j2;
            if (this.rem == 0) {
                close();
            }
            return j2;
        }

        @Override // java.io.InputStream
        public int available() {
            if (this.rem > 2147483647L) {
                return Integer.MAX_VALUE;
            }
            return (int) this.rem;
        }

        public long size() {
            return this.size;
        }

        @Override // java.io.InputStream, java.io.Closeable, java.lang.AutoCloseable
        public void close() {
            this.rem = 0L;
            ZipFileSystem.this.streams.remove(this);
        }
    }

    /* loaded from: zipfs.jar:com/sun/nio/zipfs/ZipFileSystem$EntryOutputStream.class */
    class EntryOutputStream extends DeflaterOutputStream {
        private CRC32 crc;

        /* renamed from: e, reason: collision with root package name */
        private Entry f11987e;
        private long written;
        private boolean isClosed;

        EntryOutputStream(Entry entry, OutputStream outputStream) throws IOException {
            super(outputStream, ZipFileSystem.this.getDeflater());
            this.isClosed = false;
            if (entry == null) {
                throw new NullPointerException("Zip entry is null");
            }
            this.f11987e = entry;
            this.crc = new CRC32();
        }

        @Override // java.util.zip.DeflaterOutputStream, java.io.FilterOutputStream, java.io.OutputStream
        public synchronized void write(byte[] bArr, int i2, int i3) throws IOException {
            if (this.f11987e.type != 3) {
                ZipFileSystem.this.ensureOpen();
            }
            if (this.isClosed) {
                throw new IOException("Stream closed");
            }
            if (i2 < 0 || i3 < 0 || i2 > bArr.length - i3) {
                throw new IndexOutOfBoundsException();
            }
            if (i3 == 0) {
                return;
            }
            switch (this.f11987e.method) {
                case 0:
                    this.written += i3;
                    this.out.write(bArr, i2, i3);
                    break;
                case 8:
                    super.write(bArr, i2, i3);
                    break;
                default:
                    throw new ZipException("invalid compression method");
            }
            this.crc.update(bArr, i2, i3);
        }

        @Override // java.util.zip.DeflaterOutputStream, java.io.FilterOutputStream, java.io.OutputStream, java.io.Closeable, java.lang.AutoCloseable
        public synchronized void close() throws IOException {
            if (this.isClosed) {
                return;
            }
            this.isClosed = true;
            switch (this.f11987e.method) {
                case 0:
                    Entry entry = this.f11987e;
                    Entry entry2 = this.f11987e;
                    long j2 = this.written;
                    entry2.csize = j2;
                    entry.size = j2;
                    this.f11987e.crc = this.crc.getValue();
                    break;
                case 8:
                    finish();
                    this.f11987e.size = this.def.getBytesRead();
                    this.f11987e.csize = this.def.getBytesWritten();
                    this.f11987e.crc = this.crc.getValue();
                    break;
                default:
                    throw new ZipException("invalid compression method");
            }
            if (this.out instanceof FileRolloverOutputStream) {
                FileRolloverOutputStream fileRolloverOutputStream = (FileRolloverOutputStream) this.out;
                if (fileRolloverOutputStream.tmpFileOS == null) {
                    this.f11987e.bytes = fileRolloverOutputStream.toByteArray();
                }
            }
            if (this.f11987e.type == 3) {
                ZipFileSystem.this.releaseDeflater(this.def);
                return;
            }
            super.close();
            ZipFileSystem.this.releaseDeflater(this.def);
            ZipFileSystem.this.update(this.f11987e);
        }
    }

    static void zerror(String str) {
        throw new ZipError(str);
    }

    private Inflater getInflater() {
        synchronized (this.inflaters) {
            int size = this.inflaters.size();
            if (size > 0) {
                return this.inflaters.remove(size - 1);
            }
            return new Inflater(true);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void releaseInflater(Inflater inflater) {
        synchronized (this.inflaters) {
            if (this.inflaters.size() < 20) {
                inflater.reset();
                this.inflaters.add(inflater);
            } else {
                inflater.end();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public Deflater getDeflater() {
        synchronized (this.deflaters) {
            int size = this.deflaters.size();
            if (size > 0) {
                return this.deflaters.remove(size - 1);
            }
            return new Deflater(-1, true);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void releaseDeflater(Deflater deflater) {
        synchronized (this.deflaters) {
            if (this.deflaters.size() < 20) {
                deflater.reset();
                this.deflaters.add(deflater);
            } else {
                deflater.end();
            }
        }
    }

    /* loaded from: zipfs.jar:com/sun/nio/zipfs/ZipFileSystem$END.class */
    static class END {
        int disknum;
        int sdisknum;
        int endsub;
        int centot;
        long cenlen;
        long cenoff;
        int comlen;
        byte[] comment;
        int diskNum;
        long endpos;
        int disktot;

        END() {
        }

        void write(OutputStream outputStream, long j2) throws IOException {
            boolean z2 = false;
            long j3 = this.cenlen;
            long j4 = this.cenoff;
            if (j3 >= 4294967295L) {
                j3 = 4294967295L;
                z2 = true;
            }
            if (j4 >= 4294967295L) {
                j4 = 4294967295L;
                z2 = true;
            }
            int i2 = this.centot;
            if (i2 >= 65535) {
                i2 = 65535;
                z2 = true;
            }
            if (z2) {
                ZipUtils.writeInt(outputStream, InternalZipConstants.ZIP64ENDCENDIRREC);
                ZipUtils.writeLong(outputStream, 44L);
                ZipUtils.writeShort(outputStream, 45);
                ZipUtils.writeShort(outputStream, 45);
                ZipUtils.writeInt(outputStream, 0L);
                ZipUtils.writeInt(outputStream, 0L);
                ZipUtils.writeLong(outputStream, this.centot);
                ZipUtils.writeLong(outputStream, this.centot);
                ZipUtils.writeLong(outputStream, this.cenlen);
                ZipUtils.writeLong(outputStream, this.cenoff);
                ZipUtils.writeInt(outputStream, InternalZipConstants.ZIP64ENDCENDIRLOC);
                ZipUtils.writeInt(outputStream, 0L);
                ZipUtils.writeLong(outputStream, j2);
                ZipUtils.writeInt(outputStream, 1L);
            }
            ZipUtils.writeInt(outputStream, ZipConstants.ENDSIG);
            ZipUtils.writeShort(outputStream, 0);
            ZipUtils.writeShort(outputStream, 0);
            ZipUtils.writeShort(outputStream, i2);
            ZipUtils.writeShort(outputStream, i2);
            ZipUtils.writeInt(outputStream, j3);
            ZipUtils.writeInt(outputStream, j4);
            if (this.comment != null) {
                ZipUtils.writeShort(outputStream, this.comment.length);
                ZipUtils.writeBytes(outputStream, this.comment);
            } else {
                ZipUtils.writeShort(outputStream, 0);
            }
        }
    }

    /* loaded from: zipfs.jar:com/sun/nio/zipfs/ZipFileSystem$IndexNode.class */
    static class IndexNode {
        byte[] name;
        int hashcode;
        int pos;
        IndexNode sibling;
        IndexNode child;

        IndexNode(byte[] bArr, int i2) {
            this.pos = -1;
            name(bArr);
            this.pos = i2;
        }

        static final IndexNode keyOf(byte[] bArr) {
            return new IndexNode(bArr, -1);
        }

        final void name(byte[] bArr) {
            this.name = bArr;
            this.hashcode = Arrays.hashCode(bArr);
        }

        final IndexNode as(byte[] bArr) {
            name(bArr);
            return this;
        }

        boolean isDir() {
            return this.name != null && (this.name.length == 0 || this.name[this.name.length - 1] == 47);
        }

        public boolean equals(Object obj) {
            if (!(obj instanceof IndexNode)) {
                return false;
            }
            return Arrays.equals(this.name, ((IndexNode) obj).name);
        }

        public int hashCode() {
            return this.hashcode;
        }

        IndexNode() {
            this.pos = -1;
        }
    }

    /* loaded from: zipfs.jar:com/sun/nio/zipfs/ZipFileSystem$Entry.class */
    static class Entry extends IndexNode {
        static final int CEN = 1;
        static final int NEW = 2;
        static final int FILECH = 3;
        static final int COPY = 4;
        byte[] bytes;
        Path file;
        int type;
        int version;
        int flag;
        int method;
        long mtime;
        long atime;
        long ctime;
        long crc;
        long csize;
        long size;
        byte[] extra;
        int versionMade;
        int disk;
        int attrs;
        long attrsEx;
        long locoff;
        byte[] comment;
        static final /* synthetic */ boolean $assertionsDisabled;

        static {
            $assertionsDisabled = !ZipFileSystem.class.desiredAssertionStatus();
        }

        Entry() {
            this.type = 1;
            this.method = -1;
            this.mtime = -1L;
            this.atime = -1L;
            this.ctime = -1L;
            this.crc = -1L;
            this.csize = -1L;
            this.size = -1L;
        }

        /* JADX WARN: Multi-variable type inference failed */
        /* JADX WARN: Type inference failed for: r3v0, types: [com.sun.nio.zipfs.ZipFileSystem$Entry, long] */
        Entry(byte[] bArr) {
            this.type = 1;
            this.method = -1;
            this.mtime = -1L;
            this.atime = -1L;
            this.ctime = -1L;
            this.crc = -1L;
            this.csize = -1L;
            this.size = -1L;
            name(bArr);
            ?? CurrentTimeMillis = System.currentTimeMillis();
            this.atime = CurrentTimeMillis;
            this.ctime = CurrentTimeMillis;
            CurrentTimeMillis.mtime = this;
            this.crc = 0L;
            this.size = 0L;
            this.csize = 0L;
            this.method = 8;
        }

        Entry(byte[] bArr, int i2) {
            this(bArr);
            this.type = i2;
        }

        Entry(Entry entry, int i2) {
            this.type = 1;
            this.method = -1;
            this.mtime = -1L;
            this.atime = -1L;
            this.ctime = -1L;
            this.crc = -1L;
            this.csize = -1L;
            this.size = -1L;
            name(entry.name);
            this.version = entry.version;
            this.ctime = entry.ctime;
            this.atime = entry.atime;
            this.mtime = entry.mtime;
            this.crc = entry.crc;
            this.size = entry.size;
            this.csize = entry.csize;
            this.method = entry.method;
            this.extra = entry.extra;
            this.versionMade = entry.versionMade;
            this.disk = entry.disk;
            this.attrs = entry.attrs;
            this.attrsEx = entry.attrsEx;
            this.locoff = entry.locoff;
            this.comment = entry.comment;
            this.type = i2;
        }

        Entry(byte[] bArr, Path path, int i2) {
            this(bArr, i2);
            this.file = path;
            this.method = 0;
        }

        int version() throws ZipException {
            if (this.method == 8) {
                return 20;
            }
            if (this.method == 0) {
                return 10;
            }
            throw new ZipException("unsupported compression method");
        }

        static Entry readCEN(ZipFileSystem zipFileSystem, int i2) throws IOException {
            return new Entry().cen(zipFileSystem, i2);
        }

        private Entry cen(ZipFileSystem zipFileSystem, int i2) throws IOException {
            byte[] bArr = zipFileSystem.cen;
            if (!ZipConstants.cenSigAt(bArr, i2)) {
                ZipFileSystem.zerror("invalid CEN header (bad signature)");
            }
            this.versionMade = ZipConstants.CENVEM(bArr, i2);
            this.version = ZipConstants.CENVER(bArr, i2);
            this.flag = ZipConstants.CENFLG(bArr, i2);
            this.method = ZipConstants.CENHOW(bArr, i2);
            this.mtime = ZipUtils.dosToJavaTime(ZipConstants.CENTIM(bArr, i2));
            this.crc = ZipConstants.CENCRC(bArr, i2);
            this.csize = ZipConstants.CENSIZ(bArr, i2);
            this.size = ZipConstants.CENLEN(bArr, i2);
            int iCENNAM = ZipConstants.CENNAM(bArr, i2);
            int iCENEXT = ZipConstants.CENEXT(bArr, i2);
            int iCENCOM = ZipConstants.CENCOM(bArr, i2);
            this.disk = ZipConstants.CENDSK(bArr, i2);
            this.attrs = ZipConstants.CENATT(bArr, i2);
            this.attrsEx = ZipConstants.CENATX(bArr, i2);
            this.locoff = ZipConstants.CENOFF(bArr, i2);
            int i3 = i2 + 46;
            name(Arrays.copyOfRange(bArr, i3, i3 + iCENNAM));
            int i4 = i3 + iCENNAM;
            if (iCENEXT > 0) {
                this.extra = Arrays.copyOfRange(bArr, i4, i4 + iCENEXT);
                i4 += iCENEXT;
                readExtra(zipFileSystem);
            }
            if (iCENCOM > 0) {
                this.comment = Arrays.copyOfRange(bArr, i4, i4 + iCENCOM);
            }
            return this;
        }

        int writeCEN(OutputStream outputStream) throws IOException {
            int iVersion = version();
            long j2 = this.csize;
            long j3 = this.size;
            long j4 = this.locoff;
            int i2 = 0;
            int i3 = 0;
            int i4 = 0;
            boolean z2 = false;
            int length = this.name != null ? this.name.length : 0;
            int length2 = this.extra != null ? this.extra.length : 0;
            int i5 = 0;
            int length3 = this.comment != null ? this.comment.length : 0;
            if (this.csize >= 4294967295L) {
                j2 = 4294967295L;
                i2 = 0 + 8;
            }
            if (this.size >= 4294967295L) {
                j3 = 4294967295L;
                i2 += 8;
            }
            if (this.locoff >= 4294967295L) {
                j4 = 4294967295L;
                i2 += 8;
            }
            if (i2 != 0) {
                i2 += 4;
            }
            while (i5 + 4 < length2) {
                int iSH = ZipConstants.SH(this.extra, i5);
                int iSH2 = ZipConstants.SH(this.extra, i5 + 2);
                if (iSH == 21589 || iSH == 10) {
                    z2 = true;
                }
                i5 += 4 + iSH2;
            }
            if (!z2) {
                if (ZipFileSystem.isWindows) {
                    i3 = 36;
                } else {
                    i4 = 9;
                }
            }
            ZipUtils.writeInt(outputStream, ZipConstants.CENSIG);
            if (i2 != 0) {
                ZipUtils.writeShort(outputStream, 45);
                ZipUtils.writeShort(outputStream, 45);
            } else {
                ZipUtils.writeShort(outputStream, iVersion);
                ZipUtils.writeShort(outputStream, iVersion);
            }
            ZipUtils.writeShort(outputStream, this.flag);
            ZipUtils.writeShort(outputStream, this.method);
            ZipUtils.writeInt(outputStream, (int) ZipUtils.javaToDosTime(this.mtime));
            ZipUtils.writeInt(outputStream, this.crc);
            ZipUtils.writeInt(outputStream, j2);
            ZipUtils.writeInt(outputStream, j3);
            ZipUtils.writeShort(outputStream, this.name.length);
            ZipUtils.writeShort(outputStream, length2 + i2 + i3 + i4);
            if (this.comment != null) {
                ZipUtils.writeShort(outputStream, Math.min(length3, 65535));
            } else {
                ZipUtils.writeShort(outputStream, 0);
            }
            ZipUtils.writeShort(outputStream, 0);
            ZipUtils.writeShort(outputStream, 0);
            ZipUtils.writeInt(outputStream, 0L);
            ZipUtils.writeInt(outputStream, j4);
            ZipUtils.writeBytes(outputStream, this.name);
            if (i2 != 0) {
                ZipUtils.writeShort(outputStream, 1);
                ZipUtils.writeShort(outputStream, i2 - 4);
                if (j3 == 4294967295L) {
                    ZipUtils.writeLong(outputStream, this.size);
                }
                if (j2 == 4294967295L) {
                    ZipUtils.writeLong(outputStream, this.csize);
                }
                if (j4 == 4294967295L) {
                    ZipUtils.writeLong(outputStream, this.locoff);
                }
            }
            if (i3 != 0) {
                ZipUtils.writeShort(outputStream, 10);
                ZipUtils.writeShort(outputStream, i3 - 4);
                ZipUtils.writeInt(outputStream, 0L);
                ZipUtils.writeShort(outputStream, 1);
                ZipUtils.writeShort(outputStream, 24);
                ZipUtils.writeLong(outputStream, ZipUtils.javaToWinTime(this.mtime));
                ZipUtils.writeLong(outputStream, ZipUtils.javaToWinTime(this.atime));
                ZipUtils.writeLong(outputStream, ZipUtils.javaToWinTime(this.ctime));
            }
            if (i4 != 0) {
                ZipUtils.writeShort(outputStream, 21589);
                ZipUtils.writeShort(outputStream, i4 - 4);
                if (this.ctime == -1) {
                    outputStream.write(3);
                } else {
                    outputStream.write(7);
                }
                ZipUtils.writeInt(outputStream, ZipUtils.javaToUnixTime(this.mtime));
            }
            if (this.extra != null) {
                ZipUtils.writeBytes(outputStream, this.extra);
            }
            if (this.comment != null) {
                ZipUtils.writeBytes(outputStream, this.comment);
            }
            return 46 + length + length2 + length3 + i2 + i3 + i4;
        }

        static Entry readLOC(ZipFileSystem zipFileSystem, long j2) throws IOException {
            return readLOC(zipFileSystem, j2, new byte[1024]);
        }

        static Entry readLOC(ZipFileSystem zipFileSystem, long j2, byte[] bArr) throws IOException {
            return new Entry().loc(zipFileSystem, j2, bArr);
        }

        Entry loc(ZipFileSystem zipFileSystem, long j2, byte[] bArr) throws IOException {
            if (!$assertionsDisabled && bArr.length < 30) {
                throw new AssertionError();
            }
            if (zipFileSystem.readFullyAt(bArr, 0, 30L, j2) != 30) {
                throw new ZipException("loc: reading failed");
            }
            if (!ZipConstants.locSigAt(bArr, 0)) {
                throw new ZipException("loc: wrong sig ->" + Long.toString(ZipConstants.getSig(bArr, 0), 16));
            }
            this.version = ZipConstants.LOCVER(bArr);
            this.flag = ZipConstants.LOCFLG(bArr);
            this.method = ZipConstants.LOCHOW(bArr);
            this.mtime = ZipUtils.dosToJavaTime(ZipConstants.LOCTIM(bArr));
            this.crc = ZipConstants.LOCCRC(bArr);
            this.csize = ZipConstants.LOCSIZ(bArr);
            this.size = ZipConstants.LOCLEN(bArr);
            int iLOCNAM = ZipConstants.LOCNAM(bArr);
            int iLOCEXT = ZipConstants.LOCEXT(bArr);
            this.name = new byte[iLOCNAM];
            if (zipFileSystem.readFullyAt(this.name, 0, iLOCNAM, j2 + 30) != iLOCNAM) {
                throw new ZipException("loc: name reading failed");
            }
            if (iLOCEXT > 0) {
                this.extra = new byte[iLOCEXT];
                if (zipFileSystem.readFullyAt(this.extra, 0, iLOCEXT, j2 + 30 + iLOCNAM) != iLOCEXT) {
                    throw new ZipException("loc: ext reading failed");
                }
            }
            long j3 = j2 + 30 + iLOCNAM + iLOCEXT;
            if ((this.flag & 8) != 0) {
                Entry entry0 = zipFileSystem.getEntry0(this.name);
                if (entry0 == null) {
                    throw new ZipException("loc: name not found in cen");
                }
                this.size = entry0.size;
                this.csize = entry0.csize;
                long j4 = j3 + (this.method == 0 ? this.size : this.csize);
                if (this.size >= 4294967295L || this.csize >= 4294967295L) {
                    long j5 = j4 + 24;
                } else {
                    long j6 = j4 + 16;
                }
            } else {
                if (this.extra != null && (this.size == 4294967295L || this.csize == 4294967295L)) {
                    int i2 = 0;
                    while (true) {
                        int i3 = i2;
                        if (i3 + 20 >= iLOCEXT) {
                            break;
                        }
                        int iSH = ZipConstants.SH(this.extra, i3 + 2);
                        if (ZipConstants.SH(this.extra, i3) == 1 && iSH == 16) {
                            this.size = ZipConstants.LL(this.extra, i3 + 4);
                            this.csize = ZipConstants.LL(this.extra, i3 + 12);
                            break;
                        }
                        i2 = i3 + iSH + 4;
                    }
                }
                long j7 = j3 + (this.method == 0 ? this.size : this.csize);
            }
            return this;
        }

        int writeLOC(OutputStream outputStream) throws IOException {
            ZipUtils.writeInt(outputStream, ZipConstants.LOCSIG);
            version();
            int length = this.name != null ? this.name.length : 0;
            int length2 = this.extra != null ? this.extra.length : 0;
            boolean z2 = false;
            int i2 = 0;
            int i3 = 0;
            int i4 = 0;
            int i5 = 0;
            if ((this.flag & 8) != 0) {
                ZipUtils.writeShort(outputStream, version());
                ZipUtils.writeShort(outputStream, this.flag);
                ZipUtils.writeShort(outputStream, this.method);
                ZipUtils.writeInt(outputStream, (int) ZipUtils.javaToDosTime(this.mtime));
                ZipUtils.writeInt(outputStream, 0L);
                ZipUtils.writeInt(outputStream, 0L);
                ZipUtils.writeInt(outputStream, 0L);
            } else {
                if (this.csize >= 4294967295L || this.size >= 4294967295L) {
                    i3 = 20;
                    ZipUtils.writeShort(outputStream, 45);
                } else {
                    ZipUtils.writeShort(outputStream, version());
                }
                ZipUtils.writeShort(outputStream, this.flag);
                ZipUtils.writeShort(outputStream, this.method);
                ZipUtils.writeInt(outputStream, (int) ZipUtils.javaToDosTime(this.mtime));
                ZipUtils.writeInt(outputStream, this.crc);
                if (i3 != 0) {
                    ZipUtils.writeInt(outputStream, 4294967295L);
                    ZipUtils.writeInt(outputStream, 4294967295L);
                } else {
                    ZipUtils.writeInt(outputStream, this.csize);
                    ZipUtils.writeInt(outputStream, this.size);
                }
            }
            while (i2 + 4 < length2) {
                int iSH = ZipConstants.SH(this.extra, i2);
                int iSH2 = ZipConstants.SH(this.extra, i2 + 2);
                if (iSH == 21589 || iSH == 10) {
                    z2 = true;
                }
                i2 += 4 + iSH2;
            }
            if (!z2) {
                if (ZipFileSystem.isWindows) {
                    i5 = 36;
                } else {
                    i4 = 9;
                    if (this.atime != -1) {
                        i4 = 9 + 4;
                    }
                    if (this.ctime != -1) {
                        i4 += 4;
                    }
                }
            }
            ZipUtils.writeShort(outputStream, this.name.length);
            ZipUtils.writeShort(outputStream, length2 + i3 + i5 + i4);
            ZipUtils.writeBytes(outputStream, this.name);
            if (i3 != 0) {
                ZipUtils.writeShort(outputStream, 1);
                ZipUtils.writeShort(outputStream, 16);
                ZipUtils.writeLong(outputStream, this.size);
                ZipUtils.writeLong(outputStream, this.csize);
            }
            if (i5 != 0) {
                ZipUtils.writeShort(outputStream, 10);
                ZipUtils.writeShort(outputStream, i5 - 4);
                ZipUtils.writeInt(outputStream, 0L);
                ZipUtils.writeShort(outputStream, 1);
                ZipUtils.writeShort(outputStream, 24);
                ZipUtils.writeLong(outputStream, ZipUtils.javaToWinTime(this.mtime));
                ZipUtils.writeLong(outputStream, ZipUtils.javaToWinTime(this.atime));
                ZipUtils.writeLong(outputStream, ZipUtils.javaToWinTime(this.ctime));
            }
            if (i4 != 0) {
                ZipUtils.writeShort(outputStream, 21589);
                ZipUtils.writeShort(outputStream, i4 - 4);
                int i6 = 1;
                if (this.atime != -1) {
                    i6 = 1 | 2;
                }
                if (this.ctime != -1) {
                    i6 |= 4;
                }
                outputStream.write(i6);
                ZipUtils.writeInt(outputStream, ZipUtils.javaToUnixTime(this.mtime));
                if (this.atime != -1) {
                    ZipUtils.writeInt(outputStream, ZipUtils.javaToUnixTime(this.atime));
                }
                if (this.ctime != -1) {
                    ZipUtils.writeInt(outputStream, ZipUtils.javaToUnixTime(this.ctime));
                }
            }
            if (this.extra != null) {
                ZipUtils.writeBytes(outputStream, this.extra);
            }
            return 30 + this.name.length + length2 + i3 + i5 + i4;
        }

        int writeEXT(OutputStream outputStream) throws IOException {
            ZipUtils.writeInt(outputStream, ZipConstants.EXTSIG);
            ZipUtils.writeInt(outputStream, this.crc);
            if (this.csize >= 4294967295L || this.size >= 4294967295L) {
                ZipUtils.writeLong(outputStream, this.csize);
                ZipUtils.writeLong(outputStream, this.size);
                return 24;
            }
            ZipUtils.writeInt(outputStream, this.csize);
            ZipUtils.writeInt(outputStream, this.size);
            return 16;
        }

        /* JADX WARN: Removed duplicated region for block: B:21:0x009a  */
        /* JADX WARN: Removed duplicated region for block: B:25:0x00b6 A[PHI: r15
  0x00b6: PHI (r15v4 int) = (r15v3 int), (r15v6 int) binds: [B:20:0x0097, B:24:0x00a6] A[DONT_GENERATE, DONT_INLINE]] */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct code enable 'Show inconsistent code' option in preferences
        */
        void readExtra(com.sun.nio.zipfs.ZipFileSystem r11) throws java.io.IOException {
            /*
                Method dump skipped, instructions count: 702
                To view this dump change 'Code comments level' option to 'DEBUG'
            */
            throw new UnsupportedOperationException("Method not decompiled: com.sun.nio.zipfs.ZipFileSystem.Entry.readExtra(com.sun.nio.zipfs.ZipFileSystem):void");
        }
    }

    /* loaded from: zipfs.jar:com/sun/nio/zipfs/ZipFileSystem$ExChannelCloser.class */
    private static class ExChannelCloser {
        Path path;
        SeekableByteChannel ch;
        Set<InputStream> streams;

        ExChannelCloser(Path path, SeekableByteChannel seekableByteChannel, Set<InputStream> set) {
            this.path = path;
            this.ch = seekableByteChannel;
            this.streams = set;
        }
    }

    private void addToTree(IndexNode indexNode, HashSet<IndexNode> hashSet) {
        IndexNode indexNode2;
        if (hashSet.contains(indexNode)) {
            return;
        }
        byte[] bArr = indexNode.name;
        byte[] parent = getParent(bArr);
        if (this.inodes.containsKey(this.LOOKUPKEY.as(parent))) {
            indexNode2 = this.inodes.get(this.LOOKUPKEY);
        } else {
            indexNode2 = new IndexNode(parent, -1);
            this.inodes.put(indexNode2, indexNode2);
        }
        addToTree(indexNode2, hashSet);
        indexNode.sibling = indexNode2.child;
        indexNode2.child = indexNode;
        if (bArr[bArr.length - 1] == 47) {
            hashSet.add(indexNode);
        }
    }

    private void removeFromTree(IndexNode indexNode) {
        IndexNode indexNode2;
        IndexNode indexNode3 = this.inodes.get(this.LOOKUPKEY.as(getParent(indexNode.name)));
        IndexNode indexNode4 = indexNode3.child;
        if (indexNode4.equals(indexNode)) {
            indexNode3.child = indexNode4.sibling;
            return;
        }
        do {
            indexNode2 = indexNode4;
            IndexNode indexNode5 = indexNode4.sibling;
            indexNode4 = indexNode5;
            if (indexNode5 == null) {
                return;
            }
        } while (!indexNode4.equals(indexNode));
        indexNode2.sibling = indexNode4.sibling;
    }

    private void buildNodeTree() throws IOException {
        beginWrite();
        try {
            HashSet<IndexNode> hashSet = new HashSet<>();
            IndexNode indexNode = new IndexNode(ROOTPATH, -1);
            this.inodes.put(indexNode, indexNode);
            hashSet.add(indexNode);
            for (IndexNode indexNode2 : (IndexNode[]) this.inodes.keySet().toArray(new IndexNode[0])) {
                addToTree(indexNode2, hashSet);
            }
        } finally {
            endWrite();
        }
    }
}
