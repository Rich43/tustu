package sun.security.provider;

import java.io.IOException;
import java.io.Serializable;
import java.math.BigInteger;
import java.security.AlgorithmParameters;
import java.security.InvalidKeyException;
import java.security.interfaces.DSAParams;
import java.security.spec.DSAParameterSpec;
import java.security.spec.InvalidParameterSpecException;
import sun.security.util.BitArray;
import sun.security.util.Debug;
import sun.security.util.DerInputStream;
import sun.security.util.DerValue;
import sun.security.x509.AlgIdDSA;
import sun.security.x509.X509Key;

/* loaded from: rt.jar:sun/security/provider/DSAPublicKey.class */
public class DSAPublicKey extends X509Key implements java.security.interfaces.DSAPublicKey, Serializable {
    private static final long serialVersionUID = -2994193307391104133L;

    /* renamed from: y, reason: collision with root package name */
    private BigInteger f13634y;

    public DSAPublicKey() {
    }

    public DSAPublicKey(BigInteger bigInteger, BigInteger bigInteger2, BigInteger bigInteger3, BigInteger bigInteger4) throws InvalidKeyException {
        this.f13634y = bigInteger;
        this.algid = new AlgIdDSA(bigInteger2, bigInteger3, bigInteger4);
        try {
            byte[] byteArray = new DerValue((byte) 2, bigInteger.toByteArray()).toByteArray();
            setKey(new BitArray(byteArray.length * 8, byteArray));
            encode();
        } catch (IOException e2) {
            throw new InvalidKeyException("could not DER encode y: " + e2.getMessage());
        }
    }

    public DSAPublicKey(byte[] bArr) throws InvalidKeyException {
        decode(bArr);
    }

    @Override // java.security.interfaces.DSAKey
    public DSAParams getParams() {
        try {
            if (this.algid instanceof DSAParams) {
                return (DSAParams) this.algid;
            }
            AlgorithmParameters parameters = this.algid.getParameters();
            if (parameters == null) {
                return null;
            }
            return (DSAParameterSpec) parameters.getParameterSpec(DSAParameterSpec.class);
        } catch (InvalidParameterSpecException e2) {
            return null;
        }
    }

    @Override // java.security.interfaces.DSAPublicKey
    public BigInteger getY() {
        return this.f13634y;
    }

    @Override // sun.security.x509.X509Key
    public String toString() {
        return "Sun DSA Public Key\n    Parameters:" + ((Object) this.algid) + "\n  y:\n" + Debug.toHexString(this.f13634y) + "\n";
    }

    @Override // sun.security.x509.X509Key
    protected void parseKeyBits() throws InvalidKeyException {
        try {
            this.f13634y = new DerInputStream(getKey().toByteArray()).getBigInteger();
        } catch (IOException e2) {
            throw new InvalidKeyException("Invalid key: y value\n" + e2.getMessage());
        }
    }
}
