package java.util.concurrent.locks;

import java.io.Serializable;

/* loaded from: rt.jar:java/util/concurrent/locks/AbstractOwnableSynchronizer.class */
public abstract class AbstractOwnableSynchronizer implements Serializable {
    private static final long serialVersionUID = 3737899427754241961L;
    private transient Thread exclusiveOwnerThread;

    protected AbstractOwnableSynchronizer() {
    }

    protected final void setExclusiveOwnerThread(Thread thread) {
        this.exclusiveOwnerThread = thread;
    }

    protected final Thread getExclusiveOwnerThread() {
        return this.exclusiveOwnerThread;
    }
}
