package sun.java2d.marlin;

import java.lang.ref.PhantomReference;
import java.lang.ref.ReferenceQueue;
import java.security.AccessController;
import java.util.Vector;
import sun.misc.ThreadGroupUtils;
import sun.misc.Unsafe;

/* loaded from: rt.jar:sun/java2d/marlin/OffHeapArray.class */
final class OffHeapArray {
    long address;
    long length;
    int used = 0;
    private static final ReferenceQueue<Object> rdrQueue = new ReferenceQueue<>();
    private static final Vector<OffHeapReference> refList = new Vector<>(32);
    static final Unsafe unsafe = Unsafe.getUnsafe();
    static final int SIZE_INT = Unsafe.ARRAY_INT_INDEX_SCALE;

    static {
        AccessController.doPrivileged(() -> {
            Thread thread = new Thread(ThreadGroupUtils.getRootThreadGroup(), new OffHeapDisposer(), "MarlinRenderer Disposer");
            thread.setContextClassLoader(null);
            thread.setDaemon(true);
            thread.setPriority(10);
            thread.start();
            return null;
        });
    }

    OffHeapArray(Object obj, long j2) {
        this.address = unsafe.allocateMemory(j2);
        this.length = j2;
        if (MarlinConst.logUnsafeMalloc) {
            MarlinUtils.logInfo(System.currentTimeMillis() + ": OffHeapArray.allocateMemory = " + j2 + " to addr = " + this.address);
        }
        refList.add(new OffHeapReference(obj, this));
    }

    void resize(long j2) {
        this.address = unsafe.reallocateMemory(this.address, j2);
        this.length = j2;
        if (MarlinConst.logUnsafeMalloc) {
            MarlinUtils.logInfo(System.currentTimeMillis() + ": OffHeapArray.reallocateMemory = " + j2 + " to addr = " + this.address);
        }
    }

    void free() {
        unsafe.freeMemory(this.address);
        if (MarlinConst.logUnsafeMalloc) {
            MarlinUtils.logInfo(System.currentTimeMillis() + ": OffHeapEdgeArray.free = " + this.length + " at addr = " + this.address);
        }
    }

    void fill(byte b2) {
        unsafe.setMemory(this.address, this.length, b2);
    }

    /* loaded from: rt.jar:sun/java2d/marlin/OffHeapArray$OffHeapReference.class */
    static final class OffHeapReference extends PhantomReference<Object> {
        private final OffHeapArray array;

        OffHeapReference(Object obj, OffHeapArray offHeapArray) {
            super(obj, OffHeapArray.rdrQueue);
            this.array = offHeapArray;
        }

        void dispose() {
            this.array.free();
        }
    }

    /* loaded from: rt.jar:sun/java2d/marlin/OffHeapArray$OffHeapDisposer.class */
    static final class OffHeapDisposer implements Runnable {
        OffHeapDisposer() {
        }

        @Override // java.lang.Runnable
        public void run() {
            Thread threadCurrentThread = Thread.currentThread();
            while (!threadCurrentThread.isInterrupted()) {
                try {
                    OffHeapReference offHeapReference = (OffHeapReference) OffHeapArray.rdrQueue.remove();
                    offHeapReference.dispose();
                    OffHeapArray.refList.remove(offHeapReference);
                } catch (InterruptedException e2) {
                    MarlinUtils.logException("OffHeapDisposer interrupted:", e2);
                }
            }
        }
    }
}
