package java.nio;

import sun.nio.ch.DirectBuffer;

/* loaded from: rt.jar:java/nio/DirectDoubleBufferRU.class */
class DirectDoubleBufferRU extends DirectDoubleBufferU implements DirectBuffer {
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !DirectDoubleBufferRU.class.desiredAssertionStatus();
    }

    DirectDoubleBufferRU(DirectBuffer directBuffer, int i2, int i3, int i4, int i5, int i6) {
        super(directBuffer, i2, i3, i4, i5, i6);
    }

    @Override // java.nio.DirectDoubleBufferU, java.nio.DoubleBuffer
    public DoubleBuffer slice() {
        int iPosition = position();
        int iLimit = limit();
        int i2 = iPosition <= iLimit ? iLimit - iPosition : 0;
        int i3 = iPosition << 3;
        if ($assertionsDisabled || i3 >= 0) {
            return new DirectDoubleBufferRU(this, -1, 0, i2, i2, i3);
        }
        throw new AssertionError();
    }

    @Override // java.nio.DirectDoubleBufferU, java.nio.DoubleBuffer
    public DoubleBuffer duplicate() {
        return new DirectDoubleBufferRU(this, markValue(), position(), limit(), capacity(), 0);
    }

    @Override // java.nio.DirectDoubleBufferU, java.nio.DoubleBuffer
    public DoubleBuffer asReadOnlyBuffer() {
        return duplicate();
    }

    @Override // java.nio.DirectDoubleBufferU, java.nio.DoubleBuffer
    public DoubleBuffer put(double d2) {
        throw new ReadOnlyBufferException();
    }

    @Override // java.nio.DirectDoubleBufferU, java.nio.DoubleBuffer
    public DoubleBuffer put(int i2, double d2) {
        throw new ReadOnlyBufferException();
    }

    @Override // java.nio.DirectDoubleBufferU, java.nio.DoubleBuffer
    public DoubleBuffer put(DoubleBuffer doubleBuffer) {
        throw new ReadOnlyBufferException();
    }

    @Override // java.nio.DirectDoubleBufferU, java.nio.DoubleBuffer
    public DoubleBuffer put(double[] dArr, int i2, int i3) {
        throw new ReadOnlyBufferException();
    }

    @Override // java.nio.DirectDoubleBufferU, java.nio.DoubleBuffer
    public DoubleBuffer compact() {
        throw new ReadOnlyBufferException();
    }

    @Override // java.nio.DirectDoubleBufferU, java.nio.DoubleBuffer, java.nio.Buffer
    public boolean isDirect() {
        return true;
    }

    @Override // java.nio.DirectDoubleBufferU, java.nio.Buffer
    public boolean isReadOnly() {
        return true;
    }

    @Override // java.nio.DirectDoubleBufferU, java.nio.DoubleBuffer
    public ByteOrder order() {
        return ByteOrder.nativeOrder() != ByteOrder.BIG_ENDIAN ? ByteOrder.LITTLE_ENDIAN : ByteOrder.BIG_ENDIAN;
    }
}
