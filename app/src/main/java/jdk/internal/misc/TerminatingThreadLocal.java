package jdk.internal.misc;

import java.util.Collection;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.Iterator;

/* loaded from: rt.jar:jdk/internal/misc/TerminatingThreadLocal.class */
public class TerminatingThreadLocal<T> extends ThreadLocal<T> {
    public static final ThreadLocal<Collection<TerminatingThreadLocal<?>>> REGISTRY = new ThreadLocal<Collection<TerminatingThreadLocal<?>>>() { // from class: jdk.internal.misc.TerminatingThreadLocal.1
        /* JADX INFO: Access modifiers changed from: protected */
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // java.lang.ThreadLocal
        public Collection<TerminatingThreadLocal<?>> initialValue() {
            return Collections.newSetFromMap(new IdentityHashMap(4));
        }
    };

    @Override // java.lang.ThreadLocal
    public void set(T t2) {
        super.set(t2);
        register(this);
    }

    @Override // java.lang.ThreadLocal
    public void remove() {
        super.remove();
        unregister(this);
    }

    protected void threadTerminated(T t2) {
    }

    public static void threadTerminated() {
        Iterator<TerminatingThreadLocal<?>> it = REGISTRY.get().iterator();
        while (it.hasNext()) {
            it.next()._threadTerminated();
        }
    }

    private void _threadTerminated() {
        threadTerminated(get());
    }

    public static void register(TerminatingThreadLocal<?> terminatingThreadLocal) {
        REGISTRY.get().add(terminatingThreadLocal);
    }

    private static void unregister(TerminatingThreadLocal<?> terminatingThreadLocal) {
        REGISTRY.get().remove(terminatingThreadLocal);
    }
}
