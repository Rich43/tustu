package sun.security.krb5.internal;

import java.io.IOException;

/* loaded from: rt.jar:sun/security/krb5/internal/NetClient.class */
public abstract class NetClient implements AutoCloseable {
    public abstract void send(byte[] bArr) throws IOException;

    public abstract byte[] receive() throws IOException;

    @Override // java.lang.AutoCloseable
    public abstract void close() throws IOException;

    public static NetClient getInstance(String str, String str2, int i2, int i3) throws IOException {
        if (str.equals("TCP")) {
            return new TCPClient(str2, i2, i3);
        }
        return new UDPClient(str2, i2, i3);
    }
}
