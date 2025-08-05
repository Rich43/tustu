package com.sun.corba.se.pept.transport;

/* loaded from: rt.jar:com/sun/corba/se/pept/transport/Selector.class */
public interface Selector {
    void setTimeout(long j2);

    long getTimeout();

    void registerInterestOps(EventHandler eventHandler);

    void registerForEvent(EventHandler eventHandler);

    void unregisterForEvent(EventHandler eventHandler);

    void close();
}
