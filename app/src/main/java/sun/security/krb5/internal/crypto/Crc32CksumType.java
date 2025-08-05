package sun.security.krb5.internal.crypto;

/* loaded from: rt.jar:sun/security/krb5/internal/crypto/Crc32CksumType.class */
public class Crc32CksumType extends CksumType {
    @Override // sun.security.krb5.internal.crypto.CksumType
    public int confounderSize() {
        return 0;
    }

    @Override // sun.security.krb5.internal.crypto.CksumType
    public int cksumType() {
        return 1;
    }

    @Override // sun.security.krb5.internal.crypto.CksumType
    public boolean isKeyed() {
        return false;
    }

    @Override // sun.security.krb5.internal.crypto.CksumType
    public int cksumSize() {
        return 4;
    }

    @Override // sun.security.krb5.internal.crypto.CksumType
    public int keyType() {
        return 0;
    }

    @Override // sun.security.krb5.internal.crypto.CksumType
    public int keySize() {
        return 0;
    }

    @Override // sun.security.krb5.internal.crypto.CksumType
    public byte[] calculateChecksum(byte[] bArr, int i2, byte[] bArr2, int i3) {
        return crc32.byte2crc32sum_bytes(bArr, i2);
    }

    @Override // sun.security.krb5.internal.crypto.CksumType
    public boolean verifyChecksum(byte[] bArr, int i2, byte[] bArr2, byte[] bArr3, int i3) {
        return CksumType.isChecksumEqual(bArr3, crc32.byte2crc32sum_bytes(bArr));
    }

    public static byte[] int2quad(long j2) {
        byte[] bArr = new byte[4];
        for (int i2 = 0; i2 < 4; i2++) {
            bArr[i2] = (byte) ((j2 >>> (i2 * 8)) & 255);
        }
        return bArr;
    }

    public static long bytes2long(byte[] bArr) {
        return 0 | ((bArr[0] & 255) << 24) | ((bArr[1] & 255) << 16) | ((bArr[2] & 255) << 8) | (bArr[3] & 255);
    }
}
