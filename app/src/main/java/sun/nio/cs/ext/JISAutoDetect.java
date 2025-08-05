package sun.nio.cs.ext;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.CoderResult;
import java.security.AccessController;
import sun.nio.cs.HistoricallyNamedCharset;
import sun.security.action.GetPropertyAction;

/* loaded from: charsets.jar:sun/nio/cs/ext/JISAutoDetect.class */
public class JISAutoDetect extends Charset implements HistoricallyNamedCharset {
    private static final int EUCJP_MASK = 1;
    private static final int SJIS2B_MASK = 2;
    private static final int SJIS1B_MASK = 4;
    private static final int EUCJP_KANA1_MASK = 8;
    private static final int EUCJP_KANA2_MASK = 16;

    public JISAutoDetect() {
        super("x-JISAutoDetect", ExtendedCharsets.aliasesFor("x-JISAutoDetect"));
    }

    @Override // java.nio.charset.Charset
    public boolean contains(Charset charset) {
        return charset.name().equals("US-ASCII") || (charset instanceof SJIS) || (charset instanceof EUC_JP) || (charset instanceof ISO2022_JP);
    }

    @Override // java.nio.charset.Charset
    public boolean canEncode() {
        return false;
    }

    @Override // java.nio.charset.Charset
    public CharsetDecoder newDecoder() {
        return new Decoder(this);
    }

    @Override // sun.nio.cs.HistoricallyNamedCharset
    public String historicalName() {
        return "JISAutoDetect";
    }

    @Override // java.nio.charset.Charset
    public CharsetEncoder newEncoder() {
        throw new UnsupportedOperationException();
    }

    public static byte[] getByteMask1() {
        return Decoder.maskTable1;
    }

    public static byte[] getByteMask2() {
        return Decoder.maskTable2;
    }

    public static final boolean canBeSJIS1B(int i2) {
        return (i2 & 4) != 0;
    }

    public static final boolean canBeEUCJP(int i2) {
        return (i2 & 1) != 0;
    }

    public static final boolean canBeEUCKana(int i2, int i3) {
        return ((i2 & 8) == 0 || (i3 & 16) == 0) ? false : true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static boolean looksLikeJapanese(CharBuffer charBuffer) {
        int i2 = 0;
        int i3 = 0;
        while (charBuffer.hasRemaining()) {
            char c2 = charBuffer.get();
            if (12352 <= c2 && c2 <= 12447) {
                i2++;
                if (i2 > 1) {
                    return true;
                }
            }
            if (65381 <= c2 && c2 <= 65439) {
                i3++;
                if (i3 > 1) {
                    return true;
                }
            }
        }
        return false;
    }

    /* loaded from: charsets.jar:sun/nio/cs/ext/JISAutoDetect$Decoder.class */
    private static class Decoder extends CharsetDecoder {
        private DelegatableDecoder detectedDecoder;
        private static final String SJISName = getSJISName();
        private static final String EUCJPName = getEUCJPName();
        private static final byte[] maskTable1 = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 3, 3, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 0, 5, 5, 5, 13, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 1, 1, 0};
        private static final byte[] maskTable2 = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 0, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 23, 23, 23, 23, 23, 23, 23, 23, 23, 23, 23, 23, 23, 23, 23, 23, 23, 23, 23, 23, 23, 23, 23, 23, 23, 23, 23, 23, 23, 23, 23, 23, 23, 23, 23, 23, 23, 23, 23, 23, 23, 23, 23, 23, 23, 23, 23, 23, 23, 23, 23, 23, 23, 23, 23, 23, 23, 23, 23, 23, 23, 23, 23, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 3, 3, 3, 3, 3, 3, 3, 3, 3, 1, 1, 0};

        public Decoder(Charset charset) {
            super(charset, 0.5f, 1.0f);
            this.detectedDecoder = null;
        }

        private static boolean isPlainASCII(byte b2) {
            return b2 >= 0 && b2 != 27;
        }

        private static void copyLeadingASCII(ByteBuffer byteBuffer, CharBuffer charBuffer) {
            int iPosition = byteBuffer.position();
            int iMin = iPosition + Math.min(byteBuffer.remaining(), charBuffer.remaining());
            int i2 = iPosition;
            while (i2 < iMin) {
                byte b2 = byteBuffer.get(i2);
                if (!isPlainASCII(b2)) {
                    break;
                }
                charBuffer.put((char) (b2 & 255));
                i2++;
            }
            byteBuffer.position(i2);
        }

        private CoderResult decodeLoop(Charset charset, ByteBuffer byteBuffer, CharBuffer charBuffer) {
            this.detectedDecoder = (DelegatableDecoder) charset.newDecoder();
            return this.detectedDecoder.decodeLoop(byteBuffer, charBuffer);
        }

        @Override // java.nio.charset.CharsetDecoder
        protected CoderResult decodeLoop(ByteBuffer byteBuffer, CharBuffer charBuffer) {
            if (this.detectedDecoder == null) {
                copyLeadingASCII(byteBuffer, charBuffer);
                if (!byteBuffer.hasRemaining()) {
                    return CoderResult.UNDERFLOW;
                }
                if (!charBuffer.hasRemaining()) {
                    return CoderResult.OVERFLOW;
                }
                int iLimit = (int) (byteBuffer.limit() * maxCharsPerByte());
                CharBuffer charBufferAllocate = CharBuffer.allocate(iLimit);
                Charset charsetForName = Charset.forName("ISO-2022-JP");
                if (!((DelegatableDecoder) charsetForName.newDecoder()).decodeLoop(byteBuffer.asReadOnlyBuffer(), charBufferAllocate).isError()) {
                    return decodeLoop(charsetForName, byteBuffer, charBuffer);
                }
                Charset charsetForName2 = Charset.forName(EUCJPName);
                Charset charsetForName3 = Charset.forName(SJISName);
                DelegatableDecoder delegatableDecoder = (DelegatableDecoder) charsetForName2.newDecoder();
                ByteBuffer byteBufferAsReadOnlyBuffer = byteBuffer.asReadOnlyBuffer();
                charBufferAllocate.clear();
                if (delegatableDecoder.decodeLoop(byteBufferAsReadOnlyBuffer, charBufferAllocate).isError()) {
                    return decodeLoop(charsetForName3, byteBuffer, charBuffer);
                }
                DelegatableDecoder delegatableDecoder2 = (DelegatableDecoder) charsetForName3.newDecoder();
                ByteBuffer byteBufferAsReadOnlyBuffer2 = byteBuffer.asReadOnlyBuffer();
                if (delegatableDecoder2.decodeLoop(byteBufferAsReadOnlyBuffer2, CharBuffer.allocate(iLimit)).isError()) {
                    return decodeLoop(charsetForName2, byteBuffer, charBuffer);
                }
                if (byteBufferAsReadOnlyBuffer.position() > byteBufferAsReadOnlyBuffer2.position()) {
                    return decodeLoop(charsetForName2, byteBuffer, charBuffer);
                }
                if (byteBufferAsReadOnlyBuffer.position() < byteBufferAsReadOnlyBuffer2.position()) {
                    return decodeLoop(charsetForName3, byteBuffer, charBuffer);
                }
                if (byteBuffer.position() == byteBufferAsReadOnlyBuffer.position()) {
                    return CoderResult.UNDERFLOW;
                }
                charBufferAllocate.flip();
                return decodeLoop(JISAutoDetect.looksLikeJapanese(charBufferAllocate) ? charsetForName2 : charsetForName3, byteBuffer, charBuffer);
            }
            return this.detectedDecoder.decodeLoop(byteBuffer, charBuffer);
        }

        @Override // java.nio.charset.CharsetDecoder
        protected void implReset() {
            this.detectedDecoder = null;
        }

        @Override // java.nio.charset.CharsetDecoder
        protected CoderResult implFlush(CharBuffer charBuffer) {
            if (this.detectedDecoder != null) {
                return this.detectedDecoder.implFlush(charBuffer);
            }
            return super.implFlush(charBuffer);
        }

        @Override // java.nio.charset.CharsetDecoder
        public boolean isAutoDetecting() {
            return true;
        }

        @Override // java.nio.charset.CharsetDecoder
        public boolean isCharsetDetected() {
            return this.detectedDecoder != null;
        }

        @Override // java.nio.charset.CharsetDecoder
        public Charset detectedCharset() {
            if (this.detectedDecoder == null) {
                throw new IllegalStateException("charset not yet detected");
            }
            return ((CharsetDecoder) this.detectedDecoder).charset();
        }

        private static String getSJISName() {
            String str = (String) AccessController.doPrivileged(new GetPropertyAction("os.name"));
            if (str.equals("Solaris") || str.equals("SunOS")) {
                return "PCK";
            }
            if (str.startsWith("Windows")) {
                return "windows-31J";
            }
            return "Shift_JIS";
        }

        private static String getEUCJPName() {
            String str = (String) AccessController.doPrivileged(new GetPropertyAction("os.name"));
            if (str.equals("Solaris") || str.equals("SunOS")) {
                return "x-eucjp-open";
            }
            return "EUC_JP";
        }
    }
}
