package java.net;

import java.io.IOException;
import java.io.OutputStream;

/* loaded from: rt.jar:java/net/CacheRequest.class */
public abstract class CacheRequest {
    public abstract OutputStream getBody() throws IOException;

    public abstract void abort();
}
