package java.nio;

/* loaded from: rt.jar:java/nio/HeapByteBufferR.class */
class HeapByteBufferR extends HeapByteBuffer {
    HeapByteBufferR(int i2, int i3) {
        super(i2, i3);
        this.isReadOnly = true;
    }

    HeapByteBufferR(byte[] bArr, int i2, int i3) {
        super(bArr, i2, i3);
        this.isReadOnly = true;
    }

    protected HeapByteBufferR(byte[] bArr, int i2, int i3, int i4, int i5, int i6) {
        super(bArr, i2, i3, i4, i5, i6);
        this.isReadOnly = true;
    }

    @Override // java.nio.HeapByteBuffer, java.nio.ByteBuffer
    public ByteBuffer slice() {
        int iPosition = position();
        int iLimit = limit();
        int i2 = iPosition <= iLimit ? iLimit - iPosition : 0;
        return new HeapByteBufferR(this.hb, -1, 0, i2, i2, iPosition + this.offset);
    }

    @Override // java.nio.HeapByteBuffer, java.nio.ByteBuffer
    public ByteBuffer duplicate() {
        return new HeapByteBufferR(this.hb, markValue(), position(), limit(), capacity(), this.offset);
    }

    @Override // java.nio.HeapByteBuffer, java.nio.ByteBuffer
    public ByteBuffer asReadOnlyBuffer() {
        return duplicate();
    }

    @Override // java.nio.HeapByteBuffer, java.nio.Buffer
    public boolean isReadOnly() {
        return true;
    }

    @Override // java.nio.HeapByteBuffer, java.nio.ByteBuffer
    public ByteBuffer put(byte b2) {
        throw new ReadOnlyBufferException();
    }

    @Override // java.nio.HeapByteBuffer, java.nio.ByteBuffer
    public ByteBuffer put(int i2, byte b2) {
        throw new ReadOnlyBufferException();
    }

    @Override // java.nio.HeapByteBuffer, java.nio.ByteBuffer
    public ByteBuffer put(byte[] bArr, int i2, int i3) {
        throw new ReadOnlyBufferException();
    }

    @Override // java.nio.HeapByteBuffer, java.nio.ByteBuffer
    public ByteBuffer put(ByteBuffer byteBuffer) {
        throw new ReadOnlyBufferException();
    }

    @Override // java.nio.HeapByteBuffer, java.nio.ByteBuffer
    public ByteBuffer compact() {
        throw new ReadOnlyBufferException();
    }

    @Override // java.nio.HeapByteBuffer, java.nio.ByteBuffer
    byte _get(int i2) {
        return this.hb[i2];
    }

    @Override // java.nio.HeapByteBuffer, java.nio.ByteBuffer
    void _put(int i2, byte b2) {
        throw new ReadOnlyBufferException();
    }

    @Override // java.nio.HeapByteBuffer, java.nio.ByteBuffer
    public ByteBuffer putChar(char c2) {
        throw new ReadOnlyBufferException();
    }

    @Override // java.nio.HeapByteBuffer, java.nio.ByteBuffer
    public ByteBuffer putChar(int i2, char c2) {
        throw new ReadOnlyBufferException();
    }

    @Override // java.nio.HeapByteBuffer, java.nio.ByteBuffer
    public CharBuffer asCharBuffer() {
        int iPosition = position();
        int iLimit = (limit() - iPosition) >> 1;
        int i2 = this.offset + iPosition;
        return this.bigEndian ? new ByteBufferAsCharBufferRB(this, -1, 0, iLimit, iLimit, i2) : new ByteBufferAsCharBufferRL(this, -1, 0, iLimit, iLimit, i2);
    }

    @Override // java.nio.HeapByteBuffer, java.nio.ByteBuffer
    public ByteBuffer putShort(short s2) {
        throw new ReadOnlyBufferException();
    }

    @Override // java.nio.HeapByteBuffer, java.nio.ByteBuffer
    public ByteBuffer putShort(int i2, short s2) {
        throw new ReadOnlyBufferException();
    }

    @Override // java.nio.HeapByteBuffer, java.nio.ByteBuffer
    public ShortBuffer asShortBuffer() {
        int iPosition = position();
        int iLimit = (limit() - iPosition) >> 1;
        int i2 = this.offset + iPosition;
        return this.bigEndian ? new ByteBufferAsShortBufferRB(this, -1, 0, iLimit, iLimit, i2) : new ByteBufferAsShortBufferRL(this, -1, 0, iLimit, iLimit, i2);
    }

    @Override // java.nio.HeapByteBuffer, java.nio.ByteBuffer
    public ByteBuffer putInt(int i2) {
        throw new ReadOnlyBufferException();
    }

    @Override // java.nio.HeapByteBuffer, java.nio.ByteBuffer
    public ByteBuffer putInt(int i2, int i3) {
        throw new ReadOnlyBufferException();
    }

    @Override // java.nio.HeapByteBuffer, java.nio.ByteBuffer
    public IntBuffer asIntBuffer() {
        int iPosition = position();
        int iLimit = (limit() - iPosition) >> 2;
        int i2 = this.offset + iPosition;
        return this.bigEndian ? new ByteBufferAsIntBufferRB(this, -1, 0, iLimit, iLimit, i2) : new ByteBufferAsIntBufferRL(this, -1, 0, iLimit, iLimit, i2);
    }

    @Override // java.nio.HeapByteBuffer, java.nio.ByteBuffer
    public ByteBuffer putLong(long j2) {
        throw new ReadOnlyBufferException();
    }

    @Override // java.nio.HeapByteBuffer, java.nio.ByteBuffer
    public ByteBuffer putLong(int i2, long j2) {
        throw new ReadOnlyBufferException();
    }

    @Override // java.nio.HeapByteBuffer, java.nio.ByteBuffer
    public LongBuffer asLongBuffer() {
        int iPosition = position();
        int iLimit = (limit() - iPosition) >> 3;
        int i2 = this.offset + iPosition;
        return this.bigEndian ? new ByteBufferAsLongBufferRB(this, -1, 0, iLimit, iLimit, i2) : new ByteBufferAsLongBufferRL(this, -1, 0, iLimit, iLimit, i2);
    }

    @Override // java.nio.HeapByteBuffer, java.nio.ByteBuffer
    public ByteBuffer putFloat(float f2) {
        throw new ReadOnlyBufferException();
    }

    @Override // java.nio.HeapByteBuffer, java.nio.ByteBuffer
    public ByteBuffer putFloat(int i2, float f2) {
        throw new ReadOnlyBufferException();
    }

    @Override // java.nio.HeapByteBuffer, java.nio.ByteBuffer
    public FloatBuffer asFloatBuffer() {
        int iPosition = position();
        int iLimit = (limit() - iPosition) >> 2;
        int i2 = this.offset + iPosition;
        return this.bigEndian ? new ByteBufferAsFloatBufferRB(this, -1, 0, iLimit, iLimit, i2) : new ByteBufferAsFloatBufferRL(this, -1, 0, iLimit, iLimit, i2);
    }

    @Override // java.nio.HeapByteBuffer, java.nio.ByteBuffer
    public ByteBuffer putDouble(double d2) {
        throw new ReadOnlyBufferException();
    }

    @Override // java.nio.HeapByteBuffer, java.nio.ByteBuffer
    public ByteBuffer putDouble(int i2, double d2) {
        throw new ReadOnlyBufferException();
    }

    @Override // java.nio.HeapByteBuffer, java.nio.ByteBuffer
    public DoubleBuffer asDoubleBuffer() {
        int iPosition = position();
        int iLimit = (limit() - iPosition) >> 3;
        int i2 = this.offset + iPosition;
        return this.bigEndian ? new ByteBufferAsDoubleBufferRB(this, -1, 0, iLimit, iLimit, i2) : new ByteBufferAsDoubleBufferRL(this, -1, 0, iLimit, iLimit, i2);
    }
}
