package com.sun.crypto.provider;

import java.security.InvalidKeyException;

/* loaded from: sunjce_provider.jar:com/sun/crypto/provider/SymmetricCipher.class */
abstract class SymmetricCipher {
    abstract int getBlockSize();

    abstract void init(boolean z2, String str, byte[] bArr) throws InvalidKeyException;

    abstract void encryptBlock(byte[] bArr, int i2, byte[] bArr2, int i3);

    abstract void decryptBlock(byte[] bArr, int i2, byte[] bArr2, int i3);

    SymmetricCipher() {
    }
}
