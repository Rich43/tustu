package com.sun.corba.se.pept.transport;

/* loaded from: rt.jar:com/sun/corba/se/pept/transport/InboundConnectionCache.class */
public interface InboundConnectionCache extends ConnectionCache {
    Connection get(Acceptor acceptor);

    void put(Acceptor acceptor, Connection connection);

    void remove(Connection connection);

    Acceptor getAcceptor();
}
