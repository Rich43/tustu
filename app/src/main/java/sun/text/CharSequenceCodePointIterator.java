package sun.text;

/* compiled from: CodePointIterator.java */
/* loaded from: rt.jar:sun/text/CharSequenceCodePointIterator.class */
final class CharSequenceCodePointIterator extends CodePointIterator {
    private CharSequence text;
    private int index;

    public CharSequenceCodePointIterator(CharSequence charSequence) {
        this.text = charSequence;
    }

    @Override // sun.text.CodePointIterator
    public void setToStart() {
        this.index = 0;
    }

    @Override // sun.text.CodePointIterator
    public void setToLimit() {
        this.index = this.text.length();
    }

    @Override // sun.text.CodePointIterator
    public int next() {
        if (this.index < this.text.length()) {
            CharSequence charSequence = this.text;
            int i2 = this.index;
            this.index = i2 + 1;
            char cCharAt = charSequence.charAt(i2);
            if (Character.isHighSurrogate(cCharAt) && this.index < this.text.length()) {
                char cCharAt2 = this.text.charAt(this.index + 1);
                if (Character.isLowSurrogate(cCharAt2)) {
                    this.index++;
                    return Character.toCodePoint(cCharAt, cCharAt2);
                }
            }
            return cCharAt;
        }
        return -1;
    }

    @Override // sun.text.CodePointIterator
    public int prev() {
        if (this.index > 0) {
            CharSequence charSequence = this.text;
            int i2 = this.index - 1;
            this.index = i2;
            char cCharAt = charSequence.charAt(i2);
            if (Character.isLowSurrogate(cCharAt) && this.index > 0) {
                char cCharAt2 = this.text.charAt(this.index - 1);
                if (Character.isHighSurrogate(cCharAt2)) {
                    this.index--;
                    return Character.toCodePoint(cCharAt2, cCharAt);
                }
            }
            return cCharAt;
        }
        return -1;
    }

    @Override // sun.text.CodePointIterator
    public int charIndex() {
        return this.index;
    }
}
