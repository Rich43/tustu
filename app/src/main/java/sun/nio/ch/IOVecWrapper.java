package sun.nio.ch;

import java.nio.ByteBuffer;
import sun.misc.Cleaner;

/* loaded from: rt.jar:sun/nio/ch/IOVecWrapper.class */
class IOVecWrapper {
    private static final int BASE_OFFSET = 0;
    private final AllocatedNativeObject vecArray;
    private final int size;
    private final ByteBuffer[] buf;
    private final int[] position;
    private final int[] remaining;
    private final ByteBuffer[] shadow;
    final long address;
    private static final ThreadLocal<IOVecWrapper> cached = new ThreadLocal<>();
    static int addressSize = Util.unsafe().addressSize();
    private static final int LEN_OFFSET = addressSize;
    private static final int SIZE_IOVEC = (short) (addressSize * 2);

    /* loaded from: rt.jar:sun/nio/ch/IOVecWrapper$Deallocator.class */
    private static class Deallocator implements Runnable {
        private final AllocatedNativeObject obj;

        Deallocator(AllocatedNativeObject allocatedNativeObject) {
            this.obj = allocatedNativeObject;
        }

        @Override // java.lang.Runnable
        public void run() {
            this.obj.free();
        }
    }

    private IOVecWrapper(int i2) {
        this.size = i2;
        this.buf = new ByteBuffer[i2];
        this.position = new int[i2];
        this.remaining = new int[i2];
        this.shadow = new ByteBuffer[i2];
        this.vecArray = new AllocatedNativeObject(i2 * SIZE_IOVEC, false);
        this.address = this.vecArray.address();
    }

    static IOVecWrapper get(int i2) {
        IOVecWrapper iOVecWrapper = cached.get();
        if (iOVecWrapper != null && iOVecWrapper.size < i2) {
            iOVecWrapper.vecArray.free();
            iOVecWrapper = null;
        }
        if (iOVecWrapper == null) {
            iOVecWrapper = new IOVecWrapper(i2);
            Cleaner.create(iOVecWrapper, new Deallocator(iOVecWrapper.vecArray));
            cached.set(iOVecWrapper);
        }
        return iOVecWrapper;
    }

    void setBuffer(int i2, ByteBuffer byteBuffer, int i3, int i4) {
        this.buf[i2] = byteBuffer;
        this.position[i2] = i3;
        this.remaining[i2] = i4;
    }

    void setShadow(int i2, ByteBuffer byteBuffer) {
        this.shadow[i2] = byteBuffer;
    }

    ByteBuffer getBuffer(int i2) {
        return this.buf[i2];
    }

    int getPosition(int i2) {
        return this.position[i2];
    }

    int getRemaining(int i2) {
        return this.remaining[i2];
    }

    ByteBuffer getShadow(int i2) {
        return this.shadow[i2];
    }

    void clearRefs(int i2) {
        this.buf[i2] = null;
        this.shadow[i2] = null;
    }

    void putBase(int i2, long j2) {
        int i3 = (SIZE_IOVEC * i2) + 0;
        if (addressSize == 4) {
            this.vecArray.putInt(i3, (int) j2);
        } else {
            this.vecArray.putLong(i3, j2);
        }
    }

    void putLen(int i2, long j2) {
        int i3 = (SIZE_IOVEC * i2) + LEN_OFFSET;
        if (addressSize == 4) {
            this.vecArray.putInt(i3, (int) j2);
        } else {
            this.vecArray.putLong(i3, j2);
        }
    }
}
