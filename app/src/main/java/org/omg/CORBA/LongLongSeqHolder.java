package org.omg.CORBA;

import org.omg.CORBA.portable.InputStream;
import org.omg.CORBA.portable.OutputStream;
import org.omg.CORBA.portable.Streamable;

/* loaded from: rt.jar:org/omg/CORBA/LongLongSeqHolder.class */
public final class LongLongSeqHolder implements Streamable {
    public long[] value;

    public LongLongSeqHolder() {
        this.value = null;
    }

    public LongLongSeqHolder(long[] jArr) {
        this.value = null;
        this.value = jArr;
    }

    @Override // org.omg.CORBA.portable.Streamable
    public void _read(InputStream inputStream) {
        this.value = LongLongSeqHelper.read(inputStream);
    }

    @Override // org.omg.CORBA.portable.Streamable
    public void _write(OutputStream outputStream) {
        LongLongSeqHelper.write(outputStream, this.value);
    }

    @Override // org.omg.CORBA.portable.Streamable
    public TypeCode _type() {
        return LongLongSeqHelper.type();
    }
}
