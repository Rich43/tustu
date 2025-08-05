package sun.security.krb5.internal.crypto;

import java.security.GeneralSecurityException;
import sun.security.krb5.KrbCryptoException;
import sun.security.krb5.internal.crypto.dk.AesDkCrypto;

/* loaded from: rt.jar:sun/security/krb5/internal/crypto/Aes128.class */
public class Aes128 {
    private static final AesDkCrypto CRYPTO = new AesDkCrypto(128);

    private Aes128() {
    }

    public static byte[] stringToKey(char[] cArr, String str, byte[] bArr) throws GeneralSecurityException {
        return CRYPTO.stringToKey(cArr, str, bArr);
    }

    public static int getChecksumLength() {
        return CRYPTO.getChecksumLength();
    }

    public static byte[] calculateChecksum(byte[] bArr, int i2, byte[] bArr2, int i3, int i4) throws GeneralSecurityException {
        return CRYPTO.calculateChecksum(bArr, i2, bArr2, i3, i4);
    }

    public static byte[] encrypt(byte[] bArr, int i2, byte[] bArr2, byte[] bArr3, int i3, int i4) throws GeneralSecurityException, KrbCryptoException {
        return CRYPTO.encrypt(bArr, i2, bArr2, null, bArr3, i3, i4);
    }

    public static byte[] encryptRaw(byte[] bArr, int i2, byte[] bArr2, byte[] bArr3, int i3, int i4) throws GeneralSecurityException, KrbCryptoException {
        return CRYPTO.encryptRaw(bArr, i2, bArr2, bArr3, i3, i4);
    }

    public static byte[] decrypt(byte[] bArr, int i2, byte[] bArr2, byte[] bArr3, int i3, int i4) throws GeneralSecurityException {
        return CRYPTO.decrypt(bArr, i2, bArr2, bArr3, i3, i4);
    }

    public static byte[] decryptRaw(byte[] bArr, int i2, byte[] bArr2, byte[] bArr3, int i3, int i4) throws GeneralSecurityException {
        return CRYPTO.decryptRaw(bArr, i2, bArr2, bArr3, i3, i4);
    }
}
