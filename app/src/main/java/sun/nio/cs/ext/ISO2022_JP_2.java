package sun.nio.cs.ext;

import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import sun.nio.cs.ext.DoubleByte;
import sun.nio.cs.ext.ISO2022_JP;

/* loaded from: charsets.jar:sun/nio/cs/ext/ISO2022_JP_2.class */
public class ISO2022_JP_2 extends ISO2022_JP {
    public ISO2022_JP_2() {
        super("ISO-2022-JP-2", ExtendedCharsets.aliasesFor("ISO-2022-JP-2"));
    }

    @Override // sun.nio.cs.ext.ISO2022_JP, sun.nio.cs.HistoricallyNamedCharset
    public String historicalName() {
        return "ISO2022JP2";
    }

    @Override // sun.nio.cs.ext.ISO2022_JP, java.nio.charset.Charset
    public boolean contains(Charset charset) {
        return super.contains(charset) || (charset instanceof JIS_X_0212) || (charset instanceof ISO2022_JP_2);
    }

    @Override // sun.nio.cs.ext.ISO2022_JP, java.nio.charset.Charset
    public CharsetDecoder newDecoder() {
        return new ISO2022_JP.Decoder(this, ISO2022_JP.Decoder.DEC0208, CoderHolder.DEC0212);
    }

    @Override // sun.nio.cs.ext.ISO2022_JP, java.nio.charset.Charset
    public CharsetEncoder newEncoder() {
        return new ISO2022_JP.Encoder(this, ISO2022_JP.Encoder.ENC0208, CoderHolder.ENC0212, true);
    }

    /* loaded from: charsets.jar:sun/nio/cs/ext/ISO2022_JP_2$CoderHolder.class */
    private static class CoderHolder {
        static final DoubleByte.Decoder DEC0212 = (DoubleByte.Decoder) new JIS_X_0212().newDecoder();
        static final DoubleByte.Encoder ENC0212 = (DoubleByte.Encoder) new JIS_X_0212().newEncoder();

        private CoderHolder() {
        }
    }
}
