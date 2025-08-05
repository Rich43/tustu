package com.sun.crypto.provider;

import java.security.AlgorithmParameters;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.AlgorithmParameterSpec;
import javax.crypto.BadPaddingException;
import javax.crypto.CipherSpi;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.ShortBufferException;

/* loaded from: sunjce_provider.jar:com/sun/crypto/provider/BlowfishCipher.class */
public final class BlowfishCipher extends CipherSpi {
    private CipherCore core;

    public BlowfishCipher() {
        this.core = null;
        this.core = new CipherCore(new BlowfishCrypt(), 8);
    }

    @Override // javax.crypto.CipherSpi
    protected void engineSetMode(String str) throws NoSuchAlgorithmException {
        this.core.setMode(str);
    }

    @Override // javax.crypto.CipherSpi
    protected void engineSetPadding(String str) throws NoSuchPaddingException {
        this.core.setPadding(str);
    }

    @Override // javax.crypto.CipherSpi
    protected int engineGetBlockSize() {
        return 8;
    }

    @Override // javax.crypto.CipherSpi
    protected int engineGetOutputSize(int i2) {
        return this.core.getOutputSize(i2);
    }

    @Override // javax.crypto.CipherSpi
    protected byte[] engineGetIV() {
        return this.core.getIV();
    }

    @Override // javax.crypto.CipherSpi
    protected AlgorithmParameters engineGetParameters() {
        return this.core.getParameters("Blowfish");
    }

    @Override // javax.crypto.CipherSpi
    protected void engineInit(int i2, Key key, SecureRandom secureRandom) throws InvalidKeyException {
        this.core.init(i2, key, secureRandom);
    }

    @Override // javax.crypto.CipherSpi
    protected void engineInit(int i2, Key key, AlgorithmParameterSpec algorithmParameterSpec, SecureRandom secureRandom) throws InvalidKeyException, InvalidAlgorithmParameterException {
        this.core.init(i2, key, algorithmParameterSpec, secureRandom);
    }

    @Override // javax.crypto.CipherSpi
    protected void engineInit(int i2, Key key, AlgorithmParameters algorithmParameters, SecureRandom secureRandom) throws InvalidKeyException, InvalidAlgorithmParameterException {
        this.core.init(i2, key, algorithmParameters, secureRandom);
    }

    @Override // javax.crypto.CipherSpi
    protected byte[] engineUpdate(byte[] bArr, int i2, int i3) {
        return this.core.update(bArr, i2, i3);
    }

    @Override // javax.crypto.CipherSpi
    protected int engineUpdate(byte[] bArr, int i2, int i3, byte[] bArr2, int i4) throws ShortBufferException {
        return this.core.update(bArr, i2, i3, bArr2, i4);
    }

    @Override // javax.crypto.CipherSpi
    protected byte[] engineDoFinal(byte[] bArr, int i2, int i3) throws BadPaddingException, IllegalBlockSizeException {
        return this.core.doFinal(bArr, i2, i3);
    }

    @Override // javax.crypto.CipherSpi
    protected int engineDoFinal(byte[] bArr, int i2, int i3, byte[] bArr2, int i4) throws BadPaddingException, IllegalBlockSizeException, ShortBufferException {
        return this.core.doFinal(bArr, i2, i3, bArr2, i4);
    }

    @Override // javax.crypto.CipherSpi
    protected int engineGetKeySize(Key key) throws InvalidKeyException {
        return Math.multiplyExact(key.getEncoded().length, 8);
    }

    @Override // javax.crypto.CipherSpi
    protected byte[] engineWrap(Key key) throws IllegalBlockSizeException, InvalidKeyException {
        return this.core.wrap(key);
    }

    @Override // javax.crypto.CipherSpi
    protected Key engineUnwrap(byte[] bArr, String str, int i2) throws NoSuchAlgorithmException, InvalidKeyException {
        return this.core.unwrap(bArr, str, i2);
    }
}
