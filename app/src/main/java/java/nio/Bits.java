package java.nio;

import java.security.AccessController;
import java.util.concurrent.atomic.AtomicLong;
import sun.misc.JavaLangRefAccess;
import sun.misc.JavaNioAccess;
import sun.misc.SharedSecrets;
import sun.misc.Unsafe;
import sun.misc.VM;
import sun.security.action.GetPropertyAction;

/* loaded from: rt.jar:java/nio/Bits.class */
class Bits {
    private static final Unsafe unsafe;
    private static final ByteOrder byteOrder;
    private static int pageSize;
    private static boolean unaligned;
    private static boolean unalignedKnown;
    private static volatile long maxMemory;
    private static final AtomicLong reservedMemory;
    private static final AtomicLong totalCapacity;
    private static final AtomicLong count;
    private static volatile boolean memoryLimitSet;
    private static final int MAX_SLEEPS = 9;
    static final int JNI_COPY_TO_ARRAY_THRESHOLD = 6;
    static final int JNI_COPY_FROM_ARRAY_THRESHOLD = 6;
    static final long UNSAFE_COPY_THRESHOLD = 1048576;
    static final /* synthetic */ boolean $assertionsDisabled;

    private static native void copySwapMemory0(Object obj, long j2, Object obj2, long j3, long j4, long j5);

    static {
        $assertionsDisabled = !Bits.class.desiredAssertionStatus();
        unsafe = Unsafe.getUnsafe();
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
                    byteOrder = null;
                    break;
            }
            unsafe.freeMemory(jAllocateMemory);
            pageSize = -1;
            unalignedKnown = false;
            maxMemory = VM.maxDirectMemory();
            reservedMemory = new AtomicLong();
            totalCapacity = new AtomicLong();
            count = new AtomicLong();
            memoryLimitSet = false;
            SharedSecrets.setJavaNioAccess(new JavaNioAccess() { // from class: java.nio.Bits.1
                @Override // sun.misc.JavaNioAccess
                public JavaNioAccess.BufferPool getDirectBufferPool() {
                    return new JavaNioAccess.BufferPool() { // from class: java.nio.Bits.1.1
                        @Override // sun.misc.JavaNioAccess.BufferPool
                        public String getName() {
                            return "direct";
                        }

                        @Override // sun.misc.JavaNioAccess.BufferPool
                        public long getCount() {
                            return Bits.count.get();
                        }

                        @Override // sun.misc.JavaNioAccess.BufferPool
                        public long getTotalCapacity() {
                            return Bits.totalCapacity.get();
                        }

                        @Override // sun.misc.JavaNioAccess.BufferPool
                        public long getMemoryUsed() {
                            return Bits.reservedMemory.get();
                        }
                    };
                }

                @Override // sun.misc.JavaNioAccess
                public ByteBuffer newDirectByteBuffer(long j2, int i2, Object obj) {
                    return new DirectByteBuffer(j2, i2, obj);
                }

                @Override // sun.misc.JavaNioAccess
                public void truncate(Buffer buffer) {
                    buffer.truncate();
                }
            });
        } catch (Throwable th) {
            unsafe.freeMemory(jAllocateMemory);
            throw th;
        }
    }

    private Bits() {
    }

    static short swap(short s2) {
        return Short.reverseBytes(s2);
    }

    static char swap(char c2) {
        return Character.reverseBytes(c2);
    }

    static int swap(int i2) {
        return Integer.reverseBytes(i2);
    }

    static long swap(long j2) {
        return Long.reverseBytes(j2);
    }

    private static char makeChar(byte b2, byte b3) {
        return (char) ((b2 << 8) | (b3 & 255));
    }

    static char getCharL(ByteBuffer byteBuffer, int i2) {
        return makeChar(byteBuffer._get(i2 + 1), byteBuffer._get(i2));
    }

    static char getCharL(long j2) {
        return makeChar(_get(j2 + 1), _get(j2));
    }

    static char getCharB(ByteBuffer byteBuffer, int i2) {
        return makeChar(byteBuffer._get(i2), byteBuffer._get(i2 + 1));
    }

    static char getCharB(long j2) {
        return makeChar(_get(j2), _get(j2 + 1));
    }

    static char getChar(ByteBuffer byteBuffer, int i2, boolean z2) {
        return z2 ? getCharB(byteBuffer, i2) : getCharL(byteBuffer, i2);
    }

    static char getChar(long j2, boolean z2) {
        return z2 ? getCharB(j2) : getCharL(j2);
    }

    private static byte char1(char c2) {
        return (byte) (c2 >> '\b');
    }

    private static byte char0(char c2) {
        return (byte) c2;
    }

    static void putCharL(ByteBuffer byteBuffer, int i2, char c2) {
        byteBuffer._put(i2, char0(c2));
        byteBuffer._put(i2 + 1, char1(c2));
    }

    static void putCharL(long j2, char c2) {
        _put(j2, char0(c2));
        _put(j2 + 1, char1(c2));
    }

    static void putCharB(ByteBuffer byteBuffer, int i2, char c2) {
        byteBuffer._put(i2, char1(c2));
        byteBuffer._put(i2 + 1, char0(c2));
    }

    static void putCharB(long j2, char c2) {
        _put(j2, char1(c2));
        _put(j2 + 1, char0(c2));
    }

    static void putChar(ByteBuffer byteBuffer, int i2, char c2, boolean z2) {
        if (z2) {
            putCharB(byteBuffer, i2, c2);
        } else {
            putCharL(byteBuffer, i2, c2);
        }
    }

    static void putChar(long j2, char c2, boolean z2) {
        if (z2) {
            putCharB(j2, c2);
        } else {
            putCharL(j2, c2);
        }
    }

    private static short makeShort(byte b2, byte b3) {
        return (short) ((b2 << 8) | (b3 & 255));
    }

    static short getShortL(ByteBuffer byteBuffer, int i2) {
        return makeShort(byteBuffer._get(i2 + 1), byteBuffer._get(i2));
    }

    static short getShortL(long j2) {
        return makeShort(_get(j2 + 1), _get(j2));
    }

    static short getShortB(ByteBuffer byteBuffer, int i2) {
        return makeShort(byteBuffer._get(i2), byteBuffer._get(i2 + 1));
    }

    static short getShortB(long j2) {
        return makeShort(_get(j2), _get(j2 + 1));
    }

    static short getShort(ByteBuffer byteBuffer, int i2, boolean z2) {
        return z2 ? getShortB(byteBuffer, i2) : getShortL(byteBuffer, i2);
    }

    static short getShort(long j2, boolean z2) {
        return z2 ? getShortB(j2) : getShortL(j2);
    }

    private static byte short1(short s2) {
        return (byte) (s2 >> 8);
    }

    private static byte short0(short s2) {
        return (byte) s2;
    }

    static void putShortL(ByteBuffer byteBuffer, int i2, short s2) {
        byteBuffer._put(i2, short0(s2));
        byteBuffer._put(i2 + 1, short1(s2));
    }

    static void putShortL(long j2, short s2) {
        _put(j2, short0(s2));
        _put(j2 + 1, short1(s2));
    }

    static void putShortB(ByteBuffer byteBuffer, int i2, short s2) {
        byteBuffer._put(i2, short1(s2));
        byteBuffer._put(i2 + 1, short0(s2));
    }

    static void putShortB(long j2, short s2) {
        _put(j2, short1(s2));
        _put(j2 + 1, short0(s2));
    }

    static void putShort(ByteBuffer byteBuffer, int i2, short s2, boolean z2) {
        if (z2) {
            putShortB(byteBuffer, i2, s2);
        } else {
            putShortL(byteBuffer, i2, s2);
        }
    }

    static void putShort(long j2, short s2, boolean z2) {
        if (z2) {
            putShortB(j2, s2);
        } else {
            putShortL(j2, s2);
        }
    }

    private static int makeInt(byte b2, byte b3, byte b4, byte b5) {
        return (b2 << 24) | ((b3 & 255) << 16) | ((b4 & 255) << 8) | (b5 & 255);
    }

    static int getIntL(ByteBuffer byteBuffer, int i2) {
        return makeInt(byteBuffer._get(i2 + 3), byteBuffer._get(i2 + 2), byteBuffer._get(i2 + 1), byteBuffer._get(i2));
    }

    static int getIntL(long j2) {
        return makeInt(_get(j2 + 3), _get(j2 + 2), _get(j2 + 1), _get(j2));
    }

    static int getIntB(ByteBuffer byteBuffer, int i2) {
        return makeInt(byteBuffer._get(i2), byteBuffer._get(i2 + 1), byteBuffer._get(i2 + 2), byteBuffer._get(i2 + 3));
    }

    static int getIntB(long j2) {
        return makeInt(_get(j2), _get(j2 + 1), _get(j2 + 2), _get(j2 + 3));
    }

    static int getInt(ByteBuffer byteBuffer, int i2, boolean z2) {
        return z2 ? getIntB(byteBuffer, i2) : getIntL(byteBuffer, i2);
    }

    static int getInt(long j2, boolean z2) {
        return z2 ? getIntB(j2) : getIntL(j2);
    }

    private static byte int3(int i2) {
        return (byte) (i2 >> 24);
    }

    private static byte int2(int i2) {
        return (byte) (i2 >> 16);
    }

    private static byte int1(int i2) {
        return (byte) (i2 >> 8);
    }

    private static byte int0(int i2) {
        return (byte) i2;
    }

    static void putIntL(ByteBuffer byteBuffer, int i2, int i3) {
        byteBuffer._put(i2 + 3, int3(i3));
        byteBuffer._put(i2 + 2, int2(i3));
        byteBuffer._put(i2 + 1, int1(i3));
        byteBuffer._put(i2, int0(i3));
    }

    static void putIntL(long j2, int i2) {
        _put(j2 + 3, int3(i2));
        _put(j2 + 2, int2(i2));
        _put(j2 + 1, int1(i2));
        _put(j2, int0(i2));
    }

    static void putIntB(ByteBuffer byteBuffer, int i2, int i3) {
        byteBuffer._put(i2, int3(i3));
        byteBuffer._put(i2 + 1, int2(i3));
        byteBuffer._put(i2 + 2, int1(i3));
        byteBuffer._put(i2 + 3, int0(i3));
    }

    static void putIntB(long j2, int i2) {
        _put(j2, int3(i2));
        _put(j2 + 1, int2(i2));
        _put(j2 + 2, int1(i2));
        _put(j2 + 3, int0(i2));
    }

    static void putInt(ByteBuffer byteBuffer, int i2, int i3, boolean z2) {
        if (z2) {
            putIntB(byteBuffer, i2, i3);
        } else {
            putIntL(byteBuffer, i2, i3);
        }
    }

    static void putInt(long j2, int i2, boolean z2) {
        if (z2) {
            putIntB(j2, i2);
        } else {
            putIntL(j2, i2);
        }
    }

    private static long makeLong(byte b2, byte b3, byte b4, byte b5, byte b6, byte b7, byte b8, byte b9) {
        return (b2 << 56) | ((b3 & 255) << 48) | ((b4 & 255) << 40) | ((b5 & 255) << 32) | ((b6 & 255) << 24) | ((b7 & 255) << 16) | ((b8 & 255) << 8) | (b9 & 255);
    }

    static long getLongL(ByteBuffer byteBuffer, int i2) {
        return makeLong(byteBuffer._get(i2 + 7), byteBuffer._get(i2 + 6), byteBuffer._get(i2 + 5), byteBuffer._get(i2 + 4), byteBuffer._get(i2 + 3), byteBuffer._get(i2 + 2), byteBuffer._get(i2 + 1), byteBuffer._get(i2));
    }

    static long getLongL(long j2) {
        return makeLong(_get(j2 + 7), _get(j2 + 6), _get(j2 + 5), _get(j2 + 4), _get(j2 + 3), _get(j2 + 2), _get(j2 + 1), _get(j2));
    }

    static long getLongB(ByteBuffer byteBuffer, int i2) {
        return makeLong(byteBuffer._get(i2), byteBuffer._get(i2 + 1), byteBuffer._get(i2 + 2), byteBuffer._get(i2 + 3), byteBuffer._get(i2 + 4), byteBuffer._get(i2 + 5), byteBuffer._get(i2 + 6), byteBuffer._get(i2 + 7));
    }

    static long getLongB(long j2) {
        return makeLong(_get(j2), _get(j2 + 1), _get(j2 + 2), _get(j2 + 3), _get(j2 + 4), _get(j2 + 5), _get(j2 + 6), _get(j2 + 7));
    }

    static long getLong(ByteBuffer byteBuffer, int i2, boolean z2) {
        return z2 ? getLongB(byteBuffer, i2) : getLongL(byteBuffer, i2);
    }

    static long getLong(long j2, boolean z2) {
        return z2 ? getLongB(j2) : getLongL(j2);
    }

    private static byte long7(long j2) {
        return (byte) (j2 >> 56);
    }

    private static byte long6(long j2) {
        return (byte) (j2 >> 48);
    }

    private static byte long5(long j2) {
        return (byte) (j2 >> 40);
    }

    private static byte long4(long j2) {
        return (byte) (j2 >> 32);
    }

    private static byte long3(long j2) {
        return (byte) (j2 >> 24);
    }

    private static byte long2(long j2) {
        return (byte) (j2 >> 16);
    }

    private static byte long1(long j2) {
        return (byte) (j2 >> 8);
    }

    private static byte long0(long j2) {
        return (byte) j2;
    }

    static void putLongL(ByteBuffer byteBuffer, int i2, long j2) {
        byteBuffer._put(i2 + 7, long7(j2));
        byteBuffer._put(i2 + 6, long6(j2));
        byteBuffer._put(i2 + 5, long5(j2));
        byteBuffer._put(i2 + 4, long4(j2));
        byteBuffer._put(i2 + 3, long3(j2));
        byteBuffer._put(i2 + 2, long2(j2));
        byteBuffer._put(i2 + 1, long1(j2));
        byteBuffer._put(i2, long0(j2));
    }

    static void putLongL(long j2, long j3) {
        _put(j2 + 7, long7(j3));
        _put(j2 + 6, long6(j3));
        _put(j2 + 5, long5(j3));
        _put(j2 + 4, long4(j3));
        _put(j2 + 3, long3(j3));
        _put(j2 + 2, long2(j3));
        _put(j2 + 1, long1(j3));
        _put(j2, long0(j3));
    }

    static void putLongB(ByteBuffer byteBuffer, int i2, long j2) {
        byteBuffer._put(i2, long7(j2));
        byteBuffer._put(i2 + 1, long6(j2));
        byteBuffer._put(i2 + 2, long5(j2));
        byteBuffer._put(i2 + 3, long4(j2));
        byteBuffer._put(i2 + 4, long3(j2));
        byteBuffer._put(i2 + 5, long2(j2));
        byteBuffer._put(i2 + 6, long1(j2));
        byteBuffer._put(i2 + 7, long0(j2));
    }

    static void putLongB(long j2, long j3) {
        _put(j2, long7(j3));
        _put(j2 + 1, long6(j3));
        _put(j2 + 2, long5(j3));
        _put(j2 + 3, long4(j3));
        _put(j2 + 4, long3(j3));
        _put(j2 + 5, long2(j3));
        _put(j2 + 6, long1(j3));
        _put(j2 + 7, long0(j3));
    }

    static void putLong(ByteBuffer byteBuffer, int i2, long j2, boolean z2) {
        if (z2) {
            putLongB(byteBuffer, i2, j2);
        } else {
            putLongL(byteBuffer, i2, j2);
        }
    }

    static void putLong(long j2, long j3, boolean z2) {
        if (z2) {
            putLongB(j2, j3);
        } else {
            putLongL(j2, j3);
        }
    }

    static float getFloatL(ByteBuffer byteBuffer, int i2) {
        return Float.intBitsToFloat(getIntL(byteBuffer, i2));
    }

    static float getFloatL(long j2) {
        return Float.intBitsToFloat(getIntL(j2));
    }

    static float getFloatB(ByteBuffer byteBuffer, int i2) {
        return Float.intBitsToFloat(getIntB(byteBuffer, i2));
    }

    static float getFloatB(long j2) {
        return Float.intBitsToFloat(getIntB(j2));
    }

    static float getFloat(ByteBuffer byteBuffer, int i2, boolean z2) {
        return z2 ? getFloatB(byteBuffer, i2) : getFloatL(byteBuffer, i2);
    }

    static float getFloat(long j2, boolean z2) {
        return z2 ? getFloatB(j2) : getFloatL(j2);
    }

    static void putFloatL(ByteBuffer byteBuffer, int i2, float f2) {
        putIntL(byteBuffer, i2, Float.floatToRawIntBits(f2));
    }

    static void putFloatL(long j2, float f2) {
        putIntL(j2, Float.floatToRawIntBits(f2));
    }

    static void putFloatB(ByteBuffer byteBuffer, int i2, float f2) {
        putIntB(byteBuffer, i2, Float.floatToRawIntBits(f2));
    }

    static void putFloatB(long j2, float f2) {
        putIntB(j2, Float.floatToRawIntBits(f2));
    }

    static void putFloat(ByteBuffer byteBuffer, int i2, float f2, boolean z2) {
        if (z2) {
            putFloatB(byteBuffer, i2, f2);
        } else {
            putFloatL(byteBuffer, i2, f2);
        }
    }

    static void putFloat(long j2, float f2, boolean z2) {
        if (z2) {
            putFloatB(j2, f2);
        } else {
            putFloatL(j2, f2);
        }
    }

    static double getDoubleL(ByteBuffer byteBuffer, int i2) {
        return Double.longBitsToDouble(getLongL(byteBuffer, i2));
    }

    static double getDoubleL(long j2) {
        return Double.longBitsToDouble(getLongL(j2));
    }

    static double getDoubleB(ByteBuffer byteBuffer, int i2) {
        return Double.longBitsToDouble(getLongB(byteBuffer, i2));
    }

    static double getDoubleB(long j2) {
        return Double.longBitsToDouble(getLongB(j2));
    }

    static double getDouble(ByteBuffer byteBuffer, int i2, boolean z2) {
        return z2 ? getDoubleB(byteBuffer, i2) : getDoubleL(byteBuffer, i2);
    }

    static double getDouble(long j2, boolean z2) {
        return z2 ? getDoubleB(j2) : getDoubleL(j2);
    }

    static void putDoubleL(ByteBuffer byteBuffer, int i2, double d2) {
        putLongL(byteBuffer, i2, Double.doubleToRawLongBits(d2));
    }

    static void putDoubleL(long j2, double d2) {
        putLongL(j2, Double.doubleToRawLongBits(d2));
    }

    static void putDoubleB(ByteBuffer byteBuffer, int i2, double d2) {
        putLongB(byteBuffer, i2, Double.doubleToRawLongBits(d2));
    }

    static void putDoubleB(long j2, double d2) {
        putLongB(j2, Double.doubleToRawLongBits(d2));
    }

    static void putDouble(ByteBuffer byteBuffer, int i2, double d2, boolean z2) {
        if (z2) {
            putDoubleB(byteBuffer, i2, d2);
        } else {
            putDoubleL(byteBuffer, i2, d2);
        }
    }

    static void putDouble(long j2, double d2, boolean z2) {
        if (z2) {
            putDoubleB(j2, d2);
        } else {
            putDoubleL(j2, d2);
        }
    }

    private static byte _get(long j2) {
        return unsafe.getByte(j2);
    }

    private static void _put(long j2, byte b2) {
        unsafe.putByte(j2, b2);
    }

    static Unsafe unsafe() {
        return unsafe;
    }

    static ByteOrder byteOrder() {
        if (byteOrder == null) {
            throw new Error("Unknown byte order");
        }
        return byteOrder;
    }

    static int pageSize() {
        if (pageSize == -1) {
            pageSize = unsafe().pageSize();
        }
        return pageSize;
    }

    static int pageCount(long j2) {
        return ((int) ((j2 + pageSize()) - 1)) / pageSize();
    }

    static boolean unaligned() {
        if (unalignedKnown) {
            return unaligned;
        }
        String str = (String) AccessController.doPrivileged(new GetPropertyAction("os.arch"));
        unaligned = str.equals("i386") || str.equals("x86") || str.equals("amd64") || str.equals("x86_64") || str.equals("ppc64") || str.equals("ppc64le") || str.equals("aarch64");
        unalignedKnown = true;
        return unaligned;
    }

    static void reserveMemory(long j2, int i2) {
        if (!memoryLimitSet && VM.isBooted()) {
            maxMemory = VM.maxDirectMemory();
            memoryLimitSet = true;
        }
        if (tryReserveMemory(j2, i2)) {
            return;
        }
        JavaLangRefAccess javaLangRefAccess = SharedSecrets.getJavaLangRefAccess();
        while (javaLangRefAccess.tryHandlePendingReference()) {
            if (tryReserveMemory(j2, i2)) {
                return;
            }
        }
        System.gc();
        boolean z2 = false;
        long j3 = 1;
        int i3 = 0;
        while (!tryReserveMemory(j2, i2)) {
            try {
                if (i3 < 9) {
                    if (!javaLangRefAccess.tryHandlePendingReference()) {
                        try {
                            Thread.sleep(j3);
                            j3 <<= 1;
                            i3++;
                        } catch (InterruptedException e2) {
                            z2 = true;
                        }
                    }
                } else {
                    throw new OutOfMemoryError("Direct buffer memory");
                }
            } finally {
                if (z2) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    }

    private static boolean tryReserveMemory(long j2, int i2) {
        long j3;
        do {
            long j4 = i2;
            long j5 = maxMemory;
            j3 = totalCapacity.get();
            if (j4 > j5 - j3) {
                return false;
            }
        } while (!totalCapacity.compareAndSet(j3, j3 + i2));
        reservedMemory.addAndGet(j2);
        count.incrementAndGet();
        return true;
    }

    static void unreserveMemory(long j2, int i2) {
        long jDecrementAndGet = count.decrementAndGet();
        long jAddAndGet = reservedMemory.addAndGet(-j2);
        long jAddAndGet2 = totalCapacity.addAndGet(-i2);
        if ($assertionsDisabled) {
            return;
        }
        if (jDecrementAndGet < 0 || jAddAndGet < 0 || jAddAndGet2 < 0) {
            throw new AssertionError();
        }
    }

    static void copyFromArray(Object obj, long j2, long j3, long j4, long j5) {
        long j6 = j2 + j3;
        while (j5 > 0) {
            long j7 = j5 > 1048576 ? 1048576L : j5;
            unsafe.copyMemory(obj, j6, null, j4, j7);
            j5 -= j7;
            j6 += j7;
            j4 += j7;
        }
    }

    static void copyToArray(long j2, Object obj, long j3, long j4, long j5) {
        long j6 = j3;
        long j7 = j4;
        while (true) {
            long j8 = j6 + j7;
            if (j5 > 0) {
                long j9 = j5 > 1048576 ? 1048576L : j5;
                unsafe.copyMemory(null, j2, obj, j8, j9);
                j5 -= j9;
                j2 += j9;
                j6 = j8;
                j7 = j9;
            } else {
                return;
            }
        }
    }

    static void copyFromCharArray(Object obj, long j2, long j3, long j4) {
        copySwapMemory(obj, unsafe.arrayBaseOffset(obj.getClass()) + j2, null, j3, j4, 2L);
    }

    static void copyToCharArray(long j2, Object obj, long j3, long j4) {
        copySwapMemory(null, j2, obj, unsafe.arrayBaseOffset(obj.getClass()) + j3, j4, 2L);
    }

    static void copyFromShortArray(Object obj, long j2, long j3, long j4) {
        copySwapMemory(obj, unsafe.arrayBaseOffset(obj.getClass()) + j2, null, j3, j4, 2L);
    }

    static void copyToShortArray(long j2, Object obj, long j3, long j4) {
        copySwapMemory(null, j2, obj, unsafe.arrayBaseOffset(obj.getClass()) + j3, j4, 2L);
    }

    static void copyFromIntArray(Object obj, long j2, long j3, long j4) {
        copySwapMemory(obj, unsafe.arrayBaseOffset(obj.getClass()) + j2, null, j3, j4, 4L);
    }

    static void copyToIntArray(long j2, Object obj, long j3, long j4) {
        copySwapMemory(null, j2, obj, unsafe.arrayBaseOffset(obj.getClass()) + j3, j4, 4L);
    }

    static void copyFromLongArray(Object obj, long j2, long j3, long j4) {
        copySwapMemory(obj, unsafe.arrayBaseOffset(obj.getClass()) + j2, null, j3, j4, 8L);
    }

    static void copyToLongArray(long j2, Object obj, long j3, long j4) {
        copySwapMemory(null, j2, obj, unsafe.arrayBaseOffset(obj.getClass()) + j3, j4, 8L);
    }

    private static boolean isPrimitiveArray(Class<?> cls) {
        Class<?> componentType = cls.getComponentType();
        return componentType != null && componentType.isPrimitive();
    }

    private static void copySwapMemory(Object obj, long j2, Object obj2, long j3, long j4, long j5) {
        if (j4 < 0) {
            throw new IllegalArgumentException();
        }
        if (j5 != 2 && j5 != 4 && j5 != 8) {
            throw new IllegalArgumentException();
        }
        if (j4 % j5 != 0) {
            throw new IllegalArgumentException();
        }
        if ((obj == null && j2 == 0) || (obj2 == null && j3 == 0)) {
            throw new NullPointerException();
        }
        if (obj != null && (j2 < 0 || !isPrimitiveArray(obj.getClass()))) {
            throw new IllegalArgumentException();
        }
        if (obj2 != null && (j3 < 0 || !isPrimitiveArray(obj2.getClass()))) {
            throw new IllegalArgumentException();
        }
        if (unsafe.addressSize() == 4 && ((j4 >>> 32) != 0 || (j2 >>> 32) != 0 || (j3 >>> 32) != 0)) {
            throw new IllegalArgumentException();
        }
        if (j4 == 0) {
            return;
        }
        copySwapMemory0(obj, j2, obj2, j3, j4, j5);
    }
}
