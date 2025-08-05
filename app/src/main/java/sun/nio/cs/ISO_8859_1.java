package sun.nio.cs;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.CoderResult;
import org.apache.commons.net.ftp.FTP;
import sun.nio.cs.Surrogate;

/* loaded from: rt.jar:sun/nio/cs/ISO_8859_1.class */
class ISO_8859_1 extends Charset implements HistoricallyNamedCharset {
    public ISO_8859_1() {
        super(FTP.DEFAULT_CONTROL_ENCODING, StandardCharsets.aliases_ISO_8859_1);
    }

    @Override // sun.nio.cs.HistoricallyNamedCharset
    public String historicalName() {
        return "ISO8859_1";
    }

    @Override // java.nio.charset.Charset
    public boolean contains(Charset charset) {
        return (charset instanceof US_ASCII) || (charset instanceof ISO_8859_1);
    }

    @Override // java.nio.charset.Charset
    public CharsetDecoder newDecoder() {
        return new Decoder(this);
    }

    @Override // java.nio.charset.Charset
    public CharsetEncoder newEncoder() {
        return new Encoder(this);
    }

    /* loaded from: rt.jar:sun/nio/cs/ISO_8859_1$Decoder.class */
    private static class Decoder extends CharsetDecoder implements ArrayDecoder {
        static final /* synthetic */ boolean $assertionsDisabled;

        static {
            $assertionsDisabled = !ISO_8859_1.class.desiredAssertionStatus();
        }

        private Decoder(Charset charset) {
            super(charset, 1.0f, 1.0f);
        }

        private CoderResult decodeArrayLoop(ByteBuffer byteBuffer, CharBuffer charBuffer) {
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
                    byte b2 = bArrArray[i2];
                    if (i3 >= iArrayOffset4) {
                        CoderResult coderResult = CoderResult.OVERFLOW;
                        byteBuffer.position(i2 - byteBuffer.arrayOffset());
                        charBuffer.position(i3 - charBuffer.arrayOffset());
                        return coderResult;
                    }
                    int i4 = i3;
                    i3++;
                    cArrArray[i4] = (char) (b2 & 255);
                    i2++;
                } catch (Throwable th) {
                    byteBuffer.position(i2 - byteBuffer.arrayOffset());
                    charBuffer.position(i3 - charBuffer.arrayOffset());
                    throw th;
                }
            }
            CoderResult coderResult2 = CoderResult.UNDERFLOW;
            byteBuffer.position(i2 - byteBuffer.arrayOffset());
            charBuffer.position(i3 - charBuffer.arrayOffset());
            return coderResult2;
        }

        private CoderResult decodeBufferLoop(ByteBuffer byteBuffer, CharBuffer charBuffer) {
            int iPosition = byteBuffer.position();
            while (byteBuffer.hasRemaining()) {
                try {
                    byte b2 = byteBuffer.get();
                    if (!charBuffer.hasRemaining()) {
                        CoderResult coderResult = CoderResult.OVERFLOW;
                        byteBuffer.position(iPosition);
                        return coderResult;
                    }
                    charBuffer.put((char) (b2 & 255));
                    iPosition++;
                } catch (Throwable th) {
                    byteBuffer.position(iPosition);
                    throw th;
                }
            }
            CoderResult coderResult2 = CoderResult.UNDERFLOW;
            byteBuffer.position(iPosition);
            return coderResult2;
        }

        @Override // java.nio.charset.CharsetDecoder
        protected CoderResult decodeLoop(ByteBuffer byteBuffer, CharBuffer charBuffer) {
            if (byteBuffer.hasArray() && charBuffer.hasArray()) {
                return decodeArrayLoop(byteBuffer, charBuffer);
            }
            return decodeBufferLoop(byteBuffer, charBuffer);
        }

        @Override // sun.nio.cs.ArrayDecoder
        public int decode(byte[] bArr, int i2, int i3, char[] cArr) {
            if (i3 > cArr.length) {
                i3 = cArr.length;
            }
            int i4 = 0;
            while (i4 < i3) {
                int i5 = i4;
                i4++;
                int i6 = i2;
                i2++;
                cArr[i5] = (char) (bArr[i6] & 255);
            }
            return i4;
        }
    }

    /* loaded from: rt.jar:sun/nio/cs/ISO_8859_1$Encoder.class */
    private static class Encoder extends CharsetEncoder implements ArrayEncoder {
        private final Surrogate.Parser sgp;
        private byte repl;
        static final /* synthetic */ boolean $assertionsDisabled;

        static {
            $assertionsDisabled = !ISO_8859_1.class.desiredAssertionStatus();
        }

        private Encoder(Charset charset) {
            super(charset, 1.0f, 1.0f);
            this.sgp = new Surrogate.Parser();
            this.repl = (byte) 63;
        }

        @Override // java.nio.charset.CharsetEncoder
        public boolean canEncode(char c2) {
            return c2 <= 255;
        }

        @Override // java.nio.charset.CharsetEncoder
        public boolean isLegalReplacement(byte[] bArr) {
            return true;
        }

        private static int encodeISOArray(char[] cArr, int i2, byte[] bArr, int i3, int i4) {
            int i5 = 0;
            while (i5 < i4) {
                int i6 = i2;
                i2++;
                char c2 = cArr[i6];
                if (c2 > 255) {
                    break;
                }
                int i7 = i3;
                i3++;
                bArr[i7] = (byte) c2;
                i5++;
            }
            return i5;
        }

        private CoderResult encodeArrayLoop(CharBuffer charBuffer, ByteBuffer byteBuffer) {
            int iEncodeISOArray;
            char[] cArrArray = charBuffer.array();
            int iArrayOffset = charBuffer.arrayOffset();
            int iPosition = iArrayOffset + charBuffer.position();
            int iLimit = iArrayOffset + charBuffer.limit();
            if (!$assertionsDisabled && iPosition > iLimit) {
                throw new AssertionError();
            }
            int i2 = iPosition <= iLimit ? iPosition : iLimit;
            byte[] bArrArray = byteBuffer.array();
            int iArrayOffset2 = byteBuffer.arrayOffset();
            int iPosition2 = iArrayOffset2 + byteBuffer.position();
            int iLimit2 = iArrayOffset2 + byteBuffer.limit();
            if (!$assertionsDisabled && iPosition2 > iLimit2) {
                throw new AssertionError();
            }
            int i3 = iPosition2 <= iLimit2 ? iPosition2 : iLimit2;
            int i4 = iLimit2 - i3;
            int i5 = iLimit - i2;
            int i6 = i4 < i5 ? i4 : i5;
            if (i6 <= 0) {
                iEncodeISOArray = 0;
            } else {
                try {
                    iEncodeISOArray = encodeISOArray(cArrArray, i2, bArrArray, i3, i6);
                } catch (Throwable th) {
                    charBuffer.position(i2 - iArrayOffset);
                    byteBuffer.position(i3 - iArrayOffset2);
                    throw th;
                }
            }
            int i7 = iEncodeISOArray;
            int i8 = i2 + i7;
            int i9 = i3 + i7;
            if (i7 != i6) {
                if (this.sgp.parse(cArrArray[i8], cArrArray, i8, iLimit) < 0) {
                    CoderResult coderResultError = this.sgp.error();
                    charBuffer.position(i8 - iArrayOffset);
                    byteBuffer.position(i9 - iArrayOffset2);
                    return coderResultError;
                }
                CoderResult coderResultUnmappableResult = this.sgp.unmappableResult();
                charBuffer.position(i8 - iArrayOffset);
                byteBuffer.position(i9 - iArrayOffset2);
                return coderResultUnmappableResult;
            }
            if (i6 < i5) {
                CoderResult coderResult = CoderResult.OVERFLOW;
                charBuffer.position(i8 - iArrayOffset);
                byteBuffer.position(i9 - iArrayOffset2);
                return coderResult;
            }
            CoderResult coderResult2 = CoderResult.UNDERFLOW;
            charBuffer.position(i8 - iArrayOffset);
            byteBuffer.position(i9 - iArrayOffset2);
            return coderResult2;
        }

        private CoderResult encodeBufferLoop(CharBuffer charBuffer, ByteBuffer byteBuffer) {
            int iPosition = charBuffer.position();
            while (charBuffer.hasRemaining()) {
                try {
                    char c2 = charBuffer.get();
                    if (c2 > 255) {
                        if (this.sgp.parse(c2, charBuffer) < 0) {
                            CoderResult coderResultError = this.sgp.error();
                            charBuffer.position(iPosition);
                            return coderResultError;
                        }
                        CoderResult coderResultUnmappableResult = this.sgp.unmappableResult();
                        charBuffer.position(iPosition);
                        return coderResultUnmappableResult;
                    }
                    if (!byteBuffer.hasRemaining()) {
                        CoderResult coderResult = CoderResult.OVERFLOW;
                        charBuffer.position(iPosition);
                        return coderResult;
                    }
                    byteBuffer.put((byte) c2);
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

        @Override // java.nio.charset.CharsetEncoder
        protected void implReplaceWith(byte[] bArr) {
            this.repl = bArr[0];
        }

        @Override // sun.nio.cs.ArrayEncoder
        public int encode(char[] cArr, int i2, int i3, byte[] bArr) {
            int i4 = 0;
            int iMin = Math.min(i3, bArr.length);
            int i5 = i2 + iMin;
            while (i2 < i5) {
                int iEncodeISOArray = iMin <= 0 ? 0 : encodeISOArray(cArr, i2, bArr, i4, iMin);
                i2 += iEncodeISOArray;
                i4 += iEncodeISOArray;
                if (iEncodeISOArray != iMin) {
                    i2++;
                    if (Character.isHighSurrogate(cArr[i2]) && i2 < i5 && Character.isLowSurrogate(cArr[i2])) {
                        if (i3 > bArr.length) {
                            i5++;
                            i3--;
                        }
                        i2++;
                    }
                    i4++;
                    bArr[i4] = this.repl;
                    iMin = Math.min(i5 - i2, bArr.length - i4);
                }
            }
            return i4;
        }
    }
}
