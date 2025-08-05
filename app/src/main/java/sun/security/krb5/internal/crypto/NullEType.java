package sun.security.krb5.internal.crypto;

import sun.security.krb5.internal.KrbApErrException;

/* loaded from: rt.jar:sun/security/krb5/internal/crypto/NullEType.class */
public class NullEType extends EType {
    @Override // sun.security.krb5.internal.crypto.EType
    public int eType() {
        return 0;
    }

    @Override // sun.security.krb5.internal.crypto.EType
    public int minimumPadSize() {
        return 0;
    }

    @Override // sun.security.krb5.internal.crypto.EType
    public int confounderSize() {
        return 0;
    }

    @Override // sun.security.krb5.internal.crypto.EType
    public int checksumType() {
        return 0;
    }

    @Override // sun.security.krb5.internal.crypto.EType
    public int checksumSize() {
        return 0;
    }

    @Override // sun.security.krb5.internal.crypto.EType
    public int blockSize() {
        return 1;
    }

    @Override // sun.security.krb5.internal.crypto.EType
    public int keyType() {
        return 0;
    }

    @Override // sun.security.krb5.internal.crypto.EType
    public int keySize() {
        return 0;
    }

    @Override // sun.security.krb5.internal.crypto.EType
    public byte[] encrypt(byte[] bArr, byte[] bArr2, int i2) {
        byte[] bArr3 = new byte[bArr.length];
        System.arraycopy(bArr, 0, bArr3, 0, bArr.length);
        return bArr3;
    }

    @Override // sun.security.krb5.internal.crypto.EType
    public byte[] encrypt(byte[] bArr, byte[] bArr2, byte[] bArr3, int i2) {
        byte[] bArr4 = new byte[bArr.length];
        System.arraycopy(bArr, 0, bArr4, 0, bArr.length);
        return bArr4;
    }

    @Override // sun.security.krb5.internal.crypto.EType
    public byte[] decrypt(byte[] bArr, byte[] bArr2, int i2) throws KrbApErrException {
        return (byte[]) bArr.clone();
    }

    @Override // sun.security.krb5.internal.crypto.EType
    public byte[] decrypt(byte[] bArr, byte[] bArr2, byte[] bArr3, int i2) throws KrbApErrException {
        return (byte[]) bArr.clone();
    }
}
