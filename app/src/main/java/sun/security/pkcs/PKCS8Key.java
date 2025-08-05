package sun.security.pkcs;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectStreamException;
import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyRep;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.Provider;
import java.security.Security;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import sun.security.util.Debug;
import sun.security.util.DerOutputStream;
import sun.security.util.DerValue;
import sun.security.x509.AlgorithmId;

/* loaded from: rt.jar:sun/security/pkcs/PKCS8Key.class */
public class PKCS8Key implements PrivateKey {
    private static final long serialVersionUID = -3836890099307167124L;
    protected AlgorithmId algid;
    protected byte[] key;
    protected byte[] encodedKey;
    public static final BigInteger version = BigInteger.ZERO;

    public PKCS8Key() {
    }

    private PKCS8Key(AlgorithmId algorithmId, byte[] bArr) throws InvalidKeyException {
        this.algid = algorithmId;
        this.key = bArr;
        encode();
    }

    public static PKCS8Key parse(DerValue derValue) throws InstantiationException, IOException {
        PrivateKey key = parseKey(derValue);
        if (key instanceof PKCS8Key) {
            return (PKCS8Key) key;
        }
        throw new IOException("Provider did not return PKCS8Key");
    }

    public static PrivateKey parseKey(DerValue derValue) throws InstantiationException, IOException {
        if (derValue.tag != 48) {
            throw new IOException("corrupt private key");
        }
        BigInteger bigInteger = derValue.data.getBigInteger();
        if (!version.equals(bigInteger)) {
            throw new IOException("version mismatch: (supported: " + Debug.toHexString(version) + ", parsed: " + Debug.toHexString(bigInteger));
        }
        try {
            PrivateKey privateKeyBuildPKCS8Key = buildPKCS8Key(AlgorithmId.parse(derValue.data.getDerValue()), derValue.data.getOctetString());
            if (derValue.data.available() != 0) {
                throw new IOException("excess private key");
            }
            return privateKeyBuildPKCS8Key;
        } catch (InvalidKeyException e2) {
            throw new IOException("corrupt private key");
        }
    }

    protected void parseKeyBits() throws InvalidKeyException, IOException {
        encode();
    }

    static PrivateKey buildPKCS8Key(AlgorithmId algorithmId, byte[] bArr) throws InstantiationException, IOException, InvalidKeyException {
        Provider provider;
        DerOutputStream derOutputStream = new DerOutputStream();
        encode(derOutputStream, algorithmId, bArr);
        try {
            return KeyFactory.getInstance(algorithmId.getName()).generatePrivate(new PKCS8EncodedKeySpec(derOutputStream.toByteArray()));
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e2) {
            try {
                provider = Security.getProvider("SUN");
            } catch (ClassNotFoundException e3) {
            } catch (IllegalAccessException e4) {
                throw new IOException(" [internal error]");
            } catch (InstantiationException e5) {
            }
            if (provider == null) {
                throw new InstantiationException();
            }
            String property = provider.getProperty("PrivateKey.PKCS#8." + algorithmId.getName());
            if (property == null) {
                throw new InstantiationException();
            }
            Class<?> clsLoadClass = null;
            try {
                clsLoadClass = Class.forName(property);
            } catch (ClassNotFoundException e6) {
                ClassLoader systemClassLoader = ClassLoader.getSystemClassLoader();
                if (systemClassLoader != null) {
                    clsLoadClass = systemClassLoader.loadClass(property);
                }
            }
            Object objNewInstance = null;
            if (clsLoadClass != null) {
                objNewInstance = clsLoadClass.newInstance();
            }
            if (objNewInstance instanceof PKCS8Key) {
                PKCS8Key pKCS8Key = (PKCS8Key) objNewInstance;
                pKCS8Key.algid = algorithmId;
                pKCS8Key.key = bArr;
                pKCS8Key.parseKeyBits();
                return pKCS8Key;
            }
            PKCS8Key pKCS8Key2 = new PKCS8Key();
            pKCS8Key2.algid = algorithmId;
            pKCS8Key2.key = bArr;
            return pKCS8Key2;
        }
    }

    @Override // java.security.Key
    public String getAlgorithm() {
        return this.algid.getName();
    }

    public AlgorithmId getAlgorithmId() {
        return this.algid;
    }

    public final void encode(DerOutputStream derOutputStream) throws IOException {
        encode(derOutputStream, this.algid, this.key);
    }

    @Override // java.security.Key
    public synchronized byte[] getEncoded() {
        byte[] bArrEncode = null;
        try {
            bArrEncode = encode();
        } catch (InvalidKeyException e2) {
        }
        return bArrEncode;
    }

    @Override // java.security.Key
    public String getFormat() {
        return "PKCS#8";
    }

    public byte[] encode() throws InvalidKeyException {
        if (this.encodedKey == null) {
            try {
                DerOutputStream derOutputStream = new DerOutputStream();
                encode(derOutputStream);
                this.encodedKey = derOutputStream.toByteArray();
            } catch (IOException e2) {
                throw new InvalidKeyException("IOException : " + e2.getMessage());
            }
        }
        return (byte[]) this.encodedKey.clone();
    }

    public void decode(InputStream inputStream) throws InvalidKeyException, IOException {
        try {
            DerValue derValue = new DerValue(inputStream);
            if (derValue.tag != 48) {
                throw new InvalidKeyException("invalid key format");
            }
            BigInteger bigInteger = derValue.data.getBigInteger();
            if (!bigInteger.equals(version)) {
                throw new IOException("version mismatch: (supported: " + Debug.toHexString(version) + ", parsed: " + Debug.toHexString(bigInteger));
            }
            this.algid = AlgorithmId.parse(derValue.data.getDerValue());
            this.key = derValue.data.getOctetString();
            parseKeyBits();
            if (derValue.data.available() != 0) {
            }
        } catch (IOException e2) {
            throw new InvalidKeyException("IOException : " + e2.getMessage());
        }
    }

    public void decode(byte[] bArr) throws InvalidKeyException, IOException {
        decode(new ByteArrayInputStream(bArr));
    }

    protected Object writeReplace() throws ObjectStreamException {
        return new KeyRep(KeyRep.Type.PRIVATE, getAlgorithm(), getFormat(), getEncoded());
    }

    private void readObject(ObjectInputStream objectInputStream) throws IOException {
        try {
            decode(objectInputStream);
        } catch (InvalidKeyException e2) {
            e2.printStackTrace();
            throw new IOException("deserialized key is invalid: " + e2.getMessage());
        }
    }

    static void encode(DerOutputStream derOutputStream, AlgorithmId algorithmId, byte[] bArr) throws IOException {
        DerOutputStream derOutputStream2 = new DerOutputStream();
        derOutputStream2.putInteger(version);
        algorithmId.encode(derOutputStream2);
        derOutputStream2.putOctetString(bArr);
        derOutputStream.write((byte) 48, derOutputStream2);
    }

    public boolean equals(Object obj) {
        byte[] encoded;
        if (this == obj) {
            return true;
        }
        if (obj instanceof Key) {
            if (this.encodedKey != null) {
                encoded = this.encodedKey;
            } else {
                encoded = getEncoded();
            }
            return MessageDigest.isEqual(encoded, ((Key) obj).getEncoded());
        }
        return false;
    }

    public int hashCode() {
        int i2 = 0;
        byte[] encoded = getEncoded();
        for (int i3 = 1; i3 < encoded.length; i3++) {
            i2 += encoded[i3] * i3;
        }
        return i2;
    }
}
