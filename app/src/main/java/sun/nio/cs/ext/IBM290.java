package sun.nio.cs.ext;

import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import sun.nio.cs.HistoricallyNamedCharset;
import sun.nio.cs.SingleByte;

/* loaded from: charsets.jar:sun/nio/cs/ext/IBM290.class */
public class IBM290 extends Charset implements HistoricallyNamedCharset {
    private static final String b2cTable = "]ｱｲｳｴｵｶｷｸｹｺqｻｼｽｾｿﾀﾁﾂﾃﾄﾅﾆﾇﾈﾉr�ﾊﾋﾌ~‾ﾍﾎﾏﾐﾑﾒﾓﾔﾕsﾖﾗﾘﾙ^¢\\tuvwxyzﾚﾛﾜﾝﾞﾟ{ABCDEFGHI������}JKLMNOPQR������$�STUVWXYZ������0123456789�����\u009f��\u0001\u0002\u0003\u009c\t\u0086\u007f\u0097\u008d\u008e\u000b\f\r\u000e\u000f\u0010\u0011\u0012\u0013\u009d\u0085\b\u0087\u0018\u0019\u0092\u008f\u001c\u001d\u001e\u001f\u0080\u0081\u0082\u0083\u0084\n\u0017\u001b\u0088\u0089\u008a\u008b\u008c\u0005\u0006\u0007\u0090\u0091\u0016\u0093\u0094\u0095\u0096\u0004\u0098\u0099\u009a\u009b\u0014\u0015\u009e\u001a ｡｢｣､･ｦｧｨｩ£.<(+|&ｪｫｬｭｮｯ�ｰ�!¥*);¬-/abcdefgh�,%_>?[ijklmnop`:#@'=\"";
    private static final char[] b2c = b2cTable.toCharArray();
    private static final char[] c2b = new char[768];
    private static final char[] c2bIndex = new char[256];

    public IBM290() {
        super("IBM290", ExtendedCharsets.aliasesFor("IBM290"));
    }

    @Override // sun.nio.cs.HistoricallyNamedCharset
    public String historicalName() {
        return "Cp290";
    }

    @Override // java.nio.charset.Charset
    public boolean contains(Charset charset) {
        return charset instanceof IBM290;
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
        SingleByte.initC2B(b2c, new char[]{'K', 65294, 'L', 65308, 'M', 65288, 'N', 65291, 'O', 65372, 'P', 65286, 'Z', 65281, '\\', 65290, ']', 65289, '^', 65307, '`', 65293, 'a', 65295, 'b', 65345, 'c', 65346, 'd', 65347, 'e', 65348, 'f', 65349, 'g', 65350, 'h', 65351, 'i', 65352, 'k', 65292, 'l', 65285, 'm', 65343, 'n', 65310, 'o', 65311, 'p', 65339, 'q', 65353, 'r', 65354, 's', 65355, 't', 65356, 'u', 65357, 'v', 65358, 'w', 65359, 'x', 65360, 'y', 65344, 'z', 65306, '{', 65283, '|', 65312, '}', 65287, '~', 65309, 127, 65282, 128, 65341, 139, 65361, 155, 65362, 160, 65374, 171, 65363, 176, 65342, 178, 65340, 179, 65364, 180, 65365, 181, 65366, 182, 65367, 183, 65368, 184, 65369, 185, 65370, 192, 65371, 193, 65313, 194, 65314, 195, 65315, 196, 65316, 197, 65317, 198, 65318, 199, 65319, 200, 65320, 201, 65321, 208, 65373, 209, 65322, 210, 65323, 211, 65324, 212, 65325, 213, 65326, 214, 65327, 215, 65328, 216, 65329, 217, 65330, 224, 65284, 226, 65331, 227, 65332, 228, 65333, 229, 65334, 230, 65335, 231, 65336, 232, 65337, 233, 65338, 240, 65296, 241, 65297, 242, 65298, 243, 65299, 244, 65300, 245, 65301, 246, 65302, 247, 65303, 248, 65304, 249, 65305}, c2b, c2bIndex);
    }
}
