package sun.security.krb5.internal.crypto;

import java.security.AccessController;
import java.security.GeneralSecurityException;
import java.util.Arrays;
import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import sun.security.action.GetPropertyAction;
import sun.security.krb5.KrbCryptoException;

/* loaded from: rt.jar:sun/security/krb5/internal/crypto/Des.class */
public final class Des {
    private static final String CHARSET = (String) AccessController.doPrivileged(new GetPropertyAction("sun.security.krb5.msinterop.des.s2kcharset"));
    private static final long[] bad_keys = {72340172838076673L, -72340172838076674L, 2242545357980376863L, -2242545357980376864L, 143554428589179390L, -143554428589179391L, 2296870857142767345L, -2296870857142767346L, 135110050437988849L, -2305315235293957887L, 2305315235293957886L, -135110050437988850L, 80784550989267214L, 2234100979542855169L, -2234100979542855170L, -80784550989267215L};
    private static final byte[] good_parity = {1, 1, 2, 2, 4, 4, 7, 7, 8, 8, 11, 11, 13, 13, 14, 14, 16, 16, 19, 19, 21, 21, 22, 22, 25, 25, 26, 26, 28, 28, 31, 31, 32, 32, 35, 35, 37, 37, 38, 38, 41, 41, 42, 42, 44, 44, 47, 47, 49, 49, 50, 50, 52, 52, 55, 55, 56, 56, 59, 59, 61, 61, 62, 62, 64, 64, 67, 67, 69, 69, 70, 70, 73, 73, 74, 74, 76, 76, 79, 79, 81, 81, 82, 82, 84, 84, 87, 87, 88, 88, 91, 91, 93, 93, 94, 94, 97, 97, 98, 98, 100, 100, 103, 103, 104, 104, 107, 107, 109, 109, 110, 110, 112, 112, 115, 115, 117, 117, 118, 118, 121, 121, 122, 122, 124, 124, Byte.MAX_VALUE, Byte.MAX_VALUE, Byte.MIN_VALUE, Byte.MIN_VALUE, -125, -125, -123, -123, -122, -122, -119, -119, -118, -118, -116, -116, -113, -113, -111, -111, -110, -110, -108, -108, -105, -105, -104, -104, -101, -101, -99, -99, -98, -98, -95, -95, -94, -94, -92, -92, -89, -89, -88, -88, -85, -85, -83, -83, -82, -82, -80, -80, -77, -77, -75, -75, -74, -74, -71, -71, -70, -70, -68, -68, -65, -65, -63, -63, -62, -62, -60, -60, -57, -57, -56, -56, -53, -53, -51, -51, -50, -50, -48, -48, -45, -45, -43, -43, -42, -42, -39, -39, -38, -38, -36, -36, -33, -33, -32, -32, -29, -29, -27, -27, -26, -26, -23, -23, -22, -22, -20, -20, -17, -17, -15, -15, -14, -14, -12, -12, -9, -9, -8, -8, -5, -5, -3, -3, -2, -2};

    public static final byte[] set_parity(byte[] bArr) {
        for (int i2 = 0; i2 < 8; i2++) {
            bArr[i2] = good_parity[bArr[i2] & 255];
        }
        return bArr;
    }

    public static final long set_parity(long j2) {
        return octet2long(set_parity(long2octet(j2)));
    }

    public static final boolean bad_key(long j2) {
        for (int i2 = 0; i2 < bad_keys.length; i2++) {
            if (bad_keys[i2] == j2) {
                return true;
            }
        }
        return false;
    }

    public static final boolean bad_key(byte[] bArr) {
        return bad_key(octet2long(bArr));
    }

    public static long octet2long(byte[] bArr) {
        return octet2long(bArr, 0);
    }

    public static long octet2long(byte[] bArr, int i2) {
        long j2 = 0;
        for (int i3 = 0; i3 < 8; i3++) {
            if (i3 + i2 < bArr.length) {
                j2 |= (bArr[i3 + i2] & 255) << ((7 - i3) * 8);
            }
        }
        return j2;
    }

    public static byte[] long2octet(long j2) {
        byte[] bArr = new byte[8];
        for (int i2 = 0; i2 < 8; i2++) {
            bArr[i2] = (byte) ((j2 >>> ((7 - i2) * 8)) & 255);
        }
        return bArr;
    }

    public static void long2octet(long j2, byte[] bArr) {
        long2octet(j2, bArr, 0);
    }

    public static void long2octet(long j2, byte[] bArr, int i2) {
        for (int i3 = 0; i3 < 8; i3++) {
            if (i3 + i2 < bArr.length) {
                bArr[i3 + i2] = (byte) ((j2 >>> ((7 - i3) * 8)) & 255);
            }
        }
    }

    public static void cbc_encrypt(byte[] bArr, byte[] bArr2, byte[] bArr3, byte[] bArr4, boolean z2) throws KrbCryptoException {
        try {
            Cipher cipher = Cipher.getInstance("DES/CBC/NoPadding");
            IvParameterSpec ivParameterSpec = new IvParameterSpec(bArr4);
            SecretKeySpec secretKeySpec = new SecretKeySpec(bArr3, "DES");
            try {
                SecretKeyFactory.getInstance("DES");
                if (z2) {
                    cipher.init(1, secretKeySpec, ivParameterSpec);
                } else {
                    cipher.init(2, secretKeySpec, ivParameterSpec);
                }
                byte[] bArrDoFinal = cipher.doFinal(bArr);
                System.arraycopy(bArrDoFinal, 0, bArr2, 0, bArrDoFinal.length);
            } catch (GeneralSecurityException e2) {
                KrbCryptoException krbCryptoException = new KrbCryptoException(e2.getMessage());
                krbCryptoException.initCause(e2);
                throw krbCryptoException;
            }
        } catch (GeneralSecurityException e3) {
            KrbCryptoException krbCryptoException2 = new KrbCryptoException("JCE provider may not be installed. " + e3.getMessage());
            krbCryptoException2.initCause(e3);
            throw krbCryptoException2;
        }
    }

    public static long char_to_key(char[] cArr) throws KrbCryptoException {
        byte[] bytes;
        long j2 = 0;
        Object[] objArr = null;
        try {
            if (CHARSET == null) {
                bytes = new String(cArr).getBytes();
            } else {
                bytes = new String(cArr).getBytes(CHARSET);
            }
            byte[] bArrPad = pad(bytes);
            byte[] bArr = new byte[8];
            int length = (bArrPad.length / 8) + (bArrPad.length % 8 == 0 ? 0 : 1);
            for (int i2 = 0; i2 < length; i2++) {
                long jOctet2long = octet2long(bArrPad, i2 * 8) & 9187201950435737471L;
                if (i2 % 2 == 1) {
                    long j3 = 0;
                    for (int i3 = 0; i3 < 64; i3++) {
                        j3 |= ((jOctet2long & (1 << i3)) >>> i3) << (63 - i3);
                    }
                    jOctet2long = j3 >>> 1;
                }
                j2 ^= jOctet2long << 1;
            }
            long jOctet2long2 = set_parity(j2);
            if (bad_key(jOctet2long2)) {
                byte[] bArrLong2octet = long2octet(jOctet2long2);
                bArrLong2octet[7] = (byte) (bArrLong2octet[7] ^ 240);
                jOctet2long2 = octet2long(bArrLong2octet);
            }
            long jOctet2long3 = octet2long(set_parity(des_cksum(long2octet(jOctet2long2), bArrPad, long2octet(jOctet2long2))));
            if (bad_key(jOctet2long3)) {
                byte[] bArrLong2octet2 = long2octet(jOctet2long3);
                bArrLong2octet2[7] = (byte) (bArrLong2octet2[7] ^ 240);
                jOctet2long3 = octet2long(bArrLong2octet2);
            }
            if (bytes != null) {
                Arrays.fill(bytes, 0, bytes.length, (byte) 0);
            }
            if (bArrPad != null) {
                Arrays.fill(bArrPad, 0, bArrPad.length, (byte) 0);
            }
            return jOctet2long3;
        } catch (Exception e2) {
            if (0 != 0) {
                Arrays.fill((byte[]) null, 0, objArr.length, (byte) 0);
            }
            KrbCryptoException krbCryptoException = new KrbCryptoException("Unable to convert passwd, " + ((Object) e2));
            krbCryptoException.initCause(e2);
            throw krbCryptoException;
        }
    }

    public static byte[] des_cksum(byte[] bArr, byte[] bArr2, byte[] bArr3) throws KrbCryptoException {
        byte[] bArrDoFinal = new byte[8];
        try {
            Cipher cipher = Cipher.getInstance("DES/CBC/NoPadding");
            IvParameterSpec ivParameterSpec = new IvParameterSpec(bArr);
            SecretKeySpec secretKeySpec = new SecretKeySpec(bArr3, "DES");
            try {
                SecretKeyFactory.getInstance("DES");
                cipher.init(1, secretKeySpec, ivParameterSpec);
                for (int i2 = 0; i2 < bArr2.length / 8; i2++) {
                    bArrDoFinal = cipher.doFinal(bArr2, i2 * 8, 8);
                    cipher.init(1, secretKeySpec, new IvParameterSpec(bArrDoFinal));
                }
                return bArrDoFinal;
            } catch (GeneralSecurityException e2) {
                KrbCryptoException krbCryptoException = new KrbCryptoException(e2.getMessage());
                krbCryptoException.initCause(e2);
                throw krbCryptoException;
            }
        } catch (Exception e3) {
            KrbCryptoException krbCryptoException2 = new KrbCryptoException("JCE provider may not be installed. " + e3.getMessage());
            krbCryptoException2.initCause(e3);
            throw krbCryptoException2;
        }
    }

    static byte[] pad(byte[] bArr) {
        int length = bArr.length < 8 ? bArr.length : bArr.length % 8;
        if (length == 0) {
            return bArr;
        }
        byte[] bArr2 = new byte[(8 - length) + bArr.length];
        for (int length2 = bArr2.length - 1; length2 > bArr.length - 1; length2--) {
            bArr2[length2] = 0;
        }
        System.arraycopy(bArr, 0, bArr2, 0, bArr.length);
        return bArr2;
    }

    public static byte[] string_to_key_bytes(char[] cArr) throws KrbCryptoException {
        return long2octet(char_to_key(cArr));
    }
}
