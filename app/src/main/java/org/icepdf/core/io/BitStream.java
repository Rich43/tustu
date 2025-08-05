package org.icepdf.core.io;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/* loaded from: icepdf-core.jar:org/icepdf/core/io/BitStream.class */
public class BitStream {
    InputStream in;
    OutputStream out;
    int bits = 0;
    int bits_left = 0;
    boolean readEOF = false;
    private static final int[] masks = new int[32];

    static {
        for (int i2 = 0; i2 < 32; i2++) {
            masks[i2] = (1 << i2) - 1;
        }
    }

    public BitStream(InputStream i2) {
        this.in = i2;
    }

    public BitStream(OutputStream o2) {
        this.out = o2;
    }

    public void close() throws IOException {
        if (this.in != null) {
            this.in.close();
        }
        if (this.out != null) {
            this.out.flush();
            this.out.close();
        }
    }

    public int getBits(int i2) throws IOException {
        while (true) {
            if (this.bits_left >= i2) {
                break;
            }
            int r2 = this.in.read();
            if (r2 < 0) {
                this.readEOF = true;
                break;
            }
            this.bits <<= 8;
            this.bits |= r2 & 255;
            this.bits_left += 8;
        }
        this.bits_left -= i2;
        return (this.bits >> this.bits_left) & masks[i2];
    }

    public boolean atEndOfFile() {
        return this.readEOF && this.bits_left <= 0;
    }

    public void putBit(int i2) throws IOException {
        this.bits <<= 1;
        this.bits |= i2;
        this.bits_left++;
        if (this.bits_left == 8) {
            this.out.write(this.bits);
            this.bits = 0;
            this.bits_left = 0;
        }
    }

    public void putRunBits(int i2, int len) throws IOException {
        int j2 = len - 1;
        while (j2 >= 0) {
            if (this.bits_left != 0 || j2 < 8) {
                putBit(i2);
                j2--;
            } else {
                if (i2 == 0) {
                    this.out.write(0);
                } else {
                    this.out.write(255);
                }
                j2 -= 8;
            }
        }
    }

    public int available() throws IOException {
        if (this.bits_left == 0 && this.in.available() <= 0) {
            return 0;
        }
        return 1;
    }

    public void skipByte() throws IOException {
        this.bits_left = 0;
        this.bits = 0;
    }
}
