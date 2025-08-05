package java.nio;

import java.io.FileDescriptor;
import sun.misc.Unsafe;

/* loaded from: rt.jar:java/nio/MappedByteBuffer.class */
public abstract class MappedByteBuffer extends ByteBuffer {
    private final FileDescriptor fd;
    private static byte unused;

    private native boolean isLoaded0(long j2, long j3, int i2);

    private native void load0(long j2, long j3);

    private native void force0(FileDescriptor fileDescriptor, long j2, long j3);

    MappedByteBuffer(int i2, int i3, int i4, int i5, FileDescriptor fileDescriptor) {
        super(i2, i3, i4, i5);
        this.fd = fileDescriptor;
    }

    MappedByteBuffer(int i2, int i3, int i4, int i5) {
        super(i2, i3, i4, i5);
        this.fd = null;
    }

    private void checkMapped() {
        if (this.fd == null) {
            throw new UnsupportedOperationException();
        }
    }

    private long mappingOffset() {
        int iPageSize = Bits.pageSize();
        long j2 = this.address % iPageSize;
        return j2 >= 0 ? j2 : iPageSize + j2;
    }

    private long mappingAddress(long j2) {
        return this.address - j2;
    }

    private long mappingLength(long j2) {
        return capacity() + j2;
    }

    public final boolean isLoaded() {
        checkMapped();
        if (this.address == 0 || capacity() == 0) {
            return true;
        }
        long jMappingOffset = mappingOffset();
        long jMappingLength = mappingLength(jMappingOffset);
        return isLoaded0(mappingAddress(jMappingOffset), jMappingLength, Bits.pageCount(jMappingLength));
    }

    public final MappedByteBuffer load() {
        checkMapped();
        if (this.address == 0 || capacity() == 0) {
            return this;
        }
        long jMappingOffset = mappingOffset();
        long jMappingLength = mappingLength(jMappingOffset);
        load0(mappingAddress(jMappingOffset), jMappingLength);
        Unsafe unsafe = Unsafe.getUnsafe();
        int iPageSize = Bits.pageSize();
        int iPageCount = Bits.pageCount(jMappingLength);
        long jMappingAddress = mappingAddress(jMappingOffset);
        byte b2 = 0;
        for (int i2 = 0; i2 < iPageCount; i2++) {
            b2 = (byte) (b2 ^ unsafe.getByte(jMappingAddress));
            jMappingAddress += iPageSize;
        }
        if (unused != 0) {
            unused = b2;
        }
        return this;
    }

    public final MappedByteBuffer force() {
        checkMapped();
        if (this.address != 0 && capacity() != 0) {
            long jMappingOffset = mappingOffset();
            force0(this.fd, mappingAddress(jMappingOffset), mappingLength(jMappingOffset));
        }
        return this;
    }
}
