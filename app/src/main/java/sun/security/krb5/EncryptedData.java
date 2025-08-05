package sun.security.krb5;

import java.io.IOException;
import java.math.BigInteger;
import sun.security.krb5.internal.KdcErrException;
import sun.security.krb5.internal.Krb5;
import sun.security.krb5.internal.KrbApErrException;
import sun.security.krb5.internal.crypto.EType;
import sun.security.util.DerInputStream;
import sun.security.util.DerOutputStream;
import sun.security.util.DerValue;

/* loaded from: rt.jar:sun/security/krb5/EncryptedData.class */
public class EncryptedData implements Cloneable {
    int eType;
    Integer kvno;
    byte[] cipher;
    byte[] plain;
    public static final int ETYPE_NULL = 0;
    public static final int ETYPE_DES_CBC_CRC = 1;
    public static final int ETYPE_DES_CBC_MD4 = 2;
    public static final int ETYPE_DES_CBC_MD5 = 3;
    public static final int ETYPE_ARCFOUR_HMAC = 23;
    public static final int ETYPE_ARCFOUR_HMAC_EXP = 24;
    public static final int ETYPE_DES3_CBC_HMAC_SHA1_KD = 16;
    public static final int ETYPE_AES128_CTS_HMAC_SHA1_96 = 17;
    public static final int ETYPE_AES256_CTS_HMAC_SHA1_96 = 18;

    private EncryptedData() {
    }

    public Object clone() {
        EncryptedData encryptedData = new EncryptedData();
        encryptedData.eType = this.eType;
        if (this.kvno != null) {
            encryptedData.kvno = new Integer(this.kvno.intValue());
        }
        if (this.cipher != null) {
            encryptedData.cipher = new byte[this.cipher.length];
            System.arraycopy(this.cipher, 0, encryptedData.cipher, 0, this.cipher.length);
        }
        return encryptedData;
    }

    public EncryptedData(int i2, Integer num, byte[] bArr) {
        this.eType = i2;
        this.kvno = num;
        this.cipher = bArr;
    }

    public EncryptedData(EncryptionKey encryptionKey, byte[] bArr, int i2) throws KrbCryptoException, KdcErrException {
        this.cipher = EType.getInstance(encryptionKey.getEType()).encrypt(bArr, encryptionKey.getBytes(), i2);
        this.eType = encryptionKey.getEType();
        this.kvno = encryptionKey.getKeyVersionNumber();
    }

    public byte[] decrypt(EncryptionKey encryptionKey, int i2) throws KrbCryptoException, KdcErrException, KrbApErrException {
        if (this.eType != encryptionKey.getEType()) {
            throw new KrbCryptoException("EncryptedData is encrypted using keytype " + EType.toString(this.eType) + " but decryption key is of type " + EType.toString(encryptionKey.getEType()));
        }
        EType eType = EType.getInstance(this.eType);
        this.plain = eType.decrypt(this.cipher, encryptionKey.getBytes(), i2);
        return eType.decryptedData(this.plain);
    }

    private byte[] decryptedData() throws KdcErrException {
        if (this.plain != null) {
            return EType.getInstance(this.eType).decryptedData(this.plain);
        }
        return null;
    }

    private EncryptedData(DerValue derValue) throws Asn1Exception, IOException {
        if (derValue.getTag() != 48) {
            throw new Asn1Exception(Krb5.ASN1_BAD_ID);
        }
        DerValue derValue2 = derValue.getData().getDerValue();
        if ((derValue2.getTag() & 31) == 0) {
            this.eType = derValue2.getData().getBigInteger().intValue();
            if ((derValue.getData().peekByte() & 31) == 1) {
                this.kvno = new Integer(derValue.getData().getDerValue().getData().getBigInteger().intValue());
            } else {
                this.kvno = null;
            }
            DerValue derValue3 = derValue.getData().getDerValue();
            if ((derValue3.getTag() & 31) == 2) {
                this.cipher = derValue3.getData().getOctetString();
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
        derOutputStream2.putInteger(BigInteger.valueOf(this.eType));
        derOutputStream.write(DerValue.createTag(Byte.MIN_VALUE, true, (byte) 0), derOutputStream2);
        DerOutputStream derOutputStream3 = new DerOutputStream();
        if (this.kvno != null) {
            derOutputStream3.putInteger(BigInteger.valueOf(this.kvno.longValue()));
            derOutputStream.write(DerValue.createTag(Byte.MIN_VALUE, true, (byte) 1), derOutputStream3);
            derOutputStream3 = new DerOutputStream();
        }
        derOutputStream3.putOctetString(this.cipher);
        derOutputStream.write(DerValue.createTag(Byte.MIN_VALUE, true, (byte) 2), derOutputStream3);
        DerOutputStream derOutputStream4 = new DerOutputStream();
        derOutputStream4.write((byte) 48, derOutputStream);
        return derOutputStream4.toByteArray();
    }

    public static EncryptedData parse(DerInputStream derInputStream, byte b2, boolean z2) throws Asn1Exception, IOException {
        if (z2 && (((byte) derInputStream.peekByte()) & 31) != b2) {
            return null;
        }
        DerValue derValue = derInputStream.getDerValue();
        if (b2 != (derValue.getTag() & 31)) {
            throw new Asn1Exception(Krb5.ASN1_BAD_ID);
        }
        return new EncryptedData(derValue.getData().getDerValue());
    }

    public byte[] reset(byte[] bArr) {
        byte[] bArr2 = null;
        if ((bArr[1] & 255) < 128) {
            bArr2 = new byte[bArr[1] + 2];
            System.arraycopy(bArr, 0, bArr2, 0, bArr[1] + 2);
        } else if ((bArr[1] & 255) > 128) {
            int i2 = bArr[1] & 127;
            int i3 = 0;
            for (int i4 = 0; i4 < i2; i4++) {
                i3 |= (bArr[i4 + 2] & 255) << (8 * ((i2 - i4) - 1));
            }
            bArr2 = new byte[i3 + i2 + 2];
            System.arraycopy(bArr, 0, bArr2, 0, i3 + i2 + 2);
        }
        return bArr2;
    }

    public int getEType() {
        return this.eType;
    }

    public Integer getKeyVersionNumber() {
        return this.kvno;
    }

    public byte[] getBytes() {
        return this.cipher;
    }
}
