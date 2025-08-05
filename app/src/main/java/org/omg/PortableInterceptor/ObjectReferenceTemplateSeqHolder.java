package org.omg.PortableInterceptor;

import org.omg.CORBA.TypeCode;
import org.omg.CORBA.portable.InputStream;
import org.omg.CORBA.portable.OutputStream;
import org.omg.CORBA.portable.Streamable;

/* loaded from: rt.jar:org/omg/PortableInterceptor/ObjectReferenceTemplateSeqHolder.class */
public final class ObjectReferenceTemplateSeqHolder implements Streamable {
    public ObjectReferenceTemplate[] value;

    public ObjectReferenceTemplateSeqHolder() {
        this.value = null;
    }

    public ObjectReferenceTemplateSeqHolder(ObjectReferenceTemplate[] objectReferenceTemplateArr) {
        this.value = null;
        this.value = objectReferenceTemplateArr;
    }

    @Override // org.omg.CORBA.portable.Streamable
    public void _read(InputStream inputStream) {
        this.value = ObjectReferenceTemplateSeqHelper.read(inputStream);
    }

    @Override // org.omg.CORBA.portable.Streamable
    public void _write(OutputStream outputStream) {
        ObjectReferenceTemplateSeqHelper.write(outputStream, this.value);
    }

    @Override // org.omg.CORBA.portable.Streamable
    public TypeCode _type() {
        return ObjectReferenceTemplateSeqHelper.type();
    }
}
