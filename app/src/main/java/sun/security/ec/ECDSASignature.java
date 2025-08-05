package sun.security.ec;

import java.nio.ByteBuffer;
import java.security.AlgorithmParameters;
import java.security.GeneralSecurityException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.InvalidParameterException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.ProviderException;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.SignatureException;
import java.security.SignatureSpi;
import java.security.interfaces.ECKey;
import java.security.interfaces.ECPrivateKey;
import java.security.interfaces.ECPublicKey;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.ECParameterSpec;
import java.util.Optional;
import sun.security.ec.ECDSAOperations;
import sun.security.ec.ECOperations;
import sun.security.jca.JCAUtil;
import sun.security.util.ECUtil;

/* loaded from: sunec.jar:sun/security/ec/ECDSASignature.class */
abstract class ECDSASignature extends SignatureSpi {
    private final MessageDigest messageDigest;
    private SecureRandom random;
    private boolean needsReset;
    private ECPrivateKey privateKey;
    private ECPublicKey publicKey;
    private ECParameterSpec sigParams;

    private static native byte[] signDigest(byte[] bArr, byte[] bArr2, byte[] bArr3, byte[] bArr4, int i2) throws GeneralSecurityException;

    private static native boolean verifySignedDigest(byte[] bArr, byte[] bArr2, byte[] bArr3, byte[] bArr4) throws GeneralSecurityException;

    ECDSASignature() {
        this.sigParams = null;
        this.messageDigest = null;
    }

    ECDSASignature(String str) {
        this.sigParams = null;
        try {
            this.messageDigest = MessageDigest.getInstance(str);
            this.needsReset = false;
        } catch (NoSuchAlgorithmException e2) {
            throw new ProviderException(e2);
        }
    }

    /* loaded from: sunec.jar:sun/security/ec/ECDSASignature$Raw.class */
    public static final class Raw extends ECDSASignature {
        private static final int RAW_ECDSA_MAX = 64;
        private int offset = 0;
        private final byte[] precomputedDigest = new byte[64];

        @Override // sun.security.ec.ECDSASignature, java.security.SignatureSpi
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

        @Override // sun.security.ec.ECDSASignature, java.security.SignatureSpi
        protected void engineUpdate(byte[] bArr, int i2, int i3) throws SignatureException {
            if (this.offset >= this.precomputedDigest.length) {
                this.offset = 65;
            } else {
                System.arraycopy(bArr, i2, this.precomputedDigest, this.offset, i3);
                this.offset += i3;
            }
        }

        @Override // sun.security.ec.ECDSASignature, java.security.SignatureSpi
        protected void engineUpdate(ByteBuffer byteBuffer) {
            int iRemaining = byteBuffer.remaining();
            if (iRemaining <= 0) {
                return;
            }
            if (this.offset + iRemaining >= this.precomputedDigest.length) {
                this.offset = 65;
            } else {
                byteBuffer.get(this.precomputedDigest, this.offset, iRemaining);
                this.offset += iRemaining;
            }
        }

        @Override // sun.security.ec.ECDSASignature
        protected void resetDigest() {
            this.offset = 0;
        }

        @Override // sun.security.ec.ECDSASignature
        protected byte[] getDigestValue() throws SignatureException {
            if (this.offset > 64) {
                throw new SignatureException("Message digest is too long");
            }
            byte[] bArr = new byte[this.offset];
            System.arraycopy(this.precomputedDigest, 0, bArr, 0, this.offset);
            this.offset = 0;
            return bArr;
        }
    }

    /* loaded from: sunec.jar:sun/security/ec/ECDSASignature$SHA1.class */
    public static final class SHA1 extends ECDSASignature {
        public SHA1() {
            super("SHA1");
        }
    }

    /* loaded from: sunec.jar:sun/security/ec/ECDSASignature$SHA224.class */
    public static final class SHA224 extends ECDSASignature {
        public SHA224() {
            super("SHA-224");
        }
    }

    /* loaded from: sunec.jar:sun/security/ec/ECDSASignature$SHA256.class */
    public static final class SHA256 extends ECDSASignature {
        public SHA256() {
            super("SHA-256");
        }
    }

    /* loaded from: sunec.jar:sun/security/ec/ECDSASignature$SHA384.class */
    public static final class SHA384 extends ECDSASignature {
        public SHA384() {
            super("SHA-384");
        }
    }

    /* loaded from: sunec.jar:sun/security/ec/ECDSASignature$SHA512.class */
    public static final class SHA512 extends ECDSASignature {
        public SHA512() {
            super("SHA-512");
        }
    }

    @Override // java.security.SignatureSpi
    protected void engineInitVerify(PublicKey publicKey) throws InvalidKeyException {
        ECPublicKey eCPublicKey = (ECPublicKey) ECKeyFactory.toECKey(publicKey);
        if (!isCompatible(this.sigParams, eCPublicKey.getParams())) {
            throw new InvalidKeyException("Key params does not match signature params");
        }
        this.publicKey = eCPublicKey;
        this.privateKey = null;
        resetDigest();
    }

    @Override // java.security.SignatureSpi
    protected void engineInitSign(PrivateKey privateKey) throws InvalidKeyException {
        engineInitSign(privateKey, null);
    }

    @Override // java.security.SignatureSpi
    protected void engineInitSign(PrivateKey privateKey, SecureRandom secureRandom) throws InvalidKeyException {
        ECPrivateKey eCPrivateKey = (ECPrivateKey) ECKeyFactory.toECKey(privateKey);
        if (!isCompatible(this.sigParams, eCPrivateKey.getParams())) {
            throw new InvalidKeyException("Key params does not match signature params");
        }
        this.privateKey = eCPrivateKey;
        this.publicKey = null;
        this.random = secureRandom;
        resetDigest();
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
        if (byteBuffer.remaining() <= 0) {
            return;
        }
        this.messageDigest.update(byteBuffer);
        this.needsReset = true;
    }

    private static boolean isCompatible(ECParameterSpec eCParameterSpec, ECParameterSpec eCParameterSpec2) {
        if (eCParameterSpec == null) {
            return true;
        }
        return ECUtil.equals(eCParameterSpec, eCParameterSpec2);
    }

    private byte[] signDigestImpl(ECDSAOperations eCDSAOperations, int i2, byte[] bArr, ECPrivateKeyImpl eCPrivateKeyImpl, SecureRandom secureRandom) throws SignatureException {
        byte[] bArr2 = new byte[(i2 + 7) / 8];
        byte[] arrayS = eCPrivateKeyImpl.getArrayS();
        for (int i3 = 0; i3 < 128; i3++) {
            secureRandom.nextBytes(bArr2);
            try {
                return eCDSAOperations.signDigest(arrayS, bArr, new ECDSAOperations.Seed(bArr2));
            } catch (ECOperations.IntermediateValueException e2) {
            }
        }
        throw new SignatureException("Unable to produce signature after 128 attempts");
    }

    private Optional<byte[]> signDigestImpl(ECPrivateKey eCPrivateKey, byte[] bArr, SecureRandom secureRandom) throws SignatureException {
        if (!(eCPrivateKey instanceof ECPrivateKeyImpl)) {
            return Optional.empty();
        }
        ECPrivateKeyImpl eCPrivateKeyImpl = (ECPrivateKeyImpl) eCPrivateKey;
        ECParameterSpec params = eCPrivateKey.getParams();
        int iBitLength = params.getOrder().bitLength() + 64;
        Optional<ECDSAOperations> optionalForParameters = ECDSAOperations.forParameters(params);
        if (!optionalForParameters.isPresent()) {
            return Optional.empty();
        }
        return Optional.of(signDigestImpl(optionalForParameters.get(), iBitLength, bArr, eCPrivateKeyImpl, secureRandom));
    }

    private byte[] signDigestNative(ECPrivateKey eCPrivateKey, byte[] bArr, SecureRandom secureRandom) throws SignatureException {
        byte[] byteArray = eCPrivateKey.getS().toByteArray();
        ECParameterSpec params = eCPrivateKey.getParams();
        byte[] bArrEncodeECParameterSpec = ECUtil.encodeECParameterSpec(null, params);
        byte[] bArr2 = new byte[(((params.getOrder().bitLength() + 7) >> 3) + 1) * 2];
        secureRandom.nextBytes(bArr2);
        try {
            return signDigest(bArr, byteArray, bArrEncodeECParameterSpec, bArr2, secureRandom.nextInt() | 1);
        } catch (GeneralSecurityException e2) {
            throw new SignatureException("Could not sign data", e2);
        }
    }

    @Override // java.security.SignatureSpi
    protected byte[] engineSign() throws SignatureException {
        byte[] bArrSignDigestNative;
        if (this.random == null) {
            this.random = JCAUtil.getSecureRandom();
        }
        byte[] digestValue = getDigestValue();
        Optional<byte[]> optionalSignDigestImpl = signDigestImpl(this.privateKey, digestValue, this.random);
        if (optionalSignDigestImpl.isPresent()) {
            bArrSignDigestNative = optionalSignDigestImpl.get();
        } else {
            bArrSignDigestNative = signDigestNative(this.privateKey, digestValue, this.random);
        }
        return ECUtil.encodeSignature(bArrSignDigestNative);
    }

    @Override // java.security.SignatureSpi
    protected boolean engineVerify(byte[] bArr) throws SignatureException {
        byte[] bArrEncodePoint;
        ECParameterSpec params = this.publicKey.getParams();
        byte[] bArrEncodeECParameterSpec = ECUtil.encodeECParameterSpec(null, params);
        if (this.publicKey instanceof ECPublicKeyImpl) {
            bArrEncodePoint = ((ECPublicKeyImpl) this.publicKey).getEncodedPublicValue();
        } else {
            bArrEncodePoint = ECUtil.encodePoint(this.publicKey.getW(), params.getCurve());
        }
        try {
            return verifySignedDigest(ECUtil.decodeSignature(bArr), getDigestValue(), bArrEncodePoint, bArrEncodeECParameterSpec);
        } catch (GeneralSecurityException e2) {
            throw new SignatureException("Could not verify signature", e2);
        }
    }

    @Override // java.security.SignatureSpi
    @Deprecated
    protected void engineSetParameter(String str, Object obj) throws InvalidParameterException {
        throw new UnsupportedOperationException("setParameter() not supported");
    }

    @Override // java.security.SignatureSpi
    protected void engineSetParameter(AlgorithmParameterSpec algorithmParameterSpec) throws InvalidAlgorithmParameterException {
        if (algorithmParameterSpec != null && !(algorithmParameterSpec instanceof ECParameterSpec)) {
            throw new InvalidAlgorithmParameterException("No parameter accepted");
        }
        ECKey eCKey = this.privateKey == null ? this.publicKey : this.privateKey;
        if (eCKey != null && !isCompatible((ECParameterSpec) algorithmParameterSpec, eCKey.getParams())) {
            throw new InvalidAlgorithmParameterException("Signature params does not match key params");
        }
        this.sigParams = (ECParameterSpec) algorithmParameterSpec;
    }

    @Override // java.security.SignatureSpi
    @Deprecated
    protected Object engineGetParameter(String str) throws InvalidParameterException {
        throw new UnsupportedOperationException("getParameter() not supported");
    }

    @Override // java.security.SignatureSpi
    protected AlgorithmParameters engineGetParameters() {
        if (this.sigParams == null) {
            return null;
        }
        try {
            AlgorithmParameters algorithmParameters = AlgorithmParameters.getInstance("EC");
            algorithmParameters.init(this.sigParams);
            return algorithmParameters;
        } catch (Exception e2) {
            throw new ProviderException("Error retrieving EC parameters", e2);
        }
    }
}
