package org.omg.CosNaming;

import org.omg.CORBA.TypeCode;
import org.omg.CORBA.portable.InputStream;
import org.omg.CORBA.portable.OutputStream;
import org.omg.CORBA.portable.Streamable;

/* loaded from: rt.jar:org/omg/CosNaming/BindingListHolder.class */
public final class BindingListHolder implements Streamable {
    public Binding[] value;

    public BindingListHolder() {
        this.value = null;
    }

    public BindingListHolder(Binding[] bindingArr) {
        this.value = null;
        this.value = bindingArr;
    }

    @Override // org.omg.CORBA.portable.Streamable
    public void _read(InputStream inputStream) {
        this.value = BindingListHelper.read(inputStream);
    }

    @Override // org.omg.CORBA.portable.Streamable
    public void _write(OutputStream outputStream) {
        BindingListHelper.write(outputStream, this.value);
    }

    @Override // org.omg.CORBA.portable.Streamable
    public TypeCode _type() {
        return BindingListHelper.type();
    }
}
