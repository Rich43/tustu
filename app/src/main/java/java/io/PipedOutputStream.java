package java.io;

/* loaded from: rt.jar:java/io/PipedOutputStream.class */
public class PipedOutputStream extends OutputStream {
    private PipedInputStream sink;

    public PipedOutputStream(PipedInputStream pipedInputStream) throws IOException {
        connect(pipedInputStream);
    }

    public PipedOutputStream() {
    }

    public synchronized void connect(PipedInputStream pipedInputStream) throws IOException {
        if (pipedInputStream == null) {
            throw new NullPointerException();
        }
        if (this.sink != null || pipedInputStream.connected) {
            throw new IOException("Already connected");
        }
        this.sink = pipedInputStream;
        pipedInputStream.in = -1;
        pipedInputStream.out = 0;
        pipedInputStream.connected = true;
    }

    @Override // java.io.OutputStream
    public void write(int i2) throws IOException {
        if (this.sink == null) {
            throw new IOException("Pipe not connected");
        }
        this.sink.receive(i2);
    }

    @Override // java.io.OutputStream
    public void write(byte[] bArr, int i2, int i3) throws IOException {
        if (this.sink == null) {
            throw new IOException("Pipe not connected");
        }
        if (bArr == null) {
            throw new NullPointerException();
        }
        if (i2 < 0 || i2 > bArr.length || i3 < 0 || i2 + i3 > bArr.length || i2 + i3 < 0) {
            throw new IndexOutOfBoundsException();
        }
        if (i3 == 0) {
            return;
        }
        this.sink.receive(bArr, i2, i3);
    }

    @Override // java.io.OutputStream, java.io.Flushable
    public synchronized void flush() throws IOException {
        if (this.sink != null) {
            synchronized (this.sink) {
                this.sink.notifyAll();
            }
        }
    }

    @Override // java.io.OutputStream, java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        if (this.sink != null) {
            this.sink.receivedLast();
        }
    }
}
