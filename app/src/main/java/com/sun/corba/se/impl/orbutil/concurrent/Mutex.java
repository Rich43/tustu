package com.sun.corba.se.impl.orbutil.concurrent;

/* loaded from: rt.jar:com/sun/corba/se/impl/orbutil/concurrent/Mutex.class */
public class Mutex implements Sync {
    protected boolean inuse_ = false;

    @Override // com.sun.corba.se.impl.orbutil.concurrent.Sync
    public void acquire() throws InterruptedException {
        if (Thread.interrupted()) {
            throw new InterruptedException();
        }
        synchronized (this) {
            while (this.inuse_) {
                try {
                    wait();
                } catch (InterruptedException e2) {
                    notify();
                    throw e2;
                }
            }
            this.inuse_ = true;
        }
    }

    @Override // com.sun.corba.se.impl.orbutil.concurrent.Sync
    public synchronized void release() {
        this.inuse_ = false;
        notify();
    }

    @Override // com.sun.corba.se.impl.orbutil.concurrent.Sync
    public boolean attempt(long j2) throws InterruptedException {
        if (Thread.interrupted()) {
            throw new InterruptedException();
        }
        synchronized (this) {
            if (!this.inuse_) {
                this.inuse_ = true;
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
