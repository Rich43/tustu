package com.sun.corba.se.pept.transport;

import java.util.Collection;

/* loaded from: rt.jar:com/sun/corba/se/pept/transport/TransportManager.class */
public interface TransportManager {
    ByteBufferPool getByteBufferPool(int i2);

    OutboundConnectionCache getOutboundConnectionCache(ContactInfo contactInfo);

    Collection getOutboundConnectionCaches();

    InboundConnectionCache getInboundConnectionCache(Acceptor acceptor);

    Collection getInboundConnectionCaches();

    Selector getSelector(int i2);

    void registerAcceptor(Acceptor acceptor);

    Collection getAcceptors();

    void unregisterAcceptor(Acceptor acceptor);

    void close();
}
