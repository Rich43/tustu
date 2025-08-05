package com.sun.crypto.provider;

import java.io.ByteArrayOutputStream;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.ProviderException;
import javax.crypto.AEADBadTagException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.ShortBufferException;

/* loaded from: sunjce_provider.jar:com/sun/crypto/provider/GaloisCounterMode.class */
final class GaloisCounterMode extends FeedbackCipher {
    static int DEFAULT_TAG_LEN = 16;
    static int DEFAULT_IV_LEN = 12;
    private static final int MAX_BUF_SIZE = Integer.MAX_VALUE;
    private static final int TRIGGERLEN = 65536;
    private ByteArrayOutputStream aadBuffer;
    private int sizeOfAAD;
    private ByteArrayOutputStream ibuffer;
    private int tagLenBytes;
    private byte[] subkeyH;
    private byte[] preCounterBlock;
    private GCTR gctrPAndC;
    private GHASH ghashAllToS;
    private int processed;
    private byte[] aadBufferSave;
    private int sizeOfAADSave;
    private byte[] ibufferSave;
    private int processedSave;

    static void increment32(byte[] bArr) {
        if (bArr.length != 16) {
            throw new ProviderException("Illegal counter block length");
        }
        for (int length = bArr.length - 1; length >= bArr.length - 4; length--) {
            int i2 = length;
            byte b2 = (byte) (bArr[i2] + 1);
            bArr[i2] = b2;
            if (b2 != 0) {
                return;
            }
        }
    }

    private static byte[] getLengthBlock(int i2) {
        return new byte[]{0, 0, 0, 0, 0, 0, 0, 0, (byte) (r0 >>> 56), (byte) (r0 >>> 48), (byte) (r0 >>> 40), (byte) (r0 >>> 32), (byte) (r0 >>> 24), (byte) (r0 >>> 16), (byte) (r0 >>> 8), (byte) (i2 << 3)};
    }

    private static byte[] getLengthBlock(int i2, int i3) {
        return new byte[]{(byte) (r0 >>> 56), (byte) (r0 >>> 48), (byte) (r0 >>> 40), (byte) (r0 >>> 32), (byte) (r0 >>> 24), (byte) (r0 >>> 16), (byte) (r0 >>> 8), (byte) (i2 << 3), (byte) (r0 >>> 56), (byte) (r0 >>> 48), (byte) (r0 >>> 40), (byte) (r0 >>> 32), (byte) (r0 >>> 24), (byte) (r0 >>> 16), (byte) (r0 >>> 8), (byte) (i3 << 3)};
    }

    private static byte[] expandToOneBlock(byte[] bArr, int i2, int i3) {
        if (i3 > 16) {
            throw new ProviderException("input " + i3 + " too long");
        }
        if (i3 == 16 && i2 == 0) {
            return bArr;
        }
        byte[] bArr2 = new byte[16];
        System.arraycopy(bArr, i2, bArr2, 0, i3);
        return bArr2;
    }

    private static byte[] getJ0(byte[] bArr, byte[] bArr2) {
        byte[] bArrDigest;
        if (bArr.length == 12) {
            bArrDigest = expandToOneBlock(bArr, 0, bArr.length);
            bArrDigest[15] = 1;
        } else {
            GHASH ghash = new GHASH(bArr2);
            int length = bArr.length % 16;
            if (length != 0) {
                ghash.update(bArr, 0, bArr.length - length);
                ghash.update(expandToOneBlock(bArr, bArr.length - length, length));
            } else {
                ghash.update(bArr);
            }
            ghash.update(getLengthBlock(bArr.length));
            bArrDigest = ghash.digest();
        }
        return bArrDigest;
    }

    private static void checkDataLength(int i2, int i3) {
        if (i2 > Integer.MAX_VALUE - i3) {
            throw new ProviderException("SunJCE provider only supports input size up to 2147483647 bytes");
        }
    }

    GaloisCounterMode(SymmetricCipher symmetricCipher) {
        super(symmetricCipher);
        this.aadBuffer = new ByteArrayOutputStream();
        this.sizeOfAAD = 0;
        this.ibuffer = null;
        this.tagLenBytes = DEFAULT_TAG_LEN;
        this.subkeyH = null;
        this.preCounterBlock = null;
        this.gctrPAndC = null;
        this.ghashAllToS = null;
        this.processed = 0;
        this.aadBufferSave = null;
        this.sizeOfAADSave = 0;
        this.ibufferSave = null;
        this.processedSave = 0;
        this.aadBuffer = new ByteArrayOutputStream();
    }

    @Override // com.sun.crypto.provider.FeedbackCipher
    String getFeedback() {
        return "GCM";
    }

    @Override // com.sun.crypto.provider.FeedbackCipher
    void reset() {
        if (this.aadBuffer == null) {
            this.aadBuffer = new ByteArrayOutputStream();
        } else {
            this.aadBuffer.reset();
        }
        if (this.gctrPAndC != null) {
            this.gctrPAndC.reset();
        }
        if (this.ghashAllToS != null) {
            this.ghashAllToS.reset();
        }
        this.processed = 0;
        this.sizeOfAAD = 0;
        if (this.ibuffer != null) {
            this.ibuffer.reset();
        }
    }

    @Override // com.sun.crypto.provider.FeedbackCipher
    void save() {
        this.processedSave = this.processed;
        this.sizeOfAADSave = this.sizeOfAAD;
        this.aadBufferSave = (this.aadBuffer == null || this.aadBuffer.size() == 0) ? null : this.aadBuffer.toByteArray();
        if (this.gctrPAndC != null) {
            this.gctrPAndC.save();
        }
        if (this.ghashAllToS != null) {
            this.ghashAllToS.save();
        }
        if (this.ibuffer != null) {
            this.ibufferSave = this.ibuffer.toByteArray();
        }
    }

    @Override // com.sun.crypto.provider.FeedbackCipher
    void restore() {
        this.processed = this.processedSave;
        this.sizeOfAAD = this.sizeOfAADSave;
        if (this.aadBuffer != null) {
            this.aadBuffer.reset();
            if (this.aadBufferSave != null) {
                this.aadBuffer.write(this.aadBufferSave, 0, this.aadBufferSave.length);
            }
        }
        if (this.gctrPAndC != null) {
            this.gctrPAndC.restore();
        }
        if (this.ghashAllToS != null) {
            this.ghashAllToS.restore();
        }
        if (this.ibuffer != null) {
            this.ibuffer.reset();
            this.ibuffer.write(this.ibufferSave, 0, this.ibufferSave.length);
        }
    }

    @Override // com.sun.crypto.provider.FeedbackCipher
    void init(boolean z2, String str, byte[] bArr, byte[] bArr2) throws InvalidKeyException, InvalidAlgorithmParameterException {
        init(z2, str, bArr, bArr2, DEFAULT_TAG_LEN);
    }

    void init(boolean z2, String str, byte[] bArr, byte[] bArr2, int i2) throws InvalidKeyException, InvalidAlgorithmParameterException {
        if (bArr == null) {
            throw new InvalidKeyException("Internal error");
        }
        if (bArr2 == null) {
            throw new InvalidAlgorithmParameterException("Internal error");
        }
        if (bArr2.length == 0) {
            throw new InvalidAlgorithmParameterException("IV is empty");
        }
        this.embeddedCipher.init(false, str, bArr);
        this.subkeyH = new byte[16];
        this.embeddedCipher.encryptBlock(new byte[16], 0, this.subkeyH, 0);
        this.iv = (byte[]) bArr2.clone();
        this.preCounterBlock = getJ0(this.iv, this.subkeyH);
        byte[] bArr3 = (byte[]) this.preCounterBlock.clone();
        increment32(bArr3);
        this.gctrPAndC = new GCTR(this.embeddedCipher, bArr3);
        this.ghashAllToS = new GHASH(this.subkeyH);
        this.tagLenBytes = i2;
        if (this.aadBuffer == null) {
            this.aadBuffer = new ByteArrayOutputStream();
        } else {
            this.aadBuffer.reset();
        }
        this.processed = 0;
        this.sizeOfAAD = 0;
        if (z2) {
            this.ibuffer = new ByteArrayOutputStream();
        }
    }

    @Override // com.sun.crypto.provider.FeedbackCipher
    void updateAAD(byte[] bArr, int i2, int i3) {
        if (this.aadBuffer != null) {
            this.aadBuffer.write(bArr, i2, i3);
            return;
        }
        throw new IllegalStateException("Update has been called; no more AAD data");
    }

    void processAAD() {
        if (this.aadBuffer != null) {
            if (this.aadBuffer.size() > 0) {
                byte[] byteArray = this.aadBuffer.toByteArray();
                this.sizeOfAAD = byteArray.length;
                int length = byteArray.length % 16;
                if (length != 0) {
                    this.ghashAllToS.update(byteArray, 0, byteArray.length - length);
                    this.ghashAllToS.update(expandToOneBlock(byteArray, byteArray.length - length, length));
                } else {
                    this.ghashAllToS.update(byteArray);
                }
            }
            this.aadBuffer = null;
        }
    }

    void doLastBlock(byte[] bArr, int i2, int i3, byte[] bArr2, int i4, boolean z2) throws IllegalBlockSizeException {
        byte[] bArr3;
        int i5;
        int i6 = i3;
        if (z2) {
            bArr3 = bArr2;
            i5 = i4;
        } else {
            bArr3 = bArr;
            i5 = i2;
        }
        if (i3 > 65536) {
            int i7 = i3 / 1024;
            for (int i8 = 0; i7 > i8; i8++) {
                int iUpdate = this.gctrPAndC.update(bArr, i2, 96, bArr2, i4);
                this.ghashAllToS.update(bArr3, i5, iUpdate);
                i2 += iUpdate;
                i4 += iUpdate;
                i5 += iUpdate;
            }
            i6 -= i7 * 96;
            this.processed += i7 * 96;
        }
        this.gctrPAndC.doFinal(bArr, i2, i6, bArr2, i4);
        this.processed += i6;
        int i9 = i6 % 16;
        if (i9 != 0) {
            this.ghashAllToS.update(bArr3, i5, i6 - i9);
            this.ghashAllToS.update(expandToOneBlock(bArr3, (i5 + i6) - i9, i9));
        } else {
            this.ghashAllToS.update(bArr3, i5, i6);
        }
    }

    @Override // com.sun.crypto.provider.FeedbackCipher
    int encrypt(byte[] bArr, int i2, int i3, byte[] bArr2, int i4) {
        checkDataLength(this.processed, i3);
        RangeUtil.blockSizeCheck(i3, this.blockSize);
        processAAD();
        if (i3 > 0) {
            RangeUtil.nullAndBoundsCheck(bArr, i2, i3);
            RangeUtil.nullAndBoundsCheck(bArr2, i4, i3);
            this.gctrPAndC.update(bArr, i2, i3, bArr2, i4);
            this.processed += i3;
            this.ghashAllToS.update(bArr2, i4, i3);
        }
        return i3;
    }

    @Override // com.sun.crypto.provider.FeedbackCipher
    int encryptFinal(byte[] bArr, int i2, int i3, byte[] bArr2, int i4) throws IllegalBlockSizeException, ShortBufferException {
        if (i3 > Integer.MAX_VALUE - this.tagLenBytes) {
            throw new ShortBufferException("Can't fit both data and tag into one buffer");
        }
        try {
            RangeUtil.nullAndBoundsCheck(bArr2, i4, i3 + this.tagLenBytes);
            checkDataLength(this.processed, i3);
            processAAD();
            if (i3 > 0) {
                RangeUtil.nullAndBoundsCheck(bArr, i2, i3);
                doLastBlock(bArr, i2, i3, bArr2, i4, true);
            }
            this.ghashAllToS.update(getLengthBlock(this.sizeOfAAD, this.processed));
            byte[] bArrDigest = this.ghashAllToS.digest();
            byte[] bArr3 = new byte[bArrDigest.length];
            new GCTR(this.embeddedCipher, this.preCounterBlock).doFinal(bArrDigest, 0, bArrDigest.length, bArr3, 0);
            System.arraycopy(bArr3, 0, bArr2, i4 + i3, this.tagLenBytes);
            return i3 + this.tagLenBytes;
        } catch (ArrayIndexOutOfBoundsException e2) {
            throw new ShortBufferException("Output buffer too small");
        }
    }

    @Override // com.sun.crypto.provider.FeedbackCipher
    int decrypt(byte[] bArr, int i2, int i3, byte[] bArr2, int i4) {
        checkDataLength(this.ibuffer.size(), i3);
        RangeUtil.blockSizeCheck(i3, this.blockSize);
        processAAD();
        if (i3 > 0) {
            RangeUtil.nullAndBoundsCheck(bArr, i2, i3);
            this.ibuffer.write(bArr, i2, i3);
            return 0;
        }
        return 0;
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.sun.crypto.provider.FeedbackCipher
    int decryptFinal(byte[] bArr, int i2, int i3, byte[] bArr2, int i4) throws IllegalBlockSizeException, AEADBadTagException, ShortBufferException {
        if (i3 < this.tagLenBytes) {
            throw new AEADBadTagException("Input too short - need tag");
        }
        checkDataLength(this.ibuffer.size(), i3 - this.tagLenBytes);
        try {
            RangeUtil.nullAndBoundsCheck(bArr2, i4, (this.ibuffer.size() + i3) - this.tagLenBytes);
            processAAD();
            RangeUtil.nullAndBoundsCheck(bArr, i2, i3);
            byte[] bArr3 = new byte[this.tagLenBytes];
            System.arraycopy(bArr, (i2 + i3) - this.tagLenBytes, bArr3, 0, this.tagLenBytes);
            int length = i3 - this.tagLenBytes;
            if (bArr == bArr2 || this.ibuffer.size() > 0) {
                if (length > 0) {
                    this.ibuffer.write(bArr, i2, length);
                }
                bArr = this.ibuffer.toByteArray();
                i2 = 0;
                length = bArr.length;
                this.ibuffer.reset();
            }
            if (length > 0) {
                doLastBlock(bArr, i2, length, bArr2, i4, false);
            }
            this.ghashAllToS.update(getLengthBlock(this.sizeOfAAD, this.processed));
            byte[] bArrDigest = this.ghashAllToS.digest();
            byte[] bArr4 = new byte[bArrDigest.length];
            new GCTR(this.embeddedCipher, this.preCounterBlock).doFinal(bArrDigest, 0, bArrDigest.length, bArr4, 0);
            byte b2 = false;
            for (int i5 = 0; i5 < this.tagLenBytes; i5++) {
                b2 = ((b2 == true ? 1 : 0) | (bArr3[i5] ^ bArr4[i5])) == true ? 1 : 0;
            }
            if (b2 != false) {
                throw new AEADBadTagException("Tag mismatch!");
            }
            return length;
        } catch (ArrayIndexOutOfBoundsException e2) {
            throw new ShortBufferException("Output buffer too small");
        }
    }

    int getTagLen() {
        return this.tagLenBytes;
    }

    @Override // com.sun.crypto.provider.FeedbackCipher
    int getBufferedLength() {
        if (this.ibuffer == null) {
            return 0;
        }
        return this.ibuffer.size();
    }
}
