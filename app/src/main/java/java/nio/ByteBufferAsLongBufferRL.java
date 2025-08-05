package java.nio;

/* loaded from: rt.jar:java/nio/ByteBufferAsLongBufferRL.class */
class ByteBufferAsLongBufferRL extends ByteBufferAsLongBufferL {
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !ByteBufferAsLongBufferRL.class.desiredAssertionStatus();
    }

    ByteBufferAsLongBufferRL(ByteBuffer byteBuffer) {
        super(byteBuffer);
    }

    ByteBufferAsLongBufferRL(ByteBuffer byteBuffer, int i2, int i3, int i4, int i5, int i6) {
        super(byteBuffer, i2, i3, i4, i5, i6);
    }

    @Override // java.nio.ByteBufferAsLongBufferL, java.nio.LongBuffer
    public LongBuffer slice() {
        int iPosition = position();
        int iLimit = limit();
        int i2 = iPosition <= iLimit ? iLimit - iPosition : 0;
        int i3 = (iPosition << 3) + this.offset;
        if ($assertionsDisabled || i3 >= 0) {
            return new ByteBufferAsLongBufferRL(this.f12459bb, -1, 0, i2, i2, i3);
        }
        throw new AssertionError();
    }

    @Override // java.nio.ByteBufferAsLongBufferL, java.nio.LongBuffer
    public LongBuffer duplicate() {
        return new ByteBufferAsLongBufferRL(this.f12459bb, markValue(), position(), limit(), capacity(), this.offset);
    }

    @Override // java.nio.ByteBufferAsLongBufferL, java.nio.LongBuffer
    public LongBuffer asReadOnlyBuffer() {
        return duplicate();
    }

    @Override // java.nio.ByteBufferAsLongBufferL, java.nio.LongBuffer
    public LongBuffer put(long j2) {
        throw new ReadOnlyBufferException();
    }

    @Override // java.nio.ByteBufferAsLongBufferL, java.nio.LongBuffer
    public LongBuffer put(int i2, long j2) {
        throw new ReadOnlyBufferException();
    }

    @Override // java.nio.ByteBufferAsLongBufferL, java.nio.LongBuffer
    public LongBuffer compact() {
        throw new ReadOnlyBufferException();
    }

    @Override // java.nio.ByteBufferAsLongBufferL, java.nio.LongBuffer, java.nio.Buffer
    public boolean isDirect() {
        return this.f12459bb.isDirect();
    }

    @Override // java.nio.ByteBufferAsLongBufferL, java.nio.Buffer
    public boolean isReadOnly() {
        return true;
    }

    @Override // java.nio.ByteBufferAsLongBufferL, java.nio.LongBuffer
    public ByteOrder order() {
        return ByteOrder.LITTLE_ENDIAN;
    }
}
