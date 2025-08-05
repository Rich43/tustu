package sun.security.ec;

import java.io.IOException;
import java.math.BigInteger;
import java.security.AlgorithmParameters;
import java.security.InvalidKeyException;
import java.security.interfaces.ECPrivateKey;
import java.security.spec.ECParameterSpec;
import java.security.spec.InvalidParameterSpecException;
import sun.security.pkcs.PKCS8Key;
import sun.security.util.ArrayUtil;
import sun.security.util.DerInputStream;
import sun.security.util.DerOutputStream;
import sun.security.util.DerValue;
import sun.security.util.ECParameters;
import sun.security.x509.AlgorithmId;

/* loaded from: sunec.jar:sun/security/ec/ECPrivateKeyImpl.class */
public final class ECPrivateKeyImpl extends PKCS8Key implements ECPrivateKey {
    private static final long serialVersionUID = 88695385615075129L;

    /* renamed from: s, reason: collision with root package name */
    private BigInteger f13603s;
    private byte[] arrayS;
    private ECParameterSpec params;

    public ECPrivateKeyImpl(byte[] bArr) throws InvalidKeyException {
        decode(bArr);
    }

    public ECPrivateKeyImpl(BigInteger bigInteger, ECParameterSpec eCParameterSpec) throws InvalidKeyException {
        this.f13603s = bigInteger;
        this.params = eCParameterSpec;
        makeEncoding(bigInteger);
    }

    ECPrivateKeyImpl(byte[] bArr, ECParameterSpec eCParameterSpec) throws InvalidKeyException {
        this.arrayS = (byte[]) bArr.clone();
        this.params = eCParameterSpec;
        makeEncoding(bArr);
    }

    private void makeEncoding(byte[] bArr) throws InvalidKeyException {
        this.algid = new AlgorithmId(AlgorithmId.EC_oid, ECParameters.getAlgorithmParameters(this.params));
        try {
            DerOutputStream derOutputStream = new DerOutputStream();
            derOutputStream.putInteger(1);
            byte[] bArr2 = (byte[]) bArr.clone();
            ArrayUtil.reverse(bArr2);
            derOutputStream.putOctetString(bArr2);
            this.key = new DerValue((byte) 48, derOutputStream.toByteArray()).toByteArray();
        } catch (IOException e2) {
            throw new InvalidKeyException(e2);
        }
    }

    private void makeEncoding(BigInteger bigInteger) throws InvalidKeyException {
        this.algid = new AlgorithmId(AlgorithmId.EC_oid, ECParameters.getAlgorithmParameters(this.params));
        try {
            byte[] byteArray = bigInteger.toByteArray();
            byte[] bArr = new byte[(this.params.getOrder().bitLength() + 7) / 8];
            System.arraycopy(byteArray, Math.max(byteArray.length - bArr.length, 0), bArr, Math.max(bArr.length - byteArray.length, 0), Math.min(byteArray.length, bArr.length));
            DerOutputStream derOutputStream = new DerOutputStream();
            derOutputStream.putInteger(1);
            derOutputStream.putOctetString(bArr);
            this.key = new DerValue((byte) 48, derOutputStream.toByteArray()).toByteArray();
        } catch (IOException e2) {
            throw new InvalidKeyException(e2);
        }
    }

    @Override // sun.security.pkcs.PKCS8Key, java.security.Key
    public String getAlgorithm() {
        return "EC";
    }

    @Override // java.security.interfaces.ECPrivateKey
    public BigInteger getS() {
        if (this.f13603s == null) {
            byte[] bArr = (byte[]) this.arrayS.clone();
            ArrayUtil.reverse(bArr);
            this.f13603s = new BigInteger(1, bArr);
        }
        return this.f13603s;
    }

    public byte[] getArrayS() {
        if (this.arrayS == null) {
            byte[] byteArray = getS().toByteArray();
            ArrayUtil.reverse(byteArray);
            int iBitLength = (this.params.getOrder().bitLength() + 7) / 8;
            this.arrayS = new byte[iBitLength];
            System.arraycopy(byteArray, 0, this.arrayS, 0, Math.min(iBitLength, byteArray.length));
        }
        return (byte[]) this.arrayS.clone();
    }

    @Override // java.security.interfaces.ECKey
    public ECParameterSpec getParams() {
        return this.params;
    }

    @Override // sun.security.pkcs.PKCS8Key
    protected void parseKeyBits() throws IOException, InvalidKeyException {
        try {
            DerValue derValue = new DerInputStream(this.key).getDerValue();
            if (derValue.tag != 48) {
                throw new IOException("Not a SEQUENCE");
            }
            DerInputStream derInputStream = derValue.data;
            if (derInputStream.getInteger() != 1) {
                throw new IOException("Version must be 1");
            }
            byte[] octetString = derInputStream.getOctetString();
            ArrayUtil.reverse(octetString);
            this.arrayS = octetString;
            while (derInputStream.available() != 0) {
                DerValue derValue2 = derInputStream.getDerValue();
                if (!derValue2.isContextSpecific((byte) 0) && !derValue2.isContextSpecific((byte) 1)) {
                    throw new InvalidKeyException("Unexpected value: " + ((Object) derValue2));
                }
            }
            AlgorithmParameters parameters = this.algid.getParameters();
            if (parameters == null) {
                throw new InvalidKeyException("EC domain parameters must be encoded in the algorithm identifier");
            }
            this.params = (ECParameterSpec) parameters.getParameterSpec(ECParameterSpec.class);
        } catch (IOException e2) {
            throw new InvalidKeyException("Invalid EC private key", e2);
        } catch (InvalidParameterSpecException e3) {
            throw new InvalidKeyException("Invalid EC private key", e3);
        }
    }
}
