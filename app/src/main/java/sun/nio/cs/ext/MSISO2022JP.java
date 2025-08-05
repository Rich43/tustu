package sun.nio.cs.ext;

import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import sun.nio.cs.ext.DoubleByte;
import sun.nio.cs.ext.ISO2022_JP;

/* loaded from: charsets.jar:sun/nio/cs/ext/MSISO2022JP.class */
public class MSISO2022JP extends ISO2022_JP {
    public MSISO2022JP() {
        super("x-windows-iso2022jp", ExtendedCharsets.aliasesFor("x-windows-iso2022jp"));
    }

    @Override // sun.nio.cs.ext.ISO2022_JP, sun.nio.cs.HistoricallyNamedCharset
    public String historicalName() {
        return "windows-iso2022jp";
    }

    @Override // sun.nio.cs.ext.ISO2022_JP, java.nio.charset.Charset
    public boolean contains(Charset charset) {
        return super.contains(charset) || (charset instanceof MSISO2022JP);
    }

    @Override // sun.nio.cs.ext.ISO2022_JP, java.nio.charset.Charset
    public CharsetDecoder newDecoder() {
        return new ISO2022_JP.Decoder(this, CoderHolder.DEC0208, null);
    }

    @Override // sun.nio.cs.ext.ISO2022_JP, java.nio.charset.Charset
    public CharsetEncoder newEncoder() {
        return new ISO2022_JP.Encoder(this, CoderHolder.ENC0208, null, true);
    }

    /* loaded from: charsets.jar:sun/nio/cs/ext/MSISO2022JP$CoderHolder.class */
    private static class CoderHolder {
        static final DoubleByte.Decoder DEC0208 = (DoubleByte.Decoder) new JIS_X_0208_MS932().newDecoder();
        static final DoubleByte.Encoder ENC0208 = (DoubleByte.Encoder) new JIS_X_0208_MS932().newEncoder();

        private CoderHolder() {
        }
    }
}
