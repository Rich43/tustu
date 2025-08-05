package java.util.zip;

import java.io.Closeable;
import java.io.EOFException;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Spliterators;
import java.util.WeakHashMap;
import java.util.jar.JarFile;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;
import sun.misc.JavaUtilZipFileAccess;
import sun.misc.PerfCounter;
import sun.misc.SharedSecrets;
import sun.misc.VM;

/* loaded from: rt.jar:java/util/zip/ZipFile.class */
public class ZipFile implements ZipConstants, Closeable {
    private long jzfile;
    private final String name;
    private final int total;
    private final boolean locsig;
    private volatile boolean closeRequested;
    private int manifestNum;
    private static final int STORED = 0;
    private static final int DEFLATED = 8;
    public static final int OPEN_READ = 1;
    public static final int OPEN_DELETE = 4;
    private static final boolean usemmap;
    private static final boolean ensuretrailingslash;
    private ZipCoder zc;
    private final Map<InputStream, Inflater> streams;
    private Deque<Inflater> inflaterCache;
    private static final int JZENTRY_NAME = 0;
    private static final int JZENTRY_EXTRA = 1;
    private static final int JZENTRY_COMMENT = 2;

    private static native void initIDs();

    private static native long getEntry(long j2, byte[] bArr, boolean z2);

    /* JADX INFO: Access modifiers changed from: private */
    public static native void freeEntry(long j2, long j3);

    /* JADX INFO: Access modifiers changed from: private */
    public static native long getNextEntry(long j2, int i2);

    private static native void close(long j2);

    private static native long open(String str, int i2, long j2, boolean z2) throws IOException;

    private static native int getTotal(long j2);

    private static native boolean startsWithLOC(long j2);

    private static native int getManifestNum(long j2);

    /* JADX INFO: Access modifiers changed from: private */
    public static native int read(long j2, long j3, long j4, byte[] bArr, int i2, int i3);

    private static native long getEntryTime(long j2);

    private static native long getEntryCrc(long j2);

    /* JADX INFO: Access modifiers changed from: private */
    public static native long getEntryCSize(long j2);

    /* JADX INFO: Access modifiers changed from: private */
    public static native long getEntrySize(long j2);

    private static native int getEntryMethod(long j2);

    private static native int getEntryFlag(long j2);

    private static native byte[] getCommentBytes(long j2);

    private static native byte[] getEntryBytes(long j2, int i2);

    /* JADX INFO: Access modifiers changed from: private */
    public static native String getZipMessage(long j2);

    static {
        initIDs();
        String savedProperty = VM.getSavedProperty("sun.zip.disableMemoryMapping");
        usemmap = savedProperty == null || !(savedProperty.length() == 0 || savedProperty.equalsIgnoreCase("true"));
        String savedProperty2 = VM.getSavedProperty("jdk.util.zip.ensureTrailingSlash");
        ensuretrailingslash = savedProperty2 == null || !savedProperty2.equalsIgnoreCase("false");
        SharedSecrets.setJavaUtilZipFileAccess(new JavaUtilZipFileAccess() { // from class: java.util.zip.ZipFile.1
            @Override // sun.misc.JavaUtilZipFileAccess
            public boolean startsWithLocHeader(ZipFile zipFile) {
                return zipFile.startsWithLocHeader();
            }

            @Override // sun.misc.JavaUtilZipFileAccess
            public int getManifestNum(JarFile jarFile) {
                return jarFile.getManifestNum();
            }
        });
    }

    public ZipFile(String str) throws IOException {
        this(new File(str), 1);
    }

    public ZipFile(File file, int i2) throws IOException {
        this(file, i2, StandardCharsets.UTF_8);
    }

    public ZipFile(File file) throws IOException {
        this(file, 1);
    }

    public ZipFile(File file, int i2, Charset charset) throws IOException {
        this.closeRequested = false;
        this.manifestNum = 0;
        this.streams = new WeakHashMap();
        this.inflaterCache = new ArrayDeque();
        if ((i2 & 1) == 0 || (i2 & (-6)) != 0) {
            throw new IllegalArgumentException("Illegal mode: 0x" + Integer.toHexString(i2));
        }
        String path = file.getPath();
        if (path.indexOf(0) >= 0) {
            throw new IOException("Illegal filename");
        }
        File file2 = new File(path);
        SecurityManager securityManager = System.getSecurityManager();
        if (securityManager != null) {
            securityManager.checkRead(path);
            if ((i2 & 4) != 0) {
                securityManager.checkDelete(path);
            }
        }
        if (charset == null) {
            throw new NullPointerException("charset is null");
        }
        this.zc = ZipCoder.get(charset);
        long jNanoTime = System.nanoTime();
        this.jzfile = open(path, i2, file2.lastModified(), usemmap);
        PerfCounter.getZipFileOpenTime().addElapsedTimeFrom(jNanoTime);
        PerfCounter.getZipFileCount().increment();
        this.name = path;
        this.total = getTotal(this.jzfile);
        this.locsig = startsWithLOC(this.jzfile);
        this.manifestNum = getManifestNum(this.jzfile);
    }

    public ZipFile(String str, Charset charset) throws IOException {
        this(new File(str), 1, charset);
    }

    public ZipFile(File file, Charset charset) throws IOException {
        this(file, 1, charset);
    }

    public String getComment() {
        synchronized (this) {
            ensureOpen();
            byte[] commentBytes = getCommentBytes(this.jzfile);
            if (commentBytes == null) {
                return null;
            }
            return this.zc.toString(commentBytes, commentBytes.length);
        }
    }

    public ZipEntry getEntry(String str) {
        if (str == null) {
            throw new NullPointerException("name");
        }
        synchronized (this) {
            ensureOpen();
            long entry = getEntry(this.jzfile, this.zc.getBytes(str), true);
            if (entry != 0) {
                ZipEntry zipEntry = ensuretrailingslash ? getZipEntry(null, entry) : getZipEntry(str, entry);
                freeEntry(this.jzfile, entry);
                return zipEntry;
            }
            return null;
        }
    }

    public InputStream getInputStream(ZipEntry zipEntry) throws IOException {
        long entry;
        if (zipEntry == null) {
            throw new NullPointerException("entry");
        }
        synchronized (this) {
            ensureOpen();
            if (!this.zc.isUTF8() && (zipEntry.flag & 2048) != 0) {
                entry = getEntry(this.jzfile, this.zc.getBytesUTF8(zipEntry.name), false);
            } else {
                entry = getEntry(this.jzfile, this.zc.getBytes(zipEntry.name), false);
            }
            if (entry == 0) {
                return null;
            }
            ZipFileInputStream zipFileInputStream = new ZipFileInputStream(entry);
            switch (getEntryMethod(entry)) {
                case 0:
                    synchronized (this.streams) {
                        this.streams.put(zipFileInputStream, null);
                    }
                    return zipFileInputStream;
                case 8:
                    long entrySize = getEntrySize(entry) + 2;
                    if (entrySize > 65536) {
                        entrySize = 8192;
                    }
                    if (entrySize <= 0) {
                        entrySize = 4096;
                    }
                    Inflater inflater = getInflater();
                    ZipFileInflaterInputStream zipFileInflaterInputStream = new ZipFileInflaterInputStream(zipFileInputStream, inflater, (int) entrySize);
                    synchronized (this.streams) {
                        this.streams.put(zipFileInflaterInputStream, inflater);
                    }
                    return zipFileInflaterInputStream;
                default:
                    throw new ZipException("invalid compression method");
            }
        }
    }

    /* loaded from: rt.jar:java/util/zip/ZipFile$ZipFileInflaterInputStream.class */
    private class ZipFileInflaterInputStream extends InflaterInputStream {
        private volatile boolean closeRequested;
        private boolean eof;
        private final ZipFileInputStream zfin;

        ZipFileInflaterInputStream(ZipFileInputStream zipFileInputStream, Inflater inflater, int i2) {
            super(zipFileInputStream, inflater, i2);
            this.closeRequested = false;
            this.eof = false;
            this.zfin = zipFileInputStream;
        }

        @Override // java.util.zip.InflaterInputStream, java.io.FilterInputStream, java.io.InputStream, java.io.Closeable, java.lang.AutoCloseable
        public void close() throws IOException {
            Inflater inflater;
            if (this.closeRequested) {
                return;
            }
            this.closeRequested = true;
            super.close();
            synchronized (ZipFile.this.streams) {
                inflater = (Inflater) ZipFile.this.streams.remove(this);
            }
            if (inflater != null) {
                ZipFile.this.releaseInflater(inflater);
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
            if (this.closeRequested) {
                return 0;
            }
            long size = this.zfin.size() - this.inf.getBytesWritten();
            if (size > 2147483647L) {
                return Integer.MAX_VALUE;
            }
            return (int) size;
        }

        protected void finalize() throws Throwable {
            close();
        }
    }

    private Inflater getInflater() {
        Inflater inflaterPoll;
        synchronized (this.inflaterCache) {
            do {
                inflaterPoll = this.inflaterCache.poll();
                if (null == inflaterPoll) {
                    return new Inflater(true);
                }
            } while (false != inflaterPoll.ended());
            return inflaterPoll;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void releaseInflater(Inflater inflater) {
        if (false == inflater.ended()) {
            inflater.reset();
            synchronized (this.inflaterCache) {
                this.inflaterCache.add(inflater);
            }
        }
    }

    public String getName() {
        return this.name;
    }

    /* loaded from: rt.jar:java/util/zip/ZipFile$ZipEntryIterator.class */
    private class ZipEntryIterator implements Enumeration<ZipEntry>, Iterator<ZipEntry> {

        /* renamed from: i, reason: collision with root package name */
        private int f12623i = 0;

        public ZipEntryIterator() {
            ZipFile.this.ensureOpen();
        }

        @Override // java.util.Enumeration
        public boolean hasMoreElements() {
            return hasNext();
        }

        @Override // java.util.Iterator
        public boolean hasNext() {
            boolean z2;
            synchronized (ZipFile.this) {
                ZipFile.this.ensureOpen();
                z2 = this.f12623i < ZipFile.this.total;
            }
            return z2;
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // java.util.Enumeration
        /* renamed from: nextElement */
        public ZipEntry nextElement2() {
            return next();
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // java.util.Iterator
        public ZipEntry next() {
            ZipEntry zipEntry;
            String zipMessage;
            synchronized (ZipFile.this) {
                ZipFile.this.ensureOpen();
                if (this.f12623i < ZipFile.this.total) {
                    long j2 = ZipFile.this.jzfile;
                    int i2 = this.f12623i;
                    this.f12623i = i2 + 1;
                    long nextEntry = ZipFile.getNextEntry(j2, i2);
                    if (nextEntry == 0) {
                        if (!ZipFile.this.closeRequested) {
                            zipMessage = ZipFile.getZipMessage(ZipFile.this.jzfile);
                        } else {
                            zipMessage = "ZipFile concurrently closed";
                        }
                        throw new ZipError("jzentry == 0,\n jzfile = " + ZipFile.this.jzfile + ",\n total = " + ZipFile.this.total + ",\n name = " + ZipFile.this.name + ",\n i = " + this.f12623i + ",\n message = " + zipMessage);
                    }
                    zipEntry = ZipFile.this.getZipEntry(null, nextEntry);
                    ZipFile.freeEntry(ZipFile.this.jzfile, nextEntry);
                } else {
                    throw new NoSuchElementException();
                }
            }
            return zipEntry;
        }
    }

    public Enumeration<? extends ZipEntry> entries() {
        return new ZipEntryIterator();
    }

    public Stream<? extends ZipEntry> stream() {
        return StreamSupport.stream(Spliterators.spliterator(new ZipEntryIterator(), size(), 1297), false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public ZipEntry getZipEntry(String str, long j2) {
        ZipEntry zipEntry = new ZipEntry();
        zipEntry.flag = getEntryFlag(j2);
        if (str != null) {
            zipEntry.name = str;
        } else {
            byte[] entryBytes = getEntryBytes(j2, 0);
            if (entryBytes == null) {
                zipEntry.name = "";
            } else if (!this.zc.isUTF8() && (zipEntry.flag & 2048) != 0) {
                zipEntry.name = this.zc.toStringUTF8(entryBytes, entryBytes.length);
            } else {
                zipEntry.name = this.zc.toString(entryBytes, entryBytes.length);
            }
        }
        zipEntry.xdostime = getEntryTime(j2);
        zipEntry.crc = getEntryCrc(j2);
        zipEntry.size = getEntrySize(j2);
        zipEntry.csize = getEntryCSize(j2);
        zipEntry.method = getEntryMethod(j2);
        zipEntry.setExtra0(getEntryBytes(j2, 1), false);
        byte[] entryBytes2 = getEntryBytes(j2, 2);
        if (entryBytes2 == null) {
            zipEntry.comment = null;
        } else if (!this.zc.isUTF8() && (zipEntry.flag & 2048) != 0) {
            zipEntry.comment = this.zc.toStringUTF8(entryBytes2, entryBytes2.length);
        } else {
            zipEntry.comment = this.zc.toString(entryBytes2, entryBytes2.length);
        }
        return zipEntry;
    }

    public int size() {
        ensureOpen();
        return this.total;
    }

    @Override // java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        if (this.closeRequested) {
            return;
        }
        this.closeRequested = true;
        synchronized (this) {
            synchronized (this.streams) {
                if (false == this.streams.isEmpty()) {
                    HashMap map = new HashMap(this.streams);
                    this.streams.clear();
                    Iterator it = map.entrySet().iterator();
                    while (it.hasNext()) {
                        Map.Entry entry = (Map.Entry) it.next();
                        ((InputStream) entry.getKey()).close();
                        Inflater inflater = (Inflater) entry.getValue();
                        if (inflater != null) {
                            inflater.end();
                        }
                    }
                }
            }
            synchronized (this.inflaterCache) {
                while (true) {
                    Inflater inflaterPoll = this.inflaterCache.poll();
                    if (null == inflaterPoll) {
                        break;
                    } else {
                        inflaterPoll.end();
                    }
                }
            }
            if (this.jzfile != 0) {
                long j2 = this.jzfile;
                this.jzfile = 0L;
                close(j2);
            }
        }
    }

    protected void finalize() throws IOException {
        close();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void ensureOpen() {
        if (this.closeRequested) {
            throw new IllegalStateException("zip file closed");
        }
        if (this.jzfile == 0) {
            throw new IllegalStateException("The object is not initialized.");
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void ensureOpenOrZipException() throws IOException {
        if (this.closeRequested) {
            throw new ZipException("ZipFile closed");
        }
    }

    /* loaded from: rt.jar:java/util/zip/ZipFile$ZipFileInputStream.class */
    private class ZipFileInputStream extends InputStream {
        protected long jzentry;
        protected long rem;
        protected long size;
        private volatile boolean zfisCloseRequested = false;
        private long pos = 0;

        ZipFileInputStream(long j2) {
            this.rem = ZipFile.getEntryCSize(j2);
            this.size = ZipFile.getEntrySize(j2);
            this.jzentry = j2;
        }

        @Override // java.io.InputStream
        public int read(byte[] bArr, int i2, int i3) throws IOException {
            synchronized (ZipFile.this) {
                long j2 = this.rem;
                long j3 = this.pos;
                if (j2 == 0) {
                    return -1;
                }
                if (i3 > 0) {
                    if (i3 > j2) {
                        i3 = (int) j2;
                    }
                    ZipFile.this.ensureOpenOrZipException();
                    int i4 = ZipFile.read(ZipFile.this.jzfile, this.jzentry, j3, bArr, i2, i3);
                    if (i4 > 0) {
                        this.pos = j3 + i4;
                        this.rem = j2 - i4;
                    }
                    if (this.rem == 0) {
                        close();
                    }
                    return i4;
                }
                return 0;
            }
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
        public long skip(long j2) {
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
            if (this.zfisCloseRequested) {
                return;
            }
            this.zfisCloseRequested = true;
            this.rem = 0L;
            synchronized (ZipFile.this) {
                if (this.jzentry != 0 && ZipFile.this.jzfile != 0) {
                    ZipFile.freeEntry(ZipFile.this.jzfile, this.jzentry);
                    this.jzentry = 0L;
                }
            }
            synchronized (ZipFile.this.streams) {
                ZipFile.this.streams.remove(this);
            }
        }

        protected void finalize() {
            close();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean startsWithLocHeader() {
        return this.locsig;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int getManifestNum() {
        int i2;
        synchronized (this) {
            ensureOpen();
            i2 = this.manifestNum;
        }
        return i2;
    }
}
