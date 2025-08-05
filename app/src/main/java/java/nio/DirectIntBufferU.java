package java.nio;

import sun.misc.Cleaner;
import sun.misc.Unsafe;
import sun.nio.ch.DirectBuffer;

/* loaded from: rt.jar:java/nio/DirectIntBufferU.class */
class DirectIntBufferU extends IntBuffer implements DirectBuffer {
    protected static final Unsafe unsafe;
    private static final long arrayBaseOffset;
    protected static final boolean unaligned;
    private final Object att;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !DirectIntBufferU.class.desiredAssertionStatus();
        unsafe = Bits.unsafe();
        arrayBaseOffset = unsafe.arrayBaseOffset(int[].class);
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

    DirectIntBufferU(DirectBuffer directBuffer, int i2, int i3, int i4, int i5, int i6) {
        super(i2, i3, i4, i5);
        this.address = directBuffer.address() + i6;
        this.att = directBuffer;
    }

    @Override // java.nio.IntBuffer
    public IntBuffer slice() {
        int iPosition = position();
        int iLimit = limit();
        int i2 = iPosition <= iLimit ? iLimit - iPosition : 0;
        int i3 = iPosition << 2;
        if ($assertionsDisabled || i3 >= 0) {
            return new DirectIntBufferU(this, -1, 0, i2, i2, i3);
        }
        throw new AssertionError();
    }

    @Override // java.nio.IntBuffer
    public IntBuffer duplicate() {
        return new DirectIntBufferU(this, markValue(), position(), limit(), capacity(), 0);
    }

    @Override // java.nio.IntBuffer
    public IntBuffer asReadOnlyBuffer() {
        return new DirectIntBufferRU(this, markValue(), position(), limit(), capacity(), 0);
    }

    @Override // sun.nio.ch.DirectBuffer
    public long address() {
        return this.address;
    }

    private long ix(int i2) {
        return this.address + (i2 << 2);
    }

    @Override // java.nio.IntBuffer
    public int get() {
        return unsafe.getInt(ix(nextGetIndex()));
    }

    @Override // java.nio.IntBuffer
    public int get(int i2) {
        return unsafe.getInt(ix(checkIndex(i2)));
    }

    @Override // java.nio.IntBuffer
    public IntBuffer get(int[] iArr, int i2, int i3) {
        if ((i3 << 2) > 6) {
            checkBounds(i2, i3, iArr.length);
            int iPosition = position();
            int iLimit = limit();
            if (!$assertionsDisabled && iPosition > iLimit) {
                throw new AssertionError();
            }
            if (i3 > (iPosition <= iLimit ? iLimit - iPosition : 0)) {
                throw new BufferUnderflowException();
            }
            if (order() != ByteOrder.nativeOrder()) {
                Bits.copyToIntArray(ix(iPosition), iArr, i2 << 2, i3 << 2);
            } else {
                Bits.copyToArray(ix(iPosition), iArr, arrayBaseOffset, i2 << 2, i3 << 2);
            }
            position(iPosition + i3);
        } else {
            super.get(iArr, i2, i3);
        }
        return this;
    }

    @Override // java.nio.IntBuffer
    public IntBuffer put(int i2) {
        unsafe.putInt(ix(nextPutIndex()), i2);
        return this;
    }

    @Override // java.nio.IntBuffer
    public IntBuffer put(int i2, int i3) {
        unsafe.putInt(ix(checkIndex(i2)), i3);
        return this;
    }

    @Override // java.nio.IntBuffer
    public IntBuffer put(IntBuffer intBuffer) {
        if (intBuffer instanceof DirectIntBufferU) {
            if (intBuffer == this) {
                throw new IllegalArgumentException();
            }
            DirectIntBufferU directIntBufferU = (DirectIntBufferU) intBuffer;
            int iPosition = directIntBufferU.position();
            int iLimit = directIntBufferU.limit();
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
            unsafe.copyMemory(directIntBufferU.ix(iPosition), ix(iPosition2), i2 << 2);
            directIntBufferU.position(iPosition + i2);
            position(iPosition2 + i2);
        } else if (intBuffer.hb != null) {
            int iPosition3 = intBuffer.position();
            int iLimit3 = intBuffer.limit();
            if (!$assertionsDisabled && iPosition3 > iLimit3) {
                throw new AssertionError();
            }
            int i3 = iPosition3 <= iLimit3 ? iLimit3 - iPosition3 : 0;
            put(intBuffer.hb, intBuffer.offset + iPosition3, i3);
            intBuffer.position(iPosition3 + i3);
        } else {
            super.put(intBuffer);
        }
        return this;
    }

    @Override // java.nio.IntBuffer
    public IntBuffer put(int[] iArr, int i2, int i3) {
        if ((i3 << 2) > 6) {
            checkBounds(i2, i3, iArr.length);
            int iPosition = position();
            int iLimit = limit();
            if (!$assertionsDisabled && iPosition > iLimit) {
                throw new AssertionError();
            }
            if (i3 > (iPosition <= iLimit ? iLimit - iPosition : 0)) {
                throw new BufferOverflowException();
            }
            if (order() != ByteOrder.nativeOrder()) {
                Bits.copyFromIntArray(iArr, i2 << 2, ix(iPosition), i3 << 2);
            } else {
                Bits.copyFromArray(iArr, arrayBaseOffset, i2 << 2, ix(iPosition), i3 << 2);
            }
            position(iPosition + i3);
        } else {
            super.put(iArr, i2, i3);
        }
        return this;
    }

    @Override // java.nio.IntBuffer
    public IntBuffer compact() {
        int iPosition = position();
        int iLimit = limit();
        if (!$assertionsDisabled && iPosition > iLimit) {
            throw new AssertionError();
        }
        int i2 = iPosition <= iLimit ? iLimit - iPosition : 0;
        unsafe.copyMemory(ix(iPosition), ix(0), i2 << 2);
        position(i2);
        limit(capacity());
        discardMark();
        return this;
    }

    @Override // java.nio.IntBuffer, java.nio.Buffer
    public boolean isDirect() {
        return true;
    }

    @Override // java.nio.Buffer
    public boolean isReadOnly() {
        return false;
    }

    @Override // java.nio.IntBuffer
    public ByteOrder order() {
        return ByteOrder.nativeOrder() != ByteOrder.BIG_ENDIAN ? ByteOrder.LITTLE_ENDIAN : ByteOrder.BIG_ENDIAN;
    }
}
