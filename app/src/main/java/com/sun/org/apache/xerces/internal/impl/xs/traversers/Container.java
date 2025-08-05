package com.sun.org.apache.xerces.internal.impl.xs.traversers;

/* compiled from: XSAttributeChecker.java */
/* loaded from: rt.jar:com/sun/org/apache/xerces/internal/impl/xs/traversers/Container.class */
abstract class Container {
    static final int THRESHOLD = 5;
    OneAttr[] values;
    int pos = 0;

    abstract void put(String str, OneAttr oneAttr);

    abstract OneAttr get(String str);

    Container() {
    }

    static Container getContainer(int size) {
        if (size > 5) {
            return new LargeContainer(size);
        }
        return new SmallContainer(size);
    }
}
