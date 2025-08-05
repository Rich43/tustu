package org.omg.CosNaming.NamingContextPackage;

import org.omg.CORBA.TypeCode;
import org.omg.CORBA.portable.InputStream;
import org.omg.CORBA.portable.OutputStream;
import org.omg.CORBA.portable.Streamable;

/* loaded from: rt.jar:org/omg/CosNaming/NamingContextPackage/AlreadyBoundHolder.class */
public final class AlreadyBoundHolder implements Streamable {
    public AlreadyBound value;

    public AlreadyBoundHolder() {
        this.value = null;
    }

    public AlreadyBoundHolder(AlreadyBound alreadyBound) {
        this.value = null;
        this.value = alreadyBound;
    }

    @Override // org.omg.CORBA.portable.Streamable
    public void _read(InputStream inputStream) {
        this.value = AlreadyBoundHelper.read(inputStream);
    }

    @Override // org.omg.CORBA.portable.Streamable
    public void _write(OutputStream outputStream) {
        AlreadyBoundHelper.write(outputStream, this.value);
    }

    @Override // org.omg.CORBA.portable.Streamable
    public TypeCode _type() {
        return AlreadyBoundHelper.type();
    }
}
