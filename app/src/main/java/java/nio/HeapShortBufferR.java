package java.nio;

/* loaded from: rt.jar:java/nio/HeapShortBufferR.class */
class HeapShortBufferR extends HeapShortBuffer {
    HeapShortBufferR(int i2, int i3) {
        super(i2, i3);
        this.isReadOnly = true;
    }

    HeapShortBufferR(short[] sArr, int i2, int i3) {
        super(sArr, i2, i3);
        this.isReadOnly = true;
    }

    protected HeapShortBufferR(short[] sArr, int i2, int i3, int i4, int i5, int i6) {
        super(sArr, i2, i3, i4, i5, i6);
        this.isReadOnly = true;
    }

    @Override // java.nio.HeapShortBuffer, java.nio.ShortBuffer
    public ShortBuffer slice() {
        int iPosition = position();
        int iLimit = limit();
        int i2 = iPosition <= iLimit ? iLimit - iPosition : 0;
        return new HeapShortBufferR(this.hb, -1, 0, i2, i2, iPosition + this.offset);
    }

    @Override // java.nio.HeapShortBuffer, java.nio.ShortBuffer
    public ShortBuffer duplicate() {
        return new HeapShortBufferR(this.hb, markValue(), position(), limit(), capacity(), this.offset);
    }

    @Override // java.nio.HeapShortBuffer, java.nio.ShortBuffer
    public ShortBuffer asReadOnlyBuffer() {
        return duplicate();
    }

    @Override // java.nio.HeapShortBuffer, java.nio.Buffer
    public boolean isReadOnly() {
        return true;
    }

    @Override // java.nio.HeapShortBuffer, java.nio.ShortBuffer
    public ShortBuffer put(short s2) {
        throw new ReadOnlyBufferException();
    }

    @Override // java.nio.HeapShortBuffer, java.nio.ShortBuffer
    public ShortBuffer put(int i2, short s2) {
        throw new ReadOnlyBufferException();
    }

    @Override // java.nio.HeapShortBuffer, java.nio.ShortBuffer
    public ShortBuffer put(short[] sArr, int i2, int i3) {
        throw new ReadOnlyBufferException();
    }

    @Override // java.nio.HeapShortBuffer, java.nio.ShortBuffer
    public ShortBuffer put(ShortBuffer shortBuffer) {
        throw new ReadOnlyBufferException();
    }

    @Override // java.nio.HeapShortBuffer, java.nio.ShortBuffer
    public ShortBuffer compact() {
        throw new ReadOnlyBufferException();
    }

    @Override // java.nio.HeapShortBuffer, java.nio.ShortBuffer
    public ByteOrder order() {
        return ByteOrder.nativeOrder();
    }
}
