package org.omg.CosNaming;

import org.omg.CORBA.TypeCode;
import org.omg.CORBA.portable.InputStream;
import org.omg.CORBA.portable.OutputStream;
import org.omg.CORBA.portable.Streamable;

/* loaded from: rt.jar:org/omg/CosNaming/NamingContextExtHolder.class */
public final class NamingContextExtHolder implements Streamable {
    public NamingContextExt value;

    public NamingContextExtHolder() {
        this.value = null;
    }

    public NamingContextExtHolder(NamingContextExt namingContextExt) {
        this.value = null;
        this.value = namingContextExt;
    }

    @Override // org.omg.CORBA.portable.Streamable
    public void _read(InputStream inputStream) {
        this.value = NamingContextExtHelper.read(inputStream);
    }

    @Override // org.omg.CORBA.portable.Streamable
    public void _write(OutputStream outputStream) {
        NamingContextExtHelper.write(outputStream, this.value);
    }

    @Override // org.omg.CORBA.portable.Streamable
    public TypeCode _type() {
        return NamingContextExtHelper.type();
    }
}
