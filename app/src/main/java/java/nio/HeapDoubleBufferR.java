package java.nio;

/* loaded from: rt.jar:java/nio/HeapDoubleBufferR.class */
class HeapDoubleBufferR extends HeapDoubleBuffer {
    HeapDoubleBufferR(int i2, int i3) {
        super(i2, i3);
        this.isReadOnly = true;
    }

    HeapDoubleBufferR(double[] dArr, int i2, int i3) {
        super(dArr, i2, i3);
        this.isReadOnly = true;
    }

    protected HeapDoubleBufferR(double[] dArr, int i2, int i3, int i4, int i5, int i6) {
        super(dArr, i2, i3, i4, i5, i6);
        this.isReadOnly = true;
    }

    @Override // java.nio.HeapDoubleBuffer, java.nio.DoubleBuffer
    public DoubleBuffer slice() {
        int iPosition = position();
        int iLimit = limit();
        int i2 = iPosition <= iLimit ? iLimit - iPosition : 0;
        return new HeapDoubleBufferR(this.hb, -1, 0, i2, i2, iPosition + this.offset);
    }

    @Override // java.nio.HeapDoubleBuffer, java.nio.DoubleBuffer
    public DoubleBuffer duplicate() {
        return new HeapDoubleBufferR(this.hb, markValue(), position(), limit(), capacity(), this.offset);
    }

    @Override // java.nio.HeapDoubleBuffer, java.nio.DoubleBuffer
    public DoubleBuffer asReadOnlyBuffer() {
        return duplicate();
    }

    @Override // java.nio.HeapDoubleBuffer, java.nio.Buffer
    public boolean isReadOnly() {
        return true;
    }

    @Override // java.nio.HeapDoubleBuffer, java.nio.DoubleBuffer
    public DoubleBuffer put(double d2) {
        throw new ReadOnlyBufferException();
    }

    @Override // java.nio.HeapDoubleBuffer, java.nio.DoubleBuffer
    public DoubleBuffer put(int i2, double d2) {
        throw new ReadOnlyBufferException();
    }

    @Override // java.nio.HeapDoubleBuffer, java.nio.DoubleBuffer
    public DoubleBuffer put(double[] dArr, int i2, int i3) {
        throw new ReadOnlyBufferException();
    }

    @Override // java.nio.HeapDoubleBuffer, java.nio.DoubleBuffer
    public DoubleBuffer put(DoubleBuffer doubleBuffer) {
        throw new ReadOnlyBufferException();
    }

    @Override // java.nio.HeapDoubleBuffer, java.nio.DoubleBuffer
    public DoubleBuffer compact() {
        throw new ReadOnlyBufferException();
    }

    @Override // java.nio.HeapDoubleBuffer, java.nio.DoubleBuffer
    public ByteOrder order() {
        return ByteOrder.nativeOrder();
    }
}
