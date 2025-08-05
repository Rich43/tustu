package sun.net;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/* loaded from: rt.jar:sun/net/TelnetOutputStream.class */
public class TelnetOutputStream extends BufferedOutputStream {
    boolean stickyCRLF;
    boolean seenCR;
    public boolean binaryMode;

    public TelnetOutputStream(OutputStream outputStream, boolean z2) {
        super(outputStream);
        this.stickyCRLF = false;
        this.seenCR = false;
        this.binaryMode = false;
        this.binaryMode = z2;
    }

    public void setStickyCRLF(boolean z2) {
        this.stickyCRLF = z2;
    }

    @Override // java.io.BufferedOutputStream, java.io.FilterOutputStream, java.io.OutputStream
    public void write(int i2) throws IOException {
        if (this.binaryMode) {
            super.write(i2);
            return;
        }
        if (this.seenCR) {
            if (i2 != 10) {
                super.write(0);
            }
            super.write(i2);
            if (i2 != 13) {
                this.seenCR = false;
                return;
            }
            return;
        }
        if (i2 == 10) {
            super.write(13);
            super.write(10);
            return;
        }
        if (i2 == 13) {
            if (this.stickyCRLF) {
                this.seenCR = true;
            } else {
                super.write(13);
                i2 = 0;
            }
        }
        super.write(i2);
    }

    @Override // java.io.BufferedOutputStream, java.io.FilterOutputStream, java.io.OutputStream
    public void write(byte[] bArr, int i2, int i3) throws IOException {
        if (this.binaryMode) {
            super.write(bArr, i2, i3);
            return;
        }
        while (true) {
            i3--;
            if (i3 >= 0) {
                int i4 = i2;
                i2++;
                write(bArr[i4]);
            } else {
                return;
            }
        }
    }
}
