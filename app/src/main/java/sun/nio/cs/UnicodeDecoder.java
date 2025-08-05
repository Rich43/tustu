package sun.nio.cs;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CoderResult;

/* loaded from: rt.jar:sun/nio/cs/UnicodeDecoder.class */
abstract class UnicodeDecoder extends CharsetDecoder {
    protected static final char BYTE_ORDER_MARK = 65279;
    protected static final char REVERSED_MARK = 65534;
    protected static final int NONE = 0;
    protected static final int BIG = 1;
    protected static final int LITTLE = 2;
    private final int expectedByteOrder;
    private int currentByteOrder;
    private int defaultByteOrder;

    public UnicodeDecoder(Charset charset, int i2) {
        super(charset, 0.5f, 1.0f);
        this.defaultByteOrder = 1;
        this.currentByteOrder = i2;
        this.expectedByteOrder = i2;
    }

    public UnicodeDecoder(Charset charset, int i2, int i3) {
        this(charset, i2);
        this.defaultByteOrder = i3;
    }

    private char decode(int i2, int i3) {
        if (this.currentByteOrder == 1) {
            return (char) ((i2 << 8) | i3);
        }
        return (char) ((i3 << 8) | i2);
    }

    @Override // java.nio.charset.CharsetDecoder
    protected CoderResult decodeLoop(ByteBuffer byteBuffer, CharBuffer charBuffer) {
        int iPosition = byteBuffer.position();
        while (byteBuffer.remaining() > 1) {
            try {
                int i2 = byteBuffer.get() & 255;
                int i3 = byteBuffer.get() & 255;
                if (this.currentByteOrder == 0) {
                    char c2 = (char) ((i2 << 8) | i3);
                    if (c2 == BYTE_ORDER_MARK) {
                        this.currentByteOrder = 1;
                        iPosition += 2;
                    } else if (c2 == 65534) {
                        this.currentByteOrder = 2;
                        iPosition += 2;
                    } else {
                        this.currentByteOrder = this.defaultByteOrder;
                    }
                }
                char cDecode = decode(i2, i3);
                if (cDecode == 65534) {
                    CoderResult coderResultMalformedForLength = CoderResult.malformedForLength(2);
                    byteBuffer.position(iPosition);
                    return coderResultMalformedForLength;
                }
                if (Character.isSurrogate(cDecode)) {
                    if (!Character.isHighSurrogate(cDecode)) {
                        CoderResult coderResultMalformedForLength2 = CoderResult.malformedForLength(2);
                        byteBuffer.position(iPosition);
                        return coderResultMalformedForLength2;
                    }
                    if (byteBuffer.remaining() < 2) {
                        CoderResult coderResult = CoderResult.UNDERFLOW;
                        byteBuffer.position(iPosition);
                        return coderResult;
                    }
                    char cDecode2 = decode(byteBuffer.get() & 255, byteBuffer.get() & 255);
                    if (!Character.isLowSurrogate(cDecode2)) {
                        CoderResult coderResultMalformedForLength3 = CoderResult.malformedForLength(4);
                        byteBuffer.position(iPosition);
                        return coderResultMalformedForLength3;
                    }
                    if (charBuffer.remaining() < 2) {
                        CoderResult coderResult2 = CoderResult.OVERFLOW;
                        byteBuffer.position(iPosition);
                        return coderResult2;
                    }
                    iPosition += 4;
                    charBuffer.put(cDecode);
                    charBuffer.put(cDecode2);
                } else {
                    if (!charBuffer.hasRemaining()) {
                        CoderResult coderResult3 = CoderResult.OVERFLOW;
                        byteBuffer.position(iPosition);
                        return coderResult3;
                    }
                    iPosition += 2;
                    charBuffer.put(cDecode);
                }
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
    protected void implReset() {
        this.currentByteOrder = this.expectedByteOrder;
    }
}
