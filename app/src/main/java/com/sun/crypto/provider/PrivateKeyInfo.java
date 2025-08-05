package com.sun.crypto.provider;

import java.io.IOException;
import java.math.BigInteger;
import sun.security.util.DerValue;
import sun.security.x509.AlgorithmId;

/* loaded from: sunjce_provider.jar:com/sun/crypto/provider/PrivateKeyInfo.class */
final class PrivateKeyInfo {
    private static final BigInteger VERSION = BigInteger.ZERO;
    private AlgorithmId algid;
    private byte[] privkey;

    PrivateKeyInfo(byte[] bArr) throws IOException {
        DerValue derValue = new DerValue(bArr);
        if (derValue.tag != 48) {
            throw new IOException("private key parse error: not a sequence");
        }
        BigInteger bigInteger = derValue.data.getBigInteger();
        if (!bigInteger.equals(VERSION)) {
            throw new IOException("version mismatch: (supported: " + ((Object) VERSION) + ", parsed: " + ((Object) bigInteger));
        }
        this.algid = AlgorithmId.parse(derValue.data.getDerValue());
        this.privkey = derValue.data.getOctetString();
    }

    AlgorithmId getAlgorithm() {
        return this.algid;
    }
}
