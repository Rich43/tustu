package java.util.concurrent.locks;

import java.util.concurrent.ThreadLocalRandom;
import sun.misc.Unsafe;

/* loaded from: rt.jar:java/util/concurrent/locks/LockSupport.class */
public class LockSupport {
    private static final Unsafe UNSAFE;
    private static final long parkBlockerOffset;
    private static final long SEED;
    private static final long PROBE;
    private static final long SECONDARY;

    private LockSupport() {
    }

    private static void setBlocker(Thread thread, Object obj) {
        UNSAFE.putObject(thread, parkBlockerOffset, obj);
    }

    public static void unpark(Thread thread) {
        if (thread != null) {
            UNSAFE.unpark(thread);
        }
    }

    public static void park(Object obj) {
        Thread threadCurrentThread = Thread.currentThread();
        setBlocker(threadCurrentThread, obj);
        UNSAFE.park(false, 0L);
        setBlocker(threadCurrentThread, null);
    }

    public static void parkNanos(Object obj, long j2) {
        if (j2 > 0) {
            Thread threadCurrentThread = Thread.currentThread();
            setBlocker(threadCurrentThread, obj);
            UNSAFE.park(false, j2);
            setBlocker(threadCurrentThread, null);
        }
    }

    public static void parkUntil(Object obj, long j2) {
        Thread threadCurrentThread = Thread.currentThread();
        setBlocker(threadCurrentThread, obj);
        UNSAFE.park(true, j2);
        setBlocker(threadCurrentThread, null);
    }

    public static Object getBlocker(Thread thread) {
        if (thread == null) {
            throw new NullPointerException();
        }
        return UNSAFE.getObjectVolatile(thread, parkBlockerOffset);
    }

    public static void park() {
        UNSAFE.park(false, 0L);
    }

    public static void parkNanos(long j2) {
        if (j2 > 0) {
            UNSAFE.park(false, j2);
        }
    }

    public static void parkUntil(long j2) {
        UNSAFE.park(true, j2);
    }

    static final int nextSecondarySeed() {
        int i2;
        Thread threadCurrentThread = Thread.currentThread();
        int i3 = UNSAFE.getInt(threadCurrentThread, SECONDARY);
        if (i3 != 0) {
            int i4 = i3 ^ (i3 << 13);
            int i5 = i4 ^ (i4 >>> 17);
            i2 = i5 ^ (i5 << 5);
        } else {
            int iNextInt = ThreadLocalRandom.current().nextInt();
            i2 = iNextInt;
            if (iNextInt == 0) {
                i2 = 1;
            }
        }
        UNSAFE.putInt(threadCurrentThread, SECONDARY, i2);
        return i2;
    }

    static {
        try {
            UNSAFE = Unsafe.getUnsafe();
            parkBlockerOffset = UNSAFE.objectFieldOffset(Thread.class.getDeclaredField("parkBlocker"));
            SEED = UNSAFE.objectFieldOffset(Thread.class.getDeclaredField("threadLocalRandomSeed"));
            PROBE = UNSAFE.objectFieldOffset(Thread.class.getDeclaredField("threadLocalRandomProbe"));
            SECONDARY = UNSAFE.objectFieldOffset(Thread.class.getDeclaredField("threadLocalRandomSecondarySeed"));
        } catch (Exception e2) {
            throw new Error(e2);
        }
    }
}
