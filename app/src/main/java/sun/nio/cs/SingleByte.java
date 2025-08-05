package sun.nio.cs;

import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.CoderResult;
import sun.nio.cs.Surrogate;

/* loaded from: rt.jar:sun/nio/cs/SingleByte.class */
public class SingleByte {
    /* JADX INFO: Access modifiers changed from: private */
    public static final CoderResult withResult(CoderResult coderResult, Buffer buffer, int i2, Buffer buffer2, int i3) {
        buffer.position(i2 - buffer.arrayOffset());
        buffer2.position(i3 - buffer2.arrayOffset());
        return coderResult;
    }

    /* loaded from: rt.jar:sun/nio/cs/SingleByte$Decoder.class */
    public static final class Decoder extends CharsetDecoder implements ArrayDecoder {
        private final char[] b2c;
        private char repl;

        public Decoder(Charset charset, char[] cArr) {
            super(charset, 1.0f, 1.0f);
            this.repl = (char) 65533;
            this.b2c = cArr;
        }

        private CoderResult decodeArrayLoop(ByteBuffer byteBuffer, CharBuffer charBuffer) {
            byte[] bArrArray = byteBuffer.array();
            int iArrayOffset = byteBuffer.arrayOffset() + byteBuffer.position();
            int iArrayOffset2 = byteBuffer.arrayOffset() + byteBuffer.limit();
            char[] cArrArray = charBuffer.array();
            int iArrayOffset3 = charBuffer.arrayOffset() + charBuffer.position();
            int iArrayOffset4 = charBuffer.arrayOffset() + charBuffer.limit();
            CoderResult coderResult = CoderResult.UNDERFLOW;
            if (iArrayOffset4 - iArrayOffset3 < iArrayOffset2 - iArrayOffset) {
                iArrayOffset2 = iArrayOffset + (iArrayOffset4 - iArrayOffset3);
                coderResult = CoderResult.OVERFLOW;
            }
            while (iArrayOffset < iArrayOffset2) {
                char cDecode = decode(bArrArray[iArrayOffset]);
                if (cDecode == 65533) {
                    return SingleByte.withResult(CoderResult.unmappableForLength(1), byteBuffer, iArrayOffset, charBuffer, iArrayOffset3);
                }
                int i2 = iArrayOffset3;
                iArrayOffset3++;
                cArrArray[i2] = cDecode;
                iArrayOffset++;
            }
            return SingleByte.withResult(coderResult, byteBuffer, iArrayOffset, charBuffer, iArrayOffset3);
        }

        private CoderResult decodeBufferLoop(ByteBuffer byteBuffer, CharBuffer charBuffer) {
            int iPosition = byteBuffer.position();
            while (byteBuffer.hasRemaining()) {
                try {
                    char cDecode = decode(byteBuffer.get());
                    if (cDecode == 65533) {
                        CoderResult coderResultUnmappableForLength = CoderResult.unmappableForLength(1);
                        byteBuffer.position(iPosition);
                        return coderResultUnmappableForLength;
                    }
                    if (!charBuffer.hasRemaining()) {
                        CoderResult coderResult = CoderResult.OVERFLOW;
                        byteBuffer.position(iPosition);
                        return coderResult;
                    }
                    charBuffer.put(cDecode);
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

        public final char decode(int i2) {
            return this.b2c[i2 + 128];
        }

        @Override // java.nio.charset.CharsetDecoder
        protected void implReplaceWith(String str) {
            this.repl = str.charAt(0);
        }

        @Override // sun.nio.cs.ArrayDecoder
        public int decode(byte[] bArr, int i2, int i3, char[] cArr) {
            if (i3 > cArr.length) {
                i3 = cArr.length;
            }
            int i4 = 0;
            while (i4 < i3) {
                int i5 = i2;
                i2++;
                cArr[i4] = decode(bArr[i5]);
                if (cArr[i4] == 65533) {
                    cArr[i4] = this.repl;
                }
                i4++;
            }
            return i4;
        }
    }

    /* loaded from: rt.jar:sun/nio/cs/SingleByte$Encoder.class */
    public static final class Encoder extends CharsetEncoder implements ArrayEncoder {
        private Surrogate.Parser sgp;
        private final char[] c2b;
        private final char[] c2bIndex;
        private byte repl;

        public Encoder(Charset charset, char[] cArr, char[] cArr2) {
            super(charset, 1.0f, 1.0f);
            this.repl = (byte) 63;
            this.c2b = cArr;
            this.c2bIndex = cArr2;
        }

        @Override // java.nio.charset.CharsetEncoder
        public boolean canEncode(char c2) {
            return encode(c2) != 65533;
        }

        @Override // java.nio.charset.CharsetEncoder
        public boolean isLegalReplacement(byte[] bArr) {
            return (bArr.length == 1 && bArr[0] == 63) || super.isLegalReplacement(bArr);
        }

        private CoderResult encodeArrayLoop(CharBuffer charBuffer, ByteBuffer byteBuffer) {
            char[] cArrArray = charBuffer.array();
            int iArrayOffset = charBuffer.arrayOffset() + charBuffer.position();
            int iArrayOffset2 = charBuffer.arrayOffset() + charBuffer.limit();
            byte[] bArrArray = byteBuffer.array();
            int iArrayOffset3 = byteBuffer.arrayOffset() + byteBuffer.position();
            int iArrayOffset4 = byteBuffer.arrayOffset() + byteBuffer.limit();
            CoderResult coderResult = CoderResult.UNDERFLOW;
            if (iArrayOffset4 - iArrayOffset3 < iArrayOffset2 - iArrayOffset) {
                iArrayOffset2 = iArrayOffset + (iArrayOffset4 - iArrayOffset3);
                coderResult = CoderResult.OVERFLOW;
            }
            while (iArrayOffset < iArrayOffset2) {
                char c2 = cArrArray[iArrayOffset];
                int iEncode = encode(c2);
                if (iEncode == 65533) {
                    if (!Character.isSurrogate(c2)) {
                        return SingleByte.withResult(CoderResult.unmappableForLength(1), charBuffer, iArrayOffset, byteBuffer, iArrayOffset3);
                    }
                    if (this.sgp == null) {
                        this.sgp = new Surrogate.Parser();
                    }
                    return this.sgp.parse(c2, cArrArray, iArrayOffset, iArrayOffset2) < 0 ? SingleByte.withResult(this.sgp.error(), charBuffer, iArrayOffset, byteBuffer, iArrayOffset3) : SingleByte.withResult(this.sgp.unmappableResult(), charBuffer, iArrayOffset, byteBuffer, iArrayOffset3);
                }
                int i2 = iArrayOffset3;
                iArrayOffset3++;
                bArrArray[i2] = (byte) iEncode;
                iArrayOffset++;
            }
            return SingleByte.withResult(coderResult, charBuffer, iArrayOffset, byteBuffer, iArrayOffset3);
        }

        private CoderResult encodeBufferLoop(CharBuffer charBuffer, ByteBuffer byteBuffer) {
            int iPosition = charBuffer.position();
            while (charBuffer.hasRemaining()) {
                try {
                    char c2 = charBuffer.get();
                    int iEncode = encode(c2);
                    if (iEncode == 65533) {
                        if (!Character.isSurrogate(c2)) {
                            CoderResult coderResultUnmappableForLength = CoderResult.unmappableForLength(1);
                            charBuffer.position(iPosition);
                            return coderResultUnmappableForLength;
                        }
                        if (this.sgp == null) {
                            this.sgp = new Surrogate.Parser();
                        }
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
                    byteBuffer.put((byte) iEncode);
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

        public final int encode(char c2) {
            char c3 = this.c2bIndex[c2 >> '\b'];
            if (c3 == 65533) {
                return 65533;
            }
            return this.c2b[c3 + (c2 & 255)];
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
                int iEncode = encode(c2);
                if (iEncode != 65533) {
                    int i6 = i4;
                    i4++;
                    bArr[i6] = (byte) iEncode;
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

    public static void initC2B(char[] cArr, char[] cArr2, char[] cArr3, char[] cArr4) {
        for (int i2 = 0; i2 < cArr4.length; i2++) {
            cArr4[i2] = 65533;
        }
        for (int i3 = 0; i3 < cArr3.length; i3++) {
            cArr3[i3] = 65533;
        }
        int i4 = 0;
        int i5 = 0;
        while (i5 < cArr.length) {
            char c2 = cArr[i5];
            if (c2 != 65533) {
                int i6 = c2 >> '\b';
                if (cArr4[i6] == 65533) {
                    cArr4[i6] = (char) i4;
                    i4 += 256;
                }
                cArr3[cArr4[i6] + (c2 & 255)] = (char) (i5 >= 128 ? i5 - 128 : i5 + 128);
            }
            i5++;
        }
        if (cArr2 != null) {
            int i7 = 0;
            while (i7 < cArr2.length) {
                int i8 = i7;
                int i9 = i7 + 1;
                char c3 = cArr2[i8];
                i7 = i9 + 1;
                char c4 = cArr2[i9];
                int i10 = c4 >> '\b';
                if (cArr4[i10] == 65533) {
                    cArr4[i10] = (char) i4;
                    i4 += 256;
                }
                cArr3[cArr4[i10] + (c4 & 255)] = c3;
            }
        }
    }
}
