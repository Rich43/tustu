package com.sun.corba.se.pept.transport;

import com.sun.corba.se.pept.broker.Broker;
import com.sun.corba.se.pept.encoding.InputObject;
import com.sun.corba.se.pept.encoding.OutputObject;
import com.sun.corba.se.pept.protocol.ClientRequestDispatcher;
import com.sun.corba.se.pept.protocol.MessageMediator;

/* loaded from: rt.jar:com/sun/corba/se/pept/transport/ContactInfo.class */
public interface ContactInfo {
    Broker getBroker();

    ContactInfoList getContactInfoList();

    ClientRequestDispatcher getClientRequestDispatcher();

    boolean isConnectionBased();

    boolean shouldCacheConnection();

    String getConnectionCacheType();

    void setConnectionCache(OutboundConnectionCache outboundConnectionCache);

    OutboundConnectionCache getConnectionCache();

    Connection createConnection();

    MessageMediator createMessageMediator(Broker broker, ContactInfo contactInfo, Connection connection, String str, boolean z2);

    MessageMediator createMessageMediator(Broker broker, Connection connection);

    MessageMediator finishCreatingMessageMediator(Broker broker, Connection connection, MessageMediator messageMediator);

    InputObject createInputObject(Broker broker, MessageMediator messageMediator);

    OutputObject createOutputObject(MessageMediator messageMediator);

    int hashCode();
}
