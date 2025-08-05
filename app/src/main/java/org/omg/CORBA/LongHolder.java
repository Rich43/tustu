package org.omg.CORBA;

import org.omg.CORBA.portable.InputStream;
import org.omg.CORBA.portable.OutputStream;
import org.omg.CORBA.portable.Streamable;

/* loaded from: rt.jar:org/omg/CORBA/LongHolder.class */
public final class LongHolder implements Streamable {
    public long value;

    public LongHolder() {
    }

    public LongHolder(long j2) {
        this.value = j2;
    }

    @Override // org.omg.CORBA.portable.Streamable
    public void _read(InputStream inputStream) {
        this.value = inputStream.read_longlong();
    }

    @Override // org.omg.CORBA.portable.Streamable
    public void _write(OutputStream outputStream) {
        outputStream.write_longlong(this.value);
    }

    @Override // org.omg.CORBA.portable.Streamable
    public TypeCode _type() {
        return ORB.init().get_primitive_tc(TCKind.tk_longlong);
    }
}
