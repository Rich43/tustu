package java.io;

/* loaded from: rt.jar:java/io/PipedReader.class */
public class PipedReader extends Reader {
    boolean closedByWriter;
    boolean closedByReader;
    boolean connected;
    Thread readSide;
    Thread writeSide;
    private static final int DEFAULT_PIPE_SIZE = 1024;
    char[] buffer;
    int in;
    int out;

    public PipedReader(PipedWriter pipedWriter) throws IOException {
        this(pipedWriter, 1024);
    }

    public PipedReader(PipedWriter pipedWriter, int i2) throws IOException {
        this.closedByWriter = false;
        this.closedByReader = false;
        this.connected = false;
        this.in = -1;
        this.out = 0;
        initPipe(i2);
        connect(pipedWriter);
    }

    public PipedReader() {
        this.closedByWriter = false;
        this.closedByReader = false;
        this.connected = false;
        this.in = -1;
        this.out = 0;
        initPipe(1024);
    }

    public PipedReader(int i2) {
        this.closedByWriter = false;
        this.closedByReader = false;
        this.connected = false;
        this.in = -1;
        this.out = 0;
        initPipe(i2);
    }

    private void initPipe(int i2) {
        if (i2 <= 0) {
            throw new IllegalArgumentException("Pipe size <= 0");
        }
        this.buffer = new char[i2];
    }

    public void connect(PipedWriter pipedWriter) throws IOException {
        pipedWriter.connect(this);
    }

    synchronized void receive(int i2) throws IOException {
        if (!this.connected) {
            throw new IOException("Pipe not connected");
        }
        if (this.closedByWriter || this.closedByReader) {
            throw new IOException("Pipe closed");
        }
        if (this.readSide != null && !this.readSide.isAlive()) {
            throw new IOException("Read end dead");
        }
        this.writeSide = Thread.currentThread();
        while (this.in == this.out) {
            if (this.readSide != null && !this.readSide.isAlive()) {
                throw new IOException("Pipe broken");
            }
            notifyAll();
            try {
                wait(1000L);
            } catch (InterruptedException e2) {
                throw new InterruptedIOException();
            }
        }
        if (this.in < 0) {
            this.in = 0;
            this.out = 0;
        }
        char[] cArr = this.buffer;
        int i3 = this.in;
        this.in = i3 + 1;
        cArr[i3] = (char) i2;
        if (this.in >= this.buffer.length) {
            this.in = 0;
        }
    }

    synchronized void receive(char[] cArr, int i2, int i3) throws IOException {
        while (true) {
            i3--;
            if (i3 >= 0) {
                int i4 = i2;
                i2++;
                receive(cArr[i4]);
            } else {
                return;
            }
        }
    }

    synchronized void receivedLast() {
        this.closedByWriter = true;
        notifyAll();
    }

    @Override // java.io.Reader
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
        char[] cArr = this.buffer;
        int i3 = this.out;
        this.out = i3 + 1;
        char c2 = cArr[i3];
        if (this.out >= this.buffer.length) {
            this.out = 0;
        }
        if (this.in == this.out) {
            this.in = -1;
        }
        return c2;
    }

    @Override // java.io.Reader
    public synchronized int read(char[] cArr, int i2, int i3) throws IOException {
        if (!this.connected) {
            throw new IOException("Pipe not connected");
        }
        if (this.closedByReader) {
            throw new IOException("Pipe closed");
        }
        if (this.writeSide != null && !this.writeSide.isAlive() && !this.closedByWriter && this.in < 0) {
            throw new IOException("Write end dead");
        }
        if (i2 < 0 || i2 > cArr.length || i3 < 0 || i2 + i3 > cArr.length || i2 + i3 < 0) {
            throw new IndexOutOfBoundsException();
        }
        if (i3 == 0) {
            return 0;
        }
        int i4 = read();
        if (i4 < 0) {
            return -1;
        }
        cArr[i2] = (char) i4;
        int i5 = 1;
        while (this.in >= 0) {
            i3--;
            if (i3 <= 0) {
                break;
            }
            char[] cArr2 = this.buffer;
            int i6 = this.out;
            this.out = i6 + 1;
            cArr[i2 + i5] = cArr2[i6];
            i5++;
            if (this.out >= this.buffer.length) {
                this.out = 0;
            }
            if (this.in == this.out) {
                this.in = -1;
            }
        }
        return i5;
    }

    @Override // java.io.Reader
    public synchronized boolean ready() throws IOException {
        if (!this.connected) {
            throw new IOException("Pipe not connected");
        }
        if (this.closedByReader) {
            throw new IOException("Pipe closed");
        }
        if (this.writeSide != null && !this.writeSide.isAlive() && !this.closedByWriter && this.in < 0) {
            throw new IOException("Write end dead");
        }
        if (this.in < 0) {
            return false;
        }
        return true;
    }

    @Override // java.io.Reader, java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        this.in = -1;
        this.closedByReader = true;
    }
}
