package com.sun.crypto.provider;

import java.security.InvalidKeyException;

/* loaded from: sunjce_provider.jar:com/sun/crypto/provider/PCBC.class */
final class PCBC extends FeedbackCipher {

    /* renamed from: k, reason: collision with root package name */
    private final byte[] f11826k;
    private byte[] kSave;

    PCBC(SymmetricCipher symmetricCipher) {
        super(symmetricCipher);
        this.kSave = null;
        this.f11826k = new byte[this.blockSize];
    }

    @Override // com.sun.crypto.provider.FeedbackCipher
    String getFeedback() {
        return "PCBC";
    }

    @Override // com.sun.crypto.provider.FeedbackCipher
    void init(boolean z2, String str, byte[] bArr, byte[] bArr2) throws InvalidKeyException {
        if (bArr == null || bArr2 == null || bArr2.length != this.blockSize) {
            throw new InvalidKeyException("Internal error");
        }
        this.iv = bArr2;
        reset();
        this.embeddedCipher.init(z2, str, bArr);
    }

    @Override // com.sun.crypto.provider.FeedbackCipher
    void reset() {
        System.arraycopy(this.iv, 0, this.f11826k, 0, this.blockSize);
    }

    @Override // com.sun.crypto.provider.FeedbackCipher
    void save() {
        if (this.kSave == null) {
            this.kSave = new byte[this.blockSize];
        }
        System.arraycopy(this.f11826k, 0, this.kSave, 0, this.blockSize);
    }

    @Override // com.sun.crypto.provider.FeedbackCipher
    void restore() {
        System.arraycopy(this.kSave, 0, this.f11826k, 0, this.blockSize);
    }

    @Override // com.sun.crypto.provider.FeedbackCipher
    int encrypt(byte[] bArr, int i2, int i3, byte[] bArr2, int i4) {
        RangeUtil.blockSizeCheck(i3, this.blockSize);
        RangeUtil.nullAndBoundsCheck(bArr, i2, i3);
        RangeUtil.nullAndBoundsCheck(bArr2, i4, i3);
        int i5 = i2 + i3;
        while (i2 < i5) {
            for (int i6 = 0; i6 < this.blockSize; i6++) {
                byte[] bArr3 = this.f11826k;
                int i7 = i6;
                bArr3[i7] = (byte) (bArr3[i7] ^ bArr[i6 + i2]);
            }
            this.embeddedCipher.encryptBlock(this.f11826k, 0, bArr2, i4);
            for (int i8 = 0; i8 < this.blockSize; i8++) {
                this.f11826k[i8] = (byte) (bArr[i8 + i2] ^ bArr2[i8 + i4]);
            }
            i2 += this.blockSize;
            i4 += this.blockSize;
        }
        return i3;
    }

    @Override // com.sun.crypto.provider.FeedbackCipher
    int decrypt(byte[] bArr, int i2, int i3, byte[] bArr2, int i4) {
        RangeUtil.blockSizeCheck(i3, this.blockSize);
        RangeUtil.nullAndBoundsCheck(bArr, i2, i3);
        RangeUtil.nullAndBoundsCheck(bArr2, i4, i3);
        int i5 = i2 + i3;
        while (i2 < i5) {
            this.embeddedCipher.decryptBlock(bArr, i2, bArr2, i4);
            for (int i6 = 0; i6 < this.blockSize; i6++) {
                int i7 = i6 + i4;
                bArr2[i7] = (byte) (bArr2[i7] ^ this.f11826k[i6]);
            }
            for (int i8 = 0; i8 < this.blockSize; i8++) {
                this.f11826k[i8] = (byte) (bArr2[i8 + i4] ^ bArr[i8 + i2]);
            }
            i4 += this.blockSize;
            i2 += this.blockSize;
        }
        return i3;
    }
}
