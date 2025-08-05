package sun.font;

/* loaded from: rt.jar:sun/font/Type1GlyphMapper.class */
public final class Type1GlyphMapper extends CharToGlyphMapper {
    Type1Font font;
    FontScaler scaler;

    public Type1GlyphMapper(Type1Font type1Font) {
        this.font = type1Font;
        initMapper();
    }

    private void initMapper() {
        this.scaler = this.font.getScaler();
        try {
            this.missingGlyph = this.scaler.getMissingGlyphCode();
        } catch (FontScalerException e2) {
            this.scaler = FontScaler.getNullScaler();
            try {
                this.missingGlyph = this.scaler.getMissingGlyphCode();
            } catch (FontScalerException e3) {
                this.missingGlyph = 0;
            }
        }
    }

    @Override // sun.font.CharToGlyphMapper
    public int getNumGlyphs() {
        try {
            return this.scaler.getNumGlyphs();
        } catch (FontScalerException e2) {
            this.scaler = FontScaler.getNullScaler();
            return getNumGlyphs();
        }
    }

    @Override // sun.font.CharToGlyphMapper
    public int getMissingGlyphCode() {
        return this.missingGlyph;
    }

    @Override // sun.font.CharToGlyphMapper
    public boolean canDisplay(char c2) {
        try {
            return this.scaler.getGlyphCode(c2) != this.missingGlyph;
        } catch (FontScalerException e2) {
            this.scaler = FontScaler.getNullScaler();
            return canDisplay(c2);
        }
    }

    @Override // sun.font.CharToGlyphMapper
    public int charToGlyph(char c2) {
        try {
            return this.scaler.getGlyphCode(c2);
        } catch (FontScalerException e2) {
            this.scaler = FontScaler.getNullScaler();
            return charToGlyph(c2);
        }
    }

    @Override // sun.font.CharToGlyphMapper
    public int charToGlyph(int i2) {
        if (i2 < 0 || i2 > 65535) {
            return this.missingGlyph;
        }
        try {
            return this.scaler.getGlyphCode((char) i2);
        } catch (FontScalerException e2) {
            this.scaler = FontScaler.getNullScaler();
            return charToGlyph(i2);
        }
    }

    @Override // sun.font.CharToGlyphMapper
    public void charsToGlyphs(int i2, char[] cArr, int[] iArr) {
        char c2;
        int i3 = 0;
        while (i3 < i2) {
            int i4 = cArr[i3];
            if (i4 >= 55296 && i4 <= 56319 && i3 < i2 - 1 && (c2 = cArr[i3 + 1]) >= 56320 && c2 <= 57343) {
                i4 = ((((i4 - 55296) * 1024) + c2) - 56320) + 65536;
                iArr[i3 + 1] = 65535;
            }
            iArr[i3] = charToGlyph(i4);
            if (i4 >= 65536) {
                i3++;
            }
            i3++;
        }
    }

    @Override // sun.font.CharToGlyphMapper
    public void charsToGlyphs(int i2, int[] iArr, int[] iArr2) {
        for (int i3 = 0; i3 < i2; i3++) {
            iArr2[i3] = charToGlyph(iArr[i3]);
        }
    }

    @Override // sun.font.CharToGlyphMapper
    public boolean charsToGlyphsNS(int i2, char[] cArr, int[] iArr) {
        char c2;
        int i3 = 0;
        while (i3 < i2) {
            int i4 = cArr[i3];
            if (i4 >= 55296 && i4 <= 56319 && i3 < i2 - 1 && (c2 = cArr[i3 + 1]) >= 56320 && c2 <= 57343) {
                i4 = ((((i4 - 55296) * 1024) + c2) - 56320) + 65536;
                iArr[i3 + 1] = 65535;
            }
            iArr[i3] = charToGlyph(i4);
            if (i4 >= 768) {
                if (FontUtilities.isComplexCharCode(i4)) {
                    return true;
                }
                if (i4 >= 65536) {
                    i3++;
                }
            }
            i3++;
        }
        return false;
    }
}
