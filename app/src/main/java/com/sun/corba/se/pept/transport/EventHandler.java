package com.sun.corba.se.pept.transport;

import com.sun.corba.se.spi.orbutil.threadpool.Work;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;

/* loaded from: rt.jar:com/sun/corba/se/pept/transport/EventHandler.class */
public interface EventHandler {
    void setUseSelectThreadToWait(boolean z2);

    boolean shouldUseSelectThreadToWait();

    SelectableChannel getChannel();

    int getInterestOps();

    void setSelectionKey(SelectionKey selectionKey);

    SelectionKey getSelectionKey();

    void handleEvent();

    void setUseWorkerThreadForEvent(boolean z2);

    boolean shouldUseWorkerThreadForEvent();

    void setWork(Work work);

    Work getWork();

    Acceptor getAcceptor();

    Connection getConnection();
}
