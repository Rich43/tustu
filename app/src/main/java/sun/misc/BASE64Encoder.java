package sun.misc;

import java.io.IOException;
import java.io.OutputStream;

/* loaded from: rt.jar:sun/misc/BASE64Encoder.class */
public class BASE64Encoder extends CharacterEncoder {
    private static final char[] pem_array = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '+', '/'};

    @Override // sun.misc.CharacterEncoder
    protected int bytesPerAtom() {
        return 3;
    }

    @Override // sun.misc.CharacterEncoder
    protected int bytesPerLine() {
        return 57;
    }

    @Override // sun.misc.CharacterEncoder
    protected void encodeAtom(OutputStream outputStream, byte[] bArr, int i2, int i3) throws IOException {
        if (i3 == 1) {
            byte b2 = bArr[i2];
            outputStream.write(pem_array[(b2 >>> 2) & 63]);
            outputStream.write(pem_array[((b2 << 4) & 48) + ((0 >>> 4) & 15)]);
            outputStream.write(61);
            outputStream.write(61);
            return;
        }
        if (i3 == 2) {
            byte b3 = bArr[i2];
            byte b4 = bArr[i2 + 1];
            outputStream.write(pem_array[(b3 >>> 2) & 63]);
            outputStream.write(pem_array[((b3 << 4) & 48) + ((b4 >>> 4) & 15)]);
            outputStream.write(pem_array[((b4 << 2) & 60) + ((0 >>> 6) & 3)]);
            outputStream.write(61);
            return;
        }
        byte b5 = bArr[i2];
        byte b6 = bArr[i2 + 1];
        byte b7 = bArr[i2 + 2];
        outputStream.write(pem_array[(b5 >>> 2) & 63]);
        outputStream.write(pem_array[((b5 << 4) & 48) + ((b6 >>> 4) & 15)]);
        outputStream.write(pem_array[((b6 << 2) & 60) + ((b7 >>> 6) & 3)]);
        outputStream.write(pem_array[b7 & 63]);
    }
}
