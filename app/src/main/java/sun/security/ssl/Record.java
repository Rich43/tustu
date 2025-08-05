package sun.security.ssl;

import java.io.IOException;
import java.nio.ByteBuffer;
import javax.net.ssl.SSLException;

/* loaded from: jsse.jar:sun/security/ssl/Record.class */
interface Record {
    public static final int maxMacSize = 48;
    public static final int maxDataSize = 16384;
    public static final int maxPadding = 256;
    public static final int maxIVLength = 16;
    public static final int maxFragmentSize = 18432;
    public static final boolean enableCBCProtection = Utilities.getBooleanProperty("jsse.enableCBCProtection", true);
    public static final int OVERFLOW_OF_INT08 = 256;
    public static final int OVERFLOW_OF_INT16 = 65536;
    public static final int OVERFLOW_OF_INT24 = 16777216;

    static int getInt8(ByteBuffer byteBuffer) throws IOException {
        verifyLength(byteBuffer, 1);
        return byteBuffer.get() & 255;
    }

    static int getInt16(ByteBuffer byteBuffer) throws IOException {
        verifyLength(byteBuffer, 2);
        return ((byteBuffer.get() & 255) << 8) | (byteBuffer.get() & 255);
    }

    static int getInt24(ByteBuffer byteBuffer) throws IOException {
        verifyLength(byteBuffer, 3);
        return ((byteBuffer.get() & 255) << 16) | ((byteBuffer.get() & 255) << 8) | (byteBuffer.get() & 255);
    }

    static int getInt32(ByteBuffer byteBuffer) throws IOException {
        verifyLength(byteBuffer, 4);
        return ((byteBuffer.get() & 255) << 24) | ((byteBuffer.get() & 255) << 16) | ((byteBuffer.get() & 255) << 8) | (byteBuffer.get() & 255);
    }

    static byte[] getBytes8(ByteBuffer byteBuffer) throws IOException {
        int int8 = getInt8(byteBuffer);
        verifyLength(byteBuffer, int8);
        byte[] bArr = new byte[int8];
        byteBuffer.get(bArr);
        return bArr;
    }

    static byte[] getBytes16(ByteBuffer byteBuffer) throws IOException {
        int int16 = getInt16(byteBuffer);
        verifyLength(byteBuffer, int16);
        byte[] bArr = new byte[int16];
        byteBuffer.get(bArr);
        return bArr;
    }

    static byte[] getBytes24(ByteBuffer byteBuffer) throws IOException {
        int int24 = getInt24(byteBuffer);
        verifyLength(byteBuffer, int24);
        byte[] bArr = new byte[int24];
        byteBuffer.get(bArr);
        return bArr;
    }

    static void putInt8(ByteBuffer byteBuffer, int i2) throws IOException {
        verifyLength(byteBuffer, 1);
        byteBuffer.put((byte) (i2 & 255));
    }

    static void putInt16(ByteBuffer byteBuffer, int i2) throws IOException {
        verifyLength(byteBuffer, 2);
        byteBuffer.put((byte) ((i2 >> 8) & 255));
        byteBuffer.put((byte) (i2 & 255));
    }

    static void putInt24(ByteBuffer byteBuffer, int i2) throws IOException {
        verifyLength(byteBuffer, 3);
        byteBuffer.put((byte) ((i2 >> 16) & 255));
        byteBuffer.put((byte) ((i2 >> 8) & 255));
        byteBuffer.put((byte) (i2 & 255));
    }

    static void putInt32(ByteBuffer byteBuffer, int i2) throws IOException {
        byteBuffer.put((byte) ((i2 >> 24) & 255));
        byteBuffer.put((byte) ((i2 >> 16) & 255));
        byteBuffer.put((byte) ((i2 >> 8) & 255));
        byteBuffer.put((byte) (i2 & 255));
    }

    static void putBytes8(ByteBuffer byteBuffer, byte[] bArr) throws IOException {
        if (bArr == null || bArr.length == 0) {
            verifyLength(byteBuffer, 1);
            putInt8(byteBuffer, 0);
        } else {
            verifyLength(byteBuffer, 1 + bArr.length);
            putInt8(byteBuffer, bArr.length);
            byteBuffer.put(bArr);
        }
    }

    static void putBytes16(ByteBuffer byteBuffer, byte[] bArr) throws IOException {
        if (bArr == null || bArr.length == 0) {
            verifyLength(byteBuffer, 2);
            putInt16(byteBuffer, 0);
        } else {
            verifyLength(byteBuffer, 2 + bArr.length);
            putInt16(byteBuffer, bArr.length);
            byteBuffer.put(bArr);
        }
    }

    static void putBytes24(ByteBuffer byteBuffer, byte[] bArr) throws IOException {
        if (bArr == null || bArr.length == 0) {
            verifyLength(byteBuffer, 3);
            putInt24(byteBuffer, 0);
        } else {
            verifyLength(byteBuffer, 3 + bArr.length);
            putInt24(byteBuffer, bArr.length);
            byteBuffer.put(bArr);
        }
    }

    static void verifyLength(ByteBuffer byteBuffer, int i2) throws SSLException {
        if (i2 > byteBuffer.remaining()) {
            throw new SSLException("Insufficient space in the buffer, may be cause by an unexpected end of handshake data.");
        }
    }
}
