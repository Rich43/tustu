package java.nio;

/* loaded from: rt.jar:java/nio/HeapFloatBufferR.class */
class HeapFloatBufferR extends HeapFloatBuffer {
    HeapFloatBufferR(int i2, int i3) {
        super(i2, i3);
        this.isReadOnly = true;
    }

    HeapFloatBufferR(float[] fArr, int i2, int i3) {
        super(fArr, i2, i3);
        this.isReadOnly = true;
    }

    protected HeapFloatBufferR(float[] fArr, int i2, int i3, int i4, int i5, int i6) {
        super(fArr, i2, i3, i4, i5, i6);
        this.isReadOnly = true;
    }

    @Override // java.nio.HeapFloatBuffer, java.nio.FloatBuffer
    public FloatBuffer slice() {
        int iPosition = position();
        int iLimit = limit();
        int i2 = iPosition <= iLimit ? iLimit - iPosition : 0;
        return new HeapFloatBufferR(this.hb, -1, 0, i2, i2, iPosition + this.offset);
    }

    @Override // java.nio.HeapFloatBuffer, java.nio.FloatBuffer
    public FloatBuffer duplicate() {
        return new HeapFloatBufferR(this.hb, markValue(), position(), limit(), capacity(), this.offset);
    }

    @Override // java.nio.HeapFloatBuffer, java.nio.FloatBuffer
    public FloatBuffer asReadOnlyBuffer() {
        return duplicate();
    }

    @Override // java.nio.HeapFloatBuffer, java.nio.Buffer
    public boolean isReadOnly() {
        return true;
    }

    @Override // java.nio.HeapFloatBuffer, java.nio.FloatBuffer
    public FloatBuffer put(float f2) {
        throw new ReadOnlyBufferException();
    }

    @Override // java.nio.HeapFloatBuffer, java.nio.FloatBuffer
    public FloatBuffer put(int i2, float f2) {
        throw new ReadOnlyBufferException();
    }

    @Override // java.nio.HeapFloatBuffer, java.nio.FloatBuffer
    public FloatBuffer put(float[] fArr, int i2, int i3) {
        throw new ReadOnlyBufferException();
    }

    @Override // java.nio.HeapFloatBuffer, java.nio.FloatBuffer
    public FloatBuffer put(FloatBuffer floatBuffer) {
        throw new ReadOnlyBufferException();
    }

    @Override // java.nio.HeapFloatBuffer, java.nio.FloatBuffer
    public FloatBuffer compact() {
        throw new ReadOnlyBufferException();
    }

    @Override // java.nio.HeapFloatBuffer, java.nio.FloatBuffer
    public ByteOrder order() {
        return ByteOrder.nativeOrder();
    }
}
