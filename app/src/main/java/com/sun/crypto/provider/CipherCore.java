package com.sun.crypto.provider;

import java.security.AlgorithmParameters;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.ProviderException;
import java.security.SecureRandom;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.InvalidParameterSpecException;
import java.util.Arrays;
import java.util.Locale;
import javax.crypto.AEADBadTagException;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.ShortBufferException;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.RC2ParameterSpec;

/* loaded from: sunjce_provider.jar:com/sun/crypto/provider/CipherCore.class */
final class CipherCore {
    private byte[] buffer;
    private int blockSize;
    private int unitBytes;
    private int diffBlocksize;
    private Padding padding;
    private FeedbackCipher cipher;
    private static final int ECB_MODE = 0;
    private static final int CBC_MODE = 1;
    private static final int CFB_MODE = 2;
    private static final int OFB_MODE = 3;
    private static final int PCBC_MODE = 4;
    private static final int CTR_MODE = 5;
    private static final int CTS_MODE = 6;
    static final int GCM_MODE = 7;
    private int buffered = 0;
    private int minBytes = 0;
    private int cipherMode = 0;
    private boolean decrypting = false;
    private boolean requireReinit = false;
    private byte[] lastEncKey = null;
    private byte[] lastEncIv = null;

    CipherCore(SymmetricCipher symmetricCipher, int i2) {
        this.buffer = null;
        this.blockSize = 0;
        this.unitBytes = 0;
        this.diffBlocksize = 0;
        this.padding = null;
        this.cipher = null;
        this.blockSize = i2;
        this.unitBytes = i2;
        this.diffBlocksize = i2;
        this.buffer = new byte[this.blockSize * 2];
        this.cipher = new ElectronicCodeBook(symmetricCipher);
        this.padding = new PKCS5Padding(this.blockSize);
    }

    void setMode(String str) throws NoSuchAlgorithmException {
        if (str == null) {
            throw new NoSuchAlgorithmException("null mode");
        }
        String upperCase = str.toUpperCase(Locale.ENGLISH);
        if (upperCase.equals("ECB")) {
            return;
        }
        SymmetricCipher embeddedCipher = this.cipher.getEmbeddedCipher();
        if (upperCase.equals("CBC")) {
            this.cipherMode = 1;
            this.cipher = new CipherBlockChaining(embeddedCipher);
            return;
        }
        if (upperCase.equals("CTS")) {
            this.cipherMode = 6;
            this.cipher = new CipherTextStealing(embeddedCipher);
            this.minBytes = this.blockSize + 1;
            this.padding = null;
            return;
        }
        if (upperCase.equals("CTR")) {
            this.cipherMode = 5;
            this.cipher = new CounterMode(embeddedCipher);
            this.unitBytes = 1;
            this.padding = null;
            return;
        }
        if (upperCase.equals("GCM")) {
            if (this.blockSize != 16) {
                throw new NoSuchAlgorithmException("GCM mode can only be used for AES cipher");
            }
            this.cipherMode = 7;
            this.cipher = new GaloisCounterMode(embeddedCipher);
            this.padding = null;
            return;
        }
        if (upperCase.startsWith("CFB")) {
            this.cipherMode = 2;
            this.unitBytes = getNumOfUnit(str, "CFB".length(), this.blockSize);
            this.cipher = new CipherFeedback(embeddedCipher, this.unitBytes);
        } else if (upperCase.startsWith("OFB")) {
            this.cipherMode = 3;
            this.unitBytes = getNumOfUnit(str, "OFB".length(), this.blockSize);
            this.cipher = new OutputFeedback(embeddedCipher, this.unitBytes);
        } else {
            if (upperCase.equals("PCBC")) {
                this.cipherMode = 4;
                this.cipher = new PCBC(embeddedCipher);
                return;
            }
            throw new NoSuchAlgorithmException("Cipher mode: " + str + " not found");
        }
    }

    int getMode() {
        return this.cipherMode;
    }

    private static int getNumOfUnit(String str, int i2, int i3) throws NoSuchAlgorithmException {
        int i4 = i3;
        if (str.length() > i2) {
            try {
                int iIntValue = Integer.valueOf(str.substring(i2)).intValue();
                i4 = iIntValue >> 3;
                if (iIntValue % 8 != 0 || i4 > i3) {
                    throw new NoSuchAlgorithmException("Invalid algorithm mode: " + str);
                }
            } catch (NumberFormatException e2) {
                throw new NoSuchAlgorithmException("Algorithm mode: " + str + " not implemented");
            }
        }
        return i4;
    }

    void setPadding(String str) throws NoSuchPaddingException {
        if (str == null) {
            throw new NoSuchPaddingException("null padding");
        }
        if (str.equalsIgnoreCase("NoPadding")) {
            this.padding = null;
        } else if (str.equalsIgnoreCase("ISO10126Padding")) {
            this.padding = new ISO10126Padding(this.blockSize);
        } else if (!str.equalsIgnoreCase("PKCS5Padding")) {
            throw new NoSuchPaddingException("Padding: " + str + " not implemented");
        }
        if (this.padding != null) {
            if (this.cipherMode == 5 || this.cipherMode == 6 || this.cipherMode == 7) {
                this.padding = null;
                String str2 = null;
                switch (this.cipherMode) {
                    case 5:
                        str2 = "CTR";
                        break;
                    case 6:
                        str2 = "CTS";
                        break;
                    case 7:
                        str2 = "GCM";
                        break;
                }
                if (str2 != null) {
                    throw new NoSuchPaddingException(str2 + " mode must be used with NoPadding");
                }
            }
        }
    }

    int getOutputSize(int i2) {
        return getOutputSizeByOperation(i2, true);
    }

    private int getOutputSizeByOperation(int i2, boolean z2) {
        int iAddExact = Math.addExact(Math.addExact(this.buffered, this.cipher.getBufferedLength()), i2);
        switch (this.cipherMode) {
            case 7:
                if (z2) {
                    int tagLen = ((GaloisCounterMode) this.cipher).getTagLen();
                    if (!this.decrypting) {
                        iAddExact = Math.addExact(iAddExact, tagLen);
                    } else {
                        iAddExact -= tagLen;
                    }
                }
                if (iAddExact < 0) {
                    iAddExact = 0;
                    break;
                }
                break;
            default:
                if (this.padding != null && !this.decrypting) {
                    if (this.unitBytes != this.blockSize) {
                        if (iAddExact < this.diffBlocksize) {
                            iAddExact = this.diffBlocksize;
                            break;
                        } else {
                            iAddExact = Math.addExact(iAddExact, this.blockSize - ((iAddExact - this.diffBlocksize) % this.blockSize));
                            break;
                        }
                    } else {
                        iAddExact = Math.addExact(iAddExact, this.padding.padLength(iAddExact));
                        break;
                    }
                }
                break;
        }
        return iAddExact;
    }

    byte[] getIV() {
        byte[] iv = this.cipher.getIV();
        if (iv == null) {
            return null;
        }
        return (byte[]) iv.clone();
    }

    AlgorithmParameters getParameters(String str) {
        AlgorithmParameterSpec ivParameterSpec;
        if (this.cipherMode == 0) {
            return null;
        }
        byte[] iv = getIV();
        if (iv == null) {
            if (this.cipherMode == 7) {
                iv = new byte[GaloisCounterMode.DEFAULT_IV_LEN];
            } else {
                iv = new byte[this.blockSize];
            }
            SunJCE.getRandom().nextBytes(iv);
        }
        if (this.cipherMode == 7) {
            str = "GCM";
            ivParameterSpec = new GCMParameterSpec(((GaloisCounterMode) this.cipher).getTagLen() * 8, iv);
        } else if (str.equals("RC2")) {
            ivParameterSpec = new RC2ParameterSpec(((RC2Crypt) this.cipher.getEmbeddedCipher()).getEffectiveKeyBits(), iv);
        } else {
            ivParameterSpec = new IvParameterSpec(iv);
        }
        try {
            AlgorithmParameters algorithmParameters = AlgorithmParameters.getInstance(str, SunJCE.getInstance());
            algorithmParameters.init(ivParameterSpec);
            return algorithmParameters;
        } catch (NoSuchAlgorithmException e2) {
            throw new RuntimeException("Cannot find " + str + " AlgorithmParameters implementation in SunJCE provider");
        } catch (InvalidParameterSpecException e3) {
            throw new RuntimeException(((Object) ivParameterSpec.getClass()) + " not supported");
        }
    }

    void init(int i2, Key key, SecureRandom secureRandom) throws InvalidKeyException {
        try {
            init(i2, key, (AlgorithmParameterSpec) null, secureRandom);
        } catch (InvalidAlgorithmParameterException e2) {
            throw new InvalidKeyException(e2.getMessage());
        }
    }

    void init(int i2, Key key, AlgorithmParameterSpec algorithmParameterSpec, SecureRandom secureRandom) throws InvalidKeyException, InvalidAlgorithmParameterException {
        this.decrypting = i2 == 2 || i2 == 4;
        byte[] keyBytes = getKeyBytes(key);
        int i3 = -1;
        byte[] iv = null;
        if (algorithmParameterSpec != null) {
            if (this.cipherMode == 7) {
                if (algorithmParameterSpec instanceof GCMParameterSpec) {
                    int tLen = ((GCMParameterSpec) algorithmParameterSpec).getTLen();
                    if (tLen < 96 || tLen > 128 || (tLen & 7) != 0) {
                        throw new InvalidAlgorithmParameterException("Unsupported TLen value; must be one of {128, 120, 112, 104, 96}");
                    }
                    i3 = tLen >> 3;
                    iv = ((GCMParameterSpec) algorithmParameterSpec).getIV();
                } else {
                    throw new InvalidAlgorithmParameterException("Unsupported parameter: " + ((Object) algorithmParameterSpec));
                }
            } else if (algorithmParameterSpec instanceof IvParameterSpec) {
                iv = ((IvParameterSpec) algorithmParameterSpec).getIV();
                if (iv == null || iv.length != this.blockSize) {
                    throw new InvalidAlgorithmParameterException("Wrong IV length: must be " + this.blockSize + " bytes long");
                }
            } else if (algorithmParameterSpec instanceof RC2ParameterSpec) {
                iv = ((RC2ParameterSpec) algorithmParameterSpec).getIV();
                if (iv != null && iv.length != this.blockSize) {
                    throw new InvalidAlgorithmParameterException("Wrong IV length: must be " + this.blockSize + " bytes long");
                }
            } else {
                throw new InvalidAlgorithmParameterException("Unsupported parameter: " + ((Object) algorithmParameterSpec));
            }
        }
        if (this.cipherMode == 0) {
            if (iv != null) {
                throw new InvalidAlgorithmParameterException("ECB mode cannot use IV");
            }
        } else if (iv == null) {
            if (this.decrypting) {
                throw new InvalidAlgorithmParameterException("Parameters missing");
            }
            if (secureRandom == null) {
                secureRandom = SunJCE.getRandom();
            }
            if (this.cipherMode == 7) {
                iv = new byte[GaloisCounterMode.DEFAULT_IV_LEN];
            } else {
                iv = new byte[this.blockSize];
            }
            secureRandom.nextBytes(iv);
        }
        this.buffered = 0;
        this.diffBlocksize = this.blockSize;
        String algorithm = key.getAlgorithm();
        if (this.cipherMode == 7) {
            if (i3 == -1) {
                i3 = GaloisCounterMode.DEFAULT_TAG_LEN;
            }
            if (this.decrypting) {
                this.minBytes = i3;
            } else {
                this.requireReinit = Arrays.equals(iv, this.lastEncIv) && MessageDigest.isEqual(keyBytes, this.lastEncKey);
                if (this.requireReinit) {
                    throw new InvalidAlgorithmParameterException("Cannot reuse iv for GCM encryption");
                }
                this.lastEncIv = iv;
                this.lastEncKey = keyBytes;
            }
            ((GaloisCounterMode) this.cipher).init(this.decrypting, algorithm, keyBytes, iv, i3);
        } else {
            this.cipher.init(this.decrypting, algorithm, keyBytes, iv);
        }
        this.requireReinit = false;
    }

    void init(int i2, Key key, AlgorithmParameters algorithmParameters, SecureRandom secureRandom) throws InvalidKeyException, InvalidAlgorithmParameterException {
        AlgorithmParameterSpec parameterSpec = null;
        if (algorithmParameters != null) {
            try {
                if (this.cipherMode == 7) {
                    parameterSpec = algorithmParameters.getParameterSpec(GCMParameterSpec.class);
                } else {
                    parameterSpec = algorithmParameters.getParameterSpec(IvParameterSpec.class);
                }
            } catch (InvalidParameterSpecException e2) {
                throw new InvalidAlgorithmParameterException("Wrong parameter type: " + ((String) null) + " expected");
            }
        }
        init(i2, key, parameterSpec, secureRandom);
    }

    static byte[] getKeyBytes(Key key) throws InvalidKeyException {
        if (key == null) {
            throw new InvalidKeyException("No key given");
        }
        if (!"RAW".equalsIgnoreCase(key.getFormat())) {
            throw new InvalidKeyException("Wrong format: RAW bytes needed");
        }
        byte[] encoded = key.getEncoded();
        if (encoded == null) {
            throw new InvalidKeyException("RAW key bytes missing");
        }
        return encoded;
    }

    byte[] update(byte[] bArr, int i2, int i3) {
        checkReinit();
        try {
            byte[] bArr2 = new byte[getOutputSizeByOperation(i3, false)];
            int iUpdate = update(bArr, i2, i3, bArr2, 0);
            if (iUpdate == bArr2.length) {
                return bArr2;
            }
            byte[] bArrCopyOf = Arrays.copyOf(bArr2, iUpdate);
            if (this.decrypting) {
                Arrays.fill(bArr2, (byte) 0);
            }
            return bArrCopyOf;
        } catch (ShortBufferException e2) {
            throw new ProviderException("Unexpected exception", e2);
        }
    }

    int update(byte[] bArr, int i2, int i3, byte[] bArr2, int i4) throws ShortBufferException {
        checkReinit();
        int iAddExact = Math.addExact(this.buffered, i3) - this.minBytes;
        if (this.padding != null && this.decrypting) {
            iAddExact -= this.blockSize;
        }
        int i5 = iAddExact > 0 ? iAddExact - (iAddExact % this.unitBytes) : 0;
        if (bArr2 == null || bArr2.length - i4 < i5) {
            throw new ShortBufferException("Output buffer must be (at least) " + i5 + " bytes long");
        }
        int iEncrypt = 0;
        if (i5 != 0) {
            if (bArr == bArr2 && i4 - i2 < i3 && i2 - i4 < this.buffer.length) {
                bArr = Arrays.copyOfRange(bArr, i2, Math.addExact(i2, i3));
                i2 = 0;
            }
            if (i5 <= this.buffered) {
                if (this.decrypting) {
                    iEncrypt = this.cipher.decrypt(this.buffer, 0, i5, bArr2, i4);
                } else {
                    iEncrypt = this.cipher.encrypt(this.buffer, 0, i5, bArr2, i4);
                }
                this.buffered -= i5;
                if (this.buffered != 0) {
                    System.arraycopy(this.buffer, i5, this.buffer, 0, this.buffered);
                }
            } else {
                int i6 = i5 - this.buffered;
                if (this.buffered > 0) {
                    int length = this.buffer.length - this.buffered;
                    if (length != 0) {
                        int iMin = Math.min(length, i6);
                        if (this.unitBytes != this.blockSize) {
                            iMin -= Math.addExact(this.buffered, iMin) % this.unitBytes;
                        }
                        System.arraycopy(bArr, i2, this.buffer, this.buffered, iMin);
                        i2 = Math.addExact(i2, iMin);
                        i6 -= iMin;
                        i3 -= iMin;
                        this.buffered = Math.addExact(this.buffered, iMin);
                    }
                    if (this.decrypting) {
                        iEncrypt = this.cipher.decrypt(this.buffer, 0, this.buffered, bArr2, i4);
                    } else {
                        iEncrypt = this.cipher.encrypt(this.buffer, 0, this.buffered, bArr2, i4);
                        Arrays.fill(this.buffer, (byte) 0);
                    }
                    i4 = Math.addExact(i4, iEncrypt);
                    this.buffered = 0;
                }
                if (i6 > 0) {
                    if (this.decrypting) {
                        iEncrypt += this.cipher.decrypt(bArr, i2, i6, bArr2, i4);
                    } else {
                        iEncrypt += this.cipher.encrypt(bArr, i2, i6, bArr2, i4);
                    }
                    i2 += i6;
                    i3 -= i6;
                }
            }
            if (this.unitBytes != this.blockSize) {
                if (i5 < this.diffBlocksize) {
                    this.diffBlocksize -= i5;
                } else {
                    this.diffBlocksize = this.blockSize - ((i5 - this.diffBlocksize) % this.blockSize);
                }
            }
        }
        if (i3 > 0) {
            System.arraycopy(bArr, i2, this.buffer, this.buffered, i3);
            this.buffered = Math.addExact(this.buffered, i3);
        }
        return iEncrypt;
    }

    byte[] doFinal(byte[] bArr, int i2, int i3) throws BadPaddingException, IllegalBlockSizeException {
        try {
            checkReinit();
            byte[] bArr2 = new byte[getOutputSizeByOperation(i3, true)];
            byte[] bArrPrepareInputBuffer = prepareInputBuffer(bArr, i2, i3, bArr2, 0);
            int iFillOutputBuffer = fillOutputBuffer(bArrPrepareInputBuffer, bArrPrepareInputBuffer == bArr ? i2 : 0, bArr2, 0, bArrPrepareInputBuffer == bArr ? i3 : bArrPrepareInputBuffer.length, bArr);
            endDoFinal();
            if (iFillOutputBuffer < bArr2.length) {
                byte[] bArrCopyOf = Arrays.copyOf(bArr2, iFillOutputBuffer);
                if (this.decrypting) {
                    Arrays.fill(bArr2, (byte) 0);
                }
                return bArrCopyOf;
            }
            return bArr2;
        } catch (ShortBufferException e2) {
            throw new ProviderException("Unexpected exception", e2);
        }
    }

    int doFinal(byte[] bArr, int i2, int i3, byte[] bArr2, int i4) throws BadPaddingException, IllegalBlockSizeException, ShortBufferException {
        checkReinit();
        int outputSizeByOperation = getOutputSizeByOperation(i3, true);
        int iCheckOutputCapacity = checkOutputCapacity(bArr2, i4, outputSizeByOperation);
        int i5 = this.decrypting ? 0 : i4;
        byte[] bArrPrepareInputBuffer = prepareInputBuffer(bArr, i2, i3, bArr2, i4);
        byte[] bArr3 = null;
        int i6 = bArrPrepareInputBuffer == bArr ? i2 : 0;
        int length = bArrPrepareInputBuffer == bArr ? i3 : bArrPrepareInputBuffer.length;
        if (this.decrypting) {
            if (iCheckOutputCapacity < outputSizeByOperation) {
                this.cipher.save();
            }
            bArr3 = new byte[outputSizeByOperation];
        }
        int iFillOutputBuffer = fillOutputBuffer(bArrPrepareInputBuffer, i6, this.decrypting ? bArr3 : bArr2, i5, length, bArr);
        if (this.decrypting) {
            if (iCheckOutputCapacity < iFillOutputBuffer) {
                this.cipher.restore();
                throw new ShortBufferException("Output buffer too short: " + iCheckOutputCapacity + " bytes given, " + iFillOutputBuffer + " bytes needed");
            }
            System.arraycopy(bArr3, 0, bArr2, i4, iFillOutputBuffer);
            Arrays.fill(bArr3, (byte) 0);
        }
        endDoFinal();
        return iFillOutputBuffer;
    }

    private void endDoFinal() {
        this.buffered = 0;
        this.diffBlocksize = this.blockSize;
        if (this.cipherMode != 0) {
            this.cipher.reset();
        }
    }

    private int unpad(int i2, byte[] bArr) throws BadPaddingException {
        int iUnpad = this.padding.unpad(bArr, 0, i2);
        if (iUnpad < 0) {
            throw new BadPaddingException("Given final block not properly padded. Such issues can arise if a bad key is used during decryption.");
        }
        return iUnpad;
    }

    private byte[] prepareInputBuffer(byte[] bArr, int i2, int i3, byte[] bArr2, int i4) throws IllegalBlockSizeException, ShortBufferException {
        int iAddExact = Math.addExact(this.buffered, i3);
        int iAddExact2 = Math.addExact(iAddExact, this.cipher.getBufferedLength());
        int iPadLength = 0;
        if (this.unitBytes != this.blockSize) {
            if (iAddExact2 < this.diffBlocksize) {
                iPadLength = this.diffBlocksize - iAddExact2;
            } else {
                iPadLength = this.blockSize - ((iAddExact2 - this.diffBlocksize) % this.blockSize);
            }
        } else if (this.padding != null) {
            iPadLength = this.padding.padLength(iAddExact2);
        }
        if (this.decrypting && this.padding != null && iPadLength > 0 && iPadLength != this.blockSize) {
            throw new IllegalBlockSizeException("Input length must be multiple of " + this.blockSize + " when decrypting with padded cipher");
        }
        if (this.buffered != 0 || ((!this.decrypting && this.padding != null) || (bArr == bArr2 && i4 - i2 < i3 && i2 - i4 < this.buffer.length))) {
            if (this.decrypting || this.padding == null) {
                iPadLength = 0;
            }
            byte[] bArr3 = new byte[Math.addExact(iAddExact, iPadLength)];
            if (this.buffered != 0) {
                System.arraycopy(this.buffer, 0, bArr3, 0, this.buffered);
                if (!this.decrypting) {
                    Arrays.fill(this.buffer, (byte) 0);
                }
            }
            if (i3 != 0) {
                System.arraycopy(bArr, i2, bArr3, this.buffered, i3);
            }
            if (iPadLength != 0) {
                this.padding.padWithLen(bArr3, Math.addExact(this.buffered, i3), iPadLength);
            }
            return bArr3;
        }
        return bArr;
    }

    private int fillOutputBuffer(byte[] bArr, int i2, byte[] bArr2, int i3, int i4, byte[] bArr3) throws BadPaddingException, IllegalBlockSizeException, ShortBufferException {
        try {
            int iFinalNoPadding = finalNoPadding(bArr, i2, bArr2, i3, i4);
            if (this.decrypting && this.padding != null) {
                iFinalNoPadding = unpad(iFinalNoPadding, bArr2);
            }
            int i5 = iFinalNoPadding;
            if (!this.decrypting) {
                this.requireReinit = this.cipherMode == 7;
                if (bArr != bArr3) {
                    Arrays.fill(bArr, (byte) 0);
                }
            }
            return i5;
        } catch (Throwable th) {
            if (!this.decrypting) {
                this.requireReinit = this.cipherMode == 7;
                if (bArr != bArr3) {
                    Arrays.fill(bArr, (byte) 0);
                }
            }
            throw th;
        }
    }

    private int checkOutputCapacity(byte[] bArr, int i2, int i3) throws ShortBufferException {
        int length = bArr.length - i2;
        int i4 = this.decrypting ? i3 - this.blockSize : i3;
        if (bArr == null || length < i4) {
            throw new ShortBufferException("Output buffer must be (at least) " + i4 + " bytes long");
        }
        return length;
    }

    private void checkReinit() {
        if (this.requireReinit) {
            throw new IllegalStateException("Must use either different key or iv for GCM encryption");
        }
    }

    private int finalNoPadding(byte[] bArr, int i2, byte[] bArr2, int i3, int i4) throws IllegalBlockSizeException, AEADBadTagException, ShortBufferException {
        int iEncryptFinal;
        if (this.cipherMode != 7 && (bArr == null || i4 == 0)) {
            return 0;
        }
        if (this.cipherMode != 2 && this.cipherMode != 3 && this.cipherMode != 7 && i4 % this.unitBytes != 0 && this.cipherMode != 6) {
            if (this.padding != null) {
                throw new IllegalBlockSizeException("Input length (with padding) not multiple of " + this.unitBytes + " bytes");
            }
            throw new IllegalBlockSizeException("Input length not multiple of " + this.unitBytes + " bytes");
        }
        if (this.decrypting) {
            iEncryptFinal = this.cipher.decryptFinal(bArr, i2, i4, bArr2, i3);
        } else {
            iEncryptFinal = this.cipher.encryptFinal(bArr, i2, i4, bArr2, i3);
        }
        return iEncryptFinal;
    }

    byte[] wrap(Key key) throws IllegalBlockSizeException, InvalidKeyException {
        byte[] encoded;
        byte[] bArrDoFinal = null;
        try {
            encoded = key.getEncoded();
        } catch (BadPaddingException e2) {
        }
        if (encoded == null || encoded.length == 0) {
            throw new InvalidKeyException("Cannot get an encoding of the key to be wrapped");
        }
        bArrDoFinal = doFinal(encoded, 0, encoded.length);
        return bArrDoFinal;
    }

    Key unwrap(byte[] bArr, String str, int i2) throws NoSuchAlgorithmException, InvalidKeyException {
        try {
            return ConstructKeys.constructKey(doFinal(bArr, 0, bArr.length), str, i2);
        } catch (BadPaddingException e2) {
            throw new InvalidKeyException("The wrapped key is not padded correctly");
        } catch (IllegalBlockSizeException e3) {
            throw new InvalidKeyException("The wrapped key does not have the correct length");
        }
    }

    void updateAAD(byte[] bArr, int i2, int i3) {
        checkReinit();
        this.cipher.updateAAD(bArr, i2, i3);
    }
}
