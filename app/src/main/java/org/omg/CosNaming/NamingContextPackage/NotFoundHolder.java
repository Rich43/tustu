package org.omg.CosNaming.NamingContextPackage;

import org.omg.CORBA.TypeCode;
import org.omg.CORBA.portable.InputStream;
import org.omg.CORBA.portable.OutputStream;
import org.omg.CORBA.portable.Streamable;

/* loaded from: rt.jar:org/omg/CosNaming/NamingContextPackage/NotFoundHolder.class */
public final class NotFoundHolder implements Streamable {
    public NotFound value;

    public NotFoundHolder() {
        this.value = null;
    }

    public NotFoundHolder(NotFound notFound) {
        this.value = null;
        this.value = notFound;
    }

    @Override // org.omg.CORBA.portable.Streamable
    public void _read(InputStream inputStream) {
        this.value = NotFoundHelper.read(inputStream);
    }

    @Override // org.omg.CORBA.portable.Streamable
    public void _write(OutputStream outputStream) {
        NotFoundHelper.write(outputStream, this.value);
    }

    @Override // org.omg.CORBA.portable.Streamable
    public TypeCode _type() {
        return NotFoundHelper.type();
    }
}
