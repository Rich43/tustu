package sun.nio.cs;

import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;

/* loaded from: rt.jar:sun/nio/cs/UTF_16.class */
class UTF_16 extends Unicode {
    public UTF_16() {
        super("UTF-16", StandardCharsets.aliases_UTF_16);
    }

    @Override // sun.nio.cs.HistoricallyNamedCharset
    public String historicalName() {
        return "UTF-16";
    }

    @Override // java.nio.charset.Charset
    public CharsetDecoder newDecoder() {
        return new Decoder(this);
    }

    @Override // java.nio.charset.Charset
    public CharsetEncoder newEncoder() {
        return new Encoder(this);
    }

    /* loaded from: rt.jar:sun/nio/cs/UTF_16$Decoder.class */
    private static class Decoder extends UnicodeDecoder {
        public Decoder(Charset charset) {
            super(charset, 0);
        }
    }

    /* loaded from: rt.jar:sun/nio/cs/UTF_16$Encoder.class */
    private static class Encoder extends UnicodeEncoder {
        public Encoder(Charset charset) {
            super(charset, 0, true);
        }
    }
}
