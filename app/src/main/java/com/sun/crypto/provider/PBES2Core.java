package com.sun.crypto.provider;

import com.sun.crypto.provider.PBKDF2Core;
import java.security.AlgorithmParameters;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.InvalidParameterSpecException;
import java.util.Arrays;
import javax.crypto.BadPaddingException;
import javax.crypto.CipherSpi;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.ShortBufferException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/* loaded from: sunjce_provider.jar:com/sun/crypto/provider/PBES2Core.class */
abstract class PBES2Core extends CipherSpi {
    private static final int DEFAULT_SALT_LENGTH = 20;
    private static final int DEFAULT_COUNT = 4096;
    private final CipherCore cipher;
    private final int keyLength;
    private final int blkSize;
    private final PBKDF2Core kdf;
    private final String pbeAlgo;
    private final String cipherAlgo;
    private int iCount = 4096;
    private byte[] salt = null;
    private IvParameterSpec ivSpec = null;

    PBES2Core(String str, String str2, int i2) throws NoSuchPaddingException, NoSuchAlgorithmException {
        this.cipherAlgo = str2;
        this.keyLength = i2 * 8;
        this.pbeAlgo = "PBEWith" + str + "And" + str2 + "_" + this.keyLength;
        if (str2.equals("AES")) {
            this.blkSize = 16;
            this.cipher = new CipherCore(new AESCrypt(), this.blkSize);
            switch (str) {
                case "HmacSHA1":
                    this.kdf = new PBKDF2Core.HmacSHA1();
                    break;
                case "HmacSHA224":
                    this.kdf = new PBKDF2Core.HmacSHA224();
                    break;
                case "HmacSHA256":
                    this.kdf = new PBKDF2Core.HmacSHA256();
                    break;
                case "HmacSHA384":
                    this.kdf = new PBKDF2Core.HmacSHA384();
                    break;
                case "HmacSHA512":
                    this.kdf = new PBKDF2Core.HmacSHA512();
                    break;
                default:
                    throw new NoSuchAlgorithmException("No Cipher implementation for " + str);
            }
            this.cipher.setMode("CBC");
            this.cipher.setPadding("PKCS5Padding");
            return;
        }
        throw new NoSuchAlgorithmException("No Cipher implementation for " + this.pbeAlgo);
    }

    @Override // javax.crypto.CipherSpi
    protected void engineSetMode(String str) throws NoSuchAlgorithmException {
        if (str != null && !str.equalsIgnoreCase("CBC")) {
            throw new NoSuchAlgorithmException("Invalid cipher mode: " + str);
        }
    }

    @Override // javax.crypto.CipherSpi
    protected void engineSetPadding(String str) throws NoSuchPaddingException {
        if (str != null && !str.equalsIgnoreCase("PKCS5Padding")) {
            throw new NoSuchPaddingException("Invalid padding scheme: " + str);
        }
    }

    @Override // javax.crypto.CipherSpi
    protected int engineGetBlockSize() {
        return this.blkSize;
    }

    @Override // javax.crypto.CipherSpi
    protected int engineGetOutputSize(int i2) {
        return this.cipher.getOutputSize(i2);
    }

    @Override // javax.crypto.CipherSpi
    protected byte[] engineGetIV() {
        return this.cipher.getIV();
    }

    @Override // javax.crypto.CipherSpi
    protected AlgorithmParameters engineGetParameters() {
        if (this.salt == null) {
            this.salt = new byte[20];
            SunJCE.getRandom().nextBytes(this.salt);
            this.iCount = 4096;
        }
        if (this.ivSpec == null) {
            byte[] bArr = new byte[this.blkSize];
            SunJCE.getRandom().nextBytes(bArr);
            this.ivSpec = new IvParameterSpec(bArr);
        }
        PBEParameterSpec pBEParameterSpec = new PBEParameterSpec(this.salt, this.iCount, this.ivSpec);
        try {
            AlgorithmParameters algorithmParameters = AlgorithmParameters.getInstance(this.pbeAlgo, SunJCE.getInstance());
            algorithmParameters.init(pBEParameterSpec);
            return algorithmParameters;
        } catch (NoSuchAlgorithmException e2) {
            throw new RuntimeException("SunJCE called, but not configured");
        } catch (InvalidParameterSpecException e3) {
            throw new RuntimeException("PBEParameterSpec not supported");
        }
    }

    @Override // javax.crypto.CipherSpi
    protected void engineInit(int i2, Key key, SecureRandom secureRandom) throws InvalidKeyException {
        try {
            engineInit(i2, key, (AlgorithmParameterSpec) null, secureRandom);
        } catch (InvalidAlgorithmParameterException e2) {
            InvalidKeyException invalidKeyException = new InvalidKeyException("requires PBE parameters");
            invalidKeyException.initCause(e2);
            throw invalidKeyException;
        }
    }

    @Override // javax.crypto.CipherSpi
    protected void engineInit(int i2, Key key, AlgorithmParameterSpec algorithmParameterSpec, SecureRandom secureRandom) throws InvalidKeyException, InvalidAlgorithmParameterException {
        if (key == null) {
            throw new InvalidKeyException("Null key");
        }
        byte[] encoded = key.getEncoded();
        if (encoded != null) {
            try {
                if (key.getAlgorithm().regionMatches(true, 0, "PBE", 0, 3)) {
                    if (key instanceof javax.crypto.interfaces.PBEKey) {
                        this.salt = ((javax.crypto.interfaces.PBEKey) key).getSalt();
                        if (this.salt != null && this.salt.length < 8) {
                            throw new InvalidAlgorithmParameterException("Salt must be at least 8 bytes long");
                        }
                        this.iCount = ((javax.crypto.interfaces.PBEKey) key).getIterationCount();
                        if (this.iCount == 0) {
                            this.iCount = 4096;
                        } else if (this.iCount < 0) {
                            throw new InvalidAlgorithmParameterException("Iteration count must be a positive number");
                        }
                    }
                    if (algorithmParameterSpec == null) {
                        if (this.salt == null) {
                            this.salt = new byte[20];
                            secureRandom.nextBytes(this.salt);
                            this.iCount = 4096;
                        }
                        if (i2 == 1 || i2 == 3) {
                            byte[] bArr = new byte[this.blkSize];
                            secureRandom.nextBytes(bArr);
                            this.ivSpec = new IvParameterSpec(bArr);
                        }
                    } else {
                        if (!(algorithmParameterSpec instanceof PBEParameterSpec)) {
                            throw new InvalidAlgorithmParameterException("Wrong parameter type: PBE expected");
                        }
                        byte[] salt = ((PBEParameterSpec) algorithmParameterSpec).getSalt();
                        if (salt != null && salt.length < 8) {
                            throw new InvalidAlgorithmParameterException("Salt must be at least 8 bytes long");
                        }
                        this.salt = salt;
                        int iterationCount = ((PBEParameterSpec) algorithmParameterSpec).getIterationCount();
                        if (iterationCount == 0) {
                            iterationCount = 4096;
                        } else if (iterationCount < 0) {
                            throw new InvalidAlgorithmParameterException("Iteration count must be a positive number");
                        }
                        this.iCount = iterationCount;
                        AlgorithmParameterSpec parameterSpec = ((PBEParameterSpec) algorithmParameterSpec).getParameterSpec();
                        if (parameterSpec != null) {
                            if (parameterSpec instanceof IvParameterSpec) {
                                this.ivSpec = (IvParameterSpec) parameterSpec;
                            } else {
                                throw new InvalidAlgorithmParameterException("Wrong parameter type: IV expected");
                            }
                        } else if (i2 == 1 || i2 == 3) {
                            byte[] bArr2 = new byte[this.blkSize];
                            secureRandom.nextBytes(bArr2);
                            this.ivSpec = new IvParameterSpec(bArr2);
                        } else {
                            throw new InvalidAlgorithmParameterException("Missing parameter type: IV expected");
                        }
                    }
                    char[] cArr = new char[encoded.length];
                    for (int i3 = 0; i3 < cArr.length; i3++) {
                        cArr[i3] = (char) (encoded[i3] & Byte.MAX_VALUE);
                    }
                    PBEKeySpec pBEKeySpec = new PBEKeySpec(cArr, this.salt, this.iCount, this.keyLength);
                    if (cArr != null) {
                        Arrays.fill(cArr, (char) 0);
                    }
                    if (encoded != null) {
                        Arrays.fill(encoded, (byte) 0);
                    }
                    try {
                        this.cipher.init(i2, new SecretKeySpec(this.kdf.engineGenerateSecret(pBEKeySpec).getEncoded(), this.cipherAlgo), this.ivSpec, secureRandom);
                        return;
                    } catch (InvalidKeySpecException e2) {
                        InvalidKeyException invalidKeyException = new InvalidKeyException("Cannot construct PBE key");
                        invalidKeyException.initCause(e2);
                        throw invalidKeyException;
                    }
                }
            } catch (Throwable th) {
                if (0 != 0) {
                    Arrays.fill((char[]) null, (char) 0);
                }
                if (encoded != null) {
                    Arrays.fill(encoded, (byte) 0);
                }
                throw th;
            }
        }
        throw new InvalidKeyException("Missing password");
    }

    @Override // javax.crypto.CipherSpi
    protected void engineInit(int i2, Key key, AlgorithmParameters algorithmParameters, SecureRandom secureRandom) throws InvalidKeyException, InvalidAlgorithmParameterException {
        AlgorithmParameterSpec parameterSpec = null;
        if (algorithmParameters != null) {
            try {
                parameterSpec = algorithmParameters.getParameterSpec(PBEParameterSpec.class);
            } catch (InvalidParameterSpecException e2) {
                throw new InvalidAlgorithmParameterException("Wrong parameter type: PBE expected");
            }
        }
        engineInit(i2, key, parameterSpec, secureRandom);
    }

    @Override // javax.crypto.CipherSpi
    protected byte[] engineUpdate(byte[] bArr, int i2, int i3) {
        return this.cipher.update(bArr, i2, i3);
    }

    @Override // javax.crypto.CipherSpi
    protected int engineUpdate(byte[] bArr, int i2, int i3, byte[] bArr2, int i4) throws ShortBufferException {
        return this.cipher.update(bArr, i2, i3, bArr2, i4);
    }

    @Override // javax.crypto.CipherSpi
    protected byte[] engineDoFinal(byte[] bArr, int i2, int i3) throws BadPaddingException, IllegalBlockSizeException {
        return this.cipher.doFinal(bArr, i2, i3);
    }

    @Override // javax.crypto.CipherSpi
    protected int engineDoFinal(byte[] bArr, int i2, int i3, byte[] bArr2, int i4) throws BadPaddingException, IllegalBlockSizeException, ShortBufferException {
        return this.cipher.doFinal(bArr, i2, i3, bArr2, i4);
    }

    @Override // javax.crypto.CipherSpi
    protected int engineGetKeySize(Key key) throws InvalidKeyException {
        return this.keyLength;
    }

    @Override // javax.crypto.CipherSpi
    protected byte[] engineWrap(Key key) throws IllegalBlockSizeException, InvalidKeyException {
        return this.cipher.wrap(key);
    }

    @Override // javax.crypto.CipherSpi
    protected Key engineUnwrap(byte[] bArr, String str, int i2) throws NoSuchAlgorithmException, InvalidKeyException {
        return this.cipher.unwrap(bArr, str, i2);
    }

    /* loaded from: sunjce_provider.jar:com/sun/crypto/provider/PBES2Core$HmacSHA1AndAES_128.class */
    public static final class HmacSHA1AndAES_128 extends PBES2Core {
        public HmacSHA1AndAES_128() throws NoSuchPaddingException, NoSuchAlgorithmException {
            super("HmacSHA1", "AES", 16);
        }
    }

    /* loaded from: sunjce_provider.jar:com/sun/crypto/provider/PBES2Core$HmacSHA224AndAES_128.class */
    public static final class HmacSHA224AndAES_128 extends PBES2Core {
        public HmacSHA224AndAES_128() throws NoSuchPaddingException, NoSuchAlgorithmException {
            super("HmacSHA224", "AES", 16);
        }
    }

    /* loaded from: sunjce_provider.jar:com/sun/crypto/provider/PBES2Core$HmacSHA256AndAES_128.class */
    public static final class HmacSHA256AndAES_128 extends PBES2Core {
        public HmacSHA256AndAES_128() throws NoSuchPaddingException, NoSuchAlgorithmException {
            super("HmacSHA256", "AES", 16);
        }
    }

    /* loaded from: sunjce_provider.jar:com/sun/crypto/provider/PBES2Core$HmacSHA384AndAES_128.class */
    public static final class HmacSHA384AndAES_128 extends PBES2Core {
        public HmacSHA384AndAES_128() throws NoSuchPaddingException, NoSuchAlgorithmException {
            super("HmacSHA384", "AES", 16);
        }
    }

    /* loaded from: sunjce_provider.jar:com/sun/crypto/provider/PBES2Core$HmacSHA512AndAES_128.class */
    public static final class HmacSHA512AndAES_128 extends PBES2Core {
        public HmacSHA512AndAES_128() throws NoSuchPaddingException, NoSuchAlgorithmException {
            super("HmacSHA512", "AES", 16);
        }
    }

    /* loaded from: sunjce_provider.jar:com/sun/crypto/provider/PBES2Core$HmacSHA1AndAES_256.class */
    public static final class HmacSHA1AndAES_256 extends PBES2Core {
        public HmacSHA1AndAES_256() throws NoSuchPaddingException, NoSuchAlgorithmException {
            super("HmacSHA1", "AES", 32);
        }
    }

    /* loaded from: sunjce_provider.jar:com/sun/crypto/provider/PBES2Core$HmacSHA224AndAES_256.class */
    public static final class HmacSHA224AndAES_256 extends PBES2Core {
        public HmacSHA224AndAES_256() throws NoSuchPaddingException, NoSuchAlgorithmException {
            super("HmacSHA224", "AES", 32);
        }
    }

    /* loaded from: sunjce_provider.jar:com/sun/crypto/provider/PBES2Core$HmacSHA256AndAES_256.class */
    public static final class HmacSHA256AndAES_256 extends PBES2Core {
        public HmacSHA256AndAES_256() throws NoSuchPaddingException, NoSuchAlgorithmException {
            super("HmacSHA256", "AES", 32);
        }
    }

    /* loaded from: sunjce_provider.jar:com/sun/crypto/provider/PBES2Core$HmacSHA384AndAES_256.class */
    public static final class HmacSHA384AndAES_256 extends PBES2Core {
        public HmacSHA384AndAES_256() throws NoSuchPaddingException, NoSuchAlgorithmException {
            super("HmacSHA384", "AES", 32);
        }
    }

    /* loaded from: sunjce_provider.jar:com/sun/crypto/provider/PBES2Core$HmacSHA512AndAES_256.class */
    public static final class HmacSHA512AndAES_256 extends PBES2Core {
        public HmacSHA512AndAES_256() throws NoSuchPaddingException, NoSuchAlgorithmException {
            super("HmacSHA512", "AES", 32);
        }
    }
}
