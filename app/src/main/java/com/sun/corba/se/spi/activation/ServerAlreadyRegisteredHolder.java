package com.sun.corba.se.spi.activation;

import org.omg.CORBA.TypeCode;
import org.omg.CORBA.portable.InputStream;
import org.omg.CORBA.portable.OutputStream;
import org.omg.CORBA.portable.Streamable;

/* loaded from: rt.jar:com/sun/corba/se/spi/activation/ServerAlreadyRegisteredHolder.class */
public final class ServerAlreadyRegisteredHolder implements Streamable {
    public ServerAlreadyRegistered value;

    public ServerAlreadyRegisteredHolder() {
        this.value = null;
    }

    public ServerAlreadyRegisteredHolder(ServerAlreadyRegistered serverAlreadyRegistered) {
        this.value = null;
        this.value = serverAlreadyRegistered;
    }

    @Override // org.omg.CORBA.portable.Streamable
    public void _read(InputStream inputStream) {
        this.value = ServerAlreadyRegisteredHelper.read(inputStream);
    }

    @Override // org.omg.CORBA.portable.Streamable
    public void _write(OutputStream outputStream) {
        ServerAlreadyRegisteredHelper.write(outputStream, this.value);
    }

    @Override // org.omg.CORBA.portable.Streamable
    public TypeCode _type() {
        return ServerAlreadyRegisteredHelper.type();
    }
}
