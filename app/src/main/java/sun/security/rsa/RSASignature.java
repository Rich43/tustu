package sun.security.rsa;

import java.io.IOException;
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
import java.security.interfaces.RSAKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.AlgorithmParameterSpec;
import javax.crypto.BadPaddingException;
import sun.security.rsa.RSAUtil;
import sun.security.util.DerInputStream;
import sun.security.util.DerOutputStream;
import sun.security.util.DerValue;
import sun.security.util.ObjectIdentifier;
import sun.security.x509.AlgorithmId;

/* loaded from: rt.jar:sun/security/rsa/RSASignature.class */
public abstract class RSASignature extends SignatureSpi {
    private static final int baseLength = 8;
    private final ObjectIdentifier digestOID;
    private final int encodedLength;
    private final MessageDigest md;
    private boolean digestReset;
    private RSAPrivateKey privateKey;
    private RSAPublicKey publicKey;
    private RSAPadding padding;

    RSASignature(String str, ObjectIdentifier objectIdentifier, int i2) {
        this.digestOID = objectIdentifier;
        try {
            this.md = MessageDigest.getInstance(str);
            this.digestReset = true;
            this.encodedLength = 8 + i2 + this.md.getDigestLength();
        } catch (NoSuchAlgorithmException e2) {
            throw new ProviderException(e2);
        }
    }

    @Override // java.security.SignatureSpi
    protected void engineInitVerify(PublicKey publicKey) throws InvalidKeyException {
        RSAPublicKey rSAPublicKey = (RSAPublicKey) RSAKeyFactory.toRSAKey(publicKey);
        this.privateKey = null;
        this.publicKey = rSAPublicKey;
        initCommon(rSAPublicKey, null);
    }

    @Override // java.security.SignatureSpi
    protected void engineInitSign(PrivateKey privateKey) throws InvalidKeyException {
        engineInitSign(privateKey, null);
    }

    @Override // java.security.SignatureSpi
    protected void engineInitSign(PrivateKey privateKey, SecureRandom secureRandom) throws InvalidKeyException {
        RSAPrivateKey rSAPrivateKey = (RSAPrivateKey) RSAKeyFactory.toRSAKey(privateKey);
        this.privateKey = rSAPrivateKey;
        this.publicKey = null;
        initCommon(rSAPrivateKey, secureRandom);
    }

    private void initCommon(RSAKey rSAKey, SecureRandom secureRandom) throws InvalidKeyException {
        try {
            RSAUtil.checkParamsAgainstType(RSAUtil.KeyType.RSA, rSAKey.getParams());
            resetDigest();
            try {
                this.padding = RSAPadding.getInstance(1, RSACore.getByteLength(rSAKey), secureRandom);
                if (this.encodedLength > this.padding.getMaxDataSize()) {
                    throw new InvalidKeyException("Key is too short for this signature algorithm");
                }
            } catch (InvalidAlgorithmParameterException e2) {
                throw new InvalidKeyException(e2.getMessage());
            }
        } catch (ProviderException e3) {
            throw new InvalidKeyException("Invalid key for RSA signatures", e3);
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
        this.md.update(b2);
        this.digestReset = false;
    }

    @Override // java.security.SignatureSpi
    protected void engineUpdate(byte[] bArr, int i2, int i3) throws SignatureException {
        this.md.update(bArr, i2, i3);
        this.digestReset = false;
    }

    @Override // java.security.SignatureSpi
    protected void engineUpdate(ByteBuffer byteBuffer) {
        this.md.update(byteBuffer);
        this.digestReset = false;
    }

    @Override // java.security.SignatureSpi
    protected byte[] engineSign() throws SignatureException {
        if (this.privateKey == null) {
            throw new SignatureException("Missing private key");
        }
        try {
            return RSACore.rsa(this.padding.pad(encodeSignature(this.digestOID, getDigestValue())), this.privateKey, true);
        } catch (IOException e2) {
            throw new SignatureException("Could not encode data", e2);
        } catch (GeneralSecurityException e3) {
            throw new SignatureException("Could not sign data", e3);
        }
    }

    @Override // java.security.SignatureSpi
    protected boolean engineVerify(byte[] bArr) throws SignatureException {
        if (this.publicKey == null) {
            throw new SignatureException("Missing public key");
        }
        if (bArr.length != RSACore.getByteLength(this.publicKey)) {
            throw new SignatureException("Signature length not correct: got " + bArr.length + " but was expecting " + RSACore.getByteLength(this.publicKey));
        }
        try {
            return MessageDigest.isEqual(getDigestValue(), decodeSignature(this.digestOID, this.padding.unpad(RSACore.rsa(bArr, this.publicKey))));
        } catch (IOException e2) {
            throw new SignatureException("Signature encoding error", e2);
        } catch (BadPaddingException e3) {
            return false;
        }
    }

    public static byte[] encodeSignature(ObjectIdentifier objectIdentifier, byte[] bArr) throws IOException {
        DerOutputStream derOutputStream = new DerOutputStream();
        new AlgorithmId(objectIdentifier).encode(derOutputStream);
        derOutputStream.putOctetString(bArr);
        return new DerValue((byte) 48, derOutputStream.toByteArray()).toByteArray();
    }

    public static byte[] decodeSignature(ObjectIdentifier objectIdentifier, byte[] bArr) throws IOException {
        DerInputStream derInputStream = new DerInputStream(bArr, 0, bArr.length, false);
        DerValue[] sequence = derInputStream.getSequence(2);
        if (sequence.length != 2 || derInputStream.available() != 0) {
            throw new IOException("SEQUENCE length error");
        }
        AlgorithmId algorithmId = AlgorithmId.parse(sequence[0]);
        if (!algorithmId.getOID().equals((Object) objectIdentifier)) {
            throw new IOException("ObjectIdentifier mismatch: " + ((Object) algorithmId.getOID()));
        }
        if (algorithmId.getEncodedParams() != null) {
            throw new IOException("Unexpected AlgorithmId parameters");
        }
        return sequence[1].getOctetString();
    }

    @Override // java.security.SignatureSpi
    @Deprecated
    protected void engineSetParameter(String str, Object obj) throws InvalidParameterException {
        throw new UnsupportedOperationException("setParameter() not supported");
    }

    @Override // java.security.SignatureSpi
    protected void engineSetParameter(AlgorithmParameterSpec algorithmParameterSpec) throws InvalidAlgorithmParameterException {
        if (algorithmParameterSpec != null) {
            throw new InvalidAlgorithmParameterException("No parameters accepted");
        }
    }

    @Override // java.security.SignatureSpi
    @Deprecated
    protected Object engineGetParameter(String str) throws InvalidParameterException {
        throw new UnsupportedOperationException("getParameter() not supported");
    }

    @Override // java.security.SignatureSpi
    protected AlgorithmParameters engineGetParameters() {
        return null;
    }

    /* loaded from: rt.jar:sun/security/rsa/RSASignature$MD2withRSA.class */
    public static final class MD2withRSA extends RSASignature {
        public MD2withRSA() {
            super("MD2", AlgorithmId.MD2_oid, 10);
        }
    }

    /* loaded from: rt.jar:sun/security/rsa/RSASignature$MD5withRSA.class */
    public static final class MD5withRSA extends RSASignature {
        public MD5withRSA() {
            super("MD5", AlgorithmId.MD5_oid, 10);
        }
    }

    /* loaded from: rt.jar:sun/security/rsa/RSASignature$SHA1withRSA.class */
    public static final class SHA1withRSA extends RSASignature {
        public SHA1withRSA() {
            super("SHA-1", AlgorithmId.SHA_oid, 7);
        }
    }

    /* loaded from: rt.jar:sun/security/rsa/RSASignature$SHA224withRSA.class */
    public static final class SHA224withRSA extends RSASignature {
        public SHA224withRSA() {
            super("SHA-224", AlgorithmId.SHA224_oid, 11);
        }
    }

    /* loaded from: rt.jar:sun/security/rsa/RSASignature$SHA256withRSA.class */
    public static final class SHA256withRSA extends RSASignature {
        public SHA256withRSA() {
            super("SHA-256", AlgorithmId.SHA256_oid, 11);
        }
    }

    /* loaded from: rt.jar:sun/security/rsa/RSASignature$SHA384withRSA.class */
    public static final class SHA384withRSA extends RSASignature {
        public SHA384withRSA() {
            super("SHA-384", AlgorithmId.SHA384_oid, 11);
        }
    }

    /* loaded from: rt.jar:sun/security/rsa/RSASignature$SHA512withRSA.class */
    public static final class SHA512withRSA extends RSASignature {
        public SHA512withRSA() {
            super("SHA-512", AlgorithmId.SHA512_oid, 11);
        }
    }

    /* loaded from: rt.jar:sun/security/rsa/RSASignature$SHA512_224withRSA.class */
    public static final class SHA512_224withRSA extends RSASignature {
        public SHA512_224withRSA() {
            super("SHA-512/224", AlgorithmId.SHA512_224_oid, 11);
        }
    }

    /* loaded from: rt.jar:sun/security/rsa/RSASignature$SHA512_256withRSA.class */
    public static final class SHA512_256withRSA extends RSASignature {
        public SHA512_256withRSA() {
            super("SHA-512/256", AlgorithmId.SHA512_256_oid, 11);
        }
    }
}
