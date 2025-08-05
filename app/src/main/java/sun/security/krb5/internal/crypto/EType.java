package sun.security.krb5.internal.crypto;

import java.util.ArrayList;
import java.util.Arrays;
import javax.crypto.Cipher;
import sun.security.krb5.Config;
import sun.security.krb5.EncryptionKey;
import sun.security.krb5.KrbCryptoException;
import sun.security.krb5.KrbException;
import sun.security.krb5.internal.KdcErrException;
import sun.security.krb5.internal.Krb5;
import sun.security.krb5.internal.KrbApErrException;

/* loaded from: rt.jar:sun/security/krb5/internal/crypto/EType.class */
public abstract class EType {
    private static final boolean DEBUG = Krb5.DEBUG;
    private static boolean allowWeakCrypto;
    private static final int[] BUILTIN_ETYPES;
    private static final int[] BUILTIN_ETYPES_NOAES256;

    public abstract int eType();

    public abstract int minimumPadSize();

    public abstract int confounderSize();

    public abstract int checksumType();

    public abstract int checksumSize();

    public abstract int blockSize();

    public abstract int keyType();

    public abstract int keySize();

    public abstract byte[] encrypt(byte[] bArr, byte[] bArr2, int i2) throws KrbCryptoException;

    public abstract byte[] encrypt(byte[] bArr, byte[] bArr2, byte[] bArr3, int i2) throws KrbCryptoException;

    public abstract byte[] decrypt(byte[] bArr, byte[] bArr2, int i2) throws KrbCryptoException, KrbApErrException;

    public abstract byte[] decrypt(byte[] bArr, byte[] bArr2, byte[] bArr3, int i2) throws KrbCryptoException, KrbApErrException;

    static {
        initStatic();
        BUILTIN_ETYPES = new int[]{18, 17, 16, 23, 1, 3};
        BUILTIN_ETYPES_NOAES256 = new int[]{17, 16, 23, 1, 3};
    }

    public static void initStatic() {
        boolean z2 = false;
        try {
            String str = Config.getInstance().get("libdefaults", "allow_weak_crypto");
            if (str != null) {
                if (str.equals("true")) {
                    z2 = true;
                }
            }
        } catch (Exception e2) {
            if (DEBUG) {
                System.out.println("Exception in getting allow_weak_crypto, using default value " + e2.getMessage());
            }
        }
        allowWeakCrypto = z2;
    }

    public static EType getInstance(int i2) throws KdcErrException {
        EType arcFourHmacEType;
        String str;
        switch (i2) {
            case 0:
                arcFourHmacEType = new NullEType();
                str = "sun.security.krb5.internal.crypto.NullEType";
                break;
            case 1:
                arcFourHmacEType = new DesCbcCrcEType();
                str = "sun.security.krb5.internal.crypto.DesCbcCrcEType";
                break;
            case 2:
            case 4:
            case 5:
            case 6:
            case 7:
            case 8:
            case 9:
            case 10:
            case 11:
            case 12:
            case 13:
            case 14:
            case 15:
            case 19:
            case 20:
            case 21:
            case 22:
            default:
                throw new KdcErrException(14, "encryption type = " + toString(i2) + " (" + i2 + ")");
            case 3:
                arcFourHmacEType = new DesCbcMd5EType();
                str = "sun.security.krb5.internal.crypto.DesCbcMd5EType";
                break;
            case 16:
                arcFourHmacEType = new Des3CbcHmacSha1KdEType();
                str = "sun.security.krb5.internal.crypto.Des3CbcHmacSha1KdEType";
                break;
            case 17:
                arcFourHmacEType = new Aes128CtsHmacSha1EType();
                str = "sun.security.krb5.internal.crypto.Aes128CtsHmacSha1EType";
                break;
            case 18:
                arcFourHmacEType = new Aes256CtsHmacSha1EType();
                str = "sun.security.krb5.internal.crypto.Aes256CtsHmacSha1EType";
                break;
            case 23:
                arcFourHmacEType = new ArcFourHmacEType();
                str = "sun.security.krb5.internal.crypto.ArcFourHmacEType";
                break;
        }
        if (DEBUG) {
            System.out.println(">>> EType: " + str);
        }
        return arcFourHmacEType;
    }

    public int dataSize(byte[] bArr) {
        return bArr.length - startOfData();
    }

    public int padSize(byte[] bArr) {
        return ((bArr.length - confounderSize()) - checksumSize()) - dataSize(bArr);
    }

    public int startOfChecksum() {
        return confounderSize();
    }

    public int startOfData() {
        return confounderSize() + checksumSize();
    }

    public int startOfPad(byte[] bArr) {
        return confounderSize() + checksumSize() + dataSize(bArr);
    }

    public byte[] decryptedData(byte[] bArr) {
        int iDataSize = dataSize(bArr);
        byte[] bArr2 = new byte[iDataSize];
        System.arraycopy(bArr, startOfData(), bArr2, 0, iDataSize);
        return bArr2;
    }

    public static int[] getBuiltInDefaults() {
        int[] iArr;
        int maxAllowedKeyLength = 0;
        try {
            maxAllowedKeyLength = Cipher.getMaxAllowedKeyLength("AES");
        } catch (Exception e2) {
        }
        if (maxAllowedKeyLength < 256) {
            iArr = BUILTIN_ETYPES_NOAES256;
        } else {
            iArr = BUILTIN_ETYPES;
        }
        if (!allowWeakCrypto) {
            return Arrays.copyOfRange(iArr, 0, iArr.length - 2);
        }
        return iArr;
    }

    public static int[] getDefaults(String str) throws KrbException {
        try {
            return Config.getInstance().defaultEtype(str);
        } catch (KrbException e2) {
            if (DEBUG) {
                System.out.println("Exception while getting " + str + e2.getMessage());
                System.out.println("Using default builtin etypes");
            }
            return getBuiltInDefaults();
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    public static int[] getDefaults(String str, EncryptionKey[] encryptionKeyArr) throws KrbException {
        int[] defaults = getDefaults(str);
        ArrayList arrayList = new ArrayList(defaults.length);
        for (int i2 = 0; i2 < defaults.length; i2++) {
            if (EncryptionKey.findKey(defaults[i2], encryptionKeyArr) != null) {
                arrayList.add(Integer.valueOf(defaults[i2]));
            }
        }
        int size = arrayList.size();
        if (size <= 0) {
            StringBuffer stringBuffer = new StringBuffer();
            for (EncryptionKey encryptionKey : encryptionKeyArr) {
                stringBuffer.append(toString(encryptionKey.getEType()));
                stringBuffer.append(" ");
            }
            throw new KrbException("Do not have keys of types listed in " + str + " available; only have keys of following type: " + stringBuffer.toString());
        }
        int[] iArr = new int[size];
        for (int i3 = 0; i3 < size; i3++) {
            iArr[i3] = ((Integer) arrayList.get(i3)).intValue();
        }
        return iArr;
    }

    public static boolean isSupported(int i2, int[] iArr) {
        for (int i3 : iArr) {
            if (i2 == i3) {
                return true;
            }
        }
        return false;
    }

    public static boolean isSupported(int i2) {
        return isSupported(i2, getBuiltInDefaults());
    }

    public static boolean isNewer(int i2) {
        return (i2 == 1 || i2 == 2 || i2 == 3 || i2 == 16 || i2 == 23 || i2 == 24) ? false : true;
    }

    public static String toString(int i2) {
        switch (i2) {
            case 0:
                return "NULL";
            case 1:
                return "DES CBC mode with CRC-32";
            case 2:
                return "DES CBC mode with MD4";
            case 3:
                return "DES CBC mode with MD5";
            case 4:
                return "reserved";
            case 5:
                return "DES3 CBC mode with MD5";
            case 6:
                return "reserved";
            case 7:
                return "DES3 CBC mode with SHA1";
            case 8:
            case 19:
            case 20:
            case 21:
            case 22:
            default:
                return "Unknown (" + i2 + ")";
            case 9:
                return "DSA with SHA1- Cms0ID";
            case 10:
                return "MD5 with RSA encryption - Cms0ID";
            case 11:
                return "SHA1 with RSA encryption - Cms0ID";
            case 12:
                return "RC2 CBC mode with Env0ID";
            case 13:
                return "RSA encryption with Env0ID";
            case 14:
                return "RSAES-0AEP-ENV-0ID";
            case 15:
                return "DES-EDE3-CBC-ENV-0ID";
            case 16:
                return "DES3 CBC mode with SHA1-KD";
            case 17:
                return "AES128 CTS mode with HMAC SHA1-96";
            case 18:
                return "AES256 CTS mode with HMAC SHA1-96";
            case 23:
                return "RC4 with HMAC";
            case 24:
                return "RC4 with HMAC EXP";
        }
    }
}
