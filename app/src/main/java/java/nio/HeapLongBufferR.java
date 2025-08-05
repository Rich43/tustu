package java.nio;

/* loaded from: rt.jar:java/nio/HeapLongBufferR.class */
class HeapLongBufferR extends HeapLongBuffer {
    HeapLongBufferR(int i2, int i3) {
        super(i2, i3);
        this.isReadOnly = true;
    }

    HeapLongBufferR(long[] jArr, int i2, int i3) {
        super(jArr, i2, i3);
        this.isReadOnly = true;
    }

    protected HeapLongBufferR(long[] jArr, int i2, int i3, int i4, int i5, int i6) {
        super(jArr, i2, i3, i4, i5, i6);
        this.isReadOnly = true;
    }

    @Override // java.nio.HeapLongBuffer, java.nio.LongBuffer
    public LongBuffer slice() {
        int iPosition = position();
        int iLimit = limit();
        int i2 = iPosition <= iLimit ? iLimit - iPosition : 0;
        return new HeapLongBufferR(this.hb, -1, 0, i2, i2, iPosition + this.offset);
    }

    @Override // java.nio.HeapLongBuffer, java.nio.LongBuffer
    public LongBuffer duplicate() {
        return new HeapLongBufferR(this.hb, markValue(), position(), limit(), capacity(), this.offset);
    }

    @Override // java.nio.HeapLongBuffer, java.nio.LongBuffer
    public LongBuffer asReadOnlyBuffer() {
        return duplicate();
    }

    @Override // java.nio.HeapLongBuffer, java.nio.Buffer
    public boolean isReadOnly() {
        return true;
    }

    @Override // java.nio.HeapLongBuffer, java.nio.LongBuffer
    public LongBuffer put(long j2) {
        throw new ReadOnlyBufferException();
    }

    @Override // java.nio.HeapLongBuffer, java.nio.LongBuffer
    public LongBuffer put(int i2, long j2) {
        throw new ReadOnlyBufferException();
    }

    @Override // java.nio.HeapLongBuffer, java.nio.LongBuffer
    public LongBuffer put(long[] jArr, int i2, int i3) {
        throw new ReadOnlyBufferException();
    }

    @Override // java.nio.HeapLongBuffer, java.nio.LongBuffer
    public LongBuffer put(LongBuffer longBuffer) {
        throw new ReadOnlyBufferException();
    }

    @Override // java.nio.HeapLongBuffer, java.nio.LongBuffer
    public LongBuffer compact() {
        throw new ReadOnlyBufferException();
    }

    @Override // java.nio.HeapLongBuffer, java.nio.LongBuffer
    public ByteOrder order() {
        return ByteOrder.nativeOrder();
    }
}
