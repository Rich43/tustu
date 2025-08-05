package com.sun.corba.se.spi.activation;

import org.omg.CORBA.TypeCode;
import org.omg.CORBA.portable.InputStream;
import org.omg.CORBA.portable.OutputStream;
import org.omg.CORBA.portable.Streamable;

/* loaded from: rt.jar:com/sun/corba/se/spi/activation/ServerAlreadyActiveHolder.class */
public final class ServerAlreadyActiveHolder implements Streamable {
    public ServerAlreadyActive value;

    public ServerAlreadyActiveHolder() {
        this.value = null;
    }

    public ServerAlreadyActiveHolder(ServerAlreadyActive serverAlreadyActive) {
        this.value = null;
        this.value = serverAlreadyActive;
    }

    @Override // org.omg.CORBA.portable.Streamable
    public void _read(InputStream inputStream) {
        this.value = ServerAlreadyActiveHelper.read(inputStream);
    }

    @Override // org.omg.CORBA.portable.Streamable
    public void _write(OutputStream outputStream) {
        ServerAlreadyActiveHelper.write(outputStream, this.value);
    }

    @Override // org.omg.CORBA.portable.Streamable
    public TypeCode _type() {
        return ServerAlreadyActiveHelper.type();
    }
}
