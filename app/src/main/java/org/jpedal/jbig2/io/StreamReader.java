package org.jpedal.jbig2.io;

import java.io.IOException;
import org.jpedal.jbig2.examples.pdf.PDFSegment;

/* loaded from: icepdf-core.jar:org/jpedal/jbig2/io/StreamReader.class */
public class StreamReader {
    private byte[] data;
    private int bitPointer = 7;
    private int bytePointer = 0;

    public StreamReader(byte[] data) {
        this.data = data;
    }

    public short readByte(PDFSegment pdfSeg) {
        byte[] bArr = this.data;
        int i2 = this.bytePointer;
        this.bytePointer = i2 + 1;
        short bite = (short) (bArr[i2] & 255);
        if (pdfSeg != null) {
            pdfSeg.writeToHeader(bite);
        }
        return bite;
    }

    public void readByte(short[] buf, PDFSegment pdfSeg) throws IOException {
        for (int i2 = 0; i2 < buf.length; i2++) {
            byte[] bArr = this.data;
            int i3 = this.bytePointer;
            this.bytePointer = i3 + 1;
            buf[i2] = (short) (bArr[i3] & 255);
        }
        if (pdfSeg != null) {
            pdfSeg.writeToHeader(buf);
        }
    }

    public short readByte() {
        byte[] bArr = this.data;
        int i2 = this.bytePointer;
        this.bytePointer = i2 + 1;
        short bite = (short) (bArr[i2] & 255);
        return bite;
    }

    public void readByte(short[] buf) {
        for (int i2 = 0; i2 < buf.length; i2++) {
            byte[] bArr = this.data;
            int i3 = this.bytePointer;
            this.bytePointer = i3 + 1;
            buf[i2] = (short) (bArr[i3] & 255);
        }
    }

    public int readBit() {
        short buf = readByte();
        short mask = (short) (1 << this.bitPointer);
        int bit = (buf & mask) >> this.bitPointer;
        this.bitPointer--;
        if (this.bitPointer == -1) {
            this.bitPointer = 7;
        } else {
            movePointer(-1);
        }
        return bit;
    }

    public int readBits(int num) {
        int result = 0;
        for (int i2 = 0; i2 < num; i2++) {
            result = (result << 1) | readBit();
        }
        return result;
    }

    public void movePointer(int ammount) {
        this.bytePointer += ammount;
    }

    public void consumeRemainingBits() {
        if (this.bitPointer != 7) {
            readBits(this.bitPointer + 1);
        }
    }

    public boolean isFinished() {
        return this.bytePointer == this.data.length;
    }
}
