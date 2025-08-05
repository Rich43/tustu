package org.omg.CORBA;

import org.omg.CORBA.portable.InputStream;
import org.omg.CORBA.portable.OutputStream;
import org.omg.CORBA.portable.Streamable;

/* loaded from: rt.jar:org/omg/CORBA/ShortHolder.class */
public final class ShortHolder implements Streamable {
    public short value;

    public ShortHolder() {
    }

    public ShortHolder(short s2) {
        this.value = s2;
    }

    @Override // org.omg.CORBA.portable.Streamable
    public void _read(InputStream inputStream) {
        this.value = inputStream.read_short();
    }

    @Override // org.omg.CORBA.portable.Streamable
    public void _write(OutputStream outputStream) {
        outputStream.write_short(this.value);
    }

    @Override // org.omg.CORBA.portable.Streamable
    public TypeCode _type() {
        return ORB.init().get_primitive_tc(TCKind.tk_short);
    }
}
