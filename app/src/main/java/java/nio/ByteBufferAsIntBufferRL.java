package java.nio;

/* loaded from: rt.jar:java/nio/ByteBufferAsIntBufferRL.class */
class ByteBufferAsIntBufferRL extends ByteBufferAsIntBufferL {
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !ByteBufferAsIntBufferRL.class.desiredAssertionStatus();
    }

    ByteBufferAsIntBufferRL(ByteBuffer byteBuffer) {
        super(byteBuffer);
    }

    ByteBufferAsIntBufferRL(ByteBuffer byteBuffer, int i2, int i3, int i4, int i5, int i6) {
        super(byteBuffer, i2, i3, i4, i5, i6);
    }

    @Override // java.nio.ByteBufferAsIntBufferL, java.nio.IntBuffer
    public IntBuffer slice() {
        int iPosition = position();
        int iLimit = limit();
        int i2 = iPosition <= iLimit ? iLimit - iPosition : 0;
        int i3 = (iPosition << 2) + this.offset;
        if ($assertionsDisabled || i3 >= 0) {
            return new ByteBufferAsIntBufferRL(this.f12457bb, -1, 0, i2, i2, i3);
        }
        throw new AssertionError();
    }

    @Override // java.nio.ByteBufferAsIntBufferL, java.nio.IntBuffer
    public IntBuffer duplicate() {
        return new ByteBufferAsIntBufferRL(this.f12457bb, markValue(), position(), limit(), capacity(), this.offset);
    }

    @Override // java.nio.ByteBufferAsIntBufferL, java.nio.IntBuffer
    public IntBuffer asReadOnlyBuffer() {
        return duplicate();
    }

    @Override // java.nio.ByteBufferAsIntBufferL, java.nio.IntBuffer
    public IntBuffer put(int i2) {
        throw new ReadOnlyBufferException();
    }

    @Override // java.nio.ByteBufferAsIntBufferL, java.nio.IntBuffer
    public IntBuffer put(int i2, int i3) {
        throw new ReadOnlyBufferException();
    }

    @Override // java.nio.ByteBufferAsIntBufferL, java.nio.IntBuffer
    public IntBuffer compact() {
        throw new ReadOnlyBufferException();
    }

    @Override // java.nio.ByteBufferAsIntBufferL, java.nio.IntBuffer, java.nio.Buffer
    public boolean isDirect() {
        return this.f12457bb.isDirect();
    }

    @Override // java.nio.ByteBufferAsIntBufferL, java.nio.Buffer
    public boolean isReadOnly() {
        return true;
    }

    @Override // java.nio.ByteBufferAsIntBufferL, java.nio.IntBuffer
    public ByteOrder order() {
        return ByteOrder.LITTLE_ENDIAN;
    }
}
