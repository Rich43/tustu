package com.sun.org.apache.xerces.internal.impl.xs.traversers;

/* compiled from: XSAttributeChecker.java */
/* loaded from: rt.jar:com/sun/org/apache/xerces/internal/impl/xs/traversers/SmallContainer.class */
class SmallContainer extends Container {
    String[] keys;

    SmallContainer(int size) {
        this.keys = new String[size];
        this.values = new OneAttr[size];
    }

    @Override // com.sun.org.apache.xerces.internal.impl.xs.traversers.Container
    void put(String key, OneAttr value) {
        this.keys[this.pos] = key;
        OneAttr[] oneAttrArr = this.values;
        int i2 = this.pos;
        this.pos = i2 + 1;
        oneAttrArr[i2] = value;
    }

    @Override // com.sun.org.apache.xerces.internal.impl.xs.traversers.Container
    OneAttr get(String key) {
        for (int i2 = 0; i2 < this.pos; i2++) {
            if (this.keys[i2].equals(key)) {
                return this.values[i2];
            }
        }
        return null;
    }
}
