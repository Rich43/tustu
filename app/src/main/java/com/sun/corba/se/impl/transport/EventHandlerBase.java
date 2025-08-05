package com.sun.corba.se.impl.transport;

import com.sun.corba.se.impl.orbutil.ORBUtility;
import com.sun.corba.se.pept.transport.EventHandler;
import com.sun.corba.se.spi.orb.ORB;
import com.sun.corba.se.spi.orbutil.threadpool.NoSuchThreadPoolException;
import com.sun.corba.se.spi.orbutil.threadpool.NoSuchWorkQueueException;
import com.sun.corba.se.spi.orbutil.threadpool.Work;
import java.nio.channels.SelectionKey;
import org.omg.CORBA.INTERNAL;

/* loaded from: rt.jar:com/sun/corba/se/impl/transport/EventHandlerBase.class */
public abstract class EventHandlerBase implements EventHandler {
    protected ORB orb;
    protected Work work;
    protected boolean useWorkerThreadForEvent;
    protected boolean useSelectThreadToWait;
    protected SelectionKey selectionKey;

    @Override // com.sun.corba.se.pept.transport.EventHandler
    public void setUseSelectThreadToWait(boolean z2) {
        this.useSelectThreadToWait = z2;
    }

    @Override // com.sun.corba.se.pept.transport.EventHandler
    public boolean shouldUseSelectThreadToWait() {
        return this.useSelectThreadToWait;
    }

    @Override // com.sun.corba.se.pept.transport.EventHandler
    public void setSelectionKey(SelectionKey selectionKey) {
        this.selectionKey = selectionKey;
    }

    @Override // com.sun.corba.se.pept.transport.EventHandler
    public SelectionKey getSelectionKey() {
        return this.selectionKey;
    }

    @Override // com.sun.corba.se.pept.transport.EventHandler
    public void handleEvent() {
        if (this.orb.transportDebugFlag) {
            dprint(".handleEvent->: " + ((Object) this));
        }
        getSelectionKey().interestOps(getSelectionKey().interestOps() & (getInterestOps() ^ (-1)));
        if (shouldUseWorkerThreadForEvent()) {
            Throwable th = null;
            try {
                if (this.orb.transportDebugFlag) {
                    dprint(".handleEvent: addWork to pool: 0");
                }
                this.orb.getThreadPoolManager().getThreadPool(0).getWorkQueue(0).addWork(getWork());
            } catch (NoSuchThreadPoolException e2) {
                th = e2;
            } catch (NoSuchWorkQueueException e3) {
                th = e3;
            }
            if (th != null) {
                if (this.orb.transportDebugFlag) {
                    dprint(".handleEvent: " + ((Object) th));
                }
                INTERNAL internal = new INTERNAL("NoSuchThreadPoolException");
                internal.initCause(th);
                throw internal;
            }
        } else {
            if (this.orb.transportDebugFlag) {
                dprint(".handleEvent: doWork");
            }
            getWork().doWork();
        }
        if (this.orb.transportDebugFlag) {
            dprint(".handleEvent<-: " + ((Object) this));
        }
    }

    @Override // com.sun.corba.se.pept.transport.EventHandler
    public boolean shouldUseWorkerThreadForEvent() {
        return this.useWorkerThreadForEvent;
    }

    @Override // com.sun.corba.se.pept.transport.EventHandler
    public void setUseWorkerThreadForEvent(boolean z2) {
        this.useWorkerThreadForEvent = z2;
    }

    @Override // com.sun.corba.se.pept.transport.EventHandler
    public void setWork(Work work) {
        this.work = work;
    }

    @Override // com.sun.corba.se.pept.transport.EventHandler
    public Work getWork() {
        return this.work;
    }

    private void dprint(String str) {
        ORBUtility.dprint("EventHandlerBase", str);
    }
}
