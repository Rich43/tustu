package jdk.nashorn.internal.runtime.regexp.joni;

/* loaded from: nashorn.jar:jdk/nashorn/internal/runtime/regexp/joni/BitStatus.class */
final class BitStatus {
    public static final int BIT_STATUS_BITS_NUM = 32;

    BitStatus() {
    }

    public static int bsClear() {
        return 0;
    }

    public static int bsAll() {
        return -1;
    }

    public static boolean bsAt(int stats, int n2) {
        return (n2 < 32 ? stats & (1 << n2) : stats & 1) != 0;
    }

    public static int bsOnAt(int statsp, int n2) {
        int stats;
        if (n2 < 32) {
            stats = statsp | (1 << n2);
        } else {
            stats = statsp | 1;
        }
        return stats;
    }

    public static int bsOnOff(int v2, int f2, boolean negative) {
        return negative ? v2 & (f2 ^ (-1)) : v2 | f2;
    }
}
