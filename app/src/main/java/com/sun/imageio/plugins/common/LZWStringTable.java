package com.sun.imageio.plugins.common;

import java.io.PrintStream;

/* loaded from: rt.jar:com/sun/imageio/plugins/common/LZWStringTable.class */
public class LZWStringTable {
    private static final int RES_CODES = 2;
    private static final short HASH_FREE = -1;
    private static final short NEXT_FIRST = -1;
    private static final int MAXBITS = 12;
    private static final int MAXSTR = 4096;
    private static final short HASHSIZE = 9973;
    private static final short HASHSTEP = 2039;
    short numStrings;
    byte[] strChr = new byte[4096];
    short[] strNxt = new short[4096];
    int[] strLen = new int[4096];
    short[] strHsh = new short[HASHSIZE];

    public int addCharString(short s2, byte b2) {
        int i2;
        if (this.numStrings >= 4096) {
            return 65535;
        }
        int iHash = hash(s2, b2);
        while (true) {
            i2 = iHash;
            if (this.strHsh[i2] == -1) {
                break;
            }
            iHash = (i2 + 2039) % HASHSIZE;
        }
        this.strHsh[i2] = this.numStrings;
        this.strChr[this.numStrings] = b2;
        if (s2 == -1) {
            this.strNxt[this.numStrings] = -1;
            this.strLen[this.numStrings] = 1;
        } else {
            this.strNxt[this.numStrings] = s2;
            this.strLen[this.numStrings] = this.strLen[s2] + 1;
        }
        short s3 = this.numStrings;
        this.numStrings = (short) (s3 + 1);
        return s3;
    }

    public short findCharString(short s2, byte b2) {
        if (s2 == -1) {
            return (short) (b2 & 255);
        }
        int iHash = hash(s2, b2);
        while (true) {
            int i2 = iHash;
            short s3 = this.strHsh[i2];
            if (s3 != -1) {
                if (this.strNxt[s3] == s2 && this.strChr[s3] == b2) {
                    return s3;
                }
                iHash = (i2 + 2039) % HASHSIZE;
            } else {
                return (short) -1;
            }
        }
    }

    public void clearTable(int i2) {
        this.numStrings = (short) 0;
        for (int i3 = 0; i3 < HASHSIZE; i3++) {
            this.strHsh[i3] = -1;
        }
        int i4 = (1 << i2) + 2;
        for (int i5 = 0; i5 < i4; i5++) {
            addCharString((short) -1, (byte) i5);
        }
    }

    public static int hash(short s2, byte b2) {
        return ((((short) (b2 << 8)) ^ s2) & 65535) % HASHSIZE;
    }

    public int expandCode(byte[] bArr, int i2, short s2, int i3) {
        int i4;
        if (i2 == -2 && i3 == 1) {
            i3 = 0;
        }
        if (s2 == -1 || i3 == this.strLen[s2]) {
            return 0;
        }
        int i5 = this.strLen[s2] - i3;
        int length = bArr.length - i2;
        if (length > i5) {
            i4 = i5;
        } else {
            i4 = length;
        }
        int i6 = i5 - i4;
        int i7 = i2 + i4;
        while (i7 > i2 && s2 != -1) {
            i6--;
            if (i6 < 0) {
                i7--;
                bArr[i7] = this.strChr[s2];
            }
            s2 = this.strNxt[s2];
        }
        if (i5 > i4) {
            return -i4;
        }
        return i4;
    }

    public void dump(PrintStream printStream) {
        for (int i2 = 258; i2 < this.numStrings; i2++) {
            printStream.println(" strNxt[" + i2 + "] = " + ((int) this.strNxt[i2]) + " strChr " + Integer.toHexString(this.strChr[i2] & 255) + " strLen " + Integer.toHexString(this.strLen[i2]));
        }
    }
}
