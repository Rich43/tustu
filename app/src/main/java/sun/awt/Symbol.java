package sun.awt;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.CoderResult;
import sun.security.krb5.internal.Krb5;

/* loaded from: rt.jar:sun/awt/Symbol.class */
public class Symbol extends Charset {
    public Symbol() {
        super("Symbol", null);
    }

    @Override // java.nio.charset.Charset
    public CharsetEncoder newEncoder() {
        return new Encoder(this);
    }

    @Override // java.nio.charset.Charset
    public CharsetDecoder newDecoder() {
        throw new Error("Decoder is not implemented for Symbol Charset");
    }

    @Override // java.nio.charset.Charset
    public boolean contains(Charset charset) {
        return charset instanceof Symbol;
    }

    /* loaded from: rt.jar:sun/awt/Symbol$Encoder.class */
    private static class Encoder extends CharsetEncoder {
        private static byte[] table_math;
        private static byte[] table_greek;
        static final /* synthetic */ boolean $assertionsDisabled;

        static {
            $assertionsDisabled = !Symbol.class.desiredAssertionStatus();
            table_math = new byte[]{34, 0, 100, 36, 0, -58, 68, -47, -50, -49, 0, 0, 0, 39, 0, 80, 0, -27, 45, 0, 0, -92, 0, 42, -80, -73, -42, 0, 0, -75, -91, 0, 0, 0, 0, -67, 0, 0, 0, -39, -38, -57, -56, -14, 0, 0, 0, 0, 0, 0, 0, 0, 92, 0, 0, 0, 0, 0, 0, 0, 126, 0, 0, 0, 0, 0, 0, 0, 0, 64, 0, 0, -69, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -71, -70, 0, 0, -93, -77, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -52, -55, -53, 0, -51, -54, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -59, 0, -60, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 94, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -32, -41, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -68};
            table_greek = new byte[]{65, 66, 71, 68, 69, 90, 72, 81, 73, 75, 76, 77, 78, 88, 79, 80, 82, 0, 83, 84, 85, 70, 67, 89, 87, 0, 0, 0, 0, 0, 0, 0, 97, 98, 103, 100, 101, 122, 104, 113, 105, 107, 108, 109, 110, 120, 111, 112, 114, 86, 115, 116, 117, 102, 99, 121, 119, 0, 0, 0, 0, 0, 0, 0, 74, -95, 0, 0, 106, 118};
        }

        public Encoder(Charset charset) {
            super(charset, 1.0f, 1.0f);
        }

        @Override // java.nio.charset.CharsetEncoder
        public boolean canEncode(char c2) {
            if (c2 >= 8704 && c2 <= 8943) {
                if (table_math[c2 - 8704] != 0) {
                    return true;
                }
                return false;
            }
            if (c2 >= 913 && c2 <= 982 && table_greek[c2 - Krb5.ASN1_UNSUPPORTED_TYPE] != 0) {
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
                    if (c2 >= 8704 && c2 <= 8943) {
                        int i4 = i3;
                        i3++;
                        bArrArray[i4] = table_math[c2 - 8704];
                    } else if (c2 >= 913 && c2 <= 982) {
                        int i5 = i3;
                        i3++;
                        bArrArray[i5] = table_greek[c2 - Krb5.ASN1_UNSUPPORTED_TYPE];
                    }
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
