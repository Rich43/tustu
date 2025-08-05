package java.security;

import java.nio.ByteBuffer;
import sun.security.jca.JCAUtil;

/* loaded from: rt.jar:java/security/MessageDigestSpi.class */
public abstract class MessageDigestSpi {
    private byte[] tempArray;

    protected abstract void engineUpdate(byte b2);

    protected abstract void engineUpdate(byte[] bArr, int i2, int i3);

    protected abstract byte[] engineDigest();

    protected abstract void engineReset();

    protected int engineGetDigestLength() {
        return 0;
    }

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
        int tempArraySize = JCAUtil.getTempArraySize(iRemaining);
        if (this.tempArray == null || tempArraySize > this.tempArray.length) {
            this.tempArray = new byte[tempArraySize];
        }
        while (iRemaining > 0) {
            int iMin = Math.min(iRemaining, this.tempArray.length);
            byteBuffer.get(this.tempArray, 0, iMin);
            engineUpdate(this.tempArray, 0, iMin);
            iRemaining -= iMin;
        }
    }

    protected int engineDigest(byte[] bArr, int i2, int i3) throws DigestException {
        byte[] bArrEngineDigest = engineDigest();
        if (i3 < bArrEngineDigest.length) {
            throw new DigestException("partial digests not returned");
        }
        if (bArr.length - i2 < bArrEngineDigest.length) {
            throw new DigestException("insufficient space in the output buffer to store the digest");
        }
        System.arraycopy(bArrEngineDigest, 0, bArr, i2, bArrEngineDigest.length);
        return bArrEngineDigest.length;
    }

    public Object clone() throws CloneNotSupportedException {
        if (this instanceof Cloneable) {
            return super.clone();
        }
        throw new CloneNotSupportedException();
    }
}
