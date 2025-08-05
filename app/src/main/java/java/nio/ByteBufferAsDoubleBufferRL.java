package java.nio;

/* loaded from: rt.jar:java/nio/ByteBufferAsDoubleBufferRL.class */
class ByteBufferAsDoubleBufferRL extends ByteBufferAsDoubleBufferL {
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !ByteBufferAsDoubleBufferRL.class.desiredAssertionStatus();
    }

    ByteBufferAsDoubleBufferRL(ByteBuffer byteBuffer) {
        super(byteBuffer);
    }

    ByteBufferAsDoubleBufferRL(ByteBuffer byteBuffer, int i2, int i3, int i4, int i5, int i6) {
        super(byteBuffer, i2, i3, i4, i5, i6);
    }

    @Override // java.nio.ByteBufferAsDoubleBufferL, java.nio.DoubleBuffer
    public DoubleBuffer slice() {
        int iPosition = position();
        int iLimit = limit();
        int i2 = iPosition <= iLimit ? iLimit - iPosition : 0;
        int i3 = (iPosition << 3) + this.offset;
        if ($assertionsDisabled || i3 >= 0) {
            return new ByteBufferAsDoubleBufferRL(this.f12453bb, -1, 0, i2, i2, i3);
        }
        throw new AssertionError();
    }

    @Override // java.nio.ByteBufferAsDoubleBufferL, java.nio.DoubleBuffer
    public DoubleBuffer duplicate() {
        return new ByteBufferAsDoubleBufferRL(this.f12453bb, markValue(), position(), limit(), capacity(), this.offset);
    }

    @Override // java.nio.ByteBufferAsDoubleBufferL, java.nio.DoubleBuffer
    public DoubleBuffer asReadOnlyBuffer() {
        return duplicate();
    }

    @Override // java.nio.ByteBufferAsDoubleBufferL, java.nio.DoubleBuffer
    public DoubleBuffer put(double d2) {
        throw new ReadOnlyBufferException();
    }

    @Override // java.nio.ByteBufferAsDoubleBufferL, java.nio.DoubleBuffer
    public DoubleBuffer put(int i2, double d2) {
        throw new ReadOnlyBufferException();
    }

    @Override // java.nio.ByteBufferAsDoubleBufferL, java.nio.DoubleBuffer
    public DoubleBuffer compact() {
        throw new ReadOnlyBufferException();
    }

    @Override // java.nio.ByteBufferAsDoubleBufferL, java.nio.DoubleBuffer, java.nio.Buffer
    public boolean isDirect() {
        return this.f12453bb.isDirect();
    }

    @Override // java.nio.ByteBufferAsDoubleBufferL, java.nio.Buffer
    public boolean isReadOnly() {
        return true;
    }

    @Override // java.nio.ByteBufferAsDoubleBufferL, java.nio.DoubleBuffer
    public ByteOrder order() {
        return ByteOrder.LITTLE_ENDIAN;
    }
}
