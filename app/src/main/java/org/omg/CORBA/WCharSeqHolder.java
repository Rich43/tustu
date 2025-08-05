package org.omg.CORBA;

import org.omg.CORBA.portable.InputStream;
import org.omg.CORBA.portable.OutputStream;
import org.omg.CORBA.portable.Streamable;

/* loaded from: rt.jar:org/omg/CORBA/WCharSeqHolder.class */
public final class WCharSeqHolder implements Streamable {
    public char[] value;

    public WCharSeqHolder() {
        this.value = null;
    }

    public WCharSeqHolder(char[] cArr) {
        this.value = null;
        this.value = cArr;
    }

    @Override // org.omg.CORBA.portable.Streamable
    public void _read(InputStream inputStream) {
        this.value = WCharSeqHelper.read(inputStream);
    }

    @Override // org.omg.CORBA.portable.Streamable
    public void _write(OutputStream outputStream) {
        WCharSeqHelper.write(outputStream, this.value);
    }

    @Override // org.omg.CORBA.portable.Streamable
    public TypeCode _type() {
        return WCharSeqHelper.type();
    }
}
