package sun.security.jgss.krb5;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import org.ietf.jgss.GSSException;
import sun.security.krb5.EncryptionKey;
import sun.security.krb5.internal.crypto.Aes128;
import sun.security.krb5.internal.crypto.Aes256;
import sun.security.krb5.internal.crypto.ArcFourHmac;
import sun.security.krb5.internal.crypto.Des3;
import sun.security.krb5.internal.crypto.EType;
import sun.text.normalizer.NormalizerImpl;

/* loaded from: rt.jar:sun/security/jgss/krb5/CipherHelper.class */
class CipherHelper {
    private static final int KG_USAGE_SEAL = 22;
    private static final int KG_USAGE_SIGN = 23;
    private static final int KG_USAGE_SEQ = 24;
    private static final int DES_CHECKSUM_SIZE = 8;
    private static final int DES_IV_SIZE = 8;
    private static final int AES_IV_SIZE = 16;
    private static final int HMAC_CHECKSUM_SIZE = 8;
    private static final int KG_USAGE_SIGN_MS = 15;
    private static final boolean DEBUG = Krb5Util.DEBUG;
    private static final byte[] ZERO_IV = new byte[8];
    private static final byte[] ZERO_IV_AES = new byte[16];
    private int etype;
    private int sgnAlg;
    private int sealAlg;
    private byte[] keybytes;

    CipherHelper(EncryptionKey encryptionKey) throws GSSException {
        this.etype = encryptionKey.getEType();
        this.keybytes = encryptionKey.getBytes();
        switch (this.etype) {
            case 1:
            case 3:
                this.sgnAlg = 0;
                this.sealAlg = 0;
                return;
            case 16:
                this.sgnAlg = 1024;
                this.sealAlg = 512;
                return;
            case 17:
            case 18:
                this.sgnAlg = -1;
                this.sealAlg = -1;
                return;
            case 23:
                this.sgnAlg = NormalizerImpl.JAMO_L_BASE;
                this.sealAlg = 4096;
                return;
            default:
                throw new GSSException(11, -1, "Unsupported encryption type: " + this.etype);
        }
    }

    int getSgnAlg() {
        return this.sgnAlg;
    }

    int getSealAlg() {
        return this.sealAlg;
    }

    int getProto() {
        return EType.isNewer(this.etype) ? 1 : 0;
    }

    int getEType() {
        return this.etype;
    }

    boolean isArcFour() {
        boolean z2 = false;
        if (this.etype == 23) {
            z2 = true;
        }
        return z2;
    }

    byte[] calculateChecksum(int i2, byte[] bArr, byte[] bArr2, byte[] bArr3, int i3, int i4, int i5) throws GSSException {
        int length;
        byte[] bArr4;
        int i6;
        int length2;
        byte[] bArr5;
        int i7;
        switch (i2) {
            case 0:
                try {
                    MessageDigest messageDigest = MessageDigest.getInstance("MD5");
                    messageDigest.update(bArr);
                    messageDigest.update(bArr3, i3, i4);
                    if (bArr2 != null) {
                        messageDigest.update(bArr2);
                    }
                    bArr3 = messageDigest.digest();
                    i3 = 0;
                    i4 = bArr3.length;
                    bArr = null;
                    break;
                } catch (NoSuchAlgorithmException e2) {
                    GSSException gSSException = new GSSException(11, -1, "Could not get MD5 Message Digest - " + e2.getMessage());
                    gSSException.initCause(e2);
                    throw gSSException;
                }
            case 512:
                break;
            case 1024:
                if (bArr == null && bArr2 == null) {
                    bArr5 = bArr3;
                    length2 = i4;
                    i7 = i3;
                } else {
                    length2 = (bArr != null ? bArr.length : 0) + i4 + (bArr2 != null ? bArr2.length : 0);
                    bArr5 = new byte[length2];
                    int length3 = 0;
                    if (bArr != null) {
                        System.arraycopy(bArr, 0, bArr5, 0, bArr.length);
                        length3 = bArr.length;
                    }
                    System.arraycopy(bArr3, i3, bArr5, length3, i4);
                    int i8 = length3 + i4;
                    if (bArr2 != null) {
                        System.arraycopy(bArr2, 0, bArr5, i8, bArr2.length);
                    }
                    i7 = 0;
                }
                try {
                    return Des3.calculateChecksum(this.keybytes, 23, bArr5, i7, length2);
                } catch (GeneralSecurityException e3) {
                    GSSException gSSException2 = new GSSException(11, -1, "Could not use HMAC-SHA1-DES3-KD signing algorithm - " + e3.getMessage());
                    gSSException2.initCause(e3);
                    throw gSSException2;
                }
            case NormalizerImpl.JAMO_L_BASE /* 4352 */:
                if (bArr == null && bArr2 == null) {
                    bArr4 = bArr3;
                    length = i4;
                    i6 = i3;
                } else {
                    length = (bArr != null ? bArr.length : 0) + i4 + (bArr2 != null ? bArr2.length : 0);
                    bArr4 = new byte[length];
                    int length4 = 0;
                    if (bArr != null) {
                        System.arraycopy(bArr, 0, bArr4, 0, bArr.length);
                        length4 = bArr.length;
                    }
                    System.arraycopy(bArr3, i3, bArr4, length4, i4);
                    int i9 = length4 + i4;
                    if (bArr2 != null) {
                        System.arraycopy(bArr2, 0, bArr4, i9, bArr2.length);
                    }
                    i6 = 0;
                }
                int i10 = 23;
                if (i5 == 257) {
                    i10 = 15;
                }
                try {
                    byte[] bArrCalculateChecksum = ArcFourHmac.calculateChecksum(this.keybytes, i10, bArr4, i6, length);
                    byte[] bArr6 = new byte[getChecksumLength()];
                    System.arraycopy(bArrCalculateChecksum, 0, bArr6, 0, bArr6.length);
                    return bArr6;
                } catch (GeneralSecurityException e4) {
                    GSSException gSSException3 = new GSSException(11, -1, "Could not use HMAC_MD5_ARCFOUR signing algorithm - " + e4.getMessage());
                    gSSException3.initCause(e4);
                    throw gSSException3;
                }
            default:
                throw new GSSException(11, -1, "Unsupported signing algorithm: " + this.sgnAlg);
        }
        return getDesCbcChecksum(this.keybytes, bArr, bArr3, i3, i4);
    }

    byte[] calculateChecksum(byte[] bArr, byte[] bArr2, int i2, int i3, int i4) throws GSSException {
        int length = (bArr != null ? bArr.length : 0) + i3;
        byte[] bArr3 = new byte[length];
        System.arraycopy(bArr2, i2, bArr3, 0, i3);
        if (bArr != null) {
            System.arraycopy(bArr, 0, bArr3, i3, bArr.length);
        }
        switch (this.etype) {
            case 17:
                try {
                    return Aes128.calculateChecksum(this.keybytes, i4, bArr3, 0, length);
                } catch (GeneralSecurityException e2) {
                    GSSException gSSException = new GSSException(11, -1, "Could not use AES128 signing algorithm - " + e2.getMessage());
                    gSSException.initCause(e2);
                    throw gSSException;
                }
            case 18:
                try {
                    return Aes256.calculateChecksum(this.keybytes, i4, bArr3, 0, length);
                } catch (GeneralSecurityException e3) {
                    GSSException gSSException2 = new GSSException(11, -1, "Could not use AES256 signing algorithm - " + e3.getMessage());
                    gSSException2.initCause(e3);
                    throw gSSException2;
                }
            default:
                throw new GSSException(11, -1, "Unsupported encryption type: " + this.etype);
        }
    }

    byte[] encryptSeq(byte[] bArr, byte[] bArr2, int i2, int i3) throws GSSException {
        byte[] bArr3;
        byte[] bArr4;
        switch (this.sgnAlg) {
            case 0:
            case 512:
                try {
                    return getInitializedDes(true, this.keybytes, bArr).doFinal(bArr2, i2, i3);
                } catch (GeneralSecurityException e2) {
                    GSSException gSSException = new GSSException(11, -1, "Could not encrypt sequence number using DES - " + e2.getMessage());
                    gSSException.initCause(e2);
                    throw gSSException;
                }
            case 1024:
                if (bArr.length == 8) {
                    bArr4 = bArr;
                } else {
                    bArr4 = new byte[8];
                    System.arraycopy(bArr, 0, bArr4, 0, 8);
                }
                try {
                    return Des3.encryptRaw(this.keybytes, 24, bArr4, bArr2, i2, i3);
                } catch (Exception e3) {
                    GSSException gSSException2 = new GSSException(11, -1, "Could not encrypt sequence number using DES3-KD - " + e3.getMessage());
                    gSSException2.initCause(e3);
                    throw gSSException2;
                }
            case NormalizerImpl.JAMO_L_BASE /* 4352 */:
                if (bArr.length == 8) {
                    bArr3 = bArr;
                } else {
                    bArr3 = new byte[8];
                    System.arraycopy(bArr, 0, bArr3, 0, 8);
                }
                try {
                    return ArcFourHmac.encryptSeq(this.keybytes, 24, bArr3, bArr2, i2, i3);
                } catch (Exception e4) {
                    GSSException gSSException3 = new GSSException(11, -1, "Could not encrypt sequence number using RC4-HMAC - " + e4.getMessage());
                    gSSException3.initCause(e4);
                    throw gSSException3;
                }
            default:
                throw new GSSException(11, -1, "Unsupported signing algorithm: " + this.sgnAlg);
        }
    }

    byte[] decryptSeq(byte[] bArr, byte[] bArr2, int i2, int i3) throws GSSException {
        byte[] bArr3;
        byte[] bArr4;
        switch (this.sgnAlg) {
            case 0:
            case 512:
                try {
                    return getInitializedDes(false, this.keybytes, bArr).doFinal(bArr2, i2, i3);
                } catch (GeneralSecurityException e2) {
                    GSSException gSSException = new GSSException(11, -1, "Could not decrypt sequence number using DES - " + e2.getMessage());
                    gSSException.initCause(e2);
                    throw gSSException;
                }
            case 1024:
                if (bArr.length == 8) {
                    bArr4 = bArr;
                } else {
                    bArr4 = new byte[8];
                    System.arraycopy(bArr, 0, bArr4, 0, 8);
                }
                try {
                    return Des3.decryptRaw(this.keybytes, 24, bArr4, bArr2, i2, i3);
                } catch (Exception e3) {
                    GSSException gSSException2 = new GSSException(11, -1, "Could not decrypt sequence number using DES3-KD - " + e3.getMessage());
                    gSSException2.initCause(e3);
                    throw gSSException2;
                }
            case NormalizerImpl.JAMO_L_BASE /* 4352 */:
                if (bArr.length == 8) {
                    bArr3 = bArr;
                } else {
                    bArr3 = new byte[8];
                    System.arraycopy(bArr, 0, bArr3, 0, 8);
                }
                try {
                    return ArcFourHmac.decryptSeq(this.keybytes, 24, bArr3, bArr2, i2, i3);
                } catch (Exception e4) {
                    GSSException gSSException3 = new GSSException(11, -1, "Could not decrypt sequence number using RC4-HMAC - " + e4.getMessage());
                    gSSException3.initCause(e4);
                    throw gSSException3;
                }
            default:
                throw new GSSException(11, -1, "Unsupported signing algorithm: " + this.sgnAlg);
        }
    }

    int getChecksumLength() throws GSSException {
        switch (this.etype) {
            case 1:
            case 3:
                return 8;
            case 16:
                return Des3.getChecksumLength();
            case 17:
                return Aes128.getChecksumLength();
            case 18:
                return Aes256.getChecksumLength();
            case 23:
                return 8;
            default:
                throw new GSSException(11, -1, "Unsupported encryption type: " + this.etype);
        }
    }

    void decryptData(WrapToken wrapToken, byte[] bArr, int i2, int i3, byte[] bArr2, int i4) throws GSSException {
        switch (this.sealAlg) {
            case 0:
                desCbcDecrypt(wrapToken, getDesEncryptionKey(this.keybytes), bArr, i2, i3, bArr2, i4);
                return;
            case 512:
                des3KdDecrypt(wrapToken, bArr, i2, i3, bArr2, i4);
                return;
            case 4096:
                arcFourDecrypt(wrapToken, bArr, i2, i3, bArr2, i4);
                return;
            default:
                throw new GSSException(11, -1, "Unsupported seal algorithm: " + this.sealAlg);
        }
    }

    void decryptData(WrapToken_v2 wrapToken_v2, byte[] bArr, int i2, int i3, byte[] bArr2, int i4, int i5) throws GSSException {
        switch (this.etype) {
            case 17:
                aes128Decrypt(wrapToken_v2, bArr, i2, i3, bArr2, i4, i5);
                return;
            case 18:
                aes256Decrypt(wrapToken_v2, bArr, i2, i3, bArr2, i4, i5);
                return;
            default:
                throw new GSSException(11, -1, "Unsupported etype: " + this.etype);
        }
    }

    void decryptData(WrapToken wrapToken, InputStream inputStream, int i2, byte[] bArr, int i3) throws IOException, GSSException {
        switch (this.sealAlg) {
            case 0:
                desCbcDecrypt(wrapToken, getDesEncryptionKey(this.keybytes), inputStream, i2, bArr, i3);
                return;
            case 512:
                byte[] bArr2 = new byte[i2];
                try {
                    Krb5Token.readFully(inputStream, bArr2, 0, i2);
                    des3KdDecrypt(wrapToken, bArr2, 0, i2, bArr, i3);
                    return;
                } catch (IOException e2) {
                    GSSException gSSException = new GSSException(10, -1, "Cannot read complete token");
                    gSSException.initCause(e2);
                    throw gSSException;
                }
            case 4096:
                byte[] bArr3 = new byte[i2];
                try {
                    Krb5Token.readFully(inputStream, bArr3, 0, i2);
                    arcFourDecrypt(wrapToken, bArr3, 0, i2, bArr, i3);
                    return;
                } catch (IOException e3) {
                    GSSException gSSException2 = new GSSException(10, -1, "Cannot read complete token");
                    gSSException2.initCause(e3);
                    throw gSSException2;
                }
            default:
                throw new GSSException(11, -1, "Unsupported seal algorithm: " + this.sealAlg);
        }
    }

    void decryptData(WrapToken_v2 wrapToken_v2, InputStream inputStream, int i2, byte[] bArr, int i3, int i4) throws IOException, GSSException {
        byte[] bArr2 = new byte[i2];
        try {
            Krb5Token.readFully(inputStream, bArr2, 0, i2);
            switch (this.etype) {
                case 17:
                    aes128Decrypt(wrapToken_v2, bArr2, 0, i2, bArr, i3, i4);
                    return;
                case 18:
                    aes256Decrypt(wrapToken_v2, bArr2, 0, i2, bArr, i3, i4);
                    return;
                default:
                    throw new GSSException(11, -1, "Unsupported etype: " + this.etype);
            }
        } catch (IOException e2) {
            GSSException gSSException = new GSSException(10, -1, "Cannot read complete token");
            gSSException.initCause(e2);
            throw gSSException;
        }
    }

    void encryptData(WrapToken wrapToken, byte[] bArr, byte[] bArr2, int i2, int i3, byte[] bArr3, OutputStream outputStream) throws IOException, GSSException {
        switch (this.sealAlg) {
            case 0:
                CipherOutputStream cipherOutputStream = new CipherOutputStream(outputStream, getInitializedDes(true, getDesEncryptionKey(this.keybytes), ZERO_IV));
                cipherOutputStream.write(bArr);
                cipherOutputStream.write(bArr2, i2, i3);
                cipherOutputStream.write(bArr3);
                return;
            case 512:
                outputStream.write(des3KdEncrypt(bArr, bArr2, i2, i3, bArr3));
                return;
            case 4096:
                outputStream.write(arcFourEncrypt(wrapToken, bArr, bArr2, i2, i3, bArr3));
                return;
            default:
                throw new GSSException(11, -1, "Unsupported seal algorithm: " + this.sealAlg);
        }
    }

    byte[] encryptData(WrapToken_v2 wrapToken_v2, byte[] bArr, byte[] bArr2, byte[] bArr3, int i2, int i3, int i4) throws GSSException {
        switch (this.etype) {
            case 17:
                return aes128Encrypt(bArr, bArr2, bArr3, i2, i3, i4);
            case 18:
                return aes256Encrypt(bArr, bArr2, bArr3, i2, i3, i4);
            default:
                throw new GSSException(11, -1, "Unsupported etype: " + this.etype);
        }
    }

    void encryptData(WrapToken wrapToken, byte[] bArr, byte[] bArr2, int i2, int i3, byte[] bArr3, byte[] bArr4, int i4) throws GSSException {
        switch (this.sealAlg) {
            case 0:
                Cipher initializedDes = getInitializedDes(true, getDesEncryptionKey(this.keybytes), ZERO_IV);
                try {
                    int iUpdate = i4 + initializedDes.update(bArr, 0, bArr.length, bArr4, i4);
                    initializedDes.update(bArr3, 0, bArr3.length, bArr4, iUpdate + initializedDes.update(bArr2, i2, i3, bArr4, iUpdate));
                    initializedDes.doFinal();
                    return;
                } catch (GeneralSecurityException e2) {
                    GSSException gSSException = new GSSException(11, -1, "Could not use DES Cipher - " + e2.getMessage());
                    gSSException.initCause(e2);
                    throw gSSException;
                }
            case 512:
                byte[] bArrDes3KdEncrypt = des3KdEncrypt(bArr, bArr2, i2, i3, bArr3);
                System.arraycopy(bArrDes3KdEncrypt, 0, bArr4, i4, bArrDes3KdEncrypt.length);
                return;
            case 4096:
                byte[] bArrArcFourEncrypt = arcFourEncrypt(wrapToken, bArr, bArr2, i2, i3, bArr3);
                System.arraycopy(bArrArcFourEncrypt, 0, bArr4, i4, bArrArcFourEncrypt.length);
                return;
            default:
                throw new GSSException(11, -1, "Unsupported seal algorithm: " + this.sealAlg);
        }
    }

    int encryptData(WrapToken_v2 wrapToken_v2, byte[] bArr, byte[] bArr2, byte[] bArr3, int i2, int i3, byte[] bArr4, int i4, int i5) throws GSSException {
        byte[] bArrAes256Encrypt;
        switch (this.etype) {
            case 17:
                bArrAes256Encrypt = aes128Encrypt(bArr, bArr2, bArr3, i2, i3, i5);
                break;
            case 18:
                bArrAes256Encrypt = aes256Encrypt(bArr, bArr2, bArr3, i2, i3, i5);
                break;
            default:
                throw new GSSException(11, -1, "Unsupported etype: " + this.etype);
        }
        System.arraycopy(bArrAes256Encrypt, 0, bArr4, i4, bArrAes256Encrypt.length);
        return bArrAes256Encrypt.length;
    }

    private byte[] getDesCbcChecksum(byte[] bArr, byte[] bArr2, byte[] bArr3, int i2, int i3) throws GSSException {
        int length;
        Cipher initializedDes = getInitializedDes(true, bArr, ZERO_IV);
        int blockSize = initializedDes.getBlockSize();
        byte[] bArr4 = new byte[blockSize];
        int i4 = i3 / blockSize;
        int i5 = i3 % blockSize;
        if (i5 == 0) {
            i4--;
            System.arraycopy(bArr3, i2 + (i4 * blockSize), bArr4, 0, blockSize);
        } else {
            System.arraycopy(bArr3, i2 + (i4 * blockSize), bArr4, 0, i5);
        }
        if (bArr2 == null) {
            length = blockSize;
        } else {
            try {
                length = bArr2.length;
            } catch (GeneralSecurityException e2) {
                GSSException gSSException = new GSSException(11, -1, "Could not use DES Cipher - " + e2.getMessage());
                gSSException.initCause(e2);
                throw gSSException;
            }
        }
        byte[] bArr5 = new byte[Math.max(blockSize, length)];
        if (bArr2 != null) {
            initializedDes.update(bArr2, 0, bArr2.length, bArr5, 0);
        }
        for (int i6 = 0; i6 < i4; i6++) {
            initializedDes.update(bArr3, i2, blockSize, bArr5, 0);
            i2 += blockSize;
        }
        byte[] bArr6 = new byte[blockSize];
        initializedDes.update(bArr4, 0, blockSize, bArr6, 0);
        initializedDes.doFinal();
        return bArr6;
    }

    private final Cipher getInitializedDes(boolean z2, byte[] bArr, byte[] bArr2) throws GSSException {
        try {
            IvParameterSpec ivParameterSpec = new IvParameterSpec(bArr2);
            SecretKeySpec secretKeySpec = new SecretKeySpec(bArr, "DES");
            Cipher cipher = Cipher.getInstance("DES/CBC/NoPadding");
            cipher.init(z2 ? 1 : 2, secretKeySpec, ivParameterSpec);
            return cipher;
        } catch (GeneralSecurityException e2) {
            GSSException gSSException = new GSSException(11, -1, e2.getMessage());
            gSSException.initCause(e2);
            throw gSSException;
        }
    }

    private void desCbcDecrypt(WrapToken wrapToken, byte[] bArr, byte[] bArr2, int i2, int i3, byte[] bArr3, int i4) throws GSSException {
        try {
            Cipher initializedDes = getInitializedDes(false, bArr, ZERO_IV);
            initializedDes.update(bArr2, i2, 8, wrapToken.confounder);
            int i5 = i2 + 8;
            int blockSize = initializedDes.getBlockSize();
            int i6 = ((i3 - 8) / blockSize) - 1;
            for (int i7 = 0; i7 < i6; i7++) {
                initializedDes.update(bArr2, i5, blockSize, bArr3, i4);
                i5 += blockSize;
                i4 += blockSize;
            }
            byte[] bArr4 = new byte[blockSize];
            initializedDes.update(bArr2, i5, blockSize, bArr4);
            initializedDes.doFinal();
            byte b2 = bArr4[blockSize - 1];
            if (b2 < 1 || b2 > 8) {
                throw new GSSException(10, -1, "Invalid padding on Wrap Token");
            }
            wrapToken.padding = WrapToken.pads[b2];
            System.arraycopy(bArr4, 0, bArr3, i4, blockSize - b2);
        } catch (GeneralSecurityException e2) {
            GSSException gSSException = new GSSException(11, -1, "Could not use DES cipher - " + e2.getMessage());
            gSSException.initCause(e2);
            throw gSSException;
        }
    }

    private void desCbcDecrypt(WrapToken wrapToken, byte[] bArr, InputStream inputStream, int i2, byte[] bArr2, int i3) throws IOException, GSSException {
        Cipher initializedDes = getInitializedDes(false, bArr, ZERO_IV);
        CipherInputStream cipherInputStream = new CipherInputStream(new WrapTokenInputStream(inputStream, i2), initializedDes);
        int i4 = i2 - cipherInputStream.read(wrapToken.confounder);
        int blockSize = initializedDes.getBlockSize();
        int i5 = (i4 / blockSize) - 1;
        for (int i6 = 0; i6 < i5; i6++) {
            cipherInputStream.read(bArr2, i3, blockSize);
            i3 += blockSize;
        }
        byte[] bArr3 = new byte[blockSize];
        cipherInputStream.read(bArr3);
        try {
            initializedDes.doFinal();
            byte b2 = bArr3[blockSize - 1];
            if (b2 < 1 || b2 > 8) {
                throw new GSSException(10, -1, "Invalid padding on Wrap Token");
            }
            wrapToken.padding = WrapToken.pads[b2];
            System.arraycopy(bArr3, 0, bArr2, i3, blockSize - b2);
        } catch (GeneralSecurityException e2) {
            GSSException gSSException = new GSSException(11, -1, "Could not use DES cipher - " + e2.getMessage());
            gSSException.initCause(e2);
            throw gSSException;
        }
    }

    private static byte[] getDesEncryptionKey(byte[] bArr) throws GSSException {
        if (bArr.length > 8) {
            throw new GSSException(11, -100, "Invalid DES Key!");
        }
        byte[] bArr2 = new byte[bArr.length];
        for (int i2 = 0; i2 < bArr.length; i2++) {
            bArr2[i2] = (byte) (bArr[i2] ^ 240);
        }
        return bArr2;
    }

    private void des3KdDecrypt(WrapToken wrapToken, byte[] bArr, int i2, int i3, byte[] bArr2, int i4) throws GSSException {
        try {
            byte[] bArrDecryptRaw = Des3.decryptRaw(this.keybytes, 22, ZERO_IV, bArr, i2, i3);
            byte b2 = bArrDecryptRaw[bArrDecryptRaw.length - 1];
            if (b2 < 1 || b2 > 8) {
                throw new GSSException(10, -1, "Invalid padding on Wrap Token");
            }
            wrapToken.padding = WrapToken.pads[b2];
            System.arraycopy(bArrDecryptRaw, 8, bArr2, i4, (bArrDecryptRaw.length - 8) - b2);
            System.arraycopy(bArrDecryptRaw, 0, wrapToken.confounder, 0, 8);
        } catch (GeneralSecurityException e2) {
            GSSException gSSException = new GSSException(11, -1, "Could not use DES3-KD Cipher - " + e2.getMessage());
            gSSException.initCause(e2);
            throw gSSException;
        }
    }

    private byte[] des3KdEncrypt(byte[] bArr, byte[] bArr2, int i2, int i3, byte[] bArr3) throws GSSException {
        byte[] bArr4 = new byte[bArr.length + i3 + bArr3.length];
        System.arraycopy(bArr, 0, bArr4, 0, bArr.length);
        System.arraycopy(bArr2, i2, bArr4, bArr.length, i3);
        System.arraycopy(bArr3, 0, bArr4, bArr.length + i3, bArr3.length);
        try {
            return Des3.encryptRaw(this.keybytes, 22, ZERO_IV, bArr4, 0, bArr4.length);
        } catch (Exception e2) {
            GSSException gSSException = new GSSException(11, -1, "Could not use DES3-KD Cipher - " + e2.getMessage());
            gSSException.initCause(e2);
            throw gSSException;
        }
    }

    private void arcFourDecrypt(WrapToken wrapToken, byte[] bArr, int i2, int i3, byte[] bArr2, int i4) throws GSSException {
        try {
            byte[] bArrDecryptRaw = ArcFourHmac.decryptRaw(this.keybytes, 22, ZERO_IV, bArr, i2, i3, decryptSeq(wrapToken.getChecksum(), wrapToken.getEncSeqNumber(), 0, 8));
            byte b2 = bArrDecryptRaw[bArrDecryptRaw.length - 1];
            if (b2 < 1) {
                throw new GSSException(10, -1, "Invalid padding on Wrap Token");
            }
            wrapToken.padding = WrapToken.pads[b2];
            System.arraycopy(bArrDecryptRaw, 8, bArr2, i4, (bArrDecryptRaw.length - 8) - b2);
            System.arraycopy(bArrDecryptRaw, 0, wrapToken.confounder, 0, 8);
        } catch (GeneralSecurityException e2) {
            GSSException gSSException = new GSSException(11, -1, "Could not use ArcFour Cipher - " + e2.getMessage());
            gSSException.initCause(e2);
            throw gSSException;
        }
    }

    private byte[] arcFourEncrypt(WrapToken wrapToken, byte[] bArr, byte[] bArr2, int i2, int i3, byte[] bArr3) throws GSSException {
        byte[] bArr4 = new byte[bArr.length + i3 + bArr3.length];
        System.arraycopy(bArr, 0, bArr4, 0, bArr.length);
        System.arraycopy(bArr2, i2, bArr4, bArr.length, i3);
        System.arraycopy(bArr3, 0, bArr4, bArr.length + i3, bArr3.length);
        byte[] bArr5 = new byte[4];
        WrapToken.writeBigEndian(wrapToken.getSequenceNumber(), bArr5);
        try {
            return ArcFourHmac.encryptRaw(this.keybytes, 22, bArr5, bArr4, 0, bArr4.length);
        } catch (Exception e2) {
            GSSException gSSException = new GSSException(11, -1, "Could not use ArcFour Cipher - " + e2.getMessage());
            gSSException.initCause(e2);
            throw gSSException;
        }
    }

    private byte[] aes128Encrypt(byte[] bArr, byte[] bArr2, byte[] bArr3, int i2, int i3, int i4) throws GSSException {
        byte[] bArr4 = new byte[bArr.length + i3 + bArr2.length];
        System.arraycopy(bArr, 0, bArr4, 0, bArr.length);
        System.arraycopy(bArr3, i2, bArr4, bArr.length, i3);
        System.arraycopy(bArr2, 0, bArr4, bArr.length + i3, bArr2.length);
        try {
            return Aes128.encryptRaw(this.keybytes, i4, ZERO_IV_AES, bArr4, 0, bArr4.length);
        } catch (Exception e2) {
            GSSException gSSException = new GSSException(11, -1, "Could not use AES128 Cipher - " + e2.getMessage());
            gSSException.initCause(e2);
            throw gSSException;
        }
    }

    private void aes128Decrypt(WrapToken_v2 wrapToken_v2, byte[] bArr, int i2, int i3, byte[] bArr2, int i4, int i5) throws GSSException {
        try {
            byte[] bArrDecryptRaw = Aes128.decryptRaw(this.keybytes, i5, ZERO_IV_AES, bArr, i2, i3);
            System.arraycopy(bArrDecryptRaw, 16, bArr2, i4, (bArrDecryptRaw.length - 16) - 16);
        } catch (GeneralSecurityException e2) {
            GSSException gSSException = new GSSException(11, -1, "Could not use AES128 Cipher - " + e2.getMessage());
            gSSException.initCause(e2);
            throw gSSException;
        }
    }

    private byte[] aes256Encrypt(byte[] bArr, byte[] bArr2, byte[] bArr3, int i2, int i3, int i4) throws GSSException {
        byte[] bArr4 = new byte[bArr.length + i3 + bArr2.length];
        System.arraycopy(bArr, 0, bArr4, 0, bArr.length);
        System.arraycopy(bArr3, i2, bArr4, bArr.length, i3);
        System.arraycopy(bArr2, 0, bArr4, bArr.length + i3, bArr2.length);
        try {
            return Aes256.encryptRaw(this.keybytes, i4, ZERO_IV_AES, bArr4, 0, bArr4.length);
        } catch (Exception e2) {
            GSSException gSSException = new GSSException(11, -1, "Could not use AES256 Cipher - " + e2.getMessage());
            gSSException.initCause(e2);
            throw gSSException;
        }
    }

    private void aes256Decrypt(WrapToken_v2 wrapToken_v2, byte[] bArr, int i2, int i3, byte[] bArr2, int i4, int i5) throws GSSException {
        try {
            byte[] bArrDecryptRaw = Aes256.decryptRaw(this.keybytes, i5, ZERO_IV_AES, bArr, i2, i3);
            System.arraycopy(bArrDecryptRaw, 16, bArr2, i4, (bArrDecryptRaw.length - 16) - 16);
        } catch (GeneralSecurityException e2) {
            GSSException gSSException = new GSSException(11, -1, "Could not use AES128 Cipher - " + e2.getMessage());
            gSSException.initCause(e2);
            throw gSSException;
        }
    }

    /* loaded from: rt.jar:sun/security/jgss/krb5/CipherHelper$WrapTokenInputStream.class */
    class WrapTokenInputStream extends InputStream {
        private InputStream is;
        private int length;
        private int remaining;
        private int temp;

        public WrapTokenInputStream(InputStream inputStream, int i2) {
            this.is = inputStream;
            this.length = i2;
            this.remaining = i2;
        }

        @Override // java.io.InputStream
        public final int read() throws IOException {
            if (this.remaining == 0) {
                return -1;
            }
            this.temp = this.is.read();
            if (this.temp != -1) {
                this.remaining -= this.temp;
            }
            return this.temp;
        }

        @Override // java.io.InputStream
        public final int read(byte[] bArr) throws IOException {
            if (this.remaining == 0) {
                return -1;
            }
            this.temp = Math.min(this.remaining, bArr.length);
            this.temp = this.is.read(bArr, 0, this.temp);
            if (this.temp != -1) {
                this.remaining -= this.temp;
            }
            return this.temp;
        }

        @Override // java.io.InputStream
        public final int read(byte[] bArr, int i2, int i3) throws IOException {
            if (this.remaining == 0) {
                return -1;
            }
            this.temp = Math.min(this.remaining, i3);
            this.temp = this.is.read(bArr, i2, this.temp);
            if (this.temp != -1) {
                this.remaining -= this.temp;
            }
            return this.temp;
        }

        @Override // java.io.InputStream
        public final long skip(long j2) throws IOException {
            if (this.remaining == 0) {
                return 0L;
            }
            this.temp = (int) Math.min(this.remaining, j2);
            this.temp = (int) this.is.skip(this.temp);
            this.remaining -= this.temp;
            return this.temp;
        }

        @Override // java.io.InputStream
        public final int available() throws IOException {
            return Math.min(this.remaining, this.is.available());
        }

        @Override // java.io.InputStream, java.io.Closeable, java.lang.AutoCloseable
        public final void close() throws IOException {
            this.remaining = 0;
        }
    }
}
