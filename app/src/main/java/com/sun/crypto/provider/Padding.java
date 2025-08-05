package com.sun.crypto.provider;

import javax.crypto.ShortBufferException;

/* loaded from: sunjce_provider.jar:com/sun/crypto/provider/Padding.class */
interface Padding {
    void padWithLen(byte[] bArr, int i2, int i3) throws ShortBufferException;

    int unpad(byte[] bArr, int i2, int i3);

    int padLength(int i2);
}
