package com.sun.crypto.provider;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectStreamException;
import java.io.Serializable;
import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.KeyRep;
import java.security.PrivateKey;
import java.security.ProviderException;
import java.util.Objects;
import javax.crypto.spec.DHParameterSpec;
import javax.xml.datatype.DatatypeConstants;
import sun.security.util.DerInputStream;
import sun.security.util.DerOutputStream;
import sun.security.util.DerValue;
import sun.security.util.ObjectIdentifier;

/* loaded from: sunjce_provider.jar:com/sun/crypto/provider/DHPrivateKey.class */
final class DHPrivateKey implements PrivateKey, javax.crypto.interfaces.DHPrivateKey, Serializable {
    static final long serialVersionUID = 7565477590005668886L;
    private static final BigInteger PKCS8_VERSION = BigInteger.ZERO;

    /* renamed from: x, reason: collision with root package name */
    private BigInteger f11815x;
    private byte[] key;
    private byte[] encodedKey;

    /* renamed from: p, reason: collision with root package name */
    private BigInteger f11816p;

    /* renamed from: g, reason: collision with root package name */
    private BigInteger f11817g;

    /* renamed from: l, reason: collision with root package name */
    private int f11818l;
    private int[] DH_data;

    DHPrivateKey(BigInteger bigInteger, BigInteger bigInteger2, BigInteger bigInteger3) throws InvalidKeyException {
        this(bigInteger, bigInteger2, bigInteger3, 0);
    }

    DHPrivateKey(BigInteger bigInteger, BigInteger bigInteger2, BigInteger bigInteger3, int i2) {
        this.DH_data = new int[]{1, 2, DatatypeConstants.MIN_TIMEZONE_OFFSET, 113549, 1, 3, 1};
        this.f11815x = bigInteger;
        this.f11816p = bigInteger2;
        this.f11817g = bigInteger3;
        this.f11818l = i2;
        try {
            this.key = new DerValue((byte) 2, this.f11815x.toByteArray()).toByteArray();
            this.encodedKey = getEncoded();
        } catch (IOException e2) {
            throw new ProviderException("Cannot produce ASN.1 encoding", e2);
        }
    }

    DHPrivateKey(byte[] bArr) throws InvalidKeyException, IOException {
        this.DH_data = new int[]{1, 2, DatatypeConstants.MIN_TIMEZONE_OFFSET, 113549, 1, 3, 1};
        try {
            DerValue derValue = new DerValue(new ByteArrayInputStream(bArr));
            if (derValue.tag != 48) {
                throw new InvalidKeyException("Key not a SEQUENCE");
            }
            BigInteger bigInteger = derValue.data.getBigInteger();
            if (!bigInteger.equals(PKCS8_VERSION)) {
                throw new IOException("version mismatch: (supported: " + ((Object) PKCS8_VERSION) + ", parsed: " + ((Object) bigInteger));
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
            this.f11816p = derValue3.data.getBigInteger();
            this.f11817g = derValue3.data.getBigInteger();
            if (derValue3.data.available() != 0) {
                this.f11818l = derValue3.data.getInteger();
            }
            if (derValue3.data.available() != 0) {
                throw new InvalidKeyException("Extra parameter data");
            }
            this.key = derValue.data.getOctetString();
            parseKeyBits();
            this.encodedKey = (byte[]) bArr.clone();
        } catch (IOException | NumberFormatException e2) {
            throw new InvalidKeyException("Error parsing key encoding", e2);
        }
    }

    @Override // java.security.Key
    public String getFormat() {
        return "PKCS#8";
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
                derOutputStream.putInteger(PKCS8_VERSION);
                DerOutputStream derOutputStream2 = new DerOutputStream();
                derOutputStream2.putOID(new ObjectIdentifier(this.DH_data));
                DerOutputStream derOutputStream3 = new DerOutputStream();
                derOutputStream3.putInteger(this.f11816p);
                derOutputStream3.putInteger(this.f11817g);
                if (this.f11818l != 0) {
                    derOutputStream3.putInteger(this.f11818l);
                }
                derOutputStream2.putDerValue(new DerValue((byte) 48, derOutputStream3.toByteArray()));
                derOutputStream.write((byte) 48, derOutputStream2);
                derOutputStream.putOctetString(this.key);
                DerOutputStream derOutputStream4 = new DerOutputStream();
                derOutputStream4.write((byte) 48, derOutputStream);
                this.encodedKey = derOutputStream4.toByteArray();
            } catch (IOException e2) {
                return null;
            }
        }
        return (byte[]) this.encodedKey.clone();
    }

    @Override // javax.crypto.interfaces.DHPrivateKey
    public BigInteger getX() {
        return this.f11815x;
    }

    @Override // javax.crypto.interfaces.DHKey
    public DHParameterSpec getParams() {
        if (this.f11818l != 0) {
            return new DHParameterSpec(this.f11816p, this.f11817g, this.f11818l);
        }
        return new DHParameterSpec(this.f11816p, this.f11817g);
    }

    private void parseKeyBits() throws InvalidKeyException {
        try {
            this.f11815x = new DerInputStream(this.key).getBigInteger();
        } catch (IOException e2) {
            InvalidKeyException invalidKeyException = new InvalidKeyException("Error parsing key encoding: " + e2.getMessage());
            invalidKeyException.initCause(e2);
            throw invalidKeyException;
        }
    }

    public int hashCode() {
        return Objects.hash(this.f11815x, this.f11816p, this.f11817g);
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof javax.crypto.interfaces.DHPrivateKey)) {
            return false;
        }
        javax.crypto.interfaces.DHPrivateKey dHPrivateKey = (javax.crypto.interfaces.DHPrivateKey) obj;
        DHParameterSpec params = dHPrivateKey.getParams();
        return this.f11815x.compareTo(dHPrivateKey.getX()) == 0 && this.f11816p.compareTo(params.getP()) == 0 && this.f11817g.compareTo(params.getG()) == 0;
    }

    private Object writeReplace() throws ObjectStreamException {
        return new KeyRep(KeyRep.Type.PRIVATE, getAlgorithm(), getFormat(), getEncoded());
    }
}
