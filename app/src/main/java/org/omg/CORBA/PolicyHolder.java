package org.omg.CORBA;

import org.omg.CORBA.portable.InputStream;
import org.omg.CORBA.portable.OutputStream;
import org.omg.CORBA.portable.Streamable;

/* loaded from: rt.jar:org/omg/CORBA/PolicyHolder.class */
public final class PolicyHolder implements Streamable {
    public Policy value;

    public PolicyHolder() {
        this.value = null;
    }

    public PolicyHolder(Policy policy) {
        this.value = null;
        this.value = policy;
    }

    @Override // org.omg.CORBA.portable.Streamable
    public void _read(InputStream inputStream) {
        this.value = PolicyHelper.read(inputStream);
    }

    @Override // org.omg.CORBA.portable.Streamable
    public void _write(OutputStream outputStream) {
        PolicyHelper.write(outputStream, this.value);
    }

    @Override // org.omg.CORBA.portable.Streamable
    public TypeCode _type() {
        return PolicyHelper.type();
    }
}
