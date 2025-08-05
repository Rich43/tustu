package sun.security.krb5.internal.crypto.dk;

import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import javax.crypto.Cipher;
import javax.crypto.Mac;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import sun.security.krb5.Confounder;
import sun.security.krb5.KrbCryptoException;
import sun.security.krb5.internal.crypto.KeyUsage;
import sun.security.provider.MD4;

/* loaded from: rt.jar:sun/security/krb5/internal/crypto/dk/ArcFourCrypto.class */
public class ArcFourCrypto extends DkCrypto {
    private static final boolean debug = false;
    private static final int confounderSize = 8;
    private static final byte[] ZERO_IV = {0, 0, 0, 0, 0, 0, 0, 0};
    private static final int hashSize = 16;
    private final int keyLength;

    public ArcFourCrypto(int i2) {
        this.keyLength = i2;
    }

    @Override // sun.security.krb5.internal.crypto.dk.DkCrypto
    protected int getKeySeedLength() {
        return this.keyLength;
    }

    @Override // sun.security.krb5.internal.crypto.dk.DkCrypto
    protected byte[] randomToKey(byte[] bArr) {
        return bArr;
    }

    public byte[] stringToKey(char[] cArr) throws GeneralSecurityException {
        return stringToKey(cArr, null);
    }

    private byte[] stringToKey(char[] cArr, byte[] bArr) throws GeneralSecurityException {
        if (bArr != null && bArr.length > 0) {
            throw new RuntimeException("Invalid parameter to stringToKey");
        }
        byte[] bArrCharToUtf16 = null;
        try {
            bArrCharToUtf16 = charToUtf16(cArr);
            MessageDigest md4 = MD4.getInstance();
            md4.update(bArrCharToUtf16);
            byte[] bArrDigest = md4.digest();
            if (bArrCharToUtf16 != null) {
                Arrays.fill(bArrCharToUtf16, (byte) 0);
            }
            return bArrDigest;
        } catch (Exception e2) {
            if (bArrCharToUtf16 != null) {
                Arrays.fill(bArrCharToUtf16, (byte) 0);
            }
            return null;
        } catch (Throwable th) {
            if (bArrCharToUtf16 != null) {
                Arrays.fill(bArrCharToUtf16, (byte) 0);
            }
            throw th;
        }
    }

    @Override // sun.security.krb5.internal.crypto.dk.DkCrypto
    protected Cipher getCipher(byte[] bArr, byte[] bArr2, int i2) throws GeneralSecurityException {
        if (bArr2 == null) {
            bArr2 = ZERO_IV;
        }
        SecretKeySpec secretKeySpec = new SecretKeySpec(bArr, "ARCFOUR");
        Cipher cipher = Cipher.getInstance("ARCFOUR");
        cipher.init(i2, secretKeySpec, new IvParameterSpec(bArr2, 0, bArr2.length));
        return cipher;
    }

    @Override // sun.security.krb5.internal.crypto.dk.DkCrypto
    public int getChecksumLength() {
        return 16;
    }

    @Override // sun.security.krb5.internal.crypto.dk.DkCrypto
    protected byte[] getHmac(byte[] bArr, byte[] bArr2) throws GeneralSecurityException {
        SecretKeySpec secretKeySpec = new SecretKeySpec(bArr, "HmacMD5");
        Mac mac = Mac.getInstance("HmacMD5");
        mac.init(secretKeySpec);
        return mac.doFinal(bArr2);
    }

    @Override // sun.security.krb5.internal.crypto.dk.DkCrypto
    public byte[] calculateChecksum(byte[] bArr, int i2, byte[] bArr2, int i3, int i4) throws GeneralSecurityException {
        if (!KeyUsage.isValid(i2)) {
            throw new GeneralSecurityException("Invalid key usage number: " + i2);
        }
        try {
            byte[] bytes = "signaturekey".getBytes();
            byte[] bArr3 = new byte[bytes.length + 1];
            System.arraycopy(bytes, 0, bArr3, 0, bytes.length);
            byte[] hmac = getHmac(bArr, bArr3);
            byte[] salt = getSalt(i2);
            try {
                MessageDigest messageDigest = MessageDigest.getInstance("MD5");
                messageDigest.update(salt);
                messageDigest.update(bArr2, i3, i4);
                byte[] hmac2 = getHmac(hmac, messageDigest.digest());
                if (hmac2.length == getChecksumLength()) {
                    return hmac2;
                }
                if (hmac2.length > getChecksumLength()) {
                    byte[] bArr4 = new byte[getChecksumLength()];
                    System.arraycopy(hmac2, 0, bArr4, 0, bArr4.length);
                    return bArr4;
                }
                throw new GeneralSecurityException("checksum size too short: " + hmac2.length + "; expecting : " + getChecksumLength());
            } catch (NoSuchAlgorithmException e2) {
                GeneralSecurityException generalSecurityException = new GeneralSecurityException("Calculate Checkum Failed!");
                generalSecurityException.initCause(e2);
                throw generalSecurityException;
            }
        } catch (Exception e3) {
            GeneralSecurityException generalSecurityException2 = new GeneralSecurityException("Calculate Checkum Failed!");
            generalSecurityException2.initCause(e3);
            throw generalSecurityException2;
        }
    }

    public byte[] encryptSeq(byte[] bArr, int i2, byte[] bArr2, byte[] bArr3, int i3, int i4) throws GeneralSecurityException, KrbCryptoException {
        if (!KeyUsage.isValid(i2)) {
            throw new GeneralSecurityException("Invalid key usage number: " + i2);
        }
        byte[] hmac = getHmac(getHmac(bArr, new byte[4]), bArr2);
        Cipher cipher = Cipher.getInstance("ARCFOUR");
        cipher.init(1, new SecretKeySpec(hmac, "ARCFOUR"));
        return cipher.doFinal(bArr3, i3, i4);
    }

    public byte[] decryptSeq(byte[] bArr, int i2, byte[] bArr2, byte[] bArr3, int i3, int i4) throws GeneralSecurityException, KrbCryptoException {
        if (!KeyUsage.isValid(i2)) {
            throw new GeneralSecurityException("Invalid key usage number: " + i2);
        }
        byte[] hmac = getHmac(getHmac(bArr, new byte[4]), bArr2);
        Cipher cipher = Cipher.getInstance("ARCFOUR");
        cipher.init(2, new SecretKeySpec(hmac, "ARCFOUR"));
        return cipher.doFinal(bArr3, i3, i4);
    }

    @Override // sun.security.krb5.internal.crypto.dk.DkCrypto
    public byte[] encrypt(byte[] bArr, int i2, byte[] bArr2, byte[] bArr3, byte[] bArr4, int i3, int i4) throws GeneralSecurityException, KrbCryptoException {
        if (!KeyUsage.isValid(i2)) {
            throw new GeneralSecurityException("Invalid key usage number: " + i2);
        }
        byte[] bArrBytes = Confounder.bytes(8);
        byte[] bArr5 = new byte[roundup(bArrBytes.length + i4, 1)];
        System.arraycopy(bArrBytes, 0, bArr5, 0, bArrBytes.length);
        System.arraycopy(bArr4, i3, bArr5, bArrBytes.length, i4);
        byte[] bArr6 = new byte[bArr.length];
        System.arraycopy(bArr, 0, bArr6, 0, bArr.length);
        byte[] hmac = getHmac(bArr6, getSalt(i2));
        byte[] hmac2 = getHmac(hmac, bArr5);
        byte[] hmac3 = getHmac(hmac, hmac2);
        Cipher cipher = Cipher.getInstance("ARCFOUR");
        cipher.init(1, new SecretKeySpec(hmac3, "ARCFOUR"));
        byte[] bArrDoFinal = cipher.doFinal(bArr5, 0, bArr5.length);
        byte[] bArr7 = new byte[16 + bArrDoFinal.length];
        System.arraycopy(hmac2, 0, bArr7, 0, 16);
        System.arraycopy(bArrDoFinal, 0, bArr7, 16, bArrDoFinal.length);
        return bArr7;
    }

    @Override // sun.security.krb5.internal.crypto.dk.DkCrypto
    public byte[] encryptRaw(byte[] bArr, int i2, byte[] bArr2, byte[] bArr3, int i3, int i4) throws GeneralSecurityException, KrbCryptoException {
        if (!KeyUsage.isValid(i2)) {
            throw new GeneralSecurityException("Invalid key usage number: " + i2);
        }
        byte[] bArr4 = new byte[bArr.length];
        for (int i5 = 0; i5 <= 15; i5++) {
            bArr4[i5] = (byte) (bArr[i5] ^ 240);
        }
        byte[] hmac = getHmac(getHmac(bArr4, new byte[4]), bArr2);
        Cipher cipher = Cipher.getInstance("ARCFOUR");
        cipher.init(1, new SecretKeySpec(hmac, "ARCFOUR"));
        return cipher.doFinal(bArr3, i3, i4);
    }

    @Override // sun.security.krb5.internal.crypto.dk.DkCrypto
    public byte[] decrypt(byte[] bArr, int i2, byte[] bArr2, byte[] bArr3, int i3, int i4) throws GeneralSecurityException {
        if (!KeyUsage.isValid(i2)) {
            throw new GeneralSecurityException("Invalid key usage number: " + i2);
        }
        byte[] bArr4 = new byte[bArr.length];
        System.arraycopy(bArr, 0, bArr4, 0, bArr.length);
        byte[] hmac = getHmac(bArr4, getSalt(i2));
        byte[] bArr5 = new byte[16];
        System.arraycopy(bArr3, i3, bArr5, 0, 16);
        byte[] hmac2 = getHmac(hmac, bArr5);
        Cipher cipher = Cipher.getInstance("ARCFOUR");
        cipher.init(2, new SecretKeySpec(hmac2, "ARCFOUR"));
        byte[] bArrDoFinal = cipher.doFinal(bArr3, i3 + 16, i4 - 16);
        byte[] hmac3 = getHmac(hmac, bArrDoFinal);
        boolean z2 = false;
        if (hmac3.length >= 16) {
            int i5 = 0;
            while (true) {
                if (i5 >= 16) {
                    break;
                }
                if (hmac3[i5] == bArr3[i5]) {
                    i5++;
                } else {
                    z2 = true;
                    break;
                }
            }
        }
        if (z2) {
            throw new GeneralSecurityException("Checksum failed");
        }
        byte[] bArr6 = new byte[bArrDoFinal.length - 8];
        System.arraycopy(bArrDoFinal, 8, bArr6, 0, bArr6.length);
        return bArr6;
    }

    public byte[] decryptRaw(byte[] bArr, int i2, byte[] bArr2, byte[] bArr3, int i3, int i4, byte[] bArr4) throws GeneralSecurityException {
        if (!KeyUsage.isValid(i2)) {
            throw new GeneralSecurityException("Invalid key usage number: " + i2);
        }
        byte[] bArr5 = new byte[bArr.length];
        for (int i5 = 0; i5 <= 15; i5++) {
            bArr5[i5] = (byte) (bArr[i5] ^ 240);
        }
        byte[] hmac = getHmac(bArr5, new byte[4]);
        byte[] bArr6 = new byte[4];
        System.arraycopy(bArr4, 0, bArr6, 0, bArr6.length);
        byte[] hmac2 = getHmac(hmac, bArr6);
        Cipher cipher = Cipher.getInstance("ARCFOUR");
        cipher.init(2, new SecretKeySpec(hmac2, "ARCFOUR"));
        return cipher.doFinal(bArr3, i3, i4);
    }

    private byte[] getSalt(int i2) {
        int iArcfour_translate_usage = arcfour_translate_usage(i2);
        return new byte[]{(byte) (iArcfour_translate_usage & 255), (byte) ((iArcfour_translate_usage >> 8) & 255), (byte) ((iArcfour_translate_usage >> 16) & 255), (byte) ((iArcfour_translate_usage >> 24) & 255)};
    }

    private int arcfour_translate_usage(int i2) {
        switch (i2) {
            case 3:
                return 8;
            case 9:
                return 8;
            case 23:
                return 13;
            default:
                return i2;
        }
    }
}
