package java.io;

import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import sun.nio.cs.StreamEncoder;

/* loaded from: rt.jar:java/io/OutputStreamWriter.class */
public class OutputStreamWriter extends Writer {
    private final StreamEncoder se;

    public OutputStreamWriter(OutputStream outputStream, String str) throws UnsupportedEncodingException {
        super(outputStream);
        if (str == null) {
            throw new NullPointerException("charsetName");
        }
        this.se = StreamEncoder.forOutputStreamWriter(outputStream, this, str);
    }

    public OutputStreamWriter(OutputStream outputStream) {
        super(outputStream);
        try {
            this.se = StreamEncoder.forOutputStreamWriter(outputStream, this, (String) null);
        } catch (UnsupportedEncodingException e2) {
            throw new Error(e2);
        }
    }

    public OutputStreamWriter(OutputStream outputStream, Charset charset) {
        super(outputStream);
        if (charset == null) {
            throw new NullPointerException("charset");
        }
        this.se = StreamEncoder.forOutputStreamWriter(outputStream, this, charset);
    }

    public OutputStreamWriter(OutputStream outputStream, CharsetEncoder charsetEncoder) {
        super(outputStream);
        if (charsetEncoder == null) {
            throw new NullPointerException("charset encoder");
        }
        this.se = StreamEncoder.forOutputStreamWriter(outputStream, this, charsetEncoder);
    }

    public String getEncoding() {
        return this.se.getEncoding();
    }

    void flushBuffer() throws IOException {
        this.se.flushBuffer();
    }

    @Override // java.io.Writer
    public void write(int i2) throws IOException {
        this.se.write(i2);
    }

    @Override // java.io.Writer
    public void write(char[] cArr, int i2, int i3) throws IOException {
        this.se.write(cArr, i2, i3);
    }

    @Override // java.io.Writer
    public void write(String str, int i2, int i3) throws IOException {
        this.se.write(str, i2, i3);
    }

    @Override // java.io.Writer, java.io.Flushable
    public void flush() throws IOException {
        this.se.flush();
    }

    @Override // java.io.Writer, java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        this.se.close();
    }
}
