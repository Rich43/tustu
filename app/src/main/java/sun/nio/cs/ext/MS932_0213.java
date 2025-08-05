package sun.nio.cs.ext;

import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import sun.nio.cs.ext.DoubleByte;
import sun.nio.cs.ext.SJIS_0213;

/* loaded from: charsets.jar:sun/nio/cs/ext/MS932_0213.class */
public class MS932_0213 extends Charset {
    public MS932_0213() {
        super("x-MS932_0213", ExtendedCharsets.aliasesFor("MS932_0213"));
    }

    @Override // java.nio.charset.Charset
    public boolean contains(Charset charset) {
        return charset.name().equals("US-ASCII") || (charset instanceof MS932) || (charset instanceof MS932_0213);
    }

    @Override // java.nio.charset.Charset
    public CharsetDecoder newDecoder() {
        return new Decoder(this);
    }

    @Override // java.nio.charset.Charset
    public CharsetEncoder newEncoder() {
        return new Encoder(this);
    }

    /* loaded from: charsets.jar:sun/nio/cs/ext/MS932_0213$Decoder.class */
    protected static class Decoder extends SJIS_0213.Decoder {
        static DoubleByte.Decoder decMS932 = (DoubleByte.Decoder) new MS932().newDecoder();

        protected Decoder(Charset charset) {
            super(charset);
        }

        @Override // sun.nio.cs.ext.SJIS_0213.Decoder
        protected char decodeDouble(int i2, int i3) {
            char cDecodeDouble = decMS932.decodeDouble(i2, i3);
            if (cDecodeDouble == 65533) {
                return super.decodeDouble(i2, i3);
            }
            return cDecodeDouble;
        }
    }

    /* loaded from: charsets.jar:sun/nio/cs/ext/MS932_0213$Encoder.class */
    protected static class Encoder extends SJIS_0213.Encoder {
        static DoubleByte.Encoder encMS932 = (DoubleByte.Encoder) new MS932().newEncoder();

        protected Encoder(Charset charset) {
            super(charset);
        }

        @Override // sun.nio.cs.ext.SJIS_0213.Encoder
        protected int encodeChar(char c2) {
            int iEncodeChar = encMS932.encodeChar(c2);
            if (iEncodeChar == 65533) {
                return super.encodeChar(c2);
            }
            return iEncodeChar;
        }
    }
}
