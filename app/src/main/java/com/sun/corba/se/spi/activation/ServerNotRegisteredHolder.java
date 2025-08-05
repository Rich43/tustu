package com.sun.corba.se.spi.activation;

import org.omg.CORBA.TypeCode;
import org.omg.CORBA.portable.InputStream;
import org.omg.CORBA.portable.OutputStream;
import org.omg.CORBA.portable.Streamable;

/* loaded from: rt.jar:com/sun/corba/se/spi/activation/ServerNotRegisteredHolder.class */
public final class ServerNotRegisteredHolder implements Streamable {
    public ServerNotRegistered value;

    public ServerNotRegisteredHolder() {
        this.value = null;
    }

    public ServerNotRegisteredHolder(ServerNotRegistered serverNotRegistered) {
        this.value = null;
        this.value = serverNotRegistered;
    }

    @Override // org.omg.CORBA.portable.Streamable
    public void _read(InputStream inputStream) {
        this.value = ServerNotRegisteredHelper.read(inputStream);
    }

    @Override // org.omg.CORBA.portable.Streamable
    public void _write(OutputStream outputStream) {
        ServerNotRegisteredHelper.write(outputStream, this.value);
    }

    @Override // org.omg.CORBA.portable.Streamable
    public TypeCode _type() {
        return ServerNotRegisteredHelper.type();
    }
}
