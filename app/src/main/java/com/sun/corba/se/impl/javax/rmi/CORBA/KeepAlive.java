package com.sun.corba.se.impl.javax.rmi.CORBA;

/* compiled from: Util.java */
/* loaded from: rt.jar:com/sun/corba/se/impl/javax/rmi/CORBA/KeepAlive.class */
class KeepAlive extends Thread {
    boolean quit = false;

    public KeepAlive() {
        setDaemon(false);
    }

    @Override // java.lang.Thread, java.lang.Runnable
    public synchronized void run() {
        while (!this.quit) {
            try {
                wait();
            } catch (InterruptedException e2) {
            }
        }
    }

    public synchronized void quit() {
        this.quit = true;
        notifyAll();
    }
}
