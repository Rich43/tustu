package com.sun.corba.se.impl.orbutil.concurrent;

import com.sun.corba.se.impl.orbutil.ORBUtility;

/* loaded from: rt.jar:com/sun/corba/se/impl/orbutil/concurrent/CondVar.class */
public class CondVar {
    protected boolean debug_;
    protected final Sync mutex_;
    protected final ReentrantMutex remutex_;

    private int releaseMutex() {
        int iReleaseAll = 1;
        if (this.remutex_ != null) {
            iReleaseAll = this.remutex_.releaseAll();
        } else {
            this.mutex_.release();
        }
        return iReleaseAll;
    }

    private void acquireMutex(int i2) throws InterruptedException {
        if (this.remutex_ != null) {
            this.remutex_.acquireAll(i2);
        } else {
            this.mutex_.acquire();
        }
    }

    public CondVar(Sync sync, boolean z2) {
        this.debug_ = z2;
        this.mutex_ = sync;
        if (sync instanceof ReentrantMutex) {
            this.remutex_ = (ReentrantMutex) sync;
        } else {
            this.remutex_ = null;
        }
    }

    public CondVar(Sync sync) {
        this(sync, false);
    }

    public void await() throws InterruptedException {
        boolean z2;
        int iReleaseMutex;
        boolean z3;
        if (Thread.interrupted()) {
            throw new InterruptedException();
        }
        try {
            if (this.debug_) {
                ORBUtility.dprintTrace(this, "await enter");
            }
            synchronized (this) {
                iReleaseMutex = releaseMutex();
                try {
                    wait();
                } catch (InterruptedException e2) {
                    notify();
                    throw e2;
                }
            }
            boolean z4 = false;
            while (true) {
                try {
                    z3 = z4;
                    acquireMutex(iReleaseMutex);
                    break;
                } catch (InterruptedException e3) {
                    z4 = true;
                }
            }
            if (z3) {
                Thread.currentThread().interrupt();
            }
            if (this.debug_) {
                ORBUtility.dprintTrace(this, "await exit");
            }
        } catch (Throwable th) {
            boolean z5 = false;
            while (true) {
                try {
                    z2 = z5;
                    acquireMutex(0);
                    break;
                } catch (InterruptedException e4) {
                    z5 = true;
                }
            }
            if (z2) {
                Thread.currentThread().interrupt();
            }
            if (this.debug_) {
                ORBUtility.dprintTrace(this, "await exit");
            }
            throw th;
        }
    }

    public boolean timedwait(long j2) throws InterruptedException {
        boolean z2;
        int iReleaseMutex;
        boolean z3;
        if (Thread.interrupted()) {
            throw new InterruptedException();
        }
        boolean z4 = false;
        try {
            if (this.debug_) {
                ORBUtility.dprintTrace(this, "timedwait enter");
            }
            synchronized (this) {
                iReleaseMutex = releaseMutex();
                if (j2 > 0) {
                    try {
                        long jCurrentTimeMillis = System.currentTimeMillis();
                        wait(j2);
                        z4 = System.currentTimeMillis() - jCurrentTimeMillis <= j2;
                    } catch (InterruptedException e2) {
                        notify();
                        throw e2;
                    }
                }
            }
            boolean z5 = false;
            while (true) {
                try {
                    z3 = z5;
                    acquireMutex(iReleaseMutex);
                    break;
                } catch (InterruptedException e3) {
                    z5 = true;
                }
            }
            if (z3) {
                Thread.currentThread().interrupt();
            }
            if (this.debug_) {
                ORBUtility.dprintTrace(this, "timedwait exit");
            }
            return z4;
        } catch (Throwable th) {
            boolean z6 = false;
            while (true) {
                try {
                    z2 = z6;
                    acquireMutex(0);
                    break;
                } catch (InterruptedException e4) {
                    z6 = true;
                }
            }
            if (z2) {
                Thread.currentThread().interrupt();
            }
            if (this.debug_) {
                ORBUtility.dprintTrace(this, "timedwait exit");
            }
            throw th;
        }
    }

    public synchronized void signal() {
        notify();
    }

    public synchronized void broadcast() {
        notifyAll();
    }
}
