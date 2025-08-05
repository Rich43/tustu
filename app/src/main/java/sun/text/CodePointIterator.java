package sun.text;

import java.text.CharacterIterator;

/* loaded from: rt.jar:sun/text/CodePointIterator.class */
public abstract class CodePointIterator {
    public static final int DONE = -1;

    public abstract void setToStart();

    public abstract void setToLimit();

    public abstract int next();

    public abstract int prev();

    public abstract int charIndex();

    public static CodePointIterator create(char[] cArr) {
        return new CharArrayCodePointIterator(cArr);
    }

    public static CodePointIterator create(char[] cArr, int i2, int i3) {
        return new CharArrayCodePointIterator(cArr, i2, i3);
    }

    public static CodePointIterator create(CharSequence charSequence) {
        return new CharSequenceCodePointIterator(charSequence);
    }

    public static CodePointIterator create(CharacterIterator characterIterator) {
        return new CharacterIteratorCodePointIterator(characterIterator);
    }
}
