package sun.text.normalizer;

import java.text.CharacterIterator;

/* loaded from: rt.jar:sun/text/normalizer/CharacterIteratorWrapper.class */
public class CharacterIteratorWrapper extends UCharacterIterator {
    private CharacterIterator iterator;

    public CharacterIteratorWrapper(CharacterIterator characterIterator) {
        if (characterIterator == null) {
            throw new IllegalArgumentException();
        }
        this.iterator = characterIterator;
    }

    @Override // sun.text.normalizer.UCharacterIterator
    public int current() {
        char cCurrent = this.iterator.current();
        if (cCurrent == 65535) {
            return -1;
        }
        return cCurrent;
    }

    @Override // sun.text.normalizer.UCharacterIterator
    public int getLength() {
        return this.iterator.getEndIndex() - this.iterator.getBeginIndex();
    }

    @Override // sun.text.normalizer.UCharacterIterator
    public int getIndex() {
        return this.iterator.getIndex();
    }

    @Override // sun.text.normalizer.UCharacterIterator
    public int next() {
        char cCurrent = this.iterator.current();
        this.iterator.next();
        if (cCurrent == 65535) {
            return -1;
        }
        return cCurrent;
    }

    @Override // sun.text.normalizer.UCharacterIterator
    public int previous() {
        char cPrevious = this.iterator.previous();
        if (cPrevious == 65535) {
            return -1;
        }
        return cPrevious;
    }

    @Override // sun.text.normalizer.UCharacterIterator
    public void setIndex(int i2) {
        this.iterator.setIndex(i2);
    }

    @Override // sun.text.normalizer.UCharacterIterator
    public int getText(char[] cArr, int i2) {
        int endIndex = this.iterator.getEndIndex() - this.iterator.getBeginIndex();
        int index = this.iterator.getIndex();
        if (i2 < 0 || i2 + endIndex > cArr.length) {
            throw new IndexOutOfBoundsException(Integer.toString(endIndex));
        }
        char cFirst = this.iterator.first();
        while (true) {
            char c2 = cFirst;
            if (c2 != 65535) {
                int i3 = i2;
                i2++;
                cArr[i3] = c2;
                cFirst = this.iterator.next();
            } else {
                this.iterator.setIndex(index);
                return endIndex;
            }
        }
    }

    @Override // sun.text.normalizer.UCharacterIterator
    public Object clone() {
        try {
            CharacterIteratorWrapper characterIteratorWrapper = (CharacterIteratorWrapper) super.clone();
            characterIteratorWrapper.iterator = (CharacterIterator) this.iterator.clone();
            return characterIteratorWrapper;
        } catch (CloneNotSupportedException e2) {
            return null;
        }
    }
}
