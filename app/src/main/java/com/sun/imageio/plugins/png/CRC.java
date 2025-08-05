package com.sun.imageio.plugins.png;

/* compiled from: PNGImageWriter.java */
/* loaded from: rt.jar:com/sun/imageio/plugins/png/CRC.class */
class CRC {
    private static int[] crcTable = new int[256];
    private int crc = -1;

    static {
        int i2;
        for (int i3 = 0; i3 < 256; i3++) {
            int i4 = i3;
            for (int i5 = 0; i5 < 8; i5++) {
                if ((i4 & 1) == 1) {
                    i2 = (-306674912) ^ (i4 >>> 1);
                } else {
                    i2 = i4 >>> 1;
                }
                i4 = i2;
                crcTable[i3] = i4;
            }
        }
    }

    public void reset() {
        this.crc = -1;
    }

    public void update(byte[] bArr, int i2, int i3) {
        for (int i4 = 0; i4 < i3; i4++) {
            this.crc = crcTable[(this.crc ^ bArr[i2 + i4]) & 255] ^ (this.crc >>> 8);
        }
    }

    public void update(int i2) {
        this.crc = crcTable[(this.crc ^ i2) & 255] ^ (this.crc >>> 8);
    }

    public int getValue() {
        return this.crc ^ (-1);
    }
}
