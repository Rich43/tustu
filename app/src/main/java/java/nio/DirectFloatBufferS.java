package java.nio;

import sun.misc.Cleaner;
import sun.misc.Unsafe;
import sun.nio.ch.DirectBuffer;

/* loaded from: rt.jar:java/nio/DirectFloatBufferS.class */
class DirectFloatBufferS extends FloatBuffer implements DirectBuffer {
    protected static final Unsafe unsafe;
    private static final long arrayBaseOffset;
    protected static final boolean unaligned;
    private final Object att;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !DirectFloatBufferS.class.desiredAssertionStatus();
        unsafe = Bits.unsafe();
        arrayBaseOffset = unsafe.arrayBaseOffset(float[].class);
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

    DirectFloatBufferS(DirectBuffer directBuffer, int i2, int i3, int i4, int i5, int i6) {
        super(i2, i3, i4, i5);
        this.address = directBuffer.address() + i6;
        this.att = directBuffer;
    }

    @Override // java.nio.FloatBuffer
    public FloatBuffer slice() {
        int iPosition = position();
        int iLimit = limit();
        int i2 = iPosition <= iLimit ? iLimit - iPosition : 0;
        int i3 = iPosition << 2;
        if ($assertionsDisabled || i3 >= 0) {
            return new DirectFloatBufferS(this, -1, 0, i2, i2, i3);
        }
        throw new AssertionError();
    }

    @Override // java.nio.FloatBuffer
    public FloatBuffer duplicate() {
        return new DirectFloatBufferS(this, markValue(), position(), limit(), capacity(), 0);
    }

    @Override // java.nio.FloatBuffer
    public FloatBuffer asReadOnlyBuffer() {
        return new DirectFloatBufferRS(this, markValue(), position(), limit(), capacity(), 0);
    }

    @Override // sun.nio.ch.DirectBuffer
    public long address() {
        return this.address;
    }

    private long ix(int i2) {
        return this.address + (i2 << 2);
    }

    @Override // java.nio.FloatBuffer
    public float get() {
        return Float.intBitsToFloat(Bits.swap(unsafe.getInt(ix(nextGetIndex()))));
    }

    @Override // java.nio.FloatBuffer
    public float get(int i2) {
        return Float.intBitsToFloat(Bits.swap(unsafe.getInt(ix(checkIndex(i2)))));
    }

    @Override // java.nio.FloatBuffer
    public FloatBuffer get(float[] fArr, int i2, int i3) {
        if ((i3 << 2) > 6) {
            checkBounds(i2, i3, fArr.length);
            int iPosition = position();
            int iLimit = limit();
            if (!$assertionsDisabled && iPosition > iLimit) {
                throw new AssertionError();
            }
            if (i3 > (iPosition <= iLimit ? iLimit - iPosition : 0)) {
                throw new BufferUnderflowException();
            }
            if (order() != ByteOrder.nativeOrder()) {
                Bits.copyToIntArray(ix(iPosition), fArr, i2 << 2, i3 << 2);
            } else {
                Bits.copyToArray(ix(iPosition), fArr, arrayBaseOffset, i2 << 2, i3 << 2);
            }
            position(iPosition + i3);
        } else {
            super.get(fArr, i2, i3);
        }
        return this;
    }

    @Override // java.nio.FloatBuffer
    public FloatBuffer put(float f2) {
        unsafe.putInt(ix(nextPutIndex()), Bits.swap(Float.floatToRawIntBits(f2)));
        return this;
    }

    @Override // java.nio.FloatBuffer
    public FloatBuffer put(int i2, float f2) {
        unsafe.putInt(ix(checkIndex(i2)), Bits.swap(Float.floatToRawIntBits(f2)));
        return this;
    }

    @Override // java.nio.FloatBuffer
    public FloatBuffer put(FloatBuffer floatBuffer) {
        if (floatBuffer instanceof DirectFloatBufferS) {
            if (floatBuffer == this) {
                throw new IllegalArgumentException();
            }
            DirectFloatBufferS directFloatBufferS = (DirectFloatBufferS) floatBuffer;
            int iPosition = directFloatBufferS.position();
            int iLimit = directFloatBufferS.limit();
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
            unsafe.copyMemory(directFloatBufferS.ix(iPosition), ix(iPosition2), i2 << 2);
            directFloatBufferS.position(iPosition + i2);
            position(iPosition2 + i2);
        } else if (floatBuffer.hb != null) {
            int iPosition3 = floatBuffer.position();
            int iLimit3 = floatBuffer.limit();
            if (!$assertionsDisabled && iPosition3 > iLimit3) {
                throw new AssertionError();
            }
            int i3 = iPosition3 <= iLimit3 ? iLimit3 - iPosition3 : 0;
            put(floatBuffer.hb, floatBuffer.offset + iPosition3, i3);
            floatBuffer.position(iPosition3 + i3);
        } else {
            super.put(floatBuffer);
        }
        return this;
    }

    @Override // java.nio.FloatBuffer
    public FloatBuffer put(float[] fArr, int i2, int i3) {
        if ((i3 << 2) > 6) {
            checkBounds(i2, i3, fArr.length);
            int iPosition = position();
            int iLimit = limit();
            if (!$assertionsDisabled && iPosition > iLimit) {
                throw new AssertionError();
            }
            if (i3 > (iPosition <= iLimit ? iLimit - iPosition : 0)) {
                throw new BufferOverflowException();
            }
            if (order() != ByteOrder.nativeOrder()) {
                Bits.copyFromIntArray(fArr, i2 << 2, ix(iPosition), i3 << 2);
            } else {
                Bits.copyFromArray(fArr, arrayBaseOffset, i2 << 2, ix(iPosition), i3 << 2);
            }
            position(iPosition + i3);
        } else {
            super.put(fArr, i2, i3);
        }
        return this;
    }

    @Override // java.nio.FloatBuffer
    public FloatBuffer compact() {
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

    @Override // java.nio.FloatBuffer, java.nio.Buffer
    public boolean isDirect() {
        return true;
    }

    @Override // java.nio.Buffer
    public boolean isReadOnly() {
        return false;
    }

    @Override // java.nio.FloatBuffer
    public ByteOrder order() {
        return ByteOrder.nativeOrder() == ByteOrder.BIG_ENDIAN ? ByteOrder.LITTLE_ENDIAN : ByteOrder.BIG_ENDIAN;
    }
}
