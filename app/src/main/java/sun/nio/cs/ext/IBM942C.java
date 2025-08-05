package sun.nio.cs.ext;

import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import java.util.Arrays;
import sun.nio.cs.HistoricallyNamedCharset;
import sun.nio.cs.ext.DoubleByte;

/* loaded from: charsets.jar:sun/nio/cs/ext/IBM942C.class */
public class IBM942C extends Charset implements HistoricallyNamedCharset {
    static final char[] b2cSB;
    static final char[] c2b;
    static final char[] c2bIndex;

    public IBM942C() {
        super("x-IBM942C", ExtendedCharsets.aliasesFor("x-IBM942C"));
    }

    @Override // sun.nio.cs.HistoricallyNamedCharset
    public String historicalName() {
        return "Cp942C";
    }

    @Override // java.nio.charset.Charset
    public boolean contains(Charset charset) {
        return charset.name().equals("US-ASCII") || (charset instanceof IBM942C);
    }

    @Override // java.nio.charset.Charset
    public CharsetDecoder newDecoder() {
        return new DoubleByte.Decoder(this, IBM942.b2c, b2cSB, 64, 252);
    }

    @Override // java.nio.charset.Charset
    public CharsetEncoder newEncoder() {
        return new DoubleByte.Encoder(this, c2b, c2bIndex);
    }

    static {
        IBM942.initb2c();
        b2cSB = Arrays.copyOf(IBM942.b2cSB, IBM942.b2cSB.length);
        b2cSB[26] = 26;
        b2cSB[28] = 28;
        b2cSB[92] = '\\';
        b2cSB[126] = '~';
        b2cSB[127] = 127;
        IBM942.initc2b();
        c2b = Arrays.copyOf(IBM942.c2b, IBM942.c2b.length);
        c2bIndex = Arrays.copyOf(IBM942.c2bIndex, IBM942.c2bIndex.length);
        c2b[c2bIndex[0] + 26] = 26;
        c2b[c2bIndex[0] + 28] = 28;
        c2b[c2bIndex[0] + '\\'] = '\\';
        c2b[c2bIndex[0] + '~'] = '~';
        c2b[c2bIndex[0] + 127] = 127;
    }
}
