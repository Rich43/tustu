package sun.security.krb5.internal.util;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteOrder;

/* loaded from: rt.jar:sun/security/krb5/internal/util/KrbDataInputStream.class */
public class KrbDataInputStream extends BufferedInputStream {
    private boolean bigEndian;

    public void setNativeByteOrder() {
        if (ByteOrder.nativeOrder().equals(ByteOrder.BIG_ENDIAN)) {
            this.bigEndian = true;
        } else {
            this.bigEndian = false;
        }
    }

    public KrbDataInputStream(InputStream inputStream) {
        super(inputStream);
        this.bigEndian = true;
    }

    public final int readLength4() throws IOException {
        int i2 = read(4);
        if (i2 < 0) {
            throw new IOException("Invalid encoding");
        }
        return i2;
    }

    public int read(int i2) throws IOException {
        int i3;
        int i4;
        int i5;
        byte[] bArr = new byte[i2];
        if (read(bArr, 0, i2) != i2) {
            throw new IOException("Premature end of stream reached");
        }
        int i6 = 0;
        for (int i7 = 0; i7 < i2; i7++) {
            if (this.bigEndian) {
                i3 = i6;
                i4 = bArr[i7] & 255;
                i5 = (i2 - i7) - 1;
            } else {
                i3 = i6;
                i4 = bArr[i7] & 255;
                i5 = i7;
            }
            i6 = i3 | (i4 << (i5 * 8));
        }
        return i6;
    }

    public int readVersion() throws IOException {
        return ((read() & 255) << 8) | (read() & 255);
    }
}
