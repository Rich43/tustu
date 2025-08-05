package sun.security.pkcs;

import java.io.IOException;
import sun.security.util.DerOutputStream;
import sun.security.util.DerValue;
import sun.security.x509.AlgorithmId;

/* loaded from: rt.jar:sun/security/pkcs/EncryptedPrivateKeyInfo.class */
public class EncryptedPrivateKeyInfo {
    private AlgorithmId algid;
    private byte[] encryptedData;
    private byte[] encoded;

    public EncryptedPrivateKeyInfo(byte[] bArr) throws IOException {
        if (bArr == null) {
            throw new IllegalArgumentException("encoding must not be null");
        }
        DerValue derValue = new DerValue(bArr);
        DerValue[] derValueArr = {derValue.data.getDerValue(), derValue.data.getDerValue()};
        if (derValue.data.available() != 0) {
            throw new IOException("overrun, bytes = " + derValue.data.available());
        }
        this.algid = AlgorithmId.parse(derValueArr[0]);
        if (derValueArr[0].data.available() != 0) {
            throw new IOException("encryptionAlgorithm field overrun");
        }
        this.encryptedData = derValueArr[1].getOctetString();
        if (derValueArr[1].data.available() != 0) {
            throw new IOException("encryptedData field overrun");
        }
        this.encoded = (byte[]) bArr.clone();
    }

    public EncryptedPrivateKeyInfo(AlgorithmId algorithmId, byte[] bArr) {
        this.algid = algorithmId;
        this.encryptedData = (byte[]) bArr.clone();
    }

    public AlgorithmId getAlgorithm() {
        return this.algid;
    }

    public byte[] getEncryptedData() {
        return (byte[]) this.encryptedData.clone();
    }

    public byte[] getEncoded() throws IOException {
        if (this.encoded != null) {
            return (byte[]) this.encoded.clone();
        }
        DerOutputStream derOutputStream = new DerOutputStream();
        DerOutputStream derOutputStream2 = new DerOutputStream();
        this.algid.encode(derOutputStream2);
        derOutputStream2.putOctetString(this.encryptedData);
        derOutputStream.write((byte) 48, derOutputStream2);
        this.encoded = derOutputStream.toByteArray();
        return (byte[]) this.encoded.clone();
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof EncryptedPrivateKeyInfo)) {
            return false;
        }
        try {
            byte[] encoded = getEncoded();
            byte[] encoded2 = ((EncryptedPrivateKeyInfo) obj).getEncoded();
            if (encoded.length != encoded2.length) {
                return false;
            }
            for (int i2 = 0; i2 < encoded.length; i2++) {
                if (encoded[i2] != encoded2[i2]) {
                    return false;
                }
            }
            return true;
        } catch (IOException e2) {
            return false;
        }
    }

    public int hashCode() {
        int i2 = 0;
        for (int i3 = 0; i3 < this.encryptedData.length; i3++) {
            i2 += this.encryptedData[i3] * i3;
        }
        return i2;
    }
}
