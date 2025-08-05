package com.sun.security.auth.module;

import java.io.UnsupportedEncodingException;
import org.apache.commons.net.ftp.FTP;

/* loaded from: rt.jar:com/sun/security/auth/module/Crypt.class */
class Crypt {
    private byte[] KS;
    private static final byte[] IP = {58, 50, 42, 34, 26, 18, 10, 2, 60, 52, 44, 36, 28, 20, 12, 4, 62, 54, 46, 38, 30, 22, 14, 6, 64, 56, 48, 40, 32, 24, 16, 8, 57, 49, 41, 33, 25, 17, 9, 1, 59, 51, 43, 35, 27, 19, 11, 3, 61, 53, 45, 37, 29, 21, 13, 5, 63, 55, 47, 39, 31, 23, 15, 7};
    private static final byte[] FP = {40, 8, 48, 16, 56, 24, 64, 32, 39, 7, 47, 15, 55, 23, 63, 31, 38, 6, 46, 14, 54, 22, 62, 30, 37, 5, 45, 13, 53, 21, 61, 29, 36, 4, 44, 12, 52, 20, 60, 28, 35, 3, 43, 11, 51, 19, 59, 27, 34, 2, 42, 10, 50, 18, 58, 26, 33, 1, 41, 9, 49, 17, 57, 25};
    private static final byte[] PC1_C = {57, 49, 41, 33, 25, 17, 9, 1, 58, 50, 42, 34, 26, 18, 10, 2, 59, 51, 43, 35, 27, 19, 11, 3, 60, 52, 44, 36};
    private static final byte[] PC1_D = {63, 55, 47, 39, 31, 23, 15, 7, 62, 54, 46, 38, 30, 22, 14, 6, 61, 53, 45, 37, 29, 21, 13, 5, 28, 20, 12, 4};
    private static final byte[] shifts = {1, 1, 2, 2, 2, 2, 2, 2, 1, 2, 2, 2, 2, 2, 2, 1};
    private static final byte[] PC2_C = {14, 17, 11, 24, 1, 5, 3, 28, 15, 6, 21, 10, 23, 19, 12, 4, 26, 8, 16, 7, 27, 20, 13, 2};
    private static final byte[] PC2_D = {41, 52, 31, 37, 47, 55, 30, 40, 51, 45, 33, 48, 44, 49, 39, 56, 34, 53, 46, 42, 50, 36, 29, 32};
    private static final byte[] e2 = {32, 1, 2, 3, 4, 5, 4, 5, 6, 7, 8, 9, 8, 9, 10, 11, 12, 13, 12, 13, 14, 15, 16, 17, 16, 17, 18, 19, 20, 21, 20, 21, 22, 23, 24, 25, 24, 25, 26, 27, 28, 29, 28, 29, 30, 31, 32, 1};

    /* renamed from: S, reason: collision with root package name */
    private static final byte[][] f12045S = {new byte[]{14, 4, 13, 1, 2, 15, 11, 8, 3, 10, 6, 12, 5, 9, 0, 7, 0, 15, 7, 4, 14, 2, 13, 1, 10, 6, 12, 11, 9, 5, 3, 8, 4, 1, 14, 8, 13, 6, 2, 11, 15, 12, 9, 7, 3, 10, 5, 0, 15, 12, 8, 2, 4, 9, 1, 7, 5, 11, 3, 14, 10, 0, 6, 13}, new byte[]{15, 1, 8, 14, 6, 11, 3, 4, 9, 7, 2, 13, 12, 0, 5, 10, 3, 13, 4, 7, 15, 2, 8, 14, 12, 0, 1, 10, 6, 9, 11, 5, 0, 14, 7, 11, 10, 4, 13, 1, 5, 8, 12, 6, 9, 3, 2, 15, 13, 8, 10, 1, 3, 15, 4, 2, 11, 6, 7, 12, 0, 5, 14, 9}, new byte[]{10, 0, 9, 14, 6, 3, 15, 5, 1, 13, 12, 7, 11, 4, 2, 8, 13, 7, 0, 9, 3, 4, 6, 10, 2, 8, 5, 14, 12, 11, 15, 1, 13, 6, 4, 9, 8, 15, 3, 0, 11, 1, 2, 12, 5, 10, 14, 7, 1, 10, 13, 0, 6, 9, 8, 7, 4, 15, 14, 3, 11, 5, 2, 12}, new byte[]{7, 13, 14, 3, 0, 6, 9, 10, 1, 2, 8, 5, 11, 12, 4, 15, 13, 8, 11, 5, 6, 15, 0, 3, 4, 7, 2, 12, 1, 10, 14, 9, 10, 6, 9, 0, 12, 11, 7, 13, 15, 1, 3, 14, 5, 2, 8, 4, 3, 15, 0, 6, 10, 1, 13, 8, 9, 4, 5, 11, 12, 7, 2, 14}, new byte[]{2, 12, 4, 1, 7, 10, 11, 6, 8, 5, 3, 15, 13, 0, 14, 9, 14, 11, 2, 12, 4, 7, 13, 1, 5, 0, 15, 10, 3, 9, 8, 6, 4, 2, 1, 11, 10, 13, 7, 8, 15, 9, 12, 5, 6, 3, 0, 14, 11, 8, 12, 7, 1, 14, 2, 13, 6, 15, 0, 9, 10, 4, 5, 3}, new byte[]{12, 1, 10, 15, 9, 2, 6, 8, 0, 13, 3, 4, 14, 7, 5, 11, 10, 15, 4, 2, 7, 12, 9, 5, 6, 1, 13, 14, 0, 11, 3, 8, 9, 14, 15, 5, 2, 8, 12, 3, 7, 0, 4, 10, 1, 13, 11, 6, 4, 3, 2, 12, 9, 5, 15, 10, 11, 14, 1, 7, 6, 0, 8, 13}, new byte[]{4, 11, 2, 14, 15, 0, 8, 13, 3, 12, 9, 7, 5, 10, 6, 1, 13, 0, 11, 7, 4, 9, 1, 10, 14, 3, 5, 12, 2, 15, 8, 6, 1, 4, 11, 13, 12, 3, 7, 14, 10, 15, 6, 8, 0, 5, 9, 2, 6, 11, 13, 8, 1, 4, 10, 7, 9, 5, 0, 15, 14, 2, 3, 12}, new byte[]{13, 2, 8, 4, 6, 15, 11, 1, 10, 9, 3, 14, 5, 0, 12, 7, 1, 15, 13, 8, 10, 3, 7, 4, 12, 5, 6, 11, 0, 14, 9, 2, 7, 11, 4, 1, 9, 12, 14, 2, 0, 6, 10, 13, 15, 3, 5, 8, 2, 1, 14, 7, 4, 10, 8, 13, 15, 12, 9, 0, 3, 5, 6, 11}};

    /* renamed from: P, reason: collision with root package name */
    private static final byte[] f12046P = {16, 7, 20, 21, 29, 12, 28, 17, 1, 15, 23, 26, 5, 18, 31, 10, 2, 8, 24, 14, 32, 27, 3, 9, 19, 13, 30, 6, 22, 11, 4, 25};

    /* renamed from: C, reason: collision with root package name */
    private byte[] f12042C = new byte[28];

    /* renamed from: D, reason: collision with root package name */
    private byte[] f12043D = new byte[28];

    /* renamed from: E, reason: collision with root package name */
    private byte[] f12044E = new byte[48];

    /* renamed from: L, reason: collision with root package name */
    private byte[] f12047L = new byte[64];
    private byte[] tempL = new byte[32];

    /* renamed from: f, reason: collision with root package name */
    private byte[] f12048f = new byte[32];
    private byte[] preS = new byte[48];

    private void setkey(byte[] bArr) {
        if (this.KS == null) {
            this.KS = new byte[768];
        }
        for (int i2 = 0; i2 < 28; i2++) {
            this.f12042C[i2] = bArr[PC1_C[i2] - 1];
            this.f12043D[i2] = bArr[PC1_D[i2] - 1];
        }
        for (int i3 = 0; i3 < 16; i3++) {
            for (int i4 = 0; i4 < shifts[i3]; i4++) {
                byte b2 = this.f12042C[0];
                for (int i5 = 0; i5 < 27; i5++) {
                    this.f12042C[i5] = this.f12042C[i5 + 1];
                }
                this.f12042C[27] = b2;
                byte b3 = this.f12043D[0];
                for (int i6 = 0; i6 < 27; i6++) {
                    this.f12043D[i6] = this.f12043D[i6 + 1];
                }
                this.f12043D[27] = b3;
            }
            for (int i7 = 0; i7 < 24; i7++) {
                int i8 = i3 * 48;
                this.KS[i8 + i7] = this.f12042C[PC2_C[i7] - 1];
                this.KS[i8 + i7 + 24] = this.f12043D[(PC2_D[i7] - 28) - 1];
            }
        }
        for (int i9 = 0; i9 < 48; i9++) {
            this.f12044E[i9] = e2[i9];
        }
    }

    private void encrypt(byte[] bArr, int i2) {
        if (this.KS == null) {
            this.KS = new byte[768];
        }
        for (int i3 = 0; i3 < 64; i3++) {
            this.f12047L[i3] = bArr[IP[i3] - 1];
        }
        for (int i4 = 0; i4 < 16; i4++) {
            int i5 = i4 * 48;
            for (int i6 = 0; i6 < 32; i6++) {
                this.tempL[i6] = this.f12047L[32 + i6];
            }
            for (int i7 = 0; i7 < 48; i7++) {
                this.preS[i7] = (byte) (this.f12047L[(32 + this.f12044E[i7]) - 1] ^ this.KS[i5 + i7]);
            }
            for (int i8 = 0; i8 < 8; i8++) {
                int i9 = 6 * i8;
                byte b2 = f12045S[i8][(this.preS[i9 + 0] << 5) + (this.preS[i9 + 1] << 3) + (this.preS[i9 + 2] << 2) + (this.preS[i9 + 3] << 1) + (this.preS[i9 + 4] << 0) + (this.preS[i9 + 5] << 4)];
                int i10 = 4 * i8;
                this.f12048f[i10 + 0] = (byte) ((b2 >> 3) & 1);
                this.f12048f[i10 + 1] = (byte) ((b2 >> 2) & 1);
                this.f12048f[i10 + 2] = (byte) ((b2 >> 1) & 1);
                this.f12048f[i10 + 3] = (byte) ((b2 >> 0) & 1);
            }
            for (int i11 = 0; i11 < 32; i11++) {
                this.f12047L[32 + i11] = (byte) (this.f12047L[i11] ^ this.f12048f[f12046P[i11] - 1]);
            }
            for (int i12 = 0; i12 < 32; i12++) {
                this.f12047L[i12] = this.tempL[i12];
            }
        }
        for (int i13 = 0; i13 < 32; i13++) {
            byte b3 = this.f12047L[i13];
            this.f12047L[i13] = this.f12047L[32 + i13];
            this.f12047L[32 + i13] = b3;
        }
        for (int i14 = 0; i14 < 64; i14++) {
            bArr[i14] = this.f12047L[FP[i14] - 1];
        }
    }

    public synchronized byte[] crypt(byte[] bArr, byte[] bArr2) {
        byte[] bArr3 = new byte[66];
        byte[] bArr4 = new byte[13];
        int i2 = 0;
        for (int i3 = 0; i3 < bArr.length && i2 < 64; i3++) {
            byte b2 = bArr[i3];
            int i4 = 0;
            while (i4 < 7) {
                bArr3[i2] = (byte) ((b2 >> (6 - i4)) & 1);
                i4++;
                i2++;
            }
            i2++;
        }
        setkey(bArr3);
        for (int i5 = 0; i5 < 66; i5++) {
            bArr3[i5] = 0;
        }
        for (int i6 = 0; i6 < 2; i6++) {
            int i7 = bArr2[i6];
            bArr4[i6] = (byte) i7;
            if (i7 > 90) {
                i7 -= 6;
            }
            if (i7 > 57) {
                i7 -= 7;
            }
            int i8 = i7 - 46;
            for (int i9 = 0; i9 < 6; i9++) {
                if (((i8 >> i9) & 1) != 0) {
                    byte b3 = this.f12044E[(6 * i6) + i9];
                    this.f12044E[(6 * i6) + i9] = this.f12044E[(6 * i6) + i9 + 24];
                    this.f12044E[(6 * i6) + i9 + 24] = b3;
                }
            }
        }
        for (int i10 = 0; i10 < 25; i10++) {
            encrypt(bArr3, 0);
        }
        for (int i11 = 0; i11 < 11; i11++) {
            int i12 = 0;
            for (int i13 = 0; i13 < 6; i13++) {
                i12 = (i12 << 1) | bArr3[(6 * i11) + i13];
            }
            int i14 = i12 + 46;
            if (i14 > 57) {
                i14 += 7;
            }
            if (i14 > 90) {
                i14 += 6;
            }
            bArr4[i11 + 2] = (byte) i14;
        }
        if (bArr4[1] == 0) {
            bArr4[1] = bArr4[0];
        }
        return bArr4;
    }

    public static void main(String[] strArr) {
        if (strArr.length != 2) {
            System.err.println("usage: Crypt password salt");
            System.exit(1);
        }
        try {
            byte[] bArrCrypt = new Crypt().crypt(strArr[0].getBytes(FTP.DEFAULT_CONTROL_ENCODING), strArr[1].getBytes(FTP.DEFAULT_CONTROL_ENCODING));
            for (int i2 = 0; i2 < bArrCrypt.length; i2++) {
                System.out.println(" " + i2 + " " + ((char) bArrCrypt[i2]));
            }
        } catch (UnsupportedEncodingException e3) {
        }
    }
}
