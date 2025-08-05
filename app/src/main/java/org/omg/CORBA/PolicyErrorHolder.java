package org.omg.CORBA;

import org.omg.CORBA.portable.InputStream;
import org.omg.CORBA.portable.OutputStream;
import org.omg.CORBA.portable.Streamable;

/* loaded from: rt.jar:org/omg/CORBA/PolicyErrorHolder.class */
public final class PolicyErrorHolder implements Streamable {
    public PolicyError value;

    public PolicyErrorHolder() {
        this.value = null;
    }

    public PolicyErrorHolder(PolicyError policyError) {
        this.value = null;
        this.value = policyError;
    }

    @Override // org.omg.CORBA.portable.Streamable
    public void _read(InputStream inputStream) {
        this.value = PolicyErrorHelper.read(inputStream);
    }

    @Override // org.omg.CORBA.portable.Streamable
    public void _write(OutputStream outputStream) {
        PolicyErrorHelper.write(outputStream, this.value);
    }

    @Override // org.omg.CORBA.portable.Streamable
    public TypeCode _type() {
        return PolicyErrorHelper.type();
    }
}
