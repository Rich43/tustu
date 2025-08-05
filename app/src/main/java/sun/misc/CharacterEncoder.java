package sun.misc;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.nio.ByteBuffer;

/* loaded from: rt.jar:sun/misc/CharacterEncoder.class */
public abstract class CharacterEncoder {
    protected PrintStream pStream;

    protected abstract int bytesPerAtom();

    protected abstract int bytesPerLine();

    protected abstract void encodeAtom(OutputStream outputStream, byte[] bArr, int i2, int i3) throws IOException;

    protected void encodeBufferPrefix(OutputStream outputStream) throws IOException {
        this.pStream = new PrintStream(outputStream);
    }

    protected void encodeBufferSuffix(OutputStream outputStream) throws IOException {
    }

    protected void encodeLinePrefix(OutputStream outputStream, int i2) throws IOException {
    }

    protected void encodeLineSuffix(OutputStream outputStream) throws IOException {
        this.pStream.println();
    }

    protected int readFully(InputStream inputStream, byte[] bArr) throws IOException {
        for (int i2 = 0; i2 < bArr.length; i2++) {
            int i3 = inputStream.read();
            if (i3 == -1) {
                return i2;
            }
            bArr[i2] = (byte) i3;
        }
        return bArr.length;
    }

    public void encode(InputStream inputStream, OutputStream outputStream) throws IOException {
        byte[] bArr = new byte[bytesPerLine()];
        encodeBufferPrefix(outputStream);
        while (true) {
            int fully = readFully(inputStream, bArr);
            if (fully == 0) {
                break;
            }
            encodeLinePrefix(outputStream, fully);
            int iBytesPerAtom = 0;
            while (true) {
                int i2 = iBytesPerAtom;
                if (i2 >= fully) {
                    break;
                }
                if (i2 + bytesPerAtom() <= fully) {
                    encodeAtom(outputStream, bArr, i2, bytesPerAtom());
                } else {
                    encodeAtom(outputStream, bArr, i2, fully - i2);
                }
                iBytesPerAtom = i2 + bytesPerAtom();
            }
            if (fully < bytesPerLine()) {
                break;
            } else {
                encodeLineSuffix(outputStream);
            }
        }
        encodeBufferSuffix(outputStream);
    }

    public void encode(byte[] bArr, OutputStream outputStream) throws IOException {
        encode(new ByteArrayInputStream(bArr), outputStream);
    }

    public String encode(byte[] bArr) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            encode(new ByteArrayInputStream(bArr), byteArrayOutputStream);
            return byteArrayOutputStream.toString("8859_1");
        } catch (Exception e2) {
            throw new Error("CharacterEncoder.encode internal error");
        }
    }

    private byte[] getBytes(ByteBuffer byteBuffer) {
        byte[] bArr = null;
        if (byteBuffer.hasArray()) {
            byte[] bArrArray = byteBuffer.array();
            if (bArrArray.length == byteBuffer.capacity() && bArrArray.length == byteBuffer.remaining()) {
                bArr = bArrArray;
                byteBuffer.position(byteBuffer.limit());
            }
        }
        if (bArr == null) {
            bArr = new byte[byteBuffer.remaining()];
            byteBuffer.get(bArr);
        }
        return bArr;
    }

    public void encode(ByteBuffer byteBuffer, OutputStream outputStream) throws IOException {
        encode(getBytes(byteBuffer), outputStream);
    }

    public String encode(ByteBuffer byteBuffer) {
        return encode(getBytes(byteBuffer));
    }

    public void encodeBuffer(InputStream inputStream, OutputStream outputStream) throws IOException {
        int fully;
        byte[] bArr = new byte[bytesPerLine()];
        encodeBufferPrefix(outputStream);
        do {
            fully = readFully(inputStream, bArr);
            if (fully == 0) {
                break;
            }
            encodeLinePrefix(outputStream, fully);
            int iBytesPerAtom = 0;
            while (true) {
                int i2 = iBytesPerAtom;
                if (i2 >= fully) {
                    break;
                }
                if (i2 + bytesPerAtom() <= fully) {
                    encodeAtom(outputStream, bArr, i2, bytesPerAtom());
                } else {
                    encodeAtom(outputStream, bArr, i2, fully - i2);
                }
                iBytesPerAtom = i2 + bytesPerAtom();
            }
            encodeLineSuffix(outputStream);
        } while (fully >= bytesPerLine());
        encodeBufferSuffix(outputStream);
    }

    public void encodeBuffer(byte[] bArr, OutputStream outputStream) throws IOException {
        encodeBuffer(new ByteArrayInputStream(bArr), outputStream);
    }

    public String encodeBuffer(byte[] bArr) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            encodeBuffer(new ByteArrayInputStream(bArr), byteArrayOutputStream);
            return byteArrayOutputStream.toString();
        } catch (Exception e2) {
            throw new Error("CharacterEncoder.encodeBuffer internal error");
        }
    }

    public void encodeBuffer(ByteBuffer byteBuffer, OutputStream outputStream) throws IOException {
        encodeBuffer(getBytes(byteBuffer), outputStream);
    }

    public String encodeBuffer(ByteBuffer byteBuffer) {
        return encodeBuffer(getBytes(byteBuffer));
    }
}
