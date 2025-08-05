package org.omg.CORBA;

import org.omg.CORBA.portable.InputStream;
import org.omg.CORBA.portable.OutputStream;
import org.omg.CORBA.portable.Streamable;

/* loaded from: rt.jar:org/omg/CORBA/WrongTransactionHolder.class */
public final class WrongTransactionHolder implements Streamable {
    public WrongTransaction value;

    public WrongTransactionHolder() {
        this.value = null;
    }

    public WrongTransactionHolder(WrongTransaction wrongTransaction) {
        this.value = null;
        this.value = wrongTransaction;
    }

    @Override // org.omg.CORBA.portable.Streamable
    public void _read(InputStream inputStream) {
        this.value = WrongTransactionHelper.read(inputStream);
    }

    @Override // org.omg.CORBA.portable.Streamable
    public void _write(OutputStream outputStream) {
        WrongTransactionHelper.write(outputStream, this.value);
    }

    @Override // org.omg.CORBA.portable.Streamable
    public TypeCode _type() {
        return WrongTransactionHelper.type();
    }
}
