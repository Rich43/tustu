package sun.nio.cs;

import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import sun.nio.cs.UTF_32Coder;

/* loaded from: rt.jar:sun/nio/cs/UTF_32.class */
public class UTF_32 extends Unicode {
    @Override // sun.nio.cs.Unicode, java.nio.charset.Charset
    public /* bridge */ /* synthetic */ boolean contains(Charset charset) {
        return super.contains(charset);
    }

    public UTF_32() {
        super("UTF-32", StandardCharsets.aliases_UTF_32);
    }

    @Override // sun.nio.cs.HistoricallyNamedCharset
    public String historicalName() {
        return "UTF-32";
    }

    @Override // java.nio.charset.Charset
    public CharsetDecoder newDecoder() {
        return new UTF_32Coder.Decoder(this, 0);
    }

    @Override // java.nio.charset.Charset
    public CharsetEncoder newEncoder() {
        return new UTF_32Coder.Encoder(this, 1, false);
    }
}
