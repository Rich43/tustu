package org.omg.CosNaming.NamingContextPackage;

import org.omg.CORBA.TypeCode;
import org.omg.CORBA.portable.InputStream;
import org.omg.CORBA.portable.OutputStream;
import org.omg.CORBA.portable.Streamable;

/* loaded from: rt.jar:org/omg/CosNaming/NamingContextPackage/NotFoundReasonHolder.class */
public final class NotFoundReasonHolder implements Streamable {
    public NotFoundReason value;

    public NotFoundReasonHolder() {
        this.value = null;
    }

    public NotFoundReasonHolder(NotFoundReason notFoundReason) {
        this.value = null;
        this.value = notFoundReason;
    }

    @Override // org.omg.CORBA.portable.Streamable
    public void _read(InputStream inputStream) {
        this.value = NotFoundReasonHelper.read(inputStream);
    }

    @Override // org.omg.CORBA.portable.Streamable
    public void _write(OutputStream outputStream) {
        NotFoundReasonHelper.write(outputStream, this.value);
    }

    @Override // org.omg.CORBA.portable.Streamable
    public TypeCode _type() {
        return NotFoundReasonHelper.type();
    }
}
