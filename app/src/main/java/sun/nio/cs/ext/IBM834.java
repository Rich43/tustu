package sun.nio.cs.ext;

import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import sun.nio.cs.ext.DoubleByte;

/* loaded from: charsets.jar:sun/nio/cs/ext/IBM834.class */
public class IBM834 extends Charset {
    public IBM834() {
        super("x-IBM834", ExtendedCharsets.aliasesFor("x-IBM834"));
    }

    @Override // java.nio.charset.Charset
    public boolean contains(Charset charset) {
        return charset instanceof IBM834;
    }

    @Override // java.nio.charset.Charset
    public CharsetDecoder newDecoder() {
        IBM933.initb2c();
        return new DoubleByte.Decoder_DBCSONLY(this, IBM933.b2c, null, 64, 254);
    }

    @Override // java.nio.charset.Charset
    public CharsetEncoder newEncoder() {
        IBM933.initc2b();
        return new Encoder(this);
    }

    /* loaded from: charsets.jar:sun/nio/cs/ext/IBM834$Encoder.class */
    protected static class Encoder extends DoubleByte.Encoder_DBCSONLY {
        public Encoder(Charset charset) {
            super(charset, new byte[]{-2, -2}, IBM933.c2b, IBM933.c2bIndex);
        }

        @Override // sun.nio.cs.ext.DoubleByte.Encoder_DBCSONLY, sun.nio.cs.ext.DoubleByte.Encoder
        public int encodeChar(char c2) {
            int iEncodeChar = super.encodeChar(c2);
            if (iEncodeChar == 65533) {
                if (c2 == 183) {
                    return 16707;
                }
                if (c2 == 173) {
                    return 16712;
                }
                if (c2 == 8213) {
                    return 16713;
                }
                if (c2 == 8764) {
                    return 17057;
                }
                if (c2 == 65374) {
                    return 18772;
                }
                if (c2 == 8857) {
                    return 18799;
                }
            }
            return iEncodeChar;
        }

        @Override // java.nio.charset.CharsetEncoder
        public boolean isLegalReplacement(byte[] bArr) {
            if (bArr.length == 2 && bArr[0] == -2 && bArr[1] == -2) {
                return true;
            }
            return super.isLegalReplacement(bArr);
        }
    }
}
