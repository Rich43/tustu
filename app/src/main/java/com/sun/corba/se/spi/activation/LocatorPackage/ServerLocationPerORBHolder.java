package com.sun.corba.se.spi.activation.LocatorPackage;

import org.omg.CORBA.TypeCode;
import org.omg.CORBA.portable.InputStream;
import org.omg.CORBA.portable.OutputStream;
import org.omg.CORBA.portable.Streamable;

/* loaded from: rt.jar:com/sun/corba/se/spi/activation/LocatorPackage/ServerLocationPerORBHolder.class */
public final class ServerLocationPerORBHolder implements Streamable {
    public ServerLocationPerORB value;

    public ServerLocationPerORBHolder() {
        this.value = null;
    }

    public ServerLocationPerORBHolder(ServerLocationPerORB serverLocationPerORB) {
        this.value = null;
        this.value = serverLocationPerORB;
    }

    @Override // org.omg.CORBA.portable.Streamable
    public void _read(InputStream inputStream) {
        this.value = ServerLocationPerORBHelper.read(inputStream);
    }

    @Override // org.omg.CORBA.portable.Streamable
    public void _write(OutputStream outputStream) {
        ServerLocationPerORBHelper.write(outputStream, this.value);
    }

    @Override // org.omg.CORBA.portable.Streamable
    public TypeCode _type() {
        return ServerLocationPerORBHelper.type();
    }
}
