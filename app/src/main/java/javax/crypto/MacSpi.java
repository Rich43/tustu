package javax.crypto;

import java.nio.ByteBuffer;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.spec.AlgorithmParameterSpec;

/* loaded from: jce.jar:javax/crypto/MacSpi.class */
public abstract class MacSpi {
    protected abstract int engineGetMacLength();

    protected abstract void engineInit(Key key, AlgorithmParameterSpec algorithmParameterSpec) throws InvalidKeyException, InvalidAlgorithmParameterException;

    protected abstract void engineUpdate(byte b2);

    protected abstract void engineUpdate(byte[] bArr, int i2, int i3);

    protected abstract byte[] engineDoFinal();

    protected abstract void engineReset();

    protected void engineUpdate(ByteBuffer byteBuffer) {
        if (!byteBuffer.hasRemaining()) {
            return;
        }
        if (byteBuffer.hasArray()) {
            byte[] bArrArray = byteBuffer.array();
            int iArrayOffset = byteBuffer.arrayOffset();
            int iPosition = byteBuffer.position();
            int iLimit = byteBuffer.limit();
            engineUpdate(bArrArray, iArrayOffset + iPosition, iLimit - iPosition);
            byteBuffer.position(iLimit);
            return;
        }
        int iRemaining = byteBuffer.remaining();
        byte[] bArr = new byte[CipherSpi.getTempArraySize(iRemaining)];
        while (iRemaining > 0) {
            int iMin = Math.min(iRemaining, bArr.length);
            byteBuffer.get(bArr, 0, iMin);
            engineUpdate(bArr, 0, iMin);
            iRemaining -= iMin;
        }
    }

    public Object clone() throws CloneNotSupportedException {
        if (this instanceof Cloneable) {
            return super.clone();
        }
        throw new CloneNotSupportedException();
    }
}
