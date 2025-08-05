package java.nio;

/* loaded from: rt.jar:java/nio/HeapDoubleBuffer.class */
class HeapDoubleBuffer extends DoubleBuffer {
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !HeapDoubleBuffer.class.desiredAssertionStatus();
    }

    HeapDoubleBuffer(int i2, int i3) {
        super(-1, 0, i3, i2, new double[i2], 0);
    }

    HeapDoubleBuffer(double[] dArr, int i2, int i3) {
        super(-1, i2, i2 + i3, dArr.length, dArr, 0);
    }

    protected HeapDoubleBuffer(double[] dArr, int i2, int i3, int i4, int i5, int i6) {
        super(i2, i3, i4, i5, dArr, i6);
    }

    @Override // java.nio.DoubleBuffer
    public DoubleBuffer slice() {
        int iPosition = position();
        int iLimit = limit();
        int i2 = iPosition <= iLimit ? iLimit - iPosition : 0;
        return new HeapDoubleBuffer(this.hb, -1, 0, i2, i2, iPosition + this.offset);
    }

    @Override // java.nio.DoubleBuffer
    public DoubleBuffer duplicate() {
        return new HeapDoubleBuffer(this.hb, markValue(), position(), limit(), capacity(), this.offset);
    }

    @Override // java.nio.DoubleBuffer
    public DoubleBuffer asReadOnlyBuffer() {
        return new HeapDoubleBufferR(this.hb, markValue(), position(), limit(), capacity(), this.offset);
    }

    protected int ix(int i2) {
        return i2 + this.offset;
    }

    @Override // java.nio.DoubleBuffer
    public double get() {
        return this.hb[ix(nextGetIndex())];
    }

    @Override // java.nio.DoubleBuffer
    public double get(int i2) {
        return this.hb[ix(checkIndex(i2))];
    }

    @Override // java.nio.DoubleBuffer
    public DoubleBuffer get(double[] dArr, int i2, int i3) {
        checkBounds(i2, i3, dArr.length);
        int iPosition = position();
        if (i3 > limit() - iPosition) {
            throw new BufferUnderflowException();
        }
        System.arraycopy(this.hb, ix(iPosition), dArr, i2, i3);
        position(iPosition + i3);
        return this;
    }

    @Override // java.nio.DoubleBuffer, java.nio.Buffer
    public boolean isDirect() {
        return false;
    }

    @Override // java.nio.Buffer
    public boolean isReadOnly() {
        return false;
    }

    @Override // java.nio.DoubleBuffer
    public DoubleBuffer put(double d2) {
        this.hb[ix(nextPutIndex())] = d2;
        return this;
    }

    @Override // java.nio.DoubleBuffer
    public DoubleBuffer put(int i2, double d2) {
        this.hb[ix(checkIndex(i2))] = d2;
        return this;
    }

    @Override // java.nio.DoubleBuffer
    public DoubleBuffer put(double[] dArr, int i2, int i3) {
        checkBounds(i2, i3, dArr.length);
        int iPosition = position();
        if (i3 > limit() - iPosition) {
            throw new BufferOverflowException();
        }
        System.arraycopy(dArr, i2, this.hb, ix(iPosition), i3);
        position(iPosition + i3);
        return this;
    }

    @Override // java.nio.DoubleBuffer
    public DoubleBuffer put(DoubleBuffer doubleBuffer) {
        if (doubleBuffer instanceof HeapDoubleBuffer) {
            if (doubleBuffer == this) {
                throw new IllegalArgumentException();
            }
            HeapDoubleBuffer heapDoubleBuffer = (HeapDoubleBuffer) doubleBuffer;
            int iPosition = position();
            int iPosition2 = heapDoubleBuffer.position();
            int iLimit = heapDoubleBuffer.limit() - iPosition2;
            if (iLimit > limit() - iPosition) {
                throw new BufferOverflowException();
            }
            System.arraycopy(heapDoubleBuffer.hb, heapDoubleBuffer.ix(iPosition2), this.hb, ix(iPosition), iLimit);
            heapDoubleBuffer.position(iPosition2 + iLimit);
            position(iPosition + iLimit);
        } else if (doubleBuffer.isDirect()) {
            int iRemaining = doubleBuffer.remaining();
            int iPosition3 = position();
            if (iRemaining > limit() - iPosition3) {
                throw new BufferOverflowException();
            }
            doubleBuffer.get(this.hb, ix(iPosition3), iRemaining);
            position(iPosition3 + iRemaining);
        } else {
            super.put(doubleBuffer);
        }
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
        System.arraycopy(this.hb, ix(iPosition), this.hb, ix(0), i2);
        position(i2);
        limit(capacity());
        discardMark();
        return this;
    }

    @Override // java.nio.DoubleBuffer
    public ByteOrder order() {
        return ByteOrder.nativeOrder();
    }
}
