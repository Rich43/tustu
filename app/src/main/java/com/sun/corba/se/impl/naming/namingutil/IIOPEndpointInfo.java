package com.sun.corba.se.impl.naming.namingutil;

import com.sun.corba.se.impl.orbutil.ORBConstants;

/* loaded from: rt.jar:com/sun/corba/se/impl/naming/namingutil/IIOPEndpointInfo.class */
public class IIOPEndpointInfo {
    private int major = 1;
    private int minor = 0;
    private String host = "localhost";
    private int port = ORBConstants.DEFAULT_INS_PORT;

    IIOPEndpointInfo() {
    }

    public void setHost(String str) {
        this.host = str;
    }

    public String getHost() {
        return this.host;
    }

    public void setPort(int i2) {
        this.port = i2;
    }

    public int getPort() {
        return this.port;
    }

    public void setVersion(int i2, int i3) {
        this.major = i2;
        this.minor = i3;
    }

    public int getMajor() {
        return this.major;
    }

    public int getMinor() {
        return this.minor;
    }

    public void dump() {
        System.out.println(" Major -> " + this.major + " Minor -> " + this.minor);
        System.out.println("host -> " + this.host);
        System.out.println("port -> " + this.port);
    }
}
