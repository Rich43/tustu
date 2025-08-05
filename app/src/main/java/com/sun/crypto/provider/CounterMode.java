package com.sun.crypto.provider;

import java.security.InvalidKeyException;

/* loaded from: sunjce_provider.jar:com/sun/crypto/provider/CounterMode.class */
final class CounterMode extends FeedbackCipher {
    private final byte[] counter;
    private final byte[] encryptedCounter;
    private int used;
    private byte[] counterSave;
    private byte[] encryptedCounterSave;
    private int usedSave;

    CounterMode(SymmetricCipher symmetricCipher) {
        super(symmetricCipher);
        this.counterSave = null;
        this.encryptedCounterSave = null;
        this.usedSave = 0;
        this.counter = new byte[this.blockSize];
        this.encryptedCounter = new byte[this.blockSize];
    }

    @Override // com.sun.crypto.provider.FeedbackCipher
    String getFeedback() {
        return "CTR";
    }

    @Override // com.sun.crypto.provider.FeedbackCipher
    void reset() {
        System.arraycopy(this.iv, 0, this.counter, 0, this.blockSize);
        this.used = this.blockSize;
    }

    @Override // com.sun.crypto.provider.FeedbackCipher
    void save() {
        if (this.counterSave == null) {
            this.counterSave = new byte[this.blockSize];
            this.encryptedCounterSave = new byte[this.blockSize];
        }
        System.arraycopy(this.counter, 0, this.counterSave, 0, this.blockSize);
        System.arraycopy(this.encryptedCounter, 0, this.encryptedCounterSave, 0, this.blockSize);
        this.usedSave = this.used;
    }

    @Override // com.sun.crypto.provider.FeedbackCipher
    void restore() {
        System.arraycopy(this.counterSave, 0, this.counter, 0, this.blockSize);
        System.arraycopy(this.encryptedCounterSave, 0, this.encryptedCounter, 0, this.blockSize);
        this.used = this.usedSave;
    }

    @Override // com.sun.crypto.provider.FeedbackCipher
    void init(boolean z2, String str, byte[] bArr, byte[] bArr2) throws InvalidKeyException {
        if (bArr == null || bArr2 == null || bArr2.length != this.blockSize) {
            throw new InvalidKeyException("Internal error");
        }
        this.iv = bArr2;
        reset();
        this.embeddedCipher.init(false, str, bArr);
    }

    @Override // com.sun.crypto.provider.FeedbackCipher
    int encrypt(byte[] bArr, int i2, int i3, byte[] bArr2, int i4) {
        return crypt(bArr, i2, i3, bArr2, i4);
    }

    @Override // com.sun.crypto.provider.FeedbackCipher
    int decrypt(byte[] bArr, int i2, int i3, byte[] bArr2, int i4) {
        return crypt(bArr, i2, i3, bArr2, i4);
    }

    private static void increment(byte[] bArr) {
        for (int length = bArr.length - 1; length >= 0; length--) {
            int i2 = length;
            byte b2 = (byte) (bArr[i2] + 1);
            bArr[i2] = b2;
            if (b2 != 0) {
                return;
            }
        }
    }

    private int crypt(byte[] bArr, int i2, int i3, byte[] bArr2, int i4) {
        if (i3 == 0) {
            return 0;
        }
        RangeUtil.nullAndBoundsCheck(bArr, i2, i3);
        RangeUtil.nullAndBoundsCheck(bArr2, i4, i3);
        while (true) {
            int i5 = i3;
            i3--;
            if (i5 > 0) {
                if (this.used >= this.blockSize) {
                    this.embeddedCipher.encryptBlock(this.counter, 0, this.encryptedCounter, 0);
                    increment(this.counter);
                    this.used = 0;
                }
                int i6 = i4;
                i4++;
                int i7 = i2;
                i2++;
                byte b2 = bArr[i7];
                byte[] bArr3 = this.encryptedCounter;
                int i8 = this.used;
                this.used = i8 + 1;
                bArr2[i6] = (byte) (b2 ^ bArr3[i8]);
            } else {
                return i3;
            }
        }
    }
}
