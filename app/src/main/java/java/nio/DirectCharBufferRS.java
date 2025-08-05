package java.nio;

import sun.nio.ch.DirectBuffer;

/* loaded from: rt.jar:java/nio/DirectCharBufferRS.class */
class DirectCharBufferRS extends DirectCharBufferS implements DirectBuffer {
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !DirectCharBufferRS.class.desiredAssertionStatus();
    }

    DirectCharBufferRS(DirectBuffer directBuffer, int i2, int i3, int i4, int i5, int i6) {
        super(directBuffer, i2, i3, i4, i5, i6);
    }

    @Override // java.nio.DirectCharBufferS, java.nio.CharBuffer
    public CharBuffer slice() {
        int iPosition = position();
        int iLimit = limit();
        int i2 = iPosition <= iLimit ? iLimit - iPosition : 0;
        int i3 = iPosition << 1;
        if ($assertionsDisabled || i3 >= 0) {
            return new DirectCharBufferRS(this, -1, 0, i2, i2, i3);
        }
        throw new AssertionError();
    }

    @Override // java.nio.DirectCharBufferS, java.nio.CharBuffer
    public CharBuffer duplicate() {
        return new DirectCharBufferRS(this, markValue(), position(), limit(), capacity(), 0);
    }

    @Override // java.nio.DirectCharBufferS, java.nio.CharBuffer
    public CharBuffer asReadOnlyBuffer() {
        return duplicate();
    }

    @Override // java.nio.DirectCharBufferS, java.nio.CharBuffer
    public CharBuffer put(char c2) {
        throw new ReadOnlyBufferException();
    }

    @Override // java.nio.DirectCharBufferS, java.nio.CharBuffer
    public CharBuffer put(int i2, char c2) {
        throw new ReadOnlyBufferException();
    }

    @Override // java.nio.DirectCharBufferS, java.nio.CharBuffer
    public CharBuffer put(CharBuffer charBuffer) {
        throw new ReadOnlyBufferException();
    }

    @Override // java.nio.DirectCharBufferS, java.nio.CharBuffer
    public CharBuffer put(char[] cArr, int i2, int i3) {
        throw new ReadOnlyBufferException();
    }

    @Override // java.nio.DirectCharBufferS, java.nio.CharBuffer
    public CharBuffer compact() {
        throw new ReadOnlyBufferException();
    }

    @Override // java.nio.DirectCharBufferS, java.nio.CharBuffer, java.nio.Buffer
    public boolean isDirect() {
        return true;
    }

    @Override // java.nio.DirectCharBufferS, java.nio.Buffer
    public boolean isReadOnly() {
        return true;
    }

    @Override // java.nio.DirectCharBufferS, java.nio.CharBuffer
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

    @Override // java.nio.DirectCharBufferS, java.nio.CharBuffer, java.lang.CharSequence
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
        return new DirectCharBufferRS(this, -1, i4 + i2, i4 + i3, capacity(), this.offset);
    }

    @Override // java.nio.DirectCharBufferS, java.nio.CharBuffer
    public ByteOrder order() {
        return ByteOrder.nativeOrder() == ByteOrder.BIG_ENDIAN ? ByteOrder.LITTLE_ENDIAN : ByteOrder.BIG_ENDIAN;
    }
}
