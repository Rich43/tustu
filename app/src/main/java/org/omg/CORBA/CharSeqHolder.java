package org.omg.CORBA;

import org.omg.CORBA.portable.InputStream;
import org.omg.CORBA.portable.OutputStream;
import org.omg.CORBA.portable.Streamable;

/* loaded from: rt.jar:org/omg/CORBA/CharSeqHolder.class */
public final class CharSeqHolder implements Streamable {
    public char[] value;

    public CharSeqHolder() {
        this.value = null;
    }

    public CharSeqHolder(char[] cArr) {
        this.value = null;
        this.value = cArr;
    }

    @Override // org.omg.CORBA.portable.Streamable
    public void _read(InputStream inputStream) {
        this.value = CharSeqHelper.read(inputStream);
    }

    @Override // org.omg.CORBA.portable.Streamable
    public void _write(OutputStream outputStream) {
        CharSeqHelper.write(outputStream, this.value);
    }

    @Override // org.omg.CORBA.portable.Streamable
    public TypeCode _type() {
        return CharSeqHelper.type();
    }
}
