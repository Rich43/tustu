package sun.security.krb5.internal.crypto.dk;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.security.GeneralSecurityException;
import java.util.Arrays;
import javax.crypto.Cipher;
import jdk.internal.dynalink.CallSiteDescriptor;
import sun.misc.HexDumpEncoder;
import sun.security.krb5.Confounder;
import sun.security.krb5.KrbCryptoException;
import sun.security.krb5.internal.crypto.KeyUsage;

/* loaded from: rt.jar:sun/security/krb5/internal/crypto/dk/DkCrypto.class */
public abstract class DkCrypto {
    protected static final boolean debug = false;
    static final byte[] KERBEROS_CONSTANT = {107, 101, 114, 98, 101, 114, 111, 115};

    protected abstract int getKeySeedLength();

    protected abstract byte[] randomToKey(byte[] bArr);

    protected abstract Cipher getCipher(byte[] bArr, byte[] bArr2, int i2) throws GeneralSecurityException;

    public abstract int getChecksumLength();

    protected abstract byte[] getHmac(byte[] bArr, byte[] bArr2) throws GeneralSecurityException;

    public byte[] encrypt(byte[] bArr, int i2, byte[] bArr2, byte[] bArr3, byte[] bArr4, int i3, int i4) throws GeneralSecurityException, KrbCryptoException {
        if (!KeyUsage.isValid(i2)) {
            throw new GeneralSecurityException("Invalid key usage number: " + i2);
        }
        byte[] bArrDk = null;
        byte[] bArrDk2 = null;
        try {
            byte[] bArr5 = {(byte) ((i2 >> 24) & 255), (byte) ((i2 >> 16) & 255), (byte) ((i2 >> 8) & 255), (byte) (i2 & 255), -86};
            bArrDk = dk(bArr, bArr5);
            Cipher cipher = getCipher(bArrDk, bArr2, 1);
            int blockSize = cipher.getBlockSize();
            byte[] bArrBytes = Confounder.bytes(blockSize);
            int iRoundup = roundup(bArrBytes.length + i4, blockSize);
            byte[] bArr6 = new byte[iRoundup];
            System.arraycopy(bArrBytes, 0, bArr6, 0, bArrBytes.length);
            System.arraycopy(bArr4, i3, bArr6, bArrBytes.length, i4);
            Arrays.fill(bArr6, bArrBytes.length + i4, iRoundup, (byte) 0);
            int outputSize = cipher.getOutputSize(iRoundup);
            byte[] bArr7 = new byte[outputSize + getChecksumLength()];
            cipher.doFinal(bArr6, 0, iRoundup, bArr7, 0);
            if (bArr3 != null && bArr3.length == blockSize) {
                System.arraycopy(bArr7, outputSize - blockSize, bArr3, 0, blockSize);
            }
            bArr5[4] = 85;
            bArrDk2 = dk(bArr, bArr5);
            System.arraycopy(getHmac(bArrDk2, bArr6), 0, bArr7, outputSize, getChecksumLength());
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

    public byte[] encryptRaw(byte[] bArr, int i2, byte[] bArr2, byte[] bArr3, int i3, int i4) throws GeneralSecurityException, KrbCryptoException {
        Cipher cipher = getCipher(bArr, bArr2, 1);
        int blockSize = cipher.getBlockSize();
        if (i4 % blockSize != 0) {
            throw new GeneralSecurityException("length of data to be encrypted (" + i4 + ") is not a multiple of the blocksize (" + blockSize + ")");
        }
        byte[] bArr4 = new byte[cipher.getOutputSize(i4)];
        cipher.doFinal(bArr3, 0, i4, bArr4, 0);
        return bArr4;
    }

    public byte[] decryptRaw(byte[] bArr, int i2, byte[] bArr2, byte[] bArr3, int i3, int i4) throws GeneralSecurityException {
        Cipher cipher = getCipher(bArr, bArr2, 2);
        int blockSize = cipher.getBlockSize();
        if (i4 % blockSize != 0) {
            throw new GeneralSecurityException("length of data to be decrypted (" + i4 + ") is not a multiple of the blocksize (" + blockSize + ")");
        }
        return cipher.doFinal(bArr3, i3, i4);
    }

    public byte[] decrypt(byte[] bArr, int i2, byte[] bArr2, byte[] bArr3, int i3, int i4) throws GeneralSecurityException {
        if (!KeyUsage.isValid(i2)) {
            throw new GeneralSecurityException("Invalid key usage number: " + i2);
        }
        Object[] objArr = null;
        Object[] objArr2 = null;
        try {
            byte[] bArr4 = {(byte) ((i2 >> 24) & 255), (byte) ((i2 >> 16) & 255), (byte) ((i2 >> 8) & 255), (byte) (i2 & 255), -86};
            byte[] bArrDk = dk(bArr, bArr4);
            Cipher cipher = getCipher(bArrDk, bArr2, 2);
            int blockSize = cipher.getBlockSize();
            int checksumLength = getChecksumLength();
            int i5 = i4 - checksumLength;
            byte[] bArrDoFinal = cipher.doFinal(bArr3, i3, i5);
            bArr4[4] = 85;
            byte[] bArrDk2 = dk(bArr, bArr4);
            byte[] hmac = getHmac(bArrDk2, bArrDoFinal);
            boolean z2 = false;
            if (hmac.length >= checksumLength) {
                int i6 = 0;
                while (true) {
                    if (i6 >= checksumLength) {
                        break;
                    }
                    if (hmac[i6] == bArr3[i5 + i6]) {
                        i6++;
                    } else {
                        z2 = true;
                        break;
                    }
                }
            }
            if (z2) {
                throw new GeneralSecurityException("Checksum failed");
            }
            if (bArr2 != null && bArr2.length == blockSize) {
                System.arraycopy(bArr3, (i3 + i5) - blockSize, bArr2, 0, blockSize);
            }
            byte[] bArr5 = new byte[bArrDoFinal.length - blockSize];
            System.arraycopy(bArrDoFinal, blockSize, bArr5, 0, bArr5.length);
            if (bArrDk != null) {
                Arrays.fill(bArrDk, 0, bArrDk.length, (byte) 0);
            }
            if (bArrDk2 != null) {
                Arrays.fill(bArrDk2, 0, bArrDk2.length, (byte) 0);
            }
            return bArr5;
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

    int roundup(int i2, int i3) {
        return (((i2 + i3) - 1) / i3) * i3;
    }

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

    byte[] dk(byte[] bArr, byte[] bArr2) throws GeneralSecurityException {
        return randomToKey(dr(bArr, bArr2));
    }

    private byte[] dr(byte[] bArr, byte[] bArr2) throws GeneralSecurityException {
        Cipher cipher = getCipher(bArr, null, 1);
        int blockSize = cipher.getBlockSize();
        if (bArr2.length != blockSize) {
            bArr2 = nfold(bArr2, blockSize * 8);
        }
        byte[] bArr3 = bArr2;
        int keySeedLength = getKeySeedLength() >> 3;
        byte[] bArr4 = new byte[keySeedLength];
        int i2 = 0;
        while (i2 < keySeedLength) {
            byte[] bArrDoFinal = cipher.doFinal(bArr3);
            int length = keySeedLength - i2 <= bArrDoFinal.length ? keySeedLength - i2 : bArrDoFinal.length;
            System.arraycopy(bArrDoFinal, 0, bArr4, i2, length);
            i2 += length;
            bArr3 = bArrDoFinal;
        }
        return bArr4;
    }

    static byte[] nfold(byte[] bArr, int i2) {
        int length = bArr.length;
        int i3 = i2 >> 3;
        int i4 = i3;
        int i5 = length;
        while (i5 != 0) {
            int i6 = i5;
            i5 = i4 % i5;
            i4 = i6;
        }
        int i7 = (i3 * length) / i4;
        byte[] bArr2 = new byte[i3];
        Arrays.fill(bArr2, (byte) 0);
        int i8 = 0;
        for (int i9 = i7 - 1; i9 >= 0; i9--) {
            int i10 = ((((length << 3) - 1) + (((length << 3) + 13) * (i9 / length))) + ((length - (i9 % length)) << 3)) % (length << 3);
            int i11 = i8 + (((((bArr[((length - 1) - (i10 >>> 3)) % length] & 255) << 8) | (bArr[(length - (i10 >>> 3)) % length] & 255)) >>> ((i10 & 7) + 1)) & 255) + (bArr2[i9 % i3] & 255);
            bArr2[i9 % i3] = (byte) (i11 & 255);
            i8 = i11 >>> 8;
        }
        if (i8 != 0) {
            for (int i12 = i3 - 1; i12 >= 0; i12--) {
                int i13 = i8 + (bArr2[i12] & 255);
                bArr2[i12] = (byte) (i13 & 255);
                i8 = i13 >>> 8;
            }
        }
        return bArr2;
    }

    static String bytesToString(byte[] bArr) {
        StringBuffer stringBuffer = new StringBuffer();
        for (int i2 = 0; i2 < bArr.length; i2++) {
            if ((bArr[i2] & 255) < 16) {
                stringBuffer.append("0" + Integer.toHexString(bArr[i2] & 255));
            } else {
                stringBuffer.append(Integer.toHexString(bArr[i2] & 255));
            }
        }
        return stringBuffer.toString();
    }

    private static byte[] binaryStringToBytes(String str) throws NumberFormatException {
        char[] charArray = str.toCharArray();
        byte[] bArr = new byte[charArray.length / 2];
        for (int i2 = 0; i2 < bArr.length; i2++) {
            bArr[i2] = (byte) ((Byte.parseByte(new String(charArray, i2 * 2, 1), 16) << 4) | Byte.parseByte(new String(charArray, (i2 * 2) + 1, 1), 16));
        }
        return bArr;
    }

    static void traceOutput(String str, byte[] bArr, int i2, int i3) {
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(i3);
            new HexDumpEncoder().encodeBuffer(new ByteArrayInputStream(bArr, i2, i3), byteArrayOutputStream);
            System.err.println(str + CallSiteDescriptor.TOKEN_DELIMITER + byteArrayOutputStream.toString());
        } catch (Exception e2) {
        }
    }

    static byte[] charToUtf8(char[] cArr) {
        ByteBuffer byteBufferEncode = Charset.forName("UTF-8").encode(CharBuffer.wrap(cArr));
        int iLimit = byteBufferEncode.limit();
        byte[] bArr = new byte[iLimit];
        byteBufferEncode.get(bArr, 0, iLimit);
        return bArr;
    }

    static byte[] charToUtf16(char[] cArr) {
        ByteBuffer byteBufferEncode = Charset.forName("UTF-16LE").encode(CharBuffer.wrap(cArr));
        int iLimit = byteBufferEncode.limit();
        byte[] bArr = new byte[iLimit];
        byteBufferEncode.get(bArr, 0, iLimit);
        return bArr;
    }
}
