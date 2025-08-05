package com.sun.crypto.provider;

import javax.crypto.IllegalBlockSizeException;

/* loaded from: sunjce_provider.jar:com/sun/crypto/provider/GCTR.class */
final class GCTR {
    private final SymmetricCipher aes;
    private final byte[] icb;
    private byte[] counter;
    private byte[] counterSave = null;

    GCTR(SymmetricCipher symmetricCipher, byte[] bArr) {
        this.aes = symmetricCipher;
        if (bArr.length != 16) {
            throw new RuntimeException("length of initial counter block (" + bArr.length + ") not equal to AES_BLOCK_SIZE (16)");
        }
        this.icb = bArr;
        this.counter = (byte[]) this.icb.clone();
    }

    int update(byte[] bArr, int i2, int i3, byte[] bArr2, int i4) {
        if (i3 - i2 > bArr.length) {
            throw new RuntimeException("input length out of bound");
        }
        if (i3 < 0 || i3 % 16 != 0) {
            throw new RuntimeException("input length unsupported");
        }
        if (bArr2.length - i4 < i3) {
            throw new RuntimeException("output buffer too small");
        }
        byte[] bArr3 = new byte[16];
        int i5 = i3 / 16;
        for (int i6 = 0; i6 < i5; i6++) {
            this.aes.encryptBlock(this.counter, 0, bArr3, 0);
            for (int i7 = 0; i7 < 16; i7++) {
                int i8 = (i6 * 16) + i7;
                bArr2[i4 + i8] = (byte) (bArr[i2 + i8] ^ bArr3[i7]);
            }
            GaloisCounterMode.increment32(this.counter);
        }
        return i3;
    }

    protected int doFinal(byte[] bArr, int i2, int i3, byte[] bArr2, int i4) throws IllegalBlockSizeException {
        try {
            if (i3 < 0) {
                throw new IllegalBlockSizeException("Negative input size!");
            }
            if (i3 > 0) {
                int i5 = i3 % 16;
                int i6 = i3 - i5;
                update(bArr, i2, i6, bArr2, i4);
                if (i5 != 0) {
                    byte[] bArr3 = new byte[16];
                    this.aes.encryptBlock(this.counter, 0, bArr3, 0);
                    for (int i7 = 0; i7 < i5; i7++) {
                        bArr2[i4 + i6 + i7] = (byte) (bArr[(i2 + i6) + i7] ^ bArr3[i7]);
                    }
                }
            }
            return i3;
        } finally {
            reset();
        }
    }

    void reset() {
        System.arraycopy(this.icb, 0, this.counter, 0, this.icb.length);
        this.counterSave = null;
    }

    void save() {
        this.counterSave = (byte[]) this.counter.clone();
    }

    void restore() {
        if (this.counterSave != null) {
            this.counter = this.counterSave;
        }
    }
}
