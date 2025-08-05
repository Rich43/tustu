package sun.font;

/* loaded from: rt.jar:sun/font/CharToGlyphMapper.class */
public abstract class CharToGlyphMapper {
    public static final int HI_SURROGATE_START = 55296;
    public static final int HI_SURROGATE_END = 56319;
    public static final int LO_SURROGATE_START = 56320;
    public static final int LO_SURROGATE_END = 57343;
    public static final int UNINITIALIZED_GLYPH = -1;
    public static final int INVISIBLE_GLYPH_ID = 65535;
    public static final int INVISIBLE_GLYPHS = 65534;
    protected int missingGlyph = -1;

    public abstract int getNumGlyphs();

    public abstract void charsToGlyphs(int i2, char[] cArr, int[] iArr);

    public abstract boolean charsToGlyphsNS(int i2, char[] cArr, int[] iArr);

    public abstract void charsToGlyphs(int i2, int[] iArr, int[] iArr2);

    public int getMissingGlyphCode() {
        return this.missingGlyph;
    }

    public boolean canDisplay(char c2) {
        return charToGlyph(c2) != this.missingGlyph;
    }

    public boolean canDisplay(int i2) {
        return charToGlyph(i2) != this.missingGlyph;
    }

    public int charToGlyph(char c2) {
        int[] iArr = new int[1];
        charsToGlyphs(1, new char[]{c2}, iArr);
        return iArr[0];
    }

    public int charToGlyph(int i2) {
        int[] iArr = new int[1];
        charsToGlyphs(1, new int[]{i2}, iArr);
        return iArr[0];
    }
}
