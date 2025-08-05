package sun.misc;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import sun.security.pkcs11.wrapper.Constants;

/* loaded from: rt.jar:sun/misc/HexDumpEncoder.class */
public class HexDumpEncoder extends CharacterEncoder {
    private int offset;
    private int thisLineLength;
    private int currentByte;
    private byte[] thisLine = new byte[16];

    static void hexDigit(PrintStream printStream, byte b2) {
        char c2;
        char c3;
        char c4 = (char) ((b2 >> 4) & 15);
        if (c4 > '\t') {
            c2 = (char) ((c4 - '\n') + 65);
        } else {
            c2 = (char) (c4 + '0');
        }
        printStream.write(c2);
        char c5 = (char) (b2 & 15);
        if (c5 > '\t') {
            c3 = (char) ((c5 - '\n') + 65);
        } else {
            c3 = (char) (c5 + '0');
        }
        printStream.write(c3);
    }

    @Override // sun.misc.CharacterEncoder
    protected int bytesPerAtom() {
        return 1;
    }

    @Override // sun.misc.CharacterEncoder
    protected int bytesPerLine() {
        return 16;
    }

    @Override // sun.misc.CharacterEncoder
    protected void encodeBufferPrefix(OutputStream outputStream) throws IOException {
        this.offset = 0;
        super.encodeBufferPrefix(outputStream);
    }

    @Override // sun.misc.CharacterEncoder
    protected void encodeLinePrefix(OutputStream outputStream, int i2) throws IOException {
        hexDigit(this.pStream, (byte) ((this.offset >>> 8) & 255));
        hexDigit(this.pStream, (byte) (this.offset & 255));
        this.pStream.print(": ");
        this.currentByte = 0;
        this.thisLineLength = i2;
    }

    @Override // sun.misc.CharacterEncoder
    protected void encodeAtom(OutputStream outputStream, byte[] bArr, int i2, int i3) throws IOException {
        this.thisLine[this.currentByte] = bArr[i2];
        hexDigit(this.pStream, bArr[i2]);
        this.pStream.print(" ");
        this.currentByte++;
        if (this.currentByte == 8) {
            this.pStream.print(Constants.INDENT);
        }
    }

    @Override // sun.misc.CharacterEncoder
    protected void encodeLineSuffix(OutputStream outputStream) throws IOException {
        if (this.thisLineLength < 16) {
            for (int i2 = this.thisLineLength; i2 < 16; i2++) {
                this.pStream.print("   ");
                if (i2 == 7) {
                    this.pStream.print(Constants.INDENT);
                }
            }
        }
        this.pStream.print(" ");
        for (int i3 = 0; i3 < this.thisLineLength; i3++) {
            if (this.thisLine[i3] < 32 || this.thisLine[i3] > 122) {
                this.pStream.print(".");
            } else {
                this.pStream.write(this.thisLine[i3]);
            }
        }
        this.pStream.println();
        this.offset += this.thisLineLength;
    }
}
