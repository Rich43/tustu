package org.omg.CosNaming.NamingContextPackage;

import org.omg.CORBA.TypeCode;
import org.omg.CORBA.portable.InputStream;
import org.omg.CORBA.portable.OutputStream;
import org.omg.CORBA.portable.Streamable;

/* loaded from: rt.jar:org/omg/CosNaming/NamingContextPackage/CannotProceedHolder.class */
public final class CannotProceedHolder implements Streamable {
    public CannotProceed value;

    public CannotProceedHolder() {
        this.value = null;
    }

    public CannotProceedHolder(CannotProceed cannotProceed) {
        this.value = null;
        this.value = cannotProceed;
    }

    @Override // org.omg.CORBA.portable.Streamable
    public void _read(InputStream inputStream) {
        this.value = CannotProceedHelper.read(inputStream);
    }

    @Override // org.omg.CORBA.portable.Streamable
    public void _write(OutputStream outputStream) {
        CannotProceedHelper.write(outputStream, this.value);
    }

    @Override // org.omg.CORBA.portable.Streamable
    public TypeCode _type() {
        return CannotProceedHelper.type();
    }
}
