package sun.nio.cs.ext;

import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import sun.nio.cs.HistoricallyNamedCharset;
import sun.nio.cs.ext.EUC_JP;

/* loaded from: charsets.jar:sun/nio/cs/ext/EUC_JP_LINUX.class */
public class EUC_JP_LINUX extends Charset implements HistoricallyNamedCharset {
    public EUC_JP_LINUX() {
        super("x-euc-jp-linux", ExtendedCharsets.aliasesFor("x-euc-jp-linux"));
    }

    @Override // sun.nio.cs.HistoricallyNamedCharset
    public String historicalName() {
        return "EUC_JP_LINUX";
    }

    @Override // java.nio.charset.Charset
    public boolean contains(Charset charset) {
        return (charset instanceof JIS_X_0201) || charset.name().equals("US-ASCII") || (charset instanceof EUC_JP_LINUX);
    }

    @Override // java.nio.charset.Charset
    public CharsetDecoder newDecoder() {
        return new Decoder(this);
    }

    @Override // java.nio.charset.Charset
    public CharsetEncoder newEncoder() {
        return new Encoder(this);
    }

    /* loaded from: charsets.jar:sun/nio/cs/ext/EUC_JP_LINUX$Decoder.class */
    private static class Decoder extends EUC_JP.Decoder {
        private Decoder(Charset charset) {
            super(charset, 1.0f, 1.0f, DEC0201, DEC0208, null);
        }
    }

    /* loaded from: charsets.jar:sun/nio/cs/ext/EUC_JP_LINUX$Encoder.class */
    private static class Encoder extends EUC_JP.Encoder {
        private Encoder(Charset charset) {
            super(charset, 2.0f, 2.0f, ENC0201, ENC0208, null);
        }
    }
}
