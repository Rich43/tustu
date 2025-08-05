package com.sun.tracing.dtrace;

/* loaded from: rt.jar:com/sun/tracing/dtrace/StabilityLevel.class */
public enum StabilityLevel {
    INTERNAL(0),
    PRIVATE(1),
    OBSOLETE(2),
    EXTERNAL(3),
    UNSTABLE(4),
    EVOLVING(5),
    STABLE(6),
    STANDARD(7);

    private int encoding;

    String toDisplayString() {
        return toString().substring(0, 1) + toString().substring(1).toLowerCase();
    }

    public int getEncoding() {
        return this.encoding;
    }

    StabilityLevel(int i2) {
        this.encoding = i2;
    }
}
