package java.nio;

/* loaded from: rt.jar:java/nio/HeapByteBuffer.class */
class HeapByteBuffer extends ByteBuffer {
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !HeapByteBuffer.class.desiredAssertionStatus();
    }

    HeapByteBuffer(int i2, int i3) {
        super(-1, 0, i3, i2, new byte[i2], 0);
    }

    HeapByteBuffer(byte[] bArr, int i2, int i3) {
        super(-1, i2, i2 + i3, bArr.length, bArr, 0);
    }

    protected HeapByteBuffer(byte[] bArr, int i2, int i3, int i4, int i5, int i6) {
        super(i2, i3, i4, i5, bArr, i6);
    }

    @Override // java.nio.ByteBuffer
    public ByteBuffer slice() {
        int iPosition = position();
        int iLimit = limit();
        int i2 = iPosition <= iLimit ? iLimit - iPosition : 0;
        return new HeapByteBuffer(this.hb, -1, 0, i2, i2, iPosition + this.offset);
    }

    @Override // java.nio.ByteBuffer
    public ByteBuffer duplicate() {
        return new HeapByteBuffer(this.hb, markValue(), position(), limit(), capacity(), this.offset);
    }

    @Override // java.nio.ByteBuffer
    public ByteBuffer asReadOnlyBuffer() {
        return new HeapByteBufferR(this.hb, markValue(), position(), limit(), capacity(), this.offset);
    }

    protected int ix(int i2) {
        return i2 + this.offset;
    }

    @Override // java.nio.ByteBuffer
    public byte get() {
        return this.hb[ix(nextGetIndex())];
    }

    @Override // java.nio.ByteBuffer
    public byte get(int i2) {
        return this.hb[ix(checkIndex(i2))];
    }

    @Override // java.nio.ByteBuffer
    public ByteBuffer get(byte[] bArr, int i2, int i3) {
        checkBounds(i2, i3, bArr.length);
        int iPosition = position();
        if (i3 > limit() - iPosition) {
            throw new BufferUnderflowException();
        }
        System.arraycopy(this.hb, ix(iPosition), bArr, i2, i3);
        position(iPosition + i3);
        return this;
    }

    @Override // java.nio.ByteBuffer, java.nio.Buffer
    public boolean isDirect() {
        return false;
    }

    @Override // java.nio.Buffer
    public boolean isReadOnly() {
        return false;
    }

    @Override // java.nio.ByteBuffer
    public ByteBuffer put(byte b2) {
        this.hb[ix(nextPutIndex())] = b2;
        return this;
    }

    @Override // java.nio.ByteBuffer
    public ByteBuffer put(int i2, byte b2) {
        this.hb[ix(checkIndex(i2))] = b2;
        return this;
    }

    @Override // java.nio.ByteBuffer
    public ByteBuffer put(byte[] bArr, int i2, int i3) {
        checkBounds(i2, i3, bArr.length);
        int iPosition = position();
        if (i3 > limit() - iPosition) {
            throw new BufferOverflowException();
        }
        System.arraycopy(bArr, i2, this.hb, ix(iPosition), i3);
        position(iPosition + i3);
        return this;
    }

    @Override // java.nio.ByteBuffer
    public ByteBuffer put(ByteBuffer byteBuffer) {
        if (byteBuffer instanceof HeapByteBuffer) {
            if (byteBuffer == this) {
                throw new IllegalArgumentException();
            }
            HeapByteBuffer heapByteBuffer = (HeapByteBuffer) byteBuffer;
            int iPosition = position();
            int iPosition2 = heapByteBuffer.position();
            int iLimit = heapByteBuffer.limit() - iPosition2;
            if (iLimit > limit() - iPosition) {
                throw new BufferOverflowException();
            }
            System.arraycopy(heapByteBuffer.hb, heapByteBuffer.ix(iPosition2), this.hb, ix(iPosition), iLimit);
            heapByteBuffer.position(iPosition2 + iLimit);
            position(iPosition + iLimit);
        } else if (byteBuffer.isDirect()) {
            int iRemaining = byteBuffer.remaining();
            int iPosition3 = position();
            if (iRemaining > limit() - iPosition3) {
                throw new BufferOverflowException();
            }
            byteBuffer.get(this.hb, ix(iPosition3), iRemaining);
            position(iPosition3 + iRemaining);
        } else {
            super.put(byteBuffer);
        }
        return this;
    }

    @Override // java.nio.ByteBuffer
    public ByteBuffer compact() {
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

    @Override // java.nio.ByteBuffer
    byte _get(int i2) {
        return this.hb[i2];
    }

    @Override // java.nio.ByteBuffer
    void _put(int i2, byte b2) {
        this.hb[i2] = b2;
    }

    @Override // java.nio.ByteBuffer
    public char getChar() {
        return Bits.getChar(this, ix(nextGetIndex(2)), this.bigEndian);
    }

    @Override // java.nio.ByteBuffer
    public char getChar(int i2) {
        return Bits.getChar(this, ix(checkIndex(i2, 2)), this.bigEndian);
    }

    @Override // java.nio.ByteBuffer
    public ByteBuffer putChar(char c2) {
        Bits.putChar(this, ix(nextPutIndex(2)), c2, this.bigEndian);
        return this;
    }

    @Override // java.nio.ByteBuffer
    public ByteBuffer putChar(int i2, char c2) {
        Bits.putChar(this, ix(checkIndex(i2, 2)), c2, this.bigEndian);
        return this;
    }

    @Override // java.nio.ByteBuffer
    public CharBuffer asCharBuffer() {
        int iPosition = position();
        int iLimit = (limit() - iPosition) >> 1;
        int i2 = this.offset + iPosition;
        return this.bigEndian ? new ByteBufferAsCharBufferB(this, -1, 0, iLimit, iLimit, i2) : new ByteBufferAsCharBufferL(this, -1, 0, iLimit, iLimit, i2);
    }

    @Override // java.nio.ByteBuffer
    public short getShort() {
        return Bits.getShort(this, ix(nextGetIndex(2)), this.bigEndian);
    }

    @Override // java.nio.ByteBuffer
    public short getShort(int i2) {
        return Bits.getShort(this, ix(checkIndex(i2, 2)), this.bigEndian);
    }

    @Override // java.nio.ByteBuffer
    public ByteBuffer putShort(short s2) {
        Bits.putShort(this, ix(nextPutIndex(2)), s2, this.bigEndian);
        return this;
    }

    @Override // java.nio.ByteBuffer
    public ByteBuffer putShort(int i2, short s2) {
        Bits.putShort(this, ix(checkIndex(i2, 2)), s2, this.bigEndian);
        return this;
    }

    @Override // java.nio.ByteBuffer
    public ShortBuffer asShortBuffer() {
        int iPosition = position();
        int iLimit = (limit() - iPosition) >> 1;
        int i2 = this.offset + iPosition;
        return this.bigEndian ? new ByteBufferAsShortBufferB(this, -1, 0, iLimit, iLimit, i2) : new ByteBufferAsShortBufferL(this, -1, 0, iLimit, iLimit, i2);
    }

    @Override // java.nio.ByteBuffer
    public int getInt() {
        return Bits.getInt(this, ix(nextGetIndex(4)), this.bigEndian);
    }

    @Override // java.nio.ByteBuffer
    public int getInt(int i2) {
        return Bits.getInt(this, ix(checkIndex(i2, 4)), this.bigEndian);
    }

    @Override // java.nio.ByteBuffer
    public ByteBuffer putInt(int i2) {
        Bits.putInt(this, ix(nextPutIndex(4)), i2, this.bigEndian);
        return this;
    }

    @Override // java.nio.ByteBuffer
    public ByteBuffer putInt(int i2, int i3) {
        Bits.putInt(this, ix(checkIndex(i2, 4)), i3, this.bigEndian);
        return this;
    }

    @Override // java.nio.ByteBuffer
    public IntBuffer asIntBuffer() {
        int iPosition = position();
        int iLimit = (limit() - iPosition) >> 2;
        int i2 = this.offset + iPosition;
        return this.bigEndian ? new ByteBufferAsIntBufferB(this, -1, 0, iLimit, iLimit, i2) : new ByteBufferAsIntBufferL(this, -1, 0, iLimit, iLimit, i2);
    }

    @Override // java.nio.ByteBuffer
    public long getLong() {
        return Bits.getLong(this, ix(nextGetIndex(8)), this.bigEndian);
    }

    @Override // java.nio.ByteBuffer
    public long getLong(int i2) {
        return Bits.getLong(this, ix(checkIndex(i2, 8)), this.bigEndian);
    }

    @Override // java.nio.ByteBuffer
    public ByteBuffer putLong(long j2) {
        Bits.putLong(this, ix(nextPutIndex(8)), j2, this.bigEndian);
        return this;
    }

    @Override // java.nio.ByteBuffer
    public ByteBuffer putLong(int i2, long j2) {
        Bits.putLong(this, ix(checkIndex(i2, 8)), j2, this.bigEndian);
        return this;
    }

    @Override // java.nio.ByteBuffer
    public LongBuffer asLongBuffer() {
        int iPosition = position();
        int iLimit = (limit() - iPosition) >> 3;
        int i2 = this.offset + iPosition;
        return this.bigEndian ? new ByteBufferAsLongBufferB(this, -1, 0, iLimit, iLimit, i2) : new ByteBufferAsLongBufferL(this, -1, 0, iLimit, iLimit, i2);
    }

    @Override // java.nio.ByteBuffer
    public float getFloat() {
        return Bits.getFloat(this, ix(nextGetIndex(4)), this.bigEndian);
    }

    @Override // java.nio.ByteBuffer
    public float getFloat(int i2) {
        return Bits.getFloat(this, ix(checkIndex(i2, 4)), this.bigEndian);
    }

    @Override // java.nio.ByteBuffer
    public ByteBuffer putFloat(float f2) {
        Bits.putFloat(this, ix(nextPutIndex(4)), f2, this.bigEndian);
        return this;
    }

    @Override // java.nio.ByteBuffer
    public ByteBuffer putFloat(int i2, float f2) {
        Bits.putFloat(this, ix(checkIndex(i2, 4)), f2, this.bigEndian);
        return this;
    }

    @Override // java.nio.ByteBuffer
    public FloatBuffer asFloatBuffer() {
        int iPosition = position();
        int iLimit = (limit() - iPosition) >> 2;
        int i2 = this.offset + iPosition;
        return this.bigEndian ? new ByteBufferAsFloatBufferB(this, -1, 0, iLimit, iLimit, i2) : new ByteBufferAsFloatBufferL(this, -1, 0, iLimit, iLimit, i2);
    }

    @Override // java.nio.ByteBuffer
    public double getDouble() {
        return Bits.getDouble(this, ix(nextGetIndex(8)), this.bigEndian);
    }

    @Override // java.nio.ByteBuffer
    public double getDouble(int i2) {
        return Bits.getDouble(this, ix(checkIndex(i2, 8)), this.bigEndian);
    }

    @Override // java.nio.ByteBuffer
    public ByteBuffer putDouble(double d2) {
        Bits.putDouble(this, ix(nextPutIndex(8)), d2, this.bigEndian);
        return this;
    }

    @Override // java.nio.ByteBuffer
    public ByteBuffer putDouble(int i2, double d2) {
        Bits.putDouble(this, ix(checkIndex(i2, 8)), d2, this.bigEndian);
        return this;
    }

    @Override // java.nio.ByteBuffer
    public DoubleBuffer asDoubleBuffer() {
        int iPosition = position();
        int iLimit = (limit() - iPosition) >> 3;
        int i2 = this.offset + iPosition;
        return this.bigEndian ? new ByteBufferAsDoubleBufferB(this, -1, 0, iLimit, iLimit, i2) : new ByteBufferAsDoubleBufferL(this, -1, 0, iLimit, iLimit, i2);
    }
}
