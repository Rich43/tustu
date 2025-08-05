package sun.nio.cs.ext;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.CoderResult;
import sun.nio.cs.HistoricallyNamedCharset;
import sun.nio.cs.SingleByte;
import sun.nio.cs.Surrogate;
import sun.nio.cs.ext.DoubleByte;
import sun.text.normalizer.NormalizerImpl;

/* loaded from: charsets.jar:sun/nio/cs/ext/EUC_JP.class */
public class EUC_JP extends Charset implements HistoricallyNamedCharset {
    public EUC_JP() {
        super("EUC-JP", ExtendedCharsets.aliasesFor("EUC-JP"));
    }

    @Override // sun.nio.cs.HistoricallyNamedCharset
    public String historicalName() {
        return "EUC_JP";
    }

    @Override // java.nio.charset.Charset
    public boolean contains(Charset charset) {
        return charset.name().equals("US-ASCII") || (charset instanceof JIS_X_0201) || (charset instanceof JIS_X_0208) || (charset instanceof JIS_X_0212) || (charset instanceof EUC_JP);
    }

    @Override // java.nio.charset.Charset
    public CharsetDecoder newDecoder() {
        return new Decoder(this);
    }

    @Override // java.nio.charset.Charset
    public CharsetEncoder newEncoder() {
        return new Encoder(this);
    }

    /* loaded from: charsets.jar:sun/nio/cs/ext/EUC_JP$Decoder.class */
    static class Decoder extends CharsetDecoder implements DelegatableDecoder {
        static final SingleByte.Decoder DEC0201;
        static final DoubleByte.Decoder DEC0208;
        static final DoubleByte.Decoder DEC0212;
        private final SingleByte.Decoder dec0201;
        private final DoubleByte.Decoder dec0208;
        private final DoubleByte.Decoder dec0212;
        static final /* synthetic */ boolean $assertionsDisabled;

        static {
            $assertionsDisabled = !EUC_JP.class.desiredAssertionStatus();
            DEC0201 = (SingleByte.Decoder) new JIS_X_0201().newDecoder();
            DEC0208 = (DoubleByte.Decoder) new JIS_X_0208().newDecoder();
            DEC0212 = (DoubleByte.Decoder) new JIS_X_0212().newDecoder();
        }

        protected Decoder(Charset charset) {
            this(charset, 0.5f, 1.0f, DEC0201, DEC0208, DEC0212);
        }

        protected Decoder(Charset charset, float f2, float f3, SingleByte.Decoder decoder, DoubleByte.Decoder decoder2, DoubleByte.Decoder decoder3) {
            super(charset, f2, f3);
            this.dec0201 = decoder;
            this.dec0208 = decoder2;
            this.dec0212 = decoder3;
        }

        protected char decodeDouble(int i2, int i3) {
            if (i2 == 142) {
                if (i3 < 128) {
                    return (char) 65533;
                }
                return this.dec0201.decode((byte) i3);
            }
            return this.dec0208.decodeDouble(i2 - 128, i3 - 128);
        }

        private CoderResult decodeArrayLoop(ByteBuffer byteBuffer, CharBuffer charBuffer) {
            char cDecodeDouble;
            byte[] bArrArray = byteBuffer.array();
            int iArrayOffset = byteBuffer.arrayOffset() + byteBuffer.position();
            int iArrayOffset2 = byteBuffer.arrayOffset() + byteBuffer.limit();
            if (!$assertionsDisabled && iArrayOffset > iArrayOffset2) {
                throw new AssertionError();
            }
            int i2 = iArrayOffset <= iArrayOffset2 ? iArrayOffset : iArrayOffset2;
            char[] cArrArray = charBuffer.array();
            int iArrayOffset3 = charBuffer.arrayOffset() + charBuffer.position();
            int iArrayOffset4 = charBuffer.arrayOffset() + charBuffer.limit();
            if (!$assertionsDisabled && iArrayOffset3 > iArrayOffset4) {
                throw new AssertionError();
            }
            int i3 = iArrayOffset3 <= iArrayOffset4 ? iArrayOffset3 : iArrayOffset4;
            while (i2 < iArrayOffset2) {
                try {
                    int i4 = bArrArray[i2] & 255;
                    int i5 = 1;
                    if ((i4 & 128) == 0) {
                        cDecodeDouble = (char) i4;
                    } else if (i4 == 143) {
                        if (i2 + 3 > iArrayOffset2) {
                            CoderResult coderResult = CoderResult.UNDERFLOW;
                            byteBuffer.position(i2 - byteBuffer.arrayOffset());
                            charBuffer.position(i3 - charBuffer.arrayOffset());
                            return coderResult;
                        }
                        int i6 = bArrArray[i2 + 1] & 255;
                        int i7 = bArrArray[i2 + 2] & 255;
                        i5 = 1 + 2;
                        if (this.dec0212 == null) {
                            CoderResult coderResultUnmappableForLength = CoderResult.unmappableForLength(i5);
                            byteBuffer.position(i2 - byteBuffer.arrayOffset());
                            charBuffer.position(i3 - charBuffer.arrayOffset());
                            return coderResultUnmappableForLength;
                        }
                        cDecodeDouble = this.dec0212.decodeDouble(i6 - 128, i7 - 128);
                    } else {
                        if (i2 + 2 > iArrayOffset2) {
                            CoderResult coderResult2 = CoderResult.UNDERFLOW;
                            byteBuffer.position(i2 - byteBuffer.arrayOffset());
                            charBuffer.position(i3 - charBuffer.arrayOffset());
                            return coderResult2;
                        }
                        i5 = 1 + 1;
                        cDecodeDouble = decodeDouble(i4, bArrArray[i2 + 1] & 255);
                    }
                    if (cDecodeDouble == 65533) {
                        CoderResult coderResultUnmappableForLength2 = CoderResult.unmappableForLength(i5);
                        byteBuffer.position(i2 - byteBuffer.arrayOffset());
                        charBuffer.position(i3 - charBuffer.arrayOffset());
                        return coderResultUnmappableForLength2;
                    }
                    if (i3 + 1 > iArrayOffset4) {
                        CoderResult coderResult3 = CoderResult.OVERFLOW;
                        byteBuffer.position(i2 - byteBuffer.arrayOffset());
                        charBuffer.position(i3 - charBuffer.arrayOffset());
                        return coderResult3;
                    }
                    int i8 = i3;
                    i3++;
                    cArrArray[i8] = cDecodeDouble;
                    i2 += i5;
                } catch (Throwable th) {
                    byteBuffer.position(i2 - byteBuffer.arrayOffset());
                    charBuffer.position(i3 - charBuffer.arrayOffset());
                    throw th;
                }
            }
            CoderResult coderResult4 = CoderResult.UNDERFLOW;
            byteBuffer.position(i2 - byteBuffer.arrayOffset());
            charBuffer.position(i3 - charBuffer.arrayOffset());
            return coderResult4;
        }

        private CoderResult decodeBufferLoop(ByteBuffer byteBuffer, CharBuffer charBuffer) {
            char cDecodeDouble;
            int iPosition = byteBuffer.position();
            while (byteBuffer.hasRemaining()) {
                try {
                    int i2 = byteBuffer.get() & 255;
                    int i3 = 1;
                    if ((i2 & 128) == 0) {
                        cDecodeDouble = (char) i2;
                    } else if (i2 == 143) {
                        if (byteBuffer.remaining() < 2) {
                            CoderResult coderResult = CoderResult.UNDERFLOW;
                            byteBuffer.position(iPosition);
                            return coderResult;
                        }
                        int i4 = byteBuffer.get() & 255;
                        int i5 = byteBuffer.get() & 255;
                        i3 = 1 + 2;
                        if (this.dec0212 == null) {
                            CoderResult coderResultUnmappableForLength = CoderResult.unmappableForLength(i3);
                            byteBuffer.position(iPosition);
                            return coderResultUnmappableForLength;
                        }
                        cDecodeDouble = this.dec0212.decodeDouble(i4 - 128, i5 - 128);
                    } else {
                        if (byteBuffer.remaining() < 1) {
                            CoderResult coderResult2 = CoderResult.UNDERFLOW;
                            byteBuffer.position(iPosition);
                            return coderResult2;
                        }
                        i3 = 1 + 1;
                        cDecodeDouble = decodeDouble(i2, byteBuffer.get() & 255);
                    }
                    if (cDecodeDouble == 65533) {
                        CoderResult coderResultUnmappableForLength2 = CoderResult.unmappableForLength(i3);
                        byteBuffer.position(iPosition);
                        return coderResultUnmappableForLength2;
                    }
                    if (charBuffer.remaining() < 1) {
                        CoderResult coderResult3 = CoderResult.OVERFLOW;
                        byteBuffer.position(iPosition);
                        return coderResult3;
                    }
                    charBuffer.put(cDecodeDouble);
                    iPosition += i3;
                } catch (Throwable th) {
                    byteBuffer.position(iPosition);
                    throw th;
                }
            }
            CoderResult coderResult4 = CoderResult.UNDERFLOW;
            byteBuffer.position(iPosition);
            return coderResult4;
        }

        @Override // java.nio.charset.CharsetDecoder
        public CoderResult decodeLoop(ByteBuffer byteBuffer, CharBuffer charBuffer) {
            if (byteBuffer.hasArray() && charBuffer.hasArray()) {
                return decodeArrayLoop(byteBuffer, charBuffer);
            }
            return decodeBufferLoop(byteBuffer, charBuffer);
        }

        @Override // java.nio.charset.CharsetDecoder
        public void implReset() {
            super.implReset();
        }

        @Override // java.nio.charset.CharsetDecoder
        public CoderResult implFlush(CharBuffer charBuffer) {
            return super.implFlush(charBuffer);
        }
    }

    /* loaded from: charsets.jar:sun/nio/cs/ext/EUC_JP$Encoder.class */
    static class Encoder extends CharsetEncoder {
        static final SingleByte.Encoder ENC0201;
        static final DoubleByte.Encoder ENC0208;
        static final DoubleByte.Encoder ENC0212;
        private final Surrogate.Parser sgp;
        private final SingleByte.Encoder enc0201;
        private final DoubleByte.Encoder enc0208;
        private final DoubleByte.Encoder enc0212;
        static final /* synthetic */ boolean $assertionsDisabled;

        static {
            $assertionsDisabled = !EUC_JP.class.desiredAssertionStatus();
            ENC0201 = (SingleByte.Encoder) new JIS_X_0201().newEncoder();
            ENC0208 = (DoubleByte.Encoder) new JIS_X_0208().newEncoder();
            ENC0212 = (DoubleByte.Encoder) new JIS_X_0212().newEncoder();
        }

        protected Encoder(Charset charset) {
            this(charset, 3.0f, 3.0f, ENC0201, ENC0208, ENC0212);
        }

        protected Encoder(Charset charset, float f2, float f3, SingleByte.Encoder encoder, DoubleByte.Encoder encoder2, DoubleByte.Encoder encoder3) {
            super(charset, f2, f3);
            this.sgp = new Surrogate.Parser();
            this.enc0201 = encoder;
            this.enc0208 = encoder2;
            this.enc0212 = encoder3;
        }

        @Override // java.nio.charset.CharsetEncoder
        public boolean canEncode(char c2) {
            return (encodeSingle(c2, new byte[3]) == 0 && encodeDouble(c2) == 65533) ? false : true;
        }

        protected int encodeSingle(char c2, byte[] bArr) {
            int iEncode = this.enc0201.encode(c2);
            if (iEncode == 65533) {
                return 0;
            }
            if (iEncode >= 0 && iEncode < 128) {
                bArr[0] = (byte) iEncode;
                return 1;
            }
            bArr[0] = -114;
            bArr[1] = (byte) iEncode;
            return 2;
        }

        protected int encodeDouble(char c2) {
            int iEncodeChar = this.enc0208.encodeChar(c2);
            if (iEncodeChar != 65533) {
                return iEncodeChar + 32896;
            }
            if (this.enc0212 != null) {
                iEncodeChar = this.enc0212.encodeChar(c2);
                if (iEncodeChar != 65533) {
                    iEncodeChar += 9404544;
                }
            }
            return iEncodeChar;
        }

        private CoderResult encodeArrayLoop(CharBuffer charBuffer, ByteBuffer byteBuffer) {
            char[] cArrArray = charBuffer.array();
            int iArrayOffset = charBuffer.arrayOffset() + charBuffer.position();
            int iArrayOffset2 = charBuffer.arrayOffset() + charBuffer.limit();
            if (!$assertionsDisabled && iArrayOffset > iArrayOffset2) {
                throw new AssertionError();
            }
            int i2 = iArrayOffset <= iArrayOffset2 ? iArrayOffset : iArrayOffset2;
            byte[] bArrArray = byteBuffer.array();
            int iArrayOffset3 = byteBuffer.arrayOffset() + byteBuffer.position();
            int iArrayOffset4 = byteBuffer.arrayOffset() + byteBuffer.limit();
            if (!$assertionsDisabled && iArrayOffset3 > iArrayOffset4) {
                throw new AssertionError();
            }
            int i3 = iArrayOffset3 <= iArrayOffset4 ? iArrayOffset3 : iArrayOffset4;
            byte[] bArr = new byte[3];
            while (i2 < iArrayOffset2) {
                try {
                    char c2 = cArrArray[i2];
                    if (Character.isSurrogate(c2)) {
                        if (this.sgp.parse(c2, cArrArray, i2, iArrayOffset2) < 0) {
                            CoderResult coderResultError = this.sgp.error();
                            charBuffer.position(i2 - charBuffer.arrayOffset());
                            byteBuffer.position(i3 - byteBuffer.arrayOffset());
                            return coderResultError;
                        }
                        CoderResult coderResultUnmappableResult = this.sgp.unmappableResult();
                        charBuffer.position(i2 - charBuffer.arrayOffset());
                        byteBuffer.position(i3 - byteBuffer.arrayOffset());
                        return coderResultUnmappableResult;
                    }
                    int iEncodeSingle = encodeSingle(c2, bArr);
                    if (iEncodeSingle == 0) {
                        int iEncodeDouble = encodeDouble(c2);
                        if (iEncodeDouble == 65533) {
                            CoderResult coderResultUnmappableForLength = CoderResult.unmappableForLength(1);
                            charBuffer.position(i2 - charBuffer.arrayOffset());
                            byteBuffer.position(i3 - byteBuffer.arrayOffset());
                            return coderResultUnmappableForLength;
                        }
                        if ((iEncodeDouble & 16711680) == 0) {
                            bArr[0] = (byte) ((iEncodeDouble & NormalizerImpl.CC_MASK) >> 8);
                            bArr[1] = (byte) (iEncodeDouble & 255);
                            iEncodeSingle = 2;
                        } else {
                            bArr[0] = -113;
                            bArr[1] = (byte) ((iEncodeDouble & NormalizerImpl.CC_MASK) >> 8);
                            bArr[2] = (byte) (iEncodeDouble & 255);
                            iEncodeSingle = 3;
                        }
                    }
                    if (iArrayOffset4 - i3 < iEncodeSingle) {
                        CoderResult coderResult = CoderResult.OVERFLOW;
                        charBuffer.position(i2 - charBuffer.arrayOffset());
                        byteBuffer.position(i3 - byteBuffer.arrayOffset());
                        return coderResult;
                    }
                    for (int i4 = 0; i4 < iEncodeSingle; i4++) {
                        int i5 = i3;
                        i3++;
                        bArrArray[i5] = bArr[i4];
                    }
                    i2++;
                } catch (Throwable th) {
                    charBuffer.position(i2 - charBuffer.arrayOffset());
                    byteBuffer.position(i3 - byteBuffer.arrayOffset());
                    throw th;
                }
            }
            CoderResult coderResult2 = CoderResult.UNDERFLOW;
            charBuffer.position(i2 - charBuffer.arrayOffset());
            byteBuffer.position(i3 - byteBuffer.arrayOffset());
            return coderResult2;
        }

        private CoderResult encodeBufferLoop(CharBuffer charBuffer, ByteBuffer byteBuffer) {
            byte[] bArr = new byte[3];
            int iPosition = charBuffer.position();
            while (charBuffer.hasRemaining()) {
                try {
                    char c2 = charBuffer.get();
                    if (Character.isSurrogate(c2)) {
                        if (this.sgp.parse(c2, charBuffer) < 0) {
                            CoderResult coderResultError = this.sgp.error();
                            charBuffer.position(iPosition);
                            return coderResultError;
                        }
                        CoderResult coderResultUnmappableResult = this.sgp.unmappableResult();
                        charBuffer.position(iPosition);
                        return coderResultUnmappableResult;
                    }
                    int iEncodeSingle = encodeSingle(c2, bArr);
                    if (iEncodeSingle == 0) {
                        int iEncodeDouble = encodeDouble(c2);
                        if (iEncodeDouble == 65533) {
                            CoderResult coderResultUnmappableForLength = CoderResult.unmappableForLength(1);
                            charBuffer.position(iPosition);
                            return coderResultUnmappableForLength;
                        }
                        if ((iEncodeDouble & 16711680) == 0) {
                            bArr[0] = (byte) ((iEncodeDouble & NormalizerImpl.CC_MASK) >> 8);
                            bArr[1] = (byte) (iEncodeDouble & 255);
                            iEncodeSingle = 2;
                        } else {
                            bArr[0] = -113;
                            bArr[1] = (byte) ((iEncodeDouble & NormalizerImpl.CC_MASK) >> 8);
                            bArr[2] = (byte) (iEncodeDouble & 255);
                            iEncodeSingle = 3;
                        }
                    }
                    if (byteBuffer.remaining() < iEncodeSingle) {
                        CoderResult coderResult = CoderResult.OVERFLOW;
                        charBuffer.position(iPosition);
                        return coderResult;
                    }
                    for (int i2 = 0; i2 < iEncodeSingle; i2++) {
                        byteBuffer.put(bArr[i2]);
                    }
                    iPosition++;
                } catch (Throwable th) {
                    charBuffer.position(iPosition);
                    throw th;
                }
            }
            CoderResult coderResult2 = CoderResult.UNDERFLOW;
            charBuffer.position(iPosition);
            return coderResult2;
        }

        @Override // java.nio.charset.CharsetEncoder
        protected CoderResult encodeLoop(CharBuffer charBuffer, ByteBuffer byteBuffer) {
            if (charBuffer.hasArray() && byteBuffer.hasArray()) {
                return encodeArrayLoop(charBuffer, byteBuffer);
            }
            return encodeBufferLoop(charBuffer, byteBuffer);
        }
    }
}
