package com.sun.crypto.provider;

import javax.crypto.IllegalBlockSizeException;

/* loaded from: sunjce_provider.jar:com/sun/crypto/provider/CipherTextStealing.class */
final class CipherTextStealing extends CipherBlockChaining {
    CipherTextStealing(SymmetricCipher symmetricCipher) {
        super(symmetricCipher);
    }

    @Override // com.sun.crypto.provider.CipherBlockChaining, com.sun.crypto.provider.FeedbackCipher
    String getFeedback() {
        return "CTS";
    }

    @Override // com.sun.crypto.provider.FeedbackCipher
    int encryptFinal(byte[] bArr, int i2, int i3, byte[] bArr2, int i4) throws IllegalBlockSizeException {
        if (i3 < this.blockSize) {
            throw new IllegalBlockSizeException("input is too short!");
        }
        if (i3 == this.blockSize) {
            encrypt(bArr, i2, i3, bArr2, i4);
        } else {
            int i5 = i3 % this.blockSize;
            if (i5 == 0) {
                encrypt(bArr, i2, i3, bArr2, i4);
                int i6 = (i4 + i3) - this.blockSize;
                int i7 = i6 - this.blockSize;
                byte[] bArr3 = new byte[this.blockSize];
                System.arraycopy(bArr2, i6, bArr3, 0, this.blockSize);
                System.arraycopy(bArr2, i7, bArr2, i6, this.blockSize);
                System.arraycopy(bArr3, 0, bArr2, i7, this.blockSize);
            } else {
                int i8 = i3 - (this.blockSize + i5);
                if (i8 > 0) {
                    encrypt(bArr, i2, i8, bArr2, i4);
                    i2 += i8;
                    i4 += i8;
                }
                byte[] bArr4 = new byte[this.blockSize];
                for (int i9 = 0; i9 < this.blockSize; i9++) {
                    bArr4[i9] = (byte) (bArr[i2 + i9] ^ this.f11807r[i9]);
                }
                byte[] bArr5 = new byte[this.blockSize];
                this.embeddedCipher.encryptBlock(bArr4, 0, bArr5, 0);
                System.arraycopy(bArr5, 0, bArr2, i4 + this.blockSize, i5);
                for (int i10 = 0; i10 < i5; i10++) {
                    bArr5[i10] = (byte) (bArr[(i2 + this.blockSize) + i10] ^ bArr5[i10]);
                }
                this.embeddedCipher.encryptBlock(bArr5, 0, bArr2, i4);
            }
        }
        return i3;
    }

    @Override // com.sun.crypto.provider.FeedbackCipher
    int decryptFinal(byte[] bArr, int i2, int i3, byte[] bArr2, int i4) throws IllegalBlockSizeException {
        if (i3 < this.blockSize) {
            throw new IllegalBlockSizeException("input is too short!");
        }
        if (i3 == this.blockSize) {
            decrypt(bArr, i2, i3, bArr2, i4);
        } else {
            int i5 = i3 % this.blockSize;
            if (i5 == 0) {
                int i6 = (i2 + i3) - this.blockSize;
                int i7 = (i2 + i3) - (2 * this.blockSize);
                byte[] bArr3 = new byte[2 * this.blockSize];
                System.arraycopy(bArr, i6, bArr3, 0, this.blockSize);
                System.arraycopy(bArr, i7, bArr3, this.blockSize, this.blockSize);
                int i8 = i3 - (2 * this.blockSize);
                decrypt(bArr, i2, i8, bArr2, i4);
                decrypt(bArr3, 0, 2 * this.blockSize, bArr2, i4 + i8);
            } else {
                int i9 = i3 - (this.blockSize + i5);
                if (i9 > 0) {
                    decrypt(bArr, i2, i9, bArr2, i4);
                    i2 += i9;
                    i4 += i9;
                }
                byte[] bArr4 = new byte[this.blockSize];
                this.embeddedCipher.decryptBlock(bArr, i2, bArr4, 0);
                for (int i10 = 0; i10 < i5; i10++) {
                    bArr2[i4 + this.blockSize + i10] = (byte) (bArr[(i2 + this.blockSize) + i10] ^ bArr4[i10]);
                }
                System.arraycopy(bArr, i2 + this.blockSize, bArr4, 0, i5);
                this.embeddedCipher.decryptBlock(bArr4, 0, bArr2, i4);
                for (int i11 = 0; i11 < this.blockSize; i11++) {
                    bArr2[i4 + i11] = (byte) (bArr2[i4 + i11] ^ this.f11807r[i11]);
                }
            }
        }
        return i3;
    }
}
