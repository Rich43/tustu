package org.omg.CORBA;

import org.omg.CORBA.portable.InputStream;
import org.omg.CORBA.portable.OutputStream;
import org.omg.CORBA.portable.Streamable;

/* loaded from: rt.jar:org/omg/CORBA/UShortSeqHolder.class */
public final class UShortSeqHolder implements Streamable {
    public short[] value;

    public UShortSeqHolder() {
        this.value = null;
    }

    public UShortSeqHolder(short[] sArr) {
        this.value = null;
        this.value = sArr;
    }

    @Override // org.omg.CORBA.portable.Streamable
    public void _read(InputStream inputStream) {
        this.value = UShortSeqHelper.read(inputStream);
    }

    @Override // org.omg.CORBA.portable.Streamable
    public void _write(OutputStream outputStream) {
        UShortSeqHelper.write(outputStream, this.value);
    }

    @Override // org.omg.CORBA.portable.Streamable
    public TypeCode _type() {
        return UShortSeqHelper.type();
    }
}
