package sun.java2d.marlin;

import java.util.ArrayDeque;
import java.util.Arrays;

/* loaded from: rt.jar:sun/java2d/marlin/IntArrayCache.class */
final class IntArrayCache implements MarlinConst {
    private final int arraySize;
    private int getOp = 0;
    private int createOp = 0;
    private int returnOp = 0;
    private final ArrayDeque<int[]> intArrays = new ArrayDeque<>(6);

    void dumpStats() {
        if (this.getOp > 0) {
            MarlinUtils.logInfo("IntArrayCache[" + this.arraySize + "]: get: " + this.getOp + " created: " + this.createOp + " - returned: " + this.returnOp + " :: cache size: " + this.intArrays.size());
        }
    }

    IntArrayCache(int i2) {
        this.arraySize = i2;
    }

    int[] getArray() {
        if (doStats) {
            this.getOp++;
        }
        int[] iArrPollLast = this.intArrays.pollLast();
        if (iArrPollLast != null) {
            return iArrPollLast;
        }
        if (doStats) {
            this.createOp++;
        }
        return new int[this.arraySize];
    }

    void putDirtyArray(int[] iArr, int i2) {
        if (i2 != this.arraySize) {
            if (doChecks) {
                MarlinUtils.logInfo("ArrayCache: bad length = " + i2);
            }
        } else {
            if (doStats) {
                this.returnOp++;
            }
            this.intArrays.addLast(iArr);
        }
    }

    void putArray(int[] iArr, int i2, int i3, int i4) {
        if (i2 != this.arraySize) {
            if (doChecks) {
                MarlinUtils.logInfo("ArrayCache: bad length = " + i2);
            }
        } else {
            if (doStats) {
                this.returnOp++;
            }
            fill(iArr, i3, i4, 0);
            this.intArrays.addLast(iArr);
        }
    }

    static void fill(int[] iArr, int i2, int i3, int i4) {
        if (i3 != 0) {
            Arrays.fill(iArr, i2, i3, i4);
        }
        if (doChecks) {
            check(iArr, i2, i3, i4);
        }
    }

    static void check(int[] iArr, int i2, int i3, int i4) {
        if (doChecks) {
            for (int i5 = 0; i5 < iArr.length; i5++) {
                if (iArr[i5] != i4) {
                    MarlinUtils.logException("Invalid value at: " + i5 + " = " + iArr[i5] + " from: " + i2 + " to: " + i3 + "\n" + Arrays.toString(iArr), new Throwable());
                    Arrays.fill(iArr, i4);
                    return;
                }
            }
        }
    }
}
