package java.nio;

/* loaded from: rt.jar:java/nio/ByteBufferAsFloatBufferRL.class */
class ByteBufferAsFloatBufferRL extends ByteBufferAsFloatBufferL {
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !ByteBufferAsFloatBufferRL.class.desiredAssertionStatus();
    }

    ByteBufferAsFloatBufferRL(ByteBuffer byteBuffer) {
        super(byteBuffer);
    }

    ByteBufferAsFloatBufferRL(ByteBuffer byteBuffer, int i2, int i3, int i4, int i5, int i6) {
        super(byteBuffer, i2, i3, i4, i5, i6);
    }

    @Override // java.nio.ByteBufferAsFloatBufferL, java.nio.FloatBuffer
    public FloatBuffer slice() {
        int iPosition = position();
        int iLimit = limit();
        int i2 = iPosition <= iLimit ? iLimit - iPosition : 0;
        int i3 = (iPosition << 2) + this.offset;
        if ($assertionsDisabled || i3 >= 0) {
            return new ByteBufferAsFloatBufferRL(this.f12455bb, -1, 0, i2, i2, i3);
        }
        throw new AssertionError();
    }

    @Override // java.nio.ByteBufferAsFloatBufferL, java.nio.FloatBuffer
    public FloatBuffer duplicate() {
        return new ByteBufferAsFloatBufferRL(this.f12455bb, markValue(), position(), limit(), capacity(), this.offset);
    }

    @Override // java.nio.ByteBufferAsFloatBufferL, java.nio.FloatBuffer
    public FloatBuffer asReadOnlyBuffer() {
        return duplicate();
    }

    @Override // java.nio.ByteBufferAsFloatBufferL, java.nio.FloatBuffer
    public FloatBuffer put(float f2) {
        throw new ReadOnlyBufferException();
    }

    @Override // java.nio.ByteBufferAsFloatBufferL, java.nio.FloatBuffer
    public FloatBuffer put(int i2, float f2) {
        throw new ReadOnlyBufferException();
    }

    @Override // java.nio.ByteBufferAsFloatBufferL, java.nio.FloatBuffer
    public FloatBuffer compact() {
        throw new ReadOnlyBufferException();
    }

    @Override // java.nio.ByteBufferAsFloatBufferL, java.nio.FloatBuffer, java.nio.Buffer
    public boolean isDirect() {
        return this.f12455bb.isDirect();
    }

    @Override // java.nio.ByteBufferAsFloatBufferL, java.nio.Buffer
    public boolean isReadOnly() {
        return true;
    }

    @Override // java.nio.ByteBufferAsFloatBufferL, java.nio.FloatBuffer
    public ByteOrder order() {
        return ByteOrder.LITTLE_ENDIAN;
    }
}
