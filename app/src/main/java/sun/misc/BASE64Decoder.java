package sun.misc;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PushbackInputStream;

/* loaded from: rt.jar:sun/misc/BASE64Decoder.class */
public class BASE64Decoder extends CharacterDecoder {
    private static final char[] pem_array = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '+', '/'};
    private static final byte[] pem_convert_array = new byte[256];
    byte[] decode_buffer = new byte[4];

    @Override // sun.misc.CharacterDecoder
    protected int bytesPerAtom() {
        return 4;
    }

    @Override // sun.misc.CharacterDecoder
    protected int bytesPerLine() {
        return 72;
    }

    static {
        for (int i2 = 0; i2 < 255; i2++) {
            pem_convert_array[i2] = -1;
        }
        for (int i3 = 0; i3 < pem_array.length; i3++) {
            pem_convert_array[pem_array[i3]] = (byte) i3;
        }
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    @Override // sun.misc.CharacterDecoder
    protected void decodeAtom(PushbackInputStream pushbackInputStream, OutputStream outputStream, int i2) throws IOException {
        byte b2 = -1;
        byte b3 = -1;
        byte b4 = -1;
        byte b5 = -1;
        if (i2 < 2) {
            throw new CEFormatException("BASE64Decoder: Not enough bytes for an atom.");
        }
        while (true) {
            int i3 = pushbackInputStream.read();
            if (i3 == -1) {
                throw new CEStreamExhausted();
            }
            if (i3 != 10 && i3 != 13) {
                this.decode_buffer[0] = (byte) i3;
                if (readFully(pushbackInputStream, this.decode_buffer, 1, i2 - 1) == -1) {
                    throw new CEStreamExhausted();
                }
                if (i2 > 3 && this.decode_buffer[3] == 61) {
                    i2 = 3;
                }
                if (i2 > 2 && this.decode_buffer[2] == 61) {
                    i2 = 2;
                }
                switch (i2) {
                    case 2:
                        b3 = pem_convert_array[this.decode_buffer[1] & 255];
                        b2 = pem_convert_array[this.decode_buffer[0] & 255];
                        break;
                    case 3:
                        b4 = pem_convert_array[this.decode_buffer[2] & 255];
                        b3 = pem_convert_array[this.decode_buffer[1] & 255];
                        b2 = pem_convert_array[this.decode_buffer[0] & 255];
                        break;
                    case 4:
                        b5 = pem_convert_array[this.decode_buffer[3] & 255];
                        b4 = pem_convert_array[this.decode_buffer[2] & 255];
                        b3 = pem_convert_array[this.decode_buffer[1] & 255];
                        b2 = pem_convert_array[this.decode_buffer[0] & 255];
                        break;
                }
                switch (i2) {
                    case 2:
                        outputStream.write((byte) (((b2 << 2) & 252) | ((b3 >>> 4) & 3)));
                        return;
                    case 3:
                        outputStream.write((byte) (((b2 << 2) & 252) | ((b3 >>> 4) & 3)));
                        outputStream.write((byte) (((b3 << 4) & 240) | ((b4 >>> 2) & 15)));
                        return;
                    case 4:
                        outputStream.write((byte) (((b2 << 2) & 252) | ((b3 >>> 4) & 3)));
                        outputStream.write((byte) (((b3 << 4) & 240) | ((b4 >>> 2) & 15)));
                        outputStream.write((byte) (((b4 << 6) & 192) | (b5 & 63)));
                        return;
                    default:
                        return;
                }
            }
        }
    }
}
