package java.nio;

import sun.nio.ch.DirectBuffer;

/* loaded from: rt.jar:java/nio/DirectFloatBufferRU.class */
class DirectFloatBufferRU extends DirectFloatBufferU implements DirectBuffer {
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !DirectFloatBufferRU.class.desiredAssertionStatus();
    }

    DirectFloatBufferRU(DirectBuffer directBuffer, int i2, int i3, int i4, int i5, int i6) {
        super(directBuffer, i2, i3, i4, i5, i6);
    }

    @Override // java.nio.DirectFloatBufferU, java.nio.FloatBuffer
    public FloatBuffer slice() {
        int iPosition = position();
        int iLimit = limit();
        int i2 = iPosition <= iLimit ? iLimit - iPosition : 0;
        int i3 = iPosition << 2;
        if ($assertionsDisabled || i3 >= 0) {
            return new DirectFloatBufferRU(this, -1, 0, i2, i2, i3);
        }
        throw new AssertionError();
    }

    @Override // java.nio.DirectFloatBufferU, java.nio.FloatBuffer
    public FloatBuffer duplicate() {
        return new DirectFloatBufferRU(this, markValue(), position(), limit(), capacity(), 0);
    }

    @Override // java.nio.DirectFloatBufferU, java.nio.FloatBuffer
    public FloatBuffer asReadOnlyBuffer() {
        return duplicate();
    }

    @Override // java.nio.DirectFloatBufferU, java.nio.FloatBuffer
    public FloatBuffer put(float f2) {
        throw new ReadOnlyBufferException();
    }

    @Override // java.nio.DirectFloatBufferU, java.nio.FloatBuffer
    public FloatBuffer put(int i2, float f2) {
        throw new ReadOnlyBufferException();
    }

    @Override // java.nio.DirectFloatBufferU, java.nio.FloatBuffer
    public FloatBuffer put(FloatBuffer floatBuffer) {
        throw new ReadOnlyBufferException();
    }

    @Override // java.nio.DirectFloatBufferU, java.nio.FloatBuffer
    public FloatBuffer put(float[] fArr, int i2, int i3) {
        throw new ReadOnlyBufferException();
    }

    @Override // java.nio.DirectFloatBufferU, java.nio.FloatBuffer
    public FloatBuffer compact() {
        throw new ReadOnlyBufferException();
    }

    @Override // java.nio.DirectFloatBufferU, java.nio.FloatBuffer, java.nio.Buffer
    public boolean isDirect() {
        return true;
    }

    @Override // java.nio.DirectFloatBufferU, java.nio.Buffer
    public boolean isReadOnly() {
        return true;
    }

    @Override // java.nio.DirectFloatBufferU, java.nio.FloatBuffer
    public ByteOrder order() {
        return ByteOrder.nativeOrder() != ByteOrder.BIG_ENDIAN ? ByteOrder.LITTLE_ENDIAN : ByteOrder.BIG_ENDIAN;
    }
}
