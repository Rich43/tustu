package java.nio.channels;

import java.io.IOException;
import java.nio.ByteBuffer;

/* loaded from: rt.jar:java/nio/channels/ScatteringByteChannel.class */
public interface ScatteringByteChannel extends ReadableByteChannel {
    long read(ByteBuffer[] byteBufferArr, int i2, int i3) throws IOException;

    long read(ByteBuffer[] byteBufferArr) throws IOException;
}
