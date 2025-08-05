package com.sun.corba.se.pept.transport;

import com.sun.corba.se.pept.encoding.InputObject;
import com.sun.corba.se.pept.protocol.MessageMediator;

/* loaded from: rt.jar:com/sun/corba/se/pept/transport/ResponseWaitingRoom.class */
public interface ResponseWaitingRoom {
    void registerWaiter(MessageMediator messageMediator);

    InputObject waitForResponse(MessageMediator messageMediator);

    void responseReceived(InputObject inputObject);

    void unregisterWaiter(MessageMediator messageMediator);

    int numberRegistered();
}
