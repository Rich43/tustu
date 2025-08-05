package java.nio;

/* loaded from: rt.jar:java/nio/HeapFloatBuffer.class */
class HeapFloatBuffer extends FloatBuffer {
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !HeapFloatBuffer.class.desiredAssertionStatus();
    }

    HeapFloatBuffer(int i2, int i3) {
        super(-1, 0, i3, i2, new float[i2], 0);
    }

    HeapFloatBuffer(float[] fArr, int i2, int i3) {
        super(-1, i2, i2 + i3, fArr.length, fArr, 0);
    }

    protected HeapFloatBuffer(float[] fArr, int i2, int i3, int i4, int i5, int i6) {
        super(i2, i3, i4, i5, fArr, i6);
    }

    @Override // java.nio.FloatBuffer
    public FloatBuffer slice() {
        int iPosition = position();
        int iLimit = limit();
        int i2 = iPosition <= iLimit ? iLimit - iPosition : 0;
        return new HeapFloatBuffer(this.hb, -1, 0, i2, i2, iPosition + this.offset);
    }

    @Override // java.nio.FloatBuffer
    public FloatBuffer duplicate() {
        return new HeapFloatBuffer(this.hb, markValue(), position(), limit(), capacity(), this.offset);
    }

    @Override // java.nio.FloatBuffer
    public FloatBuffer asReadOnlyBuffer() {
        return new HeapFloatBufferR(this.hb, markValue(), position(), limit(), capacity(), this.offset);
    }

    protected int ix(int i2) {
        return i2 + this.offset;
    }

    @Override // java.nio.FloatBuffer
    public float get() {
        return this.hb[ix(nextGetIndex())];
    }

    @Override // java.nio.FloatBuffer
    public float get(int i2) {
        return this.hb[ix(checkIndex(i2))];
    }

    @Override // java.nio.FloatBuffer
    public FloatBuffer get(float[] fArr, int i2, int i3) {
        checkBounds(i2, i3, fArr.length);
        int iPosition = position();
        if (i3 > limit() - iPosition) {
            throw new BufferUnderflowException();
        }
        System.arraycopy(this.hb, ix(iPosition), fArr, i2, i3);
        position(iPosition + i3);
        return this;
    }

    @Override // java.nio.FloatBuffer, java.nio.Buffer
    public boolean isDirect() {
        return false;
    }

    @Override // java.nio.Buffer
    public boolean isReadOnly() {
        return false;
    }

    @Override // java.nio.FloatBuffer
    public FloatBuffer put(float f2) {
        this.hb[ix(nextPutIndex())] = f2;
        return this;
    }

    @Override // java.nio.FloatBuffer
    public FloatBuffer put(int i2, float f2) {
        this.hb[ix(checkIndex(i2))] = f2;
        return this;
    }

    @Override // java.nio.FloatBuffer
    public FloatBuffer put(float[] fArr, int i2, int i3) {
        checkBounds(i2, i3, fArr.length);
        int iPosition = position();
        if (i3 > limit() - iPosition) {
            throw new BufferOverflowException();
        }
        System.arraycopy(fArr, i2, this.hb, ix(iPosition), i3);
        position(iPosition + i3);
        return this;
    }

    @Override // java.nio.FloatBuffer
    public FloatBuffer put(FloatBuffer floatBuffer) {
        if (floatBuffer instanceof HeapFloatBuffer) {
            if (floatBuffer == this) {
                throw new IllegalArgumentException();
            }
            HeapFloatBuffer heapFloatBuffer = (HeapFloatBuffer) floatBuffer;
            int iPosition = position();
            int iPosition2 = heapFloatBuffer.position();
            int iLimit = heapFloatBuffer.limit() - iPosition2;
            if (iLimit > limit() - iPosition) {
                throw new BufferOverflowException();
            }
            System.arraycopy(heapFloatBuffer.hb, heapFloatBuffer.ix(iPosition2), this.hb, ix(iPosition), iLimit);
            heapFloatBuffer.position(iPosition2 + iLimit);
            position(iPosition + iLimit);
        } else if (floatBuffer.isDirect()) {
            int iRemaining = floatBuffer.remaining();
            int iPosition3 = position();
            if (iRemaining > limit() - iPosition3) {
                throw new BufferOverflowException();
            }
            floatBuffer.get(this.hb, ix(iPosition3), iRemaining);
            position(iPosition3 + iRemaining);
        } else {
            super.put(floatBuffer);
        }
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
        System.arraycopy(this.hb, ix(iPosition), this.hb, ix(0), i2);
        position(i2);
        limit(capacity());
        discardMark();
        return this;
    }

    @Override // java.nio.FloatBuffer
    public ByteOrder order() {
        return ByteOrder.nativeOrder();
    }
}
