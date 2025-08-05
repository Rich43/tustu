package org.omg.CORBA;

import org.omg.CORBA.portable.InputStream;
import org.omg.CORBA.portable.OutputStream;
import org.omg.CORBA.portable.Streamable;

/* loaded from: rt.jar:org/omg/CORBA/FloatSeqHolder.class */
public final class FloatSeqHolder implements Streamable {
    public float[] value;

    public FloatSeqHolder() {
        this.value = null;
    }

    public FloatSeqHolder(float[] fArr) {
        this.value = null;
        this.value = fArr;
    }

    @Override // org.omg.CORBA.portable.Streamable
    public void _read(InputStream inputStream) {
        this.value = FloatSeqHelper.read(inputStream);
    }

    @Override // org.omg.CORBA.portable.Streamable
    public void _write(OutputStream outputStream) {
        FloatSeqHelper.write(outputStream, this.value);
    }

    @Override // org.omg.CORBA.portable.Streamable
    public TypeCode _type() {
        return FloatSeqHelper.type();
    }
}
