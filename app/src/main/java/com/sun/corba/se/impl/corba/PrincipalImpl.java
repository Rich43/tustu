package com.sun.corba.se.impl.corba;

import org.omg.CORBA.Principal;

/* loaded from: rt.jar:com/sun/corba/se/impl/corba/PrincipalImpl.class */
public class PrincipalImpl extends Principal {
    private byte[] value;

    @Override // org.omg.CORBA.Principal
    public void name(byte[] bArr) {
        this.value = bArr;
    }

    @Override // org.omg.CORBA.Principal
    public byte[] name() {
        return this.value;
    }
}
