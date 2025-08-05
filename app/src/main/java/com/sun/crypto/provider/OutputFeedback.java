package com.sun.crypto.provider;

import java.security.InvalidKeyException;

/* loaded from: sunjce_provider.jar:com/sun/crypto/provider/OutputFeedback.class */
final class OutputFeedback extends FeedbackCipher {

    /* renamed from: k, reason: collision with root package name */
    private byte[] f11825k;
    private byte[] register;
    private int numBytes;
    private byte[] registerSave;

    OutputFeedback(SymmetricCipher symmetricCipher, int i2) {
        super(symmetricCipher);
        this.f11825k = null;
        this.register = null;
        this.registerSave = null;
        this.numBytes = i2 > this.blockSize ? this.blockSize : i2;
        this.f11825k = new byte[this.blockSize];
        this.register = new byte[this.blockSize];
    }

    @Override // com.sun.crypto.provider.FeedbackCipher
    String getFeedback() {
        return "OFB";
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
    void reset() {
        System.arraycopy(this.iv, 0, this.register, 0, this.blockSize);
    }

    @Override // com.sun.crypto.provider.FeedbackCipher
    void save() {
        if (this.registerSave == null) {
            this.registerSave = new byte[this.blockSize];
        }
        System.arraycopy(this.register, 0, this.registerSave, 0, this.blockSize);
    }

    @Override // com.sun.crypto.provider.FeedbackCipher
    void restore() {
        System.arraycopy(this.registerSave, 0, this.register, 0, this.blockSize);
    }

    @Override // com.sun.crypto.provider.FeedbackCipher
    int encrypt(byte[] bArr, int i2, int i3, byte[] bArr2, int i4) {
        RangeUtil.blockSizeCheck(i3, this.numBytes);
        RangeUtil.nullAndBoundsCheck(bArr, i2, i3);
        RangeUtil.nullAndBoundsCheck(bArr2, i4, i3);
        int i5 = this.blockSize - this.numBytes;
        for (int i6 = i3 / this.numBytes; i6 > 0; i6--) {
            this.embeddedCipher.encryptBlock(this.register, 0, this.f11825k, 0);
            for (int i7 = 0; i7 < this.numBytes; i7++) {
                bArr2[i7 + i4] = (byte) (this.f11825k[i7] ^ bArr[i7 + i2]);
            }
            if (i5 != 0) {
                System.arraycopy(this.register, this.numBytes, this.register, 0, i5);
            }
            System.arraycopy(this.f11825k, 0, this.register, i5, this.numBytes);
            i2 += this.numBytes;
            i4 += this.numBytes;
        }
        return i3;
    }

    @Override // com.sun.crypto.provider.FeedbackCipher
    int encryptFinal(byte[] bArr, int i2, int i3, byte[] bArr2, int i4) {
        RangeUtil.nullAndBoundsCheck(bArr, i2, i3);
        RangeUtil.nullAndBoundsCheck(bArr2, i4, i3);
        int i5 = i3 % this.numBytes;
        int iEncrypt = encrypt(bArr, i2, i3 - i5, bArr2, i4);
        int i6 = i2 + iEncrypt;
        int i7 = i4 + iEncrypt;
        if (i5 != 0) {
            this.embeddedCipher.encryptBlock(this.register, 0, this.f11825k, 0);
            for (int i8 = 0; i8 < i5; i8++) {
                bArr2[i8 + i7] = (byte) (this.f11825k[i8] ^ bArr[i8 + i6]);
            }
        }
        return i3;
    }

    @Override // com.sun.crypto.provider.FeedbackCipher
    int decrypt(byte[] bArr, int i2, int i3, byte[] bArr2, int i4) {
        return encrypt(bArr, i2, i3, bArr2, i4);
    }

    @Override // com.sun.crypto.provider.FeedbackCipher
    int decryptFinal(byte[] bArr, int i2, int i3, byte[] bArr2, int i4) {
        return encryptFinal(bArr, i2, i3, bArr2, i4);
    }
}
