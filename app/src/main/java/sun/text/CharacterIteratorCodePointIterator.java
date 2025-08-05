package sun.text;

import java.text.CharacterIterator;

/* compiled from: CodePointIterator.java */
/* loaded from: rt.jar:sun/text/CharacterIteratorCodePointIterator.class */
final class CharacterIteratorCodePointIterator extends CodePointIterator {
    private CharacterIterator iter;

    public CharacterIteratorCodePointIterator(CharacterIterator characterIterator) {
        this.iter = characterIterator;
    }

    @Override // sun.text.CodePointIterator
    public void setToStart() {
        this.iter.setIndex(this.iter.getBeginIndex());
    }

    @Override // sun.text.CodePointIterator
    public void setToLimit() {
        this.iter.setIndex(this.iter.getEndIndex());
    }

    @Override // sun.text.CodePointIterator
    public int next() {
        char cCurrent = this.iter.current();
        if (cCurrent != 65535) {
            char next = this.iter.next();
            if (Character.isHighSurrogate(cCurrent) && next != 65535 && Character.isLowSurrogate(next)) {
                this.iter.next();
                return Character.toCodePoint(cCurrent, next);
            }
            return cCurrent;
        }
        return -1;
    }

    @Override // sun.text.CodePointIterator
    public int prev() {
        char cPrevious = this.iter.previous();
        if (cPrevious != 65535) {
            if (Character.isLowSurrogate(cPrevious)) {
                char cPrevious2 = this.iter.previous();
                if (Character.isHighSurrogate(cPrevious2)) {
                    return Character.toCodePoint(cPrevious2, cPrevious);
                }
                this.iter.next();
            }
            return cPrevious;
        }
        return -1;
    }

    @Override // sun.text.CodePointIterator
    public int charIndex() {
        return this.iter.getIndex();
    }
}
