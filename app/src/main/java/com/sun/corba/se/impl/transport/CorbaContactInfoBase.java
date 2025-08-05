package com.sun.corba.se.impl.transport;

import com.sun.corba.se.impl.encoding.CDRInputObject;
import com.sun.corba.se.impl.encoding.CDROutputObject;
import com.sun.corba.se.impl.orbutil.ORBUtility;
import com.sun.corba.se.impl.protocol.CorbaMessageMediatorImpl;
import com.sun.corba.se.impl.protocol.giopmsgheaders.Message;
import com.sun.corba.se.impl.protocol.giopmsgheaders.MessageBase;
import com.sun.corba.se.pept.broker.Broker;
import com.sun.corba.se.pept.encoding.InputObject;
import com.sun.corba.se.pept.encoding.OutputObject;
import com.sun.corba.se.pept.protocol.ClientRequestDispatcher;
import com.sun.corba.se.pept.protocol.MessageMediator;
import com.sun.corba.se.pept.transport.Connection;
import com.sun.corba.se.pept.transport.ContactInfo;
import com.sun.corba.se.pept.transport.ContactInfoList;
import com.sun.corba.se.pept.transport.OutboundConnectionCache;
import com.sun.corba.se.spi.ior.IOR;
import com.sun.corba.se.spi.ior.iiop.GIOPVersion;
import com.sun.corba.se.spi.ior.iiop.IIOPProfile;
import com.sun.corba.se.spi.orb.ORB;
import com.sun.corba.se.spi.protocol.CorbaMessageMediator;
import com.sun.corba.se.spi.transport.CorbaConnection;
import com.sun.corba.se.spi.transport.CorbaContactInfo;
import com.sun.corba.se.spi.transport.CorbaContactInfoList;
import java.nio.ByteBuffer;
import sun.corba.OutputStreamFactory;

/* loaded from: rt.jar:com/sun/corba/se/impl/transport/CorbaContactInfoBase.class */
public abstract class CorbaContactInfoBase implements CorbaContactInfo {
    protected ORB orb;
    protected CorbaContactInfoList contactInfoList;
    protected IOR effectiveTargetIOR;
    protected short addressingDisposition;
    protected OutboundConnectionCache connectionCache;

    @Override // com.sun.corba.se.pept.transport.ContactInfo
    public Broker getBroker() {
        return this.orb;
    }

    @Override // com.sun.corba.se.pept.transport.ContactInfo
    public ContactInfoList getContactInfoList() {
        return this.contactInfoList;
    }

    @Override // com.sun.corba.se.pept.transport.ContactInfo
    public ClientRequestDispatcher getClientRequestDispatcher() {
        return this.orb.getRequestDispatcherRegistry().getClientRequestDispatcher(getEffectiveProfile().getObjectKeyTemplate().getSubcontractId());
    }

    @Override // com.sun.corba.se.pept.transport.ContactInfo
    public void setConnectionCache(OutboundConnectionCache outboundConnectionCache) {
        this.connectionCache = outboundConnectionCache;
    }

    @Override // com.sun.corba.se.pept.transport.ContactInfo
    public OutboundConnectionCache getConnectionCache() {
        return this.connectionCache;
    }

    @Override // com.sun.corba.se.pept.transport.ContactInfo
    public MessageMediator createMessageMediator(Broker broker, ContactInfo contactInfo, Connection connection, String str, boolean z2) {
        return new CorbaMessageMediatorImpl((ORB) broker, contactInfo, connection, GIOPVersion.chooseRequestVersion((ORB) broker, this.effectiveTargetIOR), this.effectiveTargetIOR, ((CorbaConnection) connection).getNextRequestId(), getAddressingDisposition(), str, z2);
    }

    @Override // com.sun.corba.se.pept.transport.ContactInfo
    public MessageMediator createMessageMediator(Broker broker, Connection connection) {
        MessageBase gIOPMessage;
        ORB orb = (ORB) broker;
        CorbaConnection corbaConnection = (CorbaConnection) connection;
        if (orb.transportDebugFlag) {
            if (corbaConnection.shouldReadGiopHeaderOnly()) {
                dprint(".createMessageMediator: waiting for message header on connection: " + ((Object) corbaConnection));
            } else {
                dprint(".createMessageMediator: waiting for message on connection: " + ((Object) corbaConnection));
            }
        }
        if (corbaConnection.shouldReadGiopHeaderOnly()) {
            gIOPMessage = MessageBase.readGIOPHeader(orb, corbaConnection);
        } else {
            gIOPMessage = MessageBase.readGIOPMessage(orb, corbaConnection);
        }
        ByteBuffer byteBuffer = gIOPMessage.getByteBuffer();
        gIOPMessage.setByteBuffer(null);
        return new CorbaMessageMediatorImpl(orb, corbaConnection, gIOPMessage, byteBuffer);
    }

    @Override // com.sun.corba.se.pept.transport.ContactInfo
    public MessageMediator finishCreatingMessageMediator(Broker broker, Connection connection, MessageMediator messageMediator) {
        ORB orb = (ORB) broker;
        CorbaConnection corbaConnection = (CorbaConnection) connection;
        CorbaMessageMediator corbaMessageMediator = (CorbaMessageMediator) messageMediator;
        if (orb.transportDebugFlag) {
            dprint(".finishCreatingMessageMediator: waiting for message body on connection: " + ((Object) corbaConnection));
        }
        Message dispatchHeader = corbaMessageMediator.getDispatchHeader();
        dispatchHeader.setByteBuffer(corbaMessageMediator.getDispatchBuffer());
        Message gIOPBody = MessageBase.readGIOPBody(orb, corbaConnection, dispatchHeader);
        ByteBuffer byteBuffer = gIOPBody.getByteBuffer();
        gIOPBody.setByteBuffer(null);
        corbaMessageMediator.setDispatchHeader(gIOPBody);
        corbaMessageMediator.setDispatchBuffer(byteBuffer);
        return corbaMessageMediator;
    }

    @Override // com.sun.corba.se.pept.transport.ContactInfo
    public OutputObject createOutputObject(MessageMediator messageMediator) {
        CorbaMessageMediator corbaMessageMediator = (CorbaMessageMediator) messageMediator;
        CDROutputObject cDROutputObjectNewCDROutputObject = OutputStreamFactory.newCDROutputObject(this.orb, messageMediator, corbaMessageMediator.getRequestHeader(), corbaMessageMediator.getStreamFormatVersion());
        messageMediator.setOutputObject(cDROutputObjectNewCDROutputObject);
        return cDROutputObjectNewCDROutputObject;
    }

    @Override // com.sun.corba.se.pept.transport.ContactInfo
    public InputObject createInputObject(Broker broker, MessageMediator messageMediator) {
        CorbaMessageMediator corbaMessageMediator = (CorbaMessageMediator) messageMediator;
        return new CDRInputObject((ORB) broker, (CorbaConnection) messageMediator.getConnection(), corbaMessageMediator.getDispatchBuffer(), corbaMessageMediator.getDispatchHeader());
    }

    @Override // com.sun.corba.se.spi.transport.CorbaContactInfo
    public short getAddressingDisposition() {
        return this.addressingDisposition;
    }

    @Override // com.sun.corba.se.spi.transport.CorbaContactInfo
    public void setAddressingDisposition(short s2) {
        this.addressingDisposition = s2;
    }

    @Override // com.sun.corba.se.spi.transport.CorbaContactInfo
    public IOR getTargetIOR() {
        return this.contactInfoList.getTargetIOR();
    }

    @Override // com.sun.corba.se.spi.transport.CorbaContactInfo
    public IOR getEffectiveTargetIOR() {
        return this.effectiveTargetIOR;
    }

    @Override // com.sun.corba.se.spi.transport.CorbaContactInfo
    public IIOPProfile getEffectiveProfile() {
        return this.effectiveTargetIOR.getProfile();
    }

    public String toString() {
        return "CorbaContactInfoBase[]";
    }

    protected void dprint(String str) {
        ORBUtility.dprint("CorbaContactInfoBase", str);
    }
}
