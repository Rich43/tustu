package org.omg.CORBA;

import org.omg.CORBA.portable.InputStream;
import org.omg.CORBA.portable.OutputStream;
import org.omg.CORBA.portable.Streamable;

/* loaded from: rt.jar:org/omg/CORBA/BooleanSeqHolder.class */
public final class BooleanSeqHolder implements Streamable {
    public boolean[] value;

    public BooleanSeqHolder() {
        this.value = null;
    }

    public BooleanSeqHolder(boolean[] zArr) {
        this.value = null;
        this.value = zArr;
    }

    @Override // org.omg.CORBA.portable.Streamable
    public void _read(InputStream inputStream) {
        this.value = BooleanSeqHelper.read(inputStream);
    }

    @Override // org.omg.CORBA.portable.Streamable
    public void _write(OutputStream outputStream) {
        BooleanSeqHelper.write(outputStream, this.value);
    }

    @Override // org.omg.CORBA.portable.Streamable
    public TypeCode _type() {
        return BooleanSeqHelper.type();
    }
}
