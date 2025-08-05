package sun.nio.cs;

import com.sun.xml.internal.org.jvnet.fastinfoset.FastInfosetSerializer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;

/* loaded from: rt.jar:sun/nio/cs/UTF_16BE.class */
class UTF_16BE extends Unicode {
    public UTF_16BE() {
        super(FastInfosetSerializer.UTF_16BE, StandardCharsets.aliases_UTF_16BE);
    }

    @Override // sun.nio.cs.HistoricallyNamedCharset
    public String historicalName() {
        return "UnicodeBigUnmarked";
    }

    @Override // java.nio.charset.Charset
    public CharsetDecoder newDecoder() {
        return new Decoder(this);
    }

    @Override // java.nio.charset.Charset
    public CharsetEncoder newEncoder() {
        return new Encoder(this);
    }

    /* loaded from: rt.jar:sun/nio/cs/UTF_16BE$Decoder.class */
    private static class Decoder extends UnicodeDecoder {
        public Decoder(Charset charset) {
            super(charset, 1);
        }
    }

    /* loaded from: rt.jar:sun/nio/cs/UTF_16BE$Encoder.class */
    private static class Encoder extends UnicodeEncoder {
        public Encoder(Charset charset) {
            super(charset, 0, false);
        }
    }
}
