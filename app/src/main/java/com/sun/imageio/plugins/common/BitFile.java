package com.sun.imageio.plugins.common;

import java.io.IOException;
import javax.imageio.stream.ImageOutputStream;

/* loaded from: rt.jar:com/sun/imageio/plugins/common/BitFile.class */
public class BitFile {
    ImageOutputStream output;
    byte[] buffer = new byte[256];
    int index = 0;
    int bitsLeft = 8;
    boolean blocks;

    public BitFile(ImageOutputStream imageOutputStream, boolean z2) {
        this.blocks = false;
        this.output = imageOutputStream;
        this.blocks = z2;
    }

    public void flush() throws IOException {
        int i2 = this.index + (this.bitsLeft == 8 ? 0 : 1);
        if (i2 > 0) {
            if (this.blocks) {
                this.output.write(i2);
            }
            this.output.write(this.buffer, 0, i2);
            this.buffer[0] = 0;
            this.index = 0;
            this.bitsLeft = 8;
        }
    }

    public void writeBits(int i2, int i3) throws IOException {
        int i4 = 0;
        do {
            if ((this.index == 254 && this.bitsLeft == 0) || this.index > 254) {
                if (this.blocks) {
                    this.output.write(255);
                }
                this.output.write(this.buffer, 0, 255);
                this.buffer[0] = 0;
                this.index = 0;
                this.bitsLeft = 8;
            }
            if (i3 <= this.bitsLeft) {
                if (this.blocks) {
                    byte[] bArr = this.buffer;
                    int i5 = this.index;
                    bArr[i5] = (byte) (bArr[i5] | ((i2 & ((1 << i3) - 1)) << (8 - this.bitsLeft)));
                    i4 += i3;
                    this.bitsLeft -= i3;
                    i3 = 0;
                } else {
                    byte[] bArr2 = this.buffer;
                    int i6 = this.index;
                    bArr2[i6] = (byte) (bArr2[i6] | ((i2 & ((1 << i3) - 1)) << (this.bitsLeft - i3)));
                    i4 += i3;
                    this.bitsLeft -= i3;
                    i3 = 0;
                }
            } else if (this.blocks) {
                byte[] bArr3 = this.buffer;
                int i7 = this.index;
                bArr3[i7] = (byte) (bArr3[i7] | ((i2 & ((1 << this.bitsLeft) - 1)) << (8 - this.bitsLeft)));
                i4 += this.bitsLeft;
                i2 >>= this.bitsLeft;
                i3 -= this.bitsLeft;
                byte[] bArr4 = this.buffer;
                int i8 = this.index + 1;
                this.index = i8;
                bArr4[i8] = 0;
                this.bitsLeft = 8;
            } else {
                int i9 = (i2 >>> (i3 - this.bitsLeft)) & ((1 << this.bitsLeft) - 1);
                byte[] bArr5 = this.buffer;
                int i10 = this.index;
                bArr5[i10] = (byte) (bArr5[i10] | i9);
                i3 -= this.bitsLeft;
                i4 += this.bitsLeft;
                byte[] bArr6 = this.buffer;
                int i11 = this.index + 1;
                this.index = i11;
                bArr6[i11] = 0;
                this.bitsLeft = 8;
            }
        } while (i3 != 0);
    }
}
