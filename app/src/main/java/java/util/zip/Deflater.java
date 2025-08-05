package java.util.zip;

/* loaded from: rt.jar:java/util/zip/Deflater.class */
public class Deflater {
    private final ZStreamRef zsRef;
    private byte[] buf;
    private int off;
    private int len;
    private int level;
    private int strategy;
    private boolean setParams;
    private boolean finish;
    private boolean finished;
    private long bytesRead;
    private long bytesWritten;
    public static final int DEFLATED = 8;
    public static final int NO_COMPRESSION = 0;
    public static final int BEST_SPEED = 1;
    public static final int BEST_COMPRESSION = 9;
    public static final int DEFAULT_COMPRESSION = -1;
    public static final int FILTERED = 1;
    public static final int HUFFMAN_ONLY = 2;
    public static final int DEFAULT_STRATEGY = 0;
    public static final int NO_FLUSH = 0;
    public static final int SYNC_FLUSH = 2;
    public static final int FULL_FLUSH = 3;
    static final /* synthetic */ boolean $assertionsDisabled;

    private static native void initIDs();

    private static native long init(int i2, int i3, boolean z2);

    private static native void setDictionary(long j2, byte[] bArr, int i2, int i3);

    private native int deflateBytes(long j2, byte[] bArr, int i2, int i3, int i4);

    private static native int getAdler(long j2);

    private static native void reset(long j2);

    private static native void end(long j2);

    static {
        $assertionsDisabled = !Deflater.class.desiredAssertionStatus();
        initIDs();
    }

    public Deflater(int i2, boolean z2) {
        this.buf = new byte[0];
        this.level = i2;
        this.strategy = 0;
        this.zsRef = new ZStreamRef(init(i2, 0, z2));
    }

    public Deflater(int i2) {
        this(i2, false);
    }

    public Deflater() {
        this(-1, false);
    }

    public void setInput(byte[] bArr, int i2, int i3) {
        if (bArr == null) {
            throw new NullPointerException();
        }
        if (i2 < 0 || i3 < 0 || i2 > bArr.length - i3) {
            throw new ArrayIndexOutOfBoundsException();
        }
        synchronized (this.zsRef) {
            this.buf = bArr;
            this.off = i2;
            this.len = i3;
        }
    }

    public void setInput(byte[] bArr) {
        setInput(bArr, 0, bArr.length);
    }

    public void setDictionary(byte[] bArr, int i2, int i3) {
        if (bArr == null) {
            throw new NullPointerException();
        }
        if (i2 < 0 || i3 < 0 || i2 > bArr.length - i3) {
            throw new ArrayIndexOutOfBoundsException();
        }
        synchronized (this.zsRef) {
            ensureOpen();
            setDictionary(this.zsRef.address(), bArr, i2, i3);
        }
    }

    public void setDictionary(byte[] bArr) {
        setDictionary(bArr, 0, bArr.length);
    }

    public void setStrategy(int i2) {
        switch (i2) {
            case 0:
            case 1:
            case 2:
                synchronized (this.zsRef) {
                    if (this.strategy != i2) {
                        this.strategy = i2;
                        this.setParams = true;
                    }
                }
                return;
            default:
                throw new IllegalArgumentException();
        }
    }

    public void setLevel(int i2) {
        if ((i2 < 0 || i2 > 9) && i2 != -1) {
            throw new IllegalArgumentException("invalid compression level");
        }
        synchronized (this.zsRef) {
            if (this.level != i2) {
                this.level = i2;
                this.setParams = true;
            }
        }
    }

    public boolean needsInput() {
        boolean z2;
        synchronized (this.zsRef) {
            z2 = this.len <= 0;
        }
        return z2;
    }

    public void finish() {
        synchronized (this.zsRef) {
            this.finish = true;
        }
    }

    public boolean finished() {
        boolean z2;
        synchronized (this.zsRef) {
            z2 = this.finished;
        }
        return z2;
    }

    public int deflate(byte[] bArr, int i2, int i3) {
        return deflate(bArr, i2, i3, 0);
    }

    public int deflate(byte[] bArr) {
        return deflate(bArr, 0, bArr.length, 0);
    }

    public int deflate(byte[] bArr, int i2, int i3, int i4) {
        int iDeflateBytes;
        if (bArr == null) {
            throw new NullPointerException();
        }
        if (i2 < 0 || i3 < 0 || i2 > bArr.length - i3) {
            throw new ArrayIndexOutOfBoundsException();
        }
        synchronized (this.zsRef) {
            ensureOpen();
            if (i4 == 0 || i4 == 2 || i4 == 3) {
                int i5 = this.len;
                iDeflateBytes = deflateBytes(this.zsRef.address(), bArr, i2, i3, i4);
                this.bytesWritten += iDeflateBytes;
                this.bytesRead += i5 - this.len;
            } else {
                throw new IllegalArgumentException();
            }
        }
        return iDeflateBytes;
    }

    public int getAdler() {
        int adler;
        synchronized (this.zsRef) {
            ensureOpen();
            adler = getAdler(this.zsRef.address());
        }
        return adler;
    }

    public int getTotalIn() {
        return (int) getBytesRead();
    }

    public long getBytesRead() {
        long j2;
        synchronized (this.zsRef) {
            ensureOpen();
            j2 = this.bytesRead;
        }
        return j2;
    }

    public int getTotalOut() {
        return (int) getBytesWritten();
    }

    public long getBytesWritten() {
        long j2;
        synchronized (this.zsRef) {
            ensureOpen();
            j2 = this.bytesWritten;
        }
        return j2;
    }

    public void reset() {
        synchronized (this.zsRef) {
            ensureOpen();
            reset(this.zsRef.address());
            this.finish = false;
            this.finished = false;
            this.len = 0;
            this.off = 0;
            this.bytesWritten = 0L;
            this.bytesRead = 0L;
        }
    }

    public void end() {
        synchronized (this.zsRef) {
            long jAddress = this.zsRef.address();
            this.zsRef.clear();
            if (jAddress != 0) {
                end(jAddress);
                this.buf = null;
            }
        }
    }

    protected void finalize() {
        end();
    }

    private void ensureOpen() {
        if (!$assertionsDisabled && !Thread.holdsLock(this.zsRef)) {
            throw new AssertionError();
        }
        if (this.zsRef.address() == 0) {
            throw new NullPointerException("Deflater has been closed");
        }
    }
}
