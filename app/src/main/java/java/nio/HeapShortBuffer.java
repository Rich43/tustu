package java.nio;

/* loaded from: rt.jar:java/nio/HeapShortBuffer.class */
class HeapShortBuffer extends ShortBuffer {
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !HeapShortBuffer.class.desiredAssertionStatus();
    }

    HeapShortBuffer(int i2, int i3) {
        super(-1, 0, i3, i2, new short[i2], 0);
    }

    HeapShortBuffer(short[] sArr, int i2, int i3) {
        super(-1, i2, i2 + i3, sArr.length, sArr, 0);
    }

    protected HeapShortBuffer(short[] sArr, int i2, int i3, int i4, int i5, int i6) {
        super(i2, i3, i4, i5, sArr, i6);
    }

    @Override // java.nio.ShortBuffer
    public ShortBuffer slice() {
        int iPosition = position();
        int iLimit = limit();
        int i2 = iPosition <= iLimit ? iLimit - iPosition : 0;
        return new HeapShortBuffer(this.hb, -1, 0, i2, i2, iPosition + this.offset);
    }

    @Override // java.nio.ShortBuffer
    public ShortBuffer duplicate() {
        return new HeapShortBuffer(this.hb, markValue(), position(), limit(), capacity(), this.offset);
    }

    @Override // java.nio.ShortBuffer
    public ShortBuffer asReadOnlyBuffer() {
        return new HeapShortBufferR(this.hb, markValue(), position(), limit(), capacity(), this.offset);
    }

    protected int ix(int i2) {
        return i2 + this.offset;
    }

    @Override // java.nio.ShortBuffer
    public short get() {
        return this.hb[ix(nextGetIndex())];
    }

    @Override // java.nio.ShortBuffer
    public short get(int i2) {
        return this.hb[ix(checkIndex(i2))];
    }

    @Override // java.nio.ShortBuffer
    public ShortBuffer get(short[] sArr, int i2, int i3) {
        checkBounds(i2, i3, sArr.length);
        int iPosition = position();
        if (i3 > limit() - iPosition) {
            throw new BufferUnderflowException();
        }
        System.arraycopy(this.hb, ix(iPosition), sArr, i2, i3);
        position(iPosition + i3);
        return this;
    }

    @Override // java.nio.ShortBuffer, java.nio.Buffer
    public boolean isDirect() {
        return false;
    }

    @Override // java.nio.Buffer
    public boolean isReadOnly() {
        return false;
    }

    @Override // java.nio.ShortBuffer
    public ShortBuffer put(short s2) {
        this.hb[ix(nextPutIndex())] = s2;
        return this;
    }

    @Override // java.nio.ShortBuffer
    public ShortBuffer put(int i2, short s2) {
        this.hb[ix(checkIndex(i2))] = s2;
        return this;
    }

    @Override // java.nio.ShortBuffer
    public ShortBuffer put(short[] sArr, int i2, int i3) {
        checkBounds(i2, i3, sArr.length);
        int iPosition = position();
        if (i3 > limit() - iPosition) {
            throw new BufferOverflowException();
        }
        System.arraycopy(sArr, i2, this.hb, ix(iPosition), i3);
        position(iPosition + i3);
        return this;
    }

    @Override // java.nio.ShortBuffer
    public ShortBuffer put(ShortBuffer shortBuffer) {
        if (shortBuffer instanceof HeapShortBuffer) {
            if (shortBuffer == this) {
                throw new IllegalArgumentException();
            }
            HeapShortBuffer heapShortBuffer = (HeapShortBuffer) shortBuffer;
            int iPosition = position();
            int iPosition2 = heapShortBuffer.position();
            int iLimit = heapShortBuffer.limit() - iPosition2;
            if (iLimit > limit() - iPosition) {
                throw new BufferOverflowException();
            }
            System.arraycopy(heapShortBuffer.hb, heapShortBuffer.ix(iPosition2), this.hb, ix(iPosition), iLimit);
            heapShortBuffer.position(iPosition2 + iLimit);
            position(iPosition + iLimit);
        } else if (shortBuffer.isDirect()) {
            int iRemaining = shortBuffer.remaining();
            int iPosition3 = position();
            if (iRemaining > limit() - iPosition3) {
                throw new BufferOverflowException();
            }
            shortBuffer.get(this.hb, ix(iPosition3), iRemaining);
            position(iPosition3 + iRemaining);
        } else {
            super.put(shortBuffer);
        }
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
        System.arraycopy(this.hb, ix(iPosition), this.hb, ix(0), i2);
        position(i2);
        limit(capacity());
        discardMark();
        return this;
    }

    @Override // java.nio.ShortBuffer
    public ByteOrder order() {
        return ByteOrder.nativeOrder();
    }
}
