package org.omg.CosNaming;

import org.omg.CORBA.TypeCode;
import org.omg.CORBA.portable.InputStream;
import org.omg.CORBA.portable.OutputStream;
import org.omg.CORBA.portable.Streamable;

/* loaded from: rt.jar:org/omg/CosNaming/NamingContextHolder.class */
public final class NamingContextHolder implements Streamable {
    public NamingContext value;

    public NamingContextHolder() {
        this.value = null;
    }

    public NamingContextHolder(NamingContext namingContext) {
        this.value = null;
        this.value = namingContext;
    }

    @Override // org.omg.CORBA.portable.Streamable
    public void _read(InputStream inputStream) {
        this.value = NamingContextHelper.read(inputStream);
    }

    @Override // org.omg.CORBA.portable.Streamable
    public void _write(OutputStream outputStream) {
        NamingContextHelper.write(outputStream, this.value);
    }

    @Override // org.omg.CORBA.portable.Streamable
    public TypeCode _type() {
        return NamingContextHelper.type();
    }
}
