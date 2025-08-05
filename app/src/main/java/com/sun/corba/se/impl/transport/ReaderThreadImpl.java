package com.sun.corba.se.impl.transport;

import com.sun.corba.se.impl.orbutil.ORBUtility;
import com.sun.corba.se.pept.transport.Connection;
import com.sun.corba.se.pept.transport.ReaderThread;
import com.sun.corba.se.pept.transport.Selector;
import com.sun.corba.se.spi.orb.ORB;
import com.sun.corba.se.spi.orbutil.threadpool.Work;

/* loaded from: rt.jar:com/sun/corba/se/impl/transport/ReaderThreadImpl.class */
public class ReaderThreadImpl implements ReaderThread, Work {
    private ORB orb;
    private Connection connection;
    private Selector selector;
    private boolean keepRunning = true;
    private long enqueueTime;

    public ReaderThreadImpl(ORB orb, Connection connection, Selector selector) {
        this.orb = orb;
        this.connection = connection;
        this.selector = selector;
    }

    @Override // com.sun.corba.se.pept.transport.ReaderThread
    public Connection getConnection() {
        return this.connection;
    }

    @Override // com.sun.corba.se.pept.transport.ReaderThread
    public void close() {
        if (this.orb.transportDebugFlag) {
            dprint(".close: " + ((Object) this.connection));
        }
        this.keepRunning = false;
    }

    @Override // com.sun.corba.se.spi.orbutil.threadpool.Work
    public void doWork() {
        boolean z2;
        try {
            if (this.orb.transportDebugFlag) {
                dprint(".doWork: Start ReaderThread: " + ((Object) this.connection));
            }
            while (this.keepRunning) {
                try {
                    if (this.orb.transportDebugFlag) {
                        dprint(".doWork: Start ReaderThread cycle: " + ((Object) this.connection));
                    }
                } catch (Throwable th) {
                    if (this.orb.transportDebugFlag) {
                        dprint(".doWork: exception in read: " + ((Object) this.connection), th);
                    }
                    this.orb.getTransportManager().getSelector(0).unregisterForEvent(getConnection().getEventHandler());
                    getConnection().close();
                }
                if (this.connection.read()) {
                    if (z2) {
                        return;
                    } else {
                        return;
                    }
                } else if (this.orb.transportDebugFlag) {
                    dprint(".doWork: End ReaderThread cycle: " + ((Object) this.connection));
                }
            }
            if (this.orb.transportDebugFlag) {
                dprint(".doWork: Terminated ReaderThread: " + ((Object) this.connection));
            }
        } finally {
            if (this.orb.transportDebugFlag) {
                dprint(".doWork: Terminated ReaderThread: " + ((Object) this.connection));
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
        return "ReaderThread";
    }

    private void dprint(String str) {
        ORBUtility.dprint("ReaderThreadImpl", str);
    }

    protected void dprint(String str, Throwable th) {
        dprint(str);
        th.printStackTrace(System.out);
    }
}
