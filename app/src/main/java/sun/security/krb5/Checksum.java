package sun.security.krb5;

import java.io.IOException;
import java.math.BigInteger;
import java.util.Arrays;
import sun.security.krb5.internal.KdcErrException;
import sun.security.krb5.internal.Krb5;
import sun.security.krb5.internal.KrbApErrException;
import sun.security.krb5.internal.crypto.CksumType;
import sun.security.krb5.internal.crypto.EType;
import sun.security.util.DerInputStream;
import sun.security.util.DerOutputStream;
import sun.security.util.DerValue;

/* loaded from: rt.jar:sun/security/krb5/Checksum.class */
public class Checksum {
    private int cksumType;
    private byte[] checksum;
    public static final int CKSUMTYPE_NULL = 0;
    public static final int CKSUMTYPE_CRC32 = 1;
    public static final int CKSUMTYPE_RSA_MD4 = 2;
    public static final int CKSUMTYPE_RSA_MD4_DES = 3;
    public static final int CKSUMTYPE_DES_MAC = 4;
    public static final int CKSUMTYPE_DES_MAC_K = 5;
    public static final int CKSUMTYPE_RSA_MD4_DES_K = 6;
    public static final int CKSUMTYPE_RSA_MD5 = 7;
    public static final int CKSUMTYPE_RSA_MD5_DES = 8;
    public static final int CKSUMTYPE_HMAC_SHA1_DES3_KD = 12;
    public static final int CKSUMTYPE_HMAC_SHA1_96_AES128 = 15;
    public static final int CKSUMTYPE_HMAC_SHA1_96_AES256 = 16;
    public static final int CKSUMTYPE_HMAC_MD5_ARCFOUR = -138;
    static int CKSUMTYPE_DEFAULT;
    static int SAFECKSUMTYPE_DEFAULT;
    private static boolean DEBUG = Krb5.DEBUG;

    static {
        initStatic();
    }

    public static void initStatic() {
        Config config = null;
        try {
            config = Config.getInstance();
            String str = config.get("libdefaults", "default_checksum");
            if (str != null) {
                CKSUMTYPE_DEFAULT = Config.getType(str);
            } else {
                CKSUMTYPE_DEFAULT = -1;
            }
        } catch (Exception e2) {
            if (DEBUG) {
                System.out.println("Exception in getting default checksum value from the configuration. No default checksum set.");
                e2.printStackTrace();
            }
            CKSUMTYPE_DEFAULT = -1;
        }
        try {
            String str2 = config.get("libdefaults", "safe_checksum_type");
            if (str2 != null) {
                SAFECKSUMTYPE_DEFAULT = Config.getType(str2);
            } else {
                SAFECKSUMTYPE_DEFAULT = -1;
            }
        } catch (Exception e3) {
            if (DEBUG) {
                System.out.println("Exception in getting safe default checksum value from the configuration Setting.  No safe default checksum set.");
                e3.printStackTrace();
            }
            SAFECKSUMTYPE_DEFAULT = -1;
        }
    }

    public Checksum(byte[] bArr, int i2) {
        this.cksumType = i2;
        this.checksum = bArr;
    }

    public Checksum(int i2, byte[] bArr, EncryptionKey encryptionKey, int i3) throws KrbCryptoException, KdcErrException, KrbApErrException {
        if (i2 == -1) {
            this.cksumType = EType.getInstance(encryptionKey.getEType()).checksumType();
        } else {
            this.cksumType = i2;
        }
        this.checksum = CksumType.getInstance(this.cksumType).calculateChecksum(bArr, bArr.length, encryptionKey.getBytes(), i3);
    }

    public boolean verifyKeyedChecksum(byte[] bArr, EncryptionKey encryptionKey, int i2) throws KrbCryptoException, KdcErrException, KrbApErrException {
        CksumType cksumType = CksumType.getInstance(this.cksumType);
        if (!cksumType.isKeyed()) {
            throw new KrbApErrException(50);
        }
        return cksumType.verifyChecksum(bArr, bArr.length, encryptionKey.getBytes(), this.checksum, i2);
    }

    public boolean verifyAnyChecksum(byte[] bArr, EncryptionKey encryptionKey, int i2) throws KrbCryptoException, KdcErrException {
        return CksumType.getInstance(this.cksumType).verifyChecksum(bArr, bArr.length, encryptionKey.getBytes(), this.checksum, i2);
    }

    boolean isEqual(Checksum checksum) throws KdcErrException {
        if (this.cksumType != checksum.cksumType) {
            return false;
        }
        return CksumType.isChecksumEqual(this.checksum, checksum.checksum);
    }

    public Checksum(DerValue derValue) throws Asn1Exception, IOException {
        if (derValue.getTag() != 48) {
            throw new Asn1Exception(Krb5.ASN1_BAD_ID);
        }
        DerValue derValue2 = derValue.getData().getDerValue();
        if ((derValue2.getTag() & 31) == 0) {
            this.cksumType = derValue2.getData().getBigInteger().intValue();
            DerValue derValue3 = derValue.getData().getDerValue();
            if ((derValue3.getTag() & 31) == 1) {
                this.checksum = derValue3.getData().getOctetString();
                if (derValue.getData().available() > 0) {
                    throw new Asn1Exception(Krb5.ASN1_BAD_ID);
                }
                return;
            }
            throw new Asn1Exception(Krb5.ASN1_BAD_ID);
        }
        throw new Asn1Exception(Krb5.ASN1_BAD_ID);
    }

    public byte[] asn1Encode() throws Asn1Exception, IOException {
        DerOutputStream derOutputStream = new DerOutputStream();
        DerOutputStream derOutputStream2 = new DerOutputStream();
        derOutputStream2.putInteger(BigInteger.valueOf(this.cksumType));
        derOutputStream.write(DerValue.createTag(Byte.MIN_VALUE, true, (byte) 0), derOutputStream2);
        DerOutputStream derOutputStream3 = new DerOutputStream();
        derOutputStream3.putOctetString(this.checksum);
        derOutputStream.write(DerValue.createTag(Byte.MIN_VALUE, true, (byte) 1), derOutputStream3);
        DerOutputStream derOutputStream4 = new DerOutputStream();
        derOutputStream4.write((byte) 48, derOutputStream);
        return derOutputStream4.toByteArray();
    }

    public static Checksum parse(DerInputStream derInputStream, byte b2, boolean z2) throws Asn1Exception, IOException {
        if (z2 && (((byte) derInputStream.peekByte()) & 31) != b2) {
            return null;
        }
        DerValue derValue = derInputStream.getDerValue();
        if (b2 != (derValue.getTag() & 31)) {
            throw new Asn1Exception(Krb5.ASN1_BAD_ID);
        }
        return new Checksum(derValue.getData().getDerValue());
    }

    public final byte[] getBytes() {
        return this.checksum;
    }

    public final int getType() {
        return this.cksumType;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Checksum)) {
            return false;
        }
        try {
            return isEqual((Checksum) obj);
        } catch (KdcErrException e2) {
            return false;
        }
    }

    public int hashCode() {
        int iHashCode = (37 * 17) + this.cksumType;
        if (this.checksum != null) {
            iHashCode = (37 * iHashCode) + Arrays.hashCode(this.checksum);
        }
        return iHashCode;
    }
}
