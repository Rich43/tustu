package com.sun.istack.internal;

import java.lang.ref.WeakReference;
import java.util.concurrent.ConcurrentLinkedQueue;

/* loaded from: rt.jar:com/sun/istack/internal/Pool.class */
public interface Pool<T> {
    @NotNull
    T take();

    void recycle(@NotNull T t2);

    /* loaded from: rt.jar:com/sun/istack/internal/Pool$Impl.class */
    public static abstract class Impl<T> implements Pool<T> {
        private volatile WeakReference<ConcurrentLinkedQueue<T>> queue;

        @NotNull
        protected abstract T create();

        @Override // com.sun.istack.internal.Pool
        @NotNull
        public final T take() {
            T t2 = getQueue().poll();
            if (t2 == null) {
                return create();
            }
            return t2;
        }

        @Override // com.sun.istack.internal.Pool
        public final void recycle(T t2) {
            getQueue().offer(t2);
        }

        private ConcurrentLinkedQueue<T> getQueue() {
            ConcurrentLinkedQueue<T> d2;
            WeakReference<ConcurrentLinkedQueue<T>> q2 = this.queue;
            if (q2 != null && (d2 = q2.get()) != null) {
                return d2;
            }
            ConcurrentLinkedQueue<T> d3 = new ConcurrentLinkedQueue<>();
            this.queue = new WeakReference<>(d3);
            return d3;
        }
    }
}
