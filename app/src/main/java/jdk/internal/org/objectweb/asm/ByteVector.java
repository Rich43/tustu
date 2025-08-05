package jdk.internal.org.objectweb.asm;

/* loaded from: rt.jar:jdk/internal/org/objectweb/asm/ByteVector.class */
public class ByteVector {
    byte[] data;
    int length;

    public ByteVector() {
        this.data = new byte[64];
    }

    public ByteVector(int i2) {
        this.data = new byte[i2];
    }

    public ByteVector putByte(int i2) {
        int i3 = this.length;
        if (i3 + 1 > this.data.length) {
            enlarge(1);
        }
        this.data[i3] = (byte) i2;
        this.length = i3 + 1;
        return this;
    }

    ByteVector put11(int i2, int i3) {
        int i4 = this.length;
        if (i4 + 2 > this.data.length) {
            enlarge(2);
        }
        byte[] bArr = this.data;
        int i5 = i4 + 1;
        bArr[i4] = (byte) i2;
        bArr[i5] = (byte) i3;
        this.length = i5 + 1;
        return this;
    }

    public ByteVector putShort(int i2) {
        int i3 = this.length;
        if (i3 + 2 > this.data.length) {
            enlarge(2);
        }
        byte[] bArr = this.data;
        int i4 = i3 + 1;
        bArr[i3] = (byte) (i2 >>> 8);
        bArr[i4] = (byte) i2;
        this.length = i4 + 1;
        return this;
    }

    ByteVector put12(int i2, int i3) {
        int i4 = this.length;
        if (i4 + 3 > this.data.length) {
            enlarge(3);
        }
        byte[] bArr = this.data;
        int i5 = i4 + 1;
        bArr[i4] = (byte) i2;
        int i6 = i5 + 1;
        bArr[i5] = (byte) (i3 >>> 8);
        bArr[i6] = (byte) i3;
        this.length = i6 + 1;
        return this;
    }

    public ByteVector putInt(int i2) {
        int i3 = this.length;
        if (i3 + 4 > this.data.length) {
            enlarge(4);
        }
        byte[] bArr = this.data;
        int i4 = i3 + 1;
        bArr[i3] = (byte) (i2 >>> 24);
        int i5 = i4 + 1;
        bArr[i4] = (byte) (i2 >>> 16);
        int i6 = i5 + 1;
        bArr[i5] = (byte) (i2 >>> 8);
        bArr[i6] = (byte) i2;
        this.length = i6 + 1;
        return this;
    }

    public ByteVector putLong(long j2) {
        int i2 = this.length;
        if (i2 + 8 > this.data.length) {
            enlarge(8);
        }
        byte[] bArr = this.data;
        int i3 = (int) (j2 >>> 32);
        int i4 = i2 + 1;
        bArr[i2] = (byte) (i3 >>> 24);
        int i5 = i4 + 1;
        bArr[i4] = (byte) (i3 >>> 16);
        int i6 = i5 + 1;
        bArr[i5] = (byte) (i3 >>> 8);
        int i7 = i6 + 1;
        bArr[i6] = (byte) i3;
        int i8 = (int) j2;
        int i9 = i7 + 1;
        bArr[i7] = (byte) (i8 >>> 24);
        int i10 = i9 + 1;
        bArr[i9] = (byte) (i8 >>> 16);
        int i11 = i10 + 1;
        bArr[i10] = (byte) (i8 >>> 8);
        bArr[i11] = (byte) i8;
        this.length = i11 + 1;
        return this;
    }

    public ByteVector putUTF8(String str) {
        int length = str.length();
        if (length > 65535) {
            throw new IllegalArgumentException();
        }
        int i2 = this.length;
        if (i2 + 2 + length > this.data.length) {
            enlarge(2 + length);
        }
        byte[] bArr = this.data;
        int i3 = i2 + 1;
        bArr[i2] = (byte) (length >>> 8);
        int i4 = i3 + 1;
        bArr[i3] = (byte) length;
        for (int i5 = 0; i5 < length; i5++) {
            char cCharAt = str.charAt(i5);
            if (cCharAt >= 1 && cCharAt <= 127) {
                int i6 = i4;
                i4++;
                bArr[i6] = (byte) cCharAt;
            } else {
                this.length = i4;
                return encodeUTF8(str, i5, 65535);
            }
        }
        this.length = i4;
        return this;
    }

    ByteVector encodeUTF8(String str, int i2, int i3) {
        int length = str.length();
        int i4 = i2;
        for (int i5 = i2; i5 < length; i5++) {
            char cCharAt = str.charAt(i5);
            if (cCharAt >= 1 && cCharAt <= 127) {
                i4++;
            } else if (cCharAt > 2047) {
                i4 += 3;
            } else {
                i4 += 2;
            }
        }
        if (i4 > i3) {
            throw new IllegalArgumentException();
        }
        int i6 = (this.length - i2) - 2;
        if (i6 >= 0) {
            this.data[i6] = (byte) (i4 >>> 8);
            this.data[i6 + 1] = (byte) i4;
        }
        if ((this.length + i4) - i2 > this.data.length) {
            enlarge(i4 - i2);
        }
        int i7 = this.length;
        for (int i8 = i2; i8 < length; i8++) {
            char cCharAt2 = str.charAt(i8);
            if (cCharAt2 >= 1 && cCharAt2 <= 127) {
                int i9 = i7;
                i7++;
                this.data[i9] = (byte) cCharAt2;
            } else if (cCharAt2 > 2047) {
                int i10 = i7;
                int i11 = i7 + 1;
                this.data[i10] = (byte) (224 | ((cCharAt2 >> '\f') & 15));
                int i12 = i11 + 1;
                this.data[i11] = (byte) (128 | ((cCharAt2 >> 6) & 63));
                i7 = i12 + 1;
                this.data[i12] = (byte) (128 | (cCharAt2 & '?'));
            } else {
                int i13 = i7;
                int i14 = i7 + 1;
                this.data[i13] = (byte) (192 | ((cCharAt2 >> 6) & 31));
                i7 = i14 + 1;
                this.data[i14] = (byte) (128 | (cCharAt2 & '?'));
            }
        }
        this.length = i7;
        return this;
    }

    public ByteVector putByteArray(byte[] bArr, int i2, int i3) {
        if (this.length + i3 > this.data.length) {
            enlarge(i3);
        }
        if (bArr != null) {
            System.arraycopy(bArr, i2, this.data, this.length, i3);
        }
        this.length += i3;
        return this;
    }

    private void enlarge(int i2) {
        int length = 2 * this.data.length;
        int i3 = this.length + i2;
        byte[] bArr = new byte[length > i3 ? length : i3];
        System.arraycopy(this.data, 0, bArr, 0, this.length);
        this.data = bArr;
    }
}
