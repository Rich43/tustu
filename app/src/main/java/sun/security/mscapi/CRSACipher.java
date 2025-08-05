package sun.security.mscapi;

import java.math.BigInteger;
import java.security.AlgorithmParameters;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyException;
import java.security.KeyFactory;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.ProviderException;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.interfaces.RSAKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import javax.crypto.BadPaddingException;
import javax.crypto.CipherSpi;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.ShortBufferException;
import javax.crypto.spec.SecretKeySpec;
import sun.security.internal.spec.TlsRsaPremasterSecretParameterSpec;
import sun.security.mscapi.CSignature;
import sun.security.rsa.RSAKeyFactory;
import sun.security.util.KeyUtil;

/* loaded from: sunmscapi.jar:sun/security/mscapi/CRSACipher.class */
public final class CRSACipher extends CipherSpi {
    private static final byte[] B0 = new byte[0];
    private static final int MODE_ENCRYPT = 1;
    private static final int MODE_DECRYPT = 2;
    private static final int MODE_SIGN = 3;
    private static final int MODE_VERIFY = 4;
    private static final String PAD_PKCS1 = "PKCS1Padding";
    private static final int PAD_PKCS1_LENGTH = 11;
    private int mode;
    private byte[] buffer;
    private int bufOfs;
    private int outputSize;
    private CKey publicKey;
    private CKey privateKey;
    private SecureRandom random;
    private int paddingLength = 0;
    private AlgorithmParameterSpec spec = null;
    private String paddingType = PAD_PKCS1;

    private static native byte[] encryptDecrypt(byte[] bArr, int i2, long j2, boolean z2) throws KeyException;

    @Override // javax.crypto.CipherSpi
    protected void engineSetMode(String str) throws NoSuchAlgorithmException {
        if (!str.equalsIgnoreCase("ECB")) {
            throw new NoSuchAlgorithmException("Unsupported mode " + str);
        }
    }

    @Override // javax.crypto.CipherSpi
    protected void engineSetPadding(String str) throws NoSuchPaddingException {
        if (str.equalsIgnoreCase(PAD_PKCS1)) {
            this.paddingType = PAD_PKCS1;
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
        return null;
    }

    @Override // javax.crypto.CipherSpi
    protected void engineInit(int i2, Key key, SecureRandom secureRandom) throws InvalidKeyException {
        init(i2, key);
    }

    @Override // javax.crypto.CipherSpi
    protected void engineInit(int i2, Key key, AlgorithmParameterSpec algorithmParameterSpec, SecureRandom secureRandom) throws InvalidKeyException, InvalidAlgorithmParameterException {
        if (algorithmParameterSpec != null) {
            if (!(algorithmParameterSpec instanceof TlsRsaPremasterSecretParameterSpec)) {
                throw new InvalidAlgorithmParameterException("Parameters not supported");
            }
            this.spec = algorithmParameterSpec;
            this.random = secureRandom;
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
        boolean z2;
        switch (i2) {
            case 1:
            case 3:
                this.paddingLength = 11;
                z2 = true;
                break;
            case 2:
            case 4:
                this.paddingLength = 0;
                z2 = false;
                break;
            default:
                throw new InvalidKeyException("Unknown mode: " + i2);
        }
        if (!(key instanceof CKey)) {
            if (key instanceof RSAPublicKey) {
                RSAPublicKey rSAPublicKey = (RSAPublicKey) key;
                BigInteger modulus = rSAPublicKey.getModulus();
                BigInteger publicExponent = rSAPublicKey.getPublicExponent();
                RSAKeyFactory.checkKeyLengths((modulus.bitLength() + 7) & (-8), publicExponent, -1, 16384);
                byte[] byteArray = modulus.toByteArray();
                byte[] byteArray2 = publicExponent.toByteArray();
                int length = byteArray[0] == 0 ? (byteArray.length - 1) * 8 : byteArray.length * 8;
                try {
                    key = CSignature.importPublicKey("RSA", CSignature.RSA.generatePublicKeyBlob(length, byteArray, byteArray2), length);
                } catch (KeyStoreException e2) {
                    throw new InvalidKeyException(e2);
                }
            } else {
                throw new InvalidKeyException("Unsupported key type: " + ((Object) key));
            }
        }
        if (key instanceof PublicKey) {
            this.mode = z2 ? 1 : 4;
            this.publicKey = (CKey) key;
            this.privateKey = null;
            this.outputSize = this.publicKey.length() / 8;
        } else if (key instanceof PrivateKey) {
            this.mode = z2 ? 3 : 2;
            this.privateKey = (CKey) key;
            this.publicKey = null;
            this.outputSize = this.privateKey.length() / 8;
        } else {
            throw new InvalidKeyException("Unknown key type: " + ((Object) key));
        }
        this.bufOfs = 0;
        this.buffer = new byte[this.outputSize];
    }

    private void update(byte[] bArr, int i2, int i3) {
        if (i3 == 0 || bArr == null) {
            return;
        }
        if (this.bufOfs + i3 > this.buffer.length - this.paddingLength) {
            this.bufOfs = this.buffer.length + 1;
        } else {
            System.arraycopy(bArr, i2, this.buffer, this.bufOfs, i3);
            this.bufOfs += i3;
        }
    }

    private byte[] doFinal() throws BadPaddingException, IllegalBlockSizeException {
        try {
            if (this.bufOfs > this.buffer.length) {
                throw new IllegalBlockSizeException("Data must not be longer than " + (this.buffer.length - this.paddingLength) + " bytes");
            }
            try {
                byte[] bArr = this.buffer;
                switch (this.mode) {
                    case 1:
                        byte[] bArrEncryptDecrypt = encryptDecrypt(bArr, this.bufOfs, this.publicKey.getHCryptKey(), true);
                        this.bufOfs = 0;
                        return bArrEncryptDecrypt;
                    case 2:
                        byte[] bArrEncryptDecrypt2 = encryptDecrypt(bArr, this.bufOfs, this.privateKey.getHCryptKey(), false);
                        this.bufOfs = 0;
                        return bArrEncryptDecrypt2;
                    case 3:
                        byte[] bArrEncryptDecrypt3 = encryptDecrypt(bArr, this.bufOfs, this.privateKey.getHCryptKey(), true);
                        this.bufOfs = 0;
                        return bArrEncryptDecrypt3;
                    case 4:
                        byte[] bArrEncryptDecrypt4 = encryptDecrypt(bArr, this.bufOfs, this.publicKey.getHCryptKey(), false);
                        this.bufOfs = 0;
                        return bArrEncryptDecrypt4;
                    default:
                        throw new AssertionError((Object) "Internal error");
                }
            } catch (KeyException e2) {
                throw new ProviderException(e2);
            }
        } catch (Throwable th) {
            this.bufOfs = 0;
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
        return length;
    }

    @Override // javax.crypto.CipherSpi
    protected byte[] engineWrap(Key key) throws IllegalBlockSizeException, InvalidKeyException {
        byte[] encoded = key.getEncoded();
        if (encoded == null || encoded.length == 0) {
            throw new InvalidKeyException("Could not obtain encoded key");
        }
        if (encoded.length > this.buffer.length) {
            throw new InvalidKeyException("Key is too long for wrapping");
        }
        update(encoded, 0, encoded.length);
        try {
            return doFinal();
        } catch (BadPaddingException e2) {
            throw new InvalidKeyException("Wrapping failed", e2);
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
            if (!(this.spec instanceof TlsRsaPremasterSecretParameterSpec)) {
                throw new IllegalStateException("No TlsRsaPremasterSecretParameterSpec specified");
            }
            bArrCheckTlsPreMasterSecretKey = KeyUtil.checkTlsPreMasterSecretKey(((TlsRsaPremasterSecretParameterSpec) this.spec).getClientVersion(), ((TlsRsaPremasterSecretParameterSpec) this.spec).getServerVersion(), this.random, bArrCheckTlsPreMasterSecretKey, badPaddingException != null);
        }
        return constructKey(bArrCheckTlsPreMasterSecretKey, str, i2);
    }

    @Override // javax.crypto.CipherSpi
    protected int engineGetKeySize(Key key) throws InvalidKeyException {
        if (key instanceof CKey) {
            return ((CKey) key).length();
        }
        if (key instanceof RSAKey) {
            return ((RSAKey) key).getModulus().bitLength();
        }
        throw new InvalidKeyException("Unsupported key type: " + ((Object) key));
    }

    private static PublicKey constructPublicKey(byte[] bArr, String str) throws NoSuchAlgorithmException, InvalidKeyException {
        try {
            return KeyFactory.getInstance(str).generatePublic(new X509EncodedKeySpec(bArr));
        } catch (NoSuchAlgorithmException e2) {
            throw new NoSuchAlgorithmException("No installed provider supports the " + str + " algorithm", e2);
        } catch (InvalidKeySpecException e3) {
            throw new InvalidKeyException("Cannot construct public key", e3);
        }
    }

    private static PrivateKey constructPrivateKey(byte[] bArr, String str) throws NoSuchAlgorithmException, InvalidKeyException {
        try {
            return KeyFactory.getInstance(str).generatePrivate(new PKCS8EncodedKeySpec(bArr));
        } catch (NoSuchAlgorithmException e2) {
            throw new NoSuchAlgorithmException("No installed provider supports the " + str + " algorithm", e2);
        } catch (InvalidKeySpecException e3) {
            throw new InvalidKeyException("Cannot construct private key", e3);
        }
    }

    private static SecretKey constructSecretKey(byte[] bArr, String str) {
        return new SecretKeySpec(bArr, str);
    }

    private static Key constructKey(byte[] bArr, String str, int i2) throws NoSuchAlgorithmException, InvalidKeyException {
        switch (i2) {
            case 1:
                return constructPublicKey(bArr, str);
            case 2:
                return constructPrivateKey(bArr, str);
            case 3:
                return constructSecretKey(bArr, str);
            default:
                throw new InvalidKeyException("Unknown key type " + i2);
        }
    }
}
