package sun.nio.ch;

import java.io.FileDescriptor;
import java.io.IOException;
import java.nio.channels.SelectableChannel;
import java.security.AccessController;
import java.security.PrivilegedAction;
import sun.misc.JavaIOFileDescriptorAccess;
import sun.misc.SharedSecrets;

/* loaded from: rt.jar:sun/nio/ch/FileDispatcherImpl.class */
class FileDispatcherImpl extends FileDispatcher {
    private static final boolean fastFileTransfer;
    private final boolean append;

    static native int read0(FileDescriptor fileDescriptor, long j2, int i2) throws IOException;

    static native int pread0(FileDescriptor fileDescriptor, long j2, int i2, long j3) throws IOException;

    static native long readv0(FileDescriptor fileDescriptor, long j2, int i2) throws IOException;

    static native int write0(FileDescriptor fileDescriptor, long j2, int i2, boolean z2) throws IOException;

    static native int pwrite0(FileDescriptor fileDescriptor, long j2, int i2, long j3) throws IOException;

    static native long writev0(FileDescriptor fileDescriptor, long j2, int i2, boolean z2) throws IOException;

    static native long seek0(FileDescriptor fileDescriptor, long j2) throws IOException;

    static native int force0(FileDescriptor fileDescriptor, boolean z2) throws IOException;

    static native int truncate0(FileDescriptor fileDescriptor, long j2) throws IOException;

    static native long size0(FileDescriptor fileDescriptor) throws IOException;

    static native int lock0(FileDescriptor fileDescriptor, boolean z2, long j2, long j3, boolean z3) throws IOException;

    static native void release0(FileDescriptor fileDescriptor, long j2, long j3) throws IOException;

    static native void close0(FileDescriptor fileDescriptor) throws IOException;

    static native void closeByHandle(long j2) throws IOException;

    static native long duplicateHandle(long j2) throws IOException;

    FileDispatcherImpl(boolean z2) {
        this.append = z2;
    }

    FileDispatcherImpl() {
        this(false);
    }

    @Override // sun.nio.ch.NativeDispatcher
    boolean needsPositionLock() {
        return true;
    }

    @Override // sun.nio.ch.NativeDispatcher
    int read(FileDescriptor fileDescriptor, long j2, int i2) throws IOException {
        return read0(fileDescriptor, j2, i2);
    }

    @Override // sun.nio.ch.NativeDispatcher
    int pread(FileDescriptor fileDescriptor, long j2, int i2, long j3) throws IOException {
        return pread0(fileDescriptor, j2, i2, j3);
    }

    @Override // sun.nio.ch.NativeDispatcher
    long readv(FileDescriptor fileDescriptor, long j2, int i2) throws IOException {
        return readv0(fileDescriptor, j2, i2);
    }

    @Override // sun.nio.ch.NativeDispatcher
    int write(FileDescriptor fileDescriptor, long j2, int i2) throws IOException {
        return write0(fileDescriptor, j2, i2, this.append);
    }

    @Override // sun.nio.ch.NativeDispatcher
    int pwrite(FileDescriptor fileDescriptor, long j2, int i2, long j3) throws IOException {
        return pwrite0(fileDescriptor, j2, i2, j3);
    }

    @Override // sun.nio.ch.NativeDispatcher
    long writev(FileDescriptor fileDescriptor, long j2, int i2) throws IOException {
        return writev0(fileDescriptor, j2, i2, this.append);
    }

    @Override // sun.nio.ch.FileDispatcher
    long seek(FileDescriptor fileDescriptor, long j2) throws IOException {
        return seek0(fileDescriptor, j2);
    }

    @Override // sun.nio.ch.FileDispatcher
    int force(FileDescriptor fileDescriptor, boolean z2) throws IOException {
        return force0(fileDescriptor, z2);
    }

    @Override // sun.nio.ch.FileDispatcher
    int truncate(FileDescriptor fileDescriptor, long j2) throws IOException {
        return truncate0(fileDescriptor, j2);
    }

    @Override // sun.nio.ch.FileDispatcher
    long size(FileDescriptor fileDescriptor) throws IOException {
        return size0(fileDescriptor);
    }

    @Override // sun.nio.ch.FileDispatcher
    int lock(FileDescriptor fileDescriptor, boolean z2, long j2, long j3, boolean z3) throws IOException {
        return lock0(fileDescriptor, z2, j2, j3, z3);
    }

    @Override // sun.nio.ch.FileDispatcher
    void release(FileDescriptor fileDescriptor, long j2, long j3) throws IOException {
        release0(fileDescriptor, j2, j3);
    }

    @Override // sun.nio.ch.NativeDispatcher
    void close(FileDescriptor fileDescriptor) throws IOException {
        close0(fileDescriptor);
    }

    @Override // sun.nio.ch.FileDispatcher
    FileDescriptor duplicateForMapping(FileDescriptor fileDescriptor) throws IOException {
        JavaIOFileDescriptorAccess javaIOFileDescriptorAccess = SharedSecrets.getJavaIOFileDescriptorAccess();
        FileDescriptor fileDescriptor2 = new FileDescriptor();
        javaIOFileDescriptorAccess.setHandle(fileDescriptor2, duplicateHandle(javaIOFileDescriptorAccess.getHandle(fileDescriptor)));
        return fileDescriptor2;
    }

    @Override // sun.nio.ch.FileDispatcher
    boolean canTransferToDirectly(SelectableChannel selectableChannel) {
        return fastFileTransfer && selectableChannel.isBlocking();
    }

    @Override // sun.nio.ch.FileDispatcher
    boolean transferToDirectlyNeedsPositionLock() {
        return true;
    }

    static boolean isFastFileTransferRequested() {
        boolean z2;
        String str = (String) AccessController.doPrivileged(new PrivilegedAction<String>() { // from class: sun.nio.ch.FileDispatcherImpl.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.security.PrivilegedAction
            /* renamed from: run */
            public String run2() {
                return System.getProperty("jdk.nio.enableFastFileTransfer");
            }
        });
        if ("".equals(str)) {
            z2 = true;
        } else {
            z2 = Boolean.parseBoolean(str);
        }
        return z2;
    }

    static {
        IOUtil.load();
        fastFileTransfer = isFastFileTransferRequested();
    }
}
