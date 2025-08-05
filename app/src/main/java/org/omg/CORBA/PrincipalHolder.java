package org.omg.CORBA;

import org.omg.CORBA.portable.InputStream;
import org.omg.CORBA.portable.OutputStream;
import org.omg.CORBA.portable.Streamable;

@Deprecated
/* loaded from: rt.jar:org/omg/CORBA/PrincipalHolder.class */
public final class PrincipalHolder implements Streamable {
    public Principal value;

    public PrincipalHolder() {
    }

    public PrincipalHolder(Principal principal) {
        this.value = principal;
    }

    @Override // org.omg.CORBA.portable.Streamable
    public void _read(InputStream inputStream) {
        this.value = inputStream.read_Principal();
    }

    @Override // org.omg.CORBA.portable.Streamable
    public void _write(OutputStream outputStream) {
        outputStream.write_Principal(this.value);
    }

    @Override // org.omg.CORBA.portable.Streamable
    public TypeCode _type() {
        return ORB.init().get_primitive_tc(TCKind.tk_Principal);
    }
}
