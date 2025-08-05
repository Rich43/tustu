package com.sun.crypto.provider;

import java.math.BigInteger;
import java.security.AlgorithmParameters;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.InvalidParameterSpecException;
import java.util.Arrays;
import javax.crypto.BadPaddingException;
import javax.crypto.CipherSpi;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.ShortBufferException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/* loaded from: sunjce_provider.jar:com/sun/crypto/provider/PKCS12PBECipherCore.class */
final class PKCS12PBECipherCore {
    private CipherCore cipher;
    private int blockSize;
    private int keySize;
    private String algo;
    private String pbeAlgo;
    private byte[] salt = null;
    private int iCount = 0;
    private static final int DEFAULT_SALT_LENGTH = 20;
    private static final int DEFAULT_COUNT = 1024;
    static final int CIPHER_KEY = 1;
    static final int CIPHER_IV = 2;
    static final int MAC_KEY = 3;

    static byte[] derive(char[] cArr, byte[] bArr, int i2, int i3, int i4) {
        return derive(cArr, bArr, i2, i3, i4, "SHA-1", 64);
    }

    static byte[] derive(char[] cArr, byte[] bArr, int i2, int i3, int i4, String str, int i5) {
        int i6;
        int length = cArr.length * 2;
        if (length == 2 && cArr[0] == 0) {
            cArr = new char[0];
            i6 = 0;
        } else {
            i6 = length + 2;
        }
        byte[] bArr2 = new byte[i6];
        int i7 = 0;
        int i8 = 0;
        while (i7 < cArr.length) {
            bArr2[i8] = (byte) ((cArr[i7] >>> '\b') & 255);
            bArr2[i8 + 1] = (byte) (cArr[i7] & 255);
            i7++;
            i8 += 2;
        }
        byte[] bArr3 = new byte[i3];
        try {
            MessageDigest messageDigest = MessageDigest.getInstance(str);
            int digestLength = messageDigest.getDigestLength();
            int iRoundup = roundup(i3, digestLength) / digestLength;
            byte[] bArr4 = new byte[i5];
            int iRoundup2 = roundup(bArr.length, i5);
            int iRoundup3 = roundup(bArr2.length, i5);
            byte[] bArr5 = new byte[iRoundup2 + iRoundup3];
            Arrays.fill(bArr4, (byte) i4);
            concat(bArr, bArr5, 0, iRoundup2);
            concat(bArr2, bArr5, iRoundup2, iRoundup3);
            Arrays.fill(bArr2, (byte) 0);
            byte[] bArr6 = new byte[i5];
            byte[] byteArray = new byte[i5];
            int i9 = 0;
            while (true) {
                messageDigest.update(bArr4);
                messageDigest.update(bArr5);
                byte[] bArrDigest = messageDigest.digest();
                for (int i10 = 1; i10 < i2; i10++) {
                    bArrDigest = messageDigest.digest(bArrDigest);
                }
                System.arraycopy(bArrDigest, 0, bArr3, digestLength * i9, Math.min(i3, digestLength));
                if (i9 + 1 != iRoundup) {
                    concat(bArrDigest, bArr6, 0, bArr6.length);
                    BigInteger bigIntegerAdd = new BigInteger(1, bArr6).add(BigInteger.ONE);
                    int i11 = 0;
                    while (i11 < bArr5.length) {
                        if (byteArray.length != i5) {
                            byteArray = new byte[i5];
                        }
                        System.arraycopy(bArr5, i11, byteArray, 0, i5);
                        byteArray = new BigInteger(1, byteArray).add(bigIntegerAdd).toByteArray();
                        int length2 = byteArray.length - i5;
                        if (length2 >= 0) {
                            System.arraycopy(byteArray, length2, bArr5, i11, i5);
                        } else if (length2 < 0) {
                            Arrays.fill(bArr5, i11, i11 + (-length2), (byte) 0);
                            System.arraycopy(byteArray, 0, bArr5, i11 + (-length2), byteArray.length);
                        }
                        i11 += i5;
                    }
                    i9++;
                    i3 -= digestLength;
                } else {
                    return bArr3;
                }
            }
        } catch (Exception e2) {
            throw new RuntimeException("internal error: " + ((Object) e2));
        }
    }

    private static int roundup(int i2, int i3) {
        return ((i2 + (i3 - 1)) / i3) * i3;
    }

    private static void concat(byte[] bArr, byte[] bArr2, int i2, int i3) {
        if (bArr.length == 0) {
            return;
        }
        int length = i3 / bArr.length;
        int i4 = 0;
        int length2 = 0;
        while (true) {
            int i5 = length2;
            if (i4 < length) {
                System.arraycopy(bArr, 0, bArr2, i5 + i2, bArr.length);
                i4++;
                length2 = i5 + bArr.length;
            } else {
                System.arraycopy(bArr, 0, bArr2, i5 + i2, i3 - i5);
                return;
            }
        }
    }

    PKCS12PBECipherCore(String str, int i2) throws NoSuchAlgorithmException {
        SymmetricCipher rC2Crypt;
        this.algo = null;
        this.pbeAlgo = null;
        this.algo = str;
        if (this.algo.equals("RC4")) {
            this.pbeAlgo = "PBEWithSHA1AndRC4_" + (i2 * 8);
        } else {
            if (this.algo.equals("DESede")) {
                rC2Crypt = new DESedeCrypt();
                this.pbeAlgo = "PBEWithSHA1AndDESede";
            } else if (this.algo.equals("RC2")) {
                rC2Crypt = new RC2Crypt();
                this.pbeAlgo = "PBEWithSHA1AndRC2_" + (i2 * 8);
            } else {
                throw new NoSuchAlgorithmException("No Cipher implementation for PBEWithSHA1And" + this.algo);
            }
            this.blockSize = rC2Crypt.getBlockSize();
            this.cipher = new CipherCore(rC2Crypt, this.blockSize);
            this.cipher.setMode("CBC");
            try {
                this.cipher.setPadding("PKCS5Padding");
            } catch (NoSuchPaddingException e2) {
            }
        }
        this.keySize = i2;
    }

    void implSetMode(String str) throws NoSuchAlgorithmException {
        if (str != null && !str.equalsIgnoreCase("CBC")) {
            throw new NoSuchAlgorithmException("Invalid cipher mode: " + str);
        }
    }

    void implSetPadding(String str) throws NoSuchPaddingException {
        if (str != null && !str.equalsIgnoreCase("PKCS5Padding")) {
            throw new NoSuchPaddingException("Invalid padding scheme: " + str);
        }
    }

    int implGetBlockSize() {
        return this.blockSize;
    }

    int implGetOutputSize(int i2) {
        return this.cipher.getOutputSize(i2);
    }

    byte[] implGetIV() {
        return this.cipher.getIV();
    }

    AlgorithmParameters implGetParameters() {
        if (this.salt == null) {
            this.salt = new byte[20];
            SunJCE.getRandom().nextBytes(this.salt);
            this.iCount = 1024;
        }
        PBEParameterSpec pBEParameterSpec = new PBEParameterSpec(this.salt, this.iCount);
        try {
            AlgorithmParameters algorithmParameters = AlgorithmParameters.getInstance(this.pbeAlgo, SunJCE.getInstance());
            algorithmParameters.init(pBEParameterSpec);
            return algorithmParameters;
        } catch (NoSuchAlgorithmException e2) {
            throw new RuntimeException("SunJCE provider is not configured properly");
        } catch (InvalidParameterSpecException e3) {
            throw new RuntimeException("PBEParameterSpec not supported");
        }
    }

    void implInit(int i2, Key key, AlgorithmParameterSpec algorithmParameterSpec, SecureRandom secureRandom) throws InvalidKeyException, InvalidAlgorithmParameterException {
        implInit(i2, key, algorithmParameterSpec, secureRandom, (CipherSpi) null);
    }

    void implInit(int i2, Key key, AlgorithmParameterSpec algorithmParameterSpec, SecureRandom secureRandom, CipherSpi cipherSpi) throws InvalidKeyException, InvalidAlgorithmParameterException {
        byte[] encoded;
        char[] password;
        this.salt = null;
        this.iCount = 0;
        if (key instanceof javax.crypto.interfaces.PBEKey) {
            javax.crypto.interfaces.PBEKey pBEKey = (javax.crypto.interfaces.PBEKey) key;
            password = pBEKey.getPassword();
            this.salt = pBEKey.getSalt();
            this.iCount = pBEKey.getIterationCount();
        } else if (key instanceof SecretKey) {
            if (!key.getAlgorithm().regionMatches(true, 0, "PBE", 0, 3) || (encoded = key.getEncoded()) == null) {
                throw new InvalidKeyException("Missing password");
            }
            password = new char[encoded.length];
            for (int i3 = 0; i3 < password.length; i3++) {
                password[i3] = (char) (encoded[i3] & Byte.MAX_VALUE);
            }
            Arrays.fill(encoded, (byte) 0);
        } else {
            throw new InvalidKeyException("SecretKey of PBE type required");
        }
        if ((i2 == 2 || i2 == 4) && algorithmParameterSpec == null) {
            try {
                if (this.salt == null || this.iCount == 0) {
                    throw new InvalidAlgorithmParameterException("Parameters missing");
                }
            } finally {
                Arrays.fill(password, (char) 0);
            }
        }
        if (algorithmParameterSpec == null) {
            if (this.salt == null) {
                this.salt = new byte[20];
                if (secureRandom != null) {
                    secureRandom.nextBytes(this.salt);
                } else {
                    SunJCE.getRandom().nextBytes(this.salt);
                }
            }
            if (this.iCount == 0) {
                this.iCount = 1024;
            }
        } else {
            if (!(algorithmParameterSpec instanceof PBEParameterSpec)) {
                throw new InvalidAlgorithmParameterException("PBEParameterSpec type required");
            }
            PBEParameterSpec pBEParameterSpec = (PBEParameterSpec) algorithmParameterSpec;
            if (this.salt != null) {
                if (!Arrays.equals(this.salt, pBEParameterSpec.getSalt())) {
                    throw new InvalidAlgorithmParameterException("Inconsistent value of salt between key and params");
                }
            } else {
                this.salt = pBEParameterSpec.getSalt();
            }
            if (this.iCount != 0) {
                if (this.iCount != pBEParameterSpec.getIterationCount()) {
                    throw new InvalidAlgorithmParameterException("Different iteration count between key and params");
                }
            } else {
                this.iCount = pBEParameterSpec.getIterationCount();
            }
        }
        if (this.salt.length < 8) {
            throw new InvalidAlgorithmParameterException("Salt must be at least 8 bytes long");
        }
        if (this.iCount <= 0) {
            throw new InvalidAlgorithmParameterException("IterationCount must be a positive number");
        }
        SecretKeySpec secretKeySpec = new SecretKeySpec(derive(password, this.salt, this.iCount, this.keySize, 1), this.algo);
        if (cipherSpi != null && (cipherSpi instanceof ARCFOURCipher)) {
            ((ARCFOURCipher) cipherSpi).engineInit(i2, secretKeySpec, secureRandom);
        } else {
            this.cipher.init(i2, secretKeySpec, new IvParameterSpec(derive(password, this.salt, this.iCount, 8, 2), 0, 8), secureRandom);
        }
    }

    void implInit(int i2, Key key, AlgorithmParameters algorithmParameters, SecureRandom secureRandom) throws InvalidKeyException, InvalidAlgorithmParameterException {
        implInit(i2, key, algorithmParameters, secureRandom, (CipherSpi) null);
    }

    void implInit(int i2, Key key, AlgorithmParameters algorithmParameters, SecureRandom secureRandom, CipherSpi cipherSpi) throws InvalidKeyException, InvalidAlgorithmParameterException {
        AlgorithmParameterSpec parameterSpec = null;
        if (algorithmParameters != null) {
            try {
                parameterSpec = algorithmParameters.getParameterSpec(PBEParameterSpec.class);
            } catch (InvalidParameterSpecException e2) {
                throw new InvalidAlgorithmParameterException("requires PBE parameters");
            }
        }
        implInit(i2, key, parameterSpec, secureRandom, cipherSpi);
    }

    void implInit(int i2, Key key, SecureRandom secureRandom) throws InvalidKeyException {
        implInit(i2, key, secureRandom, (CipherSpi) null);
    }

    void implInit(int i2, Key key, SecureRandom secureRandom, CipherSpi cipherSpi) throws InvalidKeyException {
        try {
            implInit(i2, key, (AlgorithmParameterSpec) null, secureRandom, cipherSpi);
        } catch (InvalidAlgorithmParameterException e2) {
            throw new InvalidKeyException("requires PBE parameters");
        }
    }

    byte[] implUpdate(byte[] bArr, int i2, int i3) {
        return this.cipher.update(bArr, i2, i3);
    }

    int implUpdate(byte[] bArr, int i2, int i3, byte[] bArr2, int i4) throws ShortBufferException {
        return this.cipher.update(bArr, i2, i3, bArr2, i4);
    }

    byte[] implDoFinal(byte[] bArr, int i2, int i3) throws BadPaddingException, IllegalBlockSizeException {
        return this.cipher.doFinal(bArr, i2, i3);
    }

    int implDoFinal(byte[] bArr, int i2, int i3, byte[] bArr2, int i4) throws BadPaddingException, IllegalBlockSizeException, ShortBufferException {
        return this.cipher.doFinal(bArr, i2, i3, bArr2, i4);
    }

    int implGetKeySize(Key key) throws InvalidKeyException {
        return this.keySize;
    }

    byte[] implWrap(Key key) throws IllegalBlockSizeException, InvalidKeyException {
        return this.cipher.wrap(key);
    }

    Key implUnwrap(byte[] bArr, String str, int i2) throws NoSuchAlgorithmException, InvalidKeyException {
        return this.cipher.unwrap(bArr, str, i2);
    }

    /* loaded from: sunjce_provider.jar:com/sun/crypto/provider/PKCS12PBECipherCore$PBEWithSHA1AndDESede.class */
    public static final class PBEWithSHA1AndDESede extends CipherSpi {
        private final PKCS12PBECipherCore core = new PKCS12PBECipherCore("DESede", 24);

        @Override // javax.crypto.CipherSpi
        protected byte[] engineDoFinal(byte[] bArr, int i2, int i3) throws BadPaddingException, IllegalBlockSizeException {
            return this.core.implDoFinal(bArr, i2, i3);
        }

        @Override // javax.crypto.CipherSpi
        protected int engineDoFinal(byte[] bArr, int i2, int i3, byte[] bArr2, int i4) throws BadPaddingException, IllegalBlockSizeException, ShortBufferException {
            return this.core.implDoFinal(bArr, i2, i3, bArr2, i4);
        }

        @Override // javax.crypto.CipherSpi
        protected int engineGetBlockSize() {
            return this.core.implGetBlockSize();
        }

        @Override // javax.crypto.CipherSpi
        protected byte[] engineGetIV() {
            return this.core.implGetIV();
        }

        @Override // javax.crypto.CipherSpi
        protected int engineGetKeySize(Key key) throws InvalidKeyException {
            return this.core.implGetKeySize(key);
        }

        @Override // javax.crypto.CipherSpi
        protected int engineGetOutputSize(int i2) {
            return this.core.implGetOutputSize(i2);
        }

        @Override // javax.crypto.CipherSpi
        protected AlgorithmParameters engineGetParameters() {
            return this.core.implGetParameters();
        }

        @Override // javax.crypto.CipherSpi
        protected void engineInit(int i2, Key key, AlgorithmParameterSpec algorithmParameterSpec, SecureRandom secureRandom) throws InvalidKeyException, InvalidAlgorithmParameterException {
            this.core.implInit(i2, key, algorithmParameterSpec, secureRandom);
        }

        @Override // javax.crypto.CipherSpi
        protected void engineInit(int i2, Key key, AlgorithmParameters algorithmParameters, SecureRandom secureRandom) throws InvalidKeyException, InvalidAlgorithmParameterException {
            this.core.implInit(i2, key, algorithmParameters, secureRandom);
        }

        @Override // javax.crypto.CipherSpi
        protected void engineInit(int i2, Key key, SecureRandom secureRandom) throws InvalidKeyException {
            this.core.implInit(i2, key, secureRandom);
        }

        @Override // javax.crypto.CipherSpi
        protected void engineSetMode(String str) throws NoSuchAlgorithmException {
            this.core.implSetMode(str);
        }

        @Override // javax.crypto.CipherSpi
        protected void engineSetPadding(String str) throws NoSuchPaddingException {
            this.core.implSetPadding(str);
        }

        @Override // javax.crypto.CipherSpi
        protected Key engineUnwrap(byte[] bArr, String str, int i2) throws NoSuchAlgorithmException, InvalidKeyException {
            return this.core.implUnwrap(bArr, str, i2);
        }

        @Override // javax.crypto.CipherSpi
        protected byte[] engineUpdate(byte[] bArr, int i2, int i3) {
            return this.core.implUpdate(bArr, i2, i3);
        }

        @Override // javax.crypto.CipherSpi
        protected int engineUpdate(byte[] bArr, int i2, int i3, byte[] bArr2, int i4) throws ShortBufferException {
            return this.core.implUpdate(bArr, i2, i3, bArr2, i4);
        }

        @Override // javax.crypto.CipherSpi
        protected byte[] engineWrap(Key key) throws IllegalBlockSizeException, InvalidKeyException {
            return this.core.implWrap(key);
        }
    }

    /* loaded from: sunjce_provider.jar:com/sun/crypto/provider/PKCS12PBECipherCore$PBEWithSHA1AndRC2_40.class */
    public static final class PBEWithSHA1AndRC2_40 extends CipherSpi {
        private final PKCS12PBECipherCore core = new PKCS12PBECipherCore("RC2", 5);

        @Override // javax.crypto.CipherSpi
        protected byte[] engineDoFinal(byte[] bArr, int i2, int i3) throws BadPaddingException, IllegalBlockSizeException {
            return this.core.implDoFinal(bArr, i2, i3);
        }

        @Override // javax.crypto.CipherSpi
        protected int engineDoFinal(byte[] bArr, int i2, int i3, byte[] bArr2, int i4) throws BadPaddingException, IllegalBlockSizeException, ShortBufferException {
            return this.core.implDoFinal(bArr, i2, i3, bArr2, i4);
        }

        @Override // javax.crypto.CipherSpi
        protected int engineGetBlockSize() {
            return this.core.implGetBlockSize();
        }

        @Override // javax.crypto.CipherSpi
        protected byte[] engineGetIV() {
            return this.core.implGetIV();
        }

        @Override // javax.crypto.CipherSpi
        protected int engineGetKeySize(Key key) throws InvalidKeyException {
            return this.core.implGetKeySize(key);
        }

        @Override // javax.crypto.CipherSpi
        protected int engineGetOutputSize(int i2) {
            return this.core.implGetOutputSize(i2);
        }

        @Override // javax.crypto.CipherSpi
        protected AlgorithmParameters engineGetParameters() {
            return this.core.implGetParameters();
        }

        @Override // javax.crypto.CipherSpi
        protected void engineInit(int i2, Key key, AlgorithmParameterSpec algorithmParameterSpec, SecureRandom secureRandom) throws InvalidKeyException, InvalidAlgorithmParameterException {
            this.core.implInit(i2, key, algorithmParameterSpec, secureRandom);
        }

        @Override // javax.crypto.CipherSpi
        protected void engineInit(int i2, Key key, AlgorithmParameters algorithmParameters, SecureRandom secureRandom) throws InvalidKeyException, InvalidAlgorithmParameterException {
            this.core.implInit(i2, key, algorithmParameters, secureRandom);
        }

        @Override // javax.crypto.CipherSpi
        protected void engineInit(int i2, Key key, SecureRandom secureRandom) throws InvalidKeyException {
            this.core.implInit(i2, key, secureRandom);
        }

        @Override // javax.crypto.CipherSpi
        protected void engineSetMode(String str) throws NoSuchAlgorithmException {
            this.core.implSetMode(str);
        }

        @Override // javax.crypto.CipherSpi
        protected void engineSetPadding(String str) throws NoSuchPaddingException {
            this.core.implSetPadding(str);
        }

        @Override // javax.crypto.CipherSpi
        protected Key engineUnwrap(byte[] bArr, String str, int i2) throws NoSuchAlgorithmException, InvalidKeyException {
            return this.core.implUnwrap(bArr, str, i2);
        }

        @Override // javax.crypto.CipherSpi
        protected byte[] engineUpdate(byte[] bArr, int i2, int i3) {
            return this.core.implUpdate(bArr, i2, i3);
        }

        @Override // javax.crypto.CipherSpi
        protected int engineUpdate(byte[] bArr, int i2, int i3, byte[] bArr2, int i4) throws ShortBufferException {
            return this.core.implUpdate(bArr, i2, i3, bArr2, i4);
        }

        @Override // javax.crypto.CipherSpi
        protected byte[] engineWrap(Key key) throws IllegalBlockSizeException, InvalidKeyException {
            return this.core.implWrap(key);
        }
    }

    /* loaded from: sunjce_provider.jar:com/sun/crypto/provider/PKCS12PBECipherCore$PBEWithSHA1AndRC2_128.class */
    public static final class PBEWithSHA1AndRC2_128 extends CipherSpi {
        private final PKCS12PBECipherCore core = new PKCS12PBECipherCore("RC2", 16);

        @Override // javax.crypto.CipherSpi
        protected byte[] engineDoFinal(byte[] bArr, int i2, int i3) throws BadPaddingException, IllegalBlockSizeException {
            return this.core.implDoFinal(bArr, i2, i3);
        }

        @Override // javax.crypto.CipherSpi
        protected int engineDoFinal(byte[] bArr, int i2, int i3, byte[] bArr2, int i4) throws BadPaddingException, IllegalBlockSizeException, ShortBufferException {
            return this.core.implDoFinal(bArr, i2, i3, bArr2, i4);
        }

        @Override // javax.crypto.CipherSpi
        protected int engineGetBlockSize() {
            return this.core.implGetBlockSize();
        }

        @Override // javax.crypto.CipherSpi
        protected byte[] engineGetIV() {
            return this.core.implGetIV();
        }

        @Override // javax.crypto.CipherSpi
        protected int engineGetKeySize(Key key) throws InvalidKeyException {
            return this.core.implGetKeySize(key);
        }

        @Override // javax.crypto.CipherSpi
        protected int engineGetOutputSize(int i2) {
            return this.core.implGetOutputSize(i2);
        }

        @Override // javax.crypto.CipherSpi
        protected AlgorithmParameters engineGetParameters() {
            return this.core.implGetParameters();
        }

        @Override // javax.crypto.CipherSpi
        protected void engineInit(int i2, Key key, AlgorithmParameterSpec algorithmParameterSpec, SecureRandom secureRandom) throws InvalidKeyException, InvalidAlgorithmParameterException {
            this.core.implInit(i2, key, algorithmParameterSpec, secureRandom);
        }

        @Override // javax.crypto.CipherSpi
        protected void engineInit(int i2, Key key, AlgorithmParameters algorithmParameters, SecureRandom secureRandom) throws InvalidKeyException, InvalidAlgorithmParameterException {
            this.core.implInit(i2, key, algorithmParameters, secureRandom);
        }

        @Override // javax.crypto.CipherSpi
        protected void engineInit(int i2, Key key, SecureRandom secureRandom) throws InvalidKeyException {
            this.core.implInit(i2, key, secureRandom);
        }

        @Override // javax.crypto.CipherSpi
        protected void engineSetMode(String str) throws NoSuchAlgorithmException {
            this.core.implSetMode(str);
        }

        @Override // javax.crypto.CipherSpi
        protected void engineSetPadding(String str) throws NoSuchPaddingException {
            this.core.implSetPadding(str);
        }

        @Override // javax.crypto.CipherSpi
        protected Key engineUnwrap(byte[] bArr, String str, int i2) throws NoSuchAlgorithmException, InvalidKeyException {
            return this.core.implUnwrap(bArr, str, i2);
        }

        @Override // javax.crypto.CipherSpi
        protected byte[] engineUpdate(byte[] bArr, int i2, int i3) {
            return this.core.implUpdate(bArr, i2, i3);
        }

        @Override // javax.crypto.CipherSpi
        protected int engineUpdate(byte[] bArr, int i2, int i3, byte[] bArr2, int i4) throws ShortBufferException {
            return this.core.implUpdate(bArr, i2, i3, bArr2, i4);
        }

        @Override // javax.crypto.CipherSpi
        protected byte[] engineWrap(Key key) throws IllegalBlockSizeException, InvalidKeyException {
            return this.core.implWrap(key);
        }
    }

    /* loaded from: sunjce_provider.jar:com/sun/crypto/provider/PKCS12PBECipherCore$PBEWithSHA1AndRC4_40.class */
    public static final class PBEWithSHA1AndRC4_40 extends CipherSpi {
        private static final int RC4_KEYSIZE = 5;
        private final PKCS12PBECipherCore core = new PKCS12PBECipherCore("RC4", 5);
        private final ARCFOURCipher cipher = new ARCFOURCipher();

        @Override // javax.crypto.CipherSpi
        protected byte[] engineDoFinal(byte[] bArr, int i2, int i3) throws BadPaddingException, IllegalBlockSizeException {
            return this.cipher.engineDoFinal(bArr, i2, i3);
        }

        @Override // javax.crypto.CipherSpi
        protected int engineDoFinal(byte[] bArr, int i2, int i3, byte[] bArr2, int i4) throws BadPaddingException, IllegalBlockSizeException, ShortBufferException {
            return this.cipher.engineDoFinal(bArr, i2, i3, bArr2, i4);
        }

        @Override // javax.crypto.CipherSpi
        protected int engineGetBlockSize() {
            return this.cipher.engineGetBlockSize();
        }

        @Override // javax.crypto.CipherSpi
        protected byte[] engineGetIV() {
            return this.cipher.engineGetIV();
        }

        @Override // javax.crypto.CipherSpi
        protected int engineGetKeySize(Key key) throws InvalidKeyException {
            return 5;
        }

        @Override // javax.crypto.CipherSpi
        protected int engineGetOutputSize(int i2) {
            return this.cipher.engineGetOutputSize(i2);
        }

        @Override // javax.crypto.CipherSpi
        protected AlgorithmParameters engineGetParameters() {
            return this.core.implGetParameters();
        }

        @Override // javax.crypto.CipherSpi
        protected void engineInit(int i2, Key key, AlgorithmParameterSpec algorithmParameterSpec, SecureRandom secureRandom) throws InvalidKeyException, InvalidAlgorithmParameterException {
            this.core.implInit(i2, key, algorithmParameterSpec, secureRandom, this.cipher);
        }

        @Override // javax.crypto.CipherSpi
        protected void engineInit(int i2, Key key, AlgorithmParameters algorithmParameters, SecureRandom secureRandom) throws InvalidKeyException, InvalidAlgorithmParameterException {
            this.core.implInit(i2, key, algorithmParameters, secureRandom, this.cipher);
        }

        @Override // javax.crypto.CipherSpi
        protected void engineInit(int i2, Key key, SecureRandom secureRandom) throws InvalidKeyException {
            this.core.implInit(i2, key, secureRandom, this.cipher);
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
        protected Key engineUnwrap(byte[] bArr, String str, int i2) throws NoSuchAlgorithmException, InvalidKeyException {
            return this.cipher.engineUnwrap(bArr, str, i2);
        }

        @Override // javax.crypto.CipherSpi
        protected byte[] engineUpdate(byte[] bArr, int i2, int i3) {
            return this.cipher.engineUpdate(bArr, i2, i3);
        }

        @Override // javax.crypto.CipherSpi
        protected int engineUpdate(byte[] bArr, int i2, int i3, byte[] bArr2, int i4) throws ShortBufferException {
            return this.cipher.engineUpdate(bArr, i2, i3, bArr2, i4);
        }

        @Override // javax.crypto.CipherSpi
        protected byte[] engineWrap(Key key) throws IllegalBlockSizeException, InvalidKeyException {
            return this.cipher.engineWrap(key);
        }
    }

    /* loaded from: sunjce_provider.jar:com/sun/crypto/provider/PKCS12PBECipherCore$PBEWithSHA1AndRC4_128.class */
    public static final class PBEWithSHA1AndRC4_128 extends CipherSpi {
        private static final int RC4_KEYSIZE = 16;
        private final PKCS12PBECipherCore core = new PKCS12PBECipherCore("RC4", 16);
        private final ARCFOURCipher cipher = new ARCFOURCipher();

        @Override // javax.crypto.CipherSpi
        protected byte[] engineDoFinal(byte[] bArr, int i2, int i3) throws BadPaddingException, IllegalBlockSizeException {
            return this.cipher.engineDoFinal(bArr, i2, i3);
        }

        @Override // javax.crypto.CipherSpi
        protected int engineDoFinal(byte[] bArr, int i2, int i3, byte[] bArr2, int i4) throws BadPaddingException, IllegalBlockSizeException, ShortBufferException {
            return this.cipher.engineDoFinal(bArr, i2, i3, bArr2, i4);
        }

        @Override // javax.crypto.CipherSpi
        protected int engineGetBlockSize() {
            return this.cipher.engineGetBlockSize();
        }

        @Override // javax.crypto.CipherSpi
        protected byte[] engineGetIV() {
            return this.cipher.engineGetIV();
        }

        @Override // javax.crypto.CipherSpi
        protected int engineGetKeySize(Key key) throws InvalidKeyException {
            return 16;
        }

        @Override // javax.crypto.CipherSpi
        protected int engineGetOutputSize(int i2) {
            return this.cipher.engineGetOutputSize(i2);
        }

        @Override // javax.crypto.CipherSpi
        protected AlgorithmParameters engineGetParameters() {
            return this.core.implGetParameters();
        }

        @Override // javax.crypto.CipherSpi
        protected void engineInit(int i2, Key key, AlgorithmParameterSpec algorithmParameterSpec, SecureRandom secureRandom) throws InvalidKeyException, InvalidAlgorithmParameterException {
            this.core.implInit(i2, key, algorithmParameterSpec, secureRandom, this.cipher);
        }

        @Override // javax.crypto.CipherSpi
        protected void engineInit(int i2, Key key, AlgorithmParameters algorithmParameters, SecureRandom secureRandom) throws InvalidKeyException, InvalidAlgorithmParameterException {
            this.core.implInit(i2, key, algorithmParameters, secureRandom, this.cipher);
        }

        @Override // javax.crypto.CipherSpi
        protected void engineInit(int i2, Key key, SecureRandom secureRandom) throws InvalidKeyException {
            this.core.implInit(i2, key, secureRandom, this.cipher);
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
        protected Key engineUnwrap(byte[] bArr, String str, int i2) throws NoSuchAlgorithmException, InvalidKeyException {
            return this.cipher.engineUnwrap(bArr, str, i2);
        }

        @Override // javax.crypto.CipherSpi
        protected byte[] engineUpdate(byte[] bArr, int i2, int i3) {
            return this.cipher.engineUpdate(bArr, i2, i3);
        }

        @Override // javax.crypto.CipherSpi
        protected int engineUpdate(byte[] bArr, int i2, int i3, byte[] bArr2, int i4) throws ShortBufferException {
            return this.cipher.engineUpdate(bArr, i2, i3, bArr2, i4);
        }

        @Override // javax.crypto.CipherSpi
        protected byte[] engineWrap(Key key) throws IllegalBlockSizeException, InvalidKeyException {
            return this.cipher.engineWrap(key);
        }
    }
}
