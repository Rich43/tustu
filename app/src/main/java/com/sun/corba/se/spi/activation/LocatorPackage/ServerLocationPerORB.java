package com.sun.corba.se.spi.activation.LocatorPackage;

import com.sun.corba.se.spi.activation.EndPointInfo;
import org.omg.CORBA.portable.IDLEntity;

/* loaded from: rt.jar:com/sun/corba/se/spi/activation/LocatorPackage/ServerLocationPerORB.class */
public final class ServerLocationPerORB implements IDLEntity {
    public String hostname;
    public EndPointInfo[] ports;

    public ServerLocationPerORB() {
        this.hostname = null;
        this.ports = null;
    }

    public ServerLocationPerORB(String str, EndPointInfo[] endPointInfoArr) {
        this.hostname = null;
        this.ports = null;
        this.hostname = str;
        this.ports = endPointInfoArr;
    }
}
