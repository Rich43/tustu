package sun.nio.cs.ext;

import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import sun.nio.cs.HistoricallyNamedCharset;
import sun.nio.cs.SingleByte;

/* loaded from: charsets.jar:sun/nio/cs/ext/IBM833.class */
public class IBM833 extends Charset implements HistoricallyNamedCharset {
    private static final String b2cTable = "]abcdefghiￂￃￄￅￆￇ�jklmnopqrￊￋￌￍￎￏ‾~stuvwxyzￒￓￔￕￖￗ^�\\�������ￚￛￜ���{ABCDEFGHI������}JKLMNOPQR������₩�STUVWXYZ������0123456789�����\u009f��\u0001\u0002\u0003\u009c\t\u0086\u007f\u0097\u008d\u008e\u000b\f\r\u000e\u000f\u0010\u0011\u0012\u0013\u009d\u0085\b\u0087\u0018\u0019\u0092\u008f\u001c\u001d\u001e\u001f\u0080\u0081\u0082\u0083\u0084\n\u0017\u001b\u0088\u0089\u008a\u008b\u008c\u0005\u0006\u0007\u0090\u0091\u0016\u0093\u0094\u0095\u0096\u0004\u0098\u0099\u009a\u009b\u0014\u0015\u009e\u001a �ﾠﾡﾢﾣﾤﾥﾦﾧ¢.<(+|&�ﾨﾩﾪﾫﾬﾭﾮﾯ!$*);¬-/ﾰﾱﾲﾳﾴﾵﾶﾷ¦,%_>?[�ﾸﾹﾺﾻﾼﾽﾾ`:#@'=\"";
    private static final char[] b2c = b2cTable.toCharArray();
    private static final char[] c2b = new char[768];
    private static final char[] c2bIndex = new char[256];

    public IBM833() {
        super("x-IBM833", ExtendedCharsets.aliasesFor("x-IBM833"));
    }

    @Override // sun.nio.cs.HistoricallyNamedCharset
    public String historicalName() {
        return "Cp833";
    }

    @Override // java.nio.charset.Charset
    public boolean contains(Charset charset) {
        return charset instanceof IBM833;
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
        SingleByte.initC2B(b2c, new char[]{'Z', 65281, 127, 65282, '{', 65283, '[', 65284, 'l', 65285, 'P', 65286, '}', 65287, 'M', 65288, ']', 65289, '\\', 65290, 'N', 65291, 'k', 65292, '`', 65293, 'K', 65294, 'a', 65295, 240, 65296, 241, 65297, 242, 65298, 243, 65299, 244, 65300, 245, 65301, 246, 65302, 247, 65303, 248, 65304, 249, 65305, 'z', 65306, '^', 65307, 'L', 65308, '~', 65309, 'n', 65310, 'o', 65311, '|', 65312, 193, 65313, 194, 65314, 195, 65315, 196, 65316, 197, 65317, 198, 65318, 199, 65319, 200, 65320, 201, 65321, 209, 65322, 210, 65323, 211, 65324, 212, 65325, 213, 65326, 214, 65327, 215, 65328, 216, 65329, 217, 65330, 226, 65331, 227, 65332, 228, 65333, 229, 65334, 230, 65335, 231, 65336, 232, 65337, 233, 65338, 'p', 65339, 178, 65340, 128, 65341, 176, 65342, 'm', 65343, 'y', 65344, 129, 65345, 130, 65346, 131, 65347, 132, 65348, 133, 65349, 134, 65350, 135, 65351, 136, 65352, 137, 65353, 145, 65354, 146, 65355, 147, 65356, 148, 65357, 149, 65358, 150, 65359, 151, 65360, 152, 65361, 153, 65362, 162, 65363, 163, 65364, 164, 65365, 165, 65366, 166, 65367, 167, 65368, 168, 65369, 169, 65370, 192, 65371, 'O', 65372, 208, 65373, 161, 65374}, c2b, c2bIndex);
    }
}
