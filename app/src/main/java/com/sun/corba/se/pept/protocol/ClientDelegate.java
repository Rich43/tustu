package com.sun.corba.se.pept.protocol;

import com.sun.corba.se.pept.broker.Broker;
import com.sun.corba.se.pept.transport.ContactInfoList;

/* loaded from: rt.jar:com/sun/corba/se/pept/protocol/ClientDelegate.class */
public interface ClientDelegate {
    Broker getBroker();

    ContactInfoList getContactInfoList();
}
