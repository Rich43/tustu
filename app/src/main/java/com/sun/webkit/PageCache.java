package com.sun.webkit;

/* loaded from: jfxrt.jar:com/sun/webkit/PageCache.class */
public final class PageCache {
    private static native int twkGetCapacity();

    private static native void twkSetCapacity(int i2);

    private PageCache() {
        throw new AssertionError();
    }

    public static int getCapacity() {
        return twkGetCapacity();
    }

    public static void setCapacity(int capacity) {
        if (capacity < 0) {
            throw new IllegalArgumentException("capacity is negative:" + capacity);
        }
        twkSetCapacity(capacity);
    }
}
