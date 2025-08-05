package org.omg.CosNaming.NamingContextPackage;

import org.omg.CORBA.TypeCode;
import org.omg.CORBA.portable.InputStream;
import org.omg.CORBA.portable.OutputStream;
import org.omg.CORBA.portable.Streamable;

/* loaded from: rt.jar:org/omg/CosNaming/NamingContextPackage/NotEmptyHolder.class */
public final class NotEmptyHolder implements Streamable {
    public NotEmpty value;

    public NotEmptyHolder() {
        this.value = null;
    }

    public NotEmptyHolder(NotEmpty notEmpty) {
        this.value = null;
        this.value = notEmpty;
    }

    @Override // org.omg.CORBA.portable.Streamable
    public void _read(InputStream inputStream) {
        this.value = NotEmptyHelper.read(inputStream);
    }

    @Override // org.omg.CORBA.portable.Streamable
    public void _write(OutputStream outputStream) {
        NotEmptyHelper.write(outputStream, this.value);
    }

    @Override // org.omg.CORBA.portable.Streamable
    public TypeCode _type() {
        return NotEmptyHelper.type();
    }
}
