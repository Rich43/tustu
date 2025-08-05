package java.nio;

/* loaded from: rt.jar:java/nio/ByteBufferAsCharBufferL.class */
class ByteBufferAsCharBufferL extends CharBuffer {

    /* renamed from: bb, reason: collision with root package name */
    protected final ByteBuffer f12451bb;
    protected final int offset;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !ByteBufferAsCharBufferL.class.desiredAssertionStatus();
    }

    ByteBufferAsCharBufferL(ByteBuffer byteBuffer) {
        super(-1, 0, byteBuffer.remaining() >> 1, byteBuffer.remaining() >> 1);
        this.f12451bb = byteBuffer;
        int iCapacity = capacity();
        limit(iCapacity);
        int iPosition = position();
        if (!$assertionsDisabled && iPosition > iCapacity) {
            throw new AssertionError();
        }
        this.offset = iPosition;
    }

    ByteBufferAsCharBufferL(ByteBuffer byteBuffer, int i2, int i3, int i4, int i5, int i6) {
        super(i2, i3, i4, i5);
        this.f12451bb = byteBuffer;
        this.offset = i6;
    }

    @Override // java.nio.CharBuffer
    public CharBuffer slice() {
        int iPosition = position();
        int iLimit = limit();
        int i2 = iPosition <= iLimit ? iLimit - iPosition : 0;
        int i3 = (iPosition << 1) + this.offset;
        if ($assertionsDisabled || i3 >= 0) {
            return new ByteBufferAsCharBufferL(this.f12451bb, -1, 0, i2, i2, i3);
        }
        throw new AssertionError();
    }

    @Override // java.nio.CharBuffer
    public CharBuffer duplicate() {
        return new ByteBufferAsCharBufferL(this.f12451bb, markValue(), position(), limit(), capacity(), this.offset);
    }

    @Override // java.nio.CharBuffer
    public CharBuffer asReadOnlyBuffer() {
        return new ByteBufferAsCharBufferRL(this.f12451bb, markValue(), position(), limit(), capacity(), this.offset);
    }

    protected int ix(int i2) {
        return (i2 << 1) + this.offset;
    }

    @Override // java.nio.CharBuffer
    public char get() {
        return Bits.getCharL(this.f12451bb, ix(nextGetIndex()));
    }

    @Override // java.nio.CharBuffer
    public char get(int i2) {
        return Bits.getCharL(this.f12451bb, ix(checkIndex(i2)));
    }

    @Override // java.nio.CharBuffer
    char getUnchecked(int i2) {
        return Bits.getCharL(this.f12451bb, ix(i2));
    }

    @Override // java.nio.CharBuffer
    public CharBuffer put(char c2) {
        Bits.putCharL(this.f12451bb, ix(nextPutIndex()), c2);
        return this;
    }

    @Override // java.nio.CharBuffer
    public CharBuffer put(int i2, char c2) {
        Bits.putCharL(this.f12451bb, ix(checkIndex(i2)), c2);
        return this;
    }

    @Override // java.nio.CharBuffer
    public CharBuffer compact() {
        int iPosition = position();
        int iLimit = limit();
        if (!$assertionsDisabled && iPosition > iLimit) {
            throw new AssertionError();
        }
        int i2 = iPosition <= iLimit ? iLimit - iPosition : 0;
        ByteBuffer byteBufferDuplicate = this.f12451bb.duplicate();
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

    @Override // java.nio.CharBuffer, java.nio.Buffer
    public boolean isDirect() {
        return this.f12451bb.isDirect();
    }

    @Override // java.nio.Buffer
    public boolean isReadOnly() {
        return false;
    }

    @Override // java.nio.CharBuffer
    public String toString(int i2, int i3) {
        if (i3 > limit() || i2 > i3) {
            throw new IndexOutOfBoundsException();
        }
        try {
            char[] cArr = new char[i3 - i2];
            CharBuffer charBufferWrap = CharBuffer.wrap(cArr);
            CharBuffer charBufferDuplicate = duplicate();
            charBufferDuplicate.position(i2);
            charBufferDuplicate.limit(i3);
            charBufferWrap.put(charBufferDuplicate);
            return new String(cArr);
        } catch (StringIndexOutOfBoundsException e2) {
            throw new IndexOutOfBoundsException();
        }
    }

    @Override // java.nio.CharBuffer, java.lang.CharSequence
    public CharBuffer subSequence(int i2, int i3) {
        int iPosition = position();
        int iLimit = limit();
        if (!$assertionsDisabled && iPosition > iLimit) {
            throw new AssertionError();
        }
        int i4 = iPosition <= iLimit ? iPosition : iLimit;
        int i5 = iLimit - i4;
        if (i2 < 0 || i3 > i5 || i2 > i3) {
            throw new IndexOutOfBoundsException();
        }
        return new ByteBufferAsCharBufferL(this.f12451bb, -1, i4 + i2, i4 + i3, capacity(), this.offset);
    }

    @Override // java.nio.CharBuffer
    public ByteOrder order() {
        return ByteOrder.LITTLE_ENDIAN;
    }
}
