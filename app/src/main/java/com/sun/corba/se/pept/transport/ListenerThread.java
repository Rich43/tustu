package com.sun.corba.se.pept.transport;

/* loaded from: rt.jar:com/sun/corba/se/pept/transport/ListenerThread.class */
public interface ListenerThread {
    Acceptor getAcceptor();

    void close();
}
