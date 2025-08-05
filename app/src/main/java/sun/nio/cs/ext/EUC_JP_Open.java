package sun.nio.cs.ext;

import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import sun.nio.cs.HistoricallyNamedCharset;
import sun.nio.cs.ext.DoubleByte;
import sun.nio.cs.ext.EUC_JP;

/* loaded from: charsets.jar:sun/nio/cs/ext/EUC_JP_Open.class */
public class EUC_JP_Open extends Charset implements HistoricallyNamedCharset {
    public EUC_JP_Open() {
        super("x-eucJP-Open", ExtendedCharsets.aliasesFor("x-eucJP-Open"));
    }

    @Override // sun.nio.cs.HistoricallyNamedCharset
    public String historicalName() {
        return "EUC_JP_Solaris";
    }

    @Override // java.nio.charset.Charset
    public boolean contains(Charset charset) {
        return charset.name().equals("US-ASCII") || (charset instanceof JIS_X_0201) || (charset instanceof EUC_JP);
    }

    @Override // java.nio.charset.Charset
    public CharsetDecoder newDecoder() {
        return new Decoder(this);
    }

    @Override // java.nio.charset.Charset
    public CharsetEncoder newEncoder() {
        return new Encoder(this);
    }

    /* loaded from: charsets.jar:sun/nio/cs/ext/EUC_JP_Open$Decoder.class */
    private static class Decoder extends EUC_JP.Decoder {
        private static DoubleByte.Decoder DEC0208_Solaris = (DoubleByte.Decoder) new JIS_X_0208_Solaris().newDecoder();
        private static DoubleByte.Decoder DEC0212_Solaris = (DoubleByte.Decoder) new JIS_X_0212_Solaris().newDecoder();

        private Decoder(Charset charset) {
            super(charset, 0.5f, 1.0f, DEC0201, DEC0208, DEC0212_Solaris);
        }

        @Override // sun.nio.cs.ext.EUC_JP.Decoder
        protected char decodeDouble(int i2, int i3) {
            char cDecodeDouble = super.decodeDouble(i2, i3);
            if (cDecodeDouble == 65533) {
                return DEC0208_Solaris.decodeDouble(i2 - 128, i3 - 128);
            }
            return cDecodeDouble;
        }
    }

    /* loaded from: charsets.jar:sun/nio/cs/ext/EUC_JP_Open$Encoder.class */
    private static class Encoder extends EUC_JP.Encoder {
        private static DoubleByte.Encoder ENC0208_Solaris = (DoubleByte.Encoder) new JIS_X_0208_Solaris().newEncoder();
        private static DoubleByte.Encoder ENC0212_Solaris = (DoubleByte.Encoder) new JIS_X_0212_Solaris().newEncoder();

        private Encoder(Charset charset) {
            super(charset);
        }

        @Override // sun.nio.cs.ext.EUC_JP.Encoder
        protected int encodeDouble(char c2) {
            int iEncodeDouble = super.encodeDouble(c2);
            if (iEncodeDouble != 65533) {
                return iEncodeDouble;
            }
            int iEncodeChar = ENC0208_Solaris.encodeChar(c2);
            if (iEncodeChar == 65533 || iEncodeChar <= 29952) {
                return iEncodeChar == 65533 ? iEncodeChar : iEncodeChar + 32896;
            }
            return 9404544 + ENC0212_Solaris.encodeChar(c2);
        }
    }
}
