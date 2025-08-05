package com.sun.corba.se.impl.protocol.giopmsgheaders;

import org.omg.CORBA.portable.IDLEntity;
import org.omg.IOP.IOR;

/* loaded from: rt.jar:com/sun/corba/se/impl/protocol/giopmsgheaders/IORAddressingInfo.class */
public final class IORAddressingInfo implements IDLEntity {
    public int selected_profile_index;
    public IOR ior;

    public IORAddressingInfo() {
        this.selected_profile_index = 0;
        this.ior = null;
    }

    public IORAddressingInfo(int i2, IOR ior) {
        this.selected_profile_index = 0;
        this.ior = null;
        this.selected_profile_index = i2;
        this.ior = ior;
    }
}
