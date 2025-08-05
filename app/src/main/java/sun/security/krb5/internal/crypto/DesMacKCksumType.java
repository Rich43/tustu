package sun.security.krb5.internal.crypto;

import java.security.InvalidKeyException;
import javax.crypto.spec.DESKeySpec;
import sun.security.krb5.KrbCryptoException;

/* loaded from: rt.jar:sun/security/krb5/internal/crypto/DesMacKCksumType.class */
public class DesMacKCksumType extends CksumType {
    @Override // sun.security.krb5.internal.crypto.CksumType
    public int confounderSize() {
        return 0;
    }

    @Override // sun.security.krb5.internal.crypto.CksumType
    public int cksumType() {
        return 5;
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
        try {
            if (DESKeySpec.isWeak(bArr2, 0)) {
                bArr2[7] = (byte) (bArr2[7] ^ 240);
            }
        } catch (InvalidKeyException e2) {
        }
        byte[] bArr3 = new byte[bArr2.length];
        System.arraycopy(bArr2, 0, bArr3, 0, bArr2.length);
        return Des.des_cksum(bArr3, bArr, bArr2);
    }

    @Override // sun.security.krb5.internal.crypto.CksumType
    public boolean verifyChecksum(byte[] bArr, int i2, byte[] bArr2, byte[] bArr3, int i3) throws KrbCryptoException {
        return isChecksumEqual(bArr3, calculateChecksum(bArr, bArr.length, bArr2, i3));
    }
}
