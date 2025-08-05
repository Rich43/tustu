package com.sun.corba.se.impl.ior;

/* loaded from: rt.jar:com/sun/corba/se/impl/ior/ObjectAdapterIdNumber.class */
public class ObjectAdapterIdNumber extends ObjectAdapterIdArray {
    private int poaid;

    public ObjectAdapterIdNumber(int i2) {
        super("OldRootPOA", Integer.toString(i2));
        this.poaid = i2;
    }

    public int getOldPOAId() {
        return this.poaid;
    }
}
