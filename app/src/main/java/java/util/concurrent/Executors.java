package java.util.concurrent;

import java.security.AccessControlContext;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import sun.security.util.SecurityConstants;

/* loaded from: rt.jar:java/util/concurrent/Executors.class */
public class Executors {
    public static ExecutorService newFixedThreadPool(int i2) {
        return new ThreadPoolExecutor(i2, i2, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue());
    }

    public static ExecutorService newWorkStealingPool(int i2) {
        return new ForkJoinPool(i2, ForkJoinPool.defaultForkJoinWorkerThreadFactory, null, true);
    }

    public static ExecutorService newWorkStealingPool() {
        return new ForkJoinPool(Runtime.getRuntime().availableProcessors(), ForkJoinPool.defaultForkJoinWorkerThreadFactory, null, true);
    }

    public static ExecutorService newFixedThreadPool(int i2, ThreadFactory threadFactory) {
        return new ThreadPoolExecutor(i2, i2, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue(), threadFactory);
    }

    public static ExecutorService newSingleThreadExecutor() {
        return new FinalizableDelegatedExecutorService(new ThreadPoolExecutor(1, 1, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue()));
    }

    public static ExecutorService newSingleThreadExecutor(ThreadFactory threadFactory) {
        return new FinalizableDelegatedExecutorService(new ThreadPoolExecutor(1, 1, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue(), threadFactory));
    }

    public static ExecutorService newCachedThreadPool() {
        return new ThreadPoolExecutor(0, Integer.MAX_VALUE, 60L, TimeUnit.SECONDS, new SynchronousQueue());
    }

    public static ExecutorService newCachedThreadPool(ThreadFactory threadFactory) {
        return new ThreadPoolExecutor(0, Integer.MAX_VALUE, 60L, TimeUnit.SECONDS, new SynchronousQueue(), threadFactory);
    }

    public static ScheduledExecutorService newSingleThreadScheduledExecutor() {
        return new DelegatedScheduledExecutorService(new ScheduledThreadPoolExecutor(1));
    }

    public static ScheduledExecutorService newSingleThreadScheduledExecutor(ThreadFactory threadFactory) {
        return new DelegatedScheduledExecutorService(new ScheduledThreadPoolExecutor(1, threadFactory));
    }

    public static ScheduledExecutorService newScheduledThreadPool(int i2) {
        return new ScheduledThreadPoolExecutor(i2);
    }

    public static ScheduledExecutorService newScheduledThreadPool(int i2, ThreadFactory threadFactory) {
        return new ScheduledThreadPoolExecutor(i2, threadFactory);
    }

    public static ExecutorService unconfigurableExecutorService(ExecutorService executorService) {
        if (executorService == null) {
            throw new NullPointerException();
        }
        return new DelegatedExecutorService(executorService);
    }

    public static ScheduledExecutorService unconfigurableScheduledExecutorService(ScheduledExecutorService scheduledExecutorService) {
        if (scheduledExecutorService == null) {
            throw new NullPointerException();
        }
        return new DelegatedScheduledExecutorService(scheduledExecutorService);
    }

    public static ThreadFactory defaultThreadFactory() {
        return new DefaultThreadFactory();
    }

    public static ThreadFactory privilegedThreadFactory() {
        return new PrivilegedThreadFactory();
    }

    public static <T> Callable<T> callable(Runnable runnable, T t2) {
        if (runnable == null) {
            throw new NullPointerException();
        }
        return new RunnableAdapter(runnable, t2);
    }

    public static Callable<Object> callable(Runnable runnable) {
        if (runnable == null) {
            throw new NullPointerException();
        }
        return new RunnableAdapter(runnable, null);
    }

    public static Callable<Object> callable(final PrivilegedAction<?> privilegedAction) {
        if (privilegedAction == null) {
            throw new NullPointerException();
        }
        return new Callable<Object>() { // from class: java.util.concurrent.Executors.1
            @Override // java.util.concurrent.Callable
            public Object call() {
                return privilegedAction.run2();
            }
        };
    }

    public static Callable<Object> callable(final PrivilegedExceptionAction<?> privilegedExceptionAction) {
        if (privilegedExceptionAction == null) {
            throw new NullPointerException();
        }
        return new Callable<Object>() { // from class: java.util.concurrent.Executors.2
            @Override // java.util.concurrent.Callable
            public Object call() throws Exception {
                return privilegedExceptionAction.run();
            }
        };
    }

    public static <T> Callable<T> privilegedCallable(Callable<T> callable) {
        if (callable == null) {
            throw new NullPointerException();
        }
        return new PrivilegedCallable(callable);
    }

    public static <T> Callable<T> privilegedCallableUsingCurrentClassLoader(Callable<T> callable) {
        if (callable == null) {
            throw new NullPointerException();
        }
        return new PrivilegedCallableUsingCurrentClassLoader(callable);
    }

    /* loaded from: rt.jar:java/util/concurrent/Executors$RunnableAdapter.class */
    static final class RunnableAdapter<T> implements Callable<T> {
        final Runnable task;
        final T result;

        RunnableAdapter(Runnable runnable, T t2) {
            this.task = runnable;
            this.result = t2;
        }

        @Override // java.util.concurrent.Callable
        public T call() {
            this.task.run();
            return this.result;
        }
    }

    /* loaded from: rt.jar:java/util/concurrent/Executors$PrivilegedCallable.class */
    static final class PrivilegedCallable<T> implements Callable<T> {
        private final Callable<T> task;
        private final AccessControlContext acc = AccessController.getContext();

        PrivilegedCallable(Callable<T> callable) {
            this.task = callable;
        }

        @Override // java.util.concurrent.Callable
        public T call() throws Exception {
            try {
                return (T) AccessController.doPrivileged(new PrivilegedExceptionAction<T>() { // from class: java.util.concurrent.Executors.PrivilegedCallable.1
                    @Override // java.security.PrivilegedExceptionAction
                    public T run() throws Exception {
                        return (T) PrivilegedCallable.this.task.call();
                    }
                }, this.acc);
            } catch (PrivilegedActionException e2) {
                throw e2.getException();
            }
        }
    }

    /* loaded from: rt.jar:java/util/concurrent/Executors$PrivilegedCallableUsingCurrentClassLoader.class */
    static final class PrivilegedCallableUsingCurrentClassLoader<T> implements Callable<T> {
        private final Callable<T> task;
        private final AccessControlContext acc;
        private final ClassLoader ccl;

        PrivilegedCallableUsingCurrentClassLoader(Callable<T> callable) {
            SecurityManager securityManager = System.getSecurityManager();
            if (securityManager != null) {
                securityManager.checkPermission(SecurityConstants.GET_CLASSLOADER_PERMISSION);
                securityManager.checkPermission(new RuntimePermission("setContextClassLoader"));
            }
            this.task = callable;
            this.acc = AccessController.getContext();
            this.ccl = Thread.currentThread().getContextClassLoader();
        }

        @Override // java.util.concurrent.Callable
        public T call() throws Exception {
            try {
                return (T) AccessController.doPrivileged(new PrivilegedExceptionAction<T>() { // from class: java.util.concurrent.Executors.PrivilegedCallableUsingCurrentClassLoader.1
                    @Override // java.security.PrivilegedExceptionAction
                    public T run() throws Exception {
                        Thread threadCurrentThread = Thread.currentThread();
                        ClassLoader contextClassLoader = threadCurrentThread.getContextClassLoader();
                        if (PrivilegedCallableUsingCurrentClassLoader.this.ccl == contextClassLoader) {
                            return (T) PrivilegedCallableUsingCurrentClassLoader.this.task.call();
                        }
                        threadCurrentThread.setContextClassLoader(PrivilegedCallableUsingCurrentClassLoader.this.ccl);
                        try {
                            T t2 = (T) PrivilegedCallableUsingCurrentClassLoader.this.task.call();
                            threadCurrentThread.setContextClassLoader(contextClassLoader);
                            return t2;
                        } catch (Throwable th) {
                            threadCurrentThread.setContextClassLoader(contextClassLoader);
                            throw th;
                        }
                    }
                }, this.acc);
            } catch (PrivilegedActionException e2) {
                throw e2.getException();
            }
        }
    }

    /* loaded from: rt.jar:java/util/concurrent/Executors$DefaultThreadFactory.class */
    static class DefaultThreadFactory implements ThreadFactory {
        private static final AtomicInteger poolNumber = new AtomicInteger(1);
        private final ThreadGroup group;
        private final AtomicInteger threadNumber = new AtomicInteger(1);
        private final String namePrefix;

        DefaultThreadFactory() {
            SecurityManager securityManager = System.getSecurityManager();
            this.group = securityManager != null ? securityManager.getThreadGroup() : Thread.currentThread().getThreadGroup();
            this.namePrefix = "pool-" + poolNumber.getAndIncrement() + "-thread-";
        }

        @Override // java.util.concurrent.ThreadFactory
        public Thread newThread(Runnable runnable) {
            Thread thread = new Thread(this.group, runnable, this.namePrefix + this.threadNumber.getAndIncrement(), 0L);
            if (thread.isDaemon()) {
                thread.setDaemon(false);
            }
            if (thread.getPriority() != 5) {
                thread.setPriority(5);
            }
            return thread;
        }
    }

    /* loaded from: rt.jar:java/util/concurrent/Executors$PrivilegedThreadFactory.class */
    static class PrivilegedThreadFactory extends DefaultThreadFactory {
        private final AccessControlContext acc;
        private final ClassLoader ccl;

        PrivilegedThreadFactory() {
            SecurityManager securityManager = System.getSecurityManager();
            if (securityManager != null) {
                securityManager.checkPermission(SecurityConstants.GET_CLASSLOADER_PERMISSION);
                securityManager.checkPermission(new RuntimePermission("setContextClassLoader"));
            }
            this.acc = AccessController.getContext();
            this.ccl = Thread.currentThread().getContextClassLoader();
        }

        @Override // java.util.concurrent.Executors.DefaultThreadFactory, java.util.concurrent.ThreadFactory
        public Thread newThread(final Runnable runnable) {
            return super.newThread(new Runnable() { // from class: java.util.concurrent.Executors.PrivilegedThreadFactory.1
                @Override // java.lang.Runnable
                public void run() {
                    AccessController.doPrivileged(new PrivilegedAction<Void>() { // from class: java.util.concurrent.Executors.PrivilegedThreadFactory.1.1
                        /* JADX WARN: Can't rename method to resolve collision */
                        @Override // java.security.PrivilegedAction
                        /* renamed from: run */
                        public Void run2() {
                            Thread.currentThread().setContextClassLoader(PrivilegedThreadFactory.this.ccl);
                            runnable.run();
                            return null;
                        }
                    }, PrivilegedThreadFactory.this.acc);
                }
            });
        }
    }

    /* loaded from: rt.jar:java/util/concurrent/Executors$DelegatedExecutorService.class */
    static class DelegatedExecutorService extends AbstractExecutorService {

        /* renamed from: e, reason: collision with root package name */
        private final ExecutorService f12583e;

        DelegatedExecutorService(ExecutorService executorService) {
            this.f12583e = executorService;
        }

        @Override // java.util.concurrent.Executor
        public void execute(Runnable runnable) {
            this.f12583e.execute(runnable);
        }

        @Override // java.util.concurrent.ExecutorService
        public void shutdown() {
            this.f12583e.shutdown();
        }

        @Override // java.util.concurrent.ExecutorService
        public List<Runnable> shutdownNow() {
            return this.f12583e.shutdownNow();
        }

        @Override // java.util.concurrent.ExecutorService
        public boolean isShutdown() {
            return this.f12583e.isShutdown();
        }

        @Override // java.util.concurrent.ExecutorService
        public boolean isTerminated() {
            return this.f12583e.isTerminated();
        }

        @Override // java.util.concurrent.ExecutorService
        public boolean awaitTermination(long j2, TimeUnit timeUnit) throws InterruptedException {
            return this.f12583e.awaitTermination(j2, timeUnit);
        }

        @Override // java.util.concurrent.AbstractExecutorService, java.util.concurrent.ExecutorService
        public Future<?> submit(Runnable runnable) {
            return this.f12583e.submit(runnable);
        }

        @Override // java.util.concurrent.AbstractExecutorService, java.util.concurrent.ExecutorService
        public <T> Future<T> submit(Callable<T> callable) {
            return this.f12583e.submit(callable);
        }

        @Override // java.util.concurrent.AbstractExecutorService, java.util.concurrent.ExecutorService
        public <T> Future<T> submit(Runnable runnable, T t2) {
            return this.f12583e.submit(runnable, t2);
        }

        @Override // java.util.concurrent.AbstractExecutorService, java.util.concurrent.ExecutorService
        public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> collection) throws InterruptedException {
            return this.f12583e.invokeAll(collection);
        }

        @Override // java.util.concurrent.AbstractExecutorService, java.util.concurrent.ExecutorService
        public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> collection, long j2, TimeUnit timeUnit) throws InterruptedException {
            return this.f12583e.invokeAll(collection, j2, timeUnit);
        }

        @Override // java.util.concurrent.AbstractExecutorService, java.util.concurrent.ExecutorService
        public <T> T invokeAny(Collection<? extends Callable<T>> collection) throws ExecutionException, InterruptedException {
            return (T) this.f12583e.invokeAny(collection);
        }

        @Override // java.util.concurrent.AbstractExecutorService, java.util.concurrent.ExecutorService
        public <T> T invokeAny(Collection<? extends Callable<T>> collection, long j2, TimeUnit timeUnit) throws ExecutionException, InterruptedException, TimeoutException {
            return (T) this.f12583e.invokeAny(collection, j2, timeUnit);
        }
    }

    /* loaded from: rt.jar:java/util/concurrent/Executors$FinalizableDelegatedExecutorService.class */
    static class FinalizableDelegatedExecutorService extends DelegatedExecutorService {
        FinalizableDelegatedExecutorService(ExecutorService executorService) {
            super(executorService);
        }

        protected void finalize() {
            super.shutdown();
        }
    }

    /* loaded from: rt.jar:java/util/concurrent/Executors$DelegatedScheduledExecutorService.class */
    static class DelegatedScheduledExecutorService extends DelegatedExecutorService implements ScheduledExecutorService {

        /* renamed from: e, reason: collision with root package name */
        private final ScheduledExecutorService f12584e;

        DelegatedScheduledExecutorService(ScheduledExecutorService scheduledExecutorService) {
            super(scheduledExecutorService);
            this.f12584e = scheduledExecutorService;
        }

        @Override // java.util.concurrent.ScheduledExecutorService
        public ScheduledFuture<?> schedule(Runnable runnable, long j2, TimeUnit timeUnit) {
            return this.f12584e.schedule(runnable, j2, timeUnit);
        }

        @Override // java.util.concurrent.ScheduledExecutorService
        public <V> ScheduledFuture<V> schedule(Callable<V> callable, long j2, TimeUnit timeUnit) {
            return this.f12584e.schedule(callable, j2, timeUnit);
        }

        @Override // java.util.concurrent.ScheduledExecutorService
        public ScheduledFuture<?> scheduleAtFixedRate(Runnable runnable, long j2, long j3, TimeUnit timeUnit) {
            return this.f12584e.scheduleAtFixedRate(runnable, j2, j3, timeUnit);
        }

        @Override // java.util.concurrent.ScheduledExecutorService
        public ScheduledFuture<?> scheduleWithFixedDelay(Runnable runnable, long j2, long j3, TimeUnit timeUnit) {
            return this.f12584e.scheduleWithFixedDelay(runnable, j2, j3, timeUnit);
        }
    }

    private Executors() {
    }
}
