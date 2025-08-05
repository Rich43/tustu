package sun.nio.cs.ext;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.CoderResult;
import java.util.Arrays;
import sun.nio.cs.HistoricallyNamedCharset;

/* loaded from: charsets.jar:sun/nio/cs/ext/EUC_TW.class */
public class EUC_TW extends Charset implements HistoricallyNamedCharset {
    private static final int SS2 = 142;

    public EUC_TW() {
        super("x-EUC-TW", ExtendedCharsets.aliasesFor("x-EUC-TW"));
    }

    @Override // sun.nio.cs.HistoricallyNamedCharset
    public String historicalName() {
        return "EUC_TW";
    }

    @Override // java.nio.charset.Charset
    public boolean contains(Charset charset) {
        return charset.name().equals("US-ASCII") || (charset instanceof EUC_TW);
    }

    @Override // java.nio.charset.Charset
    public CharsetDecoder newDecoder() {
        return new Decoder(this);
    }

    @Override // java.nio.charset.Charset
    public CharsetEncoder newEncoder() {
        return new Encoder(this);
    }

    /* loaded from: charsets.jar:sun/nio/cs/ext/EUC_TW$Decoder.class */
    public static class Decoder extends CharsetDecoder {
        char[] c1;
        char[] c2;
        static final int b1Min = 161;
        static final int b1Max = 254;
        static final int b2Min = 161;
        static final int b2Max = 254;
        static final int dbSegSize = 94;
        static final byte[] b2cIsSupp;
        static final String[] b2c = EUC_TWMapping.b2c;
        static final byte[] cnspToIndex = new byte[256];

        public Decoder(Charset charset) {
            super(charset, 2.0f, 2.0f);
            this.c1 = new char[1];
            this.c2 = new char[2];
        }

        public char[] toUnicode(int i2, int i3, int i4) {
            return decode(i2, i3, i4, this.c1, this.c2);
        }

        static {
            Arrays.fill(cnspToIndex, (byte) -1);
            cnspToIndex[162] = 1;
            cnspToIndex[163] = 2;
            cnspToIndex[164] = 3;
            cnspToIndex[165] = 4;
            cnspToIndex[166] = 5;
            cnspToIndex[167] = 6;
            cnspToIndex[175] = 7;
            String str = EUC_TWMapping.b2cIsSuppStr;
            byte[] bArr = new byte[str.length() << 1];
            int i2 = 0;
            for (int i3 = 0; i3 < str.length(); i3++) {
                char cCharAt = str.charAt(i3);
                int i4 = i2;
                int i5 = i2 + 1;
                bArr[i4] = (byte) (cCharAt >> '\b');
                i2 = i5 + 1;
                bArr[i5] = (byte) (cCharAt & 255);
            }
            b2cIsSupp = bArr;
        }

        static boolean isLegalDB(int i2) {
            return i2 >= 161 && i2 <= 254;
        }

        static char[] decode(int i2, int i3, int i4, char[] cArr, char[] cArr2) {
            int i5;
            char cCharAt;
            if (i2 < 161 || i2 > 254 || i3 < 161 || i3 > 254 || (cCharAt = b2c[i4].charAt((i5 = (((i2 - 161) * 94) + i3) - 161))) == 65533) {
                return null;
            }
            if ((b2cIsSupp[i5] & (1 << i4)) == 0) {
                cArr[0] = cCharAt;
                return cArr;
            }
            cArr2[0] = Character.highSurrogate(0 + cCharAt);
            cArr2[1] = Character.lowSurrogate(0 + cCharAt);
            return cArr2;
        }

        private CoderResult decodeArrayLoop(ByteBuffer byteBuffer, CharBuffer charBuffer) {
            byte[] bArrArray = byteBuffer.array();
            int iArrayOffset = byteBuffer.arrayOffset() + byteBuffer.position();
            int iArrayOffset2 = byteBuffer.arrayOffset() + byteBuffer.limit();
            char[] cArrArray = charBuffer.array();
            int iArrayOffset3 = charBuffer.arrayOffset() + charBuffer.position();
            int iArrayOffset4 = charBuffer.arrayOffset() + charBuffer.limit();
            while (iArrayOffset < iArrayOffset2) {
                try {
                    int i2 = bArrArray[iArrayOffset] & 255;
                    if (i2 == 142) {
                        if (iArrayOffset2 - iArrayOffset < 4) {
                            CoderResult coderResult = CoderResult.UNDERFLOW;
                            byteBuffer.position(iArrayOffset - byteBuffer.arrayOffset());
                            charBuffer.position(iArrayOffset3 - charBuffer.arrayOffset());
                            return coderResult;
                        }
                        byte b2 = cnspToIndex[bArrArray[iArrayOffset + 1] & 255];
                        if (b2 < 0) {
                            CoderResult coderResultMalformedForLength = CoderResult.malformedForLength(2);
                            byteBuffer.position(iArrayOffset - byteBuffer.arrayOffset());
                            charBuffer.position(iArrayOffset3 - charBuffer.arrayOffset());
                            return coderResultMalformedForLength;
                        }
                        int i3 = bArrArray[iArrayOffset + 2] & 255;
                        int i4 = bArrArray[iArrayOffset + 3] & 255;
                        char[] unicode = toUnicode(i3, i4, b2);
                        if (unicode == null) {
                            if (isLegalDB(i3) && isLegalDB(i4)) {
                                CoderResult coderResultUnmappableForLength = CoderResult.unmappableForLength(4);
                                byteBuffer.position(iArrayOffset - byteBuffer.arrayOffset());
                                charBuffer.position(iArrayOffset3 - charBuffer.arrayOffset());
                                return coderResultUnmappableForLength;
                            }
                            CoderResult coderResultMalformedForLength2 = CoderResult.malformedForLength(4);
                            byteBuffer.position(iArrayOffset - byteBuffer.arrayOffset());
                            charBuffer.position(iArrayOffset3 - charBuffer.arrayOffset());
                            return coderResultMalformedForLength2;
                        }
                        if (iArrayOffset4 - iArrayOffset3 < unicode.length) {
                            CoderResult coderResult2 = CoderResult.OVERFLOW;
                            byteBuffer.position(iArrayOffset - byteBuffer.arrayOffset());
                            charBuffer.position(iArrayOffset3 - charBuffer.arrayOffset());
                            return coderResult2;
                        }
                        if (unicode.length == 1) {
                            int i5 = iArrayOffset3;
                            iArrayOffset3++;
                            cArrArray[i5] = unicode[0];
                        } else {
                            int i6 = iArrayOffset3;
                            int i7 = iArrayOffset3 + 1;
                            cArrArray[i6] = unicode[0];
                            iArrayOffset3 = i7 + 1;
                            cArrArray[i7] = unicode[1];
                        }
                        iArrayOffset += 4;
                    } else if (i2 < 128) {
                        if (iArrayOffset4 - iArrayOffset3 < 1) {
                            CoderResult coderResult3 = CoderResult.OVERFLOW;
                            byteBuffer.position(iArrayOffset - byteBuffer.arrayOffset());
                            charBuffer.position(iArrayOffset3 - charBuffer.arrayOffset());
                            return coderResult3;
                        }
                        int i8 = iArrayOffset3;
                        iArrayOffset3++;
                        cArrArray[i8] = (char) i2;
                        iArrayOffset++;
                    } else {
                        if (iArrayOffset2 - iArrayOffset < 2) {
                            CoderResult coderResult4 = CoderResult.UNDERFLOW;
                            byteBuffer.position(iArrayOffset - byteBuffer.arrayOffset());
                            charBuffer.position(iArrayOffset3 - charBuffer.arrayOffset());
                            return coderResult4;
                        }
                        int i9 = bArrArray[iArrayOffset + 1] & 255;
                        char[] unicode2 = toUnicode(i2, i9, 0);
                        if (unicode2 == null) {
                            if (isLegalDB(i2) && isLegalDB(i9)) {
                                CoderResult coderResultUnmappableForLength2 = CoderResult.unmappableForLength(2);
                                byteBuffer.position(iArrayOffset - byteBuffer.arrayOffset());
                                charBuffer.position(iArrayOffset3 - charBuffer.arrayOffset());
                                return coderResultUnmappableForLength2;
                            }
                            CoderResult coderResultMalformedForLength3 = CoderResult.malformedForLength(1);
                            byteBuffer.position(iArrayOffset - byteBuffer.arrayOffset());
                            charBuffer.position(iArrayOffset3 - charBuffer.arrayOffset());
                            return coderResultMalformedForLength3;
                        }
                        if (iArrayOffset4 - iArrayOffset3 < 1) {
                            CoderResult coderResult5 = CoderResult.OVERFLOW;
                            byteBuffer.position(iArrayOffset - byteBuffer.arrayOffset());
                            charBuffer.position(iArrayOffset3 - charBuffer.arrayOffset());
                            return coderResult5;
                        }
                        int i10 = iArrayOffset3;
                        iArrayOffset3++;
                        cArrArray[i10] = unicode2[0];
                        iArrayOffset += 2;
                    }
                } catch (Throwable th) {
                    byteBuffer.position(iArrayOffset - byteBuffer.arrayOffset());
                    charBuffer.position(iArrayOffset3 - charBuffer.arrayOffset());
                    throw th;
                }
            }
            CoderResult coderResult6 = CoderResult.UNDERFLOW;
            byteBuffer.position(iArrayOffset - byteBuffer.arrayOffset());
            charBuffer.position(iArrayOffset3 - charBuffer.arrayOffset());
            return coderResult6;
        }

        private CoderResult decodeBufferLoop(ByteBuffer byteBuffer, CharBuffer charBuffer) {
            int iPosition = byteBuffer.position();
            while (byteBuffer.hasRemaining()) {
                try {
                    int i2 = byteBuffer.get() & 255;
                    if (i2 == 142) {
                        if (byteBuffer.remaining() < 3) {
                            CoderResult coderResult = CoderResult.UNDERFLOW;
                            byteBuffer.position(iPosition);
                            return coderResult;
                        }
                        byte b2 = cnspToIndex[byteBuffer.get() & 255];
                        if (b2 < 0) {
                            CoderResult coderResultMalformedForLength = CoderResult.malformedForLength(2);
                            byteBuffer.position(iPosition);
                            return coderResultMalformedForLength;
                        }
                        int i3 = byteBuffer.get() & 255;
                        int i4 = byteBuffer.get() & 255;
                        char[] unicode = toUnicode(i3, i4, b2);
                        if (unicode == null) {
                            if (isLegalDB(i3) && isLegalDB(i4)) {
                                CoderResult coderResultUnmappableForLength = CoderResult.unmappableForLength(4);
                                byteBuffer.position(iPosition);
                                return coderResultUnmappableForLength;
                            }
                            CoderResult coderResultMalformedForLength2 = CoderResult.malformedForLength(4);
                            byteBuffer.position(iPosition);
                            return coderResultMalformedForLength2;
                        }
                        if (charBuffer.remaining() < unicode.length) {
                            CoderResult coderResult2 = CoderResult.OVERFLOW;
                            byteBuffer.position(iPosition);
                            return coderResult2;
                        }
                        if (unicode.length == 1) {
                            charBuffer.put(unicode[0]);
                        } else {
                            charBuffer.put(unicode[0]);
                            charBuffer.put(unicode[1]);
                        }
                        iPosition += 4;
                    } else if (i2 < 128) {
                        if (!charBuffer.hasRemaining()) {
                            CoderResult coderResult3 = CoderResult.OVERFLOW;
                            byteBuffer.position(iPosition);
                            return coderResult3;
                        }
                        charBuffer.put((char) i2);
                        iPosition++;
                    } else {
                        if (!byteBuffer.hasRemaining()) {
                            CoderResult coderResult4 = CoderResult.UNDERFLOW;
                            byteBuffer.position(iPosition);
                            return coderResult4;
                        }
                        int i5 = byteBuffer.get() & 255;
                        char[] unicode2 = toUnicode(i2, i5, 0);
                        if (unicode2 == null) {
                            if (isLegalDB(i2) && isLegalDB(i5)) {
                                CoderResult coderResultUnmappableForLength2 = CoderResult.unmappableForLength(2);
                                byteBuffer.position(iPosition);
                                return coderResultUnmappableForLength2;
                            }
                            CoderResult coderResultMalformedForLength3 = CoderResult.malformedForLength(1);
                            byteBuffer.position(iPosition);
                            return coderResultMalformedForLength3;
                        }
                        if (!charBuffer.hasRemaining()) {
                            CoderResult coderResult5 = CoderResult.OVERFLOW;
                            byteBuffer.position(iPosition);
                            return coderResult5;
                        }
                        charBuffer.put(unicode2[0]);
                        iPosition += 2;
                    }
                } catch (Throwable th) {
                    byteBuffer.position(iPosition);
                    throw th;
                }
            }
            CoderResult coderResult6 = CoderResult.UNDERFLOW;
            byteBuffer.position(iPosition);
            return coderResult6;
        }

        @Override // java.nio.charset.CharsetDecoder
        protected CoderResult decodeLoop(ByteBuffer byteBuffer, CharBuffer charBuffer) {
            if (byteBuffer.hasArray() && charBuffer.hasArray()) {
                return decodeArrayLoop(byteBuffer, charBuffer);
            }
            return decodeBufferLoop(byteBuffer, charBuffer);
        }
    }

    /* loaded from: charsets.jar:sun/nio/cs/ext/EUC_TW$Encoder.class */
    public static class Encoder extends CharsetEncoder {

        /* renamed from: bb, reason: collision with root package name */
        private byte[] f13593bb;
        static final char[] c2b;
        static final char[] c2bIndex;
        static final char[] c2bSupp;
        static final char[] c2bSuppIndex;
        static final byte[] c2bPlane;

        public Encoder(Charset charset) {
            super(charset, 4.0f, 4.0f);
            this.f13593bb = new byte[4];
        }

        @Override // java.nio.charset.CharsetEncoder
        public boolean canEncode(char c2) {
            return c2 <= 127 || toEUC(c2, this.f13593bb) != -1;
        }

        @Override // java.nio.charset.CharsetEncoder
        public boolean canEncode(CharSequence charSequence) {
            int i2 = 0;
            while (i2 < charSequence.length()) {
                int i3 = i2;
                i2++;
                char cCharAt = charSequence.charAt(i3);
                if (Character.isHighSurrogate(cCharAt)) {
                    if (i2 == charSequence.length()) {
                        return false;
                    }
                    i2++;
                    char cCharAt2 = charSequence.charAt(i2);
                    if (!Character.isLowSurrogate(cCharAt2) || toEUC(cCharAt, cCharAt2, this.f13593bb) == -1) {
                        return false;
                    }
                } else if (!canEncode(cCharAt)) {
                    return false;
                }
            }
            return true;
        }

        public int toEUC(char c2, char c3, byte[] bArr) {
            return encode(c2, c3, bArr);
        }

        public int toEUC(char c2, byte[] bArr) {
            return encode(c2, bArr);
        }

        private CoderResult encodeArrayLoop(CharBuffer charBuffer, ByteBuffer byteBuffer) {
            int euc;
            char[] cArrArray = charBuffer.array();
            int iArrayOffset = charBuffer.arrayOffset() + charBuffer.position();
            int iArrayOffset2 = charBuffer.arrayOffset() + charBuffer.limit();
            byte[] bArrArray = byteBuffer.array();
            int iArrayOffset3 = byteBuffer.arrayOffset() + byteBuffer.position();
            int iArrayOffset4 = byteBuffer.arrayOffset() + byteBuffer.limit();
            while (iArrayOffset < iArrayOffset2) {
                try {
                    char c2 = cArrArray[iArrayOffset];
                    int i2 = 1;
                    if (c2 < 128) {
                        this.f13593bb[0] = (byte) c2;
                        euc = 1;
                    } else {
                        euc = toEUC(c2, this.f13593bb);
                        if (euc == -1) {
                            if (Character.isHighSurrogate(c2)) {
                                if (iArrayOffset + 1 == iArrayOffset2) {
                                    CoderResult coderResult = CoderResult.UNDERFLOW;
                                    charBuffer.position(iArrayOffset - charBuffer.arrayOffset());
                                    byteBuffer.position(iArrayOffset3 - byteBuffer.arrayOffset());
                                    return coderResult;
                                }
                                if (!Character.isLowSurrogate(cArrArray[iArrayOffset + 1])) {
                                    CoderResult coderResultMalformedForLength = CoderResult.malformedForLength(1);
                                    charBuffer.position(iArrayOffset - charBuffer.arrayOffset());
                                    byteBuffer.position(iArrayOffset3 - byteBuffer.arrayOffset());
                                    return coderResultMalformedForLength;
                                }
                                euc = toEUC(c2, cArrArray[iArrayOffset + 1], this.f13593bb);
                                i2 = 2;
                            } else if (Character.isLowSurrogate(c2)) {
                                CoderResult coderResultMalformedForLength2 = CoderResult.malformedForLength(1);
                                charBuffer.position(iArrayOffset - charBuffer.arrayOffset());
                                byteBuffer.position(iArrayOffset3 - byteBuffer.arrayOffset());
                                return coderResultMalformedForLength2;
                            }
                        }
                    }
                    if (euc == -1) {
                        CoderResult coderResultUnmappableForLength = CoderResult.unmappableForLength(i2);
                        charBuffer.position(iArrayOffset - charBuffer.arrayOffset());
                        byteBuffer.position(iArrayOffset3 - byteBuffer.arrayOffset());
                        return coderResultUnmappableForLength;
                    }
                    if (iArrayOffset4 - iArrayOffset3 < euc) {
                        CoderResult coderResult2 = CoderResult.OVERFLOW;
                        charBuffer.position(iArrayOffset - charBuffer.arrayOffset());
                        byteBuffer.position(iArrayOffset3 - byteBuffer.arrayOffset());
                        return coderResult2;
                    }
                    for (int i3 = 0; i3 < euc; i3++) {
                        int i4 = iArrayOffset3;
                        iArrayOffset3++;
                        bArrArray[i4] = this.f13593bb[i3];
                    }
                    iArrayOffset += i2;
                } catch (Throwable th) {
                    charBuffer.position(iArrayOffset - charBuffer.arrayOffset());
                    byteBuffer.position(iArrayOffset3 - byteBuffer.arrayOffset());
                    throw th;
                }
            }
            CoderResult coderResult3 = CoderResult.UNDERFLOW;
            charBuffer.position(iArrayOffset - charBuffer.arrayOffset());
            byteBuffer.position(iArrayOffset3 - byteBuffer.arrayOffset());
            return coderResult3;
        }

        private CoderResult encodeBufferLoop(CharBuffer charBuffer, ByteBuffer byteBuffer) {
            int euc;
            int iPosition = charBuffer.position();
            while (charBuffer.hasRemaining()) {
                try {
                    int i2 = 1;
                    char c2 = charBuffer.get();
                    if (c2 < 128) {
                        euc = 1;
                        this.f13593bb[0] = (byte) c2;
                    } else {
                        euc = toEUC(c2, this.f13593bb);
                        if (euc == -1) {
                            if (Character.isHighSurrogate(c2)) {
                                if (!charBuffer.hasRemaining()) {
                                    CoderResult coderResult = CoderResult.UNDERFLOW;
                                    charBuffer.position(iPosition);
                                    return coderResult;
                                }
                                char c3 = charBuffer.get();
                                if (!Character.isLowSurrogate(c3)) {
                                    CoderResult coderResultMalformedForLength = CoderResult.malformedForLength(1);
                                    charBuffer.position(iPosition);
                                    return coderResultMalformedForLength;
                                }
                                euc = toEUC(c2, c3, this.f13593bb);
                                i2 = 2;
                            } else if (Character.isLowSurrogate(c2)) {
                                CoderResult coderResultMalformedForLength2 = CoderResult.malformedForLength(1);
                                charBuffer.position(iPosition);
                                return coderResultMalformedForLength2;
                            }
                        }
                    }
                    if (euc == -1) {
                        CoderResult coderResultUnmappableForLength = CoderResult.unmappableForLength(i2);
                        charBuffer.position(iPosition);
                        return coderResultUnmappableForLength;
                    }
                    if (byteBuffer.remaining() < euc) {
                        CoderResult coderResult2 = CoderResult.OVERFLOW;
                        charBuffer.position(iPosition);
                        return coderResult2;
                    }
                    for (int i3 = 0; i3 < euc; i3++) {
                        byteBuffer.put(this.f13593bb[i3]);
                    }
                    iPosition += i2;
                } catch (Throwable th) {
                    charBuffer.position(iPosition);
                    throw th;
                }
            }
            CoderResult coderResult3 = CoderResult.UNDERFLOW;
            charBuffer.position(iPosition);
            return coderResult3;
        }

        @Override // java.nio.charset.CharsetEncoder
        protected CoderResult encodeLoop(CharBuffer charBuffer, ByteBuffer byteBuffer) {
            if (charBuffer.hasArray() && byteBuffer.hasArray()) {
                return encodeArrayLoop(charBuffer, byteBuffer);
            }
            return encodeBufferLoop(charBuffer, byteBuffer);
        }

        static int encode(char c2, char c3, byte[] bArr) {
            int i2;
            char c4;
            int codePoint = Character.toCodePoint(c2, c3);
            if ((codePoint & 983040) != 131072) {
                return -1;
            }
            int i3 = codePoint - 131072;
            char c5 = c2bSuppIndex[i3 >> 8];
            if (c5 == 65533 || (c4 = c2bSupp[(i2 = c5 + (i3 & 255))]) == 65533) {
                return -1;
            }
            int i4 = (c2bPlane[i2] >> 4) & 15;
            bArr[0] = -114;
            bArr[1] = (byte) (160 | i4);
            bArr[2] = (byte) (c4 >> '\b');
            bArr[3] = (byte) c4;
            return 4;
        }

        static int encode(char c2, byte[] bArr) {
            int i2;
            char c3;
            char c4 = c2bIndex[c2 >> '\b'];
            if (c4 == 65533 || (c3 = c2b[(i2 = c4 + (c2 & 255))]) == 65533) {
                return -1;
            }
            int i3 = c2bPlane[i2] & 15;
            if (i3 == 0) {
                bArr[0] = (byte) (c3 >> '\b');
                bArr[1] = (byte) c3;
                return 2;
            }
            bArr[0] = -114;
            bArr[1] = (byte) (160 | i3);
            bArr[2] = (byte) (c3 >> '\b');
            bArr[3] = (byte) c3;
            return 4;
        }

        static {
            String[] strArr = Decoder.b2c;
            byte[] bArr = Decoder.b2cIsSupp;
            c2bIndex = EUC_TWMapping.c2bIndex;
            c2bSuppIndex = EUC_TWMapping.c2bSuppIndex;
            char[] cArr = new char[31744];
            char[] cArr2 = new char[43520];
            byte[] bArr2 = new byte[Math.max(31744, 43520)];
            Arrays.fill(cArr, (char) 65533);
            Arrays.fill(cArr2, (char) 65533);
            for (int i2 = 0; i2 < strArr.length; i2++) {
                String str = strArr[i2];
                int i3 = i2;
                if (i3 == 7) {
                    i3 = 15;
                } else if (i3 != 0) {
                    i3 = i2 + 1;
                }
                int i4 = 0;
                for (int i5 = 161; i5 <= 254; i5++) {
                    for (int i6 = 161; i6 <= 254; i6++) {
                        char cCharAt = str.charAt(i4);
                        if (cCharAt != 65533) {
                            if ((bArr[i4] & (1 << i2)) != 0) {
                                int i7 = c2bSuppIndex[cCharAt >> '\b'] + (cCharAt & 255);
                                cArr2[i7] = (char) ((i5 << 8) + i6);
                                bArr2[i7] = (byte) (bArr2[i7] | ((byte) (i3 << 4)));
                            } else {
                                int i8 = c2bIndex[cCharAt >> '\b'] + (cCharAt & 255);
                                cArr[i8] = (char) ((i5 << 8) + i6);
                                bArr2[i8] = (byte) (bArr2[i8] | ((byte) i3));
                            }
                        }
                        i4++;
                    }
                }
            }
            c2b = cArr;
            c2bSupp = cArr2;
            c2bPlane = bArr2;
        }
    }
}
