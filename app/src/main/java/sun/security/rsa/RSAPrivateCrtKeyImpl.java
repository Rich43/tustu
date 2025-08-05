package sun.security.rsa;

import java.io.IOException;
import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.ProviderException;
import java.security.interfaces.RSAPrivateCrtKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.spec.AlgorithmParameterSpec;
import sun.security.pkcs.PKCS8Key;
import sun.security.rsa.RSAUtil;
import sun.security.util.DerInputStream;
import sun.security.util.DerOutputStream;
import sun.security.util.DerValue;
import sun.security.x509.AlgorithmId;

/* loaded from: rt.jar:sun/security/rsa/RSAPrivateCrtKeyImpl.class */
public final class RSAPrivateCrtKeyImpl extends PKCS8Key implements RSAPrivateCrtKey {
    private static final long serialVersionUID = -1326088454257084918L;

    /* renamed from: n, reason: collision with root package name */
    private BigInteger f13652n;

    /* renamed from: e, reason: collision with root package name */
    private BigInteger f13653e;

    /* renamed from: d, reason: collision with root package name */
    private BigInteger f13654d;

    /* renamed from: p, reason: collision with root package name */
    private BigInteger f13655p;

    /* renamed from: q, reason: collision with root package name */
    private BigInteger f13656q;
    private BigInteger pe;
    private BigInteger qe;
    private BigInteger coeff;
    private AlgorithmParameterSpec keyParams;

    public static RSAPrivateKey newKey(byte[] bArr) throws InvalidKeyException {
        RSAPrivateCrtKeyImpl rSAPrivateCrtKeyImpl = new RSAPrivateCrtKeyImpl(bArr);
        if (checkComponents(rSAPrivateCrtKeyImpl)) {
            return rSAPrivateCrtKeyImpl;
        }
        return new RSAPrivateKeyImpl(rSAPrivateCrtKeyImpl.algid, rSAPrivateCrtKeyImpl.getModulus(), rSAPrivateCrtKeyImpl.getPrivateExponent());
    }

    static boolean checkComponents(RSAPrivateCrtKey rSAPrivateCrtKey) {
        return (rSAPrivateCrtKey.getPublicExponent().signum() == 0 || rSAPrivateCrtKey.getPrimeExponentP().signum() == 0 || rSAPrivateCrtKey.getPrimeExponentQ().signum() == 0 || rSAPrivateCrtKey.getPrimeP().signum() == 0 || rSAPrivateCrtKey.getPrimeQ().signum() == 0 || rSAPrivateCrtKey.getCrtCoefficient().signum() == 0) ? false : true;
    }

    public static RSAPrivateKey newKey(RSAUtil.KeyType keyType, AlgorithmParameterSpec algorithmParameterSpec, BigInteger bigInteger, BigInteger bigInteger2, BigInteger bigInteger3, BigInteger bigInteger4, BigInteger bigInteger5, BigInteger bigInteger6, BigInteger bigInteger7, BigInteger bigInteger8) throws ProviderException, InvalidKeyException {
        AlgorithmId algorithmIdCreateAlgorithmId = RSAUtil.createAlgorithmId(keyType, algorithmParameterSpec);
        if (bigInteger2.signum() == 0 || bigInteger4.signum() == 0 || bigInteger5.signum() == 0 || bigInteger6.signum() == 0 || bigInteger7.signum() == 0 || bigInteger8.signum() == 0) {
            return new RSAPrivateKeyImpl(algorithmIdCreateAlgorithmId, bigInteger, bigInteger3);
        }
        return new RSAPrivateCrtKeyImpl(algorithmIdCreateAlgorithmId, bigInteger, bigInteger2, bigInteger3, bigInteger4, bigInteger5, bigInteger6, bigInteger7, bigInteger8);
    }

    RSAPrivateCrtKeyImpl(byte[] bArr) throws InvalidKeyException {
        if (bArr == null || bArr.length == 0) {
            throw new InvalidKeyException("Missing key encoding");
        }
        decode(bArr);
        RSAKeyFactory.checkRSAProviderKeyLengths(this.f13652n.bitLength(), this.f13653e);
        try {
            this.keyParams = RSAUtil.getParamSpec(this.algid);
        } catch (ProviderException e2) {
            throw new InvalidKeyException(e2);
        }
    }

    RSAPrivateCrtKeyImpl(AlgorithmId algorithmId, BigInteger bigInteger, BigInteger bigInteger2, BigInteger bigInteger3, BigInteger bigInteger4, BigInteger bigInteger5, BigInteger bigInteger6, BigInteger bigInteger7, BigInteger bigInteger8) throws InvalidKeyException {
        RSAKeyFactory.checkRSAProviderKeyLengths(bigInteger.bitLength(), bigInteger2);
        this.f13652n = bigInteger;
        this.f13653e = bigInteger2;
        this.f13654d = bigInteger3;
        this.f13655p = bigInteger4;
        this.f13656q = bigInteger5;
        this.pe = bigInteger6;
        this.qe = bigInteger7;
        this.coeff = bigInteger8;
        this.keyParams = RSAUtil.getParamSpec(algorithmId);
        this.algid = algorithmId;
        try {
            DerOutputStream derOutputStream = new DerOutputStream();
            derOutputStream.putInteger(0);
            derOutputStream.putInteger(bigInteger);
            derOutputStream.putInteger(bigInteger2);
            derOutputStream.putInteger(bigInteger3);
            derOutputStream.putInteger(bigInteger4);
            derOutputStream.putInteger(bigInteger5);
            derOutputStream.putInteger(bigInteger6);
            derOutputStream.putInteger(bigInteger7);
            derOutputStream.putInteger(bigInteger8);
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
        return this.f13652n;
    }

    @Override // java.security.interfaces.RSAPrivateCrtKey
    public BigInteger getPublicExponent() {
        return this.f13653e;
    }

    @Override // java.security.interfaces.RSAPrivateKey
    public BigInteger getPrivateExponent() {
        return this.f13654d;
    }

    @Override // java.security.interfaces.RSAPrivateCrtKey
    public BigInteger getPrimeP() {
        return this.f13655p;
    }

    @Override // java.security.interfaces.RSAPrivateCrtKey
    public BigInteger getPrimeQ() {
        return this.f13656q;
    }

    @Override // java.security.interfaces.RSAPrivateCrtKey
    public BigInteger getPrimeExponentP() {
        return this.pe;
    }

    @Override // java.security.interfaces.RSAPrivateCrtKey
    public BigInteger getPrimeExponentQ() {
        return this.qe;
    }

    @Override // java.security.interfaces.RSAPrivateCrtKey
    public BigInteger getCrtCoefficient() {
        return this.coeff;
    }

    @Override // java.security.interfaces.RSAKey
    public AlgorithmParameterSpec getParams() {
        return this.keyParams;
    }

    public String toString() {
        return "SunRsaSign " + getAlgorithm() + " private CRT key, " + this.f13652n.bitLength() + " bits\n  params: " + ((Object) this.keyParams) + "\n  modulus: " + ((Object) this.f13652n) + "\n  private exponent: " + ((Object) this.f13654d);
    }

    @Override // sun.security.pkcs.PKCS8Key
    protected void parseKeyBits() throws IOException, InvalidKeyException {
        try {
            DerValue derValue = new DerInputStream(this.key).getDerValue();
            if (derValue.tag != 48) {
                throw new IOException("Not a SEQUENCE");
            }
            DerInputStream derInputStream = derValue.data;
            if (derInputStream.getInteger() != 0) {
                throw new IOException("Version must be 0");
            }
            this.f13652n = derInputStream.getPositiveBigInteger();
            this.f13653e = derInputStream.getPositiveBigInteger();
            this.f13654d = derInputStream.getPositiveBigInteger();
            this.f13655p = derInputStream.getPositiveBigInteger();
            this.f13656q = derInputStream.getPositiveBigInteger();
            this.pe = derInputStream.getPositiveBigInteger();
            this.qe = derInputStream.getPositiveBigInteger();
            this.coeff = derInputStream.getPositiveBigInteger();
            if (derValue.data.available() != 0) {
                throw new IOException("Extra data available");
            }
        } catch (IOException e2) {
            throw new InvalidKeyException("Invalid RSA private key", e2);
        }
    }
}
