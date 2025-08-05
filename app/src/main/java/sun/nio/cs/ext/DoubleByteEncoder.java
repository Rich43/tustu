package sun.nio.cs.ext;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.CoderResult;
import sun.nio.cs.Surrogate;
import sun.text.normalizer.NormalizerImpl;

/* loaded from: charsets.jar:sun/nio/cs/ext/DoubleByteEncoder.class */
public abstract class DoubleByteEncoder extends CharsetEncoder {
    private short[] index1;
    private String[] index2;
    private final Surrogate.Parser sgp;

    protected DoubleByteEncoder(Charset charset, short[] sArr, String[] strArr) {
        super(charset, 2.0f, 2.0f);
        this.sgp = new Surrogate.Parser();
        this.index1 = sArr;
        this.index2 = strArr;
    }

    protected DoubleByteEncoder(Charset charset, short[] sArr, String[] strArr, float f2, float f3) {
        super(charset, f2, f3);
        this.sgp = new Surrogate.Parser();
        this.index1 = sArr;
        this.index2 = strArr;
    }

    protected DoubleByteEncoder(Charset charset, short[] sArr, String[] strArr, byte[] bArr) {
        super(charset, 2.0f, 2.0f, bArr);
        this.sgp = new Surrogate.Parser();
        this.index1 = sArr;
        this.index2 = strArr;
    }

    protected DoubleByteEncoder(Charset charset, short[] sArr, String[] strArr, byte[] bArr, float f2, float f3) {
        super(charset, f2, f3, bArr);
        this.sgp = new Surrogate.Parser();
        this.index1 = sArr;
        this.index2 = strArr;
    }

    @Override // java.nio.charset.CharsetEncoder
    public boolean canEncode(char c2) {
        return (encodeSingle(c2) == -1 && encodeDouble(c2) == 0) ? false : true;
    }

    private CoderResult encodeArrayLoop(CharBuffer charBuffer, ByteBuffer byteBuffer) {
        char[] cArrArray = charBuffer.array();
        int iArrayOffset = charBuffer.arrayOffset() + charBuffer.position();
        int iArrayOffset2 = charBuffer.arrayOffset() + charBuffer.limit();
        byte[] bArrArray = byteBuffer.array();
        int iArrayOffset3 = byteBuffer.arrayOffset() + byteBuffer.position();
        int iArrayOffset4 = byteBuffer.arrayOffset() + byteBuffer.limit();
        while (iArrayOffset < iArrayOffset2) {
            try {
                char c2 = cArrArray[iArrayOffset];
                if (Character.isSurrogate(c2)) {
                    if (this.sgp.parse(c2, cArrArray, iArrayOffset, iArrayOffset2) < 0) {
                        CoderResult coderResultError = this.sgp.error();
                        charBuffer.position(iArrayOffset - charBuffer.arrayOffset());
                        byteBuffer.position(iArrayOffset3 - byteBuffer.arrayOffset());
                        return coderResultError;
                    }
                    if (iArrayOffset2 - iArrayOffset < 2) {
                        CoderResult coderResult = CoderResult.UNDERFLOW;
                        charBuffer.position(iArrayOffset - charBuffer.arrayOffset());
                        byteBuffer.position(iArrayOffset3 - byteBuffer.arrayOffset());
                        return coderResult;
                    }
                    char c3 = cArrArray[iArrayOffset + 1];
                    byte[] bArr = new byte[2];
                    byte[] bArrEncodeSurrogate = encodeSurrogate(c2, c3);
                    if (bArrEncodeSurrogate == null) {
                        CoderResult coderResultUnmappableResult = this.sgp.unmappableResult();
                        charBuffer.position(iArrayOffset - charBuffer.arrayOffset());
                        byteBuffer.position(iArrayOffset3 - byteBuffer.arrayOffset());
                        return coderResultUnmappableResult;
                    }
                    if (iArrayOffset4 - iArrayOffset3 < 2) {
                        CoderResult coderResult2 = CoderResult.OVERFLOW;
                        charBuffer.position(iArrayOffset - charBuffer.arrayOffset());
                        byteBuffer.position(iArrayOffset3 - byteBuffer.arrayOffset());
                        return coderResult2;
                    }
                    int i2 = iArrayOffset3;
                    int i3 = iArrayOffset3 + 1;
                    bArrArray[i2] = bArrEncodeSurrogate[0];
                    iArrayOffset3 = i3 + 1;
                    bArrArray[i3] = bArrEncodeSurrogate[1];
                    iArrayOffset += 2;
                } else {
                    if (c2 >= 65534) {
                        CoderResult coderResultUnmappableForLength = CoderResult.unmappableForLength(1);
                        charBuffer.position(iArrayOffset - charBuffer.arrayOffset());
                        byteBuffer.position(iArrayOffset3 - byteBuffer.arrayOffset());
                        return coderResultUnmappableForLength;
                    }
                    int iEncodeSingle = encodeSingle(c2);
                    if (iEncodeSingle == -1) {
                        int iEncodeDouble = encodeDouble(c2);
                        if (iEncodeDouble == 0 || c2 == 0) {
                            CoderResult coderResultUnmappableForLength2 = CoderResult.unmappableForLength(1);
                            charBuffer.position(iArrayOffset - charBuffer.arrayOffset());
                            byteBuffer.position(iArrayOffset3 - byteBuffer.arrayOffset());
                            return coderResultUnmappableForLength2;
                        }
                        if (iArrayOffset4 - iArrayOffset3 < 2) {
                            CoderResult coderResult3 = CoderResult.OVERFLOW;
                            charBuffer.position(iArrayOffset - charBuffer.arrayOffset());
                            byteBuffer.position(iArrayOffset3 - byteBuffer.arrayOffset());
                            return coderResult3;
                        }
                        int i4 = iArrayOffset3;
                        int i5 = iArrayOffset3 + 1;
                        bArrArray[i4] = (byte) ((iEncodeDouble & NormalizerImpl.CC_MASK) >> 8);
                        iArrayOffset3 = i5 + 1;
                        bArrArray[i5] = (byte) (iEncodeDouble & 255);
                        iArrayOffset++;
                    } else {
                        if (iArrayOffset4 - iArrayOffset3 < 1) {
                            CoderResult coderResult4 = CoderResult.OVERFLOW;
                            charBuffer.position(iArrayOffset - charBuffer.arrayOffset());
                            byteBuffer.position(iArrayOffset3 - byteBuffer.arrayOffset());
                            return coderResult4;
                        }
                        int i6 = iArrayOffset3;
                        iArrayOffset3++;
                        bArrArray[i6] = (byte) iEncodeSingle;
                        iArrayOffset++;
                    }
                }
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

    private CoderResult encodeBufferLoop(CharBuffer charBuffer, ByteBuffer byteBuffer) {
        int iPosition = charBuffer.position();
        while (charBuffer.hasRemaining()) {
            try {
                char c2 = charBuffer.get();
                if (Character.isSurrogate(c2)) {
                    int i2 = this.sgp.parse(c2, charBuffer);
                    if (i2 < 0) {
                        CoderResult coderResultError = this.sgp.error();
                        charBuffer.position(iPosition);
                        return coderResultError;
                    }
                    char cLow = Surrogate.low(i2);
                    byte[] bArr = new byte[2];
                    byte[] bArrEncodeSurrogate = encodeSurrogate(c2, cLow);
                    if (bArrEncodeSurrogate == null) {
                        CoderResult coderResultUnmappableResult = this.sgp.unmappableResult();
                        charBuffer.position(iPosition);
                        return coderResultUnmappableResult;
                    }
                    if (byteBuffer.remaining() < 2) {
                        CoderResult coderResult = CoderResult.OVERFLOW;
                        charBuffer.position(iPosition);
                        return coderResult;
                    }
                    iPosition += 2;
                    byteBuffer.put(bArrEncodeSurrogate[0]);
                    byteBuffer.put(bArrEncodeSurrogate[1]);
                } else {
                    if (c2 >= 65534) {
                        CoderResult coderResultUnmappableForLength = CoderResult.unmappableForLength(1);
                        charBuffer.position(iPosition);
                        return coderResultUnmappableForLength;
                    }
                    int iEncodeSingle = encodeSingle(c2);
                    if (iEncodeSingle == -1) {
                        int iEncodeDouble = encodeDouble(c2);
                        if (iEncodeDouble == 0 || c2 == 0) {
                            CoderResult coderResultUnmappableForLength2 = CoderResult.unmappableForLength(1);
                            charBuffer.position(iPosition);
                            return coderResultUnmappableForLength2;
                        }
                        if (byteBuffer.remaining() < 2) {
                            CoderResult coderResult2 = CoderResult.OVERFLOW;
                            charBuffer.position(iPosition);
                            return coderResult2;
                        }
                        iPosition++;
                        byteBuffer.put((byte) ((iEncodeDouble & NormalizerImpl.CC_MASK) >> 8));
                        byteBuffer.put((byte) iEncodeDouble);
                    } else {
                        if (byteBuffer.remaining() < 1) {
                            CoderResult coderResult3 = CoderResult.OVERFLOW;
                            charBuffer.position(iPosition);
                            return coderResult3;
                        }
                        iPosition++;
                        byteBuffer.put((byte) iEncodeSingle);
                    }
                }
            } catch (Throwable th) {
                charBuffer.position(iPosition);
                throw th;
            }
        }
        CoderResult coderResult4 = CoderResult.UNDERFLOW;
        charBuffer.position(iPosition);
        return coderResult4;
    }

    @Override // java.nio.charset.CharsetEncoder
    protected CoderResult encodeLoop(CharBuffer charBuffer, ByteBuffer byteBuffer) {
        if (charBuffer.hasArray() && byteBuffer.hasArray()) {
            return encodeArrayLoop(charBuffer, byteBuffer);
        }
        return encodeBufferLoop(charBuffer, byteBuffer);
    }

    protected int encodeDouble(char c2) {
        int i2 = this.index1[(c2 & 65280) >> 8] << 8;
        return this.index2[i2 >> 12].charAt((i2 & 4095) + (c2 & 255));
    }

    protected int encodeSingle(char c2) {
        if (c2 < 128) {
            return (byte) c2;
        }
        return -1;
    }

    protected byte[] encodeSurrogate(char c2, char c3) {
        return null;
    }
}
