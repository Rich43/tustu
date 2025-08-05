package sun.security.provider;

import java.io.IOException;
import java.io.Serializable;
import java.math.BigInteger;
import java.security.AlgorithmParameters;
import java.security.InvalidKeyException;
import java.security.interfaces.DSAParams;
import java.security.spec.DSAParameterSpec;
import java.security.spec.InvalidParameterSpecException;
import sun.security.pkcs.PKCS8Key;
import sun.security.util.DerInputStream;
import sun.security.util.DerValue;
import sun.security.x509.AlgIdDSA;

/* loaded from: rt.jar:sun/security/provider/DSAPrivateKey.class */
public final class DSAPrivateKey extends PKCS8Key implements java.security.interfaces.DSAPrivateKey, Serializable {
    private static final long serialVersionUID = -3244453684193605938L;

    /* renamed from: x, reason: collision with root package name */
    private BigInteger f13633x;

    public DSAPrivateKey() {
    }

    public DSAPrivateKey(BigInteger bigInteger, BigInteger bigInteger2, BigInteger bigInteger3, BigInteger bigInteger4) throws InvalidKeyException {
        this.f13633x = bigInteger;
        this.algid = new AlgIdDSA(bigInteger2, bigInteger3, bigInteger4);
        try {
            this.key = new DerValue((byte) 2, bigInteger.toByteArray()).toByteArray();
            encode();
        } catch (IOException e2) {
            InvalidKeyException invalidKeyException = new InvalidKeyException("could not DER encode x: " + e2.getMessage());
            invalidKeyException.initCause(e2);
            throw invalidKeyException;
        }
    }

    public DSAPrivateKey(byte[] bArr) throws InvalidKeyException {
        clearOldKey();
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

    @Override // java.security.interfaces.DSAPrivateKey
    public BigInteger getX() {
        return this.f13633x;
    }

    private void clearOldKey() {
        if (this.encodedKey != null) {
            for (int i2 = 0; i2 < this.encodedKey.length; i2++) {
                this.encodedKey[i2] = 0;
            }
        }
        if (this.key != null) {
            for (int i3 = 0; i3 < this.key.length; i3++) {
                this.key[i3] = 0;
            }
        }
    }

    @Override // sun.security.pkcs.PKCS8Key
    protected void parseKeyBits() throws InvalidKeyException {
        try {
            this.f13633x = new DerInputStream(this.key).getBigInteger();
        } catch (IOException e2) {
            InvalidKeyException invalidKeyException = new InvalidKeyException(e2.getMessage());
            invalidKeyException.initCause(e2);
            throw invalidKeyException;
        }
    }
}
