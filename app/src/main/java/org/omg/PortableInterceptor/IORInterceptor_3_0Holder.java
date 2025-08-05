package org.omg.PortableInterceptor;

import org.omg.CORBA.TypeCode;
import org.omg.CORBA.portable.InputStream;
import org.omg.CORBA.portable.OutputStream;
import org.omg.CORBA.portable.Streamable;

/* loaded from: rt.jar:org/omg/PortableInterceptor/IORInterceptor_3_0Holder.class */
public final class IORInterceptor_3_0Holder implements Streamable {
    public IORInterceptor_3_0 value;

    public IORInterceptor_3_0Holder() {
        this.value = null;
    }

    public IORInterceptor_3_0Holder(IORInterceptor_3_0 iORInterceptor_3_0) {
        this.value = null;
        this.value = iORInterceptor_3_0;
    }

    @Override // org.omg.CORBA.portable.Streamable
    public void _read(InputStream inputStream) {
        this.value = IORInterceptor_3_0Helper.read(inputStream);
    }

    @Override // org.omg.CORBA.portable.Streamable
    public void _write(OutputStream outputStream) {
        IORInterceptor_3_0Helper.write(outputStream, this.value);
    }

    @Override // org.omg.CORBA.portable.Streamable
    public TypeCode _type() {
        return IORInterceptor_3_0Helper.type();
    }
}
