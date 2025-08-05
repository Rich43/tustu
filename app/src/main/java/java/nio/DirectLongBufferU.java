package java.nio;

import sun.misc.Cleaner;
import sun.misc.Unsafe;
import sun.nio.ch.DirectBuffer;

/* loaded from: rt.jar:java/nio/DirectLongBufferU.class */
class DirectLongBufferU extends LongBuffer implements DirectBuffer {
    protected static final Unsafe unsafe;
    private static final long arrayBaseOffset;
    protected static final boolean unaligned;
    private final Object att;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !DirectLongBufferU.class.desiredAssertionStatus();
        unsafe = Bits.unsafe();
        arrayBaseOffset = unsafe.arrayBaseOffset(long[].class);
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

    DirectLongBufferU(DirectBuffer directBuffer, int i2, int i3, int i4, int i5, int i6) {
        super(i2, i3, i4, i5);
        this.address = directBuffer.address() + i6;
        this.att = directBuffer;
    }

    @Override // java.nio.LongBuffer
    public LongBuffer slice() {
        int iPosition = position();
        int iLimit = limit();
        int i2 = iPosition <= iLimit ? iLimit - iPosition : 0;
        int i3 = iPosition << 3;
        if ($assertionsDisabled || i3 >= 0) {
            return new DirectLongBufferU(this, -1, 0, i2, i2, i3);
        }
        throw new AssertionError();
    }

    @Override // java.nio.LongBuffer
    public LongBuffer duplicate() {
        return new DirectLongBufferU(this, markValue(), position(), limit(), capacity(), 0);
    }

    @Override // java.nio.LongBuffer
    public LongBuffer asReadOnlyBuffer() {
        return new DirectLongBufferRU(this, markValue(), position(), limit(), capacity(), 0);
    }

    @Override // sun.nio.ch.DirectBuffer
    public long address() {
        return this.address;
    }

    private long ix(int i2) {
        return this.address + (i2 << 3);
    }

    @Override // java.nio.LongBuffer
    public long get() {
        return unsafe.getLong(ix(nextGetIndex()));
    }

    @Override // java.nio.LongBuffer
    public long get(int i2) {
        return unsafe.getLong(ix(checkIndex(i2)));
    }

    @Override // java.nio.LongBuffer
    public LongBuffer get(long[] jArr, int i2, int i3) {
        if ((i3 << 3) > 6) {
            checkBounds(i2, i3, jArr.length);
            int iPosition = position();
            int iLimit = limit();
            if (!$assertionsDisabled && iPosition > iLimit) {
                throw new AssertionError();
            }
            if (i3 > (iPosition <= iLimit ? iLimit - iPosition : 0)) {
                throw new BufferUnderflowException();
            }
            if (order() != ByteOrder.nativeOrder()) {
                Bits.copyToLongArray(ix(iPosition), jArr, i2 << 3, i3 << 3);
            } else {
                Bits.copyToArray(ix(iPosition), jArr, arrayBaseOffset, i2 << 3, i3 << 3);
            }
            position(iPosition + i3);
        } else {
            super.get(jArr, i2, i3);
        }
        return this;
    }

    @Override // java.nio.LongBuffer
    public LongBuffer put(long j2) {
        unsafe.putLong(ix(nextPutIndex()), j2);
        return this;
    }

    @Override // java.nio.LongBuffer
    public LongBuffer put(int i2, long j2) {
        unsafe.putLong(ix(checkIndex(i2)), j2);
        return this;
    }

    @Override // java.nio.LongBuffer
    public LongBuffer put(LongBuffer longBuffer) {
        if (longBuffer instanceof DirectLongBufferU) {
            if (longBuffer == this) {
                throw new IllegalArgumentException();
            }
            DirectLongBufferU directLongBufferU = (DirectLongBufferU) longBuffer;
            int iPosition = directLongBufferU.position();
            int iLimit = directLongBufferU.limit();
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
            unsafe.copyMemory(directLongBufferU.ix(iPosition), ix(iPosition2), i2 << 3);
            directLongBufferU.position(iPosition + i2);
            position(iPosition2 + i2);
        } else if (longBuffer.hb != null) {
            int iPosition3 = longBuffer.position();
            int iLimit3 = longBuffer.limit();
            if (!$assertionsDisabled && iPosition3 > iLimit3) {
                throw new AssertionError();
            }
            int i3 = iPosition3 <= iLimit3 ? iLimit3 - iPosition3 : 0;
            put(longBuffer.hb, longBuffer.offset + iPosition3, i3);
            longBuffer.position(iPosition3 + i3);
        } else {
            super.put(longBuffer);
        }
        return this;
    }

    @Override // java.nio.LongBuffer
    public LongBuffer put(long[] jArr, int i2, int i3) {
        if ((i3 << 3) > 6) {
            checkBounds(i2, i3, jArr.length);
            int iPosition = position();
            int iLimit = limit();
            if (!$assertionsDisabled && iPosition > iLimit) {
                throw new AssertionError();
            }
            if (i3 > (iPosition <= iLimit ? iLimit - iPosition : 0)) {
                throw new BufferOverflowException();
            }
            if (order() != ByteOrder.nativeOrder()) {
                Bits.copyFromLongArray(jArr, i2 << 3, ix(iPosition), i3 << 3);
            } else {
                Bits.copyFromArray(jArr, arrayBaseOffset, i2 << 3, ix(iPosition), i3 << 3);
            }
            position(iPosition + i3);
        } else {
            super.put(jArr, i2, i3);
        }
        return this;
    }

    @Override // java.nio.LongBuffer
    public LongBuffer compact() {
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

    @Override // java.nio.LongBuffer, java.nio.Buffer
    public boolean isDirect() {
        return true;
    }

    @Override // java.nio.Buffer
    public boolean isReadOnly() {
        return false;
    }

    @Override // java.nio.LongBuffer
    public ByteOrder order() {
        return ByteOrder.nativeOrder() != ByteOrder.BIG_ENDIAN ? ByteOrder.LITTLE_ENDIAN : ByteOrder.BIG_ENDIAN;
    }
}
