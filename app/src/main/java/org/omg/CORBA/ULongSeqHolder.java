package org.omg.CORBA;

import org.omg.CORBA.portable.InputStream;
import org.omg.CORBA.portable.OutputStream;
import org.omg.CORBA.portable.Streamable;

/* loaded from: rt.jar:org/omg/CORBA/ULongSeqHolder.class */
public final class ULongSeqHolder implements Streamable {
    public int[] value;

    public ULongSeqHolder() {
        this.value = null;
    }

    public ULongSeqHolder(int[] iArr) {
        this.value = null;
        this.value = iArr;
    }

    @Override // org.omg.CORBA.portable.Streamable
    public void _read(InputStream inputStream) {
        this.value = ULongSeqHelper.read(inputStream);
    }

    @Override // org.omg.CORBA.portable.Streamable
    public void _write(OutputStream outputStream) {
        ULongSeqHelper.write(outputStream, this.value);
    }

    @Override // org.omg.CORBA.portable.Streamable
    public TypeCode _type() {
        return ULongSeqHelper.type();
    }
}
