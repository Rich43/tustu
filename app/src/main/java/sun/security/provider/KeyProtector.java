package sun.security.provider;

import java.io.IOException;
import java.security.Key;
import java.security.KeyStoreException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.util.Arrays;
import sun.security.pkcs.EncryptedPrivateKeyInfo;
import sun.security.pkcs.PKCS8Key;
import sun.security.util.DerValue;
import sun.security.util.ObjectIdentifier;
import sun.security.x509.AlgorithmId;

/* loaded from: rt.jar:sun/security/provider/KeyProtector.class */
final class KeyProtector {
    private static final int SALT_LEN = 20;
    private static final String DIGEST_ALG = "SHA";
    private static final int DIGEST_LEN = 20;
    private static final String KEY_PROTECTOR_OID = "1.3.6.1.4.1.42.2.17.1.1";
    private byte[] passwdBytes;
    private MessageDigest md;

    public KeyProtector(byte[] bArr) throws NoSuchAlgorithmException {
        if (bArr == null) {
            throw new IllegalArgumentException("password can't be null");
        }
        this.md = MessageDigest.getInstance(DIGEST_ALG);
        this.passwdBytes = bArr;
    }

    protected void finalize() {
        if (this.passwdBytes != null) {
            Arrays.fill(this.passwdBytes, (byte) 0);
            this.passwdBytes = null;
        }
    }

    public byte[] protect(Key key) throws KeyStoreException {
        if (key == null) {
            throw new IllegalArgumentException("plaintext key can't be null");
        }
        if (!"PKCS#8".equalsIgnoreCase(key.getFormat())) {
            throw new KeyStoreException("Cannot get key bytes, not PKCS#8 encoded");
        }
        byte[] encoded = key.getEncoded();
        if (encoded == null) {
            throw new KeyStoreException("Cannot get key bytes, encoding not supported");
        }
        int length = encoded.length / 20;
        if (encoded.length % 20 != 0) {
            length++;
        }
        byte[] bArr = new byte[20];
        new java.security.SecureRandom().nextBytes(bArr);
        byte[] bArr2 = new byte[encoded.length];
        int i2 = 0;
        int i3 = 0;
        byte[] bArrDigest = bArr;
        while (i2 < length) {
            this.md.update(this.passwdBytes);
            this.md.update(bArrDigest);
            bArrDigest = this.md.digest();
            this.md.reset();
            if (i2 < length - 1) {
                System.arraycopy(bArrDigest, 0, bArr2, i3, bArrDigest.length);
            } else {
                System.arraycopy(bArrDigest, 0, bArr2, i3, bArr2.length - i3);
            }
            i2++;
            i3 += 20;
        }
        byte[] bArr3 = new byte[encoded.length];
        for (int i4 = 0; i4 < bArr3.length; i4++) {
            bArr3[i4] = (byte) (encoded[i4] ^ bArr2[i4]);
        }
        byte[] bArr4 = new byte[bArr.length + bArr3.length + 20];
        System.arraycopy(bArr, 0, bArr4, 0, bArr.length);
        int length2 = 0 + bArr.length;
        System.arraycopy(bArr3, 0, bArr4, length2, bArr3.length);
        int length3 = length2 + bArr3.length;
        this.md.update(this.passwdBytes);
        Arrays.fill(this.passwdBytes, (byte) 0);
        this.passwdBytes = null;
        this.md.update(encoded);
        byte[] bArrDigest2 = this.md.digest();
        this.md.reset();
        System.arraycopy(bArrDigest2, 0, bArr4, length3, bArrDigest2.length);
        try {
            return new EncryptedPrivateKeyInfo(new AlgorithmId(new ObjectIdentifier(KEY_PROTECTOR_OID)), bArr4).getEncoded();
        } catch (IOException e2) {
            throw new KeyStoreException(e2.getMessage());
        }
    }

    public Key recover(EncryptedPrivateKeyInfo encryptedPrivateKeyInfo) throws UnrecoverableKeyException {
        if (!encryptedPrivateKeyInfo.getAlgorithm().getOID().toString().equals(KEY_PROTECTOR_OID)) {
            throw new UnrecoverableKeyException("Unsupported key protection algorithm");
        }
        byte[] encryptedData = encryptedPrivateKeyInfo.getEncryptedData();
        byte[] bArr = new byte[20];
        System.arraycopy(encryptedData, 0, bArr, 0, 20);
        int length = (encryptedData.length - 20) - 20;
        int i2 = length / 20;
        if (length % 20 != 0) {
            i2++;
        }
        byte[] bArr2 = new byte[length];
        System.arraycopy(encryptedData, 20, bArr2, 0, length);
        byte[] bArr3 = new byte[bArr2.length];
        int i3 = 0;
        int i4 = 0;
        byte[] bArrDigest = bArr;
        while (i3 < i2) {
            this.md.update(this.passwdBytes);
            this.md.update(bArrDigest);
            bArrDigest = this.md.digest();
            this.md.reset();
            if (i3 < i2 - 1) {
                System.arraycopy(bArrDigest, 0, bArr3, i4, bArrDigest.length);
            } else {
                System.arraycopy(bArrDigest, 0, bArr3, i4, bArr3.length - i4);
            }
            i3++;
            i4 += 20;
        }
        byte[] bArr4 = new byte[bArr2.length];
        for (int i5 = 0; i5 < bArr4.length; i5++) {
            bArr4[i5] = (byte) (bArr2[i5] ^ bArr3[i5]);
        }
        this.md.update(this.passwdBytes);
        Arrays.fill(this.passwdBytes, (byte) 0);
        this.passwdBytes = null;
        this.md.update(bArr4);
        byte[] bArrDigest2 = this.md.digest();
        this.md.reset();
        for (int i6 = 0; i6 < bArrDigest2.length; i6++) {
            if (bArrDigest2[i6] != encryptedData[20 + length + i6]) {
                throw new UnrecoverableKeyException("Cannot recover key");
            }
        }
        try {
            return PKCS8Key.parseKey(new DerValue(bArr4));
        } catch (IOException e2) {
            throw new UnrecoverableKeyException(e2.getMessage());
        }
    }
}
