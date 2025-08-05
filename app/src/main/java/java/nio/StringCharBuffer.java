package java.nio;

/* loaded from: rt.jar:java/nio/StringCharBuffer.class */
class StringCharBuffer extends CharBuffer {
    CharSequence str;

    StringCharBuffer(CharSequence charSequence, int i2, int i3) {
        super(-1, i2, i3, charSequence.length());
        int length = charSequence.length();
        if (i2 < 0 || i2 > length || i3 < i2 || i3 > length) {
            throw new IndexOutOfBoundsException();
        }
        this.str = charSequence;
    }

    @Override // java.nio.CharBuffer
    public CharBuffer slice() {
        int iPosition = position();
        int iLimit = limit();
        int i2 = iPosition <= iLimit ? iLimit - iPosition : 0;
        return new StringCharBuffer(this.str, -1, 0, i2, i2, this.offset + iPosition);
    }

    private StringCharBuffer(CharSequence charSequence, int i2, int i3, int i4, int i5, int i6) {
        super(i2, i3, i4, i5, null, i6);
        this.str = charSequence;
    }

    @Override // java.nio.CharBuffer
    public CharBuffer duplicate() {
        return new StringCharBuffer(this.str, markValue(), position(), limit(), capacity(), this.offset);
    }

    @Override // java.nio.CharBuffer
    public CharBuffer asReadOnlyBuffer() {
        return duplicate();
    }

    @Override // java.nio.CharBuffer
    public final char get() {
        return this.str.charAt(nextGetIndex() + this.offset);
    }

    @Override // java.nio.CharBuffer
    public final char get(int i2) {
        return this.str.charAt(checkIndex(i2) + this.offset);
    }

    @Override // java.nio.CharBuffer
    char getUnchecked(int i2) {
        return this.str.charAt(i2 + this.offset);
    }

    @Override // java.nio.CharBuffer
    public final CharBuffer put(char c2) {
        throw new ReadOnlyBufferException();
    }

    @Override // java.nio.CharBuffer
    public final CharBuffer put(int i2, char c2) {
        throw new ReadOnlyBufferException();
    }

    @Override // java.nio.CharBuffer
    public final CharBuffer compact() {
        throw new ReadOnlyBufferException();
    }

    @Override // java.nio.Buffer
    public final boolean isReadOnly() {
        return true;
    }

    @Override // java.nio.CharBuffer
    final String toString(int i2, int i3) {
        return this.str.toString().substring(i2 + this.offset, i3 + this.offset);
    }

    @Override // java.nio.CharBuffer, java.lang.CharSequence
    public final CharBuffer subSequence(int i2, int i3) {
        try {
            int iPosition = position();
            return new StringCharBuffer(this.str, -1, iPosition + checkIndex(i2, iPosition), iPosition + checkIndex(i3, iPosition), capacity(), this.offset);
        } catch (IllegalArgumentException e2) {
            throw new IndexOutOfBoundsException();
        }
    }

    @Override // java.nio.CharBuffer, java.nio.Buffer
    public boolean isDirect() {
        return false;
    }

    @Override // java.nio.CharBuffer
    public ByteOrder order() {
        return ByteOrder.nativeOrder();
    }
}
