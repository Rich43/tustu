package sun.nio.cs.ext;

import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import sun.nio.cs.HistoricallyNamedCharset;
import sun.nio.cs.SingleByte;

/* loaded from: charsets.jar:sun/nio/cs/ext/MacDingbat.class */
public class MacDingbat extends Charset implements HistoricallyNamedCharset {
    private static final String b2cTable = "���������������������������������❡❢❣❤❥❦❧♣♦♥♠①②③④⑤⑥⑦⑧⑨⑩❶❷❸❹❺❻❼❽❾❿➀➁➂➃➄➅➆➇➈➉➊➋➌➍➎➏➐➑➒➓➔→↔↕➘➙➚➛➜➝➞➟➠➡➢➣➤➥➦➧➨➩➪➫➬➭➮➯�➱➲➳➴➵➶➷➸➹➺➻➼➽➾���\u0001\u0002\u0003\u0004\u0005\u0006\u0007\b\t\n\u000b\f\r\u000e\u000f\u0010\u0011\u0012\u0013\u0014\u0015\u0016\u0017\u0018\u0019\u001a\u001b\u001c\u001d\u001e\u001f ✁✂✃✄☎✆✇✈✉☛☞✌✍✎✏✐✑✒✓✔✕✖✗✘✙✚✛✜✝✞✟✠✡✢✣✤✥✦✧★✩✪✫✬✭✮✯✰✱✲✳✴✵✶✷✸✹✺✻✼✽✾✿❀❁❂❃❄❅❆❇❈❉❊❋●❍■❏❐❑❒▲▼◆❖◗❘❙❚❛❜❝❞\u007f";
    private static final char[] b2c = b2cTable.toCharArray();
    private static final char[] c2b = new char[1792];
    private static final char[] c2bIndex = new char[256];

    public MacDingbat() {
        super("x-MacDingbat", ExtendedCharsets.aliasesFor("x-MacDingbat"));
    }

    @Override // sun.nio.cs.HistoricallyNamedCharset
    public String historicalName() {
        return "MacDingbat";
    }

    @Override // java.nio.charset.Charset
    public boolean contains(Charset charset) {
        return charset instanceof MacDingbat;
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
