package sun.nio.cs.ext;

import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import java.util.Arrays;
import sun.nio.cs.HistoricallyNamedCharset;
import sun.nio.cs.ext.DoubleByte;

/* loaded from: charsets.jar:sun/nio/cs/ext/IBM949C.class */
public class IBM949C extends Charset implements HistoricallyNamedCharset {
    static final char[] b2cSB;
    static final char[] c2b;
    static final char[] c2bIndex;

    public IBM949C() {
        super("x-IBM949C", ExtendedCharsets.aliasesFor("x-IBM949C"));
    }

    @Override // sun.nio.cs.HistoricallyNamedCharset
    public String historicalName() {
        return "Cp949C";
    }

    @Override // java.nio.charset.Charset
    public boolean contains(Charset charset) {
        return charset.name().equals("US-ASCII") || (charset instanceof IBM949C);
    }

    @Override // java.nio.charset.Charset
    public CharsetDecoder newDecoder() {
        return new DoubleByte.Decoder(this, IBM949.b2c, b2cSB, 161, 254);
    }

    @Override // java.nio.charset.Charset
    public CharsetEncoder newEncoder() {
        return new DoubleByte.Encoder(this, c2b, c2bIndex);
    }

    static {
        IBM949.initb2c();
        b2cSB = new char[256];
        for (int i2 = 0; i2 < 128; i2++) {
            b2cSB[i2] = (char) i2;
        }
        for (int i3 = 128; i3 < 256; i3++) {
            b2cSB[i3] = IBM949.b2cSB[i3];
        }
        IBM949.initc2b();
        c2b = Arrays.copyOf(IBM949.c2b, IBM949.c2b.length);
        c2bIndex = Arrays.copyOf(IBM949.c2bIndex, IBM949.c2bIndex.length);
        char c2 = 0;
        while (true) {
            char c3 = c2;
            if (c3 < 128) {
                c2b[c2bIndex[c3 >> '\b'] + (c3 & 255)] = c3;
                c2 = (char) (c3 + 1);
            } else {
                return;
            }
        }
    }
}
