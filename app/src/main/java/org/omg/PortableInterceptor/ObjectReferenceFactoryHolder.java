package org.omg.PortableInterceptor;

import org.omg.CORBA.TypeCode;
import org.omg.CORBA.portable.InputStream;
import org.omg.CORBA.portable.OutputStream;
import org.omg.CORBA.portable.Streamable;

/* loaded from: rt.jar:org/omg/PortableInterceptor/ObjectReferenceFactoryHolder.class */
public final class ObjectReferenceFactoryHolder implements Streamable {
    public ObjectReferenceFactory value;

    public ObjectReferenceFactoryHolder() {
        this.value = null;
    }

    public ObjectReferenceFactoryHolder(ObjectReferenceFactory objectReferenceFactory) {
        this.value = null;
        this.value = objectReferenceFactory;
    }

    @Override // org.omg.CORBA.portable.Streamable
    public void _read(InputStream inputStream) {
        this.value = ObjectReferenceFactoryHelper.read(inputStream);
    }

    @Override // org.omg.CORBA.portable.Streamable
    public void _write(OutputStream outputStream) {
        ObjectReferenceFactoryHelper.write(outputStream, this.value);
    }

    @Override // org.omg.CORBA.portable.Streamable
    public TypeCode _type() {
        return ObjectReferenceFactoryHelper.type();
    }
}
