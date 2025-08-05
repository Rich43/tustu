package java.nio;

/* loaded from: rt.jar:java/nio/HeapCharBuffer.class */
class HeapCharBuffer extends CharBuffer {
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !HeapCharBuffer.class.desiredAssertionStatus();
    }

    HeapCharBuffer(int i2, int i3) {
        super(-1, 0, i3, i2, new char[i2], 0);
    }

    HeapCharBuffer(char[] cArr, int i2, int i3) {
        super(-1, i2, i2 + i3, cArr.length, cArr, 0);
    }

    protected HeapCharBuffer(char[] cArr, int i2, int i3, int i4, int i5, int i6) {
        super(i2, i3, i4, i5, cArr, i6);
    }

    @Override // java.nio.CharBuffer
    public CharBuffer slice() {
        int iPosition = position();
        int iLimit = limit();
        int i2 = iPosition <= iLimit ? iLimit - iPosition : 0;
        return new HeapCharBuffer(this.hb, -1, 0, i2, i2, iPosition + this.offset);
    }

    @Override // java.nio.CharBuffer
    public CharBuffer duplicate() {
        return new HeapCharBuffer(this.hb, markValue(), position(), limit(), capacity(), this.offset);
    }

    @Override // java.nio.CharBuffer
    public CharBuffer asReadOnlyBuffer() {
        return new HeapCharBufferR(this.hb, markValue(), position(), limit(), capacity(), this.offset);
    }

    protected int ix(int i2) {
        return i2 + this.offset;
    }

    @Override // java.nio.CharBuffer
    public char get() {
        return this.hb[ix(nextGetIndex())];
    }

    @Override // java.nio.CharBuffer
    public char get(int i2) {
        return this.hb[ix(checkIndex(i2))];
    }

    @Override // java.nio.CharBuffer
    char getUnchecked(int i2) {
        return this.hb[ix(i2)];
    }

    @Override // java.nio.CharBuffer
    public CharBuffer get(char[] cArr, int i2, int i3) {
        checkBounds(i2, i3, cArr.length);
        int iPosition = position();
        if (i3 > limit() - iPosition) {
            throw new BufferUnderflowException();
        }
        System.arraycopy(this.hb, ix(iPosition), cArr, i2, i3);
        position(iPosition + i3);
        return this;
    }

    @Override // java.nio.CharBuffer, java.nio.Buffer
    public boolean isDirect() {
        return false;
    }

    @Override // java.nio.Buffer
    public boolean isReadOnly() {
        return false;
    }

    @Override // java.nio.CharBuffer
    public CharBuffer put(char c2) {
        this.hb[ix(nextPutIndex())] = c2;
        return this;
    }

    @Override // java.nio.CharBuffer
    public CharBuffer put(int i2, char c2) {
        this.hb[ix(checkIndex(i2))] = c2;
        return this;
    }

    @Override // java.nio.CharBuffer
    public CharBuffer put(char[] cArr, int i2, int i3) {
        checkBounds(i2, i3, cArr.length);
        int iPosition = position();
        if (i3 > limit() - iPosition) {
            throw new BufferOverflowException();
        }
        System.arraycopy(cArr, i2, this.hb, ix(iPosition), i3);
        position(iPosition + i3);
        return this;
    }

    @Override // java.nio.CharBuffer
    public CharBuffer put(CharBuffer charBuffer) {
        if (charBuffer instanceof HeapCharBuffer) {
            if (charBuffer == this) {
                throw new IllegalArgumentException();
            }
            HeapCharBuffer heapCharBuffer = (HeapCharBuffer) charBuffer;
            int iPosition = position();
            int iPosition2 = heapCharBuffer.position();
            int iLimit = heapCharBuffer.limit() - iPosition2;
            if (iLimit > limit() - iPosition) {
                throw new BufferOverflowException();
            }
            System.arraycopy(heapCharBuffer.hb, heapCharBuffer.ix(iPosition2), this.hb, ix(iPosition), iLimit);
            heapCharBuffer.position(iPosition2 + iLimit);
            position(iPosition + iLimit);
        } else if (charBuffer.isDirect()) {
            int iRemaining = charBuffer.remaining();
            int iPosition3 = position();
            if (iRemaining > limit() - iPosition3) {
                throw new BufferOverflowException();
            }
            charBuffer.get(this.hb, ix(iPosition3), iRemaining);
            position(iPosition3 + iRemaining);
        } else {
            super.put(charBuffer);
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
        System.arraycopy(this.hb, ix(iPosition), this.hb, ix(0), i2);
        position(i2);
        limit(capacity());
        discardMark();
        return this;
    }

    @Override // java.nio.CharBuffer
    String toString(int i2, int i3) {
        try {
            return new String(this.hb, i2 + this.offset, i3 - i2);
        } catch (StringIndexOutOfBoundsException e2) {
            throw new IndexOutOfBoundsException();
        }
    }

    @Override // java.nio.CharBuffer, java.lang.CharSequence
    public CharBuffer subSequence(int i2, int i3) {
        if (i2 < 0 || i3 > length() || i2 > i3) {
            throw new IndexOutOfBoundsException();
        }
        int iPosition = position();
        return new HeapCharBuffer(this.hb, -1, iPosition + i2, iPosition + i3, capacity(), this.offset);
    }

    @Override // java.nio.CharBuffer
    public ByteOrder order() {
        return ByteOrder.nativeOrder();
    }
}
