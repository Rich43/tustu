package sun.nio.cs;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.CoderResult;
import sun.nio.cs.Surrogate;

/* loaded from: rt.jar:sun/nio/cs/UnicodeEncoder.class */
public abstract class UnicodeEncoder extends CharsetEncoder {
    protected static final char BYTE_ORDER_MARK = 65279;
    protected static final char REVERSED_MARK = 65534;
    protected static final int BIG = 0;
    protected static final int LITTLE = 1;
    private int byteOrder;
    private boolean usesMark;
    private boolean needsMark;
    private final Surrogate.Parser sgp;

    protected UnicodeEncoder(Charset charset, int i2, boolean z2) {
        super(charset, 2.0f, z2 ? 4.0f : 2.0f, i2 == 0 ? new byte[]{-1, -3} : new byte[]{-3, -1});
        this.sgp = new Surrogate.Parser();
        this.needsMark = z2;
        this.usesMark = z2;
        this.byteOrder = i2;
    }

    private void put(char c2, ByteBuffer byteBuffer) {
        if (this.byteOrder == 0) {
            byteBuffer.put((byte) (c2 >> '\b'));
            byteBuffer.put((byte) (c2 & 255));
        } else {
            byteBuffer.put((byte) (c2 & 255));
            byteBuffer.put((byte) (c2 >> '\b'));
        }
    }

    @Override // java.nio.charset.CharsetEncoder
    protected CoderResult encodeLoop(CharBuffer charBuffer, ByteBuffer byteBuffer) {
        int iPosition = charBuffer.position();
        if (this.needsMark && charBuffer.hasRemaining()) {
            if (byteBuffer.remaining() < 2) {
                return CoderResult.OVERFLOW;
            }
            put((char) 65279, byteBuffer);
            this.needsMark = false;
        }
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
                    if (byteBuffer.remaining() < 4) {
                        CoderResult coderResult = CoderResult.OVERFLOW;
                        charBuffer.position(iPosition);
                        return coderResult;
                    }
                    iPosition += 2;
                    put(Character.highSurrogate(i2), byteBuffer);
                    put(Character.lowSurrogate(i2), byteBuffer);
                } else {
                    if (byteBuffer.remaining() < 2) {
                        CoderResult coderResult2 = CoderResult.OVERFLOW;
                        charBuffer.position(iPosition);
                        return coderResult2;
                    }
                    iPosition++;
                    put(c2, byteBuffer);
                }
            } catch (Throwable th) {
                charBuffer.position(iPosition);
                throw th;
            }
        }
        CoderResult coderResult3 = CoderResult.UNDERFLOW;
        charBuffer.position(iPosition);
        return coderResult3;
    }

    @Override // java.nio.charset.CharsetEncoder
    protected void implReset() {
        this.needsMark = this.usesMark;
    }

    @Override // java.nio.charset.CharsetEncoder
    public boolean canEncode(char c2) {
        return !Character.isSurrogate(c2);
    }
}
