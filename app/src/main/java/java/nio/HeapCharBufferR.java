package java.nio;

/* loaded from: rt.jar:java/nio/HeapCharBufferR.class */
class HeapCharBufferR extends HeapCharBuffer {
    HeapCharBufferR(int i2, int i3) {
        super(i2, i3);
        this.isReadOnly = true;
    }

    HeapCharBufferR(char[] cArr, int i2, int i3) {
        super(cArr, i2, i3);
        this.isReadOnly = true;
    }

    protected HeapCharBufferR(char[] cArr, int i2, int i3, int i4, int i5, int i6) {
        super(cArr, i2, i3, i4, i5, i6);
        this.isReadOnly = true;
    }

    @Override // java.nio.HeapCharBuffer, java.nio.CharBuffer
    public CharBuffer slice() {
        int iPosition = position();
        int iLimit = limit();
        int i2 = iPosition <= iLimit ? iLimit - iPosition : 0;
        return new HeapCharBufferR(this.hb, -1, 0, i2, i2, iPosition + this.offset);
    }

    @Override // java.nio.HeapCharBuffer, java.nio.CharBuffer
    public CharBuffer duplicate() {
        return new HeapCharBufferR(this.hb, markValue(), position(), limit(), capacity(), this.offset);
    }

    @Override // java.nio.HeapCharBuffer, java.nio.CharBuffer
    public CharBuffer asReadOnlyBuffer() {
        return duplicate();
    }

    @Override // java.nio.HeapCharBuffer, java.nio.Buffer
    public boolean isReadOnly() {
        return true;
    }

    @Override // java.nio.HeapCharBuffer, java.nio.CharBuffer
    public CharBuffer put(char c2) {
        throw new ReadOnlyBufferException();
    }

    @Override // java.nio.HeapCharBuffer, java.nio.CharBuffer
    public CharBuffer put(int i2, char c2) {
        throw new ReadOnlyBufferException();
    }

    @Override // java.nio.HeapCharBuffer, java.nio.CharBuffer
    public CharBuffer put(char[] cArr, int i2, int i3) {
        throw new ReadOnlyBufferException();
    }

    @Override // java.nio.HeapCharBuffer, java.nio.CharBuffer
    public CharBuffer put(CharBuffer charBuffer) {
        throw new ReadOnlyBufferException();
    }

    @Override // java.nio.HeapCharBuffer, java.nio.CharBuffer
    public CharBuffer compact() {
        throw new ReadOnlyBufferException();
    }

    @Override // java.nio.HeapCharBuffer, java.nio.CharBuffer
    String toString(int i2, int i3) {
        try {
            return new String(this.hb, i2 + this.offset, i3 - i2);
        } catch (StringIndexOutOfBoundsException e2) {
            throw new IndexOutOfBoundsException();
        }
    }

    @Override // java.nio.HeapCharBuffer, java.nio.CharBuffer, java.lang.CharSequence
    public CharBuffer subSequence(int i2, int i3) {
        if (i2 < 0 || i3 > length() || i2 > i3) {
            throw new IndexOutOfBoundsException();
        }
        int iPosition = position();
        return new HeapCharBufferR(this.hb, -1, iPosition + i2, iPosition + i3, capacity(), this.offset);
    }

    @Override // java.nio.HeapCharBuffer, java.nio.CharBuffer
    public ByteOrder order() {
        return ByteOrder.nativeOrder();
    }
}
