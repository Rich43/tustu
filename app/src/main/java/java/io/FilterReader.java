package java.io;

/* loaded from: rt.jar:java/io/FilterReader.class */
public abstract class FilterReader extends Reader {
    protected Reader in;

    protected FilterReader(Reader reader) {
        super(reader);
        this.in = reader;
    }

    @Override // java.io.Reader
    public int read() throws IOException {
        return this.in.read();
    }

    @Override // java.io.Reader
    public int read(char[] cArr, int i2, int i3) throws IOException {
        return this.in.read(cArr, i2, i3);
    }

    @Override // java.io.Reader
    public long skip(long j2) throws IOException {
        return this.in.skip(j2);
    }

    @Override // java.io.Reader
    public boolean ready() throws IOException {
        return this.in.ready();
    }

    @Override // java.io.Reader
    public boolean markSupported() {
        return this.in.markSupported();
    }

    @Override // java.io.Reader
    public void mark(int i2) throws IOException {
        this.in.mark(i2);
    }

    @Override // java.io.Reader
    public void reset() throws IOException {
        this.in.reset();
    }

    @Override // java.io.Reader, java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        this.in.close();
    }
}
