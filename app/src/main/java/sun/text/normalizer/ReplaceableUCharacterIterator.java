package sun.text.normalizer;

/* loaded from: rt.jar:sun/text/normalizer/ReplaceableUCharacterIterator.class */
public class ReplaceableUCharacterIterator extends UCharacterIterator {
    private Replaceable replaceable;
    private int currentIndex;

    public ReplaceableUCharacterIterator(String str) {
        if (str == null) {
            throw new IllegalArgumentException();
        }
        this.replaceable = new ReplaceableString(str);
        this.currentIndex = 0;
    }

    public ReplaceableUCharacterIterator(StringBuffer stringBuffer) {
        if (stringBuffer == null) {
            throw new IllegalArgumentException();
        }
        this.replaceable = new ReplaceableString(stringBuffer);
        this.currentIndex = 0;
    }

    @Override // sun.text.normalizer.UCharacterIterator
    public Object clone() {
        try {
            return super.clone();
        } catch (CloneNotSupportedException e2) {
            return null;
        }
    }

    @Override // sun.text.normalizer.UCharacterIterator
    public int current() {
        if (this.currentIndex < this.replaceable.length()) {
            return this.replaceable.charAt(this.currentIndex);
        }
        return -1;
    }

    @Override // sun.text.normalizer.UCharacterIterator
    public int getLength() {
        return this.replaceable.length();
    }

    @Override // sun.text.normalizer.UCharacterIterator
    public int getIndex() {
        return this.currentIndex;
    }

    @Override // sun.text.normalizer.UCharacterIterator
    public int next() {
        if (this.currentIndex < this.replaceable.length()) {
            Replaceable replaceable = this.replaceable;
            int i2 = this.currentIndex;
            this.currentIndex = i2 + 1;
            return replaceable.charAt(i2);
        }
        return -1;
    }

    @Override // sun.text.normalizer.UCharacterIterator
    public int previous() {
        if (this.currentIndex > 0) {
            Replaceable replaceable = this.replaceable;
            int i2 = this.currentIndex - 1;
            this.currentIndex = i2;
            return replaceable.charAt(i2);
        }
        return -1;
    }

    @Override // sun.text.normalizer.UCharacterIterator
    public void setIndex(int i2) {
        if (i2 < 0 || i2 > this.replaceable.length()) {
            throw new IllegalArgumentException();
        }
        this.currentIndex = i2;
    }

    @Override // sun.text.normalizer.UCharacterIterator
    public int getText(char[] cArr, int i2) {
        int length = this.replaceable.length();
        if (i2 < 0 || i2 + length > cArr.length) {
            throw new IndexOutOfBoundsException(Integer.toString(length));
        }
        this.replaceable.getChars(0, length, cArr, i2);
        return length;
    }
}
