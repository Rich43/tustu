package sun.nio.cs;

import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;

/* loaded from: rt.jar:sun/nio/cs/UTF_16LE.class */
class UTF_16LE extends Unicode {
    public UTF_16LE() {
        super("UTF-16LE", StandardCharsets.aliases_UTF_16LE);
    }

    @Override // sun.nio.cs.HistoricallyNamedCharset
    public String historicalName() {
        return "UnicodeLittleUnmarked";
    }

    @Override // java.nio.charset.Charset
    public CharsetDecoder newDecoder() {
        return new Decoder(this);
    }

    @Override // java.nio.charset.Charset
    public CharsetEncoder newEncoder() {
        return new Encoder(this);
    }

    /* loaded from: rt.jar:sun/nio/cs/UTF_16LE$Decoder.class */
    private static class Decoder extends UnicodeDecoder {
        public Decoder(Charset charset) {
            super(charset, 2);
        }
    }

    /* loaded from: rt.jar:sun/nio/cs/UTF_16LE$Encoder.class */
    private static class Encoder extends UnicodeEncoder {
        public Encoder(Charset charset) {
            super(charset, 1, false);
        }
    }
}
