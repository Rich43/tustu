package sun.security.rsa;

import java.io.IOException;
import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.interfaces.RSAPrivateKey;
import java.security.spec.AlgorithmParameterSpec;
import sun.security.pkcs.PKCS8Key;
import sun.security.util.DerOutputStream;
import sun.security.util.DerValue;
import sun.security.x509.AlgorithmId;

/* loaded from: rt.jar:sun/security/rsa/RSAPrivateKeyImpl.class */
public final class RSAPrivateKeyImpl extends PKCS8Key implements RSAPrivateKey {
    private static final long serialVersionUID = -33106691987952810L;

    /* renamed from: n, reason: collision with root package name */
    private final BigInteger f13657n;

    /* renamed from: d, reason: collision with root package name */
    private final BigInteger f13658d;
    private final AlgorithmParameterSpec keyParams;

    RSAPrivateKeyImpl(AlgorithmId algorithmId, BigInteger bigInteger, BigInteger bigInteger2) throws InvalidKeyException {
        RSAKeyFactory.checkRSAProviderKeyLengths(bigInteger.bitLength(), null);
        this.f13657n = bigInteger;
        this.f13658d = bigInteger2;
        this.keyParams = RSAUtil.getParamSpec(algorithmId);
        this.algid = algorithmId;
        try {
            DerOutputStream derOutputStream = new DerOutputStream();
            derOutputStream.putInteger(0);
            derOutputStream.putInteger(bigInteger);
            derOutputStream.putInteger(0);
            derOutputStream.putInteger(bigInteger2);
            derOutputStream.putInteger(0);
            derOutputStream.putInteger(0);
            derOutputStream.putInteger(0);
            derOutputStream.putInteger(0);
            derOutputStream.putInteger(0);
            this.key = new DerValue((byte) 48, derOutputStream.toByteArray()).toByteArray();
        } catch (IOException e2) {
            throw new InvalidKeyException(e2);
        }
    }

    @Override // sun.security.pkcs.PKCS8Key, java.security.Key
    public String getAlgorithm() {
        return this.algid.getName();
    }

    @Override // java.security.interfaces.RSAKey
    public BigInteger getModulus() {
        return this.f13657n;
    }

    @Override // java.security.interfaces.RSAPrivateKey
    public BigInteger getPrivateExponent() {
        return this.f13658d;
    }

    @Override // java.security.interfaces.RSAKey
    public AlgorithmParameterSpec getParams() {
        return this.keyParams;
    }

    public String toString() {
        return "Sun " + getAlgorithm() + " private key, " + this.f13657n.bitLength() + " bits\n  params: " + ((Object) this.keyParams) + "\n  modulus: " + ((Object) this.f13657n) + "\n  private exponent: " + ((Object) this.f13658d);
    }
}
