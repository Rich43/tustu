package sun.misc;

/* loaded from: rt.jar:sun/misc/ConditionLock.class */
public final class ConditionLock extends Lock {
    private int state;

    public ConditionLock() {
        this.state = 0;
    }

    public ConditionLock(int i2) {
        this.state = 0;
        this.state = i2;
    }

    public synchronized void lockWhen(int i2) throws InterruptedException {
        while (this.state != i2) {
            wait();
        }
        lock();
    }

    public synchronized void unlockWith(int i2) {
        this.state = i2;
        unlock();
    }
}
