package net.lingala.zip4j.util;

import java.io.DataInput;
import java.io.IOException;
import net.lingala.zip4j.exception.ZipException;

/* loaded from: zip4j_1.3.1.jar:net/lingala/zip4j/util/Raw.class */
public class Raw {
    public static long readLongLittleEndian(byte[] array, int pos) {
        long temp = 0 | (array[pos + 7] & 255);
        return (((((((((((((temp << 8) | (array[pos + 6] & 255)) << 8) | (array[pos + 5] & 255)) << 8) | (array[pos + 4] & 255)) << 8) | (array[pos + 3] & 255)) << 8) | (array[pos + 2] & 255)) << 8) | (array[pos + 1] & 255)) << 8) | (array[pos] & 255);
    }

    public static int readLeInt(DataInput di, byte[] b2) throws ZipException {
        try {
            di.readFully(b2, 0, 4);
            return (b2[0] & 255) | ((b2[1] & 255) << 8) | (((b2[2] & 255) | ((b2[3] & 255) << 8)) << 16);
        } catch (IOException e2) {
            throw new ZipException(e2);
        }
    }

    public static int readShortLittleEndian(byte[] b2, int off) {
        return (b2[off] & 255) | ((b2[off + 1] & 255) << 8);
    }

    public static final short readShortBigEndian(byte[] array, int pos) {
        short temp = (short) (0 | (array[pos] & 255));
        return (short) (((short) (temp << 8)) | (array[pos + 1] & 255));
    }

    public static int readIntLittleEndian(byte[] b2, int off) {
        return (b2[off] & 255) | ((b2[off + 1] & 255) << 8) | (((b2[off + 2] & 255) | ((b2[off + 3] & 255) << 8)) << 16);
    }

    public static byte[] toByteArray(int in, int outSize) {
        byte[] out = new byte[outSize];
        byte[] intArray = toByteArray(in);
        for (int i2 = 0; i2 < intArray.length && i2 < outSize; i2++) {
            out[i2] = intArray[i2];
        }
        return out;
    }

    public static byte[] toByteArray(int in) {
        byte[] out = {(byte) in, (byte) (in >> 8), (byte) (in >> 16), (byte) (in >> 24)};
        return out;
    }

    public static final void writeShortLittleEndian(byte[] array, int pos, short value) {
        array[pos + 1] = (byte) (value >>> 8);
        array[pos] = (byte) (value & 255);
    }

    public static final void writeIntLittleEndian(byte[] array, int pos, int value) {
        array[pos + 3] = (byte) (value >>> 24);
        array[pos + 2] = (byte) (value >>> 16);
        array[pos + 1] = (byte) (value >>> 8);
        array[pos] = (byte) (value & 255);
    }

    public static void writeLongLittleEndian(byte[] array, int pos, long value) {
        array[pos + 7] = (byte) (value >>> 56);
        array[pos + 6] = (byte) (value >>> 48);
        array[pos + 5] = (byte) (value >>> 40);
        array[pos + 4] = (byte) (value >>> 32);
        array[pos + 3] = (byte) (value >>> 24);
        array[pos + 2] = (byte) (value >>> 16);
        array[pos + 1] = (byte) (value >>> 8);
        array[pos] = (byte) (value & 255);
    }

    public static byte bitArrayToByte(int[] bitArray) throws ZipException {
        if (bitArray == null) {
            throw new ZipException("bit array is null, cannot calculate byte from bits");
        }
        if (bitArray.length != 8) {
            throw new ZipException("invalid bit array length, cannot calculate byte");
        }
        if (!checkBits(bitArray)) {
            throw new ZipException("invalid bits provided, bits contain other values than 0 or 1");
        }
        int retNum = 0;
        for (int i2 = 0; i2 < bitArray.length; i2++) {
            retNum = (int) (retNum + (Math.pow(2.0d, i2) * bitArray[i2]));
        }
        return (byte) retNum;
    }

    private static boolean checkBits(int[] bitArray) {
        for (int i2 = 0; i2 < bitArray.length; i2++) {
            if (bitArray[i2] != 0 && bitArray[i2] != 1) {
                return false;
            }
        }
        return true;
    }

    public static void prepareBuffAESIVBytes(byte[] buff, int nonce, int length) {
        buff[0] = (byte) nonce;
        buff[1] = (byte) (nonce >> 8);
        buff[2] = (byte) (nonce >> 16);
        buff[3] = (byte) (nonce >> 24);
        buff[4] = 0;
        buff[5] = 0;
        buff[6] = 0;
        buff[7] = 0;
        buff[8] = 0;
        buff[9] = 0;
        buff[10] = 0;
        buff[11] = 0;
        buff[12] = 0;
        buff[13] = 0;
        buff[14] = 0;
        buff[15] = 0;
    }

    public static byte[] convertCharArrayToByteArray(char[] charArray) {
        if (charArray == null) {
            throw new NullPointerException();
        }
        byte[] bytes = new byte[charArray.length];
        for (int i2 = 0; i2 < charArray.length; i2++) {
            bytes[i2] = (byte) charArray[i2];
        }
        return bytes;
    }
}
