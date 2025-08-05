package com.sun.corba.se.spi.activation;

import org.omg.CORBA.TypeCode;
import org.omg.CORBA.portable.InputStream;
import org.omg.CORBA.portable.OutputStream;
import org.omg.CORBA.portable.Streamable;

/* loaded from: rt.jar:com/sun/corba/se/spi/activation/ServerAlreadyUninstalledHolder.class */
public final class ServerAlreadyUninstalledHolder implements Streamable {
    public ServerAlreadyUninstalled value;

    public ServerAlreadyUninstalledHolder() {
        this.value = null;
    }

    public ServerAlreadyUninstalledHolder(ServerAlreadyUninstalled serverAlreadyUninstalled) {
        this.value = null;
        this.value = serverAlreadyUninstalled;
    }

    @Override // org.omg.CORBA.portable.Streamable
    public void _read(InputStream inputStream) {
        this.value = ServerAlreadyUninstalledHelper.read(inputStream);
    }

    @Override // org.omg.CORBA.portable.Streamable
    public void _write(OutputStream outputStream) {
        ServerAlreadyUninstalledHelper.write(outputStream, this.value);
    }

    @Override // org.omg.CORBA.portable.Streamable
    public TypeCode _type() {
        return ServerAlreadyUninstalledHelper.type();
    }
}
