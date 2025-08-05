package net.lingala.zip4j.crypto.engine;

/* loaded from: zip4j_1.3.1.jar:net/lingala/zip4j/crypto/engine/ZipCryptoEngine.class */
public class ZipCryptoEngine {
    private final int[] keys = new int[3];
    private static final int[] CRC_TABLE = new int[256];

    static {
        int i2;
        for (int i3 = 0; i3 < 256; i3++) {
            int r2 = i3;
            for (int j2 = 0; j2 < 8; j2++) {
                if ((r2 & 1) == 1) {
                    i2 = (r2 >>> 1) ^ (-306674912);
                } else {
                    i2 = r2 >>> 1;
                }
                r2 = i2;
            }
            CRC_TABLE[i3] = r2;
        }
    }

    public void initKeys(char[] password) {
        this.keys[0] = 305419896;
        this.keys[1] = 591751049;
        this.keys[2] = 878082192;
        for (char c2 : password) {
            updateKeys((byte) (c2 & 255));
        }
    }

    public void updateKeys(byte charAt) {
        this.keys[0] = crc32(this.keys[0], charAt);
        int[] iArr = this.keys;
        iArr[1] = iArr[1] + (this.keys[0] & 255);
        this.keys[1] = (this.keys[1] * 134775813) + 1;
        this.keys[2] = crc32(this.keys[2], (byte) (this.keys[1] >> 24));
    }

    private int crc32(int oldCrc, byte charAt) {
        return (oldCrc >>> 8) ^ CRC_TABLE[(oldCrc ^ charAt) & 255];
    }

    public byte decryptByte() {
        int temp = this.keys[2] | 2;
        return (byte) ((temp * (temp ^ 1)) >>> 8);
    }
}
