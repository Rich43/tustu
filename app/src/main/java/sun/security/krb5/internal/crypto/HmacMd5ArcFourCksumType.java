package sun.security.krb5.internal.crypto;

import java.security.GeneralSecurityException;
import sun.security.krb5.Checksum;
import sun.security.krb5.KrbCryptoException;

/* loaded from: rt.jar:sun/security/krb5/internal/crypto/HmacMd5ArcFourCksumType.class */
public class HmacMd5ArcFourCksumType extends CksumType {
    @Override // sun.security.krb5.internal.crypto.CksumType
    public int confounderSize() {
        return 8;
    }

    @Override // sun.security.krb5.internal.crypto.CksumType
    public int cksumType() {
        return Checksum.CKSUMTYPE_HMAC_MD5_ARCFOUR;
    }

    @Override // sun.security.krb5.internal.crypto.CksumType
    public boolean isKeyed() {
        return true;
    }

    @Override // sun.security.krb5.internal.crypto.CksumType
    public int cksumSize() {
        return 16;
    }

    @Override // sun.security.krb5.internal.crypto.CksumType
    public int keyType() {
        return 4;
    }

    @Override // sun.security.krb5.internal.crypto.CksumType
    public int keySize() {
        return 16;
    }

    @Override // sun.security.krb5.internal.crypto.CksumType
    public byte[] calculateChecksum(byte[] bArr, int i2, byte[] bArr2, int i3) throws KrbCryptoException {
        try {
            return ArcFourHmac.calculateChecksum(bArr2, i3, bArr, 0, i2);
        } catch (GeneralSecurityException e2) {
            KrbCryptoException krbCryptoException = new KrbCryptoException(e2.getMessage());
            krbCryptoException.initCause(e2);
            throw krbCryptoException;
        }
    }

    @Override // sun.security.krb5.internal.crypto.CksumType
    public boolean verifyChecksum(byte[] bArr, int i2, byte[] bArr2, byte[] bArr3, int i3) throws KrbCryptoException {
        try {
            return isChecksumEqual(bArr3, ArcFourHmac.calculateChecksum(bArr2, i3, bArr, 0, i2));
        } catch (GeneralSecurityException e2) {
            KrbCryptoException krbCryptoException = new KrbCryptoException(e2.getMessage());
            krbCryptoException.initCause(e2);
            throw krbCryptoException;
        }
    }
}
