package sun.nio.cs.ext;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.CoderResult;
import sun.nio.cs.Surrogate;

/* loaded from: charsets.jar:sun/nio/cs/ext/ISO2022.class */
abstract class ISO2022 extends Charset {
    private static final byte ISO_ESC = 27;
    private static final byte ISO_SI = 15;
    private static final byte ISO_SO = 14;
    private static final byte ISO_SS2_7 = 78;
    private static final byte ISO_SS3_7 = 79;
    private static final byte MSB = Byte.MIN_VALUE;
    private static final char REPLACE_CHAR = 65533;
    private static final byte minDesignatorLength = 3;

    public ISO2022(String str, String[] strArr) {
        super(str, strArr);
    }

    @Override // java.nio.charset.Charset
    public CharsetDecoder newDecoder() {
        return new Decoder(this);
    }

    @Override // java.nio.charset.Charset
    public CharsetEncoder newEncoder() {
        return new Encoder(this);
    }

    /* loaded from: charsets.jar:sun/nio/cs/ext/ISO2022$Decoder.class */
    protected static class Decoder extends CharsetDecoder {
        protected byte[][] SODesig;
        protected byte[][] SS2Desig;
        protected byte[][] SS3Desig;
        protected CharsetDecoder[] SODecoder;
        protected CharsetDecoder[] SS2Decoder;
        protected CharsetDecoder[] SS3Decoder;
        private static final byte SOFlag = 0;
        private static final byte SS2Flag = 1;
        private static final byte SS3Flag = 2;
        private int curSODes;
        private int curSS2Des;
        private int curSS3Des;
        private boolean shiftout;
        private CharsetDecoder[] tmpDecoder;
        static final /* synthetic */ boolean $assertionsDisabled;

        static {
            $assertionsDisabled = !ISO2022.class.desiredAssertionStatus();
        }

        protected Decoder(Charset charset) {
            super(charset, 1.0f, 1.0f);
            this.SS2Desig = (byte[][]) null;
            this.SS3Desig = (byte[][]) null;
            this.SS2Decoder = null;
            this.SS3Decoder = null;
        }

        @Override // java.nio.charset.CharsetDecoder
        protected void implReset() {
            this.curSODes = 0;
            this.curSS2Des = 0;
            this.curSS3Des = 0;
            this.shiftout = false;
        }

        private char decode(byte b2, byte b3, byte b4) {
            byte[] bArr = {(byte) (b2 | Byte.MIN_VALUE), (byte) (b3 | Byte.MIN_VALUE)};
            char[] cArr = new char[1];
            int i2 = 0;
            switch (b4) {
                case 0:
                    i2 = this.curSODes;
                    this.tmpDecoder = this.SODecoder;
                    break;
                case 1:
                    i2 = this.curSS2Des;
                    this.tmpDecoder = this.SS2Decoder;
                    break;
                case 2:
                    i2 = this.curSS3Des;
                    this.tmpDecoder = this.SS3Decoder;
                    break;
            }
            if (this.tmpDecoder != null) {
                for (int i3 = 0; i3 < this.tmpDecoder.length; i3++) {
                    if (i2 == i3) {
                        try {
                            ByteBuffer byteBufferWrap = ByteBuffer.wrap(bArr, 0, 2);
                            CharBuffer charBufferWrap = CharBuffer.wrap(cArr, 0, 1);
                            this.tmpDecoder[i3].decode(byteBufferWrap, charBufferWrap, true);
                            charBufferWrap.flip();
                            return charBufferWrap.get();
                        } catch (Exception e2) {
                        }
                    }
                }
                return (char) 65533;
            }
            return (char) 65533;
        }

        private int findDesig(byte[] bArr, int i2, int i3, byte[][] bArr2) {
            if (bArr2 == null) {
                return -1;
            }
            for (int i4 = 0; i4 < bArr2.length; i4++) {
                if (bArr2[i4] != null && i3 - i2 >= bArr2[i4].length) {
                    int i5 = 0;
                    while (i5 < bArr2[i4].length && bArr[i2 + i5] == bArr2[i4][i5]) {
                        i5++;
                    }
                    if (i5 == bArr2[i4].length) {
                        return i4;
                    }
                }
            }
            return -1;
        }

        private int findDesigBuf(ByteBuffer byteBuffer, byte[][] bArr) {
            if (bArr == null) {
                return -1;
            }
            for (int i2 = 0; i2 < bArr.length; i2++) {
                if (bArr[i2] != null && byteBuffer.remaining() >= bArr[i2].length) {
                    int i3 = 0;
                    byteBuffer.mark();
                    while (i3 < bArr[i2].length && byteBuffer.get() == bArr[i2][i3]) {
                        i3++;
                    }
                    if (i3 == bArr[i2].length) {
                        return i2;
                    }
                    byteBuffer.reset();
                }
            }
            return -1;
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
                    int i4 = bArrArray[i2] & 255;
                    int length = 1;
                    switch (i4) {
                        case 14:
                            this.shiftout = true;
                            length = 1;
                            break;
                        case 15:
                            this.shiftout = false;
                            length = 1;
                            break;
                        case 27:
                            if ((iArrayOffset2 - i2) - 1 < 3) {
                                CoderResult coderResult = CoderResult.UNDERFLOW;
                                byteBuffer.position(i2 - byteBuffer.arrayOffset());
                                charBuffer.position(i3 - charBuffer.arrayOffset());
                                return coderResult;
                            }
                            int iFindDesig = findDesig(bArrArray, i2 + 1, iArrayOffset2, this.SODesig);
                            if (iFindDesig != -1) {
                                this.curSODes = iFindDesig;
                                length = this.SODesig[iFindDesig].length + 1;
                                break;
                            } else {
                                int iFindDesig2 = findDesig(bArrArray, i2 + 1, iArrayOffset2, this.SS2Desig);
                                if (iFindDesig2 != -1) {
                                    this.curSS2Des = iFindDesig2;
                                    length = this.SS2Desig[iFindDesig2].length + 1;
                                    break;
                                } else {
                                    int iFindDesig3 = findDesig(bArrArray, i2 + 1, iArrayOffset2, this.SS3Desig);
                                    if (iFindDesig3 != -1) {
                                        this.curSS3Des = iFindDesig3;
                                        length = this.SS3Desig[iFindDesig3].length + 1;
                                        break;
                                    } else {
                                        if (iArrayOffset2 - i2 < 2) {
                                            CoderResult coderResult2 = CoderResult.UNDERFLOW;
                                            byteBuffer.position(i2 - byteBuffer.arrayOffset());
                                            charBuffer.position(i3 - charBuffer.arrayOffset());
                                            return coderResult2;
                                        }
                                        switch (bArrArray[i2 + 1]) {
                                            case 78:
                                                if (iArrayOffset2 - i2 >= 4) {
                                                    byte b2 = bArrArray[i2 + 2];
                                                    byte b3 = bArrArray[i2 + 3];
                                                    if (iArrayOffset4 - i3 >= 1) {
                                                        cArrArray[i3] = decode(b2, b3, (byte) 1);
                                                        i3++;
                                                        length = 4;
                                                        break;
                                                    } else {
                                                        CoderResult coderResult3 = CoderResult.OVERFLOW;
                                                        byteBuffer.position(i2 - byteBuffer.arrayOffset());
                                                        charBuffer.position(i3 - charBuffer.arrayOffset());
                                                        return coderResult3;
                                                    }
                                                } else {
                                                    CoderResult coderResult4 = CoderResult.UNDERFLOW;
                                                    byteBuffer.position(i2 - byteBuffer.arrayOffset());
                                                    charBuffer.position(i3 - charBuffer.arrayOffset());
                                                    return coderResult4;
                                                }
                                            case 79:
                                                if (iArrayOffset2 - i2 >= 4) {
                                                    byte b4 = bArrArray[i2 + 2];
                                                    byte b5 = bArrArray[i2 + 3];
                                                    if (iArrayOffset4 - i3 >= 1) {
                                                        cArrArray[i3] = decode(b4, b5, (byte) 2);
                                                        i3++;
                                                        length = 4;
                                                        break;
                                                    } else {
                                                        CoderResult coderResult5 = CoderResult.OVERFLOW;
                                                        byteBuffer.position(i2 - byteBuffer.arrayOffset());
                                                        charBuffer.position(i3 - charBuffer.arrayOffset());
                                                        return coderResult5;
                                                    }
                                                } else {
                                                    CoderResult coderResult6 = CoderResult.UNDERFLOW;
                                                    byteBuffer.position(i2 - byteBuffer.arrayOffset());
                                                    charBuffer.position(i3 - charBuffer.arrayOffset());
                                                    return coderResult6;
                                                }
                                            default:
                                                CoderResult coderResultMalformedForLength = CoderResult.malformedForLength(2);
                                                byteBuffer.position(i2 - byteBuffer.arrayOffset());
                                                charBuffer.position(i3 - charBuffer.arrayOffset());
                                                return coderResultMalformedForLength;
                                        }
                                    }
                                }
                            }
                        default:
                            if (iArrayOffset4 - i3 >= 1) {
                                if (!this.shiftout) {
                                    int i5 = i3;
                                    i3++;
                                    cArrArray[i5] = (char) (bArrArray[i2] & 255);
                                    break;
                                } else if (iArrayOffset4 - i3 >= 1) {
                                    if (iArrayOffset2 - i2 >= 2) {
                                        int i6 = i3;
                                        i3++;
                                        cArrArray[i6] = decode((byte) i4, (byte) (bArrArray[i2 + 1] & 255), (byte) 0);
                                        length = 2;
                                        break;
                                    } else {
                                        CoderResult coderResult7 = CoderResult.UNDERFLOW;
                                        byteBuffer.position(i2 - byteBuffer.arrayOffset());
                                        charBuffer.position(i3 - charBuffer.arrayOffset());
                                        return coderResult7;
                                    }
                                } else {
                                    CoderResult coderResult8 = CoderResult.OVERFLOW;
                                    byteBuffer.position(i2 - byteBuffer.arrayOffset());
                                    charBuffer.position(i3 - charBuffer.arrayOffset());
                                    return coderResult8;
                                }
                            } else {
                                CoderResult coderResult9 = CoderResult.OVERFLOW;
                                byteBuffer.position(i2 - byteBuffer.arrayOffset());
                                charBuffer.position(i3 - charBuffer.arrayOffset());
                                return coderResult9;
                            }
                    }
                    i2 += length;
                } catch (Throwable th) {
                    byteBuffer.position(i2 - byteBuffer.arrayOffset());
                    charBuffer.position(i3 - charBuffer.arrayOffset());
                    throw th;
                }
            }
            CoderResult coderResult10 = CoderResult.UNDERFLOW;
            byteBuffer.position(i2 - byteBuffer.arrayOffset());
            charBuffer.position(i3 - charBuffer.arrayOffset());
            return coderResult10;
        }

        private CoderResult decodeBufferLoop(ByteBuffer byteBuffer, CharBuffer charBuffer) {
            int iPosition = byteBuffer.position();
            while (byteBuffer.hasRemaining()) {
                try {
                    try {
                        byte b2 = byteBuffer.get();
                        int length = 1;
                        switch (b2) {
                            case 14:
                                this.shiftout = true;
                                break;
                            case 15:
                                this.shiftout = false;
                                break;
                            case 27:
                                if (byteBuffer.remaining() < 3) {
                                    CoderResult coderResult = CoderResult.UNDERFLOW;
                                    byteBuffer.position(iPosition);
                                    return coderResult;
                                }
                                int iFindDesigBuf = findDesigBuf(byteBuffer, this.SODesig);
                                if (iFindDesigBuf != -1) {
                                    this.curSODes = iFindDesigBuf;
                                    length = this.SODesig[iFindDesigBuf].length + 1;
                                    break;
                                } else {
                                    int iFindDesigBuf2 = findDesigBuf(byteBuffer, this.SS2Desig);
                                    if (iFindDesigBuf2 != -1) {
                                        this.curSS2Des = iFindDesigBuf2;
                                        length = this.SS2Desig[iFindDesigBuf2].length + 1;
                                        break;
                                    } else {
                                        int iFindDesigBuf3 = findDesigBuf(byteBuffer, this.SS3Desig);
                                        if (iFindDesigBuf3 != -1) {
                                            this.curSS3Des = iFindDesigBuf3;
                                            length = this.SS3Desig[iFindDesigBuf3].length + 1;
                                            break;
                                        } else {
                                            if (byteBuffer.remaining() < 1) {
                                                CoderResult coderResult2 = CoderResult.UNDERFLOW;
                                                byteBuffer.position(iPosition);
                                                return coderResult2;
                                            }
                                            switch (byteBuffer.get()) {
                                                case 78:
                                                    if (byteBuffer.remaining() >= 2) {
                                                        byte b3 = byteBuffer.get();
                                                        byte b4 = byteBuffer.get();
                                                        if (charBuffer.remaining() >= 1) {
                                                            charBuffer.put(decode(b3, b4, (byte) 1));
                                                            length = 4;
                                                            break;
                                                        } else {
                                                            CoderResult coderResult3 = CoderResult.OVERFLOW;
                                                            byteBuffer.position(iPosition);
                                                            return coderResult3;
                                                        }
                                                    } else {
                                                        CoderResult coderResult4 = CoderResult.UNDERFLOW;
                                                        byteBuffer.position(iPosition);
                                                        return coderResult4;
                                                    }
                                                case 79:
                                                    if (byteBuffer.remaining() >= 2) {
                                                        byte b5 = byteBuffer.get();
                                                        byte b6 = byteBuffer.get();
                                                        if (charBuffer.remaining() >= 1) {
                                                            charBuffer.put(decode(b5, b6, (byte) 2));
                                                            length = 4;
                                                            break;
                                                        } else {
                                                            CoderResult coderResult5 = CoderResult.OVERFLOW;
                                                            byteBuffer.position(iPosition);
                                                            return coderResult5;
                                                        }
                                                    } else {
                                                        CoderResult coderResult6 = CoderResult.UNDERFLOW;
                                                        byteBuffer.position(iPosition);
                                                        return coderResult6;
                                                    }
                                                default:
                                                    CoderResult coderResultMalformedForLength = CoderResult.malformedForLength(2);
                                                    byteBuffer.position(iPosition);
                                                    return coderResultMalformedForLength;
                                            }
                                        }
                                    }
                                }
                            default:
                                if (charBuffer.remaining() >= 1) {
                                    if (!this.shiftout) {
                                        charBuffer.put((char) (b2 & 255));
                                        break;
                                    } else if (charBuffer.remaining() >= 1) {
                                        if (byteBuffer.remaining() >= 1) {
                                            charBuffer.put(decode(b2, (byte) (byteBuffer.get() & 255), (byte) 0));
                                            length = 2;
                                            break;
                                        } else {
                                            CoderResult coderResult7 = CoderResult.UNDERFLOW;
                                            byteBuffer.position(iPosition);
                                            return coderResult7;
                                        }
                                    } else {
                                        CoderResult coderResult8 = CoderResult.OVERFLOW;
                                        byteBuffer.position(iPosition);
                                        return coderResult8;
                                    }
                                } else {
                                    CoderResult coderResult9 = CoderResult.OVERFLOW;
                                    byteBuffer.position(iPosition);
                                    return coderResult9;
                                }
                        }
                        iPosition += length;
                    } catch (Exception e2) {
                        e2.printStackTrace();
                        CoderResult coderResult10 = CoderResult.OVERFLOW;
                        byteBuffer.position(iPosition);
                        return coderResult10;
                    }
                } catch (Throwable th) {
                    byteBuffer.position(iPosition);
                    throw th;
                }
            }
            CoderResult coderResult11 = CoderResult.UNDERFLOW;
            byteBuffer.position(iPosition);
            return coderResult11;
        }

        @Override // java.nio.charset.CharsetDecoder
        protected CoderResult decodeLoop(ByteBuffer byteBuffer, CharBuffer charBuffer) {
            if (byteBuffer.hasArray() && charBuffer.hasArray()) {
                return decodeArrayLoop(byteBuffer, charBuffer);
            }
            return decodeBufferLoop(byteBuffer, charBuffer);
        }
    }

    /* loaded from: charsets.jar:sun/nio/cs/ext/ISO2022$Encoder.class */
    protected static class Encoder extends CharsetEncoder {
        private final Surrogate.Parser sgp;
        public static final byte SS2 = -114;
        public static final byte PLANE2 = -94;
        public static final byte PLANE3 = -93;
        private final byte MSB = Byte.MIN_VALUE;
        protected final byte maximumDesignatorLength = 4;
        protected String SODesig;
        protected String SS2Desig;
        protected String SS3Desig;
        protected CharsetEncoder ISOEncoder;
        private boolean shiftout;
        private boolean SODesDefined;
        private boolean SS2DesDefined;
        private boolean SS3DesDefined;
        private boolean newshiftout;
        private boolean newSODesDefined;
        private boolean newSS2DesDefined;
        private boolean newSS3DesDefined;
        static final /* synthetic */ boolean $assertionsDisabled;

        static {
            $assertionsDisabled = !ISO2022.class.desiredAssertionStatus();
        }

        protected Encoder(Charset charset) {
            super(charset, 4.0f, 8.0f);
            this.sgp = new Surrogate.Parser();
            this.MSB = Byte.MIN_VALUE;
            this.maximumDesignatorLength = (byte) 4;
            this.SS2Desig = null;
            this.SS3Desig = null;
            this.shiftout = false;
            this.SODesDefined = false;
            this.SS2DesDefined = false;
            this.SS3DesDefined = false;
            this.newshiftout = false;
            this.newSODesDefined = false;
            this.newSS2DesDefined = false;
            this.newSS3DesDefined = false;
        }

        @Override // java.nio.charset.CharsetEncoder
        public boolean canEncode(char c2) {
            return this.ISOEncoder.canEncode(c2);
        }

        @Override // java.nio.charset.CharsetEncoder
        protected void implReset() {
            this.shiftout = false;
            this.SODesDefined = false;
            this.SS2DesDefined = false;
            this.SS3DesDefined = false;
        }

        private int unicodeToNative(char c2, byte[] bArr) {
            int length = 0;
            byte[] bArr2 = new byte[4];
            try {
                CharBuffer charBufferWrap = CharBuffer.wrap(new char[]{c2});
                ByteBuffer byteBufferAllocate = ByteBuffer.allocate(4);
                this.ISOEncoder.encode(charBufferWrap, byteBufferAllocate, true);
                byteBufferAllocate.flip();
                int iRemaining = byteBufferAllocate.remaining();
                byteBufferAllocate.get(bArr2, 0, iRemaining);
                if (iRemaining == 2) {
                    if (!this.SODesDefined) {
                        this.newSODesDefined = true;
                        bArr[0] = 27;
                        byte[] bytes = this.SODesig.getBytes();
                        System.arraycopy(bytes, 0, bArr, 1, bytes.length);
                        length = bytes.length + 1;
                    }
                    if (!this.shiftout) {
                        this.newshiftout = true;
                        int i2 = length;
                        length++;
                        bArr[i2] = 14;
                    }
                    int i3 = length;
                    int i4 = length + 1;
                    bArr[i3] = (byte) (bArr2[0] & Byte.MAX_VALUE);
                    length = i4 + 1;
                    bArr[i4] = (byte) (bArr2[1] & Byte.MAX_VALUE);
                } else if (bArr2[0] == -114) {
                    if (bArr2[1] == -94) {
                        if (!this.SS2DesDefined) {
                            this.newSS2DesDefined = true;
                            bArr[0] = 27;
                            byte[] bytes2 = this.SS2Desig.getBytes();
                            System.arraycopy(bytes2, 0, bArr, 1, bytes2.length);
                            length = bytes2.length + 1;
                        }
                        int i5 = length;
                        int i6 = length + 1;
                        bArr[i5] = 27;
                        int i7 = i6 + 1;
                        bArr[i6] = 78;
                        int i8 = i7 + 1;
                        bArr[i7] = (byte) (bArr2[2] & Byte.MAX_VALUE);
                        length = i8 + 1;
                        bArr[i8] = (byte) (bArr2[3] & Byte.MAX_VALUE);
                    } else if (bArr2[1] == -93) {
                        if (!this.SS3DesDefined) {
                            this.newSS3DesDefined = true;
                            bArr[0] = 27;
                            byte[] bytes3 = this.SS3Desig.getBytes();
                            System.arraycopy(bytes3, 0, bArr, 1, bytes3.length);
                            length = bytes3.length + 1;
                        }
                        int i9 = length;
                        int i10 = length + 1;
                        bArr[i9] = 27;
                        int i11 = i10 + 1;
                        bArr[i10] = 79;
                        int i12 = i11 + 1;
                        bArr[i11] = (byte) (bArr2[2] & Byte.MAX_VALUE);
                        length = i12 + 1;
                        bArr[i12] = (byte) (bArr2[3] & Byte.MAX_VALUE);
                    }
                }
                return length;
            } catch (Exception e2) {
                return -1;
            }
        }

        private CoderResult encodeArrayLoop(CharBuffer charBuffer, ByteBuffer byteBuffer) {
            int iUnicodeToNative;
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
            byte[] bArr = new byte[8];
            this.newshiftout = this.shiftout;
            this.newSODesDefined = this.SODesDefined;
            this.newSS2DesDefined = this.SS2DesDefined;
            this.newSS3DesDefined = this.SS3DesDefined;
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
                    if (c2 < 128) {
                        if (this.shiftout) {
                            this.newshiftout = false;
                            iUnicodeToNative = 2;
                            bArr[0] = 15;
                            bArr[1] = (byte) (c2 & 127);
                        } else {
                            iUnicodeToNative = 1;
                            bArr[0] = (byte) (c2 & 127);
                        }
                        if (cArrArray[i2] == '\n') {
                            this.newSODesDefined = false;
                            this.newSS2DesDefined = false;
                            this.newSS3DesDefined = false;
                        }
                    } else {
                        iUnicodeToNative = unicodeToNative(c2, bArr);
                        if (iUnicodeToNative == 0) {
                            CoderResult coderResultUnmappableForLength = CoderResult.unmappableForLength(1);
                            charBuffer.position(i2 - charBuffer.arrayOffset());
                            byteBuffer.position(i3 - byteBuffer.arrayOffset());
                            return coderResultUnmappableForLength;
                        }
                    }
                    if (iArrayOffset4 - i3 < iUnicodeToNative) {
                        CoderResult coderResult = CoderResult.OVERFLOW;
                        charBuffer.position(i2 - charBuffer.arrayOffset());
                        byteBuffer.position(i3 - byteBuffer.arrayOffset());
                        return coderResult;
                    }
                    for (int i4 = 0; i4 < iUnicodeToNative; i4++) {
                        int i5 = i3;
                        i3++;
                        bArrArray[i5] = bArr[i4];
                    }
                    i2++;
                    this.shiftout = this.newshiftout;
                    this.SODesDefined = this.newSODesDefined;
                    this.SS2DesDefined = this.newSS2DesDefined;
                    this.SS3DesDefined = this.newSS3DesDefined;
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
            int iUnicodeToNative;
            byte[] bArr = new byte[8];
            this.newshiftout = this.shiftout;
            this.newSODesDefined = this.SODesDefined;
            this.newSS2DesDefined = this.SS2DesDefined;
            this.newSS3DesDefined = this.SS3DesDefined;
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
                    if (c2 < 128) {
                        if (this.shiftout) {
                            this.newshiftout = false;
                            iUnicodeToNative = 2;
                            bArr[0] = 15;
                            bArr[1] = (byte) (c2 & 127);
                        } else {
                            iUnicodeToNative = 1;
                            bArr[0] = (byte) (c2 & 127);
                        }
                        if (c2 == '\n') {
                            this.newSODesDefined = false;
                            this.newSS2DesDefined = false;
                            this.newSS3DesDefined = false;
                        }
                    } else {
                        iUnicodeToNative = unicodeToNative(c2, bArr);
                        if (iUnicodeToNative == 0) {
                            CoderResult coderResultUnmappableForLength = CoderResult.unmappableForLength(1);
                            charBuffer.position(iPosition);
                            return coderResultUnmappableForLength;
                        }
                    }
                    if (byteBuffer.remaining() < iUnicodeToNative) {
                        CoderResult coderResult = CoderResult.OVERFLOW;
                        charBuffer.position(iPosition);
                        return coderResult;
                    }
                    for (int i2 = 0; i2 < iUnicodeToNative; i2++) {
                        byteBuffer.put(bArr[i2]);
                    }
                    iPosition++;
                    this.shiftout = this.newshiftout;
                    this.SODesDefined = this.newSODesDefined;
                    this.SS2DesDefined = this.newSS2DesDefined;
                    this.SS3DesDefined = this.newSS3DesDefined;
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
