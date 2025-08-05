package sun.security.pkcs11;

import java.security.AlgorithmParameters;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.ProviderException;
import java.security.SecureRandom;
import java.security.spec.AlgorithmParameterSpec;
import java.util.Locale;
import javax.crypto.BadPaddingException;
import javax.crypto.CipherSpi;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.ShortBufferException;
import sun.security.internal.spec.TlsRsaPremasterSecretParameterSpec;
import sun.security.pkcs11.wrapper.CK_ATTRIBUTE;
import sun.security.pkcs11.wrapper.CK_MECHANISM;
import sun.security.pkcs11.wrapper.CK_VERSION;
import sun.security.pkcs11.wrapper.PKCS11;
import sun.security.pkcs11.wrapper.PKCS11Exception;
import sun.security.util.KeyUtil;

/* loaded from: sunpkcs11.jar:sun/security/pkcs11/P11RSACipher.class */
final class P11RSACipher extends CipherSpi {
    private static final int PKCS1_MIN_PADDING_LENGTH = 11;
    private static final byte[] B0 = new byte[0];
    private static final int MODE_ENCRYPT = 1;
    private static final int MODE_DECRYPT = 2;
    private static final int MODE_SIGN = 3;
    private static final int MODE_VERIFY = 4;
    private static final int PAD_NONE = 1;
    private static final int PAD_PKCS1 = 2;
    private final Token token;
    private final long mechanism;
    private Session session;
    private int mode;
    private int padType;
    private byte[] buffer;
    private int bufOfs;
    private P11Key p11Key;
    private boolean initialized;
    private int maxInputSize;
    private int outputSize;
    private SecureRandom random;
    private AlgorithmParameterSpec spec = null;
    private final String algorithm = "RSA";

    P11RSACipher(Token token, String str, long j2) throws PKCS11Exception {
        this.token = token;
        this.mechanism = j2;
    }

    @Override // javax.crypto.CipherSpi
    protected void engineSetMode(String str) throws NoSuchAlgorithmException {
        if (!str.equalsIgnoreCase("ECB")) {
            throw new NoSuchAlgorithmException("Unsupported mode " + str);
        }
    }

    @Override // javax.crypto.CipherSpi
    protected void engineSetPadding(String str) throws NoSuchPaddingException {
        String lowerCase = str.toLowerCase(Locale.ENGLISH);
        if (lowerCase.equals("pkcs1padding")) {
            this.padType = 2;
        } else {
            if (lowerCase.equals("nopadding")) {
                this.padType = 1;
                return;
            }
            throw new NoSuchPaddingException("Unsupported padding " + str);
        }
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
        implInit(i2, key);
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
        implInit(i2, key);
    }

    @Override // javax.crypto.CipherSpi
    protected void engineInit(int i2, Key key, AlgorithmParameters algorithmParameters, SecureRandom secureRandom) throws InvalidKeyException, InvalidAlgorithmParameterException {
        if (algorithmParameters != null) {
            throw new InvalidAlgorithmParameterException("Parameters not supported");
        }
        implInit(i2, key);
    }

    private void implInit(int i2, Key key) throws InvalidKeyException {
        boolean z2;
        reset(true);
        this.p11Key = P11KeyFactory.convertKey(this.token, key, this.algorithm);
        if (i2 == 1) {
            z2 = true;
        } else if (i2 == 2) {
            z2 = false;
        } else if (i2 == 3) {
            if (!this.p11Key.isPublic()) {
                throw new InvalidKeyException("Wrap has to be used with public keys");
            }
            return;
        } else {
            if (i2 == 4) {
                if (!this.p11Key.isPrivate()) {
                    throw new InvalidKeyException("Unwrap has to be used with private keys");
                }
                return;
            }
            throw new InvalidKeyException("Unsupported mode: " + i2);
        }
        if (this.p11Key.isPublic()) {
            this.mode = z2 ? 1 : 4;
        } else if (this.p11Key.isPrivate()) {
            this.mode = z2 ? 3 : 2;
        } else {
            throw new InvalidKeyException("Unknown key type: " + ((Object) this.p11Key));
        }
        int length = (this.p11Key.length() + 7) >> 3;
        this.outputSize = length;
        this.buffer = new byte[length];
        this.maxInputSize = (this.padType == 2 && z2) ? length - 11 : length;
        try {
            initialize();
        } catch (PKCS11Exception e2) {
            throw new InvalidKeyException("init() failed", e2);
        }
    }

    private void reset(boolean z2) {
        if (!this.initialized) {
            return;
        }
        this.initialized = false;
        try {
            if (this.session == null) {
                return;
            }
            if (z2 && this.token.explicitCancel) {
                cancelOperation();
            }
        } finally {
            this.p11Key.releaseKeyID();
            this.session = this.token.releaseSession(this.session);
        }
    }

    private void cancelOperation() {
        this.token.ensureValid();
        try {
            PKCS11 pkcs11 = this.token.p11;
            int i2 = this.maxInputSize;
            int length = this.buffer.length;
            long jId = this.session.id();
            switch (this.mode) {
                case 1:
                    pkcs11.C_Encrypt(jId, 0L, this.buffer, 0, i2, 0L, this.buffer, 0, length);
                    break;
                case 2:
                    pkcs11.C_Decrypt(jId, 0L, this.buffer, 0, i2, 0L, this.buffer, 0, length);
                    break;
                case 3:
                    pkcs11.C_Sign(jId, new byte[this.maxInputSize]);
                    break;
                case 4:
                    pkcs11.C_VerifyRecover(jId, this.buffer, 0, i2, this.buffer, 0, length);
                    break;
                default:
                    throw new ProviderException("internal error");
            }
        } catch (PKCS11Exception e2) {
        }
    }

    private void ensureInitialized() throws PKCS11Exception {
        this.token.ensureValid();
        if (!this.initialized) {
            initialize();
        }
    }

    private void initialize() throws PKCS11Exception {
        if (this.p11Key == null) {
            throw new ProviderException("Operation cannot be performed without calling engineInit first");
        }
        long keyID = this.p11Key.getKeyID();
        try {
            if (this.session == null) {
                this.session = this.token.getOpSession();
            }
            PKCS11 pkcs11 = this.token.p11;
            CK_MECHANISM ck_mechanism = new CK_MECHANISM(this.mechanism);
            switch (this.mode) {
                case 1:
                    pkcs11.C_EncryptInit(this.session.id(), ck_mechanism, keyID);
                    break;
                case 2:
                    pkcs11.C_DecryptInit(this.session.id(), ck_mechanism, keyID);
                    break;
                case 3:
                    pkcs11.C_SignInit(this.session.id(), ck_mechanism, keyID);
                    break;
                case 4:
                    pkcs11.C_VerifyRecoverInit(this.session.id(), ck_mechanism, keyID);
                    break;
                default:
                    throw new AssertionError((Object) "internal error");
            }
            this.bufOfs = 0;
            this.initialized = true;
        } catch (PKCS11Exception e2) {
            this.p11Key.releaseKeyID();
            this.session = this.token.releaseSession(this.session);
            throw e2;
        }
    }

    private void implUpdate(byte[] bArr, int i2, int i3) {
        try {
            ensureInitialized();
            if (i3 == 0 || bArr == null) {
                return;
            }
            if (this.bufOfs + i3 > this.maxInputSize) {
                this.bufOfs = this.maxInputSize + 1;
            } else {
                System.arraycopy(bArr, i2, this.buffer, this.bufOfs, i3);
                this.bufOfs += i3;
            }
        } catch (PKCS11Exception e2) {
            throw new ProviderException("update() failed", e2);
        }
    }

    private int implDoFinal(byte[] bArr, int i2, int i3) throws BadPaddingException, IllegalBlockSizeException {
        int iC_VerifyRecover;
        try {
            if (this.bufOfs > this.maxInputSize) {
                reset(true);
                throw new IllegalBlockSizeException("Data must not be longer than " + this.maxInputSize + " bytes");
            }
            try {
                ensureInitialized();
                PKCS11 pkcs11 = this.token.p11;
                switch (this.mode) {
                    case 1:
                        iC_VerifyRecover = pkcs11.C_Encrypt(this.session.id(), 0L, this.buffer, 0, this.bufOfs, 0L, bArr, i2, i3);
                        break;
                    case 2:
                        iC_VerifyRecover = pkcs11.C_Decrypt(this.session.id(), 0L, this.buffer, 0, this.bufOfs, 0L, bArr, i2, i3);
                        break;
                    case 3:
                        byte[] bArr2 = new byte[this.bufOfs];
                        System.arraycopy(this.buffer, 0, bArr2, 0, this.bufOfs);
                        byte[] bArrC_Sign = pkcs11.C_Sign(this.session.id(), bArr2);
                        if (bArrC_Sign.length > i3) {
                            throw new BadPaddingException("Output buffer (" + i3 + ") is too small to hold the produced data (" + bArrC_Sign.length + ")");
                        }
                        System.arraycopy(bArrC_Sign, 0, bArr, i2, bArrC_Sign.length);
                        iC_VerifyRecover = bArrC_Sign.length;
                        break;
                    case 4:
                        iC_VerifyRecover = pkcs11.C_VerifyRecover(this.session.id(), this.buffer, 0, this.bufOfs, bArr, i2, i3);
                        break;
                    default:
                        throw new ProviderException("internal error");
                }
                return iC_VerifyRecover;
            } catch (PKCS11Exception e2) {
                throw ((BadPaddingException) new BadPaddingException("doFinal() failed").initCause(e2));
            }
        } finally {
            reset(false);
        }
    }

    @Override // javax.crypto.CipherSpi
    protected byte[] engineUpdate(byte[] bArr, int i2, int i3) {
        implUpdate(bArr, i2, i3);
        return B0;
    }

    @Override // javax.crypto.CipherSpi
    protected int engineUpdate(byte[] bArr, int i2, int i3, byte[] bArr2, int i4) throws ShortBufferException {
        implUpdate(bArr, i2, i3);
        return 0;
    }

    @Override // javax.crypto.CipherSpi
    protected byte[] engineDoFinal(byte[] bArr, int i2, int i3) throws BadPaddingException, IllegalBlockSizeException {
        implUpdate(bArr, i2, i3);
        int iImplDoFinal = implDoFinal(this.buffer, 0, this.buffer.length);
        byte[] bArr2 = new byte[iImplDoFinal];
        System.arraycopy(this.buffer, 0, bArr2, 0, iImplDoFinal);
        return bArr2;
    }

    @Override // javax.crypto.CipherSpi
    protected int engineDoFinal(byte[] bArr, int i2, int i3, byte[] bArr2, int i4) throws BadPaddingException, IllegalBlockSizeException, ShortBufferException {
        implUpdate(bArr, i2, i3);
        return implDoFinal(bArr2, i4, bArr2.length - i4);
    }

    private byte[] doFinal() throws BadPaddingException, IllegalBlockSizeException {
        byte[] bArr = new byte[2048];
        int iImplDoFinal = implDoFinal(bArr, 0, bArr.length);
        byte[] bArr2 = new byte[iImplDoFinal];
        System.arraycopy(bArr, 0, bArr2, 0, iImplDoFinal);
        return bArr2;
    }

    @Override // javax.crypto.CipherSpi
    protected byte[] engineWrap(Key key) throws IllegalBlockSizeException, InvalidKeyException {
        try {
            P11Key p11KeyConvertKey = P11SecretKeyFactory.convertKey(this.token, key, key.getAlgorithm());
            Session opSession = null;
            long keyID = this.p11Key.getKeyID();
            long keyID2 = p11KeyConvertKey.getKeyID();
            try {
                try {
                    opSession = this.token.getOpSession();
                    byte[] bArrC_WrapKey = this.token.p11.C_WrapKey(opSession.id(), new CK_MECHANISM(this.mechanism), keyID, keyID2);
                    this.p11Key.releaseKeyID();
                    p11KeyConvertKey.releaseKeyID();
                    this.token.releaseSession(opSession);
                    return bArrC_WrapKey;
                } catch (PKCS11Exception e2) {
                    throw new InvalidKeyException("wrap() failed", e2);
                }
            } catch (Throwable th) {
                this.p11Key.releaseKeyID();
                p11KeyConvertKey.releaseKeyID();
                this.token.releaseSession(opSession);
                throw th;
            }
        } catch (InvalidKeyException e3) {
            byte[] encoded = key.getEncoded();
            if (encoded == null) {
                throw new InvalidKeyException("wrap() failed, no encoding available", e3);
            }
            implInit(1, this.p11Key);
            implUpdate(encoded, 0, encoded.length);
            try {
                try {
                    byte[] bArrDoFinal = doFinal();
                    implInit(3, this.p11Key);
                    return bArrDoFinal;
                } catch (BadPaddingException e4) {
                    throw new InvalidKeyException("wrap() failed", e4);
                }
            } catch (Throwable th2) {
                implInit(3, this.p11Key);
                throw th2;
            }
        }
    }

    @Override // javax.crypto.CipherSpi
    protected Key engineUnwrap(byte[] bArr, String str, int i2) throws NoSuchAlgorithmException, InvalidKeyException {
        boolean zEquals = str.equals("TlsRsaPremasterSecret");
        Exception exc = null;
        if (this.token.supportsRawSecretKeyImport()) {
            implInit(2, this.p11Key);
            try {
                if (bArr.length > this.maxInputSize) {
                    throw new InvalidKeyException("Key is too long for unwrapping");
                }
                byte[] bArrCheckTlsPreMasterSecretKey = null;
                implUpdate(bArr, 0, bArr.length);
                try {
                    try {
                        bArrCheckTlsPreMasterSecretKey = doFinal();
                    } catch (BadPaddingException e2) {
                        if (zEquals) {
                            exc = e2;
                        } else {
                            throw new InvalidKeyException("Unwrapping failed", e2);
                        }
                    }
                    if (zEquals) {
                        if (!(this.spec instanceof TlsRsaPremasterSecretParameterSpec)) {
                            throw new IllegalStateException("No TlsRsaPremasterSecretParameterSpec specified");
                        }
                        TlsRsaPremasterSecretParameterSpec tlsRsaPremasterSecretParameterSpec = (TlsRsaPremasterSecretParameterSpec) this.spec;
                        bArrCheckTlsPreMasterSecretKey = KeyUtil.checkTlsPreMasterSecretKey(tlsRsaPremasterSecretParameterSpec.getClientVersion(), tlsRsaPremasterSecretParameterSpec.getServerVersion(), this.random, bArrCheckTlsPreMasterSecretKey, exc != null);
                    }
                    Key keyConstructKey = ConstructKeys.constructKey(bArrCheckTlsPreMasterSecretKey, str, i2);
                    implInit(4, this.p11Key);
                    return keyConstructKey;
                } catch (IllegalBlockSizeException e3) {
                    throw new InvalidKeyException("Unwrapping failed", e3);
                }
            } catch (Throwable th) {
                implInit(4, this.p11Key);
                throw th;
            }
        }
        Session objSession = null;
        SecretKey secretKeyPolishPreMasterSecretKey = null;
        long keyID = this.p11Key.getKeyID();
        try {
            try {
                objSession = this.token.getObjSession();
                CK_ATTRIBUTE[] attributes = this.token.getAttributes("import", 4L, 16L, new CK_ATTRIBUTE[]{new CK_ATTRIBUTE(0L, 4L), new CK_ATTRIBUTE(256L, 16L)});
                secretKeyPolishPreMasterSecretKey = P11Key.secretKey(objSession, this.token.p11.C_UnwrapKey(objSession.id(), new CK_MECHANISM(this.mechanism), keyID, bArr, attributes), str, 384, attributes);
            } catch (PKCS11Exception e4) {
                if (zEquals) {
                    exc = e4;
                } else {
                    throw new InvalidKeyException("unwrap() failed", e4);
                }
            }
            if (zEquals) {
                TlsRsaPremasterSecretParameterSpec tlsRsaPremasterSecretParameterSpec2 = (TlsRsaPremasterSecretParameterSpec) this.spec;
                secretKeyPolishPreMasterSecretKey = polishPreMasterSecretKey(this.token, objSession, exc, secretKeyPolishPreMasterSecretKey, tlsRsaPremasterSecretParameterSpec2.getClientVersion(), tlsRsaPremasterSecretParameterSpec2.getServerVersion());
            }
            return secretKeyPolishPreMasterSecretKey;
        } finally {
            this.p11Key.releaseKeyID();
            this.token.releaseSession(objSession);
        }
    }

    @Override // javax.crypto.CipherSpi
    protected int engineGetKeySize(Key key) throws InvalidKeyException {
        return P11KeyFactory.convertKey(this.token, key, this.algorithm).length();
    }

    private static SecretKey polishPreMasterSecretKey(Token token, Session session, Exception exc, SecretKey secretKey, int i2, int i3) {
        CK_VERSION ck_version = new CK_VERSION((i2 >>> 8) & 255, i2 & 255);
        try {
            CK_ATTRIBUTE[] attributes = token.getAttributes("generate", 4L, 16L, new CK_ATTRIBUTE[0]);
            return exc == null ? secretKey : P11Key.secretKey(session, token.p11.C_GenerateKey(session.id(), new CK_MECHANISM(880L, ck_version), attributes), "TlsRsaPremasterSecret", 384, attributes);
        } catch (PKCS11Exception e2) {
            throw new ProviderException("Could not generate premaster secret", e2);
        }
    }
}
