package sun.nio.cs.ext;

import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import sun.nio.cs.ext.DoubleByte;
import sun.nio.cs.ext.ISO2022_JP;

/* loaded from: charsets.jar:sun/nio/cs/ext/MS50220.class */
public class MS50220 extends ISO2022_JP {
    private static final DoubleByte.Decoder DEC0208 = (DoubleByte.Decoder) new JIS_X_0208_MS5022X().newDecoder();
    private static final DoubleByte.Decoder DEC0212 = (DoubleByte.Decoder) new JIS_X_0212_MS5022X().newDecoder();
    private static final DoubleByte.Encoder ENC0208 = (DoubleByte.Encoder) new JIS_X_0208_MS5022X().newEncoder();
    private static final DoubleByte.Encoder ENC0212 = (DoubleByte.Encoder) new JIS_X_0212_MS5022X().newEncoder();

    public MS50220() {
        super("x-windows-50220", ExtendedCharsets.aliasesFor("x-windows-50220"));
    }

    protected MS50220(String str, String[] strArr) {
        super(str, strArr);
    }

    @Override // sun.nio.cs.ext.ISO2022_JP, sun.nio.cs.HistoricallyNamedCharset
    public String historicalName() {
        return "MS50220";
    }

    @Override // sun.nio.cs.ext.ISO2022_JP, java.nio.charset.Charset
    public boolean contains(Charset charset) {
        return super.contains(charset) || (charset instanceof JIS_X_0212) || (charset instanceof MS50220);
    }

    @Override // sun.nio.cs.ext.ISO2022_JP, java.nio.charset.Charset
    public CharsetDecoder newDecoder() {
        return new ISO2022_JP.Decoder(this, DEC0208, DEC0212);
    }

    @Override // sun.nio.cs.ext.ISO2022_JP, java.nio.charset.Charset
    public CharsetEncoder newEncoder() {
        return new ISO2022_JP.Encoder(this, ENC0208, ENC0212, doSBKANA());
    }

    @Override // sun.nio.cs.ext.ISO2022_JP
    protected boolean doSBKANA() {
        return false;
    }
}
