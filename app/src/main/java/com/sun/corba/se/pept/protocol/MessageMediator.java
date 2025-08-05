package com.sun.corba.se.pept.protocol;

import com.sun.corba.se.pept.broker.Broker;
import com.sun.corba.se.pept.encoding.InputObject;
import com.sun.corba.se.pept.encoding.OutputObject;
import com.sun.corba.se.pept.transport.Connection;
import com.sun.corba.se.pept.transport.ContactInfo;

/* loaded from: rt.jar:com/sun/corba/se/pept/protocol/MessageMediator.class */
public interface MessageMediator {
    Broker getBroker();

    ContactInfo getContactInfo();

    Connection getConnection();

    void initializeMessage();

    void finishSendingRequest();

    @Deprecated
    InputObject waitForResponse();

    void setOutputObject(OutputObject outputObject);

    OutputObject getOutputObject();

    void setInputObject(InputObject inputObject);

    InputObject getInputObject();
}
