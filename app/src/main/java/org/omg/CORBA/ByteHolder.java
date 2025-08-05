package org.omg.CORBA;

import org.omg.CORBA.portable.InputStream;
import org.omg.CORBA.portable.OutputStream;
import org.omg.CORBA.portable.Streamable;

/* loaded from: rt.jar:org/omg/CORBA/ByteHolder.class */
public final class ByteHolder implements Streamable {
    public byte value;

    public ByteHolder() {
    }

    public ByteHolder(byte b2) {
        this.value = b2;
    }

    @Override // org.omg.CORBA.portable.Streamable
    public void _read(InputStream inputStream) {
        this.value = inputStream.read_octet();
    }

    @Override // org.omg.CORBA.portable.Streamable
    public void _write(OutputStream outputStream) {
        outputStream.write_octet(this.value);
    }

    @Override // org.omg.CORBA.portable.Streamable
    public TypeCode _type() {
        return ORB.init().get_primitive_tc(TCKind.tk_octet);
    }
}
