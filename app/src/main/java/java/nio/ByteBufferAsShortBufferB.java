package java.nio;

/* loaded from: rt.jar:java/nio/ByteBufferAsShortBufferB.class */
class ByteBufferAsShortBufferB extends ShortBuffer {

    /* renamed from: bb, reason: collision with root package name */
    protected final ByteBuffer f12460bb;
    protected final int offset;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !ByteBufferAsShortBufferB.class.desiredAssertionStatus();
    }

    ByteBufferAsShortBufferB(ByteBuffer byteBuffer) {
        super(-1, 0, byteBuffer.remaining() >> 1, byteBuffer.remaining() >> 1);
        this.f12460bb = byteBuffer;
        int iCapacity = capacity();
        limit(iCapacity);
        int iPosition = position();
        if (!$assertionsDisabled && iPosition > iCapacity) {
            throw new AssertionError();
        }
        this.offset = iPosition;
    }

    ByteBufferAsShortBufferB(ByteBuffer byteBuffer, int i2, int i3, int i4, int i5, int i6) {
        super(i2, i3, i4, i5);
        this.f12460bb = byteBuffer;
        this.offset = i6;
    }

    @Override // java.nio.ShortBuffer
    public ShortBuffer slice() {
        int iPosition = position();
        int iLimit = limit();
        int i2 = iPosition <= iLimit ? iLimit - iPosition : 0;
        int i3 = (iPosition << 1) + this.offset;
        if ($assertionsDisabled || i3 >= 0) {
            return new ByteBufferAsShortBufferB(this.f12460bb, -1, 0, i2, i2, i3);
        }
        throw new AssertionError();
    }

    @Override // java.nio.ShortBuffer
    public ShortBuffer duplicate() {
        return new ByteBufferAsShortBufferB(this.f12460bb, markValue(), position(), limit(), capacity(), this.offset);
    }

    @Override // java.nio.ShortBuffer
    public ShortBuffer asReadOnlyBuffer() {
        return new ByteBufferAsShortBufferRB(this.f12460bb, markValue(), position(), limit(), capacity(), this.offset);
    }

    protected int ix(int i2) {
        return (i2 << 1) + this.offset;
    }

    @Override // java.nio.ShortBuffer
    public short get() {
        return Bits.getShortB(this.f12460bb, ix(nextGetIndex()));
    }

    @Override // java.nio.ShortBuffer
    public short get(int i2) {
        return Bits.getShortB(this.f12460bb, ix(checkIndex(i2)));
    }

    @Override // java.nio.ShortBuffer
    public ShortBuffer put(short s2) {
        Bits.putShortB(this.f12460bb, ix(nextPutIndex()), s2);
        return this;
    }

    @Override // java.nio.ShortBuffer
    public ShortBuffer put(int i2, short s2) {
        Bits.putShortB(this.f12460bb, ix(checkIndex(i2)), s2);
        return this;
    }

    @Override // java.nio.ShortBuffer
    public ShortBuffer compact() {
        int iPosition = position();
        int iLimit = limit();
        if (!$assertionsDisabled && iPosition > iLimit) {
            throw new AssertionError();
        }
        int i2 = iPosition <= iLimit ? iLimit - iPosition : 0;
        ByteBuffer byteBufferDuplicate = this.f12460bb.duplicate();
        byteBufferDuplicate.limit(ix(iLimit));
        byteBufferDuplicate.position(ix(0));
        ByteBuffer byteBufferSlice = byteBufferDuplicate.slice();
        byteBufferSlice.position(iPosition << 1);
        byteBufferSlice.compact();
        position(i2);
        limit(capacity());
        discardMark();
        return this;
    }

    @Override // java.nio.ShortBuffer, java.nio.Buffer
    public boolean isDirect() {
        return this.f12460bb.isDirect();
    }

    @Override // java.nio.Buffer
    public boolean isReadOnly() {
        return false;
    }

    @Override // java.nio.ShortBuffer
    public ByteOrder order() {
        return ByteOrder.BIG_ENDIAN;
    }
}
