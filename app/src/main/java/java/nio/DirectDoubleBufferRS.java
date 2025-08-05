package java.nio;

import sun.nio.ch.DirectBuffer;

/* loaded from: rt.jar:java/nio/DirectDoubleBufferRS.class */
class DirectDoubleBufferRS extends DirectDoubleBufferS implements DirectBuffer {
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !DirectDoubleBufferRS.class.desiredAssertionStatus();
    }

    DirectDoubleBufferRS(DirectBuffer directBuffer, int i2, int i3, int i4, int i5, int i6) {
        super(directBuffer, i2, i3, i4, i5, i6);
    }

    @Override // java.nio.DirectDoubleBufferS, java.nio.DoubleBuffer
    public DoubleBuffer slice() {
        int iPosition = position();
        int iLimit = limit();
        int i2 = iPosition <= iLimit ? iLimit - iPosition : 0;
        int i3 = iPosition << 3;
        if ($assertionsDisabled || i3 >= 0) {
            return new DirectDoubleBufferRS(this, -1, 0, i2, i2, i3);
        }
        throw new AssertionError();
    }

    @Override // java.nio.DirectDoubleBufferS, java.nio.DoubleBuffer
    public DoubleBuffer duplicate() {
        return new DirectDoubleBufferRS(this, markValue(), position(), limit(), capacity(), 0);
    }

    @Override // java.nio.DirectDoubleBufferS, java.nio.DoubleBuffer
    public DoubleBuffer asReadOnlyBuffer() {
        return duplicate();
    }

    @Override // java.nio.DirectDoubleBufferS, java.nio.DoubleBuffer
    public DoubleBuffer put(double d2) {
        throw new ReadOnlyBufferException();
    }

    @Override // java.nio.DirectDoubleBufferS, java.nio.DoubleBuffer
    public DoubleBuffer put(int i2, double d2) {
        throw new ReadOnlyBufferException();
    }

    @Override // java.nio.DirectDoubleBufferS, java.nio.DoubleBuffer
    public DoubleBuffer put(DoubleBuffer doubleBuffer) {
        throw new ReadOnlyBufferException();
    }

    @Override // java.nio.DirectDoubleBufferS, java.nio.DoubleBuffer
    public DoubleBuffer put(double[] dArr, int i2, int i3) {
        throw new ReadOnlyBufferException();
    }

    @Override // java.nio.DirectDoubleBufferS, java.nio.DoubleBuffer
    public DoubleBuffer compact() {
        throw new ReadOnlyBufferException();
    }

    @Override // java.nio.DirectDoubleBufferS, java.nio.DoubleBuffer, java.nio.Buffer
    public boolean isDirect() {
        return true;
    }

    @Override // java.nio.DirectDoubleBufferS, java.nio.Buffer
    public boolean isReadOnly() {
        return true;
    }

    @Override // java.nio.DirectDoubleBufferS, java.nio.DoubleBuffer
    public ByteOrder order() {
        return ByteOrder.nativeOrder() == ByteOrder.BIG_ENDIAN ? ByteOrder.LITTLE_ENDIAN : ByteOrder.BIG_ENDIAN;
    }
}
