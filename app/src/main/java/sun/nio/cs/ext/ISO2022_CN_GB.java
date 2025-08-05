package sun.nio.cs.ext;

import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import sun.nio.cs.HistoricallyNamedCharset;
import sun.nio.cs.ext.ISO2022;
import sun.nio.cs.ext.ISO2022_CN;

/* loaded from: charsets.jar:sun/nio/cs/ext/ISO2022_CN_GB.class */
public class ISO2022_CN_GB extends ISO2022 implements HistoricallyNamedCharset {
    public ISO2022_CN_GB() {
        super("x-ISO-2022-CN-GB", ExtendedCharsets.aliasesFor("x-ISO-2022-CN-GB"));
    }

    @Override // java.nio.charset.Charset
    public boolean contains(Charset charset) {
        return (charset instanceof EUC_CN) || charset.name().equals("US-ASCII") || (charset instanceof ISO2022_CN_GB);
    }

    @Override // sun.nio.cs.HistoricallyNamedCharset
    public String historicalName() {
        return "ISO2022CN_GB";
    }

    @Override // sun.nio.cs.ext.ISO2022, java.nio.charset.Charset
    public CharsetDecoder newDecoder() {
        return new ISO2022_CN.Decoder(this);
    }

    @Override // sun.nio.cs.ext.ISO2022, java.nio.charset.Charset
    public CharsetEncoder newEncoder() {
        return new Encoder(this);
    }

    /* loaded from: charsets.jar:sun/nio/cs/ext/ISO2022_CN_GB$Encoder.class */
    private static class Encoder extends ISO2022.Encoder {
        public Encoder(Charset charset) {
            super(charset);
            this.SODesig = "$)A";
            try {
                this.ISOEncoder = Charset.forName("EUC_CN").newEncoder();
            } catch (Exception e2) {
            }
        }

        @Override // java.nio.charset.CharsetEncoder
        public boolean isLegalReplacement(byte[] bArr) {
            return bArr.length == 1 && bArr[0] == 63;
        }
    }
}
