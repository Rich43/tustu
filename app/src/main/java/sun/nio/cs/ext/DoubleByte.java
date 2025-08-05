package sun.nio.cs.ext;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.CoderResult;
import java.util.Arrays;
import sun.nio.cs.ArrayDecoder;
import sun.nio.cs.ArrayEncoder;
import sun.nio.cs.Surrogate;

/* loaded from: charsets.jar:sun/nio/cs/ext/DoubleByte.class */
public class DoubleByte {
    public static final char[] B2C_UNMAPPABLE = new char[256];

    static {
        Arrays.fill(B2C_UNMAPPABLE, (char) 65533);
    }

    /* loaded from: charsets.jar:sun/nio/cs/ext/DoubleByte$Decoder.class */
    public static class Decoder extends CharsetDecoder implements DelegatableDecoder, ArrayDecoder {
        final char[][] b2c;
        final char[] b2cSB;
        final int b2Min;
        final int b2Max;

        protected CoderResult crMalformedOrUnderFlow(int i2) {
            return CoderResult.UNDERFLOW;
        }

        protected CoderResult crMalformedOrUnmappable(int i2, int i3) {
            if (this.b2c[i2] == DoubleByte.B2C_UNMAPPABLE || this.b2c[i3] != DoubleByte.B2C_UNMAPPABLE || decodeSingle(i3) != 65533) {
                return CoderResult.malformedForLength(1);
            }
            return CoderResult.unmappableForLength(2);
        }

        Decoder(Charset charset, float f2, float f3, char[][] cArr, char[] cArr2, int i2, int i3) {
            super(charset, f2, f3);
            this.b2c = cArr;
            this.b2cSB = cArr2;
            this.b2Min = i2;
            this.b2Max = i3;
        }

        Decoder(Charset charset, char[][] cArr, char[] cArr2, int i2, int i3) {
            this(charset, 0.5f, 1.0f, cArr, cArr2, i2, i3);
        }

        protected CoderResult decodeArrayLoop(ByteBuffer byteBuffer, CharBuffer charBuffer) {
            byte[] bArrArray = byteBuffer.array();
            int iArrayOffset = byteBuffer.arrayOffset() + byteBuffer.position();
            int iArrayOffset2 = byteBuffer.arrayOffset() + byteBuffer.limit();
            char[] cArrArray = charBuffer.array();
            int iArrayOffset3 = charBuffer.arrayOffset() + charBuffer.position();
            int iArrayOffset4 = charBuffer.arrayOffset() + charBuffer.limit();
            while (iArrayOffset < iArrayOffset2 && iArrayOffset3 < iArrayOffset4) {
                try {
                    int i2 = 1;
                    int i3 = bArrArray[iArrayOffset] & 255;
                    char c2 = this.b2cSB[i3];
                    if (c2 == 65533) {
                        if (iArrayOffset2 - iArrayOffset < 2) {
                            CoderResult coderResultCrMalformedOrUnderFlow = crMalformedOrUnderFlow(i3);
                            byteBuffer.position(iArrayOffset - byteBuffer.arrayOffset());
                            charBuffer.position(iArrayOffset3 - charBuffer.arrayOffset());
                            return coderResultCrMalformedOrUnderFlow;
                        }
                        int i4 = bArrArray[iArrayOffset + 1] & 255;
                        if (i4 >= this.b2Min && i4 <= this.b2Max) {
                            char c3 = this.b2c[i3][i4 - this.b2Min];
                            c2 = c3;
                            if (c3 != 65533) {
                                i2 = 1 + 1;
                            }
                        }
                        CoderResult coderResultCrMalformedOrUnmappable = crMalformedOrUnmappable(i3, i4);
                        byteBuffer.position(iArrayOffset - byteBuffer.arrayOffset());
                        charBuffer.position(iArrayOffset3 - charBuffer.arrayOffset());
                        return coderResultCrMalformedOrUnmappable;
                    }
                    int i5 = iArrayOffset3;
                    iArrayOffset3++;
                    cArrArray[i5] = c2;
                    iArrayOffset += i2;
                } finally {
                    byteBuffer.position(iArrayOffset - byteBuffer.arrayOffset());
                    charBuffer.position(iArrayOffset3 - charBuffer.arrayOffset());
                }
            }
            return iArrayOffset >= iArrayOffset2 ? CoderResult.UNDERFLOW : CoderResult.OVERFLOW;
        }

        protected CoderResult decodeBufferLoop(ByteBuffer byteBuffer, CharBuffer charBuffer) {
            int iPosition = byteBuffer.position();
            while (byteBuffer.hasRemaining() && charBuffer.hasRemaining()) {
                try {
                    int i2 = byteBuffer.get() & 255;
                    char c2 = this.b2cSB[i2];
                    int i3 = 1;
                    if (c2 == 65533) {
                        if (byteBuffer.remaining() < 1) {
                            CoderResult coderResultCrMalformedOrUnderFlow = crMalformedOrUnderFlow(i2);
                            byteBuffer.position(iPosition);
                            return coderResultCrMalformedOrUnderFlow;
                        }
                        int i4 = byteBuffer.get() & 255;
                        if (i4 >= this.b2Min && i4 <= this.b2Max) {
                            char c3 = this.b2c[i2][i4 - this.b2Min];
                            c2 = c3;
                            if (c3 != 65533) {
                                i3 = 1 + 1;
                            }
                        }
                        CoderResult coderResultCrMalformedOrUnmappable = crMalformedOrUnmappable(i2, i4);
                        byteBuffer.position(iPosition);
                        return coderResultCrMalformedOrUnmappable;
                    }
                    charBuffer.put(c2);
                    iPosition += i3;
                } finally {
                    byteBuffer.position(iPosition);
                }
            }
            return byteBuffer.hasRemaining() ? CoderResult.OVERFLOW : CoderResult.UNDERFLOW;
        }

        @Override // java.nio.charset.CharsetDecoder
        public CoderResult decodeLoop(ByteBuffer byteBuffer, CharBuffer charBuffer) {
            if (byteBuffer.hasArray() && charBuffer.hasArray()) {
                return decodeArrayLoop(byteBuffer, charBuffer);
            }
            return decodeBufferLoop(byteBuffer, charBuffer);
        }

        /* JADX WARN: Removed duplicated region for block: B:15:0x006f A[PHI: r13
  0x006f: PHI (r13v4 char) = (r13v0 char), (r13v0 char), (r13v5 char) binds: [B:10:0x004c, B:12:0x0055, B:14:0x006c] A[DONT_GENERATE, DONT_INLINE]] */
        @Override // sun.nio.cs.ArrayDecoder
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct code enable 'Show inconsistent code' option in preferences
        */
        public int decode(byte[] r5, int r6, int r7, char[] r8) {
            /*
                r4 = this;
                r0 = 0
                r9 = r0
                r0 = r6
                r1 = r7
                int r0 = r0 + r1
                r10 = r0
                r0 = r4
                java.lang.String r0 = r0.replacement()
                r1 = 0
                char r0 = r0.charAt(r1)
                r11 = r0
            L12:
                r0 = r6
                r1 = r10
                if (r0 >= r1) goto Laf
                r0 = r5
                r1 = r6
                int r6 = r6 + 1
                r0 = r0[r1]
                r1 = 255(0xff, float:3.57E-43)
                r0 = r0 & r1
                r12 = r0
                r0 = r4
                char[] r0 = r0.b2cSB
                r1 = r12
                char r0 = r0[r1]
                r13 = r0
                r0 = r13
                r1 = 65533(0xfffd, float:9.1831E-41)
                if (r0 != r1) goto La2
                r0 = r6
                r1 = r10
                if (r0 >= r1) goto L97
                r0 = r5
                r1 = r6
                int r6 = r6 + 1
                r0 = r0[r1]
                r1 = 255(0xff, float:3.57E-43)
                r0 = r0 & r1
                r14 = r0
                r0 = r14
                r1 = r4
                int r1 = r1.b2Min
                if (r0 < r1) goto L6f
                r0 = r14
                r1 = r4
                int r1 = r1.b2Max
                if (r0 > r1) goto L6f
                r0 = r4
                char[][] r0 = r0.b2c
                r1 = r12
                r0 = r0[r1]
                r1 = r14
                r2 = r4
                int r2 = r2.b2Min
                int r1 = r1 - r2
                char r0 = r0[r1]
                r1 = r0
                r13 = r1
                r1 = 65533(0xfffd, float:9.1831E-41)
                if (r0 != r1) goto L97
            L6f:
                r0 = r4
                char[][] r0 = r0.b2c
                r1 = r12
                r0 = r0[r1]
                char[] r1 = sun.nio.cs.ext.DoubleByte.B2C_UNMAPPABLE
                if (r0 == r1) goto L94
                r0 = r4
                char[][] r0 = r0.b2c
                r1 = r14
                r0 = r0[r1]
                char[] r1 = sun.nio.cs.ext.DoubleByte.B2C_UNMAPPABLE
                if (r0 != r1) goto L94
                r0 = r4
                r1 = r14
                char r0 = r0.decodeSingle(r1)
                r1 = 65533(0xfffd, float:9.1831E-41)
                if (r0 == r1) goto L97
            L94:
                int r6 = r6 + (-1)
            L97:
                r0 = r13
                r1 = 65533(0xfffd, float:9.1831E-41)
                if (r0 != r1) goto La2
                r0 = r11
                r13 = r0
            La2:
                r0 = r8
                r1 = r9
                int r9 = r9 + 1
                r2 = r13
                r0[r1] = r2
                goto L12
            Laf:
                r0 = r9
                return r0
            */
            throw new UnsupportedOperationException("Method not decompiled: sun.nio.cs.ext.DoubleByte.Decoder.decode(byte[], int, int, char[]):int");
        }

        @Override // java.nio.charset.CharsetDecoder
        public void implReset() {
            super.implReset();
        }

        @Override // java.nio.charset.CharsetDecoder
        public CoderResult implFlush(CharBuffer charBuffer) {
            return super.implFlush(charBuffer);
        }

        public char decodeSingle(int i2) {
            return this.b2cSB[i2];
        }

        public char decodeDouble(int i2, int i3) {
            if (i2 < 0 || i2 > this.b2c.length || i3 < this.b2Min || i3 > this.b2Max) {
                return (char) 65533;
            }
            return this.b2c[i2][i3 - this.b2Min];
        }
    }

    /* loaded from: charsets.jar:sun/nio/cs/ext/DoubleByte$Decoder_EBCDIC.class */
    public static class Decoder_EBCDIC extends Decoder {
        private static final int SBCS = 0;
        private static final int DBCS = 1;
        private static final int SO = 14;
        private static final int SI = 15;
        private int currentState;

        Decoder_EBCDIC(Charset charset, char[][] cArr, char[] cArr2, int i2, int i3) {
            super(charset, cArr, cArr2, i2, i3);
        }

        @Override // sun.nio.cs.ext.DoubleByte.Decoder, java.nio.charset.CharsetDecoder
        public void implReset() {
            this.currentState = 0;
        }

        private static boolean isDoubleByte(int i2, int i3) {
            return (65 <= i2 && i2 <= 254 && 65 <= i3 && i3 <= 254) || (i2 == 64 && i3 == 64);
        }

        @Override // sun.nio.cs.ext.DoubleByte.Decoder
        protected CoderResult decodeArrayLoop(ByteBuffer byteBuffer, CharBuffer charBuffer) {
            char c2;
            byte[] bArrArray = byteBuffer.array();
            int iArrayOffset = byteBuffer.arrayOffset() + byteBuffer.position();
            int iArrayOffset2 = byteBuffer.arrayOffset() + byteBuffer.limit();
            char[] cArrArray = charBuffer.array();
            int iArrayOffset3 = charBuffer.arrayOffset() + charBuffer.position();
            int iArrayOffset4 = charBuffer.arrayOffset() + charBuffer.limit();
            while (iArrayOffset < iArrayOffset2) {
                try {
                    int i2 = bArrArray[iArrayOffset] & 255;
                    int i3 = 1;
                    if (i2 == 14) {
                        if (this.currentState != 0) {
                            CoderResult coderResultMalformedForLength = CoderResult.malformedForLength(1);
                            byteBuffer.position(iArrayOffset - byteBuffer.arrayOffset());
                            charBuffer.position(iArrayOffset3 - charBuffer.arrayOffset());
                            return coderResultMalformedForLength;
                        }
                        this.currentState = 1;
                    } else if (i2 != 15) {
                        if (this.currentState != 0) {
                            if (iArrayOffset2 - iArrayOffset < 2) {
                                CoderResult coderResult = CoderResult.UNDERFLOW;
                                byteBuffer.position(iArrayOffset - byteBuffer.arrayOffset());
                                charBuffer.position(iArrayOffset3 - charBuffer.arrayOffset());
                                return coderResult;
                            }
                            int i4 = bArrArray[iArrayOffset + 1] & 255;
                            if (i4 >= this.b2Min && i4 <= this.b2Max) {
                                char c3 = this.b2c[i2][i4 - this.b2Min];
                                c2 = c3;
                                if (c3 != 65533) {
                                    i3 = 1 + 1;
                                }
                            }
                            if (isDoubleByte(i2, i4)) {
                                CoderResult coderResultUnmappableForLength = CoderResult.unmappableForLength(2);
                                byteBuffer.position(iArrayOffset - byteBuffer.arrayOffset());
                                charBuffer.position(iArrayOffset3 - charBuffer.arrayOffset());
                                return coderResultUnmappableForLength;
                            }
                            CoderResult coderResultMalformedForLength2 = CoderResult.malformedForLength(2);
                            byteBuffer.position(iArrayOffset - byteBuffer.arrayOffset());
                            charBuffer.position(iArrayOffset3 - charBuffer.arrayOffset());
                            return coderResultMalformedForLength2;
                        }
                        c2 = this.b2cSB[i2];
                        if (c2 == 65533) {
                            CoderResult coderResultUnmappableForLength2 = CoderResult.unmappableForLength(1);
                            byteBuffer.position(iArrayOffset - byteBuffer.arrayOffset());
                            charBuffer.position(iArrayOffset3 - charBuffer.arrayOffset());
                            return coderResultUnmappableForLength2;
                        }
                        if (iArrayOffset4 - iArrayOffset3 < 1) {
                            CoderResult coderResult2 = CoderResult.OVERFLOW;
                            byteBuffer.position(iArrayOffset - byteBuffer.arrayOffset());
                            charBuffer.position(iArrayOffset3 - charBuffer.arrayOffset());
                            return coderResult2;
                        }
                        int i5 = iArrayOffset3;
                        iArrayOffset3++;
                        cArrArray[i5] = c2;
                    } else {
                        if (this.currentState != 1) {
                            CoderResult coderResultMalformedForLength3 = CoderResult.malformedForLength(1);
                            byteBuffer.position(iArrayOffset - byteBuffer.arrayOffset());
                            charBuffer.position(iArrayOffset3 - charBuffer.arrayOffset());
                            return coderResultMalformedForLength3;
                        }
                        this.currentState = 0;
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
            char c2;
            int iPosition = byteBuffer.position();
            while (byteBuffer.hasRemaining()) {
                try {
                    int i2 = byteBuffer.get() & 255;
                    int i3 = 1;
                    if (i2 == 14) {
                        if (this.currentState != 0) {
                            CoderResult coderResultMalformedForLength = CoderResult.malformedForLength(1);
                            byteBuffer.position(iPosition);
                            return coderResultMalformedForLength;
                        }
                        this.currentState = 1;
                    } else if (i2 != 15) {
                        if (this.currentState != 0) {
                            if (byteBuffer.remaining() < 1) {
                                CoderResult coderResult = CoderResult.UNDERFLOW;
                                byteBuffer.position(iPosition);
                                return coderResult;
                            }
                            int i4 = byteBuffer.get() & 255;
                            if (i4 >= this.b2Min && i4 <= this.b2Max) {
                                char c3 = this.b2c[i2][i4 - this.b2Min];
                                c2 = c3;
                                if (c3 != 65533) {
                                    i3 = 1 + 1;
                                }
                            }
                            if (isDoubleByte(i2, i4)) {
                                CoderResult coderResultUnmappableForLength = CoderResult.unmappableForLength(2);
                                byteBuffer.position(iPosition);
                                return coderResultUnmappableForLength;
                            }
                            CoderResult coderResultMalformedForLength2 = CoderResult.malformedForLength(2);
                            byteBuffer.position(iPosition);
                            return coderResultMalformedForLength2;
                        }
                        c2 = this.b2cSB[i2];
                        if (c2 == 65533) {
                            CoderResult coderResultUnmappableForLength2 = CoderResult.unmappableForLength(1);
                            byteBuffer.position(iPosition);
                            return coderResultUnmappableForLength2;
                        }
                        if (charBuffer.remaining() < 1) {
                            CoderResult coderResult2 = CoderResult.OVERFLOW;
                            byteBuffer.position(iPosition);
                            return coderResult2;
                        }
                        charBuffer.put(c2);
                    } else {
                        if (this.currentState != 1) {
                            CoderResult coderResultMalformedForLength3 = CoderResult.malformedForLength(1);
                            byteBuffer.position(iPosition);
                            return coderResultMalformedForLength3;
                        }
                        this.currentState = 0;
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

        /* JADX WARN: Removed duplicated region for block: B:31:0x00d4  */
        @Override // sun.nio.cs.ext.DoubleByte.Decoder, sun.nio.cs.ArrayDecoder
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct code enable 'Show inconsistent code' option in preferences
        */
        public int decode(byte[] r5, int r6, int r7, char[] r8) {
            /*
                Method dump skipped, instructions count: 232
                To view this dump change 'Code comments level' option to 'DEBUG'
            */
            throw new UnsupportedOperationException("Method not decompiled: sun.nio.cs.ext.DoubleByte.Decoder_EBCDIC.decode(byte[], int, int, char[]):int");
        }
    }

    /* loaded from: charsets.jar:sun/nio/cs/ext/DoubleByte$Decoder_DBCSONLY.class */
    public static class Decoder_DBCSONLY extends Decoder {
        static final char[] b2cSB_UNMAPPABLE = new char[256];

        static {
            Arrays.fill(b2cSB_UNMAPPABLE, (char) 65533);
        }

        Decoder_DBCSONLY(Charset charset, char[][] cArr, char[] cArr2, int i2, int i3) {
            super(charset, 0.5f, 1.0f, cArr, b2cSB_UNMAPPABLE, i2, i3);
        }
    }

    /* loaded from: charsets.jar:sun/nio/cs/ext/DoubleByte$Decoder_EUC_SIM.class */
    public static class Decoder_EUC_SIM extends Decoder {
        private final int SS2 = 142;
        private final int SS3 = 143;

        Decoder_EUC_SIM(Charset charset, char[][] cArr, char[] cArr2, int i2, int i3) {
            super(charset, cArr, cArr2, i2, i3);
            this.SS2 = 142;
            this.SS3 = 143;
        }

        @Override // sun.nio.cs.ext.DoubleByte.Decoder
        protected CoderResult crMalformedOrUnderFlow(int i2) {
            if (i2 == 142 || i2 == 143) {
                return CoderResult.malformedForLength(1);
            }
            return CoderResult.UNDERFLOW;
        }

        @Override // sun.nio.cs.ext.DoubleByte.Decoder
        protected CoderResult crMalformedOrUnmappable(int i2, int i3) {
            if (i2 == 142 || i2 == 143) {
                return CoderResult.malformedForLength(1);
            }
            return CoderResult.unmappableForLength(2);
        }

        /* JADX WARN: Removed duplicated region for block: B:15:0x006f  */
        @Override // sun.nio.cs.ext.DoubleByte.Decoder, sun.nio.cs.ArrayDecoder
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct code enable 'Show inconsistent code' option in preferences
        */
        public int decode(byte[] r5, int r6, int r7, char[] r8) {
            /*
                r4 = this;
                r0 = 0
                r9 = r0
                r0 = r6
                r1 = r7
                int r0 = r0 + r1
                r10 = r0
                r0 = r4
                java.lang.String r0 = r0.replacement()
                r1 = 0
                char r0 = r0.charAt(r1)
                r11 = r0
            L12:
                r0 = r6
                r1 = r10
                if (r0 >= r1) goto L9a
                r0 = r5
                r1 = r6
                int r6 = r6 + 1
                r0 = r0[r1]
                r1 = 255(0xff, float:3.57E-43)
                r0 = r0 & r1
                r12 = r0
                r0 = r4
                char[] r0 = r0.b2cSB
                r1 = r12
                char r0 = r0[r1]
                r13 = r0
                r0 = r13
                r1 = 65533(0xfffd, float:9.1831E-41)
                if (r0 != r1) goto L8d
                r0 = r6
                r1 = r10
                if (r0 >= r1) goto L89
                r0 = r5
                r1 = r6
                int r6 = r6 + 1
                r0 = r0[r1]
                r1 = 255(0xff, float:3.57E-43)
                r0 = r0 & r1
                r14 = r0
                r0 = r14
                r1 = r4
                int r1 = r1.b2Min
                if (r0 < r1) goto L6f
                r0 = r14
                r1 = r4
                int r1 = r1.b2Max
                if (r0 > r1) goto L6f
                r0 = r4
                char[][] r0 = r0.b2c
                r1 = r12
                r0 = r0[r1]
                r1 = r14
                r2 = r4
                int r2 = r2.b2Min
                int r1 = r1 - r2
                char r0 = r0[r1]
                r1 = r0
                r13 = r1
                r1 = 65533(0xfffd, float:9.1831E-41)
                if (r0 != r1) goto L86
            L6f:
                r0 = r12
                r1 = 142(0x8e, float:1.99E-43)
                if (r0 == r1) goto L7f
                r0 = r12
                r1 = 143(0x8f, float:2.0E-43)
                if (r0 != r1) goto L82
            L7f:
                int r6 = r6 + (-1)
            L82:
                r0 = r11
                r13 = r0
            L86:
                goto L8d
            L89:
                r0 = r11
                r13 = r0
            L8d:
                r0 = r8
                r1 = r9
                int r9 = r9 + 1
                r2 = r13
                r0[r1] = r2
                goto L12
            L9a:
                r0 = r9
                return r0
            */
            throw new UnsupportedOperationException("Method not decompiled: sun.nio.cs.ext.DoubleByte.Decoder_EUC_SIM.decode(byte[], int, int, char[]):int");
        }
    }

    /* loaded from: charsets.jar:sun/nio/cs/ext/DoubleByte$Encoder.class */
    public static class Encoder extends CharsetEncoder implements ArrayEncoder {
        final int MAX_SINGLEBYTE = 255;
        private final char[] c2b;
        private final char[] c2bIndex;
        Surrogate.Parser sgp;
        protected byte[] repl;

        protected Encoder(Charset charset, char[] cArr, char[] cArr2) {
            super(charset, 2.0f, 2.0f);
            this.MAX_SINGLEBYTE = 255;
            this.repl = replacement();
            this.c2b = cArr;
            this.c2bIndex = cArr2;
        }

        Encoder(Charset charset, float f2, float f3, byte[] bArr, char[] cArr, char[] cArr2) {
            super(charset, f2, f3, bArr);
            this.MAX_SINGLEBYTE = 255;
            this.repl = replacement();
            this.c2b = cArr;
            this.c2bIndex = cArr2;
        }

        @Override // java.nio.charset.CharsetEncoder
        public boolean canEncode(char c2) {
            return encodeChar(c2) != 65533;
        }

        Surrogate.Parser sgp() {
            if (this.sgp == null) {
                this.sgp = new Surrogate.Parser();
            }
            return this.sgp;
        }

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
                    int iEncodeChar = encodeChar(c2);
                    if (iEncodeChar == 65533) {
                        if (!Character.isSurrogate(c2)) {
                            CoderResult coderResultUnmappableForLength = CoderResult.unmappableForLength(1);
                            charBuffer.position(iArrayOffset - charBuffer.arrayOffset());
                            byteBuffer.position(iArrayOffset3 - byteBuffer.arrayOffset());
                            return coderResultUnmappableForLength;
                        }
                        if (sgp().parse(c2, cArrArray, iArrayOffset, iArrayOffset2) < 0) {
                            CoderResult coderResultError = this.sgp.error();
                            charBuffer.position(iArrayOffset - charBuffer.arrayOffset());
                            byteBuffer.position(iArrayOffset3 - byteBuffer.arrayOffset());
                            return coderResultError;
                        }
                        CoderResult coderResultUnmappableResult = this.sgp.unmappableResult();
                        charBuffer.position(iArrayOffset - charBuffer.arrayOffset());
                        byteBuffer.position(iArrayOffset3 - byteBuffer.arrayOffset());
                        return coderResultUnmappableResult;
                    }
                    if (iEncodeChar > 255) {
                        if (iArrayOffset4 - iArrayOffset3 < 2) {
                            CoderResult coderResult = CoderResult.OVERFLOW;
                            charBuffer.position(iArrayOffset - charBuffer.arrayOffset());
                            byteBuffer.position(iArrayOffset3 - byteBuffer.arrayOffset());
                            return coderResult;
                        }
                        int i2 = iArrayOffset3;
                        int i3 = iArrayOffset3 + 1;
                        bArrArray[i2] = (byte) (iEncodeChar >> 8);
                        iArrayOffset3 = i3 + 1;
                        bArrArray[i3] = (byte) iEncodeChar;
                    } else {
                        if (iArrayOffset4 - iArrayOffset3 < 1) {
                            CoderResult coderResult2 = CoderResult.OVERFLOW;
                            charBuffer.position(iArrayOffset - charBuffer.arrayOffset());
                            byteBuffer.position(iArrayOffset3 - byteBuffer.arrayOffset());
                            return coderResult2;
                        }
                        int i4 = iArrayOffset3;
                        iArrayOffset3++;
                        bArrArray[i4] = (byte) iEncodeChar;
                    }
                    iArrayOffset++;
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

        protected CoderResult encodeBufferLoop(CharBuffer charBuffer, ByteBuffer byteBuffer) {
            int iPosition = charBuffer.position();
            while (charBuffer.hasRemaining()) {
                try {
                    char c2 = charBuffer.get();
                    int iEncodeChar = encodeChar(c2);
                    if (iEncodeChar == 65533) {
                        if (!Character.isSurrogate(c2)) {
                            CoderResult coderResultUnmappableForLength = CoderResult.unmappableForLength(1);
                            charBuffer.position(iPosition);
                            return coderResultUnmappableForLength;
                        }
                        if (sgp().parse(c2, charBuffer) < 0) {
                            CoderResult coderResultError = this.sgp.error();
                            charBuffer.position(iPosition);
                            return coderResultError;
                        }
                        CoderResult coderResultUnmappableResult = this.sgp.unmappableResult();
                        charBuffer.position(iPosition);
                        return coderResultUnmappableResult;
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
                    iPosition++;
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

        @Override // java.nio.charset.CharsetEncoder
        protected void implReplaceWith(byte[] bArr) {
            this.repl = bArr;
        }

        @Override // sun.nio.cs.ArrayEncoder
        public int encode(char[] cArr, int i2, int i3, byte[] bArr) {
            int i4 = 0;
            int i5 = i2 + i3;
            int length = bArr.length;
            while (i2 < i5) {
                int i6 = i2;
                i2++;
                char c2 = cArr[i6];
                int iEncodeChar = encodeChar(c2);
                if (iEncodeChar == 65533) {
                    if (Character.isHighSurrogate(c2) && i2 < i5 && Character.isLowSurrogate(cArr[i2])) {
                        i2++;
                    }
                    int i7 = i4;
                    i4++;
                    bArr[i7] = this.repl[0];
                    if (this.repl.length > 1) {
                        i4++;
                        bArr[i4] = this.repl[1];
                    }
                } else if (iEncodeChar > 255) {
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

        public int encodeChar(char c2) {
            return this.c2b[this.c2bIndex[c2 >> '\b'] + (c2 & 255)];
        }

        /* JADX WARN: Multi-variable type inference failed */
        static void initC2B(String[] strArr, String str, String str2, String str3, int i2, int i3, char[] cArr, char[] cArr2) {
            Arrays.fill(cArr, (char) 65533);
            int i4 = 256;
            char[] cArr3 = new char[strArr.length];
            char[] charArray = null;
            if (str != null) {
                charArray = str.toCharArray();
            }
            for (int i5 = 0; i5 < strArr.length; i5++) {
                if (strArr[i5] != null) {
                    cArr3[i5] = strArr[i5].toCharArray();
                }
            }
            if (str2 != null) {
                int i6 = 0;
                while (i6 < str2.length()) {
                    int i7 = i6;
                    int i8 = i6 + 1;
                    char cCharAt = str2.charAt(i7);
                    i6 = i8 + 1;
                    char cCharAt2 = str2.charAt(i8);
                    if (cCharAt < 256 && charArray != null) {
                        if (charArray[cCharAt] == cCharAt2) {
                            charArray[cCharAt] = 65533;
                        }
                    } else if (cArr3[cCharAt >> '\b'][(cCharAt & 255) - i2] == cCharAt2) {
                        cArr3[cCharAt >> '\b'][(cCharAt & 255) - i2] = 65533;
                    }
                }
            }
            if (charArray != null) {
                int i9 = 0;
                i4 = i4;
                while (i9 < charArray.length) {
                    char c2 = charArray[i9];
                    if (c2 != 65533) {
                        int i10 = cArr2[c2 >> '\b'];
                        i4 = i4;
                        if (i10 == 0) {
                            i10 = i4;
                            cArr2[c2 >> '\b'] = (char) i10;
                            i4 += 256;
                        }
                        cArr[i10 + (c2 & 255)] = (char) i9;
                    }
                    i9++;
                    i4 = i4;
                }
            }
            int i11 = 0;
            int i12 = i4;
            while (i11 < strArr.length) {
                char[] cArr4 = cArr3[i11];
                if (cArr4 != 0) {
                    int i13 = i2;
                    i12 = i12;
                    while (i13 <= i3) {
                        char c3 = cArr4[i13 - i2];
                        if (c3 != 65533) {
                            int i14 = cArr2[c3 >> '\b'];
                            i12 = i12;
                            if (i14 == 0) {
                                i14 = i12;
                                cArr2[c3 >> '\b'] = (char) i14;
                                i12 += 256;
                            }
                            cArr[i14 + (c3 & 255)] = (char) ((i11 << 8) | i13);
                        }
                        i13++;
                        i12 = i12;
                    }
                }
                i11++;
                i12 = i12;
            }
            if (str3 != null) {
                int i15 = 0;
                int i16 = i12;
                while (i15 < str3.length()) {
                    char cCharAt3 = str3.charAt(i15);
                    char cCharAt4 = str3.charAt(i15 + 1);
                    int i17 = cCharAt4 >> '\b';
                    if (cArr2[i17] == 0) {
                        cArr2[i17] = (char) i16;
                        i16 += 256;
                    }
                    cArr[cArr2[i17] + (cCharAt4 & 255)] = cCharAt3;
                    i15 += 2;
                    i16 = i16;
                }
            }
        }
    }

    /* loaded from: charsets.jar:sun/nio/cs/ext/DoubleByte$Encoder_DBCSONLY.class */
    public static class Encoder_DBCSONLY extends Encoder {
        Encoder_DBCSONLY(Charset charset, byte[] bArr, char[] cArr, char[] cArr2) {
            super(charset, 2.0f, 2.0f, bArr, cArr, cArr2);
        }

        @Override // sun.nio.cs.ext.DoubleByte.Encoder
        public int encodeChar(char c2) {
            int iEncodeChar = super.encodeChar(c2);
            if (iEncodeChar <= 255) {
                return 65533;
            }
            return iEncodeChar;
        }
    }

    /* loaded from: charsets.jar:sun/nio/cs/ext/DoubleByte$Encoder_EBCDIC.class */
    public static class Encoder_EBCDIC extends Encoder {
        static final int SBCS = 0;
        static final int DBCS = 1;
        static final byte SO = 14;
        static final byte SI = 15;
        protected int currentState;

        Encoder_EBCDIC(Charset charset, char[] cArr, char[] cArr2) {
            super(charset, 4.0f, 5.0f, new byte[]{111}, cArr, cArr2);
            this.currentState = 0;
        }

        @Override // java.nio.charset.CharsetEncoder
        protected void implReset() {
            this.currentState = 0;
        }

        @Override // java.nio.charset.CharsetEncoder
        protected CoderResult implFlush(ByteBuffer byteBuffer) {
            if (this.currentState == 1) {
                if (byteBuffer.remaining() < 1) {
                    return CoderResult.OVERFLOW;
                }
                byteBuffer.put((byte) 15);
            }
            implReset();
            return CoderResult.UNDERFLOW;
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
                    int iEncodeChar = encodeChar(c2);
                    if (iEncodeChar == 65533) {
                        if (!Character.isSurrogate(c2)) {
                            CoderResult coderResultUnmappableForLength = CoderResult.unmappableForLength(1);
                            charBuffer.position(iArrayOffset - charBuffer.arrayOffset());
                            byteBuffer.position(iArrayOffset3 - byteBuffer.arrayOffset());
                            return coderResultUnmappableForLength;
                        }
                        if (sgp().parse(c2, cArrArray, iArrayOffset, iArrayOffset2) < 0) {
                            CoderResult coderResultError = this.sgp.error();
                            charBuffer.position(iArrayOffset - charBuffer.arrayOffset());
                            byteBuffer.position(iArrayOffset3 - byteBuffer.arrayOffset());
                            return coderResultError;
                        }
                        CoderResult coderResultUnmappableResult = this.sgp.unmappableResult();
                        charBuffer.position(iArrayOffset - charBuffer.arrayOffset());
                        byteBuffer.position(iArrayOffset3 - byteBuffer.arrayOffset());
                        return coderResultUnmappableResult;
                    }
                    if (iEncodeChar > 255) {
                        if (this.currentState == 0) {
                            if (iArrayOffset4 - iArrayOffset3 < 1) {
                                CoderResult coderResult = CoderResult.OVERFLOW;
                                charBuffer.position(iArrayOffset - charBuffer.arrayOffset());
                                byteBuffer.position(iArrayOffset3 - byteBuffer.arrayOffset());
                                return coderResult;
                            }
                            this.currentState = 1;
                            int i2 = iArrayOffset3;
                            iArrayOffset3++;
                            bArrArray[i2] = 14;
                        }
                        if (iArrayOffset4 - iArrayOffset3 < 2) {
                            CoderResult coderResult2 = CoderResult.OVERFLOW;
                            charBuffer.position(iArrayOffset - charBuffer.arrayOffset());
                            byteBuffer.position(iArrayOffset3 - byteBuffer.arrayOffset());
                            return coderResult2;
                        }
                        int i3 = iArrayOffset3;
                        int i4 = iArrayOffset3 + 1;
                        bArrArray[i3] = (byte) (iEncodeChar >> 8);
                        iArrayOffset3 = i4 + 1;
                        bArrArray[i4] = (byte) iEncodeChar;
                    } else {
                        if (this.currentState == 1) {
                            if (iArrayOffset4 - iArrayOffset3 < 1) {
                                CoderResult coderResult3 = CoderResult.OVERFLOW;
                                charBuffer.position(iArrayOffset - charBuffer.arrayOffset());
                                byteBuffer.position(iArrayOffset3 - byteBuffer.arrayOffset());
                                return coderResult3;
                            }
                            this.currentState = 0;
                            int i5 = iArrayOffset3;
                            iArrayOffset3++;
                            bArrArray[i5] = 15;
                        }
                        if (iArrayOffset4 - iArrayOffset3 < 1) {
                            CoderResult coderResult4 = CoderResult.OVERFLOW;
                            charBuffer.position(iArrayOffset - charBuffer.arrayOffset());
                            byteBuffer.position(iArrayOffset3 - byteBuffer.arrayOffset());
                            return coderResult4;
                        }
                        int i6 = iArrayOffset3;
                        iArrayOffset3++;
                        bArrArray[i6] = (byte) iEncodeChar;
                    }
                    iArrayOffset++;
                } catch (Throwable th) {
                    charBuffer.position(iArrayOffset - charBuffer.arrayOffset());
                    byteBuffer.position(iArrayOffset3 - byteBuffer.arrayOffset());
                    throw th;
                }
            }
            CoderResult coderResult5 = CoderResult.UNDERFLOW;
            charBuffer.position(iArrayOffset - charBuffer.arrayOffset());
            byteBuffer.position(iArrayOffset3 - byteBuffer.arrayOffset());
            return coderResult5;
        }

        @Override // sun.nio.cs.ext.DoubleByte.Encoder
        protected CoderResult encodeBufferLoop(CharBuffer charBuffer, ByteBuffer byteBuffer) {
            int iPosition = charBuffer.position();
            while (charBuffer.hasRemaining()) {
                try {
                    char c2 = charBuffer.get();
                    int iEncodeChar = encodeChar(c2);
                    if (iEncodeChar == 65533) {
                        if (!Character.isSurrogate(c2)) {
                            CoderResult coderResultUnmappableForLength = CoderResult.unmappableForLength(1);
                            charBuffer.position(iPosition);
                            return coderResultUnmappableForLength;
                        }
                        if (sgp().parse(c2, charBuffer) < 0) {
                            CoderResult coderResultError = this.sgp.error();
                            charBuffer.position(iPosition);
                            return coderResultError;
                        }
                        CoderResult coderResultUnmappableResult = this.sgp.unmappableResult();
                        charBuffer.position(iPosition);
                        return coderResultUnmappableResult;
                    }
                    if (iEncodeChar > 255) {
                        if (this.currentState == 0) {
                            if (byteBuffer.remaining() < 1) {
                                CoderResult coderResult = CoderResult.OVERFLOW;
                                charBuffer.position(iPosition);
                                return coderResult;
                            }
                            this.currentState = 1;
                            byteBuffer.put((byte) 14);
                        }
                        if (byteBuffer.remaining() < 2) {
                            CoderResult coderResult2 = CoderResult.OVERFLOW;
                            charBuffer.position(iPosition);
                            return coderResult2;
                        }
                        byteBuffer.put((byte) (iEncodeChar >> 8));
                        byteBuffer.put((byte) iEncodeChar);
                    } else {
                        if (this.currentState == 1) {
                            if (byteBuffer.remaining() < 1) {
                                CoderResult coderResult3 = CoderResult.OVERFLOW;
                                charBuffer.position(iPosition);
                                return coderResult3;
                            }
                            this.currentState = 0;
                            byteBuffer.put((byte) 15);
                        }
                        if (byteBuffer.remaining() < 1) {
                            CoderResult coderResult4 = CoderResult.OVERFLOW;
                            charBuffer.position(iPosition);
                            return coderResult4;
                        }
                        byteBuffer.put((byte) iEncodeChar);
                    }
                    iPosition++;
                } catch (Throwable th) {
                    charBuffer.position(iPosition);
                    throw th;
                }
            }
            CoderResult coderResult5 = CoderResult.UNDERFLOW;
            charBuffer.position(iPosition);
            return coderResult5;
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
                    if (Character.isHighSurrogate(c2) && i2 < i5 && Character.isLowSurrogate(cArr[i2])) {
                        i2++;
                    }
                    int i7 = i4;
                    i4++;
                    bArr[i7] = this.repl[0];
                    if (this.repl.length > 1) {
                        i4++;
                        bArr[i4] = this.repl[1];
                    }
                } else if (iEncodeChar > 255) {
                    if (this.currentState == 0) {
                        this.currentState = 1;
                        int i8 = i4;
                        i4++;
                        bArr[i8] = 14;
                    }
                    int i9 = i4;
                    int i10 = i4 + 1;
                    bArr[i9] = (byte) (iEncodeChar >> 8);
                    i4 = i10 + 1;
                    bArr[i10] = (byte) iEncodeChar;
                } else {
                    if (this.currentState == 1) {
                        this.currentState = 0;
                        int i11 = i4;
                        i4++;
                        bArr[i11] = 15;
                    }
                    int i12 = i4;
                    i4++;
                    bArr[i12] = (byte) iEncodeChar;
                }
            }
            if (this.currentState == 1) {
                this.currentState = 0;
                int i13 = i4;
                i4++;
                bArr[i13] = 15;
            }
            return i4;
        }
    }

    /* loaded from: charsets.jar:sun/nio/cs/ext/DoubleByte$Encoder_EUC_SIM.class */
    public static class Encoder_EUC_SIM extends Encoder {
        Encoder_EUC_SIM(Charset charset, char[] cArr, char[] cArr2) {
            super(charset, cArr, cArr2);
        }
    }
}
