package java.nio.channels;

import java.io.Closeable;
import java.io.IOException;

/* loaded from: rt.jar:java/nio/channels/Channel.class */
public interface Channel extends Closeable {
    boolean isOpen();

    @Override // java.io.Closeable, java.lang.AutoCloseable
    void close() throws IOException;
}
