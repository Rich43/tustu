package com.sun.corba.se.impl.transport;

import com.sun.corba.se.impl.orbutil.ORBUtility;
import com.sun.corba.se.pept.transport.Acceptor;
import com.sun.corba.se.pept.transport.ListenerThread;
import com.sun.corba.se.pept.transport.Selector;
import com.sun.corba.se.spi.orb.ORB;
import com.sun.corba.se.spi.orbutil.threadpool.Work;

/* loaded from: rt.jar:com/sun/corba/se/impl/transport/ListenerThreadImpl.class */
public class ListenerThreadImpl implements ListenerThread, Work {
    private ORB orb;
    private Acceptor acceptor;
    private Selector selector;
    private boolean keepRunning = true;
    private long enqueueTime;

    public ListenerThreadImpl(ORB orb, Acceptor acceptor, Selector selector) {
        this.orb = orb;
        this.acceptor = acceptor;
        this.selector = selector;
    }

    @Override // com.sun.corba.se.pept.transport.ListenerThread
    public Acceptor getAcceptor() {
        return this.acceptor;
    }

    @Override // com.sun.corba.se.pept.transport.ListenerThread
    public void close() {
        if (this.orb.transportDebugFlag) {
            dprint(".close: " + ((Object) this.acceptor));
        }
        this.keepRunning = false;
    }

    @Override // com.sun.corba.se.spi.orbutil.threadpool.Work
    public void doWork() {
        try {
            if (this.orb.transportDebugFlag) {
                dprint(".doWork: Start ListenerThread: " + ((Object) this.acceptor));
            }
            while (this.keepRunning) {
                try {
                    if (this.orb.transportDebugFlag) {
                        dprint(".doWork: BEFORE ACCEPT CYCLE: " + ((Object) this.acceptor));
                    }
                    this.acceptor.accept();
                    if (this.orb.transportDebugFlag) {
                        dprint(".doWork: AFTER ACCEPT CYCLE: " + ((Object) this.acceptor));
                    }
                } catch (Throwable th) {
                    if (this.orb.transportDebugFlag) {
                        dprint(".doWork: Exception in accept: " + ((Object) this.acceptor), th);
                    }
                    this.orb.getTransportManager().getSelector(0).unregisterForEvent(getAcceptor().getEventHandler());
                    getAcceptor().close();
                }
            }
        } finally {
            if (this.orb.transportDebugFlag) {
                dprint(".doWork: Terminated ListenerThread: " + ((Object) this.acceptor));
            }
        }
    }

    @Override // com.sun.corba.se.spi.orbutil.threadpool.Work
    public void setEnqueueTime(long j2) {
        this.enqueueTime = j2;
    }

    @Override // com.sun.corba.se.spi.orbutil.threadpool.Work
    public long getEnqueueTime() {
        return this.enqueueTime;
    }

    @Override // com.sun.corba.se.spi.orbutil.threadpool.Work
    public String getName() {
        return "ListenerThread";
    }

    private void dprint(String str) {
        ORBUtility.dprint("ListenerThreadImpl", str);
    }

    private void dprint(String str, Throwable th) {
        dprint(str);
        th.printStackTrace(System.out);
    }
}
