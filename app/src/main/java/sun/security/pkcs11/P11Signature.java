package sun.security.pkcs11;

import java.io.IOException;
import java.math.BigInteger;
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
import java.security.interfaces.DSAKey;
import java.security.interfaces.ECKey;
import java.security.interfaces.RSAKey;
import java.security.spec.AlgorithmParameterSpec;
import sun.nio.ch.DirectBuffer;
import sun.security.pkcs11.wrapper.CK_MECHANISM;
import sun.security.pkcs11.wrapper.CK_MECHANISM_INFO;
import sun.security.pkcs11.wrapper.PKCS11Exception;
import sun.security.rsa.RSAPadding;
import sun.security.rsa.RSASignature;
import sun.security.util.DerInputStream;
import sun.security.util.DerOutputStream;
import sun.security.util.DerValue;
import sun.security.util.KeyUtil;
import sun.security.util.ObjectIdentifier;
import sun.security.x509.AlgorithmId;

/* loaded from: sunpkcs11.jar:sun/security/pkcs11/P11Signature.class */
final class P11Signature extends SignatureSpi {
    private final Token token;
    private final String algorithm;
    private final String keyAlgorithm;
    private final long mechanism;
    private final ObjectIdentifier digestOID;
    private final int type;
    private P11Key p11Key;
    private final MessageDigest md;
    private Session session;
    private int mode;
    private boolean initialized;
    private final byte[] buffer;
    private int bytesProcessed;
    private static final int M_SIGN = 1;
    private static final int M_VERIFY = 2;
    private static final int T_DIGEST = 1;
    private static final int T_UPDATE = 2;
    private static final int T_RAW = 3;
    private static final int RAW_ECDSA_MAX = 128;

    P11Signature(Token token, String str, long j2) throws PKCS11Exception, NoSuchAlgorithmException {
        String str2;
        this.token = token;
        this.algorithm = str;
        this.mechanism = j2;
        byte[] bArr = null;
        ObjectIdentifier objectIdentifier = null;
        MessageDigest messageDigest = null;
        switch ((int) j2) {
            case 1:
            case 3:
                this.keyAlgorithm = "RSA";
                this.type = 1;
                if (str.equals("MD5withRSA")) {
                    messageDigest = MessageDigest.getInstance("MD5");
                    objectIdentifier = AlgorithmId.MD5_oid;
                    break;
                } else if (str.equals("SHA1withRSA")) {
                    messageDigest = MessageDigest.getInstance("SHA-1");
                    objectIdentifier = AlgorithmId.SHA_oid;
                    break;
                } else if (str.equals("MD2withRSA")) {
                    messageDigest = MessageDigest.getInstance("MD2");
                    objectIdentifier = AlgorithmId.MD2_oid;
                    break;
                } else if (str.equals("SHA224withRSA")) {
                    messageDigest = MessageDigest.getInstance("SHA-224");
                    objectIdentifier = AlgorithmId.SHA224_oid;
                    break;
                } else if (str.equals("SHA256withRSA")) {
                    messageDigest = MessageDigest.getInstance("SHA-256");
                    objectIdentifier = AlgorithmId.SHA256_oid;
                    break;
                } else if (str.equals("SHA384withRSA")) {
                    messageDigest = MessageDigest.getInstance("SHA-384");
                    objectIdentifier = AlgorithmId.SHA384_oid;
                    break;
                } else if (str.equals("SHA512withRSA")) {
                    messageDigest = MessageDigest.getInstance("SHA-512");
                    objectIdentifier = AlgorithmId.SHA512_oid;
                    break;
                } else {
                    throw new ProviderException("Unknown signature: " + str);
                }
            case 4:
            case 5:
            case 6:
            case 64:
            case 65:
            case 66:
            case 70:
                this.keyAlgorithm = "RSA";
                this.type = 2;
                bArr = new byte[1];
                break;
            case 17:
                this.keyAlgorithm = "DSA";
                if (str.equals("DSA")) {
                    this.type = 1;
                    messageDigest = MessageDigest.getInstance("SHA-1");
                    break;
                } else if (str.equals("RawDSA")) {
                    this.type = 3;
                    bArr = new byte[20];
                    break;
                } else {
                    throw new ProviderException(str);
                }
            case 18:
                this.keyAlgorithm = "DSA";
                this.type = 2;
                bArr = new byte[1];
                break;
            case 4161:
                this.keyAlgorithm = "EC";
                if (str.equals("NONEwithECDSA")) {
                    this.type = 3;
                    bArr = new byte[128];
                    break;
                } else {
                    if (str.equals("SHA1withECDSA")) {
                        str2 = "SHA-1";
                    } else if (str.equals("SHA224withECDSA")) {
                        str2 = "SHA-224";
                    } else if (str.equals("SHA256withECDSA")) {
                        str2 = "SHA-256";
                    } else if (str.equals("SHA384withECDSA")) {
                        str2 = "SHA-384";
                    } else if (str.equals("SHA512withECDSA")) {
                        str2 = "SHA-512";
                    } else {
                        throw new ProviderException(str);
                    }
                    this.type = 1;
                    messageDigest = MessageDigest.getInstance(str2);
                    break;
                }
            case 4162:
                this.keyAlgorithm = "EC";
                this.type = 2;
                bArr = new byte[1];
                break;
            default:
                throw new ProviderException("Unknown mechanism: " + j2);
        }
        this.buffer = bArr;
        this.digestOID = objectIdentifier;
        this.md = messageDigest;
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
        byte[] bArr;
        byte[] bArrDigest;
        byte[] bArrDigest2;
        this.token.ensureValid();
        try {
            if (this.mode == 1) {
                if (this.type == 2) {
                    this.token.p11.C_SignFinal(this.session.id(), 0);
                } else {
                    if (this.type == 1) {
                        bArrDigest2 = this.md.digest();
                    } else {
                        bArrDigest2 = this.buffer;
                    }
                    this.token.p11.C_Sign(this.session.id(), bArrDigest2);
                }
            } else {
                if (this.keyAlgorithm.equals("DSA")) {
                    bArr = new byte[40];
                } else {
                    bArr = new byte[(this.p11Key.length() + 7) >> 3];
                }
                if (this.type == 2) {
                    this.token.p11.C_VerifyFinal(this.session.id(), bArr);
                } else {
                    if (this.type == 1) {
                        bArrDigest = this.md.digest();
                    } else {
                        bArrDigest = this.buffer;
                    }
                    this.token.p11.C_Verify(this.session.id(), bArrDigest, bArr);
                }
            }
        } catch (PKCS11Exception e2) {
            if (e2.getErrorCode() == 145) {
                return;
            }
            if (this.mode == 2) {
                long errorCode = e2.getErrorCode();
                if (errorCode == 192 || errorCode == 193) {
                    return;
                }
            }
            throw new ProviderException("cancel failed", e2);
        }
    }

    private void ensureInitialized() {
        if (!this.initialized) {
            initialize();
        }
    }

    private void initialize() {
        if (this.p11Key == null) {
            throw new ProviderException("Operation cannot be performed without calling engineInit first");
        }
        long keyID = this.p11Key.getKeyID();
        try {
            this.token.ensureValid();
            if (this.session == null) {
                this.session = this.token.getOpSession();
            }
            if (this.mode == 1) {
                this.token.p11.C_SignInit(this.session.id(), new CK_MECHANISM(this.mechanism), keyID);
            } else {
                this.token.p11.C_VerifyInit(this.session.id(), new CK_MECHANISM(this.mechanism), keyID);
            }
            if (this.bytesProcessed != 0) {
                this.bytesProcessed = 0;
                if (this.md != null) {
                    this.md.reset();
                }
            }
            this.initialized = true;
        } catch (PKCS11Exception e2) {
            this.p11Key.releaseKeyID();
            this.session = this.token.releaseSession(this.session);
            throw new ProviderException("Initialization failed", e2);
        }
    }

    private void checkKeySize(String str, Key key) throws InvalidKeyException {
        int fieldSize;
        CK_MECHANISM_INFO mechanismInfo = null;
        try {
            mechanismInfo = this.token.getMechanismInfo(this.mechanism);
        } catch (PKCS11Exception e2) {
        }
        if (mechanismInfo == null) {
            return;
        }
        int i2 = mechanismInfo.iMinKeySize;
        int i3 = mechanismInfo.iMaxKeySize;
        if (this.md != null && this.mechanism == 17 && i3 > 1024) {
            i3 = 1024;
        }
        if (key instanceof P11Key) {
            fieldSize = ((P11Key) key).length();
        } else {
            try {
                if (str.equals("RSA")) {
                    fieldSize = ((RSAKey) key).getModulus().bitLength();
                } else if (str.equals("DSA")) {
                    fieldSize = ((DSAKey) key).getParams().getP().bitLength();
                } else if (str.equals("EC")) {
                    fieldSize = ((ECKey) key).getParams().getCurve().getField().getFieldSize();
                } else {
                    throw new ProviderException("Error: unsupported algo " + str);
                }
            } catch (ClassCastException e3) {
                throw new InvalidKeyException(str + " key must be the right type", e3);
            }
        }
        if (fieldSize < i2) {
            throw new InvalidKeyException(str + " key must be at least " + i2 + " bits");
        }
        if (fieldSize > i3) {
            throw new InvalidKeyException(str + " key must be at most " + i3 + " bits");
        }
        if (str.equals("RSA")) {
            checkRSAKeyLength(fieldSize);
        }
    }

    private void checkRSAKeyLength(int i2) throws InvalidKeyException {
        int i3;
        try {
            int maxDataSize = RSAPadding.getInstance(1, (i2 + 7) >> 3).getMaxDataSize();
            if (this.algorithm.equals("MD5withRSA") || this.algorithm.equals("MD2withRSA")) {
                i3 = 34;
            } else if (this.algorithm.equals("SHA1withRSA")) {
                i3 = 35;
            } else if (this.algorithm.equals("SHA224withRSA")) {
                i3 = 47;
            } else if (this.algorithm.equals("SHA256withRSA")) {
                i3 = 51;
            } else if (this.algorithm.equals("SHA384withRSA")) {
                i3 = 67;
            } else if (this.algorithm.equals("SHA512withRSA")) {
                i3 = 83;
            } else {
                throw new ProviderException("Unknown signature algo: " + this.algorithm);
            }
            if (i3 > maxDataSize) {
                throw new InvalidKeyException("Key is too short for this signature algorithm");
            }
        } catch (InvalidAlgorithmParameterException e2) {
            throw new InvalidKeyException(e2.getMessage());
        }
    }

    @Override // java.security.SignatureSpi
    protected void engineInitVerify(PublicKey publicKey) throws InvalidKeyException {
        if (publicKey == null) {
            throw new InvalidKeyException("Key must not be null");
        }
        if (publicKey != this.p11Key) {
            checkKeySize(this.keyAlgorithm, publicKey);
        }
        reset(true);
        this.mode = 2;
        this.p11Key = P11KeyFactory.convertKey(this.token, publicKey, this.keyAlgorithm);
        initialize();
    }

    @Override // java.security.SignatureSpi
    protected void engineInitSign(PrivateKey privateKey) throws InvalidKeyException {
        if (privateKey == null) {
            throw new InvalidKeyException("Key must not be null");
        }
        if (privateKey != this.p11Key) {
            checkKeySize(this.keyAlgorithm, privateKey);
        }
        reset(true);
        this.mode = 1;
        this.p11Key = P11KeyFactory.convertKey(this.token, privateKey, this.keyAlgorithm);
        initialize();
    }

    @Override // java.security.SignatureSpi
    protected void engineUpdate(byte b2) throws SignatureException {
        ensureInitialized();
        switch (this.type) {
            case 1:
                this.md.update(b2);
                this.bytesProcessed++;
                return;
            case 2:
                this.buffer[0] = b2;
                engineUpdate(this.buffer, 0, 1);
                return;
            case 3:
                if (this.bytesProcessed >= this.buffer.length) {
                    this.bytesProcessed = this.buffer.length + 1;
                    return;
                }
                byte[] bArr = this.buffer;
                int i2 = this.bytesProcessed;
                this.bytesProcessed = i2 + 1;
                bArr[i2] = b2;
                return;
            default:
                throw new ProviderException("Internal error");
        }
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
        switch (this.type) {
            case 1:
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
            case 3:
                if (this.bytesProcessed + i3 > this.buffer.length) {
                    this.bytesProcessed = this.buffer.length + 1;
                    return;
                } else {
                    System.arraycopy(bArr, i2, this.buffer, this.bytesProcessed, i3);
                    this.bytesProcessed += i3;
                    return;
                }
            default:
                throw new ProviderException("Internal error");
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // java.security.SignatureSpi
    protected void engineUpdate(ByteBuffer byteBuffer) {
        ensureInitialized();
        int iRemaining = byteBuffer.remaining();
        if (iRemaining <= 0) {
            return;
        }
        switch (this.type) {
            case 1:
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
            case 3:
                if (this.bytesProcessed + iRemaining > this.buffer.length) {
                    this.bytesProcessed = this.buffer.length + 1;
                    return;
                } else {
                    byteBuffer.get(this.buffer, this.bytesProcessed, iRemaining);
                    this.bytesProcessed += iRemaining;
                    return;
                }
            default:
                reset(false);
                throw new ProviderException("Internal error");
        }
    }

    @Override // java.security.SignatureSpi
    protected byte[] engineSign() throws SignatureException {
        byte[] bArrDigest;
        byte[] bArrC_Sign;
        ensureInitialized();
        try {
            try {
                if (this.type == 2) {
                    bArrC_Sign = this.token.p11.C_SignFinal(this.session.id(), this.keyAlgorithm.equals("DSA") ? 40 : 0);
                } else {
                    if (this.type == 1) {
                        bArrDigest = this.md.digest();
                    } else if (this.mechanism == 17) {
                        if (this.bytesProcessed != this.buffer.length) {
                            throw new SignatureException("Data for RawDSA must be exactly 20 bytes long");
                        }
                        bArrDigest = this.buffer;
                    } else {
                        if (this.bytesProcessed > this.buffer.length) {
                            throw new SignatureException("Data for NONEwithECDSA must be at most 128 bytes long");
                        }
                        bArrDigest = new byte[this.bytesProcessed];
                        System.arraycopy(this.buffer, 0, bArrDigest, 0, this.bytesProcessed);
                    }
                    if (!this.keyAlgorithm.equals("RSA")) {
                        bArrC_Sign = this.token.p11.C_Sign(this.session.id(), bArrDigest);
                    } else {
                        byte[] bArrEncodeSignature = encodeSignature(bArrDigest);
                        if (this.mechanism == 3) {
                            bArrEncodeSignature = pkcs1Pad(bArrEncodeSignature);
                        }
                        bArrC_Sign = this.token.p11.C_Sign(this.session.id(), bArrEncodeSignature);
                    }
                }
                if (!this.keyAlgorithm.equals("RSA")) {
                    byte[] bArrDsaToASN1 = dsaToASN1(bArrC_Sign);
                    reset(false);
                    return bArrDsaToASN1;
                }
                byte[] bArr = bArrC_Sign;
                reset(false);
                return bArr;
            } catch (PKCS11Exception e2) {
                throw new ProviderException(e2);
            }
        } catch (Throwable th) {
            reset(true);
            throw th;
        }
    }

    @Override // java.security.SignatureSpi
    protected boolean engineVerify(byte[] bArr) throws SignatureException {
        byte[] bArrDigest;
        ensureInitialized();
        try {
            try {
                if (this.keyAlgorithm.equals("DSA")) {
                    bArr = asn1ToDSA(bArr);
                } else if (this.keyAlgorithm.equals("EC")) {
                    bArr = asn1ToECDSA(bArr);
                }
                if (this.type == 2) {
                    this.token.p11.C_VerifyFinal(this.session.id(), bArr);
                } else {
                    if (this.type == 1) {
                        bArrDigest = this.md.digest();
                    } else if (this.mechanism == 17) {
                        if (this.bytesProcessed != this.buffer.length) {
                            throw new SignatureException("Data for RawDSA must be exactly 20 bytes long");
                        }
                        bArrDigest = this.buffer;
                    } else {
                        if (this.bytesProcessed > this.buffer.length) {
                            throw new SignatureException("Data for NONEwithECDSA must be at most 128 bytes long");
                        }
                        bArrDigest = new byte[this.bytesProcessed];
                        System.arraycopy(this.buffer, 0, bArrDigest, 0, this.bytesProcessed);
                    }
                    if (this.keyAlgorithm.equals("RSA")) {
                        byte[] bArrEncodeSignature = encodeSignature(bArrDigest);
                        if (this.mechanism == 3) {
                            bArrEncodeSignature = pkcs1Pad(bArrEncodeSignature);
                        }
                        this.token.p11.C_Verify(this.session.id(), bArrEncodeSignature, bArr);
                    } else {
                        this.token.p11.C_Verify(this.session.id(), bArrDigest, bArr);
                    }
                }
                reset(false);
                return true;
            } catch (PKCS11Exception e2) {
                long errorCode = e2.getErrorCode();
                if (errorCode == 192) {
                    reset(false);
                    return false;
                }
                if (errorCode == 193) {
                    reset(false);
                    return false;
                }
                if (errorCode != 33) {
                    throw new ProviderException(e2);
                }
                reset(false);
                return false;
            }
        } catch (Throwable th) {
            reset(true);
            throw th;
        }
    }

    private byte[] pkcs1Pad(byte[] bArr) {
        try {
            return RSAPadding.getInstance(1, (this.p11Key.length() + 7) >> 3).pad(bArr);
        } catch (GeneralSecurityException e2) {
            throw new ProviderException(e2);
        }
    }

    private byte[] encodeSignature(byte[] bArr) throws SignatureException {
        try {
            return RSASignature.encodeSignature(this.digestOID, bArr);
        } catch (IOException e2) {
            throw new SignatureException("Invalid encoding", e2);
        }
    }

    private static byte[] dsaToASN1(byte[] bArr) {
        int length = bArr.length >> 1;
        BigInteger bigInteger = new BigInteger(1, P11Util.subarray(bArr, 0, length));
        BigInteger bigInteger2 = new BigInteger(1, P11Util.subarray(bArr, length, length));
        try {
            DerOutputStream derOutputStream = new DerOutputStream(100);
            derOutputStream.putInteger(bigInteger);
            derOutputStream.putInteger(bigInteger2);
            return new DerValue((byte) 48, derOutputStream.toByteArray()).toByteArray();
        } catch (IOException e2) {
            throw new RuntimeException("Internal error", e2);
        }
    }

    private static byte[] asn1ToDSA(byte[] bArr) throws SignatureException, IOException {
        try {
            DerInputStream derInputStream = new DerInputStream(bArr, 0, bArr.length, false);
            DerValue[] sequence = derInputStream.getSequence(2);
            if (sequence.length != 2 || derInputStream.available() != 0) {
                throw new IOException("Invalid encoding for signature");
            }
            BigInteger positiveBigInteger = sequence[0].getPositiveBigInteger();
            BigInteger positiveBigInteger2 = sequence[1].getPositiveBigInteger();
            byte[] byteArray = toByteArray(positiveBigInteger, 20);
            byte[] byteArray2 = toByteArray(positiveBigInteger2, 20);
            if (byteArray == null || byteArray2 == null) {
                throw new SignatureException("Out of range value for R or S");
            }
            return P11Util.concat(byteArray, byteArray2);
        } catch (SignatureException e2) {
            throw e2;
        } catch (Exception e3) {
            throw new SignatureException("Invalid encoding for signature", e3);
        }
    }

    private byte[] asn1ToECDSA(byte[] bArr) throws SignatureException, IOException {
        try {
            DerInputStream derInputStream = new DerInputStream(bArr, 0, bArr.length, false);
            DerValue[] sequence = derInputStream.getSequence(2);
            if (sequence.length != 2 || derInputStream.available() != 0) {
                throw new IOException("Invalid encoding for signature");
            }
            BigInteger positiveBigInteger = sequence[0].getPositiveBigInteger();
            BigInteger positiveBigInteger2 = sequence[1].getPositiveBigInteger();
            byte[] bArrTrimZeroes = KeyUtil.trimZeroes(positiveBigInteger.toByteArray());
            byte[] bArrTrimZeroes2 = KeyUtil.trimZeroes(positiveBigInteger2.toByteArray());
            int iMax = Math.max(bArrTrimZeroes.length, bArrTrimZeroes2.length);
            byte[] bArr2 = new byte[iMax << 1];
            System.arraycopy(bArrTrimZeroes, 0, bArr2, iMax - bArrTrimZeroes.length, bArrTrimZeroes.length);
            System.arraycopy(bArrTrimZeroes2, 0, bArr2, bArr2.length - bArrTrimZeroes2.length, bArrTrimZeroes2.length);
            return bArr2;
        } catch (Exception e2) {
            throw new SignatureException("Invalid encoding for signature", e2);
        }
    }

    private static byte[] toByteArray(BigInteger bigInteger, int i2) {
        byte[] byteArray = bigInteger.toByteArray();
        int length = byteArray.length;
        if (length == i2) {
            return byteArray;
        }
        if (length == i2 + 1 && byteArray[0] == 0) {
            byte[] bArr = new byte[i2];
            System.arraycopy(byteArray, 1, bArr, 0, i2);
            return bArr;
        }
        if (length > i2) {
            return null;
        }
        byte[] bArr2 = new byte[i2];
        System.arraycopy(byteArray, 0, bArr2, i2 - length, length);
        return bArr2;
    }

    @Override // java.security.SignatureSpi
    protected void engineSetParameter(String str, Object obj) throws InvalidParameterException {
        throw new UnsupportedOperationException("setParameter() not supported");
    }

    @Override // java.security.SignatureSpi
    protected void engineSetParameter(AlgorithmParameterSpec algorithmParameterSpec) throws InvalidAlgorithmParameterException {
        if (algorithmParameterSpec != null) {
            throw new InvalidAlgorithmParameterException("No parameter accepted");
        }
    }

    @Override // java.security.SignatureSpi
    protected Object engineGetParameter(String str) throws InvalidParameterException {
        throw new UnsupportedOperationException("getParameter() not supported");
    }

    @Override // java.security.SignatureSpi
    protected AlgorithmParameters engineGetParameters() {
        return null;
    }
}
