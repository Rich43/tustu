package com.sun.corba.se.impl.transport;

import com.sun.corba.se.impl.orbutil.ORBUtility;
import com.sun.corba.se.pept.transport.Connection;
import com.sun.corba.se.spi.ior.IOR;
import com.sun.corba.se.spi.orb.ORB;
import com.sun.corba.se.spi.transport.CorbaContactInfoList;
import com.sun.corba.se.spi.transport.CorbaTransportManager;
import com.sun.corba.se.spi.transport.SocketInfo;

/* loaded from: rt.jar:com/sun/corba/se/impl/transport/SocketOrChannelContactInfoImpl.class */
public class SocketOrChannelContactInfoImpl extends CorbaContactInfoBase implements SocketInfo {
    protected boolean isHashCodeCached;
    protected int cachedHashCode;
    protected String socketType;
    protected String hostname;
    protected int port;

    protected SocketOrChannelContactInfoImpl() {
        this.isHashCodeCached = false;
    }

    protected SocketOrChannelContactInfoImpl(ORB orb, CorbaContactInfoList corbaContactInfoList) {
        this.isHashCodeCached = false;
        this.orb = orb;
        this.contactInfoList = corbaContactInfoList;
    }

    public SocketOrChannelContactInfoImpl(ORB orb, CorbaContactInfoList corbaContactInfoList, String str, String str2, int i2) {
        this(orb, corbaContactInfoList);
        this.socketType = str;
        this.hostname = str2;
        this.port = i2;
    }

    public SocketOrChannelContactInfoImpl(ORB orb, CorbaContactInfoList corbaContactInfoList, IOR ior, short s2, String str, String str2, int i2) {
        this(orb, corbaContactInfoList, str, str2, i2);
        this.effectiveTargetIOR = ior;
        this.addressingDisposition = s2;
    }

    @Override // com.sun.corba.se.pept.transport.ContactInfo
    public boolean isConnectionBased() {
        return true;
    }

    @Override // com.sun.corba.se.pept.transport.ContactInfo
    public boolean shouldCacheConnection() {
        return true;
    }

    @Override // com.sun.corba.se.pept.transport.ContactInfo
    public String getConnectionCacheType() {
        return CorbaTransportManager.SOCKET_OR_CHANNEL_CONNECTION_CACHE;
    }

    @Override // com.sun.corba.se.pept.transport.ContactInfo
    public Connection createConnection() {
        return new SocketOrChannelConnectionImpl(this.orb, this, this.socketType, this.hostname, this.port);
    }

    @Override // com.sun.corba.se.spi.transport.CorbaContactInfo
    public String getMonitoringName() {
        return "SocketConnections";
    }

    @Override // com.sun.corba.se.spi.transport.SocketInfo, com.sun.corba.se.spi.legacy.connection.LegacyServerSocketEndPointInfo
    public String getType() {
        return this.socketType;
    }

    @Override // com.sun.corba.se.spi.transport.SocketInfo
    public String getHost() {
        return this.hostname;
    }

    @Override // com.sun.corba.se.spi.transport.SocketInfo, com.sun.corba.se.spi.legacy.connection.LegacyServerSocketEndPointInfo
    public int getPort() {
        return this.port;
    }

    @Override // com.sun.corba.se.pept.transport.ContactInfo
    public int hashCode() {
        if (!this.isHashCodeCached) {
            this.cachedHashCode = (this.socketType.hashCode() ^ this.hostname.hashCode()) ^ this.port;
            this.isHashCodeCached = true;
        }
        return this.cachedHashCode;
    }

    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof SocketOrChannelContactInfoImpl)) {
            return false;
        }
        SocketOrChannelContactInfoImpl socketOrChannelContactInfoImpl = (SocketOrChannelContactInfoImpl) obj;
        if (this.port != socketOrChannelContactInfoImpl.port || !this.hostname.equals(socketOrChannelContactInfoImpl.hostname)) {
            return false;
        }
        if (this.socketType == null) {
            if (socketOrChannelContactInfoImpl.socketType != null) {
                return false;
            }
            return true;
        }
        if (!this.socketType.equals(socketOrChannelContactInfoImpl.socketType)) {
            return false;
        }
        return true;
    }

    @Override // com.sun.corba.se.impl.transport.CorbaContactInfoBase
    public String toString() {
        return "SocketOrChannelContactInfoImpl[" + this.socketType + " " + this.hostname + " " + this.port + "]";
    }

    @Override // com.sun.corba.se.impl.transport.CorbaContactInfoBase
    protected void dprint(String str) {
        ORBUtility.dprint("SocketOrChannelContactInfoImpl", str);
    }
}
