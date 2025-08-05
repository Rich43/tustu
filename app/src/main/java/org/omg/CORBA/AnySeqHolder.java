package org.omg.CORBA;

import org.omg.CORBA.portable.InputStream;
import org.omg.CORBA.portable.OutputStream;
import org.omg.CORBA.portable.Streamable;

/* loaded from: rt.jar:org/omg/CORBA/AnySeqHolder.class */
public final class AnySeqHolder implements Streamable {
    public Any[] value;

    public AnySeqHolder() {
        this.value = null;
    }

    public AnySeqHolder(Any[] anyArr) {
        this.value = null;
        this.value = anyArr;
    }

    @Override // org.omg.CORBA.portable.Streamable
    public void _read(InputStream inputStream) {
        this.value = AnySeqHelper.read(inputStream);
    }

    @Override // org.omg.CORBA.portable.Streamable
    public void _write(OutputStream outputStream) {
        AnySeqHelper.write(outputStream, this.value);
    }

    @Override // org.omg.CORBA.portable.Streamable
    public TypeCode _type() {
        return AnySeqHelper.type();
    }
}
