package java.nio;

/* loaded from: rt.jar:java/nio/ByteBufferAsIntBufferRB.class */
class ByteBufferAsIntBufferRB extends ByteBufferAsIntBufferB {
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !ByteBufferAsIntBufferRB.class.desiredAssertionStatus();
    }

    ByteBufferAsIntBufferRB(ByteBuffer byteBuffer) {
        super(byteBuffer);
    }

    ByteBufferAsIntBufferRB(ByteBuffer byteBuffer, int i2, int i3, int i4, int i5, int i6) {
        super(byteBuffer, i2, i3, i4, i5, i6);
    }

    @Override // java.nio.ByteBufferAsIntBufferB, java.nio.IntBuffer
    public IntBuffer slice() {
        int iPosition = position();
        int iLimit = limit();
        int i2 = iPosition <= iLimit ? iLimit - iPosition : 0;
        int i3 = (iPosition << 2) + this.offset;
        if ($assertionsDisabled || i3 >= 0) {
            return new ByteBufferAsIntBufferRB(this.f12456bb, -1, 0, i2, i2, i3);
        }
        throw new AssertionError();
    }

    @Override // java.nio.ByteBufferAsIntBufferB, java.nio.IntBuffer
    public IntBuffer duplicate() {
        return new ByteBufferAsIntBufferRB(this.f12456bb, markValue(), position(), limit(), capacity(), this.offset);
    }

    @Override // java.nio.ByteBufferAsIntBufferB, java.nio.IntBuffer
    public IntBuffer asReadOnlyBuffer() {
        return duplicate();
    }

    @Override // java.nio.ByteBufferAsIntBufferB, java.nio.IntBuffer
    public IntBuffer put(int i2) {
        throw new ReadOnlyBufferException();
    }

    @Override // java.nio.ByteBufferAsIntBufferB, java.nio.IntBuffer
    public IntBuffer put(int i2, int i3) {
        throw new ReadOnlyBufferException();
    }

    @Override // java.nio.ByteBufferAsIntBufferB, java.nio.IntBuffer
    public IntBuffer compact() {
        throw new ReadOnlyBufferException();
    }

    @Override // java.nio.ByteBufferAsIntBufferB, java.nio.IntBuffer, java.nio.Buffer
    public boolean isDirect() {
        return this.f12456bb.isDirect();
    }

    @Override // java.nio.ByteBufferAsIntBufferB, java.nio.Buffer
    public boolean isReadOnly() {
        return true;
    }

    @Override // java.nio.ByteBufferAsIntBufferB, java.nio.IntBuffer
    public ByteOrder order() {
        return ByteOrder.BIG_ENDIAN;
    }
}
