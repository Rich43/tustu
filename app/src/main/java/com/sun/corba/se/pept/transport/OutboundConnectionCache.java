package com.sun.corba.se.pept.transport;

/* loaded from: rt.jar:com/sun/corba/se/pept/transport/OutboundConnectionCache.class */
public interface OutboundConnectionCache extends ConnectionCache {
    Connection get(ContactInfo contactInfo);

    void put(ContactInfo contactInfo, Connection connection);

    void remove(ContactInfo contactInfo);
}
