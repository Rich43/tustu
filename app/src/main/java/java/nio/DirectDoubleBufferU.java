package java.nio;

import sun.misc.Cleaner;
import sun.misc.Unsafe;
import sun.nio.ch.DirectBuffer;

/* loaded from: rt.jar:java/nio/DirectDoubleBufferU.class */
class DirectDoubleBufferU extends DoubleBuffer implements DirectBuffer {
    protected static final Unsafe unsafe;
    private static final long arrayBaseOffset;
    protected static final boolean unaligned;
    private final Object att;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !DirectDoubleBufferU.class.desiredAssertionStatus();
        unsafe = Bits.unsafe();
        arrayBaseOffset = unsafe.arrayBaseOffset(double[].class);
        unaligned = Bits.unaligned();
    }

    @Override // sun.nio.ch.DirectBuffer
    public Object attachment() {
        return this.att;
    }

    @Override // sun.nio.ch.DirectBuffer
    public Cleaner cleaner() {
        return null;
    }

    DirectDoubleBufferU(DirectBuffer directBuffer, int i2, int i3, int i4, int i5, int i6) {
        super(i2, i3, i4, i5);
        this.address = directBuffer.address() + i6;
        this.att = directBuffer;
    }

    @Override // java.nio.DoubleBuffer
    public DoubleBuffer slice() {
        int iPosition = position();
        int iLimit = limit();
        int i2 = iPosition <= iLimit ? iLimit - iPosition : 0;
        int i3 = iPosition << 3;
        if ($assertionsDisabled || i3 >= 0) {
            return new DirectDoubleBufferU(this, -1, 0, i2, i2, i3);
        }
        throw new AssertionError();
    }

    @Override // java.nio.DoubleBuffer
    public DoubleBuffer duplicate() {
        return new DirectDoubleBufferU(this, markValue(), position(), limit(), capacity(), 0);
    }

    @Override // java.nio.DoubleBuffer
    public DoubleBuffer asReadOnlyBuffer() {
        return new DirectDoubleBufferRU(this, markValue(), position(), limit(), capacity(), 0);
    }

    @Override // sun.nio.ch.DirectBuffer
    public long address() {
        return this.address;
    }

    private long ix(int i2) {
        return this.address + (i2 << 3);
    }

    @Override // java.nio.DoubleBuffer
    public double get() {
        return unsafe.getDouble(ix(nextGetIndex()));
    }

    @Override // java.nio.DoubleBuffer
    public double get(int i2) {
        return unsafe.getDouble(ix(checkIndex(i2)));
    }

    @Override // java.nio.DoubleBuffer
    public DoubleBuffer get(double[] dArr, int i2, int i3) {
        if ((i3 << 3) > 6) {
            checkBounds(i2, i3, dArr.length);
            int iPosition = position();
            int iLimit = limit();
            if (!$assertionsDisabled && iPosition > iLimit) {
                throw new AssertionError();
            }
            if (i3 > (iPosition <= iLimit ? iLimit - iPosition : 0)) {
                throw new BufferUnderflowException();
            }
            if (order() != ByteOrder.nativeOrder()) {
                Bits.copyToLongArray(ix(iPosition), dArr, i2 << 3, i3 << 3);
            } else {
                Bits.copyToArray(ix(iPosition), dArr, arrayBaseOffset, i2 << 3, i3 << 3);
            }
            position(iPosition + i3);
        } else {
            super.get(dArr, i2, i3);
        }
        return this;
    }

    @Override // java.nio.DoubleBuffer
    public DoubleBuffer put(double d2) {
        unsafe.putDouble(ix(nextPutIndex()), d2);
        return this;
    }

    @Override // java.nio.DoubleBuffer
    public DoubleBuffer put(int i2, double d2) {
        unsafe.putDouble(ix(checkIndex(i2)), d2);
        return this;
    }

    @Override // java.nio.DoubleBuffer
    public DoubleBuffer put(DoubleBuffer doubleBuffer) {
        if (doubleBuffer instanceof DirectDoubleBufferU) {
            if (doubleBuffer == this) {
                throw new IllegalArgumentException();
            }
            DirectDoubleBufferU directDoubleBufferU = (DirectDoubleBufferU) doubleBuffer;
            int iPosition = directDoubleBufferU.position();
            int iLimit = directDoubleBufferU.limit();
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
            unsafe.copyMemory(directDoubleBufferU.ix(iPosition), ix(iPosition2), i2 << 3);
            directDoubleBufferU.position(iPosition + i2);
            position(iPosition2 + i2);
        } else if (doubleBuffer.hb != null) {
            int iPosition3 = doubleBuffer.position();
            int iLimit3 = doubleBuffer.limit();
            if (!$assertionsDisabled && iPosition3 > iLimit3) {
                throw new AssertionError();
            }
            int i3 = iPosition3 <= iLimit3 ? iLimit3 - iPosition3 : 0;
            put(doubleBuffer.hb, doubleBuffer.offset + iPosition3, i3);
            doubleBuffer.position(iPosition3 + i3);
        } else {
            super.put(doubleBuffer);
        }
        return this;
    }

    @Override // java.nio.DoubleBuffer
    public DoubleBuffer put(double[] dArr, int i2, int i3) {
        if ((i3 << 3) > 6) {
            checkBounds(i2, i3, dArr.length);
            int iPosition = position();
            int iLimit = limit();
            if (!$assertionsDisabled && iPosition > iLimit) {
                throw new AssertionError();
            }
            if (i3 > (iPosition <= iLimit ? iLimit - iPosition : 0)) {
                throw new BufferOverflowException();
            }
            if (order() != ByteOrder.nativeOrder()) {
                Bits.copyFromLongArray(dArr, i2 << 3, ix(iPosition), i3 << 3);
            } else {
                Bits.copyFromArray(dArr, arrayBaseOffset, i2 << 3, ix(iPosition), i3 << 3);
            }
            position(iPosition + i3);
        } else {
            super.put(dArr, i2, i3);
        }
        return this;
    }

    @Override // java.nio.DoubleBuffer
    public DoubleBuffer compact() {
        int iPosition = position();
        int iLimit = limit();
        if (!$assertionsDisabled && iPosition > iLimit) {
            throw new AssertionError();
        }
        int i2 = iPosition <= iLimit ? iLimit - iPosition : 0;
        unsafe.copyMemory(ix(iPosition), ix(0), i2 << 3);
        position(i2);
        limit(capacity());
        discardMark();
        return this;
    }

    @Override // java.nio.DoubleBuffer, java.nio.Buffer
    public boolean isDirect() {
        return true;
    }

    @Override // java.nio.Buffer
    public boolean isReadOnly() {
        return false;
    }

    @Override // java.nio.DoubleBuffer
    public ByteOrder order() {
        return ByteOrder.nativeOrder() != ByteOrder.BIG_ENDIAN ? ByteOrder.LITTLE_ENDIAN : ByteOrder.BIG_ENDIAN;
    }
}
