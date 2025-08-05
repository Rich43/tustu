package com.sun.corba.se.pept.transport;

/* loaded from: rt.jar:com/sun/corba/se/pept/transport/ConnectionCache.class */
public interface ConnectionCache {
    String getCacheType();

    void stampTime(Connection connection);

    long numberOfConnections();

    long numberOfIdleConnections();

    long numberOfBusyConnections();

    boolean reclaim();

    void close();
}
