package sun.security.rsa;

import java.io.IOException;
import java.io.ObjectStreamException;
import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.KeyRep;
import java.security.ProviderException;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.AlgorithmParameterSpec;
import sun.security.rsa.RSAUtil;
import sun.security.util.BitArray;
import sun.security.util.DerInputStream;
import sun.security.util.DerOutputStream;
import sun.security.util.DerValue;
import sun.security.x509.AlgorithmId;
import sun.security.x509.X509Key;

/* loaded from: rt.jar:sun/security/rsa/RSAPublicKeyImpl.class */
public final class RSAPublicKeyImpl extends X509Key implements RSAPublicKey {
    private static final long serialVersionUID = 2644735423591199609L;
    private static final BigInteger THREE = BigInteger.valueOf(3);

    /* renamed from: n, reason: collision with root package name */
    private BigInteger f13659n;

    /* renamed from: e, reason: collision with root package name */
    private BigInteger f13660e;
    private AlgorithmParameterSpec keyParams;

    public static RSAPublicKey newKey(byte[] bArr) throws InvalidKeyException {
        return new RSAPublicKeyImpl(bArr);
    }

    public static RSAPublicKey newKey(RSAUtil.KeyType keyType, AlgorithmParameterSpec algorithmParameterSpec, BigInteger bigInteger, BigInteger bigInteger2) throws InvalidKeyException {
        return new RSAPublicKeyImpl(RSAUtil.createAlgorithmId(keyType, algorithmParameterSpec), bigInteger, bigInteger2);
    }

    RSAPublicKeyImpl(AlgorithmId algorithmId, BigInteger bigInteger, BigInteger bigInteger2) throws InvalidKeyException {
        RSAKeyFactory.checkRSAProviderKeyLengths(bigInteger.bitLength(), bigInteger2);
        checkExponentRange(bigInteger, bigInteger2);
        this.f13659n = bigInteger;
        this.f13660e = bigInteger2;
        this.keyParams = RSAUtil.getParamSpec(algorithmId);
        this.algid = algorithmId;
        try {
            DerOutputStream derOutputStream = new DerOutputStream();
            derOutputStream.putInteger(bigInteger);
            derOutputStream.putInteger(bigInteger2);
            byte[] byteArray = new DerValue((byte) 48, derOutputStream.toByteArray()).toByteArray();
            setKey(new BitArray(byteArray.length * 8, byteArray));
        } catch (IOException e2) {
            throw new InvalidKeyException(e2);
        }
    }

    RSAPublicKeyImpl(byte[] bArr) throws InvalidKeyException {
        if (bArr == null || bArr.length == 0) {
            throw new InvalidKeyException("Missing key encoding");
        }
        decode(bArr);
        RSAKeyFactory.checkRSAProviderKeyLengths(this.f13659n.bitLength(), this.f13660e);
        checkExponentRange(this.f13659n, this.f13660e);
        try {
            this.keyParams = RSAUtil.getParamSpec(this.algid);
        } catch (ProviderException e2) {
            throw new InvalidKeyException(e2);
        }
    }

    static void checkExponentRange(BigInteger bigInteger, BigInteger bigInteger2) throws InvalidKeyException {
        if (bigInteger2.compareTo(bigInteger) >= 0) {
            throw new InvalidKeyException("exponent is larger than modulus");
        }
        if (bigInteger2.compareTo(THREE) < 0) {
            throw new InvalidKeyException("exponent is smaller than 3");
        }
    }

    @Override // sun.security.x509.X509Key, java.security.Key
    public String getAlgorithm() {
        return this.algid.getName();
    }

    @Override // java.security.interfaces.RSAKey
    public BigInteger getModulus() {
        return this.f13659n;
    }

    @Override // java.security.interfaces.RSAPublicKey
    public BigInteger getPublicExponent() {
        return this.f13660e;
    }

    @Override // java.security.interfaces.RSAKey
    public AlgorithmParameterSpec getParams() {
        return this.keyParams;
    }

    @Override // sun.security.x509.X509Key
    protected void parseKeyBits() throws IOException, InvalidKeyException {
        try {
            DerValue derValue = new DerInputStream(getKey().toByteArray()).getDerValue();
            if (derValue.tag != 48) {
                throw new IOException("Not a SEQUENCE");
            }
            DerInputStream derInputStream = derValue.data;
            this.f13659n = derInputStream.getPositiveBigInteger();
            this.f13660e = derInputStream.getPositiveBigInteger();
            if (derValue.data.available() != 0) {
                throw new IOException("Extra data available");
            }
        } catch (IOException e2) {
            throw new InvalidKeyException("Invalid RSA public key", e2);
        }
    }

    @Override // sun.security.x509.X509Key
    public String toString() {
        return "Sun " + getAlgorithm() + " public key, " + this.f13659n.bitLength() + " bits\n  params: " + ((Object) this.keyParams) + "\n  modulus: " + ((Object) this.f13659n) + "\n  public exponent: " + ((Object) this.f13660e);
    }

    protected Object writeReplace() throws ObjectStreamException {
        return new KeyRep(KeyRep.Type.PUBLIC, getAlgorithm(), getFormat(), getEncoded());
    }
}
