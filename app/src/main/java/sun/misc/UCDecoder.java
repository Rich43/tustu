package sun.misc;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PushbackInputStream;
import sun.text.normalizer.NormalizerImpl;

/* loaded from: rt.jar:sun/misc/UCDecoder.class */
public class UCDecoder extends CharacterDecoder {
    private static final byte[] map_array = {48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 80, 81, 82, 83, 84, 85, 86, 87, 88, 89, 90, 97, 98, 99, 100, 101, 102, 103, 104, 105, 106, 107, 108, 109, 110, 111, 112, 113, 114, 115, 116, 117, 118, 119, 120, 121, 122, 40, 41};
    private int sequence;
    private byte[] tmp = new byte[2];
    private CRC16 crc = new CRC16();
    private ByteArrayOutputStream lineAndSeq = new ByteArrayOutputStream(2);

    @Override // sun.misc.CharacterDecoder
    protected int bytesPerAtom() {
        return 2;
    }

    @Override // sun.misc.CharacterDecoder
    protected int bytesPerLine() {
        return 48;
    }

    @Override // sun.misc.CharacterDecoder
    protected void decodeAtom(PushbackInputStream pushbackInputStream, OutputStream outputStream, int i2) throws IOException {
        byte b2 = -1;
        byte b3 = -1;
        byte b4 = -1;
        byte[] bArr = new byte[3];
        if (pushbackInputStream.read(bArr) != 3) {
            throw new CEStreamExhausted();
        }
        for (int i3 = 0; i3 < 64 && (b2 == -1 || b3 == -1 || b4 == -1); i3++) {
            if (bArr[0] == map_array[i3]) {
                b2 = (byte) i3;
            }
            if (bArr[1] == map_array[i3]) {
                b3 = (byte) i3;
            }
            if (bArr[2] == map_array[i3]) {
                b4 = (byte) i3;
            }
        }
        byte b5 = (byte) (((b2 & 56) << 2) + (b3 & 31));
        byte b6 = (byte) (((b2 & 7) << 5) + (b4 & 31));
        int i4 = 0;
        int i5 = 0;
        int i6 = 1;
        while (true) {
            int i7 = i6;
            if (i7 >= 256) {
                break;
            }
            if ((b5 & i7) != 0) {
                i4++;
            }
            if ((b6 & i7) != 0) {
                i5++;
            }
            i6 = i7 * 2;
        }
        int i8 = (b4 & 32) / 32;
        if ((i4 & 1) != (b3 & 32) / 32) {
            throw new CEFormatException("UCDecoder: High byte parity error.");
        }
        if ((i5 & 1) != i8) {
            throw new CEFormatException("UCDecoder: Low byte parity error.");
        }
        outputStream.write(b5);
        this.crc.update(b5);
        if (i2 == 2) {
            outputStream.write(b6);
            this.crc.update(b6);
        }
    }

    @Override // sun.misc.CharacterDecoder
    protected void decodeBufferPrefix(PushbackInputStream pushbackInputStream, OutputStream outputStream) {
        this.sequence = 0;
    }

    @Override // sun.misc.CharacterDecoder
    protected int decodeLinePrefix(PushbackInputStream pushbackInputStream, OutputStream outputStream) throws IOException {
        this.crc.value = 0;
        while (pushbackInputStream.read(this.tmp, 0, 1) != -1) {
            if (this.tmp[0] == 42) {
                this.lineAndSeq.reset();
                decodeAtom(pushbackInputStream, this.lineAndSeq, 2);
                byte[] byteArray = this.lineAndSeq.toByteArray();
                int i2 = byteArray[0] & 255;
                if ((byteArray[1] & 255) != this.sequence) {
                    throw new CEFormatException("UCDecoder: Out of sequence line.");
                }
                this.sequence = (this.sequence + 1) & 255;
                return i2;
            }
        }
        throw new CEStreamExhausted();
    }

    @Override // sun.misc.CharacterDecoder
    protected void decodeLineSuffix(PushbackInputStream pushbackInputStream, OutputStream outputStream) throws IOException {
        int i2 = this.crc.value;
        this.lineAndSeq.reset();
        decodeAtom(pushbackInputStream, this.lineAndSeq, 2);
        byte[] byteArray = this.lineAndSeq.toByteArray();
        if (((byteArray[0] << 8) & NormalizerImpl.CC_MASK) + (byteArray[1] & 255) != i2) {
            throw new CEFormatException("UCDecoder: CRC check failed.");
        }
    }
}
