package com.sun.corba.se.spi.activation;

import org.omg.CORBA.TypeCode;
import org.omg.CORBA.portable.InputStream;
import org.omg.CORBA.portable.OutputStream;
import org.omg.CORBA.portable.Streamable;

/* loaded from: rt.jar:com/sun/corba/se/spi/activation/InitialNameServiceHolder.class */
public final class InitialNameServiceHolder implements Streamable {
    public InitialNameService value;

    public InitialNameServiceHolder() {
        this.value = null;
    }

    public InitialNameServiceHolder(InitialNameService initialNameService) {
        this.value = null;
        this.value = initialNameService;
    }

    @Override // org.omg.CORBA.portable.Streamable
    public void _read(InputStream inputStream) {
        this.value = InitialNameServiceHelper.read(inputStream);
    }

    @Override // org.omg.CORBA.portable.Streamable
    public void _write(OutputStream outputStream) {
        InitialNameServiceHelper.write(outputStream, this.value);
    }

    @Override // org.omg.CORBA.portable.Streamable
    public TypeCode _type() {
        return InitialNameServiceHelper.type();
    }
}
