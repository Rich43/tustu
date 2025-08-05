package java.io;

/* loaded from: rt.jar:java/io/PipedWriter.class */
public class PipedWriter extends Writer {
    private PipedReader sink;
    private boolean closed = false;

    public PipedWriter(PipedReader pipedReader) throws IOException {
        connect(pipedReader);
    }

    public PipedWriter() {
    }

    public synchronized void connect(PipedReader pipedReader) throws IOException {
        if (pipedReader == null) {
            throw new NullPointerException();
        }
        if (this.sink != null || pipedReader.connected) {
            throw new IOException("Already connected");
        }
        if (pipedReader.closedByReader || this.closed) {
            throw new IOException("Pipe closed");
        }
        this.sink = pipedReader;
        pipedReader.in = -1;
        pipedReader.out = 0;
        pipedReader.connected = true;
    }

    @Override // java.io.Writer
    public void write(int i2) throws IOException {
        if (this.sink == null) {
            throw new IOException("Pipe not connected");
        }
        this.sink.receive(i2);
    }

    @Override // java.io.Writer
    public void write(char[] cArr, int i2, int i3) throws IOException {
        if (this.sink == null) {
            throw new IOException("Pipe not connected");
        }
        if ((i2 | i3 | (i2 + i3) | (cArr.length - (i2 + i3))) < 0) {
            throw new IndexOutOfBoundsException();
        }
        this.sink.receive(cArr, i2, i3);
    }

    @Override // java.io.Writer, java.io.Flushable
    public synchronized void flush() throws IOException {
        if (this.sink != null) {
            if (this.sink.closedByReader || this.closed) {
                throw new IOException("Pipe closed");
            }
            synchronized (this.sink) {
                this.sink.notifyAll();
            }
        }
    }

    @Override // java.io.Writer, java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        this.closed = true;
        if (this.sink != null) {
            this.sink.receivedLast();
        }
    }
}
