package sun.net.www.http;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

/* loaded from: rt.jar:sun/net/www/http/HttpCaptureInputStream.class */
public class HttpCaptureInputStream extends FilterInputStream {
    private HttpCapture capture;

    public HttpCaptureInputStream(InputStream inputStream, HttpCapture httpCapture) {
        super(inputStream);
        this.capture = null;
        this.capture = httpCapture;
    }

    @Override // java.io.FilterInputStream, java.io.InputStream
    public int read() throws IOException {
        int i2 = super.read();
        this.capture.received(i2);
        return i2;
    }

    @Override // java.io.FilterInputStream, java.io.InputStream, java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        try {
            this.capture.flush();
        } catch (IOException e2) {
        }
        super.close();
    }

    @Override // java.io.FilterInputStream, java.io.InputStream
    public int read(byte[] bArr) throws IOException {
        int i2 = super.read(bArr);
        for (int i3 = 0; i3 < i2; i3++) {
            this.capture.received(bArr[i3]);
        }
        return i2;
    }

    @Override // java.io.FilterInputStream, java.io.InputStream
    public int read(byte[] bArr, int i2, int i3) throws IOException {
        int i4 = super.read(bArr, i2, i3);
        for (int i5 = 0; i5 < i4; i5++) {
            this.capture.received(bArr[i2 + i5]);
        }
        return i4;
    }
}
