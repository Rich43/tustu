package com.sun.javafx.font;

import com.sun.javafx.font.FontFileReader;

/* loaded from: jfxrt.jar:com/sun/javafx/font/CMap.class */
abstract class CMap {
    static final char noSuchChar = 65533;
    static final int SHORTMASK = 65535;
    static final int INTMASK = -1;
    private static final int MAX_CODE_POINTS = 1114111;
    public static final NullCMapClass theNullCmap = new NullCMapClass();

    abstract char getGlyph(int i2);

    CMap() {
    }

    static CMap initialize(PrismFontFile font) {
        CMap cmap = null;
        int three0 = 0;
        int three1 = 0;
        int three10 = 0;
        int zeroStarOffset = 0;
        boolean zeroStar = false;
        boolean threeStar = false;
        FontFileReader.Buffer cmapBuffer = font.readTable(1668112752);
        short numberSubTables = cmapBuffer.getShort(2);
        for (int i2 = 0; i2 < numberSubTables; i2++) {
            cmapBuffer.position((i2 * 8) + 4);
            int platformID = cmapBuffer.getShort();
            if (platformID == 0) {
                zeroStar = true;
                cmapBuffer.getShort();
                zeroStarOffset = cmapBuffer.getInt();
            } else if (platformID == 3) {
                threeStar = true;
                int encodingID = cmapBuffer.getShort();
                int offset = cmapBuffer.getInt();
                switch (encodingID) {
                    case 0:
                        three0 = offset;
                        break;
                    case 1:
                        three1 = offset;
                        break;
                    case 10:
                        three10 = offset;
                        break;
                }
            }
        }
        if (threeStar) {
            if (three10 != 0) {
                cmap = createCMap(cmapBuffer, three10);
            } else if (three0 != 0) {
                cmap = createCMap(cmapBuffer, three0);
            } else if (three1 != 0) {
                cmap = createCMap(cmapBuffer, three1);
            }
        } else if (zeroStar && zeroStarOffset != 0) {
            cmap = createCMap(cmapBuffer, zeroStarOffset);
        } else {
            cmap = createCMap(cmapBuffer, cmapBuffer.getInt(8));
        }
        return cmap;
    }

    static CMap createCMap(FontFileReader.Buffer buffer, int offset) {
        int subtableFormat = buffer.getChar(offset);
        switch (subtableFormat) {
            case 0:
                return new CMapFormat0(buffer, offset);
            case 1:
            case 3:
            case 5:
            case 7:
            case 9:
            case 11:
            default:
                throw new RuntimeException("Cmap format unimplemented: " + ((int) buffer.getChar(offset)));
            case 2:
                return new CMapFormat2(buffer, offset);
            case 4:
                return new CMapFormat4(buffer, offset);
            case 6:
                return new CMapFormat6(buffer, offset);
            case 8:
                return new CMapFormat8(buffer, offset);
            case 10:
                return new CMapFormat10(buffer, offset);
            case 12:
                return new CMapFormat12(buffer, offset);
        }
    }

    /* loaded from: jfxrt.jar:com/sun/javafx/font/CMap$CMapFormat4.class */
    static class CMapFormat4 extends CMap {
        int segCount;
        int entrySelector;
        int rangeShift;
        char[] endCount;
        char[] startCount;
        short[] idDelta;
        char[] idRangeOffset;
        char[] glyphIds;

        CMapFormat4(FontFileReader.Buffer buffer, int offset) {
            buffer.position(offset);
            buffer.getChar();
            int subtableLength = buffer.getChar();
            subtableLength = offset + subtableLength > buffer.capacity() ? buffer.capacity() - offset : subtableLength;
            buffer.getChar();
            this.segCount = buffer.getChar() / 2;
            buffer.getChar();
            this.entrySelector = buffer.getChar();
            this.rangeShift = buffer.getChar() / 2;
            this.startCount = new char[this.segCount];
            this.endCount = new char[this.segCount];
            this.idDelta = new short[this.segCount];
            this.idRangeOffset = new char[this.segCount];
            for (int i2 = 0; i2 < this.segCount; i2++) {
                this.endCount[i2] = buffer.getChar();
            }
            buffer.getChar();
            for (int i3 = 0; i3 < this.segCount; i3++) {
                this.startCount[i3] = buffer.getChar();
            }
            for (int i4 = 0; i4 < this.segCount; i4++) {
                this.idDelta[i4] = (short) buffer.getChar();
            }
            for (int i5 = 0; i5 < this.segCount; i5++) {
                char ctmp = buffer.getChar();
                this.idRangeOffset[i5] = (char) ((ctmp >> 1) & 65535);
            }
            int pos = ((this.segCount * 8) + 16) / 2;
            buffer.position((pos * 2) + offset);
            int numGlyphIds = (subtableLength / 2) - pos;
            this.glyphIds = new char[numGlyphIds];
            for (int i6 = 0; i6 < numGlyphIds; i6++) {
                this.glyphIds[i6] = buffer.getChar();
            }
        }

        @Override // com.sun.javafx.font.CMap
        char getGlyph(int charCode) {
            int index;
            char glyphCode = 0;
            int controlGlyph = getControlCodeGlyph(charCode, true);
            if (controlGlyph >= 0) {
                return (char) controlGlyph;
            }
            int left = 0;
            int right = this.startCount.length;
            int length = this.startCount.length;
            while (true) {
                index = length >> 1;
                if (left >= right) {
                    break;
                }
                if (this.endCount[index] < charCode) {
                    left = index + 1;
                } else {
                    right = index;
                }
                length = left + right;
            }
            if (charCode >= this.startCount[index] && charCode <= this.endCount[index]) {
                char c2 = this.idRangeOffset[index];
                if (c2 == 0) {
                    glyphCode = (char) (charCode + this.idDelta[index]);
                } else {
                    int glyphIDIndex = (c2 - this.segCount) + index + (charCode - this.startCount[index]);
                    glyphCode = this.glyphIds[glyphIDIndex];
                    if (glyphCode != 0) {
                        glyphCode = (char) (glyphCode + this.idDelta[index]);
                    }
                }
            }
            return glyphCode;
        }
    }

    /* loaded from: jfxrt.jar:com/sun/javafx/font/CMap$CMapFormat0.class */
    static class CMapFormat0 extends CMap {
        byte[] cmap;

        CMapFormat0(FontFileReader.Buffer buffer, int offset) {
            int len = buffer.getChar(offset + 2);
            this.cmap = new byte[len - 6];
            buffer.get(offset + 6, this.cmap, 0, len - 6);
        }

        @Override // com.sun.javafx.font.CMap
        char getGlyph(int charCode) {
            if (charCode < 256) {
                if (charCode < 16) {
                    switch (charCode) {
                        case 9:
                        case 10:
                        case 13:
                            return (char) 65535;
                    }
                }
                return (char) (255 & this.cmap[charCode]);
            }
            return (char) 0;
        }
    }

    /* loaded from: jfxrt.jar:com/sun/javafx/font/CMap$CMapFormat2.class */
    static class CMapFormat2 extends CMap {
        char[] subHeaderKey = new char[256];
        char[] firstCodeArray;
        char[] entryCountArray;
        short[] idDeltaArray;
        char[] idRangeOffSetArray;
        char[] glyphIndexArray;

        CMapFormat2(FontFileReader.Buffer buffer, int offset) {
            int tableLen = buffer.getChar(offset + 2);
            buffer.position(offset + 6);
            char maxSubHeader = 0;
            for (int i2 = 0; i2 < 256; i2++) {
                this.subHeaderKey[i2] = buffer.getChar();
                if (this.subHeaderKey[i2] > maxSubHeader) {
                    maxSubHeader = this.subHeaderKey[i2];
                }
            }
            int numSubHeaders = (maxSubHeader >> 3) + 1;
            this.firstCodeArray = new char[numSubHeaders];
            this.entryCountArray = new char[numSubHeaders];
            this.idDeltaArray = new short[numSubHeaders];
            this.idRangeOffSetArray = new char[numSubHeaders];
            for (int i3 = 0; i3 < numSubHeaders; i3++) {
                this.firstCodeArray[i3] = buffer.getChar();
                this.entryCountArray[i3] = buffer.getChar();
                this.idDeltaArray[i3] = (short) buffer.getChar();
                this.idRangeOffSetArray[i3] = buffer.getChar();
            }
            int glyphIndexArrSize = ((tableLen - 518) - (numSubHeaders * 8)) / 2;
            this.glyphIndexArray = new char[glyphIndexArrSize];
            for (int i4 = 0; i4 < glyphIndexArrSize; i4++) {
                this.glyphIndexArray[i4] = buffer.getChar();
            }
        }

        @Override // com.sun.javafx.font.CMap
        char getGlyph(int charCode) {
            char mapMe;
            char mapMe2;
            int controlGlyph = getControlCodeGlyph(charCode, true);
            if (controlGlyph >= 0) {
                return (char) controlGlyph;
            }
            char highByte = (char) (charCode >> 8);
            char lowByte = (char) (charCode & 255);
            int key = this.subHeaderKey[highByte] >> 3;
            if (key != 0) {
                mapMe = lowByte;
            } else {
                mapMe = highByte;
                if (mapMe == 0) {
                    mapMe = lowByte;
                }
            }
            char firstCode = this.firstCodeArray[key];
            if (mapMe >= firstCode && (mapMe2 = (char) (mapMe - firstCode)) < this.entryCountArray[key]) {
                int glyphArrayOffset = ((this.idRangeOffSetArray.length - key) * 8) - 6;
                int glyphSubArrayStart = (this.idRangeOffSetArray[key] - glyphArrayOffset) / 2;
                char glyphCode = this.glyphIndexArray[glyphSubArrayStart + mapMe2];
                if (glyphCode != 0) {
                    return (char) (glyphCode + this.idDeltaArray[key]);
                }
                return (char) 0;
            }
            return (char) 0;
        }
    }

    /* loaded from: jfxrt.jar:com/sun/javafx/font/CMap$CMapFormat6.class */
    static class CMapFormat6 extends CMap {
        char firstCode;
        char entryCount;
        char[] glyphIdArray;

        CMapFormat6(FontFileReader.Buffer buffer, int offset) {
            buffer.position(offset + 6);
            this.firstCode = buffer.getChar();
            this.entryCount = buffer.getChar();
            this.glyphIdArray = new char[this.entryCount];
            for (int i2 = 0; i2 < this.entryCount; i2++) {
                this.glyphIdArray[i2] = buffer.getChar();
            }
        }

        @Override // com.sun.javafx.font.CMap
        char getGlyph(int charCode) {
            int controlGlyph = getControlCodeGlyph(charCode, true);
            if (controlGlyph >= 0) {
                return (char) controlGlyph;
            }
            int charCode2 = charCode - this.firstCode;
            if (charCode2 < 0 || charCode2 >= this.entryCount) {
                return (char) 0;
            }
            return this.glyphIdArray[charCode2];
        }
    }

    /* loaded from: jfxrt.jar:com/sun/javafx/font/CMap$CMapFormat8.class */
    static class CMapFormat8 extends CMap {
        CMapFormat8(FontFileReader.Buffer buffer, int offset) {
        }

        @Override // com.sun.javafx.font.CMap
        char getGlyph(int charCode) {
            return (char) 0;
        }
    }

    /* loaded from: jfxrt.jar:com/sun/javafx/font/CMap$CMapFormat10.class */
    static class CMapFormat10 extends CMap {
        long startCharCode;
        int numChars;
        char[] glyphIdArray;

        CMapFormat10(FontFileReader.Buffer buffer, int offset) {
            buffer.position(offset + 12);
            this.startCharCode = buffer.getInt() & (-1);
            this.numChars = buffer.getInt() & (-1);
            if (this.numChars <= 0 || this.numChars > 1114111 || offset > ((buffer.capacity() - (this.numChars * 2)) - 12) - 8) {
                throw new RuntimeException("Invalid cmap subtable");
            }
            this.glyphIdArray = new char[this.numChars];
            for (int i2 = 0; i2 < this.numChars; i2++) {
                this.glyphIdArray[i2] = buffer.getChar();
            }
        }

        @Override // com.sun.javafx.font.CMap
        char getGlyph(int charCode) {
            int code = (int) (charCode - this.startCharCode);
            if (code < 0 || code >= this.numChars) {
                return (char) 0;
            }
            return this.glyphIdArray[code];
        }
    }

    /* loaded from: jfxrt.jar:com/sun/javafx/font/CMap$CMapFormat12.class */
    static class CMapFormat12 extends CMap {
        int numGroups;
        int highBit = 0;
        int power;
        int extra;
        long[] startCharCode;
        long[] endCharCode;
        int[] startGlyphID;

        CMapFormat12(FontFileReader.Buffer buffer, int offset) {
            this.numGroups = buffer.getInt(offset + 12);
            if (this.numGroups <= 0 || this.numGroups > 1114111 || offset > ((buffer.capacity() - (this.numGroups * 12)) - 12) - 4) {
                throw new RuntimeException("Invalid cmap subtable");
            }
            this.startCharCode = new long[this.numGroups];
            this.endCharCode = new long[this.numGroups];
            this.startGlyphID = new int[this.numGroups];
            buffer.position(offset + 16);
            for (int i2 = 0; i2 < this.numGroups; i2++) {
                this.startCharCode[i2] = buffer.getInt() & (-1);
                this.endCharCode[i2] = buffer.getInt() & (-1);
                this.startGlyphID[i2] = buffer.getInt() & (-1);
            }
            int value = this.numGroups;
            if (value >= 65536) {
                value >>= 16;
                this.highBit += 16;
            }
            if (value >= 256) {
                value >>= 8;
                this.highBit += 8;
            }
            if (value >= 16) {
                value >>= 4;
                this.highBit += 4;
            }
            if (value >= 4) {
                value >>= 2;
                this.highBit += 2;
            }
            if (value >= 2) {
                int i3 = value >> 1;
                this.highBit++;
            }
            this.power = 1 << this.highBit;
            this.extra = this.numGroups - this.power;
        }

        @Override // com.sun.javafx.font.CMap
        char getGlyph(int charCode) {
            int controlGlyph = getControlCodeGlyph(charCode, false);
            if (controlGlyph >= 0) {
                return (char) controlGlyph;
            }
            int probe = this.power;
            int range = 0;
            if (this.startCharCode[this.extra] <= charCode) {
                range = this.extra;
            }
            while (probe > 1) {
                probe >>= 1;
                if (this.startCharCode[range + probe] <= charCode) {
                    range += probe;
                }
            }
            if (this.startCharCode[range] <= charCode && this.endCharCode[range] >= charCode) {
                return (char) (this.startGlyphID[range] + (charCode - this.startCharCode[range]));
            }
            return (char) 0;
        }
    }

    /* loaded from: jfxrt.jar:com/sun/javafx/font/CMap$NullCMapClass.class */
    static class NullCMapClass extends CMap {
        NullCMapClass() {
        }

        @Override // com.sun.javafx.font.CMap
        char getGlyph(int charCode) {
            return (char) 0;
        }
    }

    final int getControlCodeGlyph(int charCode, boolean noSurrogates) {
        if (charCode < 16) {
            switch (charCode) {
                case 9:
                case 10:
                case 13:
                    return 65535;
                case 11:
                case 12:
                default:
                    return -1;
            }
        }
        if (charCode >= 8204) {
            if (charCode <= 8207) {
                return 65535;
            }
            if (charCode >= 8232 && charCode <= 8238) {
                return 65535;
            }
            if (charCode >= 8298 && charCode <= 8303) {
                return 65535;
            }
            if (noSurrogates && charCode >= 65535) {
                return 0;
            }
            return -1;
        }
        return -1;
    }
}
