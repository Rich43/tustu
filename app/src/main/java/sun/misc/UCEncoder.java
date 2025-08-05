package sun.misc;

import java.io.IOException;
import java.io.OutputStream;

/* loaded from: rt.jar:sun/misc/UCEncoder.class */
public class UCEncoder extends CharacterEncoder {
    private static final byte[] map_array = {48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 80, 81, 82, 83, 84, 85, 86, 87, 88, 89, 90, 97, 98, 99, 100, 101, 102, 103, 104, 105, 106, 107, 108, 109, 110, 111, 112, 113, 114, 115, 116, 117, 118, 119, 120, 121, 122, 40, 41};
    private int sequence;
    private byte[] tmp = new byte[2];
    private CRC16 crc = new CRC16();

    @Override // sun.misc.CharacterEncoder
    protected int bytesPerAtom() {
        return 2;
    }

    @Override // sun.misc.CharacterEncoder
    protected int bytesPerLine() {
        return 48;
    }

    @Override // sun.misc.CharacterEncoder
    protected void encodeAtom(OutputStream outputStream, byte[] bArr, int i2, int i3) throws IOException {
        byte b2;
        byte b3 = bArr[i2];
        if (i3 == 2) {
            b2 = bArr[i2 + 1];
        } else {
            b2 = 0;
        }
        this.crc.update(b3);
        if (i3 == 2) {
            this.crc.update(b2);
        }
        outputStream.write(map_array[((b3 >>> 2) & 56) + ((b2 >>> 5) & 7)]);
        int i4 = 0;
        int i5 = 0;
        int i6 = 1;
        while (true) {
            int i7 = i6;
            if (i7 < 256) {
                if ((b3 & i7) != 0) {
                    i4++;
                }
                if ((b2 & i7) != 0) {
                    i5++;
                }
                i6 = i7 * 2;
            } else {
                outputStream.write(map_array[(b3 & 31) + ((i4 & 1) * 32)]);
                outputStream.write(map_array[(b2 & 31) + ((i5 & 1) * 32)]);
                return;
            }
        }
    }

    @Override // sun.misc.CharacterEncoder
    protected void encodeLinePrefix(OutputStream outputStream, int i2) throws IOException {
        outputStream.write(42);
        this.crc.value = 0;
        this.tmp[0] = (byte) i2;
        this.tmp[1] = (byte) this.sequence;
        this.sequence = (this.sequence + 1) & 255;
        encodeAtom(outputStream, this.tmp, 0, 2);
    }

    @Override // sun.misc.CharacterEncoder
    protected void encodeLineSuffix(OutputStream outputStream) throws IOException {
        this.tmp[0] = (byte) ((this.crc.value >>> 8) & 255);
        this.tmp[1] = (byte) (this.crc.value & 255);
        encodeAtom(outputStream, this.tmp, 0, 2);
        this.pStream.println();
    }

    @Override // sun.misc.CharacterEncoder
    protected void encodeBufferPrefix(OutputStream outputStream) throws IOException {
        this.sequence = 0;
        super.encodeBufferPrefix(outputStream);
    }
}
