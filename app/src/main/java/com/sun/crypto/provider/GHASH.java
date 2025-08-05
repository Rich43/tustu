package com.sun.crypto.provider;

import java.security.ProviderException;

/* loaded from: sunjce_provider.jar:com/sun/crypto/provider/GHASH.class */
final class GHASH {
    private static final int AES_BLOCK_SIZE = 16;
    private final long[] subkeyH;
    private final long[] state;
    private long stateSave0;
    private long stateSave1;

    private static long getLong(byte[] bArr, int i2) {
        long j2 = 0;
        for (int i3 = i2; i3 < i2 + 8; i3++) {
            j2 = (j2 << 8) + (bArr[i3] & 255);
        }
        return j2;
    }

    private static void putLong(byte[] bArr, int i2, long j2) {
        for (int i3 = (i2 + 8) - 1; i3 >= i2; i3--) {
            bArr[i3] = (byte) j2;
            j2 >>= 8;
        }
    }

    private static void blockMult(long[] jArr, long[] jArr2) {
        long j2 = 0;
        long j3 = 0;
        long j4 = jArr2[0];
        long j5 = jArr2[1];
        long j6 = jArr[0];
        for (int i2 = 0; i2 < 64; i2++) {
            long j7 = j6 >> 63;
            j2 ^= j4 & j7;
            j3 ^= j5 & j7;
            long j8 = (j5 << 63) >> 63;
            j5 = (j5 >>> 1) | ((j4 & 1) << 63);
            j4 = (j4 >>> 1) ^ ((-2233785415175766016L) & j8);
            j6 <<= 1;
        }
        long j9 = jArr[1];
        for (int i3 = 64; i3 < 127; i3++) {
            long j10 = j9 >> 63;
            j2 ^= j4 & j10;
            j3 ^= j5 & j10;
            long j11 = (j5 << 63) >> 63;
            j5 = (j5 >>> 1) | ((j4 & 1) << 63);
            j4 = (j4 >>> 1) ^ ((-2233785415175766016L) & j11);
            j9 <<= 1;
        }
        long j12 = j9 >> 63;
        jArr[0] = j2 ^ (j4 & j12);
        jArr[1] = j3 ^ (j5 & j12);
    }

    GHASH(byte[] bArr) throws ProviderException {
        if (bArr == null || bArr.length != 16) {
            throw new ProviderException("Internal error");
        }
        this.state = new long[2];
        this.subkeyH = new long[2];
        this.subkeyH[0] = getLong(bArr, 0);
        this.subkeyH[1] = getLong(bArr, 8);
    }

    void reset() {
        this.state[0] = 0;
        this.state[1] = 0;
    }

    void save() {
        this.stateSave0 = this.state[0];
        this.stateSave1 = this.state[1];
    }

    void restore() {
        this.state[0] = this.stateSave0;
        this.state[1] = this.stateSave1;
    }

    private static void processBlock(byte[] bArr, int i2, long[] jArr, long[] jArr2) {
        jArr[0] = jArr[0] ^ getLong(bArr, i2);
        jArr[1] = jArr[1] ^ getLong(bArr, i2 + 8);
        blockMult(jArr, jArr2);
    }

    void update(byte[] bArr) {
        update(bArr, 0, bArr.length);
    }

    void update(byte[] bArr, int i2, int i3) {
        if (i3 == 0) {
            return;
        }
        ghashRangeCheck(bArr, i2, i3, this.state, this.subkeyH);
        processBlocks(bArr, i2, i3 / 16, this.state, this.subkeyH);
    }

    private static void ghashRangeCheck(byte[] bArr, int i2, int i3, long[] jArr, long[] jArr2) {
        if (i3 < 0) {
            throw new RuntimeException("invalid input length: " + i3);
        }
        if (i2 < 0) {
            throw new RuntimeException("invalid offset: " + i2);
        }
        if (i3 > bArr.length - i2) {
            throw new RuntimeException("input length out of bound: " + i3 + " > " + (bArr.length - i2));
        }
        if (i3 % 16 != 0) {
            throw new RuntimeException("input length/block size mismatch: " + i3);
        }
        if (jArr.length != 2) {
            throw new RuntimeException("internal state has invalid length: " + jArr.length);
        }
        if (jArr2.length != 2) {
            throw new RuntimeException("internal subkeyH has invalid length: " + jArr2.length);
        }
    }

    private static void processBlocks(byte[] bArr, int i2, int i3, long[] jArr, long[] jArr2) {
        int i4 = i2;
        while (i3 > 0) {
            processBlock(bArr, i4, jArr, jArr2);
            i3--;
            i4 += 16;
        }
    }

    byte[] digest() {
        byte[] bArr = new byte[16];
        putLong(bArr, 0, this.state[0]);
        putLong(bArr, 8, this.state[1]);
        reset();
        return bArr;
    }
}
