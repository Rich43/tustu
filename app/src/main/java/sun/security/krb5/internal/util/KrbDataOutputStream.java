package sun.security.krb5.internal.util;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import sun.text.normalizer.NormalizerImpl;

/* loaded from: rt.jar:sun/security/krb5/internal/util/KrbDataOutputStream.class */
public class KrbDataOutputStream extends BufferedOutputStream {
    public KrbDataOutputStream(OutputStream outputStream) {
        super(outputStream);
    }

    public void write32(int i2) throws IOException {
        write(new byte[]{(byte) (((i2 & (-16777216)) >> 24) & 255), (byte) (((i2 & 16711680) >> 16) & 255), (byte) (((i2 & NormalizerImpl.CC_MASK) >> 8) & 255), (byte) (i2 & 255)}, 0, 4);
    }

    public void write16(int i2) throws IOException {
        write(new byte[]{(byte) (((i2 & NormalizerImpl.CC_MASK) >> 8) & 255), (byte) (i2 & 255)}, 0, 2);
    }

    public void write8(int i2) throws IOException {
        write(i2 & 255);
    }
}
