package sun.nio.cs.ext;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.CoderResult;
import sun.nio.cs.Surrogate;

/* loaded from: charsets.jar:sun/nio/cs/ext/SimpleEUCEncoder.class */
public abstract class SimpleEUCEncoder extends CharsetEncoder {
    protected short[] index1;
    protected String index2;
    protected String index2a;
    protected String index2b;
    protected String index2c;
    protected int mask1;
    protected int mask2;
    protected int shift;
    private byte[] outputByte;
    private final Surrogate.Parser sgp;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !SimpleEUCEncoder.class.desiredAssertionStatus();
    }

    protected SimpleEUCEncoder(Charset charset) {
        super(charset, 3.0f, 4.0f);
        this.outputByte = new byte[4];
        this.sgp = new Surrogate.Parser();
    }

    @Override // java.nio.charset.CharsetEncoder
    public boolean canEncode(char c2) {
        String str;
        int i2 = this.index1[(c2 & this.mask1) >> this.shift] + (c2 & this.mask2);
        if (i2 < 7500) {
            str = this.index2;
        } else if (i2 < 15000) {
            i2 -= 7500;
            str = this.index2a;
        } else if (i2 < 22500) {
            i2 -= 15000;
            str = this.index2b;
        } else {
            i2 -= 22500;
            str = this.index2c;
        }
        return (str.charAt(2 * i2) == 0 && str.charAt((2 * i2) + 1) == 0 && c2 != 0) ? false : true;
    }

    private CoderResult encodeArrayLoop(CharBuffer charBuffer, ByteBuffer byteBuffer) {
        String str;
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
                boolean z2 = true;
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
                if (c2 >= 65534) {
                    CoderResult coderResultUnmappableForLength = CoderResult.unmappableForLength(1);
                    charBuffer.position(i2 - charBuffer.arrayOffset());
                    byteBuffer.position(i3 - byteBuffer.arrayOffset());
                    return coderResultUnmappableForLength;
                }
                int i4 = this.index1[(c2 & this.mask1) >> this.shift] + (c2 & this.mask2);
                if (i4 < 7500) {
                    str = this.index2;
                } else if (i4 < 15000) {
                    i4 -= 7500;
                    str = this.index2a;
                } else if (i4 < 22500) {
                    i4 -= 15000;
                    str = this.index2b;
                } else {
                    i4 -= 22500;
                    str = this.index2c;
                }
                char cCharAt = str.charAt(2 * i4);
                this.outputByte[0] = (byte) ((cCharAt & 65280) >> 8);
                this.outputByte[1] = (byte) (cCharAt & 255);
                char cCharAt2 = str.charAt((2 * i4) + 1);
                this.outputByte[2] = (byte) ((cCharAt2 & 65280) >> 8);
                this.outputByte[3] = (byte) (cCharAt2 & 255);
                int i5 = 0;
                while (true) {
                    if (i5 >= this.outputByte.length) {
                        break;
                    }
                    if (this.outputByte[i5] != 0) {
                        z2 = false;
                        break;
                    }
                    i5++;
                }
                if (z2 && c2 != 0) {
                    CoderResult coderResultUnmappableForLength2 = CoderResult.unmappableForLength(1);
                    charBuffer.position(i2 - charBuffer.arrayOffset());
                    byteBuffer.position(i3 - byteBuffer.arrayOffset());
                    return coderResultUnmappableForLength2;
                }
                int i6 = 0;
                int length = this.outputByte.length;
                while (length > 1) {
                    int i7 = i6;
                    i6++;
                    if (this.outputByte[i7] != 0) {
                        break;
                    }
                    length--;
                }
                if (i3 + length > iArrayOffset4) {
                    CoderResult coderResult = CoderResult.OVERFLOW;
                    charBuffer.position(i2 - charBuffer.arrayOffset());
                    byteBuffer.position(i3 - byteBuffer.arrayOffset());
                    return coderResult;
                }
                for (int length2 = this.outputByte.length - length; length2 < this.outputByte.length; length2++) {
                    int i8 = i3;
                    i3++;
                    bArrArray[i8] = this.outputByte[length2];
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
        String str;
        int iPosition = charBuffer.position();
        while (charBuffer.hasRemaining()) {
            try {
                char c2 = charBuffer.get();
                boolean z2 = true;
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
                if (c2 >= 65534) {
                    CoderResult coderResultUnmappableForLength = CoderResult.unmappableForLength(1);
                    charBuffer.position(iPosition);
                    return coderResultUnmappableForLength;
                }
                int i2 = this.index1[(c2 & this.mask1) >> this.shift] + (c2 & this.mask2);
                if (i2 < 7500) {
                    str = this.index2;
                } else if (i2 < 15000) {
                    i2 -= 7500;
                    str = this.index2a;
                } else if (i2 < 22500) {
                    i2 -= 15000;
                    str = this.index2b;
                } else {
                    i2 -= 22500;
                    str = this.index2c;
                }
                char cCharAt = str.charAt(2 * i2);
                this.outputByte[0] = (byte) ((cCharAt & 65280) >> 8);
                this.outputByte[1] = (byte) (cCharAt & 255);
                char cCharAt2 = str.charAt((2 * i2) + 1);
                this.outputByte[2] = (byte) ((cCharAt2 & 65280) >> 8);
                this.outputByte[3] = (byte) (cCharAt2 & 255);
                int i3 = 0;
                while (true) {
                    if (i3 >= this.outputByte.length) {
                        break;
                    }
                    if (this.outputByte[i3] != 0) {
                        z2 = false;
                        break;
                    }
                    i3++;
                }
                if (z2 && c2 != 0) {
                    CoderResult coderResultUnmappableForLength2 = CoderResult.unmappableForLength(1);
                    charBuffer.position(iPosition);
                    return coderResultUnmappableForLength2;
                }
                int i4 = 0;
                int length = this.outputByte.length;
                while (length > 1) {
                    int i5 = i4;
                    i4++;
                    if (this.outputByte[i5] != 0) {
                        break;
                    }
                    length--;
                }
                if (byteBuffer.remaining() < length) {
                    CoderResult coderResult = CoderResult.OVERFLOW;
                    charBuffer.position(iPosition);
                    return coderResult;
                }
                for (int length2 = this.outputByte.length - length; length2 < this.outputByte.length; length2++) {
                    byteBuffer.put(this.outputByte[length2]);
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

    public byte encode(char c2) {
        return (byte) this.index2.charAt(this.index1[(c2 & this.mask1) >> this.shift] + (c2 & this.mask2));
    }
}
