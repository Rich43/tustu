package sun.security.krb5.internal.crypto;

import sun.security.krb5.KrbCryptoException;
import sun.security.krb5.internal.KrbApErrException;

/* loaded from: rt.jar:sun/security/krb5/internal/crypto/DesCbcCrcEType.class */
public class DesCbcCrcEType extends DesCbcEType {
    @Override // sun.security.krb5.internal.crypto.DesCbcEType, sun.security.krb5.internal.crypto.EType
    public /* bridge */ /* synthetic */ byte[] decrypt(byte[] bArr, byte[] bArr2, byte[] bArr3, int i2) throws KrbCryptoException, KrbApErrException {
        return super.decrypt(bArr, bArr2, bArr3, i2);
    }

    @Override // sun.security.krb5.internal.crypto.DesCbcEType, sun.security.krb5.internal.crypto.EType
    public /* bridge */ /* synthetic */ byte[] encrypt(byte[] bArr, byte[] bArr2, byte[] bArr3, int i2) throws KrbCryptoException {
        return super.encrypt(bArr, bArr2, bArr3, i2);
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
        return 1;
    }

    @Override // sun.security.krb5.internal.crypto.EType
    public int minimumPadSize() {
        return 4;
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
        return 4;
    }

    @Override // sun.security.krb5.internal.crypto.DesCbcEType, sun.security.krb5.internal.crypto.EType
    public byte[] encrypt(byte[] bArr, byte[] bArr2, int i2) throws KrbCryptoException {
        return encrypt(bArr, bArr2, bArr2, i2);
    }

    @Override // sun.security.krb5.internal.crypto.DesCbcEType, sun.security.krb5.internal.crypto.EType
    public byte[] decrypt(byte[] bArr, byte[] bArr2, int i2) throws KrbCryptoException, KrbApErrException {
        return decrypt(bArr, bArr2, bArr2, i2);
    }

    @Override // sun.security.krb5.internal.crypto.DesCbcEType
    protected byte[] calculateChecksum(byte[] bArr, int i2) {
        return crc32.byte2crc32sum_bytes(bArr, i2);
    }
}
