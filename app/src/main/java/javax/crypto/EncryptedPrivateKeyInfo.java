package javax.crypto;

import java.io.IOException;
import java.security.AlgorithmParameters;
import java.security.GeneralSecurityException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.Provider;
import java.security.Security;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import sun.security.util.DerInputStream;
import sun.security.util.DerOutputStream;
import sun.security.util.DerValue;
import sun.security.x509.AlgorithmId;

/* loaded from: jce.jar:javax/crypto/EncryptedPrivateKeyInfo.class */
public class EncryptedPrivateKeyInfo {
    private AlgorithmId algid;
    private byte[] encryptedData;
    private byte[] encoded;

    public EncryptedPrivateKeyInfo(byte[] bArr) throws IOException {
        this.encoded = null;
        if (bArr == null) {
            throw new NullPointerException("the encoded parameter must be non-null");
        }
        this.encoded = (byte[]) bArr.clone();
        DerValue derValueWrap = DerValue.wrap(this.encoded);
        if (derValueWrap.tag != 48) {
            throw new IOException("DER header error: no SEQ tag");
        }
        DerValue[] derValueArr = {derValueWrap.data.getDerValue(), derValueWrap.data.getDerValue()};
        if (derValueWrap.data.available() != 0) {
            throw new IOException("overrun, bytes = " + derValueWrap.data.available());
        }
        this.algid = AlgorithmId.parse(derValueArr[0]);
        if (derValueArr[0].data.available() != 0) {
            throw new IOException("encryptionAlgorithm field overrun");
        }
        this.encryptedData = derValueArr[1].getOctetString();
        if (derValueArr[1].data.available() != 0) {
            throw new IOException("encryptedData field overrun");
        }
    }

    public EncryptedPrivateKeyInfo(String str, byte[] bArr) throws NoSuchAlgorithmException {
        this.encoded = null;
        if (str == null) {
            throw new NullPointerException("the algName parameter must be non-null");
        }
        this.algid = AlgorithmId.get(str);
        if (bArr == null) {
            throw new NullPointerException("the encryptedData parameter must be non-null");
        }
        if (bArr.length == 0) {
            throw new IllegalArgumentException("the encryptedData parameter must not be empty");
        }
        this.encryptedData = (byte[]) bArr.clone();
        this.encoded = null;
    }

    public EncryptedPrivateKeyInfo(AlgorithmParameters algorithmParameters, byte[] bArr) throws NoSuchAlgorithmException {
        this.encoded = null;
        if (algorithmParameters == null) {
            throw new NullPointerException("algParams must be non-null");
        }
        this.algid = AlgorithmId.get(algorithmParameters);
        if (bArr == null) {
            throw new NullPointerException("encryptedData must be non-null");
        }
        if (bArr.length == 0) {
            throw new IllegalArgumentException("the encryptedData parameter must not be empty");
        }
        this.encryptedData = (byte[]) bArr.clone();
        this.encoded = null;
    }

    public String getAlgName() {
        return this.algid.getName();
    }

    public AlgorithmParameters getAlgParameters() {
        return this.algid.getParameters();
    }

    public byte[] getEncryptedData() {
        return (byte[]) this.encryptedData.clone();
    }

    public PKCS8EncodedKeySpec getKeySpec(Cipher cipher) throws InvalidKeySpecException {
        try {
            byte[] bArrDoFinal = cipher.doFinal(this.encryptedData);
            checkPKCS8Encoding(bArrDoFinal);
            return new PKCS8EncodedKeySpec(bArrDoFinal);
        } catch (IOException | IllegalStateException | GeneralSecurityException e2) {
            throw new InvalidKeySpecException("Cannot retrieve the PKCS8EncodedKeySpec", e2);
        }
    }

    private PKCS8EncodedKeySpec getKeySpecImpl(Key key, Provider provider) throws NoSuchAlgorithmException, InvalidKeyException {
        Cipher cipher;
        try {
            if (provider == null) {
                cipher = Cipher.getInstance(this.algid.getName());
            } else {
                cipher = Cipher.getInstance(this.algid.getName(), provider);
            }
            cipher.init(2, key, this.algid.getParameters());
            byte[] bArrDoFinal = cipher.doFinal(this.encryptedData);
            checkPKCS8Encoding(bArrDoFinal);
            return new PKCS8EncodedKeySpec(bArrDoFinal);
        } catch (IOException | GeneralSecurityException e2) {
            throw new InvalidKeyException("Cannot retrieve the PKCS8EncodedKeySpec", e2);
        } catch (NoSuchAlgorithmException e3) {
            throw e3;
        }
    }

    public PKCS8EncodedKeySpec getKeySpec(Key key) throws NoSuchAlgorithmException, InvalidKeyException {
        if (key == null) {
            throw new NullPointerException("decryptKey is null");
        }
        return getKeySpecImpl(key, null);
    }

    public PKCS8EncodedKeySpec getKeySpec(Key key, String str) throws NoSuchAlgorithmException, InvalidKeyException, NoSuchProviderException {
        if (key == null) {
            throw new NullPointerException("decryptKey is null");
        }
        if (str == null) {
            throw new NullPointerException("provider is null");
        }
        Provider provider = Security.getProvider(str);
        if (provider == null) {
            throw new NoSuchProviderException("provider " + str + " not found");
        }
        return getKeySpecImpl(key, provider);
    }

    public PKCS8EncodedKeySpec getKeySpec(Key key, Provider provider) throws NoSuchAlgorithmException, InvalidKeyException {
        if (key == null) {
            throw new NullPointerException("decryptKey is null");
        }
        if (provider == null) {
            throw new NullPointerException("provider is null");
        }
        return getKeySpecImpl(key, provider);
    }

    public byte[] getEncoded() throws IOException {
        if (this.encoded == null) {
            DerOutputStream derOutputStream = new DerOutputStream();
            DerOutputStream derOutputStream2 = new DerOutputStream();
            this.algid.encode(derOutputStream2);
            derOutputStream2.putOctetString(this.encryptedData);
            derOutputStream.write((byte) 48, derOutputStream2);
            this.encoded = derOutputStream.toByteArray();
        }
        return (byte[]) this.encoded.clone();
    }

    private static void checkTag(DerValue derValue, byte b2, String str) throws IOException {
        if (derValue.getTag() != b2) {
            throw new IOException("invalid key encoding - wrong tag for " + str);
        }
    }

    private static void checkPKCS8Encoding(byte[] bArr) throws IOException {
        DerValue[] sequence = new DerInputStream(bArr).getSequence(3);
        switch (sequence.length) {
            case 3:
                break;
            case 4:
                checkTag(sequence[3], Byte.MIN_VALUE, "attributes");
                break;
            default:
                throw new IOException("invalid key encoding");
        }
        checkTag(sequence[0], (byte) 2, "version");
        DerInputStream derInputStream = sequence[1].toDerInputStream();
        derInputStream.getOID();
        if (derInputStream.available() != 0) {
            derInputStream.getDerValue();
        }
        checkTag(sequence[2], (byte) 4, "privateKey");
    }
}
