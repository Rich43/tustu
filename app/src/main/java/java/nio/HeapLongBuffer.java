package java.nio;

/* loaded from: rt.jar:java/nio/HeapLongBuffer.class */
class HeapLongBuffer extends LongBuffer {
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !HeapLongBuffer.class.desiredAssertionStatus();
    }

    HeapLongBuffer(int i2, int i3) {
        super(-1, 0, i3, i2, new long[i2], 0);
    }

    HeapLongBuffer(long[] jArr, int i2, int i3) {
        super(-1, i2, i2 + i3, jArr.length, jArr, 0);
    }

    protected HeapLongBuffer(long[] jArr, int i2, int i3, int i4, int i5, int i6) {
        super(i2, i3, i4, i5, jArr, i6);
    }

    @Override // java.nio.LongBuffer
    public LongBuffer slice() {
        int iPosition = position();
        int iLimit = limit();
        int i2 = iPosition <= iLimit ? iLimit - iPosition : 0;
        return new HeapLongBuffer(this.hb, -1, 0, i2, i2, iPosition + this.offset);
    }

    @Override // java.nio.LongBuffer
    public LongBuffer duplicate() {
        return new HeapLongBuffer(this.hb, markValue(), position(), limit(), capacity(), this.offset);
    }

    @Override // java.nio.LongBuffer
    public LongBuffer asReadOnlyBuffer() {
        return new HeapLongBufferR(this.hb, markValue(), position(), limit(), capacity(), this.offset);
    }

    protected int ix(int i2) {
        return i2 + this.offset;
    }

    @Override // java.nio.LongBuffer
    public long get() {
        return this.hb[ix(nextGetIndex())];
    }

    @Override // java.nio.LongBuffer
    public long get(int i2) {
        return this.hb[ix(checkIndex(i2))];
    }

    @Override // java.nio.LongBuffer
    public LongBuffer get(long[] jArr, int i2, int i3) {
        checkBounds(i2, i3, jArr.length);
        int iPosition = position();
        if (i3 > limit() - iPosition) {
            throw new BufferUnderflowException();
        }
        System.arraycopy(this.hb, ix(iPosition), jArr, i2, i3);
        position(iPosition + i3);
        return this;
    }

    @Override // java.nio.LongBuffer, java.nio.Buffer
    public boolean isDirect() {
        return false;
    }

    @Override // java.nio.Buffer
    public boolean isReadOnly() {
        return false;
    }

    @Override // java.nio.LongBuffer
    public LongBuffer put(long j2) {
        this.hb[ix(nextPutIndex())] = j2;
        return this;
    }

    @Override // java.nio.LongBuffer
    public LongBuffer put(int i2, long j2) {
        this.hb[ix(checkIndex(i2))] = j2;
        return this;
    }

    @Override // java.nio.LongBuffer
    public LongBuffer put(long[] jArr, int i2, int i3) {
        checkBounds(i2, i3, jArr.length);
        int iPosition = position();
        if (i3 > limit() - iPosition) {
            throw new BufferOverflowException();
        }
        System.arraycopy(jArr, i2, this.hb, ix(iPosition), i3);
        position(iPosition + i3);
        return this;
    }

    @Override // java.nio.LongBuffer
    public LongBuffer put(LongBuffer longBuffer) {
        if (longBuffer instanceof HeapLongBuffer) {
            if (longBuffer == this) {
                throw new IllegalArgumentException();
            }
            HeapLongBuffer heapLongBuffer = (HeapLongBuffer) longBuffer;
            int iPosition = position();
            int iPosition2 = heapLongBuffer.position();
            int iLimit = heapLongBuffer.limit() - iPosition2;
            if (iLimit > limit() - iPosition) {
                throw new BufferOverflowException();
            }
            System.arraycopy(heapLongBuffer.hb, heapLongBuffer.ix(iPosition2), this.hb, ix(iPosition), iLimit);
            heapLongBuffer.position(iPosition2 + iLimit);
            position(iPosition + iLimit);
        } else if (longBuffer.isDirect()) {
            int iRemaining = longBuffer.remaining();
            int iPosition3 = position();
            if (iRemaining > limit() - iPosition3) {
                throw new BufferOverflowException();
            }
            longBuffer.get(this.hb, ix(iPosition3), iRemaining);
            position(iPosition3 + iRemaining);
        } else {
            super.put(longBuffer);
        }
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
        System.arraycopy(this.hb, ix(iPosition), this.hb, ix(0), i2);
        position(i2);
        limit(capacity());
        discardMark();
        return this;
    }

    @Override // java.nio.LongBuffer
    public ByteOrder order() {
        return ByteOrder.nativeOrder();
    }
}
