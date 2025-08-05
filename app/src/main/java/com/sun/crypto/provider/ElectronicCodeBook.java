package com.sun.crypto.provider;

import java.security.InvalidKeyException;

/* loaded from: sunjce_provider.jar:com/sun/crypto/provider/ElectronicCodeBook.class */
final class ElectronicCodeBook extends FeedbackCipher {
    ElectronicCodeBook(SymmetricCipher symmetricCipher) {
        super(symmetricCipher);
    }

    @Override // com.sun.crypto.provider.FeedbackCipher
    String getFeedback() {
        return "ECB";
    }

    @Override // com.sun.crypto.provider.FeedbackCipher
    void reset() {
    }

    @Override // com.sun.crypto.provider.FeedbackCipher
    void save() {
    }

    @Override // com.sun.crypto.provider.FeedbackCipher
    void restore() {
    }

    @Override // com.sun.crypto.provider.FeedbackCipher
    void init(boolean z2, String str, byte[] bArr, byte[] bArr2) throws InvalidKeyException {
        if (bArr == null || bArr2 != null) {
            throw new InvalidKeyException("Internal error");
        }
        this.embeddedCipher.init(z2, str, bArr);
    }

    @Override // com.sun.crypto.provider.FeedbackCipher
    int encrypt(byte[] bArr, int i2, int i3, byte[] bArr2, int i4) {
        RangeUtil.blockSizeCheck(i3, this.blockSize);
        RangeUtil.nullAndBoundsCheck(bArr, i2, i3);
        RangeUtil.nullAndBoundsCheck(bArr2, i4, i3);
        int i5 = i3;
        while (true) {
            int i6 = i5;
            if (i6 >= this.blockSize) {
                this.embeddedCipher.encryptBlock(bArr, i2, bArr2, i4);
                i2 += this.blockSize;
                i4 += this.blockSize;
                i5 = i6 - this.blockSize;
            } else {
                return i3;
            }
        }
    }

    @Override // com.sun.crypto.provider.FeedbackCipher
    int decrypt(byte[] bArr, int i2, int i3, byte[] bArr2, int i4) {
        RangeUtil.blockSizeCheck(i3, this.blockSize);
        RangeUtil.nullAndBoundsCheck(bArr, i2, i3);
        RangeUtil.nullAndBoundsCheck(bArr2, i4, i3);
        int i5 = i3;
        while (true) {
            int i6 = i5;
            if (i6 >= this.blockSize) {
                this.embeddedCipher.decryptBlock(bArr, i2, bArr2, i4);
                i2 += this.blockSize;
                i4 += this.blockSize;
                i5 = i6 - this.blockSize;
            } else {
                return i3;
            }
        }
    }
}
