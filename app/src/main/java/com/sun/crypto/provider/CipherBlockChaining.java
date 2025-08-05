package com.sun.crypto.provider;

import java.security.InvalidKeyException;

/* loaded from: sunjce_provider.jar:com/sun/crypto/provider/CipherBlockChaining.class */
class CipherBlockChaining extends FeedbackCipher {

    /* renamed from: r, reason: collision with root package name */
    protected byte[] f11807r;

    /* renamed from: k, reason: collision with root package name */
    private byte[] f11808k;
    private byte[] rSave;

    CipherBlockChaining(SymmetricCipher symmetricCipher) {
        super(symmetricCipher);
        this.rSave = null;
        this.f11808k = new byte[this.blockSize];
        this.f11807r = new byte[this.blockSize];
    }

    @Override // com.sun.crypto.provider.FeedbackCipher
    String getFeedback() {
        return "CBC";
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
        System.arraycopy(this.iv, 0, this.f11807r, 0, this.blockSize);
    }

    @Override // com.sun.crypto.provider.FeedbackCipher
    void save() {
        if (this.rSave == null) {
            this.rSave = new byte[this.blockSize];
        }
        System.arraycopy(this.f11807r, 0, this.rSave, 0, this.blockSize);
    }

    @Override // com.sun.crypto.provider.FeedbackCipher
    void restore() {
        System.arraycopy(this.rSave, 0, this.f11807r, 0, this.blockSize);
    }

    @Override // com.sun.crypto.provider.FeedbackCipher
    int encrypt(byte[] bArr, int i2, int i3, byte[] bArr2, int i4) {
        if (i3 <= 0) {
            return i3;
        }
        RangeUtil.blockSizeCheck(i3, this.blockSize);
        RangeUtil.nullAndBoundsCheck(bArr, i2, i3);
        RangeUtil.nullAndBoundsCheck(bArr2, i4, i3);
        return implEncrypt(bArr, i2, i3, bArr2, i4);
    }

    private int implEncrypt(byte[] bArr, int i2, int i3, byte[] bArr2, int i4) {
        int i5 = i2 + i3;
        while (i2 < i5) {
            for (int i6 = 0; i6 < this.blockSize; i6++) {
                this.f11808k[i6] = (byte) (bArr[i6 + i2] ^ this.f11807r[i6]);
            }
            this.embeddedCipher.encryptBlock(this.f11808k, 0, bArr2, i4);
            System.arraycopy(bArr2, i4, this.f11807r, 0, this.blockSize);
            i2 += this.blockSize;
            i4 += this.blockSize;
        }
        return i3;
    }

    @Override // com.sun.crypto.provider.FeedbackCipher
    int decrypt(byte[] bArr, int i2, int i3, byte[] bArr2, int i4) {
        if (i3 <= 0) {
            return i3;
        }
        RangeUtil.blockSizeCheck(i3, this.blockSize);
        RangeUtil.nullAndBoundsCheck(bArr, i2, i3);
        RangeUtil.nullAndBoundsCheck(bArr2, i4, i3);
        return implDecrypt(bArr, i2, i3, bArr2, i4);
    }

    private int implDecrypt(byte[] bArr, int i2, int i3, byte[] bArr2, int i4) {
        int i5 = i2 + i3;
        while (i2 < i5) {
            this.embeddedCipher.decryptBlock(bArr, i2, this.f11808k, 0);
            for (int i6 = 0; i6 < this.blockSize; i6++) {
                bArr2[i6 + i4] = (byte) (this.f11808k[i6] ^ this.f11807r[i6]);
            }
            System.arraycopy(bArr, i2, this.f11807r, 0, this.blockSize);
            i2 += this.blockSize;
            i4 += this.blockSize;
        }
        return i3;
    }
}
