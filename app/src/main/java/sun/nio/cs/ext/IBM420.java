package sun.nio.cs.ext;

import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import sun.nio.cs.HistoricallyNamedCharset;
import sun.nio.cs.SingleByte;

/* loaded from: charsets.jar:sun/nio/cs/ext/IBM420.class */
public class IBM420 extends Charset implements HistoricallyNamedCharset {
    private static final String b2cTable = "شabcdefghiﺷصﺻضﺿطظjklmnopqrعﻊﻋﻌغﻎﻏ÷stuvwxyzﻐفﻓقﻗكﻛلﻵﻶﻷﻸ��ﻻﻼﻟمﻣنﻧه؛ABCDEFGHI\u00adﻫ�ﻬ�و؟JKLMNOPQRىﻰيﻲﻳ٠×�STUVWXYZ١٢�٣٤٥0123456789�٦٧٨٩\u009f��\u0001\u0002\u0003\u009c\t\u0086\u007f\u0097\u008d\u008e\u000b\f\r\u000e\u000f\u0010\u0011\u0012\u0013\u009d\n\b\u0087\u0018\u0019\u0092\u008f\u001c\u001d\u001e\u001f\u0080\u0081\u0082\u0083\u0084\n\u0017\u001b\u0088\u0089\u008a\u008b\u008c\u0005\u0006\u0007\u0090\u0091\u0016\u0093\u0094\u0095\u0096\u0004\u0098\u0099\u009a\u009b\u0014\u0015\u009e\u001a  ّﹽـ\u200bءآﺂأ¢.<(+|&ﺄؤ��ئاﺎبﺑ!$*);¬-/ةتﺗثﺛجﺟح¦,%_>?ﺣخﺧدذرزسﺳ،:#@'=\"";
    private static final char[] b2c = b2cTable.toCharArray();
    private static final char[] c2b = new char[1280];
    private static final char[] c2bIndex = new char[256];

    public IBM420() {
        super("IBM420", ExtendedCharsets.aliasesFor("IBM420"));
    }

    @Override // sun.nio.cs.HistoricallyNamedCharset
    public String historicalName() {
        return "Cp420";
    }

    @Override // java.nio.charset.Charset
    public boolean contains(Charset charset) {
        return charset instanceof IBM420;
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
        char[] cArr = b2c;
        char[] charArray = b2cTable.toCharArray();
        charArray[165] = 65533;
        SingleByte.initC2B(charArray, "\u0015\u0085BﹼFﺀGﺁIﺃK٬K．L＜M（N＋O｜P＆RﺅRﺆUﺉUﺊUﺋUﺌVإVﺇVﺍWﺈXﺏXﺐYﺒZ！[＄\\٭\\＊]）^；`－a／bﺓbﺔcﺕcﺖdﺘeﺙeﺚfﺜgﺝgﺞhﺠiﺡiﺢk٫k，l٪l％m＿n＞o？pﺤqﺥqﺦrﺨsﺩsﺪtﺫtﺬuﺭuﺮvﺯvﺰwﺱwﺲxﺴz：{＃|＠}＇~＝\u007f＂\u0080ﺵ\u0080ﺶ\u0081ａ\u0082ｂ\u0083ｃ\u0084ｄ\u0085ｅ\u0086ｆ\u0087ｇ\u0088ｈ\u0089ｉ\u008aﺸ\u008bﺹ\u008bﺺ\u008cﺼ\u008dﺽ\u008dﺾ\u008eﻀ\u008fﻁ\u008fﻂ\u008fﻃ\u008fﻄ\u0090ﻅ\u0090ﻆ\u0090ﻇ\u0090ﻈ\u0091ｊ\u0092ｋ\u0093ｌ\u0094ｍ\u0095ｎ\u0096ｏ\u0097ｐ\u0098ｑ\u0099ｒ\u009aﻉ\u009eﻍ¢ｓ£ｔ¤ｕ¥ｖ¦ｗ§ｘ¨ｙ©ｚ«ﻑ«ﻒ¬ﻔ\u00adﻕ\u00adﻖ®ﻘ¯ﻙ¯ﻚ°ﻜ±ﻝ±ﻞ¸ﻹ¹ﻺºﻠ»ﻡ»ﻢ¼ﻤ½ﻥ½ﻦ¾ﻨ¿ﻩ¿ﻪÁＡÂＢÃＣÄＤÅＥÆＦÇＧÈＨÉＩÏﻭÏﻮÑＪÒＫÓＬÔＭÕＮÖＯ×ＰØＱÙＲÚﻯÜﻱÞﻴâＳãＴäＵåＶæＷçＸèＹéＺð０ñ１ò２ó３ô４õ５ö６÷７ø８ù９".toCharArray(), c2b, c2bIndex);
    }
}
