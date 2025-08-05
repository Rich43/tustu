package sun.misc;

import java.nio.Buffer;
import java.nio.ByteBuffer;

/* loaded from: rt.jar:sun/misc/JavaNioAccess.class */
public interface JavaNioAccess {

    /* loaded from: rt.jar:sun/misc/JavaNioAccess$BufferPool.class */
    public interface BufferPool {
        String getName();

        long getCount();

        long getTotalCapacity();

        long getMemoryUsed();
    }

    BufferPool getDirectBufferPool();

    ByteBuffer newDirectByteBuffer(long j2, int i2, Object obj);

    void truncate(Buffer buffer);
}
