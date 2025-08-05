package sun.security.krb5.internal.crypto;

import java.security.MessageDigest;
import sun.security.krb5.KrbCryptoException;
import sun.security.krb5.internal.KrbApErrException;

/* loaded from: rt.jar:sun/security/krb5/internal/crypto/DesCbcMd5EType.class */
public final class DesCbcMd5EType extends DesCbcEType {
    @Override // sun.security.krb5.internal.crypto.DesCbcEType, sun.security.krb5.internal.crypto.EType
    public /* bridge */ /* synthetic */ byte[] decrypt(byte[] bArr, byte[] bArr2, byte[] bArr3, int i2) throws KrbCryptoException, KrbApErrException {
        return super.decrypt(bArr, bArr2, bArr3, i2);
    }

    @Override // sun.security.krb5.internal.crypto.DesCbcEType, sun.security.krb5.internal.crypto.EType
    public /* bridge */ /* synthetic */ byte[] decrypt(byte[] bArr, byte[] bArr2, int i2) throws KrbCryptoException, KrbApErrException {
        return super.decrypt(bArr, bArr2, i2);
    }

    @Override // sun.security.krb5.internal.crypto.DesCbcEType, sun.security.krb5.internal.crypto.EType
    public /* bridge */ /* synthetic */ byte[] encrypt(byte[] bArr, byte[] bArr2, byte[] bArr3, int i2) throws KrbCryptoException {
        return super.encrypt(bArr, bArr2, bArr3, i2);
    }

    @Override // sun.security.krb5.internal.crypto.DesCbcEType, sun.security.krb5.internal.crypto.EType
    public /* bridge */ /* synthetic */ byte[] encrypt(byte[] bArr, byte[] bArr2, int i2) throws KrbCryptoException {
        return super.encrypt(bArr, bArr2, i2);
    }

    @Override // sun.security.krb5.internal.crypto.DesCbcEType, sun.security.krb5.internal.crypto.EType
    public /* bridge */ /* synthetic */ int keySize() {
        return super.keySize();
    }

    @Override // sun.security.krb5.internal.crypto.DesCbcEType, sun.security.krb5.internal.crypto.EType
    public /* bridge */ /* synthetic */ int keyType() {
        return super.keyType();
    }

    @Override // sun.security.krb5.internal.crypto.DesCbcEType, sun.security.krb5.internal.crypto.EType
    public /* bridge */ /* synthetic */ int blockSize() {
        return super.blockSize();
    }

    @Override // sun.security.krb5.internal.crypto.EType
    public int eType() {
        return 3;
    }

    @Override // sun.security.krb5.internal.crypto.EType
    public int minimumPadSize() {
        return 0;
    }

    @Override // sun.security.krb5.internal.crypto.EType
    public int confounderSize() {
        return 8;
    }

    @Override // sun.security.krb5.internal.crypto.EType
    public int checksumType() {
        return 7;
    }

    @Override // sun.security.krb5.internal.crypto.EType
    public int checksumSize() {
        return 16;
    }

    @Override // sun.security.krb5.internal.crypto.DesCbcEType
    protected byte[] calculateChecksum(byte[] bArr, int i2) throws KrbCryptoException {
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
}
