package com.sun.corba.se.impl.transport;

import com.sun.corba.se.impl.encoding.CDROutputObject;
import com.sun.corba.se.impl.logging.ORBUtilSystemException;
import com.sun.corba.se.impl.protocol.CorbaMessageMediatorImpl;
import com.sun.corba.se.impl.protocol.SharedCDRClientRequestDispatcherImpl;
import com.sun.corba.se.pept.broker.Broker;
import com.sun.corba.se.pept.encoding.OutputObject;
import com.sun.corba.se.pept.protocol.ClientRequestDispatcher;
import com.sun.corba.se.pept.protocol.MessageMediator;
import com.sun.corba.se.pept.transport.Connection;
import com.sun.corba.se.pept.transport.ContactInfo;
import com.sun.corba.se.spi.ior.IOR;
import com.sun.corba.se.spi.ior.iiop.GIOPVersion;
import com.sun.corba.se.spi.logging.CORBALogDomains;
import com.sun.corba.se.spi.orb.ORB;
import com.sun.corba.se.spi.protocol.CorbaMessageMediator;
import com.sun.corba.se.spi.transport.CorbaContactInfoList;
import sun.corba.OutputStreamFactory;

/* loaded from: rt.jar:com/sun/corba/se/impl/transport/SharedCDRContactInfoImpl.class */
public class SharedCDRContactInfoImpl extends CorbaContactInfoBase {
    private static int requestId = 0;
    protected ORBUtilSystemException wrapper;

    public SharedCDRContactInfoImpl(ORB orb, CorbaContactInfoList corbaContactInfoList, IOR ior, short s2) {
        this.orb = orb;
        this.contactInfoList = corbaContactInfoList;
        this.effectiveTargetIOR = ior;
        this.addressingDisposition = s2;
    }

    @Override // com.sun.corba.se.impl.transport.CorbaContactInfoBase, com.sun.corba.se.pept.transport.ContactInfo
    public ClientRequestDispatcher getClientRequestDispatcher() {
        return new SharedCDRClientRequestDispatcherImpl();
    }

    @Override // com.sun.corba.se.pept.transport.ContactInfo
    public boolean isConnectionBased() {
        return false;
    }

    @Override // com.sun.corba.se.pept.transport.ContactInfo
    public boolean shouldCacheConnection() {
        return false;
    }

    @Override // com.sun.corba.se.pept.transport.ContactInfo
    public String getConnectionCacheType() {
        throw getWrapper().methodShouldNotBeCalled();
    }

    @Override // com.sun.corba.se.pept.transport.ContactInfo
    public Connection createConnection() {
        throw getWrapper().methodShouldNotBeCalled();
    }

    @Override // com.sun.corba.se.impl.transport.CorbaContactInfoBase, com.sun.corba.se.pept.transport.ContactInfo
    public MessageMediator createMessageMediator(Broker broker, ContactInfo contactInfo, Connection connection, String str, boolean z2) {
        if (connection != null) {
            throw new RuntimeException("connection is not null");
        }
        GIOPVersion gIOPVersionChooseRequestVersion = GIOPVersion.chooseRequestVersion((ORB) broker, this.effectiveTargetIOR);
        IOR ior = this.effectiveTargetIOR;
        int i2 = requestId;
        requestId = i2 + 1;
        return new CorbaMessageMediatorImpl((ORB) broker, contactInfo, null, gIOPVersionChooseRequestVersion, ior, i2, getAddressingDisposition(), str, z2);
    }

    @Override // com.sun.corba.se.impl.transport.CorbaContactInfoBase, com.sun.corba.se.pept.transport.ContactInfo
    public OutputObject createOutputObject(MessageMediator messageMediator) {
        CorbaMessageMediator corbaMessageMediator = (CorbaMessageMediator) messageMediator;
        CDROutputObject cDROutputObjectNewCDROutputObject = OutputStreamFactory.newCDROutputObject(this.orb, messageMediator, corbaMessageMediator.getRequestHeader(), corbaMessageMediator.getStreamFormatVersion(), 0);
        messageMediator.setOutputObject(cDROutputObjectNewCDROutputObject);
        return cDROutputObjectNewCDROutputObject;
    }

    @Override // com.sun.corba.se.spi.transport.CorbaContactInfo
    public String getMonitoringName() {
        throw getWrapper().methodShouldNotBeCalled();
    }

    @Override // com.sun.corba.se.impl.transport.CorbaContactInfoBase
    public String toString() {
        return "SharedCDRContactInfoImpl[]";
    }

    protected ORBUtilSystemException getWrapper() {
        if (this.wrapper == null) {
            this.wrapper = ORBUtilSystemException.get(this.orb, CORBALogDomains.RPC_TRANSPORT);
        }
        return this.wrapper;
    }
}
