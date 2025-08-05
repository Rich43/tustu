package com.sun.crypto.provider;

import java.io.UnsupportedEncodingException;
import java.security.DigestException;
import java.security.GeneralSecurityException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidParameterException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.ProviderException;
import java.security.SecureRandom;
import java.security.spec.AlgorithmParameterSpec;
import java.util.Arrays;
import javax.crypto.KeyGeneratorSpi;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import net.lingala.zip4j.util.InternalZipConstants;
import sun.security.internal.spec.TlsPrfParameterSpec;

/* loaded from: sunjce_provider.jar:com/sun/crypto/provider/TlsPrfGenerator.class */
abstract class TlsPrfGenerator extends KeyGeneratorSpi {
    private static final byte[] B0 = new byte[0];
    static final byte[] LABEL_MASTER_SECRET = {109, 97, 115, 116, 101, 114, 32, 115, 101, 99, 114, 101, 116};
    static final byte[] LABEL_EXTENDED_MASTER_SECRET = {101, 120, 116, 101, 110, 100, 101, 100, 32, 109, 97, 115, 116, 101, 114, 32, 115, 101, 99, 114, 101, 116};
    static final byte[] LABEL_KEY_EXPANSION = {107, 101, 121, 32, 101, 120, 112, 97, 110, 115, 105, 111, 110};
    static final byte[] LABEL_CLIENT_WRITE_KEY = {99, 108, 105, 101, 110, 116, 32, 119, 114, 105, 116, 101, 32, 107, 101, 121};
    static final byte[] LABEL_SERVER_WRITE_KEY = {115, 101, 114, 118, 101, 114, 32, 119, 114, 105, 116, 101, 32, 107, 101, 121};
    static final byte[] LABEL_IV_BLOCK = {73, 86, 32, 98, 108, 111, 99, 107};
    private static final byte[] HMAC_ipad64 = genPad((byte) 54, 64);
    private static final byte[] HMAC_ipad128 = genPad((byte) 54, 128);
    private static final byte[] HMAC_opad64 = genPad((byte) 92, 64);
    private static final byte[] HMAC_opad128 = genPad((byte) 92, 128);
    static final byte[][] SSL3_CONST = genConst();
    private static final String MSG = "TlsPrfGenerator must be initialized using a TlsPrfParameterSpec";
    private TlsPrfParameterSpec spec;

    static byte[] genPad(byte b2, int i2) {
        byte[] bArr = new byte[i2];
        Arrays.fill(bArr, b2);
        return bArr;
    }

    static byte[] concat(byte[] bArr, byte[] bArr2) {
        int length = bArr.length;
        int length2 = bArr2.length;
        byte[] bArr3 = new byte[length + length2];
        System.arraycopy(bArr, 0, bArr3, 0, length);
        System.arraycopy(bArr2, 0, bArr3, length, length2);
        return bArr3;
    }

    /* JADX WARN: Type inference failed for: r0v2, types: [byte[], byte[][]] */
    private static byte[][] genConst() {
        ?? r0 = new byte[10];
        for (int i2 = 0; i2 < 10; i2++) {
            byte[] bArr = new byte[i2 + 1];
            Arrays.fill(bArr, (byte) (65 + i2));
            r0[i2] = bArr;
        }
        return r0;
    }

    @Override // javax.crypto.KeyGeneratorSpi
    protected void engineInit(SecureRandom secureRandom) {
        throw new InvalidParameterException(MSG);
    }

    @Override // javax.crypto.KeyGeneratorSpi
    protected void engineInit(AlgorithmParameterSpec algorithmParameterSpec, SecureRandom secureRandom) throws InvalidAlgorithmParameterException {
        if (!(algorithmParameterSpec instanceof TlsPrfParameterSpec)) {
            throw new InvalidAlgorithmParameterException(MSG);
        }
        this.spec = (TlsPrfParameterSpec) algorithmParameterSpec;
        SecretKey secret = this.spec.getSecret();
        if (secret != null && !"RAW".equals(secret.getFormat())) {
            throw new InvalidAlgorithmParameterException("Key encoding format must be RAW");
        }
    }

    @Override // javax.crypto.KeyGeneratorSpi
    protected void engineInit(int i2, SecureRandom secureRandom) {
        throw new InvalidParameterException(MSG);
    }

    SecretKey engineGenerateKey0(boolean z2) {
        byte[] bArrDoTLS10PRF;
        if (this.spec == null) {
            throw new IllegalStateException("TlsPrfGenerator must be initialized");
        }
        SecretKey secret = this.spec.getSecret();
        byte[] encoded = secret == null ? null : secret.getEncoded();
        try {
            byte[] bytes = this.spec.getLabel().getBytes(InternalZipConstants.CHARSET_UTF8);
            int outputLength = this.spec.getOutputLength();
            if (z2) {
                bArrDoTLS10PRF = doTLS12PRF(encoded, bytes, this.spec.getSeed(), outputLength, this.spec.getPRFHashAlg(), this.spec.getPRFHashLength(), this.spec.getPRFBlockSize());
            } else {
                bArrDoTLS10PRF = doTLS10PRF(encoded, bytes, this.spec.getSeed(), outputLength);
            }
            return new SecretKeySpec(bArrDoTLS10PRF, "TlsPrf");
        } catch (UnsupportedEncodingException e2) {
            throw new ProviderException("Could not generate PRF", e2);
        } catch (GeneralSecurityException e3) {
            throw new ProviderException("Could not generate PRF", e3);
        }
    }

    static byte[] doTLS12PRF(byte[] bArr, byte[] bArr2, byte[] bArr3, int i2, String str, int i3, int i4) throws NoSuchAlgorithmException, DigestException {
        if (str == null) {
            throw new NoSuchAlgorithmException("Unspecified PRF algorithm");
        }
        return doTLS12PRF(bArr, bArr2, bArr3, i2, MessageDigest.getInstance(str), i3, i4);
    }

    static byte[] doTLS12PRF(byte[] bArr, byte[] bArr2, byte[] bArr3, int i2, MessageDigest messageDigest, int i3, int i4) throws DigestException {
        byte[] bArr4;
        byte[] bArr5;
        if (bArr == null) {
            bArr = B0;
        }
        if (bArr.length > i4) {
            bArr = messageDigest.digest(bArr);
        }
        byte[] bArr6 = new byte[i2];
        switch (i4) {
            case 64:
                bArr4 = (byte[]) HMAC_ipad64.clone();
                bArr5 = (byte[]) HMAC_opad64.clone();
                break;
            case 128:
                bArr4 = (byte[]) HMAC_ipad128.clone();
                bArr5 = (byte[]) HMAC_opad128.clone();
                break;
            default:
                throw new DigestException("Unexpected block size.");
        }
        expand(messageDigest, i3, bArr, 0, bArr.length, bArr2, bArr3, bArr6, bArr4, bArr5);
        return bArr6;
    }

    static byte[] doTLS10PRF(byte[] bArr, byte[] bArr2, byte[] bArr3, int i2) throws NoSuchAlgorithmException, DigestException {
        return doTLS10PRF(bArr, bArr2, bArr3, i2, MessageDigest.getInstance("MD5"), MessageDigest.getInstance("SHA1"));
    }

    static byte[] doTLS10PRF(byte[] bArr, byte[] bArr2, byte[] bArr3, int i2, MessageDigest messageDigest, MessageDigest messageDigest2) throws DigestException {
        if (bArr == null) {
            bArr = B0;
        }
        int length = bArr.length >> 1;
        int length2 = length + (bArr.length & 1);
        byte[] bArrDigest = bArr;
        int length3 = length2;
        byte[] bArr4 = new byte[i2];
        if (length2 > 64) {
            messageDigest.update(bArr, 0, length2);
            bArrDigest = messageDigest.digest();
            length3 = bArrDigest.length;
        }
        expand(messageDigest, 16, bArrDigest, 0, length3, bArr2, bArr3, bArr4, (byte[]) HMAC_ipad64.clone(), (byte[]) HMAC_opad64.clone());
        if (length2 > 64) {
            messageDigest2.update(bArr, length, length2);
            bArrDigest = messageDigest2.digest();
            length3 = bArrDigest.length;
            length = 0;
        }
        expand(messageDigest2, 20, bArrDigest, length, length3, bArr2, bArr3, bArr4, (byte[]) HMAC_ipad64.clone(), (byte[]) HMAC_opad64.clone());
        return bArr4;
    }

    private static void expand(MessageDigest messageDigest, int i2, byte[] bArr, int i3, int i4, byte[] bArr2, byte[] bArr3, byte[] bArr4, byte[] bArr5, byte[] bArr6) throws DigestException {
        for (int i5 = 0; i5 < i4; i5++) {
            int i6 = i5;
            bArr5[i6] = (byte) (bArr5[i6] ^ bArr[i5 + i3]);
            int i7 = i5;
            bArr6[i7] = (byte) (bArr6[i7] ^ bArr[i5 + i3]);
        }
        byte[] bArr7 = new byte[i2];
        byte[] bArr8 = null;
        int length = bArr4.length;
        int i8 = 0;
        while (length > 0) {
            messageDigest.update(bArr5);
            if (bArr8 == null) {
                messageDigest.update(bArr2);
                messageDigest.update(bArr3);
            } else {
                messageDigest.update(bArr8);
            }
            messageDigest.digest(bArr7, 0, i2);
            messageDigest.update(bArr6);
            messageDigest.update(bArr7);
            if (bArr8 == null) {
                bArr8 = new byte[i2];
            }
            messageDigest.digest(bArr8, 0, i2);
            messageDigest.update(bArr5);
            messageDigest.update(bArr8);
            messageDigest.update(bArr2);
            messageDigest.update(bArr3);
            messageDigest.digest(bArr7, 0, i2);
            messageDigest.update(bArr6);
            messageDigest.update(bArr7);
            messageDigest.digest(bArr7, 0, i2);
            int iMin = Math.min(i2, length);
            for (int i9 = 0; i9 < iMin; i9++) {
                int i10 = i8;
                i8++;
                bArr4[i10] = (byte) (bArr4[i10] ^ bArr7[i9]);
            }
            length -= iMin;
        }
    }

    /* loaded from: sunjce_provider.jar:com/sun/crypto/provider/TlsPrfGenerator$V12.class */
    public static class V12 extends TlsPrfGenerator {
        @Override // javax.crypto.KeyGeneratorSpi
        protected SecretKey engineGenerateKey() {
            return engineGenerateKey0(true);
        }
    }

    /* loaded from: sunjce_provider.jar:com/sun/crypto/provider/TlsPrfGenerator$V10.class */
    public static class V10 extends TlsPrfGenerator {
        @Override // javax.crypto.KeyGeneratorSpi
        protected SecretKey engineGenerateKey() {
            return engineGenerateKey0(false);
        }
    }
}
