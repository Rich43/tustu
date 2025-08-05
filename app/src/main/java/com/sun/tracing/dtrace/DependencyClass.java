package com.sun.tracing.dtrace;

/* loaded from: rt.jar:com/sun/tracing/dtrace/DependencyClass.class */
public enum DependencyClass {
    UNKNOWN(0),
    CPU(1),
    PLATFORM(2),
    GROUP(3),
    ISA(4),
    COMMON(5);

    private int encoding;

    public String toDisplayString() {
        return toString().substring(0, 1) + toString().substring(1).toLowerCase();
    }

    public int getEncoding() {
        return this.encoding;
    }

    DependencyClass(int i2) {
        this.encoding = i2;
    }
}
