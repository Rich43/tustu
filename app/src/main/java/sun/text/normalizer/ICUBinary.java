package sun.text.normalizer;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

/* loaded from: rt.jar:sun/text/normalizer/ICUBinary.class */
public final class ICUBinary {
    private static final byte MAGIC1 = -38;
    private static final byte MAGIC2 = 39;
    private static final byte BIG_ENDIAN_ = 1;
    private static final byte CHAR_SET_ = 0;
    private static final byte CHAR_SIZE_ = 2;
    private static final String MAGIC_NUMBER_AUTHENTICATION_FAILED_ = "ICU data file error: Not an ICU data file";
    private static final String HEADER_AUTHENTICATION_FAILED_ = "ICU data file error: Header authentication failed, please check if you have a valid ICU data file";

    /* loaded from: rt.jar:sun/text/normalizer/ICUBinary$Authenticate.class */
    public interface Authenticate {
        boolean isDataVersionAcceptable(byte[] bArr);
    }

    public static final byte[] readHeader(InputStream inputStream, byte[] bArr, Authenticate authenticate) throws IOException {
        DataInputStream dataInputStream = new DataInputStream(inputStream);
        char c2 = dataInputStream.readChar();
        byte b2 = dataInputStream.readByte();
        byte b3 = dataInputStream.readByte();
        int i2 = 2 + 1 + 1;
        if (b2 != MAGIC1 || b3 != 39) {
            throw new IOException(MAGIC_NUMBER_AUTHENTICATION_FAILED_);
        }
        dataInputStream.readChar();
        dataInputStream.readChar();
        byte b4 = dataInputStream.readByte();
        byte b5 = dataInputStream.readByte();
        byte b6 = dataInputStream.readByte();
        dataInputStream.readByte();
        byte[] bArr2 = new byte[4];
        dataInputStream.readFully(bArr2);
        byte[] bArr3 = new byte[4];
        dataInputStream.readFully(bArr3);
        byte[] bArr4 = new byte[4];
        dataInputStream.readFully(bArr4);
        int i3 = i2 + 2 + 2 + 1 + 1 + 1 + 1 + 4 + 4 + 4;
        if (c2 < i3) {
            throw new IOException("Internal Error: Header size error");
        }
        dataInputStream.skipBytes(c2 - i3);
        if (b4 != 1 || b5 != 0 || b6 != 2 || !Arrays.equals(bArr, bArr2) || (authenticate != null && !authenticate.isDataVersionAcceptable(bArr3))) {
            throw new IOException(HEADER_AUTHENTICATION_FAILED_);
        }
        return bArr4;
    }
}
