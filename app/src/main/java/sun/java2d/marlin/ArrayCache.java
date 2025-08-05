package sun.java2d.marlin;

import java.util.Arrays;

/* loaded from: rt.jar:sun/java2d/marlin/ArrayCache.class */
public final class ArrayCache implements MarlinConst {
    static final int BUCKETS = 4;
    static final int MIN_ARRAY_SIZE = 4096;
    static final int MAX_ARRAY_SIZE;
    static final int MASK_CLR_1 = -2;
    static final int THRESHOLD_ARRAY_SIZE;
    static final int[] ARRAY_SIZES;
    static final int MIN_DIRTY_BYTE_ARRAY_SIZE = 65536;
    static final int MAX_DIRTY_BYTE_ARRAY_SIZE;
    static final int[] DIRTY_BYTE_ARRAY_SIZES;
    static final long THRESHOLD_LARGE_ARRAY_SIZE;
    static final long THRESHOLD_HUGE_ARRAY_SIZE;
    private static int resizeInt;
    private static int resizeDirtyInt;
    private static int resizeDirtyFloat;
    private static int resizeDirtyByte;
    private static int oversize;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !ArrayCache.class.desiredAssertionStatus();
        ARRAY_SIZES = new int[4];
        DIRTY_BYTE_ARRAY_SIZES = new int[4];
        resizeInt = 0;
        resizeDirtyInt = 0;
        resizeDirtyFloat = 0;
        resizeDirtyByte = 0;
        oversize = 0;
        int i2 = 4096;
        int i3 = 0;
        while (i3 < 4) {
            ARRAY_SIZES[i3] = i2;
            if (doTrace) {
                MarlinUtils.logInfo("arraySize[" + i3 + "]: " + i2);
            }
            i3++;
            i2 <<= 2;
        }
        MAX_ARRAY_SIZE = i2 >> 2;
        int i4 = 65536;
        int i5 = 0;
        while (i5 < 4) {
            DIRTY_BYTE_ARRAY_SIZES[i5] = i4;
            if (doTrace) {
                MarlinUtils.logInfo("dirty arraySize[" + i5 + "]: " + i4);
            }
            i5++;
            i4 <<= 1;
        }
        MAX_DIRTY_BYTE_ARRAY_SIZE = i4 >> 1;
        THRESHOLD_ARRAY_SIZE = Math.max(2097152, MAX_ARRAY_SIZE);
        THRESHOLD_LARGE_ARRAY_SIZE = 8 * THRESHOLD_ARRAY_SIZE;
        THRESHOLD_HUGE_ARRAY_SIZE = 8 * THRESHOLD_LARGE_ARRAY_SIZE;
        if (doStats) {
            MarlinUtils.logInfo("ArrayCache.BUCKETS        = 4");
            MarlinUtils.logInfo("ArrayCache.MIN_ARRAY_SIZE = 4096");
            MarlinUtils.logInfo("ArrayCache.MAX_ARRAY_SIZE = " + MAX_ARRAY_SIZE);
            MarlinUtils.logInfo("ArrayCache.ARRAY_SIZES = " + Arrays.toString(ARRAY_SIZES));
            MarlinUtils.logInfo("ArrayCache.MIN_DIRTY_BYTE_ARRAY_SIZE = 65536");
            MarlinUtils.logInfo("ArrayCache.MAX_DIRTY_BYTE_ARRAY_SIZE = " + MAX_DIRTY_BYTE_ARRAY_SIZE);
            MarlinUtils.logInfo("ArrayCache.ARRAY_SIZES = " + Arrays.toString(DIRTY_BYTE_ARRAY_SIZES));
            MarlinUtils.logInfo("ArrayCache.THRESHOLD_ARRAY_SIZE = " + THRESHOLD_ARRAY_SIZE);
            MarlinUtils.logInfo("ArrayCache.THRESHOLD_LARGE_ARRAY_SIZE = " + THRESHOLD_LARGE_ARRAY_SIZE);
            MarlinUtils.logInfo("ArrayCache.THRESHOLD_HUGE_ARRAY_SIZE = " + THRESHOLD_HUGE_ARRAY_SIZE);
        }
    }

    private ArrayCache() {
    }

    static synchronized void incResizeInt() {
        resizeInt++;
    }

    static synchronized void incResizeDirtyInt() {
        resizeDirtyInt++;
    }

    static synchronized void incResizeDirtyFloat() {
        resizeDirtyFloat++;
    }

    static synchronized void incResizeDirtyByte() {
        resizeDirtyByte++;
    }

    static synchronized void incOversize() {
        oversize++;
    }

    static void dumpStats() {
        if (resizeInt != 0 || resizeDirtyInt != 0 || resizeDirtyFloat != 0 || resizeDirtyByte != 0 || oversize != 0) {
            MarlinUtils.logInfo("ArrayCache: int resize: " + resizeInt + " - dirty int resize: " + resizeDirtyInt + " - dirty float resize: " + resizeDirtyFloat + " - dirty byte resize: " + resizeDirtyByte + " - oversize: " + oversize);
        }
    }

    static int getBucket(int i2) {
        for (int i3 = 0; i3 < ARRAY_SIZES.length; i3++) {
            if (i2 <= ARRAY_SIZES[i3]) {
                return i3;
            }
        }
        return -1;
    }

    static int getBucketDirtyBytes(int i2) {
        for (int i3 = 0; i3 < DIRTY_BYTE_ARRAY_SIZES.length; i3++) {
            if (i2 <= DIRTY_BYTE_ARRAY_SIZES[i3]) {
                return i3;
            }
        }
        return -1;
    }

    public static int getNewSize(int i2, int i3) {
        int i4;
        if (i3 < 0) {
            throw new ArrayIndexOutOfBoundsException("array exceeds maximum capacity !");
        }
        if (!$assertionsDisabled && i2 < 0) {
            throw new AssertionError();
        }
        int i5 = i2 & (-2);
        if (i5 > THRESHOLD_ARRAY_SIZE) {
            i4 = i5 + (i5 >> 1);
        } else {
            i4 = i5 << 1;
        }
        if (i4 < i3) {
            i4 = ((i3 >> 12) + 1) << 12;
        }
        if (i4 < 0) {
            i4 = Integer.MAX_VALUE;
        }
        return i4;
    }

    public static long getNewLargeSize(long j2, long j3) {
        long j4;
        if ((j3 >> 31) != 0) {
            throw new ArrayIndexOutOfBoundsException("array exceeds maximum capacity !");
        }
        if (!$assertionsDisabled && j2 < 0) {
            throw new AssertionError();
        }
        if (j2 > THRESHOLD_HUGE_ARRAY_SIZE) {
            j4 = j2 + (j2 >> 2);
        } else if (j2 > THRESHOLD_LARGE_ARRAY_SIZE) {
            j4 = j2 + (j2 >> 1);
        } else {
            j4 = j2 << 1;
        }
        if (j4 < j3) {
            j4 = ((j3 >> 12) + 1) << 12;
        }
        if (j4 > 2147483647L) {
            j4 = 2147483647L;
        }
        return j4;
    }
}
