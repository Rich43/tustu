package sun.java2d;

import java.lang.ref.Reference;
import java.util.concurrent.ConcurrentLinkedQueue;
import sun.java2d.ReentrantContext;

/* loaded from: rt.jar:sun/java2d/ReentrantContextProviderCLQ.class */
public abstract class ReentrantContextProviderCLQ<K extends ReentrantContext> extends ReentrantContextProvider<K> {
    private final ConcurrentLinkedQueue<Reference<K>> ctxQueue;

    public ReentrantContextProviderCLQ(int i2) {
        super(i2);
        this.ctxQueue = new ConcurrentLinkedQueue<>();
    }

    @Override // sun.java2d.ReentrantContextProvider
    public final K acquire() {
        Reference<K> referencePoll;
        K kNewContext = null;
        while (kNewContext == null && (referencePoll = this.ctxQueue.poll()) != null) {
            kNewContext = referencePoll.get();
        }
        if (kNewContext == null) {
            kNewContext = newContext();
            kNewContext.usage = (byte) 2;
        }
        return kNewContext;
    }

    @Override // sun.java2d.ReentrantContextProvider
    public final void release(K k2) {
        if (k2.usage == 2) {
            this.ctxQueue.offer(getOrCreateReference(k2));
        }
    }
}
