package sun.security.krb5.internal.rcache;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.SeekableByteChannel;
import java.nio.charset.StandardCharsets;
import java.util.StringTokenizer;

/* loaded from: rt.jar:sun/security/krb5/internal/rcache/AuthTime.class */
public class AuthTime {
    final int ctime;
    final int cusec;
    final String client;
    final String server;

    public AuthTime(String str, String str2, int i2, int i3) {
        this.ctime = i2;
        this.cusec = i3;
        this.client = str;
        this.server = str2;
    }

    public String toString() {
        return String.format("%d/%06d/----/%s", Integer.valueOf(this.ctime), Integer.valueOf(this.cusec), this.client);
    }

    private static String readStringWithLength(SeekableByteChannel seekableByteChannel) throws IOException {
        ByteBuffer byteBufferAllocate = ByteBuffer.allocate(4);
        byteBufferAllocate.order(ByteOrder.nativeOrder());
        seekableByteChannel.read(byteBufferAllocate);
        byteBufferAllocate.flip();
        int i2 = byteBufferAllocate.getInt();
        if (i2 > 1024) {
            throw new IOException("Invalid string length");
        }
        ByteBuffer byteBufferAllocate2 = ByteBuffer.allocate(i2);
        if (seekableByteChannel.read(byteBufferAllocate2) != i2) {
            throw new IOException("Not enough string");
        }
        byte[] bArrArray = byteBufferAllocate2.array();
        return bArrArray[i2 - 1] == 0 ? new String(bArrArray, 0, i2 - 1, StandardCharsets.UTF_8) : new String(bArrArray, StandardCharsets.UTF_8);
    }

    public static AuthTime readFrom(SeekableByteChannel seekableByteChannel) throws IOException {
        String stringWithLength = readStringWithLength(seekableByteChannel);
        String stringWithLength2 = readStringWithLength(seekableByteChannel);
        ByteBuffer byteBufferAllocate = ByteBuffer.allocate(8);
        seekableByteChannel.read(byteBufferAllocate);
        byteBufferAllocate.order(ByteOrder.nativeOrder());
        int i2 = byteBufferAllocate.getInt(0);
        int i3 = byteBufferAllocate.getInt(4);
        if (stringWithLength.isEmpty()) {
            StringTokenizer stringTokenizer = new StringTokenizer(stringWithLength2, " :");
            if (stringTokenizer.countTokens() != 6) {
                throw new IOException("Incorrect rcache style");
            }
            stringTokenizer.nextToken();
            String strNextToken = stringTokenizer.nextToken();
            stringTokenizer.nextToken();
            String strNextToken2 = stringTokenizer.nextToken();
            stringTokenizer.nextToken();
            return new AuthTimeWithHash(strNextToken2, stringTokenizer.nextToken(), i3, i2, strNextToken);
        }
        return new AuthTime(stringWithLength, stringWithLength2, i3, i2);
    }

    protected byte[] encode0(String str, String str2) {
        byte[] bytes = str.getBytes(StandardCharsets.UTF_8);
        byte[] bytes2 = str2.getBytes(StandardCharsets.UTF_8);
        byte[] bArr = new byte[1];
        ByteBuffer byteBufferOrder = ByteBuffer.allocate(4 + bytes.length + 1 + 4 + bytes2.length + 1 + 4 + 4).order(ByteOrder.nativeOrder());
        byteBufferOrder.putInt(bytes.length + 1).put(bytes).put(bArr).putInt(bytes2.length + 1).put(bytes2).put(bArr).putInt(this.cusec).putInt(this.ctime);
        return byteBufferOrder.array();
    }

    public byte[] encode(boolean z2) {
        return encode0(this.client, this.server);
    }
}
