package com.sun.corba.se.spi.activation;

import org.omg.CORBA.TypeCode;
import org.omg.CORBA.portable.InputStream;
import org.omg.CORBA.portable.OutputStream;
import org.omg.CORBA.portable.Streamable;

/* loaded from: rt.jar:com/sun/corba/se/spi/activation/ServerNotActiveHolder.class */
public final class ServerNotActiveHolder implements Streamable {
    public ServerNotActive value;

    public ServerNotActiveHolder() {
        this.value = null;
    }

    public ServerNotActiveHolder(ServerNotActive serverNotActive) {
        this.value = null;
        this.value = serverNotActive;
    }

    @Override // org.omg.CORBA.portable.Streamable
    public void _read(InputStream inputStream) {
        this.value = ServerNotActiveHelper.read(inputStream);
    }

    @Override // org.omg.CORBA.portable.Streamable
    public void _write(OutputStream outputStream) {
        ServerNotActiveHelper.write(outputStream, this.value);
    }

    @Override // org.omg.CORBA.portable.Streamable
    public TypeCode _type() {
        return ServerNotActiveHelper.type();
    }
}
