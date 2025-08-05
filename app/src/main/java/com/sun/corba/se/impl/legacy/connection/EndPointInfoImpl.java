package com.sun.corba.se.impl.legacy.connection;

import com.sun.corba.se.spi.legacy.connection.LegacyServerSocketEndPointInfo;
import com.sun.corba.se.spi.transport.SocketInfo;

/* loaded from: rt.jar:com/sun/corba/se/impl/legacy/connection/EndPointInfoImpl.class */
public class EndPointInfoImpl implements SocketInfo, LegacyServerSocketEndPointInfo {
    protected String type;
    protected String hostname;
    protected int port;
    protected int locatorPort = -1;
    protected String name = LegacyServerSocketEndPointInfo.NO_NAME;

    public EndPointInfoImpl(String str, int i2, String str2) {
        this.type = str;
        this.port = i2;
        this.hostname = str2;
    }

    @Override // com.sun.corba.se.spi.transport.SocketInfo, com.sun.corba.se.spi.legacy.connection.LegacyServerSocketEndPointInfo
    public String getType() {
        return this.type;
    }

    @Override // com.sun.corba.se.spi.transport.SocketInfo
    public String getHost() {
        return this.hostname;
    }

    @Override // com.sun.corba.se.spi.legacy.connection.LegacyServerSocketEndPointInfo
    public String getHostName() {
        return this.hostname;
    }

    @Override // com.sun.corba.se.spi.transport.SocketInfo, com.sun.corba.se.spi.legacy.connection.LegacyServerSocketEndPointInfo
    public int getPort() {
        return this.port;
    }

    @Override // com.sun.corba.se.spi.legacy.connection.LegacyServerSocketEndPointInfo
    public int getLocatorPort() {
        return this.locatorPort;
    }

    @Override // com.sun.corba.se.spi.legacy.connection.LegacyServerSocketEndPointInfo
    public void setLocatorPort(int i2) {
        this.locatorPort = i2;
    }

    @Override // com.sun.corba.se.spi.legacy.connection.LegacyServerSocketEndPointInfo
    public String getName() {
        return this.name;
    }

    public int hashCode() {
        return (this.type.hashCode() ^ this.hostname.hashCode()) ^ this.port;
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof EndPointInfoImpl)) {
            return false;
        }
        EndPointInfoImpl endPointInfoImpl = (EndPointInfoImpl) obj;
        if (this.type == null) {
            if (endPointInfoImpl.type != null) {
                return false;
            }
        } else if (!this.type.equals(endPointInfoImpl.type)) {
            return false;
        }
        if (this.port != endPointInfoImpl.port || !this.hostname.equals(endPointInfoImpl.hostname)) {
            return false;
        }
        return true;
    }

    public String toString() {
        return this.type + " " + this.name + " " + this.hostname + " " + this.port;
    }
}
