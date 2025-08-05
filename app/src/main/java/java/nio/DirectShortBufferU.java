package java.nio;

import sun.misc.Cleaner;
import sun.misc.Unsafe;
import sun.nio.ch.DirectBuffer;

/* loaded from: rt.jar:java/nio/DirectShortBufferU.class */
class DirectShortBufferU extends ShortBuffer implements DirectBuffer {
    protected static final Unsafe unsafe;
    private static final long arrayBaseOffset;
    protected static final boolean unaligned;
    private final Object att;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !DirectShortBufferU.class.desiredAssertionStatus();
        unsafe = Bits.unsafe();
        arrayBaseOffset = unsafe.arrayBaseOffset(short[].class);
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

    DirectShortBufferU(DirectBuffer directBuffer, int i2, int i3, int i4, int i5, int i6) {
        super(i2, i3, i4, i5);
        this.address = directBuffer.address() + i6;
        this.att = directBuffer;
    }

    @Override // java.nio.ShortBuffer
    public ShortBuffer slice() {
        int iPosition = position();
        int iLimit = limit();
        int i2 = iPosition <= iLimit ? iLimit - iPosition : 0;
        int i3 = iPosition << 1;
        if ($assertionsDisabled || i3 >= 0) {
            return new DirectShortBufferU(this, -1, 0, i2, i2, i3);
        }
        throw new AssertionError();
    }

    @Override // java.nio.ShortBuffer
    public ShortBuffer duplicate() {
        return new DirectShortBufferU(this, markValue(), position(), limit(), capacity(), 0);
    }

    @Override // java.nio.ShortBuffer
    public ShortBuffer asReadOnlyBuffer() {
        return new DirectShortBufferRU(this, markValue(), position(), limit(), capacity(), 0);
    }

    @Override // sun.nio.ch.DirectBuffer
    public long address() {
        return this.address;
    }

    private long ix(int i2) {
        return this.address + (i2 << 1);
    }

    @Override // java.nio.ShortBuffer
    public short get() {
        return unsafe.getShort(ix(nextGetIndex()));
    }

    @Override // java.nio.ShortBuffer
    public short get(int i2) {
        return unsafe.getShort(ix(checkIndex(i2)));
    }

    @Override // java.nio.ShortBuffer
    public ShortBuffer get(short[] sArr, int i2, int i3) {
        if ((i3 << 1) > 6) {
            checkBounds(i2, i3, sArr.length);
            int iPosition = position();
            int iLimit = limit();
            if (!$assertionsDisabled && iPosition > iLimit) {
                throw new AssertionError();
            }
            if (i3 > (iPosition <= iLimit ? iLimit - iPosition : 0)) {
                throw new BufferUnderflowException();
            }
            if (order() != ByteOrder.nativeOrder()) {
                Bits.copyToShortArray(ix(iPosition), sArr, i2 << 1, i3 << 1);
            } else {
                Bits.copyToArray(ix(iPosition), sArr, arrayBaseOffset, i2 << 1, i3 << 1);
            }
            position(iPosition + i3);
        } else {
            super.get(sArr, i2, i3);
        }
        return this;
    }

    @Override // java.nio.ShortBuffer
    public ShortBuffer put(short s2) {
        unsafe.putShort(ix(nextPutIndex()), s2);
        return this;
    }

    @Override // java.nio.ShortBuffer
    public ShortBuffer put(int i2, short s2) {
        unsafe.putShort(ix(checkIndex(i2)), s2);
        return this;
    }

    @Override // java.nio.ShortBuffer
    public ShortBuffer put(ShortBuffer shortBuffer) {
        if (shortBuffer instanceof DirectShortBufferU) {
            if (shortBuffer == this) {
                throw new IllegalArgumentException();
            }
            DirectShortBufferU directShortBufferU = (DirectShortBufferU) shortBuffer;
            int iPosition = directShortBufferU.position();
            int iLimit = directShortBufferU.limit();
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
            unsafe.copyMemory(directShortBufferU.ix(iPosition), ix(iPosition2), i2 << 1);
            directShortBufferU.position(iPosition + i2);
            position(iPosition2 + i2);
        } else if (shortBuffer.hb != null) {
            int iPosition3 = shortBuffer.position();
            int iLimit3 = shortBuffer.limit();
            if (!$assertionsDisabled && iPosition3 > iLimit3) {
                throw new AssertionError();
            }
            int i3 = iPosition3 <= iLimit3 ? iLimit3 - iPosition3 : 0;
            put(shortBuffer.hb, shortBuffer.offset + iPosition3, i3);
            shortBuffer.position(iPosition3 + i3);
        } else {
            super.put(shortBuffer);
        }
        return this;
    }

    @Override // java.nio.ShortBuffer
    public ShortBuffer put(short[] sArr, int i2, int i3) {
        if ((i3 << 1) > 6) {
            checkBounds(i2, i3, sArr.length);
            int iPosition = position();
            int iLimit = limit();
            if (!$assertionsDisabled && iPosition > iLimit) {
                throw new AssertionError();
            }
            if (i3 > (iPosition <= iLimit ? iLimit - iPosition : 0)) {
                throw new BufferOverflowException();
            }
            if (order() != ByteOrder.nativeOrder()) {
                Bits.copyFromShortArray(sArr, i2 << 1, ix(iPosition), i3 << 1);
            } else {
                Bits.copyFromArray(sArr, arrayBaseOffset, i2 << 1, ix(iPosition), i3 << 1);
            }
            position(iPosition + i3);
        } else {
            super.put(sArr, i2, i3);
        }
        return this;
    }

    @Override // java.nio.ShortBuffer
    public ShortBuffer compact() {
        int iPosition = position();
        int iLimit = limit();
        if (!$assertionsDisabled && iPosition > iLimit) {
            throw new AssertionError();
        }
        int i2 = iPosition <= iLimit ? iLimit - iPosition : 0;
        unsafe.copyMemory(ix(iPosition), ix(0), i2 << 1);
        position(i2);
        limit(capacity());
        discardMark();
        return this;
    }

    @Override // java.nio.ShortBuffer, java.nio.Buffer
    public boolean isDirect() {
        return true;
    }

    @Override // java.nio.Buffer
    public boolean isReadOnly() {
        return false;
    }

    @Override // java.nio.ShortBuffer
    public ByteOrder order() {
        return ByteOrder.nativeOrder() != ByteOrder.BIG_ENDIAN ? ByteOrder.LITTLE_ENDIAN : ByteOrder.BIG_ENDIAN;
    }
}
