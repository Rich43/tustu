package org.omg.CosNaming;

import org.omg.CORBA.TypeCode;
import org.omg.CORBA.portable.InputStream;
import org.omg.CORBA.portable.OutputStream;
import org.omg.CORBA.portable.Streamable;

/* loaded from: rt.jar:org/omg/CosNaming/BindingIteratorHolder.class */
public final class BindingIteratorHolder implements Streamable {
    public BindingIterator value;

    public BindingIteratorHolder() {
        this.value = null;
    }

    public BindingIteratorHolder(BindingIterator bindingIterator) {
        this.value = null;
        this.value = bindingIterator;
    }

    @Override // org.omg.CORBA.portable.Streamable
    public void _read(InputStream inputStream) {
        this.value = BindingIteratorHelper.read(inputStream);
    }

    @Override // org.omg.CORBA.portable.Streamable
    public void _write(OutputStream outputStream) {
        BindingIteratorHelper.write(outputStream, this.value);
    }

    @Override // org.omg.CORBA.portable.Streamable
    public TypeCode _type() {
        return BindingIteratorHelper.type();
    }
}
