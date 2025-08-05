package com.sun.corba.se.pept.transport;

import java.nio.ByteBuffer;

/* loaded from: rt.jar:com/sun/corba/se/pept/transport/ByteBufferPool.class */
public interface ByteBufferPool {
    ByteBuffer getByteBuffer(int i2);

    void releaseByteBuffer(ByteBuffer byteBuffer);

    int activeCount();
}
