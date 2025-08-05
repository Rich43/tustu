package sun.font;

import com.sun.istack.internal.localization.Localizable;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.IntBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CodingErrorAction;

/* loaded from: rt.jar:sun/font/CMap.class */
abstract class CMap {
    static final short ShiftJISEncoding = 2;
    static final short GBKEncoding = 3;
    static final short Big5Encoding = 4;
    static final short WansungEncoding = 5;
    static final short JohabEncoding = 6;
    static final short MSUnicodeSurrogateEncoding = 10;
    static final char noSuchChar = 65533;
    static final int SHORTMASK = 65535;
    static final int INTMASK = Integer.MAX_VALUE;
    char[] xlat;
    static final char[][] converterMaps = new char[7];
    public static final NullCMapClass theNullCmap = new NullCMapClass();

    abstract char getGlyph(int i2);

    CMap() {
    }

    static CMap initialize(TrueTypeFont trueTypeFont) {
        CMap cMapCreateCMap = null;
        int i2 = 0;
        int i3 = 0;
        int i4 = 0;
        int i5 = 0;
        int i6 = 0;
        int i7 = 0;
        int i8 = 0;
        int i9 = 0;
        boolean z2 = false;
        ByteBuffer tableBuffer = trueTypeFont.getTableBuffer(1668112752);
        trueTypeFont.getTableSize(1668112752);
        short s2 = tableBuffer.getShort(2);
        for (int i10 = 0; i10 < s2; i10++) {
            tableBuffer.position((i10 * 8) + 4);
            if (tableBuffer.getShort() == 3) {
                z2 = true;
                short s3 = tableBuffer.getShort();
                int i11 = tableBuffer.getInt();
                switch (s3) {
                    case 0:
                        i2 = i11;
                        break;
                    case 1:
                        i3 = i11;
                        break;
                    case 2:
                        i4 = i11;
                        break;
                    case 3:
                        i5 = i11;
                        break;
                    case 4:
                        i6 = i11;
                        break;
                    case 5:
                        i7 = i11;
                        break;
                    case 6:
                        i8 = i11;
                        break;
                    case 10:
                        i9 = i11;
                        break;
                }
            }
        }
        if (z2) {
            if (i9 != 0) {
                cMapCreateCMap = createCMap(tableBuffer, i9, null);
            } else if (i2 != 0) {
                cMapCreateCMap = createCMap(tableBuffer, i2, null);
            } else if (i3 != 0) {
                cMapCreateCMap = createCMap(tableBuffer, i3, null);
            } else if (i4 != 0) {
                cMapCreateCMap = createCMap(tableBuffer, i4, getConverterMap((short) 2));
            } else if (i5 != 0) {
                cMapCreateCMap = createCMap(tableBuffer, i5, getConverterMap((short) 3));
            } else if (i6 != 0) {
                if (FontUtilities.isSolaris && trueTypeFont.platName != null && (trueTypeFont.platName.startsWith("/usr/openwin/lib/locale/zh_CN.EUC/X11/fonts/TrueType") || trueTypeFont.platName.startsWith("/usr/openwin/lib/locale/zh_CN/X11/fonts/TrueType") || trueTypeFont.platName.startsWith("/usr/openwin/lib/locale/zh/X11/fonts/TrueType"))) {
                    cMapCreateCMap = createCMap(tableBuffer, i6, getConverterMap((short) 3));
                } else {
                    cMapCreateCMap = createCMap(tableBuffer, i6, getConverterMap((short) 4));
                }
            } else if (i7 != 0) {
                cMapCreateCMap = createCMap(tableBuffer, i7, getConverterMap((short) 5));
            } else if (i8 != 0) {
                cMapCreateCMap = createCMap(tableBuffer, i8, getConverterMap((short) 6));
            }
        } else {
            cMapCreateCMap = createCMap(tableBuffer, tableBuffer.getInt(8), null);
        }
        return cMapCreateCMap;
    }

    static char[] getConverter(short s2) {
        int i2;
        int i3;
        String str;
        switch (s2) {
            case 2:
                i2 = 33088;
                i3 = 64764;
                str = "SJIS";
                break;
            case 3:
                i2 = 33088;
                i3 = 65184;
                str = "GBK";
                break;
            case 4:
                i2 = 41280;
                i3 = 65278;
                str = "Big5";
                break;
            case 5:
                i2 = 41377;
                i3 = 65246;
                str = "EUC_KR";
                break;
            case 6:
                i2 = 33089;
                i3 = 65022;
                str = "Johab";
                break;
            default:
                return null;
        }
        try {
            char[] cArr = new char[65536];
            for (int i4 = 0; i4 < 65536; i4++) {
                cArr[i4] = 65533;
            }
            byte[] bArr = new byte[((i3 - i2) + 1) * 2];
            char[] cArr2 = new char[(i3 - i2) + 1];
            int i5 = 0;
            if (s2 == 2) {
                for (int i6 = i2; i6 <= i3; i6++) {
                    int i7 = (i6 >> 8) & 255;
                    if (i7 >= 161 && i7 <= 223) {
                        int i8 = i5;
                        int i9 = i5 + 1;
                        bArr[i8] = -1;
                        i5 = i9 + 1;
                        bArr[i9] = -1;
                    } else {
                        int i10 = i5;
                        int i11 = i5 + 1;
                        bArr[i10] = (byte) i7;
                        i5 = i11 + 1;
                        bArr[i11] = (byte) (i6 & 255);
                    }
                }
            } else {
                for (int i12 = i2; i12 <= i3; i12++) {
                    int i13 = i5;
                    int i14 = i5 + 1;
                    bArr[i13] = (byte) ((i12 >> 8) & 255);
                    i5 = i14 + 1;
                    bArr[i14] = (byte) (i12 & 255);
                }
            }
            Charset.forName(str).newDecoder().onMalformedInput(CodingErrorAction.REPLACE).onUnmappableCharacter(CodingErrorAction.REPLACE).replaceWith(Localizable.NOT_LOCALIZABLE).decode(ByteBuffer.wrap(bArr, 0, bArr.length), CharBuffer.wrap(cArr2, 0, cArr2.length), true);
            for (int i15 = 32; i15 <= 126; i15++) {
                cArr[i15] = (char) i15;
            }
            if (s2 == 2) {
                for (int i16 = 161; i16 <= 223; i16++) {
                    cArr[i16] = (char) ((i16 - 161) + 65377);
                }
            }
            System.arraycopy(cArr2, 0, cArr, i2, cArr2.length);
            char[] cArr3 = new char[65536];
            for (int i17 = 0; i17 < 65536; i17++) {
                if (cArr[i17] != 65533) {
                    cArr3[cArr[i17]] = (char) i17;
                }
            }
            return cArr3;
        } catch (Exception e2) {
            e2.printStackTrace();
            return null;
        }
    }

    static char[] getConverterMap(short s2) {
        if (converterMaps[s2] == null) {
            converterMaps[s2] = getConverter(s2);
        }
        return converterMaps[s2];
    }

    static CMap createCMap(ByteBuffer byteBuffer, int i2, char[] cArr) {
        long j2;
        char c2 = byteBuffer.getChar(i2);
        if (c2 < '\b') {
            j2 = byteBuffer.getChar(i2 + 2);
        } else {
            j2 = byteBuffer.getInt(i2 + 4) & Integer.MAX_VALUE;
        }
        if (i2 + j2 > byteBuffer.capacity() && FontUtilities.isLogging()) {
            FontUtilities.getLogger().warning("Cmap subtable overflows buffer.");
        }
        switch (c2) {
            case 0:
                return new CMapFormat0(byteBuffer, i2);
            case 1:
            case 3:
            case 5:
            case 7:
            case '\t':
            case 11:
            default:
                throw new RuntimeException("Cmap format unimplemented: " + ((int) byteBuffer.getChar(i2)));
            case 2:
                return new CMapFormat2(byteBuffer, i2, cArr);
            case 4:
                return new CMapFormat4(byteBuffer, i2, cArr);
            case 6:
                return new CMapFormat6(byteBuffer, i2, cArr);
            case '\b':
                return new CMapFormat8(byteBuffer, i2, cArr);
            case '\n':
                return new CMapFormat10(byteBuffer, i2, cArr);
            case '\f':
                return new CMapFormat12(byteBuffer, i2, cArr);
        }
    }

    /* loaded from: rt.jar:sun/font/CMap$CMapFormat4.class */
    static class CMapFormat4 extends CMap {
        int segCount;
        int entrySelector;
        int rangeShift;
        char[] endCount;
        char[] startCount;
        short[] idDelta;
        char[] idRangeOffset;
        char[] glyphIds;

        CMapFormat4(ByteBuffer byteBuffer, int i2, char[] cArr) {
            this.xlat = cArr;
            byteBuffer.position(i2);
            CharBuffer charBufferAsCharBuffer = byteBuffer.asCharBuffer();
            charBufferAsCharBuffer.get();
            int iCapacity = charBufferAsCharBuffer.get();
            iCapacity = i2 + iCapacity > byteBuffer.capacity() ? byteBuffer.capacity() - i2 : iCapacity;
            charBufferAsCharBuffer.get();
            this.segCount = charBufferAsCharBuffer.get() / 2;
            charBufferAsCharBuffer.get();
            this.entrySelector = charBufferAsCharBuffer.get();
            this.rangeShift = charBufferAsCharBuffer.get() / 2;
            this.startCount = new char[this.segCount];
            this.endCount = new char[this.segCount];
            this.idDelta = new short[this.segCount];
            this.idRangeOffset = new char[this.segCount];
            for (int i3 = 0; i3 < this.segCount; i3++) {
                this.endCount[i3] = charBufferAsCharBuffer.get();
            }
            charBufferAsCharBuffer.get();
            for (int i4 = 0; i4 < this.segCount; i4++) {
                this.startCount[i4] = charBufferAsCharBuffer.get();
            }
            for (int i5 = 0; i5 < this.segCount; i5++) {
                this.idDelta[i5] = (short) charBufferAsCharBuffer.get();
            }
            for (int i6 = 0; i6 < this.segCount; i6++) {
                this.idRangeOffset[i6] = (char) ((charBufferAsCharBuffer.get() >> 1) & 65535);
            }
            int i7 = ((this.segCount * 8) + 16) / 2;
            charBufferAsCharBuffer.position(i7);
            int i8 = (iCapacity / 2) - i7;
            this.glyphIds = new char[i8];
            for (int i9 = 0; i9 < i8; i9++) {
                this.glyphIds[i9] = charBufferAsCharBuffer.get();
            }
        }

        @Override // sun.font.CMap
        char getGlyph(int i2) {
            int i3;
            char c2 = 0;
            int controlCodeGlyph = getControlCodeGlyph(i2, true);
            if (controlCodeGlyph >= 0) {
                return (char) controlCodeGlyph;
            }
            if (this.xlat != null) {
                i2 = this.xlat[i2];
            }
            int i4 = 0;
            int length = this.startCount.length;
            int length2 = this.startCount.length;
            while (true) {
                i3 = length2 >> 1;
                if (i4 >= length) {
                    break;
                }
                if (this.endCount[i3] < i2) {
                    i4 = i3 + 1;
                } else {
                    length = i3;
                }
                length2 = i4 + length;
            }
            if (i2 >= this.startCount[i3] && i2 <= this.endCount[i3]) {
                char c3 = this.idRangeOffset[i3];
                if (c3 == 0) {
                    c2 = (char) (i2 + this.idDelta[i3]);
                } else {
                    c2 = this.glyphIds[(c3 - this.segCount) + i3 + (i2 - this.startCount[i3])];
                    if (c2 != 0) {
                        c2 = (char) (c2 + this.idDelta[i3]);
                    }
                }
            }
            if (c2 != 0) {
            }
            return c2;
        }
    }

    /* loaded from: rt.jar:sun/font/CMap$CMapFormat0.class */
    static class CMapFormat0 extends CMap {
        byte[] cmap;

        CMapFormat0(ByteBuffer byteBuffer, int i2) {
            this.cmap = new byte[byteBuffer.getChar(i2 + 2) - 6];
            byteBuffer.position(i2 + 6);
            byteBuffer.get(this.cmap);
        }

        @Override // sun.font.CMap
        char getGlyph(int i2) {
            if (i2 < 256) {
                if (i2 < 16) {
                    switch (i2) {
                        case 9:
                        case 10:
                        case 13:
                            return (char) 65535;
                    }
                }
                return (char) (255 & this.cmap[i2]);
            }
            return (char) 0;
        }
    }

    /* loaded from: rt.jar:sun/font/CMap$CMapFormat2.class */
    static class CMapFormat2 extends CMap {
        char[] subHeaderKey = new char[256];
        char[] firstCodeArray;
        char[] entryCountArray;
        short[] idDeltaArray;
        char[] idRangeOffSetArray;
        char[] glyphIndexArray;

        CMapFormat2(ByteBuffer byteBuffer, int i2, char[] cArr) {
            this.xlat = cArr;
            char c2 = byteBuffer.getChar(i2 + 2);
            byteBuffer.position(i2 + 6);
            CharBuffer charBufferAsCharBuffer = byteBuffer.asCharBuffer();
            char c3 = 0;
            for (int i3 = 0; i3 < 256; i3++) {
                this.subHeaderKey[i3] = charBufferAsCharBuffer.get();
                if (this.subHeaderKey[i3] > c3) {
                    c3 = this.subHeaderKey[i3];
                }
            }
            int i4 = (c3 >> 3) + 1;
            this.firstCodeArray = new char[i4];
            this.entryCountArray = new char[i4];
            this.idDeltaArray = new short[i4];
            this.idRangeOffSetArray = new char[i4];
            for (int i5 = 0; i5 < i4; i5++) {
                this.firstCodeArray[i5] = charBufferAsCharBuffer.get();
                this.entryCountArray[i5] = charBufferAsCharBuffer.get();
                this.idDeltaArray[i5] = (short) charBufferAsCharBuffer.get();
                this.idRangeOffSetArray[i5] = charBufferAsCharBuffer.get();
            }
            int i6 = ((c2 - 518) - (i4 * 8)) / 2;
            this.glyphIndexArray = new char[i6];
            for (int i7 = 0; i7 < i6; i7++) {
                this.glyphIndexArray[i7] = charBufferAsCharBuffer.get();
            }
        }

        @Override // sun.font.CMap
        char getGlyph(int i2) {
            char c2;
            char c3;
            int controlCodeGlyph = getControlCodeGlyph(i2, true);
            if (controlCodeGlyph >= 0) {
                return (char) controlCodeGlyph;
            }
            if (this.xlat != null) {
                i2 = this.xlat[i2];
            }
            char c4 = (char) (i2 >> 8);
            char c5 = (char) (i2 & 255);
            int i3 = this.subHeaderKey[c4] >> 3;
            if (i3 != 0) {
                c2 = c5;
            } else {
                c2 = c4;
                if (c2 == 0) {
                    c2 = c5;
                }
            }
            char c6 = this.firstCodeArray[i3];
            if (c2 >= c6 && (c3 = (char) (c2 - c6)) < this.entryCountArray[i3]) {
                char c7 = this.glyphIndexArray[((this.idRangeOffSetArray[i3] - (((this.idRangeOffSetArray.length - i3) * 8) - 6)) / 2) + c3];
                if (c7 != 0) {
                    return (char) (c7 + this.idDeltaArray[i3]);
                }
                return (char) 0;
            }
            return (char) 0;
        }
    }

    /* loaded from: rt.jar:sun/font/CMap$CMapFormat6.class */
    static class CMapFormat6 extends CMap {
        char firstCode;
        char entryCount;
        char[] glyphIdArray;

        CMapFormat6(ByteBuffer byteBuffer, int i2, char[] cArr) {
            byteBuffer.position(i2 + 6);
            CharBuffer charBufferAsCharBuffer = byteBuffer.asCharBuffer();
            this.firstCode = charBufferAsCharBuffer.get();
            this.entryCount = charBufferAsCharBuffer.get();
            this.glyphIdArray = new char[this.entryCount];
            for (int i3 = 0; i3 < this.entryCount; i3++) {
                this.glyphIdArray[i3] = charBufferAsCharBuffer.get();
            }
        }

        @Override // sun.font.CMap
        char getGlyph(int i2) {
            int controlCodeGlyph = getControlCodeGlyph(i2, true);
            if (controlCodeGlyph >= 0) {
                return (char) controlCodeGlyph;
            }
            if (this.xlat != null) {
                i2 = this.xlat[i2];
            }
            int i3 = i2 - this.firstCode;
            if (i3 < 0 || i3 >= this.entryCount) {
                return (char) 0;
            }
            return this.glyphIdArray[i3];
        }
    }

    /* loaded from: rt.jar:sun/font/CMap$CMapFormat8.class */
    static class CMapFormat8 extends CMap {
        byte[] is32 = new byte[8192];
        int nGroups;
        int[] startCharCode;
        int[] endCharCode;
        int[] startGlyphID;

        CMapFormat8(ByteBuffer byteBuffer, int i2, char[] cArr) {
            byteBuffer.position(12);
            byteBuffer.get(this.is32);
            this.nGroups = byteBuffer.getInt() & Integer.MAX_VALUE;
            if (byteBuffer.remaining() < 12 * this.nGroups) {
                throw new RuntimeException("Format 8 table exceeded");
            }
            this.startCharCode = new int[this.nGroups];
            this.endCharCode = new int[this.nGroups];
            this.startGlyphID = new int[this.nGroups];
        }

        @Override // sun.font.CMap
        char getGlyph(int i2) {
            if (this.xlat != null) {
                throw new RuntimeException("xlat array for cmap fmt=8");
            }
            return (char) 0;
        }
    }

    /* loaded from: rt.jar:sun/font/CMap$CMapFormat10.class */
    static class CMapFormat10 extends CMap {
        long firstCode;
        int entryCount;
        char[] glyphIdArray;

        CMapFormat10(ByteBuffer byteBuffer, int i2, char[] cArr) {
            byteBuffer.position(i2 + 12);
            this.firstCode = byteBuffer.getInt() & Integer.MAX_VALUE;
            this.entryCount = byteBuffer.getInt() & Integer.MAX_VALUE;
            if (byteBuffer.remaining() < 2 * this.entryCount) {
                throw new RuntimeException("Format 10 table exceeded");
            }
            CharBuffer charBufferAsCharBuffer = byteBuffer.asCharBuffer();
            this.glyphIdArray = new char[this.entryCount];
            for (int i3 = 0; i3 < this.entryCount; i3++) {
                this.glyphIdArray[i3] = charBufferAsCharBuffer.get();
            }
        }

        @Override // sun.font.CMap
        char getGlyph(int i2) {
            if (this.xlat != null) {
                throw new RuntimeException("xlat array for cmap fmt=10");
            }
            int i3 = (int) (i2 - this.firstCode);
            if (i3 < 0 || i3 >= this.entryCount) {
                return (char) 0;
            }
            return this.glyphIdArray[i3];
        }
    }

    /* loaded from: rt.jar:sun/font/CMap$CMapFormat12.class */
    static class CMapFormat12 extends CMap {
        int numGroups;
        int highBit = 0;
        int power;
        int extra;
        long[] startCharCode;
        long[] endCharCode;
        int[] startGlyphID;

        CMapFormat12(ByteBuffer byteBuffer, int i2, char[] cArr) {
            if (cArr != null) {
                throw new RuntimeException("xlat array for cmap fmt=12");
            }
            byteBuffer.position(i2 + 12);
            this.numGroups = byteBuffer.getInt() & Integer.MAX_VALUE;
            if (byteBuffer.remaining() < 12 * this.numGroups) {
                throw new RuntimeException("Format 12 table exceeded");
            }
            this.startCharCode = new long[this.numGroups];
            this.endCharCode = new long[this.numGroups];
            this.startGlyphID = new int[this.numGroups];
            IntBuffer intBufferAsIntBuffer = byteBuffer.slice().asIntBuffer();
            for (int i3 = 0; i3 < this.numGroups; i3++) {
                this.startCharCode[i3] = intBufferAsIntBuffer.get() & Integer.MAX_VALUE;
                this.endCharCode[i3] = intBufferAsIntBuffer.get() & Integer.MAX_VALUE;
                this.startGlyphID[i3] = intBufferAsIntBuffer.get() & Integer.MAX_VALUE;
            }
            int i4 = this.numGroups;
            if (i4 >= 65536) {
                i4 >>= 16;
                this.highBit += 16;
            }
            if (i4 >= 256) {
                i4 >>= 8;
                this.highBit += 8;
            }
            if (i4 >= 16) {
                i4 >>= 4;
                this.highBit += 4;
            }
            if (i4 >= 4) {
                i4 >>= 2;
                this.highBit += 2;
            }
            if (i4 >= 2) {
                int i5 = i4 >> 1;
                this.highBit++;
            }
            this.power = 1 << this.highBit;
            this.extra = this.numGroups - this.power;
        }

        @Override // sun.font.CMap
        char getGlyph(int i2) {
            int controlCodeGlyph = getControlCodeGlyph(i2, false);
            if (controlCodeGlyph >= 0) {
                return (char) controlCodeGlyph;
            }
            int i3 = this.power;
            int i4 = 0;
            if (this.startCharCode[this.extra] <= i2) {
                i4 = this.extra;
            }
            while (i3 > 1) {
                i3 >>= 1;
                if (this.startCharCode[i4 + i3] <= i2) {
                    i4 += i3;
                }
            }
            if (this.startCharCode[i4] <= i2 && this.endCharCode[i4] >= i2) {
                return (char) (this.startGlyphID[i4] + (i2 - this.startCharCode[i4]));
            }
            return (char) 0;
        }
    }

    /* loaded from: rt.jar:sun/font/CMap$NullCMapClass.class */
    static class NullCMapClass extends CMap {
        NullCMapClass() {
        }

        @Override // sun.font.CMap
        char getGlyph(int i2) {
            return (char) 0;
        }
    }

    final int getControlCodeGlyph(int i2, boolean z2) {
        if (i2 < 16) {
            switch (i2) {
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
        if (i2 >= 8204) {
            if (i2 <= 8207) {
                return 65535;
            }
            if (i2 >= 8232 && i2 <= 8238) {
                return 65535;
            }
            if (i2 >= 8298 && i2 <= 8303) {
                return 65535;
            }
            if (z2 && i2 >= 65535) {
                return 0;
            }
            return -1;
        }
        return -1;
    }
}
