package javax.crypto;

import java.nio.ByteBuffer;
import java.security.AlgorithmParameters;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.ProviderException;
import java.security.SecureRandom;
import java.security.spec.AlgorithmParameterSpec;

/* loaded from: jce.jar:javax/crypto/CipherSpi.class */
public abstract class CipherSpi {
    protected abstract void engineSetMode(String str) throws NoSuchAlgorithmException;

    protected abstract void engineSetPadding(String str) throws NoSuchPaddingException;

    protected abstract int engineGetBlockSize();

    protected abstract int engineGetOutputSize(int i2);

    protected abstract byte[] engineGetIV();

    protected abstract AlgorithmParameters engineGetParameters();

    protected abstract void engineInit(int i2, Key key, SecureRandom secureRandom) throws InvalidKeyException;

    protected abstract void engineInit(int i2, Key key, AlgorithmParameterSpec algorithmParameterSpec, SecureRandom secureRandom) throws InvalidKeyException, InvalidAlgorithmParameterException;

    protected abstract void engineInit(int i2, Key key, AlgorithmParameters algorithmParameters, SecureRandom secureRandom) throws InvalidKeyException, InvalidAlgorithmParameterException;

    protected abstract byte[] engineUpdate(byte[] bArr, int i2, int i3);

    protected abstract int engineUpdate(byte[] bArr, int i2, int i3, byte[] bArr2, int i4) throws ShortBufferException;

    protected abstract byte[] engineDoFinal(byte[] bArr, int i2, int i3) throws BadPaddingException, IllegalBlockSizeException;

    protected abstract int engineDoFinal(byte[] bArr, int i2, int i3, byte[] bArr2, int i4) throws BadPaddingException, IllegalBlockSizeException, ShortBufferException;

    protected int engineUpdate(ByteBuffer byteBuffer, ByteBuffer byteBuffer2) throws ShortBufferException {
        try {
            return bufferCrypt(byteBuffer, byteBuffer2, true);
        } catch (BadPaddingException e2) {
            throw new ProviderException("Internal error in update()");
        } catch (IllegalBlockSizeException e3) {
            throw new ProviderException("Internal error in update()");
        }
    }

    protected int engineDoFinal(ByteBuffer byteBuffer, ByteBuffer byteBuffer2) throws BadPaddingException, IllegalBlockSizeException, ShortBufferException {
        return bufferCrypt(byteBuffer, byteBuffer2, false);
    }

    static int getTempArraySize(int i2) {
        return Math.min(4096, i2);
    }

    private int bufferCrypt(ByteBuffer byteBuffer, ByteBuffer byteBuffer2, boolean z2) throws BadPaddingException, IllegalBlockSizeException, ShortBufferException {
        byte[] bArrEngineUpdate;
        byte[] bArrEngineDoFinal;
        int iEngineUpdate;
        if (byteBuffer == null || byteBuffer2 == null) {
            throw new NullPointerException("Input and output buffers must not be null");
        }
        int iPosition = byteBuffer.position();
        int iLimit = byteBuffer.limit();
        int i2 = iLimit - iPosition;
        if (z2 && i2 == 0) {
            return 0;
        }
        int iEngineGetOutputSize = engineGetOutputSize(i2);
        if (byteBuffer2.remaining() < iEngineGetOutputSize) {
            throw new ShortBufferException("Need at least " + iEngineGetOutputSize + " bytes of space in output buffer");
        }
        boolean zHasArray = byteBuffer.hasArray();
        int length = 0;
        if (byteBuffer2.hasArray()) {
            byte[] bArrArray = byteBuffer2.array();
            int iPosition2 = byteBuffer2.position();
            int iArrayOffset = byteBuffer2.arrayOffset() + iPosition2;
            if (zHasArray) {
                byte[] bArrArray2 = byteBuffer.array();
                int iArrayOffset2 = byteBuffer.arrayOffset() + iPosition;
                if (z2) {
                    length = engineUpdate(bArrArray2, iArrayOffset2, i2, bArrArray, iArrayOffset);
                } else {
                    length = engineDoFinal(bArrArray2, iArrayOffset2, i2, bArrArray, iArrayOffset);
                }
                byteBuffer.position(iLimit);
            } else {
                byte[] bArr = new byte[getTempArraySize(i2)];
                do {
                    int iMin = Math.min(i2, bArr.length);
                    if (iMin > 0) {
                        byteBuffer.get(bArr, 0, iMin);
                    }
                    if (z2 || i2 > iMin) {
                        iEngineUpdate = engineUpdate(bArr, 0, iMin, bArrArray, iArrayOffset);
                    } else {
                        iEngineUpdate = engineDoFinal(bArr, 0, iMin, bArrArray, iArrayOffset);
                    }
                    length += iEngineUpdate;
                    iArrayOffset += iEngineUpdate;
                    i2 -= iMin;
                } while (i2 > 0);
            }
            byteBuffer2.position(iPosition2 + length);
        } else if (zHasArray) {
            byte[] bArrArray3 = byteBuffer.array();
            int iArrayOffset3 = byteBuffer.arrayOffset() + iPosition;
            if (z2) {
                bArrEngineDoFinal = engineUpdate(bArrArray3, iArrayOffset3, i2);
            } else {
                bArrEngineDoFinal = engineDoFinal(bArrArray3, iArrayOffset3, i2);
            }
            byteBuffer.position(iLimit);
            if (bArrEngineDoFinal != null && bArrEngineDoFinal.length != 0) {
                byteBuffer2.put(bArrEngineDoFinal);
                length = bArrEngineDoFinal.length;
            }
        } else {
            byte[] bArr2 = new byte[getTempArraySize(i2)];
            do {
                int iMin2 = Math.min(i2, bArr2.length);
                if (iMin2 > 0) {
                    byteBuffer.get(bArr2, 0, iMin2);
                }
                if (z2 || i2 > iMin2) {
                    bArrEngineUpdate = engineUpdate(bArr2, 0, iMin2);
                } else {
                    bArrEngineUpdate = engineDoFinal(bArr2, 0, iMin2);
                }
                if (bArrEngineUpdate != null && bArrEngineUpdate.length != 0) {
                    byteBuffer2.put(bArrEngineUpdate);
                    length += bArrEngineUpdate.length;
                }
                i2 -= iMin2;
            } while (i2 > 0);
        }
        return length;
    }

    protected byte[] engineWrap(Key key) throws IllegalBlockSizeException, InvalidKeyException {
        throw new UnsupportedOperationException();
    }

    protected Key engineUnwrap(byte[] bArr, String str, int i2) throws NoSuchAlgorithmException, InvalidKeyException {
        throw new UnsupportedOperationException();
    }

    protected int engineGetKeySize(Key key) throws InvalidKeyException {
        throw new UnsupportedOperationException();
    }

    protected void engineUpdateAAD(byte[] bArr, int i2, int i3) {
        throw new UnsupportedOperationException("The underlying Cipher implementation does not support this method");
    }

    protected void engineUpdateAAD(ByteBuffer byteBuffer) {
        throw new UnsupportedOperationException("The underlying Cipher implementation does not support this method");
    }
}
