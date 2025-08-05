package javax.imageio.stream;

import java.io.IOException;
import java.io.UTFDataFormatException;
import java.nio.ByteOrder;

/* loaded from: rt.jar:javax/imageio/stream/ImageOutputStreamImpl.class */
public abstract class ImageOutputStreamImpl extends ImageInputStreamImpl implements ImageOutputStream {
    public abstract void write(int i2) throws IOException;

    public abstract void write(byte[] bArr, int i2, int i3) throws IOException;

    @Override // javax.imageio.stream.ImageOutputStream, java.io.DataOutput
    public void write(byte[] bArr) throws IOException {
        write(bArr, 0, bArr.length);
    }

    @Override // javax.imageio.stream.ImageOutputStream, java.io.DataOutput
    public void writeBoolean(boolean z2) throws IOException {
        write(z2 ? 1 : 0);
    }

    @Override // javax.imageio.stream.ImageOutputStream, java.io.DataOutput
    public void writeByte(int i2) throws IOException {
        write(i2);
    }

    @Override // javax.imageio.stream.ImageOutputStream, java.io.DataOutput
    public void writeShort(int i2) throws IOException {
        if (this.byteOrder == ByteOrder.BIG_ENDIAN) {
            this.byteBuf[0] = (byte) (i2 >>> 8);
            this.byteBuf[1] = (byte) (i2 >>> 0);
        } else {
            this.byteBuf[0] = (byte) (i2 >>> 0);
            this.byteBuf[1] = (byte) (i2 >>> 8);
        }
        write(this.byteBuf, 0, 2);
    }

    @Override // javax.imageio.stream.ImageOutputStream, java.io.DataOutput
    public void writeChar(int i2) throws IOException {
        writeShort(i2);
    }

    @Override // javax.imageio.stream.ImageOutputStream, java.io.DataOutput
    public void writeInt(int i2) throws IOException {
        if (this.byteOrder == ByteOrder.BIG_ENDIAN) {
            this.byteBuf[0] = (byte) (i2 >>> 24);
            this.byteBuf[1] = (byte) (i2 >>> 16);
            this.byteBuf[2] = (byte) (i2 >>> 8);
            this.byteBuf[3] = (byte) (i2 >>> 0);
        } else {
            this.byteBuf[0] = (byte) (i2 >>> 0);
            this.byteBuf[1] = (byte) (i2 >>> 8);
            this.byteBuf[2] = (byte) (i2 >>> 16);
            this.byteBuf[3] = (byte) (i2 >>> 24);
        }
        write(this.byteBuf, 0, 4);
    }

    @Override // javax.imageio.stream.ImageOutputStream, java.io.DataOutput
    public void writeLong(long j2) throws IOException {
        if (this.byteOrder == ByteOrder.BIG_ENDIAN) {
            this.byteBuf[0] = (byte) (j2 >>> 56);
            this.byteBuf[1] = (byte) (j2 >>> 48);
            this.byteBuf[2] = (byte) (j2 >>> 40);
            this.byteBuf[3] = (byte) (j2 >>> 32);
            this.byteBuf[4] = (byte) (j2 >>> 24);
            this.byteBuf[5] = (byte) (j2 >>> 16);
            this.byteBuf[6] = (byte) (j2 >>> 8);
            this.byteBuf[7] = (byte) (j2 >>> 0);
        } else {
            this.byteBuf[0] = (byte) (j2 >>> 0);
            this.byteBuf[1] = (byte) (j2 >>> 8);
            this.byteBuf[2] = (byte) (j2 >>> 16);
            this.byteBuf[3] = (byte) (j2 >>> 24);
            this.byteBuf[4] = (byte) (j2 >>> 32);
            this.byteBuf[5] = (byte) (j2 >>> 40);
            this.byteBuf[6] = (byte) (j2 >>> 48);
            this.byteBuf[7] = (byte) (j2 >>> 56);
        }
        write(this.byteBuf, 0, 4);
        write(this.byteBuf, 4, 4);
    }

    @Override // javax.imageio.stream.ImageOutputStream, java.io.DataOutput
    public void writeFloat(float f2) throws IOException {
        writeInt(Float.floatToIntBits(f2));
    }

    @Override // javax.imageio.stream.ImageOutputStream, java.io.DataOutput
    public void writeDouble(double d2) throws IOException {
        writeLong(Double.doubleToLongBits(d2));
    }

    @Override // javax.imageio.stream.ImageOutputStream, java.io.DataOutput
    public void writeBytes(String str) throws IOException {
        int length = str.length();
        for (int i2 = 0; i2 < length; i2++) {
            write((byte) str.charAt(i2));
        }
    }

    @Override // javax.imageio.stream.ImageOutputStream, java.io.DataOutput
    public void writeChars(String str) throws IOException {
        int length = str.length();
        byte[] bArr = new byte[length * 2];
        int i2 = 0;
        if (this.byteOrder == ByteOrder.BIG_ENDIAN) {
            for (int i3 = 0; i3 < length; i3++) {
                char cCharAt = str.charAt(i3);
                int i4 = i2;
                int i5 = i2 + 1;
                bArr[i4] = (byte) (cCharAt >>> '\b');
                i2 = i5 + 1;
                bArr[i5] = (byte) (cCharAt >>> 0);
            }
        } else {
            for (int i6 = 0; i6 < length; i6++) {
                char cCharAt2 = str.charAt(i6);
                int i7 = i2;
                int i8 = i2 + 1;
                bArr[i7] = (byte) (cCharAt2 >>> 0);
                i2 = i8 + 1;
                bArr[i8] = (byte) (cCharAt2 >>> '\b');
            }
        }
        write(bArr, 0, length * 2);
    }

    @Override // javax.imageio.stream.ImageOutputStream, java.io.DataOutput
    public void writeUTF(String str) throws IOException {
        int length = str.length();
        int i2 = 0;
        char[] cArr = new char[length];
        str.getChars(0, length, cArr, 0);
        for (int i3 = 0; i3 < length; i3++) {
            char c2 = cArr[i3];
            if (c2 >= 1 && c2 <= 127) {
                i2++;
            } else if (c2 > 2047) {
                i2 += 3;
            } else {
                i2 += 2;
            }
        }
        if (i2 > 65535) {
            throw new UTFDataFormatException("utflen > 65536!");
        }
        byte[] bArr = new byte[i2 + 2];
        int i4 = 0 + 1;
        bArr[0] = (byte) ((i2 >>> 8) & 255);
        int i5 = i4 + 1;
        bArr[i4] = (byte) ((i2 >>> 0) & 255);
        for (int i6 = 0; i6 < length; i6++) {
            char c3 = cArr[i6];
            if (c3 >= 1 && c3 <= 127) {
                int i7 = i5;
                i5++;
                bArr[i7] = (byte) c3;
            } else if (c3 > 2047) {
                int i8 = i5;
                int i9 = i5 + 1;
                bArr[i8] = (byte) (224 | ((c3 >> '\f') & 15));
                int i10 = i9 + 1;
                bArr[i9] = (byte) (128 | ((c3 >> 6) & 63));
                i5 = i10 + 1;
                bArr[i10] = (byte) (128 | ((c3 >> 0) & 63));
            } else {
                int i11 = i5;
                int i12 = i5 + 1;
                bArr[i11] = (byte) (192 | ((c3 >> 6) & 31));
                i5 = i12 + 1;
                bArr[i12] = (byte) (128 | ((c3 >> 0) & 63));
            }
        }
        write(bArr, 0, i2 + 2);
    }

    @Override // javax.imageio.stream.ImageOutputStream
    public void writeShorts(short[] sArr, int i2, int i3) throws IOException {
        if (i2 < 0 || i3 < 0 || i2 + i3 > sArr.length || i2 + i3 < 0) {
            throw new IndexOutOfBoundsException("off < 0 || len < 0 || off + len > s.length!");
        }
        byte[] bArr = new byte[i3 * 2];
        int i4 = 0;
        if (this.byteOrder == ByteOrder.BIG_ENDIAN) {
            for (int i5 = 0; i5 < i3; i5++) {
                short s2 = sArr[i2 + i5];
                int i6 = i4;
                int i7 = i4 + 1;
                bArr[i6] = (byte) (s2 >>> 8);
                i4 = i7 + 1;
                bArr[i7] = (byte) (s2 >>> 0);
            }
        } else {
            for (int i8 = 0; i8 < i3; i8++) {
                short s3 = sArr[i2 + i8];
                int i9 = i4;
                int i10 = i4 + 1;
                bArr[i9] = (byte) (s3 >>> 0);
                i4 = i10 + 1;
                bArr[i10] = (byte) (s3 >>> 8);
            }
        }
        write(bArr, 0, i3 * 2);
    }

    @Override // javax.imageio.stream.ImageOutputStream
    public void writeChars(char[] cArr, int i2, int i3) throws IOException {
        if (i2 < 0 || i3 < 0 || i2 + i3 > cArr.length || i2 + i3 < 0) {
            throw new IndexOutOfBoundsException("off < 0 || len < 0 || off + len > c.length!");
        }
        byte[] bArr = new byte[i3 * 2];
        int i4 = 0;
        if (this.byteOrder == ByteOrder.BIG_ENDIAN) {
            for (int i5 = 0; i5 < i3; i5++) {
                char c2 = cArr[i2 + i5];
                int i6 = i4;
                int i7 = i4 + 1;
                bArr[i6] = (byte) (c2 >>> '\b');
                i4 = i7 + 1;
                bArr[i7] = (byte) (c2 >>> 0);
            }
        } else {
            for (int i8 = 0; i8 < i3; i8++) {
                char c3 = cArr[i2 + i8];
                int i9 = i4;
                int i10 = i4 + 1;
                bArr[i9] = (byte) (c3 >>> 0);
                i4 = i10 + 1;
                bArr[i10] = (byte) (c3 >>> '\b');
            }
        }
        write(bArr, 0, i3 * 2);
    }

    @Override // javax.imageio.stream.ImageOutputStream
    public void writeInts(int[] iArr, int i2, int i3) throws IOException {
        if (i2 < 0 || i3 < 0 || i2 + i3 > iArr.length || i2 + i3 < 0) {
            throw new IndexOutOfBoundsException("off < 0 || len < 0 || off + len > i.length!");
        }
        byte[] bArr = new byte[i3 * 4];
        int i4 = 0;
        if (this.byteOrder == ByteOrder.BIG_ENDIAN) {
            for (int i5 = 0; i5 < i3; i5++) {
                int i6 = iArr[i2 + i5];
                int i7 = i4;
                int i8 = i4 + 1;
                bArr[i7] = (byte) (i6 >>> 24);
                int i9 = i8 + 1;
                bArr[i8] = (byte) (i6 >>> 16);
                int i10 = i9 + 1;
                bArr[i9] = (byte) (i6 >>> 8);
                i4 = i10 + 1;
                bArr[i10] = (byte) (i6 >>> 0);
            }
        } else {
            for (int i11 = 0; i11 < i3; i11++) {
                int i12 = iArr[i2 + i11];
                int i13 = i4;
                int i14 = i4 + 1;
                bArr[i13] = (byte) (i12 >>> 0);
                int i15 = i14 + 1;
                bArr[i14] = (byte) (i12 >>> 8);
                int i16 = i15 + 1;
                bArr[i15] = (byte) (i12 >>> 16);
                i4 = i16 + 1;
                bArr[i16] = (byte) (i12 >>> 24);
            }
        }
        write(bArr, 0, i3 * 4);
    }

    @Override // javax.imageio.stream.ImageOutputStream
    public void writeLongs(long[] jArr, int i2, int i3) throws IOException {
        if (i2 < 0 || i3 < 0 || i2 + i3 > jArr.length || i2 + i3 < 0) {
            throw new IndexOutOfBoundsException("off < 0 || len < 0 || off + len > l.length!");
        }
        byte[] bArr = new byte[i3 * 8];
        int i4 = 0;
        if (this.byteOrder == ByteOrder.BIG_ENDIAN) {
            for (int i5 = 0; i5 < i3; i5++) {
                long j2 = jArr[i2 + i5];
                int i6 = i4;
                int i7 = i4 + 1;
                bArr[i6] = (byte) (j2 >>> 56);
                int i8 = i7 + 1;
                bArr[i7] = (byte) (j2 >>> 48);
                int i9 = i8 + 1;
                bArr[i8] = (byte) (j2 >>> 40);
                int i10 = i9 + 1;
                bArr[i9] = (byte) (j2 >>> 32);
                int i11 = i10 + 1;
                bArr[i10] = (byte) (j2 >>> 24);
                int i12 = i11 + 1;
                bArr[i11] = (byte) (j2 >>> 16);
                int i13 = i12 + 1;
                bArr[i12] = (byte) (j2 >>> 8);
                i4 = i13 + 1;
                bArr[i13] = (byte) (j2 >>> 0);
            }
        } else {
            for (int i14 = 0; i14 < i3; i14++) {
                long j3 = jArr[i2 + i14];
                int i15 = i4;
                int i16 = i4 + 1;
                bArr[i15] = (byte) (j3 >>> 0);
                int i17 = i16 + 1;
                bArr[i16] = (byte) (j3 >>> 8);
                int i18 = i17 + 1;
                bArr[i17] = (byte) (j3 >>> 16);
                int i19 = i18 + 1;
                bArr[i18] = (byte) (j3 >>> 24);
                int i20 = i19 + 1;
                bArr[i19] = (byte) (j3 >>> 32);
                int i21 = i20 + 1;
                bArr[i20] = (byte) (j3 >>> 40);
                int i22 = i21 + 1;
                bArr[i21] = (byte) (j3 >>> 48);
                i4 = i22 + 1;
                bArr[i22] = (byte) (j3 >>> 56);
            }
        }
        write(bArr, 0, i3 * 8);
    }

    @Override // javax.imageio.stream.ImageOutputStream
    public void writeFloats(float[] fArr, int i2, int i3) throws IOException {
        if (i2 < 0 || i3 < 0 || i2 + i3 > fArr.length || i2 + i3 < 0) {
            throw new IndexOutOfBoundsException("off < 0 || len < 0 || off + len > f.length!");
        }
        byte[] bArr = new byte[i3 * 4];
        int i4 = 0;
        if (this.byteOrder == ByteOrder.BIG_ENDIAN) {
            for (int i5 = 0; i5 < i3; i5++) {
                int iFloatToIntBits = Float.floatToIntBits(fArr[i2 + i5]);
                int i6 = i4;
                int i7 = i4 + 1;
                bArr[i6] = (byte) (iFloatToIntBits >>> 24);
                int i8 = i7 + 1;
                bArr[i7] = (byte) (iFloatToIntBits >>> 16);
                int i9 = i8 + 1;
                bArr[i8] = (byte) (iFloatToIntBits >>> 8);
                i4 = i9 + 1;
                bArr[i9] = (byte) (iFloatToIntBits >>> 0);
            }
        } else {
            for (int i10 = 0; i10 < i3; i10++) {
                int iFloatToIntBits2 = Float.floatToIntBits(fArr[i2 + i10]);
                int i11 = i4;
                int i12 = i4 + 1;
                bArr[i11] = (byte) (iFloatToIntBits2 >>> 0);
                int i13 = i12 + 1;
                bArr[i12] = (byte) (iFloatToIntBits2 >>> 8);
                int i14 = i13 + 1;
                bArr[i13] = (byte) (iFloatToIntBits2 >>> 16);
                i4 = i14 + 1;
                bArr[i14] = (byte) (iFloatToIntBits2 >>> 24);
            }
        }
        write(bArr, 0, i3 * 4);
    }

    @Override // javax.imageio.stream.ImageOutputStream
    public void writeDoubles(double[] dArr, int i2, int i3) throws IOException {
        if (i2 < 0 || i3 < 0 || i2 + i3 > dArr.length || i2 + i3 < 0) {
            throw new IndexOutOfBoundsException("off < 0 || len < 0 || off + len > d.length!");
        }
        byte[] bArr = new byte[i3 * 8];
        int i4 = 0;
        if (this.byteOrder == ByteOrder.BIG_ENDIAN) {
            for (int i5 = 0; i5 < i3; i5++) {
                long jDoubleToLongBits = Double.doubleToLongBits(dArr[i2 + i5]);
                int i6 = i4;
                int i7 = i4 + 1;
                bArr[i6] = (byte) (jDoubleToLongBits >>> 56);
                int i8 = i7 + 1;
                bArr[i7] = (byte) (jDoubleToLongBits >>> 48);
                int i9 = i8 + 1;
                bArr[i8] = (byte) (jDoubleToLongBits >>> 40);
                int i10 = i9 + 1;
                bArr[i9] = (byte) (jDoubleToLongBits >>> 32);
                int i11 = i10 + 1;
                bArr[i10] = (byte) (jDoubleToLongBits >>> 24);
                int i12 = i11 + 1;
                bArr[i11] = (byte) (jDoubleToLongBits >>> 16);
                int i13 = i12 + 1;
                bArr[i12] = (byte) (jDoubleToLongBits >>> 8);
                i4 = i13 + 1;
                bArr[i13] = (byte) (jDoubleToLongBits >>> 0);
            }
        } else {
            for (int i14 = 0; i14 < i3; i14++) {
                long jDoubleToLongBits2 = Double.doubleToLongBits(dArr[i2 + i14]);
                int i15 = i4;
                int i16 = i4 + 1;
                bArr[i15] = (byte) (jDoubleToLongBits2 >>> 0);
                int i17 = i16 + 1;
                bArr[i16] = (byte) (jDoubleToLongBits2 >>> 8);
                int i18 = i17 + 1;
                bArr[i17] = (byte) (jDoubleToLongBits2 >>> 16);
                int i19 = i18 + 1;
                bArr[i18] = (byte) (jDoubleToLongBits2 >>> 24);
                int i20 = i19 + 1;
                bArr[i19] = (byte) (jDoubleToLongBits2 >>> 32);
                int i21 = i20 + 1;
                bArr[i20] = (byte) (jDoubleToLongBits2 >>> 40);
                int i22 = i21 + 1;
                bArr[i21] = (byte) (jDoubleToLongBits2 >>> 48);
                i4 = i22 + 1;
                bArr[i22] = (byte) (jDoubleToLongBits2 >>> 56);
            }
        }
        write(bArr, 0, i3 * 8);
    }

    @Override // javax.imageio.stream.ImageOutputStream
    public void writeBit(int i2) throws IOException {
        writeBits(1 & i2, 1);
    }

    @Override // javax.imageio.stream.ImageOutputStream
    public void writeBits(long j2, int i2) throws IOException {
        checkClosed();
        if (i2 < 0 || i2 > 64) {
            throw new IllegalArgumentException("Bad value for numBits!");
        }
        if (i2 == 0) {
            return;
        }
        if (getStreamPosition() > 0 || this.bitOffset > 0) {
            int i3 = this.bitOffset;
            int i4 = read();
            if (i4 != -1) {
                seek(getStreamPosition() - 1);
            } else {
                i4 = 0;
            }
            if (i2 + i3 < 8) {
                write((int) ((i4 & ((r0 << r0) ^ (-1))) | ((j2 & ((-1) >>> (32 - i2))) << (8 - (i3 + i2)))));
                seek(getStreamPosition() - 1);
                this.bitOffset = i3 + i2;
                i2 = 0;
            } else {
                int i5 = 8 - i3;
                write((int) ((i4 & (r0 ^ (-1))) | ((j2 >> (i2 - i5)) & ((-1) >>> (32 - i5)))));
                i2 -= i5;
            }
        }
        if (i2 > 7) {
            int i6 = i2 % 8;
            for (int i7 = i2 / 8; i7 > 0; i7--) {
                int i8 = ((i7 - 1) * 8) + i6;
                write((int) ((i8 == 0 ? j2 : j2 >> i8) & 255));
            }
            i2 = i6;
        }
        if (i2 != 0) {
            int i9 = read();
            if (i9 != -1) {
                seek(getStreamPosition() - 1);
            } else {
                i9 = 0;
            }
            write((int) ((i9 & ((r0 << r0) ^ (-1))) | ((j2 & ((-1) >>> (32 - i2))) << (8 - i2))));
            seek(getStreamPosition() - 1);
            this.bitOffset = i2;
        }
    }

    protected final void flushBits() throws IOException {
        int i2;
        checkClosed();
        if (this.bitOffset != 0) {
            int i3 = this.bitOffset;
            int i4 = read();
            if (i4 < 0) {
                i2 = 0;
                this.bitOffset = 0;
            } else {
                seek(getStreamPosition() - 1);
                i2 = i4 & ((-1) << (8 - i3));
            }
            write(i2);
        }
    }
}
