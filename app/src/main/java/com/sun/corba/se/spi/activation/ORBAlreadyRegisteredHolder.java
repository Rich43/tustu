package com.sun.corba.se.spi.activation;

import org.omg.CORBA.TypeCode;
import org.omg.CORBA.portable.InputStream;
import org.omg.CORBA.portable.OutputStream;
import org.omg.CORBA.portable.Streamable;

/* loaded from: rt.jar:com/sun/corba/se/spi/activation/ORBAlreadyRegisteredHolder.class */
public final class ORBAlreadyRegisteredHolder implements Streamable {
    public ORBAlreadyRegistered value;

    public ORBAlreadyRegisteredHolder() {
        this.value = null;
    }

    public ORBAlreadyRegisteredHolder(ORBAlreadyRegistered oRBAlreadyRegistered) {
        this.value = null;
        this.value = oRBAlreadyRegistered;
    }

    @Override // org.omg.CORBA.portable.Streamable
    public void _read(InputStream inputStream) {
        this.value = ORBAlreadyRegisteredHelper.read(inputStream);
    }

    @Override // org.omg.CORBA.portable.Streamable
    public void _write(OutputStream outputStream) {
        ORBAlreadyRegisteredHelper.write(outputStream, this.value);
    }

    @Override // org.omg.CORBA.portable.Streamable
    public TypeCode _type() {
        return ORBAlreadyRegisteredHelper.type();
    }
}
