package org.omg.CosNaming;

import org.omg.CORBA.TypeCode;
import org.omg.CORBA.portable.InputStream;
import org.omg.CORBA.portable.OutputStream;
import org.omg.CORBA.portable.Streamable;

/* loaded from: rt.jar:org/omg/CosNaming/NameComponentHolder.class */
public final class NameComponentHolder implements Streamable {
    public NameComponent value;

    public NameComponentHolder() {
        this.value = null;
    }

    public NameComponentHolder(NameComponent nameComponent) {
        this.value = null;
        this.value = nameComponent;
    }

    @Override // org.omg.CORBA.portable.Streamable
    public void _read(InputStream inputStream) {
        this.value = NameComponentHelper.read(inputStream);
    }

    @Override // org.omg.CORBA.portable.Streamable
    public void _write(OutputStream outputStream) {
        NameComponentHelper.write(outputStream, this.value);
    }

    @Override // org.omg.CORBA.portable.Streamable
    public TypeCode _type() {
        return NameComponentHelper.type();
    }
}
