package sun.security.util;

import java.io.ByteArrayOutputStream;

/* loaded from: rt.jar:sun/security/util/BitArray.class */
public class BitArray {
    private byte[] repn;
    private int length;
    private static final int BITS_PER_UNIT = 8;
    private static final byte[][] NYBBLE = {new byte[]{48, 48, 48, 48}, new byte[]{48, 48, 48, 49}, new byte[]{48, 48, 49, 48}, new byte[]{48, 48, 49, 49}, new byte[]{48, 49, 48, 48}, new byte[]{48, 49, 48, 49}, new byte[]{48, 49, 49, 48}, new byte[]{48, 49, 49, 49}, new byte[]{49, 48, 48, 48}, new byte[]{49, 48, 48, 49}, new byte[]{49, 48, 49, 48}, new byte[]{49, 48, 49, 49}, new byte[]{49, 49, 48, 48}, new byte[]{49, 49, 48, 49}, new byte[]{49, 49, 49, 48}, new byte[]{49, 49, 49, 49}};
    private static final int BYTES_PER_LINE = 8;

    private static int subscript(int i2) {
        return i2 / 8;
    }

    private static int position(int i2) {
        return 1 << (7 - (i2 % 8));
    }

    public BitArray(int i2) throws IllegalArgumentException {
        if (i2 < 0) {
            throw new IllegalArgumentException("Negative length for BitArray");
        }
        this.length = i2;
        this.repn = new byte[((i2 + 8) - 1) / 8];
    }

    public BitArray(int i2, byte[] bArr) throws IllegalArgumentException {
        this(i2, bArr, 0);
    }

    public BitArray(int i2, byte[] bArr, int i3) throws IllegalArgumentException {
        if (i2 < 0) {
            throw new IllegalArgumentException("Negative length for BitArray");
        }
        if ((bArr.length - i3) * 8 < i2) {
            throw new IllegalArgumentException("Byte array too short to represent " + i2 + "-bit array");
        }
        this.length = i2;
        int i4 = ((i2 + 8) - 1) / 8;
        byte b2 = (byte) (255 << ((i4 * 8) - i2));
        this.repn = new byte[i4];
        System.arraycopy(bArr, i3, this.repn, 0, i4);
        if (i4 > 0) {
            byte[] bArr2 = this.repn;
            int i5 = i4 - 1;
            bArr2[i5] = (byte) (bArr2[i5] & b2);
        }
    }

    public BitArray(boolean[] zArr) throws ArrayIndexOutOfBoundsException {
        this.length = zArr.length;
        this.repn = new byte[(this.length + 7) / 8];
        for (int i2 = 0; i2 < this.length; i2++) {
            set(i2, zArr[i2]);
        }
    }

    private BitArray(BitArray bitArray) {
        this.length = bitArray.length;
        this.repn = (byte[]) bitArray.repn.clone();
    }

    public boolean get(int i2) throws ArrayIndexOutOfBoundsException {
        if (i2 < 0 || i2 >= this.length) {
            throw new ArrayIndexOutOfBoundsException(Integer.toString(i2));
        }
        return (this.repn[subscript(i2)] & position(i2)) != 0;
    }

    public void set(int i2, boolean z2) throws ArrayIndexOutOfBoundsException {
        if (i2 < 0 || i2 >= this.length) {
            throw new ArrayIndexOutOfBoundsException(Integer.toString(i2));
        }
        int iSubscript = subscript(i2);
        int iPosition = position(i2);
        if (z2) {
            byte[] bArr = this.repn;
            bArr[iSubscript] = (byte) (bArr[iSubscript] | iPosition);
        } else {
            byte[] bArr2 = this.repn;
            bArr2[iSubscript] = (byte) (bArr2[iSubscript] & (iPosition ^ (-1)));
        }
    }

    public int length() {
        return this.length;
    }

    public byte[] toByteArray() {
        return (byte[]) this.repn.clone();
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj == null || !(obj instanceof BitArray)) {
            return false;
        }
        BitArray bitArray = (BitArray) obj;
        if (bitArray.length != this.length) {
            return false;
        }
        for (int i2 = 0; i2 < this.repn.length; i2++) {
            if (this.repn[i2] != bitArray.repn[i2]) {
                return false;
            }
        }
        return true;
    }

    public boolean[] toBooleanArray() {
        boolean[] zArr = new boolean[this.length];
        for (int i2 = 0; i2 < this.length; i2++) {
            zArr[i2] = get(i2);
        }
        return zArr;
    }

    public int hashCode() {
        int i2 = 0;
        for (int i3 = 0; i3 < this.repn.length; i3++) {
            i2 = (31 * i2) + this.repn[i3];
        }
        return i2 ^ this.length;
    }

    public Object clone() {
        return new BitArray(this);
    }

    public String toString() {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        for (int i2 = 0; i2 < this.repn.length - 1; i2++) {
            byteArrayOutputStream.write(NYBBLE[(this.repn[i2] >> 4) & 15], 0, 4);
            byteArrayOutputStream.write(NYBBLE[this.repn[i2] & 15], 0, 4);
            if (i2 % 8 == 7) {
                byteArrayOutputStream.write(10);
            } else {
                byteArrayOutputStream.write(32);
            }
        }
        for (int length = 8 * (this.repn.length - 1); length < this.length; length++) {
            byteArrayOutputStream.write(get(length) ? 49 : 48);
        }
        return new String(byteArrayOutputStream.toByteArray());
    }

    public BitArray truncate() {
        for (int i2 = this.length - 1; i2 >= 0; i2--) {
            if (get(i2)) {
                return new BitArray(i2 + 1, this.repn, 0);
            }
        }
        return new BitArray(1);
    }
}
