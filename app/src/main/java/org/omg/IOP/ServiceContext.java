package org.omg.IOP;

import org.omg.CORBA.portable.IDLEntity;

/* loaded from: rt.jar:org/omg/IOP/ServiceContext.class */
public final class ServiceContext implements IDLEntity {
    public int context_id;
    public byte[] context_data;

    public ServiceContext() {
        this.context_id = 0;
        this.context_data = null;
    }

    public ServiceContext(int i2, byte[] bArr) {
        this.context_id = 0;
        this.context_data = null;
        this.context_id = i2;
        this.context_data = bArr;
    }
}
