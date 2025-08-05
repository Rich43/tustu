package sun.misc;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

/* loaded from: rt.jar:sun/misc/UUEncoder.class */
public class UUEncoder extends CharacterEncoder {
    private String bufferName;
    private int mode;

    public UUEncoder() {
        this.bufferName = "encoder.buf";
        this.mode = 644;
    }

    public UUEncoder(String str) {
        this.bufferName = str;
        this.mode = 644;
    }

    public UUEncoder(String str, int i2) {
        this.bufferName = str;
        this.mode = i2;
    }

    @Override // sun.misc.CharacterEncoder
    protected int bytesPerAtom() {
        return 3;
    }

    @Override // sun.misc.CharacterEncoder
    protected int bytesPerLine() {
        return 45;
    }

    @Override // sun.misc.CharacterEncoder
    protected void encodeAtom(OutputStream outputStream, byte[] bArr, int i2, int i3) throws IOException {
        byte b2 = 1;
        byte b3 = 1;
        byte b4 = bArr[i2];
        if (i3 > 1) {
            b2 = bArr[i2 + 1];
        }
        if (i3 > 2) {
            b3 = bArr[i2 + 2];
        }
        int i4 = (b4 >>> 2) & 63;
        outputStream.write(i4 + 32);
        outputStream.write((((b4 << 4) & 48) | ((b2 >>> 4) & 15)) + 32);
        outputStream.write((((b2 << 2) & 60) | ((b3 >>> 6) & 3)) + 32);
        outputStream.write((b3 & 63) + 32);
    }

    @Override // sun.misc.CharacterEncoder
    protected void encodeLinePrefix(OutputStream outputStream, int i2) throws IOException {
        outputStream.write((i2 & 63) + 32);
    }

    @Override // sun.misc.CharacterEncoder
    protected void encodeLineSuffix(OutputStream outputStream) throws IOException {
        this.pStream.println();
    }

    @Override // sun.misc.CharacterEncoder
    protected void encodeBufferPrefix(OutputStream outputStream) throws IOException {
        this.pStream = new PrintStream(outputStream);
        this.pStream.print("begin " + this.mode + " ");
        if (this.bufferName != null) {
            this.pStream.println(this.bufferName);
        } else {
            this.pStream.println("encoder.bin");
        }
        this.pStream.flush();
    }

    @Override // sun.misc.CharacterEncoder
    protected void encodeBufferSuffix(OutputStream outputStream) throws IOException {
        this.pStream.println(" \nend");
        this.pStream.flush();
    }
}
