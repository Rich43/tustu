package org.omg.CORBA;

import org.omg.CORBA.portable.InputStream;
import org.omg.CORBA.portable.OutputStream;
import org.omg.CORBA.portable.Streamable;

/* loaded from: rt.jar:org/omg/CORBA/LongSeqHolder.class */
public final class LongSeqHolder implements Streamable {
    public int[] value;

    public LongSeqHolder() {
        this.value = null;
    }

    public LongSeqHolder(int[] iArr) {
        this.value = null;
        this.value = iArr;
    }

    @Override // org.omg.CORBA.portable.Streamable
    public void _read(InputStream inputStream) {
        this.value = LongSeqHelper.read(inputStream);
    }

    @Override // org.omg.CORBA.portable.Streamable
    public void _write(OutputStream outputStream) {
        LongSeqHelper.write(outputStream, this.value);
    }

    @Override // org.omg.CORBA.portable.Streamable
    public TypeCode _type() {
        return LongSeqHelper.type();
    }
}
