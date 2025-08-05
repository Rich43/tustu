package java.io;

/* loaded from: rt.jar:java/io/PipedInputStream.class */
public class PipedInputStream extends InputStream {
    boolean closedByWriter;
    volatile boolean closedByReader;
    boolean connected;
    Thread readSide;
    Thread writeSide;
    private static final int DEFAULT_PIPE_SIZE = 1024;
    protected static final int PIPE_SIZE = 1024;
    protected byte[] buffer;
    protected int in;
    protected int out;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !PipedInputStream.class.desiredAssertionStatus();
    }

    public PipedInputStream(PipedOutputStream pipedOutputStream) throws IOException {
        this(pipedOutputStream, 1024);
    }

    public PipedInputStream(PipedOutputStream pipedOutputStream, int i2) throws IOException {
        this.closedByWriter = false;
        this.closedByReader = false;
        this.connected = false;
        this.in = -1;
        this.out = 0;
        initPipe(i2);
        connect(pipedOutputStream);
    }

    public PipedInputStream() {
        this.closedByWriter = false;
        this.closedByReader = false;
        this.connected = false;
        this.in = -1;
        this.out = 0;
        initPipe(1024);
    }

    public PipedInputStream(int i2) {
        this.closedByWriter = false;
        this.closedByReader = false;
        this.connected = false;
        this.in = -1;
        this.out = 0;
        initPipe(i2);
    }

    private void initPipe(int i2) {
        if (i2 <= 0) {
            throw new IllegalArgumentException("Pipe Size <= 0");
        }
        this.buffer = new byte[i2];
    }

    public void connect(PipedOutputStream pipedOutputStream) throws IOException {
        pipedOutputStream.connect(this);
    }

    protected synchronized void receive(int i2) throws IOException {
        checkStateForReceive();
        this.writeSide = Thread.currentThread();
        if (this.in == this.out) {
            awaitSpace();
        }
        if (this.in < 0) {
            this.in = 0;
            this.out = 0;
        }
        byte[] bArr = this.buffer;
        int i3 = this.in;
        this.in = i3 + 1;
        bArr[i3] = (byte) (i2 & 255);
        if (this.in >= this.buffer.length) {
            this.in = 0;
        }
    }

    synchronized void receive(byte[] bArr, int i2, int i3) throws IOException {
        checkStateForReceive();
        this.writeSide = Thread.currentThread();
        int i4 = i3;
        while (i4 > 0) {
            if (this.in == this.out) {
                awaitSpace();
            }
            int length = 0;
            if (this.out < this.in) {
                length = this.buffer.length - this.in;
            } else if (this.in < this.out) {
                if (this.in == -1) {
                    this.out = 0;
                    this.in = 0;
                    length = this.buffer.length - this.in;
                } else {
                    length = this.out - this.in;
                }
            }
            if (length > i4) {
                length = i4;
            }
            if (!$assertionsDisabled && length <= 0) {
                throw new AssertionError();
            }
            System.arraycopy(bArr, i2, this.buffer, this.in, length);
            i4 -= length;
            i2 += length;
            this.in += length;
            if (this.in >= this.buffer.length) {
                this.in = 0;
            }
        }
    }

    private void checkStateForReceive() throws IOException {
        if (!this.connected) {
            throw new IOException("Pipe not connected");
        }
        if (this.closedByWriter || this.closedByReader) {
            throw new IOException("Pipe closed");
        }
        if (this.readSide != null && !this.readSide.isAlive()) {
            throw new IOException("Read end dead");
        }
    }

    private void awaitSpace() throws IOException {
        while (this.in == this.out) {
            checkStateForReceive();
            notifyAll();
            try {
                wait(1000L);
            } catch (InterruptedException e2) {
                throw new InterruptedIOException();
            }
        }
    }

    synchronized void receivedLast() {
        this.closedByWriter = true;
        notifyAll();
    }

    @Override // java.io.InputStream
    public synchronized int read() throws IOException {
        if (!this.connected) {
            throw new IOException("Pipe not connected");
        }
        if (this.closedByReader) {
            throw new IOException("Pipe closed");
        }
        if (this.writeSide != null && !this.writeSide.isAlive() && !this.closedByWriter && this.in < 0) {
            throw new IOException("Write end dead");
        }
        this.readSide = Thread.currentThread();
        int i2 = 2;
        while (this.in < 0) {
            if (this.closedByWriter) {
                return -1;
            }
            if (this.writeSide != null && !this.writeSide.isAlive()) {
                i2--;
                if (i2 < 0) {
                    throw new IOException("Pipe broken");
                }
            }
            notifyAll();
            try {
                wait(1000L);
            } catch (InterruptedException e2) {
                throw new InterruptedIOException();
            }
        }
        byte[] bArr = this.buffer;
        int i3 = this.out;
        this.out = i3 + 1;
        int i4 = bArr[i3] & 255;
        if (this.out >= this.buffer.length) {
            this.out = 0;
        }
        if (this.in == this.out) {
            this.in = -1;
        }
        return i4;
    }

    @Override // java.io.InputStream
    public synchronized int read(byte[] bArr, int i2, int i3) throws IOException {
        int length;
        if (bArr == null) {
            throw new NullPointerException();
        }
        if (i2 < 0 || i3 < 0 || i3 > bArr.length - i2) {
            throw new IndexOutOfBoundsException();
        }
        if (i3 == 0) {
            return 0;
        }
        int i4 = read();
        if (i4 < 0) {
            return -1;
        }
        bArr[i2] = (byte) i4;
        int i5 = 1;
        while (this.in >= 0 && i3 > 1) {
            if (this.in > this.out) {
                length = Math.min(this.buffer.length - this.out, this.in - this.out);
            } else {
                length = this.buffer.length - this.out;
            }
            if (length > i3 - 1) {
                length = i3 - 1;
            }
            System.arraycopy(this.buffer, this.out, bArr, i2 + i5, length);
            this.out += length;
            i5 += length;
            i3 -= length;
            if (this.out >= this.buffer.length) {
                this.out = 0;
            }
            if (this.in == this.out) {
                this.in = -1;
            }
        }
        return i5;
    }

    @Override // java.io.InputStream
    public synchronized int available() throws IOException {
        if (this.in < 0) {
            return 0;
        }
        if (this.in == this.out) {
            return this.buffer.length;
        }
        if (this.in > this.out) {
            return this.in - this.out;
        }
        return (this.in + this.buffer.length) - this.out;
    }

    @Override // java.io.InputStream, java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        this.closedByReader = true;
        synchronized (this) {
            this.in = -1;
        }
    }
}
