package sun.misc;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PushbackInputStream;

/* loaded from: rt.jar:sun/misc/UUDecoder.class */
public class UUDecoder extends CharacterDecoder {
    public String bufferName;
    public int mode;
    private byte[] decoderBuffer = new byte[4];

    @Override // sun.misc.CharacterDecoder
    protected int bytesPerAtom() {
        return 3;
    }

    @Override // sun.misc.CharacterDecoder
    protected int bytesPerLine() {
        return 45;
    }

    @Override // sun.misc.CharacterDecoder
    protected void decodeAtom(PushbackInputStream pushbackInputStream, OutputStream outputStream, int i2) throws IOException {
        StringBuffer stringBuffer = new StringBuffer();
        for (int i3 = 0; i3 < 4; i3++) {
            int i4 = pushbackInputStream.read();
            if (i4 == -1) {
                throw new CEStreamExhausted();
            }
            stringBuffer.append((char) i4);
            this.decoderBuffer[i3] = (byte) ((i4 - 32) & 63);
        }
        int i5 = ((this.decoderBuffer[0] << 2) & 252) | ((this.decoderBuffer[1] >>> 4) & 3);
        int i6 = ((this.decoderBuffer[1] << 4) & 240) | ((this.decoderBuffer[2] >>> 2) & 15);
        int i7 = ((this.decoderBuffer[2] << 6) & 192) | (this.decoderBuffer[3] & 63);
        outputStream.write((byte) (i5 & 255));
        if (i2 > 1) {
            outputStream.write((byte) (i6 & 255));
        }
        if (i2 > 2) {
            outputStream.write((byte) (i7 & 255));
        }
    }

    @Override // sun.misc.CharacterDecoder
    protected void decodeBufferPrefix(PushbackInputStream pushbackInputStream, OutputStream outputStream) throws IOException {
        int i2;
        StringBuffer stringBuffer = new StringBuffer(32);
        boolean z2 = true;
        while (true) {
            boolean z3 = z2;
            int i3 = pushbackInputStream.read();
            if (i3 == -1) {
                throw new CEFormatException("UUDecoder: No begin line.");
            }
            if (i3 == 98 && z3) {
                i3 = pushbackInputStream.read();
                if (i3 == 101) {
                    while (i3 != 10 && i3 != 13) {
                        i3 = pushbackInputStream.read();
                        if (i3 == -1) {
                            throw new CEFormatException("UUDecoder: No begin line.");
                        }
                        if (i3 != 10 && i3 != 13) {
                            stringBuffer.append((char) i3);
                        }
                    }
                    String string = stringBuffer.toString();
                    if (string.indexOf(32) != 3) {
                        throw new CEFormatException("UUDecoder: Malformed begin line.");
                    }
                    this.mode = Integer.parseInt(string.substring(4, 7));
                    this.bufferName = string.substring(string.indexOf(32, 6) + 1);
                    if (i3 == 13 && (i2 = pushbackInputStream.read()) != 10 && i2 != -1) {
                        pushbackInputStream.unread(i2);
                        return;
                    }
                    return;
                }
            }
            z2 = i3 == 10 || i3 == 13;
        }
    }

    @Override // sun.misc.CharacterDecoder
    protected int decodeLinePrefix(PushbackInputStream pushbackInputStream, OutputStream outputStream) throws IOException {
        int i2 = pushbackInputStream.read();
        if (i2 == 32) {
            pushbackInputStream.read();
            int i3 = pushbackInputStream.read();
            if (i3 != 10 && i3 != -1) {
                pushbackInputStream.unread(i3);
            }
            throw new CEStreamExhausted();
        }
        if (i2 == -1) {
            throw new CEFormatException("UUDecoder: Short Buffer.");
        }
        int i4 = (i2 - 32) & 63;
        if (i4 > bytesPerLine()) {
            throw new CEFormatException("UUDecoder: Bad Line Length.");
        }
        return i4;
    }

    @Override // sun.misc.CharacterDecoder
    protected void decodeLineSuffix(PushbackInputStream pushbackInputStream, OutputStream outputStream) throws IOException {
        int i2;
        do {
            i2 = pushbackInputStream.read();
            if (i2 == -1) {
                throw new CEStreamExhausted();
            }
            if (i2 == 10) {
                return;
            }
        } while (i2 != 13);
        int i3 = pushbackInputStream.read();
        if (i3 != 10 && i3 != -1) {
            pushbackInputStream.unread(i3);
        }
    }

    @Override // sun.misc.CharacterDecoder
    protected void decodeBufferSuffix(PushbackInputStream pushbackInputStream, OutputStream outputStream) throws IOException {
        pushbackInputStream.read(this.decoderBuffer);
        if (this.decoderBuffer[0] != 101 || this.decoderBuffer[1] != 110 || this.decoderBuffer[2] != 100) {
            throw new CEFormatException("UUDecoder: Missing 'end' line.");
        }
    }
}
