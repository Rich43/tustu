package java.nio.channels;

import java.io.IOException;
import java.nio.ByteBuffer;

/* loaded from: rt.jar:java/nio/channels/ReadableByteChannel.class */
public interface ReadableByteChannel extends Channel {
    int read(ByteBuffer byteBuffer) throws IOException;
}
