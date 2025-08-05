package org.omg.IOP;

import org.omg.CORBA.TypeCode;
import org.omg.CORBA.portable.InputStream;
import org.omg.CORBA.portable.OutputStream;
import org.omg.CORBA.portable.Streamable;

/* loaded from: rt.jar:org/omg/IOP/ServiceContextListHolder.class */
public final class ServiceContextListHolder implements Streamable {
    public ServiceContext[] value;

    public ServiceContextListHolder() {
        this.value = null;
    }

    public ServiceContextListHolder(ServiceContext[] serviceContextArr) {
        this.value = null;
        this.value = serviceContextArr;
    }

    @Override // org.omg.CORBA.portable.Streamable
    public void _read(InputStream inputStream) {
        this.value = ServiceContextListHelper.read(inputStream);
    }

    @Override // org.omg.CORBA.portable.Streamable
    public void _write(OutputStream outputStream) {
        ServiceContextListHelper.write(outputStream, this.value);
    }

    @Override // org.omg.CORBA.portable.Streamable
    public TypeCode _type() {
        return ServiceContextListHelper.type();
    }
}
