package sun.nio.ch;

import java.nio.ByteOrder;
import sun.misc.Unsafe;

/* loaded from: rt.jar:sun/nio/ch/NativeObject.class */
class NativeObject {
    protected static final Unsafe unsafe;
    protected long allocationAddress;
    private final long address;
    private static ByteOrder byteOrder;
    private static int pageSize;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !NativeObject.class.desiredAssertionStatus();
        unsafe = Unsafe.getUnsafe();
        byteOrder = null;
        pageSize = -1;
    }

    NativeObject(long j2) {
        this.allocationAddress = j2;
        this.address = j2;
    }

    NativeObject(long j2, long j3) {
        this.allocationAddress = j2;
        this.address = j2 + j3;
    }

    protected NativeObject(int i2, boolean z2) {
        if (!z2) {
            this.allocationAddress = unsafe.allocateMemory(i2);
            this.address = this.allocationAddress;
        } else {
            int iPageSize = pageSize();
            long jAllocateMemory = unsafe.allocateMemory(i2 + iPageSize);
            this.allocationAddress = jAllocateMemory;
            this.address = (jAllocateMemory + iPageSize) - (jAllocateMemory & (iPageSize - 1));
        }
    }

    long address() {
        return this.address;
    }

    long allocationAddress() {
        return this.allocationAddress;
    }

    NativeObject subObject(int i2) {
        return new NativeObject(i2 + this.address);
    }

    NativeObject getObject(int i2) {
        long j2;
        switch (addressSize()) {
            case 4:
                j2 = unsafe.getInt(i2 + this.address) & (-1);
                break;
            case 8:
                j2 = unsafe.getLong(i2 + this.address);
                break;
            default:
                throw new InternalError("Address size not supported");
        }
        return new NativeObject(j2);
    }

    void putObject(int i2, NativeObject nativeObject) {
        switch (addressSize()) {
            case 4:
                putInt(i2, (int) (nativeObject.address & (-1)));
                return;
            case 8:
                putLong(i2, nativeObject.address);
                return;
            default:
                throw new InternalError("Address size not supported");
        }
    }

    final byte getByte(int i2) {
        return unsafe.getByte(i2 + this.address);
    }

    final void putByte(int i2, byte b2) {
        unsafe.putByte(i2 + this.address, b2);
    }

    final short getShort(int i2) {
        return unsafe.getShort(i2 + this.address);
    }

    final void putShort(int i2, short s2) {
        unsafe.putShort(i2 + this.address, s2);
    }

    final char getChar(int i2) {
        return unsafe.getChar(i2 + this.address);
    }

    final void putChar(int i2, char c2) {
        unsafe.putChar(i2 + this.address, c2);
    }

    final int getInt(int i2) {
        return unsafe.getInt(i2 + this.address);
    }

    final void putInt(int i2, int i3) {
        unsafe.putInt(i2 + this.address, i3);
    }

    final long getLong(int i2) {
        return unsafe.getLong(i2 + this.address);
    }

    final void putLong(int i2, long j2) {
        unsafe.putLong(i2 + this.address, j2);
    }

    final float getFloat(int i2) {
        return unsafe.getFloat(i2 + this.address);
    }

    final void putFloat(int i2, float f2) {
        unsafe.putFloat(i2 + this.address, f2);
    }

    final double getDouble(int i2) {
        return unsafe.getDouble(i2 + this.address);
    }

    final void putDouble(int i2, double d2) {
        unsafe.putDouble(i2 + this.address, d2);
    }

    static int addressSize() {
        return unsafe.addressSize();
    }

    static ByteOrder byteOrder() {
        if (byteOrder != null) {
            return byteOrder;
        }
        long jAllocateMemory = unsafe.allocateMemory(8L);
        try {
            unsafe.putLong(jAllocateMemory, 72623859790382856L);
            switch (unsafe.getByte(jAllocateMemory)) {
                case 1:
                    byteOrder = ByteOrder.BIG_ENDIAN;
                    break;
                case 8:
                    byteOrder = ByteOrder.LITTLE_ENDIAN;
                    break;
                default:
                    if (!$assertionsDisabled) {
                        throw new AssertionError();
                    }
                    break;
            }
            unsafe.freeMemory(jAllocateMemory);
            return byteOrder;
        } catch (Throwable th) {
            unsafe.freeMemory(jAllocateMemory);
            throw th;
        }
    }

    static int pageSize() {
        if (pageSize == -1) {
            pageSize = unsafe.pageSize();
        }
        return pageSize;
    }
}
