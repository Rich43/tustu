package com.sun.crypto.provider;

import com.sun.imageio.plugins.jpeg.JPEG;
import java.security.InvalidKeyException;
import org.apache.commons.net.telnet.TelnetCommand;

/* loaded from: sunjce_provider.jar:com/sun/crypto/provider/RC2Crypt.class */
final class RC2Crypt extends SymmetricCipher {
    private static final int[] PI_TABLE = {217, 120, TelnetCommand.GA, 196, 25, 221, 181, 237, 40, 233, 253, 121, 74, 160, 216, 157, 198, 126, 55, 131, 43, 118, 83, 142, 98, 76, 100, 136, 68, 139, 251, 162, 23, 154, 89, 245, 135, 179, 79, 19, 97, 69, 109, 141, 9, 129, 125, 50, 189, 143, 64, 235, 134, 183, 123, 11, 240, 149, 33, 34, 92, 107, 78, 130, 84, 214, 101, 147, 206, 96, 178, 28, 115, 86, 192, 20, 167, 140, 241, 220, 18, 117, 202, 31, 59, 190, 228, 209, 66, 61, 212, 48, 163, 60, 182, 38, 111, 191, 14, 218, 70, 105, 7, 87, 39, 242, 29, 155, 188, 148, 67, 3, 248, 17, 199, 246, 144, 239, 62, 231, 6, 195, 213, 47, 200, 102, 30, 215, 8, JPEG.APP8, 234, 222, 128, 82, 238, 247, 132, 170, 114, 172, 53, 77, 106, 42, 150, 26, 210, 113, 90, 21, 73, 116, 75, 159, 208, 94, 4, 24, 164, 236, 194, 224, 65, 110, 15, 81, 203, 204, 36, 145, 175, 80, 161, 244, 112, 57, 153, 124, 58, 133, 35, 184, 180, 122, 252, 2, 54, 91, 37, 85, 151, 49, 45, 93, 250, 152, 227, 138, 146, 174, 5, 223, 41, 16, 103, 108, 186, 201, 211, 0, 230, 207, 225, 158, 168, 44, 99, 22, 1, 63, 88, 226, 137, 169, 13, 56, 52, 27, 171, 51, 255, 176, 187, 72, 12, 95, 185, 177, 205, 46, 197, 243, 219, 71, 229, 165, 156, 119, 10, 166, 32, 104, 254, 127, 193, 173};
    private final int[] expandedKey = new int[64];
    private int effectiveKeyBits;

    RC2Crypt() {
    }

    @Override // com.sun.crypto.provider.SymmetricCipher
    int getBlockSize() {
        return 8;
    }

    int getEffectiveKeyBits() {
        return this.effectiveKeyBits;
    }

    void initEffectiveKeyBits(int i2) {
        this.effectiveKeyBits = i2;
    }

    static void checkKey(String str, int i2) throws InvalidKeyException {
        if (!str.equals("RC2")) {
            throw new InvalidKeyException("Key algorithm must be RC2");
        }
        if (i2 < 5 || i2 > 128) {
            throw new InvalidKeyException("RC2 key length must be between 40 and 1024 bit");
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.sun.crypto.provider.SymmetricCipher
    void init(boolean z2, String str, byte[] bArr) throws InvalidKeyException {
        int length = bArr.length;
        if (this.effectiveKeyBits == 0) {
            this.effectiveKeyBits = length << 3;
        }
        checkKey(str, length);
        byte[] bArr2 = new byte[128];
        System.arraycopy(bArr, 0, bArr2, 0, length);
        int i2 = bArr2[length - 1];
        for (int i3 = length; i3 < 128; i3++) {
            i2 = PI_TABLE[(i2 + bArr2[i3 - length]) & 255];
            bArr2[i3] = (byte) i2;
        }
        int i4 = (this.effectiveKeyBits + 7) >> 3;
        int i5 = PI_TABLE[bArr2[128 - i4] & (255 >> ((-this.effectiveKeyBits) & 7))];
        bArr2[128 - i4] = (byte) i5;
        for (int i6 = 127 - i4; i6 >= 0; i6--) {
            i5 = PI_TABLE[i5 ^ (bArr2[i6 + i4] & 255)];
            bArr2[i6] = (byte) i5;
        }
        int i7 = 0;
        int i8 = 0;
        while (i7 < 64) {
            this.expandedKey[i7] = (bArr2[i8] & 255) + ((bArr2[i8 + 1] & 255) << 8);
            i7++;
            i8 += 2;
        }
    }

    @Override // com.sun.crypto.provider.SymmetricCipher
    void encryptBlock(byte[] bArr, int i2, byte[] bArr2, int i3) {
        int i4 = (bArr[i2] & 255) + ((bArr[i2 + 1] & 255) << 8);
        int i5 = (bArr[i2 + 2] & 255) + ((bArr[i2 + 3] & 255) << 8);
        int i6 = (bArr[i2 + 4] & 255) + ((bArr[i2 + 5] & 255) << 8);
        int i7 = (bArr[i2 + 6] & 255) + ((bArr[i2 + 7] & 255) << 8);
        for (int i8 = 0; i8 < 20; i8 += 4) {
            int i9 = (i4 + this.expandedKey[i8] + (i7 & i6) + ((i7 ^ (-1)) & i5)) & 65535;
            i4 = (i9 << 1) | (i9 >>> 15);
            int i10 = (i5 + this.expandedKey[i8 + 1] + (i4 & i7) + ((i4 ^ (-1)) & i6)) & 65535;
            i5 = (i10 << 2) | (i10 >>> 14);
            int i11 = (i6 + this.expandedKey[i8 + 2] + (i5 & i4) + ((i5 ^ (-1)) & i7)) & 65535;
            i6 = (i11 << 3) | (i11 >>> 13);
            int i12 = (i7 + this.expandedKey[i8 + 3] + (i6 & i5) + ((i6 ^ (-1)) & i4)) & 65535;
            i7 = (i12 << 5) | (i12 >>> 11);
        }
        int i13 = i4 + this.expandedKey[i7 & 63];
        int i14 = i5 + this.expandedKey[i13 & 63];
        int i15 = i6 + this.expandedKey[i14 & 63];
        int i16 = i7 + this.expandedKey[i15 & 63];
        for (int i17 = 20; i17 < 44; i17 += 4) {
            int i18 = (i13 + this.expandedKey[i17] + (i16 & i15) + ((i16 ^ (-1)) & i14)) & 65535;
            i13 = (i18 << 1) | (i18 >>> 15);
            int i19 = (i14 + this.expandedKey[i17 + 1] + (i13 & i16) + ((i13 ^ (-1)) & i15)) & 65535;
            i14 = (i19 << 2) | (i19 >>> 14);
            int i20 = (i15 + this.expandedKey[i17 + 2] + (i14 & i13) + ((i14 ^ (-1)) & i16)) & 65535;
            i15 = (i20 << 3) | (i20 >>> 13);
            int i21 = (i16 + this.expandedKey[i17 + 3] + (i15 & i14) + ((i15 ^ (-1)) & i13)) & 65535;
            i16 = (i21 << 5) | (i21 >>> 11);
        }
        int i22 = i13 + this.expandedKey[i16 & 63];
        int i23 = i14 + this.expandedKey[i22 & 63];
        int i24 = i15 + this.expandedKey[i23 & 63];
        int i25 = i16 + this.expandedKey[i24 & 63];
        for (int i26 = 44; i26 < 64; i26 += 4) {
            int i27 = (i22 + this.expandedKey[i26] + (i25 & i24) + ((i25 ^ (-1)) & i23)) & 65535;
            i22 = (i27 << 1) | (i27 >>> 15);
            int i28 = (i23 + this.expandedKey[i26 + 1] + (i22 & i25) + ((i22 ^ (-1)) & i24)) & 65535;
            i23 = (i28 << 2) | (i28 >>> 14);
            int i29 = (i24 + this.expandedKey[i26 + 2] + (i23 & i22) + ((i23 ^ (-1)) & i25)) & 65535;
            i24 = (i29 << 3) | (i29 >>> 13);
            int i30 = (i25 + this.expandedKey[i26 + 3] + (i24 & i23) + ((i24 ^ (-1)) & i22)) & 65535;
            i25 = (i30 << 5) | (i30 >>> 11);
        }
        bArr2[i3] = (byte) i22;
        bArr2[i3 + 1] = (byte) (i22 >> 8);
        bArr2[i3 + 2] = (byte) i23;
        bArr2[i3 + 3] = (byte) (i23 >> 8);
        bArr2[i3 + 4] = (byte) i24;
        bArr2[i3 + 5] = (byte) (i24 >> 8);
        bArr2[i3 + 6] = (byte) i25;
        bArr2[i3 + 7] = (byte) (i25 >> 8);
    }

    @Override // com.sun.crypto.provider.SymmetricCipher
    void decryptBlock(byte[] bArr, int i2, byte[] bArr2, int i3) {
        int i4 = (bArr[i2] & 255) + ((bArr[i2 + 1] & 255) << 8);
        int i5 = (bArr[i2 + 2] & 255) + ((bArr[i2 + 3] & 255) << 8);
        int i6 = (bArr[i2 + 4] & 255) + ((bArr[i2 + 5] & 255) << 8);
        int i7 = (bArr[i2 + 6] & 255) + ((bArr[i2 + 7] & 255) << 8);
        for (int i8 = 64; i8 > 44; i8 -= 4) {
            i7 = ((((((i7 << 11) | (i7 >>> 5)) & 65535) - this.expandedKey[i8 - 1]) - (i6 & i5)) - ((i6 ^ (-1)) & i4)) & 65535;
            i6 = ((((((i6 << 13) | (i6 >>> 3)) & 65535) - this.expandedKey[i8 - 2]) - (i5 & i4)) - ((i5 ^ (-1)) & i7)) & 65535;
            i5 = ((((((i5 << 14) | (i5 >>> 2)) & 65535) - this.expandedKey[i8 - 3]) - (i4 & i7)) - ((i4 ^ (-1)) & i6)) & 65535;
            i4 = ((((((i4 << 15) | (i4 >>> 1)) & 65535) - this.expandedKey[i8 - 4]) - (i7 & i6)) - ((i7 ^ (-1)) & i5)) & 65535;
        }
        int i9 = (i7 - this.expandedKey[i6 & 63]) & 65535;
        int i10 = (i6 - this.expandedKey[i5 & 63]) & 65535;
        int i11 = (i5 - this.expandedKey[i4 & 63]) & 65535;
        int i12 = (i4 - this.expandedKey[i9 & 63]) & 65535;
        for (int i13 = 44; i13 > 20; i13 -= 4) {
            i9 = ((((((i9 << 11) | (i9 >>> 5)) & 65535) - this.expandedKey[i13 - 1]) - (i10 & i11)) - ((i10 ^ (-1)) & i12)) & 65535;
            i10 = ((((((i10 << 13) | (i10 >>> 3)) & 65535) - this.expandedKey[i13 - 2]) - (i11 & i12)) - ((i11 ^ (-1)) & i9)) & 65535;
            i11 = ((((((i11 << 14) | (i11 >>> 2)) & 65535) - this.expandedKey[i13 - 3]) - (i12 & i9)) - ((i12 ^ (-1)) & i10)) & 65535;
            i12 = ((((((i12 << 15) | (i12 >>> 1)) & 65535) - this.expandedKey[i13 - 4]) - (i9 & i10)) - ((i9 ^ (-1)) & i11)) & 65535;
        }
        int i14 = (i9 - this.expandedKey[i10 & 63]) & 65535;
        int i15 = (i10 - this.expandedKey[i11 & 63]) & 65535;
        int i16 = (i11 - this.expandedKey[i12 & 63]) & 65535;
        int i17 = (i12 - this.expandedKey[i14 & 63]) & 65535;
        for (int i18 = 20; i18 > 0; i18 -= 4) {
            i14 = ((((((i14 << 11) | (i14 >>> 5)) & 65535) - this.expandedKey[i18 - 1]) - (i15 & i16)) - ((i15 ^ (-1)) & i17)) & 65535;
            i15 = ((((((i15 << 13) | (i15 >>> 3)) & 65535) - this.expandedKey[i18 - 2]) - (i16 & i17)) - ((i16 ^ (-1)) & i14)) & 65535;
            i16 = ((((((i16 << 14) | (i16 >>> 2)) & 65535) - this.expandedKey[i18 - 3]) - (i17 & i14)) - ((i17 ^ (-1)) & i15)) & 65535;
            i17 = ((((((i17 << 15) | (i17 >>> 1)) & 65535) - this.expandedKey[i18 - 4]) - (i14 & i15)) - ((i14 ^ (-1)) & i16)) & 65535;
        }
        bArr2[i3] = (byte) i17;
        bArr2[i3 + 1] = (byte) (i17 >> 8);
        bArr2[i3 + 2] = (byte) i16;
        bArr2[i3 + 3] = (byte) (i16 >> 8);
        bArr2[i3 + 4] = (byte) i15;
        bArr2[i3 + 5] = (byte) (i15 >> 8);
        bArr2[i3 + 6] = (byte) i14;
        bArr2[i3 + 7] = (byte) (i14 >> 8);
    }
}
