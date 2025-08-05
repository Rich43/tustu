package com.sun.corba.se.spi.activation;

import org.omg.CORBA.TypeCode;
import org.omg.CORBA.portable.InputStream;
import org.omg.CORBA.portable.OutputStream;
import org.omg.CORBA.portable.Streamable;

/* loaded from: rt.jar:com/sun/corba/se/spi/activation/NoSuchEndPointHolder.class */
public final class NoSuchEndPointHolder implements Streamable {
    public NoSuchEndPoint value;

    public NoSuchEndPointHolder() {
        this.value = null;
    }

    public NoSuchEndPointHolder(NoSuchEndPoint noSuchEndPoint) {
        this.value = null;
        this.value = noSuchEndPoint;
    }

    @Override // org.omg.CORBA.portable.Streamable
    public void _read(InputStream inputStream) {
        this.value = NoSuchEndPointHelper.read(inputStream);
    }

    @Override // org.omg.CORBA.portable.Streamable
    public void _write(OutputStream outputStream) {
        NoSuchEndPointHelper.write(outputStream, this.value);
    }

    @Override // org.omg.CORBA.portable.Streamable
    public TypeCode _type() {
        return NoSuchEndPointHelper.type();
    }
}
