package sun.awt.windows;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.CoderResult;

/* loaded from: rt.jar:sun/awt/windows/WingDings.class */
public final class WingDings extends Charset {
    public WingDings() {
        super("WingDings", null);
    }

    @Override // java.nio.charset.Charset
    public CharsetEncoder newEncoder() {
        return new Encoder(this);
    }

    @Override // java.nio.charset.Charset
    public CharsetDecoder newDecoder() {
        throw new Error("Decoder isn't implemented for WingDings Charset");
    }

    @Override // java.nio.charset.Charset
    public boolean contains(Charset charset) {
        return charset instanceof WingDings;
    }

    /* loaded from: rt.jar:sun/awt/windows/WingDings$Encoder.class */
    private static class Encoder extends CharsetEncoder {
        private static byte[] table;
        static final /* synthetic */ boolean $assertionsDisabled;

        static {
            $assertionsDisabled = !WingDings.class.desiredAssertionStatus();
            table = new byte[]{0, 35, 34, 0, 0, 0, 41, 62, 81, 42, 0, 0, 65, 63, 0, 0, 0, 0, 0, -4, 0, 0, 0, -5, 0, 0, 0, 0, 0, 0, 86, 0, 88, 89, 0, 0, 0, 0, 0, 0, 0, 0, -75, 0, 0, 0, 0, 0, -74, 0, 0, 0, -83, -81, -84, 0, 0, 0, 0, 0, 0, 0, 0, 124, 123, 0, 0, 0, 84, 0, 0, 0, 0, 0, 0, 0, 0, -90, 0, 0, 0, 113, 114, 0, 0, 0, 117, 0, 0, 0, 0, 0, 0, 125, 126, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -116, -115, -114, -113, -112, -111, -110, -109, -108, -107, -127, -126, -125, -124, -123, -122, -121, -120, -119, -118, -116, -115, -114, -113, -112, -111, -110, -109, -108, -107, -24, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -24, -40, 0, 0, -60, -58, 0, 0, -16, 0, 0, 0, 0, 0, 0, 0, 0, 0, -36, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        }

        public Encoder(Charset charset) {
            super(charset, 1.0f, 1.0f);
        }

        @Override // java.nio.charset.CharsetEncoder
        public boolean canEncode(char c2) {
            if (c2 >= 9985 && c2 <= 10174 && table[c2 - 9984] != 0) {
                return true;
            }
            return false;
        }

        @Override // java.nio.charset.CharsetEncoder
        protected CoderResult encodeLoop(CharBuffer charBuffer, ByteBuffer byteBuffer) {
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
                    if (iArrayOffset4 - i3 < 1) {
                        CoderResult coderResult = CoderResult.OVERFLOW;
                        charBuffer.position(i2 - charBuffer.arrayOffset());
                        byteBuffer.position(i3 - byteBuffer.arrayOffset());
                        return coderResult;
                    }
                    if (!canEncode(c2)) {
                        CoderResult coderResultUnmappableForLength = CoderResult.unmappableForLength(1);
                        charBuffer.position(i2 - charBuffer.arrayOffset());
                        byteBuffer.position(i3 - byteBuffer.arrayOffset());
                        return coderResultUnmappableForLength;
                    }
                    i2++;
                    int i4 = i3;
                    i3++;
                    bArrArray[i4] = table[c2 - 9984];
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

        @Override // java.nio.charset.CharsetEncoder
        public boolean isLegalReplacement(byte[] bArr) {
            return true;
        }
    }
}
