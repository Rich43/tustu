package com.sun.xml.internal.org.jvnet.mimepull;

import java.nio.ByteBuffer;

/* loaded from: rt.jar:com/sun/xml/internal/org/jvnet/mimepull/Chunk.class */
final class Chunk {
    volatile Chunk next;
    volatile Data data;

    public Chunk(Data data) {
        this.data = data;
    }

    public Chunk createNext(DataHead dataHead, ByteBuffer buf) {
        Chunk chunk = new Chunk(this.data.createNext(dataHead, buf));
        this.next = chunk;
        return chunk;
    }
}
