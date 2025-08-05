package sun.java2d;

import java.lang.ref.Reference;
import sun.java2d.ReentrantContext;

/* loaded from: rt.jar:sun/java2d/ReentrantContextProviderTL.class */
public abstract class ReentrantContextProviderTL<K extends ReentrantContext> extends ReentrantContextProvider<K> {
    private final ThreadLocal<Reference<K>> ctxTL;
    private final ReentrantContextProviderCLQ<K> ctxProviderCLQ;

    public ReentrantContextProviderTL(int i2) {
        this(i2, 2);
    }

    public ReentrantContextProviderTL(int i2, int i3) {
        super(i2);
        this.ctxTL = new ThreadLocal<>();
        this.ctxProviderCLQ = (ReentrantContextProviderCLQ<K>) new ReentrantContextProviderCLQ<K>(i3) { // from class: sun.java2d.ReentrantContextProviderTL.1
            @Override // sun.java2d.ReentrantContextProvider
            protected K newContext() {
                return (K) this.newContext();
            }
        };
    }

    @Override // sun.java2d.ReentrantContextProvider
    public final K acquire() {
        ReentrantContext reentrantContextAcquire = null;
        Reference<K> reference = this.ctxTL.get();
        if (reference != null) {
            reentrantContextAcquire = reference.get();
        }
        if (reentrantContextAcquire == null) {
            reentrantContextAcquire = newContext();
            this.ctxTL.set(getOrCreateReference(reentrantContextAcquire));
        }
        if (reentrantContextAcquire.usage == 0) {
            reentrantContextAcquire.usage = (byte) 1;
        } else {
            reentrantContextAcquire = this.ctxProviderCLQ.acquire();
        }
        return (K) reentrantContextAcquire;
    }

    @Override // sun.java2d.ReentrantContextProvider
    public final void release(K k2) {
        if (k2.usage == 1) {
            k2.usage = (byte) 0;
        } else {
            this.ctxProviderCLQ.release(k2);
        }
    }
}
