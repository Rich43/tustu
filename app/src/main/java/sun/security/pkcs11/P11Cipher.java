package sun.security.pkcs11;

import java.nio.ByteBuffer;
import java.security.AlgorithmParameters;
import java.security.GeneralSecurityException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.ProviderException;
import java.security.SecureRandom;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.InvalidParameterSpecException;
import java.util.Arrays;
import java.util.Locale;
import javax.crypto.BadPaddingException;
import javax.crypto.CipherSpi;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.ShortBufferException;
import javax.crypto.spec.IvParameterSpec;
import sun.nio.ch.DirectBuffer;
import sun.security.jca.JCAUtil;
import sun.security.pkcs11.wrapper.CK_AES_CTR_PARAMS;
import sun.security.pkcs11.wrapper.CK_MECHANISM;
import sun.security.pkcs11.wrapper.PKCS11Constants;
import sun.security.pkcs11.wrapper.PKCS11Exception;

/* loaded from: sunpkcs11.jar:sun/security/pkcs11/P11Cipher.class */
final class P11Cipher extends CipherSpi {
    private static final int MODE_ECB = 3;
    private static final int MODE_CBC = 4;
    private static final int MODE_CTR = 5;
    private static final int PAD_NONE = 5;
    private static final int PAD_PKCS5 = 6;
    private final Token token;
    private final String algorithm;
    private final String keyAlgorithm;
    private final long mechanism;
    private Session session;
    private P11Key p11Key;
    private boolean initialized;
    private boolean encrypt;
    private int blockMode;
    private final int blockSize;
    private int paddingType;
    private Padding paddingObj;
    private byte[] padBuffer;
    private int padBufferLen;
    private byte[] iv;
    private int bytesBuffered;
    private int fixedKeySize;
    private boolean reqBlockUpdates = false;

    /* loaded from: sunpkcs11.jar:sun/security/pkcs11/P11Cipher$Padding.class */
    private interface Padding {
        int setPaddingBytes(byte[] bArr, int i2, int i3);

        int unpad(byte[] bArr, int i2) throws BadPaddingException, IllegalBlockSizeException;
    }

    /* loaded from: sunpkcs11.jar:sun/security/pkcs11/P11Cipher$PKCS5Padding.class */
    private static class PKCS5Padding implements Padding {
        private final int blockSize;

        PKCS5Padding(int i2) throws NoSuchPaddingException {
            if (i2 == 0) {
                throw new NoSuchPaddingException("PKCS#5 padding not supported with stream ciphers");
            }
            this.blockSize = i2;
        }

        @Override // sun.security.pkcs11.P11Cipher.Padding
        public int setPaddingBytes(byte[] bArr, int i2, int i3) {
            Arrays.fill(bArr, i2, i2 + i3, (byte) (i3 & 127));
            return i3;
        }

        @Override // sun.security.pkcs11.P11Cipher.Padding
        public int unpad(byte[] bArr, int i2) throws BadPaddingException, IllegalBlockSizeException {
            if (i2 < 1 || i2 % this.blockSize != 0) {
                throw new IllegalBlockSizeException("Input length must be multiples of " + this.blockSize);
            }
            byte b2 = bArr[i2 - 1];
            if (b2 < 1 || b2 > this.blockSize) {
                throw new BadPaddingException("Invalid pad value!");
            }
            for (int i3 = i2 - b2; i3 < i2; i3++) {
                if (bArr[i3] != b2) {
                    throw new BadPaddingException("Invalid pad bytes!");
                }
            }
            return b2;
        }
    }

    P11Cipher(Token token, String str, long j2) throws PKCS11Exception, NoSuchAlgorithmException {
        this.fixedKeySize = -1;
        this.token = token;
        this.algorithm = str;
        this.mechanism = j2;
        String[] strArrSplit = str.split("/");
        if (strArrSplit[0].startsWith("AES")) {
            this.blockSize = 16;
            int iIndexOf = strArrSplit[0].indexOf(95);
            if (iIndexOf != -1) {
                this.fixedKeySize = Integer.parseInt(strArrSplit[0].substring(iIndexOf + 1)) / 8;
            }
            this.keyAlgorithm = "AES";
        } else {
            this.keyAlgorithm = strArrSplit[0];
            if (this.keyAlgorithm.equals("RC4") || this.keyAlgorithm.equals("ARCFOUR")) {
                this.blockSize = 0;
            } else {
                this.blockSize = 8;
            }
        }
        this.blockMode = strArrSplit.length > 1 ? parseMode(strArrSplit[1]) : 3;
        try {
            engineSetPadding(strArrSplit.length > 2 ? strArrSplit[2] : this.blockSize == 0 ? "NoPadding" : "PKCS5Padding");
        } catch (NoSuchPaddingException e2) {
            throw new ProviderException(e2);
        }
    }

    @Override // javax.crypto.CipherSpi
    protected void engineSetMode(String str) throws NoSuchAlgorithmException {
        throw new NoSuchAlgorithmException("Unsupported mode " + str);
    }

    private int parseMode(String str) throws NoSuchAlgorithmException {
        int i2;
        String upperCase = str.toUpperCase(Locale.ENGLISH);
        if (upperCase.equals("ECB")) {
            i2 = 3;
        } else if (upperCase.equals("CBC")) {
            if (this.blockSize == 0) {
                throw new NoSuchAlgorithmException("CBC mode not supported with stream ciphers");
            }
            i2 = 4;
        } else if (upperCase.equals("CTR")) {
            i2 = 5;
        } else {
            throw new NoSuchAlgorithmException("Unsupported mode " + upperCase);
        }
        return i2;
    }

    @Override // javax.crypto.CipherSpi
    protected void engineSetPadding(String str) throws NoSuchPaddingException {
        this.paddingObj = null;
        this.padBuffer = null;
        String upperCase = str.toUpperCase(Locale.ENGLISH);
        if (upperCase.equals("NOPADDING")) {
            this.paddingType = 5;
            return;
        }
        if (upperCase.equals("PKCS5PADDING")) {
            if (this.blockMode == 5) {
                throw new NoSuchPaddingException("PKCS#5 padding not supported with CTR mode");
            }
            this.paddingType = 6;
            if (this.mechanism != 293 && this.mechanism != 310 && this.mechanism != PKCS11Constants.CKM_AES_CBC_PAD) {
                this.paddingObj = new PKCS5Padding(this.blockSize);
                this.padBuffer = new byte[this.blockSize];
                char[] cArr = this.token.tokenInfo.label;
                this.reqBlockUpdates = cArr[0] == 'N' && cArr[1] == 'S' && cArr[2] == 'S';
                return;
            }
            return;
        }
        throw new NoSuchPaddingException("Unsupported padding " + upperCase);
    }

    @Override // javax.crypto.CipherSpi
    protected int engineGetBlockSize() {
        return this.blockSize;
    }

    @Override // javax.crypto.CipherSpi
    protected int engineGetOutputSize(int i2) {
        return doFinalLength(i2);
    }

    @Override // javax.crypto.CipherSpi
    protected byte[] engineGetIV() {
        if (this.iv == null) {
            return null;
        }
        return (byte[]) this.iv.clone();
    }

    @Override // javax.crypto.CipherSpi
    protected AlgorithmParameters engineGetParameters() {
        if (this.iv == null) {
            return null;
        }
        IvParameterSpec ivParameterSpec = new IvParameterSpec(this.iv);
        try {
            AlgorithmParameters algorithmParameters = AlgorithmParameters.getInstance(this.keyAlgorithm, P11Util.getSunJceProvider());
            algorithmParameters.init(ivParameterSpec);
            return algorithmParameters;
        } catch (GeneralSecurityException e2) {
            throw new ProviderException("Could not encode parameters", e2);
        }
    }

    @Override // javax.crypto.CipherSpi
    protected void engineInit(int i2, Key key, SecureRandom secureRandom) throws InvalidKeyException {
        try {
            implInit(i2, key, null, secureRandom);
        } catch (InvalidAlgorithmParameterException e2) {
            throw new InvalidKeyException("init() failed", e2);
        }
    }

    @Override // javax.crypto.CipherSpi
    protected void engineInit(int i2, Key key, AlgorithmParameterSpec algorithmParameterSpec, SecureRandom secureRandom) throws InvalidKeyException, InvalidAlgorithmParameterException {
        byte[] iv;
        if (algorithmParameterSpec != null) {
            if (!(algorithmParameterSpec instanceof IvParameterSpec)) {
                throw new InvalidAlgorithmParameterException("Only IvParameterSpec supported");
            }
            iv = ((IvParameterSpec) algorithmParameterSpec).getIV();
        } else {
            iv = null;
        }
        implInit(i2, key, iv, secureRandom);
    }

    @Override // javax.crypto.CipherSpi
    protected void engineInit(int i2, Key key, AlgorithmParameters algorithmParameters, SecureRandom secureRandom) throws InvalidKeyException, InvalidAlgorithmParameterException {
        byte[] iv;
        if (algorithmParameters != null) {
            try {
                iv = ((IvParameterSpec) algorithmParameters.getParameterSpec(IvParameterSpec.class)).getIV();
            } catch (InvalidParameterSpecException e2) {
                throw new InvalidAlgorithmParameterException("Could not decode IV", e2);
            }
        } else {
            iv = null;
        }
        implInit(i2, key, iv, secureRandom);
    }

    private void implInit(int i2, Key key, byte[] bArr, SecureRandom secureRandom) throws InvalidKeyException, InvalidAlgorithmParameterException {
        int length;
        reset(true);
        if (this.fixedKeySize != -1) {
            if (key instanceof P11Key) {
                length = ((P11Key) key).length() >> 3;
            } else {
                length = key.getEncoded().length;
            }
            if (length != this.fixedKeySize) {
                throw new InvalidKeyException("Key size is invalid");
            }
        }
        switch (i2) {
            case 1:
                this.encrypt = true;
                break;
            case 2:
                this.encrypt = false;
                break;
            default:
                throw new InvalidAlgorithmParameterException("Unsupported mode: " + i2);
        }
        if (this.blockMode == 3) {
            if (bArr != null) {
                if (this.blockSize == 0) {
                    throw new InvalidAlgorithmParameterException("IV not used with stream ciphers");
                }
                throw new InvalidAlgorithmParameterException("IV not used in ECB mode");
            }
        } else if (bArr == null) {
            if (!this.encrypt) {
                throw new InvalidAlgorithmParameterException(this.blockMode == 4 ? "IV must be specified for decryption in CBC mode" : "IV must be specified for decryption in CTR mode");
            }
            if (secureRandom == null) {
                secureRandom = JCAUtil.getSecureRandom();
            }
            bArr = new byte[this.blockSize];
            secureRandom.nextBytes(bArr);
        } else if (bArr.length != this.blockSize) {
            throw new InvalidAlgorithmParameterException("IV length must match block size");
        }
        this.iv = bArr;
        this.p11Key = P11SecretKeyFactory.convertKey(this.token, key, this.keyAlgorithm);
        try {
            initialize();
        } catch (PKCS11Exception e2) {
            throw new InvalidKeyException("Could not initialize cipher", e2);
        }
    }

    private void reset(boolean z2) {
        if (!this.initialized) {
            return;
        }
        this.initialized = false;
        try {
            if (this.session == null) {
                return;
            }
            if (z2 && this.token.explicitCancel) {
                cancelOperation();
            }
        } finally {
            this.p11Key.releaseKeyID();
            this.session = this.token.releaseSession(this.session);
            this.bytesBuffered = 0;
            this.padBufferLen = 0;
        }
    }

    private void cancelOperation() {
        this.token.ensureValid();
        try {
            int iDoFinalLength = doFinalLength(0);
            byte[] bArr = new byte[iDoFinalLength];
            if (this.encrypt) {
                this.token.p11.C_EncryptFinal(this.session.id(), 0L, bArr, 0, iDoFinalLength);
            } else {
                this.token.p11.C_DecryptFinal(this.session.id(), 0L, bArr, 0, iDoFinalLength);
            }
        } catch (PKCS11Exception e2) {
            if (e2.getErrorCode() != 145 && this.encrypt) {
                throw new ProviderException("Cancel failed", e2);
            }
        }
    }

    private void ensureInitialized() throws PKCS11Exception {
        if (!this.initialized) {
            initialize();
        }
    }

    private void initialize() throws PKCS11Exception {
        if (this.p11Key == null) {
            throw new ProviderException("Operation cannot be performed without calling engineInit first");
        }
        this.token.ensureValid();
        long keyID = this.p11Key.getKeyID();
        try {
            if (this.session == null) {
                this.session = this.token.getOpSession();
            }
            CK_MECHANISM ck_mechanism = this.blockMode == 5 ? new CK_MECHANISM(this.mechanism, new CK_AES_CTR_PARAMS(this.iv)) : new CK_MECHANISM(this.mechanism, this.iv);
            if (this.encrypt) {
                this.token.p11.C_EncryptInit(this.session.id(), ck_mechanism, keyID);
            } else {
                this.token.p11.C_DecryptInit(this.session.id(), ck_mechanism, keyID);
            }
            this.initialized = true;
            this.bytesBuffered = 0;
            this.padBufferLen = 0;
        } catch (PKCS11Exception e2) {
            this.p11Key.releaseKeyID();
            this.session = this.token.releaseSession(this.session);
            throw e2;
        }
    }

    private int updateLength(int i2) {
        if (i2 <= 0) {
            return 0;
        }
        int i3 = i2 + this.bytesBuffered;
        if (this.blockSize != 0 && this.blockMode != 5) {
            i3 -= i3 & (this.blockSize - 1);
        }
        return i3;
    }

    private int doFinalLength(int i2) {
        if (i2 < 0) {
            return 0;
        }
        int i3 = i2 + this.bytesBuffered;
        if (this.blockSize != 0 && this.encrypt && this.paddingType != 5) {
            i3 += this.blockSize - (i3 & (this.blockSize - 1));
        }
        return i3;
    }

    @Override // javax.crypto.CipherSpi
    protected byte[] engineUpdate(byte[] bArr, int i2, int i3) {
        try {
            byte[] bArr2 = new byte[updateLength(i3)];
            return P11Util.convert(bArr2, 0, engineUpdate(bArr, i2, i3, bArr2, 0));
        } catch (ShortBufferException e2) {
            throw new ProviderException(e2);
        }
    }

    @Override // javax.crypto.CipherSpi
    protected int engineUpdate(byte[] bArr, int i2, int i3, byte[] bArr2, int i4) throws ShortBufferException {
        return implUpdate(bArr, i2, i3, bArr2, i4, bArr2.length - i4);
    }

    @Override // javax.crypto.CipherSpi
    protected int engineUpdate(ByteBuffer byteBuffer, ByteBuffer byteBuffer2) throws ShortBufferException {
        return implUpdate(byteBuffer, byteBuffer2);
    }

    @Override // javax.crypto.CipherSpi
    protected byte[] engineDoFinal(byte[] bArr, int i2, int i3) throws BadPaddingException, IllegalBlockSizeException {
        try {
            byte[] bArr2 = new byte[doFinalLength(i3)];
            return P11Util.convert(bArr2, 0, engineDoFinal(bArr, i2, i3, bArr2, 0));
        } catch (ShortBufferException e2) {
            throw new ProviderException(e2);
        }
    }

    @Override // javax.crypto.CipherSpi
    protected int engineDoFinal(byte[] bArr, int i2, int i3, byte[] bArr2, int i4) throws BadPaddingException, IllegalBlockSizeException, ShortBufferException {
        int iEngineUpdate = 0;
        if (i3 != 0 && bArr != null) {
            iEngineUpdate = engineUpdate(bArr, i2, i3, bArr2, i4);
            i4 += iEngineUpdate;
        }
        return iEngineUpdate + implDoFinal(bArr2, i4, bArr2.length - i4);
    }

    @Override // javax.crypto.CipherSpi
    protected int engineDoFinal(ByteBuffer byteBuffer, ByteBuffer byteBuffer2) throws BadPaddingException, IllegalBlockSizeException, ShortBufferException {
        return engineUpdate(byteBuffer, byteBuffer2) + implDoFinal(byteBuffer2);
    }

    private int implUpdate(byte[] bArr, int i2, int i3, byte[] bArr2, int i4, int i5) throws ShortBufferException {
        if (i5 < updateLength(i3)) {
            throw new ShortBufferException();
        }
        try {
            ensureInitialized();
            int iC_DecryptUpdate = 0;
            int length = 0;
            if (this.paddingObj != null && (!this.encrypt || this.reqBlockUpdates)) {
                if (this.padBufferLen != 0) {
                    if (this.padBufferLen != this.padBuffer.length) {
                        int length2 = this.padBuffer.length - this.padBufferLen;
                        if (i3 > length2) {
                            bufferInputBytes(bArr, i2, length2);
                            i2 += length2;
                            i3 -= length2;
                        } else {
                            bufferInputBytes(bArr, i2, i3);
                            return 0;
                        }
                    }
                    if (this.encrypt) {
                        iC_DecryptUpdate = this.token.p11.C_EncryptUpdate(this.session.id(), 0L, this.padBuffer, 0, this.padBufferLen, 0L, bArr2, i4, i5);
                    } else {
                        iC_DecryptUpdate = this.token.p11.C_DecryptUpdate(this.session.id(), 0L, this.padBuffer, 0, this.padBufferLen, 0L, bArr2, i4, i5);
                    }
                    this.padBufferLen = 0;
                }
                length = i3 & (this.blockSize - 1);
                if (!this.encrypt && length == 0) {
                    length = this.padBuffer.length;
                }
                i3 -= length;
            }
            if (i3 > 0) {
                if (this.encrypt) {
                    iC_DecryptUpdate += this.token.p11.C_EncryptUpdate(this.session.id(), 0L, bArr, i2, i3, 0L, bArr2, i4 + iC_DecryptUpdate, i5 - iC_DecryptUpdate);
                } else {
                    iC_DecryptUpdate += this.token.p11.C_DecryptUpdate(this.session.id(), 0L, bArr, i2, i3, 0L, bArr2, i4 + iC_DecryptUpdate, i5 - iC_DecryptUpdate);
                }
            }
            if (this.paddingObj != null && length > 0) {
                bufferInputBytes(bArr, i2 + i3, length);
            }
            this.bytesBuffered += i3 - iC_DecryptUpdate;
            return iC_DecryptUpdate;
        } catch (PKCS11Exception e2) {
            if (e2.getErrorCode() == 336) {
                throw ((ShortBufferException) new ShortBufferException().initCause(e2));
            }
            reset(true);
            throw new ProviderException("update() failed", e2);
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    private int implUpdate(ByteBuffer byteBuffer, ByteBuffer byteBuffer2) throws ShortBufferException {
        int iRemaining = byteBuffer.remaining();
        if (iRemaining <= 0) {
            return 0;
        }
        int iRemaining2 = byteBuffer2.remaining();
        if (iRemaining2 < updateLength(iRemaining)) {
            throw new ShortBufferException();
        }
        int iPosition = byteBuffer.position();
        try {
            ensureInitialized();
            long jAddress = 0;
            int iArrayOffset = 0;
            byte[] bArrArray = null;
            if (byteBuffer instanceof DirectBuffer) {
                jAddress = ((DirectBuffer) byteBuffer).address();
                iArrayOffset = iPosition;
            } else if (byteBuffer.hasArray()) {
                bArrArray = byteBuffer.array();
                iArrayOffset = iPosition + byteBuffer.arrayOffset();
            }
            long jAddress2 = 0;
            int iPosition2 = 0;
            byte[] bArrArray2 = null;
            if (byteBuffer2 instanceof DirectBuffer) {
                jAddress2 = ((DirectBuffer) byteBuffer2).address();
                iPosition2 = byteBuffer2.position();
            } else if (byteBuffer2.hasArray()) {
                bArrArray2 = byteBuffer2.array();
                iPosition2 = byteBuffer2.position() + byteBuffer2.arrayOffset();
            } else {
                bArrArray2 = new byte[iRemaining2];
            }
            int iC_DecryptUpdate = 0;
            int length = 0;
            if (this.paddingObj != null && (!this.encrypt || this.reqBlockUpdates)) {
                if (this.padBufferLen != 0) {
                    if (this.padBufferLen != this.padBuffer.length) {
                        int length2 = this.padBuffer.length - this.padBufferLen;
                        if (iRemaining > length2) {
                            bufferInputBytes(byteBuffer, length2);
                            iArrayOffset += length2;
                            iRemaining -= length2;
                        } else {
                            bufferInputBytes(byteBuffer, iRemaining);
                            return 0;
                        }
                    }
                    if (this.encrypt) {
                        iC_DecryptUpdate = this.token.p11.C_EncryptUpdate(this.session.id(), 0L, this.padBuffer, 0, this.padBufferLen, jAddress2, bArrArray2, iPosition2, iRemaining2);
                    } else {
                        iC_DecryptUpdate = this.token.p11.C_DecryptUpdate(this.session.id(), 0L, this.padBuffer, 0, this.padBufferLen, jAddress2, bArrArray2, iPosition2, iRemaining2);
                    }
                    this.padBufferLen = 0;
                }
                length = iRemaining & (this.blockSize - 1);
                if (!this.encrypt && length == 0) {
                    length = this.padBuffer.length;
                }
                iRemaining -= length;
            }
            if (iRemaining > 0) {
                if (jAddress == 0 && bArrArray == null) {
                    bArrArray = new byte[iRemaining];
                    byteBuffer.get(bArrArray);
                } else {
                    byteBuffer.position(byteBuffer.position() + iRemaining);
                }
                if (this.encrypt) {
                    iC_DecryptUpdate += this.token.p11.C_EncryptUpdate(this.session.id(), jAddress, bArrArray, iArrayOffset, iRemaining, jAddress2, bArrArray2, iPosition2 + iC_DecryptUpdate, iRemaining2 - iC_DecryptUpdate);
                } else {
                    iC_DecryptUpdate += this.token.p11.C_DecryptUpdate(this.session.id(), jAddress, bArrArray, iArrayOffset, iRemaining, jAddress2, bArrArray2, iPosition2 + iC_DecryptUpdate, iRemaining2 - iC_DecryptUpdate);
                }
            }
            if (this.paddingObj != null && length > 0) {
                bufferInputBytes(byteBuffer, length);
            }
            this.bytesBuffered += iRemaining - iC_DecryptUpdate;
            if (!(byteBuffer2 instanceof DirectBuffer) && !byteBuffer2.hasArray()) {
                byteBuffer2.put(bArrArray2, iPosition2, iC_DecryptUpdate);
            } else {
                byteBuffer2.position(byteBuffer2.position() + iC_DecryptUpdate);
            }
            return iC_DecryptUpdate;
        } catch (PKCS11Exception e2) {
            byteBuffer.position(iPosition);
            if (e2.getErrorCode() == 336) {
                throw ((ShortBufferException) new ShortBufferException().initCause(e2));
            }
            reset(true);
            throw new ProviderException("update() failed", e2);
        }
    }

    private int implDoFinal(byte[] bArr, int i2, int i3) throws BadPaddingException, IllegalBlockSizeException, ShortBufferException {
        boolean z2;
        int iC_DecryptFinal;
        int iDoFinalLength = doFinalLength(0);
        if (i3 < iDoFinalLength) {
            throw new ShortBufferException();
        }
        try {
            try {
                ensureInitialized();
                int iC_DecryptUpdate = 0;
                if (this.encrypt) {
                    if (this.paddingObj != null) {
                        int i4 = 0;
                        if (this.reqBlockUpdates) {
                            if (this.padBufferLen == this.padBuffer.length) {
                                iC_DecryptUpdate = this.token.p11.C_EncryptUpdate(this.session.id(), 0L, this.padBuffer, 0, this.padBufferLen, 0L, bArr, i2, i3);
                            } else {
                                i4 = this.padBufferLen;
                            }
                        }
                        iC_DecryptUpdate += this.token.p11.C_EncryptUpdate(this.session.id(), 0L, this.padBuffer, 0, i4 + this.paddingObj.setPaddingBytes(this.padBuffer, i4, iDoFinalLength - this.bytesBuffered), 0L, bArr, i2 + iC_DecryptUpdate, i3 - iC_DecryptUpdate);
                    }
                    z2 = false;
                    iC_DecryptFinal = iC_DecryptUpdate + this.token.p11.C_EncryptFinal(this.session.id(), 0L, bArr, i2 + iC_DecryptUpdate, i3 - iC_DecryptUpdate);
                } else {
                    if (this.bytesBuffered == 0 && this.padBufferLen == 0) {
                        return 0;
                    }
                    if (this.paddingObj != null) {
                        if (this.padBufferLen != 0) {
                            iC_DecryptUpdate = this.token.p11.C_DecryptUpdate(this.session.id(), 0L, this.padBuffer, 0, this.padBufferLen, 0L, this.padBuffer, 0, this.padBuffer.length);
                        }
                        z2 = false;
                        int iC_DecryptFinal2 = iC_DecryptUpdate + this.token.p11.C_DecryptFinal(this.session.id(), 0L, this.padBuffer, iC_DecryptUpdate, this.padBuffer.length - iC_DecryptUpdate);
                        iC_DecryptFinal = iC_DecryptFinal2 - this.paddingObj.unpad(this.padBuffer, iC_DecryptFinal2);
                        System.arraycopy(this.padBuffer, 0, bArr, i2, iC_DecryptFinal);
                    } else {
                        z2 = false;
                        iC_DecryptFinal = this.token.p11.C_DecryptFinal(this.session.id(), 0L, bArr, i2, i3);
                    }
                }
                int i5 = iC_DecryptFinal;
                reset(z2);
                return i5;
            } catch (PKCS11Exception e2) {
                handleException(e2);
                throw new ProviderException("doFinal() failed", e2);
            }
        } finally {
            reset(true);
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    private int implDoFinal(ByteBuffer byteBuffer) throws BadPaddingException, IllegalBlockSizeException, ShortBufferException {
        boolean z2;
        int iC_DecryptFinal;
        int iRemaining = byteBuffer.remaining();
        int iDoFinalLength = doFinalLength(0);
        if (iRemaining < iDoFinalLength) {
            throw new ShortBufferException();
        }
        try {
            try {
                ensureInitialized();
                long jAddress = 0;
                byte[] bArrArray = null;
                int iPosition = 0;
                if (byteBuffer instanceof DirectBuffer) {
                    jAddress = ((DirectBuffer) byteBuffer).address();
                    iPosition = byteBuffer.position();
                } else if (byteBuffer.hasArray()) {
                    bArrArray = byteBuffer.array();
                    iPosition = byteBuffer.position() + byteBuffer.arrayOffset();
                } else {
                    bArrArray = new byte[iRemaining];
                }
                int iC_DecryptUpdate = 0;
                if (this.encrypt) {
                    if (this.paddingObj != null) {
                        int i2 = 0;
                        if (this.reqBlockUpdates) {
                            if (this.padBufferLen == this.padBuffer.length) {
                                iC_DecryptUpdate = this.token.p11.C_EncryptUpdate(this.session.id(), 0L, this.padBuffer, 0, this.padBufferLen, jAddress, bArrArray, iPosition, iRemaining);
                            } else {
                                i2 = this.padBufferLen;
                            }
                        }
                        iC_DecryptUpdate += this.token.p11.C_EncryptUpdate(this.session.id(), 0L, this.padBuffer, 0, i2 + this.paddingObj.setPaddingBytes(this.padBuffer, i2, iDoFinalLength - this.bytesBuffered), jAddress, bArrArray, iPosition + iC_DecryptUpdate, iRemaining - iC_DecryptUpdate);
                    }
                    z2 = false;
                    iC_DecryptFinal = iC_DecryptUpdate + this.token.p11.C_EncryptFinal(this.session.id(), jAddress, bArrArray, iPosition + iC_DecryptUpdate, iRemaining - iC_DecryptUpdate);
                } else {
                    if (this.bytesBuffered == 0 && this.padBufferLen == 0) {
                        return 0;
                    }
                    if (this.paddingObj != null) {
                        if (this.padBufferLen != 0) {
                            iC_DecryptUpdate = this.token.p11.C_DecryptUpdate(this.session.id(), 0L, this.padBuffer, 0, this.padBufferLen, 0L, this.padBuffer, 0, this.padBuffer.length);
                            this.padBufferLen = 0;
                        }
                        z2 = false;
                        int iC_DecryptFinal2 = iC_DecryptUpdate + this.token.p11.C_DecryptFinal(this.session.id(), 0L, this.padBuffer, iC_DecryptUpdate, this.padBuffer.length - iC_DecryptUpdate);
                        iC_DecryptFinal = iC_DecryptFinal2 - this.paddingObj.unpad(this.padBuffer, iC_DecryptFinal2);
                        bArrArray = this.padBuffer;
                        iPosition = 0;
                    } else {
                        z2 = false;
                        iC_DecryptFinal = this.token.p11.C_DecryptFinal(this.session.id(), jAddress, bArrArray, iPosition, iRemaining);
                    }
                }
                if ((!this.encrypt && this.paddingObj != null) || (!(byteBuffer instanceof DirectBuffer) && !byteBuffer.hasArray())) {
                    byteBuffer.put(bArrArray, iPosition, iC_DecryptFinal);
                } else {
                    byteBuffer.position(byteBuffer.position() + iC_DecryptFinal);
                }
                int i3 = iC_DecryptFinal;
                reset(z2);
                return i3;
            } catch (PKCS11Exception e2) {
                handleException(e2);
                throw new ProviderException("doFinal() failed", e2);
            }
        } finally {
            reset(true);
        }
    }

    private void handleException(PKCS11Exception pKCS11Exception) throws IllegalBlockSizeException, ShortBufferException {
        long errorCode = pKCS11Exception.getErrorCode();
        if (errorCode == 336) {
            throw ((ShortBufferException) new ShortBufferException().initCause(pKCS11Exception));
        }
        if (errorCode == 33 || errorCode == 65) {
            throw ((IllegalBlockSizeException) new IllegalBlockSizeException(pKCS11Exception.toString()).initCause(pKCS11Exception));
        }
    }

    @Override // javax.crypto.CipherSpi
    protected byte[] engineWrap(Key key) throws IllegalBlockSizeException, InvalidKeyException {
        throw new UnsupportedOperationException("engineWrap()");
    }

    @Override // javax.crypto.CipherSpi
    protected Key engineUnwrap(byte[] bArr, String str, int i2) throws NoSuchAlgorithmException, InvalidKeyException {
        throw new UnsupportedOperationException("engineUnwrap()");
    }

    @Override // javax.crypto.CipherSpi
    protected int engineGetKeySize(Key key) throws InvalidKeyException {
        return P11SecretKeyFactory.convertKey(this.token, key, this.keyAlgorithm).length();
    }

    private final void bufferInputBytes(byte[] bArr, int i2, int i3) {
        System.arraycopy(bArr, i2, this.padBuffer, this.padBufferLen, i3);
        this.padBufferLen += i3;
        this.bytesBuffered += i3;
    }

    private final void bufferInputBytes(ByteBuffer byteBuffer, int i2) {
        byteBuffer.get(this.padBuffer, this.padBufferLen, i2);
        this.padBufferLen += i2;
        this.bytesBuffered += i2;
    }
}
