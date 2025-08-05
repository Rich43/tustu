package sun.misc;

import java.io.FileDescriptor;

/* loaded from: rt.jar:sun/misc/JavaIOFileDescriptorAccess.class */
public interface JavaIOFileDescriptorAccess {
    void set(FileDescriptor fileDescriptor, int i2);

    int get(FileDescriptor fileDescriptor);

    void setHandle(FileDescriptor fileDescriptor, long j2);

    long getHandle(FileDescriptor fileDescriptor);
}
