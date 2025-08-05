package java.nio;

/* loaded from: rt.jar:java/nio/ByteBufferAsFloatBufferL.class */
class ByteBufferAsFloatBufferL extends FloatBuffer {

    /* renamed from: bb, reason: collision with root package name */
    protected final ByteBuffer f12455bb;
    protected final int offset;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !ByteBufferAsFloatBufferL.class.desiredAssertionStatus();
    }

    ByteBufferAsFloatBufferL(ByteBuffer byteBuffer) {
        super(-1, 0, byteBuffer.remaining() >> 2, byteBuffer.remaining() >> 2);
        this.f12455bb = byteBuffer;
        int iCapacity = capacity();
        limit(iCapacity);
        int iPosition = position();
        if (!$assertionsDisabled && iPosition > iCapacity) {
            throw new AssertionError();
        }
        this.offset = iPosition;
    }

    ByteBufferAsFloatBufferL(ByteBuffer byteBuffer, int i2, int i3, int i4, int i5, int i6) {
        super(i2, i3, i4, i5);
        this.f12455bb = byteBuffer;
        this.offset = i6;
    }

    @Override // java.nio.FloatBuffer
    public FloatBuffer slice() {
        int iPosition = position();
        int iLimit = limit();
        int i2 = iPosition <= iLimit ? iLimit - iPosition : 0;
        int i3 = (iPosition << 2) + this.offset;
        if ($assertionsDisabled || i3 >= 0) {
            return new ByteBufferAsFloatBufferL(this.f12455bb, -1, 0, i2, i2, i3);
        }
        throw new AssertionError();
    }

    @Override // java.nio.FloatBuffer
    public FloatBuffer duplicate() {
        return new ByteBufferAsFloatBufferL(this.f12455bb, markValue(), position(), limit(), capacity(), this.offset);
    }

    @Override // java.nio.FloatBuffer
    public FloatBuffer asReadOnlyBuffer() {
        return new ByteBufferAsFloatBufferRL(this.f12455bb, markValue(), position(), limit(), capacity(), this.offset);
    }

    protected int ix(int i2) {
        return (i2 << 2) + this.offset;
    }

    @Override // java.nio.FloatBuffer
    public float get() {
        return Bits.getFloatL(this.f12455bb, ix(nextGetIndex()));
    }

    @Override // java.nio.FloatBuffer
    public float get(int i2) {
        return Bits.getFloatL(this.f12455bb, ix(checkIndex(i2)));
    }

    @Override // java.nio.FloatBuffer
    public FloatBuffer put(float f2) {
        Bits.putFloatL(this.f12455bb, ix(nextPutIndex()), f2);
        return this;
    }

    @Override // java.nio.FloatBuffer
    public FloatBuffer put(int i2, float f2) {
        Bits.putFloatL(this.f12455bb, ix(checkIndex(i2)), f2);
        return this;
    }

    @Override // java.nio.FloatBuffer
    public FloatBuffer compact() {
        int iPosition = position();
        int iLimit = limit();
        if (!$assertionsDisabled && iPosition > iLimit) {
            throw new AssertionError();
        }
        int i2 = iPosition <= iLimit ? iLimit - iPosition : 0;
        ByteBuffer byteBufferDuplicate = this.f12455bb.duplicate();
        byteBufferDuplicate.limit(ix(iLimit));
        byteBufferDuplicate.position(ix(0));
        ByteBuffer byteBufferSlice = byteBufferDuplicate.slice();
        byteBufferSlice.position(iPosition << 2);
        byteBufferSlice.compact();
        position(i2);
        limit(capacity());
        discardMark();
        return this;
    }

    @Override // java.nio.FloatBuffer, java.nio.Buffer
    public boolean isDirect() {
        return this.f12455bb.isDirect();
    }

    @Override // java.nio.Buffer
    public boolean isReadOnly() {
        return false;
    }

    @Override // java.nio.FloatBuffer
    public ByteOrder order() {
        return ByteOrder.LITTLE_ENDIAN;
    }
}
