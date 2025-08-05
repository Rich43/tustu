package com.sun.org.apache.xml.internal.security.utils;

import java.lang.Throwable;
import java.lang.ref.WeakReference;
import java.util.Collections;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

/* loaded from: rt.jar:com/sun/org/apache/xml/internal/security/utils/WeakObjectPool.class */
public abstract class WeakObjectPool<T, E extends Throwable> {
    private static final Integer MARKER_VALUE = Integer.MAX_VALUE;
    private final BlockingQueue<WeakReference<T>> available = new LinkedBlockingDeque();
    private final Map<T, Integer> onLoan = Collections.synchronizedMap(new WeakHashMap());

    protected abstract T createObject() throws Throwable;

    protected WeakObjectPool() {
    }

    public T getObject() throws Throwable {
        T t2;
        T tCreateObject = null;
        do {
            WeakReference<T> weakReferencePoll = this.available.poll();
            if (weakReferencePoll == null) {
                break;
            }
            t2 = weakReferencePoll.get();
            tCreateObject = t2;
        } while (t2 == null);
        if (tCreateObject == null) {
            tCreateObject = createObject();
        }
        this.onLoan.put(tCreateObject, MARKER_VALUE);
        return tCreateObject;
    }

    public boolean repool(T t2) {
        if (t2 != null && this.onLoan.containsKey(t2)) {
            synchronized (t2) {
                if (this.onLoan.remove(t2) != null) {
                    return this.available.offer(new WeakReference<>(t2));
                }
                return false;
            }
        }
        return false;
    }
}
