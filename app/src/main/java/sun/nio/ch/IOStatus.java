package sun.nio.ch;

/* loaded from: rt.jar:sun/nio/ch/IOStatus.class */
public final class IOStatus {
    public static final int EOF = -1;
    public static final int UNAVAILABLE = -2;
    public static final int INTERRUPTED = -3;
    public static final int UNSUPPORTED = -4;
    public static final int THROWN = -5;
    public static final int UNSUPPORTED_CASE = -6;

    private IOStatus() {
    }

    public static int normalize(int i2) {
        if (i2 == -2) {
            return 0;
        }
        return i2;
    }

    public static boolean check(int i2) {
        return i2 >= -2;
    }

    public static long normalize(long j2) {
        if (j2 == -2) {
            return 0L;
        }
        return j2;
    }

    public static boolean check(long j2) {
        return j2 >= -2;
    }

    public static boolean checkAll(long j2) {
        return j2 > -1 || j2 < -6;
    }
}
