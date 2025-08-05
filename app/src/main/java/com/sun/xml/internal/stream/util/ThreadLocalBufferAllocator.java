package com.sun.xml.internal.stream.util;

import java.lang.ref.SoftReference;

/* loaded from: rt.jar:com/sun/xml/internal/stream/util/ThreadLocalBufferAllocator.class */
public class ThreadLocalBufferAllocator {
    private static final ThreadLocal<SoftReference<BufferAllocator>> TL = new ThreadLocal<>();

    public static BufferAllocator getBufferAllocator() {
        BufferAllocator ba2 = null;
        SoftReference<BufferAllocator> sr = TL.get();
        if (sr != null) {
            ba2 = sr.get();
        }
        if (ba2 == null) {
            ba2 = new BufferAllocator();
            TL.set(new SoftReference<>(ba2));
        }
        return ba2;
    }
}
