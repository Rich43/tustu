package com.sun.javafx.text;

import java.text.CharacterIterator;

/* loaded from: jfxrt.jar:com/sun/javafx/text/CharArrayIterator.class */
class CharArrayIterator implements CharacterIterator {
    private char[] chars;
    private int pos;
    private int begin;

    public CharArrayIterator(char[] chars) {
        reset(chars, 0);
    }

    public CharArrayIterator(char[] chars, int begin) {
        reset(chars, begin);
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
    public char setIndex(int position) {
        int position2 = position - this.begin;
        if (position2 < 0 || position2 > this.chars.length) {
            throw new IllegalArgumentException("Invalid index");
        }
        this.pos = position2;
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
        CharArrayIterator c2 = new CharArrayIterator(this.chars, this.begin);
        c2.pos = this.pos;
        return c2;
    }

    void reset(char[] chars) {
        reset(chars, 0);
    }

    void reset(char[] chars, int begin) {
        this.chars = chars;
        this.begin = begin;
        this.pos = 0;
    }
}
