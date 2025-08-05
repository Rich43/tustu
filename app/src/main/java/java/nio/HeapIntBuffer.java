package java.nio;

/* loaded from: rt.jar:java/nio/HeapIntBuffer.class */
class HeapIntBuffer extends IntBuffer {
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !HeapIntBuffer.class.desiredAssertionStatus();
    }

    HeapIntBuffer(int i2, int i3) {
        super(-1, 0, i3, i2, new int[i2], 0);
    }

    HeapIntBuffer(int[] iArr, int i2, int i3) {
        super(-1, i2, i2 + i3, iArr.length, iArr, 0);
    }

    protected HeapIntBuffer(int[] iArr, int i2, int i3, int i4, int i5, int i6) {
        super(i2, i3, i4, i5, iArr, i6);
    }

    @Override // java.nio.IntBuffer
    public IntBuffer slice() {
        int iPosition = position();
        int iLimit = limit();
        int i2 = iPosition <= iLimit ? iLimit - iPosition : 0;
        return new HeapIntBuffer(this.hb, -1, 0, i2, i2, iPosition + this.offset);
    }

    @Override // java.nio.IntBuffer
    public IntBuffer duplicate() {
        return new HeapIntBuffer(this.hb, markValue(), position(), limit(), capacity(), this.offset);
    }

    @Override // java.nio.IntBuffer
    public IntBuffer asReadOnlyBuffer() {
        return new HeapIntBufferR(this.hb, markValue(), position(), limit(), capacity(), this.offset);
    }

    protected int ix(int i2) {
        return i2 + this.offset;
    }

    @Override // java.nio.IntBuffer
    public int get() {
        return this.hb[ix(nextGetIndex())];
    }

    @Override // java.nio.IntBuffer
    public int get(int i2) {
        return this.hb[ix(checkIndex(i2))];
    }

    @Override // java.nio.IntBuffer
    public IntBuffer get(int[] iArr, int i2, int i3) {
        checkBounds(i2, i3, iArr.length);
        int iPosition = position();
        if (i3 > limit() - iPosition) {
            throw new BufferUnderflowException();
        }
        System.arraycopy(this.hb, ix(iPosition), iArr, i2, i3);
        position(iPosition + i3);
        return this;
    }

    @Override // java.nio.IntBuffer, java.nio.Buffer
    public boolean isDirect() {
        return false;
    }

    @Override // java.nio.Buffer
    public boolean isReadOnly() {
        return false;
    }

    @Override // java.nio.IntBuffer
    public IntBuffer put(int i2) {
        this.hb[ix(nextPutIndex())] = i2;
        return this;
    }

    @Override // java.nio.IntBuffer
    public IntBuffer put(int i2, int i3) {
        this.hb[ix(checkIndex(i2))] = i3;
        return this;
    }

    @Override // java.nio.IntBuffer
    public IntBuffer put(int[] iArr, int i2, int i3) {
        checkBounds(i2, i3, iArr.length);
        int iPosition = position();
        if (i3 > limit() - iPosition) {
            throw new BufferOverflowException();
        }
        System.arraycopy(iArr, i2, this.hb, ix(iPosition), i3);
        position(iPosition + i3);
        return this;
    }

    @Override // java.nio.IntBuffer
    public IntBuffer put(IntBuffer intBuffer) {
        if (intBuffer instanceof HeapIntBuffer) {
            if (intBuffer == this) {
                throw new IllegalArgumentException();
            }
            HeapIntBuffer heapIntBuffer = (HeapIntBuffer) intBuffer;
            int iPosition = position();
            int iPosition2 = heapIntBuffer.position();
            int iLimit = heapIntBuffer.limit() - iPosition2;
            if (iLimit > limit() - iPosition) {
                throw new BufferOverflowException();
            }
            System.arraycopy(heapIntBuffer.hb, heapIntBuffer.ix(iPosition2), this.hb, ix(iPosition), iLimit);
            heapIntBuffer.position(iPosition2 + iLimit);
            position(iPosition + iLimit);
        } else if (intBuffer.isDirect()) {
            int iRemaining = intBuffer.remaining();
            int iPosition3 = position();
            if (iRemaining > limit() - iPosition3) {
                throw new BufferOverflowException();
            }
            intBuffer.get(this.hb, ix(iPosition3), iRemaining);
            position(iPosition3 + iRemaining);
        } else {
            super.put(intBuffer);
        }
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
        System.arraycopy(this.hb, ix(iPosition), this.hb, ix(0), i2);
        position(i2);
        limit(capacity());
        discardMark();
        return this;
    }

    @Override // java.nio.IntBuffer
    public ByteOrder order() {
        return ByteOrder.nativeOrder();
    }
}
