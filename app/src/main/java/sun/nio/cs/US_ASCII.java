package sun.nio.cs;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.CoderResult;
import sun.nio.cs.Surrogate;

/* loaded from: rt.jar:sun/nio/cs/US_ASCII.class */
public class US_ASCII extends Charset implements HistoricallyNamedCharset {
    public US_ASCII() {
        super("US-ASCII", StandardCharsets.aliases_US_ASCII);
    }

    @Override // sun.nio.cs.HistoricallyNamedCharset
    public String historicalName() {
        return "ASCII";
    }

    @Override // java.nio.charset.Charset
    public boolean contains(Charset charset) {
        return charset instanceof US_ASCII;
    }

    @Override // java.nio.charset.Charset
    public CharsetDecoder newDecoder() {
        return new Decoder(this);
    }

    @Override // java.nio.charset.Charset
    public CharsetEncoder newEncoder() {
        return new Encoder(this);
    }

    /* loaded from: rt.jar:sun/nio/cs/US_ASCII$Decoder.class */
    private static class Decoder extends CharsetDecoder implements ArrayDecoder {
        private char repl;
        static final /* synthetic */ boolean $assertionsDisabled;

        static {
            $assertionsDisabled = !US_ASCII.class.desiredAssertionStatus();
        }

        private Decoder(Charset charset) {
            super(charset, 1.0f, 1.0f);
            this.repl = (char) 65533;
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
                    if (b2 < 0) {
                        CoderResult coderResultMalformedForLength = CoderResult.malformedForLength(1);
                        byteBuffer.position(i2 - byteBuffer.arrayOffset());
                        charBuffer.position(i3 - charBuffer.arrayOffset());
                        return coderResultMalformedForLength;
                    }
                    if (i3 >= iArrayOffset4) {
                        CoderResult coderResult = CoderResult.OVERFLOW;
                        byteBuffer.position(i2 - byteBuffer.arrayOffset());
                        charBuffer.position(i3 - charBuffer.arrayOffset());
                        return coderResult;
                    }
                    int i4 = i3;
                    i3++;
                    cArrArray[i4] = (char) b2;
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
                    if (b2 < 0) {
                        CoderResult coderResultMalformedForLength = CoderResult.malformedForLength(1);
                        byteBuffer.position(iPosition);
                        return coderResultMalformedForLength;
                    }
                    if (!charBuffer.hasRemaining()) {
                        CoderResult coderResult = CoderResult.OVERFLOW;
                        byteBuffer.position(iPosition);
                        return coderResult;
                    }
                    charBuffer.put((char) b2);
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

        @Override // java.nio.charset.CharsetDecoder
        protected void implReplaceWith(String str) {
            this.repl = str.charAt(0);
        }

        @Override // sun.nio.cs.ArrayDecoder
        public int decode(byte[] bArr, int i2, int i3, char[] cArr) {
            int i4 = 0;
            int iMin = Math.min(i3, cArr.length);
            while (i4 < iMin) {
                int i5 = i2;
                i2++;
                byte b2 = bArr[i5];
                if (b2 >= 0) {
                    int i6 = i4;
                    i4++;
                    cArr[i6] = (char) b2;
                } else {
                    int i7 = i4;
                    i4++;
                    cArr[i7] = this.repl;
                }
            }
            return i4;
        }
    }

    /* loaded from: rt.jar:sun/nio/cs/US_ASCII$Encoder.class */
    private static class Encoder extends CharsetEncoder implements ArrayEncoder {
        private final Surrogate.Parser sgp;
        private byte repl;
        static final /* synthetic */ boolean $assertionsDisabled;

        static {
            $assertionsDisabled = !US_ASCII.class.desiredAssertionStatus();
        }

        private Encoder(Charset charset) {
            super(charset, 1.0f, 1.0f);
            this.sgp = new Surrogate.Parser();
            this.repl = (byte) 63;
        }

        @Override // java.nio.charset.CharsetEncoder
        public boolean canEncode(char c2) {
            return c2 < 128;
        }

        @Override // java.nio.charset.CharsetEncoder
        public boolean isLegalReplacement(byte[] bArr) {
            return (bArr.length == 1 && bArr[0] >= 0) || super.isLegalReplacement(bArr);
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
            while (i2 < iArrayOffset2) {
                try {
                    char c2 = cArrArray[i2];
                    if (c2 >= 128) {
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
                    if (i3 >= iArrayOffset4) {
                        CoderResult coderResult = CoderResult.OVERFLOW;
                        charBuffer.position(i2 - charBuffer.arrayOffset());
                        byteBuffer.position(i3 - byteBuffer.arrayOffset());
                        return coderResult;
                    }
                    bArrArray[i3] = (byte) c2;
                    i2++;
                    i3++;
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
            int iPosition = charBuffer.position();
            while (charBuffer.hasRemaining()) {
                try {
                    char c2 = charBuffer.get();
                    if (c2 >= 128) {
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
            int iMin = i2 + Math.min(i3, bArr.length);
            while (i2 < iMin) {
                int i5 = i2;
                i2++;
                char c2 = cArr[i5];
                if (c2 < 128) {
                    int i6 = i4;
                    i4++;
                    bArr[i6] = (byte) c2;
                } else {
                    if (Character.isHighSurrogate(c2) && i2 < iMin && Character.isLowSurrogate(cArr[i2])) {
                        if (i3 > bArr.length) {
                            iMin++;
                            i3--;
                        }
                        i2++;
                    }
                    int i7 = i4;
                    i4++;
                    bArr[i7] = this.repl;
                }
            }
            return i4;
        }
    }
}
