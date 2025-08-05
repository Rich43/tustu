package sun.security.krb5.internal.crypto.dk;

import java.security.GeneralSecurityException;
import java.security.InvalidKeyException;
import java.util.Arrays;
import javax.crypto.Cipher;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.DESedeKeySpec;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/* loaded from: rt.jar:sun/security/krb5/internal/crypto/dk/Des3DkCrypto.class */
public class Des3DkCrypto extends DkCrypto {
    private static final byte[] ZERO_IV = {0, 0, 0, 0, 0, 0, 0, 0};

    @Override // sun.security.krb5.internal.crypto.dk.DkCrypto
    protected int getKeySeedLength() {
        return 168;
    }

    public byte[] stringToKey(char[] cArr) throws GeneralSecurityException {
        byte[] bArrCharToUtf8 = null;
        try {
            bArrCharToUtf8 = charToUtf8(cArr);
            byte[] bArrStringToKey = stringToKey(bArrCharToUtf8, null);
            if (bArrCharToUtf8 != null) {
                Arrays.fill(bArrCharToUtf8, (byte) 0);
            }
            return bArrStringToKey;
        } catch (Throwable th) {
            if (bArrCharToUtf8 != null) {
                Arrays.fill(bArrCharToUtf8, (byte) 0);
            }
            throw th;
        }
    }

    private byte[] stringToKey(byte[] bArr, byte[] bArr2) throws GeneralSecurityException {
        if (bArr2 != null && bArr2.length > 0) {
            throw new RuntimeException("Invalid parameter to stringToKey");
        }
        return dk(randomToKey(nfold(bArr, getKeySeedLength())), KERBEROS_CONSTANT);
    }

    public byte[] parityFix(byte[] bArr) throws GeneralSecurityException {
        setParityBit(bArr);
        return bArr;
    }

    @Override // sun.security.krb5.internal.crypto.dk.DkCrypto
    protected byte[] randomToKey(byte[] bArr) {
        if (bArr.length != 21) {
            throw new IllegalArgumentException("input must be 168 bits");
        }
        byte[] bArrKeyCorrection = keyCorrection(des3Expand(bArr, 0, 7));
        byte[] bArrKeyCorrection2 = keyCorrection(des3Expand(bArr, 7, 14));
        byte[] bArrKeyCorrection3 = keyCorrection(des3Expand(bArr, 14, 21));
        byte[] bArr2 = new byte[24];
        System.arraycopy(bArrKeyCorrection, 0, bArr2, 0, 8);
        System.arraycopy(bArrKeyCorrection2, 0, bArr2, 8, 8);
        System.arraycopy(bArrKeyCorrection3, 0, bArr2, 16, 8);
        return bArr2;
    }

    private static byte[] keyCorrection(byte[] bArr) {
        try {
            if (DESKeySpec.isWeak(bArr, 0)) {
                bArr[7] = (byte) (bArr[7] ^ 240);
            }
        } catch (InvalidKeyException e2) {
        }
        return bArr;
    }

    private static byte[] des3Expand(byte[] bArr, int i2, int i3) {
        if (i3 - i2 != 7) {
            throw new IllegalArgumentException("Invalid length of DES Key Value:" + i2 + "," + i3);
        }
        byte[] bArr2 = new byte[8];
        byte b2 = 0;
        System.arraycopy(bArr, i2, bArr2, 0, 7);
        byte b3 = 0;
        for (int i4 = i2; i4 < i3; i4++) {
            byte b4 = (byte) (bArr[i4] & 1);
            b3 = (byte) (b3 + 1);
            if (b4 != 0) {
                b2 = (byte) (b2 | (b4 << b3));
            }
        }
        bArr2[7] = b2;
        setParityBit(bArr2);
        return bArr2;
    }

    private static void setParityBit(byte[] bArr) {
        for (int i2 = 0; i2 < bArr.length; i2++) {
            int i3 = bArr[i2] & 254;
            bArr[i2] = (byte) (i3 | ((Integer.bitCount(i3) & 1) ^ 1));
        }
    }

    @Override // sun.security.krb5.internal.crypto.dk.DkCrypto
    protected Cipher getCipher(byte[] bArr, byte[] bArr2, int i2) throws GeneralSecurityException {
        SecretKey secretKeyGenerateSecret = SecretKeyFactory.getInstance("desede").generateSecret(new DESedeKeySpec(bArr, 0));
        if (bArr2 == null) {
            bArr2 = ZERO_IV;
        }
        Cipher cipher = Cipher.getInstance("DESede/CBC/NoPadding");
        cipher.init(i2, secretKeyGenerateSecret, new IvParameterSpec(bArr2, 0, bArr2.length));
        return cipher;
    }

    @Override // sun.security.krb5.internal.crypto.dk.DkCrypto
    public int getChecksumLength() {
        return 20;
    }

    @Override // sun.security.krb5.internal.crypto.dk.DkCrypto
    protected byte[] getHmac(byte[] bArr, byte[] bArr2) throws GeneralSecurityException {
        SecretKeySpec secretKeySpec = new SecretKeySpec(bArr, "HmacSHA1");
        Mac mac = Mac.getInstance("HmacSHA1");
        mac.init(secretKeySpec);
        return mac.doFinal(bArr2);
    }
}
