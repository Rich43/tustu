package sun.nio.cs.ext;

import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import sun.nio.cs.HistoricallyNamedCharset;
import sun.nio.cs.ext.ISO2022;

/* loaded from: charsets.jar:sun/nio/cs/ext/ISO2022_KR.class */
public class ISO2022_KR extends ISO2022 implements HistoricallyNamedCharset {
    private static Charset ksc5601_cs;

    public ISO2022_KR() {
        super("ISO-2022-KR", ExtendedCharsets.aliasesFor("ISO-2022-KR"));
        ksc5601_cs = new EUC_KR();
    }

    @Override // java.nio.charset.Charset
    public boolean contains(Charset charset) {
        return (charset instanceof EUC_KR) || charset.name().equals("US-ASCII") || (charset instanceof ISO2022_KR);
    }

    @Override // sun.nio.cs.HistoricallyNamedCharset
    public String historicalName() {
        return "ISO2022KR";
    }

    @Override // sun.nio.cs.ext.ISO2022, java.nio.charset.Charset
    public CharsetDecoder newDecoder() {
        return new Decoder(this);
    }

    @Override // sun.nio.cs.ext.ISO2022, java.nio.charset.Charset
    public CharsetEncoder newEncoder() {
        return new Encoder(this);
    }

    /* loaded from: charsets.jar:sun/nio/cs/ext/ISO2022_KR$Decoder.class */
    private static class Decoder extends ISO2022.Decoder {
        /* JADX WARN: Type inference failed for: r1v2, types: [byte[], byte[][]] */
        public Decoder(Charset charset) {
            super(charset);
            this.SODesig = new byte[]{new byte[]{36, 41, 67}};
            this.SODecoder = new CharsetDecoder[1];
            try {
                this.SODecoder[0] = ISO2022_KR.ksc5601_cs.newDecoder();
            } catch (Exception e2) {
            }
        }
    }

    /* loaded from: charsets.jar:sun/nio/cs/ext/ISO2022_KR$Encoder.class */
    private static class Encoder extends ISO2022.Encoder {
        public Encoder(Charset charset) {
            super(charset);
            this.SODesig = "$)C";
            try {
                this.ISOEncoder = ISO2022_KR.ksc5601_cs.newEncoder();
            } catch (Exception e2) {
            }
        }

        @Override // sun.nio.cs.ext.ISO2022.Encoder, java.nio.charset.CharsetEncoder
        public boolean canEncode(char c2) {
            return this.ISOEncoder.canEncode(c2);
        }
    }
}
