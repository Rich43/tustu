package org.omg.IOP;

import org.omg.CORBA.TypeCode;
import org.omg.CORBA.portable.InputStream;
import org.omg.CORBA.portable.OutputStream;
import org.omg.CORBA.portable.Streamable;

/* loaded from: rt.jar:org/omg/IOP/MultipleComponentProfileHolder.class */
public final class MultipleComponentProfileHolder implements Streamable {
    public TaggedComponent[] value;

    public MultipleComponentProfileHolder() {
        this.value = null;
    }

    public MultipleComponentProfileHolder(TaggedComponent[] taggedComponentArr) {
        this.value = null;
        this.value = taggedComponentArr;
    }

    @Override // org.omg.CORBA.portable.Streamable
    public void _read(InputStream inputStream) {
        this.value = MultipleComponentProfileHelper.read(inputStream);
    }

    @Override // org.omg.CORBA.portable.Streamable
    public void _write(OutputStream outputStream) {
        MultipleComponentProfileHelper.write(outputStream, this.value);
    }

    @Override // org.omg.CORBA.portable.Streamable
    public TypeCode _type() {
        return MultipleComponentProfileHelper.type();
    }
}
