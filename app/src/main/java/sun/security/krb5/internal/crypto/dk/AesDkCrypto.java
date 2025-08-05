package sun.security.krb5.internal.crypto.dk;

import java.security.GeneralSecurityException;
import java.util.Arrays;
import javax.crypto.Cipher;
import javax.crypto.Mac;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import sun.security.krb5.Confounder;
import sun.security.krb5.KrbCryptoException;
import sun.security.krb5.internal.crypto.KeyUsage;

/* loaded from: rt.jar:sun/security/krb5/internal/crypto/dk/AesDkCrypto.class */
public class AesDkCrypto extends DkCrypto {
    private static final boolean debug = false;
    private static final int BLOCK_SIZE = 16;
    private static final int DEFAULT_ITERATION_COUNT = 4096;
    private static final byte[] ZERO_IV = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
    private static final int hashSize = 12;
    private final int keyLength;

    public AesDkCrypto(int i2) {
        this.keyLength = i2;
    }

    @Override // sun.security.krb5.internal.crypto.dk.DkCrypto
    protected int getKeySeedLength() {
        return this.keyLength;
    }

    public byte[] stringToKey(char[] cArr, String str, byte[] bArr) throws GeneralSecurityException {
        byte[] bytes = null;
        try {
            bytes = str.getBytes("UTF-8");
            byte[] bArrStringToKey = stringToKey(cArr, bytes, bArr);
            if (bytes != null) {
                Arrays.fill(bytes, (byte) 0);
            }
            return bArrStringToKey;
        } catch (Exception e2) {
            if (bytes != null) {
                Arrays.fill(bytes, (byte) 0);
            }
            return null;
        } catch (Throwable th) {
            if (bytes != null) {
                Arrays.fill(bytes, (byte) 0);
            }
            throw th;
        }
    }

    private byte[] stringToKey(char[] cArr, byte[] bArr, byte[] bArr2) throws GeneralSecurityException {
        int bigEndian = 4096;
        if (bArr2 != null) {
            if (bArr2.length != 4) {
                throw new RuntimeException("Invalid parameter to stringToKey");
            }
            bigEndian = readBigEndian(bArr2, 0, 4);
        }
        return dk(randomToKey(PBKDF2(cArr, bArr, bigEndian, getKeySeedLength())), KERBEROS_CONSTANT);
    }

    @Override // sun.security.krb5.internal.crypto.dk.DkCrypto
    protected byte[] randomToKey(byte[] bArr) {
        return bArr;
    }

    @Override // sun.security.krb5.internal.crypto.dk.DkCrypto
    protected Cipher getCipher(byte[] bArr, byte[] bArr2, int i2) throws GeneralSecurityException {
        if (bArr2 == null) {
            bArr2 = ZERO_IV;
        }
        SecretKeySpec secretKeySpec = new SecretKeySpec(bArr, "AES");
        Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
        cipher.init(i2, secretKeySpec, new IvParameterSpec(bArr2, 0, bArr2.length));
        return cipher;
    }

    @Override // sun.security.krb5.internal.crypto.dk.DkCrypto
    public int getChecksumLength() {
        return 12;
    }

    @Override // sun.security.krb5.internal.crypto.dk.DkCrypto
    protected byte[] getHmac(byte[] bArr, byte[] bArr2) throws IllegalStateException, GeneralSecurityException {
        SecretKeySpec secretKeySpec = new SecretKeySpec(bArr, "HMAC");
        Mac mac = Mac.getInstance("HmacSHA1");
        mac.init(secretKeySpec);
        byte[] bArrDoFinal = mac.doFinal(bArr2);
        byte[] bArr3 = new byte[12];
        System.arraycopy(bArrDoFinal, 0, bArr3, 0, 12);
        return bArr3;
    }

    @Override // sun.security.krb5.internal.crypto.dk.DkCrypto
    public byte[] calculateChecksum(byte[] bArr, int i2, byte[] bArr2, int i3, int i4) throws GeneralSecurityException {
        if (!KeyUsage.isValid(i2)) {
            throw new GeneralSecurityException("Invalid key usage number: " + i2);
        }
        byte[] bArrDk = dk(bArr, new byte[]{(byte) ((i2 >> 24) & 255), (byte) ((i2 >> 16) & 255), (byte) ((i2 >> 8) & 255), (byte) (i2 & 255), -103});
        try {
            byte[] hmac = getHmac(bArrDk, bArr2);
            if (hmac.length == getChecksumLength()) {
                return hmac;
            }
            if (hmac.length > getChecksumLength()) {
                byte[] bArr3 = new byte[getChecksumLength()];
                System.arraycopy(hmac, 0, bArr3, 0, bArr3.length);
                Arrays.fill(bArrDk, 0, bArrDk.length, (byte) 0);
                return bArr3;
            }
            throw new GeneralSecurityException("checksum size too short: " + hmac.length + "; expecting : " + getChecksumLength());
        } finally {
            Arrays.fill(bArrDk, 0, bArrDk.length, (byte) 0);
        }
    }

    @Override // sun.security.krb5.internal.crypto.dk.DkCrypto
    public byte[] encrypt(byte[] bArr, int i2, byte[] bArr2, byte[] bArr3, byte[] bArr4, int i3, int i4) throws GeneralSecurityException, KrbCryptoException {
        if (!KeyUsage.isValid(i2)) {
            throw new GeneralSecurityException("Invalid key usage number: " + i2);
        }
        return encryptCTS(bArr, i2, bArr2, bArr3, bArr4, i3, i4, true);
    }

    @Override // sun.security.krb5.internal.crypto.dk.DkCrypto
    public byte[] encryptRaw(byte[] bArr, int i2, byte[] bArr2, byte[] bArr3, int i3, int i4) throws GeneralSecurityException, KrbCryptoException {
        if (!KeyUsage.isValid(i2)) {
            throw new GeneralSecurityException("Invalid key usage number: " + i2);
        }
        return encryptCTS(bArr, i2, bArr2, null, bArr3, i3, i4, false);
    }

    @Override // sun.security.krb5.internal.crypto.dk.DkCrypto
    public byte[] decrypt(byte[] bArr, int i2, byte[] bArr2, byte[] bArr3, int i3, int i4) throws GeneralSecurityException {
        if (!KeyUsage.isValid(i2)) {
            throw new GeneralSecurityException("Invalid key usage number: " + i2);
        }
        return decryptCTS(bArr, i2, bArr2, bArr3, i3, i4, true);
    }

    @Override // sun.security.krb5.internal.crypto.dk.DkCrypto
    public byte[] decryptRaw(byte[] bArr, int i2, byte[] bArr2, byte[] bArr3, int i3, int i4) throws GeneralSecurityException {
        if (!KeyUsage.isValid(i2)) {
            throw new GeneralSecurityException("Invalid key usage number: " + i2);
        }
        return decryptCTS(bArr, i2, bArr2, bArr3, i3, i4, false);
    }

    private byte[] encryptCTS(byte[] bArr, int i2, byte[] bArr2, byte[] bArr3, byte[] bArr4, int i3, int i4, boolean z2) throws GeneralSecurityException, KrbCryptoException {
        byte[] bArr5;
        byte[] bArrDk = null;
        byte[] bArrDk2 = null;
        try {
            byte[] bArr6 = {(byte) ((i2 >> 24) & 255), (byte) ((i2 >> 16) & 255), (byte) ((i2 >> 8) & 255), (byte) (i2 & 255), -86};
            bArrDk = dk(bArr, bArr6);
            if (z2) {
                byte[] bArrBytes = Confounder.bytes(16);
                bArr5 = new byte[bArrBytes.length + i4];
                System.arraycopy(bArrBytes, 0, bArr5, 0, bArrBytes.length);
                System.arraycopy(bArr4, i3, bArr5, bArrBytes.length, i4);
            } else {
                bArr5 = new byte[i4];
                System.arraycopy(bArr4, i3, bArr5, 0, i4);
            }
            byte[] bArr7 = new byte[bArr5.length + 12];
            Cipher cipher = Cipher.getInstance("AES/CTS/NoPadding");
            cipher.init(1, new SecretKeySpec(bArrDk, "AES"), new IvParameterSpec(bArr2, 0, bArr2.length));
            cipher.doFinal(bArr5, 0, bArr5.length, bArr7);
            bArr6[4] = 85;
            bArrDk2 = dk(bArr, bArr6);
            byte[] hmac = getHmac(bArrDk2, bArr5);
            System.arraycopy(hmac, 0, bArr7, bArr5.length, hmac.length);
            if (bArrDk != null) {
                Arrays.fill(bArrDk, 0, bArrDk.length, (byte) 0);
            }
            if (bArrDk2 != null) {
                Arrays.fill(bArrDk2, 0, bArrDk2.length, (byte) 0);
            }
            return bArr7;
        } catch (Throwable th) {
            if (bArrDk != null) {
                Arrays.fill(bArrDk, 0, bArrDk.length, (byte) 0);
            }
            if (bArrDk2 != null) {
                Arrays.fill(bArrDk2, 0, bArrDk2.length, (byte) 0);
            }
            throw th;
        }
    }

    private byte[] decryptCTS(byte[] bArr, int i2, byte[] bArr2, byte[] bArr3, int i3, int i4, boolean z2) throws GeneralSecurityException {
        Object[] objArr = null;
        Object[] objArr2 = null;
        try {
            byte[] bArr4 = {(byte) ((i2 >> 24) & 255), (byte) ((i2 >> 16) & 255), (byte) ((i2 >> 8) & 255), (byte) (i2 & 255), -86};
            byte[] bArrDk = dk(bArr, bArr4);
            Cipher cipher = Cipher.getInstance("AES/CTS/NoPadding");
            cipher.init(2, new SecretKeySpec(bArrDk, "AES"), new IvParameterSpec(bArr2, 0, bArr2.length));
            byte[] bArrDoFinal = cipher.doFinal(bArr3, i3, i4 - 12);
            bArr4[4] = 85;
            byte[] bArrDk2 = dk(bArr, bArr4);
            byte[] hmac = getHmac(bArrDk2, bArrDoFinal);
            int i5 = (i3 + i4) - 12;
            boolean z3 = false;
            if (hmac.length >= 12) {
                int i6 = 0;
                while (true) {
                    if (i6 >= 12) {
                        break;
                    }
                    if (hmac[i6] == bArr3[i5 + i6]) {
                        i6++;
                    } else {
                        z3 = true;
                        break;
                    }
                }
            }
            if (z3) {
                throw new GeneralSecurityException("Checksum failed");
            }
            if (z2) {
                byte[] bArr5 = new byte[bArrDoFinal.length - 16];
                System.arraycopy(bArrDoFinal, 16, bArr5, 0, bArr5.length);
                if (bArrDk != null) {
                    Arrays.fill(bArrDk, 0, bArrDk.length, (byte) 0);
                }
                if (bArrDk2 != null) {
                    Arrays.fill(bArrDk2, 0, bArrDk2.length, (byte) 0);
                }
                return bArr5;
            }
            if (bArrDk != null) {
                Arrays.fill(bArrDk, 0, bArrDk.length, (byte) 0);
            }
            if (bArrDk2 != null) {
                Arrays.fill(bArrDk2, 0, bArrDk2.length, (byte) 0);
            }
            return bArrDoFinal;
        } catch (Throwable th) {
            if (0 != 0) {
                Arrays.fill((byte[]) null, 0, objArr.length, (byte) 0);
            }
            if (0 != 0) {
                Arrays.fill((byte[]) null, 0, objArr2.length, (byte) 0);
            }
            throw th;
        }
    }

    private static byte[] PBKDF2(char[] cArr, byte[] bArr, int i2, int i3) throws GeneralSecurityException {
        return SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1").generateSecret(new PBEKeySpec(cArr, bArr, i2, i3)).getEncoded();
    }

    public static final int readBigEndian(byte[] bArr, int i2, int i3) {
        int i4 = 0;
        int i5 = (i3 - 1) * 8;
        while (i3 > 0) {
            i4 += (bArr[i2] & 255) << i5;
            i5 -= 8;
            i2++;
            i3--;
        }
        return i4;
    }
}
