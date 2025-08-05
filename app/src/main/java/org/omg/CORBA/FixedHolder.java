package org.omg.CORBA;

import java.math.BigDecimal;
import org.omg.CORBA.portable.InputStream;
import org.omg.CORBA.portable.OutputStream;
import org.omg.CORBA.portable.Streamable;

/* loaded from: rt.jar:org/omg/CORBA/FixedHolder.class */
public final class FixedHolder implements Streamable {
    public BigDecimal value;

    public FixedHolder() {
    }

    public FixedHolder(BigDecimal bigDecimal) {
        this.value = bigDecimal;
    }

    @Override // org.omg.CORBA.portable.Streamable
    public void _read(InputStream inputStream) {
        this.value = inputStream.read_fixed();
    }

    @Override // org.omg.CORBA.portable.Streamable
    public void _write(OutputStream outputStream) {
        outputStream.write_fixed(this.value);
    }

    @Override // org.omg.CORBA.portable.Streamable
    public TypeCode _type() {
        return ORB.init().get_primitive_tc(TCKind.tk_fixed);
    }
}
