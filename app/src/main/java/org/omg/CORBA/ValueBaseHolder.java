package org.omg.CORBA;

import java.io.Serializable;
import org.omg.CORBA.portable.InputStream;
import org.omg.CORBA.portable.OutputStream;
import org.omg.CORBA.portable.Streamable;

/* loaded from: rt.jar:org/omg/CORBA/ValueBaseHolder.class */
public final class ValueBaseHolder implements Streamable {
    public Serializable value;

    public ValueBaseHolder() {
    }

    public ValueBaseHolder(Serializable serializable) {
        this.value = serializable;
    }

    @Override // org.omg.CORBA.portable.Streamable
    public void _read(InputStream inputStream) {
        this.value = ((org.omg.CORBA_2_3.portable.InputStream) inputStream).read_value();
    }

    @Override // org.omg.CORBA.portable.Streamable
    public void _write(OutputStream outputStream) {
        ((org.omg.CORBA_2_3.portable.OutputStream) outputStream).write_value(this.value);
    }

    @Override // org.omg.CORBA.portable.Streamable
    public TypeCode _type() {
        return ORB.init().get_primitive_tc(TCKind.tk_value);
    }
}
