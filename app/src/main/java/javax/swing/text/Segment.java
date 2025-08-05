package javax.swing.text;

import java.text.CharacterIterator;

/* loaded from: rt.jar:javax/swing/text/Segment.class */
public class Segment implements Cloneable, CharacterIterator, CharSequence {
    public char[] array;
    public int offset;
    public int count;
    private boolean partialReturn;
    private int pos;

    public Segment() {
        this(null, 0, 0);
    }

    public Segment(char[] cArr, int i2, int i3) {
        this.array = cArr;
        this.offset = i2;
        this.count = i3;
        this.partialReturn = false;
    }

    public void setPartialReturn(boolean z2) {
        this.partialReturn = z2;
    }

    public boolean isPartialReturn() {
        return this.partialReturn;
    }

    @Override // java.lang.CharSequence
    public String toString() {
        if (this.array != null) {
            return new String(this.array, this.offset, this.count);
        }
        return "";
    }

    @Override // java.text.CharacterIterator
    public char first() {
        this.pos = this.offset;
        if (this.count != 0) {
            return this.array[this.pos];
        }
        return (char) 65535;
    }

    @Override // java.text.CharacterIterator
    public char last() {
        this.pos = this.offset + this.count;
        if (this.count != 0) {
            this.pos--;
            return this.array[this.pos];
        }
        return (char) 65535;
    }

    @Override // java.text.CharacterIterator
    public char current() {
        if (this.count != 0 && this.pos < this.offset + this.count) {
            return this.array[this.pos];
        }
        return (char) 65535;
    }

    @Override // java.text.CharacterIterator
    public char next() {
        this.pos++;
        int i2 = this.offset + this.count;
        if (this.pos >= i2) {
            this.pos = i2;
            return (char) 65535;
        }
        return current();
    }

    @Override // java.text.CharacterIterator
    public char previous() {
        if (this.pos == this.offset) {
            return (char) 65535;
        }
        this.pos--;
        return current();
    }

    @Override // java.text.CharacterIterator
    public char setIndex(int i2) {
        int i3 = this.offset + this.count;
        if (i2 < this.offset || i2 > i3) {
            throw new IllegalArgumentException("bad position: " + i2);
        }
        this.pos = i2;
        if (this.pos != i3 && this.count != 0) {
            return this.array[this.pos];
        }
        return (char) 65535;
    }

    @Override // java.text.CharacterIterator
    public int getBeginIndex() {
        return this.offset;
    }

    @Override // java.text.CharacterIterator
    public int getEndIndex() {
        return this.offset + this.count;
    }

    @Override // java.text.CharacterIterator
    public int getIndex() {
        return this.pos;
    }

    @Override // java.lang.CharSequence
    public char charAt(int i2) {
        if (i2 < 0 || i2 >= this.count) {
            throw new StringIndexOutOfBoundsException(i2);
        }
        return this.array[this.offset + i2];
    }

    @Override // java.lang.CharSequence
    public int length() {
        return this.count;
    }

    @Override // java.lang.CharSequence
    public CharSequence subSequence(int i2, int i3) {
        if (i2 < 0) {
            throw new StringIndexOutOfBoundsException(i2);
        }
        if (i3 > this.count) {
            throw new StringIndexOutOfBoundsException(i3);
        }
        if (i2 > i3) {
            throw new StringIndexOutOfBoundsException(i3 - i2);
        }
        Segment segment = new Segment();
        segment.array = this.array;
        segment.offset = this.offset + i2;
        segment.count = i3 - i2;
        return segment;
    }

    @Override // java.text.CharacterIterator
    public Object clone() {
        Object objClone;
        try {
            objClone = super.clone();
        } catch (CloneNotSupportedException e2) {
            objClone = null;
        }
        return objClone;
    }
}
