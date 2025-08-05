package org.omg.CORBA;

import org.omg.CORBA.portable.InputStream;
import org.omg.CORBA.portable.OutputStream;
import org.omg.CORBA.portable.Streamable;

/* loaded from: rt.jar:org/omg/CORBA/ULongLongSeqHolder.class */
public final class ULongLongSeqHolder implements Streamable {
    public long[] value;

    public ULongLongSeqHolder() {
        this.value = null;
    }

    public ULongLongSeqHolder(long[] jArr) {
        this.value = null;
        this.value = jArr;
    }

    @Override // org.omg.CORBA.portable.Streamable
    public void _read(InputStream inputStream) {
        this.value = ULongLongSeqHelper.read(inputStream);
    }

    @Override // org.omg.CORBA.portable.Streamable
    public void _write(OutputStream outputStream) {
        ULongLongSeqHelper.write(outputStream, this.value);
    }

    @Override // org.omg.CORBA.portable.Streamable
    public TypeCode _type() {
        return ULongLongSeqHelper.type();
    }
}
