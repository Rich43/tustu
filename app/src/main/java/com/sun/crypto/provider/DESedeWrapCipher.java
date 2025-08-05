package com.sun.crypto.provider;

import java.security.AlgorithmParameters;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.InvalidParameterSpecException;
import javax.crypto.BadPaddingException;
import javax.crypto.CipherSpi;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.ShortBufferException;
import javax.crypto.spec.IvParameterSpec;

/* loaded from: sunjce_provider.jar:com/sun/crypto/provider/DESedeWrapCipher.class */
public final class DESedeWrapCipher extends CipherSpi {
    private static final byte[] IV2 = {74, -35, -94, 44, 121, -24, 33, 5};
    private static final int CHECKSUM_LEN = 8;
    private static final int IV_LEN = 8;
    private byte[] iv = null;
    private Key cipherKey = null;
    private boolean decrypting = false;
    private FeedbackCipher cipher = new CipherBlockChaining(new DESedeCrypt());

    @Override // javax.crypto.CipherSpi
    protected void engineSetMode(String str) throws NoSuchAlgorithmException {
        if (!str.equalsIgnoreCase("CBC")) {
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
        return 8;
    }

    @Override // javax.crypto.CipherSpi
    protected int engineGetOutputSize(int i2) {
        int iAddExact;
        if (this.decrypting) {
            iAddExact = i2 - 16;
        } else {
            iAddExact = Math.addExact(i2, 16);
        }
        if (iAddExact < 0) {
            return 0;
        }
        return iAddExact;
    }

    @Override // javax.crypto.CipherSpi
    protected byte[] engineGetIV() {
        if (this.iv == null) {
            return null;
        }
        return (byte[]) this.iv.clone();
    }

    @Override // javax.crypto.CipherSpi
    protected void engineInit(int i2, Key key, SecureRandom secureRandom) throws InvalidKeyException {
        try {
            engineInit(i2, key, (AlgorithmParameterSpec) null, secureRandom);
        } catch (InvalidAlgorithmParameterException e2) {
            InvalidKeyException invalidKeyException = new InvalidKeyException("Parameters required");
            invalidKeyException.initCause(e2);
            throw invalidKeyException;
        }
    }

    @Override // javax.crypto.CipherSpi
    protected void engineInit(int i2, Key key, AlgorithmParameterSpec algorithmParameterSpec, SecureRandom secureRandom) throws InvalidKeyException, InvalidAlgorithmParameterException {
        byte[] bArr;
        if (i2 == 3) {
            this.decrypting = false;
            if (algorithmParameterSpec == null) {
                this.iv = new byte[8];
                if (secureRandom == null) {
                    secureRandom = SunJCE.getRandom();
                }
                secureRandom.nextBytes(this.iv);
            } else if (algorithmParameterSpec instanceof IvParameterSpec) {
                this.iv = ((IvParameterSpec) algorithmParameterSpec).getIV();
            } else {
                throw new InvalidAlgorithmParameterException("Wrong parameter type: IV expected");
            }
            bArr = this.iv;
        } else if (i2 == 4) {
            if (algorithmParameterSpec != null) {
                throw new InvalidAlgorithmParameterException("No parameter accepted for unwrapping keys");
            }
            this.iv = null;
            this.decrypting = true;
            bArr = IV2;
        } else {
            throw new UnsupportedOperationException("This cipher can only be used for key wrapping and unwrapping");
        }
        this.cipher.init(this.decrypting, key.getAlgorithm(), key.getEncoded(), bArr);
        this.cipherKey = key;
    }

    @Override // javax.crypto.CipherSpi
    protected void engineInit(int i2, Key key, AlgorithmParameters algorithmParameters, SecureRandom secureRandom) throws InvalidKeyException, InvalidAlgorithmParameterException {
        IvParameterSpec ivParameterSpec = null;
        if (algorithmParameters != null) {
            try {
                DESedeParameters dESedeParameters = new DESedeParameters();
                dESedeParameters.engineInit(algorithmParameters.getEncoded());
                ivParameterSpec = (IvParameterSpec) dESedeParameters.engineGetParameterSpec(IvParameterSpec.class);
            } catch (Exception e2) {
                InvalidAlgorithmParameterException invalidAlgorithmParameterException = new InvalidAlgorithmParameterException("Wrong parameter type: IV expected");
                invalidAlgorithmParameterException.initCause(e2);
                throw invalidAlgorithmParameterException;
            }
        }
        engineInit(i2, key, ivParameterSpec, secureRandom);
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
        AlgorithmParameters algorithmParameters = null;
        if (this.iv != null) {
            String algorithm = this.cipherKey.getAlgorithm();
            try {
                algorithmParameters = AlgorithmParameters.getInstance(algorithm, SunJCE.getInstance());
                algorithmParameters.init(new IvParameterSpec(this.iv));
            } catch (NoSuchAlgorithmException e2) {
                throw new RuntimeException("Cannot find " + algorithm + " AlgorithmParameters implementation in SunJCE provider");
            } catch (InvalidParameterSpecException e3) {
                throw new RuntimeException("IvParameterSpec not supported");
            }
        }
        return algorithmParameters;
    }

    @Override // javax.crypto.CipherSpi
    protected int engineGetKeySize(Key key) throws InvalidKeyException {
        byte[] encoded = key.getEncoded();
        if (encoded.length != 24) {
            throw new InvalidKeyException("Invalid key length: " + encoded.length + " bytes");
        }
        return 112;
    }

    @Override // javax.crypto.CipherSpi
    protected byte[] engineWrap(Key key) throws IllegalBlockSizeException, InvalidKeyException {
        byte[] encoded = key.getEncoded();
        if (encoded == null || encoded.length == 0) {
            throw new InvalidKeyException("Cannot get an encoding of the key to be wrapped");
        }
        byte[] checksum = getChecksum(encoded);
        byte[] bArr = new byte[Math.addExact(encoded.length, 8)];
        System.arraycopy(encoded, 0, bArr, 0, encoded.length);
        System.arraycopy(checksum, 0, bArr, encoded.length, 8);
        byte[] bArr2 = new byte[Math.addExact(this.iv.length, bArr.length)];
        System.arraycopy(this.iv, 0, bArr2, 0, this.iv.length);
        this.cipher.encrypt(bArr, 0, bArr.length, bArr2, this.iv.length);
        for (int i2 = 0; i2 < bArr2.length / 2; i2++) {
            byte b2 = bArr2[i2];
            bArr2[i2] = bArr2[(bArr2.length - 1) - i2];
            bArr2[(bArr2.length - 1) - i2] = b2;
        }
        try {
            this.cipher.init(false, this.cipherKey.getAlgorithm(), this.cipherKey.getEncoded(), IV2);
            byte[] bArr3 = new byte[bArr2.length];
            this.cipher.encrypt(bArr2, 0, bArr2.length, bArr3, 0);
            try {
                this.cipher.init(this.decrypting, this.cipherKey.getAlgorithm(), this.cipherKey.getEncoded(), this.iv);
                return bArr3;
            } catch (InvalidAlgorithmParameterException e2) {
                throw new RuntimeException("Internal cipher IV is invalid");
            } catch (InvalidKeyException e3) {
                throw new RuntimeException("Internal cipher key is corrupted");
            }
        } catch (InvalidAlgorithmParameterException e4) {
            throw new RuntimeException("Internal cipher IV is invalid");
        } catch (InvalidKeyException e5) {
            throw new RuntimeException("Internal cipher key is corrupted");
        }
    }

    @Override // javax.crypto.CipherSpi
    protected Key engineUnwrap(byte[] bArr, String str, int i2) throws NoSuchAlgorithmException, InvalidKeyException {
        if (bArr.length == 0) {
            throw new InvalidKeyException("The wrapped key is empty");
        }
        byte[] bArr2 = new byte[bArr.length];
        this.cipher.decrypt(bArr, 0, bArr.length, bArr2, 0);
        for (int i3 = 0; i3 < bArr2.length / 2; i3++) {
            byte b2 = bArr2[i3];
            bArr2[i3] = bArr2[(bArr2.length - 1) - i3];
            bArr2[(bArr2.length - 1) - i3] = b2;
        }
        this.iv = new byte[8];
        System.arraycopy(bArr2, 0, this.iv, 0, this.iv.length);
        try {
            this.cipher.init(true, this.cipherKey.getAlgorithm(), this.cipherKey.getEncoded(), this.iv);
            byte[] bArr3 = new byte[bArr2.length - this.iv.length];
            this.cipher.decrypt(bArr2, this.iv.length, bArr3.length, bArr3, 0);
            int length = bArr3.length - 8;
            byte[] checksum = getChecksum(bArr3, 0, length);
            for (int i4 = 0; i4 < 8; i4++) {
                if (bArr3[length + i4] != checksum[i4]) {
                    throw new InvalidKeyException("Checksum comparison failed");
                }
            }
            try {
                this.cipher.init(this.decrypting, this.cipherKey.getAlgorithm(), this.cipherKey.getEncoded(), IV2);
                byte[] bArr4 = new byte[length];
                System.arraycopy(bArr3, 0, bArr4, 0, length);
                return ConstructKeys.constructKey(bArr4, str, i2);
            } catch (InvalidAlgorithmParameterException e2) {
                throw new InvalidKeyException("IV in wrapped key is invalid");
            }
        } catch (InvalidAlgorithmParameterException e3) {
            throw new InvalidKeyException("IV in wrapped key is invalid");
        }
    }

    private static final byte[] getChecksum(byte[] bArr) {
        return getChecksum(bArr, 0, bArr.length);
    }

    private static final byte[] getChecksum(byte[] bArr, int i2, int i3) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA1");
            messageDigest.update(bArr, i2, i3);
            byte[] bArr2 = new byte[8];
            System.arraycopy(messageDigest.digest(), 0, bArr2, 0, bArr2.length);
            return bArr2;
        } catch (NoSuchAlgorithmException e2) {
            throw new RuntimeException("SHA1 message digest not available");
        }
    }
}
