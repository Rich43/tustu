package sun.text;

/* compiled from: CodePointIterator.java */
/* loaded from: rt.jar:sun/text/CharArrayCodePointIterator.class */
final class CharArrayCodePointIterator extends CodePointIterator {
    private char[] text;
    private int start;
    private int limit;
    private int index;

    public CharArrayCodePointIterator(char[] cArr) {
        this.text = cArr;
        this.limit = cArr.length;
    }

    public CharArrayCodePointIterator(char[] cArr, int i2, int i3) {
        if (i2 < 0 || i3 < i2 || i3 > cArr.length) {
            throw new IllegalArgumentException();
        }
        this.text = cArr;
        this.index = i2;
        this.start = i2;
        this.limit = i3;
    }

    @Override // sun.text.CodePointIterator
    public void setToStart() {
        this.index = this.start;
    }

    @Override // sun.text.CodePointIterator
    public void setToLimit() {
        this.index = this.limit;
    }

    @Override // sun.text.CodePointIterator
    public int next() {
        if (this.index < this.limit) {
            char[] cArr = this.text;
            int i2 = this.index;
            this.index = i2 + 1;
            char c2 = cArr[i2];
            if (Character.isHighSurrogate(c2) && this.index < this.limit) {
                char c3 = this.text[this.index];
                if (Character.isLowSurrogate(c3)) {
                    this.index++;
                    return Character.toCodePoint(c2, c3);
                }
            }
            return c2;
        }
        return -1;
    }

    @Override // sun.text.CodePointIterator
    public int prev() {
        if (this.index > this.start) {
            char[] cArr = this.text;
            int i2 = this.index - 1;
            this.index = i2;
            char c2 = cArr[i2];
            if (Character.isLowSurrogate(c2) && this.index > this.start) {
                char c3 = this.text[this.index - 1];
                if (Character.isHighSurrogate(c3)) {
                    this.index--;
                    return Character.toCodePoint(c3, c2);
                }
            }
            return c2;
        }
        return -1;
    }

    @Override // sun.text.CodePointIterator
    public int charIndex() {
        return this.index;
    }
}
