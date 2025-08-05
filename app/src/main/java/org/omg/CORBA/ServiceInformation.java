package org.omg.CORBA;

import org.omg.CORBA.portable.IDLEntity;

/* loaded from: rt.jar:org/omg/CORBA/ServiceInformation.class */
public final class ServiceInformation implements IDLEntity {
    public int[] service_options;
    public ServiceDetail[] service_details;

    public ServiceInformation() {
    }

    public ServiceInformation(int[] iArr, ServiceDetail[] serviceDetailArr) {
        this.service_options = iArr;
        this.service_details = serviceDetailArr;
    }
}
