package com.sun.crypto.provider;

import javax.crypto.ShortBufferException;

/* loaded from: sunjce_provider.jar:com/sun/crypto/provider/ISO10126Padding.class */
final class ISO10126Padding implements Padding {
    private int blockSize;

    ISO10126Padding(int i2) {
        this.blockSize = i2;
    }

    @Override // com.sun.crypto.provider.Padding
    public void padWithLen(byte[] bArr, int i2, int i3) throws ShortBufferException {
        if (bArr == null) {
            return;
        }
        int iAddExact = Math.addExact(i2, i3);
        if (iAddExact > bArr.length) {
            throw new ShortBufferException("Buffer too small to hold padding");
        }
        byte[] bArr2 = new byte[i3 - 1];
        SunJCE.getRandom().nextBytes(bArr2);
        System.arraycopy(bArr2, 0, bArr, i2, i3 - 1);
        bArr[iAddExact - 1] = (byte) (i3 & 255);
    }

    @Override // com.sun.crypto.provider.Padding
    public int unpad(byte[] bArr, int i2, int i3) {
        int i4;
        if (bArr == null || i3 == 0) {
            return 0;
        }
        int iAddExact = Math.addExact(i2, i3);
        int i5 = bArr[iAddExact - 1] & 255;
        if (i5 < 1 || i5 > this.blockSize || (i4 = iAddExact - i5) < i2) {
            return -1;
        }
        return i4;
    }

    @Override // com.sun.crypto.provider.Padding
    public int padLength(int i2) {
        return this.blockSize - (i2 % this.blockSize);
    }
}
