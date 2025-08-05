package org.omg.PortableServer.ServantLocatorPackage;

import org.omg.CORBA.NO_IMPLEMENT;
import org.omg.CORBA.TypeCode;
import org.omg.CORBA.portable.InputStream;
import org.omg.CORBA.portable.OutputStream;
import org.omg.CORBA.portable.Streamable;

/* loaded from: rt.jar:org/omg/PortableServer/ServantLocatorPackage/CookieHolder.class */
public final class CookieHolder implements Streamable {
    public Object value;

    public CookieHolder() {
    }

    public CookieHolder(Object obj) {
        this.value = obj;
    }

    @Override // org.omg.CORBA.portable.Streamable
    public void _read(InputStream inputStream) {
        throw new NO_IMPLEMENT();
    }

    @Override // org.omg.CORBA.portable.Streamable
    public void _write(OutputStream outputStream) {
        throw new NO_IMPLEMENT();
    }

    @Override // org.omg.CORBA.portable.Streamable
    public TypeCode _type() {
        throw new NO_IMPLEMENT();
    }
}
