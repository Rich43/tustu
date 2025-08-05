package java.nio.channels;

import java.io.IOException;

/* loaded from: rt.jar:java/nio/channels/AsynchronousChannel.class */
public interface AsynchronousChannel extends Channel {
    @Override // java.nio.channels.Channel, java.io.Closeable, java.lang.AutoCloseable
    void close() throws IOException;
}
