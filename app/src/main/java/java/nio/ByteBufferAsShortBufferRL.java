package java.nio;

/* loaded from: rt.jar:java/nio/ByteBufferAsShortBufferRL.class */
class ByteBufferAsShortBufferRL extends ByteBufferAsShortBufferL {
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !ByteBufferAsShortBufferRL.class.desiredAssertionStatus();
    }

    ByteBufferAsShortBufferRL(ByteBuffer byteBuffer) {
        super(byteBuffer);
    }

    ByteBufferAsShortBufferRL(ByteBuffer byteBuffer, int i2, int i3, int i4, int i5, int i6) {
        super(byteBuffer, i2, i3, i4, i5, i6);
    }

    @Override // java.nio.ByteBufferAsShortBufferL, java.nio.ShortBuffer
    public ShortBuffer slice() {
        int iPosition = position();
        int iLimit = limit();
        int i2 = iPosition <= iLimit ? iLimit - iPosition : 0;
        int i3 = (iPosition << 1) + this.offset;
        if ($assertionsDisabled || i3 >= 0) {
            return new ByteBufferAsShortBufferRL(this.f12461bb, -1, 0, i2, i2, i3);
        }
        throw new AssertionError();
    }

    @Override // java.nio.ByteBufferAsShortBufferL, java.nio.ShortBuffer
    public ShortBuffer duplicate() {
        return new ByteBufferAsShortBufferRL(this.f12461bb, markValue(), position(), limit(), capacity(), this.offset);
    }

    @Override // java.nio.ByteBufferAsShortBufferL, java.nio.ShortBuffer
    public ShortBuffer asReadOnlyBuffer() {
        return duplicate();
    }

    @Override // java.nio.ByteBufferAsShortBufferL, java.nio.ShortBuffer
    public ShortBuffer put(short s2) {
        throw new ReadOnlyBufferException();
    }

    @Override // java.nio.ByteBufferAsShortBufferL, java.nio.ShortBuffer
    public ShortBuffer put(int i2, short s2) {
        throw new ReadOnlyBufferException();
    }

    @Override // java.nio.ByteBufferAsShortBufferL, java.nio.ShortBuffer
    public ShortBuffer compact() {
        throw new ReadOnlyBufferException();
    }

    @Override // java.nio.ByteBufferAsShortBufferL, java.nio.ShortBuffer, java.nio.Buffer
    public boolean isDirect() {
        return this.f12461bb.isDirect();
    }

    @Override // java.nio.ByteBufferAsShortBufferL, java.nio.Buffer
    public boolean isReadOnly() {
        return true;
    }

    @Override // java.nio.ByteBufferAsShortBufferL, java.nio.ShortBuffer
    public ByteOrder order() {
        return ByteOrder.LITTLE_ENDIAN;
    }
}
