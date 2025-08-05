package java.nio;

/* loaded from: rt.jar:java/nio/ByteBufferAsLongBufferRB.class */
class ByteBufferAsLongBufferRB extends ByteBufferAsLongBufferB {
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !ByteBufferAsLongBufferRB.class.desiredAssertionStatus();
    }

    ByteBufferAsLongBufferRB(ByteBuffer byteBuffer) {
        super(byteBuffer);
    }

    ByteBufferAsLongBufferRB(ByteBuffer byteBuffer, int i2, int i3, int i4, int i5, int i6) {
        super(byteBuffer, i2, i3, i4, i5, i6);
    }

    @Override // java.nio.ByteBufferAsLongBufferB, java.nio.LongBuffer
    public LongBuffer slice() {
        int iPosition = position();
        int iLimit = limit();
        int i2 = iPosition <= iLimit ? iLimit - iPosition : 0;
        int i3 = (iPosition << 3) + this.offset;
        if ($assertionsDisabled || i3 >= 0) {
            return new ByteBufferAsLongBufferRB(this.f12458bb, -1, 0, i2, i2, i3);
        }
        throw new AssertionError();
    }

    @Override // java.nio.ByteBufferAsLongBufferB, java.nio.LongBuffer
    public LongBuffer duplicate() {
        return new ByteBufferAsLongBufferRB(this.f12458bb, markValue(), position(), limit(), capacity(), this.offset);
    }

    @Override // java.nio.ByteBufferAsLongBufferB, java.nio.LongBuffer
    public LongBuffer asReadOnlyBuffer() {
        return duplicate();
    }

    @Override // java.nio.ByteBufferAsLongBufferB, java.nio.LongBuffer
    public LongBuffer put(long j2) {
        throw new ReadOnlyBufferException();
    }

    @Override // java.nio.ByteBufferAsLongBufferB, java.nio.LongBuffer
    public LongBuffer put(int i2, long j2) {
        throw new ReadOnlyBufferException();
    }

    @Override // java.nio.ByteBufferAsLongBufferB, java.nio.LongBuffer
    public LongBuffer compact() {
        throw new ReadOnlyBufferException();
    }

    @Override // java.nio.ByteBufferAsLongBufferB, java.nio.LongBuffer, java.nio.Buffer
    public boolean isDirect() {
        return this.f12458bb.isDirect();
    }

    @Override // java.nio.ByteBufferAsLongBufferB, java.nio.Buffer
    public boolean isReadOnly() {
        return true;
    }

    @Override // java.nio.ByteBufferAsLongBufferB, java.nio.LongBuffer
    public ByteOrder order() {
        return ByteOrder.BIG_ENDIAN;
    }
}
