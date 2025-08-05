package sun.security.rsa;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.security.AlgorithmParameters;
import java.security.DigestException;
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
import java.security.interfaces.RSAKey;
import java.security.interfaces.RSAPrivateCrtKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.MGF1ParameterSpec;
import java.security.spec.PSSParameterSpec;
import java.util.Arrays;
import java.util.Hashtable;
import javax.crypto.BadPaddingException;
import sun.security.jca.JCAUtil;
import sun.util.locale.LanguageTag;

/* loaded from: rt.jar:sun/security/rsa/RSAPSSSignature.class */
public class RSAPSSSignature extends SignatureSpi {
    private static final boolean DEBUG = false;
    private static final byte[] EIGHT_BYTES_OF_ZEROS = new byte[8];
    private static final Hashtable<String, Integer> DIGEST_LENGTHS = new Hashtable<>();
    private SecureRandom random;
    private boolean digestReset = true;
    private RSAPrivateKey privKey = null;
    private RSAPublicKey pubKey = null;
    private PSSParameterSpec sigParams = null;
    private MessageDigest md = null;

    private boolean isDigestEqual(String str, String str2) {
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

    @Override // java.security.SignatureSpi
    protected void engineInitVerify(PublicKey publicKey) throws InvalidKeyException {
        if (publicKey instanceof RSAPublicKey) {
            RSAPublicKey rSAPublicKey = (RSAPublicKey) publicKey;
            isPublicKeyValid(rSAPublicKey);
            this.pubKey = rSAPublicKey;
            this.privKey = null;
            resetDigest();
            return;
        }
        throw new InvalidKeyException("key must be RSAPublicKey");
    }

    @Override // java.security.SignatureSpi
    protected void engineInitSign(PrivateKey privateKey) throws InvalidKeyException {
        engineInitSign(privateKey, null);
    }

    @Override // java.security.SignatureSpi
    protected void engineInitSign(PrivateKey privateKey, SecureRandom secureRandom) throws InvalidKeyException {
        if (privateKey instanceof RSAPrivateKey) {
            RSAPrivateKey rSAPrivateKey = (RSAPrivateKey) privateKey;
            isPrivateKeyValid(rSAPrivateKey);
            this.privKey = rSAPrivateKey;
            this.pubKey = null;
            this.random = secureRandom == null ? JCAUtil.getSecureRandom() : secureRandom;
            resetDigest();
            return;
        }
        throw new InvalidKeyException("key must be RSAPrivateKey");
    }

    private static boolean isCompatible(AlgorithmParameterSpec algorithmParameterSpec, PSSParameterSpec pSSParameterSpec) {
        if (algorithmParameterSpec == null) {
            return true;
        }
        if (!(algorithmParameterSpec instanceof PSSParameterSpec)) {
            return false;
        }
        if (pSSParameterSpec == null) {
            return true;
        }
        PSSParameterSpec pSSParameterSpec2 = (PSSParameterSpec) algorithmParameterSpec;
        if (pSSParameterSpec2.getSaltLength() > pSSParameterSpec.getSaltLength()) {
            return false;
        }
        PSSParameterSpec pSSParameterSpec3 = new PSSParameterSpec(pSSParameterSpec2.getDigestAlgorithm(), pSSParameterSpec2.getMGFAlgorithm(), pSSParameterSpec2.getMGFParameters(), pSSParameterSpec.getSaltLength(), pSSParameterSpec2.getTrailerField());
        PSSParameters pSSParameters = new PSSParameters();
        try {
            pSSParameters.engineInit(pSSParameterSpec3);
            byte[] bArrEngineGetEncoded = pSSParameters.engineGetEncoded();
            pSSParameters.engineInit(pSSParameterSpec);
            return Arrays.equals(bArrEngineGetEncoded, pSSParameters.engineGetEncoded());
        } catch (Exception e2) {
            return false;
        }
    }

    private void isPrivateKeyValid(RSAPrivateKey rSAPrivateKey) throws InvalidKeyException {
        try {
            if (rSAPrivateKey instanceof RSAPrivateCrtKey) {
                RSAPrivateCrtKey rSAPrivateCrtKey = (RSAPrivateCrtKey) rSAPrivateKey;
                if (RSAPrivateCrtKeyImpl.checkComponents(rSAPrivateCrtKey)) {
                    RSAKeyFactory.checkRSAProviderKeyLengths(rSAPrivateCrtKey.getModulus().bitLength(), rSAPrivateCrtKey.getPublicExponent());
                } else {
                    throw new InvalidKeyException("Some of the CRT-specific components are not available");
                }
            } else {
                RSAKeyFactory.checkRSAProviderKeyLengths(rSAPrivateKey.getModulus().bitLength(), null);
            }
            isValid(rSAPrivateKey);
        } catch (InvalidKeyException e2) {
            throw e2;
        } catch (Exception e3) {
            throw new InvalidKeyException("Can not access private key components", e3);
        }
    }

    private void isPublicKeyValid(RSAPublicKey rSAPublicKey) throws InvalidKeyException {
        try {
            RSAKeyFactory.checkRSAProviderKeyLengths(rSAPublicKey.getModulus().bitLength(), rSAPublicKey.getPublicExponent());
            isValid(rSAPublicKey);
        } catch (InvalidKeyException e2) {
            throw e2;
        } catch (Exception e3) {
            throw new InvalidKeyException("Can not access public key components", e3);
        }
    }

    private void isValid(RSAKey rSAKey) throws InvalidKeyException {
        try {
            rSAKey.getParams();
            if (!isCompatible(rSAKey.getParams(), this.sigParams)) {
                throw new InvalidKeyException("Key contains incompatible PSS parameter values");
            }
            if (this.sigParams != null) {
                Integer num = DIGEST_LENGTHS.get(this.sigParams.getDigestAlgorithm());
                if (num == null) {
                    throw new ProviderException("Unsupported digest algo: " + this.sigParams.getDigestAlgorithm());
                }
                checkKeyLength(rSAKey, num.intValue(), this.sigParams.getSaltLength());
            }
        } catch (SignatureException e2) {
            throw new InvalidKeyException(e2);
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
        if (pSSParameterSpec == this.sigParams) {
            return pSSParameterSpec;
        }
        RSAKey rSAKey = this.privKey == null ? this.pubKey : this.privKey;
        if (rSAKey != null && !isCompatible(rSAKey.getParams(), pSSParameterSpec)) {
            throw new InvalidAlgorithmParameterException("Signature parameters does not match key parameters");
        }
        if (!pSSParameterSpec.getMGFAlgorithm().equalsIgnoreCase("MGF1")) {
            throw new InvalidAlgorithmParameterException("Only supports MGF1");
        }
        if (pSSParameterSpec.getTrailerField() != 1) {
            throw new InvalidAlgorithmParameterException("Only supports TrailerFieldBC(1)");
        }
        String digestAlgorithm = pSSParameterSpec.getDigestAlgorithm();
        if (rSAKey != null) {
            try {
                checkKeyLength(rSAKey, DIGEST_LENGTHS.get(digestAlgorithm).intValue(), pSSParameterSpec.getSaltLength());
            } catch (SignatureException e2) {
                throw new InvalidAlgorithmParameterException(e2);
            }
        }
        return pSSParameterSpec;
    }

    private void ensureInit() throws SignatureException {
        if ((this.privKey == null ? this.pubKey : this.privKey) == null) {
            throw new SignatureException("Missing key");
        }
        if (this.sigParams == null) {
            throw new SignatureException("Parameters required for RSASSA-PSS signatures");
        }
    }

    private static void checkKeyLength(RSAKey rSAKey, int i2, int i3) throws SignatureException {
        if (rSAKey != null) {
            int keyLengthInBits = (getKeyLengthInBits(rSAKey) + 7) >> 3;
            int iAddExact = Math.addExact(Math.addExact(i2, i3), 2);
            if (keyLengthInBits < iAddExact) {
                throw new SignatureException("Key is too short, need min " + iAddExact + " bytes");
            }
        }
    }

    private void resetDigest() {
        if (!this.digestReset) {
            this.md.reset();
            this.digestReset = true;
        }
    }

    private byte[] getDigestValue() {
        this.digestReset = true;
        return this.md.digest();
    }

    @Override // java.security.SignatureSpi
    protected void engineUpdate(byte b2) throws SignatureException {
        ensureInit();
        this.md.update(b2);
        this.digestReset = false;
    }

    @Override // java.security.SignatureSpi
    protected void engineUpdate(byte[] bArr, int i2, int i3) throws SignatureException {
        ensureInit();
        this.md.update(bArr, i2, i3);
        this.digestReset = false;
    }

    @Override // java.security.SignatureSpi
    protected void engineUpdate(ByteBuffer byteBuffer) {
        try {
            ensureInit();
            this.md.update(byteBuffer);
            this.digestReset = false;
        } catch (SignatureException e2) {
            throw new RuntimeException(e2.getMessage());
        }
    }

    @Override // java.security.SignatureSpi
    protected byte[] engineSign() throws SignatureException {
        ensureInit();
        try {
            return RSACore.rsa(encodeSignature(getDigestValue()), this.privKey, true);
        } catch (IOException e2) {
            throw new SignatureException("Could not encode data", e2);
        } catch (GeneralSecurityException e3) {
            throw new SignatureException("Could not sign data", e3);
        }
    }

    @Override // java.security.SignatureSpi
    protected boolean engineVerify(byte[] bArr) throws SignatureException {
        ensureInit();
        try {
            try {
                if (bArr.length != RSACore.getByteLength(this.pubKey)) {
                    throw new SignatureException("Signature length not correct: got " + bArr.length + " but was expecting " + RSACore.getByteLength(this.pubKey));
                }
                boolean zDecodeSignature = decodeSignature(getDigestValue(), RSACore.rsa(bArr, this.pubKey));
                resetDigest();
                return zDecodeSignature;
            } catch (IOException e2) {
                throw new SignatureException("Signature encoding error", e2);
            } catch (BadPaddingException e3) {
                resetDigest();
                return false;
            }
        } catch (Throwable th) {
            resetDigest();
            throw th;
        }
    }

    private static int getKeyLengthInBits(RSAKey rSAKey) {
        if (rSAKey != null) {
            return rSAKey.getModulus().bitLength();
        }
        return -1;
    }

    private byte[] encodeSignature(byte[] bArr) throws DigestException, IOException, RuntimeException {
        String algorithm;
        AlgorithmParameterSpec mGFParameters = this.sigParams.getMGFParameters();
        if (mGFParameters != null) {
            algorithm = ((MGF1ParameterSpec) mGFParameters).getDigestAlgorithm();
        } else {
            algorithm = this.md.getAlgorithm();
        }
        try {
            int keyLengthInBits = getKeyLengthInBits(this.privKey) - 1;
            int i2 = (keyLengthInBits + 7) >> 3;
            int digestLength = this.md.getDigestLength();
            int i3 = (i2 - digestLength) - 1;
            int saltLength = this.sigParams.getSaltLength();
            byte[] bArr2 = new byte[i2];
            bArr2[(i3 - saltLength) - 1] = 1;
            bArr2[bArr2.length - 1] = -68;
            if (!this.digestReset) {
                throw new ProviderException("Digest should be reset");
            }
            this.md.update(EIGHT_BYTES_OF_ZEROS);
            this.digestReset = false;
            this.md.update(bArr);
            if (saltLength != 0) {
                byte[] bArr3 = new byte[saltLength];
                this.random.nextBytes(bArr3);
                this.md.update(bArr3);
                System.arraycopy(bArr3, 0, bArr2, i3 - saltLength, saltLength);
            }
            this.md.digest(bArr2, i3, digestLength);
            this.digestReset = true;
            new MGF1(algorithm).generateAndXor(bArr2, i3, digestLength, i3, bArr2, 0);
            int i4 = (i2 << 3) - keyLengthInBits;
            if (i4 != 0) {
                bArr2[0] = (byte) (bArr2[0] & ((byte) (255 >>> i4)));
            }
            return bArr2;
        } catch (NoSuchAlgorithmException e2) {
            throw new IOException(e2.toString());
        }
    }

    private boolean decodeSignature(byte[] bArr, byte[] bArr2) throws IOException, RuntimeException {
        String algorithm;
        int length = bArr.length;
        int saltLength = this.sigParams.getSaltLength();
        int keyLengthInBits = getKeyLengthInBits(this.pubKey) - 1;
        int i2 = (keyLengthInBits + 7) >> 3;
        int length2 = bArr2.length - i2;
        if ((length2 == 1 && bArr2[0] != 0) || i2 < length + saltLength + 2 || bArr2[(length2 + i2) - 1] != -68) {
            return false;
        }
        int i3 = (i2 << 3) - keyLengthInBits;
        if (i3 != 0) {
            if ((bArr2[length2] & ((byte) (255 << (8 - i3)))) != 0) {
                return false;
            }
        }
        AlgorithmParameterSpec mGFParameters = this.sigParams.getMGFParameters();
        if (mGFParameters != null) {
            algorithm = ((MGF1ParameterSpec) mGFParameters).getDigestAlgorithm();
        } else {
            algorithm = this.md.getAlgorithm();
        }
        int i4 = (i2 - length) - 1;
        try {
            new MGF1(algorithm).generateAndXor(bArr2, length2 + i4, length, i4, bArr2, length2);
            if (i3 != 0) {
                bArr2[length2] = (byte) (bArr2[length2] & ((byte) (255 >>> i3)));
            }
            int i5 = length2;
            while (i5 < length2 + ((i4 - saltLength) - 1)) {
                if (bArr2[i5] == 0) {
                    i5++;
                } else {
                    return false;
                }
            }
            if (bArr2[i5] != 1) {
                return false;
            }
            this.md.update(EIGHT_BYTES_OF_ZEROS);
            this.digestReset = false;
            this.md.update(bArr);
            if (saltLength > 0) {
                this.md.update(bArr2, length2 + (i4 - saltLength), saltLength);
            }
            byte[] bArrDigest = this.md.digest();
            this.digestReset = true;
            return MessageDigest.isEqual(bArrDigest, Arrays.copyOfRange(bArr2, length2 + i4, (length2 + i2) - 1));
        } catch (NoSuchAlgorithmException e2) {
            throw new IOException(e2.toString());
        }
    }

    @Override // java.security.SignatureSpi
    @Deprecated
    protected void engineSetParameter(String str, Object obj) throws InvalidParameterException {
        throw new UnsupportedOperationException("setParameter() not supported");
    }

    @Override // java.security.SignatureSpi
    protected void engineSetParameter(AlgorithmParameterSpec algorithmParameterSpec) throws InvalidAlgorithmParameterException {
        this.sigParams = validateSigParams(algorithmParameterSpec);
        if (!this.digestReset) {
            throw new ProviderException("Cannot set parameters during operations");
        }
        String digestAlgorithm = this.sigParams.getDigestAlgorithm();
        if (this.md == null || !this.md.getAlgorithm().equalsIgnoreCase(digestAlgorithm)) {
            try {
                this.md = MessageDigest.getInstance(digestAlgorithm);
            } catch (NoSuchAlgorithmException e2) {
                throw new InvalidAlgorithmParameterException("Unsupported digest algorithm " + digestAlgorithm, e2);
            }
        }
    }

    @Override // java.security.SignatureSpi
    @Deprecated
    protected Object engineGetParameter(String str) throws InvalidParameterException {
        throw new UnsupportedOperationException("getParameter() not supported");
    }

    @Override // java.security.SignatureSpi
    protected AlgorithmParameters engineGetParameters() {
        AlgorithmParameters algorithmParameters = null;
        if (this.sigParams != null) {
            try {
                algorithmParameters = AlgorithmParameters.getInstance("RSASSA-PSS");
                algorithmParameters.init(this.sigParams);
            } catch (GeneralSecurityException e2) {
                throw new ProviderException(e2.getMessage());
            }
        }
        return algorithmParameters;
    }
}
