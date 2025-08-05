package sun.nio.ch;

import java.io.FileDescriptor;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.security.AccessController;
import java.security.PrivilegedAction;

/* loaded from: rt.jar:sun/nio/ch/IOUtil.class */
public class IOUtil {
    static final int IOV_MAX;
    static final /* synthetic */ boolean $assertionsDisabled;

    static native boolean randomBytes(byte[] bArr);

    static native long makePipe(boolean z2);

    static native boolean drain(int i2) throws IOException;

    public static native void configureBlocking(FileDescriptor fileDescriptor, boolean z2) throws IOException;

    public static native int fdVal(FileDescriptor fileDescriptor);

    static native void setfdVal(FileDescriptor fileDescriptor, int i2);

    static native int fdLimit();

    static native int iovMax();

    static native void initIDs();

    static {
        $assertionsDisabled = !IOUtil.class.desiredAssertionStatus();
        AccessController.doPrivileged(new PrivilegedAction<Void>() { // from class: sun.nio.ch.IOUtil.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.security.PrivilegedAction
            public Void run() {
                System.loadLibrary("net");
                System.loadLibrary("nio");
                return null;
            }
        });
        initIDs();
        IOV_MAX = iovMax();
    }

    private IOUtil() {
    }

    static int write(FileDescriptor fileDescriptor, ByteBuffer byteBuffer, long j2, NativeDispatcher nativeDispatcher) throws IOException {
        if (byteBuffer instanceof DirectBuffer) {
            return writeFromNativeBuffer(fileDescriptor, byteBuffer, j2, nativeDispatcher);
        }
        int iPosition = byteBuffer.position();
        int iLimit = byteBuffer.limit();
        if (!$assertionsDisabled && iPosition > iLimit) {
            throw new AssertionError();
        }
        ByteBuffer temporaryDirectBuffer = Util.getTemporaryDirectBuffer(iPosition <= iLimit ? iLimit - iPosition : 0);
        try {
            temporaryDirectBuffer.put(byteBuffer);
            temporaryDirectBuffer.flip();
            byteBuffer.position(iPosition);
            int iWriteFromNativeBuffer = writeFromNativeBuffer(fileDescriptor, temporaryDirectBuffer, j2, nativeDispatcher);
            if (iWriteFromNativeBuffer > 0) {
                byteBuffer.position(iPosition + iWriteFromNativeBuffer);
            }
            return iWriteFromNativeBuffer;
        } finally {
            Util.offerFirstTemporaryDirectBuffer(temporaryDirectBuffer);
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    private static int writeFromNativeBuffer(FileDescriptor fileDescriptor, ByteBuffer byteBuffer, long j2, NativeDispatcher nativeDispatcher) throws IOException {
        int iWrite;
        int iPosition = byteBuffer.position();
        int iLimit = byteBuffer.limit();
        if (!$assertionsDisabled && iPosition > iLimit) {
            throw new AssertionError();
        }
        int i2 = iPosition <= iLimit ? iLimit - iPosition : 0;
        if (i2 == 0) {
            return 0;
        }
        if (j2 != -1) {
            iWrite = nativeDispatcher.pwrite(fileDescriptor, ((DirectBuffer) byteBuffer).address() + iPosition, i2, j2);
        } else {
            iWrite = nativeDispatcher.write(fileDescriptor, ((DirectBuffer) byteBuffer).address() + iPosition, i2);
        }
        if (iWrite > 0) {
            byteBuffer.position(iPosition + iWrite);
        }
        return iWrite;
    }

    static long write(FileDescriptor fileDescriptor, ByteBuffer[] byteBufferArr, NativeDispatcher nativeDispatcher) throws IOException {
        return write(fileDescriptor, byteBufferArr, 0, byteBufferArr.length, nativeDispatcher);
    }

    /* JADX WARN: Finally extract failed */
    static long write(FileDescriptor fileDescriptor, ByteBuffer[] byteBufferArr, int i2, int i3, NativeDispatcher nativeDispatcher) throws IOException {
        IOVecWrapper iOVecWrapper = IOVecWrapper.get(i3);
        int i4 = 0;
        try {
            int i5 = i2 + i3;
            for (int i6 = i2; i6 < i5 && i4 < IOV_MAX; i6++) {
                ByteBuffer byteBuffer = byteBufferArr[i6];
                int iPosition = byteBuffer.position();
                int iLimit = byteBuffer.limit();
                if (!$assertionsDisabled && iPosition > iLimit) {
                    throw new AssertionError();
                }
                int i7 = iPosition <= iLimit ? iLimit - iPosition : 0;
                if (i7 > 0) {
                    iOVecWrapper.setBuffer(i4, byteBuffer, iPosition, i7);
                    if (!(byteBuffer instanceof DirectBuffer)) {
                        ByteBuffer temporaryDirectBuffer = Util.getTemporaryDirectBuffer(i7);
                        temporaryDirectBuffer.put(byteBuffer);
                        temporaryDirectBuffer.flip();
                        iOVecWrapper.setShadow(i4, temporaryDirectBuffer);
                        byteBuffer.position(iPosition);
                        byteBuffer = temporaryDirectBuffer;
                        iPosition = temporaryDirectBuffer.position();
                    }
                    iOVecWrapper.putBase(i4, ((DirectBuffer) byteBuffer).address() + iPosition);
                    iOVecWrapper.putLen(i4, i7);
                    i4++;
                }
            }
            if (i4 == 0) {
                if (0 == 0) {
                    for (int i8 = 0; i8 < i4; i8++) {
                        ByteBuffer shadow = iOVecWrapper.getShadow(i8);
                        if (shadow != null) {
                            Util.offerLastTemporaryDirectBuffer(shadow);
                        }
                        iOVecWrapper.clearRefs(i8);
                    }
                }
                return 0L;
            }
            long jWritev = nativeDispatcher.writev(fileDescriptor, iOVecWrapper.address, i4);
            long j2 = jWritev;
            for (int i9 = 0; i9 < i4; i9++) {
                if (j2 > 0) {
                    ByteBuffer buffer = iOVecWrapper.getBuffer(i9);
                    int position = iOVecWrapper.getPosition(i9);
                    int remaining = iOVecWrapper.getRemaining(i9);
                    int i10 = j2 > ((long) remaining) ? remaining : (int) j2;
                    buffer.position(position + i10);
                    j2 -= i10;
                }
                ByteBuffer shadow2 = iOVecWrapper.getShadow(i9);
                if (shadow2 != null) {
                    Util.offerLastTemporaryDirectBuffer(shadow2);
                }
                iOVecWrapper.clearRefs(i9);
            }
            if (1 == 0) {
                for (int i11 = 0; i11 < i4; i11++) {
                    ByteBuffer shadow3 = iOVecWrapper.getShadow(i11);
                    if (shadow3 != null) {
                        Util.offerLastTemporaryDirectBuffer(shadow3);
                    }
                    iOVecWrapper.clearRefs(i11);
                }
            }
            return jWritev;
        } catch (Throwable th) {
            if (0 == 0) {
                for (int i12 = 0; i12 < 0; i12++) {
                    ByteBuffer shadow4 = iOVecWrapper.getShadow(i12);
                    if (shadow4 != null) {
                        Util.offerLastTemporaryDirectBuffer(shadow4);
                    }
                    iOVecWrapper.clearRefs(i12);
                }
            }
            throw th;
        }
    }

    static int read(FileDescriptor fileDescriptor, ByteBuffer byteBuffer, long j2, NativeDispatcher nativeDispatcher) throws IOException {
        if (byteBuffer.isReadOnly()) {
            throw new IllegalArgumentException("Read-only buffer");
        }
        if (byteBuffer instanceof DirectBuffer) {
            return readIntoNativeBuffer(fileDescriptor, byteBuffer, j2, nativeDispatcher);
        }
        ByteBuffer temporaryDirectBuffer = Util.getTemporaryDirectBuffer(byteBuffer.remaining());
        try {
            int intoNativeBuffer = readIntoNativeBuffer(fileDescriptor, temporaryDirectBuffer, j2, nativeDispatcher);
            temporaryDirectBuffer.flip();
            if (intoNativeBuffer > 0) {
                byteBuffer.put(temporaryDirectBuffer);
            }
            return intoNativeBuffer;
        } finally {
            Util.offerFirstTemporaryDirectBuffer(temporaryDirectBuffer);
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    private static int readIntoNativeBuffer(FileDescriptor fileDescriptor, ByteBuffer byteBuffer, long j2, NativeDispatcher nativeDispatcher) throws IOException {
        int iPread;
        int iPosition = byteBuffer.position();
        int iLimit = byteBuffer.limit();
        if (!$assertionsDisabled && iPosition > iLimit) {
            throw new AssertionError();
        }
        int i2 = iPosition <= iLimit ? iLimit - iPosition : 0;
        if (i2 == 0) {
            return 0;
        }
        if (j2 != -1) {
            iPread = nativeDispatcher.pread(fileDescriptor, ((DirectBuffer) byteBuffer).address() + iPosition, i2, j2);
        } else {
            iPread = nativeDispatcher.read(fileDescriptor, ((DirectBuffer) byteBuffer).address() + iPosition, i2);
        }
        if (iPread > 0) {
            byteBuffer.position(iPosition + iPread);
        }
        return iPread;
    }

    static long read(FileDescriptor fileDescriptor, ByteBuffer[] byteBufferArr, NativeDispatcher nativeDispatcher) throws IOException {
        return read(fileDescriptor, byteBufferArr, 0, byteBufferArr.length, nativeDispatcher);
    }

    /* JADX WARN: Finally extract failed */
    static long read(FileDescriptor fileDescriptor, ByteBuffer[] byteBufferArr, int i2, int i3, NativeDispatcher nativeDispatcher) throws IOException {
        IOVecWrapper iOVecWrapper = IOVecWrapper.get(i3);
        int i4 = 0;
        try {
            int i5 = i2 + i3;
            for (int i6 = i2; i6 < i5 && i4 < IOV_MAX; i6++) {
                ByteBuffer byteBuffer = byteBufferArr[i6];
                if (byteBuffer.isReadOnly()) {
                    throw new IllegalArgumentException("Read-only buffer");
                }
                int iPosition = byteBuffer.position();
                int iLimit = byteBuffer.limit();
                if (!$assertionsDisabled && iPosition > iLimit) {
                    throw new AssertionError();
                }
                int i7 = iPosition <= iLimit ? iLimit - iPosition : 0;
                if (i7 > 0) {
                    iOVecWrapper.setBuffer(i4, byteBuffer, iPosition, i7);
                    if (!(byteBuffer instanceof DirectBuffer)) {
                        ByteBuffer temporaryDirectBuffer = Util.getTemporaryDirectBuffer(i7);
                        iOVecWrapper.setShadow(i4, temporaryDirectBuffer);
                        byteBuffer = temporaryDirectBuffer;
                        iPosition = temporaryDirectBuffer.position();
                    }
                    iOVecWrapper.putBase(i4, ((DirectBuffer) byteBuffer).address() + iPosition);
                    iOVecWrapper.putLen(i4, i7);
                    i4++;
                }
            }
            if (i4 == 0) {
                if (0 == 0) {
                    for (int i8 = 0; i8 < i4; i8++) {
                        ByteBuffer shadow = iOVecWrapper.getShadow(i8);
                        if (shadow != null) {
                            Util.offerLastTemporaryDirectBuffer(shadow);
                        }
                        iOVecWrapper.clearRefs(i8);
                    }
                }
                return 0L;
            }
            long vVar = nativeDispatcher.readv(fileDescriptor, iOVecWrapper.address, i4);
            long j2 = vVar;
            for (int i9 = 0; i9 < i4; i9++) {
                ByteBuffer shadow2 = iOVecWrapper.getShadow(i9);
                if (j2 > 0) {
                    ByteBuffer buffer = iOVecWrapper.getBuffer(i9);
                    int remaining = iOVecWrapper.getRemaining(i9);
                    int i10 = j2 > ((long) remaining) ? remaining : (int) j2;
                    if (shadow2 == null) {
                        buffer.position(iOVecWrapper.getPosition(i9) + i10);
                    } else {
                        shadow2.limit(shadow2.position() + i10);
                        buffer.put(shadow2);
                    }
                    j2 -= i10;
                }
                if (shadow2 != null) {
                    Util.offerLastTemporaryDirectBuffer(shadow2);
                }
                iOVecWrapper.clearRefs(i9);
            }
            if (1 == 0) {
                for (int i11 = 0; i11 < i4; i11++) {
                    ByteBuffer shadow3 = iOVecWrapper.getShadow(i11);
                    if (shadow3 != null) {
                        Util.offerLastTemporaryDirectBuffer(shadow3);
                    }
                    iOVecWrapper.clearRefs(i11);
                }
            }
            return vVar;
        } catch (Throwable th) {
            if (0 == 0) {
                for (int i12 = 0; i12 < 0; i12++) {
                    ByteBuffer shadow4 = iOVecWrapper.getShadow(i12);
                    if (shadow4 != null) {
                        Util.offerLastTemporaryDirectBuffer(shadow4);
                    }
                    iOVecWrapper.clearRefs(i12);
                }
            }
            throw th;
        }
    }

    public static FileDescriptor newFD(int i2) {
        FileDescriptor fileDescriptor = new FileDescriptor();
        setfdVal(fileDescriptor, i2);
        return fileDescriptor;
    }

    public static void load() {
    }
}
