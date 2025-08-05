package sun.security.krb5.internal.crypto;

import sun.security.krb5.Confounder;
import sun.security.krb5.KrbCryptoException;
import sun.security.krb5.internal.KrbApErrException;

/* loaded from: rt.jar:sun/security/krb5/internal/crypto/DesCbcEType.class */
abstract class DesCbcEType extends EType {
    protected abstract byte[] calculateChecksum(byte[] bArr, int i2) throws KrbCryptoException;

    DesCbcEType() {
    }

    @Override // sun.security.krb5.internal.crypto.EType
    public int blockSize() {
        return 8;
    }

    @Override // sun.security.krb5.internal.crypto.EType
    public int keyType() {
        return 1;
    }

    @Override // sun.security.krb5.internal.crypto.EType
    public int keySize() {
        return 8;
    }

    @Override // sun.security.krb5.internal.crypto.EType
    public byte[] encrypt(byte[] bArr, byte[] bArr2, int i2) throws KrbCryptoException {
        return encrypt(bArr, bArr2, new byte[keySize()], i2);
    }

    @Override // sun.security.krb5.internal.crypto.EType
    public byte[] encrypt(byte[] bArr, byte[] bArr2, byte[] bArr3, int i2) throws KrbCryptoException {
        byte[] bArr4;
        byte bBlockSize;
        if (bArr2.length > 8) {
            throw new KrbCryptoException("Invalid DES Key!");
        }
        int length = bArr.length + confounderSize() + checksumSize();
        if (length % blockSize() == 0) {
            bArr4 = new byte[length + blockSize()];
            bBlockSize = 8;
        } else {
            bArr4 = new byte[(length + blockSize()) - (length % blockSize())];
            bBlockSize = (byte) (blockSize() - (length % blockSize()));
        }
        for (int i3 = length; i3 < bArr4.length; i3++) {
            bArr4[i3] = bBlockSize;
        }
        System.arraycopy(Confounder.bytes(confounderSize()), 0, bArr4, 0, confounderSize());
        System.arraycopy(bArr, 0, bArr4, startOfData(), bArr.length);
        System.arraycopy(calculateChecksum(bArr4, bArr4.length), 0, bArr4, startOfChecksum(), checksumSize());
        byte[] bArr5 = new byte[bArr4.length];
        Des.cbc_encrypt(bArr4, bArr5, bArr2, bArr3, true);
        return bArr5;
    }

    @Override // sun.security.krb5.internal.crypto.EType
    public byte[] decrypt(byte[] bArr, byte[] bArr2, int i2) throws KrbCryptoException, KrbApErrException {
        return decrypt(bArr, bArr2, new byte[keySize()], i2);
    }

    @Override // sun.security.krb5.internal.crypto.EType
    public byte[] decrypt(byte[] bArr, byte[] bArr2, byte[] bArr3, int i2) throws KrbCryptoException, KrbApErrException {
        if (bArr2.length > 8) {
            throw new KrbCryptoException("Invalid DES Key!");
        }
        byte[] bArr4 = new byte[bArr.length];
        Des.cbc_encrypt(bArr, bArr4, bArr2, bArr3, false);
        if (!isChecksumValid(bArr4)) {
            throw new KrbApErrException(31);
        }
        return bArr4;
    }

    private void copyChecksumField(byte[] bArr, byte[] bArr2) {
        for (int i2 = 0; i2 < checksumSize(); i2++) {
            bArr[startOfChecksum() + i2] = bArr2[i2];
        }
    }

    private byte[] checksumField(byte[] bArr) {
        byte[] bArr2 = new byte[checksumSize()];
        for (int i2 = 0; i2 < checksumSize(); i2++) {
            bArr2[i2] = bArr[startOfChecksum() + i2];
        }
        return bArr2;
    }

    private void resetChecksumField(byte[] bArr) {
        for (int iStartOfChecksum = startOfChecksum(); iStartOfChecksum < startOfChecksum() + checksumSize(); iStartOfChecksum++) {
            bArr[iStartOfChecksum] = 0;
        }
    }

    private byte[] generateChecksum(byte[] bArr) throws KrbCryptoException {
        byte[] bArrChecksumField = checksumField(bArr);
        resetChecksumField(bArr);
        byte[] bArrCalculateChecksum = calculateChecksum(bArr, bArr.length);
        copyChecksumField(bArr, bArrChecksumField);
        return bArrCalculateChecksum;
    }

    private boolean isChecksumEqual(byte[] bArr, byte[] bArr2) {
        if (bArr == bArr2) {
            return true;
        }
        if (bArr == null && bArr2 != null) {
            return false;
        }
        if ((bArr != null && bArr2 == null) || bArr.length != bArr2.length) {
            return false;
        }
        for (int i2 = 0; i2 < bArr.length; i2++) {
            if (bArr[i2] != bArr2[i2]) {
                return false;
            }
        }
        return true;
    }

    protected boolean isChecksumValid(byte[] bArr) throws KrbCryptoException {
        return isChecksumEqual(checksumField(bArr), generateChecksum(bArr));
    }
}
