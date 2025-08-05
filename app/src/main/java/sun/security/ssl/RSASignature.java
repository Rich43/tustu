package sun.security.ssl;

import java.security.AlgorithmParameters;
import java.security.DigestException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.InvalidParameterException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Signature;
import java.security.SignatureException;
import java.security.SignatureSpi;
import java.security.spec.AlgorithmParameterSpec;

/* loaded from: jsse.jar:sun/security/ssl/RSASignature.class */
public final class RSASignature extends SignatureSpi {
    private final Signature rawRsa = JsseJce.getSignature("NONEwithRSA");
    private final MessageDigest mdMD5 = JsseJce.getMessageDigest("MD5");
    private final MessageDigest mdSHA = JsseJce.getMessageDigest("SHA");

    static Signature getInstance() throws NoSuchAlgorithmException {
        return JsseJce.getSignature("MD5andSHA1withRSA");
    }

    @Override // java.security.SignatureSpi
    protected void engineInitVerify(PublicKey publicKey) throws InvalidKeyException {
        if (publicKey == null) {
            throw new InvalidKeyException("Public key must not be null");
        }
        this.mdMD5.reset();
        this.mdSHA.reset();
        this.rawRsa.initVerify(publicKey);
    }

    @Override // java.security.SignatureSpi
    protected void engineInitSign(PrivateKey privateKey) throws InvalidKeyException {
        engineInitSign(privateKey, null);
    }

    @Override // java.security.SignatureSpi
    protected void engineInitSign(PrivateKey privateKey, SecureRandom secureRandom) throws InvalidKeyException {
        if (privateKey == null) {
            throw new InvalidKeyException("Private key must not be null");
        }
        this.mdMD5.reset();
        this.mdSHA.reset();
        this.rawRsa.initSign(privateKey, secureRandom);
    }

    @Override // java.security.SignatureSpi
    protected void engineUpdate(byte b2) {
        this.mdMD5.update(b2);
        this.mdSHA.update(b2);
    }

    @Override // java.security.SignatureSpi
    protected void engineUpdate(byte[] bArr, int i2, int i3) {
        this.mdMD5.update(bArr, i2, i3);
        this.mdSHA.update(bArr, i2, i3);
    }

    private byte[] getDigest() throws SignatureException {
        try {
            byte[] bArr = new byte[36];
            this.mdMD5.digest(bArr, 0, 16);
            this.mdSHA.digest(bArr, 16, 20);
            return bArr;
        } catch (DigestException e2) {
            throw new SignatureException(e2);
        }
    }

    @Override // java.security.SignatureSpi
    protected byte[] engineSign() throws SignatureException {
        this.rawRsa.update(getDigest());
        return this.rawRsa.sign();
    }

    @Override // java.security.SignatureSpi
    protected boolean engineVerify(byte[] bArr) throws SignatureException {
        return engineVerify(bArr, 0, bArr.length);
    }

    @Override // java.security.SignatureSpi
    protected boolean engineVerify(byte[] bArr, int i2, int i3) throws SignatureException {
        this.rawRsa.update(getDigest());
        return this.rawRsa.verify(bArr, i2, i3);
    }

    @Override // java.security.SignatureSpi
    protected void engineSetParameter(String str, Object obj) throws InvalidParameterException {
        throw new InvalidParameterException("Parameters not supported");
    }

    @Override // java.security.SignatureSpi
    protected void engineSetParameter(AlgorithmParameterSpec algorithmParameterSpec) throws InvalidAlgorithmParameterException {
        if (algorithmParameterSpec != null) {
            throw new InvalidAlgorithmParameterException("No parameters accepted");
        }
    }

    @Override // java.security.SignatureSpi
    protected Object engineGetParameter(String str) throws InvalidParameterException {
        throw new InvalidParameterException("Parameters not supported");
    }

    @Override // java.security.SignatureSpi
    protected AlgorithmParameters engineGetParameters() {
        return null;
    }
}
