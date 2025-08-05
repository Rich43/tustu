package org.omg.CosNaming.NamingContextExtPackage;

import org.omg.CORBA.TypeCode;
import org.omg.CORBA.portable.InputStream;
import org.omg.CORBA.portable.OutputStream;
import org.omg.CORBA.portable.Streamable;

/* loaded from: rt.jar:org/omg/CosNaming/NamingContextExtPackage/InvalidAddressHolder.class */
public final class InvalidAddressHolder implements Streamable {
    public InvalidAddress value;

    public InvalidAddressHolder() {
        this.value = null;
    }

    public InvalidAddressHolder(InvalidAddress invalidAddress) {
        this.value = null;
        this.value = invalidAddress;
    }

    @Override // org.omg.CORBA.portable.Streamable
    public void _read(InputStream inputStream) {
        this.value = InvalidAddressHelper.read(inputStream);
    }

    @Override // org.omg.CORBA.portable.Streamable
    public void _write(OutputStream outputStream) {
        InvalidAddressHelper.write(outputStream, this.value);
    }

    @Override // org.omg.CORBA.portable.Streamable
    public TypeCode _type() {
        return InvalidAddressHelper.type();
    }
}
