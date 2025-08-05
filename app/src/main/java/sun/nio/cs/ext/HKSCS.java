package sun.nio.cs.ext;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CoderResult;
import java.util.Arrays;
import sun.nio.cs.Surrogate;
import sun.nio.cs.ext.DoubleByte;

/* loaded from: charsets.jar:sun/nio/cs/ext/HKSCS.class */
public class HKSCS {

    /* loaded from: charsets.jar:sun/nio/cs/ext/HKSCS$Decoder.class */
    public static class Decoder extends DoubleByte.Decoder {
        static int b2Min = 64;
        static int b2Max = 254;
        private char[][] b2cBmp;
        private char[][] b2cSupp;
        private DoubleByte.Decoder big5Dec;

        protected Decoder(Charset charset, DoubleByte.Decoder decoder, char[][] cArr, char[][] cArr2) {
            super(charset, 0.5f, 1.0f, (char[][]) null, null, 0, 0);
            this.big5Dec = decoder;
            this.b2cBmp = cArr;
            this.b2cSupp = cArr2;
        }

        @Override // sun.nio.cs.ext.DoubleByte.Decoder
        public char decodeSingle(int i2) {
            return this.big5Dec.decodeSingle(i2);
        }

        public char decodeBig5(int i2, int i3) {
            return this.big5Dec.decodeDouble(i2, i3);
        }

        @Override // sun.nio.cs.ext.DoubleByte.Decoder
        public char decodeDouble(int i2, int i3) {
            return this.b2cBmp[i2][i3 - b2Min];
        }

        public char decodeDoubleEx(int i2, int i3) {
            return this.b2cSupp[i2][i3 - b2Min];
        }

        @Override // sun.nio.cs.ext.DoubleByte.Decoder
        protected CoderResult decodeArrayLoop(ByteBuffer byteBuffer, CharBuffer charBuffer) {
            byte[] bArrArray = byteBuffer.array();
            int iArrayOffset = byteBuffer.arrayOffset() + byteBuffer.position();
            int iArrayOffset2 = byteBuffer.arrayOffset() + byteBuffer.limit();
            char[] cArrArray = charBuffer.array();
            int iArrayOffset3 = charBuffer.arrayOffset() + charBuffer.position();
            int iArrayOffset4 = charBuffer.arrayOffset() + charBuffer.limit();
            while (iArrayOffset < iArrayOffset2) {
                try {
                    int i2 = bArrArray[iArrayOffset] & 255;
                    char cDecodeSingle = decodeSingle(i2);
                    int i3 = 1;
                    int i4 = 1;
                    if (cDecodeSingle == 65533) {
                        if (iArrayOffset2 - iArrayOffset < 2) {
                            CoderResult coderResult = CoderResult.UNDERFLOW;
                            byteBuffer.position(iArrayOffset - byteBuffer.arrayOffset());
                            charBuffer.position(iArrayOffset3 - charBuffer.arrayOffset());
                            return coderResult;
                        }
                        int i5 = bArrArray[iArrayOffset + 1] & 255;
                        i3 = 1 + 1;
                        if (i5 < b2Min || i5 > b2Max) {
                            CoderResult coderResultUnmappableForLength = CoderResult.unmappableForLength(2);
                            byteBuffer.position(iArrayOffset - byteBuffer.arrayOffset());
                            charBuffer.position(iArrayOffset3 - charBuffer.arrayOffset());
                            return coderResultUnmappableForLength;
                        }
                        cDecodeSingle = decodeDouble(i2, i5);
                        if (cDecodeSingle == 65533) {
                            cDecodeSingle = decodeDoubleEx(i2, i5);
                            if (cDecodeSingle == 65533) {
                                cDecodeSingle = decodeBig5(i2, i5);
                                if (cDecodeSingle == 65533) {
                                    CoderResult coderResultUnmappableForLength2 = CoderResult.unmappableForLength(2);
                                    byteBuffer.position(iArrayOffset - byteBuffer.arrayOffset());
                                    charBuffer.position(iArrayOffset3 - charBuffer.arrayOffset());
                                    return coderResultUnmappableForLength2;
                                }
                            } else {
                                i4 = 2;
                            }
                        }
                    }
                    if (iArrayOffset4 - iArrayOffset3 < i4) {
                        CoderResult coderResult2 = CoderResult.OVERFLOW;
                        byteBuffer.position(iArrayOffset - byteBuffer.arrayOffset());
                        charBuffer.position(iArrayOffset3 - charBuffer.arrayOffset());
                        return coderResult2;
                    }
                    if (i4 == 2) {
                        int i6 = iArrayOffset3;
                        int i7 = iArrayOffset3 + 1;
                        cArrArray[i6] = Surrogate.high(0 + cDecodeSingle);
                        iArrayOffset3 = i7 + 1;
                        cArrArray[i7] = Surrogate.low(0 + cDecodeSingle);
                    } else {
                        int i8 = iArrayOffset3;
                        iArrayOffset3++;
                        cArrArray[i8] = cDecodeSingle;
                    }
                    iArrayOffset += i3;
                } catch (Throwable th) {
                    byteBuffer.position(iArrayOffset - byteBuffer.arrayOffset());
                    charBuffer.position(iArrayOffset3 - charBuffer.arrayOffset());
                    throw th;
                }
            }
            CoderResult coderResult3 = CoderResult.UNDERFLOW;
            byteBuffer.position(iArrayOffset - byteBuffer.arrayOffset());
            charBuffer.position(iArrayOffset3 - charBuffer.arrayOffset());
            return coderResult3;
        }

        @Override // sun.nio.cs.ext.DoubleByte.Decoder
        protected CoderResult decodeBufferLoop(ByteBuffer byteBuffer, CharBuffer charBuffer) {
            int iPosition = byteBuffer.position();
            while (byteBuffer.hasRemaining()) {
                try {
                    int i2 = byteBuffer.get() & 255;
                    int i3 = 1;
                    int i4 = 1;
                    char cDecodeSingle = decodeSingle(i2);
                    if (cDecodeSingle == 65533) {
                        if (byteBuffer.remaining() < 1) {
                            CoderResult coderResult = CoderResult.UNDERFLOW;
                            byteBuffer.position(iPosition);
                            return coderResult;
                        }
                        int i5 = byteBuffer.get() & 255;
                        i3 = 1 + 1;
                        if (i5 < b2Min || i5 > b2Max) {
                            CoderResult coderResultUnmappableForLength = CoderResult.unmappableForLength(2);
                            byteBuffer.position(iPosition);
                            return coderResultUnmappableForLength;
                        }
                        cDecodeSingle = decodeDouble(i2, i5);
                        if (cDecodeSingle == 65533) {
                            cDecodeSingle = decodeDoubleEx(i2, i5);
                            if (cDecodeSingle == 65533) {
                                cDecodeSingle = decodeBig5(i2, i5);
                                if (cDecodeSingle == 65533) {
                                    CoderResult coderResultUnmappableForLength2 = CoderResult.unmappableForLength(2);
                                    byteBuffer.position(iPosition);
                                    return coderResultUnmappableForLength2;
                                }
                            } else {
                                i4 = 2;
                            }
                        }
                    }
                    if (charBuffer.remaining() < i4) {
                        CoderResult coderResult2 = CoderResult.OVERFLOW;
                        byteBuffer.position(iPosition);
                        return coderResult2;
                    }
                    if (i4 == 2) {
                        charBuffer.put(Surrogate.high(0 + cDecodeSingle));
                        charBuffer.put(Surrogate.low(0 + cDecodeSingle));
                    } else {
                        charBuffer.put(cDecodeSingle);
                    }
                    iPosition += i3;
                } catch (Throwable th) {
                    byteBuffer.position(iPosition);
                    throw th;
                }
            }
            CoderResult coderResult3 = CoderResult.UNDERFLOW;
            byteBuffer.position(iPosition);
            return coderResult3;
        }

        @Override // sun.nio.cs.ext.DoubleByte.Decoder, sun.nio.cs.ArrayDecoder
        public int decode(byte[] bArr, int i2, int i3, char[] cArr) {
            int i4 = 0;
            int i5 = i2 + i3;
            char cCharAt = replacement().charAt(0);
            while (i2 < i5) {
                int i6 = i2;
                i2++;
                int i7 = bArr[i6] & 255;
                char cDecodeSingle = decodeSingle(i7);
                if (cDecodeSingle == 65533) {
                    if (i5 == i2) {
                        cDecodeSingle = cCharAt;
                    } else {
                        i2++;
                        int i8 = bArr[i2] & 255;
                        if (i8 < b2Min || i8 > b2Max) {
                            cDecodeSingle = cCharAt;
                        } else {
                            char cDecodeDouble = decodeDouble(i7, i8);
                            cDecodeSingle = cDecodeDouble;
                            if (cDecodeDouble == 65533) {
                                char cDecodeDoubleEx = decodeDoubleEx(i7, i8);
                                if (cDecodeDoubleEx == 65533) {
                                    cDecodeSingle = decodeBig5(i7, i8);
                                    if (cDecodeSingle == 65533) {
                                        cDecodeSingle = cCharAt;
                                    }
                                } else {
                                    int i9 = i4;
                                    int i10 = i4 + 1;
                                    cArr[i9] = Surrogate.high(0 + cDecodeDoubleEx);
                                    i4 = i10 + 1;
                                    cArr[i10] = Surrogate.low(0 + cDecodeDoubleEx);
                                }
                            }
                        }
                    }
                }
                int i11 = i4;
                i4++;
                cArr[i11] = cDecodeSingle;
            }
            return i4;
        }

        @Override // sun.nio.cs.ext.DoubleByte.Decoder, java.nio.charset.CharsetDecoder
        public CoderResult decodeLoop(ByteBuffer byteBuffer, CharBuffer charBuffer) {
            if (byteBuffer.hasArray() && charBuffer.hasArray()) {
                return decodeArrayLoop(byteBuffer, charBuffer);
            }
            return decodeBufferLoop(byteBuffer, charBuffer);
        }

        static void initb2c(char[][] cArr, String[] strArr) {
            for (int i2 = 0; i2 < strArr.length; i2++) {
                if (strArr[i2] == null) {
                    cArr[i2] = DoubleByte.B2C_UNMAPPABLE;
                } else {
                    cArr[i2] = strArr[i2].toCharArray();
                }
            }
        }
    }

    /* loaded from: charsets.jar:sun/nio/cs/ext/HKSCS$Encoder.class */
    public static class Encoder extends DoubleByte.Encoder {
        private DoubleByte.Encoder big5Enc;
        private char[][] c2bBmp;
        private char[][] c2bSupp;
        private byte[] repl;
        static char[] C2B_UNMAPPABLE = new char[256];

        protected Encoder(Charset charset, DoubleByte.Encoder encoder, char[][] cArr, char[][] cArr2) {
            super(charset, null, null);
            this.repl = replacement();
            this.big5Enc = encoder;
            this.c2bBmp = cArr;
            this.c2bSupp = cArr2;
        }

        public int encodeBig5(char c2) {
            return this.big5Enc.encodeChar(c2);
        }

        @Override // sun.nio.cs.ext.DoubleByte.Encoder
        public int encodeChar(char c2) {
            char c3 = this.c2bBmp[c2 >> '\b'][c2 & 255];
            if (c3 == 65533) {
                return encodeBig5(c2);
            }
            return c3;
        }

        public int encodeSupp(int i2) {
            if ((i2 & 983040) != 131072) {
                return 65533;
            }
            return this.c2bSupp[(i2 >> 8) & 255][i2 & 255];
        }

        @Override // sun.nio.cs.ext.DoubleByte.Encoder, java.nio.charset.CharsetEncoder
        public boolean canEncode(char c2) {
            return encodeChar(c2) != 65533;
        }

        @Override // sun.nio.cs.ext.DoubleByte.Encoder
        protected CoderResult encodeArrayLoop(CharBuffer charBuffer, ByteBuffer byteBuffer) {
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
                    int iEncodeChar = encodeChar(c2);
                    if (iEncodeChar == 65533) {
                        if (!Character.isSurrogate(c2)) {
                            CoderResult coderResultUnmappableForLength = CoderResult.unmappableForLength(1);
                            charBuffer.position(iArrayOffset - charBuffer.arrayOffset());
                            byteBuffer.position(iArrayOffset3 - byteBuffer.arrayOffset());
                            return coderResultUnmappableForLength;
                        }
                        int i3 = sgp().parse(c2, cArrArray, iArrayOffset, iArrayOffset2);
                        if (i3 < 0) {
                            CoderResult coderResultError = this.sgp.error();
                            charBuffer.position(iArrayOffset - charBuffer.arrayOffset());
                            byteBuffer.position(iArrayOffset3 - byteBuffer.arrayOffset());
                            return coderResultError;
                        }
                        iEncodeChar = encodeSupp(i3);
                        if (iEncodeChar == 65533) {
                            CoderResult coderResultUnmappableForLength2 = CoderResult.unmappableForLength(2);
                            charBuffer.position(iArrayOffset - charBuffer.arrayOffset());
                            byteBuffer.position(iArrayOffset3 - byteBuffer.arrayOffset());
                            return coderResultUnmappableForLength2;
                        }
                        i2 = 2;
                    }
                    if (iEncodeChar > 255) {
                        if (iArrayOffset4 - iArrayOffset3 < 2) {
                            CoderResult coderResult = CoderResult.OVERFLOW;
                            charBuffer.position(iArrayOffset - charBuffer.arrayOffset());
                            byteBuffer.position(iArrayOffset3 - byteBuffer.arrayOffset());
                            return coderResult;
                        }
                        int i4 = iArrayOffset3;
                        int i5 = iArrayOffset3 + 1;
                        bArrArray[i4] = (byte) (iEncodeChar >> 8);
                        iArrayOffset3 = i5 + 1;
                        bArrArray[i5] = (byte) iEncodeChar;
                    } else {
                        if (iArrayOffset4 - iArrayOffset3 < 1) {
                            CoderResult coderResult2 = CoderResult.OVERFLOW;
                            charBuffer.position(iArrayOffset - charBuffer.arrayOffset());
                            byteBuffer.position(iArrayOffset3 - byteBuffer.arrayOffset());
                            return coderResult2;
                        }
                        int i6 = iArrayOffset3;
                        iArrayOffset3++;
                        bArrArray[i6] = (byte) iEncodeChar;
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

        @Override // sun.nio.cs.ext.DoubleByte.Encoder
        protected CoderResult encodeBufferLoop(CharBuffer charBuffer, ByteBuffer byteBuffer) {
            int iPosition = charBuffer.position();
            while (charBuffer.hasRemaining()) {
                try {
                    int i2 = 1;
                    char c2 = charBuffer.get();
                    int iEncodeChar = encodeChar(c2);
                    if (iEncodeChar == 65533) {
                        if (!Character.isSurrogate(c2)) {
                            CoderResult coderResultUnmappableForLength = CoderResult.unmappableForLength(1);
                            charBuffer.position(iPosition);
                            return coderResultUnmappableForLength;
                        }
                        int i3 = sgp().parse(c2, charBuffer);
                        if (i3 < 0) {
                            CoderResult coderResultError = this.sgp.error();
                            charBuffer.position(iPosition);
                            return coderResultError;
                        }
                        iEncodeChar = encodeSupp(i3);
                        if (iEncodeChar == 65533) {
                            CoderResult coderResultUnmappableForLength2 = CoderResult.unmappableForLength(2);
                            charBuffer.position(iPosition);
                            return coderResultUnmappableForLength2;
                        }
                        i2 = 2;
                    }
                    if (iEncodeChar > 255) {
                        if (byteBuffer.remaining() < 2) {
                            CoderResult coderResult = CoderResult.OVERFLOW;
                            charBuffer.position(iPosition);
                            return coderResult;
                        }
                        byteBuffer.put((byte) (iEncodeChar >> 8));
                        byteBuffer.put((byte) iEncodeChar);
                    } else {
                        if (byteBuffer.remaining() < 1) {
                            CoderResult coderResult2 = CoderResult.OVERFLOW;
                            charBuffer.position(iPosition);
                            return coderResult2;
                        }
                        byteBuffer.put((byte) iEncodeChar);
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

        @Override // sun.nio.cs.ext.DoubleByte.Encoder, java.nio.charset.CharsetEncoder
        protected CoderResult encodeLoop(CharBuffer charBuffer, ByteBuffer byteBuffer) {
            if (charBuffer.hasArray() && byteBuffer.hasArray()) {
                return encodeArrayLoop(charBuffer, byteBuffer);
            }
            return encodeBufferLoop(charBuffer, byteBuffer);
        }

        @Override // sun.nio.cs.ext.DoubleByte.Encoder, java.nio.charset.CharsetEncoder
        protected void implReplaceWith(byte[] bArr) {
            this.repl = bArr;
        }

        @Override // sun.nio.cs.ext.DoubleByte.Encoder, sun.nio.cs.ArrayEncoder
        public int encode(char[] cArr, int i2, int i3, byte[] bArr) {
            int i4 = 0;
            int i5 = i2 + i3;
            while (i2 < i5) {
                int i6 = i2;
                i2++;
                char c2 = cArr[i6];
                int iEncodeChar = encodeChar(c2);
                if (iEncodeChar == 65533) {
                    if (Character.isHighSurrogate(c2) && i2 != i5 && Character.isLowSurrogate(cArr[i2])) {
                        i2++;
                        int iEncodeSupp = encodeSupp(Character.toCodePoint(c2, cArr[i2]));
                        iEncodeChar = iEncodeSupp;
                        if (iEncodeSupp == 65533) {
                        }
                    }
                    int i7 = i4;
                    i4++;
                    bArr[i7] = this.repl[0];
                    if (this.repl.length > 1) {
                        i4++;
                        bArr[i4] = this.repl[1];
                    }
                }
                if (iEncodeChar > 255) {
                    int i8 = i4;
                    int i9 = i4 + 1;
                    bArr[i8] = (byte) (iEncodeChar >> 8);
                    i4 = i9 + 1;
                    bArr[i9] = (byte) iEncodeChar;
                } else {
                    int i10 = i4;
                    i4++;
                    bArr[i10] = (byte) iEncodeChar;
                }
            }
            return i4;
        }

        static {
            Arrays.fill(C2B_UNMAPPABLE, (char) 65533);
        }

        static void initc2b(char[][] cArr, String[] strArr, String str) {
            Arrays.fill(cArr, C2B_UNMAPPABLE);
            for (int i2 = 0; i2 < 256; i2++) {
                String str2 = strArr[i2];
                if (str2 != null) {
                    for (int i3 = 0; i3 < str2.length(); i3++) {
                        char cCharAt = str2.charAt(i3);
                        int i4 = cCharAt >> '\b';
                        if (cArr[i4] == C2B_UNMAPPABLE) {
                            cArr[i4] = new char[256];
                            Arrays.fill(cArr[i4], (char) 65533);
                        }
                        cArr[i4][cCharAt & 255] = (char) ((i2 << 8) | (i3 + 64));
                    }
                }
            }
            if (str != null) {
                char c2 = 57344;
                for (int i5 = 0; i5 < str.length(); i5++) {
                    char cCharAt2 = str.charAt(i5);
                    if (cCharAt2 != 65533) {
                        int i6 = c2 >> '\b';
                        if (cArr[i6] == C2B_UNMAPPABLE) {
                            cArr[i6] = new char[256];
                            Arrays.fill(cArr[i6], (char) 65533);
                        }
                        cArr[i6][c2 & 255] = cCharAt2;
                    }
                    c2 = (char) (c2 + 1);
                }
            }
        }
    }
}
