package com.sun.corba.se.spi.activation;

import org.omg.CORBA.TypeCode;
import org.omg.CORBA.portable.InputStream;
import org.omg.CORBA.portable.OutputStream;
import org.omg.CORBA.portable.Streamable;

/* loaded from: rt.jar:com/sun/corba/se/spi/activation/ORBPortInfoHolder.class */
public final class ORBPortInfoHolder implements Streamable {
    public ORBPortInfo value;

    public ORBPortInfoHolder() {
        this.value = null;
    }

    public ORBPortInfoHolder(ORBPortInfo oRBPortInfo) {
        this.value = null;
        this.value = oRBPortInfo;
    }

    @Override // org.omg.CORBA.portable.Streamable
    public void _read(InputStream inputStream) {
        this.value = ORBPortInfoHelper.read(inputStream);
    }

    @Override // org.omg.CORBA.portable.Streamable
    public void _write(OutputStream outputStream) {
        ORBPortInfoHelper.write(outputStream, this.value);
    }

    @Override // org.omg.CORBA.portable.Streamable
    public TypeCode _type() {
        return ORBPortInfoHelper.type();
    }
}
