package com.sun.crypto.provider;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import javax.crypto.AEADBadTagException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.ShortBufferException;

/* loaded from: sunjce_provider.jar:com/sun/crypto/provider/FeedbackCipher.class */
abstract class FeedbackCipher {
    final SymmetricCipher embeddedCipher;
    final int blockSize;
    byte[] iv;

    abstract String getFeedback();

    abstract void save();

    abstract void restore();

    abstract void init(boolean z2, String str, byte[] bArr, byte[] bArr2) throws InvalidKeyException, InvalidAlgorithmParameterException;

    abstract void reset();

    abstract int encrypt(byte[] bArr, int i2, int i3, byte[] bArr2, int i4);

    abstract int decrypt(byte[] bArr, int i2, int i3, byte[] bArr2, int i4);

    FeedbackCipher(SymmetricCipher symmetricCipher) {
        this.embeddedCipher = symmetricCipher;
        this.blockSize = symmetricCipher.getBlockSize();
    }

    final SymmetricCipher getEmbeddedCipher() {
        return this.embeddedCipher;
    }

    final int getBlockSize() {
        return this.blockSize;
    }

    final byte[] getIV() {
        return this.iv;
    }

    int encryptFinal(byte[] bArr, int i2, int i3, byte[] bArr2, int i4) throws IllegalBlockSizeException, ShortBufferException {
        return encrypt(bArr, i2, i3, bArr2, i4);
    }

    int decryptFinal(byte[] bArr, int i2, int i3, byte[] bArr2, int i4) throws IllegalBlockSizeException, AEADBadTagException, ShortBufferException {
        return decrypt(bArr, i2, i3, bArr2, i4);
    }

    void updateAAD(byte[] bArr, int i2, int i3) {
        throw new IllegalStateException("No AAD accepted");
    }

    int getBufferedLength() {
        return 0;
    }
}
