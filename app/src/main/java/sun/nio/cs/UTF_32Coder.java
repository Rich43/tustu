package sun.nio.cs;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.CoderResult;

/* loaded from: rt.jar:sun/nio/cs/UTF_32Coder.class */
class UTF_32Coder {
    protected static final int BOM_BIG = 65279;
    protected static final int BOM_LITTLE = -131072;
    protected static final int NONE = 0;
    protected static final int BIG = 1;
    protected static final int LITTLE = 2;

    UTF_32Coder() {
    }

    /* loaded from: rt.jar:sun/nio/cs/UTF_32Coder$Decoder.class */
    protected static class Decoder extends CharsetDecoder {
        private int currentBO;
        private int expectedBO;

        protected Decoder(Charset charset, int i2) {
            super(charset, 0.25f, 1.0f);
            this.expectedBO = i2;
            this.currentBO = 0;
        }

        private int getCP(ByteBuffer byteBuffer) {
            if (this.currentBO == 1) {
                return ((byteBuffer.get() & 255) << 24) | ((byteBuffer.get() & 255) << 16) | ((byteBuffer.get() & 255) << 8) | (byteBuffer.get() & 255);
            }
            return (byteBuffer.get() & 255) | ((byteBuffer.get() & 255) << 8) | ((byteBuffer.get() & 255) << 16) | ((byteBuffer.get() & 255) << 24);
        }

        @Override // java.nio.charset.CharsetDecoder
        protected CoderResult decodeLoop(ByteBuffer byteBuffer, CharBuffer charBuffer) {
            if (byteBuffer.remaining() < 4) {
                return CoderResult.UNDERFLOW;
            }
            int iPosition = byteBuffer.position();
            try {
                if (this.currentBO == 0) {
                    int i2 = ((byteBuffer.get() & 255) << 24) | ((byteBuffer.get() & 255) << 16) | ((byteBuffer.get() & 255) << 8) | (byteBuffer.get() & 255);
                    if (i2 == UTF_32Coder.BOM_BIG && this.expectedBO != 2) {
                        this.currentBO = 1;
                        iPosition += 4;
                    } else if (i2 != UTF_32Coder.BOM_LITTLE || this.expectedBO == 1) {
                        if (this.expectedBO == 0) {
                            this.currentBO = 1;
                        } else {
                            this.currentBO = this.expectedBO;
                        }
                        byteBuffer.position(iPosition);
                    } else {
                        this.currentBO = 2;
                        iPosition += 4;
                    }
                }
                while (byteBuffer.remaining() >= 4) {
                    int cp = getCP(byteBuffer);
                    if (Character.isBmpCodePoint(cp)) {
                        if (!charBuffer.hasRemaining()) {
                            CoderResult coderResult = CoderResult.OVERFLOW;
                            byteBuffer.position(iPosition);
                            return coderResult;
                        }
                        iPosition += 4;
                        charBuffer.put((char) cp);
                    } else {
                        if (!Character.isValidCodePoint(cp)) {
                            CoderResult coderResultMalformedForLength = CoderResult.malformedForLength(4);
                            byteBuffer.position(iPosition);
                            return coderResultMalformedForLength;
                        }
                        if (charBuffer.remaining() < 2) {
                            CoderResult coderResult2 = CoderResult.OVERFLOW;
                            byteBuffer.position(iPosition);
                            return coderResult2;
                        }
                        iPosition += 4;
                        charBuffer.put(Character.highSurrogate(cp));
                        charBuffer.put(Character.lowSurrogate(cp));
                    }
                }
                CoderResult coderResult3 = CoderResult.UNDERFLOW;
                byteBuffer.position(iPosition);
                return coderResult3;
            } catch (Throwable th) {
                byteBuffer.position(iPosition);
                throw th;
            }
        }

        @Override // java.nio.charset.CharsetDecoder
        protected void implReset() {
            this.currentBO = 0;
        }
    }

    /* loaded from: rt.jar:sun/nio/cs/UTF_32Coder$Encoder.class */
    protected static class Encoder extends CharsetEncoder {
        private boolean doBOM;
        private boolean doneBOM;
        private int byteOrder;

        protected void put(int i2, ByteBuffer byteBuffer) {
            if (this.byteOrder == 1) {
                byteBuffer.put((byte) (i2 >> 24));
                byteBuffer.put((byte) (i2 >> 16));
                byteBuffer.put((byte) (i2 >> 8));
                byteBuffer.put((byte) i2);
                return;
            }
            byteBuffer.put((byte) i2);
            byteBuffer.put((byte) (i2 >> 8));
            byteBuffer.put((byte) (i2 >> 16));
            byteBuffer.put((byte) (i2 >> 24));
        }

        protected Encoder(Charset charset, int i2, boolean z2) {
            super(charset, 4.0f, z2 ? 8.0f : 4.0f, i2 == 1 ? new byte[]{0, 0, -1, -3} : new byte[]{-3, -1, 0, 0});
            this.doBOM = false;
            this.doneBOM = true;
            this.byteOrder = i2;
            this.doBOM = z2;
            this.doneBOM = !z2;
        }

        @Override // java.nio.charset.CharsetEncoder
        protected CoderResult encodeLoop(CharBuffer charBuffer, ByteBuffer byteBuffer) {
            int iPosition = charBuffer.position();
            if (!this.doneBOM && charBuffer.hasRemaining()) {
                if (byteBuffer.remaining() < 4) {
                    return CoderResult.OVERFLOW;
                }
                put(UTF_32Coder.BOM_BIG, byteBuffer);
                this.doneBOM = true;
            }
            while (charBuffer.hasRemaining()) {
                try {
                    char c2 = charBuffer.get();
                    if (Character.isSurrogate(c2)) {
                        if (!Character.isHighSurrogate(c2)) {
                            CoderResult coderResultMalformedForLength = CoderResult.malformedForLength(1);
                            charBuffer.position(iPosition);
                            return coderResultMalformedForLength;
                        }
                        if (!charBuffer.hasRemaining()) {
                            CoderResult coderResult = CoderResult.UNDERFLOW;
                            charBuffer.position(iPosition);
                            return coderResult;
                        }
                        char c3 = charBuffer.get();
                        if (!Character.isLowSurrogate(c3)) {
                            CoderResult coderResultMalformedForLength2 = CoderResult.malformedForLength(1);
                            charBuffer.position(iPosition);
                            return coderResultMalformedForLength2;
                        }
                        if (byteBuffer.remaining() < 4) {
                            CoderResult coderResult2 = CoderResult.OVERFLOW;
                            charBuffer.position(iPosition);
                            return coderResult2;
                        }
                        iPosition += 2;
                        put(Character.toCodePoint(c2, c3), byteBuffer);
                    } else {
                        if (byteBuffer.remaining() < 4) {
                            CoderResult coderResult3 = CoderResult.OVERFLOW;
                            charBuffer.position(iPosition);
                            return coderResult3;
                        }
                        iPosition++;
                        put(c2, byteBuffer);
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
        protected void implReset() {
            this.doneBOM = !this.doBOM;
        }
    }
}
