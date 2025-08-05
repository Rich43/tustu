package com.sun.corba.se.spi.activation;

import org.omg.CORBA.TypeCode;
import org.omg.CORBA.portable.InputStream;
import org.omg.CORBA.portable.OutputStream;
import org.omg.CORBA.portable.Streamable;

/* loaded from: rt.jar:com/sun/corba/se/spi/activation/ORBPortInfoListHolder.class */
public final class ORBPortInfoListHolder implements Streamable {
    public ORBPortInfo[] value;

    public ORBPortInfoListHolder() {
        this.value = null;
    }

    public ORBPortInfoListHolder(ORBPortInfo[] oRBPortInfoArr) {
        this.value = null;
        this.value = oRBPortInfoArr;
    }

    @Override // org.omg.CORBA.portable.Streamable
    public void _read(InputStream inputStream) {
        this.value = ORBPortInfoListHelper.read(inputStream);
    }

    @Override // org.omg.CORBA.portable.Streamable
    public void _write(OutputStream outputStream) {
        ORBPortInfoListHelper.write(outputStream, this.value);
    }

    @Override // org.omg.CORBA.portable.Streamable
    public TypeCode _type() {
        return ORBPortInfoListHelper.type();
    }
}
