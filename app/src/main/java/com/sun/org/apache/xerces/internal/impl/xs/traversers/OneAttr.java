package com.sun.org.apache.xerces.internal.impl.xs.traversers;

/* compiled from: XSAttributeChecker.java */
/* loaded from: rt.jar:com/sun/org/apache/xerces/internal/impl/xs/traversers/OneAttr.class */
class OneAttr {
    public String name;
    public int dvIndex;
    public int valueIndex;
    public Object dfltValue;

    public OneAttr(String name, int dvIndex, int valueIndex, Object dfltValue) {
        this.name = name;
        this.dvIndex = dvIndex;
        this.valueIndex = valueIndex;
        this.dfltValue = dfltValue;
    }
}
