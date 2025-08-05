package sun.nio.cs;

import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import sun.nio.cs.UTF_32Coder;

/* loaded from: rt.jar:sun/nio/cs/UTF_32LE_BOM.class */
public class UTF_32LE_BOM extends Unicode {
    @Override // sun.nio.cs.Unicode, java.nio.charset.Charset
    public /* bridge */ /* synthetic */ boolean contains(Charset charset) {
        return super.contains(charset);
    }

    public UTF_32LE_BOM() {
        super("X-UTF-32LE-BOM", StandardCharsets.aliases_UTF_32LE_BOM);
    }

    @Override // sun.nio.cs.HistoricallyNamedCharset
    public String historicalName() {
        return "X-UTF-32LE-BOM";
    }

    @Override // java.nio.charset.Charset
    public CharsetDecoder newDecoder() {
        return new UTF_32Coder.Decoder(this, 2);
    }

    @Override // java.nio.charset.Charset
    public CharsetEncoder newEncoder() {
        return new UTF_32Coder.Encoder(this, 2, true);
    }
}
