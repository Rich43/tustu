package org.omg.CORBA;

import org.omg.CORBA.portable.InputStream;
import org.omg.CORBA.portable.OutputStream;
import org.omg.CORBA.portable.Streamable;

/* loaded from: rt.jar:org/omg/CORBA/DoubleSeqHolder.class */
public final class DoubleSeqHolder implements Streamable {
    public double[] value;

    public DoubleSeqHolder() {
        this.value = null;
    }

    public DoubleSeqHolder(double[] dArr) {
        this.value = null;
        this.value = dArr;
    }

    @Override // org.omg.CORBA.portable.Streamable
    public void _read(InputStream inputStream) {
        this.value = DoubleSeqHelper.read(inputStream);
    }

    @Override // org.omg.CORBA.portable.Streamable
    public void _write(OutputStream outputStream) {
        DoubleSeqHelper.write(outputStream, this.value);
    }

    @Override // org.omg.CORBA.portable.Streamable
    public TypeCode _type() {
        return DoubleSeqHelper.type();
    }
}
