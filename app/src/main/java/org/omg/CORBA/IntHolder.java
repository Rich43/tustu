package org.omg.CORBA;

import org.omg.CORBA.portable.InputStream;
import org.omg.CORBA.portable.OutputStream;
import org.omg.CORBA.portable.Streamable;

/* loaded from: rt.jar:org/omg/CORBA/IntHolder.class */
public final class IntHolder implements Streamable {
    public int value;

    public IntHolder() {
    }

    public IntHolder(int i2) {
        this.value = i2;
    }

    @Override // org.omg.CORBA.portable.Streamable
    public void _read(InputStream inputStream) {
        this.value = inputStream.read_long();
    }

    @Override // org.omg.CORBA.portable.Streamable
    public void _write(OutputStream outputStream) {
        outputStream.write_long(this.value);
    }

    @Override // org.omg.CORBA.portable.Streamable
    public TypeCode _type() {
        return ORB.init().get_primitive_tc(TCKind.tk_long);
    }
}
