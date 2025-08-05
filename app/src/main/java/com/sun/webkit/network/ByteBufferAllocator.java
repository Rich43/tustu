package com.sun.webkit.network;

import java.nio.ByteBuffer;

/* compiled from: ByteBufferPool.java */
/* loaded from: jfxrt.jar:com/sun/webkit/network/ByteBufferAllocator.class */
interface ByteBufferAllocator {
    ByteBuffer allocate() throws InterruptedException;

    void release(ByteBuffer byteBuffer);
}
