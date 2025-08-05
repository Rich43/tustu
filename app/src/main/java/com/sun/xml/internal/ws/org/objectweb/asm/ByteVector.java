package com.sun.xml.internal.ws.org.objectweb.asm;

/* loaded from: rt.jar:com/sun/xml/internal/ws/org/objectweb/asm/ByteVector.class */
public class ByteVector {
    byte[] data;
    int length;

    public ByteVector() {
        this.data = new byte[64];
    }

    public ByteVector(int initialSize) {
        this.data = new byte[initialSize];
    }

    public ByteVector putByte(int b2) {
        int length = this.length;
        if (length + 1 > this.data.length) {
            enlarge(1);
        }
        this.data[length] = (byte) b2;
        this.length = length + 1;
        return this;
    }

    ByteVector put11(int b1, int b2) {
        int length = this.length;
        if (length + 2 > this.data.length) {
            enlarge(2);
        }
        byte[] data = this.data;
        int length2 = length + 1;
        data[length] = (byte) b1;
        data[length2] = (byte) b2;
        this.length = length2 + 1;
        return this;
    }

    public ByteVector putShort(int s2) {
        int length = this.length;
        if (length + 2 > this.data.length) {
            enlarge(2);
        }
        byte[] data = this.data;
        int length2 = length + 1;
        data[length] = (byte) (s2 >>> 8);
        data[length2] = (byte) s2;
        this.length = length2 + 1;
        return this;
    }

    ByteVector put12(int b2, int s2) {
        int length = this.length;
        if (length + 3 > this.data.length) {
            enlarge(3);
        }
        byte[] data = this.data;
        int length2 = length + 1;
        data[length] = (byte) b2;
        int length3 = length2 + 1;
        data[length2] = (byte) (s2 >>> 8);
        data[length3] = (byte) s2;
        this.length = length3 + 1;
        return this;
    }

    public ByteVector putInt(int i2) {
        int length = this.length;
        if (length + 4 > this.data.length) {
            enlarge(4);
        }
        byte[] data = this.data;
        int length2 = length + 1;
        data[length] = (byte) (i2 >>> 24);
        int length3 = length2 + 1;
        data[length2] = (byte) (i2 >>> 16);
        int length4 = length3 + 1;
        data[length3] = (byte) (i2 >>> 8);
        data[length4] = (byte) i2;
        this.length = length4 + 1;
        return this;
    }

    public ByteVector putLong(long l2) {
        int length = this.length;
        if (length + 8 > this.data.length) {
            enlarge(8);
        }
        byte[] data = this.data;
        int i2 = (int) (l2 >>> 32);
        int length2 = length + 1;
        data[length] = (byte) (i2 >>> 24);
        int length3 = length2 + 1;
        data[length2] = (byte) (i2 >>> 16);
        int length4 = length3 + 1;
        data[length3] = (byte) (i2 >>> 8);
        int length5 = length4 + 1;
        data[length4] = (byte) i2;
        int i3 = (int) l2;
        int length6 = length5 + 1;
        data[length5] = (byte) (i3 >>> 24);
        int length7 = length6 + 1;
        data[length6] = (byte) (i3 >>> 16);
        int length8 = length7 + 1;
        data[length7] = (byte) (i3 >>> 8);
        data[length8] = (byte) i3;
        this.length = length8 + 1;
        return this;
    }

    public ByteVector putUTF8(String s2) {
        int charLength = s2.length();
        if (this.length + 2 + charLength > this.data.length) {
            enlarge(2 + charLength);
        }
        int len = this.length;
        byte[] data = this.data;
        int len2 = len + 1;
        data[len] = (byte) (charLength >>> 8);
        int len3 = len2 + 1;
        data[len2] = (byte) charLength;
        for (int i2 = 0; i2 < charLength; i2++) {
            char c2 = s2.charAt(i2);
            if (c2 >= 1 && c2 <= 127) {
                int i3 = len3;
                len3++;
                data[i3] = (byte) c2;
            } else {
                int byteLength = i2;
                for (int j2 = i2; j2 < charLength; j2++) {
                    char c3 = s2.charAt(j2);
                    if (c3 >= 1 && c3 <= 127) {
                        byteLength++;
                    } else if (c3 > 2047) {
                        byteLength += 3;
                    } else {
                        byteLength += 2;
                    }
                }
                data[this.length] = (byte) (byteLength >>> 8);
                data[this.length + 1] = (byte) byteLength;
                if (this.length + 2 + byteLength > data.length) {
                    this.length = len3;
                    enlarge(2 + byteLength);
                    data = this.data;
                }
                for (int j3 = i2; j3 < charLength; j3++) {
                    char c4 = s2.charAt(j3);
                    if (c4 >= 1 && c4 <= 127) {
                        int i4 = len3;
                        len3++;
                        data[i4] = (byte) c4;
                    } else if (c4 > 2047) {
                        int i5 = len3;
                        int len4 = len3 + 1;
                        data[i5] = (byte) (224 | ((c4 >> '\f') & 15));
                        int len5 = len4 + 1;
                        data[len4] = (byte) (128 | ((c4 >> 6) & 63));
                        len3 = len5 + 1;
                        data[len5] = (byte) (128 | (c4 & '?'));
                    } else {
                        int i6 = len3;
                        int len6 = len3 + 1;
                        data[i6] = (byte) (192 | ((c4 >> 6) & 31));
                        len3 = len6 + 1;
                        data[len6] = (byte) (128 | (c4 & '?'));
                    }
                }
                this.length = len3;
                return this;
            }
        }
        this.length = len3;
        return this;
    }

    public ByteVector putByteArray(byte[] b2, int off, int len) {
        if (this.length + len > this.data.length) {
            enlarge(len);
        }
        if (b2 != null) {
            System.arraycopy(b2, off, this.data, this.length, len);
        }
        this.length += len;
        return this;
    }

    private void enlarge(int size) {
        int length1 = 2 * this.data.length;
        int length2 = this.length + size;
        byte[] newData = new byte[length1 > length2 ? length1 : length2];
        System.arraycopy(this.data, 0, newData, 0, this.length);
        this.data = newData;
    }
}
