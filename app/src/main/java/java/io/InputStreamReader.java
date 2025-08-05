package java.io;

import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import sun.nio.cs.StreamDecoder;

/* loaded from: rt.jar:java/io/InputStreamReader.class */
public class InputStreamReader extends Reader {
    private final StreamDecoder sd;

    public InputStreamReader(InputStream inputStream) {
        super(inputStream);
        try {
            this.sd = StreamDecoder.forInputStreamReader(inputStream, this, (String) null);
        } catch (UnsupportedEncodingException e2) {
            throw new Error(e2);
        }
    }

    public InputStreamReader(InputStream inputStream, String str) throws UnsupportedEncodingException {
        super(inputStream);
        if (str == null) {
            throw new NullPointerException("charsetName");
        }
        this.sd = StreamDecoder.forInputStreamReader(inputStream, this, str);
    }

    public InputStreamReader(InputStream inputStream, Charset charset) {
        super(inputStream);
        if (charset == null) {
            throw new NullPointerException("charset");
        }
        this.sd = StreamDecoder.forInputStreamReader(inputStream, this, charset);
    }

    public InputStreamReader(InputStream inputStream, CharsetDecoder charsetDecoder) {
        super(inputStream);
        if (charsetDecoder == null) {
            throw new NullPointerException("charset decoder");
        }
        this.sd = StreamDecoder.forInputStreamReader(inputStream, this, charsetDecoder);
    }

    public String getEncoding() {
        return this.sd.getEncoding();
    }

    @Override // java.io.Reader
    public int read() throws IOException {
        return this.sd.read();
    }

    @Override // java.io.Reader
    public int read(char[] cArr, int i2, int i3) throws IOException {
        return this.sd.read(cArr, i2, i3);
    }

    @Override // java.io.Reader
    public boolean ready() throws IOException {
        return this.sd.ready();
    }

    @Override // java.io.Reader, java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        this.sd.close();
    }
}
