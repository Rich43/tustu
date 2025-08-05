package java.nio;

import sun.misc.Cleaner;
import sun.misc.Unsafe;
import sun.nio.ch.DirectBuffer;

/* loaded from: rt.jar:java/nio/DirectCharBufferU.class */
class DirectCharBufferU extends CharBuffer implements DirectBuffer {
    protected static final Unsafe unsafe;
    private static final long arrayBaseOffset;
    protected static final boolean unaligned;
    private final Object att;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !DirectCharBufferU.class.desiredAssertionStatus();
        unsafe = Bits.unsafe();
        arrayBaseOffset = unsafe.arrayBaseOffset(char[].class);
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

    DirectCharBufferU(DirectBuffer directBuffer, int i2, int i3, int i4, int i5, int i6) {
        super(i2, i3, i4, i5);
        this.address = directBuffer.address() + i6;
        this.att = directBuffer;
    }

    @Override // java.nio.CharBuffer
    public CharBuffer slice() {
        int iPosition = position();
        int iLimit = limit();
        int i2 = iPosition <= iLimit ? iLimit - iPosition : 0;
        int i3 = iPosition << 1;
        if ($assertionsDisabled || i3 >= 0) {
            return new DirectCharBufferU(this, -1, 0, i2, i2, i3);
        }
        throw new AssertionError();
    }

    @Override // java.nio.CharBuffer
    public CharBuffer duplicate() {
        return new DirectCharBufferU(this, markValue(), position(), limit(), capacity(), 0);
    }

    @Override // java.nio.CharBuffer
    public CharBuffer asReadOnlyBuffer() {
        return new DirectCharBufferRU(this, markValue(), position(), limit(), capacity(), 0);
    }

    @Override // sun.nio.ch.DirectBuffer
    public long address() {
        return this.address;
    }

    private long ix(int i2) {
        return this.address + (i2 << 1);
    }

    @Override // java.nio.CharBuffer
    public char get() {
        return unsafe.getChar(ix(nextGetIndex()));
    }

    @Override // java.nio.CharBuffer
    public char get(int i2) {
        return unsafe.getChar(ix(checkIndex(i2)));
    }

    @Override // java.nio.CharBuffer
    char getUnchecked(int i2) {
        return unsafe.getChar(ix(i2));
    }

    @Override // java.nio.CharBuffer
    public CharBuffer get(char[] cArr, int i2, int i3) {
        if ((i3 << 1) > 6) {
            checkBounds(i2, i3, cArr.length);
            int iPosition = position();
            int iLimit = limit();
            if (!$assertionsDisabled && iPosition > iLimit) {
                throw new AssertionError();
            }
            if (i3 > (iPosition <= iLimit ? iLimit - iPosition : 0)) {
                throw new BufferUnderflowException();
            }
            if (order() != ByteOrder.nativeOrder()) {
                Bits.copyToCharArray(ix(iPosition), cArr, i2 << 1, i3 << 1);
            } else {
                Bits.copyToArray(ix(iPosition), cArr, arrayBaseOffset, i2 << 1, i3 << 1);
            }
            position(iPosition + i3);
        } else {
            super.get(cArr, i2, i3);
        }
        return this;
    }

    @Override // java.nio.CharBuffer
    public CharBuffer put(char c2) {
        unsafe.putChar(ix(nextPutIndex()), c2);
        return this;
    }

    @Override // java.nio.CharBuffer
    public CharBuffer put(int i2, char c2) {
        unsafe.putChar(ix(checkIndex(i2)), c2);
        return this;
    }

    @Override // java.nio.CharBuffer
    public CharBuffer put(CharBuffer charBuffer) {
        if (charBuffer instanceof DirectCharBufferU) {
            if (charBuffer == this) {
                throw new IllegalArgumentException();
            }
            DirectCharBufferU directCharBufferU = (DirectCharBufferU) charBuffer;
            int iPosition = directCharBufferU.position();
            int iLimit = directCharBufferU.limit();
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
            unsafe.copyMemory(directCharBufferU.ix(iPosition), ix(iPosition2), i2 << 1);
            directCharBufferU.position(iPosition + i2);
            position(iPosition2 + i2);
        } else if (charBuffer.hb != null) {
            int iPosition3 = charBuffer.position();
            int iLimit3 = charBuffer.limit();
            if (!$assertionsDisabled && iPosition3 > iLimit3) {
                throw new AssertionError();
            }
            int i3 = iPosition3 <= iLimit3 ? iLimit3 - iPosition3 : 0;
            put(charBuffer.hb, charBuffer.offset + iPosition3, i3);
            charBuffer.position(iPosition3 + i3);
        } else {
            super.put(charBuffer);
        }
        return this;
    }

    @Override // java.nio.CharBuffer
    public CharBuffer put(char[] cArr, int i2, int i3) {
        if ((i3 << 1) > 6) {
            checkBounds(i2, i3, cArr.length);
            int iPosition = position();
            int iLimit = limit();
            if (!$assertionsDisabled && iPosition > iLimit) {
                throw new AssertionError();
            }
            if (i3 > (iPosition <= iLimit ? iLimit - iPosition : 0)) {
                throw new BufferOverflowException();
            }
            if (order() != ByteOrder.nativeOrder()) {
                Bits.copyFromCharArray(cArr, i2 << 1, ix(iPosition), i3 << 1);
            } else {
                Bits.copyFromArray(cArr, arrayBaseOffset, i2 << 1, ix(iPosition), i3 << 1);
            }
            position(iPosition + i3);
        } else {
            super.put(cArr, i2, i3);
        }
        return this;
    }

    @Override // java.nio.CharBuffer
    public CharBuffer compact() {
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

    @Override // java.nio.CharBuffer, java.nio.Buffer
    public boolean isDirect() {
        return true;
    }

    @Override // java.nio.Buffer
    public boolean isReadOnly() {
        return false;
    }

    @Override // java.nio.CharBuffer
    public String toString(int i2, int i3) {
        if (i3 > limit() || i2 > i3) {
            throw new IndexOutOfBoundsException();
        }
        try {
            char[] cArr = new char[i3 - i2];
            CharBuffer charBufferWrap = CharBuffer.wrap(cArr);
            CharBuffer charBufferDuplicate = duplicate();
            charBufferDuplicate.position(i2);
            charBufferDuplicate.limit(i3);
            charBufferWrap.put(charBufferDuplicate);
            return new String(cArr);
        } catch (StringIndexOutOfBoundsException e2) {
            throw new IndexOutOfBoundsException();
        }
    }

    @Override // java.nio.CharBuffer, java.lang.CharSequence
    public CharBuffer subSequence(int i2, int i3) {
        int iPosition = position();
        int iLimit = limit();
        if (!$assertionsDisabled && iPosition > iLimit) {
            throw new AssertionError();
        }
        int i4 = iPosition <= iLimit ? iPosition : iLimit;
        int i5 = iLimit - i4;
        if (i2 < 0 || i3 > i5 || i2 > i3) {
            throw new IndexOutOfBoundsException();
        }
        return new DirectCharBufferU(this, -1, i4 + i2, i4 + i3, capacity(), this.offset);
    }

    @Override // java.nio.CharBuffer
    public ByteOrder order() {
        return ByteOrder.nativeOrder() != ByteOrder.BIG_ENDIAN ? ByteOrder.LITTLE_ENDIAN : ByteOrder.BIG_ENDIAN;
    }
}
