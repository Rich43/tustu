package sun.nio.cs.ext;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.CoderResult;
import java.security.AccessController;
import java.security.PrivilegedAction;
import sun.nio.cs.CharsetMapping;

/* loaded from: charsets.jar:sun/nio/cs/ext/SJIS_0213.class */
public class SJIS_0213 extends Charset {
    static CharsetMapping mapping = (CharsetMapping) AccessController.doPrivileged(new PrivilegedAction<CharsetMapping>() { // from class: sun.nio.cs.ext.SJIS_0213.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // java.security.PrivilegedAction
        /* renamed from: run */
        public CharsetMapping run2() {
            return CharsetMapping.get(SJIS_0213.class.getResourceAsStream("sjis0213.dat"));
        }
    });

    public SJIS_0213() {
        super("x-SJIS_0213", ExtendedCharsets.aliasesFor("SJIS_0213"));
    }

    @Override // java.nio.charset.Charset
    public boolean contains(Charset charset) {
        return charset.name().equals("US-ASCII") || (charset instanceof SJIS) || (charset instanceof SJIS_0213);
    }

    @Override // java.nio.charset.Charset
    public CharsetDecoder newDecoder() {
        return new Decoder(this);
    }

    @Override // java.nio.charset.Charset
    public CharsetEncoder newEncoder() {
        return new Encoder(this);
    }

    /* loaded from: charsets.jar:sun/nio/cs/ext/SJIS_0213$Decoder.class */
    protected static class Decoder extends CharsetDecoder {
        protected static final char UNMAPPABLE = 65533;
        private char[] cc;
        private CharsetMapping.Entry comp;

        protected Decoder(Charset charset) {
            super(charset, 0.5f, 1.0f);
            this.cc = new char[2];
            this.comp = new CharsetMapping.Entry();
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
                    char cDecodeSingle = decodeSingle(i2);
                    int i3 = 1;
                    int i4 = 1;
                    char[] cArrDecodeDoubleEx = null;
                    if (cDecodeSingle == 65533) {
                        if (iArrayOffset2 - iArrayOffset < 2) {
                            CoderResult coderResult = CoderResult.UNDERFLOW;
                            byteBuffer.position(iArrayOffset - byteBuffer.arrayOffset());
                            charBuffer.position(iArrayOffset3 - charBuffer.arrayOffset());
                            return coderResult;
                        }
                        int i5 = bArrArray[iArrayOffset + 1] & 255;
                        cDecodeSingle = decodeDouble(i2, i5);
                        i3 = 1 + 1;
                        if (cDecodeSingle == 65533) {
                            cArrDecodeDoubleEx = decodeDoubleEx(i2, i5);
                            if (cArrDecodeDoubleEx == null) {
                                if (decodeSingle(i5) == 65533) {
                                    CoderResult coderResultUnmappableForLength = CoderResult.unmappableForLength(2);
                                    byteBuffer.position(iArrayOffset - byteBuffer.arrayOffset());
                                    charBuffer.position(iArrayOffset3 - charBuffer.arrayOffset());
                                    return coderResultUnmappableForLength;
                                }
                                CoderResult coderResultUnmappableForLength2 = CoderResult.unmappableForLength(1);
                                byteBuffer.position(iArrayOffset - byteBuffer.arrayOffset());
                                charBuffer.position(iArrayOffset3 - charBuffer.arrayOffset());
                                return coderResultUnmappableForLength2;
                            }
                            i4 = 1 + 1;
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
                        cArrArray[i6] = cArrDecodeDoubleEx[0];
                        iArrayOffset3 = i7 + 1;
                        cArrArray[i7] = cArrDecodeDoubleEx[1];
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

        private CoderResult decodeBufferLoop(ByteBuffer byteBuffer, CharBuffer charBuffer) {
            int iPosition = byteBuffer.position();
            while (byteBuffer.hasRemaining()) {
                try {
                    char[] cArrDecodeDoubleEx = null;
                    int i2 = byteBuffer.get() & 255;
                    char cDecodeSingle = decodeSingle(i2);
                    int i3 = 1;
                    int i4 = 1;
                    if (cDecodeSingle == 65533) {
                        if (byteBuffer.remaining() < 1) {
                            CoderResult coderResult = CoderResult.UNDERFLOW;
                            byteBuffer.position(iPosition);
                            return coderResult;
                        }
                        int i5 = byteBuffer.get() & 255;
                        i3 = 1 + 1;
                        cDecodeSingle = decodeDouble(i2, i5);
                        if (cDecodeSingle == 65533) {
                            cArrDecodeDoubleEx = decodeDoubleEx(i2, i5);
                            if (cArrDecodeDoubleEx == null) {
                                if (decodeSingle(i5) == 65533) {
                                    CoderResult coderResultUnmappableForLength = CoderResult.unmappableForLength(2);
                                    byteBuffer.position(iPosition);
                                    return coderResultUnmappableForLength;
                                }
                                CoderResult coderResultUnmappableForLength2 = CoderResult.unmappableForLength(1);
                                byteBuffer.position(iPosition);
                                return coderResultUnmappableForLength2;
                            }
                            i4 = 1 + 1;
                        }
                    }
                    if (charBuffer.remaining() < i4) {
                        CoderResult coderResult2 = CoderResult.OVERFLOW;
                        byteBuffer.position(iPosition);
                        return coderResult2;
                    }
                    if (i4 == 2) {
                        charBuffer.put(cArrDecodeDoubleEx[0]);
                        charBuffer.put(cArrDecodeDoubleEx[1]);
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

        @Override // java.nio.charset.CharsetDecoder
        protected CoderResult decodeLoop(ByteBuffer byteBuffer, CharBuffer charBuffer) {
            if (byteBuffer.hasArray() && charBuffer.hasArray()) {
                return decodeArrayLoop(byteBuffer, charBuffer);
            }
            return decodeBufferLoop(byteBuffer, charBuffer);
        }

        protected char decodeSingle(int i2) {
            return SJIS_0213.mapping.decodeSingle(i2);
        }

        protected char decodeDouble(int i2, int i3) {
            return SJIS_0213.mapping.decodeDouble(i2, i3);
        }

        protected char[] decodeDoubleEx(int i2, int i3) {
            int i4 = (i2 << 8) | i3;
            if (SJIS_0213.mapping.decodeSurrogate(i4, this.cc) != null) {
                return this.cc;
            }
            this.comp.f13589bs = i4;
            if (SJIS_0213.mapping.decodeComposite(this.comp, this.cc) != null) {
                return this.cc;
            }
            return null;
        }
    }

    /* loaded from: charsets.jar:sun/nio/cs/ext/SJIS_0213$Encoder.class */
    protected static class Encoder extends CharsetEncoder {
        protected static final int UNMAPPABLE = 65533;
        protected static final int MAX_SINGLEBYTE = 255;
        private CharsetMapping.Entry comp;
        char leftoverBase;

        protected Encoder(Charset charset) {
            super(charset, 2.0f, 2.0f);
            this.comp = new CharsetMapping.Entry();
            this.leftoverBase = (char) 0;
        }

        @Override // java.nio.charset.CharsetEncoder
        public boolean canEncode(char c2) {
            return encodeChar(c2) != 65533;
        }

        protected int encodeChar(char c2) {
            return SJIS_0213.mapping.encodeChar(c2);
        }

        protected int encodeSurrogate(char c2, char c3) {
            return SJIS_0213.mapping.encodeSurrogate(c2, c3);
        }

        protected int encodeComposite(char c2, char c3) {
            this.comp.cp = c2;
            this.comp.cp2 = c3;
            return SJIS_0213.mapping.encodeComposite(this.comp);
        }

        protected boolean isCompositeBase(char c2) {
            this.comp.cp = c2;
            return SJIS_0213.mapping.isCompositeBase(this.comp);
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
                    if (this.leftoverBase != 0) {
                        boolean z2 = false;
                        int iEncodeComposite = encodeComposite(this.leftoverBase, c2);
                        if (iEncodeComposite == 65533) {
                            iEncodeComposite = encodeChar(this.leftoverBase);
                        } else {
                            z2 = true;
                        }
                        if (iArrayOffset4 - iArrayOffset3 < 2) {
                            CoderResult coderResult = CoderResult.OVERFLOW;
                            charBuffer.position(iArrayOffset - charBuffer.arrayOffset());
                            byteBuffer.position(iArrayOffset3 - byteBuffer.arrayOffset());
                            return coderResult;
                        }
                        int i2 = iArrayOffset3;
                        int i3 = iArrayOffset3 + 1;
                        bArrArray[i2] = (byte) (iEncodeComposite >> 8);
                        iArrayOffset3 = i3 + 1;
                        bArrArray[i3] = (byte) iEncodeComposite;
                        this.leftoverBase = (char) 0;
                        if (z2) {
                            iArrayOffset++;
                        }
                    }
                    if (isCompositeBase(c2)) {
                        this.leftoverBase = c2;
                    } else {
                        int iEncodeChar = encodeChar(c2);
                        if (iEncodeChar <= 255) {
                            if (iArrayOffset4 <= iArrayOffset3) {
                                CoderResult coderResult2 = CoderResult.OVERFLOW;
                                charBuffer.position(iArrayOffset - charBuffer.arrayOffset());
                                byteBuffer.position(iArrayOffset3 - byteBuffer.arrayOffset());
                                return coderResult2;
                            }
                            int i4 = iArrayOffset3;
                            iArrayOffset3++;
                            bArrArray[i4] = (byte) iEncodeChar;
                        } else if (iEncodeChar != 65533) {
                            if (iArrayOffset4 - iArrayOffset3 < 2) {
                                CoderResult coderResult3 = CoderResult.OVERFLOW;
                                charBuffer.position(iArrayOffset - charBuffer.arrayOffset());
                                byteBuffer.position(iArrayOffset3 - byteBuffer.arrayOffset());
                                return coderResult3;
                            }
                            int i5 = iArrayOffset3;
                            int i6 = iArrayOffset3 + 1;
                            bArrArray[i5] = (byte) (iEncodeChar >> 8);
                            iArrayOffset3 = i6 + 1;
                            bArrArray[i6] = (byte) iEncodeChar;
                        } else {
                            if (!Character.isHighSurrogate(c2)) {
                                if (Character.isLowSurrogate(c2)) {
                                    CoderResult coderResultMalformedForLength = CoderResult.malformedForLength(1);
                                    charBuffer.position(iArrayOffset - charBuffer.arrayOffset());
                                    byteBuffer.position(iArrayOffset3 - byteBuffer.arrayOffset());
                                    return coderResultMalformedForLength;
                                }
                                CoderResult coderResultUnmappableForLength = CoderResult.unmappableForLength(1);
                                charBuffer.position(iArrayOffset - charBuffer.arrayOffset());
                                byteBuffer.position(iArrayOffset3 - byteBuffer.arrayOffset());
                                return coderResultUnmappableForLength;
                            }
                            if (iArrayOffset + 1 == iArrayOffset2) {
                                CoderResult coderResult4 = CoderResult.UNDERFLOW;
                                charBuffer.position(iArrayOffset - charBuffer.arrayOffset());
                                byteBuffer.position(iArrayOffset3 - byteBuffer.arrayOffset());
                                return coderResult4;
                            }
                            char c3 = cArrArray[iArrayOffset + 1];
                            if (!Character.isLowSurrogate(c3)) {
                                CoderResult coderResultMalformedForLength2 = CoderResult.malformedForLength(1);
                                charBuffer.position(iArrayOffset - charBuffer.arrayOffset());
                                byteBuffer.position(iArrayOffset3 - byteBuffer.arrayOffset());
                                return coderResultMalformedForLength2;
                            }
                            int iEncodeSurrogate = encodeSurrogate(c2, c3);
                            if (iEncodeSurrogate == 65533) {
                                CoderResult coderResultUnmappableForLength2 = CoderResult.unmappableForLength(2);
                                charBuffer.position(iArrayOffset - charBuffer.arrayOffset());
                                byteBuffer.position(iArrayOffset3 - byteBuffer.arrayOffset());
                                return coderResultUnmappableForLength2;
                            }
                            if (iArrayOffset4 - iArrayOffset3 < 2) {
                                CoderResult coderResult5 = CoderResult.OVERFLOW;
                                charBuffer.position(iArrayOffset - charBuffer.arrayOffset());
                                byteBuffer.position(iArrayOffset3 - byteBuffer.arrayOffset());
                                return coderResult5;
                            }
                            int i7 = iArrayOffset3;
                            int i8 = iArrayOffset3 + 1;
                            bArrArray[i7] = (byte) (iEncodeSurrogate >> 8);
                            iArrayOffset3 = i8 + 1;
                            bArrArray[i8] = (byte) iEncodeSurrogate;
                            iArrayOffset++;
                        }
                    }
                    iArrayOffset++;
                } catch (Throwable th) {
                    charBuffer.position(iArrayOffset - charBuffer.arrayOffset());
                    byteBuffer.position(iArrayOffset3 - byteBuffer.arrayOffset());
                    throw th;
                }
            }
            CoderResult coderResult6 = CoderResult.UNDERFLOW;
            charBuffer.position(iArrayOffset - charBuffer.arrayOffset());
            byteBuffer.position(iArrayOffset3 - byteBuffer.arrayOffset());
            return coderResult6;
        }

        protected CoderResult encodeBufferLoop(CharBuffer charBuffer, ByteBuffer byteBuffer) {
            int iPosition = charBuffer.position();
            while (charBuffer.hasRemaining()) {
                try {
                    char c2 = charBuffer.get();
                    if (this.leftoverBase != 0) {
                        boolean z2 = false;
                        int iEncodeComposite = encodeComposite(this.leftoverBase, c2);
                        if (iEncodeComposite == 65533) {
                            iEncodeComposite = encodeChar(this.leftoverBase);
                        } else {
                            z2 = true;
                        }
                        if (byteBuffer.remaining() < 2) {
                            CoderResult coderResult = CoderResult.OVERFLOW;
                            charBuffer.position(iPosition);
                            return coderResult;
                        }
                        byteBuffer.put((byte) (iEncodeComposite >> 8));
                        byteBuffer.put((byte) iEncodeComposite);
                        this.leftoverBase = (char) 0;
                        if (z2) {
                            iPosition++;
                        }
                    }
                    if (isCompositeBase(c2)) {
                        this.leftoverBase = c2;
                    } else {
                        int iEncodeChar = encodeChar(c2);
                        if (iEncodeChar <= 255) {
                            if (byteBuffer.remaining() < 1) {
                                CoderResult coderResult2 = CoderResult.OVERFLOW;
                                charBuffer.position(iPosition);
                                return coderResult2;
                            }
                            byteBuffer.put((byte) iEncodeChar);
                        } else if (iEncodeChar != 65533) {
                            if (byteBuffer.remaining() < 2) {
                                CoderResult coderResult3 = CoderResult.OVERFLOW;
                                charBuffer.position(iPosition);
                                return coderResult3;
                            }
                            byteBuffer.put((byte) (iEncodeChar >> 8));
                            byteBuffer.put((byte) iEncodeChar);
                        } else {
                            if (!Character.isHighSurrogate(c2)) {
                                if (Character.isLowSurrogate(c2)) {
                                    CoderResult coderResultMalformedForLength = CoderResult.malformedForLength(1);
                                    charBuffer.position(iPosition);
                                    return coderResultMalformedForLength;
                                }
                                CoderResult coderResultUnmappableForLength = CoderResult.unmappableForLength(1);
                                charBuffer.position(iPosition);
                                return coderResultUnmappableForLength;
                            }
                            if (!charBuffer.hasRemaining()) {
                                CoderResult coderResult4 = CoderResult.UNDERFLOW;
                                charBuffer.position(iPosition);
                                return coderResult4;
                            }
                            char c3 = charBuffer.get();
                            if (!Character.isLowSurrogate(c3)) {
                                CoderResult coderResultMalformedForLength2 = CoderResult.malformedForLength(1);
                                charBuffer.position(iPosition);
                                return coderResultMalformedForLength2;
                            }
                            int iEncodeSurrogate = encodeSurrogate(c2, c3);
                            if (iEncodeSurrogate == 65533) {
                                CoderResult coderResultUnmappableForLength2 = CoderResult.unmappableForLength(2);
                                charBuffer.position(iPosition);
                                return coderResultUnmappableForLength2;
                            }
                            if (byteBuffer.remaining() < 2) {
                                CoderResult coderResult5 = CoderResult.OVERFLOW;
                                charBuffer.position(iPosition);
                                return coderResult5;
                            }
                            byteBuffer.put((byte) (iEncodeSurrogate >> 8));
                            byteBuffer.put((byte) iEncodeSurrogate);
                            iPosition++;
                        }
                    }
                    iPosition++;
                } catch (Throwable th) {
                    charBuffer.position(iPosition);
                    throw th;
                }
            }
            CoderResult coderResult6 = CoderResult.UNDERFLOW;
            charBuffer.position(iPosition);
            return coderResult6;
        }

        @Override // java.nio.charset.CharsetEncoder
        protected CoderResult encodeLoop(CharBuffer charBuffer, ByteBuffer byteBuffer) {
            if (charBuffer.hasArray() && byteBuffer.hasArray()) {
                return encodeArrayLoop(charBuffer, byteBuffer);
            }
            return encodeBufferLoop(charBuffer, byteBuffer);
        }

        @Override // java.nio.charset.CharsetEncoder
        protected CoderResult implFlush(ByteBuffer byteBuffer) {
            if (this.leftoverBase > 0) {
                if (byteBuffer.remaining() < 2) {
                    return CoderResult.OVERFLOW;
                }
                int iEncodeChar = encodeChar(this.leftoverBase);
                byteBuffer.put((byte) (iEncodeChar >> 8));
                byteBuffer.put((byte) iEncodeChar);
                this.leftoverBase = (char) 0;
            }
            return CoderResult.UNDERFLOW;
        }

        @Override // java.nio.charset.CharsetEncoder
        protected void implReset() {
            this.leftoverBase = (char) 0;
        }
    }
}
