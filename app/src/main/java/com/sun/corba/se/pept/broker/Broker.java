package com.sun.corba.se.pept.broker;

import com.sun.corba.se.pept.protocol.ClientInvocationInfo;
import com.sun.corba.se.pept.transport.TransportManager;

/* loaded from: rt.jar:com/sun/corba/se/pept/broker/Broker.class */
public interface Broker {
    ClientInvocationInfo createOrIncrementInvocationInfo();

    ClientInvocationInfo getInvocationInfo();

    void releaseOrDecrementInvocationInfo();

    TransportManager getTransportManager();
}
