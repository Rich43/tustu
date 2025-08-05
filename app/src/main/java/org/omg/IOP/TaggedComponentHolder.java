package org.omg.IOP;

import org.omg.CORBA.TypeCode;
import org.omg.CORBA.portable.InputStream;
import org.omg.CORBA.portable.OutputStream;
import org.omg.CORBA.portable.Streamable;

/* loaded from: rt.jar:org/omg/IOP/TaggedComponentHolder.class */
public final class TaggedComponentHolder implements Streamable {
    public TaggedComponent value;

    public TaggedComponentHolder() {
        this.value = null;
    }

    public TaggedComponentHolder(TaggedComponent taggedComponent) {
        this.value = null;
        this.value = taggedComponent;
    }

    @Override // org.omg.CORBA.portable.Streamable
    public void _read(InputStream inputStream) {
        this.value = TaggedComponentHelper.read(inputStream);
    }

    @Override // org.omg.CORBA.portable.Streamable
    public void _write(OutputStream outputStream) {
        TaggedComponentHelper.write(outputStream, this.value);
    }

    @Override // org.omg.CORBA.portable.Streamable
    public TypeCode _type() {
        return TaggedComponentHelper.type();
    }
}
