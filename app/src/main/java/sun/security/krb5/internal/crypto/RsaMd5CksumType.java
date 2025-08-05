package sun.security.krb5.internal.crypto;

import java.security.MessageDigest;
import sun.security.krb5.KrbCryptoException;

/* loaded from: rt.jar:sun/security/krb5/internal/crypto/RsaMd5CksumType.class */
public final class RsaMd5CksumType extends CksumType {
    @Override // sun.security.krb5.internal.crypto.CksumType
    public int confounderSize() {
        return 0;
    }

    @Override // sun.security.krb5.internal.crypto.CksumType
    public int cksumType() {
        return 7;
    }

    @Override // sun.security.krb5.internal.crypto.CksumType
    public boolean isKeyed() {
        return false;
    }

    @Override // sun.security.krb5.internal.crypto.CksumType
    public int cksumSize() {
        return 16;
    }

    @Override // sun.security.krb5.internal.crypto.CksumType
    public int keyType() {
        return 0;
    }

    @Override // sun.security.krb5.internal.crypto.CksumType
    public int keySize() {
        return 0;
    }

    @Override // sun.security.krb5.internal.crypto.CksumType
    public byte[] calculateChecksum(byte[] bArr, int i2, byte[] bArr2, int i3) throws KrbCryptoException {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            try {
                messageDigest.update(bArr);
                return messageDigest.digest();
            } catch (Exception e2) {
                throw new KrbCryptoException(e2.getMessage());
            }
        } catch (Exception e3) {
            throw new KrbCryptoException("JCE provider may not be installed. " + e3.getMessage());
        }
    }

    @Override // sun.security.krb5.internal.crypto.CksumType
    public boolean verifyChecksum(byte[] bArr, int i2, byte[] bArr2, byte[] bArr3, int i3) throws KrbCryptoException {
        try {
            return CksumType.isChecksumEqual(MessageDigest.getInstance("MD5").digest(bArr), bArr3);
        } catch (Exception e2) {
            return false;
        }
    }
}
