package java.nio;

/* loaded from: rt.jar:java/nio/ByteBufferAsDoubleBufferB.class */
class ByteBufferAsDoubleBufferB extends DoubleBuffer {

    /* renamed from: bb, reason: collision with root package name */
    protected final ByteBuffer f12452bb;
    protected final int offset;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !ByteBufferAsDoubleBufferB.class.desiredAssertionStatus();
    }

    ByteBufferAsDoubleBufferB(ByteBuffer byteBuffer) {
        super(-1, 0, byteBuffer.remaining() >> 3, byteBuffer.remaining() >> 3);
        this.f12452bb = byteBuffer;
        int iCapacity = capacity();
        limit(iCapacity);
        int iPosition = position();
        if (!$assertionsDisabled && iPosition > iCapacity) {
            throw new AssertionError();
        }
        this.offset = iPosition;
    }

    ByteBufferAsDoubleBufferB(ByteBuffer byteBuffer, int i2, int i3, int i4, int i5, int i6) {
        super(i2, i3, i4, i5);
        this.f12452bb = byteBuffer;
        this.offset = i6;
    }

    @Override // java.nio.DoubleBuffer
    public DoubleBuffer slice() {
        int iPosition = position();
        int iLimit = limit();
        int i2 = iPosition <= iLimit ? iLimit - iPosition : 0;
        int i3 = (iPosition << 3) + this.offset;
        if ($assertionsDisabled || i3 >= 0) {
            return new ByteBufferAsDoubleBufferB(this.f12452bb, -1, 0, i2, i2, i3);
        }
        throw new AssertionError();
    }

    @Override // java.nio.DoubleBuffer
    public DoubleBuffer duplicate() {
        return new ByteBufferAsDoubleBufferB(this.f12452bb, markValue(), position(), limit(), capacity(), this.offset);
    }

    @Override // java.nio.DoubleBuffer
    public DoubleBuffer asReadOnlyBuffer() {
        return new ByteBufferAsDoubleBufferRB(this.f12452bb, markValue(), position(), limit(), capacity(), this.offset);
    }

    protected int ix(int i2) {
        return (i2 << 3) + this.offset;
    }

    @Override // java.nio.DoubleBuffer
    public double get() {
        return Bits.getDoubleB(this.f12452bb, ix(nextGetIndex()));
    }

    @Override // java.nio.DoubleBuffer
    public double get(int i2) {
        return Bits.getDoubleB(this.f12452bb, ix(checkIndex(i2)));
    }

    @Override // java.nio.DoubleBuffer
    public DoubleBuffer put(double d2) {
        Bits.putDoubleB(this.f12452bb, ix(nextPutIndex()), d2);
        return this;
    }

    @Override // java.nio.DoubleBuffer
    public DoubleBuffer put(int i2, double d2) {
        Bits.putDoubleB(this.f12452bb, ix(checkIndex(i2)), d2);
        return this;
    }

    @Override // java.nio.DoubleBuffer
    public DoubleBuffer compact() {
        int iPosition = position();
        int iLimit = limit();
        if (!$assertionsDisabled && iPosition > iLimit) {
            throw new AssertionError();
        }
        int i2 = iPosition <= iLimit ? iLimit - iPosition : 0;
        ByteBuffer byteBufferDuplicate = this.f12452bb.duplicate();
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

    @Override // java.nio.DoubleBuffer, java.nio.Buffer
    public boolean isDirect() {
        return this.f12452bb.isDirect();
    }

    @Override // java.nio.Buffer
    public boolean isReadOnly() {
        return false;
    }

    @Override // java.nio.DoubleBuffer
    public ByteOrder order() {
        return ByteOrder.BIG_ENDIAN;
    }
}
