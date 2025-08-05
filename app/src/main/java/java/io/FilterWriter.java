package java.io;

/* loaded from: rt.jar:java/io/FilterWriter.class */
public abstract class FilterWriter extends Writer {
    protected Writer out;

    protected FilterWriter(Writer writer) {
        super(writer);
        this.out = writer;
    }

    @Override // java.io.Writer
    public void write(int i2) throws IOException {
        this.out.write(i2);
    }

    @Override // java.io.Writer
    public void write(char[] cArr, int i2, int i3) throws IOException {
        this.out.write(cArr, i2, i3);
    }

    @Override // java.io.Writer
    public void write(String str, int i2, int i3) throws IOException {
        this.out.write(str, i2, i3);
    }

    @Override // java.io.Writer, java.io.Flushable
    public void flush() throws IOException {
        this.out.flush();
    }

    @Override // java.io.Writer, java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        this.out.close();
    }
}
