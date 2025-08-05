package com.sun.xml.internal.stream.util;

/* loaded from: rt.jar:com/sun/xml/internal/stream/util/BufferAllocator.class */
public class BufferAllocator {
    public static int SMALL_SIZE_LIMIT = 128;
    public static int MEDIUM_SIZE_LIMIT = 2048;
    public static int LARGE_SIZE_LIMIT = 8192;
    char[] smallCharBuffer;
    char[] mediumCharBuffer;
    char[] largeCharBuffer;
    byte[] smallByteBuffer;
    byte[] mediumByteBuffer;
    byte[] largeByteBuffer;

    public char[] getCharBuffer(int size) {
        if (size <= SMALL_SIZE_LIMIT) {
            char[] buffer = this.smallCharBuffer;
            this.smallCharBuffer = null;
            return buffer;
        }
        if (size <= MEDIUM_SIZE_LIMIT) {
            char[] buffer2 = this.mediumCharBuffer;
            this.mediumCharBuffer = null;
            return buffer2;
        }
        if (size <= LARGE_SIZE_LIMIT) {
            char[] buffer3 = this.largeCharBuffer;
            this.largeCharBuffer = null;
            return buffer3;
        }
        return null;
    }

    public void returnCharBuffer(char[] c2) {
        if (c2 == null) {
            return;
        }
        if (c2.length <= SMALL_SIZE_LIMIT) {
            this.smallCharBuffer = c2;
        } else if (c2.length <= MEDIUM_SIZE_LIMIT) {
            this.mediumCharBuffer = c2;
        } else if (c2.length <= LARGE_SIZE_LIMIT) {
            this.largeCharBuffer = c2;
        }
    }

    public byte[] getByteBuffer(int size) {
        if (size <= SMALL_SIZE_LIMIT) {
            byte[] buffer = this.smallByteBuffer;
            this.smallByteBuffer = null;
            return buffer;
        }
        if (size <= MEDIUM_SIZE_LIMIT) {
            byte[] buffer2 = this.mediumByteBuffer;
            this.mediumByteBuffer = null;
            return buffer2;
        }
        if (size <= LARGE_SIZE_LIMIT) {
            byte[] buffer3 = this.largeByteBuffer;
            this.largeByteBuffer = null;
            return buffer3;
        }
        return null;
    }

    public void returnByteBuffer(byte[] b2) {
        if (b2 == null) {
            return;
        }
        if (b2.length <= SMALL_SIZE_LIMIT) {
            this.smallByteBuffer = b2;
        } else if (b2.length <= MEDIUM_SIZE_LIMIT) {
            this.mediumByteBuffer = b2;
        } else if (b2.length <= LARGE_SIZE_LIMIT) {
            this.largeByteBuffer = b2;
        }
    }
}
