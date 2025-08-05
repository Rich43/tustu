package sun.nio.cs.ext;

import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import sun.nio.cs.HistoricallyNamedCharset;
import sun.nio.cs.ext.EUC_TW;
import sun.nio.cs.ext.ISO2022;
import sun.nio.cs.ext.ISO2022_CN;

/* loaded from: charsets.jar:sun/nio/cs/ext/ISO2022_CN_CNS.class */
public class ISO2022_CN_CNS extends ISO2022 implements HistoricallyNamedCharset {
    public ISO2022_CN_CNS() {
        super("x-ISO-2022-CN-CNS", ExtendedCharsets.aliasesFor("x-ISO-2022-CN-CNS"));
    }

    @Override // java.nio.charset.Charset
    public boolean contains(Charset charset) {
        return (charset instanceof EUC_TW) || charset.name().equals("US-ASCII") || (charset instanceof ISO2022_CN_CNS);
    }

    @Override // sun.nio.cs.HistoricallyNamedCharset
    public String historicalName() {
        return "ISO2022CN_CNS";
    }

    @Override // sun.nio.cs.ext.ISO2022, java.nio.charset.Charset
    public CharsetDecoder newDecoder() {
        return new ISO2022_CN.Decoder(this);
    }

    @Override // sun.nio.cs.ext.ISO2022, java.nio.charset.Charset
    public CharsetEncoder newEncoder() {
        return new Encoder(this);
    }

    /* loaded from: charsets.jar:sun/nio/cs/ext/ISO2022_CN_CNS$Encoder.class */
    private static class Encoder extends ISO2022.Encoder {

        /* renamed from: bb, reason: collision with root package name */
        private byte[] f13594bb;

        public Encoder(Charset charset) {
            super(charset);
            this.f13594bb = new byte[4];
            this.SODesig = "$)G";
            this.SS2Desig = "$*H";
            this.SS3Desig = "$+I";
            try {
                this.ISOEncoder = Charset.forName("EUC_TW").newEncoder();
            } catch (Exception e2) {
            }
        }

        @Override // sun.nio.cs.ext.ISO2022.Encoder, java.nio.charset.CharsetEncoder
        public boolean canEncode(char c2) {
            int euc;
            return c2 <= 127 || (euc = ((EUC_TW.Encoder) this.ISOEncoder).toEUC(c2, this.f13594bb)) == 2 || (euc == 4 && this.f13594bb[0] == -114 && (this.f13594bb[1] == -94 || this.f13594bb[1] == -93));
        }

        @Override // java.nio.charset.CharsetEncoder
        public boolean isLegalReplacement(byte[] bArr) {
            return bArr.length == 1 && bArr[0] == 63;
        }
    }
}
