package sun.nio.cs;

import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;

/* loaded from: rt.jar:sun/nio/cs/UTF_16LE_BOM.class */
class UTF_16LE_BOM extends Unicode {
    public UTF_16LE_BOM() {
        super("x-UTF-16LE-BOM", StandardCharsets.aliases_UTF_16LE_BOM);
    }

    @Override // sun.nio.cs.HistoricallyNamedCharset
    public String historicalName() {
        return "UnicodeLittle";
    }

    @Override // java.nio.charset.Charset
    public CharsetDecoder newDecoder() {
        return new Decoder(this);
    }

    @Override // java.nio.charset.Charset
    public CharsetEncoder newEncoder() {
        return new Encoder(this);
    }

    /* loaded from: rt.jar:sun/nio/cs/UTF_16LE_BOM$Decoder.class */
    private static class Decoder extends UnicodeDecoder {
        public Decoder(Charset charset) {
            super(charset, 0, 2);
        }
    }

    /* loaded from: rt.jar:sun/nio/cs/UTF_16LE_BOM$Encoder.class */
    private static class Encoder extends UnicodeEncoder {
        public Encoder(Charset charset) {
            super(charset, 1, true);
        }
    }
}
