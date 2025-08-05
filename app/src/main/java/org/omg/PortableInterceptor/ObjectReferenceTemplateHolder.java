package org.omg.PortableInterceptor;

import org.omg.CORBA.TypeCode;
import org.omg.CORBA.portable.InputStream;
import org.omg.CORBA.portable.OutputStream;
import org.omg.CORBA.portable.Streamable;

/* loaded from: rt.jar:org/omg/PortableInterceptor/ObjectReferenceTemplateHolder.class */
public final class ObjectReferenceTemplateHolder implements Streamable {
    public ObjectReferenceTemplate value;

    public ObjectReferenceTemplateHolder() {
        this.value = null;
    }

    public ObjectReferenceTemplateHolder(ObjectReferenceTemplate objectReferenceTemplate) {
        this.value = null;
        this.value = objectReferenceTemplate;
    }

    @Override // org.omg.CORBA.portable.Streamable
    public void _read(InputStream inputStream) {
        this.value = ObjectReferenceTemplateHelper.read(inputStream);
    }

    @Override // org.omg.CORBA.portable.Streamable
    public void _write(OutputStream outputStream) {
        ObjectReferenceTemplateHelper.write(outputStream, this.value);
    }

    @Override // org.omg.CORBA.portable.Streamable
    public TypeCode _type() {
        return ObjectReferenceTemplateHelper.type();
    }
}
