package sun.security.krb5.internal.crypto;

import java.security.GeneralSecurityException;
import sun.security.krb5.KrbCryptoException;
import sun.security.krb5.internal.KrbApErrException;

/* loaded from: rt.jar:sun/security/krb5/internal/crypto/Aes128CtsHmacSha1EType.class */
public final class Aes128CtsHmacSha1EType extends EType {
    @Override // sun.security.krb5.internal.crypto.EType
    public int eType() {
        return 17;
    }

    @Override // sun.security.krb5.internal.crypto.EType
    public int minimumPadSize() {
        return 0;
    }

    @Override // sun.security.krb5.internal.crypto.EType
    public int confounderSize() {
        return blockSize();
    }

    @Override // sun.security.krb5.internal.crypto.EType
    public int checksumType() {
        return 15;
    }

    @Override // sun.security.krb5.internal.crypto.EType
    public int checksumSize() {
        return Aes128.getChecksumLength();
    }

    @Override // sun.security.krb5.internal.crypto.EType
    public int blockSize() {
        return 16;
    }

    @Override // sun.security.krb5.internal.crypto.EType
    public int keyType() {
        return 3;
    }

    @Override // sun.security.krb5.internal.crypto.EType
    public int keySize() {
        return 16;
    }

    @Override // sun.security.krb5.internal.crypto.EType
    public byte[] encrypt(byte[] bArr, byte[] bArr2, int i2) throws KrbCryptoException {
        return encrypt(bArr, bArr2, new byte[blockSize()], i2);
    }

    @Override // sun.security.krb5.internal.crypto.EType
    public byte[] encrypt(byte[] bArr, byte[] bArr2, byte[] bArr3, int i2) throws KrbCryptoException {
        try {
            return Aes128.encrypt(bArr2, i2, bArr3, bArr, 0, bArr.length);
        } catch (GeneralSecurityException e2) {
            KrbCryptoException krbCryptoException = new KrbCryptoException(e2.getMessage());
            krbCryptoException.initCause(e2);
            throw krbCryptoException;
        }
    }

    @Override // sun.security.krb5.internal.crypto.EType
    public byte[] decrypt(byte[] bArr, byte[] bArr2, int i2) throws KrbCryptoException, KrbApErrException {
        return decrypt(bArr, bArr2, new byte[blockSize()], i2);
    }

    @Override // sun.security.krb5.internal.crypto.EType
    public byte[] decrypt(byte[] bArr, byte[] bArr2, byte[] bArr3, int i2) throws KrbCryptoException, KrbApErrException {
        try {
            return Aes128.decrypt(bArr2, i2, bArr3, bArr, 0, bArr.length);
        } catch (GeneralSecurityException e2) {
            KrbCryptoException krbCryptoException = new KrbCryptoException(e2.getMessage());
            krbCryptoException.initCause(e2);
            throw krbCryptoException;
        }
    }

    @Override // sun.security.krb5.internal.crypto.EType
    public byte[] decryptedData(byte[] bArr) {
        return bArr;
    }
}
