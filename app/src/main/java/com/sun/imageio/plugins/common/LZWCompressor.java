package com.sun.imageio.plugins.common;

import java.io.IOException;
import java.io.PrintStream;
import javax.imageio.stream.ImageOutputStream;

/* loaded from: rt.jar:com/sun/imageio/plugins/common/LZWCompressor.class */
public class LZWCompressor {
    int codeSize;
    int clearCode;
    int endOfInfo;
    int numBits;
    int limit;
    short prefix;

    /* renamed from: bf, reason: collision with root package name */
    BitFile f11838bf;
    LZWStringTable lzss;
    boolean tiffFudge;

    public LZWCompressor(ImageOutputStream imageOutputStream, int i2, boolean z2) throws IOException {
        this.f11838bf = new BitFile(imageOutputStream, !z2);
        this.codeSize = i2;
        this.tiffFudge = z2;
        this.clearCode = 1 << i2;
        this.endOfInfo = this.clearCode + 1;
        this.numBits = i2 + 1;
        this.limit = (1 << this.numBits) - 1;
        if (this.tiffFudge) {
            this.limit--;
        }
        this.prefix = (short) -1;
        this.lzss = new LZWStringTable();
        this.lzss.clearTable(i2);
        this.f11838bf.writeBits(this.clearCode, this.numBits);
    }

    public void compress(byte[] bArr, int i2, int i3) throws IOException {
        int i4 = i2 + i3;
        for (int i5 = i2; i5 < i4; i5++) {
            byte b2 = bArr[i5];
            short sFindCharString = this.lzss.findCharString(this.prefix, b2);
            if (sFindCharString != -1) {
                this.prefix = sFindCharString;
            } else {
                this.f11838bf.writeBits(this.prefix, this.numBits);
                if (this.lzss.addCharString(this.prefix, b2) > this.limit) {
                    if (this.numBits == 12) {
                        this.f11838bf.writeBits(this.clearCode, this.numBits);
                        this.lzss.clearTable(this.codeSize);
                        this.numBits = this.codeSize + 1;
                    } else {
                        this.numBits++;
                    }
                    this.limit = (1 << this.numBits) - 1;
                    if (this.tiffFudge) {
                        this.limit--;
                    }
                }
                this.prefix = (short) (b2 & 255);
            }
        }
    }

    public void flush() throws IOException {
        if (this.prefix != -1) {
            this.f11838bf.writeBits(this.prefix, this.numBits);
        }
        this.f11838bf.writeBits(this.endOfInfo, this.numBits);
        this.f11838bf.flush();
    }

    public void dump(PrintStream printStream) {
        this.lzss.dump(printStream);
    }
}
