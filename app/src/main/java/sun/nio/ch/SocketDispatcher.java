package sun.nio.ch;

import java.io.FileDescriptor;
import java.io.IOException;

/* loaded from: rt.jar:sun/nio/ch/SocketDispatcher.class */
class SocketDispatcher extends NativeDispatcher {
    static native int read0(FileDescriptor fileDescriptor, long j2, int i2) throws IOException;

    static native long readv0(FileDescriptor fileDescriptor, long j2, int i2) throws IOException;

    static native int write0(FileDescriptor fileDescriptor, long j2, int i2) throws IOException;

    static native long writev0(FileDescriptor fileDescriptor, long j2, int i2) throws IOException;

    static native void preClose0(FileDescriptor fileDescriptor) throws IOException;

    static native void close0(FileDescriptor fileDescriptor) throws IOException;

    SocketDispatcher() {
    }

    static {
        IOUtil.load();
    }

    @Override // sun.nio.ch.NativeDispatcher
    int read(FileDescriptor fileDescriptor, long j2, int i2) throws IOException {
        return read0(fileDescriptor, j2, i2);
    }

    @Override // sun.nio.ch.NativeDispatcher
    long readv(FileDescriptor fileDescriptor, long j2, int i2) throws IOException {
        return readv0(fileDescriptor, j2, i2);
    }

    @Override // sun.nio.ch.NativeDispatcher
    int write(FileDescriptor fileDescriptor, long j2, int i2) throws IOException {
        return write0(fileDescriptor, j2, i2);
    }

    @Override // sun.nio.ch.NativeDispatcher
    long writev(FileDescriptor fileDescriptor, long j2, int i2) throws IOException {
        return writev0(fileDescriptor, j2, i2);
    }

    @Override // sun.nio.ch.NativeDispatcher
    void preClose(FileDescriptor fileDescriptor) throws IOException {
        preClose0(fileDescriptor);
    }

    @Override // sun.nio.ch.NativeDispatcher
    void close(FileDescriptor fileDescriptor) throws IOException {
        close0(fileDescriptor);
    }
}
