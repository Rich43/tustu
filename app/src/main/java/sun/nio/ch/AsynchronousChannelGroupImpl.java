package sun.nio.ch;

import java.io.FileDescriptor;
import java.io.IOException;
import java.nio.channels.AsynchronousChannelGroup;
import java.nio.channels.Channel;
import java.nio.channels.spi.AsynchronousChannelProvider;
import java.security.AccessControlContext;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import sun.security.action.GetIntegerAction;

/* loaded from: rt.jar:sun/nio/ch/AsynchronousChannelGroupImpl.class */
abstract class AsynchronousChannelGroupImpl extends AsynchronousChannelGroup implements Executor {
    private static final int internalThreadCount = ((Integer) AccessController.doPrivileged(new GetIntegerAction("sun.nio.ch.internalThreadPoolSize", 1))).intValue();
    private final ThreadPool pool;
    private final AtomicInteger threadCount;
    private ScheduledThreadPoolExecutor timeoutExecutor;
    private final Queue<Runnable> taskQueue;
    private final AtomicBoolean shutdown;
    private final Object shutdownNowLock;
    private volatile boolean terminateInitiated;

    abstract void executeOnHandlerTask(Runnable runnable);

    abstract boolean isEmpty();

    abstract Object attachForeignChannel(Channel channel, FileDescriptor fileDescriptor) throws IOException;

    abstract void detachForeignChannel(Object obj);

    abstract void closeAllChannels() throws IOException;

    abstract void shutdownHandlerTasks();

    AsynchronousChannelGroupImpl(AsynchronousChannelProvider asynchronousChannelProvider, ThreadPool threadPool) {
        super(asynchronousChannelProvider);
        this.threadCount = new AtomicInteger();
        this.shutdown = new AtomicBoolean();
        this.shutdownNowLock = new Object();
        this.pool = threadPool;
        if (threadPool.isFixedThreadPool()) {
            this.taskQueue = new ConcurrentLinkedQueue();
        } else {
            this.taskQueue = null;
        }
        this.timeoutExecutor = (ScheduledThreadPoolExecutor) Executors.newScheduledThreadPool(1, ThreadPool.defaultThreadFactory());
        this.timeoutExecutor.setRemoveOnCancelPolicy(true);
    }

    final ExecutorService executor() {
        return this.pool.executor();
    }

    final boolean isFixedThreadPool() {
        return this.pool.isFixedThreadPool();
    }

    final int fixedThreadCount() {
        if (isFixedThreadPool()) {
            return this.pool.poolSize();
        }
        return this.pool.poolSize() + internalThreadCount;
    }

    private Runnable bindToGroup(final Runnable runnable) {
        return new Runnable() { // from class: sun.nio.ch.AsynchronousChannelGroupImpl.1
            @Override // java.lang.Runnable
            public void run() {
                Invoker.bindToGroup(this);
                runnable.run();
            }
        };
    }

    private void startInternalThread(final Runnable runnable) {
        AccessController.doPrivileged(new PrivilegedAction<Void>() { // from class: sun.nio.ch.AsynchronousChannelGroupImpl.2
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.security.PrivilegedAction
            public Void run() {
                ThreadPool.defaultThreadFactory().newThread(runnable).start();
                return null;
            }
        });
    }

    protected final void startThreads(Runnable runnable) {
        if (!isFixedThreadPool()) {
            for (int i2 = 0; i2 < internalThreadCount; i2++) {
                startInternalThread(runnable);
                this.threadCount.incrementAndGet();
            }
        }
        if (this.pool.poolSize() > 0) {
            Runnable runnableBindToGroup = bindToGroup(runnable);
            for (int i3 = 0; i3 < this.pool.poolSize(); i3++) {
                try {
                    this.pool.executor().execute(runnableBindToGroup);
                    this.threadCount.incrementAndGet();
                } catch (RejectedExecutionException e2) {
                    return;
                }
            }
        }
    }

    final int threadCount() {
        return this.threadCount.get();
    }

    final int threadExit(Runnable runnable, boolean z2) {
        if (z2) {
            try {
                if (Invoker.isBoundToAnyGroup()) {
                    this.pool.executor().execute(bindToGroup(runnable));
                } else {
                    startInternalThread(runnable);
                }
                return this.threadCount.get();
            } catch (RejectedExecutionException e2) {
            }
        }
        return this.threadCount.decrementAndGet();
    }

    final void executeOnPooledThread(Runnable runnable) {
        if (isFixedThreadPool()) {
            executeOnHandlerTask(runnable);
        } else {
            this.pool.executor().execute(bindToGroup(runnable));
        }
    }

    final void offerTask(Runnable runnable) {
        this.taskQueue.offer(runnable);
    }

    final Runnable pollTask() {
        if (this.taskQueue == null) {
            return null;
        }
        return this.taskQueue.poll();
    }

    final Future<?> schedule(Runnable runnable, long j2, TimeUnit timeUnit) {
        try {
            return this.timeoutExecutor.schedule(runnable, j2, timeUnit);
        } catch (RejectedExecutionException e2) {
            if (this.terminateInitiated) {
                return null;
            }
            throw new AssertionError(e2);
        }
    }

    @Override // java.nio.channels.AsynchronousChannelGroup
    public final boolean isShutdown() {
        return this.shutdown.get();
    }

    @Override // java.nio.channels.AsynchronousChannelGroup
    public final boolean isTerminated() {
        return this.pool.executor().isTerminated();
    }

    private void shutdownExecutors() {
        AccessController.doPrivileged(new PrivilegedAction<Void>() { // from class: sun.nio.ch.AsynchronousChannelGroupImpl.3
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.security.PrivilegedAction
            public Void run() {
                AsynchronousChannelGroupImpl.this.pool.executor().shutdown();
                AsynchronousChannelGroupImpl.this.timeoutExecutor.shutdown();
                return null;
            }
        }, (AccessControlContext) null, new RuntimePermission("modifyThread"));
    }

    @Override // java.nio.channels.AsynchronousChannelGroup
    public final void shutdown() {
        if (this.shutdown.getAndSet(true) || !isEmpty()) {
            return;
        }
        synchronized (this.shutdownNowLock) {
            if (!this.terminateInitiated) {
                this.terminateInitiated = true;
                shutdownHandlerTasks();
                shutdownExecutors();
            }
        }
    }

    @Override // java.nio.channels.AsynchronousChannelGroup
    public final void shutdownNow() throws IOException {
        this.shutdown.set(true);
        synchronized (this.shutdownNowLock) {
            if (!this.terminateInitiated) {
                this.terminateInitiated = true;
                closeAllChannels();
                shutdownHandlerTasks();
                shutdownExecutors();
            }
        }
    }

    final void detachFromThreadPool() {
        if (this.shutdown.getAndSet(true)) {
            throw new AssertionError((Object) "Already shutdown");
        }
        if (!isEmpty()) {
            throw new AssertionError((Object) "Group not empty");
        }
        shutdownHandlerTasks();
    }

    @Override // java.nio.channels.AsynchronousChannelGroup
    public final boolean awaitTermination(long j2, TimeUnit timeUnit) throws InterruptedException {
        return this.pool.executor().awaitTermination(j2, timeUnit);
    }

    @Override // java.util.concurrent.Executor
    public final void execute(final Runnable runnable) {
        if (System.getSecurityManager() != null) {
            final AccessControlContext context = AccessController.getContext();
            runnable = new Runnable() { // from class: sun.nio.ch.AsynchronousChannelGroupImpl.4
                @Override // java.lang.Runnable
                public void run() {
                    AccessController.doPrivileged(new PrivilegedAction<Void>() { // from class: sun.nio.ch.AsynchronousChannelGroupImpl.4.1
                        /* JADX WARN: Can't rename method to resolve collision */
                        @Override // java.security.PrivilegedAction
                        public Void run() {
                            runnable.run();
                            return null;
                        }
                    }, context);
                }
            };
        }
        executeOnPooledThread(runnable);
    }
}
