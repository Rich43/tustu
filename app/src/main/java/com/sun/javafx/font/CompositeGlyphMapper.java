package com.sun.javafx.font;

import java.util.HashMap;

/* loaded from: jfxrt.jar:com/sun/javafx/font/CompositeGlyphMapper.class */
public class CompositeGlyphMapper extends CharToGlyphMapper {
    public static final int SLOTMASK = -16777216;
    public static final int GLYPHMASK = 16777215;
    public static final int NBLOCKS = 216;
    public static final int BLOCKSZ = 256;
    public static final int MAXUNICODE = 55296;
    private static final int SIMPLE_ASCII_MASK_START = 32;
    private static final int SIMPLE_ASCII_MASK_END = 126;
    private static final int ASCII_COUNT = 95;
    private boolean asciiCacheOK;
    private char[] charToGlyph;
    CompositeFontResource font;
    CharToGlyphMapper[] slotMappers;
    HashMap<Integer, Integer> glyphMap;

    public CompositeGlyphMapper(CompositeFontResource compFont) {
        this.font = compFont;
        this.missingGlyph = 0;
        this.glyphMap = new HashMap<>();
        this.slotMappers = new CharToGlyphMapper[compFont.getNumSlots()];
        this.asciiCacheOK = true;
    }

    private final CharToGlyphMapper getSlotMapper(int slot) {
        if (slot >= this.slotMappers.length) {
            CharToGlyphMapper[] tmp = new CharToGlyphMapper[this.font.getNumSlots()];
            System.arraycopy(this.slotMappers, 0, tmp, 0, this.slotMappers.length);
            this.slotMappers = tmp;
        }
        CharToGlyphMapper mapper = this.slotMappers[slot];
        if (mapper == null) {
            mapper = this.font.getSlotResource(slot).getGlyphMapper();
            this.slotMappers[slot] = mapper;
        }
        return mapper;
    }

    @Override // com.sun.javafx.font.CharToGlyphMapper
    public int getMissingGlyphCode() {
        return this.missingGlyph;
    }

    public final int compositeGlyphCode(int slot, int glyphCode) {
        return (slot << 24) | (glyphCode & 16777215);
    }

    private final int convertToGlyph(int unicode) {
        for (int slot = 0; slot < this.font.getNumSlots(); slot++) {
            if (slot >= 255) {
                return this.missingGlyph;
            }
            CharToGlyphMapper mapper = getSlotMapper(slot);
            int glyphCode = mapper.charToGlyph(unicode);
            if (glyphCode != mapper.getMissingGlyphCode()) {
                int glyphCode2 = compositeGlyphCode(slot, glyphCode);
                this.glyphMap.put(Integer.valueOf(unicode), Integer.valueOf(glyphCode2));
                return glyphCode2;
            }
        }
        return this.missingGlyph;
    }

    private int getAsciiGlyphCode(int charCode) {
        if (!this.asciiCacheOK || charCode > 126 || charCode < 32) {
            return -1;
        }
        if (this.charToGlyph == null) {
            char[] glyphCodes = new char[95];
            CharToGlyphMapper mapper = getSlotMapper(0);
            int missingGlyphCode = mapper.getMissingGlyphCode();
            for (int i2 = 0; i2 < 95; i2++) {
                int glyphCode = mapper.charToGlyph(32 + i2);
                if (glyphCode == missingGlyphCode) {
                    this.charToGlyph = null;
                    this.asciiCacheOK = false;
                    return -1;
                }
                glyphCodes[i2] = (char) glyphCode;
            }
            this.charToGlyph = glyphCodes;
        }
        int index = charCode - 32;
        return this.charToGlyph[index];
    }

    @Override // com.sun.javafx.font.CharToGlyphMapper
    public int getGlyphCode(int charCode) {
        int retVal = getAsciiGlyphCode(charCode);
        if (retVal >= 0) {
            return retVal;
        }
        Integer codeInt = this.glyphMap.get(Integer.valueOf(charCode));
        if (codeInt != null) {
            return codeInt.intValue();
        }
        return convertToGlyph(charCode);
    }
}
