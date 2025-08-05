package com.sun.crypto.provider;

import com.sun.org.apache.xml.internal.security.keys.content.x509.XMLX509Certificate;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectStreamException;
import java.io.Serializable;
import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.KeyRep;
import java.security.ProviderException;
import java.security.PublicKey;
import java.util.Objects;
import javax.crypto.spec.DHParameterSpec;
import javax.xml.datatype.DatatypeConstants;
import sun.security.util.Debug;
import sun.security.util.DerInputStream;
import sun.security.util.DerOutputStream;
import sun.security.util.DerValue;
import sun.security.util.ObjectIdentifier;

/* loaded from: sunjce_provider.jar:com/sun/crypto/provider/DHPublicKey.class */
final class DHPublicKey implements PublicKey, javax.crypto.interfaces.DHPublicKey, Serializable {
    static final long serialVersionUID = 7647557958927458271L;

    /* renamed from: y, reason: collision with root package name */
    private BigInteger f11819y;
    private byte[] key;
    private byte[] encodedKey;

    /* renamed from: p, reason: collision with root package name */
    private BigInteger f11820p;

    /* renamed from: g, reason: collision with root package name */
    private BigInteger f11821g;

    /* renamed from: l, reason: collision with root package name */
    private int f11822l;
    private int[] DH_data;

    DHPublicKey(BigInteger bigInteger, BigInteger bigInteger2, BigInteger bigInteger3) throws InvalidKeyException {
        this(bigInteger, bigInteger2, bigInteger3, 0);
    }

    DHPublicKey(BigInteger bigInteger, BigInteger bigInteger2, BigInteger bigInteger3, int i2) {
        this.DH_data = new int[]{1, 2, DatatypeConstants.MIN_TIMEZONE_OFFSET, 113549, 1, 3, 1};
        this.f11819y = bigInteger;
        this.f11820p = bigInteger2;
        this.f11821g = bigInteger3;
        this.f11822l = i2;
        try {
            this.key = new DerValue((byte) 2, this.f11819y.toByteArray()).toByteArray();
            this.encodedKey = getEncoded();
        } catch (IOException e2) {
            throw new ProviderException("Cannot produce ASN.1 encoding", e2);
        }
    }

    DHPublicKey(byte[] bArr) throws InvalidKeyException {
        this.DH_data = new int[]{1, 2, DatatypeConstants.MIN_TIMEZONE_OFFSET, 113549, 1, 3, 1};
        try {
            DerValue derValue = new DerValue(new ByteArrayInputStream(bArr));
            if (derValue.tag != 48) {
                throw new InvalidKeyException("Invalid key format");
            }
            DerValue derValue2 = derValue.data.getDerValue();
            if (derValue2.tag != 48) {
                throw new InvalidKeyException("AlgId is not a SEQUENCE");
            }
            DerInputStream derInputStream = derValue2.toDerInputStream();
            if (derInputStream.getOID() == null) {
                throw new InvalidKeyException("Null OID");
            }
            if (derInputStream.available() == 0) {
                throw new InvalidKeyException("Parameters missing");
            }
            DerValue derValue3 = derInputStream.getDerValue();
            if (derValue3.tag == 5) {
                throw new InvalidKeyException("Null parameters");
            }
            if (derValue3.tag != 48) {
                throw new InvalidKeyException("Parameters not a SEQUENCE");
            }
            derValue3.data.reset();
            this.f11820p = derValue3.data.getBigInteger();
            this.f11821g = derValue3.data.getBigInteger();
            if (derValue3.data.available() != 0) {
                this.f11822l = derValue3.data.getInteger();
            }
            if (derValue3.data.available() != 0) {
                throw new InvalidKeyException("Extra parameter data");
            }
            this.key = derValue.data.getBitString();
            parseKeyBits();
            if (derValue.data.available() != 0) {
                throw new InvalidKeyException("Excess key data");
            }
            this.encodedKey = (byte[]) bArr.clone();
        } catch (IOException | NumberFormatException e2) {
            throw new InvalidKeyException("Error parsing key encoding", e2);
        }
    }

    @Override // java.security.Key
    public String getFormat() {
        return XMLX509Certificate.JCA_CERT_ID;
    }

    @Override // java.security.Key
    public String getAlgorithm() {
        return "DH";
    }

    @Override // java.security.Key
    public synchronized byte[] getEncoded() {
        if (this.encodedKey == null) {
            try {
                DerOutputStream derOutputStream = new DerOutputStream();
                derOutputStream.putOID(new ObjectIdentifier(this.DH_data));
                DerOutputStream derOutputStream2 = new DerOutputStream();
                derOutputStream2.putInteger(this.f11820p);
                derOutputStream2.putInteger(this.f11821g);
                if (this.f11822l != 0) {
                    derOutputStream2.putInteger(this.f11822l);
                }
                derOutputStream.putDerValue(new DerValue((byte) 48, derOutputStream2.toByteArray()));
                DerOutputStream derOutputStream3 = new DerOutputStream();
                derOutputStream3.write((byte) 48, derOutputStream);
                derOutputStream3.putBitString(this.key);
                DerOutputStream derOutputStream4 = new DerOutputStream();
                derOutputStream4.write((byte) 48, derOutputStream3);
                this.encodedKey = derOutputStream4.toByteArray();
            } catch (IOException e2) {
                return null;
            }
        }
        return (byte[]) this.encodedKey.clone();
    }

    @Override // javax.crypto.interfaces.DHPublicKey
    public BigInteger getY() {
        return this.f11819y;
    }

    @Override // javax.crypto.interfaces.DHKey
    public DHParameterSpec getParams() {
        if (this.f11822l != 0) {
            return new DHParameterSpec(this.f11820p, this.f11821g, this.f11822l);
        }
        return new DHParameterSpec(this.f11820p, this.f11821g);
    }

    public String toString() {
        String property = System.getProperty("line.separator");
        StringBuffer stringBuffer = new StringBuffer("SunJCE Diffie-Hellman Public Key:" + property + "y:" + property + Debug.toHexString(this.f11819y) + property + "p:" + property + Debug.toHexString(this.f11820p) + property + "g:" + property + Debug.toHexString(this.f11821g));
        if (this.f11822l != 0) {
            stringBuffer.append(property + "l:" + property + "    " + this.f11822l);
        }
        return stringBuffer.toString();
    }

    private void parseKeyBits() throws InvalidKeyException {
        try {
            this.f11819y = new DerInputStream(this.key).getBigInteger();
        } catch (IOException e2) {
            throw new InvalidKeyException("Error parsing key encoding: " + e2.toString());
        }
    }

    public int hashCode() {
        return Objects.hash(this.f11819y, this.f11820p, this.f11821g);
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof javax.crypto.interfaces.DHPublicKey)) {
            return false;
        }
        javax.crypto.interfaces.DHPublicKey dHPublicKey = (javax.crypto.interfaces.DHPublicKey) obj;
        DHParameterSpec params = dHPublicKey.getParams();
        return this.f11819y.compareTo(dHPublicKey.getY()) == 0 && this.f11820p.compareTo(params.getP()) == 0 && this.f11821g.compareTo(params.getG()) == 0;
    }

    private Object writeReplace() throws ObjectStreamException {
        return new KeyRep(KeyRep.Type.PUBLIC, getAlgorithm(), getFormat(), getEncoded());
    }
}
