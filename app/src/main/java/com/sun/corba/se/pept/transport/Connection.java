package com.sun.corba.se.pept.transport;

import com.sun.corba.se.pept.encoding.InputObject;
import com.sun.corba.se.pept.encoding.OutputObject;
import com.sun.corba.se.pept.protocol.MessageMediator;

/* loaded from: rt.jar:com/sun/corba/se/pept/transport/Connection.class */
public interface Connection {
    boolean shouldRegisterReadEvent();

    boolean shouldRegisterServerReadEvent();

    boolean read();

    void close();

    Acceptor getAcceptor();

    ContactInfo getContactInfo();

    EventHandler getEventHandler();

    boolean isServer();

    boolean isBusy();

    long getTimeStamp();

    void setTimeStamp(long j2);

    void setState(String str);

    void writeLock();

    void writeUnlock();

    void sendWithoutLock(OutputObject outputObject);

    void registerWaiter(MessageMediator messageMediator);

    InputObject waitForResponse(MessageMediator messageMediator);

    void unregisterWaiter(MessageMediator messageMediator);

    void setConnectionCache(ConnectionCache connectionCache);

    ConnectionCache getConnectionCache();
}
