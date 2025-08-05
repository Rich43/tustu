package java.awt.font;

import java.text.CharacterIterator;

/* loaded from: rt.jar:java/awt/font/CharArrayIterator.class */
class CharArrayIterator implements CharacterIterator {
    private char[] chars;
    private int pos;
    private int begin;

    CharArrayIterator(char[] cArr) {
        reset(cArr, 0);
    }

    CharArrayIterator(char[] cArr, int i2) {
        reset(cArr, i2);
    }

    @Override // java.text.CharacterIterator
    public char first() {
        this.pos = 0;
        return current();
    }

    @Override // java.text.CharacterIterator
    public char last() {
        if (this.chars.length > 0) {
            this.pos = this.chars.length - 1;
        } else {
            this.pos = 0;
        }
        return current();
    }

    @Override // java.text.CharacterIterator
    public char current() {
        if (this.pos >= 0 && this.pos < this.chars.length) {
            return this.chars[this.pos];
        }
        return (char) 65535;
    }

    @Override // java.text.CharacterIterator
    public char next() {
        if (this.pos < this.chars.length - 1) {
            this.pos++;
            return this.chars[this.pos];
        }
        this.pos = this.chars.length;
        return (char) 65535;
    }

    @Override // java.text.CharacterIterator
    public char previous() {
        if (this.pos > 0) {
            this.pos--;
            return this.chars[this.pos];
        }
        this.pos = 0;
        return (char) 65535;
    }

    @Override // java.text.CharacterIterator
    public char setIndex(int i2) {
        int i3 = i2 - this.begin;
        if (i3 < 0 || i3 > this.chars.length) {
            throw new IllegalArgumentException("Invalid index");
        }
        this.pos = i3;
        return current();
    }

    @Override // java.text.CharacterIterator
    public int getBeginIndex() {
        return this.begin;
    }

    @Override // java.text.CharacterIterator
    public int getEndIndex() {
        return this.begin + this.chars.length;
    }

    @Override // java.text.CharacterIterator
    public int getIndex() {
        return this.begin + this.pos;
    }

    @Override // java.text.CharacterIterator
    public Object clone() {
        CharArrayIterator charArrayIterator = new CharArrayIterator(this.chars, this.begin);
        charArrayIterator.pos = this.pos;
        return charArrayIterator;
    }

    void reset(char[] cArr) {
        reset(cArr, 0);
    }

    void reset(char[] cArr, int i2) {
        this.chars = cArr;
        this.begin = i2;
        this.pos = 0;
    }
}
