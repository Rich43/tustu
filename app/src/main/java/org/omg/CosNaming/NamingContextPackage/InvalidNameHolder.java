package org.omg.CosNaming.NamingContextPackage;

import org.omg.CORBA.TypeCode;
import org.omg.CORBA.portable.InputStream;
import org.omg.CORBA.portable.OutputStream;
import org.omg.CORBA.portable.Streamable;

/* loaded from: rt.jar:org/omg/CosNaming/NamingContextPackage/InvalidNameHolder.class */
public final class InvalidNameHolder implements Streamable {
    public InvalidName value;

    public InvalidNameHolder() {
        this.value = null;
    }

    public InvalidNameHolder(InvalidName invalidName) {
        this.value = null;
        this.value = invalidName;
    }

    @Override // org.omg.CORBA.portable.Streamable
    public void _read(InputStream inputStream) {
        this.value = InvalidNameHelper.read(inputStream);
    }

    @Override // org.omg.CORBA.portable.Streamable
    public void _write(OutputStream outputStream) {
        InvalidNameHelper.write(outputStream, this.value);
    }

    @Override // org.omg.CORBA.portable.Streamable
    public TypeCode _type() {
        return InvalidNameHelper.type();
    }
}
