package java.text;

import sun.text.bidi.BidiBase;

/* loaded from: rt.jar:java/text/Bidi.class */
public final class Bidi {
    public static final int DIRECTION_LEFT_TO_RIGHT = 0;
    public static final int DIRECTION_RIGHT_TO_LEFT = 1;
    public static final int DIRECTION_DEFAULT_LEFT_TO_RIGHT = -2;
    public static final int DIRECTION_DEFAULT_RIGHT_TO_LEFT = -1;
    private BidiBase bidiBase;

    public Bidi(String str, int i2) {
        if (str == null) {
            throw new IllegalArgumentException("paragraph is null");
        }
        this.bidiBase = new BidiBase(str.toCharArray(), 0, null, 0, str.length(), i2);
    }

    public Bidi(AttributedCharacterIterator attributedCharacterIterator) {
        if (attributedCharacterIterator == null) {
            throw new IllegalArgumentException("paragraph is null");
        }
        this.bidiBase = new BidiBase(0, 0);
        this.bidiBase.setPara(attributedCharacterIterator);
    }

    public Bidi(char[] cArr, int i2, byte[] bArr, int i3, int i4, int i5) {
        if (cArr == null) {
            throw new IllegalArgumentException("text is null");
        }
        if (i4 < 0) {
            throw new IllegalArgumentException("bad length: " + i4);
        }
        if (i2 < 0 || i4 > cArr.length - i2) {
            throw new IllegalArgumentException("bad range: " + i2 + " length: " + i4 + " for text of length: " + cArr.length);
        }
        if (bArr != null && (i3 < 0 || i4 > bArr.length - i3)) {
            throw new IllegalArgumentException("bad range: " + i3 + " length: " + i4 + " for embeddings of length: " + cArr.length);
        }
        this.bidiBase = new BidiBase(cArr, i2, bArr, i3, i4, i5);
    }

    public Bidi createLineBidi(int i2, int i3) {
        Bidi bidi = new Bidi(new AttributedString("").getIterator());
        return this.bidiBase.setLine(this, this.bidiBase, bidi, bidi.bidiBase, i2, i3);
    }

    public boolean isMixed() {
        return this.bidiBase.isMixed();
    }

    public boolean isLeftToRight() {
        return this.bidiBase.isLeftToRight();
    }

    public boolean isRightToLeft() {
        return this.bidiBase.isRightToLeft();
    }

    public int getLength() {
        return this.bidiBase.getLength();
    }

    public boolean baseIsLeftToRight() {
        return this.bidiBase.baseIsLeftToRight();
    }

    public int getBaseLevel() {
        return this.bidiBase.getParaLevel();
    }

    public int getLevelAt(int i2) {
        return this.bidiBase.getLevelAt(i2);
    }

    public int getRunCount() {
        return this.bidiBase.countRuns();
    }

    public int getRunLevel(int i2) {
        return this.bidiBase.getRunLevel(i2);
    }

    public int getRunStart(int i2) {
        return this.bidiBase.getRunStart(i2);
    }

    public int getRunLimit(int i2) {
        return this.bidiBase.getRunLimit(i2);
    }

    public static boolean requiresBidi(char[] cArr, int i2, int i3) {
        return BidiBase.requiresBidi(cArr, i2, i3);
    }

    public static void reorderVisually(byte[] bArr, int i2, Object[] objArr, int i3, int i4) {
        BidiBase.reorderVisually(bArr, i2, objArr, i3, i4);
    }

    public String toString() {
        return this.bidiBase.toString();
    }
}
