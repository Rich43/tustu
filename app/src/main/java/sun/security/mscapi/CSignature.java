package sun.security.mscapi;

import java.io.IOException;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.security.AlgorithmParameters;
import java.security.GeneralSecurityException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.InvalidParameterException;
import java.security.KeyStoreException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.ProviderException;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;
import java.security.SignatureSpi;
import java.security.interfaces.ECPublicKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.MGF1ParameterSpec;
import java.security.spec.PSSParameterSpec;
import java.util.Locale;
import sun.security.rsa.RSAKeyFactory;
import sun.security.util.ECUtil;
import sun.security.util.KeyUtil;
import sun.util.locale.LanguageTag;

/* loaded from: sunmscapi.jar:sun/security/mscapi/CSignature.class */
abstract class CSignature extends SignatureSpi {
    protected String keyAlgorithm;
    protected MessageDigest messageDigest;
    protected String messageDigestAlgorithm;
    protected boolean needsReset;
    protected CPrivateKey privateKey = null;
    protected CPublicKey publicKey = null;

    static native byte[] signCngHash(int i2, byte[] bArr, int i3, int i4, String str, long j2, long j3) throws SignatureException;

    /* JADX INFO: Access modifiers changed from: private */
    public static native boolean verifyCngSignedHash(int i2, byte[] bArr, int i3, byte[] bArr2, int i4, int i5, String str, long j2, long j3) throws SignatureException;

    /* JADX INFO: Access modifiers changed from: private */
    public static native byte[] signHash(boolean z2, byte[] bArr, int i2, String str, long j2, long j3) throws SignatureException;

    /* JADX INFO: Access modifiers changed from: private */
    public static native boolean verifySignedHash(byte[] bArr, int i2, String str, byte[] bArr2, int i3, long j2, long j3) throws SignatureException;

    static native CPublicKey importPublicKey(String str, byte[] bArr, int i2) throws KeyStoreException;

    static native CPublicKey importECPublicKey(String str, byte[] bArr, int i2) throws KeyStoreException;

    CSignature(String str, String str2) {
        this.keyAlgorithm = str;
        if (str2 != null) {
            try {
                this.messageDigest = MessageDigest.getInstance(str2);
                this.messageDigestAlgorithm = this.messageDigest.getAlgorithm();
            } catch (NoSuchAlgorithmException e2) {
                throw new ProviderException(e2);
            }
        } else {
            this.messageDigest = null;
            this.messageDigestAlgorithm = null;
        }
        this.needsReset = false;
    }

    /* loaded from: sunmscapi.jar:sun/security/mscapi/CSignature$RSA.class */
    static class RSA extends CSignature {
        static native byte[] generatePublicKeyBlob(int i2, byte[] bArr, byte[] bArr2) throws InvalidKeyException;

        public RSA(String str) {
            super("RSA", str);
        }

        @Override // java.security.SignatureSpi
        protected void engineInitSign(PrivateKey privateKey) throws InvalidKeyException {
            if (privateKey == null) {
                throw new InvalidKeyException("Key cannot be null");
            }
            if (!(privateKey instanceof CPrivateKey) || !privateKey.getAlgorithm().equalsIgnoreCase("RSA")) {
                throw new InvalidKeyException("Key type not supported: " + ((Object) privateKey.getClass()) + " " + privateKey.getAlgorithm());
            }
            this.privateKey = (CPrivateKey) privateKey;
            RSAKeyFactory.checkKeyLengths((this.privateKey.length() + 7) & (-8), null, 512, 16384);
            this.publicKey = null;
            resetDigest();
        }

        @Override // java.security.SignatureSpi
        protected void engineInitVerify(PublicKey publicKey) throws InvalidKeyException {
            if (publicKey == null) {
                throw new InvalidKeyException("Key cannot be null");
            }
            if (!(publicKey instanceof RSAPublicKey)) {
                throw new InvalidKeyException("Key type not supported: " + ((Object) publicKey.getClass()));
            }
            if (!(publicKey instanceof CPublicKey)) {
                RSAPublicKey rSAPublicKey = (RSAPublicKey) publicKey;
                BigInteger modulus = rSAPublicKey.getModulus();
                BigInteger publicExponent = rSAPublicKey.getPublicExponent();
                RSAKeyFactory.checkKeyLengths((modulus.bitLength() + 7) & (-8), publicExponent, -1, 16384);
                byte[] byteArray = modulus.toByteArray();
                byte[] byteArray2 = publicExponent.toByteArray();
                int length = byteArray[0] == 0 ? (byteArray.length - 1) * 8 : byteArray.length * 8;
                try {
                    this.publicKey = importPublicKey("RSA", generatePublicKeyBlob(length, byteArray, byteArray2), length);
                } catch (KeyStoreException e2) {
                    throw new InvalidKeyException(e2);
                }
            } else {
                this.publicKey = (CPublicKey) publicKey;
            }
            this.privateKey = null;
            resetDigest();
        }

        @Override // java.security.SignatureSpi
        protected byte[] engineSign() throws SignatureException {
            byte[] digestValue = getDigestValue();
            if (this.privateKey.getHCryptKey() != 0) {
                return CSignature.convertEndianArray(CSignature.signHash(this instanceof NONEwithRSA, digestValue, digestValue.length, this.messageDigestAlgorithm, this.privateKey.getHCryptProvider(), this.privateKey.getHCryptKey()));
            }
            return signCngHash(1, digestValue, digestValue.length, 0, this instanceof NONEwithRSA ? null : this.messageDigestAlgorithm, this.privateKey.getHCryptProvider(), 0L);
        }

        @Override // java.security.SignatureSpi
        protected boolean engineVerify(byte[] bArr) throws SignatureException {
            byte[] digestValue = getDigestValue();
            return this.publicKey.getHCryptKey() == 0 ? CSignature.verifyCngSignedHash(1, digestValue, digestValue.length, bArr, bArr.length, 0, this.messageDigestAlgorithm, this.publicKey.getHCryptProvider(), 0L) : CSignature.verifySignedHash(digestValue, digestValue.length, this.messageDigestAlgorithm, CSignature.convertEndianArray(bArr), bArr.length, this.publicKey.getHCryptProvider(), this.publicKey.getHCryptKey());
        }
    }

    /* loaded from: sunmscapi.jar:sun/security/mscapi/CSignature$NONEwithRSA.class */
    public static final class NONEwithRSA extends RSA {
        private static final int RAW_RSA_MAX = 64;
        private final byte[] precomputedDigest;
        private int offset;

        public NONEwithRSA() {
            super(null);
            this.offset = 0;
            this.precomputedDigest = new byte[64];
        }

        @Override // sun.security.mscapi.CSignature, java.security.SignatureSpi
        protected void engineUpdate(byte b2) throws SignatureException {
            if (this.offset >= this.precomputedDigest.length) {
                this.offset = 65;
                return;
            }
            byte[] bArr = this.precomputedDigest;
            int i2 = this.offset;
            this.offset = i2 + 1;
            bArr[i2] = b2;
        }

        @Override // sun.security.mscapi.CSignature, java.security.SignatureSpi
        protected void engineUpdate(byte[] bArr, int i2, int i3) throws SignatureException {
            if (i3 > this.precomputedDigest.length - this.offset) {
                this.offset = 65;
            } else {
                System.arraycopy(bArr, i2, this.precomputedDigest, this.offset, i3);
                this.offset += i3;
            }
        }

        @Override // sun.security.mscapi.CSignature, java.security.SignatureSpi
        protected void engineUpdate(ByteBuffer byteBuffer) {
            int iRemaining = byteBuffer.remaining();
            if (iRemaining <= 0) {
                return;
            }
            if (iRemaining > this.precomputedDigest.length - this.offset) {
                this.offset = 65;
            } else {
                byteBuffer.get(this.precomputedDigest, this.offset, iRemaining);
                this.offset += iRemaining;
            }
        }

        @Override // sun.security.mscapi.CSignature
        protected void resetDigest() {
            this.offset = 0;
        }

        @Override // sun.security.mscapi.CSignature
        protected byte[] getDigestValue() throws SignatureException {
            if (this.offset > 64) {
                throw new SignatureException("Message digest is too long");
            }
            if (this.offset == 20) {
                setDigestName("SHA1");
            } else if (this.offset == 36) {
                setDigestName("SHA1+MD5");
            } else if (this.offset == 32) {
                setDigestName("SHA-256");
            } else if (this.offset == 48) {
                setDigestName("SHA-384");
            } else if (this.offset == 64) {
                setDigestName("SHA-512");
            } else if (this.offset == 16) {
                setDigestName("MD5");
            } else {
                throw new SignatureException("Message digest length is not supported");
            }
            byte[] bArr = new byte[this.offset];
            System.arraycopy(this.precomputedDigest, 0, bArr, 0, this.offset);
            this.offset = 0;
            return bArr;
        }
    }

    /* loaded from: sunmscapi.jar:sun/security/mscapi/CSignature$SHA1withRSA.class */
    public static final class SHA1withRSA extends RSA {
        public SHA1withRSA() {
            super("SHA1");
        }
    }

    /* loaded from: sunmscapi.jar:sun/security/mscapi/CSignature$SHA256withRSA.class */
    public static final class SHA256withRSA extends RSA {
        public SHA256withRSA() {
            super("SHA-256");
        }
    }

    /* loaded from: sunmscapi.jar:sun/security/mscapi/CSignature$SHA384withRSA.class */
    public static final class SHA384withRSA extends RSA {
        public SHA384withRSA() {
            super("SHA-384");
        }
    }

    /* loaded from: sunmscapi.jar:sun/security/mscapi/CSignature$SHA512withRSA.class */
    public static final class SHA512withRSA extends RSA {
        public SHA512withRSA() {
            super("SHA-512");
        }
    }

    /* loaded from: sunmscapi.jar:sun/security/mscapi/CSignature$MD5withRSA.class */
    public static final class MD5withRSA extends RSA {
        public MD5withRSA() {
            super("MD5");
        }
    }

    /* loaded from: sunmscapi.jar:sun/security/mscapi/CSignature$MD2withRSA.class */
    public static final class MD2withRSA extends RSA {
        public MD2withRSA() {
            super("MD2");
        }
    }

    /* loaded from: sunmscapi.jar:sun/security/mscapi/CSignature$SHA1withECDSA.class */
    public static final class SHA1withECDSA extends ECDSA {
        public SHA1withECDSA() {
            super("SHA-1");
        }
    }

    /* loaded from: sunmscapi.jar:sun/security/mscapi/CSignature$SHA224withECDSA.class */
    public static final class SHA224withECDSA extends ECDSA {
        public SHA224withECDSA() {
            super("SHA-224");
        }
    }

    /* loaded from: sunmscapi.jar:sun/security/mscapi/CSignature$SHA256withECDSA.class */
    public static final class SHA256withECDSA extends ECDSA {
        public SHA256withECDSA() {
            super("SHA-256");
        }
    }

    /* loaded from: sunmscapi.jar:sun/security/mscapi/CSignature$SHA384withECDSA.class */
    public static final class SHA384withECDSA extends ECDSA {
        public SHA384withECDSA() {
            super("SHA-384");
        }
    }

    /* loaded from: sunmscapi.jar:sun/security/mscapi/CSignature$SHA512withECDSA.class */
    public static final class SHA512withECDSA extends ECDSA {
        public SHA512withECDSA() {
            super("SHA-512");
        }
    }

    /* loaded from: sunmscapi.jar:sun/security/mscapi/CSignature$ECDSA.class */
    static class ECDSA extends CSignature {
        public ECDSA(String str) {
            super("EC", str);
        }

        @Override // java.security.SignatureSpi
        protected void engineInitSign(PrivateKey privateKey) throws InvalidKeyException {
            if (privateKey == null) {
                throw new InvalidKeyException("Key cannot be null");
            }
            if (!(privateKey instanceof CPrivateKey) || !privateKey.getAlgorithm().equalsIgnoreCase("EC")) {
                throw new InvalidKeyException("Key type not supported: " + ((Object) privateKey.getClass()) + " " + privateKey.getAlgorithm());
            }
            this.privateKey = (CPrivateKey) privateKey;
            this.publicKey = null;
            resetDigest();
        }

        @Override // java.security.SignatureSpi
        protected void engineInitVerify(PublicKey publicKey) throws InvalidKeyException {
            if (publicKey == null) {
                throw new InvalidKeyException("Key cannot be null");
            }
            if (!(publicKey instanceof ECPublicKey)) {
                throw new InvalidKeyException("Key type not supported: " + ((Object) publicKey.getClass()));
            }
            if (!(publicKey instanceof CPublicKey)) {
                try {
                    this.publicKey = importECPublicKey("EC", CKey.generateECBlob(publicKey), KeyUtil.getKeySize(publicKey));
                } catch (KeyStoreException e2) {
                    throw new InvalidKeyException(e2);
                }
            } else {
                this.publicKey = (CPublicKey) publicKey;
            }
            this.privateKey = null;
            resetDigest();
        }

        @Override // java.security.SignatureSpi
        protected byte[] engineSign() throws SignatureException {
            byte[] digestValue = getDigestValue();
            return ECUtil.encodeSignature(signCngHash(0, digestValue, digestValue.length, 0, null, this.privateKey.getHCryptProvider(), 0L));
        }

        @Override // java.security.SignatureSpi
        protected boolean engineVerify(byte[] bArr) throws SignatureException, IOException {
            byte[] digestValue = getDigestValue();
            byte[] bArrDecodeSignature = ECUtil.decodeSignature(bArr);
            return CSignature.verifyCngSignedHash(0, digestValue, digestValue.length, bArrDecodeSignature, bArrDecodeSignature.length, 0, null, this.publicKey.getHCryptProvider(), 0L);
        }
    }

    /* loaded from: sunmscapi.jar:sun/security/mscapi/CSignature$PSS.class */
    public static final class PSS extends RSA {
        private PSSParameterSpec pssParams;
        private Signature fallbackSignature;

        public PSS() {
            super(null);
            this.pssParams = null;
        }

        @Override // sun.security.mscapi.CSignature.RSA, java.security.SignatureSpi
        protected void engineInitSign(PrivateKey privateKey) throws InvalidKeyException {
            super.engineInitSign(privateKey);
            this.fallbackSignature = null;
        }

        @Override // sun.security.mscapi.CSignature.RSA, java.security.SignatureSpi
        protected void engineInitVerify(PublicKey publicKey) throws InvalidKeyException {
            if (publicKey == null) {
                throw new InvalidKeyException("Key cannot be null");
            }
            if (!(publicKey instanceof RSAPublicKey)) {
                throw new InvalidKeyException("Key type not supported: " + ((Object) publicKey.getClass()));
            }
            this.privateKey = null;
            if (publicKey instanceof CPublicKey) {
                this.fallbackSignature = null;
                this.publicKey = (CPublicKey) publicKey;
            } else {
                if (this.fallbackSignature == null) {
                    try {
                        this.fallbackSignature = Signature.getInstance("RSASSA-PSS", "SunRsaSign");
                    } catch (NoSuchAlgorithmException | NoSuchProviderException e2) {
                        throw new InvalidKeyException("Invalid key", e2);
                    }
                }
                this.fallbackSignature.initVerify(publicKey);
                if (this.pssParams != null) {
                    try {
                        this.fallbackSignature.setParameter(this.pssParams);
                    } catch (InvalidAlgorithmParameterException e3) {
                        throw new InvalidKeyException("Invalid params", e3);
                    }
                }
                this.publicKey = null;
            }
            resetDigest();
        }

        @Override // sun.security.mscapi.CSignature, java.security.SignatureSpi
        protected void engineUpdate(byte b2) throws SignatureException {
            ensureInit();
            if (this.fallbackSignature != null) {
                this.fallbackSignature.update(b2);
            } else {
                this.messageDigest.update(b2);
            }
            this.needsReset = true;
        }

        @Override // sun.security.mscapi.CSignature, java.security.SignatureSpi
        protected void engineUpdate(byte[] bArr, int i2, int i3) throws SignatureException {
            ensureInit();
            if (this.fallbackSignature != null) {
                this.fallbackSignature.update(bArr, i2, i3);
            } else {
                this.messageDigest.update(bArr, i2, i3);
            }
            this.needsReset = true;
        }

        @Override // sun.security.mscapi.CSignature, java.security.SignatureSpi
        protected void engineUpdate(ByteBuffer byteBuffer) {
            try {
                ensureInit();
                if (this.fallbackSignature != null) {
                    try {
                        this.fallbackSignature.update(byteBuffer);
                    } catch (SignatureException e2) {
                        throw new RuntimeException(e2.getMessage());
                    }
                } else {
                    this.messageDigest.update(byteBuffer);
                }
                this.needsReset = true;
            } catch (SignatureException e3) {
                throw new RuntimeException(e3.getMessage());
            }
        }

        @Override // sun.security.mscapi.CSignature.RSA, java.security.SignatureSpi
        protected byte[] engineSign() throws SignatureException {
            ensureInit();
            byte[] digestValue = getDigestValue();
            return signCngHash(2, digestValue, digestValue.length, this.pssParams.getSaltLength(), ((MGF1ParameterSpec) this.pssParams.getMGFParameters()).getDigestAlgorithm(), this.privateKey.getHCryptProvider(), this.privateKey.getHCryptKey());
        }

        @Override // sun.security.mscapi.CSignature.RSA, java.security.SignatureSpi
        protected boolean engineVerify(byte[] bArr) throws SignatureException {
            ensureInit();
            if (this.fallbackSignature != null) {
                this.needsReset = false;
                return this.fallbackSignature.verify(bArr);
            }
            byte[] digestValue = getDigestValue();
            return CSignature.verifyCngSignedHash(2, digestValue, digestValue.length, bArr, bArr.length, this.pssParams.getSaltLength(), ((MGF1ParameterSpec) this.pssParams.getMGFParameters()).getDigestAlgorithm(), this.publicKey.getHCryptProvider(), this.publicKey.getHCryptKey());
        }

        @Override // sun.security.mscapi.CSignature, java.security.SignatureSpi
        protected void engineSetParameter(AlgorithmParameterSpec algorithmParameterSpec) throws InvalidAlgorithmParameterException {
            if (this.needsReset) {
                throw new ProviderException("Cannot set parameters during operations");
            }
            this.pssParams = validateSigParams(algorithmParameterSpec);
            if (this.fallbackSignature != null) {
                this.fallbackSignature.setParameter(algorithmParameterSpec);
            }
        }

        @Override // sun.security.mscapi.CSignature, java.security.SignatureSpi
        protected AlgorithmParameters engineGetParameters() {
            AlgorithmParameters algorithmParameters = null;
            if (this.pssParams != null) {
                try {
                    algorithmParameters = AlgorithmParameters.getInstance("RSASSA-PSS");
                    algorithmParameters.init(this.pssParams);
                } catch (GeneralSecurityException e2) {
                    throw new ProviderException(e2.getMessage());
                }
            }
            return algorithmParameters;
        }

        private void ensureInit() throws SignatureException {
            if (this.privateKey == null && this.publicKey == null && this.fallbackSignature == null) {
                throw new SignatureException("Missing key");
            }
            if (this.pssParams == null) {
                throw new SignatureException("Parameters required for RSASSA-PSS signatures");
            }
            if (this.fallbackSignature == null && this.messageDigest == null) {
                try {
                    this.messageDigest = MessageDigest.getInstance(this.pssParams.getDigestAlgorithm());
                } catch (NoSuchAlgorithmException e2) {
                    throw new SignatureException(e2);
                }
            }
        }

        private PSSParameterSpec validateSigParams(AlgorithmParameterSpec algorithmParameterSpec) throws InvalidAlgorithmParameterException {
            if (algorithmParameterSpec == null) {
                throw new InvalidAlgorithmParameterException("Parameters cannot be null");
            }
            if (!(algorithmParameterSpec instanceof PSSParameterSpec)) {
                throw new InvalidAlgorithmParameterException("parameters must be type PSSParameterSpec");
            }
            PSSParameterSpec pSSParameterSpec = (PSSParameterSpec) algorithmParameterSpec;
            if (pSSParameterSpec == this.pssParams) {
                return pSSParameterSpec;
            }
            if (!pSSParameterSpec.getMGFAlgorithm().equalsIgnoreCase("MGF1")) {
                throw new InvalidAlgorithmParameterException("Only supports MGF1");
            }
            if (pSSParameterSpec.getTrailerField() != 1) {
                throw new InvalidAlgorithmParameterException("Only supports TrailerFieldBC(1)");
            }
            AlgorithmParameterSpec mGFParameters = pSSParameterSpec.getMGFParameters();
            if (!(mGFParameters instanceof MGF1ParameterSpec)) {
                throw new InvalidAlgorithmParameterException("Only support MGF1ParameterSpec");
            }
            MGF1ParameterSpec mGF1ParameterSpec = (MGF1ParameterSpec) mGFParameters;
            String strReplaceAll = pSSParameterSpec.getDigestAlgorithm().toLowerCase(Locale.ROOT).replaceAll(LanguageTag.SEP, "");
            if (strReplaceAll.equals("sha")) {
                strReplaceAll = "sha1";
            }
            String strReplaceAll2 = mGF1ParameterSpec.getDigestAlgorithm().toLowerCase(Locale.ROOT).replaceAll(LanguageTag.SEP, "");
            if (strReplaceAll2.equals("sha")) {
                strReplaceAll2 = "sha1";
            }
            if (!strReplaceAll2.equals(strReplaceAll)) {
                throw new InvalidAlgorithmParameterException("MGF1 hash must be the same as message hash");
            }
            return pSSParameterSpec;
        }
    }

    protected void resetDigest() {
        if (this.needsReset) {
            if (this.messageDigest != null) {
                this.messageDigest.reset();
            }
            this.needsReset = false;
        }
    }

    protected byte[] getDigestValue() throws SignatureException {
        this.needsReset = false;
        return this.messageDigest.digest();
    }

    protected void setDigestName(String str) {
        this.messageDigestAlgorithm = str;
    }

    @Override // java.security.SignatureSpi
    protected void engineUpdate(byte b2) throws SignatureException {
        this.messageDigest.update(b2);
        this.needsReset = true;
    }

    @Override // java.security.SignatureSpi
    protected void engineUpdate(byte[] bArr, int i2, int i3) throws SignatureException {
        this.messageDigest.update(bArr, i2, i3);
        this.needsReset = true;
    }

    @Override // java.security.SignatureSpi
    protected void engineUpdate(ByteBuffer byteBuffer) {
        this.messageDigest.update(byteBuffer);
        this.needsReset = true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static byte[] convertEndianArray(byte[] bArr) {
        if (bArr == null || bArr.length == 0) {
            return bArr;
        }
        byte[] bArr2 = new byte[bArr.length];
        for (int i2 = 0; i2 < bArr.length; i2++) {
            bArr2[i2] = bArr[(bArr.length - i2) - 1];
        }
        return bArr2;
    }

    @Override // java.security.SignatureSpi
    @Deprecated
    protected void engineSetParameter(String str, Object obj) throws InvalidParameterException {
        throw new InvalidParameterException("Parameter not supported");
    }

    @Override // java.security.SignatureSpi
    protected void engineSetParameter(AlgorithmParameterSpec algorithmParameterSpec) throws InvalidAlgorithmParameterException {
        if (algorithmParameterSpec != null) {
            throw new InvalidAlgorithmParameterException("No parameter accepted");
        }
    }

    @Override // java.security.SignatureSpi
    @Deprecated
    protected Object engineGetParameter(String str) throws InvalidParameterException {
        throw new InvalidParameterException("Parameter not supported");
    }

    @Override // java.security.SignatureSpi
    protected AlgorithmParameters engineGetParameters() {
        return null;
    }
}
