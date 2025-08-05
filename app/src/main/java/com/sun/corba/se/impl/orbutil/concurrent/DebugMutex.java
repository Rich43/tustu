package com.sun.corba.se.impl.orbutil.concurrent;

import org.omg.CORBA.INTERNAL;

/* loaded from: rt.jar:com/sun/corba/se/impl/orbutil/concurrent/DebugMutex.class */
public class DebugMutex implements Sync {
    protected boolean inuse_ = false;
    protected Thread holder_ = null;

    @Override // com.sun.corba.se.impl.orbutil.concurrent.Sync
    public void acquire() throws InterruptedException {
        if (Thread.interrupted()) {
            throw new InterruptedException();
        }
        synchronized (this) {
            if (this.holder_ == Thread.currentThread()) {
                throw new INTERNAL("Attempt to acquire Mutex by thread holding the Mutex");
            }
            while (this.inuse_) {
                try {
                    wait();
                } catch (InterruptedException e2) {
                    notify();
                    throw e2;
                }
            }
            this.inuse_ = true;
            this.holder_ = Thread.currentThread();
        }
    }

    @Override // com.sun.corba.se.impl.orbutil.concurrent.Sync
    public synchronized void release() {
        if (Thread.currentThread() != this.holder_) {
            throw new INTERNAL("Attempt to release Mutex by thread not holding the Mutex");
        }
        this.holder_ = null;
        this.inuse_ = false;
        notify();
    }

    @Override // com.sun.corba.se.impl.orbutil.concurrent.Sync
    public boolean attempt(long j2) throws InterruptedException {
        if (Thread.interrupted()) {
            throw new InterruptedException();
        }
        synchronized (this) {
            Thread threadCurrentThread = Thread.currentThread();
            if (!this.inuse_) {
                this.inuse_ = true;
                this.holder_ = threadCurrentThread;
                return true;
            }
            if (j2 <= 0) {
                return false;
            }
            long jCurrentTimeMillis = j2;
            long jCurrentTimeMillis2 = System.currentTimeMillis();
            do {
                try {
                    wait(jCurrentTimeMillis);
                    if (!this.inuse_) {
                        this.inuse_ = true;
                        this.holder_ = threadCurrentThread;
                        return true;
                    }
                    jCurrentTimeMillis = j2 - (System.currentTimeMillis() - jCurrentTimeMillis2);
                } catch (InterruptedException e2) {
                    notify();
                    throw e2;
                }
            } while (jCurrentTimeMillis > 0);
            return false;
        }
    }
}
