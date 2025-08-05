package sun.security.pkcs11;

import java.nio.ByteBuffer;
import java.security.AlgorithmParameters;
import java.security.GeneralSecurityException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.InvalidParameterException;
import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.ProviderException;
import java.security.PublicKey;
import java.security.SignatureException;
import java.security.SignatureSpi;
import java.security.interfaces.RSAKey;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.MGF1ParameterSpec;
import java.security.spec.PSSParameterSpec;
import java.util.Hashtable;
import sun.nio.ch.DirectBuffer;
import sun.security.pkcs11.wrapper.CK_MECHANISM;
import sun.security.pkcs11.wrapper.CK_MECHANISM_INFO;
import sun.security.pkcs11.wrapper.CK_RSA_PKCS_PSS_PARAMS;
import sun.security.pkcs11.wrapper.PKCS11Exception;
import sun.util.locale.LanguageTag;

/* loaded from: sunpkcs11.jar:sun/security/pkcs11/P11PSSSignature.class */
final class P11PSSSignature extends SignatureSpi {
    private static final boolean DEBUG = false;
    private static final Hashtable<String, Integer> DIGEST_LENGTHS = new Hashtable<>();
    private final Token token;
    private final String algorithm;
    private static final String KEY_ALGO = "RSA";
    private final CK_MECHANISM mechanism;
    private final int type;
    private final String mdAlg;
    private MessageDigest md;
    private Session session;
    private int mode;
    private static final int M_SIGN = 1;
    private static final int M_VERIFY = 2;
    private static final int T_DIGEST = 1;
    private static final int T_UPDATE = 2;
    private P11Key p11Key = null;
    private PSSParameterSpec sigParams = null;
    private boolean isActive = false;
    private boolean initialized = false;
    private final byte[] buffer = new byte[1];
    private int bytesProcessed = 0;

    static {
        DIGEST_LENGTHS.put("SHA-1", 20);
        DIGEST_LENGTHS.put("SHA", 20);
        DIGEST_LENGTHS.put("SHA1", 20);
        DIGEST_LENGTHS.put("SHA-224", 28);
        DIGEST_LENGTHS.put("SHA224", 28);
        DIGEST_LENGTHS.put("SHA-256", 32);
        DIGEST_LENGTHS.put("SHA256", 32);
        DIGEST_LENGTHS.put("SHA-384", 48);
        DIGEST_LENGTHS.put("SHA384", 48);
        DIGEST_LENGTHS.put("SHA-512", 64);
        DIGEST_LENGTHS.put("SHA512", 64);
        DIGEST_LENGTHS.put("SHA-512/224", 28);
        DIGEST_LENGTHS.put("SHA512/224", 28);
        DIGEST_LENGTHS.put("SHA-512/256", 32);
        DIGEST_LENGTHS.put("SHA512/256", 32);
    }

    private static boolean isDigestEqual(String str, String str2) {
        if (str == null || str2 == null) {
            return false;
        }
        if (str2.indexOf(LanguageTag.SEP) != -1) {
            return str.equalsIgnoreCase(str2);
        }
        if (str.equals("SHA-1")) {
            return str2.equalsIgnoreCase("SHA") || str2.equalsIgnoreCase("SHA1");
        }
        StringBuilder sb = new StringBuilder(str2);
        if (str2.regionMatches(true, 0, "SHA", 0, 3)) {
            return str.equalsIgnoreCase(sb.insert(3, LanguageTag.SEP).toString());
        }
        throw new ProviderException("Unsupported digest algorithm " + str2);
    }

    P11PSSSignature(Token token, String str, long j2) throws PKCS11Exception, NoSuchAlgorithmException {
        this.md = null;
        this.token = token;
        this.algorithm = str;
        this.mechanism = new CK_MECHANISM(j2);
        int iIndexOf = str.indexOf("with");
        this.mdAlg = iIndexOf == -1 ? null : str.substring(0, iIndexOf);
        switch ((int) j2) {
            case 13:
                this.type = 1;
                break;
            case 14:
            case 67:
            case 68:
            case 69:
            case 71:
                this.type = 2;
                break;
            default:
                throw new ProviderException("Unsupported mechanism: " + j2);
        }
        this.md = null;
    }

    private void ensureInitialized() throws SignatureException {
        this.token.ensureValid();
        if (this.p11Key == null) {
            throw new SignatureException("Missing key");
        }
        if (this.sigParams == null) {
            if (this.mdAlg == null) {
                throw new SignatureException("Parameters required for RSASSA-PSS signature");
            }
            this.sigParams = new PSSParameterSpec(this.mdAlg, "MGF1", new MGF1ParameterSpec(this.mdAlg), DIGEST_LENGTHS.get(this.mdAlg).intValue(), 1);
            this.mechanism.setParameter(new CK_RSA_PKCS_PSS_PARAMS(this.mdAlg, "MGF1", this.mdAlg, DIGEST_LENGTHS.get(this.mdAlg).intValue()));
        }
        if (!this.initialized) {
            initialize();
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
            this.mechanism.freeHandle();
            this.session = this.token.releaseSession(this.session);
            this.isActive = false;
        }
    }

    private void cancelOperation() {
        this.token.ensureValid();
        try {
            if (this.mode == 1) {
                if (this.type == 2) {
                    this.token.p11.C_SignFinal(this.session.id(), 0);
                } else {
                    this.token.p11.C_Sign(this.session.id(), this.md == null ? new byte[0] : this.md.digest());
                }
            } else {
                byte[] bArr = new byte[(this.p11Key.length() + 7) >> 3];
                if (this.type == 2) {
                    this.token.p11.C_VerifyFinal(this.session.id(), bArr);
                } else {
                    this.token.p11.C_Verify(this.session.id(), this.md == null ? new byte[0] : this.md.digest(), bArr);
                }
            }
        } catch (PKCS11Exception e2) {
            if (e2.getErrorCode() != 145 && this.mode == 1) {
                throw new ProviderException("cancel failed", e2);
            }
        }
    }

    private void initialize() {
        if (this.p11Key == null) {
            throw new ProviderException("No Key found, call initSign/initVerify first");
        }
        long keyID = this.p11Key.getKeyID();
        try {
            if (this.session == null) {
                this.session = this.token.getOpSession();
            }
            if (this.mode == 1) {
                this.token.p11.C_SignInit(this.session.id(), this.mechanism, keyID);
            } else {
                this.token.p11.C_VerifyInit(this.session.id(), this.mechanism, keyID);
            }
            if (this.bytesProcessed != 0) {
                this.bytesProcessed = 0;
                if (this.md != null) {
                    this.md.reset();
                }
            }
            this.initialized = true;
            this.isActive = false;
        } catch (PKCS11Exception e2) {
            this.p11Key.releaseKeyID();
            this.session = this.token.releaseSession(this.session);
            throw new ProviderException("Initialization failed", e2);
        }
    }

    private void checkKeySize(Key key) throws InvalidKeyException {
        if (!key.getAlgorithm().equals(KEY_ALGO)) {
            throw new InvalidKeyException("Only RSA keys are supported");
        }
        CK_MECHANISM_INFO mechanismInfo = null;
        try {
            mechanismInfo = this.token.getMechanismInfo(this.mechanism.mechanism);
        } catch (PKCS11Exception e2) {
        }
        int iBitLength = 0;
        if (mechanismInfo != null) {
            if (key instanceof P11Key) {
                iBitLength = (((P11Key) key).length() + 7) >> 3;
            } else if (key instanceof RSAKey) {
                iBitLength = ((RSAKey) key).getModulus().bitLength() >> 3;
            } else {
                throw new InvalidKeyException("Unrecognized key type " + ((Object) key));
            }
            if (mechanismInfo.iMinKeySize != 0 && iBitLength < (mechanismInfo.iMinKeySize >> 3)) {
                throw new InvalidKeyException("RSA key must be at least " + mechanismInfo.iMinKeySize + " bits");
            }
            if (mechanismInfo.iMaxKeySize != Integer.MAX_VALUE && iBitLength > (mechanismInfo.iMaxKeySize >> 3)) {
                throw new InvalidKeyException("RSA key must be at most " + mechanismInfo.iMaxKeySize + " bits");
            }
        }
        if (this.sigParams != null) {
            int iAddExact = Math.addExact(Math.addExact(this.sigParams.getSaltLength(), DIGEST_LENGTHS.get(this.sigParams.getDigestAlgorithm()).intValue()), 2);
            if (iBitLength < iAddExact) {
                throw new InvalidKeyException("Key is too short for current params, need min " + iAddExact);
            }
        }
    }

    private void setSigParams(AlgorithmParameterSpec algorithmParameterSpec) throws InvalidAlgorithmParameterException {
        int length;
        if (algorithmParameterSpec == null) {
            throw new InvalidAlgorithmParameterException("PSS Parameter required");
        }
        if (!(algorithmParameterSpec instanceof PSSParameterSpec)) {
            throw new InvalidAlgorithmParameterException("Only PSSParameterSpec is supported");
        }
        PSSParameterSpec pSSParameterSpec = (PSSParameterSpec) algorithmParameterSpec;
        if (pSSParameterSpec == this.sigParams) {
            return;
        }
        String digestAlgorithm = pSSParameterSpec.getDigestAlgorithm();
        if (this.mdAlg != null && !isDigestEqual(digestAlgorithm, this.mdAlg)) {
            throw new InvalidAlgorithmParameterException("Digest algorithm in Signature parameters must be " + this.mdAlg);
        }
        Integer num = DIGEST_LENGTHS.get(digestAlgorithm);
        if (num == null) {
            throw new InvalidAlgorithmParameterException("Unsupported digest algorithm in Signature parameters: " + digestAlgorithm);
        }
        if (!pSSParameterSpec.getMGFAlgorithm().equalsIgnoreCase("MGF1")) {
            throw new InvalidAlgorithmParameterException("Only supports MGF1");
        }
        if (pSSParameterSpec.getTrailerField() != 1) {
            throw new InvalidAlgorithmParameterException("Only supports TrailerFieldBC(1)");
        }
        int saltLength = pSSParameterSpec.getSaltLength();
        if (this.p11Key != null && ((length = (((this.p11Key.length() + 7) >> 3) - num.intValue()) - 2) < 0 || saltLength > length)) {
            throw new InvalidAlgorithmParameterException("Invalid with current key size");
        }
        try {
            this.mechanism.setParameter(new CK_RSA_PKCS_PSS_PARAMS(digestAlgorithm, "MGF1", digestAlgorithm, saltLength));
            this.sigParams = pSSParameterSpec;
        } catch (IllegalArgumentException e2) {
            throw new InvalidAlgorithmParameterException(e2);
        }
    }

    @Override // java.security.SignatureSpi
    protected void engineInitVerify(PublicKey publicKey) throws InvalidKeyException {
        if (publicKey == null) {
            throw new InvalidKeyException("Key must not be null");
        }
        if (publicKey != this.p11Key) {
            checkKeySize(publicKey);
        }
        reset(true);
        this.mode = 2;
        this.p11Key = P11KeyFactory.convertKey(this.token, publicKey, KEY_ALGO);
    }

    @Override // java.security.SignatureSpi
    protected void engineInitSign(PrivateKey privateKey) throws InvalidKeyException {
        if (privateKey == null) {
            throw new InvalidKeyException("Key must not be null");
        }
        if (privateKey != this.p11Key) {
            checkKeySize(privateKey);
        }
        reset(true);
        this.mode = 1;
        this.p11Key = P11KeyFactory.convertKey(this.token, privateKey, KEY_ALGO);
    }

    @Override // java.security.SignatureSpi
    protected void engineUpdate(byte b2) throws SignatureException {
        ensureInitialized();
        this.isActive = true;
        this.buffer[0] = b2;
        engineUpdate(this.buffer, 0, 1);
    }

    @Override // java.security.SignatureSpi
    protected void engineUpdate(byte[] bArr, int i2, int i3) throws SignatureException {
        ensureInitialized();
        if (i3 == 0) {
            return;
        }
        if (i3 + this.bytesProcessed < 0) {
            throw new ProviderException("Processed bytes limits exceeded.");
        }
        this.isActive = true;
        switch (this.type) {
            case 1:
                if (this.md == null) {
                    throw new ProviderException("PSS Parameters required");
                }
                this.md.update(bArr, i2, i3);
                this.bytesProcessed += i3;
                return;
            case 2:
                try {
                    if (this.mode == 1) {
                        this.token.p11.C_SignUpdate(this.session.id(), 0L, bArr, i2, i3);
                    } else {
                        this.token.p11.C_VerifyUpdate(this.session.id(), 0L, bArr, i2, i3);
                    }
                    this.bytesProcessed += i3;
                    return;
                } catch (PKCS11Exception e2) {
                    reset(false);
                    throw new ProviderException(e2);
                }
            default:
                throw new ProviderException("Internal error");
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // java.security.SignatureSpi
    protected void engineUpdate(ByteBuffer byteBuffer) {
        try {
            ensureInitialized();
            int iRemaining = byteBuffer.remaining();
            if (iRemaining <= 0) {
                return;
            }
            this.isActive = true;
            switch (this.type) {
                case 1:
                    if (this.md == null) {
                        throw new ProviderException("PSS Parameters required");
                    }
                    this.md.update(byteBuffer);
                    this.bytesProcessed += iRemaining;
                    return;
                case 2:
                    if (!(byteBuffer instanceof DirectBuffer)) {
                        super.engineUpdate(byteBuffer);
                        return;
                    }
                    long jAddress = ((DirectBuffer) byteBuffer).address();
                    int iPosition = byteBuffer.position();
                    try {
                        if (this.mode == 1) {
                            this.token.p11.C_SignUpdate(this.session.id(), jAddress + iPosition, null, 0, iRemaining);
                        } else {
                            this.token.p11.C_VerifyUpdate(this.session.id(), jAddress + iPosition, null, 0, iRemaining);
                        }
                        this.bytesProcessed += iRemaining;
                        byteBuffer.position(iPosition + iRemaining);
                        return;
                    } catch (PKCS11Exception e2) {
                        reset(false);
                        throw new ProviderException("Update failed", e2);
                    }
                default:
                    reset(false);
                    throw new ProviderException("Internal error");
            }
        } catch (SignatureException e3) {
            throw new ProviderException(e3);
        }
    }

    @Override // java.security.SignatureSpi
    protected byte[] engineSign() throws SignatureException {
        byte[] bArrC_Sign;
        ensureInitialized();
        try {
            try {
                if (this.type == 2) {
                    bArrC_Sign = this.token.p11.C_SignFinal(this.session.id(), 0);
                } else {
                    if (this.md == null) {
                        throw new ProviderException("PSS Parameters required");
                    }
                    bArrC_Sign = this.token.p11.C_Sign(this.session.id(), this.md.digest());
                }
                byte[] bArr = bArrC_Sign;
                reset(false);
                return bArr;
            } catch (ProviderException e2) {
                throw e2;
            } catch (PKCS11Exception e3) {
                throw new ProviderException(e3);
            }
        } catch (Throwable th) {
            reset(true);
            throw th;
        }
    }

    @Override // java.security.SignatureSpi
    protected boolean engineVerify(byte[] bArr) throws SignatureException {
        ensureInitialized();
        try {
            try {
                if (this.type == 2) {
                    this.token.p11.C_VerifyFinal(this.session.id(), bArr);
                } else {
                    if (this.md == null) {
                        throw new ProviderException("PSS Parameters required");
                    }
                    this.token.p11.C_Verify(this.session.id(), this.md.digest(), bArr);
                }
                reset(false);
                return true;
            } catch (ProviderException e2) {
                throw e2;
            } catch (PKCS11Exception e3) {
                long errorCode = e3.getErrorCode();
                if (errorCode == 192) {
                    reset(false);
                    return false;
                }
                if (errorCode == 193) {
                    reset(false);
                    return false;
                }
                if (errorCode != 33) {
                    throw new ProviderException(e3);
                }
                reset(false);
                return false;
            }
        } catch (Throwable th) {
            reset(true);
            throw th;
        }
    }

    @Override // java.security.SignatureSpi
    protected void engineSetParameter(String str, Object obj) throws InvalidParameterException {
        throw new UnsupportedOperationException("setParameter() not supported");
    }

    @Override // java.security.SignatureSpi
    protected void engineSetParameter(AlgorithmParameterSpec algorithmParameterSpec) throws InvalidAlgorithmParameterException {
        if (this.isActive) {
            throw new ProviderException("Cannot set parameters during operations");
        }
        setSigParams(algorithmParameterSpec);
        if (this.type == 1) {
            try {
                this.md = MessageDigest.getInstance(this.sigParams.getDigestAlgorithm());
            } catch (NoSuchAlgorithmException e2) {
                throw new InvalidAlgorithmParameterException(e2);
            }
        }
    }

    @Override // java.security.SignatureSpi
    protected Object engineGetParameter(String str) throws InvalidParameterException {
        throw new UnsupportedOperationException("getParameter() not supported");
    }

    @Override // java.security.SignatureSpi
    protected AlgorithmParameters engineGetParameters() {
        if (this.sigParams != null) {
            try {
                AlgorithmParameters algorithmParameters = AlgorithmParameters.getInstance("RSASSA-PSS");
                algorithmParameters.init(this.sigParams);
                return algorithmParameters;
            } catch (GeneralSecurityException e2) {
                throw new RuntimeException(e2);
            }
        }
        return null;
    }
}
