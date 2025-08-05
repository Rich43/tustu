package java.util.zip;

/* loaded from: rt.jar:java/util/zip/Inflater.class */
public class Inflater {
    private final ZStreamRef zsRef;
    private byte[] buf;
    private int off;
    private int len;
    private boolean finished;
    private boolean needDict;
    private long bytesRead;
    private long bytesWritten;
    private static final byte[] defaultBuf;
    static final /* synthetic */ boolean $assertionsDisabled;

    private static native void initIDs();

    private static native long init(boolean z2);

    private static native void setDictionary(long j2, byte[] bArr, int i2, int i3);

    private native int inflateBytes(long j2, byte[] bArr, int i2, int i3) throws DataFormatException;

    private static native int getAdler(long j2);

    private static native void reset(long j2);

    private static native void end(long j2);

    static {
        $assertionsDisabled = !Inflater.class.desiredAssertionStatus();
        defaultBuf = new byte[0];
        initIDs();
    }

    public Inflater(boolean z2) {
        this.buf = defaultBuf;
        this.zsRef = new ZStreamRef(init(z2));
    }

    public Inflater() {
        this(false);
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
            this.needDict = false;
        }
    }

    public void setDictionary(byte[] bArr) {
        setDictionary(bArr, 0, bArr.length);
    }

    public int getRemaining() {
        int i2;
        synchronized (this.zsRef) {
            i2 = this.len;
        }
        return i2;
    }

    public boolean needsInput() {
        boolean z2;
        synchronized (this.zsRef) {
            z2 = this.len <= 0;
        }
        return z2;
    }

    public boolean needsDictionary() {
        boolean z2;
        synchronized (this.zsRef) {
            z2 = this.needDict;
        }
        return z2;
    }

    public boolean finished() {
        boolean z2;
        synchronized (this.zsRef) {
            z2 = this.finished;
        }
        return z2;
    }

    public int inflate(byte[] bArr, int i2, int i3) throws DataFormatException {
        int iInflateBytes;
        if (bArr == null) {
            throw new NullPointerException();
        }
        if (i2 < 0 || i3 < 0 || i2 > bArr.length - i3) {
            throw new ArrayIndexOutOfBoundsException();
        }
        synchronized (this.zsRef) {
            ensureOpen();
            int i4 = this.len;
            iInflateBytes = inflateBytes(this.zsRef.address(), bArr, i2, i3);
            this.bytesWritten += iInflateBytes;
            this.bytesRead += i4 - this.len;
        }
        return iInflateBytes;
    }

    public int inflate(byte[] bArr) throws DataFormatException {
        return inflate(bArr, 0, bArr.length);
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
            this.buf = defaultBuf;
            this.finished = false;
            this.needDict = false;
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
            throw new NullPointerException("Inflater has been closed");
        }
    }

    boolean ended() {
        boolean z2;
        synchronized (this.zsRef) {
            z2 = this.zsRef.address() == 0;
        }
        return z2;
    }
}
