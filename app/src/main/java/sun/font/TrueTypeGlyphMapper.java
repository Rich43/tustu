package sun.font;

import java.nio.ByteBuffer;
import java.util.Locale;
import sun.font.CMap;

/* loaded from: rt.jar:sun/font/TrueTypeGlyphMapper.class */
public class TrueTypeGlyphMapper extends CharToGlyphMapper {
    static final char REVERSE_SOLIDUS = '\\';
    static final char JA_YEN = 165;
    static final char JA_FULLWIDTH_TILDE_CHAR = 65374;
    static final char JA_WAVE_DASH_CHAR = 12316;
    static final boolean isJAlocale = Locale.JAPAN.equals(Locale.getDefault());
    private final boolean needsJAremapping;
    private boolean remapJAWaveDash;
    TrueTypeFont font;
    CMap cmap;
    int numGlyphs;

    public TrueTypeGlyphMapper(TrueTypeFont trueTypeFont) {
        this.font = trueTypeFont;
        try {
            this.cmap = CMap.initialize(trueTypeFont);
        } catch (Exception e2) {
            this.cmap = null;
        }
        if (this.cmap == null) {
            handleBadCMAP();
        }
        this.missingGlyph = 0;
        ByteBuffer tableBuffer = trueTypeFont.getTableBuffer(1835104368);
        if (tableBuffer != null && tableBuffer.capacity() >= 6) {
            this.numGlyphs = tableBuffer.getChar(4);
        } else {
            handleBadCMAP();
        }
        if (FontUtilities.isSolaris && isJAlocale && trueTypeFont.supportsJA()) {
            this.needsJAremapping = true;
            if (FontUtilities.isSolaris8 && getGlyphFromCMAP(JA_WAVE_DASH_CHAR) == this.missingGlyph) {
                this.remapJAWaveDash = true;
                return;
            }
            return;
        }
        this.needsJAremapping = false;
    }

    @Override // sun.font.CharToGlyphMapper
    public int getNumGlyphs() {
        return this.numGlyphs;
    }

    private char getGlyphFromCMAP(int i2) {
        try {
            char glyph = this.cmap.getGlyph(i2);
            if (glyph < this.numGlyphs || glyph >= 65534) {
                return glyph;
            }
            if (FontUtilities.isLogging()) {
                FontUtilities.getLogger().warning(((Object) this.font) + " out of range glyph id=" + Integer.toHexString(glyph) + " for char " + Integer.toHexString(i2));
            }
            return (char) this.missingGlyph;
        } catch (Exception e2) {
            handleBadCMAP();
            return (char) this.missingGlyph;
        }
    }

    private void handleBadCMAP() {
        if (FontUtilities.isLogging()) {
            FontUtilities.getLogger().severe("Null Cmap for " + ((Object) this.font) + "substituting for this font");
        }
        SunFontManager.getInstance().deRegisterBadFont(this.font);
        this.cmap = CMap.theNullCmap;
    }

    private final char remapJAChar(char c2) {
        switch (c2) {
            case '\\':
                return (char) 165;
            case JA_WAVE_DASH_CHAR /* 12316 */:
                if (this.remapJAWaveDash) {
                    return (char) 65374;
                }
                break;
        }
        return c2;
    }

    private final int remapJAIntChar(int i2) {
        switch (i2) {
            case 92:
                return 165;
            case JA_WAVE_DASH_CHAR /* 12316 */:
                if (this.remapJAWaveDash) {
                    return JA_FULLWIDTH_TILDE_CHAR;
                }
                break;
        }
        return i2;
    }

    @Override // sun.font.CharToGlyphMapper
    public int charToGlyph(char c2) {
        if (this.needsJAremapping) {
            c2 = remapJAChar(c2);
        }
        char glyphFromCMAP = getGlyphFromCMAP(c2);
        if (this.font.checkUseNatives() && glyphFromCMAP < this.font.glyphToCharMap.length) {
            this.font.glyphToCharMap[glyphFromCMAP] = c2;
        }
        return glyphFromCMAP;
    }

    @Override // sun.font.CharToGlyphMapper
    public int charToGlyph(int i2) {
        if (this.needsJAremapping) {
            i2 = remapJAIntChar(i2);
        }
        char glyphFromCMAP = getGlyphFromCMAP(i2);
        if (this.font.checkUseNatives() && glyphFromCMAP < this.font.glyphToCharMap.length) {
            this.font.glyphToCharMap[glyphFromCMAP] = (char) i2;
        }
        return glyphFromCMAP;
    }

    @Override // sun.font.CharToGlyphMapper
    public void charsToGlyphs(int i2, int[] iArr, int[] iArr2) {
        for (int i3 = 0; i3 < i2; i3++) {
            if (this.needsJAremapping) {
                iArr2[i3] = getGlyphFromCMAP(remapJAIntChar(iArr[i3]));
            } else {
                iArr2[i3] = getGlyphFromCMAP(iArr[i3]);
            }
            if (this.font.checkUseNatives() && iArr2[i3] < this.font.glyphToCharMap.length) {
                this.font.glyphToCharMap[iArr2[i3]] = (char) iArr[i3];
            }
        }
    }

    @Override // sun.font.CharToGlyphMapper
    public void charsToGlyphs(int i2, char[] cArr, int[] iArr) {
        char cRemapJAChar;
        char c2;
        int i3 = 0;
        while (i3 < i2) {
            if (this.needsJAremapping) {
                cRemapJAChar = remapJAChar(cArr[i3]);
            } else {
                cRemapJAChar = cArr[i3];
            }
            if (cRemapJAChar >= 55296 && cRemapJAChar <= 56319 && i3 < i2 - 1 && (c2 = cArr[i3 + 1]) >= 56320 && c2 <= 57343) {
                iArr[i3] = getGlyphFromCMAP(((((cRemapJAChar - 55296) * 1024) + c2) - 56320) + 65536);
                i3++;
                iArr[i3] = 65535;
            } else {
                iArr[i3] = getGlyphFromCMAP(cRemapJAChar);
                if (this.font.checkUseNatives() && iArr[i3] < this.font.glyphToCharMap.length) {
                    this.font.glyphToCharMap[iArr[i3]] = cRemapJAChar;
                }
            }
            i3++;
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v33, types: [int] */
    @Override // sun.font.CharToGlyphMapper
    public boolean charsToGlyphsNS(int i2, char[] cArr, int[] iArr) {
        char cRemapJAChar;
        char c2;
        int i3 = 0;
        while (i3 < i2) {
            if (this.needsJAremapping) {
                cRemapJAChar = remapJAChar(cArr[i3]);
            } else {
                cRemapJAChar = cArr[i3];
            }
            if (cRemapJAChar >= 55296 && cRemapJAChar <= 56319 && i3 < i2 - 1 && (c2 = cArr[i3 + 1]) >= 56320 && c2 <= 57343) {
                cRemapJAChar = ((((cRemapJAChar - 55296) * 1024) + c2) - 56320) + 65536;
                iArr[i3 + 1] = 65535;
            }
            iArr[i3] = getGlyphFromCMAP(cRemapJAChar);
            if (this.font.checkUseNatives() && iArr[i3] < this.font.glyphToCharMap.length) {
                this.font.glyphToCharMap[iArr[i3]] = cRemapJAChar;
            }
            if (cRemapJAChar >= 768) {
                if (FontUtilities.isComplexCharCode(cRemapJAChar)) {
                    return true;
                }
                if (cRemapJAChar >= 0) {
                    i3++;
                }
            }
            i3++;
        }
        return false;
    }

    boolean hasSupplementaryChars() {
        return (this.cmap instanceof CMap.CMapFormat8) || (this.cmap instanceof CMap.CMapFormat10) || (this.cmap instanceof CMap.CMapFormat12);
    }
}
