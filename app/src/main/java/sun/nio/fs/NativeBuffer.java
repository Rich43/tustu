package sun.nio.fs;

import sun.misc.Cleaner;
import sun.misc.Unsafe;

/* loaded from: rt.jar:sun/nio/fs/NativeBuffer.class */
class NativeBuffer {
    private static final Unsafe unsafe = Unsafe.getUnsafe();
    private final long address;
    private final int size;
    private final Cleaner cleaner;
    private Object owner;

    /* loaded from: rt.jar:sun/nio/fs/NativeBuffer$Deallocator.class */
    private static class Deallocator implements Runnable {
        private final long address;

        Deallocator(long j2) {
            this.address = j2;
        }

        @Override // java.lang.Runnable
        public void run() {
            NativeBuffer.unsafe.freeMemory(this.address);
        }
    }

    NativeBuffer(int i2) {
        this.address = unsafe.allocateMemory(i2);
        this.size = i2;
        this.cleaner = Cleaner.create(this, new Deallocator(this.address));
    }

    void release() {
        NativeBuffers.releaseNativeBuffer(this);
    }

    long address() {
        return this.address;
    }

    int size() {
        return this.size;
    }

    Cleaner cleaner() {
        return this.cleaner;
    }

    void setOwner(Object obj) {
        this.owner = obj;
    }

    Object owner() {
        return this.owner;
    }
}
