package sun.security.krb5;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Arrays;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.DESedeKeySpec;
import sun.security.krb5.internal.Krb5;
import sun.security.krb5.internal.PAData;
import sun.security.krb5.internal.ccache.CCacheOutputStream;
import sun.security.krb5.internal.crypto.Aes128;
import sun.security.krb5.internal.crypto.Aes256;
import sun.security.krb5.internal.crypto.ArcFourHmac;
import sun.security.krb5.internal.crypto.Des;
import sun.security.krb5.internal.crypto.Des3;
import sun.security.krb5.internal.crypto.EType;
import sun.security.krb5.internal.ktab.KeyTab;
import sun.security.util.DerInputStream;
import sun.security.util.DerOutputStream;
import sun.security.util.DerValue;

/* loaded from: rt.jar:sun/security/krb5/EncryptionKey.class */
public class EncryptionKey implements Cloneable {
    private int keyType;
    private byte[] keyValue;
    private Integer kvno;
    public static final EncryptionKey NULL_KEY = new EncryptionKey(new byte[0], 0, (Integer) null);
    private static final boolean DEBUG = Krb5.DEBUG;

    public synchronized int getEType() {
        return this.keyType;
    }

    public final Integer getKeyVersionNumber() {
        return this.kvno;
    }

    public final byte[] getBytes() {
        return this.keyValue;
    }

    public synchronized Object clone() {
        return new EncryptionKey(this.keyValue, this.keyType, this.kvno);
    }

    public static EncryptionKey[] acquireSecretKeys(PrincipalName principalName, String str) {
        if (principalName == null) {
            throw new IllegalArgumentException("Cannot have null pricipal name to look in keytab.");
        }
        return KeyTab.getInstance(str).readServiceKeys(principalName);
    }

    public static EncryptionKey acquireSecretKey(PrincipalName principalName, char[] cArr, int i2, PAData.SaltAndParams saltAndParams) throws KrbException {
        String salt;
        byte[] bArr;
        if (saltAndParams != null) {
            salt = saltAndParams.salt != null ? saltAndParams.salt : principalName.getSalt();
            bArr = saltAndParams.params;
        } else {
            salt = principalName.getSalt();
            bArr = null;
        }
        return acquireSecretKey(cArr, salt, i2, bArr);
    }

    public static EncryptionKey acquireSecretKey(char[] cArr, String str, int i2, byte[] bArr) throws KrbException {
        return new EncryptionKey(stringToKey(cArr, str, bArr, i2), i2, (Integer) null);
    }

    public static EncryptionKey[] acquireSecretKeys(char[] cArr, String str) throws KrbException {
        int[] defaults = EType.getDefaults("default_tkt_enctypes");
        EncryptionKey[] encryptionKeyArr = new EncryptionKey[defaults.length];
        for (int i2 = 0; i2 < defaults.length; i2++) {
            if (EType.isSupported(defaults[i2])) {
                encryptionKeyArr[i2] = new EncryptionKey(stringToKey(cArr, str, null, defaults[i2]), defaults[i2], (Integer) null);
            } else if (DEBUG) {
                System.out.println("Encryption Type " + EType.toString(defaults[i2]) + " is not supported/enabled");
            }
        }
        return encryptionKeyArr;
    }

    public EncryptionKey(byte[] bArr, int i2, Integer num) {
        if (bArr != null) {
            this.keyValue = new byte[bArr.length];
            System.arraycopy(bArr, 0, this.keyValue, 0, bArr.length);
            this.keyType = i2;
            this.kvno = num;
            return;
        }
        throw new IllegalArgumentException("EncryptionKey: Key bytes cannot be null!");
    }

    public EncryptionKey(int i2, byte[] bArr) {
        this(bArr, i2, (Integer) null);
    }

    private static byte[] stringToKey(char[] cArr, String str, byte[] bArr, int i2) throws KrbCryptoException {
        char[] charArray = str.toCharArray();
        char[] cArr2 = new char[cArr.length + charArray.length];
        System.arraycopy(cArr, 0, cArr2, 0, cArr.length);
        System.arraycopy(charArray, 0, cArr2, cArr.length, charArray.length);
        Arrays.fill(charArray, '0');
        try {
            try {
                switch (i2) {
                    case 1:
                    case 3:
                        byte[] bArrString_to_key_bytes = Des.string_to_key_bytes(cArr2);
                        Arrays.fill(cArr2, '0');
                        return bArrString_to_key_bytes;
                    case 16:
                        byte[] bArrStringToKey = Des3.stringToKey(cArr2);
                        Arrays.fill(cArr2, '0');
                        return bArrStringToKey;
                    case 17:
                        byte[] bArrStringToKey2 = Aes128.stringToKey(cArr, str, bArr);
                        Arrays.fill(cArr2, '0');
                        return bArrStringToKey2;
                    case 18:
                        byte[] bArrStringToKey3 = Aes256.stringToKey(cArr, str, bArr);
                        Arrays.fill(cArr2, '0');
                        return bArrStringToKey3;
                    case 23:
                        byte[] bArrStringToKey4 = ArcFourHmac.stringToKey(cArr);
                        Arrays.fill(cArr2, '0');
                        return bArrStringToKey4;
                    default:
                        throw new IllegalArgumentException("encryption type " + EType.toString(i2) + " not supported");
                }
            } catch (GeneralSecurityException e2) {
                KrbCryptoException krbCryptoException = new KrbCryptoException(e2.getMessage());
                krbCryptoException.initCause(e2);
                throw krbCryptoException;
            }
        } catch (Throwable th) {
            Arrays.fill(cArr2, '0');
            throw th;
        }
    }

    public EncryptionKey(char[] cArr, String str, String str2) throws KrbCryptoException {
        if (str2 == null || str2.equalsIgnoreCase("DES")) {
            this.keyType = 3;
        } else if (str2.equalsIgnoreCase("DESede")) {
            this.keyType = 16;
        } else if (str2.equalsIgnoreCase("AES128")) {
            this.keyType = 17;
        } else if (str2.equalsIgnoreCase("ArcFourHmac")) {
            this.keyType = 23;
        } else if (str2.equalsIgnoreCase("AES256")) {
            this.keyType = 18;
            if (!EType.isSupported(this.keyType)) {
                throw new IllegalArgumentException("Algorithm " + str2 + " not enabled");
            }
        } else {
            throw new IllegalArgumentException("Algorithm " + str2 + " not supported");
        }
        this.keyValue = stringToKey(cArr, str, null, this.keyType);
        this.kvno = null;
    }

    public EncryptionKey(EncryptionKey encryptionKey) throws KrbCryptoException {
        this.keyValue = Confounder.bytes(encryptionKey.keyValue.length);
        for (int i2 = 0; i2 < this.keyValue.length; i2++) {
            byte[] bArr = this.keyValue;
            int i3 = i2;
            bArr[i3] = (byte) (bArr[i3] ^ encryptionKey.keyValue[i2]);
        }
        this.keyType = encryptionKey.keyType;
        try {
            if (this.keyType == 3 || this.keyType == 1) {
                if (!DESKeySpec.isParityAdjusted(this.keyValue, 0)) {
                    this.keyValue = Des.set_parity(this.keyValue);
                }
                if (DESKeySpec.isWeak(this.keyValue, 0)) {
                    this.keyValue[7] = (byte) (this.keyValue[7] ^ 240);
                }
            }
            if (this.keyType == 16) {
                if (!DESedeKeySpec.isParityAdjusted(this.keyValue, 0)) {
                    this.keyValue = Des3.parityFix(this.keyValue);
                }
                byte[] bArr2 = new byte[8];
                for (int i4 = 0; i4 < this.keyValue.length; i4 += 8) {
                    System.arraycopy(this.keyValue, i4, bArr2, 0, 8);
                    if (DESKeySpec.isWeak(bArr2, 0)) {
                        this.keyValue[i4 + 7] = (byte) (this.keyValue[i4 + 7] ^ 240);
                    }
                }
            }
        } catch (GeneralSecurityException e2) {
            KrbCryptoException krbCryptoException = new KrbCryptoException(e2.getMessage());
            krbCryptoException.initCause(e2);
            throw krbCryptoException;
        }
    }

    public EncryptionKey(DerValue derValue) throws Asn1Exception, IOException {
        if (derValue.getTag() != 48) {
            throw new Asn1Exception(Krb5.ASN1_BAD_ID);
        }
        DerValue derValue2 = derValue.getData().getDerValue();
        if ((derValue2.getTag() & 31) == 0) {
            this.keyType = derValue2.getData().getBigInteger().intValue();
            DerValue derValue3 = derValue.getData().getDerValue();
            if ((derValue3.getTag() & 31) == 1) {
                this.keyValue = derValue3.getData().getOctetString();
                if (derValue3.getData().available() > 0) {
                    throw new Asn1Exception(Krb5.ASN1_BAD_ID);
                }
                return;
            }
            throw new Asn1Exception(Krb5.ASN1_BAD_ID);
        }
        throw new Asn1Exception(Krb5.ASN1_BAD_ID);
    }

    public synchronized byte[] asn1Encode() throws Asn1Exception, IOException {
        DerOutputStream derOutputStream = new DerOutputStream();
        DerOutputStream derOutputStream2 = new DerOutputStream();
        derOutputStream2.putInteger(this.keyType);
        derOutputStream.write(DerValue.createTag(Byte.MIN_VALUE, true, (byte) 0), derOutputStream2);
        DerOutputStream derOutputStream3 = new DerOutputStream();
        derOutputStream3.putOctetString(this.keyValue);
        derOutputStream.write(DerValue.createTag(Byte.MIN_VALUE, true, (byte) 1), derOutputStream3);
        DerOutputStream derOutputStream4 = new DerOutputStream();
        derOutputStream4.write((byte) 48, derOutputStream);
        return derOutputStream4.toByteArray();
    }

    public synchronized void destroy() {
        if (this.keyValue != null) {
            for (int i2 = 0; i2 < this.keyValue.length; i2++) {
                this.keyValue[i2] = 0;
            }
        }
    }

    public static EncryptionKey parse(DerInputStream derInputStream, byte b2, boolean z2) throws Asn1Exception, IOException {
        if (z2 && (((byte) derInputStream.peekByte()) & 31) != b2) {
            return null;
        }
        DerValue derValue = derInputStream.getDerValue();
        if (b2 != (derValue.getTag() & 31)) {
            throw new Asn1Exception(Krb5.ASN1_BAD_ID);
        }
        return new EncryptionKey(derValue.getData().getDerValue());
    }

    public synchronized void writeKey(CCacheOutputStream cCacheOutputStream) throws IOException {
        cCacheOutputStream.write16(this.keyType);
        cCacheOutputStream.write16(this.keyType);
        cCacheOutputStream.write32(this.keyValue.length);
        for (int i2 = 0; i2 < this.keyValue.length; i2++) {
            cCacheOutputStream.write8(this.keyValue[i2]);
        }
    }

    public String toString() {
        return new String("EncryptionKey: keyType=" + this.keyType + " kvno=" + ((Object) this.kvno) + " keyValue (hex dump)=" + ((this.keyValue == null || this.keyValue.length == 0) ? " Empty Key" : '\n' + Krb5.hexDumper.encodeBuffer(this.keyValue) + '\n'));
    }

    public static EncryptionKey findKey(int i2, EncryptionKey[] encryptionKeyArr) throws KrbException {
        return findKey(i2, null, encryptionKeyArr);
    }

    private static boolean versionMatches(Integer num, Integer num2) {
        if (num == null || num.intValue() == 0 || num2 == null || num2.intValue() == 0) {
            return true;
        }
        return num.equals(num2);
    }

    public static EncryptionKey findKey(int i2, Integer num, EncryptionKey[] encryptionKeyArr) throws KrbException {
        if (!EType.isSupported(i2)) {
            throw new KrbException("Encryption type " + EType.toString(i2) + " is not supported/enabled");
        }
        boolean z2 = false;
        int iIntValue = 0;
        EncryptionKey encryptionKey = null;
        for (int i3 = 0; i3 < encryptionKeyArr.length; i3++) {
            int eType = encryptionKeyArr[i3].getEType();
            if (EType.isSupported(eType)) {
                Integer keyVersionNumber = encryptionKeyArr[i3].getKeyVersionNumber();
                if (i2 == eType) {
                    z2 = true;
                    if (versionMatches(num, keyVersionNumber)) {
                        return encryptionKeyArr[i3];
                    }
                    if (keyVersionNumber.intValue() > iIntValue) {
                        encryptionKey = encryptionKeyArr[i3];
                        iIntValue = keyVersionNumber.intValue();
                    }
                } else {
                    continue;
                }
            }
        }
        if (i2 == 1 || i2 == 3) {
            for (int i4 = 0; i4 < encryptionKeyArr.length; i4++) {
                int eType2 = encryptionKeyArr[i4].getEType();
                if (eType2 == 1 || eType2 == 3) {
                    Integer keyVersionNumber2 = encryptionKeyArr[i4].getKeyVersionNumber();
                    z2 = true;
                    if (versionMatches(num, keyVersionNumber2)) {
                        return new EncryptionKey(i2, encryptionKeyArr[i4].getBytes());
                    }
                    if (keyVersionNumber2.intValue() > iIntValue) {
                        encryptionKey = new EncryptionKey(i2, encryptionKeyArr[i4].getBytes());
                        iIntValue = keyVersionNumber2.intValue();
                    }
                }
            }
        }
        if (z2) {
            return encryptionKey;
        }
        return null;
    }
}
