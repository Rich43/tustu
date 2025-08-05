package com.sun.corba.se.impl.orbutil.concurrent;

import com.sun.corba.se.impl.orbutil.ORBUtility;
import org.omg.CORBA.INTERNAL;

/* loaded from: rt.jar:com/sun/corba/se/impl/orbutil/concurrent/ReentrantMutex.class */
public class ReentrantMutex implements Sync {
    protected Thread holder_;
    protected int counter_;
    protected boolean debug;

    public ReentrantMutex() {
        this(false);
    }

    public ReentrantMutex(boolean z2) {
        this.holder_ = null;
        this.counter_ = 0;
        this.debug = false;
        this.debug = z2;
    }

    @Override // com.sun.corba.se.impl.orbutil.concurrent.Sync
    public void acquire() throws InterruptedException {
        if (Thread.interrupted()) {
            throw new InterruptedException();
        }
        synchronized (this) {
            try {
                if (this.debug) {
                    ORBUtility.dprintTrace(this, "acquire enter: holder_=" + ORBUtility.getThreadName(this.holder_) + " counter_=" + this.counter_);
                }
                Thread threadCurrentThread = Thread.currentThread();
                if (this.holder_ != threadCurrentThread) {
                    while (this.counter_ > 0) {
                        try {
                            wait();
                        } catch (InterruptedException e2) {
                            notify();
                            throw e2;
                        }
                    }
                    if (this.counter_ != 0) {
                        throw new INTERNAL("counter not 0 when first acquiring mutex");
                    }
                    this.holder_ = threadCurrentThread;
                }
                this.counter_++;
                if (this.debug) {
                    ORBUtility.dprintTrace(this, "acquire exit: holder_=" + ORBUtility.getThreadName(this.holder_) + " counter_=" + this.counter_);
                }
            } catch (Throwable th) {
                if (this.debug) {
                    ORBUtility.dprintTrace(this, "acquire exit: holder_=" + ORBUtility.getThreadName(this.holder_) + " counter_=" + this.counter_);
                }
                throw th;
            }
        }
    }

    void acquireAll(int i2) throws InterruptedException {
        if (Thread.interrupted()) {
            throw new InterruptedException();
        }
        synchronized (this) {
            try {
                if (this.debug) {
                    ORBUtility.dprintTrace(this, "acquireAll enter: count=" + i2 + " holder_=" + ORBUtility.getThreadName(this.holder_) + " counter_=" + this.counter_);
                }
                Thread threadCurrentThread = Thread.currentThread();
                if (this.holder_ == threadCurrentThread) {
                    throw new INTERNAL("Cannot acquireAll while holding the mutex");
                }
                while (this.counter_ > 0) {
                    try {
                        wait();
                    } catch (InterruptedException e2) {
                        notify();
                        throw e2;
                    }
                }
                if (this.counter_ != 0) {
                    throw new INTERNAL("counter not 0 when first acquiring mutex");
                }
                this.holder_ = threadCurrentThread;
                this.counter_ = i2;
                if (this.debug) {
                    ORBUtility.dprintTrace(this, "acquireAll exit: count=" + i2 + " holder_=" + ORBUtility.getThreadName(this.holder_) + " counter_=" + this.counter_);
                }
            } catch (Throwable th) {
                if (this.debug) {
                    ORBUtility.dprintTrace(this, "acquireAll exit: count=" + i2 + " holder_=" + ORBUtility.getThreadName(this.holder_) + " counter_=" + this.counter_);
                }
                throw th;
            }
        }
    }

    @Override // com.sun.corba.se.impl.orbutil.concurrent.Sync
    public synchronized void release() {
        try {
            if (this.debug) {
                ORBUtility.dprintTrace(this, "release enter:  holder_=" + ORBUtility.getThreadName(this.holder_) + " counter_=" + this.counter_);
            }
            if (Thread.currentThread() != this.holder_) {
                throw new INTERNAL("Attempt to release Mutex by thread not holding the Mutex");
            }
            this.counter_--;
            if (this.counter_ == 0) {
                this.holder_ = null;
                notify();
            }
        } finally {
            if (this.debug) {
                ORBUtility.dprintTrace(this, "release exit:  holder_=" + ORBUtility.getThreadName(this.holder_) + " counter_=" + this.counter_);
            }
        }
    }

    synchronized int releaseAll() {
        try {
            if (this.debug) {
                ORBUtility.dprintTrace(this, "releaseAll enter:  holder_=" + ORBUtility.getThreadName(this.holder_) + " counter_=" + this.counter_);
            }
            if (Thread.currentThread() != this.holder_) {
                throw new INTERNAL("Attempt to releaseAll Mutex by thread not holding the Mutex");
            }
            int i2 = this.counter_;
            this.counter_ = 0;
            this.holder_ = null;
            notify();
            if (this.debug) {
                ORBUtility.dprintTrace(this, "releaseAll exit:  holder_=" + ORBUtility.getThreadName(this.holder_) + " counter_=" + this.counter_);
            }
            return i2;
        } catch (Throwable th) {
            if (this.debug) {
                ORBUtility.dprintTrace(this, "releaseAll exit:  holder_=" + ORBUtility.getThreadName(this.holder_) + " counter_=" + this.counter_);
            }
            throw th;
        }
    }

    @Override // com.sun.corba.se.impl.orbutil.concurrent.Sync
    public boolean attempt(long j2) throws InterruptedException {
        if (Thread.interrupted()) {
            throw new InterruptedException();
        }
        synchronized (this) {
            try {
                if (this.debug) {
                    ORBUtility.dprintTrace(this, "attempt enter: msecs=" + j2 + " holder_=" + ORBUtility.getThreadName(this.holder_) + " counter_=" + this.counter_);
                }
                Thread threadCurrentThread = Thread.currentThread();
                if (this.counter_ == 0) {
                    this.holder_ = threadCurrentThread;
                    this.counter_ = 1;
                    if (this.debug) {
                        ORBUtility.dprintTrace(this, "attempt exit:  holder_=" + ORBUtility.getThreadName(this.holder_) + " counter_=" + this.counter_);
                    }
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
                        if (this.counter_ == 0) {
                            this.holder_ = threadCurrentThread;
                            this.counter_ = 1;
                            if (this.debug) {
                                ORBUtility.dprintTrace(this, "attempt exit:  holder_=" + ORBUtility.getThreadName(this.holder_) + " counter_=" + this.counter_);
                            }
                            return true;
                        }
                        jCurrentTimeMillis = j2 - (System.currentTimeMillis() - jCurrentTimeMillis2);
                    } catch (InterruptedException e2) {
                        notify();
                        throw e2;
                    }
                } while (jCurrentTimeMillis > 0);
                if (this.debug) {
                    ORBUtility.dprintTrace(this, "attempt exit:  holder_=" + ORBUtility.getThreadName(this.holder_) + " counter_=" + this.counter_);
                }
                return false;
            } finally {
                if (this.debug) {
                    ORBUtility.dprintTrace(this, "attempt exit:  holder_=" + ORBUtility.getThreadName(this.holder_) + " counter_=" + this.counter_);
                }
            }
        }
    }
}
