package com.sun.org.apache.xerces.internal.impl.xs.traversers;

import java.util.HashMap;
import java.util.Map;

/* compiled from: XSAttributeChecker.java */
/* loaded from: rt.jar:com/sun/org/apache/xerces/internal/impl/xs/traversers/LargeContainer.class */
class LargeContainer extends Container {
    Map items;

    LargeContainer(int size) {
        this.items = new HashMap((size * 2) + 1);
        this.values = new OneAttr[size];
    }

    @Override // com.sun.org.apache.xerces.internal.impl.xs.traversers.Container
    void put(String key, OneAttr value) {
        this.items.put(key, value);
        OneAttr[] oneAttrArr = this.values;
        int i2 = this.pos;
        this.pos = i2 + 1;
        oneAttrArr[i2] = value;
    }

    @Override // com.sun.org.apache.xerces.internal.impl.xs.traversers.Container
    OneAttr get(String key) {
        OneAttr ret = (OneAttr) this.items.get(key);
        return ret;
    }
}
