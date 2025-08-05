package org.omg.CORBA;

import org.omg.CORBA.portable.InputStream;
import org.omg.CORBA.portable.OutputStream;
import org.omg.CORBA.portable.Streamable;

/* loaded from: rt.jar:org/omg/CORBA/ServiceInformationHolder.class */
public final class ServiceInformationHolder implements Streamable {
    public ServiceInformation value;

    public ServiceInformationHolder() {
        this(null);
    }

    public ServiceInformationHolder(ServiceInformation serviceInformation) {
        this.value = serviceInformation;
    }

    @Override // org.omg.CORBA.portable.Streamable
    public void _write(OutputStream outputStream) {
        ServiceInformationHelper.write(outputStream, this.value);
    }

    @Override // org.omg.CORBA.portable.Streamable
    public void _read(InputStream inputStream) {
        this.value = ServiceInformationHelper.read(inputStream);
    }

    @Override // org.omg.CORBA.portable.Streamable
    public TypeCode _type() {
        return ServiceInformationHelper.type();
    }
}
