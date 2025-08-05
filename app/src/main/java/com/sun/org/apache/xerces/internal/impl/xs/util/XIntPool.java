package com.sun.org.apache.xerces.internal.impl.xs.util;

/* loaded from: rt.jar:com/sun/org/apache/xerces/internal/impl/xs/util/XIntPool.class */
public final class XIntPool {
    private static final short POOL_SIZE = 10;
    private static final XInt[] fXIntPool = new XInt[10];

    static {
        for (int i2 = 0; i2 < 10; i2++) {
            fXIntPool[i2] = new XInt(i2);
        }
    }

    public final XInt getXInt(int value) {
        if (value >= 0 && value < fXIntPool.length) {
            return fXIntPool[value];
        }
        return new XInt(value);
    }
}
