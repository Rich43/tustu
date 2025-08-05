package sun.font;

/* loaded from: rt.jar:sun/font/CompositeGlyphMapper.class */
public class CompositeGlyphMapper extends CharToGlyphMapper {
    public static final int SLOTMASK = -16777216;
    public static final int GLYPHMASK = 16777215;
    public static final int NBLOCKS = 216;
    public static final int BLOCKSZ = 256;
    public static final int MAXUNICODE = 55296;
    CompositeFont font;
    CharToGlyphMapper[] slotMappers;
    int[][] glyphMaps;
    private boolean hasExcludes;

    public CompositeGlyphMapper(CompositeFont compositeFont) {
        this.font = compositeFont;
        initMapper();
        this.hasExcludes = (compositeFont.exclusionRanges == null || compositeFont.maxIndices == null) ? false : true;
    }

    public final int compositeGlyphCode(int i2, int i3) {
        return (i2 << 24) | (i3 & 16777215);
    }

    /* JADX WARN: Type inference failed for: r1v12, types: [int[], int[][]] */
    private final void initMapper() {
        if (this.missingGlyph == -1) {
            if (this.glyphMaps == null) {
                this.glyphMaps = new int[216];
            }
            this.slotMappers = new CharToGlyphMapper[this.font.numSlots];
            this.missingGlyph = this.font.getSlotFont(0).getMissingGlyphCode();
            this.missingGlyph = compositeGlyphCode(0, this.missingGlyph);
        }
    }

    private int getCachedGlyphCode(int i2) {
        int[] iArr;
        if (i2 >= 55296 || (iArr = this.glyphMaps[i2 >> 8]) == null) {
            return -1;
        }
        return iArr[i2 & 255];
    }

    private void setCachedGlyphCode(int i2, int i3) {
        if (i2 >= 55296) {
            return;
        }
        int i4 = i2 >> 8;
        if (this.glyphMaps[i4] == null) {
            this.glyphMaps[i4] = new int[256];
            for (int i5 = 0; i5 < 256; i5++) {
                this.glyphMaps[i4][i5] = -1;
            }
        }
        this.glyphMaps[i4][i2 & 255] = i3;
    }

    private final CharToGlyphMapper getSlotMapper(int i2) {
        CharToGlyphMapper mapper = this.slotMappers[i2];
        if (mapper == null) {
            mapper = this.font.getSlotFont(i2).getMapper();
            this.slotMappers[i2] = mapper;
        }
        return mapper;
    }

    private final int convertToGlyph(int i2) {
        CharToGlyphMapper slotMapper;
        int iCharToGlyph;
        for (int i3 = 0; i3 < this.font.numSlots; i3++) {
            if ((!this.hasExcludes || !this.font.isExcludedChar(i3, i2)) && (iCharToGlyph = (slotMapper = getSlotMapper(i3)).charToGlyph(i2)) != slotMapper.getMissingGlyphCode()) {
                int iCompositeGlyphCode = compositeGlyphCode(i3, iCharToGlyph);
                setCachedGlyphCode(i2, iCompositeGlyphCode);
                return iCompositeGlyphCode;
            }
        }
        return this.missingGlyph;
    }

    @Override // sun.font.CharToGlyphMapper
    public int getNumGlyphs() {
        int numGlyphs = 0;
        for (int i2 = 0; i2 < 1; i2++) {
            CharToGlyphMapper mapper = this.slotMappers[i2];
            if (mapper == null) {
                mapper = this.font.getSlotFont(i2).getMapper();
                this.slotMappers[i2] = mapper;
            }
            numGlyphs += mapper.getNumGlyphs();
        }
        return numGlyphs;
    }

    @Override // sun.font.CharToGlyphMapper
    public int charToGlyph(int i2) {
        int cachedGlyphCode = getCachedGlyphCode(i2);
        if (cachedGlyphCode == -1) {
            cachedGlyphCode = convertToGlyph(i2);
        }
        return cachedGlyphCode;
    }

    public int charToGlyph(int i2, int i3) {
        CharToGlyphMapper slotMapper;
        int iCharToGlyph;
        if (i3 >= 0 && (iCharToGlyph = (slotMapper = getSlotMapper(i3)).charToGlyph(i2)) != slotMapper.getMissingGlyphCode()) {
            return compositeGlyphCode(i3, iCharToGlyph);
        }
        return charToGlyph(i2);
    }

    @Override // sun.font.CharToGlyphMapper
    public int charToGlyph(char c2) {
        int cachedGlyphCode = getCachedGlyphCode(c2);
        if (cachedGlyphCode == -1) {
            cachedGlyphCode = convertToGlyph(c2);
        }
        return cachedGlyphCode;
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
            int cachedGlyphCode = getCachedGlyphCode(i4);
            iArr[i3] = cachedGlyphCode;
            if (cachedGlyphCode == -1) {
                iArr[i3] = convertToGlyph(i4);
            }
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

    @Override // sun.font.CharToGlyphMapper
    public void charsToGlyphs(int i2, char[] cArr, int[] iArr) {
        char c2;
        int i3 = 0;
        while (i3 < i2) {
            char c3 = cArr[i3];
            if (c3 >= 55296 && c3 <= 56319 && i3 < i2 - 1 && (c2 = cArr[i3 + 1]) >= 56320 && c2 <= 57343) {
                int i4 = ((((c3 - 55296) * 1024) + c2) - 56320) + 65536;
                int cachedGlyphCode = getCachedGlyphCode(i4);
                iArr[i3] = cachedGlyphCode;
                if (cachedGlyphCode == -1) {
                    iArr[i3] = convertToGlyph(i4);
                }
                i3++;
                iArr[i3] = 65535;
            } else {
                int cachedGlyphCode2 = getCachedGlyphCode(c3);
                iArr[i3] = cachedGlyphCode2;
                if (cachedGlyphCode2 == -1) {
                    iArr[i3] = convertToGlyph(c3);
                }
            }
            i3++;
        }
    }

    @Override // sun.font.CharToGlyphMapper
    public void charsToGlyphs(int i2, int[] iArr, int[] iArr2) {
        for (int i3 = 0; i3 < i2; i3++) {
            int i4 = iArr[i3];
            iArr2[i3] = getCachedGlyphCode(i4);
            if (iArr2[i3] == -1) {
                iArr2[i3] = convertToGlyph(i4);
            }
        }
    }
}
