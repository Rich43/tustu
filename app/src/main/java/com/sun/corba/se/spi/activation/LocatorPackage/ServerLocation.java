package com.sun.corba.se.spi.activation.LocatorPackage;

import com.sun.corba.se.spi.activation.ORBPortInfo;
import org.omg.CORBA.portable.IDLEntity;

/* loaded from: rt.jar:com/sun/corba/se/spi/activation/LocatorPackage/ServerLocation.class */
public final class ServerLocation implements IDLEntity {
    public String hostname;
    public ORBPortInfo[] ports;

    public ServerLocation() {
        this.hostname = null;
        this.ports = null;
    }

    public ServerLocation(String str, ORBPortInfo[] oRBPortInfoArr) {
        this.hostname = null;
        this.ports = null;
        this.hostname = str;
        this.ports = oRBPortInfoArr;
    }
}
