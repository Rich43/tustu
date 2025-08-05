package sun.net.www.http;

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/* loaded from: rt.jar:sun/net/www/http/HttpCaptureOutputStream.class */
public class HttpCaptureOutputStream extends FilterOutputStream {
    private HttpCapture capture;

    public HttpCaptureOutputStream(OutputStream outputStream, HttpCapture httpCapture) {
        super(outputStream);
        this.capture = null;
        this.capture = httpCapture;
    }

    @Override // java.io.FilterOutputStream, java.io.OutputStream
    public void write(int i2) throws IOException {
        this.capture.sent(i2);
        this.out.write(i2);
    }

    @Override // java.io.FilterOutputStream, java.io.OutputStream
    public void write(byte[] bArr) throws IOException {
        for (byte b2 : bArr) {
            this.capture.sent(b2);
        }
        this.out.write(bArr);
    }

    @Override // java.io.FilterOutputStream, java.io.OutputStream
    public void write(byte[] bArr, int i2, int i3) throws IOException {
        for (int i4 = i2; i4 < i3; i4++) {
            this.capture.sent(bArr[i4]);
        }
        this.out.write(bArr, i2, i3);
    }

    @Override // java.io.FilterOutputStream, java.io.OutputStream, java.io.Flushable
    public void flush() throws IOException {
        try {
            this.capture.flush();
        } catch (IOException e2) {
        }
        super.flush();
    }
}
