package sun.java2d.pipe;

import sun.misc.Unsafe;

/* loaded from: rt.jar:sun/java2d/pipe/RenderBuffer.class */
public class RenderBuffer {
    protected static final long SIZEOF_BYTE = 1;
    protected static final long SIZEOF_SHORT = 2;
    protected static final long SIZEOF_INT = 4;
    protected static final long SIZEOF_FLOAT = 4;
    protected static final long SIZEOF_LONG = 8;
    protected static final long SIZEOF_DOUBLE = 8;
    private static final int COPY_FROM_ARRAY_THRESHOLD = 6;
    protected final Unsafe unsafe = Unsafe.getUnsafe();
    protected final long baseAddress;
    protected final long endAddress;
    protected long curAddress;
    protected final int capacity;

    protected RenderBuffer(int i2) {
        long jAllocateMemory = this.unsafe.allocateMemory(i2);
        this.baseAddress = jAllocateMemory;
        this.curAddress = jAllocateMemory;
        this.endAddress = this.baseAddress + i2;
        this.capacity = i2;
    }

    public static RenderBuffer allocate(int i2) {
        return new RenderBuffer(i2);
    }

    public final long getAddress() {
        return this.baseAddress;
    }

    public final int capacity() {
        return this.capacity;
    }

    public final int remaining() {
        return (int) (this.endAddress - this.curAddress);
    }

    public final int position() {
        return (int) (this.curAddress - this.baseAddress);
    }

    public final void position(long j2) {
        this.curAddress = this.baseAddress + j2;
    }

    public final void clear() {
        this.curAddress = this.baseAddress;
    }

    public final RenderBuffer skip(long j2) {
        this.curAddress += j2;
        return this;
    }

    public final RenderBuffer putByte(byte b2) {
        this.unsafe.putByte(this.curAddress, b2);
        this.curAddress++;
        return this;
    }

    public RenderBuffer put(byte[] bArr) {
        return put(bArr, 0, bArr.length);
    }

    public RenderBuffer put(byte[] bArr, int i2, int i3) {
        if (i3 > 6) {
            long j2 = i3 * 1;
            this.unsafe.copyMemory(bArr, (i2 * 1) + Unsafe.ARRAY_BYTE_BASE_OFFSET, null, this.curAddress, j2);
            position(position() + j2);
        } else {
            int i4 = i2 + i3;
            for (int i5 = i2; i5 < i4; i5++) {
                putByte(bArr[i5]);
            }
        }
        return this;
    }

    public final RenderBuffer putShort(short s2) {
        this.unsafe.putShort(this.curAddress, s2);
        this.curAddress += 2;
        return this;
    }

    public RenderBuffer put(short[] sArr) {
        return put(sArr, 0, sArr.length);
    }

    public RenderBuffer put(short[] sArr, int i2, int i3) {
        if (i3 > 6) {
            long j2 = i3 * 2;
            this.unsafe.copyMemory(sArr, (i2 * 2) + Unsafe.ARRAY_SHORT_BASE_OFFSET, null, this.curAddress, j2);
            position(position() + j2);
        } else {
            int i4 = i2 + i3;
            for (int i5 = i2; i5 < i4; i5++) {
                putShort(sArr[i5]);
            }
        }
        return this;
    }

    public final RenderBuffer putInt(int i2, int i3) {
        this.unsafe.putInt(this.baseAddress + i2, i3);
        return this;
    }

    public final RenderBuffer putInt(int i2) {
        this.unsafe.putInt(this.curAddress, i2);
        this.curAddress += 4;
        return this;
    }

    public RenderBuffer put(int[] iArr) {
        return put(iArr, 0, iArr.length);
    }

    public RenderBuffer put(int[] iArr, int i2, int i3) {
        if (i3 > 6) {
            long j2 = i3 * 4;
            this.unsafe.copyMemory(iArr, (i2 * 4) + Unsafe.ARRAY_INT_BASE_OFFSET, null, this.curAddress, j2);
            position(position() + j2);
        } else {
            int i4 = i2 + i3;
            for (int i5 = i2; i5 < i4; i5++) {
                putInt(iArr[i5]);
            }
        }
        return this;
    }

    public final RenderBuffer putFloat(float f2) {
        this.unsafe.putFloat(this.curAddress, f2);
        this.curAddress += 4;
        return this;
    }

    public RenderBuffer put(float[] fArr) {
        return put(fArr, 0, fArr.length);
    }

    public RenderBuffer put(float[] fArr, int i2, int i3) {
        if (i3 > 6) {
            long j2 = i3 * 4;
            this.unsafe.copyMemory(fArr, (i2 * 4) + Unsafe.ARRAY_FLOAT_BASE_OFFSET, null, this.curAddress, j2);
            position(position() + j2);
        } else {
            int i4 = i2 + i3;
            for (int i5 = i2; i5 < i4; i5++) {
                putFloat(fArr[i5]);
            }
        }
        return this;
    }

    public final RenderBuffer putLong(long j2) {
        this.unsafe.putLong(this.curAddress, j2);
        this.curAddress += 8;
        return this;
    }

    public RenderBuffer put(long[] jArr) {
        return put(jArr, 0, jArr.length);
    }

    public RenderBuffer put(long[] jArr, int i2, int i3) {
        if (i3 > 6) {
            long j2 = i3 * 8;
            this.unsafe.copyMemory(jArr, (i2 * 8) + Unsafe.ARRAY_LONG_BASE_OFFSET, null, this.curAddress, j2);
            position(position() + j2);
        } else {
            int i4 = i2 + i3;
            for (int i5 = i2; i5 < i4; i5++) {
                putLong(jArr[i5]);
            }
        }
        return this;
    }

    public final RenderBuffer putDouble(double d2) {
        this.unsafe.putDouble(this.curAddress, d2);
        this.curAddress += 8;
        return this;
    }
}
