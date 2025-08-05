package com.sun.corba.se.spi.activation;

import org.omg.CORBA.portable.IDLEntity;

/* loaded from: rt.jar:com/sun/corba/se/spi/activation/EndPointInfo.class */
public final class EndPointInfo implements IDLEntity {
    public String endpointType;
    public int port;

    public EndPointInfo() {
        this.endpointType = null;
        this.port = 0;
    }

    public EndPointInfo(String str, int i2) {
        this.endpointType = null;
        this.port = 0;
        this.endpointType = str;
        this.port = i2;
    }
}
