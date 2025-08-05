package org.omg.CORBA;

import org.omg.CORBA.portable.InputStream;
import org.omg.CORBA.portable.OutputStream;
import org.omg.CORBA.portable.Streamable;

/* loaded from: rt.jar:org/omg/CORBA/StringSeqHolder.class */
public final class StringSeqHolder implements Streamable {
    public String[] value;

    public StringSeqHolder() {
        this.value = null;
    }

    public StringSeqHolder(String[] strArr) {
        this.value = null;
        this.value = strArr;
    }

    @Override // org.omg.CORBA.portable.Streamable
    public void _read(InputStream inputStream) {
        this.value = StringSeqHelper.read(inputStream);
    }

    @Override // org.omg.CORBA.portable.Streamable
    public void _write(OutputStream outputStream) {
        StringSeqHelper.write(outputStream, this.value);
    }

    @Override // org.omg.CORBA.portable.Streamable
    public TypeCode _type() {
        return StringSeqHelper.type();
    }
}
