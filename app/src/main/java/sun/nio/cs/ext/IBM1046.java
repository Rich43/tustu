package sun.nio.cs.ext;

import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import sun.nio.cs.HistoricallyNamedCharset;
import sun.nio.cs.SingleByte;

/* loaded from: charsets.jar:sun/nio/cs/ext/IBM1046.class */
public class IBM1046 extends Charset implements HistoricallyNamedCharset {
    private static final String b2cTable = "ﺈ×÷\uf8f6\uf8f5\uf8f4\uf8f7ﹱ\u0088■│─┐┌└┘ﹹﹻﹽﹿﹷﺊﻰﻳﻲﻎﻏﻐﻶﻸﻺﻼ \uf8fa\uf8f9\uf8f8¤\uf8fbﺋﺑﺗﺛﺟﺣ،\u00adﺧﺳ٠١٢٣٤٥٦٧٨٩ﺷ؛ﺻﺿﻊ؟ﻋﺀﺁﺃﺅﺇﺉﺍﺏﺓﺕﺙﺝﺡﺥﺩﺫﺭﺯﺱﺵﺹﺽﻃﻇﻉﻍﻌﺂﺄﺎﻓـﻑﻕﻙﻝﻡﻥﻫﻭﻯﻱﹰﹲﹴﹶﹸﹺﹼﹾﻗﻛﻟ\uf8fcﻵﻷﻹﻻﻣﻧﻬﻩ���\u0001\u0002\u0003\u0004\u0005\u0006\u0007\b\t\n\u000b\f\r\u000e\u000f\u0010\u0011\u0012\u0013\u0014\u0015\u0016\u0017\u0018\u0019\u001a\u001b\u001c\u001d\u001e\u001f !\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~\u007f";
    private static final char[] b2c = b2cTable.toCharArray();
    private static final char[] c2b = new char[1536];
    private static final char[] c2bIndex = new char[256];

    public IBM1046() {
        super("x-IBM1046", ExtendedCharsets.aliasesFor("x-IBM1046"));
    }

    @Override // sun.nio.cs.HistoricallyNamedCharset
    public String historicalName() {
        return "Cp1046";
    }

    @Override // java.nio.charset.Charset
    public boolean contains(Charset charset) {
        return charset instanceof IBM1046;
    }

    @Override // java.nio.charset.Charset
    public CharsetDecoder newDecoder() {
        return new SingleByte.Decoder(this, b2c);
    }

    @Override // java.nio.charset.Charset
    public CharsetEncoder newEncoder() {
        return new SingleByte.Encoder(this, c2b, c2bIndex);
    }

    static {
        SingleByte.initC2B(b2c, null, c2b, c2bIndex);
    }
}
