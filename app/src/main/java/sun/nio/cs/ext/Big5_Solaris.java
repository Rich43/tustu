package sun.nio.cs.ext;

import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import java.util.Arrays;
import sun.nio.cs.HistoricallyNamedCharset;
import sun.nio.cs.ext.DoubleByte;

/* loaded from: charsets.jar:sun/nio/cs/ext/Big5_Solaris.class */
public class Big5_Solaris extends Charset implements HistoricallyNamedCharset {
    static char[][] b2c;
    static char[] b2cSB;
    static char[] c2b;
    static char[] c2bIndex;
    private static volatile boolean b2cInitialized = false;
    private static volatile boolean c2bInitialized = false;

    public Big5_Solaris() {
        super("x-Big5-Solaris", ExtendedCharsets.aliasesFor("x-Big5-Solaris"));
    }

    @Override // sun.nio.cs.HistoricallyNamedCharset
    public String historicalName() {
        return "Big5_Solaris";
    }

    @Override // java.nio.charset.Charset
    public boolean contains(Charset charset) {
        return charset.name().equals("US-ASCII") || (charset instanceof Big5) || (charset instanceof Big5_Solaris);
    }

    @Override // java.nio.charset.Charset
    public CharsetDecoder newDecoder() {
        initb2c();
        return new DoubleByte.Decoder(this, b2c, b2cSB, 64, 254);
    }

    @Override // java.nio.charset.Charset
    public CharsetEncoder newEncoder() {
        initc2b();
        return new DoubleByte.Encoder(this, c2b, c2bIndex);
    }

    static void initb2c() {
        if (b2cInitialized) {
            return;
        }
        synchronized (Big5_Solaris.class) {
            if (b2cInitialized) {
                return;
            }
            Big5.initb2c();
            b2c = (char[][]) Big5.b2c.clone();
            int[] iArr = {63958, 30849, 63959, 37561, 63960, 35023, 63961, 22715, 63962, 24658, 63963, 31911, 63964, 23290};
            if (b2c[249] == DoubleByte.B2C_UNMAPPABLE) {
                b2c[249] = new char[191];
                Arrays.fill(b2c[249], (char) 65533);
            }
            int i2 = 0;
            while (i2 < iArr.length) {
                int i3 = i2;
                int i4 = i2 + 1;
                i2 = i4 + 1;
                b2c[249][iArr[i3] & 191] = (char) iArr[i4];
            }
            b2cSB = Big5.b2cSB;
            b2cInitialized = true;
        }
    }

    static void initc2b() {
        if (c2bInitialized) {
            return;
        }
        synchronized (Big5_Solaris.class) {
            if (c2bInitialized) {
                return;
            }
            Big5.initc2b();
            c2b = (char[]) Big5.c2b.clone();
            c2bIndex = (char[]) Big5.c2bIndex.clone();
            int[] iArr = {30849, 63958, 37561, 63959, 35023, 63960, 22715, 63961, 24658, 63962, 31911, 63963, 23290, 63964};
            int i2 = 0;
            while (i2 < iArr.length) {
                int i3 = i2;
                int i4 = i2 + 1;
                int i5 = iArr[i3];
                i2 = i4 + 1;
                c2b[c2bIndex[i5 >> 8] + (i5 & 255)] = (char) iArr[i4];
            }
            c2bInitialized = true;
        }
    }
}
