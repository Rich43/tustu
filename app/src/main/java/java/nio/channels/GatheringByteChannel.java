package java.nio.channels;

import java.io.IOException;
import java.nio.ByteBuffer;

/* loaded from: rt.jar:java/nio/channels/GatheringByteChannel.class */
public interface GatheringByteChannel extends WritableByteChannel {
    long write(ByteBuffer[] byteBufferArr, int i2, int i3) throws IOException;

    long write(ByteBuffer[] byteBufferArr) throws IOException;
}
