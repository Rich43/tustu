package com.sun.xml.internal.ws.api.server;

import java.util.concurrent.Executor;

/* loaded from: rt.jar:com/sun/xml/internal/ws/api/server/ThreadLocalContainerResolver.class */
public class ThreadLocalContainerResolver extends ContainerResolver {
    private ThreadLocal<Container> containerThreadLocal = new ThreadLocal<Container>() { // from class: com.sun.xml.internal.ws.api.server.ThreadLocalContainerResolver.1
        /* JADX INFO: Access modifiers changed from: protected */
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // java.lang.ThreadLocal
        public Container initialValue() {
            return Container.NONE;
        }
    };

    @Override // com.sun.xml.internal.ws.api.server.ContainerResolver
    public Container getContainer() {
        return this.containerThreadLocal.get();
    }

    public Container enterContainer(Container container) {
        Container old = this.containerThreadLocal.get();
        this.containerThreadLocal.set(container);
        return old;
    }

    public void exitContainer(Container old) {
        this.containerThreadLocal.set(old);
    }

    public Executor wrapExecutor(final Container container, final Executor ex) {
        if (ex == null) {
            return null;
        }
        return new Executor() { // from class: com.sun.xml.internal.ws.api.server.ThreadLocalContainerResolver.2
            @Override // java.util.concurrent.Executor
            public void execute(final Runnable command) {
                ex.execute(new Runnable() { // from class: com.sun.xml.internal.ws.api.server.ThreadLocalContainerResolver.2.1
                    @Override // java.lang.Runnable
                    public void run() {
                        Container old = ThreadLocalContainerResolver.this.enterContainer(container);
                        try {
                            command.run();
                        } finally {
                            ThreadLocalContainerResolver.this.exitContainer(old);
                        }
                    }
                });
            }
        };
    }
}
