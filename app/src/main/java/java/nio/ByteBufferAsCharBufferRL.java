package java.nio;

/* loaded from: rt.jar:java/nio/ByteBufferAsCharBufferRL.class */
class ByteBufferAsCharBufferRL extends ByteBufferAsCharBufferL {
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !ByteBufferAsCharBufferRL.class.desiredAssertionStatus();
    }

    ByteBufferAsCharBufferRL(ByteBuffer byteBuffer) {
        super(byteBuffer);
    }

    ByteBufferAsCharBufferRL(ByteBuffer byteBuffer, int i2, int i3, int i4, int i5, int i6) {
        super(byteBuffer, i2, i3, i4, i5, i6);
    }

    @Override // java.nio.ByteBufferAsCharBufferL, java.nio.CharBuffer
    public CharBuffer slice() {
        int iPosition = position();
        int iLimit = limit();
        int i2 = iPosition <= iLimit ? iLimit - iPosition : 0;
        int i3 = (iPosition << 1) + this.offset;
        if ($assertionsDisabled || i3 >= 0) {
            return new ByteBufferAsCharBufferRL(this.f12451bb, -1, 0, i2, i2, i3);
        }
        throw new AssertionError();
    }

    @Override // java.nio.ByteBufferAsCharBufferL, java.nio.CharBuffer
    public CharBuffer duplicate() {
        return new ByteBufferAsCharBufferRL(this.f12451bb, markValue(), position(), limit(), capacity(), this.offset);
    }

    @Override // java.nio.ByteBufferAsCharBufferL, java.nio.CharBuffer
    public CharBuffer asReadOnlyBuffer() {
        return duplicate();
    }

    @Override // java.nio.ByteBufferAsCharBufferL, java.nio.CharBuffer
    public CharBuffer put(char c2) {
        throw new ReadOnlyBufferException();
    }

    @Override // java.nio.ByteBufferAsCharBufferL, java.nio.CharBuffer
    public CharBuffer put(int i2, char c2) {
        throw new ReadOnlyBufferException();
    }

    @Override // java.nio.ByteBufferAsCharBufferL, java.nio.CharBuffer
    public CharBuffer compact() {
        throw new ReadOnlyBufferException();
    }

    @Override // java.nio.ByteBufferAsCharBufferL, java.nio.CharBuffer, java.nio.Buffer
    public boolean isDirect() {
        return this.f12451bb.isDirect();
    }

    @Override // java.nio.ByteBufferAsCharBufferL, java.nio.Buffer
    public boolean isReadOnly() {
        return true;
    }

    @Override // java.nio.ByteBufferAsCharBufferL, java.nio.CharBuffer
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

    @Override // java.nio.ByteBufferAsCharBufferL, java.nio.CharBuffer, java.lang.CharSequence
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
        return new ByteBufferAsCharBufferRL(this.f12451bb, -1, i4 + i2, i4 + i3, capacity(), this.offset);
    }

    @Override // java.nio.ByteBufferAsCharBufferL, java.nio.CharBuffer
    public ByteOrder order() {
        return ByteOrder.LITTLE_ENDIAN;
    }
}
