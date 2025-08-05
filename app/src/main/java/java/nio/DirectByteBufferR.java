package java.nio;

import java.io.FileDescriptor;
import sun.nio.ch.DirectBuffer;

/* loaded from: rt.jar:java/nio/DirectByteBufferR.class */
class DirectByteBufferR extends DirectByteBuffer implements DirectBuffer {
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !DirectByteBufferR.class.desiredAssertionStatus();
    }

    DirectByteBufferR(int i2) {
        super(i2);
    }

    protected DirectByteBufferR(int i2, long j2, FileDescriptor fileDescriptor, Runnable runnable) {
        super(i2, j2, fileDescriptor, runnable);
    }

    DirectByteBufferR(DirectBuffer directBuffer, int i2, int i3, int i4, int i5, int i6) {
        super(directBuffer, i2, i3, i4, i5, i6);
    }

    @Override // java.nio.DirectByteBuffer, java.nio.ByteBuffer
    public ByteBuffer slice() {
        int iPosition = position();
        int iLimit = limit();
        int i2 = iPosition <= iLimit ? iLimit - iPosition : 0;
        int i3 = iPosition << 0;
        if ($assertionsDisabled || i3 >= 0) {
            return new DirectByteBufferR(this, -1, 0, i2, i2, i3);
        }
        throw new AssertionError();
    }

    @Override // java.nio.DirectByteBuffer, java.nio.ByteBuffer
    public ByteBuffer duplicate() {
        return new DirectByteBufferR(this, markValue(), position(), limit(), capacity(), 0);
    }

    @Override // java.nio.DirectByteBuffer, java.nio.ByteBuffer
    public ByteBuffer asReadOnlyBuffer() {
        return duplicate();
    }

    @Override // java.nio.DirectByteBuffer, java.nio.ByteBuffer
    public ByteBuffer put(byte b2) {
        throw new ReadOnlyBufferException();
    }

    @Override // java.nio.DirectByteBuffer, java.nio.ByteBuffer
    public ByteBuffer put(int i2, byte b2) {
        throw new ReadOnlyBufferException();
    }

    @Override // java.nio.DirectByteBuffer, java.nio.ByteBuffer
    public ByteBuffer put(ByteBuffer byteBuffer) {
        throw new ReadOnlyBufferException();
    }

    @Override // java.nio.DirectByteBuffer, java.nio.ByteBuffer
    public ByteBuffer put(byte[] bArr, int i2, int i3) {
        throw new ReadOnlyBufferException();
    }

    @Override // java.nio.DirectByteBuffer, java.nio.ByteBuffer
    public ByteBuffer compact() {
        throw new ReadOnlyBufferException();
    }

    @Override // java.nio.DirectByteBuffer, java.nio.ByteBuffer, java.nio.Buffer
    public boolean isDirect() {
        return true;
    }

    @Override // java.nio.DirectByteBuffer, java.nio.Buffer
    public boolean isReadOnly() {
        return true;
    }

    @Override // java.nio.DirectByteBuffer, java.nio.ByteBuffer
    byte _get(int i2) {
        return unsafe.getByte(this.address + i2);
    }

    @Override // java.nio.DirectByteBuffer, java.nio.ByteBuffer
    void _put(int i2, byte b2) {
        throw new ReadOnlyBufferException();
    }

    private ByteBuffer putChar(long j2, char c2) {
        throw new ReadOnlyBufferException();
    }

    @Override // java.nio.DirectByteBuffer, java.nio.ByteBuffer
    public ByteBuffer putChar(char c2) {
        throw new ReadOnlyBufferException();
    }

    @Override // java.nio.DirectByteBuffer, java.nio.ByteBuffer
    public ByteBuffer putChar(int i2, char c2) {
        throw new ReadOnlyBufferException();
    }

    @Override // java.nio.DirectByteBuffer, java.nio.ByteBuffer
    public CharBuffer asCharBuffer() {
        int iPosition = position();
        int iLimit = limit();
        if (!$assertionsDisabled && iPosition > iLimit) {
            throw new AssertionError();
        }
        int i2 = (iPosition <= iLimit ? iLimit - iPosition : 0) >> 1;
        return (unaligned || (this.address + ((long) iPosition)) % 2 == 0) ? this.nativeByteOrder ? new DirectCharBufferRU(this, -1, 0, i2, i2, iPosition) : new DirectCharBufferRS(this, -1, 0, i2, i2, iPosition) : this.bigEndian ? new ByteBufferAsCharBufferRB(this, -1, 0, i2, i2, iPosition) : new ByteBufferAsCharBufferRL(this, -1, 0, i2, i2, iPosition);
    }

    private ByteBuffer putShort(long j2, short s2) {
        throw new ReadOnlyBufferException();
    }

    @Override // java.nio.DirectByteBuffer, java.nio.ByteBuffer
    public ByteBuffer putShort(short s2) {
        throw new ReadOnlyBufferException();
    }

    @Override // java.nio.DirectByteBuffer, java.nio.ByteBuffer
    public ByteBuffer putShort(int i2, short s2) {
        throw new ReadOnlyBufferException();
    }

    @Override // java.nio.DirectByteBuffer, java.nio.ByteBuffer
    public ShortBuffer asShortBuffer() {
        int iPosition = position();
        int iLimit = limit();
        if (!$assertionsDisabled && iPosition > iLimit) {
            throw new AssertionError();
        }
        int i2 = (iPosition <= iLimit ? iLimit - iPosition : 0) >> 1;
        return (unaligned || (this.address + ((long) iPosition)) % 2 == 0) ? this.nativeByteOrder ? new DirectShortBufferRU(this, -1, 0, i2, i2, iPosition) : new DirectShortBufferRS(this, -1, 0, i2, i2, iPosition) : this.bigEndian ? new ByteBufferAsShortBufferRB(this, -1, 0, i2, i2, iPosition) : new ByteBufferAsShortBufferRL(this, -1, 0, i2, i2, iPosition);
    }

    private ByteBuffer putInt(long j2, int i2) {
        throw new ReadOnlyBufferException();
    }

    @Override // java.nio.DirectByteBuffer, java.nio.ByteBuffer
    public ByteBuffer putInt(int i2) {
        throw new ReadOnlyBufferException();
    }

    @Override // java.nio.DirectByteBuffer, java.nio.ByteBuffer
    public ByteBuffer putInt(int i2, int i3) {
        throw new ReadOnlyBufferException();
    }

    @Override // java.nio.DirectByteBuffer, java.nio.ByteBuffer
    public IntBuffer asIntBuffer() {
        int iPosition = position();
        int iLimit = limit();
        if (!$assertionsDisabled && iPosition > iLimit) {
            throw new AssertionError();
        }
        int i2 = (iPosition <= iLimit ? iLimit - iPosition : 0) >> 2;
        return (unaligned || (this.address + ((long) iPosition)) % 4 == 0) ? this.nativeByteOrder ? new DirectIntBufferRU(this, -1, 0, i2, i2, iPosition) : new DirectIntBufferRS(this, -1, 0, i2, i2, iPosition) : this.bigEndian ? new ByteBufferAsIntBufferRB(this, -1, 0, i2, i2, iPosition) : new ByteBufferAsIntBufferRL(this, -1, 0, i2, i2, iPosition);
    }

    private ByteBuffer putLong(long j2, long j3) {
        throw new ReadOnlyBufferException();
    }

    @Override // java.nio.DirectByteBuffer, java.nio.ByteBuffer
    public ByteBuffer putLong(long j2) {
        throw new ReadOnlyBufferException();
    }

    @Override // java.nio.DirectByteBuffer, java.nio.ByteBuffer
    public ByteBuffer putLong(int i2, long j2) {
        throw new ReadOnlyBufferException();
    }

    @Override // java.nio.DirectByteBuffer, java.nio.ByteBuffer
    public LongBuffer asLongBuffer() {
        int iPosition = position();
        int iLimit = limit();
        if (!$assertionsDisabled && iPosition > iLimit) {
            throw new AssertionError();
        }
        int i2 = (iPosition <= iLimit ? iLimit - iPosition : 0) >> 3;
        return (unaligned || (this.address + ((long) iPosition)) % 8 == 0) ? this.nativeByteOrder ? new DirectLongBufferRU(this, -1, 0, i2, i2, iPosition) : new DirectLongBufferRS(this, -1, 0, i2, i2, iPosition) : this.bigEndian ? new ByteBufferAsLongBufferRB(this, -1, 0, i2, i2, iPosition) : new ByteBufferAsLongBufferRL(this, -1, 0, i2, i2, iPosition);
    }

    private ByteBuffer putFloat(long j2, float f2) {
        throw new ReadOnlyBufferException();
    }

    @Override // java.nio.DirectByteBuffer, java.nio.ByteBuffer
    public ByteBuffer putFloat(float f2) {
        throw new ReadOnlyBufferException();
    }

    @Override // java.nio.DirectByteBuffer, java.nio.ByteBuffer
    public ByteBuffer putFloat(int i2, float f2) {
        throw new ReadOnlyBufferException();
    }

    @Override // java.nio.DirectByteBuffer, java.nio.ByteBuffer
    public FloatBuffer asFloatBuffer() {
        int iPosition = position();
        int iLimit = limit();
        if (!$assertionsDisabled && iPosition > iLimit) {
            throw new AssertionError();
        }
        int i2 = (iPosition <= iLimit ? iLimit - iPosition : 0) >> 2;
        return (unaligned || (this.address + ((long) iPosition)) % 4 == 0) ? this.nativeByteOrder ? new DirectFloatBufferRU(this, -1, 0, i2, i2, iPosition) : new DirectFloatBufferRS(this, -1, 0, i2, i2, iPosition) : this.bigEndian ? new ByteBufferAsFloatBufferRB(this, -1, 0, i2, i2, iPosition) : new ByteBufferAsFloatBufferRL(this, -1, 0, i2, i2, iPosition);
    }

    private ByteBuffer putDouble(long j2, double d2) {
        throw new ReadOnlyBufferException();
    }

    @Override // java.nio.DirectByteBuffer, java.nio.ByteBuffer
    public ByteBuffer putDouble(double d2) {
        throw new ReadOnlyBufferException();
    }

    @Override // java.nio.DirectByteBuffer, java.nio.ByteBuffer
    public ByteBuffer putDouble(int i2, double d2) {
        throw new ReadOnlyBufferException();
    }

    @Override // java.nio.DirectByteBuffer, java.nio.ByteBuffer
    public DoubleBuffer asDoubleBuffer() {
        int iPosition = position();
        int iLimit = limit();
        if (!$assertionsDisabled && iPosition > iLimit) {
            throw new AssertionError();
        }
        int i2 = (iPosition <= iLimit ? iLimit - iPosition : 0) >> 3;
        return (unaligned || (this.address + ((long) iPosition)) % 8 == 0) ? this.nativeByteOrder ? new DirectDoubleBufferRU(this, -1, 0, i2, i2, iPosition) : new DirectDoubleBufferRS(this, -1, 0, i2, i2, iPosition) : this.bigEndian ? new ByteBufferAsDoubleBufferRB(this, -1, 0, i2, i2, iPosition) : new ByteBufferAsDoubleBufferRL(this, -1, 0, i2, i2, iPosition);
    }
}
