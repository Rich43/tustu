package com.sun.corba.se.impl.legacy.connection;

import jdk.internal.dynalink.CallSiteDescriptor;

/* loaded from: rt.jar:com/sun/corba/se/impl/legacy/connection/USLPort.class */
public class USLPort {
    private String type;
    private int port;

    public USLPort(String str, int i2) {
        this.type = str;
        this.port = i2;
    }

    public String getType() {
        return this.type;
    }

    public int getPort() {
        return this.port;
    }

    public String toString() {
        return this.type + CallSiteDescriptor.TOKEN_DELIMITER + this.port;
    }
}
