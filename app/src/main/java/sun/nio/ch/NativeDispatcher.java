package sun.nio.ch;

import java.io.FileDescriptor;
import java.io.IOException;

/* loaded from: rt.jar:sun/nio/ch/NativeDispatcher.class */
abstract class NativeDispatcher {
    abstract int read(FileDescriptor fileDescriptor, long j2, int i2) throws IOException;

    abstract long readv(FileDescriptor fileDescriptor, long j2, int i2) throws IOException;

    abstract int write(FileDescriptor fileDescriptor, long j2, int i2) throws IOException;

    abstract long writev(FileDescriptor fileDescriptor, long j2, int i2) throws IOException;

    abstract void close(FileDescriptor fileDescriptor) throws IOException;

    NativeDispatcher() {
    }

    boolean needsPositionLock() {
        return false;
    }

    int pread(FileDescriptor fileDescriptor, long j2, int i2, long j3) throws IOException {
        throw new IOException("Operation Unsupported");
    }

    int pwrite(FileDescriptor fileDescriptor, long j2, int i2, long j3) throws IOException {
        throw new IOException("Operation Unsupported");
    }

    void preClose(FileDescriptor fileDescriptor) throws IOException {
    }
}
