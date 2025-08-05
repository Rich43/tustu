package com.sun.corba.se.spi.activation.InitialNameServicePackage;

import org.omg.CORBA.TypeCode;
import org.omg.CORBA.portable.InputStream;
import org.omg.CORBA.portable.OutputStream;
import org.omg.CORBA.portable.Streamable;

/* loaded from: rt.jar:com/sun/corba/se/spi/activation/InitialNameServicePackage/NameAlreadyBoundHolder.class */
public final class NameAlreadyBoundHolder implements Streamable {
    public NameAlreadyBound value;

    public NameAlreadyBoundHolder() {
        this.value = null;
    }

    public NameAlreadyBoundHolder(NameAlreadyBound nameAlreadyBound) {
        this.value = null;
        this.value = nameAlreadyBound;
    }

    @Override // org.omg.CORBA.portable.Streamable
    public void _read(InputStream inputStream) {
        this.value = NameAlreadyBoundHelper.read(inputStream);
    }

    @Override // org.omg.CORBA.portable.Streamable
    public void _write(OutputStream outputStream) {
        NameAlreadyBoundHelper.write(outputStream, this.value);
    }

    @Override // org.omg.CORBA.portable.Streamable
    public TypeCode _type() {
        return NameAlreadyBoundHelper.type();
    }
}
