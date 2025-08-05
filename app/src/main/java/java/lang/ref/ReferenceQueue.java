package java.lang.ref;

import java.util.function.Consumer;
import sun.misc.VM;

/* loaded from: rt.jar:java/lang/ref/ReferenceQueue.class */
public class ReferenceQueue<T> {
    static ReferenceQueue<Object> NULL;
    static ReferenceQueue<Object> ENQUEUED;
    private Lock lock = new Lock();
    private volatile Reference<? extends T> head = null;
    private long queueLength = 0;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !ReferenceQueue.class.desiredAssertionStatus();
        NULL = new Null();
        ENQUEUED = new Null();
    }

    /* loaded from: rt.jar:java/lang/ref/ReferenceQueue$Null.class */
    private static class Null<S> extends ReferenceQueue<S> {
        private Null() {
        }

        @Override // java.lang.ref.ReferenceQueue
        boolean enqueue(Reference<? extends S> reference) {
            return false;
        }
    }

    /* loaded from: rt.jar:java/lang/ref/ReferenceQueue$Lock.class */
    private static class Lock {
        private Lock() {
        }
    }

    boolean enqueue(Reference<? extends T> reference) {
        synchronized (this.lock) {
            ReferenceQueue<? super Object> referenceQueue = reference.queue;
            if (referenceQueue == NULL || referenceQueue == ENQUEUED) {
                return false;
            }
            if (!$assertionsDisabled && referenceQueue != this) {
                throw new AssertionError();
            }
            reference.queue = ENQUEUED;
            reference.next = this.head == null ? reference : this.head;
            this.head = reference;
            this.queueLength++;
            if (reference instanceof FinalReference) {
                VM.addFinalRefCount(1);
            }
            this.lock.notifyAll();
            return true;
        }
    }

    private Reference<? extends T> reallyPoll() {
        Reference<? extends T> reference = this.head;
        if (reference != null) {
            Reference<? extends T> reference2 = reference.next;
            this.head = reference2 == reference ? null : reference2;
            reference.queue = NULL;
            reference.next = reference;
            this.queueLength--;
            if (reference instanceof FinalReference) {
                VM.addFinalRefCount(-1);
            }
            return reference;
        }
        return null;
    }

    public Reference<? extends T> poll() {
        Reference<? extends T> referenceReallyPoll;
        if (this.head == null) {
            return null;
        }
        synchronized (this.lock) {
            referenceReallyPoll = reallyPoll();
        }
        return referenceReallyPoll;
    }

    public Reference<? extends T> remove(long j2) throws InterruptedException, IllegalArgumentException {
        if (j2 < 0) {
            throw new IllegalArgumentException("Negative timeout value");
        }
        synchronized (this.lock) {
            Reference<? extends T> referenceReallyPoll = reallyPoll();
            if (referenceReallyPoll != null) {
                return referenceReallyPoll;
            }
            long jNanoTime = j2 == 0 ? 0L : System.nanoTime();
            while (true) {
                this.lock.wait(j2);
                Reference<? extends T> referenceReallyPoll2 = reallyPoll();
                if (referenceReallyPoll2 != null) {
                    return referenceReallyPoll2;
                }
                if (j2 != 0) {
                    long jNanoTime2 = System.nanoTime();
                    j2 -= (jNanoTime2 - jNanoTime) / 1000000;
                    if (j2 <= 0) {
                        return null;
                    }
                    jNanoTime = jNanoTime2;
                }
            }
        }
    }

    public Reference<? extends T> remove() throws InterruptedException {
        return remove(0L);
    }

    void forEach(Consumer<? super Reference<? extends T>> consumer) {
        Reference<? extends T> reference = this.head;
        while (true) {
            Reference<? extends T> reference2 = reference;
            if (reference2 != null) {
                consumer.accept(reference2);
                Reference<? extends T> reference3 = reference2.next;
                if (reference3 == reference2) {
                    if (reference2.queue == ENQUEUED) {
                        reference = null;
                    } else {
                        reference = this.head;
                    }
                } else {
                    reference = reference3;
                }
            } else {
                return;
            }
        }
    }
}
