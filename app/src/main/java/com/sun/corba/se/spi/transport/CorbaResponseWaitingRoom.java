package com.sun.corba.se.spi.transport;

import com.sun.corba.se.pept.protocol.MessageMediator;
import com.sun.corba.se.pept.transport.ResponseWaitingRoom;
import org.omg.CORBA.SystemException;

/* loaded from: rt.jar:com/sun/corba/se/spi/transport/CorbaResponseWaitingRoom.class */
public interface CorbaResponseWaitingRoom extends ResponseWaitingRoom {
    void signalExceptionToAllWaiters(SystemException systemException);

    MessageMediator getMessageMediator(int i2);
}
