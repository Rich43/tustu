package java.nio;

import java.io.FileDescriptor;
import sun.misc.Cleaner;
import sun.misc.Unsafe;
import sun.misc.VM;
import sun.nio.ch.DirectBuffer;

/* loaded from: rt.jar:java/nio/DirectByteBuffer.class */
class DirectByteBuffer extends MappedByteBuffer implements DirectBuffer {
    protected static final Unsafe unsafe;
    private static final long arrayBaseOffset;
    protected static final boolean unaligned;
    private final Object att;
    private final Cleaner cleaner;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !DirectByteBuffer.class.desiredAssertionStatus();
        unsafe = Bits.unsafe();
        arrayBaseOffset = unsafe.arrayBaseOffset(byte[].class);
        unaligned = Bits.unaligned();
    }

    @Override // sun.nio.ch.DirectBuffer
    public Object attachment() {
        return this.att;
    }

    /* loaded from: rt.jar:java/nio/DirectByteBuffer$Deallocator.class */
    private static class Deallocator implements Runnable {
        private static Unsafe unsafe;
        private long address;
        private long size;
        private int capacity;
        static final /* synthetic */ boolean $assertionsDisabled;

        static {
            $assertionsDisabled = !DirectByteBuffer.class.desiredAssertionStatus();
            unsafe = Unsafe.getUnsafe();
        }

        private Deallocator(long j2, long j3, int i2) {
            if (!$assertionsDisabled && j2 == 0) {
                throw new AssertionError();
            }
            this.address = j2;
            this.size = j3;
            this.capacity = i2;
        }

        @Override // java.lang.Runnable
        public void run() {
            if (this.address == 0) {
                return;
            }
            unsafe.freeMemory(this.address);
            this.address = 0L;
            Bits.unreserveMemory(this.size, this.capacity);
        }
    }

    @Override // sun.nio.ch.DirectBuffer
    public Cleaner cleaner() {
        return this.cleaner;
    }

    DirectByteBuffer(int i2) {
        super(-1, 0, i2, i2);
        boolean zIsDirectMemoryPageAligned = VM.isDirectMemoryPageAligned();
        int iPageSize = Bits.pageSize();
        long jMax = Math.max(1L, i2 + (zIsDirectMemoryPageAligned ? iPageSize : 0));
        Bits.reserveMemory(jMax, i2);
        try {
            long jAllocateMemory = unsafe.allocateMemory(jMax);
            unsafe.setMemory(jAllocateMemory, jMax, (byte) 0);
            if (zIsDirectMemoryPageAligned && jAllocateMemory % iPageSize != 0) {
                this.address = (jAllocateMemory + iPageSize) - (jAllocateMemory & (iPageSize - 1));
            } else {
                this.address = jAllocateMemory;
            }
            this.cleaner = Cleaner.create(this, new Deallocator(jAllocateMemory, jMax, i2));
            this.att = null;
        } catch (OutOfMemoryError e2) {
            Bits.unreserveMemory(jMax, i2);
            throw e2;
        }
    }

    DirectByteBuffer(long j2, int i2, Object obj) {
        super(-1, 0, i2, i2);
        this.address = j2;
        this.cleaner = null;
        this.att = obj;
    }

    private DirectByteBuffer(long j2, int i2) {
        super(-1, 0, i2, i2);
        this.address = j2;
        this.cleaner = null;
        this.att = null;
    }

    protected DirectByteBuffer(int i2, long j2, FileDescriptor fileDescriptor, Runnable runnable) {
        super(-1, 0, i2, i2, fileDescriptor);
        this.address = j2;
        this.cleaner = Cleaner.create(this, runnable);
        this.att = null;
    }

    DirectByteBuffer(DirectBuffer directBuffer, int i2, int i3, int i4, int i5, int i6) {
        super(i2, i3, i4, i5);
        this.address = directBuffer.address() + i6;
        this.cleaner = null;
        this.att = directBuffer;
    }

    @Override // java.nio.ByteBuffer
    public ByteBuffer slice() {
        int iPosition = position();
        int iLimit = limit();
        int i2 = iPosition <= iLimit ? iLimit - iPosition : 0;
        int i3 = iPosition << 0;
        if ($assertionsDisabled || i3 >= 0) {
            return new DirectByteBuffer(this, -1, 0, i2, i2, i3);
        }
        throw new AssertionError();
    }

    @Override // java.nio.ByteBuffer
    public ByteBuffer duplicate() {
        return new DirectByteBuffer(this, markValue(), position(), limit(), capacity(), 0);
    }

    @Override // java.nio.ByteBuffer
    public ByteBuffer asReadOnlyBuffer() {
        return new DirectByteBufferR(this, markValue(), position(), limit(), capacity(), 0);
    }

    @Override // sun.nio.ch.DirectBuffer
    public long address() {
        return this.address;
    }

    private long ix(int i2) {
        return this.address + (i2 << 0);
    }

    @Override // java.nio.ByteBuffer
    public byte get() {
        return unsafe.getByte(ix(nextGetIndex()));
    }

    @Override // java.nio.ByteBuffer
    public byte get(int i2) {
        return unsafe.getByte(ix(checkIndex(i2)));
    }

    @Override // java.nio.ByteBuffer
    public ByteBuffer get(byte[] bArr, int i2, int i3) {
        if ((i3 << 0) > 6) {
            checkBounds(i2, i3, bArr.length);
            int iPosition = position();
            int iLimit = limit();
            if (!$assertionsDisabled && iPosition > iLimit) {
                throw new AssertionError();
            }
            if (i3 > (iPosition <= iLimit ? iLimit - iPosition : 0)) {
                throw new BufferUnderflowException();
            }
            Bits.copyToArray(ix(iPosition), bArr, arrayBaseOffset, i2 << 0, i3 << 0);
            position(iPosition + i3);
        } else {
            super.get(bArr, i2, i3);
        }
        return this;
    }

    @Override // java.nio.ByteBuffer
    public ByteBuffer put(byte b2) {
        unsafe.putByte(ix(nextPutIndex()), b2);
        return this;
    }

    @Override // java.nio.ByteBuffer
    public ByteBuffer put(int i2, byte b2) {
        unsafe.putByte(ix(checkIndex(i2)), b2);
        return this;
    }

    @Override // java.nio.ByteBuffer
    public ByteBuffer put(ByteBuffer byteBuffer) {
        if (byteBuffer instanceof DirectByteBuffer) {
            if (byteBuffer == this) {
                throw new IllegalArgumentException();
            }
            DirectByteBuffer directByteBuffer = (DirectByteBuffer) byteBuffer;
            int iPosition = directByteBuffer.position();
            int iLimit = directByteBuffer.limit();
            if (!$assertionsDisabled && iPosition > iLimit) {
                throw new AssertionError();
            }
            int i2 = iPosition <= iLimit ? iLimit - iPosition : 0;
            int iPosition2 = position();
            int iLimit2 = limit();
            if (!$assertionsDisabled && iPosition2 > iLimit2) {
                throw new AssertionError();
            }
            if (i2 > (iPosition2 <= iLimit2 ? iLimit2 - iPosition2 : 0)) {
                throw new BufferOverflowException();
            }
            unsafe.copyMemory(directByteBuffer.ix(iPosition), ix(iPosition2), i2 << 0);
            directByteBuffer.position(iPosition + i2);
            position(iPosition2 + i2);
        } else if (byteBuffer.hb != null) {
            int iPosition3 = byteBuffer.position();
            int iLimit3 = byteBuffer.limit();
            if (!$assertionsDisabled && iPosition3 > iLimit3) {
                throw new AssertionError();
            }
            int i3 = iPosition3 <= iLimit3 ? iLimit3 - iPosition3 : 0;
            put(byteBuffer.hb, byteBuffer.offset + iPosition3, i3);
            byteBuffer.position(iPosition3 + i3);
        } else {
            super.put(byteBuffer);
        }
        return this;
    }

    @Override // java.nio.ByteBuffer
    public ByteBuffer put(byte[] bArr, int i2, int i3) {
        if ((i3 << 0) > 6) {
            checkBounds(i2, i3, bArr.length);
            int iPosition = position();
            int iLimit = limit();
            if (!$assertionsDisabled && iPosition > iLimit) {
                throw new AssertionError();
            }
            if (i3 > (iPosition <= iLimit ? iLimit - iPosition : 0)) {
                throw new BufferOverflowException();
            }
            Bits.copyFromArray(bArr, arrayBaseOffset, i2 << 0, ix(iPosition), i3 << 0);
            position(iPosition + i3);
        } else {
            super.put(bArr, i2, i3);
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
        unsafe.copyMemory(ix(iPosition), ix(0), i2 << 0);
        position(i2);
        limit(capacity());
        discardMark();
        return this;
    }

    @Override // java.nio.ByteBuffer, java.nio.Buffer
    public boolean isDirect() {
        return true;
    }

    @Override // java.nio.Buffer
    public boolean isReadOnly() {
        return false;
    }

    @Override // java.nio.ByteBuffer
    byte _get(int i2) {
        return unsafe.getByte(this.address + i2);
    }

    @Override // java.nio.ByteBuffer
    void _put(int i2, byte b2) {
        unsafe.putByte(this.address + i2, b2);
    }

    private char getChar(long j2) {
        if (unaligned) {
            char c2 = unsafe.getChar(j2);
            return this.nativeByteOrder ? c2 : Bits.swap(c2);
        }
        return Bits.getChar(j2, this.bigEndian);
    }

    @Override // java.nio.ByteBuffer
    public char getChar() {
        return getChar(ix(nextGetIndex(2)));
    }

    @Override // java.nio.ByteBuffer
    public char getChar(int i2) {
        return getChar(ix(checkIndex(i2, 2)));
    }

    private ByteBuffer putChar(long j2, char c2) {
        if (unaligned) {
            unsafe.putChar(j2, this.nativeByteOrder ? c2 : Bits.swap(c2));
        } else {
            Bits.putChar(j2, c2, this.bigEndian);
        }
        return this;
    }

    @Override // java.nio.ByteBuffer
    public ByteBuffer putChar(char c2) {
        putChar(ix(nextPutIndex(2)), c2);
        return this;
    }

    @Override // java.nio.ByteBuffer
    public ByteBuffer putChar(int i2, char c2) {
        putChar(ix(checkIndex(i2, 2)), c2);
        return this;
    }

    @Override // java.nio.ByteBuffer
    public CharBuffer asCharBuffer() {
        int iPosition = position();
        int iLimit = limit();
        if (!$assertionsDisabled && iPosition > iLimit) {
            throw new AssertionError();
        }
        int i2 = (iPosition <= iLimit ? iLimit - iPosition : 0) >> 1;
        return (unaligned || (this.address + ((long) iPosition)) % 2 == 0) ? this.nativeByteOrder ? new DirectCharBufferU(this, -1, 0, i2, i2, iPosition) : new DirectCharBufferS(this, -1, 0, i2, i2, iPosition) : this.bigEndian ? new ByteBufferAsCharBufferB(this, -1, 0, i2, i2, iPosition) : new ByteBufferAsCharBufferL(this, -1, 0, i2, i2, iPosition);
    }

    private short getShort(long j2) {
        if (unaligned) {
            short s2 = unsafe.getShort(j2);
            return this.nativeByteOrder ? s2 : Bits.swap(s2);
        }
        return Bits.getShort(j2, this.bigEndian);
    }

    @Override // java.nio.ByteBuffer
    public short getShort() {
        return getShort(ix(nextGetIndex(2)));
    }

    @Override // java.nio.ByteBuffer
    public short getShort(int i2) {
        return getShort(ix(checkIndex(i2, 2)));
    }

    private ByteBuffer putShort(long j2, short s2) {
        if (unaligned) {
            unsafe.putShort(j2, this.nativeByteOrder ? s2 : Bits.swap(s2));
        } else {
            Bits.putShort(j2, s2, this.bigEndian);
        }
        return this;
    }

    @Override // java.nio.ByteBuffer
    public ByteBuffer putShort(short s2) {
        putShort(ix(nextPutIndex(2)), s2);
        return this;
    }

    @Override // java.nio.ByteBuffer
    public ByteBuffer putShort(int i2, short s2) {
        putShort(ix(checkIndex(i2, 2)), s2);
        return this;
    }

    @Override // java.nio.ByteBuffer
    public ShortBuffer asShortBuffer() {
        int iPosition = position();
        int iLimit = limit();
        if (!$assertionsDisabled && iPosition > iLimit) {
            throw new AssertionError();
        }
        int i2 = (iPosition <= iLimit ? iLimit - iPosition : 0) >> 1;
        return (unaligned || (this.address + ((long) iPosition)) % 2 == 0) ? this.nativeByteOrder ? new DirectShortBufferU(this, -1, 0, i2, i2, iPosition) : new DirectShortBufferS(this, -1, 0, i2, i2, iPosition) : this.bigEndian ? new ByteBufferAsShortBufferB(this, -1, 0, i2, i2, iPosition) : new ByteBufferAsShortBufferL(this, -1, 0, i2, i2, iPosition);
    }

    private int getInt(long j2) {
        if (unaligned) {
            int i2 = unsafe.getInt(j2);
            return this.nativeByteOrder ? i2 : Bits.swap(i2);
        }
        return Bits.getInt(j2, this.bigEndian);
    }

    @Override // java.nio.ByteBuffer
    public int getInt() {
        return getInt(ix(nextGetIndex(4)));
    }

    @Override // java.nio.ByteBuffer
    public int getInt(int i2) {
        return getInt(ix(checkIndex(i2, 4)));
    }

    private ByteBuffer putInt(long j2, int i2) {
        if (unaligned) {
            unsafe.putInt(j2, this.nativeByteOrder ? i2 : Bits.swap(i2));
        } else {
            Bits.putInt(j2, i2, this.bigEndian);
        }
        return this;
    }

    @Override // java.nio.ByteBuffer
    public ByteBuffer putInt(int i2) {
        putInt(ix(nextPutIndex(4)), i2);
        return this;
    }

    @Override // java.nio.ByteBuffer
    public ByteBuffer putInt(int i2, int i3) {
        putInt(ix(checkIndex(i2, 4)), i3);
        return this;
    }

    @Override // java.nio.ByteBuffer
    public IntBuffer asIntBuffer() {
        int iPosition = position();
        int iLimit = limit();
        if (!$assertionsDisabled && iPosition > iLimit) {
            throw new AssertionError();
        }
        int i2 = (iPosition <= iLimit ? iLimit - iPosition : 0) >> 2;
        return (unaligned || (this.address + ((long) iPosition)) % 4 == 0) ? this.nativeByteOrder ? new DirectIntBufferU(this, -1, 0, i2, i2, iPosition) : new DirectIntBufferS(this, -1, 0, i2, i2, iPosition) : this.bigEndian ? new ByteBufferAsIntBufferB(this, -1, 0, i2, i2, iPosition) : new ByteBufferAsIntBufferL(this, -1, 0, i2, i2, iPosition);
    }

    private long getLong(long j2) {
        if (unaligned) {
            long j3 = unsafe.getLong(j2);
            return this.nativeByteOrder ? j3 : Bits.swap(j3);
        }
        return Bits.getLong(j2, this.bigEndian);
    }

    @Override // java.nio.ByteBuffer
    public long getLong() {
        return getLong(ix(nextGetIndex(8)));
    }

    @Override // java.nio.ByteBuffer
    public long getLong(int i2) {
        return getLong(ix(checkIndex(i2, 8)));
    }

    private ByteBuffer putLong(long j2, long j3) {
        if (unaligned) {
            unsafe.putLong(j2, this.nativeByteOrder ? j3 : Bits.swap(j3));
        } else {
            Bits.putLong(j2, j3, this.bigEndian);
        }
        return this;
    }

    @Override // java.nio.ByteBuffer
    public ByteBuffer putLong(long j2) {
        putLong(ix(nextPutIndex(8)), j2);
        return this;
    }

    @Override // java.nio.ByteBuffer
    public ByteBuffer putLong(int i2, long j2) {
        putLong(ix(checkIndex(i2, 8)), j2);
        return this;
    }

    @Override // java.nio.ByteBuffer
    public LongBuffer asLongBuffer() {
        int iPosition = position();
        int iLimit = limit();
        if (!$assertionsDisabled && iPosition > iLimit) {
            throw new AssertionError();
        }
        int i2 = (iPosition <= iLimit ? iLimit - iPosition : 0) >> 3;
        return (unaligned || (this.address + ((long) iPosition)) % 8 == 0) ? this.nativeByteOrder ? new DirectLongBufferU(this, -1, 0, i2, i2, iPosition) : new DirectLongBufferS(this, -1, 0, i2, i2, iPosition) : this.bigEndian ? new ByteBufferAsLongBufferB(this, -1, 0, i2, i2, iPosition) : new ByteBufferAsLongBufferL(this, -1, 0, i2, i2, iPosition);
    }

    private float getFloat(long j2) {
        if (unaligned) {
            int i2 = unsafe.getInt(j2);
            return Float.intBitsToFloat(this.nativeByteOrder ? i2 : Bits.swap(i2));
        }
        return Bits.getFloat(j2, this.bigEndian);
    }

    @Override // java.nio.ByteBuffer
    public float getFloat() {
        return getFloat(ix(nextGetIndex(4)));
    }

    @Override // java.nio.ByteBuffer
    public float getFloat(int i2) {
        return getFloat(ix(checkIndex(i2, 4)));
    }

    private ByteBuffer putFloat(long j2, float f2) {
        if (unaligned) {
            int iFloatToRawIntBits = Float.floatToRawIntBits(f2);
            unsafe.putInt(j2, this.nativeByteOrder ? iFloatToRawIntBits : Bits.swap(iFloatToRawIntBits));
        } else {
            Bits.putFloat(j2, f2, this.bigEndian);
        }
        return this;
    }

    @Override // java.nio.ByteBuffer
    public ByteBuffer putFloat(float f2) {
        putFloat(ix(nextPutIndex(4)), f2);
        return this;
    }

    @Override // java.nio.ByteBuffer
    public ByteBuffer putFloat(int i2, float f2) {
        putFloat(ix(checkIndex(i2, 4)), f2);
        return this;
    }

    @Override // java.nio.ByteBuffer
    public FloatBuffer asFloatBuffer() {
        int iPosition = position();
        int iLimit = limit();
        if (!$assertionsDisabled && iPosition > iLimit) {
            throw new AssertionError();
        }
        int i2 = (iPosition <= iLimit ? iLimit - iPosition : 0) >> 2;
        return (unaligned || (this.address + ((long) iPosition)) % 4 == 0) ? this.nativeByteOrder ? new DirectFloatBufferU(this, -1, 0, i2, i2, iPosition) : new DirectFloatBufferS(this, -1, 0, i2, i2, iPosition) : this.bigEndian ? new ByteBufferAsFloatBufferB(this, -1, 0, i2, i2, iPosition) : new ByteBufferAsFloatBufferL(this, -1, 0, i2, i2, iPosition);
    }

    private double getDouble(long j2) {
        if (unaligned) {
            long j3 = unsafe.getLong(j2);
            return Double.longBitsToDouble(this.nativeByteOrder ? j3 : Bits.swap(j3));
        }
        return Bits.getDouble(j2, this.bigEndian);
    }

    @Override // java.nio.ByteBuffer
    public double getDouble() {
        return getDouble(ix(nextGetIndex(8)));
    }

    @Override // java.nio.ByteBuffer
    public double getDouble(int i2) {
        return getDouble(ix(checkIndex(i2, 8)));
    }

    private ByteBuffer putDouble(long j2, double d2) {
        if (unaligned) {
            long jDoubleToRawLongBits = Double.doubleToRawLongBits(d2);
            unsafe.putLong(j2, this.nativeByteOrder ? jDoubleToRawLongBits : Bits.swap(jDoubleToRawLongBits));
        } else {
            Bits.putDouble(j2, d2, this.bigEndian);
        }
        return this;
    }

    @Override // java.nio.ByteBuffer
    public ByteBuffer putDouble(double d2) {
        putDouble(ix(nextPutIndex(8)), d2);
        return this;
    }

    @Override // java.nio.ByteBuffer
    public ByteBuffer putDouble(int i2, double d2) {
        putDouble(ix(checkIndex(i2, 8)), d2);
        return this;
    }

    @Override // java.nio.ByteBuffer
    public DoubleBuffer asDoubleBuffer() {
        int iPosition = position();
        int iLimit = limit();
        if (!$assertionsDisabled && iPosition > iLimit) {
            throw new AssertionError();
        }
        int i2 = (iPosition <= iLimit ? iLimit - iPosition : 0) >> 3;
        return (unaligned || (this.address + ((long) iPosition)) % 8 == 0) ? this.nativeByteOrder ? new DirectDoubleBufferU(this, -1, 0, i2, i2, iPosition) : new DirectDoubleBufferS(this, -1, 0, i2, i2, iPosition) : this.bigEndian ? new ByteBufferAsDoubleBufferB(this, -1, 0, i2, i2, iPosition) : new ByteBufferAsDoubleBufferL(this, -1, 0, i2, i2, iPosition);
    }
}
