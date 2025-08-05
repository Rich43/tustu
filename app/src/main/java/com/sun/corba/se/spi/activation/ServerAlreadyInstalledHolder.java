package com.sun.corba.se.spi.activation;

import org.omg.CORBA.TypeCode;
import org.omg.CORBA.portable.InputStream;
import org.omg.CORBA.portable.OutputStream;
import org.omg.CORBA.portable.Streamable;

/* loaded from: rt.jar:com/sun/corba/se/spi/activation/ServerAlreadyInstalledHolder.class */
public final class ServerAlreadyInstalledHolder implements Streamable {
    public ServerAlreadyInstalled value;

    public ServerAlreadyInstalledHolder() {
        this.value = null;
    }

    public ServerAlreadyInstalledHolder(ServerAlreadyInstalled serverAlreadyInstalled) {
        this.value = null;
        this.value = serverAlreadyInstalled;
    }

    @Override // org.omg.CORBA.portable.Streamable
    public void _read(InputStream inputStream) {
        this.value = ServerAlreadyInstalledHelper.read(inputStream);
    }

    @Override // org.omg.CORBA.portable.Streamable
    public void _write(OutputStream outputStream) {
        ServerAlreadyInstalledHelper.write(outputStream, this.value);
    }

    @Override // org.omg.CORBA.portable.Streamable
    public TypeCode _type() {
        return ServerAlreadyInstalledHelper.type();
    }
}
