package com.sun.xml.internal.ws.api.pipe;

import com.sun.xml.internal.ws.api.server.Container;
import com.sun.xml.internal.ws.api.server.ContainerResolver;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/* loaded from: rt.jar:com/sun/xml/internal/ws/api/pipe/Engine.class */
public class Engine {
    private volatile Executor threadPool;
    public final String id;
    private final Container container;

    String getId() {
        return this.id;
    }

    Container getContainer() {
        return this.container;
    }

    Executor getExecutor() {
        return this.threadPool;
    }

    public Engine(String id, Executor threadPool) {
        this(id, ContainerResolver.getDefault().getContainer(), threadPool);
    }

    public Engine(String id, Container container, Executor threadPool) {
        this(id, container);
        this.threadPool = threadPool != null ? wrap(threadPool) : null;
    }

    public Engine(String id) {
        this(id, ContainerResolver.getDefault().getContainer());
    }

    public Engine(String id, Container container) {
        this.id = id;
        this.container = container;
    }

    public void setExecutor(Executor threadPool) {
        this.threadPool = threadPool != null ? wrap(threadPool) : null;
    }

    void addRunnable(Fiber fiber) {
        if (this.threadPool == null) {
            synchronized (this) {
                this.threadPool = wrap(Executors.newCachedThreadPool(new DaemonThreadFactory()));
            }
        }
        this.threadPool.execute(fiber);
    }

    private Executor wrap(Executor ex) {
        return ContainerResolver.getDefault().wrapExecutor(this.container, ex);
    }

    public Fiber createFiber() {
        return new Fiber(this);
    }

    /* loaded from: rt.jar:com/sun/xml/internal/ws/api/pipe/Engine$DaemonThreadFactory.class */
    private static class DaemonThreadFactory implements ThreadFactory {
        static final AtomicInteger poolNumber = new AtomicInteger(1);
        final AtomicInteger threadNumber = new AtomicInteger(1);
        final String namePrefix = "jaxws-engine-" + poolNumber.getAndIncrement() + "-thread-";

        DaemonThreadFactory() {
        }

        @Override // java.util.concurrent.ThreadFactory
        public Thread newThread(Runnable r2) {
            Thread t2 = new Thread(null, r2, this.namePrefix + this.threadNumber.getAndIncrement(), 0L);
            if (!t2.isDaemon()) {
                t2.setDaemon(true);
            }
            if (t2.getPriority() != 5) {
                t2.setPriority(5);
            }
            return t2;
        }
    }
}
