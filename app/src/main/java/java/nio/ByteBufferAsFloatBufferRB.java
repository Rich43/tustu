package java.nio;

/* loaded from: rt.jar:java/nio/ByteBufferAsFloatBufferRB.class */
class ByteBufferAsFloatBufferRB extends ByteBufferAsFloatBufferB {
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !ByteBufferAsFloatBufferRB.class.desiredAssertionStatus();
    }

    ByteBufferAsFloatBufferRB(ByteBuffer byteBuffer) {
        super(byteBuffer);
    }

    ByteBufferAsFloatBufferRB(ByteBuffer byteBuffer, int i2, int i3, int i4, int i5, int i6) {
        super(byteBuffer, i2, i3, i4, i5, i6);
    }

    @Override // java.nio.ByteBufferAsFloatBufferB, java.nio.FloatBuffer
    public FloatBuffer slice() {
        int iPosition = position();
        int iLimit = limit();
        int i2 = iPosition <= iLimit ? iLimit - iPosition : 0;
        int i3 = (iPosition << 2) + this.offset;
        if ($assertionsDisabled || i3 >= 0) {
            return new ByteBufferAsFloatBufferRB(this.f12454bb, -1, 0, i2, i2, i3);
        }
        throw new AssertionError();
    }

    @Override // java.nio.ByteBufferAsFloatBufferB, java.nio.FloatBuffer
    public FloatBuffer duplicate() {
        return new ByteBufferAsFloatBufferRB(this.f12454bb, markValue(), position(), limit(), capacity(), this.offset);
    }

    @Override // java.nio.ByteBufferAsFloatBufferB, java.nio.FloatBuffer
    public FloatBuffer asReadOnlyBuffer() {
        return duplicate();
    }

    @Override // java.nio.ByteBufferAsFloatBufferB, java.nio.FloatBuffer
    public FloatBuffer put(float f2) {
        throw new ReadOnlyBufferException();
    }

    @Override // java.nio.ByteBufferAsFloatBufferB, java.nio.FloatBuffer
    public FloatBuffer put(int i2, float f2) {
        throw new ReadOnlyBufferException();
    }

    @Override // java.nio.ByteBufferAsFloatBufferB, java.nio.FloatBuffer
    public FloatBuffer compact() {
        throw new ReadOnlyBufferException();
    }

    @Override // java.nio.ByteBufferAsFloatBufferB, java.nio.FloatBuffer, java.nio.Buffer
    public boolean isDirect() {
        return this.f12454bb.isDirect();
    }

    @Override // java.nio.ByteBufferAsFloatBufferB, java.nio.Buffer
    public boolean isReadOnly() {
        return true;
    }

    @Override // java.nio.ByteBufferAsFloatBufferB, java.nio.FloatBuffer
    public ByteOrder order() {
        return ByteOrder.BIG_ENDIAN;
    }
}
