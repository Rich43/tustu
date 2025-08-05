package org.omg.CORBA;

import org.omg.CORBA.portable.InputStream;
import org.omg.CORBA.portable.OutputStream;
import org.omg.CORBA.portable.Streamable;

/* loaded from: rt.jar:org/omg/CORBA/PolicyListHolder.class */
public final class PolicyListHolder implements Streamable {
    public Policy[] value;

    public PolicyListHolder() {
        this.value = null;
    }

    public PolicyListHolder(Policy[] policyArr) {
        this.value = null;
        this.value = policyArr;
    }

    @Override // org.omg.CORBA.portable.Streamable
    public void _read(InputStream inputStream) {
        this.value = PolicyListHelper.read(inputStream);
    }

    @Override // org.omg.CORBA.portable.Streamable
    public void _write(OutputStream outputStream) {
        PolicyListHelper.write(outputStream, this.value);
    }

    @Override // org.omg.CORBA.portable.Streamable
    public TypeCode _type() {
        return PolicyListHelper.type();
    }
}
