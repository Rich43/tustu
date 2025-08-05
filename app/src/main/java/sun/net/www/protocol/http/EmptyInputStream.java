package sun.net.www.protocol.http;

import java.io.InputStream;

/* compiled from: HttpURLConnection.java */
/* loaded from: rt.jar:sun/net/www/protocol/http/EmptyInputStream.class */
class EmptyInputStream extends InputStream {
    EmptyInputStream() {
    }

    @Override // java.io.InputStream
    public int available() {
        return 0;
    }

    @Override // java.io.InputStream
    public int read() {
        return -1;
    }
}
