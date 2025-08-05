package com.sun.corba.se.spi.activation;

import org.omg.CORBA.TypeCode;
import org.omg.CORBA.portable.InputStream;
import org.omg.CORBA.portable.OutputStream;
import org.omg.CORBA.portable.Streamable;

/* loaded from: rt.jar:com/sun/corba/se/spi/activation/ServerHeldDownHolder.class */
public final class ServerHeldDownHolder implements Streamable {
    public ServerHeldDown value;

    public ServerHeldDownHolder() {
        this.value = null;
    }

    public ServerHeldDownHolder(ServerHeldDown serverHeldDown) {
        this.value = null;
        this.value = serverHeldDown;
    }

    @Override // org.omg.CORBA.portable.Streamable
    public void _read(InputStream inputStream) {
        this.value = ServerHeldDownHelper.read(inputStream);
    }

    @Override // org.omg.CORBA.portable.Streamable
    public void _write(OutputStream outputStream) {
        ServerHeldDownHelper.write(outputStream, this.value);
    }

    @Override // org.omg.CORBA.portable.Streamable
    public TypeCode _type() {
        return ServerHeldDownHelper.type();
    }
}
