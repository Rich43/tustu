package sun.nio.cs.ext;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.CoderResult;
import sun.nio.cs.HistoricallyNamedCharset;
import sun.nio.cs.US_ASCII;
import sun.nio.cs.ext.DoubleByte;
import sun.nio.cs.ext.EUC_TW;

/* loaded from: charsets.jar:sun/nio/cs/ext/ISO2022_CN.class */
public class ISO2022_CN extends Charset implements HistoricallyNamedCharset {
    private static final byte ISO_ESC = 27;
    private static final byte ISO_SI = 15;
    private static final byte ISO_SO = 14;
    private static final byte ISO_SS2_7 = 78;
    private static final byte ISO_SS3_7 = 79;
    private static final byte MSB = Byte.MIN_VALUE;
    private static final char REPLACE_CHAR = 65533;
    private static final byte SODesigGB = 0;
    private static final byte SODesigCNS = 1;

    public ISO2022_CN() {
        super("ISO-2022-CN", ExtendedCharsets.aliasesFor("ISO-2022-CN"));
    }

    @Override // sun.nio.cs.HistoricallyNamedCharset
    public String historicalName() {
        return "ISO2022CN";
    }

    @Override // java.nio.charset.Charset
    public boolean contains(Charset charset) {
        return (charset instanceof EUC_CN) || (charset instanceof US_ASCII) || (charset instanceof EUC_TW) || (charset instanceof ISO2022_CN);
    }

    @Override // java.nio.charset.Charset
    public CharsetDecoder newDecoder() {
        return new Decoder(this);
    }

    @Override // java.nio.charset.Charset
    public CharsetEncoder newEncoder() {
        throw new UnsupportedOperationException();
    }

    @Override // java.nio.charset.Charset
    public boolean canEncode() {
        return false;
    }

    /* loaded from: charsets.jar:sun/nio/cs/ext/ISO2022_CN$Decoder.class */
    static class Decoder extends CharsetDecoder {
        private boolean shiftOut;
        private byte currentSODesig;
        private static final Charset gb2312;
        private static final Charset cns;
        private final DoubleByte.Decoder gb2312Decoder;
        private final EUC_TW.Decoder cnsDecoder;
        static final /* synthetic */ boolean $assertionsDisabled;

        static {
            $assertionsDisabled = !ISO2022_CN.class.desiredAssertionStatus();
            gb2312 = new EUC_CN();
            cns = new EUC_TW();
        }

        Decoder(Charset charset) {
            super(charset, 1.0f, 1.0f);
            this.shiftOut = false;
            this.currentSODesig = (byte) 0;
            this.gb2312Decoder = (DoubleByte.Decoder) gb2312.newDecoder();
            this.cnsDecoder = (EUC_TW.Decoder) cns.newDecoder();
        }

        @Override // java.nio.charset.CharsetDecoder
        protected void implReset() {
            this.shiftOut = false;
            this.currentSODesig = (byte) 0;
        }

        private char cnsDecode(byte b2, byte b3, byte b4) {
            int i2;
            byte b5 = (byte) (b2 | Byte.MIN_VALUE);
            byte b6 = (byte) (b3 | Byte.MIN_VALUE);
            if (b4 == 78) {
                i2 = 1;
            } else if (b4 == 79) {
                i2 = 2;
            } else {
                return (char) 65533;
            }
            char[] unicode = this.cnsDecoder.toUnicode(b5 & 255, b6 & 255, i2);
            if (unicode == null || unicode.length == 2) {
                return (char) 65533;
            }
            return unicode[0];
        }

        private char SODecode(byte b2, byte b3, byte b4) {
            byte b5 = (byte) (b2 | Byte.MIN_VALUE);
            byte b6 = (byte) (b3 | Byte.MIN_VALUE);
            if (b4 == 0) {
                return this.gb2312Decoder.decodeDouble(b5 & 255, b6 & 255);
            }
            char[] unicode = this.cnsDecoder.toUnicode(b5 & 255, b6 & 255, 0);
            if (unicode == null) {
                return (char) 65533;
            }
            return unicode[0];
        }

        /* JADX WARN: Code restructure failed: missing block: B:118:0x0278, code lost:
        
            if (r7.remaining() >= 1) goto L122;
         */
        /* JADX WARN: Code restructure failed: missing block: B:119:0x027b, code lost:
        
            r0 = java.nio.charset.CoderResult.OVERFLOW;
         */
        /* JADX WARN: Code restructure failed: missing block: B:120:0x0281, code lost:
        
            r6.position(r8);
         */
        /* JADX WARN: Code restructure failed: missing block: B:121:0x0288, code lost:
        
            return r0;
         */
        /* JADX WARN: Code restructure failed: missing block: B:123:0x028d, code lost:
        
            if (r5.shiftOut != false) goto L162;
         */
        /* JADX WARN: Code restructure failed: missing block: B:124:0x0290, code lost:
        
            r7.put((char) (r9 & 255));
            r8 = r8 + r13;
         */
        /* JADX WARN: Code restructure failed: missing block: B:126:0x02a9, code lost:
        
            if (r6.remaining() >= 1) goto L130;
         */
        /* JADX WARN: Code restructure failed: missing block: B:127:0x02ac, code lost:
        
            r0 = java.nio.charset.CoderResult.UNDERFLOW;
         */
        /* JADX WARN: Code restructure failed: missing block: B:128:0x02b2, code lost:
        
            r6.position(r8);
         */
        /* JADX WARN: Code restructure failed: missing block: B:129:0x02b9, code lost:
        
            return r0;
         */
        /* JADX WARN: Code restructure failed: missing block: B:130:0x02ba, code lost:
        
            r13 = r13 + 1;
            r0 = SODecode(r9, r6.get(), r5.currentSODesig);
         */
        /* JADX WARN: Code restructure failed: missing block: B:131:0x02d5, code lost:
        
            if (r0 != 65533) goto L135;
         */
        /* JADX WARN: Code restructure failed: missing block: B:132:0x02d8, code lost:
        
            r0 = java.nio.charset.CoderResult.unmappableForLength(r13);
         */
        /* JADX WARN: Code restructure failed: missing block: B:133:0x02e0, code lost:
        
            r6.position(r8);
         */
        /* JADX WARN: Code restructure failed: missing block: B:134:0x02e7, code lost:
        
            return r0;
         */
        /* JADX WARN: Code restructure failed: missing block: B:135:0x02e8, code lost:
        
            r7.put(r0);
            r8 = r8 + r13;
         */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct code enable 'Show inconsistent code' option in preferences
        */
        private java.nio.charset.CoderResult decodeBufferLoop(java.nio.ByteBuffer r6, java.nio.CharBuffer r7) {
            /*
                Method dump skipped, instructions count: 784
                To view this dump change 'Code comments level' option to 'DEBUG'
            */
            throw new UnsupportedOperationException("Method not decompiled: sun.nio.cs.ext.ISO2022_CN.Decoder.decodeBufferLoop(java.nio.ByteBuffer, java.nio.CharBuffer):java.nio.charset.CoderResult");
        }

        /* JADX WARN: Code restructure failed: missing block: B:138:0x042e, code lost:
        
            if ((r0 - r18) >= 1) goto L142;
         */
        /* JADX WARN: Code restructure failed: missing block: B:139:0x0431, code lost:
        
            r0 = java.nio.charset.CoderResult.OVERFLOW;
         */
        /* JADX WARN: Code restructure failed: missing block: B:140:0x0437, code lost:
        
            r6.position(r15 - r6.arrayOffset());
            r7.position(r18 - r7.arrayOffset());
         */
        /* JADX WARN: Code restructure failed: missing block: B:141:0x0450, code lost:
        
            return r0;
         */
        /* JADX WARN: Code restructure failed: missing block: B:143:0x0455, code lost:
        
            if (r5.shiftOut != false) goto L145;
         */
        /* JADX WARN: Code restructure failed: missing block: B:144:0x0458, code lost:
        
            r1 = r18;
            r18 = r18 + 1;
            r0[r1] = (char) (r9 & 255);
         */
        /* JADX WARN: Code restructure failed: missing block: B:146:0x0470, code lost:
        
            if ((r15 + 2) <= r0) goto L150;
         */
        /* JADX WARN: Code restructure failed: missing block: B:147:0x0473, code lost:
        
            r0 = java.nio.charset.CoderResult.UNDERFLOW;
         */
        /* JADX WARN: Code restructure failed: missing block: B:148:0x0479, code lost:
        
            r6.position(r15 - r6.arrayOffset());
            r7.position(r18 - r7.arrayOffset());
         */
        /* JADX WARN: Code restructure failed: missing block: B:149:0x0492, code lost:
        
            return r0;
         */
        /* JADX WARN: Code restructure failed: missing block: B:150:0x0493, code lost:
        
            r8 = r8 + 1;
            r0 = SODecode(r9, r0[r15 + 1], r5.currentSODesig);
         */
        /* JADX WARN: Code restructure failed: missing block: B:151:0x04b1, code lost:
        
            if (r0 != 65533) goto L155;
         */
        /* JADX WARN: Code restructure failed: missing block: B:152:0x04b4, code lost:
        
            r0 = java.nio.charset.CoderResult.unmappableForLength(r8);
         */
        /* JADX WARN: Code restructure failed: missing block: B:153:0x04bb, code lost:
        
            r6.position(r15 - r6.arrayOffset());
            r7.position(r18 - r7.arrayOffset());
         */
        /* JADX WARN: Code restructure failed: missing block: B:154:0x04d4, code lost:
        
            return r0;
         */
        /* JADX WARN: Code restructure failed: missing block: B:155:0x04d5, code lost:
        
            r1 = r18;
            r18 = r18 + 1;
            r0[r1] = r0;
         */
        /* JADX WARN: Code restructure failed: missing block: B:156:0x04df, code lost:
        
            r15 = r15 + r8;
         */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct code enable 'Show inconsistent code' option in preferences
        */
        private java.nio.charset.CoderResult decodeArrayLoop(java.nio.ByteBuffer r6, java.nio.CharBuffer r7) {
            /*
                Method dump skipped, instructions count: 1317
                To view this dump change 'Code comments level' option to 'DEBUG'
            */
            throw new UnsupportedOperationException("Method not decompiled: sun.nio.cs.ext.ISO2022_CN.Decoder.decodeArrayLoop(java.nio.ByteBuffer, java.nio.CharBuffer):java.nio.charset.CoderResult");
        }

        @Override // java.nio.charset.CharsetDecoder
        protected CoderResult decodeLoop(ByteBuffer byteBuffer, CharBuffer charBuffer) {
            if (byteBuffer.hasArray() && charBuffer.hasArray()) {
                return decodeArrayLoop(byteBuffer, charBuffer);
            }
            return decodeBufferLoop(byteBuffer, charBuffer);
        }
    }
}
