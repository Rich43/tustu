package com.sun.crypto.provider;

import java.security.AlgorithmParameters;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.AlgorithmParameterSpec;
import javax.crypto.CipherSpi;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.ShortBufferException;

/* loaded from: sunjce_provider.jar:com/sun/crypto/provider/ARCFOURCipher.class */
public final class ARCFOURCipher extends CipherSpi {

    /* renamed from: S, reason: collision with root package name */
    private final int[] f11805S = new int[256];
    private int is;
    private int js;
    private byte[] lastKey;

    private void init(byte[] bArr) {
        for (int i2 = 0; i2 < 256; i2++) {
            this.f11805S[i2] = i2;
        }
        int i3 = 0;
        int i4 = 0;
        for (int i5 = 0; i5 < 256; i5++) {
            int i6 = this.f11805S[i5];
            i3 = (i3 + i6 + bArr[i4]) & 255;
            this.f11805S[i5] = this.f11805S[i3];
            this.f11805S[i3] = i6;
            i4++;
            if (i4 == bArr.length) {
                i4 = 0;
            }
        }
        this.is = 0;
        this.js = 0;
    }

    private void crypt(byte[] bArr, int i2, int i3, byte[] bArr2, int i4) {
        if (this.is < 0) {
            init(this.lastKey);
        }
        while (true) {
            int i5 = i3;
            i3--;
            if (i5 > 0) {
                this.is = (this.is + 1) & 255;
                int i6 = this.f11805S[this.is];
                this.js = (this.js + i6) & 255;
                int i7 = this.f11805S[this.js];
                this.f11805S[this.is] = i7;
                this.f11805S[this.js] = i6;
                int i8 = i4;
                i4++;
                int i9 = i2;
                i2++;
                bArr2[i8] = (byte) (bArr[i9] ^ this.f11805S[(i6 + i7) & 255]);
            } else {
                return;
            }
        }
    }

    @Override // javax.crypto.CipherSpi
    protected void engineSetMode(String str) throws NoSuchAlgorithmException {
        if (!str.equalsIgnoreCase("ECB")) {
            throw new NoSuchAlgorithmException("Unsupported mode " + str);
        }
    }

    @Override // javax.crypto.CipherSpi
    protected void engineSetPadding(String str) throws NoSuchPaddingException {
        if (!str.equalsIgnoreCase("NoPadding")) {
            throw new NoSuchPaddingException("Padding must be NoPadding");
        }
    }

    @Override // javax.crypto.CipherSpi
    protected int engineGetBlockSize() {
        return 0;
    }

    @Override // javax.crypto.CipherSpi
    protected int engineGetOutputSize(int i2) {
        return i2;
    }

    @Override // javax.crypto.CipherSpi
    protected byte[] engineGetIV() {
        return null;
    }

    @Override // javax.crypto.CipherSpi
    protected AlgorithmParameters engineGetParameters() {
        return null;
    }

    @Override // javax.crypto.CipherSpi
    protected void engineInit(int i2, Key key, SecureRandom secureRandom) throws InvalidKeyException {
        init(i2, key);
    }

    @Override // javax.crypto.CipherSpi
    protected void engineInit(int i2, Key key, AlgorithmParameterSpec algorithmParameterSpec, SecureRandom secureRandom) throws InvalidKeyException, InvalidAlgorithmParameterException {
        if (algorithmParameterSpec != null) {
            throw new InvalidAlgorithmParameterException("Parameters not supported");
        }
        init(i2, key);
    }

    @Override // javax.crypto.CipherSpi
    protected void engineInit(int i2, Key key, AlgorithmParameters algorithmParameters, SecureRandom secureRandom) throws InvalidKeyException, InvalidAlgorithmParameterException {
        if (algorithmParameters != null) {
            throw new InvalidAlgorithmParameterException("Parameters not supported");
        }
        init(i2, key);
    }

    private void init(int i2, Key key) throws InvalidKeyException {
        if (i2 < 1 || i2 > 4) {
            throw new InvalidKeyException("Unknown opmode: " + i2);
        }
        this.lastKey = getEncodedKey(key);
        init(this.lastKey);
    }

    private static byte[] getEncodedKey(Key key) throws InvalidKeyException {
        String algorithm = key.getAlgorithm();
        if (!algorithm.equals("RC4") && !algorithm.equals("ARCFOUR")) {
            throw new InvalidKeyException("Not an ARCFOUR key: " + algorithm);
        }
        if (!"RAW".equals(key.getFormat())) {
            throw new InvalidKeyException("Key encoding format must be RAW");
        }
        byte[] encoded = key.getEncoded();
        if (encoded.length < 5 || encoded.length > 128) {
            throw new InvalidKeyException("Key length must be between 40 and 1024 bit");
        }
        return encoded;
    }

    @Override // javax.crypto.CipherSpi
    protected byte[] engineUpdate(byte[] bArr, int i2, int i3) {
        byte[] bArr2 = new byte[i3];
        crypt(bArr, i2, i3, bArr2, 0);
        return bArr2;
    }

    @Override // javax.crypto.CipherSpi
    protected int engineUpdate(byte[] bArr, int i2, int i3, byte[] bArr2, int i4) throws ShortBufferException {
        if (bArr2.length - i4 < i3) {
            throw new ShortBufferException("Output buffer too small");
        }
        crypt(bArr, i2, i3, bArr2, i4);
        return i3;
    }

    @Override // javax.crypto.CipherSpi
    protected byte[] engineDoFinal(byte[] bArr, int i2, int i3) {
        byte[] bArrEngineUpdate = engineUpdate(bArr, i2, i3);
        this.is = -1;
        return bArrEngineUpdate;
    }

    @Override // javax.crypto.CipherSpi
    protected int engineDoFinal(byte[] bArr, int i2, int i3, byte[] bArr2, int i4) throws ShortBufferException {
        int iEngineUpdate = engineUpdate(bArr, i2, i3, bArr2, i4);
        this.is = -1;
        return iEngineUpdate;
    }

    @Override // javax.crypto.CipherSpi
    protected byte[] engineWrap(Key key) throws IllegalBlockSizeException, InvalidKeyException {
        byte[] encoded = key.getEncoded();
        if (encoded == null || encoded.length == 0) {
            throw new InvalidKeyException("Could not obtain encoded key");
        }
        return engineDoFinal(encoded, 0, encoded.length);
    }

    @Override // javax.crypto.CipherSpi
    protected Key engineUnwrap(byte[] bArr, String str, int i2) throws NoSuchAlgorithmException, InvalidKeyException {
        return ConstructKeys.constructKey(engineDoFinal(bArr, 0, bArr.length), str, i2);
    }

    @Override // javax.crypto.CipherSpi
    protected int engineGetKeySize(Key key) throws InvalidKeyException {
        return Math.multiplyExact(getEncodedKey(key).length, 8);
    }
}
