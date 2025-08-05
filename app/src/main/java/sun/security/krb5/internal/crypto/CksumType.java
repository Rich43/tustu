package sun.security.krb5.internal.crypto;

import sun.security.krb5.Checksum;
import sun.security.krb5.KrbCryptoException;
import sun.security.krb5.internal.KdcErrException;
import sun.security.krb5.internal.Krb5;

/* loaded from: rt.jar:sun/security/krb5/internal/crypto/CksumType.class */
public abstract class CksumType {
    private static boolean DEBUG = Krb5.DEBUG;

    public abstract int confounderSize();

    public abstract int cksumType();

    public abstract boolean isKeyed();

    public abstract int cksumSize();

    public abstract int keyType();

    public abstract int keySize();

    public abstract byte[] calculateChecksum(byte[] bArr, int i2, byte[] bArr2, int i3) throws KrbCryptoException;

    public abstract boolean verifyChecksum(byte[] bArr, int i2, byte[] bArr2, byte[] bArr3, int i3) throws KrbCryptoException;

    public static CksumType getInstance(int i2) throws KdcErrException {
        CksumType hmacMd5ArcFourCksumType;
        String str;
        switch (i2) {
            case Checksum.CKSUMTYPE_HMAC_MD5_ARCFOUR /* -138 */:
                hmacMd5ArcFourCksumType = new HmacMd5ArcFourCksumType();
                str = "sun.security.krb5.internal.crypto.HmacMd5ArcFourCksumType";
                break;
            case 1:
                hmacMd5ArcFourCksumType = new Crc32CksumType();
                str = "sun.security.krb5.internal.crypto.Crc32CksumType";
                break;
            case 2:
            case 3:
            case 6:
            default:
                throw new KdcErrException(15);
            case 4:
                hmacMd5ArcFourCksumType = new DesMacCksumType();
                str = "sun.security.krb5.internal.crypto.DesMacCksumType";
                break;
            case 5:
                hmacMd5ArcFourCksumType = new DesMacKCksumType();
                str = "sun.security.krb5.internal.crypto.DesMacKCksumType";
                break;
            case 7:
                hmacMd5ArcFourCksumType = new RsaMd5CksumType();
                str = "sun.security.krb5.internal.crypto.RsaMd5CksumType";
                break;
            case 8:
                hmacMd5ArcFourCksumType = new RsaMd5DesCksumType();
                str = "sun.security.krb5.internal.crypto.RsaMd5DesCksumType";
                break;
            case 12:
                hmacMd5ArcFourCksumType = new HmacSha1Des3KdCksumType();
                str = "sun.security.krb5.internal.crypto.HmacSha1Des3KdCksumType";
                break;
            case 15:
                hmacMd5ArcFourCksumType = new HmacSha1Aes128CksumType();
                str = "sun.security.krb5.internal.crypto.HmacSha1Aes128CksumType";
                break;
            case 16:
                hmacMd5ArcFourCksumType = new HmacSha1Aes256CksumType();
                str = "sun.security.krb5.internal.crypto.HmacSha1Aes256CksumType";
                break;
        }
        if (DEBUG) {
            System.out.println(">>> CksumType: " + str);
        }
        return hmacMd5ArcFourCksumType;
    }

    public static boolean isChecksumEqual(byte[] bArr, byte[] bArr2) {
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
}
