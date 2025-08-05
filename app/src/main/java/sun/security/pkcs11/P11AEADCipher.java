package sun.security.pkcs11;

import java.io.ByteArrayOutputStream;
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
import java.security.spec.InvalidParameterSpecException;
import java.util.Arrays;
import java.util.Locale;
import javax.crypto.BadPaddingException;
import javax.crypto.CipherSpi;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.ShortBufferException;
import javax.crypto.spec.GCMParameterSpec;
import sun.nio.ch.DirectBuffer;
import sun.security.jca.JCAUtil;
import sun.security.pkcs11.wrapper.CK_GCM_PARAMS;
import sun.security.pkcs11.wrapper.CK_MECHANISM;
import sun.security.pkcs11.wrapper.PKCS11Exception;

/* loaded from: sunpkcs11.jar:sun/security/pkcs11/P11AEADCipher.class */
final class P11AEADCipher extends CipherSpi {
    private static final int MODE_GCM = 10;
    private static final int GCM_DEFAULT_TAG_LEN = 16;
    private static final int GCM_DEFAULT_IV_LEN = 16;
    private static final String ALGO = "AES";
    private final Token token;
    private final long mechanism;
    private final int blockMode;
    private final int fixedKeySize;
    private Session session = null;
    private P11Key p11Key = null;
    private boolean initialized = false;
    private boolean encrypt = true;
    private byte[] iv = null;
    private int tagLen = -1;
    private SecureRandom random = JCAUtil.getSecureRandom();
    private ByteArrayOutputStream dataBuffer = new ByteArrayOutputStream();
    private ByteArrayOutputStream aadBuffer = new ByteArrayOutputStream();
    private boolean updateCalled = false;
    private boolean requireReinit = false;
    private P11Key lastEncKey = null;
    private byte[] lastEncIv = null;

    P11AEADCipher(Token token, String str, long j2) throws PKCS11Exception, NoSuchAlgorithmException {
        this.token = token;
        this.mechanism = j2;
        String[] strArrSplit = str.split("/");
        if (strArrSplit.length != 3) {
            throw new ProviderException("Unsupported Transformation format: " + str);
        }
        if (!strArrSplit[0].startsWith(ALGO)) {
            throw new ProviderException("Only support AES for AEAD cipher mode");
        }
        int iIndexOf = strArrSplit[0].indexOf(95);
        if (iIndexOf != -1) {
            this.fixedKeySize = Integer.parseInt(strArrSplit[0].substring(iIndexOf + 1)) >> 3;
        } else {
            this.fixedKeySize = -1;
        }
        this.blockMode = parseMode(strArrSplit[1]);
        if (!strArrSplit[2].equals("NoPadding")) {
            throw new ProviderException("Only NoPadding is supported for AEAD cipher mode");
        }
    }

    @Override // javax.crypto.CipherSpi
    protected void engineSetMode(String str) throws NoSuchAlgorithmException {
        throw new NoSuchAlgorithmException("Unsupported mode " + str);
    }

    private int parseMode(String str) throws NoSuchAlgorithmException {
        String upperCase = str.toUpperCase(Locale.ENGLISH);
        if (upperCase.equals("GCM")) {
            return 10;
        }
        throw new NoSuchAlgorithmException("Unsupported mode " + upperCase);
    }

    @Override // javax.crypto.CipherSpi
    protected void engineSetPadding(String str) throws NoSuchPaddingException {
        throw new NoSuchPaddingException("Unsupported padding " + str);
    }

    @Override // javax.crypto.CipherSpi
    protected int engineGetBlockSize() {
        return 16;
    }

    @Override // javax.crypto.CipherSpi
    protected int engineGetOutputSize(int i2) {
        return doFinalLength(i2);
    }

    @Override // javax.crypto.CipherSpi
    protected byte[] engineGetIV() {
        if (this.iv == null) {
            return null;
        }
        return (byte[]) this.iv.clone();
    }

    @Override // javax.crypto.CipherSpi
    protected AlgorithmParameters engineGetParameters() {
        if (this.encrypt && this.iv == null && this.tagLen == -1) {
            switch (this.blockMode) {
                case 10:
                    this.iv = new byte[16];
                    this.tagLen = 16;
                    this.random.nextBytes(this.iv);
                    break;
                default:
                    throw new ProviderException("Unsupported mode");
            }
        }
        try {
            switch (this.blockMode) {
                case 10:
                    GCMParameterSpec gCMParameterSpec = new GCMParameterSpec(this.tagLen << 3, this.iv);
                    AlgorithmParameters algorithmParameters = AlgorithmParameters.getInstance("GCM");
                    algorithmParameters.init(gCMParameterSpec);
                    return algorithmParameters;
                default:
                    throw new ProviderException("Unsupported mode");
            }
        } catch (GeneralSecurityException e2) {
            throw new ProviderException("Could not encode parameters", e2);
        }
    }

    @Override // javax.crypto.CipherSpi
    protected void engineInit(int i2, Key key, SecureRandom secureRandom) throws InvalidKeyException {
        if (i2 == 2) {
            throw new InvalidKeyException("Parameters required for decryption");
        }
        this.updateCalled = false;
        try {
            implInit(i2, key, null, -1, secureRandom);
        } catch (InvalidAlgorithmParameterException e2) {
            throw new InvalidKeyException("init() failed", e2);
        }
    }

    @Override // javax.crypto.CipherSpi
    protected void engineInit(int i2, Key key, AlgorithmParameterSpec algorithmParameterSpec, SecureRandom secureRandom) throws InvalidKeyException, InvalidAlgorithmParameterException {
        if (i2 == 2 && algorithmParameterSpec == null) {
            throw new InvalidAlgorithmParameterException("Parameters required for decryption");
        }
        this.updateCalled = false;
        byte[] iv = null;
        int tLen = -1;
        if (algorithmParameterSpec != null) {
            switch (this.blockMode) {
                case 10:
                    if (!(algorithmParameterSpec instanceof GCMParameterSpec)) {
                        throw new InvalidAlgorithmParameterException("Only GCMParameterSpec is supported");
                    }
                    iv = ((GCMParameterSpec) algorithmParameterSpec).getIV();
                    tLen = ((GCMParameterSpec) algorithmParameterSpec).getTLen() >> 3;
                    break;
                default:
                    throw new ProviderException("Unsupported mode");
            }
        }
        implInit(i2, key, iv, tLen, secureRandom);
    }

    @Override // javax.crypto.CipherSpi
    protected void engineInit(int i2, Key key, AlgorithmParameters algorithmParameters, SecureRandom secureRandom) throws InvalidKeyException, InvalidAlgorithmParameterException {
        if (i2 == 2 && algorithmParameters == null) {
            throw new InvalidAlgorithmParameterException("Parameters required for decryption");
        }
        this.updateCalled = false;
        AlgorithmParameterSpec parameterSpec = null;
        if (algorithmParameters != null) {
            try {
                switch (this.blockMode) {
                    case 10:
                        parameterSpec = algorithmParameters.getParameterSpec(GCMParameterSpec.class);
                        break;
                    default:
                        throw new ProviderException("Unsupported mode");
                }
            } catch (InvalidParameterSpecException e2) {
                throw new InvalidAlgorithmParameterException(e2);
            }
        }
        engineInit(i2, key, parameterSpec, secureRandom);
    }

    private void implInit(int i2, Key key, byte[] bArr, int i3, SecureRandom secureRandom) throws InvalidKeyException, InvalidAlgorithmParameterException {
        int length;
        reset(true);
        if (this.fixedKeySize != -1) {
            if (key instanceof P11Key) {
                length = ((P11Key) key).length() >> 3;
            } else {
                length = key.getEncoded().length;
            }
            if (length != this.fixedKeySize) {
                throw new InvalidKeyException("Key size is invalid");
            }
        }
        P11Key p11KeyConvertKey = P11SecretKeyFactory.convertKey(this.token, key, ALGO);
        switch (i2) {
            case 1:
                this.encrypt = true;
                this.requireReinit = Arrays.equals(bArr, this.lastEncIv) && p11KeyConvertKey == this.lastEncKey;
                if (this.requireReinit) {
                    throw new InvalidAlgorithmParameterException("Cannot reuse iv for GCM encryption");
                }
                break;
            case 2:
                this.encrypt = false;
                this.requireReinit = false;
                break;
            default:
                throw new InvalidAlgorithmParameterException("Unsupported mode: " + i2);
        }
        if (secureRandom != null) {
            this.random = secureRandom;
        }
        if (bArr == null && i3 == -1) {
            switch (this.blockMode) {
                case 10:
                    bArr = new byte[16];
                    this.random.nextBytes(bArr);
                    i3 = 16;
                    break;
                default:
                    throw new ProviderException("Unsupported mode");
            }
        }
        this.iv = bArr;
        this.tagLen = i3;
        this.p11Key = p11KeyConvertKey;
        try {
            initialize();
        } catch (PKCS11Exception e2) {
            throw new InvalidKeyException("Could not initialize cipher", e2);
        }
    }

    private void cancelOperation() {
        int iDoFinalLength = doFinalLength(0);
        byte[] bArr = new byte[iDoFinalLength];
        byte[] byteArray = this.dataBuffer.toByteArray();
        int length = byteArray.length;
        try {
            if (this.encrypt) {
                this.token.p11.C_Encrypt(this.session.id(), 0L, byteArray, 0, length, 0L, bArr, 0, iDoFinalLength);
            } else {
                this.token.p11.C_Decrypt(this.session.id(), 0L, byteArray, 0, length, 0L, bArr, 0, iDoFinalLength);
            }
        } catch (PKCS11Exception e2) {
            if (e2.getErrorCode() != 145 && this.encrypt) {
                throw new ProviderException("Cancel failed", e2);
            }
        }
    }

    private void ensureInitialized() throws PKCS11Exception {
        if (this.initialized && this.aadBuffer.size() > 0) {
            reset(true);
        }
        if (!this.initialized) {
            initialize();
        }
    }

    private void initialize() throws PKCS11Exception {
        if (this.p11Key == null) {
            throw new ProviderException("Operation cannot be performed without calling engineInit first");
        }
        if (this.requireReinit) {
            throw new IllegalStateException("Must use either different key or iv for GCM encryption");
        }
        this.token.ensureValid();
        byte[] byteArray = this.aadBuffer.size() > 0 ? this.aadBuffer.toByteArray() : null;
        long keyID = this.p11Key.getKeyID();
        try {
            try {
                switch (this.blockMode) {
                    case 10:
                        CK_MECHANISM ck_mechanism = new CK_MECHANISM(this.mechanism, new CK_GCM_PARAMS(this.tagLen << 3, this.iv, byteArray));
                        if (this.session == null) {
                            this.session = this.token.getOpSession();
                        }
                        if (this.encrypt) {
                            this.token.p11.C_EncryptInit(this.session.id(), ck_mechanism, keyID);
                        } else {
                            this.token.p11.C_DecryptInit(this.session.id(), ck_mechanism, keyID);
                        }
                        this.initialized = true;
                        return;
                    default:
                        throw new ProviderException("Unsupported mode: " + this.blockMode);
                }
            } catch (PKCS11Exception e2) {
                this.p11Key.releaseKeyID();
                this.session = this.token.releaseSession(this.session);
                throw e2;
            }
        } finally {
            this.dataBuffer.reset();
            this.aadBuffer.reset();
        }
    }

    private int doFinalLength(int i2) {
        if (i2 < 0) {
            throw new ProviderException("Invalid negative input length");
        }
        int size = i2 + this.dataBuffer.size();
        if (this.encrypt) {
            size += this.tagLen;
        }
        return size;
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
            this.dataBuffer.reset();
        }
    }

    @Override // javax.crypto.CipherSpi
    protected byte[] engineUpdate(byte[] bArr, int i2, int i3) {
        this.updateCalled = true;
        implUpdate(bArr, i2, i3);
        return new byte[0];
    }

    @Override // javax.crypto.CipherSpi
    protected int engineUpdate(byte[] bArr, int i2, int i3, byte[] bArr2, int i4) throws ShortBufferException {
        this.updateCalled = true;
        implUpdate(bArr, i2, i3);
        return 0;
    }

    @Override // javax.crypto.CipherSpi
    protected int engineUpdate(ByteBuffer byteBuffer, ByteBuffer byteBuffer2) throws ShortBufferException {
        this.updateCalled = true;
        implUpdate(byteBuffer);
        return 0;
    }

    @Override // javax.crypto.CipherSpi
    protected synchronized void engineUpdateAAD(byte[] bArr, int i2, int i3) throws IllegalStateException {
        if (bArr == null || i2 < 0 || i2 + i3 > bArr.length) {
            throw new IllegalArgumentException("Invalid AAD");
        }
        if (this.requireReinit) {
            throw new IllegalStateException("Must use either different key or iv for GCM encryption");
        }
        if (this.p11Key == null) {
            throw new IllegalStateException("Need to initialize Cipher first");
        }
        if (this.updateCalled) {
            throw new IllegalStateException("Update has been called; no more AAD data");
        }
        this.aadBuffer.write(bArr, i2, i3);
    }

    @Override // javax.crypto.CipherSpi
    protected void engineUpdateAAD(ByteBuffer byteBuffer) throws IllegalStateException {
        if (byteBuffer == null) {
            throw new IllegalArgumentException("Invalid AAD");
        }
        byte[] bArr = new byte[byteBuffer.remaining()];
        byteBuffer.get(bArr);
        engineUpdateAAD(bArr, 0, bArr.length);
    }

    @Override // javax.crypto.CipherSpi
    protected byte[] engineDoFinal(byte[] bArr, int i2, int i3) throws BadPaddingException, IllegalBlockSizeException {
        try {
            try {
                byte[] bArr2 = new byte[doFinalLength(i3)];
                byte[] bArrConvert = P11Util.convert(bArr2, 0, engineDoFinal(bArr, i2, i3, bArr2, 0));
                this.updateCalled = false;
                return bArrConvert;
            } catch (ShortBufferException e2) {
                throw new ProviderException(e2);
            }
        } catch (Throwable th) {
            this.updateCalled = false;
            throw th;
        }
    }

    @Override // javax.crypto.CipherSpi
    protected int engineDoFinal(byte[] bArr, int i2, int i3, byte[] bArr2, int i4) throws BadPaddingException, IllegalBlockSizeException, ShortBufferException {
        try {
            int iImplDoFinal = implDoFinal(bArr, i2, i3, bArr2, i4, bArr2.length - i4);
            this.updateCalled = false;
            return iImplDoFinal;
        } catch (Throwable th) {
            this.updateCalled = false;
            throw th;
        }
    }

    @Override // javax.crypto.CipherSpi
    protected int engineDoFinal(ByteBuffer byteBuffer, ByteBuffer byteBuffer2) throws BadPaddingException, IllegalBlockSizeException, ShortBufferException {
        try {
            int iImplDoFinal = implDoFinal(byteBuffer, byteBuffer2);
            this.updateCalled = false;
            return iImplDoFinal;
        } catch (Throwable th) {
            this.updateCalled = false;
            throw th;
        }
    }

    private int implUpdate(byte[] bArr, int i2, int i3) {
        if (i3 > 0) {
            this.updateCalled = true;
            try {
                ensureInitialized();
                this.dataBuffer.write(bArr, i2, i3);
                return 0;
            } catch (PKCS11Exception e2) {
                reset(false);
                throw new ProviderException("update() failed", e2);
            }
        }
        return 0;
    }

    private int implUpdate(ByteBuffer byteBuffer) {
        int iRemaining = byteBuffer.remaining();
        if (iRemaining > 0) {
            try {
                ensureInitialized();
                byte[] bArr = new byte[iRemaining];
                byteBuffer.get(bArr);
                this.dataBuffer.write(bArr, 0, bArr.length);
                return 0;
            } catch (PKCS11Exception e2) {
                reset(false);
                throw new ProviderException("update() failed", e2);
            }
        }
        return 0;
    }

    private int implDoFinal(byte[] bArr, int i2, int i3, byte[] bArr2, int i4, int i5) throws BadPaddingException, IllegalBlockSizeException, ShortBufferException {
        int iC_Decrypt;
        boolean z2;
        if (i5 < doFinalLength(i3)) {
            throw new ShortBufferException();
        }
        try {
            try {
                ensureInitialized();
                if (this.dataBuffer.size() > 0) {
                    if (bArr != null && i2 > 0 && i3 > 0 && i2 < bArr.length - i3) {
                        this.dataBuffer.write(bArr, i2, i3);
                    }
                    bArr = this.dataBuffer.toByteArray();
                    i2 = 0;
                    i3 = bArr.length;
                }
                if (this.encrypt) {
                    iC_Decrypt = this.token.p11.C_Encrypt(this.session.id(), 0L, bArr, i2, i3, 0L, bArr2, i4, i5);
                    z2 = false;
                } else {
                    if (i3 == 0) {
                        return 0;
                    }
                    iC_Decrypt = this.token.p11.C_Decrypt(this.session.id(), 0L, bArr, i2, i3, 0L, bArr2, i4, i5);
                    z2 = false;
                }
                int i6 = iC_Decrypt;
                if (this.encrypt) {
                    this.lastEncKey = this.p11Key;
                    this.lastEncIv = this.iv;
                    this.requireReinit = true;
                }
                reset(z2);
                return i6;
            } catch (PKCS11Exception e2) {
                handleException(e2);
                throw new ProviderException("doFinal() failed", e2);
            }
        } finally {
            if (this.encrypt) {
                this.lastEncKey = this.p11Key;
                this.lastEncIv = this.iv;
                this.requireReinit = true;
            }
            reset(true);
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    private int implDoFinal(ByteBuffer byteBuffer, ByteBuffer byteBuffer2) throws BadPaddingException, IllegalBlockSizeException, ShortBufferException {
        int iC_Decrypt;
        int iRemaining = byteBuffer2.remaining();
        int iRemaining2 = byteBuffer.remaining();
        if (iRemaining < doFinalLength(iRemaining2)) {
            throw new ShortBufferException();
        }
        boolean z2 = true;
        try {
            try {
                ensureInitialized();
                long jAddress = 0;
                byte[] bArrArray = null;
                int iPosition = 0;
                if (this.dataBuffer.size() > 0) {
                    if (iRemaining2 > 0) {
                        byte[] bArr = new byte[iRemaining2];
                        byteBuffer.get(bArr);
                        this.dataBuffer.write(bArr, 0, bArr.length);
                    }
                    bArrArray = this.dataBuffer.toByteArray();
                    iPosition = 0;
                    iRemaining2 = bArrArray.length;
                } else if (byteBuffer instanceof DirectBuffer) {
                    jAddress = ((DirectBuffer) byteBuffer).address();
                    iPosition = byteBuffer.position();
                } else if (byteBuffer.hasArray()) {
                    bArrArray = byteBuffer.array();
                    iPosition = byteBuffer.position() + byteBuffer.arrayOffset();
                } else {
                    bArrArray = new byte[iRemaining2];
                    byteBuffer.get(bArrArray);
                }
                long jAddress2 = 0;
                byte[] bArrArray2 = null;
                int iPosition2 = 0;
                if (byteBuffer2 instanceof DirectBuffer) {
                    jAddress2 = ((DirectBuffer) byteBuffer2).address();
                    iPosition2 = byteBuffer2.position();
                } else if (byteBuffer2.hasArray()) {
                    bArrArray2 = byteBuffer2.array();
                    iPosition2 = byteBuffer2.position() + byteBuffer2.arrayOffset();
                } else {
                    bArrArray2 = new byte[iRemaining];
                }
                if (this.encrypt) {
                    iC_Decrypt = this.token.p11.C_Encrypt(this.session.id(), jAddress, bArrArray, iPosition, iRemaining2, jAddress2, bArrArray2, iPosition2, iRemaining);
                    z2 = false;
                } else if (iRemaining2 != 0) {
                    iC_Decrypt = this.token.p11.C_Decrypt(this.session.id(), jAddress, bArrArray, iPosition, iRemaining2, jAddress2, bArrArray2, iPosition2, iRemaining);
                    z2 = false;
                } else {
                    if (this.encrypt) {
                        this.lastEncKey = this.p11Key;
                        this.lastEncIv = this.iv;
                        this.requireReinit = true;
                    }
                    reset(true);
                    return 0;
                }
                byteBuffer2.position(byteBuffer2.position() + iC_Decrypt);
                int i2 = iC_Decrypt;
                if (this.encrypt) {
                    this.lastEncKey = this.p11Key;
                    this.lastEncIv = this.iv;
                    this.requireReinit = true;
                }
                reset(z2);
                return i2;
            } catch (PKCS11Exception e2) {
                handleException(e2);
                throw new ProviderException("doFinal() failed", e2);
            }
        } catch (Throwable th) {
            if (this.encrypt) {
                this.lastEncKey = this.p11Key;
                this.lastEncIv = this.iv;
                this.requireReinit = true;
            }
            reset(z2);
            throw th;
        }
    }

    private void handleException(PKCS11Exception pKCS11Exception) throws BadPaddingException, IllegalBlockSizeException, ShortBufferException {
        long errorCode = pKCS11Exception.getErrorCode();
        if (errorCode == 336) {
            throw ((ShortBufferException) new ShortBufferException().initCause(pKCS11Exception));
        }
        if (errorCode == 33 || errorCode == 65) {
            throw ((IllegalBlockSizeException) new IllegalBlockSizeException(pKCS11Exception.toString()).initCause(pKCS11Exception));
        }
        if (errorCode == 64 || errorCode == 5) {
            throw ((BadPaddingException) new BadPaddingException(pKCS11Exception.toString()).initCause(pKCS11Exception));
        }
    }

    @Override // javax.crypto.CipherSpi
    protected byte[] engineWrap(Key key) throws IllegalBlockSizeException, InvalidKeyException {
        throw new UnsupportedOperationException("engineWrap()");
    }

    @Override // javax.crypto.CipherSpi
    protected Key engineUnwrap(byte[] bArr, String str, int i2) throws NoSuchAlgorithmException, InvalidKeyException {
        throw new UnsupportedOperationException("engineUnwrap()");
    }

    @Override // javax.crypto.CipherSpi
    protected int engineGetKeySize(Key key) throws InvalidKeyException {
        return P11SecretKeyFactory.convertKey(this.token, key, ALGO).length();
    }
}
