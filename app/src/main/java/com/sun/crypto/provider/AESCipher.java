package com.sun.crypto.provider;

import java.nio.ByteBuffer;
import java.security.AlgorithmParameters;
import java.security.GeneralSecurityException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.ProviderException;
import java.security.SecureRandom;
import java.security.spec.AlgorithmParameterSpec;
import javax.crypto.BadPaddingException;
import javax.crypto.CipherSpi;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.ShortBufferException;

/* loaded from: sunjce_provider.jar:com/sun/crypto/provider/AESCipher.class */
abstract class AESCipher extends CipherSpi {
    private CipherCore core;
    private final int fixedKeySize;
    private boolean updateCalled;

    /* loaded from: sunjce_provider.jar:com/sun/crypto/provider/AESCipher$General.class */
    public static final class General extends AESCipher {
        public General() {
            super(-1);
        }
    }

    /* loaded from: sunjce_provider.jar:com/sun/crypto/provider/AESCipher$OidImpl.class */
    static abstract class OidImpl extends AESCipher {
        protected OidImpl(int i2, String str, String str2) {
            super(i2);
            try {
                engineSetMode(str);
                engineSetPadding(str2);
            } catch (GeneralSecurityException e2) {
                ProviderException providerException = new ProviderException("Internal Error");
                providerException.initCause(e2);
                throw providerException;
            }
        }
    }

    /* loaded from: sunjce_provider.jar:com/sun/crypto/provider/AESCipher$AES128_ECB_NoPadding.class */
    public static final class AES128_ECB_NoPadding extends OidImpl {
        public AES128_ECB_NoPadding() {
            super(16, "ECB", "NOPADDING");
        }
    }

    /* loaded from: sunjce_provider.jar:com/sun/crypto/provider/AESCipher$AES192_ECB_NoPadding.class */
    public static final class AES192_ECB_NoPadding extends OidImpl {
        public AES192_ECB_NoPadding() {
            super(24, "ECB", "NOPADDING");
        }
    }

    /* loaded from: sunjce_provider.jar:com/sun/crypto/provider/AESCipher$AES256_ECB_NoPadding.class */
    public static final class AES256_ECB_NoPadding extends OidImpl {
        public AES256_ECB_NoPadding() {
            super(32, "ECB", "NOPADDING");
        }
    }

    /* loaded from: sunjce_provider.jar:com/sun/crypto/provider/AESCipher$AES128_CBC_NoPadding.class */
    public static final class AES128_CBC_NoPadding extends OidImpl {
        public AES128_CBC_NoPadding() {
            super(16, "CBC", "NOPADDING");
        }
    }

    /* loaded from: sunjce_provider.jar:com/sun/crypto/provider/AESCipher$AES192_CBC_NoPadding.class */
    public static final class AES192_CBC_NoPadding extends OidImpl {
        public AES192_CBC_NoPadding() {
            super(24, "CBC", "NOPADDING");
        }
    }

    /* loaded from: sunjce_provider.jar:com/sun/crypto/provider/AESCipher$AES256_CBC_NoPadding.class */
    public static final class AES256_CBC_NoPadding extends OidImpl {
        public AES256_CBC_NoPadding() {
            super(32, "CBC", "NOPADDING");
        }
    }

    /* loaded from: sunjce_provider.jar:com/sun/crypto/provider/AESCipher$AES128_OFB_NoPadding.class */
    public static final class AES128_OFB_NoPadding extends OidImpl {
        public AES128_OFB_NoPadding() {
            super(16, "OFB", "NOPADDING");
        }
    }

    /* loaded from: sunjce_provider.jar:com/sun/crypto/provider/AESCipher$AES192_OFB_NoPadding.class */
    public static final class AES192_OFB_NoPadding extends OidImpl {
        public AES192_OFB_NoPadding() {
            super(24, "OFB", "NOPADDING");
        }
    }

    /* loaded from: sunjce_provider.jar:com/sun/crypto/provider/AESCipher$AES256_OFB_NoPadding.class */
    public static final class AES256_OFB_NoPadding extends OidImpl {
        public AES256_OFB_NoPadding() {
            super(32, "OFB", "NOPADDING");
        }
    }

    /* loaded from: sunjce_provider.jar:com/sun/crypto/provider/AESCipher$AES128_CFB_NoPadding.class */
    public static final class AES128_CFB_NoPadding extends OidImpl {
        public AES128_CFB_NoPadding() {
            super(16, "CFB", "NOPADDING");
        }
    }

    /* loaded from: sunjce_provider.jar:com/sun/crypto/provider/AESCipher$AES192_CFB_NoPadding.class */
    public static final class AES192_CFB_NoPadding extends OidImpl {
        public AES192_CFB_NoPadding() {
            super(24, "CFB", "NOPADDING");
        }
    }

    /* loaded from: sunjce_provider.jar:com/sun/crypto/provider/AESCipher$AES256_CFB_NoPadding.class */
    public static final class AES256_CFB_NoPadding extends OidImpl {
        public AES256_CFB_NoPadding() {
            super(32, "CFB", "NOPADDING");
        }
    }

    /* loaded from: sunjce_provider.jar:com/sun/crypto/provider/AESCipher$AES128_GCM_NoPadding.class */
    public static final class AES128_GCM_NoPadding extends OidImpl {
        public AES128_GCM_NoPadding() {
            super(16, "GCM", "NOPADDING");
        }
    }

    /* loaded from: sunjce_provider.jar:com/sun/crypto/provider/AESCipher$AES192_GCM_NoPadding.class */
    public static final class AES192_GCM_NoPadding extends OidImpl {
        public AES192_GCM_NoPadding() {
            super(24, "GCM", "NOPADDING");
        }
    }

    /* loaded from: sunjce_provider.jar:com/sun/crypto/provider/AESCipher$AES256_GCM_NoPadding.class */
    public static final class AES256_GCM_NoPadding extends OidImpl {
        public AES256_GCM_NoPadding() {
            super(32, "GCM", "NOPADDING");
        }
    }

    static final void checkKeySize(Key key, int i2) throws InvalidKeyException {
        if (i2 != -1) {
            if (key == null) {
                throw new InvalidKeyException("The key must not be null");
            }
            byte[] encoded = key.getEncoded();
            if (encoded == null) {
                throw new InvalidKeyException("Key encoding must not be null");
            }
            if (encoded.length != i2) {
                throw new InvalidKeyException("The key must be " + i2 + " bytes");
            }
        }
    }

    protected AESCipher(int i2) {
        this.core = null;
        this.core = new CipherCore(new AESCrypt(), 16);
        this.fixedKeySize = i2;
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
        return 16;
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
        return this.core.getParameters("AES");
    }

    @Override // javax.crypto.CipherSpi
    protected void engineInit(int i2, Key key, SecureRandom secureRandom) throws InvalidKeyException {
        checkKeySize(key, this.fixedKeySize);
        this.updateCalled = false;
        this.core.init(i2, key, secureRandom);
    }

    @Override // javax.crypto.CipherSpi
    protected void engineInit(int i2, Key key, AlgorithmParameterSpec algorithmParameterSpec, SecureRandom secureRandom) throws InvalidKeyException, InvalidAlgorithmParameterException {
        checkKeySize(key, this.fixedKeySize);
        this.updateCalled = false;
        this.core.init(i2, key, algorithmParameterSpec, secureRandom);
    }

    @Override // javax.crypto.CipherSpi
    protected void engineInit(int i2, Key key, AlgorithmParameters algorithmParameters, SecureRandom secureRandom) throws InvalidKeyException, InvalidAlgorithmParameterException {
        checkKeySize(key, this.fixedKeySize);
        this.updateCalled = false;
        this.core.init(i2, key, algorithmParameters, secureRandom);
    }

    @Override // javax.crypto.CipherSpi
    protected byte[] engineUpdate(byte[] bArr, int i2, int i3) {
        this.updateCalled = true;
        return this.core.update(bArr, i2, i3);
    }

    @Override // javax.crypto.CipherSpi
    protected int engineUpdate(byte[] bArr, int i2, int i3, byte[] bArr2, int i4) throws ShortBufferException {
        this.updateCalled = true;
        return this.core.update(bArr, i2, i3, bArr2, i4);
    }

    @Override // javax.crypto.CipherSpi
    protected byte[] engineDoFinal(byte[] bArr, int i2, int i3) throws BadPaddingException, IllegalBlockSizeException {
        byte[] bArrDoFinal = this.core.doFinal(bArr, i2, i3);
        this.updateCalled = false;
        return bArrDoFinal;
    }

    @Override // javax.crypto.CipherSpi
    protected int engineDoFinal(byte[] bArr, int i2, int i3, byte[] bArr2, int i4) throws BadPaddingException, IllegalBlockSizeException, ShortBufferException {
        int iDoFinal = this.core.doFinal(bArr, i2, i3, bArr2, i4);
        this.updateCalled = false;
        return iDoFinal;
    }

    @Override // javax.crypto.CipherSpi
    protected int engineGetKeySize(Key key) throws InvalidKeyException {
        byte[] encoded = key.getEncoded();
        if (!AESCrypt.isKeySizeValid(encoded.length)) {
            throw new InvalidKeyException("Invalid AES key length: " + encoded.length + " bytes");
        }
        return Math.multiplyExact(encoded.length, 8);
    }

    @Override // javax.crypto.CipherSpi
    protected byte[] engineWrap(Key key) throws IllegalBlockSizeException, InvalidKeyException {
        return this.core.wrap(key);
    }

    @Override // javax.crypto.CipherSpi
    protected Key engineUnwrap(byte[] bArr, String str, int i2) throws NoSuchAlgorithmException, InvalidKeyException {
        return this.core.unwrap(bArr, str, i2);
    }

    @Override // javax.crypto.CipherSpi
    protected void engineUpdateAAD(byte[] bArr, int i2, int i3) {
        if (this.core.getMode() == 7 && this.updateCalled) {
            throw new IllegalStateException("AAD must be supplied before encryption/decryption starts");
        }
        this.core.updateAAD(bArr, i2, i3);
    }

    @Override // javax.crypto.CipherSpi
    protected void engineUpdateAAD(ByteBuffer byteBuffer) {
        int iLimit;
        if (this.core.getMode() == 7 && this.updateCalled) {
            throw new IllegalStateException("AAD must be supplied before encryption/decryption starts");
        }
        if (byteBuffer != null && (iLimit = byteBuffer.limit() - byteBuffer.position()) > 0) {
            if (byteBuffer.hasArray()) {
                this.core.updateAAD(byteBuffer.array(), Math.addExact(byteBuffer.arrayOffset(), byteBuffer.position()), iLimit);
                byteBuffer.position(byteBuffer.limit());
            } else {
                byte[] bArr = new byte[iLimit];
                byteBuffer.get(bArr);
                this.core.updateAAD(bArr, 0, iLimit);
            }
        }
    }
}
