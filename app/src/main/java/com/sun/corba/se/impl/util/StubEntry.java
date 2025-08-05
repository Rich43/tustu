package com.sun.corba.se.impl.util;

import org.omg.CORBA.Object;

/* compiled from: Utility.java */
/* loaded from: rt.jar:com/sun/corba/se/impl/util/StubEntry.class */
class StubEntry {
    Object stub;
    boolean mostDerived;

    StubEntry(Object object, boolean z2) {
        this.stub = object;
        this.mostDerived = z2;
    }
}
