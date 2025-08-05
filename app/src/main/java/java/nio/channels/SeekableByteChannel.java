package java.nio.channels;

import java.io.IOException;
import java.nio.ByteBuffer;

/* loaded from: rt.jar:java/nio/channels/SeekableByteChannel.class */
public interface SeekableByteChannel extends ByteChannel {
    @Override // java.nio.channels.ReadableByteChannel
    int read(ByteBuffer byteBuffer) throws IOException;

    int write(ByteBuffer byteBuffer) throws IOException;

    long position() throws IOException;

    SeekableByteChannel position(long j2) throws IOException;

    long size() throws IOException;

    SeekableByteChannel truncate(long j2) throws IOException;
}
