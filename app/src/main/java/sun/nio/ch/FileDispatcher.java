package sun.nio.ch;

import java.io.FileDescriptor;
import java.io.IOException;
import java.nio.channels.SelectableChannel;

/* loaded from: rt.jar:sun/nio/ch/FileDispatcher.class */
abstract class FileDispatcher extends NativeDispatcher {
    public static final int NO_LOCK = -1;
    public static final int LOCKED = 0;
    public static final int RET_EX_LOCK = 1;
    public static final int INTERRUPTED = 2;

    abstract long seek(FileDescriptor fileDescriptor, long j2) throws IOException;

    abstract int force(FileDescriptor fileDescriptor, boolean z2) throws IOException;

    abstract int truncate(FileDescriptor fileDescriptor, long j2) throws IOException;

    abstract long size(FileDescriptor fileDescriptor) throws IOException;

    abstract int lock(FileDescriptor fileDescriptor, boolean z2, long j2, long j3, boolean z3) throws IOException;

    abstract void release(FileDescriptor fileDescriptor, long j2, long j3) throws IOException;

    abstract FileDescriptor duplicateForMapping(FileDescriptor fileDescriptor) throws IOException;

    abstract boolean canTransferToDirectly(SelectableChannel selectableChannel);

    abstract boolean transferToDirectlyNeedsPositionLock();

    FileDispatcher() {
    }
}
