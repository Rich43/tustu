package javax.swing.text;

import java.text.BreakIterator;
import java.text.CharacterIterator;
import java.text.StringCharacterIterator;
import java.util.Arrays;

/* loaded from: rt.jar:javax/swing/text/WhitespaceBasedBreakIterator.class */
class WhitespaceBasedBreakIterator extends BreakIterator {
    private char[] text = new char[0];
    private int[] breaks = {0};
    private int pos = 0;

    WhitespaceBasedBreakIterator() {
    }

    @Override // java.text.BreakIterator
    public void setText(CharacterIterator characterIterator) {
        int beginIndex = characterIterator.getBeginIndex();
        this.text = new char[characterIterator.getEndIndex() - beginIndex];
        int[] iArr = new int[this.text.length + 1];
        int i2 = 0 + 1;
        iArr[0] = beginIndex;
        int i3 = 0;
        boolean z2 = false;
        char cFirst = characterIterator.first();
        while (true) {
            char c2 = cFirst;
            if (c2 == 65535) {
                break;
            }
            this.text[i3] = c2;
            boolean zIsWhitespace = Character.isWhitespace(c2);
            if (z2 && !zIsWhitespace) {
                int i4 = i2;
                i2++;
                iArr[i4] = i3 + beginIndex;
            }
            z2 = zIsWhitespace;
            i3++;
            cFirst = characterIterator.next();
        }
        if (this.text.length > 0) {
            int i5 = i2;
            i2++;
            iArr[i5] = this.text.length + beginIndex;
        }
        int[] iArr2 = new int[i2];
        this.breaks = iArr2;
        System.arraycopy(iArr, 0, iArr2, 0, i2);
    }

    @Override // java.text.BreakIterator
    public CharacterIterator getText() {
        return new StringCharacterIterator(new String(this.text));
    }

    @Override // java.text.BreakIterator
    public int first() {
        int[] iArr = this.breaks;
        this.pos = 0;
        return iArr[0];
    }

    @Override // java.text.BreakIterator
    public int last() {
        int[] iArr = this.breaks;
        int length = this.breaks.length - 1;
        this.pos = length;
        return iArr[length];
    }

    @Override // java.text.BreakIterator
    public int current() {
        return this.breaks[this.pos];
    }

    @Override // java.text.BreakIterator
    public int next() {
        if (this.pos == this.breaks.length - 1) {
            return -1;
        }
        int[] iArr = this.breaks;
        int i2 = this.pos + 1;
        this.pos = i2;
        return iArr[i2];
    }

    @Override // java.text.BreakIterator
    public int previous() {
        if (this.pos == 0) {
            return -1;
        }
        int[] iArr = this.breaks;
        int i2 = this.pos - 1;
        this.pos = i2;
        return iArr[i2];
    }

    @Override // java.text.BreakIterator
    public int next(int i2) {
        return checkhit(this.pos + i2);
    }

    @Override // java.text.BreakIterator
    public int following(int i2) {
        return adjacent(i2, 1);
    }

    @Override // java.text.BreakIterator
    public int preceding(int i2) {
        return adjacent(i2, -1);
    }

    private int checkhit(int i2) {
        if (i2 < 0 || i2 >= this.breaks.length) {
            return -1;
        }
        int[] iArr = this.breaks;
        this.pos = i2;
        return iArr[i2];
    }

    private int adjacent(int i2, int i3) {
        int iBinarySearch = Arrays.binarySearch(this.breaks, i2);
        return checkhit(Math.abs(iBinarySearch) + i3 + (iBinarySearch < 0 ? i3 < 0 ? -1 : -2 : 0));
    }
}
