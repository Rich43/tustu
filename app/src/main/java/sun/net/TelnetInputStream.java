package sun.net;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

/* loaded from: rt.jar:sun/net/TelnetInputStream.class */
public class TelnetInputStream extends FilterInputStream {
    boolean stickyCRLF;
    boolean seenCR;
    public boolean binaryMode;

    public TelnetInputStream(InputStream inputStream, boolean z2) {
        super(inputStream);
        this.stickyCRLF = false;
        this.seenCR = false;
        this.binaryMode = false;
        this.binaryMode = z2;
    }

    public void setStickyCRLF(boolean z2) {
        this.stickyCRLF = z2;
    }

    @Override // java.io.FilterInputStream, java.io.InputStream
    public int read() throws IOException {
        if (this.binaryMode) {
            return super.read();
        }
        if (this.seenCR) {
            this.seenCR = false;
            return 10;
        }
        int i2 = super.read();
        if (i2 == 13) {
            switch (super.read()) {
                case -1:
                default:
                    throw new TelnetProtocolException("misplaced CR in input");
                case 0:
                    return 13;
                case 10:
                    if (this.stickyCRLF) {
                        this.seenCR = true;
                        return 13;
                    }
                    return 10;
            }
        }
        return i2;
    }

    @Override // java.io.FilterInputStream, java.io.InputStream
    public int read(byte[] bArr) throws IOException {
        return read(bArr, 0, bArr.length);
    }

    @Override // java.io.FilterInputStream, java.io.InputStream
    public int read(byte[] bArr, int i2, int i3) throws IOException {
        int i4;
        if (this.binaryMode) {
            return super.read(bArr, i2, i3);
        }
        while (true) {
            i3--;
            if (i3 < 0 || (i4 = read()) == -1) {
                break;
            }
            int i5 = i2;
            i2++;
            bArr[i5] = (byte) i4;
        }
        if (i2 > i2) {
            return i2 - i2;
        }
        return -1;
    }
}
