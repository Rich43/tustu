package sun.misc;

/* loaded from: rt.jar:sun/misc/Lock.class */
public class Lock {
    private boolean locked = false;

    public final synchronized void lock() throws InterruptedException {
        while (this.locked) {
            wait();
        }
        this.locked = true;
    }

    public final synchronized void unlock() {
        this.locked = false;
        notifyAll();
    }
}
