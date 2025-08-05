package sun.security.krb5.internal.crypto;

import java.security.InvalidKeyException;
import java.security.MessageDigest;
import javax.crypto.spec.DESKeySpec;
import sun.security.krb5.Confounder;
import sun.security.krb5.KrbCryptoException;

/* loaded from: rt.jar:sun/security/krb5/internal/crypto/RsaMd5DesCksumType.class */
public final class RsaMd5DesCksumType extends CksumType {
    @Override // sun.security.krb5.internal.crypto.CksumType
    public int confounderSize() {
        return 8;
    }

    @Override // sun.security.krb5.internal.crypto.CksumType
    public int cksumType() {
        return 8;
    }

    @Override // sun.security.krb5.internal.crypto.CksumType
    public boolean isKeyed() {
        return true;
    }

    @Override // sun.security.krb5.internal.crypto.CksumType
    public int cksumSize() {
        return 24;
    }

    @Override // sun.security.krb5.internal.crypto.CksumType
    public int keyType() {
        return 1;
    }

    @Override // sun.security.krb5.internal.crypto.CksumType
    public int keySize() {
        return 8;
    }

    @Override // sun.security.krb5.internal.crypto.CksumType
    public byte[] calculateChecksum(byte[] bArr, int i2, byte[] bArr2, int i3) throws KrbCryptoException {
        byte[] bArr3 = new byte[i2 + confounderSize()];
        byte[] bArrBytes = Confounder.bytes(confounderSize());
        System.arraycopy(bArrBytes, 0, bArr3, 0, confounderSize());
        System.arraycopy(bArr, 0, bArr3, confounderSize(), i2);
        byte[] bArrCalculateRawChecksum = calculateRawChecksum(bArr3, bArr3.length);
        byte[] bArr4 = new byte[cksumSize()];
        System.arraycopy(bArrBytes, 0, bArr4, 0, confounderSize());
        System.arraycopy(bArrCalculateRawChecksum, 0, bArr4, confounderSize(), cksumSize() - confounderSize());
        byte[] bArr5 = new byte[keySize()];
        System.arraycopy(bArr2, 0, bArr5, 0, bArr2.length);
        for (int i4 = 0; i4 < bArr5.length; i4++) {
            bArr5[i4] = (byte) (bArr5[i4] ^ 240);
        }
        try {
            if (DESKeySpec.isWeak(bArr5, 0)) {
                bArr5[7] = (byte) (bArr5[7] ^ 240);
            }
        } catch (InvalidKeyException e2) {
        }
        byte[] bArr6 = new byte[bArr5.length];
        byte[] bArr7 = new byte[bArr4.length];
        Des.cbc_encrypt(bArr4, bArr7, bArr5, bArr6, true);
        return bArr7;
    }

    @Override // sun.security.krb5.internal.crypto.CksumType
    public boolean verifyChecksum(byte[] bArr, int i2, byte[] bArr2, byte[] bArr3, int i3) throws KrbCryptoException {
        byte[] bArrDecryptKeyedChecksum = decryptKeyedChecksum(bArr3, bArr2);
        byte[] bArr4 = new byte[i2 + confounderSize()];
        System.arraycopy(bArrDecryptKeyedChecksum, 0, bArr4, 0, confounderSize());
        System.arraycopy(bArr, 0, bArr4, confounderSize(), i2);
        byte[] bArrCalculateRawChecksum = calculateRawChecksum(bArr4, bArr4.length);
        byte[] bArr5 = new byte[cksumSize() - confounderSize()];
        System.arraycopy(bArrDecryptKeyedChecksum, confounderSize(), bArr5, 0, cksumSize() - confounderSize());
        return isChecksumEqual(bArr5, bArrCalculateRawChecksum);
    }

    private byte[] decryptKeyedChecksum(byte[] bArr, byte[] bArr2) throws KrbCryptoException {
        byte[] bArr3 = new byte[keySize()];
        System.arraycopy(bArr2, 0, bArr3, 0, bArr2.length);
        for (int i2 = 0; i2 < bArr3.length; i2++) {
            bArr3[i2] = (byte) (bArr3[i2] ^ 240);
        }
        try {
            if (DESKeySpec.isWeak(bArr3, 0)) {
                bArr3[7] = (byte) (bArr3[7] ^ 240);
            }
        } catch (InvalidKeyException e2) {
        }
        byte[] bArr4 = new byte[bArr3.length];
        byte[] bArr5 = new byte[bArr.length];
        Des.cbc_encrypt(bArr, bArr5, bArr3, bArr4, false);
        return bArr5;
    }

    private byte[] calculateRawChecksum(byte[] bArr, int i2) throws KrbCryptoException {
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
