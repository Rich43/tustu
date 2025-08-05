package sun.text.normalizer;

import java.text.CharacterIterator;

/* loaded from: rt.jar:sun/text/normalizer/UCharacterIterator.class */
public abstract class UCharacterIterator implements Cloneable {
    public static final int DONE = -1;

    public abstract int current();

    public abstract int getLength();

    public abstract int getIndex();

    public abstract int next();

    public abstract int previous();

    public abstract void setIndex(int i2);

    public abstract int getText(char[] cArr, int i2);

    protected UCharacterIterator() {
    }

    public static final UCharacterIterator getInstance(String str) {
        return new ReplaceableUCharacterIterator(str);
    }

    public static final UCharacterIterator getInstance(StringBuffer stringBuffer) {
        return new ReplaceableUCharacterIterator(stringBuffer);
    }

    public static final UCharacterIterator getInstance(CharacterIterator characterIterator) {
        return new CharacterIteratorWrapper(characterIterator);
    }

    public int nextCodePoint() {
        int next = next();
        if (UTF16.isLeadSurrogate((char) next)) {
            int next2 = next();
            if (UTF16.isTrailSurrogate((char) next2)) {
                return UCharacterProperty.getRawSupplementary((char) next, (char) next2);
            }
            if (next2 != -1) {
                previous();
            }
        }
        return next;
    }

    public final int getText(char[] cArr) {
        return getText(cArr, 0);
    }

    public String getText() {
        char[] cArr = new char[getLength()];
        getText(cArr);
        return new String(cArr);
    }

    public int moveIndex(int i2) {
        int iMax = Math.max(0, Math.min(getIndex() + i2, getLength()));
        setIndex(iMax);
        return iMax;
    }

    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
