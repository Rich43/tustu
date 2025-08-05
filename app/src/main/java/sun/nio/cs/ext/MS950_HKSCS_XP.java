package sun.nio.cs.ext;

import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import sun.nio.cs.ext.DoubleByte;
import sun.nio.cs.ext.HKSCS;

/* loaded from: charsets.jar:sun/nio/cs/ext/MS950_HKSCS_XP.class */
public class MS950_HKSCS_XP extends Charset {
    public MS950_HKSCS_XP() {
        super("x-MS950-HKSCS-XP", ExtendedCharsets.aliasesFor("x-MS950-HKSCS-XP"));
    }

    @Override // java.nio.charset.Charset
    public boolean contains(Charset charset) {
        return charset.name().equals("US-ASCII") || (charset instanceof MS950) || (charset instanceof MS950_HKSCS_XP);
    }

    @Override // java.nio.charset.Charset
    public CharsetDecoder newDecoder() {
        return new Decoder(this);
    }

    @Override // java.nio.charset.Charset
    public CharsetEncoder newEncoder() {
        return new Encoder(this);
    }

    /* loaded from: charsets.jar:sun/nio/cs/ext/MS950_HKSCS_XP$Decoder.class */
    static class Decoder extends HKSCS.Decoder {
        private static DoubleByte.Decoder ms950 = (DoubleByte.Decoder) new MS950().newDecoder();
        private static char[][] b2cBmp = new char[256];

        /* JADX WARN: Type inference failed for: r0v4, types: [char[], char[][]] */
        static {
            initb2c(b2cBmp, HKSCS_XPMapping.b2cBmpStr);
        }

        @Override // sun.nio.cs.ext.HKSCS.Decoder
        public char decodeDoubleEx(int i2, int i3) {
            return (char) 65533;
        }

        private Decoder(Charset charset) {
            super(charset, ms950, b2cBmp, (char[][]) null);
        }
    }

    /* loaded from: charsets.jar:sun/nio/cs/ext/MS950_HKSCS_XP$Encoder.class */
    private static class Encoder extends HKSCS.Encoder {
        private static DoubleByte.Encoder ms950 = (DoubleByte.Encoder) new MS950().newEncoder();
        static char[][] c2bBmp = new char[256];

        /* JADX WARN: Type inference failed for: r0v4, types: [char[], char[][]] */
        static {
            initc2b(c2bBmp, HKSCS_XPMapping.b2cBmpStr, null);
        }

        @Override // sun.nio.cs.ext.HKSCS.Encoder
        public int encodeSupp(int i2) {
            return 65533;
        }

        private Encoder(Charset charset) {
            super(charset, ms950, c2bBmp, (char[][]) null);
        }
    }
}
