package sun.nio.cs;

import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.CoderResult;
import java.nio.charset.CodingErrorAction;
import sun.nio.cs.Surrogate;

/* loaded from: rt.jar:sun/nio/cs/CESU_8.class */
class CESU_8 extends Unicode {
    public CESU_8() {
        super("CESU-8", StandardCharsets.aliases_CESU_8);
    }

    @Override // sun.nio.cs.HistoricallyNamedCharset
    public String historicalName() {
        return "CESU8";
    }

    @Override // java.nio.charset.Charset
    public CharsetDecoder newDecoder() {
        return new Decoder(this);
    }

    @Override // java.nio.charset.Charset
    public CharsetEncoder newEncoder() {
        return new Encoder(this);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final void updatePositions(Buffer buffer, int i2, Buffer buffer2, int i3) {
        buffer.position(i2 - buffer.arrayOffset());
        buffer2.position(i3 - buffer2.arrayOffset());
    }

    /* loaded from: rt.jar:sun/nio/cs/CESU_8$Decoder.class */
    private static class Decoder extends CharsetDecoder implements ArrayDecoder {
        static final /* synthetic */ boolean $assertionsDisabled;

        static {
            $assertionsDisabled = !CESU_8.class.desiredAssertionStatus();
        }

        private Decoder(Charset charset) {
            super(charset, 1.0f, 1.0f);
        }

        private static boolean isNotContinuation(int i2) {
            return (i2 & 192) != 128;
        }

        private static boolean isMalformed3(int i2, int i3, int i4) {
            return ((i2 != -32 || (i3 & 224) != 128) && (i3 & 192) == 128 && (i4 & 192) == 128) ? false : true;
        }

        private static boolean isMalformed3_2(int i2, int i3) {
            return (i2 == -32 && (i3 & 224) == 128) || (i3 & 192) != 128;
        }

        private static boolean isMalformed4(int i2, int i3, int i4) {
            return ((i2 & 192) == 128 && (i3 & 192) == 128 && (i4 & 192) == 128) ? false : true;
        }

        private static boolean isMalformed4_2(int i2, int i3) {
            return (i2 == 240 && i3 == 144) || (i3 & 192) != 128;
        }

        private static boolean isMalformed4_3(int i2) {
            return (i2 & 192) != 128;
        }

        private static CoderResult malformedN(ByteBuffer byteBuffer, int i2) {
            switch (i2) {
                case 1:
                case 2:
                    return CoderResult.malformedForLength(1);
                case 3:
                    byte b2 = byteBuffer.get();
                    byte b3 = byteBuffer.get();
                    return CoderResult.malformedForLength(((b2 == -32 && (b3 & 224) == 128) || isNotContinuation(b3)) ? 1 : 2);
                case 4:
                    int i3 = byteBuffer.get() & 255;
                    int i4 = byteBuffer.get() & 255;
                    if (i3 > 244 || ((i3 == 240 && (i4 < 144 || i4 > 191)) || ((i3 == 244 && (i4 & 240) != 128) || isNotContinuation(i4)))) {
                        return CoderResult.malformedForLength(1);
                    }
                    if (isNotContinuation(byteBuffer.get())) {
                        return CoderResult.malformedForLength(2);
                    }
                    return CoderResult.malformedForLength(3);
                default:
                    if ($assertionsDisabled) {
                        return null;
                    }
                    throw new AssertionError();
            }
        }

        private static CoderResult malformed(ByteBuffer byteBuffer, int i2, CharBuffer charBuffer, int i3, int i4) {
            byteBuffer.position(i2 - byteBuffer.arrayOffset());
            CoderResult coderResultMalformedN = malformedN(byteBuffer, i4);
            CESU_8.updatePositions(byteBuffer, i2, charBuffer, i3);
            return coderResultMalformedN;
        }

        private static CoderResult malformed(ByteBuffer byteBuffer, int i2, int i3) {
            byteBuffer.position(i2);
            CoderResult coderResultMalformedN = malformedN(byteBuffer, i3);
            byteBuffer.position(i2);
            return coderResultMalformedN;
        }

        private static CoderResult malformedForLength(ByteBuffer byteBuffer, int i2, CharBuffer charBuffer, int i3, int i4) {
            CESU_8.updatePositions(byteBuffer, i2, charBuffer, i3);
            return CoderResult.malformedForLength(i4);
        }

        private static CoderResult malformedForLength(ByteBuffer byteBuffer, int i2, int i3) {
            byteBuffer.position(i2);
            return CoderResult.malformedForLength(i3);
        }

        private static CoderResult xflow(Buffer buffer, int i2, int i3, Buffer buffer2, int i4, int i5) {
            CESU_8.updatePositions(buffer, i2, buffer2, i4);
            return (i5 == 0 || i3 - i2 < i5) ? CoderResult.UNDERFLOW : CoderResult.OVERFLOW;
        }

        private static CoderResult xflow(Buffer buffer, int i2, int i3) {
            buffer.position(i2);
            return (i3 == 0 || buffer.remaining() < i3) ? CoderResult.UNDERFLOW : CoderResult.OVERFLOW;
        }

        private CoderResult decodeArrayLoop(ByteBuffer byteBuffer, CharBuffer charBuffer) {
            byte[] bArrArray = byteBuffer.array();
            int iArrayOffset = byteBuffer.arrayOffset() + byteBuffer.position();
            int iArrayOffset2 = byteBuffer.arrayOffset() + byteBuffer.limit();
            char[] cArrArray = charBuffer.array();
            int iArrayOffset3 = charBuffer.arrayOffset() + charBuffer.position();
            int iArrayOffset4 = charBuffer.arrayOffset() + charBuffer.limit();
            int iMin = iArrayOffset3 + Math.min(iArrayOffset2 - iArrayOffset, iArrayOffset4 - iArrayOffset3);
            while (iArrayOffset3 < iMin && bArrArray[iArrayOffset] >= 0) {
                int i2 = iArrayOffset3;
                iArrayOffset3++;
                int i3 = iArrayOffset;
                iArrayOffset++;
                cArrArray[i2] = (char) bArrArray[i3];
            }
            while (iArrayOffset < iArrayOffset2) {
                byte b2 = bArrArray[iArrayOffset];
                if (b2 >= 0) {
                    if (iArrayOffset3 >= iArrayOffset4) {
                        return xflow(byteBuffer, iArrayOffset, iArrayOffset2, charBuffer, iArrayOffset3, 1);
                    }
                    int i4 = iArrayOffset3;
                    iArrayOffset3++;
                    cArrArray[i4] = (char) b2;
                    iArrayOffset++;
                } else if ((b2 >> 5) != -2 || (b2 & 30) == 0) {
                    if ((b2 >> 4) == -2) {
                        int i5 = iArrayOffset2 - iArrayOffset;
                        if (i5 < 3 || iArrayOffset3 >= iArrayOffset4) {
                            if (i5 > 1 && isMalformed3_2(b2, bArrArray[iArrayOffset + 1])) {
                                return malformedForLength(byteBuffer, iArrayOffset, charBuffer, iArrayOffset3, 1);
                            }
                            return xflow(byteBuffer, iArrayOffset, iArrayOffset2, charBuffer, iArrayOffset3, 3);
                        }
                        byte b3 = bArrArray[iArrayOffset + 1];
                        byte b4 = bArrArray[iArrayOffset + 2];
                        if (isMalformed3(b2, b3, b4)) {
                            return malformed(byteBuffer, iArrayOffset, charBuffer, iArrayOffset3, 3);
                        }
                        int i6 = iArrayOffset3;
                        iArrayOffset3++;
                        cArrArray[i6] = (char) (((b2 << 12) ^ (b3 << 6)) ^ (b4 ^ (-123008)));
                        iArrayOffset += 3;
                    } else {
                        return malformed(byteBuffer, iArrayOffset, charBuffer, iArrayOffset3, 1);
                    }
                } else {
                    if (iArrayOffset2 - iArrayOffset < 2 || iArrayOffset3 >= iArrayOffset4) {
                        return xflow(byteBuffer, iArrayOffset, iArrayOffset2, charBuffer, iArrayOffset3, 2);
                    }
                    byte b5 = bArrArray[iArrayOffset + 1];
                    if (isNotContinuation(b5)) {
                        return malformedForLength(byteBuffer, iArrayOffset, charBuffer, iArrayOffset3, 1);
                    }
                    int i7 = iArrayOffset3;
                    iArrayOffset3++;
                    cArrArray[i7] = (char) (((b2 << 6) ^ b5) ^ 3968);
                    iArrayOffset += 2;
                }
            }
            return xflow(byteBuffer, iArrayOffset, iArrayOffset2, charBuffer, iArrayOffset3, 0);
        }

        private CoderResult decodeBufferLoop(ByteBuffer byteBuffer, CharBuffer charBuffer) {
            int iPosition = byteBuffer.position();
            int iLimit = byteBuffer.limit();
            while (iPosition < iLimit) {
                byte b2 = byteBuffer.get();
                if (b2 >= 0) {
                    if (charBuffer.remaining() < 1) {
                        return xflow(byteBuffer, iPosition, 1);
                    }
                    charBuffer.put((char) b2);
                    iPosition++;
                } else if ((b2 >> 5) != -2 || (b2 & 30) == 0) {
                    if ((b2 >> 4) == -2) {
                        int i2 = iLimit - iPosition;
                        if (i2 < 3 || charBuffer.remaining() < 1) {
                            if (i2 > 1 && isMalformed3_2(b2, byteBuffer.get())) {
                                return malformedForLength(byteBuffer, iPosition, 1);
                            }
                            return xflow(byteBuffer, iPosition, 3);
                        }
                        byte b3 = byteBuffer.get();
                        byte b4 = byteBuffer.get();
                        if (isMalformed3(b2, b3, b4)) {
                            return malformed(byteBuffer, iPosition, 3);
                        }
                        charBuffer.put((char) (((b2 << 12) ^ (b3 << 6)) ^ (b4 ^ (-123008))));
                        iPosition += 3;
                    } else {
                        return malformed(byteBuffer, iPosition, 1);
                    }
                } else {
                    if (iLimit - iPosition < 2 || charBuffer.remaining() < 1) {
                        return xflow(byteBuffer, iPosition, 2);
                    }
                    byte b5 = byteBuffer.get();
                    if (isNotContinuation(b5)) {
                        return malformedForLength(byteBuffer, iPosition, 1);
                    }
                    charBuffer.put((char) (((b2 << 6) ^ b5) ^ 3968));
                    iPosition += 2;
                }
            }
            return xflow(byteBuffer, iPosition, 0);
        }

        @Override // java.nio.charset.CharsetDecoder
        protected CoderResult decodeLoop(ByteBuffer byteBuffer, CharBuffer charBuffer) {
            if (byteBuffer.hasArray() && charBuffer.hasArray()) {
                return decodeArrayLoop(byteBuffer, charBuffer);
            }
            return decodeBufferLoop(byteBuffer, charBuffer);
        }

        private static ByteBuffer getByteBuffer(ByteBuffer byteBuffer, byte[] bArr, int i2) {
            if (byteBuffer == null) {
                byteBuffer = ByteBuffer.wrap(bArr);
            }
            byteBuffer.position(i2);
            return byteBuffer;
        }

        @Override // sun.nio.cs.ArrayDecoder
        public int decode(byte[] bArr, int i2, int i3, char[] cArr) {
            int i4 = i2 + i3;
            int i5 = 0;
            int iMin = Math.min(i3, cArr.length);
            ByteBuffer byteBuffer = null;
            while (i5 < iMin && bArr[i2] >= 0) {
                int i6 = i5;
                i5++;
                int i7 = i2;
                i2++;
                cArr[i6] = (char) bArr[i7];
            }
            while (i2 < i4) {
                int i8 = i2;
                i2++;
                byte b2 = bArr[i8];
                if (b2 >= 0) {
                    int i9 = i5;
                    i5++;
                    cArr[i9] = (char) b2;
                } else if ((b2 >> 5) == -2 && (b2 & 30) != 0) {
                    if (i2 < i4) {
                        i2++;
                        byte b3 = bArr[i2];
                        if (isNotContinuation(b3)) {
                            if (malformedInputAction() != CodingErrorAction.REPLACE) {
                                return -1;
                            }
                            int i10 = i5;
                            i5++;
                            cArr[i10] = replacement().charAt(0);
                            i2--;
                        } else {
                            int i11 = i5;
                            i5++;
                            cArr[i11] = (char) (((b2 << 6) ^ b3) ^ 3968);
                        }
                    } else {
                        if (malformedInputAction() != CodingErrorAction.REPLACE) {
                            return -1;
                        }
                        int i12 = i5;
                        int i13 = i5 + 1;
                        cArr[i12] = replacement().charAt(0);
                        return i13;
                    }
                } else if ((b2 >> 4) == -2) {
                    if (i2 + 1 < i4) {
                        int i14 = i2 + 1;
                        byte b4 = bArr[i2];
                        i2 = i14 + 1;
                        byte b5 = bArr[i14];
                        if (isMalformed3(b2, b4, b5)) {
                            if (malformedInputAction() != CodingErrorAction.REPLACE) {
                                return -1;
                            }
                            int i15 = i5;
                            i5++;
                            cArr[i15] = replacement().charAt(0);
                            int i16 = i2 - 3;
                            byteBuffer = getByteBuffer(byteBuffer, bArr, i16);
                            i2 = i16 + malformedN(byteBuffer, 3).length();
                        } else {
                            int i17 = i5;
                            i5++;
                            cArr[i17] = (char) (((b2 << 12) ^ (b4 << 6)) ^ (b5 ^ (-123008)));
                        }
                    } else {
                        if (malformedInputAction() != CodingErrorAction.REPLACE) {
                            return -1;
                        }
                        if (i2 < i4 && isMalformed3_2(b2, bArr[i2])) {
                            int i18 = i5;
                            i5++;
                            cArr[i18] = replacement().charAt(0);
                        } else {
                            int i19 = i5;
                            int i20 = i5 + 1;
                            cArr[i19] = replacement().charAt(0);
                            return i20;
                        }
                    }
                } else {
                    if (malformedInputAction() != CodingErrorAction.REPLACE) {
                        return -1;
                    }
                    int i21 = i5;
                    i5++;
                    cArr[i21] = replacement().charAt(0);
                }
            }
            return i5;
        }
    }

    /* loaded from: rt.jar:sun/nio/cs/CESU_8$Encoder.class */
    private static class Encoder extends CharsetEncoder implements ArrayEncoder {
        private Surrogate.Parser sgp;
        private char[] c2;

        private Encoder(Charset charset) {
            super(charset, 1.1f, 3.0f);
        }

        @Override // java.nio.charset.CharsetEncoder
        public boolean canEncode(char c2) {
            return !Character.isSurrogate(c2);
        }

        @Override // java.nio.charset.CharsetEncoder
        public boolean isLegalReplacement(byte[] bArr) {
            return (bArr.length == 1 && bArr[0] >= 0) || super.isLegalReplacement(bArr);
        }

        private static CoderResult overflow(CharBuffer charBuffer, int i2, ByteBuffer byteBuffer, int i3) {
            CESU_8.updatePositions(charBuffer, i2, byteBuffer, i3);
            return CoderResult.OVERFLOW;
        }

        private static CoderResult overflow(CharBuffer charBuffer, int i2) {
            charBuffer.position(i2);
            return CoderResult.OVERFLOW;
        }

        private static void to3Bytes(byte[] bArr, int i2, char c2) {
            bArr[i2] = (byte) (224 | (c2 >> '\f'));
            bArr[i2 + 1] = (byte) (128 | ((c2 >> 6) & 63));
            bArr[i2 + 2] = (byte) (128 | (c2 & '?'));
        }

        private static void to3Bytes(ByteBuffer byteBuffer, char c2) {
            byteBuffer.put((byte) (224 | (c2 >> '\f')));
            byteBuffer.put((byte) (128 | ((c2 >> 6) & 63)));
            byteBuffer.put((byte) (128 | (c2 & '?')));
        }

        private CoderResult encodeArrayLoop(CharBuffer charBuffer, ByteBuffer byteBuffer) {
            char[] cArrArray = charBuffer.array();
            int iArrayOffset = charBuffer.arrayOffset() + charBuffer.position();
            int iArrayOffset2 = charBuffer.arrayOffset() + charBuffer.limit();
            byte[] bArrArray = byteBuffer.array();
            int iArrayOffset3 = byteBuffer.arrayOffset() + byteBuffer.position();
            int iArrayOffset4 = byteBuffer.arrayOffset() + byteBuffer.limit();
            int iMin = iArrayOffset3 + Math.min(iArrayOffset2 - iArrayOffset, iArrayOffset4 - iArrayOffset3);
            while (iArrayOffset3 < iMin && cArrArray[iArrayOffset] < 128) {
                int i2 = iArrayOffset3;
                iArrayOffset3++;
                int i3 = iArrayOffset;
                iArrayOffset++;
                bArrArray[i2] = (byte) cArrArray[i3];
            }
            while (iArrayOffset < iArrayOffset2) {
                char c2 = cArrArray[iArrayOffset];
                if (c2 < 128) {
                    if (iArrayOffset3 >= iArrayOffset4) {
                        return overflow(charBuffer, iArrayOffset, byteBuffer, iArrayOffset3);
                    }
                    int i4 = iArrayOffset3;
                    iArrayOffset3++;
                    bArrArray[i4] = (byte) c2;
                } else if (c2 < 2048) {
                    if (iArrayOffset4 - iArrayOffset3 < 2) {
                        return overflow(charBuffer, iArrayOffset, byteBuffer, iArrayOffset3);
                    }
                    int i5 = iArrayOffset3;
                    int i6 = iArrayOffset3 + 1;
                    bArrArray[i5] = (byte) (192 | (c2 >> 6));
                    iArrayOffset3 = i6 + 1;
                    bArrArray[i6] = (byte) (128 | (c2 & '?'));
                } else if (Character.isSurrogate(c2)) {
                    if (this.sgp == null) {
                        this.sgp = new Surrogate.Parser();
                    }
                    int i7 = this.sgp.parse(c2, cArrArray, iArrayOffset, iArrayOffset2);
                    if (i7 < 0) {
                        CESU_8.updatePositions(charBuffer, iArrayOffset, byteBuffer, iArrayOffset3);
                        return this.sgp.error();
                    }
                    if (iArrayOffset4 - iArrayOffset3 < 6) {
                        return overflow(charBuffer, iArrayOffset, byteBuffer, iArrayOffset3);
                    }
                    to3Bytes(bArrArray, iArrayOffset3, Character.highSurrogate(i7));
                    int i8 = iArrayOffset3 + 3;
                    to3Bytes(bArrArray, i8, Character.lowSurrogate(i7));
                    iArrayOffset3 = i8 + 3;
                    iArrayOffset++;
                } else {
                    if (iArrayOffset4 - iArrayOffset3 < 3) {
                        return overflow(charBuffer, iArrayOffset, byteBuffer, iArrayOffset3);
                    }
                    to3Bytes(bArrArray, iArrayOffset3, c2);
                    iArrayOffset3 += 3;
                }
                iArrayOffset++;
            }
            CESU_8.updatePositions(charBuffer, iArrayOffset, byteBuffer, iArrayOffset3);
            return CoderResult.UNDERFLOW;
        }

        private CoderResult encodeBufferLoop(CharBuffer charBuffer, ByteBuffer byteBuffer) {
            int iPosition = charBuffer.position();
            while (charBuffer.hasRemaining()) {
                char c2 = charBuffer.get();
                if (c2 < 128) {
                    if (!byteBuffer.hasRemaining()) {
                        return overflow(charBuffer, iPosition);
                    }
                    byteBuffer.put((byte) c2);
                } else if (c2 < 2048) {
                    if (byteBuffer.remaining() < 2) {
                        return overflow(charBuffer, iPosition);
                    }
                    byteBuffer.put((byte) (192 | (c2 >> 6)));
                    byteBuffer.put((byte) (128 | (c2 & '?')));
                } else if (Character.isSurrogate(c2)) {
                    if (this.sgp == null) {
                        this.sgp = new Surrogate.Parser();
                    }
                    int i2 = this.sgp.parse(c2, charBuffer);
                    if (i2 < 0) {
                        charBuffer.position(iPosition);
                        return this.sgp.error();
                    }
                    if (byteBuffer.remaining() < 6) {
                        return overflow(charBuffer, iPosition);
                    }
                    to3Bytes(byteBuffer, Character.highSurrogate(i2));
                    to3Bytes(byteBuffer, Character.lowSurrogate(i2));
                    iPosition++;
                } else {
                    if (byteBuffer.remaining() < 3) {
                        return overflow(charBuffer, iPosition);
                    }
                    to3Bytes(byteBuffer, c2);
                }
                iPosition++;
            }
            charBuffer.position(iPosition);
            return CoderResult.UNDERFLOW;
        }

        @Override // java.nio.charset.CharsetEncoder
        protected final CoderResult encodeLoop(CharBuffer charBuffer, ByteBuffer byteBuffer) {
            if (charBuffer.hasArray() && byteBuffer.hasArray()) {
                return encodeArrayLoop(charBuffer, byteBuffer);
            }
            return encodeBufferLoop(charBuffer, byteBuffer);
        }

        @Override // sun.nio.cs.ArrayEncoder
        public int encode(char[] cArr, int i2, int i3, byte[] bArr) {
            int i4 = i2 + i3;
            int i5 = 0;
            int iMin = 0 + Math.min(i3, bArr.length);
            while (i5 < iMin && cArr[i2] < 128) {
                int i6 = i5;
                i5++;
                int i7 = i2;
                i2++;
                bArr[i6] = (byte) cArr[i7];
            }
            while (i2 < i4) {
                int i8 = i2;
                i2++;
                char c2 = cArr[i8];
                if (c2 < 128) {
                    int i9 = i5;
                    i5++;
                    bArr[i9] = (byte) c2;
                } else if (c2 < 2048) {
                    int i10 = i5;
                    int i11 = i5 + 1;
                    bArr[i10] = (byte) (192 | (c2 >> 6));
                    i5 = i11 + 1;
                    bArr[i11] = (byte) (128 | (c2 & '?'));
                } else if (Character.isSurrogate(c2)) {
                    if (this.sgp == null) {
                        this.sgp = new Surrogate.Parser();
                    }
                    int i12 = this.sgp.parse(c2, cArr, i2 - 1, i4);
                    if (i12 < 0) {
                        if (malformedInputAction() != CodingErrorAction.REPLACE) {
                            return -1;
                        }
                        int i13 = i5;
                        i5++;
                        bArr[i13] = replacement()[0];
                    } else {
                        to3Bytes(bArr, i5, Character.highSurrogate(i12));
                        int i14 = i5 + 3;
                        to3Bytes(bArr, i14, Character.lowSurrogate(i12));
                        i5 = i14 + 3;
                        i2++;
                    }
                } else {
                    to3Bytes(bArr, i5, c2);
                    i5 += 3;
                }
            }
            return i5;
        }
    }
}
