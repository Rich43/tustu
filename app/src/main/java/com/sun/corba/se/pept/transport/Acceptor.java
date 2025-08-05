package com.sun.corba.se.pept.transport;

import com.sun.corba.se.pept.broker.Broker;
import com.sun.corba.se.pept.encoding.InputObject;
import com.sun.corba.se.pept.encoding.OutputObject;
import com.sun.corba.se.pept.protocol.MessageMediator;

/* loaded from: rt.jar:com/sun/corba/se/pept/transport/Acceptor.class */
public interface Acceptor {
    boolean initialize();

    boolean initialized();

    String getConnectionCacheType();

    void setConnectionCache(InboundConnectionCache inboundConnectionCache);

    InboundConnectionCache getConnectionCache();

    boolean shouldRegisterAcceptEvent();

    void accept();

    void close();

    EventHandler getEventHandler();

    MessageMediator createMessageMediator(Broker broker, Connection connection);

    MessageMediator finishCreatingMessageMediator(Broker broker, Connection connection, MessageMediator messageMediator);

    InputObject createInputObject(Broker broker, MessageMediator messageMediator);

    OutputObject createOutputObject(Broker broker, MessageMediator messageMediator);
}
