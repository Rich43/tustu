package org.omg.IOP;

import org.omg.CORBA.TypeCode;
import org.omg.CORBA.portable.InputStream;
import org.omg.CORBA.portable.OutputStream;
import org.omg.CORBA.portable.Streamable;

/* loaded from: rt.jar:org/omg/IOP/ServiceContextHolder.class */
public final class ServiceContextHolder implements Streamable {
    public ServiceContext value;

    public ServiceContextHolder() {
        this.value = null;
    }

    public ServiceContextHolder(ServiceContext serviceContext) {
        this.value = null;
        this.value = serviceContext;
    }

    @Override // org.omg.CORBA.portable.Streamable
    public void _read(InputStream inputStream) {
        this.value = ServiceContextHelper.read(inputStream);
    }

    @Override // org.omg.CORBA.portable.Streamable
    public void _write(OutputStream outputStream) {
        ServiceContextHelper.write(outputStream, this.value);
    }

    @Override // org.omg.CORBA.portable.Streamable
    public TypeCode _type() {
        return ServiceContextHelper.type();
    }
}
