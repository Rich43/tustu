package org.omg.CORBA;

import org.omg.CORBA.portable.InputStream;
import org.omg.CORBA.portable.OutputStream;
import org.omg.CORBA.portable.Streamable;

/* loaded from: rt.jar:org/omg/CORBA/CurrentHolder.class */
public final class CurrentHolder implements Streamable {
    public Current value;

    public CurrentHolder() {
        this.value = null;
    }

    public CurrentHolder(Current current) {
        this.value = null;
        this.value = current;
    }

    @Override // org.omg.CORBA.portable.Streamable
    public void _read(InputStream inputStream) {
        this.value = CurrentHelper.read(inputStream);
    }

    @Override // org.omg.CORBA.portable.Streamable
    public void _write(OutputStream outputStream) {
        CurrentHelper.write(outputStream, this.value);
    }

    @Override // org.omg.CORBA.portable.Streamable
    public TypeCode _type() {
        return CurrentHelper.type();
    }
}
