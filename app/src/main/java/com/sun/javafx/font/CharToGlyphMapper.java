package com.sun.javafx.font;

/* loaded from: jfxrt.jar:com/sun/javafx/font/CharToGlyphMapper.class */
public abstract class CharToGlyphMapper {
    public static final int HI_SURROGATE_SHIFT = 10;
    public static final int HI_SURROGATE_START = 55296;
    public static final int HI_SURROGATE_END = 56319;
    public static final int LO_SURROGATE_START = 56320;
    public static final int LO_SURROGATE_END = 57343;
    public static final int SURROGATES_START = 65536;
    public static final int MISSING_GLYPH = 0;
    public static final int INVISIBLE_GLYPH_ID = 65535;
    protected int missingGlyph = 0;

    public abstract int getGlyphCode(int i2);

    public boolean canDisplay(char cp) {
        int glyph = charToGlyph(cp);
        return glyph != this.missingGlyph;
    }

    public int getMissingGlyphCode() {
        return this.missingGlyph;
    }

    public int charToGlyph(char unicode) {
        return getGlyphCode(unicode);
    }

    public int charToGlyph(int unicode) {
        return getGlyphCode(unicode);
    }

    public void charsToGlyphs(int start, int count, char[] unicodes, int[] glyphs, int glyphStart) {
        char low;
        int i2 = 0;
        while (i2 < count) {
            char c2 = unicodes[start + i2];
            if (c2 >= 55296 && c2 <= 56319 && i2 + 1 < count && (low = unicodes[start + i2 + 1]) >= 56320 && low <= 57343) {
                int code = ((((c2 - 55296) << 10) + low) - 56320) + 65536;
                glyphs[glyphStart + i2] = getGlyphCode(code);
                i2++;
                glyphs[glyphStart + i2] = 65535;
            } else {
                glyphs[glyphStart + i2] = getGlyphCode(c2);
            }
            i2++;
        }
    }

    public void charsToGlyphs(int start, int count, char[] unicodes, int[] glyphs) {
        charsToGlyphs(start, count, unicodes, glyphs, 0);
    }

    public void charsToGlyphs(int count, char[] unicodes, int[] glyphs) {
        charsToGlyphs(0, count, unicodes, glyphs, 0);
    }
}
