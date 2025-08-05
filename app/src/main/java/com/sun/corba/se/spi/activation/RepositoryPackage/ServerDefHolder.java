package com.sun.corba.se.spi.activation.RepositoryPackage;

import org.omg.CORBA.TypeCode;
import org.omg.CORBA.portable.InputStream;
import org.omg.CORBA.portable.OutputStream;
import org.omg.CORBA.portable.Streamable;

/* loaded from: rt.jar:com/sun/corba/se/spi/activation/RepositoryPackage/ServerDefHolder.class */
public final class ServerDefHolder implements Streamable {
    public ServerDef value;

    public ServerDefHolder() {
        this.value = null;
    }

    public ServerDefHolder(ServerDef serverDef) {
        this.value = null;
        this.value = serverDef;
    }

    @Override // org.omg.CORBA.portable.Streamable
    public void _read(InputStream inputStream) {
        this.value = ServerDefHelper.read(inputStream);
    }

    @Override // org.omg.CORBA.portable.Streamable
    public void _write(OutputStream outputStream) {
        ServerDefHelper.write(outputStream, this.value);
    }

    @Override // org.omg.CORBA.portable.Streamable
    public TypeCode _type() {
        return ServerDefHelper.type();
    }
}
