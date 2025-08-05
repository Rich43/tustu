package sun.security.ssl;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

/* loaded from: jsse.jar:sun/security/ssl/HandshakeOutStream.class */
public class HandshakeOutStream extends ByteArrayOutputStream {
    OutputRecord outputRecord;

    HandshakeOutStream(OutputRecord outputRecord) {
        this.outputRecord = outputRecord;
    }

    void complete() throws IOException {
        if (size() < 4) {
            throw new RuntimeException("handshake message is not available");
        }
        if (this.outputRecord != null) {
            if (!this.outputRecord.isClosed()) {
                this.outputRecord.encodeHandshake(this.buf, 0, this.count);
            } else if (SSLLogger.isOn && SSLLogger.isOn("ssl")) {
                SSLLogger.warning("outbound has closed, ignore outbound handshake messages", ByteBuffer.wrap(this.buf, 0, this.count));
            }
            reset();
        }
    }

    @Override // java.io.ByteArrayOutputStream, java.io.OutputStream
    public void write(byte[] bArr, int i2, int i3) {
        checkOverflow(i3, 16777216);
        super.write(bArr, i2, i3);
    }

    @Override // java.io.OutputStream, java.io.Flushable
    public void flush() throws IOException {
        if (this.outputRecord != null) {
            this.outputRecord.flush();
        }
    }

    void putInt8(int i2) throws IOException {
        checkOverflow(i2, 256);
        super.write(i2);
    }

    void putInt16(int i2) throws IOException {
        checkOverflow(i2, 65536);
        super.write(i2 >> 8);
        super.write(i2);
    }

    void putInt24(int i2) throws IOException {
        checkOverflow(i2, 16777216);
        super.write(i2 >> 16);
        super.write(i2 >> 8);
        super.write(i2);
    }

    void putInt32(int i2) throws IOException {
        super.write(i2 >> 24);
        super.write(i2 >> 16);
        super.write(i2 >> 8);
        super.write(i2);
    }

    void putBytes8(byte[] bArr) throws IOException {
        if (bArr == null) {
            putInt8(0);
        } else {
            putInt8(bArr.length);
            super.write(bArr, 0, bArr.length);
        }
    }

    public void putBytes16(byte[] bArr) throws IOException {
        if (bArr == null) {
            putInt16(0);
        } else {
            putInt16(bArr.length);
            super.write(bArr, 0, bArr.length);
        }
    }

    void putBytes24(byte[] bArr) throws IOException {
        if (bArr == null) {
            putInt24(0);
        } else {
            putInt24(bArr.length);
            super.write(bArr, 0, bArr.length);
        }
    }

    private static void checkOverflow(int i2, int i3) {
        if (i2 >= i3) {
            throw new RuntimeException("Field length overflow, the field length (" + i2 + ") should be less than " + i3);
        }
    }
}
