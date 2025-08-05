package com.sun.crypto.provider;

import android.R;
import com.sun.corba.se.impl.encoding.OSFCodeSetRegistry;
import com.sun.org.apache.xpath.internal.compiler.OpCodes;
import java.net.SocketOptions;
import java.security.InvalidKeyException;
import sun.security.jgss.krb5.Krb5Token;

/* loaded from: sunjce_provider.jar:com/sun/crypto/provider/DESCrypt.class */
class DESCrypt extends SymmetricCipher implements DESConstants {
    private static final int[] s0p = {4260096, 65536, 1077936128, 1078001920, 4194304, 1073807616, 1073807360, 1077936128, 1073807616, 4260096, 4259840, 1073742080, 1077936384, 4194304, 0, 1073807360, 65536, 1073741824, 4194560, OSFCodeSetRegistry.UCS_2_VALUE, 1078001920, 4259840, 1073742080, 4194560, 1073741824, 256, OSFCodeSetRegistry.UCS_2_VALUE, 1078001664, 256, 1077936384, 1078001664, 0, 0, 1078001920, 4194560, 1073807360, 4260096, 65536, 1073742080, 4194560, 1078001664, 256, OSFCodeSetRegistry.UCS_2_VALUE, 1077936128, 1073807616, 1073741824, 1077936128, 4259840, 1078001920, OSFCodeSetRegistry.UCS_2_VALUE, 4259840, 1077936384, 4194304, 1073742080, 1073807360, 0, 65536, 4194304, 1077936384, 4260096, 1073741824, 1078001664, 256, 1073807616};
    private static final int[] s1p = {134352898, 0, 135168, 134348800, 134217730, SocketOptions.SO_RCVBUF, 134221824, 135168, 4096, 134348802, 2, 134221824, 131074, 134352896, 134348800, 2, 131072, 134221826, 134348802, 4096, 135170, 134217728, 0, 131074, 134221826, 135170, 134352896, 134217730, 134217728, 131072, SocketOptions.SO_RCVBUF, 134352898, 131074, 134352896, 134221824, 135170, 134352898, 131074, 134217730, 0, 134217728, SocketOptions.SO_RCVBUF, 131072, 134348802, 4096, 134217728, 135170, 134221826, 134352896, 4096, 0, 134217730, 2, 134352898, 135168, 134348800, 134348802, 131072, SocketOptions.SO_RCVBUF, 134221824, 134221826, 2, 134348800, 135168};
    private static final int[] s2p = {545259520, 8421408, 32, 545259552, 536903680, 8388608, 545259552, 32800, 8388640, 32768, 8421376, 536870912, 545292320, 536870944, 536870912, 545292288, 0, 536903680, 8421408, 32, 536870944, 545292320, 32768, 545259520, 545292288, 8388640, 536903712, 8421376, 32800, 0, 8388608, 536903712, 8421408, 32, 536870912, 32768, 536870944, 536903680, 8421376, 545259552, 0, 8421408, 32800, 545292288, 536903680, 8388608, 545292320, 536870912, 536903712, 545259520, 8388608, 545292320, 32768, 8388640, 545259552, 32800, 8388640, 0, 545292288, 536870944, 545259520, 536903712, 32, 8421376};
    private static final int[] s3p = {524801, 33554944, 1, 34079233, 0, 34078720, 33554945, 524289, 34079232, 33554433, 33554432, 513, 33554433, 524801, 524288, 33554432, 34078721, 524800, 512, 1, 524800, 33554945, 34078720, 512, 513, 0, 524289, 34079232, 33554944, 34078721, 34079233, 524288, 34078721, 513, 524288, 33554433, 524800, 33554944, 1, 34078720, 33554945, 0, 512, 524289, 0, 34078721, 34079232, 512, 33554432, 34079233, 524801, 524288, 34079233, 1, 33554944, 524801, 524289, 524800, 34078720, 33554945, 513, 33554432, 33554433, 34079232};
    private static final int[] s4p = {16777216, 8192, 128, 16785540, 16785412, 16777344, 8324, 16785408, 8192, 4, 16777220, 8320, 16777348, 16785412, 16785536, 0, 8320, 16777216, 8196, 132, 16777344, 8324, 0, 16777220, 4, 16777348, 16785540, 8196, 16785408, 128, 132, 16785536, 16785536, 16777348, 8196, 16785408, 8192, 4, 16777220, 16777344, 16777216, 8320, 16785540, 0, 8324, 16777216, 128, 8196, 16777348, 128, 0, 16785540, 16785412, 16785536, 132, 8192, 8320, 16785412, 16777344, 132, 4, 8324, 16785408, 16777220};
    private static final int[] s5p = {268435464, 262152, 0, 268698624, 262152, 1024, 268436488, 262144, OpCodes.NODETYPE_PI, 268698632, 263168, 268435456, 268436480, 268435464, 268697600, 263176, 262144, 268436488, 268697608, 0, 1024, 8, 268698624, 268697608, 268698632, 268697600, 268435456, OpCodes.NODETYPE_PI, 8, 263168, 263176, 268436480, OpCodes.NODETYPE_PI, 268435456, 268436480, 263176, 268698624, 262152, 0, 268436480, 268435456, 1024, 268697608, 262144, 262152, 268698632, 263168, 8, 268698632, 263168, 262144, 268436488, 268435464, 268697600, 263176, 0, 1024, 268435464, 268436488, 268698624, 268697600, OpCodes.NODETYPE_PI, 8, 268697608};
    private static final int[] s6p = {2048, 64, 2097216, -2145386496, -2145384384, -2147481600, 2112, 0, 2097152, -2145386432, -2147483584, 2099200, Integer.MIN_VALUE, 2099264, 2099200, -2147483584, -2145386432, 2048, -2147481600, -2145384384, 0, 2097216, -2145386496, 2112, -2145384448, -2147481536, 2099264, Integer.MIN_VALUE, -2147481536, -2145384448, 64, 2097152, -2147481536, 2099200, -2145384448, -2147483584, 2048, 64, 2097152, -2145384448, -2145386432, -2147481536, 2112, 0, 64, -2145386496, Integer.MIN_VALUE, 2097216, 0, -2145386432, 2097216, 2112, -2147483584, 2048, -2145384384, 2097152, 2099264, Integer.MIN_VALUE, -2147481600, -2145384384, -2145386496, 2099264, 2099200, -2147481600};
    private static final int[] s7p = {68157456, 68173824, 16400, 0, 67125248, 1048592, 68157440, 68173840, 16, 67108864, 1064960, 16400, 1064976, 67125264, 67108880, 68157440, 16384, 1064976, 1048592, 67125248, 68173840, 67108880, 0, 1064960, 67108864, 1048576, 67125264, 68157456, 1048576, 16384, 68173824, 16, 1048576, 16384, 67108880, 68173840, 16400, 67108864, 0, 1064960, 68157456, 67125264, 67125248, 1048592, 68173824, 16, 1048592, 67125248, 68173840, 1048576, 68157440, 67108880, 1064960, 16400, 67125264, 68157440, 16, 68173824, 1064976, 0, 67108864, 68157456, 16384, 1064976};
    private static final int[] permRight0 = {0, 1073741824, 4194304, 1077936128, 16384, 1073758208, 4210688, 1077952512, 64, 1073741888, 4194368, 1077936192, 16448, 1073758272, 4210752, 1077952576};
    private static final int[] permLeft1 = {0, 1073741824, 4194304, 1077936128, 16384, 1073758208, 4210688, 1077952512, 64, 1073741888, 4194368, 1077936192, 16448, 1073758272, 4210752, 1077952576};
    private static final int[] permRight2 = {0, 268435456, 1048576, 269484032, 4096, 268439552, 1052672, 269488128, 16, 268435472, 1048592, 269484048, 4112, 268439568, 1052688, 269488144};
    private static final int[] permLeft3 = {0, 268435456, 1048576, 269484032, 4096, 268439552, 1052672, 269488128, 16, 268435472, 1048592, 269484048, 4112, 268439568, 1052688, 269488144};
    private static final int[] permRight4 = {0, 67108864, 262144, 67371008, 1024, 67109888, 263168, 67372032, 4, 67108868, 262148, 67371012, Krb5Token.MIC_ID_v2, 67109892, 263172, 67372036};
    private static final int[] permLeft5 = {0, 67108864, 262144, 67371008, 1024, 67109888, 263168, 67372032, 4, 67108868, 262148, 67371012, Krb5Token.MIC_ID_v2, 67109892, 263172, 67372036};
    private static final int[] permRight6 = {0, 16777216, 65536, R.attr.theme, 256, 16777472, OSFCodeSetRegistry.UCS_2_VALUE, R.attr.transcriptMode, 1, 16777217, OSFCodeSetRegistry.ISO_8859_1_VALUE, R.attr.label, 257, 16777473, 65793, R.attr.cacheColorHint};
    private static final int[] permLeft7 = {0, 16777216, 65536, R.attr.theme, 256, 16777472, OSFCodeSetRegistry.UCS_2_VALUE, R.attr.transcriptMode, 1, 16777217, OSFCodeSetRegistry.ISO_8859_1_VALUE, R.attr.label, 257, 16777473, 65793, R.attr.cacheColorHint};
    private static final int[] permRight8 = {0, Integer.MIN_VALUE, 8388608, -2139095040, 32768, -2147450880, 8421376, -2139062272, 128, -2147483520, 8388736, -2139094912, 32896, -2147450752, 8421504, -2139062144};
    private static final int[] permLeft9 = {0, Integer.MIN_VALUE, 8388608, -2139095040, 32768, -2147450880, 8421376, -2139062272, 128, -2147483520, 8388736, -2139094912, 32896, -2147450752, 8421504, -2139062144};
    private static final int[] permRightA = {0, 536870912, 2097152, 538968064, 8192, 536879104, 2105344, 538976256, 32, 536870944, 2097184, 538968096, 8224, 536879136, 2105376, 538976288};
    private static final int[] permLeftB = {0, 536870912, 2097152, 538968064, 8192, 536879104, 2105344, 538976256, 32, 536870944, 2097184, 538968096, 8224, 536879136, 2105376, 538976288};
    private static final int[] permRightC = {0, 134217728, 524288, 134742016, 2048, 134219776, 526336, 134744064, 8, 134217736, 524296, 134742024, 2056, 134219784, 526344, 134744072};
    private static final int[] permLeftD = {0, 134217728, 524288, 134742016, 2048, 134219776, 526336, 134744064, 8, 134217736, 524296, 134742024, 2056, 134219784, 526344, 134744072};
    private static final int[] permRightE = {0, 33554432, 131072, 33685504, 512, 33554944, 131584, 33686016, 2, 33554434, 131074, 33685506, 514, 33554946, 131586, 33686018};
    private static final int[] permLeftF = {0, 33554432, 131072, 33685504, 512, 33554944, 131584, 33686016, 2, 33554434, 131074, 33685506, 514, 33554946, 131586, 33686018};
    private static final int[] initPermLeft0 = {0, 32768, 0, 32768, 128, 32896, 128, 32896, 0, 32768, 0, 32768, 128, 32896, 128, 32896};
    private static final int[] initPermRight0 = {0, 0, 32768, 32768, 0, 0, 32768, 32768, 128, 128, 32896, 32896, 128, 128, 32896, 32896};
    private static final int[] initPermLeft1 = {0, Integer.MIN_VALUE, 0, Integer.MIN_VALUE, 8388608, -2139095040, 8388608, -2139095040, 0, Integer.MIN_VALUE, 0, Integer.MIN_VALUE, 8388608, -2139095040, 8388608, -2139095040};
    private static final int[] initPermRight1 = {0, 0, Integer.MIN_VALUE, Integer.MIN_VALUE, 0, 0, Integer.MIN_VALUE, Integer.MIN_VALUE, 8388608, 8388608, -2139095040, -2139095040, 8388608, 8388608, -2139095040, -2139095040};
    private static final int[] initPermLeft2 = {0, 16384, 0, 16384, 64, 16448, 64, 16448, 0, 16384, 0, 16384, 64, 16448, 64, 16448};
    private static final int[] initPermRight2 = {0, 0, 16384, 16384, 0, 0, 16384, 16384, 64, 64, 16448, 16448, 64, 64, 16448, 16448};
    private static final int[] initPermLeft3 = {0, 1073741824, 0, 1073741824, 4194304, 1077936128, 4194304, 1077936128, 0, 1073741824, 0, 1073741824, 4194304, 1077936128, 4194304, 1077936128};
    private static final int[] initPermRight3 = {0, 0, 1073741824, 1073741824, 0, 0, 1073741824, 1073741824, 4194304, 4194304, 1077936128, 1077936128, 4194304, 4194304, 1077936128, 1077936128};
    private static final int[] initPermLeft4 = {0, 8192, 0, 8192, 32, 8224, 32, 8224, 0, 8192, 0, 8192, 32, 8224, 32, 8224};
    private static final int[] initPermRight4 = {0, 0, 8192, 8192, 0, 0, 8192, 8192, 32, 32, 8224, 8224, 32, 32, 8224, 8224};
    private static final int[] initPermLeft5 = {0, 536870912, 0, 536870912, 2097152, 538968064, 2097152, 538968064, 0, 536870912, 0, 536870912, 2097152, 538968064, 2097152, 538968064};
    private static final int[] initPermRight5 = {0, 0, 536870912, 536870912, 0, 0, 536870912, 536870912, 2097152, 2097152, 538968064, 538968064, 2097152, 2097152, 538968064, 538968064};
    private static final int[] initPermLeft6 = {0, 4096, 0, 4096, 16, 4112, 16, 4112, 0, 4096, 0, 4096, 16, 4112, 16, 4112};
    private static final int[] initPermRight6 = {0, 0, 4096, 4096, 0, 0, 4096, 4096, 16, 16, 4112, 4112, 16, 16, 4112, 4112};
    private static final int[] initPermLeft7 = {0, 268435456, 0, 268435456, 1048576, 269484032, 1048576, 269484032, 0, 268435456, 0, 268435456, 1048576, 269484032, 1048576, 269484032};
    private static final int[] initPermRight7 = {0, 0, 268435456, 268435456, 0, 0, 268435456, 268435456, 1048576, 1048576, 269484032, 269484032, 1048576, 1048576, 269484032, 269484032};
    private static final int[] initPermLeft8 = {0, 2048, 0, 2048, 8, 2056, 8, 2056, 0, 2048, 0, 2048, 8, 2056, 8, 2056};
    private static final int[] initPermRight8 = {0, 0, 2048, 2048, 0, 0, 2048, 2048, 8, 8, 2056, 2056, 8, 8, 2056, 2056};
    private static final int[] initPermLeft9 = {0, 134217728, 0, 134217728, 524288, 134742016, 524288, 134742016, 0, 134217728, 0, 134217728, 524288, 134742016, 524288, 134742016};
    private static final int[] initPermRight9 = {0, 0, 134217728, 134217728, 0, 0, 134217728, 134217728, 524288, 524288, 134742016, 134742016, 524288, 524288, 134742016, 134742016};
    private static final int[] initPermLeftA = {0, 1024, 0, 1024, 4, Krb5Token.MIC_ID_v2, 4, Krb5Token.MIC_ID_v2, 0, 1024, 0, 1024, 4, Krb5Token.MIC_ID_v2, 4, Krb5Token.MIC_ID_v2};
    private static final int[] initPermRightA = {0, 0, 1024, 1024, 0, 0, 1024, 1024, 4, 4, Krb5Token.MIC_ID_v2, Krb5Token.MIC_ID_v2, 4, 4, Krb5Token.MIC_ID_v2, Krb5Token.MIC_ID_v2};
    private static final int[] initPermLeftB = {0, 67108864, 0, 67108864, 262144, 67371008, 262144, 67371008, 0, 67108864, 0, 67108864, 262144, 67371008, 262144, 67371008};
    private static final int[] initPermRightB = {0, 0, 67108864, 67108864, 0, 0, 67108864, 67108864, 262144, 262144, 67371008, 67371008, 262144, 262144, 67371008, 67371008};
    private static final int[] initPermLeftC = {0, 512, 0, 512, 2, 514, 2, 514, 0, 512, 0, 512, 2, 514, 2, 514};
    private static final int[] initPermRightC = {0, 0, 512, 512, 0, 0, 512, 512, 2, 2, 514, 514, 2, 2, 514, 514};
    private static final int[] initPermLeftD = {0, 33554432, 0, 33554432, 131072, 33685504, 131072, 33685504, 0, 33554432, 0, 33554432, 131072, 33685504, 131072, 33685504};
    private static final int[] initPermRightD = {0, 0, 33554432, 33554432, 0, 0, 33554432, 33554432, 131072, 131072, 33685504, 33685504, 131072, 131072, 33685504, 33685504};
    private static final int[] initPermLeftE = {0, 256, 0, 256, 1, 257, 1, 257, 0, 256, 0, 256, 1, 257, 1, 257};
    private static final int[] initPermRightE = {0, 0, 256, 256, 0, 0, 256, 256, 1, 1, 257, 257, 1, 1, 257, 257};
    private static final int[] initPermLeftF = {0, 16777216, 0, 16777216, 65536, R.attr.theme, 65536, R.attr.theme, 0, 16777216, 0, 16777216, 65536, R.attr.theme, 65536, R.attr.theme};
    private static final int[] initPermRightF = {0, 0, 16777216, 16777216, 0, 0, 16777216, 16777216, 65536, 65536, R.attr.theme, R.attr.theme, 65536, 65536, R.attr.theme, R.attr.theme};
    byte[] expandedKey = null;
    boolean decrypting = false;

    DESCrypt() {
    }

    @Override // com.sun.crypto.provider.SymmetricCipher
    int getBlockSize() {
        return 8;
    }

    @Override // com.sun.crypto.provider.SymmetricCipher
    void init(boolean z2, String str, byte[] bArr) throws InvalidKeyException {
        this.decrypting = z2;
        if (!str.equalsIgnoreCase("DES")) {
            throw new InvalidKeyException("Wrong algorithm: DES required");
        }
        if (bArr.length != 8) {
            throw new InvalidKeyException("Wrong key size");
        }
        expandKey(bArr);
    }

    @Override // com.sun.crypto.provider.SymmetricCipher
    void encryptBlock(byte[] bArr, int i2, byte[] bArr2, int i3) {
        cipherBlock(bArr, i2, bArr2, i3);
    }

    @Override // com.sun.crypto.provider.SymmetricCipher
    void decryptBlock(byte[] bArr, int i2, byte[] bArr2, int i3) {
        cipherBlock(bArr, i2, bArr2, i3);
    }

    void cipherBlock(byte[] bArr, int i2, byte[] bArr2, int i3) {
        int i4;
        int i5;
        int iInitialPermutationLeft = initialPermutationLeft(bArr, i2);
        int iInitialPermutationRight = initialPermutationRight(bArr, i2);
        byte[] bArr3 = this.expandedKey;
        if (this.decrypting) {
            i4 = 8;
            i5 = 120;
        } else {
            i4 = -8;
            i5 = 0;
        }
        for (int i6 = 0; i6 < 16; i6++) {
            int i7 = (iInitialPermutationRight << 1) | ((iInitialPermutationRight >> 31) & 1);
            int i8 = (iInitialPermutationLeft ^ ((((((s0p[(i7 & 63) ^ bArr3[i5 + 0]] ^ s1p[((i7 >> 4) & 63) ^ bArr3[i5 + 1]]) ^ s2p[((i7 >> 8) & 63) ^ bArr3[i5 + 2]]) ^ s3p[((i7 >> 12) & 63) ^ bArr3[i5 + 3]]) ^ s4p[((i7 >> 16) & 63) ^ bArr3[i5 + 4]]) ^ s5p[((i7 >> 20) & 63) ^ bArr3[i5 + 5]]) ^ s6p[((i7 >> 24) & 63) ^ bArr3[i5 + 6]])) ^ s7p[(((iInitialPermutationRight & 1) << 5) | ((iInitialPermutationRight >> 27) & 31)) ^ bArr3[i5 + 7]];
            iInitialPermutationLeft = iInitialPermutationRight;
            iInitialPermutationRight = i8;
            i5 -= i4;
        }
        perm(iInitialPermutationRight, iInitialPermutationLeft, bArr2, i3);
    }

    private static void perm(int i2, int i3, byte[] bArr, int i4) {
        int i5 = permRight0[i2 & 15];
        int i6 = i2 >> 4;
        int i7 = permLeft1[i6 & 15];
        int i8 = i6 >> 4;
        int i9 = i5 | permRight2[i8 & 15];
        int i10 = i8 >> 4;
        int i11 = i7 | permLeft3[i10 & 15];
        int i12 = i10 >> 4;
        int i13 = i9 | permRight4[i12 & 15];
        int i14 = i12 >> 4;
        int i15 = i11 | permLeft5[i14 & 15];
        int i16 = i14 >> 4;
        int i17 = i13 | permRight6[i16 & 15];
        int i18 = i15 | permLeft7[(i16 >> 4) & 15];
        int i19 = i17 | permRight8[i3 & 15];
        int i20 = i3 >> 4;
        int i21 = i18 | permLeft9[i20 & 15];
        int i22 = i20 >> 4;
        int i23 = i19 | permRightA[i22 & 15];
        int i24 = i22 >> 4;
        int i25 = i21 | permLeftB[i24 & 15];
        int i26 = i24 >> 4;
        int i27 = i23 | permRightC[i26 & 15];
        int i28 = i26 >> 4;
        int i29 = i25 | permLeftD[i28 & 15];
        int i30 = i28 >> 4;
        int i31 = i27 | permRightE[i30 & 15];
        int i32 = i29 | permLeftF[(i30 >> 4) & 15];
        bArr[i4 + 0] = (byte) i32;
        bArr[i4 + 1] = (byte) (i32 >> 8);
        bArr[i4 + 2] = (byte) (i32 >> 16);
        bArr[i4 + 3] = (byte) (i32 >> 24);
        bArr[i4 + 4] = (byte) i31;
        bArr[i4 + 5] = (byte) (i31 >> 8);
        bArr[i4 + 6] = (byte) (i31 >> 16);
        bArr[i4 + 7] = (byte) (i31 >> 24);
    }

    private static int initialPermutationLeft(byte[] bArr, int i2) {
        return initPermLeft1[bArr[i2] & 15] | initPermLeft0[(bArr[i2] >> 4) & 15] | initPermLeft3[bArr[i2 + 1] & 15] | initPermLeft2[(bArr[i2 + 1] >> 4) & 15] | initPermLeft5[bArr[i2 + 2] & 15] | initPermLeft4[(bArr[i2 + 2] >> 4) & 15] | initPermLeft7[bArr[i2 + 3] & 15] | initPermLeft6[(bArr[i2 + 3] >> 4) & 15] | initPermLeft9[bArr[i2 + 4] & 15] | initPermLeft8[(bArr[i2 + 4] >> 4) & 15] | initPermLeftB[bArr[i2 + 5] & 15] | initPermLeftA[(bArr[i2 + 5] >> 4) & 15] | initPermLeftD[bArr[i2 + 6] & 15] | initPermLeftC[(bArr[i2 + 6] >> 4) & 15] | initPermLeftF[bArr[i2 + 7] & 15] | initPermLeftE[(bArr[i2 + 7] >> 4) & 15];
    }

    private static int initialPermutationRight(byte[] bArr, int i2) {
        return initPermRight1[bArr[i2] & 15] | initPermRight0[(bArr[i2] >> 4) & 15] | initPermRight3[bArr[i2 + 1] & 15] | initPermRight2[(bArr[i2 + 1] >> 4) & 15] | initPermRight5[bArr[i2 + 2] & 15] | initPermRight4[(bArr[i2 + 2] >> 4) & 15] | initPermRight7[bArr[i2 + 3] & 15] | initPermRight6[(bArr[i2 + 3] >> 4) & 15] | initPermRight9[bArr[i2 + 4] & 15] | initPermRight8[(bArr[i2 + 4] >> 4) & 15] | initPermRightB[bArr[i2 + 5] & 15] | initPermRightA[(bArr[i2 + 5] >> 4) & 15] | initPermRightD[bArr[i2 + 6] & 15] | initPermRightC[(bArr[i2 + 6] >> 4) & 15] | initPermRightF[bArr[i2 + 7] & 15] | initPermRightE[(bArr[i2 + 7] >> 4) & 15];
    }

    void expandKey(byte[] bArr) {
        byte[] bArr2 = new byte[128];
        byte b2 = bArr[0];
        if ((b2 & 128) != 0) {
            bArr2[3] = (byte) (bArr2[3] | 2);
            bArr2[9] = (byte) (bArr2[9] | 8);
            bArr2[18] = (byte) (bArr2[18] | 8);
            bArr2[27] = (byte) (bArr2[27] | 32);
            bArr2[33] = (byte) (bArr2[33] | 2);
            bArr2[42] = (byte) (bArr2[42] | 16);
            bArr2[48] = (byte) (bArr2[48] | 8);
            bArr2[65] = (byte) (bArr2[65] | 16);
            bArr2[74] = (byte) (bArr2[74] | 2);
            bArr2[80] = (byte) (bArr2[80] | 2);
            bArr2[89] = (byte) (bArr2[89] | 4);
            bArr2[99] = (byte) (bArr2[99] | 16);
            bArr2[104] = (byte) (bArr2[104] | 4);
            bArr2[122] = (byte) (bArr2[122] | 32);
        }
        if ((b2 & 64) != 0) {
            bArr2[1] = (byte) (bArr2[1] | 4);
            bArr2[8] = (byte) (bArr2[8] | 1);
            bArr2[18] = (byte) (bArr2[18] | 4);
            bArr2[25] = (byte) (bArr2[25] | 32);
            bArr2[34] = (byte) (bArr2[34] | 32);
            bArr2[41] = (byte) (bArr2[41] | 8);
            bArr2[50] = (byte) (bArr2[50] | 8);
            bArr2[59] = (byte) (bArr2[59] | 32);
            bArr2[64] = (byte) (bArr2[64] | 16);
            bArr2[75] = (byte) (bArr2[75] | 4);
            bArr2[90] = (byte) (bArr2[90] | 1);
            bArr2[97] = (byte) (bArr2[97] | 16);
            bArr2[106] = (byte) (bArr2[106] | 2);
            bArr2[112] = (byte) (bArr2[112] | 2);
            bArr2[123] = (byte) (bArr2[123] | 1);
        }
        if ((b2 & 32) != 0) {
            bArr2[2] = (byte) (bArr2[2] | 1);
            bArr2[19] = (byte) (bArr2[19] | 8);
            bArr2[35] = (byte) (bArr2[35] | 1);
            bArr2[40] = (byte) (bArr2[40] | 1);
            bArr2[50] = (byte) (bArr2[50] | 4);
            bArr2[57] = (byte) (bArr2[57] | 32);
            bArr2[75] = (byte) (bArr2[75] | 2);
            bArr2[80] = (byte) (bArr2[80] | 32);
            bArr2[89] = (byte) (bArr2[89] | 1);
            bArr2[96] = (byte) (bArr2[96] | 16);
            bArr2[107] = (byte) (bArr2[107] | 4);
            bArr2[120] = (byte) (bArr2[120] | 8);
        }
        if ((b2 & 16) != 0) {
            bArr2[4] = (byte) (bArr2[4] | 32);
            bArr2[20] = (byte) (bArr2[20] | 2);
            bArr2[31] = (byte) (bArr2[31] | 4);
            bArr2[37] = (byte) (bArr2[37] | 32);
            bArr2[47] = (byte) (bArr2[47] | 1);
            bArr2[54] = (byte) (bArr2[54] | 1);
            bArr2[63] = (byte) (bArr2[63] | 2);
            bArr2[68] = (byte) (bArr2[68] | 1);
            bArr2[78] = (byte) (bArr2[78] | 4);
            bArr2[84] = (byte) (bArr2[84] | 8);
            bArr2[101] = (byte) (bArr2[101] | 16);
            bArr2[108] = (byte) (bArr2[108] | 4);
            bArr2[119] = (byte) (bArr2[119] | 16);
            bArr2[126] = (byte) (bArr2[126] | 8);
        }
        if ((b2 & 8) != 0) {
            bArr2[5] = (byte) (bArr2[5] | 4);
            bArr2[15] = (byte) (bArr2[15] | 4);
            bArr2[21] = (byte) (bArr2[21] | 32);
            bArr2[31] = (byte) (bArr2[31] | 1);
            bArr2[38] = (byte) (bArr2[38] | 1);
            bArr2[47] = (byte) (bArr2[47] | 2);
            bArr2[53] = (byte) (bArr2[53] | 2);
            bArr2[68] = (byte) (bArr2[68] | 8);
            bArr2[85] = (byte) (bArr2[85] | 16);
            bArr2[92] = (byte) (bArr2[92] | 4);
            bArr2[103] = (byte) (bArr2[103] | 16);
            bArr2[108] = (byte) (bArr2[108] | 32);
            bArr2[118] = (byte) (bArr2[118] | 32);
            bArr2[124] = (byte) (bArr2[124] | 2);
        }
        if ((b2 & 4) != 0) {
            bArr2[15] = (byte) (bArr2[15] | 2);
            bArr2[21] = (byte) (bArr2[21] | 2);
            bArr2[39] = (byte) (bArr2[39] | 8);
            bArr2[46] = (byte) (bArr2[46] | 16);
            bArr2[55] = (byte) (bArr2[55] | 32);
            bArr2[61] = (byte) (bArr2[61] | 1);
            bArr2[71] = (byte) (bArr2[71] | 16);
            bArr2[76] = (byte) (bArr2[76] | 32);
            bArr2[86] = (byte) (bArr2[86] | 32);
            bArr2[93] = (byte) (bArr2[93] | 4);
            bArr2[102] = (byte) (bArr2[102] | 2);
            bArr2[108] = (byte) (bArr2[108] | 16);
            bArr2[117] = (byte) (bArr2[117] | 8);
            bArr2[126] = (byte) (bArr2[126] | 1);
        }
        if ((b2 & 2) != 0) {
            bArr2[14] = (byte) (bArr2[14] | 16);
            bArr2[23] = (byte) (bArr2[23] | 32);
            bArr2[29] = (byte) (bArr2[29] | 1);
            bArr2[38] = (byte) (bArr2[38] | 8);
            bArr2[52] = (byte) (bArr2[52] | 2);
            bArr2[63] = (byte) (bArr2[63] | 4);
            bArr2[70] = (byte) (bArr2[70] | 2);
            bArr2[76] = (byte) (bArr2[76] | 16);
            bArr2[85] = (byte) (bArr2[85] | 8);
            bArr2[100] = (byte) (bArr2[100] | 1);
            bArr2[110] = (byte) (bArr2[110] | 4);
            bArr2[116] = (byte) (bArr2[116] | 8);
            bArr2[127] = (byte) (bArr2[127] | 8);
        }
        byte b3 = bArr[1];
        if ((b3 & 128) != 0) {
            bArr2[1] = (byte) (bArr2[1] | 8);
            bArr2[8] = (byte) (bArr2[8] | 32);
            bArr2[17] = (byte) (bArr2[17] | 1);
            bArr2[24] = (byte) (bArr2[24] | 16);
            bArr2[35] = (byte) (bArr2[35] | 4);
            bArr2[50] = (byte) (bArr2[50] | 1);
            bArr2[57] = (byte) (bArr2[57] | 16);
            bArr2[67] = (byte) (bArr2[67] | 8);
            bArr2[83] = (byte) (bArr2[83] | 1);
            bArr2[88] = (byte) (bArr2[88] | 1);
            bArr2[98] = (byte) (bArr2[98] | 4);
            bArr2[105] = (byte) (bArr2[105] | 32);
            bArr2[114] = (byte) (bArr2[114] | 32);
            bArr2[123] = (byte) (bArr2[123] | 2);
        }
        if ((b3 & 64) != 0) {
            bArr2[0] = (byte) (bArr2[0] | 1);
            bArr2[11] = (byte) (bArr2[11] | 16);
            bArr2[16] = (byte) (bArr2[16] | 4);
            bArr2[35] = (byte) (bArr2[35] | 2);
            bArr2[40] = (byte) (bArr2[40] | 32);
            bArr2[49] = (byte) (bArr2[49] | 1);
            bArr2[56] = (byte) (bArr2[56] | 16);
            bArr2[65] = (byte) (bArr2[65] | 2);
            bArr2[74] = (byte) (bArr2[74] | 16);
            bArr2[80] = (byte) (bArr2[80] | 8);
            bArr2[99] = (byte) (bArr2[99] | 8);
            bArr2[115] = (byte) (bArr2[115] | 1);
            bArr2[121] = (byte) (bArr2[121] | 4);
        }
        if ((b3 & 32) != 0) {
            bArr2[9] = (byte) (bArr2[9] | 16);
            bArr2[18] = (byte) (bArr2[18] | 2);
            bArr2[24] = (byte) (bArr2[24] | 2);
            bArr2[33] = (byte) (bArr2[33] | 4);
            bArr2[43] = (byte) (bArr2[43] | 16);
            bArr2[48] = (byte) (bArr2[48] | 4);
            bArr2[66] = (byte) (bArr2[66] | 32);
            bArr2[73] = (byte) (bArr2[73] | 8);
            bArr2[82] = (byte) (bArr2[82] | 8);
            bArr2[91] = (byte) (bArr2[91] | 32);
            bArr2[97] = (byte) (bArr2[97] | 2);
            bArr2[106] = (byte) (bArr2[106] | 16);
            bArr2[112] = (byte) (bArr2[112] | 8);
            bArr2[122] = (byte) (bArr2[122] | 1);
        }
        if ((b3 & 16) != 0) {
            bArr2[14] = (byte) (bArr2[14] | 32);
            bArr2[21] = (byte) (bArr2[21] | 4);
            bArr2[30] = (byte) (bArr2[30] | 2);
            bArr2[36] = (byte) (bArr2[36] | 16);
            bArr2[45] = (byte) (bArr2[45] | 8);
            bArr2[60] = (byte) (bArr2[60] | 1);
            bArr2[69] = (byte) (bArr2[69] | 2);
            bArr2[87] = (byte) (bArr2[87] | 8);
            bArr2[94] = (byte) (bArr2[94] | 16);
            bArr2[103] = (byte) (bArr2[103] | 32);
            bArr2[109] = (byte) (bArr2[109] | 1);
            bArr2[118] = (byte) (bArr2[118] | 8);
            bArr2[124] = (byte) (bArr2[124] | 32);
        }
        if ((b3 & 8) != 0) {
            bArr2[7] = (byte) (bArr2[7] | 4);
            bArr2[14] = (byte) (bArr2[14] | 2);
            bArr2[20] = (byte) (bArr2[20] | 16);
            bArr2[29] = (byte) (bArr2[29] | 8);
            bArr2[44] = (byte) (bArr2[44] | 1);
            bArr2[54] = (byte) (bArr2[54] | 4);
            bArr2[60] = (byte) (bArr2[60] | 8);
            bArr2[71] = (byte) (bArr2[71] | 8);
            bArr2[78] = (byte) (bArr2[78] | 16);
            bArr2[87] = (byte) (bArr2[87] | 32);
            bArr2[93] = (byte) (bArr2[93] | 1);
            bArr2[102] = (byte) (bArr2[102] | 8);
            bArr2[116] = (byte) (bArr2[116] | 2);
            bArr2[125] = (byte) (bArr2[125] | 4);
        }
        if ((b3 & 4) != 0) {
            bArr2[7] = (byte) (bArr2[7] | 2);
            bArr2[12] = (byte) (bArr2[12] | 1);
            bArr2[22] = (byte) (bArr2[22] | 4);
            bArr2[28] = (byte) (bArr2[28] | 8);
            bArr2[45] = (byte) (bArr2[45] | 16);
            bArr2[52] = (byte) (bArr2[52] | 4);
            bArr2[63] = (byte) (bArr2[63] | 16);
            bArr2[70] = (byte) (bArr2[70] | 8);
            bArr2[84] = (byte) (bArr2[84] | 2);
            bArr2[95] = (byte) (bArr2[95] | 4);
            bArr2[101] = (byte) (bArr2[101] | 32);
            bArr2[111] = (byte) (bArr2[111] | 1);
            bArr2[118] = (byte) (bArr2[118] | 1);
        }
        if ((b3 & 2) != 0) {
            bArr2[6] = (byte) (bArr2[6] | 16);
            bArr2[13] = (byte) (bArr2[13] | 16);
            bArr2[20] = (byte) (bArr2[20] | 4);
            bArr2[31] = (byte) (bArr2[31] | 16);
            bArr2[36] = (byte) (bArr2[36] | 32);
            bArr2[46] = (byte) (bArr2[46] | 32);
            bArr2[53] = (byte) (bArr2[53] | 4);
            bArr2[62] = (byte) (bArr2[62] | 2);
            bArr2[69] = (byte) (bArr2[69] | 32);
            bArr2[79] = (byte) (bArr2[79] | 1);
            bArr2[86] = (byte) (bArr2[86] | 1);
            bArr2[95] = (byte) (bArr2[95] | 2);
            bArr2[101] = (byte) (bArr2[101] | 2);
            bArr2[119] = (byte) (bArr2[119] | 8);
        }
        byte b4 = bArr[2];
        if ((b4 & 128) != 0) {
            bArr2[0] = (byte) (bArr2[0] | 32);
            bArr2[10] = (byte) (bArr2[10] | 8);
            bArr2[19] = (byte) (bArr2[19] | 32);
            bArr2[25] = (byte) (bArr2[25] | 2);
            bArr2[34] = (byte) (bArr2[34] | 16);
            bArr2[40] = (byte) (bArr2[40] | 8);
            bArr2[59] = (byte) (bArr2[59] | 8);
            bArr2[66] = (byte) (bArr2[66] | 2);
            bArr2[72] = (byte) (bArr2[72] | 2);
            bArr2[81] = (byte) (bArr2[81] | 4);
            bArr2[91] = (byte) (bArr2[91] | 16);
            bArr2[96] = (byte) (bArr2[96] | 4);
            bArr2[115] = (byte) (bArr2[115] | 2);
            bArr2[121] = (byte) (bArr2[121] | 8);
        }
        if ((b4 & 64) != 0) {
            bArr2[3] = (byte) (bArr2[3] | 16);
            bArr2[10] = (byte) (bArr2[10] | 4);
            bArr2[17] = (byte) (bArr2[17] | 32);
            bArr2[26] = (byte) (bArr2[26] | 32);
            bArr2[33] = (byte) (bArr2[33] | 8);
            bArr2[42] = (byte) (bArr2[42] | 8);
            bArr2[51] = (byte) (bArr2[51] | 32);
            bArr2[57] = (byte) (bArr2[57] | 2);
            bArr2[67] = (byte) (bArr2[67] | 4);
            bArr2[82] = (byte) (bArr2[82] | 1);
            bArr2[89] = (byte) (bArr2[89] | 16);
            bArr2[98] = (byte) (bArr2[98] | 2);
            bArr2[104] = (byte) (bArr2[104] | 2);
            bArr2[113] = (byte) (bArr2[113] | 4);
            bArr2[120] = (byte) (bArr2[120] | 1);
        }
        if ((b4 & 32) != 0) {
            bArr2[1] = (byte) (bArr2[1] | 16);
            bArr2[11] = (byte) (bArr2[11] | 8);
            bArr2[27] = (byte) (bArr2[27] | 1);
            bArr2[32] = (byte) (bArr2[32] | 1);
            bArr2[42] = (byte) (bArr2[42] | 4);
            bArr2[49] = (byte) (bArr2[49] | 32);
            bArr2[58] = (byte) (bArr2[58] | 32);
            bArr2[67] = (byte) (bArr2[67] | 2);
            bArr2[72] = (byte) (bArr2[72] | 32);
            bArr2[81] = (byte) (bArr2[81] | 1);
            bArr2[88] = (byte) (bArr2[88] | 16);
            bArr2[99] = (byte) (bArr2[99] | 4);
            bArr2[114] = (byte) (bArr2[114] | 1);
        }
        if ((b4 & 16) != 0) {
            bArr2[6] = (byte) (bArr2[6] | 32);
            bArr2[12] = (byte) (bArr2[12] | 2);
            bArr2[23] = (byte) (bArr2[23] | 4);
            bArr2[29] = (byte) (bArr2[29] | 32);
            bArr2[39] = (byte) (bArr2[39] | 1);
            bArr2[46] = (byte) (bArr2[46] | 1);
            bArr2[55] = (byte) (bArr2[55] | 2);
            bArr2[61] = (byte) (bArr2[61] | 2);
            bArr2[70] = (byte) (bArr2[70] | 4);
            bArr2[76] = (byte) (bArr2[76] | 8);
            bArr2[93] = (byte) (bArr2[93] | 16);
            bArr2[100] = (byte) (bArr2[100] | 4);
            bArr2[111] = (byte) (bArr2[111] | 16);
            bArr2[116] = (byte) (bArr2[116] | 32);
        }
        if ((b4 & 8) != 0) {
            bArr2[6] = (byte) (bArr2[6] | 2);
            bArr2[13] = (byte) (bArr2[13] | 32);
            bArr2[23] = (byte) (bArr2[23] | 1);
            bArr2[30] = (byte) (bArr2[30] | 1);
            bArr2[39] = (byte) (bArr2[39] | 2);
            bArr2[45] = (byte) (bArr2[45] | 2);
            bArr2[63] = (byte) (bArr2[63] | 8);
            bArr2[77] = (byte) (bArr2[77] | 16);
            bArr2[84] = (byte) (bArr2[84] | 4);
            bArr2[95] = (byte) (bArr2[95] | 16);
            bArr2[100] = (byte) (bArr2[100] | 32);
            bArr2[110] = (byte) (bArr2[110] | 32);
            bArr2[117] = (byte) (bArr2[117] | 4);
            bArr2[127] = (byte) (bArr2[127] | 4);
        }
        if ((b4 & 4) != 0) {
            bArr2[4] = (byte) (bArr2[4] | 1);
            bArr2[13] = (byte) (bArr2[13] | 2);
            bArr2[31] = (byte) (bArr2[31] | 8);
            bArr2[38] = (byte) (bArr2[38] | 16);
            bArr2[47] = (byte) (bArr2[47] | 32);
            bArr2[53] = (byte) (bArr2[53] | 1);
            bArr2[62] = (byte) (bArr2[62] | 8);
            bArr2[68] = (byte) (bArr2[68] | 32);
            bArr2[78] = (byte) (bArr2[78] | 32);
            bArr2[85] = (byte) (bArr2[85] | 4);
            bArr2[94] = (byte) (bArr2[94] | 2);
            bArr2[100] = (byte) (bArr2[100] | 16);
            bArr2[109] = (byte) (bArr2[109] | 8);
            bArr2[127] = (byte) (bArr2[127] | 2);
        }
        if ((b4 & 2) != 0) {
            bArr2[5] = (byte) (bArr2[5] | 16);
            bArr2[15] = (byte) (bArr2[15] | 32);
            bArr2[21] = (byte) (bArr2[21] | 1);
            bArr2[30] = (byte) (bArr2[30] | 8);
            bArr2[44] = (byte) (bArr2[44] | 2);
            bArr2[55] = (byte) (bArr2[55] | 4);
            bArr2[61] = (byte) (bArr2[61] | 32);
            bArr2[68] = (byte) (bArr2[68] | 16);
            bArr2[77] = (byte) (bArr2[77] | 8);
            bArr2[92] = (byte) (bArr2[92] | 1);
            bArr2[102] = (byte) (bArr2[102] | 4);
            bArr2[108] = (byte) (bArr2[108] | 8);
            bArr2[126] = (byte) (bArr2[126] | 16);
        }
        byte b5 = bArr[3];
        if ((b5 & 128) != 0) {
            bArr2[2] = (byte) (bArr2[2] | 8);
            bArr2[9] = (byte) (bArr2[9] | 1);
            bArr2[16] = (byte) (bArr2[16] | 16);
            bArr2[27] = (byte) (bArr2[27] | 4);
            bArr2[42] = (byte) (bArr2[42] | 1);
            bArr2[49] = (byte) (bArr2[49] | 16);
            bArr2[58] = (byte) (bArr2[58] | 2);
            bArr2[75] = (byte) (bArr2[75] | 1);
            bArr2[80] = (byte) (bArr2[80] | 1);
            bArr2[90] = (byte) (bArr2[90] | 4);
            bArr2[97] = (byte) (bArr2[97] | 32);
            bArr2[106] = (byte) (bArr2[106] | 32);
            bArr2[113] = (byte) (bArr2[113] | 8);
            bArr2[120] = (byte) (bArr2[120] | 32);
        }
        if ((b5 & 64) != 0) {
            bArr2[2] = (byte) (bArr2[2] | 4);
            bArr2[8] = (byte) (bArr2[8] | 4);
            bArr2[27] = (byte) (bArr2[27] | 2);
            bArr2[32] = (byte) (bArr2[32] | 32);
            bArr2[41] = (byte) (bArr2[41] | 1);
            bArr2[48] = (byte) (bArr2[48] | 16);
            bArr2[59] = (byte) (bArr2[59] | 4);
            bArr2[66] = (byte) (bArr2[66] | 16);
            bArr2[72] = (byte) (bArr2[72] | 8);
            bArr2[91] = (byte) (bArr2[91] | 8);
            bArr2[107] = (byte) (bArr2[107] | 1);
            bArr2[112] = (byte) (bArr2[112] | 1);
            bArr2[123] = (byte) (bArr2[123] | 16);
        }
        if ((b5 & 32) != 0) {
            bArr2[3] = (byte) (bArr2[3] | 8);
            bArr2[10] = (byte) (bArr2[10] | 2);
            bArr2[16] = (byte) (bArr2[16] | 2);
            bArr2[25] = (byte) (bArr2[25] | 4);
            bArr2[35] = (byte) (bArr2[35] | 16);
            bArr2[40] = (byte) (bArr2[40] | 4);
            bArr2[59] = (byte) (bArr2[59] | 2);
            bArr2[65] = (byte) (bArr2[65] | 8);
            bArr2[74] = (byte) (bArr2[74] | 8);
            bArr2[83] = (byte) (bArr2[83] | 32);
            bArr2[89] = (byte) (bArr2[89] | 2);
            bArr2[98] = (byte) (bArr2[98] | 16);
            bArr2[104] = (byte) (bArr2[104] | 8);
            bArr2[121] = (byte) (bArr2[121] | 16);
        }
        if ((b5 & 16) != 0) {
            bArr2[4] = (byte) (bArr2[4] | 2);
            bArr2[13] = (byte) (bArr2[13] | 4);
            bArr2[22] = (byte) (bArr2[22] | 2);
            bArr2[28] = (byte) (bArr2[28] | 16);
            bArr2[37] = (byte) (bArr2[37] | 8);
            bArr2[52] = (byte) (bArr2[52] | 1);
            bArr2[62] = (byte) (bArr2[62] | 4);
            bArr2[79] = (byte) (bArr2[79] | 8);
            bArr2[86] = (byte) (bArr2[86] | 16);
            bArr2[95] = (byte) (bArr2[95] | 32);
            bArr2[101] = (byte) (bArr2[101] | 1);
            bArr2[110] = (byte) (bArr2[110] | 8);
            bArr2[126] = (byte) (bArr2[126] | 32);
        }
        if ((b5 & 8) != 0) {
            bArr2[5] = (byte) (bArr2[5] | 32);
            bArr2[12] = (byte) (bArr2[12] | 16);
            bArr2[21] = (byte) (bArr2[21] | 8);
            bArr2[36] = (byte) (bArr2[36] | 1);
            bArr2[46] = (byte) (bArr2[46] | 4);
            bArr2[52] = (byte) (bArr2[52] | 8);
            bArr2[70] = (byte) (bArr2[70] | 16);
            bArr2[79] = (byte) (bArr2[79] | 32);
            bArr2[85] = (byte) (bArr2[85] | 1);
            bArr2[94] = (byte) (bArr2[94] | 8);
            bArr2[108] = (byte) (bArr2[108] | 2);
            bArr2[119] = (byte) (bArr2[119] | 4);
            bArr2[126] = (byte) (bArr2[126] | 2);
        }
        if ((b5 & 4) != 0) {
            bArr2[5] = (byte) (bArr2[5] | 2);
            bArr2[14] = (byte) (bArr2[14] | 4);
            bArr2[20] = (byte) (bArr2[20] | 8);
            bArr2[37] = (byte) (bArr2[37] | 16);
            bArr2[44] = (byte) (bArr2[44] | 4);
            bArr2[55] = (byte) (bArr2[55] | 16);
            bArr2[60] = (byte) (bArr2[60] | 32);
            bArr2[76] = (byte) (bArr2[76] | 2);
            bArr2[87] = (byte) (bArr2[87] | 4);
            bArr2[93] = (byte) (bArr2[93] | 32);
            bArr2[103] = (byte) (bArr2[103] | 1);
            bArr2[110] = (byte) (bArr2[110] | 1);
            bArr2[119] = (byte) (bArr2[119] | 2);
            bArr2[124] = (byte) (bArr2[124] | 1);
        }
        if ((b5 & 2) != 0) {
            bArr2[7] = (byte) (bArr2[7] | 32);
            bArr2[12] = (byte) (bArr2[12] | 4);
            bArr2[23] = (byte) (bArr2[23] | 16);
            bArr2[28] = (byte) (bArr2[28] | 32);
            bArr2[38] = (byte) (bArr2[38] | 32);
            bArr2[45] = (byte) (bArr2[45] | 4);
            bArr2[54] = (byte) (bArr2[54] | 2);
            bArr2[60] = (byte) (bArr2[60] | 16);
            bArr2[71] = (byte) (bArr2[71] | 1);
            bArr2[78] = (byte) (bArr2[78] | 1);
            bArr2[87] = (byte) (bArr2[87] | 2);
            bArr2[93] = (byte) (bArr2[93] | 2);
            bArr2[111] = (byte) (bArr2[111] | 8);
            bArr2[118] = (byte) (bArr2[118] | 16);
            bArr2[125] = (byte) (bArr2[125] | 16);
        }
        byte b6 = bArr[4];
        if ((b6 & 128) != 0) {
            bArr2[1] = (byte) (bArr2[1] | 1);
            bArr2[11] = (byte) (bArr2[11] | 32);
            bArr2[17] = (byte) (bArr2[17] | 2);
            bArr2[26] = (byte) (bArr2[26] | 16);
            bArr2[32] = (byte) (bArr2[32] | 8);
            bArr2[51] = (byte) (bArr2[51] | 8);
            bArr2[64] = (byte) (bArr2[64] | 2);
            bArr2[73] = (byte) (bArr2[73] | 4);
            bArr2[83] = (byte) (bArr2[83] | 16);
            bArr2[88] = (byte) (bArr2[88] | 4);
            bArr2[107] = (byte) (bArr2[107] | 2);
            bArr2[112] = (byte) (bArr2[112] | 32);
            bArr2[122] = (byte) (bArr2[122] | 8);
        }
        if ((b6 & 64) != 0) {
            bArr2[0] = (byte) (bArr2[0] | 4);
            bArr2[9] = (byte) (bArr2[9] | 32);
            bArr2[18] = (byte) (bArr2[18] | 32);
            bArr2[25] = (byte) (bArr2[25] | 8);
            bArr2[34] = (byte) (bArr2[34] | 8);
            bArr2[43] = (byte) (bArr2[43] | 32);
            bArr2[49] = (byte) (bArr2[49] | 2);
            bArr2[58] = (byte) (bArr2[58] | 16);
            bArr2[74] = (byte) (bArr2[74] | 1);
            bArr2[81] = (byte) (bArr2[81] | 16);
            bArr2[90] = (byte) (bArr2[90] | 2);
            bArr2[96] = (byte) (bArr2[96] | 2);
            bArr2[105] = (byte) (bArr2[105] | 4);
            bArr2[115] = (byte) (bArr2[115] | 16);
            bArr2[122] = (byte) (bArr2[122] | 4);
        }
        if ((b6 & 32) != 0) {
            bArr2[2] = (byte) (bArr2[2] | 2);
            bArr2[19] = (byte) (bArr2[19] | 1);
            bArr2[24] = (byte) (bArr2[24] | 1);
            bArr2[34] = (byte) (bArr2[34] | 4);
            bArr2[41] = (byte) (bArr2[41] | 32);
            bArr2[50] = (byte) (bArr2[50] | 32);
            bArr2[57] = (byte) (bArr2[57] | 8);
            bArr2[64] = (byte) (bArr2[64] | 32);
            bArr2[73] = (byte) (bArr2[73] | 1);
            bArr2[80] = (byte) (bArr2[80] | 16);
            bArr2[91] = (byte) (bArr2[91] | 4);
            bArr2[106] = (byte) (bArr2[106] | 1);
            bArr2[113] = (byte) (bArr2[113] | 16);
            bArr2[123] = (byte) (bArr2[123] | 8);
        }
        if ((b6 & 16) != 0) {
            bArr2[3] = (byte) (bArr2[3] | 4);
            bArr2[10] = (byte) (bArr2[10] | 16);
            bArr2[16] = (byte) (bArr2[16] | 8);
            bArr2[35] = (byte) (bArr2[35] | 8);
            bArr2[51] = (byte) (bArr2[51] | 1);
            bArr2[56] = (byte) (bArr2[56] | 1);
            bArr2[67] = (byte) (bArr2[67] | 16);
            bArr2[72] = (byte) (bArr2[72] | 4);
            bArr2[91] = (byte) (bArr2[91] | 2);
            bArr2[96] = (byte) (bArr2[96] | 32);
            bArr2[105] = (byte) (bArr2[105] | 1);
            bArr2[112] = (byte) (bArr2[112] | 16);
            bArr2[121] = (byte) (bArr2[121] | 2);
        }
        if ((b6 & 8) != 0) {
            bArr2[4] = (byte) (bArr2[4] | 16);
            bArr2[15] = (byte) (bArr2[15] | 1);
            bArr2[22] = (byte) (bArr2[22] | 1);
            bArr2[31] = (byte) (bArr2[31] | 2);
            bArr2[37] = (byte) (bArr2[37] | 2);
            bArr2[55] = (byte) (bArr2[55] | 8);
            bArr2[62] = (byte) (bArr2[62] | 16);
            bArr2[69] = (byte) (bArr2[69] | 16);
            bArr2[76] = (byte) (bArr2[76] | 4);
            bArr2[87] = (byte) (bArr2[87] | 16);
            bArr2[92] = (byte) (bArr2[92] | 32);
            bArr2[102] = (byte) (bArr2[102] | 32);
            bArr2[109] = (byte) (bArr2[109] | 4);
            bArr2[118] = (byte) (bArr2[118] | 2);
            bArr2[125] = (byte) (bArr2[125] | 32);
        }
        if ((b6 & 4) != 0) {
            bArr2[6] = (byte) (bArr2[6] | 4);
            bArr2[23] = (byte) (bArr2[23] | 8);
            bArr2[30] = (byte) (bArr2[30] | 16);
            bArr2[39] = (byte) (bArr2[39] | 32);
            bArr2[45] = (byte) (bArr2[45] | 1);
            bArr2[54] = (byte) (bArr2[54] | 8);
            bArr2[70] = (byte) (bArr2[70] | 32);
            bArr2[77] = (byte) (bArr2[77] | 4);
            bArr2[86] = (byte) (bArr2[86] | 2);
            bArr2[92] = (byte) (bArr2[92] | 16);
            bArr2[101] = (byte) (bArr2[101] | 8);
            bArr2[116] = (byte) (bArr2[116] | 1);
            bArr2[125] = (byte) (bArr2[125] | 2);
        }
        if ((b6 & 2) != 0) {
            bArr2[4] = (byte) (bArr2[4] | 4);
            bArr2[13] = (byte) (bArr2[13] | 1);
            bArr2[22] = (byte) (bArr2[22] | 8);
            bArr2[36] = (byte) (bArr2[36] | 2);
            bArr2[47] = (byte) (bArr2[47] | 4);
            bArr2[53] = (byte) (bArr2[53] | 32);
            bArr2[63] = (byte) (bArr2[63] | 1);
            bArr2[69] = (byte) (bArr2[69] | 8);
            bArr2[84] = (byte) (bArr2[84] | 1);
            bArr2[94] = (byte) (bArr2[94] | 4);
            bArr2[100] = (byte) (bArr2[100] | 8);
            bArr2[117] = (byte) (bArr2[117] | 16);
            bArr2[127] = (byte) (bArr2[127] | 32);
        }
        byte b7 = bArr[5];
        if ((b7 & 128) != 0) {
            bArr2[3] = (byte) (bArr2[3] | 32);
            bArr2[8] = (byte) (bArr2[8] | 16);
            bArr2[19] = (byte) (bArr2[19] | 4);
            bArr2[34] = (byte) (bArr2[34] | 1);
            bArr2[41] = (byte) (bArr2[41] | 16);
            bArr2[50] = (byte) (bArr2[50] | 2);
            bArr2[56] = (byte) (bArr2[56] | 2);
            bArr2[67] = (byte) (bArr2[67] | 1);
            bArr2[72] = (byte) (bArr2[72] | 1);
            bArr2[82] = (byte) (bArr2[82] | 4);
            bArr2[89] = (byte) (bArr2[89] | 32);
            bArr2[98] = (byte) (bArr2[98] | 32);
            bArr2[105] = (byte) (bArr2[105] | 8);
            bArr2[114] = (byte) (bArr2[114] | 8);
            bArr2[121] = (byte) (bArr2[121] | 1);
        }
        if ((b7 & 64) != 0) {
            bArr2[1] = (byte) (bArr2[1] | 32);
            bArr2[19] = (byte) (bArr2[19] | 2);
            bArr2[24] = (byte) (bArr2[24] | 32);
            bArr2[33] = (byte) (bArr2[33] | 1);
            bArr2[40] = (byte) (bArr2[40] | 16);
            bArr2[51] = (byte) (bArr2[51] | 4);
            bArr2[64] = (byte) (bArr2[64] | 8);
            bArr2[83] = (byte) (bArr2[83] | 8);
            bArr2[99] = (byte) (bArr2[99] | 1);
            bArr2[104] = (byte) (bArr2[104] | 1);
            bArr2[114] = (byte) (bArr2[114] | 4);
            bArr2[120] = (byte) (bArr2[120] | 4);
        }
        if ((b7 & 32) != 0) {
            bArr2[8] = (byte) (bArr2[8] | 2);
            bArr2[17] = (byte) (bArr2[17] | 4);
            bArr2[27] = (byte) (bArr2[27] | 16);
            bArr2[32] = (byte) (bArr2[32] | 4);
            bArr2[51] = (byte) (bArr2[51] | 2);
            bArr2[56] = (byte) (bArr2[56] | 32);
            bArr2[66] = (byte) (bArr2[66] | 8);
            bArr2[75] = (byte) (bArr2[75] | 32);
            bArr2[81] = (byte) (bArr2[81] | 2);
            bArr2[90] = (byte) (bArr2[90] | 16);
            bArr2[96] = (byte) (bArr2[96] | 8);
            bArr2[115] = (byte) (bArr2[115] | 8);
            bArr2[122] = (byte) (bArr2[122] | 2);
        }
        if ((b7 & 16) != 0) {
            bArr2[2] = (byte) (bArr2[2] | 16);
            bArr2[18] = (byte) (bArr2[18] | 1);
            bArr2[25] = (byte) (bArr2[25] | 16);
            bArr2[34] = (byte) (bArr2[34] | 2);
            bArr2[40] = (byte) (bArr2[40] | 2);
            bArr2[49] = (byte) (bArr2[49] | 4);
            bArr2[59] = (byte) (bArr2[59] | 16);
            bArr2[66] = (byte) (bArr2[66] | 4);
            bArr2[73] = (byte) (bArr2[73] | 32);
            bArr2[82] = (byte) (bArr2[82] | 32);
            bArr2[89] = (byte) (bArr2[89] | 8);
            bArr2[98] = (byte) (bArr2[98] | 8);
            bArr2[107] = (byte) (bArr2[107] | 32);
            bArr2[113] = (byte) (bArr2[113] | 2);
            bArr2[123] = (byte) (bArr2[123] | 4);
        }
        if ((b7 & 8) != 0) {
            bArr2[7] = (byte) (bArr2[7] | 1);
            bArr2[13] = (byte) (bArr2[13] | 8);
            bArr2[28] = (byte) (bArr2[28] | 1);
            bArr2[38] = (byte) (bArr2[38] | 4);
            bArr2[44] = (byte) (bArr2[44] | 8);
            bArr2[61] = (byte) (bArr2[61] | 16);
            bArr2[71] = (byte) (bArr2[71] | 32);
            bArr2[77] = (byte) (bArr2[77] | 1);
            bArr2[86] = (byte) (bArr2[86] | 8);
            bArr2[100] = (byte) (bArr2[100] | 2);
            bArr2[111] = (byte) (bArr2[111] | 4);
            bArr2[117] = (byte) (bArr2[117] | 32);
            bArr2[124] = (byte) (bArr2[124] | 16);
        }
        if ((b7 & 4) != 0) {
            bArr2[12] = (byte) (bArr2[12] | 8);
            bArr2[29] = (byte) (bArr2[29] | 16);
            bArr2[36] = (byte) (bArr2[36] | 4);
            bArr2[47] = (byte) (bArr2[47] | 16);
            bArr2[52] = (byte) (bArr2[52] | 32);
            bArr2[62] = (byte) (bArr2[62] | 32);
            bArr2[68] = (byte) (bArr2[68] | 2);
            bArr2[79] = (byte) (bArr2[79] | 4);
            bArr2[85] = (byte) (bArr2[85] | 32);
            bArr2[95] = (byte) (bArr2[95] | 1);
            bArr2[102] = (byte) (bArr2[102] | 1);
            bArr2[111] = (byte) (bArr2[111] | 2);
            bArr2[117] = (byte) (bArr2[117] | 2);
            bArr2[126] = (byte) (bArr2[126] | 4);
        }
        if ((b7 & 2) != 0) {
            bArr2[5] = (byte) (bArr2[5] | 1);
            bArr2[15] = (byte) (bArr2[15] | 16);
            bArr2[20] = (byte) (bArr2[20] | 32);
            bArr2[30] = (byte) (bArr2[30] | 32);
            bArr2[37] = (byte) (bArr2[37] | 4);
            bArr2[46] = (byte) (bArr2[46] | 2);
            bArr2[52] = (byte) (bArr2[52] | 16);
            bArr2[61] = (byte) (bArr2[61] | 8);
            bArr2[70] = (byte) (bArr2[70] | 1);
            bArr2[79] = (byte) (bArr2[79] | 2);
            bArr2[85] = (byte) (bArr2[85] | 2);
            bArr2[103] = (byte) (bArr2[103] | 8);
            bArr2[110] = (byte) (bArr2[110] | 16);
            bArr2[119] = (byte) (bArr2[119] | 32);
            bArr2[124] = (byte) (bArr2[124] | 4);
        }
        byte b8 = bArr[6];
        if ((b8 & 128) != 0) {
            bArr2[0] = (byte) (bArr2[0] | 16);
            bArr2[9] = (byte) (bArr2[9] | 2);
            bArr2[18] = (byte) (bArr2[18] | 16);
            bArr2[24] = (byte) (bArr2[24] | 8);
            bArr2[43] = (byte) (bArr2[43] | 8);
            bArr2[59] = (byte) (bArr2[59] | 1);
            bArr2[65] = (byte) (bArr2[65] | 4);
            bArr2[75] = (byte) (bArr2[75] | 16);
            bArr2[80] = (byte) (bArr2[80] | 4);
            bArr2[99] = (byte) (bArr2[99] | 2);
            bArr2[104] = (byte) (bArr2[104] | 32);
            bArr2[113] = (byte) (bArr2[113] | 1);
            bArr2[123] = (byte) (bArr2[123] | 32);
        }
        if ((b8 & 64) != 0) {
            bArr2[10] = (byte) (bArr2[10] | 32);
            bArr2[17] = (byte) (bArr2[17] | 8);
            bArr2[26] = (byte) (bArr2[26] | 8);
            bArr2[35] = (byte) (bArr2[35] | 32);
            bArr2[41] = (byte) (bArr2[41] | 2);
            bArr2[50] = (byte) (bArr2[50] | 16);
            bArr2[56] = (byte) (bArr2[56] | 8);
            bArr2[66] = (byte) (bArr2[66] | 1);
            bArr2[73] = (byte) (bArr2[73] | 16);
            bArr2[82] = (byte) (bArr2[82] | 2);
            bArr2[88] = (byte) (bArr2[88] | 2);
            bArr2[97] = (byte) (bArr2[97] | 4);
            bArr2[107] = (byte) (bArr2[107] | 16);
            bArr2[112] = (byte) (bArr2[112] | 4);
            bArr2[121] = (byte) (bArr2[121] | 32);
        }
        if ((b8 & 32) != 0) {
            bArr2[0] = (byte) (bArr2[0] | 2);
            bArr2[11] = (byte) (bArr2[11] | 1);
            bArr2[16] = (byte) (bArr2[16] | 1);
            bArr2[26] = (byte) (bArr2[26] | 4);
            bArr2[33] = (byte) (bArr2[33] | 32);
            bArr2[42] = (byte) (bArr2[42] | 32);
            bArr2[49] = (byte) (bArr2[49] | 8);
            bArr2[58] = (byte) (bArr2[58] | 8);
            bArr2[65] = (byte) (bArr2[65] | 1);
            bArr2[72] = (byte) (bArr2[72] | 16);
            bArr2[83] = (byte) (bArr2[83] | 4);
            bArr2[98] = (byte) (bArr2[98] | 1);
            bArr2[105] = (byte) (bArr2[105] | 16);
            bArr2[114] = (byte) (bArr2[114] | 2);
        }
        if ((b8 & 16) != 0) {
            bArr2[8] = (byte) (bArr2[8] | 8);
            bArr2[27] = (byte) (bArr2[27] | 8);
            bArr2[43] = (byte) (bArr2[43] | 1);
            bArr2[48] = (byte) (bArr2[48] | 1);
            bArr2[58] = (byte) (bArr2[58] | 4);
            bArr2[64] = (byte) (bArr2[64] | 4);
            bArr2[83] = (byte) (bArr2[83] | 2);
            bArr2[88] = (byte) (bArr2[88] | 32);
            bArr2[97] = (byte) (bArr2[97] | 1);
            bArr2[104] = (byte) (bArr2[104] | 16);
            bArr2[115] = (byte) (bArr2[115] | 4);
            bArr2[122] = (byte) (bArr2[122] | 16);
        }
        if ((b8 & 8) != 0) {
            bArr2[5] = (byte) (bArr2[5] | 8);
            bArr2[14] = (byte) (bArr2[14] | 1);
            bArr2[23] = (byte) (bArr2[23] | 2);
            bArr2[29] = (byte) (bArr2[29] | 2);
            bArr2[47] = (byte) (bArr2[47] | 8);
            bArr2[54] = (byte) (bArr2[54] | 16);
            bArr2[63] = (byte) (bArr2[63] | 32);
            bArr2[68] = (byte) (bArr2[68] | 4);
            bArr2[79] = (byte) (bArr2[79] | 16);
            bArr2[84] = (byte) (bArr2[84] | 32);
            bArr2[94] = (byte) (bArr2[94] | 32);
            bArr2[101] = (byte) (bArr2[101] | 4);
            bArr2[110] = (byte) (bArr2[110] | 2);
            bArr2[116] = (byte) (bArr2[116] | 16);
            bArr2[127] = (byte) (bArr2[127] | 1);
        }
        if ((b8 & 4) != 0) {
            bArr2[4] = (byte) (bArr2[4] | 8);
            bArr2[15] = (byte) (bArr2[15] | 8);
            bArr2[22] = (byte) (bArr2[22] | 16);
            bArr2[31] = (byte) (bArr2[31] | 32);
            bArr2[37] = (byte) (bArr2[37] | 1);
            bArr2[46] = (byte) (bArr2[46] | 8);
            bArr2[60] = (byte) (bArr2[60] | 2);
            bArr2[69] = (byte) (bArr2[69] | 4);
            bArr2[78] = (byte) (bArr2[78] | 2);
            bArr2[84] = (byte) (bArr2[84] | 16);
            bArr2[93] = (byte) (bArr2[93] | 8);
            bArr2[108] = (byte) (bArr2[108] | 1);
            bArr2[118] = (byte) (bArr2[118] | 4);
        }
        if ((b8 & 2) != 0) {
            bArr2[7] = (byte) (bArr2[7] | 16);
            bArr2[14] = (byte) (bArr2[14] | 8);
            bArr2[28] = (byte) (bArr2[28] | 2);
            bArr2[39] = (byte) (bArr2[39] | 4);
            bArr2[45] = (byte) (bArr2[45] | 32);
            bArr2[55] = (byte) (bArr2[55] | 1);
            bArr2[62] = (byte) (bArr2[62] | 1);
            bArr2[76] = (byte) (bArr2[76] | 1);
            bArr2[86] = (byte) (bArr2[86] | 4);
            bArr2[92] = (byte) (bArr2[92] | 8);
            bArr2[109] = (byte) (bArr2[109] | 16);
            bArr2[116] = (byte) (bArr2[116] | 4);
            bArr2[125] = (byte) (bArr2[125] | 1);
        }
        byte b9 = bArr[7];
        if ((b9 & 128) != 0) {
            bArr2[1] = (byte) (bArr2[1] | 2);
            bArr2[11] = (byte) (bArr2[11] | 4);
            bArr2[26] = (byte) (bArr2[26] | 1);
            bArr2[33] = (byte) (bArr2[33] | 16);
            bArr2[42] = (byte) (bArr2[42] | 2);
            bArr2[48] = (byte) (bArr2[48] | 2);
            bArr2[57] = (byte) (bArr2[57] | 4);
            bArr2[64] = (byte) (bArr2[64] | 1);
            bArr2[74] = (byte) (bArr2[74] | 4);
            bArr2[81] = (byte) (bArr2[81] | 32);
            bArr2[90] = (byte) (bArr2[90] | 32);
            bArr2[97] = (byte) (bArr2[97] | 8);
            bArr2[106] = (byte) (bArr2[106] | 8);
            bArr2[115] = (byte) (bArr2[115] | 32);
            bArr2[120] = (byte) (bArr2[120] | 16);
        }
        if ((b9 & 64) != 0) {
            bArr2[2] = (byte) (bArr2[2] | 32);
            bArr2[11] = (byte) (bArr2[11] | 2);
            bArr2[16] = (byte) (bArr2[16] | 32);
            bArr2[25] = (byte) (bArr2[25] | 1);
            bArr2[32] = (byte) (bArr2[32] | 16);
            bArr2[43] = (byte) (bArr2[43] | 4);
            bArr2[58] = (byte) (bArr2[58] | 1);
            bArr2[75] = (byte) (bArr2[75] | 8);
            bArr2[91] = (byte) (bArr2[91] | 1);
            bArr2[96] = (byte) (bArr2[96] | 1);
            bArr2[106] = (byte) (bArr2[106] | 4);
            bArr2[113] = (byte) (bArr2[113] | 32);
        }
        if ((b9 & 32) != 0) {
            bArr2[3] = (byte) (bArr2[3] | 1);
            bArr2[9] = (byte) (bArr2[9] | 4);
            bArr2[19] = (byte) (bArr2[19] | 16);
            bArr2[24] = (byte) (bArr2[24] | 4);
            bArr2[43] = (byte) (bArr2[43] | 2);
            bArr2[48] = (byte) (bArr2[48] | 32);
            bArr2[57] = (byte) (bArr2[57] | 1);
            bArr2[67] = (byte) (bArr2[67] | 32);
            bArr2[73] = (byte) (bArr2[73] | 2);
            bArr2[82] = (byte) (bArr2[82] | 16);
            bArr2[88] = (byte) (bArr2[88] | 8);
            bArr2[107] = (byte) (bArr2[107] | 8);
            bArr2[120] = (byte) (bArr2[120] | 2);
        }
        if ((b9 & 16) != 0) {
            bArr2[0] = (byte) (bArr2[0] | 8);
            bArr2[10] = (byte) (bArr2[10] | 1);
            bArr2[17] = (byte) (bArr2[17] | 16);
            bArr2[26] = (byte) (bArr2[26] | 2);
            bArr2[32] = (byte) (bArr2[32] | 2);
            bArr2[41] = (byte) (bArr2[41] | 4);
            bArr2[51] = (byte) (bArr2[51] | 16);
            bArr2[56] = (byte) (bArr2[56] | 4);
            bArr2[65] = (byte) (bArr2[65] | 32);
            bArr2[74] = (byte) (bArr2[74] | 32);
            bArr2[81] = (byte) (bArr2[81] | 8);
            bArr2[90] = (byte) (bArr2[90] | 8);
            bArr2[99] = (byte) (bArr2[99] | 32);
            bArr2[105] = (byte) (bArr2[105] | 2);
            bArr2[114] = (byte) (bArr2[114] | 16);
        }
        if ((b9 & 8) != 0) {
            bArr2[6] = (byte) (bArr2[6] | 1);
            bArr2[20] = (byte) (bArr2[20] | 1);
            bArr2[30] = (byte) (bArr2[30] | 4);
            bArr2[36] = (byte) (bArr2[36] | 8);
            bArr2[53] = (byte) (bArr2[53] | 16);
            bArr2[60] = (byte) (bArr2[60] | 4);
            bArr2[69] = (byte) (bArr2[69] | 1);
            bArr2[78] = (byte) (bArr2[78] | 8);
            bArr2[92] = (byte) (bArr2[92] | 2);
            bArr2[103] = (byte) (bArr2[103] | 4);
            bArr2[109] = (byte) (bArr2[109] | 32);
            bArr2[119] = (byte) (bArr2[119] | 1);
            bArr2[125] = (byte) (bArr2[125] | 8);
        }
        if ((b9 & 4) != 0) {
            bArr2[7] = (byte) (bArr2[7] | 8);
            bArr2[21] = (byte) (bArr2[21] | 16);
            bArr2[28] = (byte) (bArr2[28] | 4);
            bArr2[39] = (byte) (bArr2[39] | 16);
            bArr2[44] = (byte) (bArr2[44] | 32);
            bArr2[54] = (byte) (bArr2[54] | 32);
            bArr2[61] = (byte) (bArr2[61] | 4);
            bArr2[71] = (byte) (bArr2[71] | 4);
            bArr2[77] = (byte) (bArr2[77] | 32);
            bArr2[87] = (byte) (bArr2[87] | 1);
            bArr2[94] = (byte) (bArr2[94] | 1);
            bArr2[103] = (byte) (bArr2[103] | 2);
            bArr2[109] = (byte) (bArr2[109] | 2);
            bArr2[124] = (byte) (bArr2[124] | 8);
        }
        if ((b9 & 2) != 0) {
            bArr2[6] = (byte) (bArr2[6] | 8);
            bArr2[12] = (byte) (bArr2[12] | 32);
            bArr2[22] = (byte) (bArr2[22] | 32);
            bArr2[29] = (byte) (bArr2[29] | 4);
            bArr2[38] = (byte) (bArr2[38] | 2);
            bArr2[44] = (byte) (bArr2[44] | 16);
            bArr2[53] = (byte) (bArr2[53] | 8);
            bArr2[71] = (byte) (bArr2[71] | 2);
            bArr2[77] = (byte) (bArr2[77] | 2);
            bArr2[95] = (byte) (bArr2[95] | 8);
            bArr2[102] = (byte) (bArr2[102] | 16);
            bArr2[111] = (byte) (bArr2[111] | 32);
            bArr2[117] = (byte) (bArr2[117] | 1);
            bArr2[127] = (byte) (bArr2[127] | 16);
        }
        this.expandedKey = bArr2;
    }
}
