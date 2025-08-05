package org.omg.CosNaming;

import org.omg.CORBA.TypeCode;
import org.omg.CORBA.portable.InputStream;
import org.omg.CORBA.portable.OutputStream;
import org.omg.CORBA.portable.Streamable;

/* loaded from: rt.jar:org/omg/CosNaming/BindingTypeHolder.class */
public final class BindingTypeHolder implements Streamable {
    public BindingType value;

    public BindingTypeHolder() {
        this.value = null;
    }

    public BindingTypeHolder(BindingType bindingType) {
        this.value = null;
        this.value = bindingType;
    }

    @Override // org.omg.CORBA.portable.Streamable
    public void _read(InputStream inputStream) {
        this.value = BindingTypeHelper.read(inputStream);
    }

    @Override // org.omg.CORBA.portable.Streamable
    public void _write(OutputStream outputStream) {
        BindingTypeHelper.write(outputStream, this.value);
    }

    @Override // org.omg.CORBA.portable.Streamable
    public TypeCode _type() {
        return BindingTypeHelper.type();
    }
}
