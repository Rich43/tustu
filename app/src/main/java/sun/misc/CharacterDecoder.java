package sun.misc;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PushbackInputStream;
import java.nio.ByteBuffer;

/* loaded from: rt.jar:sun/misc/CharacterDecoder.class */
public abstract class CharacterDecoder {
    protected abstract int bytesPerAtom();

    protected abstract int bytesPerLine();

    protected void decodeBufferPrefix(PushbackInputStream pushbackInputStream, OutputStream outputStream) throws IOException {
    }

    protected void decodeBufferSuffix(PushbackInputStream pushbackInputStream, OutputStream outputStream) throws IOException {
    }

    protected int decodeLinePrefix(PushbackInputStream pushbackInputStream, OutputStream outputStream) throws IOException {
        return bytesPerLine();
    }

    protected void decodeLineSuffix(PushbackInputStream pushbackInputStream, OutputStream outputStream) throws IOException {
    }

    protected void decodeAtom(PushbackInputStream pushbackInputStream, OutputStream outputStream, int i2) throws IOException {
        throw new CEStreamExhausted();
    }

    protected int readFully(InputStream inputStream, byte[] bArr, int i2, int i3) throws IOException {
        for (int i4 = 0; i4 < i3; i4++) {
            int i5 = inputStream.read();
            if (i5 == -1) {
                if (i4 == 0) {
                    return -1;
                }
                return i4;
            }
            bArr[i4 + i2] = (byte) i5;
        }
        return i3;
    }

    public void decodeBuffer(InputStream inputStream, OutputStream outputStream) throws IOException {
        int iBytesPerAtom = 0;
        PushbackInputStream pushbackInputStream = new PushbackInputStream(inputStream);
        decodeBufferPrefix(pushbackInputStream, outputStream);
        while (true) {
            try {
                int iDecodeLinePrefix = decodeLinePrefix(pushbackInputStream, outputStream);
                int iBytesPerAtom2 = 0;
                while (iBytesPerAtom2 + bytesPerAtom() < iDecodeLinePrefix) {
                    decodeAtom(pushbackInputStream, outputStream, bytesPerAtom());
                    iBytesPerAtom += bytesPerAtom();
                    iBytesPerAtom2 += bytesPerAtom();
                }
                if (iBytesPerAtom2 + bytesPerAtom() == iDecodeLinePrefix) {
                    decodeAtom(pushbackInputStream, outputStream, bytesPerAtom());
                    iBytesPerAtom += bytesPerAtom();
                } else {
                    decodeAtom(pushbackInputStream, outputStream, iDecodeLinePrefix - iBytesPerAtom2);
                    iBytesPerAtom += iDecodeLinePrefix - iBytesPerAtom2;
                }
                decodeLineSuffix(pushbackInputStream, outputStream);
            } catch (CEStreamExhausted e2) {
                decodeBufferSuffix(pushbackInputStream, outputStream);
                return;
            }
        }
    }

    public byte[] decodeBuffer(String str) throws IOException {
        byte[] bArr = new byte[str.length()];
        str.getBytes(0, str.length(), bArr, 0);
        InputStream byteArrayInputStream = new ByteArrayInputStream(bArr);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        decodeBuffer(byteArrayInputStream, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

    public byte[] decodeBuffer(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        decodeBuffer(inputStream, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

    public ByteBuffer decodeBufferToByteBuffer(String str) throws IOException {
        return ByteBuffer.wrap(decodeBuffer(str));
    }

    public ByteBuffer decodeBufferToByteBuffer(InputStream inputStream) throws IOException {
        return ByteBuffer.wrap(decodeBuffer(inputStream));
    }
}
