package org.icepdf.core.pobjects.filters;

/* loaded from: icepdf-core.jar:org/icepdf/core/pobjects/filters/CCITTFaxDecoder.class */
public class CCITTFaxDecoder {
    static int[] table1 = {0, 1, 3, 7, 15, 31, 63, 127, 255};
    static int[] table2 = {0, 128, 192, 224, 240, 248, 252, 254, 255};
    static byte[] flipTable = {0, Byte.MIN_VALUE, 64, -64, 32, -96, 96, -32, 16, -112, 80, -48, 48, -80, 112, -16, 8, -120, 72, -56, 40, -88, 104, -24, 24, -104, 88, -40, 56, -72, 120, -8, 4, -124, 68, -60, 36, -92, 100, -28, 20, -108, 84, -44, 52, -76, 116, -12, 12, -116, 76, -52, 44, -84, 108, -20, 28, -100, 92, -36, 60, -68, 124, -4, 2, -126, 66, -62, 34, -94, 98, -30, 18, -110, 82, -46, 50, -78, 114, -14, 10, -118, 74, -54, 42, -86, 106, -22, 26, -102, 90, -38, 58, -70, 122, -6, 6, -122, 70, -58, 38, -90, 102, -26, 22, -106, 86, -42, 54, -74, 118, -10, 14, -114, 78, -50, 46, -82, 110, -18, 30, -98, 94, -34, 62, -66, 126, -2, 1, -127, 65, -63, 33, -95, 97, -31, 17, -111, 81, -47, 49, -79, 113, -15, 9, -119, 73, -55, 41, -87, 105, -23, 25, -103, 89, -39, 57, -71, 121, -7, 5, -123, 69, -59, 37, -91, 101, -27, 21, -107, 85, -43, 53, -75, 117, -11, 13, -115, 77, -51, 45, -83, 109, -19, 29, -99, 93, -35, 61, -67, 125, -3, 3, -125, 67, -61, 35, -93, 99, -29, 19, -109, 83, -45, 51, -77, 115, -13, 11, -117, 75, -53, 43, -85, 107, -21, 27, -101, 91, -37, 59, -69, 123, -5, 7, -121, 71, -57, 39, -89, 103, -25, 23, -105, 87, -41, 55, -73, 119, -9, 15, -113, 79, -49, 47, -81, 111, -17, 31, -97, 95, -33, 63, -65, Byte.MAX_VALUE, -1};
    static short[] white = {6430, 6400, 6400, 6400, 3225, 3225, 3225, 3225, 944, 944, 944, 944, 976, 976, 976, 976, 1456, 1456, 1456, 1456, 1488, 1488, 1488, 1488, 718, 718, 718, 718, 718, 718, 718, 718, 750, 750, 750, 750, 750, 750, 750, 750, 1520, 1520, 1520, 1520, 1552, 1552, 1552, 1552, 428, 428, 428, 428, 428, 428, 428, 428, 428, 428, 428, 428, 428, 428, 428, 428, 654, 654, 654, 654, 654, 654, 654, 654, 1072, 1072, 1072, 1072, 1104, 1104, 1104, 1104, 1136, 1136, 1136, 1136, 1168, 1168, 1168, 1168, 1200, 1200, 1200, 1200, 1232, 1232, 1232, 1232, 622, 622, 622, 622, 622, 622, 622, 622, 1008, 1008, 1008, 1008, 1040, 1040, 1040, 1040, 44, 44, 44, 44, 44, 44, 44, 44, 44, 44, 44, 44, 44, 44, 44, 44, 396, 396, 396, 396, 396, 396, 396, 396, 396, 396, 396, 396, 396, 396, 396, 396, 1712, 1712, 1712, 1712, 1744, 1744, 1744, 1744, 846, 846, 846, 846, 846, 846, 846, 846, 1264, 1264, 1264, 1264, 1296, 1296, 1296, 1296, 1328, 1328, 1328, 1328, 1360, 1360, 1360, 1360, 1392, 1392, 1392, 1392, 1424, 1424, 1424, 1424, 686, 686, 686, 686, 686, 686, 686, 686, 910, 910, 910, 910, 910, 910, 910, 910, 1968, 1968, 1968, 1968, 2000, 2000, 2000, 2000, 2032, 2032, 2032, 2032, 16, 16, 16, 16, 10257, 10257, 10257, 10257, 12305, 12305, 12305, 12305, 330, 330, 330, 330, 330, 330, 330, 330, 330, 330, 330, 330, 330, 330, 330, 330, 330, 330, 330, 330, 330, 330, 330, 330, 330, 330, 330, 330, 330, 330, 330, 330, 362, 362, 362, 362, 362, 362, 362, 362, 362, 362, 362, 362, 362, 362, 362, 362, 362, 362, 362, 362, 362, 362, 362, 362, 362, 362, 362, 362, 362, 362, 362, 362, 878, 878, 878, 878, 878, 878, 878, 878, 1904, 1904, 1904, 1904, 1936, 1936, 1936, 1936, -18413, -18413, -16365, -16365, -14317, -14317, -10221, -10221, 590, 590, 590, 590, 590, 590, 590, 590, 782, 782, 782, 782, 782, 782, 782, 782, 1584, 1584, 1584, 1584, 1616, 1616, 1616, 1616, 1648, 1648, 1648, 1648, 1680, 1680, 1680, 1680, 814, 814, 814, 814, 814, 814, 814, 814, 1776, 1776, 1776, 1776, 1808, 1808, 1808, 1808, 1840, 1840, 1840, 1840, 1872, 1872, 1872, 1872, 6157, 6157, 6157, 6157, 6157, 6157, 6157, 6157, 6157, 6157, 6157, 6157, 6157, 6157, 6157, 6157, -12275, -12275, -12275, -12275, -12275, -12275, -12275, -12275, -12275, -12275, -12275, -12275, -12275, -12275, -12275, -12275, 14353, 14353, 14353, 14353, 16401, 16401, 16401, 16401, 22547, 22547, 24595, 24595, 20497, 20497, 20497, 20497, 18449, 18449, 18449, 18449, 26643, 26643, 28691, 28691, 30739, 30739, -32749, -32749, -30701, -30701, -28653, -28653, -26605, -26605, -24557, -24557, -22509, -22509, -20461, -20461, 8207, 8207, 8207, 8207, 8207, 8207, 8207, 8207, 72, 72, 72, 72, 72, 72, 72, 72, 72, 72, 72, 72, 72, 72, 72, 72, 72, 72, 72, 72, 72, 72, 72, 72, 72, 72, 72, 72, 72, 72, 72, 72, 72, 72, 72, 72, 72, 72, 72, 72, 72, 72, 72, 72, 72, 72, 72, 72, 72, 72, 72, 72, 72, 72, 72, 72, 72, 72, 72, 72, 72, 72, 72, 72, 104, 104, 104, 104, 104, 104, 104, 104, 104, 104, 104, 104, 104, 104, 104, 104, 104, 104, 104, 104, 104, 104, 104, 104, 104, 104, 104, 104, 104, 104, 104, 104, 104, 104, 104, 104, 104, 104, 104, 104, 104, 104, 104, 104, 104, 104, 104, 104, 104, 104, 104, 104, 104, 104, 104, 104, 104, 104, 104, 104, 104, 104, 104, 104, 4107, 4107, 4107, 4107, 4107, 4107, 4107, 4107, 4107, 4107, 4107, 4107, 4107, 4107, 4107, 4107, 4107, 4107, 4107, 4107, 4107, 4107, 4107, 4107, 4107, 4107, 4107, 4107, 4107, 4107, 4107, 4107, 266, 266, 266, 266, 266, 266, 266, 266, 266, 266, 266, 266, 266, 266, 266, 266, 266, 266, 266, 266, 266, 266, 266, 266, 266, 266, 266, 266, 266, 266, 266, 266, 298, 298, 298, 298, 298, 298, 298, 298, 298, 298, 298, 298, 298, 298, 298, 298, 298, 298, 298, 298, 298, 298, 298, 298, 298, 298, 298, 298, 298, 298, 298, 298, 524, 524, 524, 524, 524, 524, 524, 524, 524, 524, 524, 524, 524, 524, 524, 524, 556, 556, 556, 556, 556, 556, 556, 556, 556, 556, 556, 556, 556, 556, 556, 556, 136, 136, 136, 136, 136, 136, 136, 136, 136, 136, 136, 136, 136, 136, 136, 136, 136, 136, 136, 136, 136, 136, 136, 136, 136, 136, 136, 136, 136, 136, 136, 136, 136, 136, 136, 136, 136, 136, 136, 136, 136, 136, 136, 136, 136, 136, 136, 136, 136, 136, 136, 136, 136, 136, 136, 136, 136, 136, 136, 136, 136, 136, 136, 136, 168, 168, 168, 168, 168, 168, 168, 168, 168, 168, 168, 168, 168, 168, 168, 168, 168, 168, 168, 168, 168, 168, 168, 168, 168, 168, 168, 168, 168, 168, 168, 168, 168, 168, 168, 168, 168, 168, 168, 168, 168, 168, 168, 168, 168, 168, 168, 168, 168, 168, 168, 168, 168, 168, 168, 168, 168, 168, 168, 168, 168, 168, 168, 168, 460, 460, 460, 460, 460, 460, 460, 460, 460, 460, 460, 460, 460, 460, 460, 460, 492, 492, 492, 492, 492, 492, 492, 492, 492, 492, 492, 492, 492, 492, 492, 492, 2059, 2059, 2059, 2059, 2059, 2059, 2059, 2059, 2059, 2059, 2059, 2059, 2059, 2059, 2059, 2059, 2059, 2059, 2059, 2059, 2059, 2059, 2059, 2059, 2059, 2059, 2059, 2059, 2059, 2059, 2059, 2059, 200, 200, 200, 200, 200, 200, 200, 200, 200, 200, 200, 200, 200, 200, 200, 200, 200, 200, 200, 200, 200, 200, 200, 200, 200, 200, 200, 200, 200, 200, 200, 200, 200, 200, 200, 200, 200, 200, 200, 200, 200, 200, 200, 200, 200, 200, 200, 200, 200, 200, 200, 200, 200, 200, 200, 200, 200, 200, 200, 200, 200, 200, 200, 200, 232, 232, 232, 232, 232, 232, 232, 232, 232, 232, 232, 232, 232, 232, 232, 232, 232, 232, 232, 232, 232, 232, 232, 232, 232, 232, 232, 232, 232, 232, 232, 232, 232, 232, 232, 232, 232, 232, 232, 232, 
    232, 232, 232, 232, 232, 232, 232, 232, 232, 232, 232, 232, 232, 232, 232, 232, 232, 232, 232, 232, 232, 232, 232, 232};
    static short[] additionalMakeup = {28679, 28679, 31752, -32759, -31735, -30711, -29687, -28663, 29703, 29703, 30727, 30727, -27639, -26615, -25591, -24567};
    static short[] initBlack = {3226, 6412, 200, 168, 38, 38, 134, 134, 100, 100, 100, 100, 68, 68, 68, 68};
    static short[] twoBitBlack = {292, 260, 226, 226};
    static short[] black = {62, 62, 30, 30, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 3225, 3225, 3225, 3225, 3225, 3225, 3225, 3225, 3225, 3225, 3225, 3225, 3225, 3225, 3225, 3225, 3225, 3225, 3225, 3225, 3225, 3225, 3225, 3225, 3225, 3225, 3225, 3225, 3225, 3225, 3225, 3225, 588, 588, 588, 588, 588, 588, 588, 588, 1680, 1680, 20499, 22547, 24595, 26643, 1776, 1776, 1808, 1808, -24557, -22509, -20461, -18413, 1904, 1904, 1936, 1936, -16365, -14317, 782, 782, 782, 782, 814, 814, 814, 814, -12269, -10221, 10257, 10257, 12305, 12305, 14353, 14353, 16403, 18451, 1712, 1712, 1744, 1744, 28691, 30739, -32749, -30701, -28653, -26605, 2061, 2061, 2061, 2061, 2061, 2061, 2061, 2061, 424, 424, 424, 424, 424, 424, 424, 424, 424, 424, 424, 424, 424, 424, 424, 424, 424, 424, 424, 424, 424, 424, 424, 424, 424, 424, 424, 424, 424, 424, 424, 424, 750, 750, 750, 750, 1616, 1616, 1648, 1648, 1424, 1424, 1456, 1456, 1488, 1488, 1520, 1520, 1840, 1840, 1872, 1872, 1968, 1968, 8209, 8209, 524, 524, 524, 524, 524, 524, 524, 524, 556, 556, 556, 556, 556, 556, 556, 556, 1552, 1552, 1584, 1584, 2000, 2000, 2032, 2032, 976, 976, 1008, 1008, 1040, 1040, 1072, 1072, 1296, 1296, 1328, 1328, 718, 718, 718, 718, 456, 456, 456, 456, 456, 456, 456, 456, 456, 456, 456, 456, 456, 456, 456, 456, 456, 456, 456, 456, 456, 456, 456, 456, 456, 456, 456, 456, 456, 456, 456, 456, 326, 326, 326, 326, 326, 326, 326, 326, 326, 326, 326, 326, 326, 326, 326, 326, 326, 326, 326, 326, 326, 326, 326, 326, 326, 326, 326, 326, 326, 326, 326, 326, 326, 326, 326, 326, 326, 326, 326, 326, 326, 326, 326, 326, 326, 326, 326, 326, 326, 326, 326, 326, 326, 326, 326, 326, 326, 326, 326, 326, 326, 326, 326, 326, 358, 358, 358, 358, 358, 358, 358, 358, 358, 358, 358, 358, 358, 358, 358, 358, 358, 358, 358, 358, 358, 358, 358, 358, 358, 358, 358, 358, 358, 358, 358, 358, 358, 358, 358, 358, 358, 358, 358, 358, 358, 358, 358, 358, 358, 358, 358, 358, 358, 358, 358, 358, 358, 358, 358, 358, 358, 358, 358, 358, 358, 358, 358, 358, 490, 490, 490, 490, 490, 490, 490, 490, 490, 490, 490, 490, 490, 490, 490, 490, 4113, 4113, 6161, 6161, 848, 848, 880, 880, 912, 912, 944, 944, 622, 622, 622, 622, 654, 654, 654, 654, 1104, 1104, 1136, 1136, 1168, 1168, 1200, 1200, 1232, 1232, 1264, 1264, 686, 686, 686, 686, 1360, 1360, 1392, 1392, 12, 12, 12, 12, 12, 12, 12, 12, 390, 390, 390, 390, 390, 390, 390, 390, 390, 390, 390, 390, 390, 390, 390, 390, 390, 390, 390, 390, 390, 390, 390, 390, 390, 390, 390, 390, 390, 390, 390, 390, 390, 390, 390, 390, 390, 390, 390, 390, 390, 390, 390, 390, 390, 390, 390, 390, 390, 390, 390, 390, 390, 390, 390, 390, 390, 390, 390, 390, 390, 390, 390, 390};
    static byte[] twoDCodes = {80, 88, 23, 71, 30, 30, 62, 62, 4, 4, 4, 4, 4, 4, 4, 4, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 35, 35, 35, 35, 35, 35, 35, 35, 35, 35, 35, 35, 35, 35, 35, 35, 51, 51, 51, 51, 51, 51, 51, 51, 51, 51, 51, 51, 51, 51, 51, 51, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41};
    private byte[] data;

    /* renamed from: w, reason: collision with root package name */
    private int f13122w;
    private int fillOrder;
    private int[] prevChangingElems;
    private int[] currChangingElems;
    private boolean align = false;
    private int changingElemSize = 0;
    private int lastChangingElement = 0;
    private boolean fillBits = false;
    private int bitPointer = 0;
    private int bytePointer = 0;

    public CCITTFaxDecoder(int fillOrder, int w2, int h2) {
        this.fillOrder = fillOrder;
        this.f13122w = w2;
        this.prevChangingElems = new int[w2 + 1];
        this.currChangingElems = new int[w2 + 1];
    }

    private boolean align() {
        if (this.align && this.bitPointer != 0) {
            this.bytePointer++;
            this.bitPointer = 0;
            return true;
        }
        return false;
    }

    protected boolean consumeEOL() {
        int next12Bits = nextNBits(12);
        if (next12Bits == 1) {
            return true;
        }
        updatePointer(12);
        return false;
    }

    private int decodeBlackCodeWord() {
        int runLength = 0;
        boolean isWhite = false;
        while (!isWhite) {
            int current = nextLesserThan8Bits(4);
            short s2 = initBlack[current];
            int i2 = s2 & 1;
            int bits = (s2 >>> 1) & 15;
            int code = (s2 >>> 5) & 2047;
            if (code == 100) {
                int current2 = nextNBits(9);
                short s3 = black[current2];
                int isT = s3 & 1;
                int bits2 = (s3 >>> 1) & 15;
                int code2 = (s3 >>> 5) & 2047;
                if (bits2 == 12) {
                    updatePointer(5);
                    int current3 = nextLesserThan8Bits(4);
                    short s4 = additionalMakeup[current3];
                    int bits3 = (s4 >>> 1) & 7;
                    runLength += (s4 >>> 4) & 4095;
                    updatePointer(4 - bits3);
                } else {
                    if (bits2 == 15) {
                        throw new RuntimeException("EOL code word encountered in Black run.");
                    }
                    runLength += code2;
                    updatePointer(9 - bits2);
                    if (isT == 0) {
                        isWhite = true;
                    }
                }
            } else if (code == 200) {
                int current4 = nextLesserThan8Bits(2);
                short s5 = twoBitBlack[current4];
                runLength += (s5 >>> 5) & 2047;
                int bits4 = (s5 >>> 1) & 15;
                updatePointer(2 - bits4);
                isWhite = true;
            } else {
                runLength += code;
                updatePointer(4 - bits);
                isWhite = true;
            }
        }
        return runLength;
    }

    protected void decodeNextScanline(byte[] buffer, int lineOffset, int bitOffset) {
        boolean isWhite = true;
        this.changingElemSize = 0;
        while (true) {
            if (bitOffset >= this.f13122w) {
                break;
            }
            while (isWhite) {
                int current = nextNBits(10);
                short s2 = white[current];
                int isT = s2 & 1;
                int bits = (s2 >>> 1) & 15;
                if (bits == 12) {
                    int twoBits = nextLesserThan8Bits(2);
                    short s3 = additionalMakeup[((current << 2) & 12) | twoBits];
                    int bits2 = (s3 >>> 1) & 7;
                    bitOffset += (s3 >>> 4) & 4095;
                    updatePointer(4 - bits2);
                } else {
                    if (bits == 0) {
                        throw new RuntimeException("Invalid code encountered.");
                    }
                    if (bits == 15) {
                        updatePointer(10);
                        return;
                    }
                    bitOffset += (s2 >>> 5) & 2047;
                    updatePointer(10 - bits);
                    if (isT == 0) {
                        isWhite = false;
                        int[] iArr = this.currChangingElems;
                        int i2 = this.changingElemSize;
                        this.changingElemSize = i2 + 1;
                        iArr[i2] = bitOffset;
                    }
                }
            }
            if (bitOffset == this.f13122w) {
                align();
                break;
            }
            while (!isWhite) {
                int current2 = nextLesserThan8Bits(4);
                short s4 = initBlack[current2];
                int i3 = s4 & 1;
                int bits3 = (s4 >>> 1) & 15;
                int code = (s4 >>> 5) & 2047;
                if (code == 100) {
                    int current3 = nextNBits(9);
                    short s5 = black[current3];
                    int isT2 = s5 & 1;
                    int bits4 = (s5 >>> 1) & 15;
                    int code2 = (s5 >>> 5) & 2047;
                    if (bits4 == 12) {
                        updatePointer(5);
                        int current4 = nextLesserThan8Bits(4);
                        short s6 = additionalMakeup[current4];
                        int bits5 = (s6 >>> 1) & 7;
                        int code3 = (s6 >>> 4) & 4095;
                        setToBlack(buffer, lineOffset, bitOffset, code3);
                        bitOffset += code3;
                        updatePointer(4 - bits5);
                    } else {
                        if (bits4 == 15) {
                            updatePointer(9);
                            return;
                        }
                        setToBlack(buffer, lineOffset, bitOffset, code2);
                        bitOffset += code2;
                        updatePointer(9 - bits4);
                        if (isT2 == 0) {
                            isWhite = true;
                            int[] iArr2 = this.currChangingElems;
                            int i4 = this.changingElemSize;
                            this.changingElemSize = i4 + 1;
                            iArr2[i4] = bitOffset;
                        }
                    }
                } else if (code == 200) {
                    int current5 = nextLesserThan8Bits(2);
                    short s7 = twoBitBlack[current5];
                    int code4 = (s7 >>> 5) & 2047;
                    int bits6 = (s7 >>> 1) & 15;
                    setToBlack(buffer, lineOffset, bitOffset, code4);
                    bitOffset += code4;
                    updatePointer(2 - bits6);
                    isWhite = true;
                    int[] iArr3 = this.currChangingElems;
                    int i5 = this.changingElemSize;
                    this.changingElemSize = i5 + 1;
                    iArr3[i5] = bitOffset;
                } else {
                    setToBlack(buffer, lineOffset, bitOffset, code);
                    bitOffset += code;
                    updatePointer(4 - bits3);
                    isWhite = true;
                    int[] iArr4 = this.currChangingElems;
                    int i6 = this.changingElemSize;
                    this.changingElemSize = i6 + 1;
                    iArr4[i6] = bitOffset;
                }
            }
            if (bitOffset == this.f13122w) {
                align();
                break;
            }
        }
        int[] iArr5 = this.currChangingElems;
        int i7 = this.changingElemSize;
        this.changingElemSize = i7 + 1;
        iArr5[i7] = bitOffset;
    }

    public void decodeT41D(byte[] buffer, byte[] compData, int startX, int height) {
        this.data = compData;
        int scanlineStride = (this.f13122w + 7) / 8;
        this.bitPointer = 0;
        this.bytePointer = 0;
        int lineOffset = 0;
        for (int i2 = 0; i2 < height; i2++) {
            consumeEOL();
            decodeNextScanline(buffer, lineOffset, startX);
            lineOffset += scanlineStride;
        }
    }

    public void decodeT42D(byte[] buffer, byte[] compData, int startX, int height) {
        this.data = compData;
        int scanlineStride = (this.f13122w + 7) / 8;
        this.bitPointer = 0;
        this.bytePointer = 0;
        int[] b2 = new int[2];
        if (readEOL(true) != 1) {
            throw new RuntimeException("First scanline must be 1D encoded.");
        }
        decodeNextScanline(buffer, 0, startX);
        int lineOffset = 0 + scanlineStride;
        for (int lines = 1; lines < height; lines++) {
            if (readEOL(false) == 0) {
                int[] temp = this.prevChangingElems;
                this.prevChangingElems = this.currChangingElems;
                this.currChangingElems = temp;
                int currIndex = 0;
                int a0 = -1;
                boolean isWhite = true;
                int bitOffset = startX;
                this.lastChangingElement = 0;
                while (bitOffset < this.f13122w) {
                    getNextChangingElement(a0, isWhite, b2);
                    int b1 = b2[0];
                    int b22 = b2[1];
                    int entry = twoDCodes[nextLesserThan8Bits(7)] & 255;
                    int code = (entry & 120) >>> 3;
                    int bits = entry & 7;
                    if (code == 0) {
                        if (!isWhite) {
                            setToBlack(buffer, lineOffset, bitOffset, b22 - bitOffset);
                        }
                        a0 = b22;
                        bitOffset = b22;
                        updatePointer(7 - bits);
                    } else if (code == 1) {
                        updatePointer(7 - bits);
                        if (isWhite) {
                            int bitOffset2 = bitOffset + decodeWhiteCodeWord();
                            int i2 = currIndex;
                            int currIndex2 = currIndex + 1;
                            this.currChangingElems[i2] = bitOffset2;
                            int number = decodeBlackCodeWord();
                            setToBlack(buffer, lineOffset, bitOffset2, number);
                            bitOffset = bitOffset2 + number;
                            currIndex = currIndex2 + 1;
                            this.currChangingElems[currIndex2] = bitOffset;
                        } else {
                            int number2 = decodeBlackCodeWord();
                            setToBlack(buffer, lineOffset, bitOffset, number2);
                            int bitOffset3 = bitOffset + number2;
                            int i3 = currIndex;
                            int currIndex3 = currIndex + 1;
                            this.currChangingElems[i3] = bitOffset3;
                            bitOffset = bitOffset3 + decodeWhiteCodeWord();
                            currIndex = currIndex3 + 1;
                            this.currChangingElems[currIndex3] = bitOffset;
                        }
                        a0 = bitOffset;
                    } else if (code <= 8) {
                        int a1 = b1 + (code - 5);
                        int i4 = currIndex;
                        currIndex++;
                        this.currChangingElems[i4] = a1;
                        if (!isWhite) {
                            setToBlack(buffer, lineOffset, bitOffset, a1 - bitOffset);
                        }
                        a0 = a1;
                        bitOffset = a1;
                        isWhite = !isWhite;
                        updatePointer(7 - bits);
                    } else {
                        throw new RuntimeException("Invalid code encountered while decoding 2D group 3 compressed data.");
                    }
                }
                this.currChangingElems[currIndex] = bitOffset;
                this.changingElemSize = currIndex + 1;
            } else {
                decodeNextScanline(buffer, lineOffset, startX);
            }
            lineOffset += scanlineStride;
        }
    }

    public void decodeT6(byte[] buffer, byte[] compData, int startX, int height) {
        this.data = compData;
        int scanlineStride = (this.f13122w + 7) / 8;
        this.bitPointer = 0;
        this.bytePointer = 0;
        int[] b2 = new int[2];
        int[] cce = this.currChangingElems;
        this.changingElemSize = 0;
        int i2 = this.changingElemSize;
        this.changingElemSize = i2 + 1;
        cce[i2] = this.f13122w;
        int i3 = this.changingElemSize;
        this.changingElemSize = i3 + 1;
        cce[i3] = this.f13122w;
        int lineOffset = 0;
        for (int lines = 0; lines < height; lines++) {
            int a0 = -1;
            boolean isWhite = true;
            int[] temp = this.prevChangingElems;
            this.prevChangingElems = this.currChangingElems;
            this.currChangingElems = temp;
            int currIndex = 0;
            int bitOffset = startX;
            this.lastChangingElement = 0;
            while (bitOffset < this.f13122w) {
                getNextChangingElement(a0, isWhite, b2);
                int b1 = b2[0];
                int b22 = b2[1];
                int entry = twoDCodes[nextLesserThan8Bits(7)] & 255;
                int code = (entry & 120) >>> 3;
                int bits = entry & 7;
                if (code == 0) {
                    if (!isWhite) {
                        if (b22 > this.f13122w) {
                            b22 = this.f13122w;
                        }
                        setToBlack(buffer, lineOffset, bitOffset, b22 - bitOffset);
                    }
                    int i4 = b22;
                    a0 = i4;
                    bitOffset = i4;
                    updatePointer(7 - bits);
                } else if (code == 1) {
                    updatePointer(7 - bits);
                    if (isWhite) {
                        int bitOffset2 = bitOffset + decodeWhiteCodeWord();
                        int i5 = currIndex;
                        int currIndex2 = currIndex + 1;
                        temp[i5] = bitOffset2;
                        int number = decodeBlackCodeWord();
                        if (number > this.f13122w - bitOffset2) {
                            number = this.f13122w - bitOffset2;
                        }
                        setToBlack(buffer, lineOffset, bitOffset2, number);
                        bitOffset = bitOffset2 + number;
                        currIndex = currIndex2 + 1;
                        temp[currIndex2] = bitOffset;
                    } else {
                        int number2 = decodeBlackCodeWord();
                        if (number2 > this.f13122w - bitOffset) {
                            number2 = this.f13122w - bitOffset;
                        }
                        setToBlack(buffer, lineOffset, bitOffset, number2);
                        int bitOffset3 = bitOffset + number2;
                        int i6 = currIndex;
                        int currIndex3 = currIndex + 1;
                        temp[i6] = bitOffset3;
                        bitOffset = bitOffset3 + decodeWhiteCodeWord();
                        currIndex = currIndex3 + 1;
                        temp[currIndex3] = bitOffset;
                    }
                    a0 = bitOffset;
                } else if (code <= 8) {
                    int a1 = b1 + (code - 5);
                    int i7 = currIndex;
                    currIndex++;
                    temp[i7] = a1;
                    if (!isWhite) {
                        if (a1 > this.f13122w) {
                            a1 = this.f13122w;
                        }
                        setToBlack(buffer, lineOffset, bitOffset, a1 - bitOffset);
                    }
                    int i8 = a1;
                    a0 = i8;
                    bitOffset = i8;
                    isWhite = !isWhite;
                    updatePointer(7 - bits);
                } else if (code == 11) {
                    if (nextLesserThan8Bits(3) != 7) {
                        throw new RuntimeException("Invalid code encountered while decoding 2D group 4 compressed data.");
                    }
                    int zeros = 0;
                    boolean exit = false;
                    while (!exit) {
                        while (nextLesserThan8Bits(1) != 1) {
                            zeros++;
                        }
                        if (zeros > 5) {
                            zeros -= 6;
                            if (!isWhite && zeros > 0) {
                                int i9 = currIndex;
                                currIndex++;
                                temp[i9] = bitOffset;
                            }
                            bitOffset += zeros;
                            if (zeros > 0) {
                                isWhite = true;
                            }
                            if (nextLesserThan8Bits(1) == 0) {
                                if (!isWhite) {
                                    int i10 = currIndex;
                                    currIndex++;
                                    temp[i10] = bitOffset;
                                }
                                isWhite = true;
                            } else {
                                if (isWhite) {
                                    int i11 = currIndex;
                                    currIndex++;
                                    temp[i11] = bitOffset;
                                }
                                isWhite = false;
                            }
                            exit = true;
                        }
                        if (zeros == 5) {
                            if (!isWhite) {
                                int i12 = currIndex;
                                currIndex++;
                                temp[i12] = bitOffset;
                            }
                            bitOffset += zeros;
                            isWhite = true;
                        } else {
                            int bitOffset4 = bitOffset + zeros;
                            int i13 = currIndex;
                            currIndex++;
                            temp[i13] = bitOffset4;
                            setToBlack(buffer, lineOffset, bitOffset4, 1);
                            bitOffset = bitOffset4 + 1;
                            isWhite = false;
                        }
                    }
                } else {
                    updatePointer(7 - bits);
                    bitOffset = this.f13122w;
                }
            }
            align();
            if (currIndex <= this.f13122w) {
                int i14 = currIndex;
                currIndex++;
                temp[i14] = bitOffset;
            }
            this.changingElemSize = currIndex;
            lineOffset += scanlineStride;
        }
    }

    private int decodeWhiteCodeWord() {
        int runLength = 0;
        boolean isWhite = true;
        while (isWhite) {
            int current = nextNBits(10);
            short s2 = white[current];
            int isT = s2 & 1;
            int bits = (s2 >>> 1) & 15;
            if (bits == 12) {
                int twoBits = nextLesserThan8Bits(2);
                short s3 = additionalMakeup[((current << 2) & 12) | twoBits];
                int bits2 = (s3 >>> 1) & 7;
                int code = (s3 >>> 4) & 4095;
                runLength += code;
                updatePointer(4 - bits2);
            } else {
                if (bits == 0) {
                    throw new RuntimeException("Invalid code encountered.");
                }
                if (bits == 15) {
                    throw new RuntimeException("EOL code word encountered in White run.");
                }
                int code2 = (s2 >>> 5) & 2047;
                runLength += code2;
                updatePointer(10 - bits);
                if (isT == 0) {
                    isWhite = false;
                }
            }
        }
        return runLength;
    }

    private void getNextChangingElement(int a0, boolean isWhite, int[] ret) {
        int start;
        int[] pce = this.prevChangingElems;
        int ces = this.changingElemSize;
        int start2 = this.lastChangingElement > 0 ? this.lastChangingElement - 1 : 0;
        if (isWhite) {
            start = start2 & (-2);
        } else {
            start = start2 | 1;
        }
        ret[0] = this.f13122w;
        ret[1] = this.f13122w;
        int i2 = start;
        while (true) {
            if (i2 >= ces) {
                break;
            }
            int temp = pce[i2];
            if (temp <= a0) {
                i2 += 2;
            } else {
                this.lastChangingElement = i2;
                ret[0] = temp;
                break;
            }
        }
        if (i2 + 1 < ces) {
            ret[1] = pce[i2 + 1];
        }
    }

    public boolean isAlign() {
        return this.align;
    }

    public boolean isFillBits() {
        return this.fillBits;
    }

    private int nextLesserThan8Bits(int bitsToGet) {
        byte b2;
        byte next;
        int i1;
        int l2 = this.data.length - 1;
        int bp2 = this.bytePointer;
        if (this.fillOrder == 1) {
            b2 = this.data[bp2];
            if (bp2 == l2) {
                next = 0;
            } else {
                next = this.data[bp2 + 1];
            }
        } else if (this.fillOrder == 2) {
            b2 = flipTable[this.data[bp2] & 255];
            if (bp2 == l2) {
                next = 0;
            } else {
                next = flipTable[this.data[bp2 + 1] & 255];
            }
        } else {
            throw new RuntimeException("tag must be either 1 or 2.");
        }
        int bitsLeft = 8 - this.bitPointer;
        int bitsFromNextByte = bitsToGet - bitsLeft;
        int shift = bitsLeft - bitsToGet;
        if (shift >= 0) {
            i1 = (b2 & table1[bitsLeft]) >>> shift;
            this.bitPointer += bitsToGet;
            if (this.bitPointer == 8) {
                this.bitPointer = 0;
                this.bytePointer++;
            }
        } else {
            int i12 = (b2 & table1[bitsLeft]) << (-shift);
            int i2 = (next & table2[bitsFromNextByte]) >>> (8 - bitsFromNextByte);
            i1 = i12 | i2;
            this.bytePointer++;
            this.bitPointer = bitsFromNextByte;
        }
        return i1;
    }

    private int nextNBits(int bitsToGet) {
        byte b2;
        byte next;
        byte next2next;
        int l2 = this.data.length - 1;
        int bp2 = this.bytePointer;
        if (this.fillOrder == 1) {
            b2 = this.data[bp2];
            if (bp2 == l2) {
                next = 0;
                next2next = 0;
            } else if (bp2 + 1 == l2) {
                next = this.data[bp2 + 1];
                next2next = 0;
            } else {
                next = this.data[bp2 + 1];
                next2next = this.data[bp2 + 2];
            }
        } else if (this.fillOrder == 2) {
            b2 = flipTable[this.data[bp2] & 255];
            if (bp2 == l2) {
                next = 0;
                next2next = 0;
            } else if (bp2 + 1 == l2) {
                next = flipTable[this.data[bp2 + 1] & 255];
                next2next = 0;
            } else {
                next = flipTable[this.data[bp2 + 1] & 255];
                next2next = flipTable[this.data[bp2 + 2] & 255];
            }
        } else {
            throw new RuntimeException("tag must be either 1 or 2.");
        }
        int bitsLeft = 8 - this.bitPointer;
        int bitsFromNextByte = bitsToGet - bitsLeft;
        int bitsFromNext2NextByte = 0;
        if (bitsFromNextByte > 8) {
            bitsFromNext2NextByte = bitsFromNextByte - 8;
            bitsFromNextByte = 8;
        }
        this.bytePointer++;
        int i1 = (b2 & table1[bitsLeft]) << (bitsToGet - bitsLeft);
        int i2 = (next & table2[bitsFromNextByte]) >>> (8 - bitsFromNextByte);
        if (bitsFromNext2NextByte != 0) {
            int i3 = (next2next & table2[bitsFromNext2NextByte]) >>> (8 - bitsFromNext2NextByte);
            i2 = (i2 << bitsFromNext2NextByte) | i3;
            this.bytePointer++;
            this.bitPointer = bitsFromNext2NextByte;
        } else if (bitsFromNextByte == 8) {
            this.bitPointer = 0;
            this.bytePointer++;
        } else {
            this.bitPointer = bitsFromNextByte;
        }
        int i4 = i1 | i2;
        return i4;
    }

    private int readEOL(boolean isFirstEOL) {
        if (!seekEOL()) {
            throw new RuntimeException("EOL not found");
        }
        if (!this.fillBits) {
            int next12Bits = nextNBits(12);
            if (isFirstEOL && next12Bits == 0 && nextNBits(4) == 1) {
                this.fillBits = true;
                return 1;
            }
            if (next12Bits != 1) {
                throw new RuntimeException("Scanline must begin with EOL code word.");
            }
        } else {
            int bitsLeft = 8 - this.bitPointer;
            if (nextNBits(bitsLeft) != 0) {
                throw new RuntimeException("All fill bits preceding EOL code must be 0.");
            }
            if (bitsLeft < 4 && nextNBits(8) != 0) {
                throw new RuntimeException("All fill bits preceding EOL code must be 0.");
            }
            int next8 = nextNBits(8);
            if (isFirstEOL && (next8 & 240) == 16) {
                this.fillBits = false;
                updatePointer(4);
            } else {
                while (next8 != 1) {
                    if (next8 != 0) {
                        throw new RuntimeException("0 bits expected before EOL");
                    }
                    next8 = nextNBits(8);
                }
            }
        }
        return nextLesserThan8Bits(1);
    }

    private boolean seekEOL() {
        int bitIndexMax = (this.data.length * 8) - 1;
        int bitIndex = (this.bytePointer * 8) + this.bitPointer;
        while (bitIndex <= bitIndexMax - 12) {
            int next12Bits = nextNBits(12);
            bitIndex += 12;
            while (next12Bits != 1 && bitIndex < bitIndexMax) {
                next12Bits = ((next12Bits & 2047) << 1) | (nextLesserThan8Bits(1) & 1);
                bitIndex++;
            }
            if (next12Bits == 1) {
                updatePointer(12);
                return true;
            }
        }
        return false;
    }

    public void setAlign(boolean align) {
        this.align = align;
    }

    public void setFillBits(boolean fillBits) {
        this.fillBits = fillBits;
    }

    private void setToBlack(byte[] buffer, int lineOffset, int bitOffset, int numBits) {
        int bitNum = (8 * lineOffset) + bitOffset;
        int lastBit = bitNum + numBits;
        int byteNum = bitNum >> 3;
        int shift = bitNum & 7;
        if (shift > 0) {
            int maskVal = 1 << (7 - shift);
            byte val = buffer[byteNum];
            while (maskVal > 0 && bitNum < lastBit) {
                val = (byte) (val | maskVal);
                maskVal >>= 1;
                bitNum++;
            }
            buffer[byteNum] = val;
        }
        int byteNum2 = bitNum >> 3;
        while (bitNum < lastBit - 7) {
            int i2 = byteNum2;
            byteNum2++;
            buffer[i2] = -1;
            bitNum += 8;
        }
        while (bitNum < lastBit) {
            int byteNum3 = bitNum >> 3;
            buffer[byteNum3] = (byte) (buffer[byteNum3] | (1 << (7 - (bitNum & 7))));
            bitNum++;
        }
    }

    private void updatePointer(int bitsToMoveBack) {
        if (bitsToMoveBack > 8) {
            this.bytePointer -= bitsToMoveBack / 8;
            bitsToMoveBack %= 8;
        }
        int i2 = this.bitPointer - bitsToMoveBack;
        if (i2 < 0) {
            this.bytePointer--;
            this.bitPointer = 8 + i2;
        } else {
            this.bitPointer = i2;
        }
    }
}
