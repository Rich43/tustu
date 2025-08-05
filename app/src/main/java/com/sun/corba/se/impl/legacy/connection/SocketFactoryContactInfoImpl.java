package com.sun.corba.se.impl.legacy.connection;

import com.sun.corba.se.impl.logging.ORBUtilSystemException;
import com.sun.corba.se.impl.transport.SocketOrChannelContactInfoImpl;
import com.sun.corba.se.pept.transport.Connection;
import com.sun.corba.se.spi.ior.IOR;
import com.sun.corba.se.spi.logging.CORBALogDomains;
import com.sun.corba.se.spi.orb.ORB;
import com.sun.corba.se.spi.transport.CorbaContactInfoList;
import com.sun.corba.se.spi.transport.SocketInfo;

/* loaded from: rt.jar:com/sun/corba/se/impl/legacy/connection/SocketFactoryContactInfoImpl.class */
public class SocketFactoryContactInfoImpl extends SocketOrChannelContactInfoImpl {
    protected ORBUtilSystemException wrapper;
    protected SocketInfo socketInfo;

    public SocketFactoryContactInfoImpl() {
    }

    public SocketFactoryContactInfoImpl(ORB orb, CorbaContactInfoList corbaContactInfoList, IOR ior, short s2, SocketInfo socketInfo) {
        super(orb, corbaContactInfoList);
        this.effectiveTargetIOR = ior;
        this.addressingDisposition = s2;
        this.wrapper = ORBUtilSystemException.get(orb, CORBALogDomains.RPC_TRANSPORT);
        this.socketInfo = orb.getORBData().getLegacySocketFactory().getEndPointInfo(orb, ior, socketInfo);
        this.socketType = this.socketInfo.getType();
        this.hostname = this.socketInfo.getHost();
        this.port = this.socketInfo.getPort();
    }

    @Override // com.sun.corba.se.impl.transport.SocketOrChannelContactInfoImpl, com.sun.corba.se.pept.transport.ContactInfo
    public Connection createConnection() {
        return new SocketFactoryConnectionImpl(this.orb, this, this.orb.getORBData().connectionSocketUseSelectThreadToWait(), this.orb.getORBData().connectionSocketUseWorkerThreadForEvent());
    }

    @Override // com.sun.corba.se.impl.transport.SocketOrChannelContactInfoImpl, com.sun.corba.se.impl.transport.CorbaContactInfoBase
    public String toString() {
        return "SocketFactoryContactInfoImpl[" + this.socketType + " " + this.hostname + " " + this.port + "]";
    }
}
