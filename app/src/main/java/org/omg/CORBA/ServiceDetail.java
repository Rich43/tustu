package org.omg.CORBA;

import org.omg.CORBA.portable.IDLEntity;

/* loaded from: rt.jar:org/omg/CORBA/ServiceDetail.class */
public final class ServiceDetail implements IDLEntity {
    public int service_detail_type;
    public byte[] service_detail;

    public ServiceDetail() {
    }

    public ServiceDetail(int i2, byte[] bArr) {
        this.service_detail_type = i2;
        this.service_detail = bArr;
    }
}
