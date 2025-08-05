package sun.security.krb5.internal.crypto;

import java.security.GeneralSecurityException;
import sun.security.krb5.KrbCryptoException;
import sun.security.krb5.internal.crypto.dk.ArcFourCrypto;

/* loaded from: rt.jar:sun/security/krb5/internal/crypto/ArcFourHmac.class */
public class ArcFourHmac {
    private static final ArcFourCrypto CRYPTO = new ArcFourCrypto(128);

    private ArcFourHmac() {
    }

    public static byte[] stringToKey(char[] cArr) throws GeneralSecurityException {
        return CRYPTO.stringToKey(cArr);
    }

    public static int getChecksumLength() {
        return CRYPTO.getChecksumLength();
    }

    public static byte[] calculateChecksum(byte[] bArr, int i2, byte[] bArr2, int i3, int i4) throws GeneralSecurityException {
        return CRYPTO.calculateChecksum(bArr, i2, bArr2, i3, i4);
    }

    public static byte[] encryptSeq(byte[] bArr, int i2, byte[] bArr2, byte[] bArr3, int i3, int i4) throws GeneralSecurityException, KrbCryptoException {
        return CRYPTO.encryptSeq(bArr, i2, bArr2, bArr3, i3, i4);
    }

    public static byte[] decryptSeq(byte[] bArr, int i2, byte[] bArr2, byte[] bArr3, int i3, int i4) throws GeneralSecurityException, KrbCryptoException {
        return CRYPTO.decryptSeq(bArr, i2, bArr2, bArr3, i3, i4);
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

    public static byte[] decryptRaw(byte[] bArr, int i2, byte[] bArr2, byte[] bArr3, int i3, int i4, byte[] bArr4) throws GeneralSecurityException {
        return CRYPTO.decryptRaw(bArr, i2, bArr2, bArr3, i3, i4, bArr4);
    }
}
