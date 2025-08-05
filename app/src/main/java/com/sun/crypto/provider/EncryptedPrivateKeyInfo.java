package com.sun.crypto.provider;

import java.io.IOException;
import sun.security.util.DerOutputStream;
import sun.security.util.DerValue;
import sun.security.x509.AlgorithmId;

/* loaded from: sunjce_provider.jar:com/sun/crypto/provider/EncryptedPrivateKeyInfo.class */
final class EncryptedPrivateKeyInfo {
    private AlgorithmId algid;
    private byte[] encryptedData;
    private byte[] encoded;

    EncryptedPrivateKeyInfo(byte[] bArr) throws IOException {
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

    EncryptedPrivateKeyInfo(AlgorithmId algorithmId, byte[] bArr) {
        this.algid = algorithmId;
        this.encryptedData = (byte[]) bArr.clone();
        this.encoded = null;
    }

    AlgorithmId getAlgorithm() {
        return this.algid;
    }

    byte[] getEncryptedData() {
        return (byte[]) this.encryptedData.clone();
    }

    byte[] getEncoded() throws IOException {
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
}
