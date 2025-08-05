package com.sun.corba.se.spi.activation;

import org.omg.CORBA.TypeCode;
import org.omg.CORBA.portable.InputStream;
import org.omg.CORBA.portable.OutputStream;
import org.omg.CORBA.portable.Streamable;

/* loaded from: rt.jar:com/sun/corba/se/spi/activation/EndpointInfoListHolder.class */
public final class EndpointInfoListHolder implements Streamable {
    public EndPointInfo[] value;

    public EndpointInfoListHolder() {
        this.value = null;
    }

    public EndpointInfoListHolder(EndPointInfo[] endPointInfoArr) {
        this.value = null;
        this.value = endPointInfoArr;
    }

    @Override // org.omg.CORBA.portable.Streamable
    public void _read(InputStream inputStream) {
        this.value = EndpointInfoListHelper.read(inputStream);
    }

    @Override // org.omg.CORBA.portable.Streamable
    public void _write(OutputStream outputStream) {
        EndpointInfoListHelper.write(outputStream, this.value);
    }

    @Override // org.omg.CORBA.portable.Streamable
    public TypeCode _type() {
        return EndpointInfoListHelper.type();
    }
}
