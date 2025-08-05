package com.sun.crypto.provider;

import java.security.InvalidKeyException;

/* loaded from: sunjce_provider.jar:com/sun/crypto/provider/DESedeCrypt.class */
final class DESedeCrypt extends DESCrypt implements DESConstants {
    private byte[] key1 = null;
    private byte[] key2 = null;
    private byte[] key3 = null;
    private byte[] buf1 = new byte[8];
    private byte[] buf2 = new byte[8];

    DESedeCrypt() {
    }

    @Override // com.sun.crypto.provider.DESCrypt, com.sun.crypto.provider.SymmetricCipher
    void init(boolean z2, String str, byte[] bArr) throws InvalidKeyException {
        if (!str.equalsIgnoreCase("DESede") && !str.equalsIgnoreCase("TripleDES")) {
            throw new InvalidKeyException("Wrong algorithm: DESede or TripleDES required");
        }
        if (bArr.length != 24) {
            throw new InvalidKeyException("Wrong key size");
        }
        byte[] bArr2 = new byte[8];
        this.key1 = new byte[128];
        System.arraycopy(bArr, 0, bArr2, 0, 8);
        expandKey(bArr2);
        System.arraycopy(this.expandedKey, 0, this.key1, 0, 128);
        if (keyEquals(bArr2, 0, bArr, 16, 8)) {
            this.key3 = this.key1;
        } else {
            this.key3 = new byte[128];
            System.arraycopy(bArr, 16, bArr2, 0, 8);
            expandKey(bArr2);
            System.arraycopy(this.expandedKey, 0, this.key3, 0, 128);
        }
        this.key2 = new byte[128];
        System.arraycopy(bArr, 8, bArr2, 0, 8);
        expandKey(bArr2);
        System.arraycopy(this.expandedKey, 0, this.key2, 0, 128);
    }

    @Override // com.sun.crypto.provider.DESCrypt, com.sun.crypto.provider.SymmetricCipher
    void encryptBlock(byte[] bArr, int i2, byte[] bArr2, int i3) {
        this.expandedKey = this.key1;
        this.decrypting = false;
        cipherBlock(bArr, i2, this.buf1, 0);
        this.expandedKey = this.key2;
        this.decrypting = true;
        cipherBlock(this.buf1, 0, this.buf2, 0);
        this.expandedKey = this.key3;
        this.decrypting = false;
        cipherBlock(this.buf2, 0, bArr2, i3);
    }

    @Override // com.sun.crypto.provider.DESCrypt, com.sun.crypto.provider.SymmetricCipher
    void decryptBlock(byte[] bArr, int i2, byte[] bArr2, int i3) {
        this.expandedKey = this.key3;
        this.decrypting = true;
        cipherBlock(bArr, i2, this.buf1, 0);
        this.expandedKey = this.key2;
        this.decrypting = false;
        cipherBlock(this.buf1, 0, this.buf2, 0);
        this.expandedKey = this.key1;
        this.decrypting = true;
        cipherBlock(this.buf2, 0, bArr2, i3);
    }

    private boolean keyEquals(byte[] bArr, int i2, byte[] bArr2, int i3, int i4) {
        for (int i5 = 0; i5 < i4; i5++) {
            if (bArr[i5 + i2] != bArr2[i5 + i3]) {
                return false;
            }
        }
        return true;
    }
}
