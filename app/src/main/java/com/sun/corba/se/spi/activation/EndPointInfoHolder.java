package com.sun.corba.se.spi.activation;

import org.omg.CORBA.TypeCode;
import org.omg.CORBA.portable.InputStream;
import org.omg.CORBA.portable.OutputStream;
import org.omg.CORBA.portable.Streamable;

/* loaded from: rt.jar:com/sun/corba/se/spi/activation/EndPointInfoHolder.class */
public final class EndPointInfoHolder implements Streamable {
    public EndPointInfo value;

    public EndPointInfoHolder() {
        this.value = null;
    }

    public EndPointInfoHolder(EndPointInfo endPointInfo) {
        this.value = null;
        this.value = endPointInfo;
    }

    @Override // org.omg.CORBA.portable.Streamable
    public void _read(InputStream inputStream) {
        this.value = EndPointInfoHelper.read(inputStream);
    }

    @Override // org.omg.CORBA.portable.Streamable
    public void _write(OutputStream outputStream) {
        EndPointInfoHelper.write(outputStream, this.value);
    }

    @Override // org.omg.CORBA.portable.Streamable
    public TypeCode _type() {
        return EndPointInfoHelper.type();
    }
}
