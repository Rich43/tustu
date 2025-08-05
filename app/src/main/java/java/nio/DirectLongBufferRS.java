package java.nio;

import sun.nio.ch.DirectBuffer;

/* loaded from: rt.jar:java/nio/DirectLongBufferRS.class */
class DirectLongBufferRS extends DirectLongBufferS implements DirectBuffer {
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !DirectLongBufferRS.class.desiredAssertionStatus();
    }

    DirectLongBufferRS(DirectBuffer directBuffer, int i2, int i3, int i4, int i5, int i6) {
        super(directBuffer, i2, i3, i4, i5, i6);
    }

    @Override // java.nio.DirectLongBufferS, java.nio.LongBuffer
    public LongBuffer slice() {
        int iPosition = position();
        int iLimit = limit();
        int i2 = iPosition <= iLimit ? iLimit - iPosition : 0;
        int i3 = iPosition << 3;
        if ($assertionsDisabled || i3 >= 0) {
            return new DirectLongBufferRS(this, -1, 0, i2, i2, i3);
        }
        throw new AssertionError();
    }

    @Override // java.nio.DirectLongBufferS, java.nio.LongBuffer
    public LongBuffer duplicate() {
        return new DirectLongBufferRS(this, markValue(), position(), limit(), capacity(), 0);
    }

    @Override // java.nio.DirectLongBufferS, java.nio.LongBuffer
    public LongBuffer asReadOnlyBuffer() {
        return duplicate();
    }

    @Override // java.nio.DirectLongBufferS, java.nio.LongBuffer
    public LongBuffer put(long j2) {
        throw new ReadOnlyBufferException();
    }

    @Override // java.nio.DirectLongBufferS, java.nio.LongBuffer
    public LongBuffer put(int i2, long j2) {
        throw new ReadOnlyBufferException();
    }

    @Override // java.nio.DirectLongBufferS, java.nio.LongBuffer
    public LongBuffer put(LongBuffer longBuffer) {
        throw new ReadOnlyBufferException();
    }

    @Override // java.nio.DirectLongBufferS, java.nio.LongBuffer
    public LongBuffer put(long[] jArr, int i2, int i3) {
        throw new ReadOnlyBufferException();
    }

    @Override // java.nio.DirectLongBufferS, java.nio.LongBuffer
    public LongBuffer compact() {
        throw new ReadOnlyBufferException();
    }

    @Override // java.nio.DirectLongBufferS, java.nio.LongBuffer, java.nio.Buffer
    public boolean isDirect() {
        return true;
    }

    @Override // java.nio.DirectLongBufferS, java.nio.Buffer
    public boolean isReadOnly() {
        return true;
    }

    @Override // java.nio.DirectLongBufferS, java.nio.LongBuffer
    public ByteOrder order() {
        return ByteOrder.nativeOrder() == ByteOrder.BIG_ENDIAN ? ByteOrder.LITTLE_ENDIAN : ByteOrder.BIG_ENDIAN;
    }
}
