package sun.java2d.marlin;

import java.util.ArrayDeque;
import java.util.Arrays;

/* loaded from: rt.jar:sun/java2d/marlin/ByteArrayCache.class */
final class ByteArrayCache implements MarlinConst {
    private final int arraySize;
    private int getOp = 0;
    private int createOp = 0;
    private int returnOp = 0;
    private final ArrayDeque<byte[]> byteArrays = new ArrayDeque<>(6);

    void dumpStats() {
        if (this.getOp > 0) {
            MarlinUtils.logInfo("ByteArrayCache[" + this.arraySize + "]: get: " + this.getOp + " created: " + this.createOp + " - returned: " + this.returnOp + " :: cache size: " + this.byteArrays.size());
        }
    }

    ByteArrayCache(int i2) {
        this.arraySize = i2;
    }

    byte[] getArray() {
        if (doStats) {
            this.getOp++;
        }
        byte[] bArrPollLast = this.byteArrays.pollLast();
        if (bArrPollLast != null) {
            return bArrPollLast;
        }
        if (doStats) {
            this.createOp++;
        }
        return new byte[this.arraySize];
    }

    void putDirtyArray(byte[] bArr, int i2) {
        if (i2 != this.arraySize) {
            if (doChecks) {
                MarlinUtils.logInfo("ArrayCache: bad length = " + i2);
            }
        } else {
            if (doStats) {
                this.returnOp++;
            }
            this.byteArrays.addLast(bArr);
        }
    }

    void putArray(byte[] bArr, int i2, int i3, int i4) {
        if (i2 != this.arraySize) {
            if (doChecks) {
                MarlinUtils.logInfo("ArrayCache: bad length = " + i2);
            }
        } else {
            if (doStats) {
                this.returnOp++;
            }
            fill(bArr, i3, i4, (byte) 0);
            this.byteArrays.addLast(bArr);
        }
    }

    static void fill(byte[] bArr, int i2, int i3, byte b2) {
        if (i3 != 0) {
            Arrays.fill(bArr, i2, i3, b2);
        }
        if (doChecks) {
            check(bArr, i2, i3, b2);
        }
    }

    static void check(byte[] bArr, int i2, int i3, byte b2) {
        if (doChecks) {
            for (int i4 = 0; i4 < bArr.length; i4++) {
                if (bArr[i4] != b2) {
                    MarlinUtils.logException("Invalid value at: " + i4 + " = " + ((int) bArr[i4]) + " from: " + i2 + " to: " + i3 + "\n" + Arrays.toString(bArr), new Throwable());
                    Arrays.fill(bArr, b2);
                    return;
                }
            }
        }
    }
}
