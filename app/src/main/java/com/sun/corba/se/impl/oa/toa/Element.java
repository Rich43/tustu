package com.sun.corba.se.impl.oa.toa;

import com.sun.corba.se.impl.orbutil.ORBUtility;

/* compiled from: TransientObjectManager.java */
/* loaded from: rt.jar:com/sun/corba/se/impl/oa/toa/Element.class */
final class Element {
    Object servant;
    int index;
    Object servantData = null;
    int counter = 0;
    boolean valid = false;

    Element(int i2, Object obj) {
        this.servant = null;
        this.index = -1;
        this.servant = obj;
        this.index = i2;
    }

    byte[] getKey(Object obj, Object obj2) {
        this.servant = obj;
        this.servantData = obj2;
        this.valid = true;
        return toBytes();
    }

    byte[] toBytes() {
        byte[] bArr = new byte[8];
        ORBUtility.intToBytes(this.index, bArr, 0);
        ORBUtility.intToBytes(this.counter, bArr, 4);
        return bArr;
    }

    void delete(Element element) {
        if (!this.valid) {
            return;
        }
        this.counter++;
        this.servantData = null;
        this.valid = false;
        this.servant = element;
    }

    public String toString() {
        return "Element[" + this.index + ", " + this.counter + "]";
    }
}
