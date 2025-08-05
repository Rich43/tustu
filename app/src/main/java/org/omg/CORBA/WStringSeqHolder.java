package org.omg.CORBA;

import org.omg.CORBA.portable.InputStream;
import org.omg.CORBA.portable.OutputStream;
import org.omg.CORBA.portable.Streamable;

/* loaded from: rt.jar:org/omg/CORBA/WStringSeqHolder.class */
public final class WStringSeqHolder implements Streamable {
    public String[] value;

    public WStringSeqHolder() {
        this.value = null;
    }

    public WStringSeqHolder(String[] strArr) {
        this.value = null;
        this.value = strArr;
    }

    @Override // org.omg.CORBA.portable.Streamable
    public void _read(InputStream inputStream) {
        this.value = WStringSeqHelper.read(inputStream);
    }

    @Override // org.omg.CORBA.portable.Streamable
    public void _write(OutputStream outputStream) {
        WStringSeqHelper.write(outputStream, this.value);
    }

    @Override // org.omg.CORBA.portable.Streamable
    public TypeCode _type() {
        return WStringSeqHelper.type();
    }
}
