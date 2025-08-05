package java.nio;

/* loaded from: rt.jar:java/nio/ByteBufferAsIntBufferL.class */
class ByteBufferAsIntBufferL extends IntBuffer {

    /* renamed from: bb, reason: collision with root package name */
    protected final ByteBuffer f12457bb;
    protected final int offset;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !ByteBufferAsIntBufferL.class.desiredAssertionStatus();
    }

    ByteBufferAsIntBufferL(ByteBuffer byteBuffer) {
        super(-1, 0, byteBuffer.remaining() >> 2, byteBuffer.remaining() >> 2);
        this.f12457bb = byteBuffer;
        int iCapacity = capacity();
        limit(iCapacity);
        int iPosition = position();
        if (!$assertionsDisabled && iPosition > iCapacity) {
            throw new AssertionError();
        }
        this.offset = iPosition;
    }

    ByteBufferAsIntBufferL(ByteBuffer byteBuffer, int i2, int i3, int i4, int i5, int i6) {
        super(i2, i3, i4, i5);
        this.f12457bb = byteBuffer;
        this.offset = i6;
    }

    @Override // java.nio.IntBuffer
    public IntBuffer slice() {
        int iPosition = position();
        int iLimit = limit();
        int i2 = iPosition <= iLimit ? iLimit - iPosition : 0;
        int i3 = (iPosition << 2) + this.offset;
        if ($assertionsDisabled || i3 >= 0) {
            return new ByteBufferAsIntBufferL(this.f12457bb, -1, 0, i2, i2, i3);
        }
        throw new AssertionError();
    }

    @Override // java.nio.IntBuffer
    public IntBuffer duplicate() {
        return new ByteBufferAsIntBufferL(this.f12457bb, markValue(), position(), limit(), capacity(), this.offset);
    }

    @Override // java.nio.IntBuffer
    public IntBuffer asReadOnlyBuffer() {
        return new ByteBufferAsIntBufferRL(this.f12457bb, markValue(), position(), limit(), capacity(), this.offset);
    }

    protected int ix(int i2) {
        return (i2 << 2) + this.offset;
    }

    @Override // java.nio.IntBuffer
    public int get() {
        return Bits.getIntL(this.f12457bb, ix(nextGetIndex()));
    }

    @Override // java.nio.IntBuffer
    public int get(int i2) {
        return Bits.getIntL(this.f12457bb, ix(checkIndex(i2)));
    }

    @Override // java.nio.IntBuffer
    public IntBuffer put(int i2) {
        Bits.putIntL(this.f12457bb, ix(nextPutIndex()), i2);
        return this;
    }

    @Override // java.nio.IntBuffer
    public IntBuffer put(int i2, int i3) {
        Bits.putIntL(this.f12457bb, ix(checkIndex(i2)), i3);
        return this;
    }

    @Override // java.nio.IntBuffer
    public IntBuffer compact() {
        int iPosition = position();
        int iLimit = limit();
        if (!$assertionsDisabled && iPosition > iLimit) {
            throw new AssertionError();
        }
        int i2 = iPosition <= iLimit ? iLimit - iPosition : 0;
        ByteBuffer byteBufferDuplicate = this.f12457bb.duplicate();
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

    @Override // java.nio.IntBuffer, java.nio.Buffer
    public boolean isDirect() {
        return this.f12457bb.isDirect();
    }

    @Override // java.nio.Buffer
    public boolean isReadOnly() {
        return false;
    }

    @Override // java.nio.IntBuffer
    public ByteOrder order() {
        return ByteOrder.LITTLE_ENDIAN;
    }
}
