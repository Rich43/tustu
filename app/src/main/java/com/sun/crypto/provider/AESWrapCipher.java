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

/* loaded from: sunjce_provider.jar:com/sun/crypto/provider/AESWrapCipher.class */
abstract class AESWrapCipher extends CipherSpi {
    private static final byte[] IV = {-90, -90, -90, -90, -90, -90, -90, -90};
    private static final int blksize = 16;
    private final int fixedKeySize;
    private boolean decrypting = false;
    private AESCrypt cipher = new AESCrypt();

    /* loaded from: sunjce_provider.jar:com/sun/crypto/provider/AESWrapCipher$General.class */
    public static final class General extends AESWrapCipher {
        public General() {
            super(-1);
        }
    }

    /* loaded from: sunjce_provider.jar:com/sun/crypto/provider/AESWrapCipher$AES128.class */
    public static final class AES128 extends AESWrapCipher {
        public AES128() {
            super(16);
        }
    }

    /* loaded from: sunjce_provider.jar:com/sun/crypto/provider/AESWrapCipher$AES192.class */
    public static final class AES192 extends AESWrapCipher {
        public AES192() {
            super(24);
        }
    }

    /* loaded from: sunjce_provider.jar:com/sun/crypto/provider/AESWrapCipher$AES256.class */
    public static final class AES256 extends AESWrapCipher {
        public AES256() {
            super(32);
        }
    }

    public AESWrapCipher(int i2) {
        this.fixedKeySize = i2;
    }

    @Override // javax.crypto.CipherSpi
    protected void engineSetMode(String str) throws NoSuchAlgorithmException {
        if (!str.equalsIgnoreCase("ECB")) {
            throw new NoSuchAlgorithmException(str + " cannot be used");
        }
    }

    @Override // javax.crypto.CipherSpi
    protected void engineSetPadding(String str) throws NoSuchPaddingException {
        if (!str.equalsIgnoreCase("NoPadding")) {
            throw new NoSuchPaddingException(str + " cannot be used");
        }
    }

    @Override // javax.crypto.CipherSpi
    protected int engineGetBlockSize() {
        return 16;
    }

    @Override // javax.crypto.CipherSpi
    protected int engineGetOutputSize(int i2) {
        int iAddExact;
        if (this.decrypting) {
            iAddExact = i2 - 8;
        } else {
            iAddExact = Math.addExact(i2, 8);
        }
        if (iAddExact < 0) {
            return 0;
        }
        return iAddExact;
    }

    @Override // javax.crypto.CipherSpi
    protected byte[] engineGetIV() {
        return null;
    }

    @Override // javax.crypto.CipherSpi
    protected void engineInit(int i2, Key key, SecureRandom secureRandom) throws InvalidKeyException {
        if (i2 == 3) {
            this.decrypting = false;
        } else if (i2 == 4) {
            this.decrypting = true;
        } else {
            throw new UnsupportedOperationException("This cipher can only be used for key wrapping and unwrapping");
        }
        AESCipher.checkKeySize(key, this.fixedKeySize);
        this.cipher.init(this.decrypting, key.getAlgorithm(), key.getEncoded());
    }

    @Override // javax.crypto.CipherSpi
    protected void engineInit(int i2, Key key, AlgorithmParameterSpec algorithmParameterSpec, SecureRandom secureRandom) throws InvalidKeyException, InvalidAlgorithmParameterException {
        if (algorithmParameterSpec != null) {
            throw new InvalidAlgorithmParameterException("This cipher does not accept any parameters");
        }
        engineInit(i2, key, secureRandom);
    }

    @Override // javax.crypto.CipherSpi
    protected void engineInit(int i2, Key key, AlgorithmParameters algorithmParameters, SecureRandom secureRandom) throws InvalidKeyException, InvalidAlgorithmParameterException {
        if (algorithmParameters != null) {
            throw new InvalidAlgorithmParameterException("This cipher does not accept any parameters");
        }
        engineInit(i2, key, secureRandom);
    }

    @Override // javax.crypto.CipherSpi
    protected byte[] engineUpdate(byte[] bArr, int i2, int i3) {
        throw new IllegalStateException("Cipher has not been initialized");
    }

    @Override // javax.crypto.CipherSpi
    protected int engineUpdate(byte[] bArr, int i2, int i3, byte[] bArr2, int i4) throws ShortBufferException {
        throw new IllegalStateException("Cipher has not been initialized");
    }

    @Override // javax.crypto.CipherSpi
    protected byte[] engineDoFinal(byte[] bArr, int i2, int i3) throws BadPaddingException, IllegalBlockSizeException {
        throw new IllegalStateException("Cipher has not been initialized");
    }

    @Override // javax.crypto.CipherSpi
    protected int engineDoFinal(byte[] bArr, int i2, int i3, byte[] bArr2, int i4) throws BadPaddingException, IllegalBlockSizeException, ShortBufferException {
        throw new IllegalStateException("Cipher has not been initialized");
    }

    @Override // javax.crypto.CipherSpi
    protected AlgorithmParameters engineGetParameters() {
        return null;
    }

    @Override // javax.crypto.CipherSpi
    protected int engineGetKeySize(Key key) throws InvalidKeyException {
        byte[] encoded = key.getEncoded();
        if (!AESCrypt.isKeySizeValid(encoded.length)) {
            throw new InvalidKeyException("Invalid key length: " + encoded.length + " bytes");
        }
        return Math.multiplyExact(encoded.length, 8);
    }

    @Override // javax.crypto.CipherSpi
    protected byte[] engineWrap(Key key) throws IllegalBlockSizeException, InvalidKeyException {
        byte[] encoded = key.getEncoded();
        if (encoded == null || encoded.length == 0) {
            throw new InvalidKeyException("Cannot get an encoding of the key to be wrapped");
        }
        byte[] bArr = new byte[Math.addExact(encoded.length, 8)];
        if (encoded.length == 8) {
            System.arraycopy(IV, 0, bArr, 0, IV.length);
            System.arraycopy(encoded, 0, bArr, IV.length, 8);
            this.cipher.encryptBlock(bArr, 0, bArr, 0);
        } else {
            if (encoded.length % 8 != 0) {
                throw new IllegalBlockSizeException("length of the to be wrapped key should be multiples of 8 bytes");
            }
            System.arraycopy(IV, 0, bArr, 0, IV.length);
            System.arraycopy(encoded, 0, bArr, IV.length, encoded.length);
            int length = encoded.length / 8;
            byte[] bArr2 = new byte[16];
            for (int i2 = 0; i2 < 6; i2++) {
                for (int i3 = 1; i3 <= length; i3++) {
                    int i4 = i3 + (i2 * length);
                    System.arraycopy(bArr, 0, bArr2, 0, IV.length);
                    System.arraycopy(bArr, i3 * 8, bArr2, IV.length, 8);
                    this.cipher.encryptBlock(bArr2, 0, bArr2, 0);
                    int i5 = 1;
                    while (i4 != 0) {
                        int length2 = IV.length - i5;
                        bArr2[length2] = (byte) (bArr2[length2] ^ ((byte) i4));
                        i4 >>>= 8;
                        i5++;
                    }
                    System.arraycopy(bArr2, 0, bArr, 0, IV.length);
                    System.arraycopy(bArr2, 8, bArr, 8 * i3, 8);
                }
            }
        }
        return bArr;
    }

    @Override // javax.crypto.CipherSpi
    protected Key engineUnwrap(byte[] bArr, String str, int i2) throws NoSuchAlgorithmException, InvalidKeyException {
        int length = bArr.length;
        if (length == 0) {
            throw new InvalidKeyException("The wrapped key is empty");
        }
        if (length % 8 != 0) {
            throw new InvalidKeyException("The wrapped key has invalid key length");
        }
        byte[] bArr2 = new byte[length - 8];
        byte[] bArr3 = new byte[16];
        if (length == 16) {
            this.cipher.decryptBlock(bArr, 0, bArr3, 0);
            for (int i3 = 0; i3 < IV.length; i3++) {
                if (IV[i3] != bArr3[i3]) {
                    throw new InvalidKeyException("Integrity check failed");
                }
            }
            System.arraycopy(bArr3, IV.length, bArr2, 0, bArr2.length);
        } else {
            System.arraycopy(bArr, 0, bArr3, 0, IV.length);
            System.arraycopy(bArr, IV.length, bArr2, 0, bArr2.length);
            int length2 = bArr2.length / 8;
            for (int i4 = 5; i4 >= 0; i4--) {
                for (int i5 = length2; i5 > 0; i5--) {
                    int i6 = i5 + (i4 * length2);
                    System.arraycopy(bArr2, 8 * (i5 - 1), bArr3, IV.length, 8);
                    int i7 = 1;
                    while (i6 != 0) {
                        int length3 = IV.length - i7;
                        bArr3[length3] = (byte) (bArr3[length3] ^ ((byte) i6));
                        i6 >>>= 8;
                        i7++;
                    }
                    this.cipher.decryptBlock(bArr3, 0, bArr3, 0);
                    System.arraycopy(bArr3, IV.length, bArr2, 8 * (i5 - 1), 8);
                }
            }
            for (int i8 = 0; i8 < IV.length; i8++) {
                if (IV[i8] != bArr3[i8]) {
                    throw new InvalidKeyException("Integrity check failed");
                }
            }
        }
        return ConstructKeys.constructKey(bArr2, str, i2);
    }
}
