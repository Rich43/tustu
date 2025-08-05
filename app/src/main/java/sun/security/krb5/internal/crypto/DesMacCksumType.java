package sun.security.krb5.internal.crypto;

import java.security.InvalidKeyException;
import javax.crypto.spec.DESKeySpec;
import sun.security.krb5.Confounder;
import sun.security.krb5.KrbCryptoException;

/* loaded from: rt.jar:sun/security/krb5/internal/crypto/DesMacCksumType.class */
public class DesMacCksumType extends CksumType {
    @Override // sun.security.krb5.internal.crypto.CksumType
    public int confounderSize() {
        return 8;
    }

    @Override // sun.security.krb5.internal.crypto.CksumType
    public int cksumType() {
        return 4;
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
        try {
            if (DESKeySpec.isWeak(bArr2, 0)) {
                bArr2[7] = (byte) (bArr2[7] ^ 240);
            }
        } catch (InvalidKeyException e2) {
        }
        byte[] bArrDes_cksum = Des.des_cksum(new byte[bArr2.length], bArr3, bArr2);
        byte[] bArr4 = new byte[cksumSize()];
        System.arraycopy(bArrBytes, 0, bArr4, 0, confounderSize());
        System.arraycopy(bArrDes_cksum, 0, bArr4, confounderSize(), cksumSize() - confounderSize());
        byte[] bArr5 = new byte[keySize()];
        System.arraycopy(bArr2, 0, bArr5, 0, bArr2.length);
        for (int i4 = 0; i4 < bArr5.length; i4++) {
            bArr5[i4] = (byte) (bArr5[i4] ^ 240);
        }
        try {
            if (DESKeySpec.isWeak(bArr5, 0)) {
                bArr5[7] = (byte) (bArr5[7] ^ 240);
            }
        } catch (InvalidKeyException e3) {
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
        try {
            if (DESKeySpec.isWeak(bArr2, 0)) {
                bArr2[7] = (byte) (bArr2[7] ^ 240);
            }
        } catch (InvalidKeyException e2) {
        }
        byte[] bArrDes_cksum = Des.des_cksum(new byte[bArr2.length], bArr4, bArr2);
        byte[] bArr5 = new byte[cksumSize() - confounderSize()];
        System.arraycopy(bArrDecryptKeyedChecksum, confounderSize(), bArr5, 0, cksumSize() - confounderSize());
        return isChecksumEqual(bArr5, bArrDes_cksum);
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
}
