package com.sun.javafx.animation;

import javafx.util.Duration;

/* loaded from: jfxrt.jar:com/sun/javafx/animation/TickCalculation.class */
public class TickCalculation {
    public static final int TICKS_PER_SECOND = 6000;
    private static final double TICKS_PER_MILI = 6.0d;
    private static final double TICKS_PER_NANO = 6.0E-6d;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !TickCalculation.class.desiredAssertionStatus();
    }

    private TickCalculation() {
    }

    public static long add(long op1, long op2) {
        if (!$assertionsDisabled && op1 < 0) {
            throw new AssertionError();
        }
        if (op1 == Long.MAX_VALUE || op2 == Long.MAX_VALUE) {
            return Long.MAX_VALUE;
        }
        if (op2 == Long.MIN_VALUE) {
            return 0L;
        }
        if (op2 >= 0) {
            long result = op1 + op2;
            return result < 0 ? Long.MAX_VALUE : result;
        }
        return Math.max(0L, op1 + op2);
    }

    public static long sub(long op1, long op2) {
        if (!$assertionsDisabled && op1 < 0) {
            throw new AssertionError();
        }
        if (op1 == Long.MAX_VALUE || op2 == Long.MIN_VALUE) {
            return Long.MAX_VALUE;
        }
        if (op2 == Long.MAX_VALUE) {
            return 0L;
        }
        if (op2 >= 0) {
            return Math.max(0L, op1 - op2);
        }
        long result = op1 - op2;
        return result < 0 ? Long.MAX_VALUE : result;
    }

    public static long fromMillis(double millis) {
        return Math.round(TICKS_PER_MILI * millis);
    }

    public static long fromNano(long nano) {
        return Math.round(TICKS_PER_NANO * nano);
    }

    public static long fromDuration(Duration duration) {
        return fromMillis(duration.toMillis());
    }

    public static long fromDuration(Duration duration, double rate) {
        return Math.round((TICKS_PER_MILI * duration.toMillis()) / Math.abs(rate));
    }

    public static Duration toDuration(long ticks) {
        return Duration.millis(toMillis(ticks));
    }

    public static double toMillis(long ticks) {
        return ticks / TICKS_PER_MILI;
    }
}
