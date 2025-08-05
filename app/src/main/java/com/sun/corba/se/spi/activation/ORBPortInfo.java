package com.sun.corba.se.spi.activation;

import org.omg.CORBA.portable.IDLEntity;

/* loaded from: rt.jar:com/sun/corba/se/spi/activation/ORBPortInfo.class */
public final class ORBPortInfo implements IDLEntity {
    public String orbId;
    public int port;

    public ORBPortInfo() {
        this.orbId = null;
        this.port = 0;
    }

    public ORBPortInfo(String str, int i2) {
        this.orbId = null;
        this.port = 0;
        this.orbId = str;
        this.port = i2;
    }
}
