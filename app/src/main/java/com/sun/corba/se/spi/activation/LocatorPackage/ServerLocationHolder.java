package com.sun.corba.se.spi.activation.LocatorPackage;

import org.omg.CORBA.TypeCode;
import org.omg.CORBA.portable.InputStream;
import org.omg.CORBA.portable.OutputStream;
import org.omg.CORBA.portable.Streamable;

/* loaded from: rt.jar:com/sun/corba/se/spi/activation/LocatorPackage/ServerLocationHolder.class */
public final class ServerLocationHolder implements Streamable {
    public ServerLocation value;

    public ServerLocationHolder() {
        this.value = null;
    }

    public ServerLocationHolder(ServerLocation serverLocation) {
        this.value = null;
        this.value = serverLocation;
    }

    @Override // org.omg.CORBA.portable.Streamable
    public void _read(InputStream inputStream) {
        this.value = ServerLocationHelper.read(inputStream);
    }

    @Override // org.omg.CORBA.portable.Streamable
    public void _write(OutputStream outputStream) {
        ServerLocationHelper.write(outputStream, this.value);
    }

    @Override // org.omg.CORBA.portable.Streamable
    public TypeCode _type() {
        return ServerLocationHelper.type();
    }
}
