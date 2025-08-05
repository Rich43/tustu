package com.sun.crypto.provider;

import java.security.AlgorithmParameters;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.interfaces.RSAKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.InvalidParameterSpecException;
import java.security.spec.MGF1ParameterSpec;
import java.util.Arrays;
import java.util.Locale;
import javax.crypto.BadPaddingException;
import javax.crypto.CipherSpi;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.ShortBufferException;
import javax.crypto.spec.OAEPParameterSpec;
import javax.crypto.spec.PSource;
import sun.security.internal.spec.TlsRsaPremasterSecretParameterSpec;
import sun.security.jca.Providers;
import sun.security.rsa.RSACore;
import sun.security.rsa.RSAKeyFactory;
import sun.security.rsa.RSAPadding;
import sun.security.util.KeyUtil;

/* loaded from: sunjce_provider.jar:com/sun/crypto/provider/RSACipher.class */
public final class RSACipher extends CipherSpi {
    private static final byte[] B0 = new byte[0];
    private static final int MODE_ENCRYPT = 1;
    private static final int MODE_DECRYPT = 2;
    private static final int MODE_SIGN = 3;
    private static final int MODE_VERIFY = 4;
    private static final String PAD_NONE = "NoPadding";
    private static final String PAD_PKCS1 = "PKCS1Padding";
    private static final String PAD_OAEP_MGF1 = "OAEP";
    private int mode;
    private RSAPadding padding;
    private byte[] buffer;
    private int bufOfs;
    private int outputSize;
    private RSAPublicKey publicKey;
    private RSAPrivateKey privateKey;
    private SecureRandom random;
    private AlgorithmParameterSpec spec = null;
    private String oaepHashAlgorithm = "SHA-1";
    private String paddingType = PAD_PKCS1;

    @Override // javax.crypto.CipherSpi
    protected void engineSetMode(String str) throws NoSuchAlgorithmException {
        if (!str.equalsIgnoreCase("ECB")) {
            throw new NoSuchAlgorithmException("Unsupported mode " + str);
        }
    }

    @Override // javax.crypto.CipherSpi
    protected void engineSetPadding(String str) throws NoSuchPaddingException {
        if (str.equalsIgnoreCase(PAD_NONE)) {
            this.paddingType = PAD_NONE;
            return;
        }
        if (str.equalsIgnoreCase(PAD_PKCS1)) {
            this.paddingType = PAD_PKCS1;
            return;
        }
        String lowerCase = str.toLowerCase(Locale.ENGLISH);
        if (lowerCase.equals("oaeppadding")) {
            this.paddingType = PAD_OAEP_MGF1;
            return;
        }
        if (lowerCase.startsWith("oaepwith") && lowerCase.endsWith("andmgf1padding")) {
            this.paddingType = PAD_OAEP_MGF1;
            this.oaepHashAlgorithm = str.substring(8, str.length() - 14);
            if (Providers.getProviderList().getService("MessageDigest", this.oaepHashAlgorithm) == null) {
                throw new NoSuchPaddingException("MessageDigest not available for " + str);
            }
            return;
        }
        throw new NoSuchPaddingException("Padding " + str + " not supported");
    }

    @Override // javax.crypto.CipherSpi
    protected int engineGetBlockSize() {
        return 0;
    }

    @Override // javax.crypto.CipherSpi
    protected int engineGetOutputSize(int i2) {
        return this.outputSize;
    }

    @Override // javax.crypto.CipherSpi
    protected byte[] engineGetIV() {
        return null;
    }

    @Override // javax.crypto.CipherSpi
    protected AlgorithmParameters engineGetParameters() {
        if (this.spec != null && (this.spec instanceof OAEPParameterSpec)) {
            try {
                AlgorithmParameters algorithmParameters = AlgorithmParameters.getInstance(PAD_OAEP_MGF1, SunJCE.getInstance());
                algorithmParameters.init(this.spec);
                return algorithmParameters;
            } catch (NoSuchAlgorithmException e2) {
                throw new RuntimeException("Cannot find OAEP  AlgorithmParameters implementation in SunJCE provider");
            } catch (InvalidParameterSpecException e3) {
                throw new RuntimeException("OAEPParameterSpec not supported");
            }
        }
        return null;
    }

    @Override // javax.crypto.CipherSpi
    protected void engineInit(int i2, Key key, SecureRandom secureRandom) throws InvalidKeyException {
        try {
            init(i2, key, secureRandom, null);
        } catch (InvalidAlgorithmParameterException e2) {
            InvalidKeyException invalidKeyException = new InvalidKeyException("Wrong parameters");
            invalidKeyException.initCause(e2);
            throw invalidKeyException;
        }
    }

    @Override // javax.crypto.CipherSpi
    protected void engineInit(int i2, Key key, AlgorithmParameterSpec algorithmParameterSpec, SecureRandom secureRandom) throws InvalidKeyException, InvalidAlgorithmParameterException {
        init(i2, key, secureRandom, algorithmParameterSpec);
    }

    @Override // javax.crypto.CipherSpi
    protected void engineInit(int i2, Key key, AlgorithmParameters algorithmParameters, SecureRandom secureRandom) throws InvalidKeyException, InvalidAlgorithmParameterException {
        if (algorithmParameters == null) {
            init(i2, key, secureRandom, null);
            return;
        }
        try {
            init(i2, key, secureRandom, (OAEPParameterSpec) algorithmParameters.getParameterSpec(OAEPParameterSpec.class));
        } catch (InvalidParameterSpecException e2) {
            InvalidAlgorithmParameterException invalidAlgorithmParameterException = new InvalidAlgorithmParameterException("Wrong parameter");
            invalidAlgorithmParameterException.initCause(e2);
            throw invalidAlgorithmParameterException;
        }
    }

    private void init(int i2, Key key, SecureRandom secureRandom, AlgorithmParameterSpec algorithmParameterSpec) throws InvalidKeyException, InvalidAlgorithmParameterException {
        boolean z2;
        switch (i2) {
            case 1:
            case 3:
                z2 = true;
                break;
            case 2:
            case 4:
                z2 = false;
                break;
            default:
                throw new InvalidKeyException("Unknown mode: " + i2);
        }
        RSAKey rSAKey = RSAKeyFactory.toRSAKey(key);
        if (key instanceof RSAPublicKey) {
            this.mode = z2 ? 1 : 4;
            this.publicKey = (RSAPublicKey) key;
            this.privateKey = null;
        } else {
            this.mode = z2 ? 3 : 2;
            this.privateKey = (RSAPrivateKey) key;
            this.publicKey = null;
        }
        int byteLength = RSACore.getByteLength(rSAKey.getModulus());
        this.outputSize = byteLength;
        this.bufOfs = 0;
        if (this.paddingType == PAD_NONE) {
            if (algorithmParameterSpec != null) {
                throw new InvalidAlgorithmParameterException("Parameters not supported");
            }
            this.padding = RSAPadding.getInstance(3, byteLength, secureRandom);
            this.buffer = new byte[byteLength];
            return;
        }
        if (this.paddingType == PAD_PKCS1) {
            if (algorithmParameterSpec != null) {
                if (!(algorithmParameterSpec instanceof TlsRsaPremasterSecretParameterSpec)) {
                    throw new InvalidAlgorithmParameterException("Parameters not supported");
                }
                this.spec = algorithmParameterSpec;
                this.random = secureRandom;
            }
            this.padding = RSAPadding.getInstance(this.mode <= 2 ? 2 : 1, byteLength, secureRandom);
            if (z2) {
                this.buffer = new byte[this.padding.getMaxDataSize()];
                return;
            } else {
                this.buffer = new byte[byteLength];
                return;
            }
        }
        if (this.mode == 3 || this.mode == 4) {
            throw new InvalidKeyException("OAEP cannot be used to sign or verify signatures");
        }
        if (algorithmParameterSpec != null) {
            if (!(algorithmParameterSpec instanceof OAEPParameterSpec)) {
                throw new InvalidAlgorithmParameterException("Wrong Parameters for OAEP Padding");
            }
            this.spec = algorithmParameterSpec;
        } else {
            this.spec = new OAEPParameterSpec(this.oaepHashAlgorithm, "MGF1", MGF1ParameterSpec.SHA1, PSource.PSpecified.DEFAULT);
        }
        this.padding = RSAPadding.getInstance(4, byteLength, secureRandom, (OAEPParameterSpec) this.spec);
        if (z2) {
            this.buffer = new byte[this.padding.getMaxDataSize()];
        } else {
            this.buffer = new byte[byteLength];
        }
    }

    private void update(byte[] bArr, int i2, int i3) {
        if (i3 == 0 || bArr == null) {
            return;
        }
        if (i3 > this.buffer.length - this.bufOfs) {
            this.bufOfs = this.buffer.length + 1;
        } else {
            System.arraycopy(bArr, i2, this.buffer, this.bufOfs, i3);
            this.bufOfs += i3;
        }
    }

    private byte[] doFinal() throws BadPaddingException, IllegalBlockSizeException {
        byte[] bArrRsa;
        byte[] bArrUnpad;
        if (this.bufOfs > this.buffer.length) {
            throw new IllegalBlockSizeException("Data must not be longer than " + this.buffer.length + " bytes");
        }
        try {
            switch (this.mode) {
                case 1:
                    bArrRsa = this.padding.pad(this.buffer, 0, this.bufOfs);
                    bArrUnpad = RSACore.rsa(bArrRsa, this.publicKey);
                    break;
                case 2:
                    bArrRsa = RSACore.rsa(RSACore.convert(this.buffer, 0, this.bufOfs), this.privateKey, false);
                    bArrUnpad = this.padding.unpad(bArrRsa);
                    break;
                case 3:
                    bArrRsa = this.padding.pad(this.buffer, 0, this.bufOfs);
                    bArrUnpad = RSACore.rsa(bArrRsa, this.privateKey, true);
                    break;
                case 4:
                    bArrRsa = RSACore.rsa(RSACore.convert(this.buffer, 0, this.bufOfs), this.publicKey);
                    bArrUnpad = this.padding.unpad(bArrRsa);
                    break;
                default:
                    throw new AssertionError((Object) "Internal error");
            }
            byte[] bArr = bArrUnpad;
            Arrays.fill(this.buffer, 0, this.bufOfs, (byte) 0);
            this.bufOfs = 0;
            if (bArrRsa != null && bArrRsa != this.buffer && bArrRsa != bArrUnpad) {
                Arrays.fill(bArrRsa, (byte) 0);
            }
            return bArr;
        } catch (Throwable th) {
            Arrays.fill(this.buffer, 0, this.bufOfs, (byte) 0);
            this.bufOfs = 0;
            if (0 != 0 && null != this.buffer && 0 != 0) {
                Arrays.fill((byte[]) null, (byte) 0);
            }
            throw th;
        }
    }

    @Override // javax.crypto.CipherSpi
    protected byte[] engineUpdate(byte[] bArr, int i2, int i3) {
        update(bArr, i2, i3);
        return B0;
    }

    @Override // javax.crypto.CipherSpi
    protected int engineUpdate(byte[] bArr, int i2, int i3, byte[] bArr2, int i4) {
        update(bArr, i2, i3);
        return 0;
    }

    @Override // javax.crypto.CipherSpi
    protected byte[] engineDoFinal(byte[] bArr, int i2, int i3) throws BadPaddingException, IllegalBlockSizeException {
        update(bArr, i2, i3);
        return doFinal();
    }

    @Override // javax.crypto.CipherSpi
    protected int engineDoFinal(byte[] bArr, int i2, int i3, byte[] bArr2, int i4) throws BadPaddingException, IllegalBlockSizeException, ShortBufferException {
        if (this.outputSize > bArr2.length - i4) {
            throw new ShortBufferException("Need " + this.outputSize + " bytes for output");
        }
        update(bArr, i2, i3);
        byte[] bArrDoFinal = doFinal();
        int length = bArrDoFinal.length;
        System.arraycopy(bArrDoFinal, 0, bArr2, i4, length);
        Arrays.fill(bArrDoFinal, (byte) 0);
        return length;
    }

    @Override // javax.crypto.CipherSpi
    protected byte[] engineWrap(Key key) throws IllegalBlockSizeException, InvalidKeyException {
        byte[] encoded = key.getEncoded();
        if (encoded == null || encoded.length == 0) {
            throw new InvalidKeyException("Could not obtain encoded key");
        }
        try {
            if (encoded.length > this.buffer.length) {
                throw new InvalidKeyException("Key is too long for wrapping");
            }
            update(encoded, 0, encoded.length);
            try {
                byte[] bArrDoFinal = doFinal();
                Arrays.fill(encoded, (byte) 0);
                return bArrDoFinal;
            } catch (BadPaddingException e2) {
                throw new InvalidKeyException("Wrapping failed", e2);
            }
        } catch (Throwable th) {
            Arrays.fill(encoded, (byte) 0);
            throw th;
        }
    }

    @Override // javax.crypto.CipherSpi
    protected Key engineUnwrap(byte[] bArr, String str, int i2) throws NoSuchAlgorithmException, InvalidKeyException {
        if (bArr.length > this.buffer.length) {
            throw new InvalidKeyException("Key is too long for unwrapping");
        }
        boolean zEquals = str.equals("TlsRsaPremasterSecret");
        BadPaddingException badPaddingException = null;
        byte[] bArrCheckTlsPreMasterSecretKey = null;
        update(bArr, 0, bArr.length);
        try {
            bArrCheckTlsPreMasterSecretKey = doFinal();
        } catch (BadPaddingException e2) {
            if (zEquals) {
                badPaddingException = e2;
            } else {
                throw new InvalidKeyException("Unwrapping failed", e2);
            }
        } catch (IllegalBlockSizeException e3) {
            throw new InvalidKeyException("Unwrapping failed", e3);
        }
        if (zEquals) {
            try {
                if (!(this.spec instanceof TlsRsaPremasterSecretParameterSpec)) {
                    throw new IllegalStateException("No TlsRsaPremasterSecretParameterSpec specified");
                }
                bArrCheckTlsPreMasterSecretKey = KeyUtil.checkTlsPreMasterSecretKey(((TlsRsaPremasterSecretParameterSpec) this.spec).getClientVersion(), ((TlsRsaPremasterSecretParameterSpec) this.spec).getServerVersion(), this.random, bArrCheckTlsPreMasterSecretKey, badPaddingException != null);
            } catch (Throwable th) {
                if (bArrCheckTlsPreMasterSecretKey != null) {
                    Arrays.fill(bArrCheckTlsPreMasterSecretKey, (byte) 0);
                }
                throw th;
            }
        }
        Key keyConstructKey = ConstructKeys.constructKey(bArrCheckTlsPreMasterSecretKey, str, i2);
        if (bArrCheckTlsPreMasterSecretKey != null) {
            Arrays.fill(bArrCheckTlsPreMasterSecretKey, (byte) 0);
        }
        return keyConstructKey;
    }

    @Override // javax.crypto.CipherSpi
    protected int engineGetKeySize(Key key) throws InvalidKeyException {
        return RSAKeyFactory.toRSAKey(key).getModulus().bitLength();
    }
}
