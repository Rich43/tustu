package sun.security.rsa;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.MGF1ParameterSpec;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import javax.crypto.BadPaddingException;
import javax.crypto.spec.OAEPParameterSpec;
import javax.crypto.spec.PSource;
import sun.security.jca.JCAUtil;

/* loaded from: rt.jar:sun/security/rsa/RSAPadding.class */
public final class RSAPadding {
    public static final int PAD_BLOCKTYPE_1 = 1;
    public static final int PAD_BLOCKTYPE_2 = 2;
    public static final int PAD_NONE = 3;
    public static final int PAD_OAEP_MGF1 = 4;
    private final int type;
    private final int paddedSize;
    private SecureRandom random;
    private final int maxDataSize;
    private MessageDigest md;
    private MGF1 mgf;
    private byte[] lHash;
    private static final Map<String, byte[]> emptyHashes = Collections.synchronizedMap(new HashMap());

    public static RSAPadding getInstance(int i2, int i3) throws InvalidKeyException, InvalidAlgorithmParameterException {
        return new RSAPadding(i2, i3, null, null);
    }

    public static RSAPadding getInstance(int i2, int i3, SecureRandom secureRandom) throws InvalidKeyException, InvalidAlgorithmParameterException {
        return new RSAPadding(i2, i3, secureRandom, null);
    }

    public static RSAPadding getInstance(int i2, int i3, SecureRandom secureRandom, OAEPParameterSpec oAEPParameterSpec) throws InvalidKeyException, InvalidAlgorithmParameterException {
        return new RSAPadding(i2, i3, secureRandom, oAEPParameterSpec);
    }

    private RSAPadding(int i2, int i3, SecureRandom secureRandom, OAEPParameterSpec oAEPParameterSpec) throws InvalidKeyException, InvalidAlgorithmParameterException {
        this.type = i2;
        this.paddedSize = i3;
        this.random = secureRandom;
        if (i3 < 64) {
            throw new InvalidKeyException("Padded size must be at least 64");
        }
        switch (i2) {
            case 1:
            case 2:
                this.maxDataSize = i3 - 11;
                return;
            case 3:
                this.maxDataSize = i3;
                return;
            case 4:
                String digestAlgorithm = "SHA-1";
                String digestAlgorithm2 = digestAlgorithm;
                byte[] value = null;
                if (oAEPParameterSpec != null) {
                    try {
                        digestAlgorithm = oAEPParameterSpec.getDigestAlgorithm();
                        String mGFAlgorithm = oAEPParameterSpec.getMGFAlgorithm();
                        if (!mGFAlgorithm.equalsIgnoreCase("MGF1")) {
                            throw new InvalidAlgorithmParameterException("Unsupported MGF algo: " + mGFAlgorithm);
                        }
                        digestAlgorithm2 = ((MGF1ParameterSpec) oAEPParameterSpec.getMGFParameters()).getDigestAlgorithm();
                        PSource pSource = oAEPParameterSpec.getPSource();
                        String algorithm = pSource.getAlgorithm();
                        if (!algorithm.equalsIgnoreCase("PSpecified")) {
                            throw new InvalidAlgorithmParameterException("Unsupported pSource algo: " + algorithm);
                        }
                        value = ((PSource.PSpecified) pSource).getValue();
                    } catch (NoSuchAlgorithmException e2) {
                        throw new InvalidKeyException("Digest not available", e2);
                    }
                }
                this.md = MessageDigest.getInstance(digestAlgorithm);
                this.mgf = new MGF1(digestAlgorithm2);
                this.lHash = getInitialHash(this.md, value);
                this.maxDataSize = (i3 - 2) - (2 * this.lHash.length);
                if (this.maxDataSize <= 0) {
                    throw new InvalidKeyException("Key is too short for encryption using OAEPPadding with " + digestAlgorithm + " and " + this.mgf.getName());
                }
                return;
            default:
                throw new InvalidKeyException("Invalid padding: " + i2);
        }
    }

    private static byte[] getInitialHash(MessageDigest messageDigest, byte[] bArr) {
        byte[] bArrDigest;
        if (bArr == null || bArr.length == 0) {
            String algorithm = messageDigest.getAlgorithm();
            bArrDigest = emptyHashes.get(algorithm);
            if (bArrDigest == null) {
                bArrDigest = messageDigest.digest();
                emptyHashes.put(algorithm, bArrDigest);
            }
        } else {
            bArrDigest = messageDigest.digest(bArr);
        }
        return bArrDigest;
    }

    public int getMaxDataSize() {
        return this.maxDataSize;
    }

    public byte[] pad(byte[] bArr) throws BadPaddingException {
        return pad(bArr, 0, bArr.length);
    }

    public byte[] pad(byte[] bArr, int i2, int i3) throws BadPaddingException {
        if (i3 > this.maxDataSize) {
            throw new BadPaddingException("Data must be shorter than " + (this.maxDataSize + 1) + " bytes but received " + i3 + " bytes.");
        }
        switch (this.type) {
            case 1:
            case 2:
                return padV15(bArr, i2, i3);
            case 3:
                return RSACore.convert(bArr, i2, i3);
            case 4:
                return padOAEP(bArr, i2, i3);
            default:
                throw new AssertionError();
        }
    }

    public byte[] unpad(byte[] bArr) throws BadPaddingException {
        if (bArr.length != this.paddedSize) {
            throw new BadPaddingException("Decryption error.The padded array length (" + bArr.length + ") is not the specified padded size (" + this.paddedSize + ")");
        }
        switch (this.type) {
            case 1:
            case 2:
                return unpadV15(bArr);
            case 3:
                return bArr;
            case 4:
                return unpadOAEP(bArr);
            default:
                throw new AssertionError();
        }
    }

    private byte[] padV15(byte[] bArr, int i2, int i3) throws BadPaddingException {
        int i4;
        byte[] bArr2 = new byte[this.paddedSize];
        System.arraycopy(bArr, i2, bArr2, this.paddedSize - i3, i3);
        int i5 = (this.paddedSize - 3) - i3;
        int i6 = 0 + 1;
        bArr2[0] = 0;
        int i7 = i6 + 1;
        bArr2[i6] = (byte) this.type;
        if (this.type != 1) {
            if (this.random == null) {
                this.random = JCAUtil.getSecureRandom();
            }
            byte[] bArr3 = new byte[64];
            int length = -1;
            while (true) {
                int i8 = i5;
                i5--;
                if (i8 <= 0) {
                    break;
                }
                do {
                    if (length < 0) {
                        this.random.nextBytes(bArr3);
                        length = bArr3.length - 1;
                    }
                    int i9 = length;
                    length--;
                    i4 = bArr3[i9] & 255;
                } while (i4 == 0);
                int i10 = i7;
                i7++;
                bArr2[i10] = (byte) i4;
            }
        } else {
            while (true) {
                int i11 = i5;
                i5--;
                if (i11 <= 0) {
                    break;
                }
                int i12 = i7;
                i7++;
                bArr2[i12] = -1;
            }
        }
        return bArr2;
    }

    private byte[] unpadV15(byte[] bArr) throws BadPaddingException {
        boolean z2 = false;
        int i2 = 0 + 1;
        if (bArr[0] != 0) {
            z2 = true;
        }
        int i3 = i2 + 1;
        if (bArr[i2] != this.type) {
            z2 = true;
        }
        int i4 = 0;
        while (i3 < bArr.length) {
            int i5 = i3;
            i3++;
            int i6 = bArr[i5] & 255;
            if (i6 == 0 && i4 == 0) {
                i4 = i3;
            }
            if (i3 == bArr.length && i4 == 0) {
                z2 = true;
            }
            if (this.type == 1 && i6 != 255 && i4 == 0) {
                z2 = true;
            }
        }
        int length = bArr.length - i4;
        if (length > this.maxDataSize) {
            z2 = true;
        }
        System.arraycopy(bArr, 0, new byte[i4], 0, i4);
        byte[] bArr2 = new byte[length];
        System.arraycopy(bArr, i4, bArr2, 0, length);
        BadPaddingException badPaddingException = new BadPaddingException("Decryption error");
        if (z2) {
            throw badPaddingException;
        }
        return bArr2;
    }

    private byte[] padOAEP(byte[] bArr, int i2, int i3) throws BadPaddingException, RuntimeException {
        if (this.random == null) {
            this.random = JCAUtil.getSecureRandom();
        }
        int length = this.lHash.length;
        byte[] bArr2 = new byte[length];
        this.random.nextBytes(bArr2);
        byte[] bArr3 = new byte[this.paddedSize];
        System.arraycopy(bArr2, 0, bArr3, 1, length);
        int i4 = length + 1;
        int length2 = bArr3.length - i4;
        int i5 = this.paddedSize - i3;
        System.arraycopy(this.lHash, 0, bArr3, i4, length);
        bArr3[i5 - 1] = 1;
        System.arraycopy(bArr, i2, bArr3, i5, i3);
        this.mgf.generateAndXor(bArr3, 1, length, length2, bArr3, i4);
        this.mgf.generateAndXor(bArr3, i4, length2, length, bArr3, 1);
        return bArr3;
    }

    private byte[] unpadOAEP(byte[] bArr) throws BadPaddingException, RuntimeException {
        boolean z2 = false;
        int length = this.lHash.length;
        if (bArr[0] != 0) {
            z2 = true;
        }
        int i2 = length + 1;
        int length2 = bArr.length - i2;
        this.mgf.generateAndXor(bArr, i2, length2, length, bArr, 1);
        this.mgf.generateAndXor(bArr, 1, length, length2, bArr, i2);
        for (int i3 = 0; i3 < length; i3++) {
            if (this.lHash[i3] != bArr[i2 + i3]) {
                z2 = true;
            }
        }
        int i4 = i2 + length;
        int length3 = -1;
        for (int i5 = i4; i5 < bArr.length; i5++) {
            byte b2 = bArr[i5];
            if (length3 == -1 && b2 != 0) {
                if (b2 == 1) {
                    length3 = i5;
                } else {
                    z2 = true;
                }
            }
        }
        if (length3 == -1) {
            z2 = true;
            length3 = bArr.length - 1;
        }
        int i6 = length3 + 1;
        byte[] bArr2 = new byte[i6 - i4];
        System.arraycopy(bArr, i4, bArr2, 0, bArr2.length);
        byte[] bArr3 = new byte[bArr.length - i6];
        System.arraycopy(bArr, i6, bArr3, 0, bArr3.length);
        BadPaddingException badPaddingException = new BadPaddingException("Decryption error");
        if (z2) {
            throw badPaddingException;
        }
        return bArr3;
    }
}
