package org.omg.IOP;

import org.omg.CORBA.TypeCode;
import org.omg.CORBA.portable.InputStream;
import org.omg.CORBA.portable.OutputStream;
import org.omg.CORBA.portable.Streamable;

/* loaded from: rt.jar:org/omg/IOP/TaggedProfileHolder.class */
public final class TaggedProfileHolder implements Streamable {
    public TaggedProfile value;

    public TaggedProfileHolder() {
        this.value = null;
    }

    public TaggedProfileHolder(TaggedProfile taggedProfile) {
        this.value = null;
        this.value = taggedProfile;
    }

    @Override // org.omg.CORBA.portable.Streamable
    public void _read(InputStream inputStream) {
        this.value = TaggedProfileHelper.read(inputStream);
    }

    @Override // org.omg.CORBA.portable.Streamable
    public void _write(OutputStream outputStream) {
        TaggedProfileHelper.write(outputStream, this.value);
    }

    @Override // org.omg.CORBA.portable.Streamable
    public TypeCode _type() {
        return TaggedProfileHelper.type();
    }
}
