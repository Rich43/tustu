package com.sun.xml.internal.fastinfoset.util;

/* loaded from: rt.jar:com/sun/xml/internal/fastinfoset/util/CharArray.class */
public class CharArray implements CharSequence {
    public char[] ch;
    public int start;
    public int length;
    protected int _hash;

    protected CharArray() {
    }

    public CharArray(char[] _ch, int _start, int _length, boolean copy) {
        set(_ch, _start, _length, copy);
    }

    public final void set(char[] _ch, int _start, int _length, boolean copy) {
        if (copy) {
            this.ch = new char[_length];
            this.start = 0;
            this.length = _length;
            System.arraycopy(_ch, _start, this.ch, 0, _length);
        } else {
            this.ch = _ch;
            this.start = _start;
            this.length = _length;
        }
        this._hash = 0;
    }

    public final void cloneArray() {
        char[] _ch = new char[this.length];
        System.arraycopy(this.ch, this.start, _ch, 0, this.length);
        this.ch = _ch;
        this.start = 0;
    }

    @Override // java.lang.CharSequence
    public String toString() {
        return new String(this.ch, this.start, this.length);
    }

    public int hashCode() {
        if (this._hash == 0) {
            for (int i2 = this.start; i2 < this.start + this.length; i2++) {
                this._hash = (31 * this._hash) + this.ch[i2];
            }
        }
        return this._hash;
    }

    public static final int hashCode(char[] ch, int start, int length) {
        int hash = 0;
        for (int i2 = start; i2 < start + length; i2++) {
            hash = (31 * hash) + ch[i2];
        }
        return hash;
    }

    public final boolean equalsCharArray(CharArray cha) {
        int i2;
        int i3;
        if (this == cha) {
            return true;
        }
        if (this.length == cha.length) {
            int n2 = this.length;
            int i4 = this.start;
            int j2 = cha.start;
            do {
                int i5 = n2;
                n2--;
                if (i5 == 0) {
                    return true;
                }
                i2 = i4;
                i4++;
                i3 = j2;
                j2++;
            } while (this.ch[i2] == cha.ch[i3]);
            return false;
        }
        return false;
    }

    public final boolean equalsCharArray(char[] ch, int start, int length) {
        int i2;
        int i3;
        if (this.length == length) {
            int n2 = this.length;
            int i4 = this.start;
            int j2 = start;
            do {
                int i5 = n2;
                n2--;
                if (i5 == 0) {
                    return true;
                }
                i2 = i4;
                i4++;
                i3 = j2;
                j2++;
            } while (this.ch[i2] == ch[i3]);
            return false;
        }
        return false;
    }

    public boolean equals(Object obj) {
        int i2;
        int i3;
        if (this == obj) {
            return true;
        }
        if (obj instanceof CharArray) {
            CharArray cha = (CharArray) obj;
            if (this.length == cha.length) {
                int n2 = this.length;
                int i4 = this.start;
                int j2 = cha.start;
                do {
                    int i5 = n2;
                    n2--;
                    if (i5 == 0) {
                        return true;
                    }
                    i2 = i4;
                    i4++;
                    i3 = j2;
                    j2++;
                } while (this.ch[i2] == cha.ch[i3]);
                return false;
            }
            return false;
        }
        return false;
    }

    @Override // java.lang.CharSequence
    public final int length() {
        return this.length;
    }

    @Override // java.lang.CharSequence
    public final char charAt(int index) {
        return this.ch[this.start + index];
    }

    @Override // java.lang.CharSequence
    public final CharSequence subSequence(int start, int end) {
        return new CharArray(this.ch, this.start + start, end - start, false);
    }
}
