package com.sun.org.apache.xerces.internal.impl.xs.util;

/* loaded from: rt.jar:com/sun/org/apache/xerces/internal/impl/xs/util/XInt.class */
public final class XInt {
    private int fValue;

    XInt(int value) {
        this.fValue = value;
    }

    public final int intValue() {
        return this.fValue;
    }

    public final short shortValue() {
        return (short) this.fValue;
    }

    public final boolean equals(XInt compareVal) {
        return this.fValue == compareVal.fValue;
    }

    public String toString() {
        return Integer.toString(this.fValue);
    }
}
