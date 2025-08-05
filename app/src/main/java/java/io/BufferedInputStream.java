package java.io;

import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;

/* loaded from: rt.jar:java/io/BufferedInputStream.class */
public class BufferedInputStream extends FilterInputStream {
    protected volatile byte[] buf;
    protected int count;
    protected int pos;
    protected int markpos;
    protected int marklimit;
    private static int DEFAULT_BUFFER_SIZE = 8192;
    private static int MAX_BUFFER_SIZE = 2147483639;
    private static final AtomicReferenceFieldUpdater<BufferedInputStream, byte[]> bufUpdater = AtomicReferenceFieldUpdater.newUpdater(BufferedInputStream.class, byte[].class, "buf");

    private InputStream getInIfOpen() throws IOException {
        InputStream inputStream = this.in;
        if (inputStream == null) {
            throw new IOException("Stream closed");
        }
        return inputStream;
    }

    private byte[] getBufIfOpen() throws IOException {
        byte[] bArr = this.buf;
        if (bArr == null) {
            throw new IOException("Stream closed");
        }
        return bArr;
    }

    public BufferedInputStream(InputStream inputStream) {
        this(inputStream, DEFAULT_BUFFER_SIZE);
    }

    public BufferedInputStream(InputStream inputStream, int i2) {
        super(inputStream);
        this.markpos = -1;
        if (i2 <= 0) {
            throw new IllegalArgumentException("Buffer size <= 0");
        }
        this.buf = new byte[i2];
    }

    private void fill() throws IOException {
        byte[] bufIfOpen = getBufIfOpen();
        if (this.markpos < 0) {
            this.pos = 0;
        } else if (this.pos >= bufIfOpen.length) {
            if (this.markpos > 0) {
                int i2 = this.pos - this.markpos;
                System.arraycopy(bufIfOpen, this.markpos, bufIfOpen, 0, i2);
                this.pos = i2;
                this.markpos = 0;
            } else if (bufIfOpen.length >= this.marklimit) {
                this.markpos = -1;
                this.pos = 0;
            } else {
                if (bufIfOpen.length >= MAX_BUFFER_SIZE) {
                    throw new OutOfMemoryError("Required array size too large");
                }
                int i3 = this.pos <= MAX_BUFFER_SIZE - this.pos ? this.pos * 2 : MAX_BUFFER_SIZE;
                if (i3 > this.marklimit) {
                    i3 = this.marklimit;
                }
                byte[] bArr = new byte[i3];
                System.arraycopy(bufIfOpen, 0, bArr, 0, this.pos);
                if (!bufUpdater.compareAndSet(this, bufIfOpen, bArr)) {
                    throw new IOException("Stream closed");
                }
                bufIfOpen = bArr;
            }
        }
        this.count = this.pos;
        int i4 = getInIfOpen().read(bufIfOpen, this.pos, bufIfOpen.length - this.pos);
        if (i4 > 0) {
            this.count = i4 + this.pos;
        }
    }

    @Override // java.io.FilterInputStream, java.io.InputStream
    public synchronized int read() throws IOException {
        if (this.pos >= this.count) {
            fill();
            if (this.pos >= this.count) {
                return -1;
            }
        }
        byte[] bufIfOpen = getBufIfOpen();
        int i2 = this.pos;
        this.pos = i2 + 1;
        return bufIfOpen[i2] & 255;
    }

    private int read1(byte[] bArr, int i2, int i3) throws IOException {
        int i4 = this.count - this.pos;
        if (i4 <= 0) {
            if (i3 >= getBufIfOpen().length && this.markpos < 0) {
                return getInIfOpen().read(bArr, i2, i3);
            }
            fill();
            i4 = this.count - this.pos;
            if (i4 <= 0) {
                return -1;
            }
        }
        int i5 = i4 < i3 ? i4 : i3;
        System.arraycopy(getBufIfOpen(), this.pos, bArr, i2, i5);
        this.pos += i5;
        return i5;
    }

    @Override // java.io.FilterInputStream, java.io.InputStream
    public synchronized int read(byte[] bArr, int i2, int i3) throws IOException {
        getBufIfOpen();
        if ((i2 | i3 | (i2 + i3) | (bArr.length - (i2 + i3))) < 0) {
            throw new IndexOutOfBoundsException();
        }
        if (i3 == 0) {
            return 0;
        }
        int i4 = 0;
        while (true) {
            int i5 = read1(bArr, i2 + i4, i3 - i4);
            if (i5 <= 0) {
                return i4 == 0 ? i5 : i4;
            }
            i4 += i5;
            if (i4 >= i3) {
                return i4;
            }
            InputStream inputStream = this.in;
            if (inputStream != null && inputStream.available() <= 0) {
                return i4;
            }
        }
    }

    @Override // java.io.FilterInputStream, java.io.InputStream
    public synchronized long skip(long j2) throws IOException {
        getBufIfOpen();
        if (j2 <= 0) {
            return 0L;
        }
        long j3 = this.count - this.pos;
        if (j3 <= 0) {
            if (this.markpos < 0) {
                return getInIfOpen().skip(j2);
            }
            fill();
            j3 = this.count - this.pos;
            if (j3 <= 0) {
                return 0L;
            }
        }
        long j4 = j3 < j2 ? j3 : j2;
        this.pos = (int) (this.pos + j4);
        return j4;
    }

    @Override // java.io.FilterInputStream, java.io.InputStream
    public synchronized int available() throws IOException {
        int i2 = this.count - this.pos;
        int iAvailable = getInIfOpen().available();
        if (i2 > Integer.MAX_VALUE - iAvailable) {
            return Integer.MAX_VALUE;
        }
        return i2 + iAvailable;
    }

    @Override // java.io.FilterInputStream, java.io.InputStream
    public synchronized void mark(int i2) {
        this.marklimit = i2;
        this.markpos = this.pos;
    }

    @Override // java.io.FilterInputStream, java.io.InputStream
    public synchronized void reset() throws IOException {
        getBufIfOpen();
        if (this.markpos < 0) {
            throw new IOException("Resetting to invalid mark");
        }
        this.pos = this.markpos;
    }

    @Override // java.io.FilterInputStream, java.io.InputStream
    public boolean markSupported() {
        return true;
    }

    @Override // java.io.FilterInputStream, java.io.InputStream, java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        byte[] bArr;
        do {
            bArr = this.buf;
            if (bArr == null) {
                return;
            }
        } while (!bufUpdater.compareAndSet(this, bArr, null));
        InputStream inputStream = this.in;
        this.in = null;
        if (inputStream != null) {
            inputStream.close();
        }
    }
}
