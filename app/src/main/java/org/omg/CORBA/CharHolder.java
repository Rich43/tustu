package org.omg.CORBA;

import org.omg.CORBA.portable.InputStream;
import org.omg.CORBA.portable.OutputStream;
import org.omg.CORBA.portable.Streamable;

/* loaded from: rt.jar:org/omg/CORBA/CharHolder.class */
public final class CharHolder implements Streamable {
    public char value;

    public CharHolder() {
    }

    public CharHolder(char c2) {
        this.value = c2;
    }

    @Override // org.omg.CORBA.portable.Streamable
    public void _read(InputStream inputStream) {
        this.value = inputStream.read_char();
    }

    @Override // org.omg.CORBA.portable.Streamable
    public void _write(OutputStream outputStream) {
        outputStream.write_char(this.value);
    }

    @Override // org.omg.CORBA.portable.Streamable
    public TypeCode _type() {
        return ORB.init().get_primitive_tc(TCKind.tk_char);
    }
}
