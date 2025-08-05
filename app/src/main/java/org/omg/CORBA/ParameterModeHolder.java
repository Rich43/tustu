package org.omg.CORBA;

import org.omg.CORBA.portable.InputStream;
import org.omg.CORBA.portable.OutputStream;
import org.omg.CORBA.portable.Streamable;

/* loaded from: rt.jar:org/omg/CORBA/ParameterModeHolder.class */
public final class ParameterModeHolder implements Streamable {
    public ParameterMode value;

    public ParameterModeHolder() {
        this.value = null;
    }

    public ParameterModeHolder(ParameterMode parameterMode) {
        this.value = null;
        this.value = parameterMode;
    }

    @Override // org.omg.CORBA.portable.Streamable
    public void _read(InputStream inputStream) {
        this.value = ParameterModeHelper.read(inputStream);
    }

    @Override // org.omg.CORBA.portable.Streamable
    public void _write(OutputStream outputStream) {
        ParameterModeHelper.write(outputStream, this.value);
    }

    @Override // org.omg.CORBA.portable.Streamable
    public TypeCode _type() {
        return ParameterModeHelper.type();
    }
}
