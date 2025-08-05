package sun.nio.cs.ext;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.CoderResult;
import java.nio.charset.CodingErrorAction;
import sun.nio.cs.HistoricallyNamedCharset;
import sun.nio.cs.Surrogate;
import sun.nio.cs.US_ASCII;
import sun.nio.cs.ext.DoubleByte;

/* loaded from: charsets.jar:sun/nio/cs/ext/ISO2022_JP.class */
public class ISO2022_JP extends Charset implements HistoricallyNamedCharset {
    private static final int ASCII = 0;
    private static final int JISX0201_1976 = 1;
    private static final int JISX0208_1978 = 2;
    private static final int JISX0208_1983 = 3;
    private static final int JISX0212_1990 = 4;
    private static final int JISX0201_1976_KANA = 5;
    private static final int SHIFTOUT = 6;
    private static final int ESC = 27;
    private static final int SO = 14;
    private static final int SI = 15;

    public ISO2022_JP() {
        super("ISO-2022-JP", ExtendedCharsets.aliasesFor("ISO-2022-JP"));
    }

    protected ISO2022_JP(String str, String[] strArr) {
        super(str, strArr);
    }

    @Override // sun.nio.cs.HistoricallyNamedCharset
    public String historicalName() {
        return "ISO2022JP";
    }

    @Override // java.nio.charset.Charset
    public boolean contains(Charset charset) {
        return (charset instanceof JIS_X_0201) || (charset instanceof US_ASCII) || (charset instanceof JIS_X_0208) || (charset instanceof ISO2022_JP);
    }

    @Override // java.nio.charset.Charset
    public CharsetDecoder newDecoder() {
        return new Decoder(this);
    }

    @Override // java.nio.charset.Charset
    public CharsetEncoder newEncoder() {
        return new Encoder(this);
    }

    protected boolean doSBKANA() {
        return true;
    }

    /* loaded from: charsets.jar:sun/nio/cs/ext/ISO2022_JP$Decoder.class */
    static class Decoder extends CharsetDecoder implements DelegatableDecoder {
        static final DoubleByte.Decoder DEC0208;
        private int currentState;
        private int previousState;
        private DoubleByte.Decoder dec0208;
        private DoubleByte.Decoder dec0212;
        static final /* synthetic */ boolean $assertionsDisabled;

        static {
            $assertionsDisabled = !ISO2022_JP.class.desiredAssertionStatus();
            DEC0208 = (DoubleByte.Decoder) new JIS_X_0208().newDecoder();
        }

        private Decoder(Charset charset) {
            this(charset, DEC0208, null);
        }

        protected Decoder(Charset charset, DoubleByte.Decoder decoder, DoubleByte.Decoder decoder2) {
            super(charset, 0.5f, 1.0f);
            this.dec0208 = decoder;
            this.dec0212 = decoder2;
            this.currentState = 0;
            this.previousState = 0;
        }

        @Override // java.nio.charset.CharsetDecoder
        public void implReset() {
            this.currentState = 0;
            this.previousState = 0;
        }

        /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
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
                    int i4 = bArrArray[i2] & 255;
                    int i5 = 1;
                    if ((i4 & 128) != 0) {
                        CoderResult coderResultMalformedForLength = CoderResult.malformedForLength(1);
                        byteBuffer.position(i2 - byteBuffer.arrayOffset());
                        charBuffer.position(i3 - charBuffer.arrayOffset());
                        return coderResultMalformedForLength;
                    }
                    if (i4 == 27 || i4 == 14 || i4 == 15) {
                        if (i4 == 27) {
                            if (i2 + 1 + 2 > iArrayOffset2) {
                                CoderResult coderResult = CoderResult.UNDERFLOW;
                                byteBuffer.position(i2 - byteBuffer.arrayOffset());
                                charBuffer.position(i3 - charBuffer.arrayOffset());
                                return coderResult;
                            }
                            int i6 = 1 + 1;
                            int i7 = bArrArray[i2 + 1] & 255;
                            if (i7 == 40) {
                                i5 = i6 + 1;
                                int i8 = bArrArray[i2 + i6] & 255;
                                if (i8 == 66) {
                                    this.currentState = 0;
                                } else if (i8 == 74) {
                                    this.currentState = 1;
                                } else {
                                    if (i8 != 73) {
                                        CoderResult coderResultMalformedForLength2 = CoderResult.malformedForLength(i5);
                                        byteBuffer.position(i2 - byteBuffer.arrayOffset());
                                        charBuffer.position(i3 - charBuffer.arrayOffset());
                                        return coderResultMalformedForLength2;
                                    }
                                    this.currentState = 5;
                                }
                            } else {
                                if (i7 != 36) {
                                    CoderResult coderResultMalformedForLength3 = CoderResult.malformedForLength(i6);
                                    byteBuffer.position(i2 - byteBuffer.arrayOffset());
                                    charBuffer.position(i3 - charBuffer.arrayOffset());
                                    return coderResultMalformedForLength3;
                                }
                                i5 = i6 + 1;
                                int i9 = bArrArray[i2 + i6] & 255;
                                if (i9 == 64) {
                                    this.currentState = 2;
                                } else if (i9 == 66) {
                                    this.currentState = 3;
                                } else {
                                    if (i9 != 40 || this.dec0212 == null) {
                                        CoderResult coderResultMalformedForLength4 = CoderResult.malformedForLength(i5);
                                        byteBuffer.position(i2 - byteBuffer.arrayOffset());
                                        charBuffer.position(i3 - charBuffer.arrayOffset());
                                        return coderResultMalformedForLength4;
                                    }
                                    if (i2 + i5 + 1 > iArrayOffset2) {
                                        CoderResult coderResult2 = CoderResult.UNDERFLOW;
                                        byteBuffer.position(i2 - byteBuffer.arrayOffset());
                                        charBuffer.position(i3 - charBuffer.arrayOffset());
                                        return coderResult2;
                                    }
                                    i5++;
                                    if ((bArrArray[i2 + i5] & 255) != 68) {
                                        CoderResult coderResultMalformedForLength5 = CoderResult.malformedForLength(i5);
                                        byteBuffer.position(i2 - byteBuffer.arrayOffset());
                                        charBuffer.position(i3 - charBuffer.arrayOffset());
                                        return coderResultMalformedForLength5;
                                    }
                                    this.currentState = 4;
                                }
                            }
                        } else if (i4 == 14) {
                            this.previousState = this.currentState;
                            this.currentState = 6;
                        } else if (i4 == 15) {
                            this.currentState = this.previousState;
                        }
                        i2 += i5;
                    } else {
                        if (i3 + 1 > iArrayOffset4) {
                            CoderResult coderResult3 = CoderResult.OVERFLOW;
                            byteBuffer.position(i2 - byteBuffer.arrayOffset());
                            charBuffer.position(i3 - charBuffer.arrayOffset());
                            return coderResult3;
                        }
                        switch (this.currentState) {
                            case 0:
                                int i10 = i3;
                                i3++;
                                cArrArray[i10] = (char) (i4 & 255);
                                i2 += i5;
                            case 1:
                                switch (i4) {
                                    case 92:
                                        int i11 = i3;
                                        i3++;
                                        cArrArray[i11] = 165;
                                        break;
                                    case 126:
                                        int i12 = i3;
                                        i3++;
                                        cArrArray[i12] = 8254;
                                        break;
                                    default:
                                        int i13 = i3;
                                        i3++;
                                        cArrArray[i13] = (char) i4;
                                        break;
                                }
                                i2 += i5;
                            case 2:
                            case 3:
                                if (i2 + 1 + 1 > iArrayOffset2) {
                                    CoderResult coderResult4 = CoderResult.UNDERFLOW;
                                    byteBuffer.position(i2 - byteBuffer.arrayOffset());
                                    charBuffer.position(i3 - charBuffer.arrayOffset());
                                    return coderResult4;
                                }
                                i5 = 1 + 1;
                                char cDecodeDouble = this.dec0208.decodeDouble(i4, bArrArray[i2 + 1] & 255);
                                if (cDecodeDouble == 65533) {
                                    CoderResult coderResultUnmappableForLength = CoderResult.unmappableForLength(i5);
                                    byteBuffer.position(i2 - byteBuffer.arrayOffset());
                                    charBuffer.position(i3 - charBuffer.arrayOffset());
                                    return coderResultUnmappableForLength;
                                }
                                int i14 = i3;
                                i3++;
                                cArrArray[i14] = cDecodeDouble;
                                i2 += i5;
                            case 4:
                                if (i2 + 1 + 1 > iArrayOffset2) {
                                    CoderResult coderResult5 = CoderResult.UNDERFLOW;
                                    byteBuffer.position(i2 - byteBuffer.arrayOffset());
                                    charBuffer.position(i3 - charBuffer.arrayOffset());
                                    return coderResult5;
                                }
                                i5 = 1 + 1;
                                char cDecodeDouble2 = this.dec0212.decodeDouble(i4, bArrArray[i2 + 1] & 255);
                                if (cDecodeDouble2 == 65533) {
                                    CoderResult coderResultUnmappableForLength2 = CoderResult.unmappableForLength(i5);
                                    byteBuffer.position(i2 - byteBuffer.arrayOffset());
                                    charBuffer.position(i3 - charBuffer.arrayOffset());
                                    return coderResultUnmappableForLength2;
                                }
                                int i15 = i3;
                                i3++;
                                cArrArray[i15] = cDecodeDouble2;
                                i2 += i5;
                            case 5:
                            case 6:
                                if (i4 > 95) {
                                    CoderResult coderResultMalformedForLength6 = CoderResult.malformedForLength(1);
                                    byteBuffer.position(i2 - byteBuffer.arrayOffset());
                                    charBuffer.position(i3 - charBuffer.arrayOffset());
                                    return coderResultMalformedForLength6;
                                }
                                int i16 = i3;
                                i3++;
                                cArrArray[i16] = (char) (i4 + 65344);
                                i2 += i5;
                            default:
                                i2 += i5;
                        }
                    }
                } catch (Throwable th) {
                    byteBuffer.position(i2 - byteBuffer.arrayOffset());
                    charBuffer.position(i3 - charBuffer.arrayOffset());
                    throw th;
                }
            }
            CoderResult coderResult6 = CoderResult.UNDERFLOW;
            byteBuffer.position(i2 - byteBuffer.arrayOffset());
            charBuffer.position(i3 - charBuffer.arrayOffset());
            return coderResult6;
        }

        /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
        private CoderResult decodeBufferLoop(ByteBuffer byteBuffer, CharBuffer charBuffer) {
            int iPosition = byteBuffer.position();
            while (byteBuffer.hasRemaining()) {
                try {
                    int i2 = byteBuffer.get() & 255;
                    int i3 = 1;
                    if ((i2 & 128) != 0) {
                        CoderResult coderResultMalformedForLength = CoderResult.malformedForLength(1);
                        byteBuffer.position(iPosition);
                        return coderResultMalformedForLength;
                    }
                    if (i2 == 27 || i2 == 14 || i2 == 15) {
                        if (i2 == 27) {
                            if (byteBuffer.remaining() < 2) {
                                CoderResult coderResult = CoderResult.UNDERFLOW;
                                byteBuffer.position(iPosition);
                                return coderResult;
                            }
                            int i4 = byteBuffer.get() & 255;
                            int i5 = 1 + 1;
                            if (i4 == 40) {
                                int i6 = byteBuffer.get() & 255;
                                i3 = i5 + 1;
                                if (i6 == 66) {
                                    this.currentState = 0;
                                } else if (i6 == 74) {
                                    this.currentState = 1;
                                } else {
                                    if (i6 != 73) {
                                        CoderResult coderResultMalformedForLength2 = CoderResult.malformedForLength(i3);
                                        byteBuffer.position(iPosition);
                                        return coderResultMalformedForLength2;
                                    }
                                    this.currentState = 5;
                                }
                            } else {
                                if (i4 != 36) {
                                    CoderResult coderResultMalformedForLength3 = CoderResult.malformedForLength(i5);
                                    byteBuffer.position(iPosition);
                                    return coderResultMalformedForLength3;
                                }
                                int i7 = byteBuffer.get() & 255;
                                i3 = i5 + 1;
                                if (i7 == 64) {
                                    this.currentState = 2;
                                } else if (i7 == 66) {
                                    this.currentState = 3;
                                } else {
                                    if (i7 != 40 || this.dec0212 == null) {
                                        CoderResult coderResultMalformedForLength4 = CoderResult.malformedForLength(i3);
                                        byteBuffer.position(iPosition);
                                        return coderResultMalformedForLength4;
                                    }
                                    if (!byteBuffer.hasRemaining()) {
                                        CoderResult coderResult2 = CoderResult.UNDERFLOW;
                                        byteBuffer.position(iPosition);
                                        return coderResult2;
                                    }
                                    i3++;
                                    if ((byteBuffer.get() & 255) != 68) {
                                        CoderResult coderResultMalformedForLength5 = CoderResult.malformedForLength(i3);
                                        byteBuffer.position(iPosition);
                                        return coderResultMalformedForLength5;
                                    }
                                    this.currentState = 4;
                                }
                            }
                        } else if (i2 == 14) {
                            this.previousState = this.currentState;
                            this.currentState = 6;
                        } else if (i2 == 15) {
                            this.currentState = this.previousState;
                        }
                        iPosition += i3;
                    } else {
                        if (!charBuffer.hasRemaining()) {
                            CoderResult coderResult3 = CoderResult.OVERFLOW;
                            byteBuffer.position(iPosition);
                            return coderResult3;
                        }
                        switch (this.currentState) {
                            case 0:
                                charBuffer.put((char) (i2 & 255));
                                iPosition += i3;
                            case 1:
                                switch (i2) {
                                    case 92:
                                        charBuffer.put((char) 165);
                                        break;
                                    case 126:
                                        charBuffer.put((char) 8254);
                                        break;
                                    default:
                                        charBuffer.put((char) i2);
                                        break;
                                }
                                iPosition += i3;
                            case 2:
                            case 3:
                                if (!byteBuffer.hasRemaining()) {
                                    CoderResult coderResult4 = CoderResult.UNDERFLOW;
                                    byteBuffer.position(iPosition);
                                    return coderResult4;
                                }
                                i3 = 1 + 1;
                                char cDecodeDouble = this.dec0208.decodeDouble(i2, byteBuffer.get() & 255);
                                if (cDecodeDouble == 65533) {
                                    CoderResult coderResultUnmappableForLength = CoderResult.unmappableForLength(i3);
                                    byteBuffer.position(iPosition);
                                    return coderResultUnmappableForLength;
                                }
                                charBuffer.put(cDecodeDouble);
                                iPosition += i3;
                            case 4:
                                if (!byteBuffer.hasRemaining()) {
                                    CoderResult coderResult5 = CoderResult.UNDERFLOW;
                                    byteBuffer.position(iPosition);
                                    return coderResult5;
                                }
                                i3 = 1 + 1;
                                char cDecodeDouble2 = this.dec0212.decodeDouble(i2, byteBuffer.get() & 255);
                                if (cDecodeDouble2 == 65533) {
                                    CoderResult coderResultUnmappableForLength2 = CoderResult.unmappableForLength(i3);
                                    byteBuffer.position(iPosition);
                                    return coderResultUnmappableForLength2;
                                }
                                charBuffer.put(cDecodeDouble2);
                                iPosition += i3;
                            case 5:
                            case 6:
                                if (i2 > 95) {
                                    CoderResult coderResultMalformedForLength6 = CoderResult.malformedForLength(1);
                                    byteBuffer.position(iPosition);
                                    return coderResultMalformedForLength6;
                                }
                                charBuffer.put((char) (i2 + 65344));
                                iPosition += i3;
                            default:
                                iPosition += i3;
                        }
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
        public CoderResult decodeLoop(ByteBuffer byteBuffer, CharBuffer charBuffer) {
            if (byteBuffer.hasArray() && charBuffer.hasArray()) {
                return decodeArrayLoop(byteBuffer, charBuffer);
            }
            return decodeBufferLoop(byteBuffer, charBuffer);
        }

        @Override // java.nio.charset.CharsetDecoder
        public CoderResult implFlush(CharBuffer charBuffer) {
            return super.implFlush(charBuffer);
        }
    }

    /* loaded from: charsets.jar:sun/nio/cs/ext/ISO2022_JP$Encoder.class */
    static class Encoder extends CharsetEncoder {
        static final DoubleByte.Encoder ENC0208;
        private static byte[] repl;
        private int currentMode;
        private int replaceMode;
        private DoubleByte.Encoder enc0208;
        private DoubleByte.Encoder enc0212;
        private boolean doSBKANA;
        private final Surrogate.Parser sgp;
        static final /* synthetic */ boolean $assertionsDisabled;

        static {
            $assertionsDisabled = !ISO2022_JP.class.desiredAssertionStatus();
            ENC0208 = (DoubleByte.Encoder) new JIS_X_0208().newEncoder();
            repl = new byte[]{33, 41};
        }

        private Encoder(Charset charset) {
            this(charset, ENC0208, null, true);
        }

        Encoder(Charset charset, DoubleByte.Encoder encoder, DoubleByte.Encoder encoder2, boolean z2) {
            super(charset, 4.0f, encoder2 != null ? 9.0f : 8.0f, repl);
            this.currentMode = 0;
            this.replaceMode = 3;
            this.sgp = new Surrogate.Parser();
            this.enc0208 = encoder;
            this.enc0212 = encoder2;
            this.doSBKANA = z2;
        }

        protected int encodeSingle(char c2) {
            return -1;
        }

        @Override // java.nio.charset.CharsetEncoder
        protected void implReset() {
            this.currentMode = 0;
        }

        @Override // java.nio.charset.CharsetEncoder
        protected void implReplaceWith(byte[] bArr) {
            if (bArr.length == 1) {
                this.replaceMode = 0;
            } else if (bArr.length == 2) {
                this.replaceMode = 3;
            }
        }

        @Override // java.nio.charset.CharsetEncoder
        protected CoderResult implFlush(ByteBuffer byteBuffer) {
            if (this.currentMode != 0) {
                if (byteBuffer.remaining() < 3) {
                    return CoderResult.OVERFLOW;
                }
                byteBuffer.put((byte) 27);
                byteBuffer.put((byte) 40);
                byteBuffer.put((byte) 66);
                this.currentMode = 0;
            }
            return CoderResult.UNDERFLOW;
        }

        @Override // java.nio.charset.CharsetEncoder
        public boolean canEncode(char c2) {
            return c2 <= 127 || (c2 >= 65377 && c2 <= 65439) || c2 == 165 || c2 == 8254 || this.enc0208.canEncode(c2) || (this.enc0212 != null && this.enc0212.canEncode(c2));
        }

        private CoderResult encodeArrayLoop(CharBuffer charBuffer, ByteBuffer byteBuffer) {
            int iEncodeChar;
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
                    if (c2 <= 127) {
                        if (this.currentMode != 0) {
                            if (iArrayOffset4 - i3 < 3) {
                                CoderResult coderResult = CoderResult.OVERFLOW;
                                charBuffer.position(i2 - charBuffer.arrayOffset());
                                byteBuffer.position(i3 - byteBuffer.arrayOffset());
                                return coderResult;
                            }
                            int i4 = i3;
                            int i5 = i3 + 1;
                            bArrArray[i4] = 27;
                            int i6 = i5 + 1;
                            bArrArray[i5] = 40;
                            i3 = i6 + 1;
                            bArrArray[i6] = 66;
                            this.currentMode = 0;
                        }
                        if (iArrayOffset4 - i3 < 1) {
                            CoderResult coderResult2 = CoderResult.OVERFLOW;
                            charBuffer.position(i2 - charBuffer.arrayOffset());
                            byteBuffer.position(i3 - byteBuffer.arrayOffset());
                            return coderResult2;
                        }
                        int i7 = i3;
                        i3++;
                        bArrArray[i7] = (byte) c2;
                    } else if (c2 >= 65377 && c2 <= 65439 && this.doSBKANA) {
                        if (this.currentMode != 5) {
                            if (iArrayOffset4 - i3 < 3) {
                                CoderResult coderResult3 = CoderResult.OVERFLOW;
                                charBuffer.position(i2 - charBuffer.arrayOffset());
                                byteBuffer.position(i3 - byteBuffer.arrayOffset());
                                return coderResult3;
                            }
                            int i8 = i3;
                            int i9 = i3 + 1;
                            bArrArray[i8] = 27;
                            int i10 = i9 + 1;
                            bArrArray[i9] = 40;
                            i3 = i10 + 1;
                            bArrArray[i10] = 73;
                            this.currentMode = 5;
                        }
                        if (iArrayOffset4 - i3 < 1) {
                            CoderResult coderResult4 = CoderResult.OVERFLOW;
                            charBuffer.position(i2 - charBuffer.arrayOffset());
                            byteBuffer.position(i3 - byteBuffer.arrayOffset());
                            return coderResult4;
                        }
                        int i11 = i3;
                        i3++;
                        bArrArray[i11] = (byte) (c2 - 65344);
                    } else if (c2 == 165 || c2 == 8254) {
                        if (this.currentMode != 1) {
                            if (iArrayOffset4 - i3 < 3) {
                                CoderResult coderResult5 = CoderResult.OVERFLOW;
                                charBuffer.position(i2 - charBuffer.arrayOffset());
                                byteBuffer.position(i3 - byteBuffer.arrayOffset());
                                return coderResult5;
                            }
                            int i12 = i3;
                            int i13 = i3 + 1;
                            bArrArray[i12] = 27;
                            int i14 = i13 + 1;
                            bArrArray[i13] = 40;
                            i3 = i14 + 1;
                            bArrArray[i14] = 74;
                            this.currentMode = 1;
                        }
                        if (iArrayOffset4 - i3 < 1) {
                            CoderResult coderResult6 = CoderResult.OVERFLOW;
                            charBuffer.position(i2 - charBuffer.arrayOffset());
                            byteBuffer.position(i3 - byteBuffer.arrayOffset());
                            return coderResult6;
                        }
                        int i15 = i3;
                        i3++;
                        bArrArray[i15] = c2 == 165 ? (byte) 92 : (byte) 126;
                    } else {
                        int iEncodeChar2 = this.enc0208.encodeChar(c2);
                        if (iEncodeChar2 != 65533) {
                            if (this.currentMode != 3) {
                                if (iArrayOffset4 - i3 < 3) {
                                    CoderResult coderResult7 = CoderResult.OVERFLOW;
                                    charBuffer.position(i2 - charBuffer.arrayOffset());
                                    byteBuffer.position(i3 - byteBuffer.arrayOffset());
                                    return coderResult7;
                                }
                                int i16 = i3;
                                int i17 = i3 + 1;
                                bArrArray[i16] = 27;
                                int i18 = i17 + 1;
                                bArrArray[i17] = 36;
                                i3 = i18 + 1;
                                bArrArray[i18] = 66;
                                this.currentMode = 3;
                            }
                            if (iArrayOffset4 - i3 < 2) {
                                CoderResult coderResult8 = CoderResult.OVERFLOW;
                                charBuffer.position(i2 - charBuffer.arrayOffset());
                                byteBuffer.position(i3 - byteBuffer.arrayOffset());
                                return coderResult8;
                            }
                            int i19 = i3;
                            int i20 = i3 + 1;
                            bArrArray[i19] = (byte) (iEncodeChar2 >> 8);
                            i3 = i20 + 1;
                            bArrArray[i20] = (byte) (iEncodeChar2 & 255);
                        } else {
                            if (this.enc0212 == null || (iEncodeChar = this.enc0212.encodeChar(c2)) == 65533) {
                                if (Character.isSurrogate(c2) && this.sgp.parse(c2, cArrArray, i2, iArrayOffset2) < 0) {
                                    CoderResult coderResultError = this.sgp.error();
                                    charBuffer.position(i2 - charBuffer.arrayOffset());
                                    byteBuffer.position(i3 - byteBuffer.arrayOffset());
                                    return coderResultError;
                                }
                                if (unmappableCharacterAction() == CodingErrorAction.REPLACE && this.currentMode != this.replaceMode) {
                                    if (iArrayOffset4 - i3 < 3) {
                                        CoderResult coderResult9 = CoderResult.OVERFLOW;
                                        charBuffer.position(i2 - charBuffer.arrayOffset());
                                        byteBuffer.position(i3 - byteBuffer.arrayOffset());
                                        return coderResult9;
                                    }
                                    if (this.replaceMode == 0) {
                                        int i21 = i3;
                                        int i22 = i3 + 1;
                                        bArrArray[i21] = 27;
                                        int i23 = i22 + 1;
                                        bArrArray[i22] = 40;
                                        i3 = i23 + 1;
                                        bArrArray[i23] = 66;
                                    } else {
                                        int i24 = i3;
                                        int i25 = i3 + 1;
                                        bArrArray[i24] = 27;
                                        int i26 = i25 + 1;
                                        bArrArray[i25] = 36;
                                        i3 = i26 + 1;
                                        bArrArray[i26] = 66;
                                    }
                                    this.currentMode = this.replaceMode;
                                }
                                if (Character.isSurrogate(c2)) {
                                    CoderResult coderResultUnmappableResult = this.sgp.unmappableResult();
                                    charBuffer.position(i2 - charBuffer.arrayOffset());
                                    byteBuffer.position(i3 - byteBuffer.arrayOffset());
                                    return coderResultUnmappableResult;
                                }
                                CoderResult coderResultUnmappableForLength = CoderResult.unmappableForLength(1);
                                charBuffer.position(i2 - charBuffer.arrayOffset());
                                byteBuffer.position(i3 - byteBuffer.arrayOffset());
                                return coderResultUnmappableForLength;
                            }
                            if (this.currentMode != 4) {
                                if (iArrayOffset4 - i3 < 4) {
                                    CoderResult coderResult10 = CoderResult.OVERFLOW;
                                    charBuffer.position(i2 - charBuffer.arrayOffset());
                                    byteBuffer.position(i3 - byteBuffer.arrayOffset());
                                    return coderResult10;
                                }
                                int i27 = i3;
                                int i28 = i3 + 1;
                                bArrArray[i27] = 27;
                                int i29 = i28 + 1;
                                bArrArray[i28] = 36;
                                int i30 = i29 + 1;
                                bArrArray[i29] = 40;
                                i3 = i30 + 1;
                                bArrArray[i30] = 68;
                                this.currentMode = 4;
                            }
                            if (iArrayOffset4 - i3 < 2) {
                                CoderResult coderResult11 = CoderResult.OVERFLOW;
                                charBuffer.position(i2 - charBuffer.arrayOffset());
                                byteBuffer.position(i3 - byteBuffer.arrayOffset());
                                return coderResult11;
                            }
                            int i31 = i3;
                            int i32 = i3 + 1;
                            bArrArray[i31] = (byte) (iEncodeChar >> 8);
                            i3 = i32 + 1;
                            bArrArray[i32] = (byte) (iEncodeChar & 255);
                        }
                    }
                    i2++;
                } catch (Throwable th) {
                    charBuffer.position(i2 - charBuffer.arrayOffset());
                    byteBuffer.position(i3 - byteBuffer.arrayOffset());
                    throw th;
                }
            }
            CoderResult coderResult12 = CoderResult.UNDERFLOW;
            charBuffer.position(i2 - charBuffer.arrayOffset());
            byteBuffer.position(i3 - byteBuffer.arrayOffset());
            return coderResult12;
        }

        private CoderResult encodeBufferLoop(CharBuffer charBuffer, ByteBuffer byteBuffer) {
            int iEncodeChar;
            int iPosition = charBuffer.position();
            while (charBuffer.hasRemaining()) {
                try {
                    char c2 = charBuffer.get();
                    if (c2 <= 127) {
                        if (this.currentMode != 0) {
                            if (byteBuffer.remaining() < 3) {
                                CoderResult coderResult = CoderResult.OVERFLOW;
                                charBuffer.position(iPosition);
                                return coderResult;
                            }
                            byteBuffer.put((byte) 27);
                            byteBuffer.put((byte) 40);
                            byteBuffer.put((byte) 66);
                            this.currentMode = 0;
                        }
                        if (byteBuffer.remaining() < 1) {
                            CoderResult coderResult2 = CoderResult.OVERFLOW;
                            charBuffer.position(iPosition);
                            return coderResult2;
                        }
                        byteBuffer.put((byte) c2);
                    } else if (c2 >= 65377 && c2 <= 65439 && this.doSBKANA) {
                        if (this.currentMode != 5) {
                            if (byteBuffer.remaining() < 3) {
                                CoderResult coderResult3 = CoderResult.OVERFLOW;
                                charBuffer.position(iPosition);
                                return coderResult3;
                            }
                            byteBuffer.put((byte) 27);
                            byteBuffer.put((byte) 40);
                            byteBuffer.put((byte) 73);
                            this.currentMode = 5;
                        }
                        if (byteBuffer.remaining() < 1) {
                            CoderResult coderResult4 = CoderResult.OVERFLOW;
                            charBuffer.position(iPosition);
                            return coderResult4;
                        }
                        byteBuffer.put((byte) (c2 - 65344));
                    } else if (c2 == 165 || c2 == 8254) {
                        if (this.currentMode != 1) {
                            if (byteBuffer.remaining() < 3) {
                                CoderResult coderResult5 = CoderResult.OVERFLOW;
                                charBuffer.position(iPosition);
                                return coderResult5;
                            }
                            byteBuffer.put((byte) 27);
                            byteBuffer.put((byte) 40);
                            byteBuffer.put((byte) 74);
                            this.currentMode = 1;
                        }
                        if (byteBuffer.remaining() < 1) {
                            CoderResult coderResult6 = CoderResult.OVERFLOW;
                            charBuffer.position(iPosition);
                            return coderResult6;
                        }
                        byteBuffer.put(c2 == 165 ? (byte) 92 : (byte) 126);
                    } else {
                        int iEncodeChar2 = this.enc0208.encodeChar(c2);
                        if (iEncodeChar2 != 65533) {
                            if (this.currentMode != 3) {
                                if (byteBuffer.remaining() < 3) {
                                    CoderResult coderResult7 = CoderResult.OVERFLOW;
                                    charBuffer.position(iPosition);
                                    return coderResult7;
                                }
                                byteBuffer.put((byte) 27);
                                byteBuffer.put((byte) 36);
                                byteBuffer.put((byte) 66);
                                this.currentMode = 3;
                            }
                            if (byteBuffer.remaining() < 2) {
                                CoderResult coderResult8 = CoderResult.OVERFLOW;
                                charBuffer.position(iPosition);
                                return coderResult8;
                            }
                            byteBuffer.put((byte) (iEncodeChar2 >> 8));
                            byteBuffer.put((byte) (iEncodeChar2 & 255));
                        } else {
                            if (this.enc0212 == null || (iEncodeChar = this.enc0212.encodeChar(c2)) == 65533) {
                                if (Character.isSurrogate(c2) && this.sgp.parse(c2, charBuffer) < 0) {
                                    CoderResult coderResultError = this.sgp.error();
                                    charBuffer.position(iPosition);
                                    return coderResultError;
                                }
                                if (unmappableCharacterAction() == CodingErrorAction.REPLACE && this.currentMode != this.replaceMode) {
                                    if (byteBuffer.remaining() < 3) {
                                        CoderResult coderResult9 = CoderResult.OVERFLOW;
                                        charBuffer.position(iPosition);
                                        return coderResult9;
                                    }
                                    if (this.replaceMode == 0) {
                                        byteBuffer.put((byte) 27);
                                        byteBuffer.put((byte) 40);
                                        byteBuffer.put((byte) 66);
                                    } else {
                                        byteBuffer.put((byte) 27);
                                        byteBuffer.put((byte) 36);
                                        byteBuffer.put((byte) 66);
                                    }
                                    this.currentMode = this.replaceMode;
                                }
                                if (Character.isSurrogate(c2)) {
                                    CoderResult coderResultUnmappableResult = this.sgp.unmappableResult();
                                    charBuffer.position(iPosition);
                                    return coderResultUnmappableResult;
                                }
                                CoderResult coderResultUnmappableForLength = CoderResult.unmappableForLength(1);
                                charBuffer.position(iPosition);
                                return coderResultUnmappableForLength;
                            }
                            if (this.currentMode != 4) {
                                if (byteBuffer.remaining() < 4) {
                                    CoderResult coderResult10 = CoderResult.OVERFLOW;
                                    charBuffer.position(iPosition);
                                    return coderResult10;
                                }
                                byteBuffer.put((byte) 27);
                                byteBuffer.put((byte) 36);
                                byteBuffer.put((byte) 40);
                                byteBuffer.put((byte) 68);
                                this.currentMode = 4;
                            }
                            if (byteBuffer.remaining() < 2) {
                                CoderResult coderResult11 = CoderResult.OVERFLOW;
                                charBuffer.position(iPosition);
                                return coderResult11;
                            }
                            byteBuffer.put((byte) (iEncodeChar >> 8));
                            byteBuffer.put((byte) (iEncodeChar & 255));
                        }
                    }
                    iPosition++;
                } catch (Throwable th) {
                    charBuffer.position(iPosition);
                    throw th;
                }
            }
            CoderResult coderResult12 = CoderResult.UNDERFLOW;
            charBuffer.position(iPosition);
            return coderResult12;
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
