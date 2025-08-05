package java.io;

/* loaded from: rt.jar:java/io/BufferedOutputStream.class */
public class BufferedOutputStream extends FilterOutputStream {
    protected byte[] buf;
    protected int count;

    public BufferedOutputStream(OutputStream outputStream) {
        this(outputStream, 8192);
    }

    public BufferedOutputStream(OutputStream outputStream, int i2) {
        super(outputStream);
        if (i2 <= 0) {
            throw new IllegalArgumentException("Buffer size <= 0");
        }
        this.buf = new byte[i2];
    }

    private void flushBuffer() throws IOException {
        if (this.count > 0) {
            this.out.write(this.buf, 0, this.count);
            this.count = 0;
        }
    }

    @Override // java.io.FilterOutputStream, java.io.OutputStream
    public synchronized void write(int i2) throws IOException {
        if (this.count >= this.buf.length) {
            flushBuffer();
        }
        byte[] bArr = this.buf;
        int i3 = this.count;
        this.count = i3 + 1;
        bArr[i3] = (byte) i2;
    }

    @Override // java.io.FilterOutputStream, java.io.OutputStream
    public synchronized void write(byte[] bArr, int i2, int i3) throws IOException {
        if (i3 >= this.buf.length) {
            flushBuffer();
            this.out.write(bArr, i2, i3);
        } else {
            if (i3 > this.buf.length - this.count) {
                flushBuffer();
            }
            System.arraycopy(bArr, i2, this.buf, this.count, i3);
            this.count += i3;
        }
    }

    @Override // java.io.FilterOutputStream, java.io.OutputStream, java.io.Flushable
    public synchronized void flush() throws IOException {
        flushBuffer();
        this.out.flush();
    }
}
