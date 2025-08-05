package sun.java2d.marlin;

import java.util.ArrayDeque;
import java.util.Arrays;

/* loaded from: rt.jar:sun/java2d/marlin/FloatArrayCache.class */
final class FloatArrayCache implements MarlinConst {
    private final int arraySize;
    private int getOp = 0;
    private int createOp = 0;
    private int returnOp = 0;
    private final ArrayDeque<float[]> floatArrays = new ArrayDeque<>(6);

    void dumpStats() {
        if (this.getOp > 0) {
            MarlinUtils.logInfo("FloatArrayCache[" + this.arraySize + "]: get: " + this.getOp + " created: " + this.createOp + " - returned: " + this.returnOp + " :: cache size: " + this.floatArrays.size());
        }
    }

    FloatArrayCache(int i2) {
        this.arraySize = i2;
    }

    float[] getArray() {
        if (doStats) {
            this.getOp++;
        }
        float[] fArrPollLast = this.floatArrays.pollLast();
        if (fArrPollLast != null) {
            return fArrPollLast;
        }
        if (doStats) {
            this.createOp++;
        }
        return new float[this.arraySize];
    }

    void putDirtyArray(float[] fArr, int i2) {
        if (i2 != this.arraySize) {
            if (doChecks) {
                MarlinUtils.logInfo("ArrayCache: bad length = " + i2);
            }
        } else {
            if (doStats) {
                this.returnOp++;
            }
            this.floatArrays.addLast(fArr);
        }
    }

    void putArray(float[] fArr, int i2, int i3, int i4) {
        if (i2 != this.arraySize) {
            if (doChecks) {
                MarlinUtils.logInfo("ArrayCache: bad length = " + i2);
            }
        } else {
            if (doStats) {
                this.returnOp++;
            }
            fill(fArr, i3, i4, 0.0f);
            this.floatArrays.addLast(fArr);
        }
    }

    static void fill(float[] fArr, int i2, int i3, float f2) {
        if (i3 != 0) {
            Arrays.fill(fArr, i2, i3, f2);
        }
        if (doChecks) {
            check(fArr, i2, i3, f2);
        }
    }

    static void check(float[] fArr, int i2, int i3, float f2) {
        if (doChecks) {
            for (int i4 = 0; i4 < fArr.length; i4++) {
                if (fArr[i4] != f2) {
                    MarlinUtils.logException("Invalid value at: " + i4 + " = " + fArr[i4] + " from: " + i2 + " to: " + i3 + "\n" + Arrays.toString(fArr), new Throwable());
                    Arrays.fill(fArr, f2);
                    return;
                }
            }
        }
    }
}
