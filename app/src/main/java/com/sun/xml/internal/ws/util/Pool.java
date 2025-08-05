package com.sun.xml.internal.ws.util;

import com.sun.xml.internal.ws.api.pipe.Tube;
import com.sun.xml.internal.ws.api.pipe.TubeCloner;
import java.lang.ref.WeakReference;
import java.util.concurrent.ConcurrentLinkedQueue;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;

/* loaded from: rt.jar:com/sun/xml/internal/ws/util/Pool.class */
public abstract class Pool<T> {
    private volatile WeakReference<ConcurrentLinkedQueue<T>> queue;

    protected abstract T create();

    public final T take() {
        T t2 = getQueue().poll();
        if (t2 == null) {
            return create();
        }
        return t2;
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

    public final void recycle(T t2) {
        getQueue().offer(t2);
    }

    /* loaded from: rt.jar:com/sun/xml/internal/ws/util/Pool$Marshaller.class */
    public static final class Marshaller extends Pool<javax.xml.bind.Marshaller> {
        private final JAXBContext context;

        public Marshaller(JAXBContext context) {
            this.context = context;
        }

        /* JADX INFO: Access modifiers changed from: protected */
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // com.sun.xml.internal.ws.util.Pool
        public javax.xml.bind.Marshaller create() {
            try {
                return this.context.createMarshaller();
            } catch (JAXBException e2) {
                throw new AssertionError(e2);
            }
        }
    }

    /* loaded from: rt.jar:com/sun/xml/internal/ws/util/Pool$Unmarshaller.class */
    public static final class Unmarshaller extends Pool<javax.xml.bind.Unmarshaller> {
        private final JAXBContext context;

        public Unmarshaller(JAXBContext context) {
            this.context = context;
        }

        /* JADX INFO: Access modifiers changed from: protected */
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // com.sun.xml.internal.ws.util.Pool
        public javax.xml.bind.Unmarshaller create() {
            try {
                return this.context.createUnmarshaller();
            } catch (JAXBException e2) {
                throw new AssertionError(e2);
            }
        }
    }

    /* loaded from: rt.jar:com/sun/xml/internal/ws/util/Pool$TubePool.class */
    public static final class TubePool extends Pool<Tube> {
        private final Tube master;

        public TubePool(Tube master) {
            this.master = master;
            recycle(master);
        }

        /* JADX INFO: Access modifiers changed from: protected */
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // com.sun.xml.internal.ws.util.Pool
        public Tube create() {
            return TubeCloner.clone(this.master);
        }

        @Deprecated
        public final Tube takeMaster() {
            return this.master;
        }
    }
}
