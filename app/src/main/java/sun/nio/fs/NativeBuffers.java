package sun.nio.fs;

import jdk.internal.misc.TerminatingThreadLocal;
import sun.misc.Unsafe;

/* loaded from: rt.jar:sun/nio/fs/NativeBuffers.class */
class NativeBuffers {
    private static final Unsafe unsafe;
    private static final int TEMP_BUF_POOL_SIZE = 3;
    private static ThreadLocal<NativeBuffer[]> threadLocal;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !NativeBuffers.class.desiredAssertionStatus();
        unsafe = Unsafe.getUnsafe();
        threadLocal = new TerminatingThreadLocal<NativeBuffer[]>() { // from class: sun.nio.fs.NativeBuffers.1
            /* JADX INFO: Access modifiers changed from: protected */
            @Override // jdk.internal.misc.TerminatingThreadLocal
            public void threadTerminated(NativeBuffer[] nativeBufferArr) {
                if (nativeBufferArr != null) {
                    for (int i2 = 0; i2 < 3; i2++) {
                        NativeBuffer nativeBuffer = nativeBufferArr[i2];
                        if (nativeBuffer != null) {
                            nativeBuffer.cleaner().clean();
                            nativeBufferArr[i2] = null;
                        }
                    }
                }
            }
        };
    }

    private NativeBuffers() {
    }

    static NativeBuffer allocNativeBuffer(int i2) {
        if (i2 < 2048) {
            i2 = 2048;
        }
        return new NativeBuffer(i2);
    }

    static NativeBuffer getNativeBufferFromCache(int i2) {
        NativeBuffer[] nativeBufferArr = threadLocal.get();
        if (nativeBufferArr != null) {
            for (int i3 = 0; i3 < 3; i3++) {
                NativeBuffer nativeBuffer = nativeBufferArr[i3];
                if (nativeBuffer != null && nativeBuffer.size() >= i2) {
                    nativeBufferArr[i3] = null;
                    return nativeBuffer;
                }
            }
            return null;
        }
        return null;
    }

    static NativeBuffer getNativeBuffer(int i2) {
        NativeBuffer nativeBufferFromCache = getNativeBufferFromCache(i2);
        if (nativeBufferFromCache != null) {
            nativeBufferFromCache.setOwner(null);
            return nativeBufferFromCache;
        }
        return allocNativeBuffer(i2);
    }

    static void releaseNativeBuffer(NativeBuffer nativeBuffer) {
        NativeBuffer[] nativeBufferArr = threadLocal.get();
        if (nativeBufferArr == null) {
            NativeBuffer[] nativeBufferArr2 = new NativeBuffer[3];
            nativeBufferArr2[0] = nativeBuffer;
            threadLocal.set(nativeBufferArr2);
            return;
        }
        for (int i2 = 0; i2 < 3; i2++) {
            if (nativeBufferArr[i2] == null) {
                nativeBufferArr[i2] = nativeBuffer;
                return;
            }
        }
        for (int i3 = 0; i3 < 3; i3++) {
            NativeBuffer nativeBuffer2 = nativeBufferArr[i3];
            if (nativeBuffer2.size() < nativeBuffer.size()) {
                nativeBuffer2.cleaner().clean();
                nativeBufferArr[i3] = nativeBuffer;
                return;
            }
        }
        nativeBuffer.cleaner().clean();
    }

    static void copyCStringToNativeBuffer(byte[] bArr, NativeBuffer nativeBuffer) {
        long j2 = Unsafe.ARRAY_BYTE_BASE_OFFSET;
        long length = bArr.length;
        if (!$assertionsDisabled && nativeBuffer.size() < length + 1) {
            throw new AssertionError();
        }
        unsafe.copyMemory(bArr, j2, null, nativeBuffer.address(), length);
        unsafe.putByte(nativeBuffer.address() + length, (byte) 0);
    }

    static NativeBuffer asNativeBuffer(byte[] bArr) {
        NativeBuffer nativeBuffer = getNativeBuffer(bArr.length + 1);
        copyCStringToNativeBuffer(bArr, nativeBuffer);
        return nativeBuffer;
    }
}
