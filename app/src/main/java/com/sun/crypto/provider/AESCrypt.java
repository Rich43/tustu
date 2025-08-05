package com.sun.crypto.provider;

import java.security.InvalidKeyException;
import java.security.MessageDigest;

/* loaded from: sunjce_provider.jar:com/sun/crypto/provider/AESCrypt.class */
final class AESCrypt extends SymmetricCipher implements AESConstants {
    private boolean ROUNDS_12 = false;
    private boolean ROUNDS_14 = false;
    private int[][] sessionK = (int[][]) null;

    /* renamed from: K, reason: collision with root package name */
    private int[] f11803K = null;
    private byte[] lastKey = null;
    private int limit = 0;
    private static int[] alog;
    private static int[] log;

    /* renamed from: S, reason: collision with root package name */
    private static final byte[] f11804S = new byte[256];
    private static final byte[] Si = new byte[256];
    private static final int[] T1 = new int[256];
    private static final int[] T2 = new int[256];
    private static final int[] T3 = new int[256];
    private static final int[] T4 = new int[256];
    private static final int[] T5 = new int[256];
    private static final int[] T6 = new int[256];
    private static final int[] T7 = new int[256];
    private static final int[] T8 = new int[256];
    private static final int[] U1 = new int[256];
    private static final int[] U2 = new int[256];
    private static final int[] U3 = new int[256];
    private static final int[] U4 = new int[256];
    private static final byte[] rcon = new byte[30];

    AESCrypt() {
    }

    @Override // com.sun.crypto.provider.SymmetricCipher
    int getBlockSize() {
        return 16;
    }

    @Override // com.sun.crypto.provider.SymmetricCipher
    void init(boolean z2, String str, byte[] bArr) throws InvalidKeyException {
        if (!str.equalsIgnoreCase("AES") && !str.equalsIgnoreCase("Rijndael")) {
            throw new InvalidKeyException("Wrong algorithm: AES or Rijndael required");
        }
        if (!isKeySizeValid(bArr.length)) {
            throw new InvalidKeyException("Invalid AES key length: " + bArr.length + " bytes");
        }
        if (!MessageDigest.isEqual(bArr, this.lastKey)) {
            makeSessionKey(bArr);
            this.lastKey = (byte[]) bArr.clone();
        }
        this.f11803K = this.sessionK[z2 ? (char) 1 : (char) 0];
    }

    private static final int[] expandToSubKey(int[][] iArr, boolean z2) {
        int length = iArr.length;
        int[] iArr2 = new int[length * 4];
        if (z2) {
            for (int i2 = 0; i2 < 4; i2++) {
                iArr2[i2] = iArr[length - 1][i2];
            }
            for (int i3 = 1; i3 < length; i3++) {
                for (int i4 = 0; i4 < 4; i4++) {
                    iArr2[(i3 * 4) + i4] = iArr[i3 - 1][i4];
                }
            }
        } else {
            for (int i5 = 0; i5 < length; i5++) {
                for (int i6 = 0; i6 < 4; i6++) {
                    iArr2[(i5 * 4) + i6] = iArr[i5][i6];
                }
            }
        }
        return iArr2;
    }

    /* JADX WARN: Multi-variable type inference failed */
    static {
        alog = new int[256];
        log = new int[256];
        alog[0] = 1;
        for (int i2 = 1; i2 < 256; i2++) {
            int i3 = (alog[i2 - 1] << 1) ^ alog[i2 - 1];
            if ((i3 & 256) != 0) {
                i3 ^= 283;
            }
            alog[i2] = i3;
        }
        for (int i4 = 1; i4 < 255; i4++) {
            log[alog[i4]] = i4;
        }
        byte[] bArr = {new byte[]{1, 1, 1, 1, 1, 0, 0, 0}, new byte[]{0, 1, 1, 1, 1, 1, 0, 0}, new byte[]{0, 0, 1, 1, 1, 1, 1, 0}, new byte[]{0, 0, 0, 1, 1, 1, 1, 1}, new byte[]{1, 0, 0, 0, 1, 1, 1, 1}, new byte[]{1, 1, 0, 0, 0, 1, 1, 1}, new byte[]{1, 1, 1, 0, 0, 0, 1, 1}, new byte[]{1, 1, 1, 1, 0, 0, 0, 1}};
        byte[] bArr2 = {0, 1, 1, 0, 0, 0, 1, 1};
        byte[][] bArr3 = new byte[256][8];
        bArr3[1][7] = 1;
        for (int i5 = 2; i5 < 256; i5++) {
            int i6 = alog[255 - log[i5]];
            for (int i7 = 0; i7 < 8; i7++) {
                bArr3[i5][i7] = (byte) ((i6 >>> (7 - i7)) & 1);
            }
        }
        byte[][] bArr4 = new byte[256][8];
        for (int i8 = 0; i8 < 256; i8++) {
            for (int i9 = 0; i9 < 8; i9++) {
                bArr4[i8][i9] = bArr2[i9];
                for (int i10 = 0; i10 < 8; i10++) {
                    byte[] bArr5 = bArr4[i8];
                    int i11 = i9;
                    bArr5[i11] = (byte) (bArr5[i11] ^ ((bArr[i9][i10] == true ? 1 : 0) * bArr3[i8][i10]));
                }
            }
        }
        for (int i12 = 0; i12 < 256; i12++) {
            f11804S[i12] = (byte) (bArr4[i12][0] << 7);
            for (int i13 = 1; i13 < 8; i13++) {
                byte[] bArr6 = f11804S;
                int i14 = i12;
                bArr6[i14] = (byte) (bArr6[i14] ^ (bArr4[i12][i13] << (7 - i13)));
            }
            Si[f11804S[i12] & 255] = (byte) i12;
        }
        byte[] bArr7 = {new byte[]{2, 1, 1, 3}, new byte[]{3, 2, 1, 1}, new byte[]{1, 3, 2, 1}, new byte[]{1, 1, 3, 2}};
        byte[][] bArr8 = new byte[4][8];
        for (int i15 = 0; i15 < 4; i15++) {
            for (int i16 = 0; i16 < 4; i16++) {
                bArr8[i15][i16] = bArr7[i15][i16] == true ? 1 : 0;
            }
            bArr8[i15][i15 + 4] = 1;
        }
        byte[][] bArr9 = new byte[4][4];
        for (int i17 = 0; i17 < 4; i17++) {
            boolean z2 = bArr8[i17][i17] == true ? 1 : 0;
            boolean z3 = z2;
            if (!z2) {
                int i18 = i17 + 1;
                while (bArr8[i18][i17] == 0 && i18 < 4) {
                    i18++;
                }
                if (i18 == 4) {
                    throw new RuntimeException("G matrix is not invertible");
                }
                for (int i19 = 0; i19 < 8; i19++) {
                    byte b2 = bArr8[i17][i19];
                    bArr8[i17][i19] = bArr8[i18][i19] == true ? 1 : 0;
                    bArr8[i18][i19] = b2 == true ? 1 : 0;
                }
                z3 = bArr8[i17][i17] == true ? 1 : 0;
            }
            for (int i20 = 0; i20 < 8; i20++) {
                if (bArr8[i17][i20] != 0) {
                    bArr8[i17][i20] = (byte) alog[((255 + log[(bArr8[i17][i20] == true ? 1 : 0) & 255]) - log[(z3 ? 1 : 0) & 255]) % 255];
                }
            }
            for (int i21 = 0; i21 < 4; i21++) {
                if (i17 != i21) {
                    for (int i22 = i17 + 1; i22 < 8; i22++) {
                        byte[] bArr10 = bArr8[i21];
                        int i23 = i22;
                        bArr10[i23] = (byte) ((bArr10[i23] == true ? 1 : 0) ^ mul(bArr8[i17][i22] == true ? 1 : 0, bArr8[i21][i17] == true ? 1 : 0));
                    }
                    bArr8[i21][i17] = 0;
                }
            }
        }
        for (int i24 = 0; i24 < 4; i24++) {
            for (int i25 = 0; i25 < 4; i25++) {
                bArr9[i24][i25] = bArr8[i24][i25 + 4] == true ? 1 : 0;
            }
        }
        for (int i26 = 0; i26 < 256; i26++) {
            byte b3 = f11804S[i26];
            T1[i26] = mul4(b3, bArr7[0]);
            T2[i26] = mul4(b3, bArr7[1]);
            T3[i26] = mul4(b3, bArr7[2]);
            T4[i26] = mul4(b3, bArr7[3]);
            byte b4 = Si[i26];
            T5[i26] = mul4(b4, bArr9[0]);
            T6[i26] = mul4(b4, bArr9[1]);
            T7[i26] = mul4(b4, bArr9[2]);
            T8[i26] = mul4(b4, bArr9[3]);
            U1[i26] = mul4(i26, bArr9[0]);
            U2[i26] = mul4(i26, bArr9[1]);
            U3[i26] = mul4(i26, bArr9[2]);
            U4[i26] = mul4(i26, bArr9[3]);
        }
        rcon[0] = 1;
        int iMul = 1;
        for (int i27 = 1; i27 < 30; i27++) {
            iMul = mul(2, iMul);
            rcon[i27] = (byte) iMul;
        }
        log = null;
        alog = null;
    }

    private static final int mul(int i2, int i3) {
        if (i2 == 0 || i3 == 0) {
            return 0;
        }
        return alog[(log[i2 & 255] + log[i3 & 255]) % 255];
    }

    private static final int mul4(int i2, byte[] bArr) {
        if (i2 == 0) {
            return 0;
        }
        int i3 = log[i2 & 255];
        return ((bArr[0] != 0 ? alog[(i3 + log[bArr[0] & 255]) % 255] & 255 : 0) << 24) | ((bArr[1] != 0 ? alog[(i3 + log[bArr[1] & 255]) % 255] & 255 : 0) << 16) | ((bArr[2] != 0 ? alog[(i3 + log[bArr[2] & 255]) % 255] & 255 : 0) << 8) | (bArr[3] != 0 ? alog[(i3 + log[bArr[3] & 255]) % 255] & 255 : 0);
    }

    static final boolean isKeySizeValid(int i2) {
        for (int i3 = 0; i3 < AES_KEYSIZES.length; i3++) {
            if (i2 == AES_KEYSIZES[i3]) {
                return true;
            }
        }
        return false;
    }

    @Override // com.sun.crypto.provider.SymmetricCipher
    void encryptBlock(byte[] bArr, int i2, byte[] bArr2, int i3) {
        implEncryptBlock(bArr, i2, bArr2, i3);
    }

    private void implEncryptBlock(byte[] bArr, int i2, byte[] bArr2, int i3) {
        int i4 = i2 + 1;
        int i5 = bArr[i2] << 24;
        int i6 = i4 + 1;
        int i7 = i5 | ((bArr[i4] & 255) << 16);
        int i8 = i6 + 1;
        int i9 = i7 | ((bArr[i6] & 255) << 8);
        int i10 = i8 + 1;
        int i11 = 0 + 1;
        int i12 = (i9 | (bArr[i8] & 255)) ^ this.f11803K[0];
        int i13 = i10 + 1;
        int i14 = bArr[i10] << 24;
        int i15 = i13 + 1;
        int i16 = i14 | ((bArr[i13] & 255) << 16);
        int i17 = i15 + 1;
        int i18 = i16 | ((bArr[i15] & 255) << 8);
        int i19 = i17 + 1;
        int i20 = i11 + 1;
        int i21 = (i18 | (bArr[i17] & 255)) ^ this.f11803K[i11];
        int i22 = i19 + 1;
        int i23 = bArr[i19] << 24;
        int i24 = i22 + 1;
        int i25 = i23 | ((bArr[i22] & 255) << 16);
        int i26 = i24 + 1;
        int i27 = i25 | ((bArr[i24] & 255) << 8);
        int i28 = i26 + 1;
        int i29 = i20 + 1;
        int i30 = (i27 | (bArr[i26] & 255)) ^ this.f11803K[i20];
        int i31 = i28 + 1;
        int i32 = bArr[i28] << 24;
        int i33 = i31 + 1;
        int i34 = i32 | ((bArr[i31] & 255) << 16);
        int i35 = i33 + 1;
        int i36 = i34 | ((bArr[i33] & 255) << 8);
        int i37 = i35 + 1;
        int i38 = i29 + 1;
        int i39 = (i36 | (bArr[i35] & 255)) ^ this.f11803K[i29];
        while (i38 < this.limit) {
            int i40 = i38;
            int i41 = i38 + 1;
            int i42 = (((T1[i12 >>> 24] ^ T2[(i21 >>> 16) & 255]) ^ T3[(i30 >>> 8) & 255]) ^ T4[i39 & 255]) ^ this.f11803K[i40];
            int i43 = i41 + 1;
            int i44 = (((T1[i21 >>> 24] ^ T2[(i30 >>> 16) & 255]) ^ T3[(i39 >>> 8) & 255]) ^ T4[i12 & 255]) ^ this.f11803K[i41];
            int i45 = i43 + 1;
            int i46 = (((T1[i30 >>> 24] ^ T2[(i39 >>> 16) & 255]) ^ T3[(i12 >>> 8) & 255]) ^ T4[i21 & 255]) ^ this.f11803K[i43];
            i38 = i45 + 1;
            i39 = (((T1[i39 >>> 24] ^ T2[(i12 >>> 16) & 255]) ^ T3[(i21 >>> 8) & 255]) ^ T4[i30 & 255]) ^ this.f11803K[i45];
            i12 = i42;
            i21 = i44;
            i30 = i46;
        }
        int i47 = i38;
        int i48 = i38 + 1;
        int i49 = this.f11803K[i47];
        int i50 = i3 + 1;
        bArr2[i3] = (byte) (f11804S[i12 >>> 24] ^ (i49 >>> 24));
        int i51 = i50 + 1;
        bArr2[i50] = (byte) (f11804S[(i21 >>> 16) & 255] ^ (i49 >>> 16));
        int i52 = i51 + 1;
        bArr2[i51] = (byte) (f11804S[(i30 >>> 8) & 255] ^ (i49 >>> 8));
        int i53 = i52 + 1;
        bArr2[i52] = (byte) (f11804S[i39 & 255] ^ i49);
        int i54 = i48 + 1;
        int i55 = this.f11803K[i48];
        int i56 = i53 + 1;
        bArr2[i53] = (byte) (f11804S[i21 >>> 24] ^ (i55 >>> 24));
        int i57 = i56 + 1;
        bArr2[i56] = (byte) (f11804S[(i30 >>> 16) & 255] ^ (i55 >>> 16));
        int i58 = i57 + 1;
        bArr2[i57] = (byte) (f11804S[(i39 >>> 8) & 255] ^ (i55 >>> 8));
        int i59 = i58 + 1;
        bArr2[i58] = (byte) (f11804S[i12 & 255] ^ i55);
        int i60 = i54 + 1;
        int i61 = this.f11803K[i54];
        int i62 = i59 + 1;
        bArr2[i59] = (byte) (f11804S[i30 >>> 24] ^ (i61 >>> 24));
        int i63 = i62 + 1;
        bArr2[i62] = (byte) (f11804S[(i39 >>> 16) & 255] ^ (i61 >>> 16));
        int i64 = i63 + 1;
        bArr2[i63] = (byte) (f11804S[(i12 >>> 8) & 255] ^ (i61 >>> 8));
        int i65 = i64 + 1;
        bArr2[i64] = (byte) (f11804S[i21 & 255] ^ i61);
        int i66 = i60 + 1;
        int i67 = this.f11803K[i60];
        int i68 = i65 + 1;
        bArr2[i65] = (byte) (f11804S[i39 >>> 24] ^ (i67 >>> 24));
        int i69 = i68 + 1;
        bArr2[i68] = (byte) (f11804S[(i12 >>> 16) & 255] ^ (i67 >>> 16));
        bArr2[i69] = (byte) (f11804S[(i21 >>> 8) & 255] ^ (i67 >>> 8));
        bArr2[i69 + 1] = (byte) (f11804S[i30 & 255] ^ i67);
    }

    @Override // com.sun.crypto.provider.SymmetricCipher
    void decryptBlock(byte[] bArr, int i2, byte[] bArr2, int i3) {
        implDecryptBlock(bArr, i2, bArr2, i3);
    }

    private void implDecryptBlock(byte[] bArr, int i2, byte[] bArr2, int i3) {
        int i4 = i2 + 1;
        int i5 = bArr[i2] << 24;
        int i6 = i4 + 1;
        int i7 = i5 | ((bArr[i4] & 255) << 16);
        int i8 = i6 + 1;
        int i9 = i7 | ((bArr[i6] & 255) << 8);
        int i10 = i8 + 1;
        int i11 = 4 + 1;
        int i12 = (i9 | (bArr[i8] & 255)) ^ this.f11803K[4];
        int i13 = i10 + 1;
        int i14 = bArr[i10] << 24;
        int i15 = i13 + 1;
        int i16 = i14 | ((bArr[i13] & 255) << 16);
        int i17 = i15 + 1;
        int i18 = i16 | ((bArr[i15] & 255) << 8);
        int i19 = i17 + 1;
        int i20 = i11 + 1;
        int i21 = (i18 | (bArr[i17] & 255)) ^ this.f11803K[i11];
        int i22 = i19 + 1;
        int i23 = bArr[i19] << 24;
        int i24 = i22 + 1;
        int i25 = i23 | ((bArr[i22] & 255) << 16);
        int i26 = i24 + 1;
        int i27 = i25 | ((bArr[i24] & 255) << 8);
        int i28 = i26 + 1;
        int i29 = i20 + 1;
        int i30 = (i27 | (bArr[i26] & 255)) ^ this.f11803K[i20];
        int i31 = i28 + 1;
        int i32 = bArr[i28] << 24;
        int i33 = i31 + 1;
        int i34 = i29 + 1;
        int i35 = (((i32 | ((bArr[i31] & 255) << 16)) | ((bArr[i33] & 255) << 8)) | (bArr[i33 + 1] & 255)) ^ this.f11803K[i29];
        if (this.ROUNDS_12) {
            int i36 = i34 + 1;
            int i37 = (((T5[i12 >>> 24] ^ T6[(i35 >>> 16) & 255]) ^ T7[(i30 >>> 8) & 255]) ^ T8[i21 & 255]) ^ this.f11803K[i34];
            int i38 = i36 + 1;
            int i39 = (((T5[i21 >>> 24] ^ T6[(i12 >>> 16) & 255]) ^ T7[(i35 >>> 8) & 255]) ^ T8[i30 & 255]) ^ this.f11803K[i36];
            int i40 = i38 + 1;
            int i41 = (((T5[i30 >>> 24] ^ T6[(i21 >>> 16) & 255]) ^ T7[(i12 >>> 8) & 255]) ^ T8[i35 & 255]) ^ this.f11803K[i38];
            int i42 = i40 + 1;
            int i43 = (((T5[i35 >>> 24] ^ T6[(i30 >>> 16) & 255]) ^ T7[(i21 >>> 8) & 255]) ^ T8[i12 & 255]) ^ this.f11803K[i40];
            int i44 = i42 + 1;
            i12 = (((T5[i37 >>> 24] ^ T6[(i43 >>> 16) & 255]) ^ T7[(i41 >>> 8) & 255]) ^ T8[i39 & 255]) ^ this.f11803K[i42];
            int i45 = i44 + 1;
            i21 = (((T5[i39 >>> 24] ^ T6[(i37 >>> 16) & 255]) ^ T7[(i43 >>> 8) & 255]) ^ T8[i41 & 255]) ^ this.f11803K[i44];
            int i46 = i45 + 1;
            i30 = (((T5[i41 >>> 24] ^ T6[(i39 >>> 16) & 255]) ^ T7[(i37 >>> 8) & 255]) ^ T8[i43 & 255]) ^ this.f11803K[i45];
            i34 = i46 + 1;
            i35 = (((T5[i43 >>> 24] ^ T6[(i41 >>> 16) & 255]) ^ T7[(i39 >>> 8) & 255]) ^ T8[i37 & 255]) ^ this.f11803K[i46];
            if (this.ROUNDS_14) {
                int i47 = i34 + 1;
                int i48 = (((T5[i12 >>> 24] ^ T6[(i35 >>> 16) & 255]) ^ T7[(i30 >>> 8) & 255]) ^ T8[i21 & 255]) ^ this.f11803K[i34];
                int i49 = i47 + 1;
                int i50 = (((T5[i21 >>> 24] ^ T6[(i12 >>> 16) & 255]) ^ T7[(i35 >>> 8) & 255]) ^ T8[i30 & 255]) ^ this.f11803K[i47];
                int i51 = i49 + 1;
                int i52 = (((T5[i30 >>> 24] ^ T6[(i21 >>> 16) & 255]) ^ T7[(i12 >>> 8) & 255]) ^ T8[i35 & 255]) ^ this.f11803K[i49];
                int i53 = i51 + 1;
                int i54 = (((T5[i35 >>> 24] ^ T6[(i30 >>> 16) & 255]) ^ T7[(i21 >>> 8) & 255]) ^ T8[i12 & 255]) ^ this.f11803K[i51];
                int i55 = i53 + 1;
                i12 = (((T5[i48 >>> 24] ^ T6[(i54 >>> 16) & 255]) ^ T7[(i52 >>> 8) & 255]) ^ T8[i50 & 255]) ^ this.f11803K[i53];
                int i56 = i55 + 1;
                i21 = (((T5[i50 >>> 24] ^ T6[(i48 >>> 16) & 255]) ^ T7[(i54 >>> 8) & 255]) ^ T8[i52 & 255]) ^ this.f11803K[i55];
                int i57 = i56 + 1;
                i30 = (((T5[i52 >>> 24] ^ T6[(i50 >>> 16) & 255]) ^ T7[(i48 >>> 8) & 255]) ^ T8[i54 & 255]) ^ this.f11803K[i56];
                i34 = i57 + 1;
                i35 = (((T5[i54 >>> 24] ^ T6[(i52 >>> 16) & 255]) ^ T7[(i50 >>> 8) & 255]) ^ T8[i48 & 255]) ^ this.f11803K[i57];
            }
        }
        int i58 = i34;
        int i59 = i34 + 1;
        int i60 = (((T5[i12 >>> 24] ^ T6[(i35 >>> 16) & 255]) ^ T7[(i30 >>> 8) & 255]) ^ T8[i21 & 255]) ^ this.f11803K[i58];
        int i61 = i59 + 1;
        int i62 = (((T5[i21 >>> 24] ^ T6[(i12 >>> 16) & 255]) ^ T7[(i35 >>> 8) & 255]) ^ T8[i30 & 255]) ^ this.f11803K[i59];
        int i63 = i61 + 1;
        int i64 = (((T5[i30 >>> 24] ^ T6[(i21 >>> 16) & 255]) ^ T7[(i12 >>> 8) & 255]) ^ T8[i35 & 255]) ^ this.f11803K[i61];
        int i65 = i63 + 1;
        int i66 = (((T5[i35 >>> 24] ^ T6[(i30 >>> 16) & 255]) ^ T7[(i21 >>> 8) & 255]) ^ T8[i12 & 255]) ^ this.f11803K[i63];
        int i67 = i65 + 1;
        int i68 = (((T5[i60 >>> 24] ^ T6[(i66 >>> 16) & 255]) ^ T7[(i64 >>> 8) & 255]) ^ T8[i62 & 255]) ^ this.f11803K[i65];
        int i69 = i67 + 1;
        int i70 = (((T5[i62 >>> 24] ^ T6[(i60 >>> 16) & 255]) ^ T7[(i66 >>> 8) & 255]) ^ T8[i64 & 255]) ^ this.f11803K[i67];
        int i71 = i69 + 1;
        int i72 = (((T5[i64 >>> 24] ^ T6[(i62 >>> 16) & 255]) ^ T7[(i60 >>> 8) & 255]) ^ T8[i66 & 255]) ^ this.f11803K[i69];
        int i73 = i71 + 1;
        int i74 = (((T5[i66 >>> 24] ^ T6[(i64 >>> 16) & 255]) ^ T7[(i62 >>> 8) & 255]) ^ T8[i60 & 255]) ^ this.f11803K[i71];
        int i75 = i73 + 1;
        int i76 = (((T5[i68 >>> 24] ^ T6[(i74 >>> 16) & 255]) ^ T7[(i72 >>> 8) & 255]) ^ T8[i70 & 255]) ^ this.f11803K[i73];
        int i77 = i75 + 1;
        int i78 = (((T5[i70 >>> 24] ^ T6[(i68 >>> 16) & 255]) ^ T7[(i74 >>> 8) & 255]) ^ T8[i72 & 255]) ^ this.f11803K[i75];
        int i79 = i77 + 1;
        int i80 = (((T5[i72 >>> 24] ^ T6[(i70 >>> 16) & 255]) ^ T7[(i68 >>> 8) & 255]) ^ T8[i74 & 255]) ^ this.f11803K[i77];
        int i81 = i79 + 1;
        int i82 = (((T5[i74 >>> 24] ^ T6[(i72 >>> 16) & 255]) ^ T7[(i70 >>> 8) & 255]) ^ T8[i68 & 255]) ^ this.f11803K[i79];
        int i83 = i81 + 1;
        int i84 = (((T5[i76 >>> 24] ^ T6[(i82 >>> 16) & 255]) ^ T7[(i80 >>> 8) & 255]) ^ T8[i78 & 255]) ^ this.f11803K[i81];
        int i85 = i83 + 1;
        int i86 = (((T5[i78 >>> 24] ^ T6[(i76 >>> 16) & 255]) ^ T7[(i82 >>> 8) & 255]) ^ T8[i80 & 255]) ^ this.f11803K[i83];
        int i87 = i85 + 1;
        int i88 = (((T5[i80 >>> 24] ^ T6[(i78 >>> 16) & 255]) ^ T7[(i76 >>> 8) & 255]) ^ T8[i82 & 255]) ^ this.f11803K[i85];
        int i89 = i87 + 1;
        int i90 = (((T5[i82 >>> 24] ^ T6[(i80 >>> 16) & 255]) ^ T7[(i78 >>> 8) & 255]) ^ T8[i76 & 255]) ^ this.f11803K[i87];
        int i91 = i89 + 1;
        int i92 = (((T5[i84 >>> 24] ^ T6[(i90 >>> 16) & 255]) ^ T7[(i88 >>> 8) & 255]) ^ T8[i86 & 255]) ^ this.f11803K[i89];
        int i93 = i91 + 1;
        int i94 = (((T5[i86 >>> 24] ^ T6[(i84 >>> 16) & 255]) ^ T7[(i90 >>> 8) & 255]) ^ T8[i88 & 255]) ^ this.f11803K[i91];
        int i95 = i93 + 1;
        int i96 = (((T5[i88 >>> 24] ^ T6[(i86 >>> 16) & 255]) ^ T7[(i84 >>> 8) & 255]) ^ T8[i90 & 255]) ^ this.f11803K[i93];
        int i97 = i95 + 1;
        int i98 = (((T5[i90 >>> 24] ^ T6[(i88 >>> 16) & 255]) ^ T7[(i86 >>> 8) & 255]) ^ T8[i84 & 255]) ^ this.f11803K[i95];
        int i99 = i97 + 1;
        int i100 = (((T5[i92 >>> 24] ^ T6[(i98 >>> 16) & 255]) ^ T7[(i96 >>> 8) & 255]) ^ T8[i94 & 255]) ^ this.f11803K[i97];
        int i101 = i99 + 1;
        int i102 = (((T5[i94 >>> 24] ^ T6[(i92 >>> 16) & 255]) ^ T7[(i98 >>> 8) & 255]) ^ T8[i96 & 255]) ^ this.f11803K[i99];
        int i103 = i101 + 1;
        int i104 = (((T5[i96 >>> 24] ^ T6[(i94 >>> 16) & 255]) ^ T7[(i92 >>> 8) & 255]) ^ T8[i98 & 255]) ^ this.f11803K[i101];
        int i105 = i103 + 1;
        int i106 = (((T5[i98 >>> 24] ^ T6[(i96 >>> 16) & 255]) ^ T7[(i94 >>> 8) & 255]) ^ T8[i92 & 255]) ^ this.f11803K[i103];
        int i107 = i105 + 1;
        int i108 = (((T5[i100 >>> 24] ^ T6[(i106 >>> 16) & 255]) ^ T7[(i104 >>> 8) & 255]) ^ T8[i102 & 255]) ^ this.f11803K[i105];
        int i109 = i107 + 1;
        int i110 = (((T5[i102 >>> 24] ^ T6[(i100 >>> 16) & 255]) ^ T7[(i106 >>> 8) & 255]) ^ T8[i104 & 255]) ^ this.f11803K[i107];
        int i111 = i109 + 1;
        int i112 = (((T5[i104 >>> 24] ^ T6[(i102 >>> 16) & 255]) ^ T7[(i100 >>> 8) & 255]) ^ T8[i106 & 255]) ^ this.f11803K[i109];
        int i113 = i111 + 1;
        int i114 = (((T5[i106 >>> 24] ^ T6[(i104 >>> 16) & 255]) ^ T7[(i102 >>> 8) & 255]) ^ T8[i100 & 255]) ^ this.f11803K[i111];
        int i115 = i113 + 1;
        int i116 = (((T5[i108 >>> 24] ^ T6[(i114 >>> 16) & 255]) ^ T7[(i112 >>> 8) & 255]) ^ T8[i110 & 255]) ^ this.f11803K[i113];
        int i117 = i115 + 1;
        int i118 = (((T5[i110 >>> 24] ^ T6[(i108 >>> 16) & 255]) ^ T7[(i114 >>> 8) & 255]) ^ T8[i112 & 255]) ^ this.f11803K[i115];
        int i119 = i117 + 1;
        int i120 = (((T5[i112 >>> 24] ^ T6[(i110 >>> 16) & 255]) ^ T7[(i108 >>> 8) & 255]) ^ T8[i114 & 255]) ^ this.f11803K[i117];
        int i121 = i119 + 1;
        int i122 = (((T5[i114 >>> 24] ^ T6[(i112 >>> 16) & 255]) ^ T7[(i110 >>> 8) & 255]) ^ T8[i108 & 255]) ^ this.f11803K[i119];
        int i123 = i121 + 1;
        int i124 = (((T5[i116 >>> 24] ^ T6[(i122 >>> 16) & 255]) ^ T7[(i120 >>> 8) & 255]) ^ T8[i118 & 255]) ^ this.f11803K[i121];
        int i125 = i123 + 1;
        int i126 = (((T5[i118 >>> 24] ^ T6[(i116 >>> 16) & 255]) ^ T7[(i122 >>> 8) & 255]) ^ T8[i120 & 255]) ^ this.f11803K[i123];
        int i127 = i125 + 1;
        int i128 = (((T5[i120 >>> 24] ^ T6[(i118 >>> 16) & 255]) ^ T7[(i116 >>> 8) & 255]) ^ T8[i122 & 255]) ^ this.f11803K[i125];
        int i129 = i127 + 1;
        int i130 = (((T5[i122 >>> 24] ^ T6[(i120 >>> 16) & 255]) ^ T7[(i118 >>> 8) & 255]) ^ T8[i116 & 255]) ^ this.f11803K[i127];
        int i131 = this.f11803K[0];
        int i132 = i3 + 1;
        bArr2[i3] = (byte) (Si[i124 >>> 24] ^ (i131 >>> 24));
        int i133 = i132 + 1;
        bArr2[i132] = (byte) (Si[(i130 >>> 16) & 255] ^ (i131 >>> 16));
        int i134 = i133 + 1;
        bArr2[i133] = (byte) (Si[(i128 >>> 8) & 255] ^ (i131 >>> 8));
        int i135 = i134 + 1;
        bArr2[i134] = (byte) (Si[i126 & 255] ^ i131);
        int i136 = this.f11803K[1];
        int i137 = i135 + 1;
        bArr2[i135] = (byte) (Si[i126 >>> 24] ^ (i136 >>> 24));
        int i138 = i137 + 1;
        bArr2[i137] = (byte) (Si[(i124 >>> 16) & 255] ^ (i136 >>> 16));
        int i139 = i138 + 1;
        bArr2[i138] = (byte) (Si[(i130 >>> 8) & 255] ^ (i136 >>> 8));
        int i140 = i139 + 1;
        bArr2[i139] = (byte) (Si[i128 & 255] ^ i136);
        int i141 = this.f11803K[2];
        int i142 = i140 + 1;
        bArr2[i140] = (byte) (Si[i128 >>> 24] ^ (i141 >>> 24));
        int i143 = i142 + 1;
        bArr2[i142] = (byte) (Si[(i126 >>> 16) & 255] ^ (i141 >>> 16));
        int i144 = i143 + 1;
        bArr2[i143] = (byte) (Si[(i124 >>> 8) & 255] ^ (i141 >>> 8));
        int i145 = i144 + 1;
        bArr2[i144] = (byte) (Si[i130 & 255] ^ i141);
        int i146 = this.f11803K[3];
        int i147 = i145 + 1;
        bArr2[i145] = (byte) (Si[i130 >>> 24] ^ (i146 >>> 24));
        int i148 = i147 + 1;
        bArr2[i147] = (byte) (Si[(i128 >>> 16) & 255] ^ (i146 >>> 16));
        bArr2[i148] = (byte) (Si[(i126 >>> 8) & 255] ^ (i146 >>> 8));
        bArr2[i148 + 1] = (byte) (Si[i124 & 255] ^ i146);
    }

    /* JADX WARN: Type inference failed for: r1v22, types: [int[], int[][]] */
    private void makeSessionKey(byte[] bArr) throws InvalidKeyException {
        if (bArr == null) {
            throw new InvalidKeyException("Empty key");
        }
        if (!isKeySizeValid(bArr.length)) {
            throw new InvalidKeyException("Invalid AES key length: " + bArr.length + " bytes");
        }
        int rounds = getRounds(bArr.length);
        int i2 = (rounds + 1) * 4;
        int[][] iArr = new int[rounds + 1][4];
        int[][] iArr2 = new int[rounds + 1][4];
        int length = bArr.length / 4;
        int[] iArr3 = new int[length];
        int i3 = 0;
        int i4 = 0;
        while (i3 < length) {
            iArr3[i3] = (bArr[i4] << 24) | ((bArr[i4 + 1] & 255) << 16) | ((bArr[i4 + 2] & 255) << 8) | (bArr[i4 + 3] & 255);
            i3++;
            i4 += 4;
        }
        int i5 = 0;
        int i6 = 0;
        while (i6 < length && i5 < i2) {
            iArr[i5 / 4][i5 % 4] = iArr3[i6];
            iArr2[rounds - (i5 / 4)][i5 % 4] = iArr3[i6];
            i6++;
            i5++;
        }
        int i7 = 0;
        while (i5 < i2) {
            int i8 = iArr3[length - 1];
            int i9 = i7;
            i7++;
            iArr3[0] = iArr3[0] ^ (((((f11804S[(i8 >>> 16) & 255] << 24) ^ ((f11804S[(i8 >>> 8) & 255] & 255) << 16)) ^ ((f11804S[i8 & 255] & 255) << 8)) ^ (f11804S[i8 >>> 24] & 255)) ^ (rcon[i9] << 24));
            if (length != 8) {
                int i10 = 1;
                int i11 = 0;
                while (i10 < length) {
                    int i12 = i10;
                    iArr3[i12] = iArr3[i12] ^ iArr3[i11];
                    i10++;
                    i11++;
                }
            } else {
                int i13 = 1;
                int i14 = 0;
                while (i13 < length / 2) {
                    int i15 = i13;
                    iArr3[i15] = iArr3[i15] ^ iArr3[i14];
                    i13++;
                    i14++;
                }
                int i16 = iArr3[(length / 2) - 1];
                int i17 = length / 2;
                iArr3[i17] = iArr3[i17] ^ ((((f11804S[i16 & 255] & 255) ^ ((f11804S[(i16 >>> 8) & 255] & 255) << 8)) ^ ((f11804S[(i16 >>> 16) & 255] & 255) << 16)) ^ (f11804S[i16 >>> 24] << 24));
                int i18 = length / 2;
                int i19 = i18 + 1;
                while (i19 < length) {
                    int i20 = i19;
                    iArr3[i20] = iArr3[i20] ^ iArr3[i18];
                    i19++;
                    i18++;
                }
            }
            int i21 = 0;
            while (i21 < length && i5 < i2) {
                iArr[i5 / 4][i5 % 4] = iArr3[i21];
                iArr2[rounds - (i5 / 4)][i5 % 4] = iArr3[i21];
                i21++;
                i5++;
            }
        }
        for (int i22 = 1; i22 < rounds; i22++) {
            for (int i23 = 0; i23 < 4; i23++) {
                int i24 = iArr2[i22][i23];
                iArr2[i22][i23] = ((U1[(i24 >>> 24) & 255] ^ U2[(i24 >>> 16) & 255]) ^ U3[(i24 >>> 8) & 255]) ^ U4[i24 & 255];
            }
        }
        int[] iArrExpandToSubKey = expandToSubKey(iArr, false);
        int[] iArrExpandToSubKey2 = expandToSubKey(iArr2, true);
        this.ROUNDS_12 = rounds >= 12;
        this.ROUNDS_14 = rounds == 14;
        this.limit = rounds * 4;
        this.sessionK = new int[]{iArrExpandToSubKey, iArrExpandToSubKey2};
    }

    private static int getRounds(int i2) {
        return (i2 >> 2) + 6;
    }
}
