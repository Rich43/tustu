package sun.security.jgss;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/* loaded from: rt.jar:sun/security/jgss/GSSToken.class */
public abstract class GSSToken {
    public static final void writeLittleEndian(int i2, byte[] bArr) {
        writeLittleEndian(i2, bArr, 0);
    }

    public static final void writeLittleEndian(int i2, byte[] bArr, int i3) {
        int i4 = i3 + 1;
        bArr[i3] = (byte) i2;
        int i5 = i4 + 1;
        bArr[i4] = (byte) (i2 >>> 8);
        int i6 = i5 + 1;
        bArr[i5] = (byte) (i2 >>> 16);
        int i7 = i6 + 1;
        bArr[i6] = (byte) (i2 >>> 24);
    }

    public static final void writeBigEndian(int i2, byte[] bArr) {
        writeBigEndian(i2, bArr, 0);
    }

    public static final void writeBigEndian(int i2, byte[] bArr, int i3) {
        int i4 = i3 + 1;
        bArr[i3] = (byte) (i2 >>> 24);
        int i5 = i4 + 1;
        bArr[i4] = (byte) (i2 >>> 16);
        int i6 = i5 + 1;
        bArr[i5] = (byte) (i2 >>> 8);
        int i7 = i6 + 1;
        bArr[i6] = (byte) i2;
    }

    public static final int readLittleEndian(byte[] bArr, int i2, int i3) {
        int i4 = 0;
        int i5 = 0;
        while (i3 > 0) {
            i4 += (bArr[i2] & 255) << i5;
            i5 += 8;
            i2++;
            i3--;
        }
        return i4;
    }

    public static final int readBigEndian(byte[] bArr, int i2, int i3) {
        int i4 = 0;
        int i5 = (i3 - 1) * 8;
        while (i3 > 0) {
            i4 += (bArr[i2] & 255) << i5;
            i5 -= 8;
            i2++;
            i3--;
        }
        return i4;
    }

    public static final void writeInt(int i2, OutputStream outputStream) throws IOException {
        outputStream.write(i2 >>> 8);
        outputStream.write(i2);
    }

    public static final int writeInt(int i2, byte[] bArr, int i3) {
        int i4 = i3 + 1;
        bArr[i3] = (byte) (i2 >>> 8);
        int i5 = i4 + 1;
        bArr[i4] = (byte) i2;
        return i5;
    }

    public static final int readInt(InputStream inputStream) throws IOException {
        return ((255 & inputStream.read()) << 8) | (255 & inputStream.read());
    }

    public static final int readInt(byte[] bArr, int i2) {
        return ((255 & bArr[i2]) << 8) | (255 & bArr[i2 + 1]);
    }

    public static final void readFully(InputStream inputStream, byte[] bArr) throws IOException {
        readFully(inputStream, bArr, 0, bArr.length);
    }

    public static final void readFully(InputStream inputStream, byte[] bArr, int i2, int i3) throws IOException {
        while (i3 > 0) {
            int i4 = inputStream.read(bArr, i2, i3);
            if (i4 == -1) {
                throw new EOFException("Cannot read all " + i3 + " bytes needed to form this token!");
            }
            i2 += i4;
            i3 -= i4;
        }
    }

    public static final void debug(String str) {
        System.err.print(str);
    }

    public static final String getHexBytes(byte[] bArr) {
        return getHexBytes(bArr, 0, bArr.length);
    }

    public static final String getHexBytes(byte[] bArr, int i2) {
        return getHexBytes(bArr, 0, i2);
    }

    public static final String getHexBytes(byte[] bArr, int i2, int i3) {
        StringBuffer stringBuffer = new StringBuffer();
        for (int i4 = i2; i4 < i2 + i3; i4++) {
            int i5 = (bArr[i4] >> 4) & 15;
            int i6 = bArr[i4] & 15;
            stringBuffer.append(Integer.toHexString(i5));
            stringBuffer.append(Integer.toHexString(i6));
            stringBuffer.append(' ');
        }
        return stringBuffer.toString();
    }
}
