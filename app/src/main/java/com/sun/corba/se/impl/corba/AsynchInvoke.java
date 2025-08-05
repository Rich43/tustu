package com.sun.corba.se.impl.corba;

import com.sun.corba.se.spi.orb.ORB;

/* loaded from: rt.jar:com/sun/corba/se/impl/corba/AsynchInvoke.class */
public class AsynchInvoke implements Runnable {
    private RequestImpl _req;
    private ORB _orb;
    private boolean _notifyORB;

    public AsynchInvoke(ORB orb, RequestImpl requestImpl, boolean z2) {
        this._orb = orb;
        this._req = requestImpl;
        this._notifyORB = z2;
    }

    @Override // java.lang.Runnable
    public void run() {
        this._req.doInvocation();
        synchronized (this._req) {
            this._req.gotResponse = true;
            this._req.notify();
        }
        if (this._notifyORB) {
            this._orb.notifyORB();
        }
    }
}
