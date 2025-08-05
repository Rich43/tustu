package java.nio;

/* loaded from: rt.jar:java/nio/ByteBufferAsLongBufferL.class */
class ByteBufferAsLongBufferL extends LongBuffer {

    /* renamed from: bb, reason: collision with root package name */
    protected final ByteBuffer f12459bb;
    protected final int offset;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !ByteBufferAsLongBufferL.class.desiredAssertionStatus();
    }

    ByteBufferAsLongBufferL(ByteBuffer byteBuffer) {
        super(-1, 0, byteBuffer.remaining() >> 3, byteBuffer.remaining() >> 3);
        this.f12459bb = byteBuffer;
        int iCapacity = capacity();
        limit(iCapacity);
        int iPosition = position();
        if (!$assertionsDisabled && iPosition > iCapacity) {
            throw new AssertionError();
        }
        this.offset = iPosition;
    }

    ByteBufferAsLongBufferL(ByteBuffer byteBuffer, int i2, int i3, int i4, int i5, int i6) {
        super(i2, i3, i4, i5);
        this.f12459bb = byteBuffer;
        this.offset = i6;
    }

    @Override // java.nio.LongBuffer
    public LongBuffer slice() {
        int iPosition = position();
        int iLimit = limit();
        int i2 = iPosition <= iLimit ? iLimit - iPosition : 0;
        int i3 = (iPosition << 3) + this.offset;
        if ($assertionsDisabled || i3 >= 0) {
            return new ByteBufferAsLongBufferL(this.f12459bb, -1, 0, i2, i2, i3);
        }
        throw new AssertionError();
    }

    @Override // java.nio.LongBuffer
    public LongBuffer duplicate() {
        return new ByteBufferAsLongBufferL(this.f12459bb, markValue(), position(), limit(), capacity(), this.offset);
    }

    @Override // java.nio.LongBuffer
    public LongBuffer asReadOnlyBuffer() {
        return new ByteBufferAsLongBufferRL(this.f12459bb, markValue(), position(), limit(), capacity(), this.offset);
    }

    protected int ix(int i2) {
        return (i2 << 3) + this.offset;
    }

    @Override // java.nio.LongBuffer
    public long get() {
        return Bits.getLongL(this.f12459bb, ix(nextGetIndex()));
    }

    @Override // java.nio.LongBuffer
    public long get(int i2) {
        return Bits.getLongL(this.f12459bb, ix(checkIndex(i2)));
    }

    @Override // java.nio.LongBuffer
    public LongBuffer put(long j2) {
        Bits.putLongL(this.f12459bb, ix(nextPutIndex()), j2);
        return this;
    }

    @Override // java.nio.LongBuffer
    public LongBuffer put(int i2, long j2) {
        Bits.putLongL(this.f12459bb, ix(checkIndex(i2)), j2);
        return this;
    }

    @Override // java.nio.LongBuffer
    public LongBuffer compact() {
        int iPosition = position();
        int iLimit = limit();
        if (!$assertionsDisabled && iPosition > iLimit) {
            throw new AssertionError();
        }
        int i2 = iPosition <= iLimit ? iLimit - iPosition : 0;
        ByteBuffer byteBufferDuplicate = this.f12459bb.duplicate();
        byteBufferDuplicate.limit(ix(iLimit));
        byteBufferDuplicate.position(ix(0));
        ByteBuffer byteBufferSlice = byteBufferDuplicate.slice();
        byteBufferSlice.position(iPosition << 3);
        byteBufferSlice.compact();
        position(i2);
        limit(capacity());
        discardMark();
        return this;
    }

    @Override // java.nio.LongBuffer, java.nio.Buffer
    public boolean isDirect() {
        return this.f12459bb.isDirect();
    }

    @Override // java.nio.Buffer
    public boolean isReadOnly() {
        return false;
    }

    @Override // java.nio.LongBuffer
    public ByteOrder order() {
        return ByteOrder.LITTLE_ENDIAN;
    }
}
