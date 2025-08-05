package java.text;

/* loaded from: rt.jar:java/text/StringCharacterIterator.class */
public final class StringCharacterIterator implements CharacterIterator {
    private String text;
    private int begin;
    private int end;
    private int pos;

    public StringCharacterIterator(String str) {
        this(str, 0);
    }

    public StringCharacterIterator(String str, int i2) {
        this(str, 0, str.length(), i2);
    }

    public StringCharacterIterator(String str, int i2, int i3, int i4) {
        if (str == null) {
            throw new NullPointerException();
        }
        this.text = str;
        if (i2 < 0 || i2 > i3 || i3 > str.length()) {
            throw new IllegalArgumentException("Invalid substring range");
        }
        if (i4 < i2 || i4 > i3) {
            throw new IllegalArgumentException("Invalid position");
        }
        this.begin = i2;
        this.end = i3;
        this.pos = i4;
    }

    public void setText(String str) {
        if (str == null) {
            throw new NullPointerException();
        }
        this.text = str;
        this.begin = 0;
        this.end = str.length();
        this.pos = 0;
    }

    @Override // java.text.CharacterIterator
    public char first() {
        this.pos = this.begin;
        return current();
    }

    @Override // java.text.CharacterIterator
    public char last() {
        if (this.end != this.begin) {
            this.pos = this.end - 1;
        } else {
            this.pos = this.end;
        }
        return current();
    }

    @Override // java.text.CharacterIterator
    public char setIndex(int i2) {
        if (i2 < this.begin || i2 > this.end) {
            throw new IllegalArgumentException("Invalid index");
        }
        this.pos = i2;
        return current();
    }

    @Override // java.text.CharacterIterator
    public char current() {
        if (this.pos >= this.begin && this.pos < this.end) {
            return this.text.charAt(this.pos);
        }
        return (char) 65535;
    }

    @Override // java.text.CharacterIterator
    public char next() {
        if (this.pos < this.end - 1) {
            this.pos++;
            return this.text.charAt(this.pos);
        }
        this.pos = this.end;
        return (char) 65535;
    }

    @Override // java.text.CharacterIterator
    public char previous() {
        if (this.pos > this.begin) {
            this.pos--;
            return this.text.charAt(this.pos);
        }
        return (char) 65535;
    }

    @Override // java.text.CharacterIterator
    public int getBeginIndex() {
        return this.begin;
    }

    @Override // java.text.CharacterIterator
    public int getEndIndex() {
        return this.end;
    }

    @Override // java.text.CharacterIterator
    public int getIndex() {
        return this.pos;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof StringCharacterIterator)) {
            return false;
        }
        StringCharacterIterator stringCharacterIterator = (StringCharacterIterator) obj;
        if (hashCode() != stringCharacterIterator.hashCode() || !this.text.equals(stringCharacterIterator.text) || this.pos != stringCharacterIterator.pos || this.begin != stringCharacterIterator.begin || this.end != stringCharacterIterator.end) {
            return false;
        }
        return true;
    }

    public int hashCode() {
        return ((this.text.hashCode() ^ this.pos) ^ this.begin) ^ this.end;
    }

    @Override // java.text.CharacterIterator
    public Object clone() {
        try {
            return (StringCharacterIterator) super.clone();
        } catch (CloneNotSupportedException e2) {
            throw new InternalError(e2);
        }
    }
}
