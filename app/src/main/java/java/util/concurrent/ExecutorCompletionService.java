package java.util.concurrent;

/* loaded from: rt.jar:java/util/concurrent/ExecutorCompletionService.class */
public class ExecutorCompletionService<V> implements CompletionService<V> {
    private final Executor executor;
    private final AbstractExecutorService aes;
    private final BlockingQueue<Future<V>> completionQueue;

    /* loaded from: rt.jar:java/util/concurrent/ExecutorCompletionService$QueueingFuture.class */
    private class QueueingFuture extends FutureTask<Void> {
        private final Future<V> task;

        QueueingFuture(RunnableFuture<V> runnableFuture) {
            super(runnableFuture, null);
            this.task = runnableFuture;
        }

        @Override // java.util.concurrent.FutureTask
        protected void done() {
            ExecutorCompletionService.this.completionQueue.add(this.task);
        }
    }

    private RunnableFuture<V> newTaskFor(Callable<V> callable) {
        if (this.aes == null) {
            return new FutureTask(callable);
        }
        return this.aes.newTaskFor(callable);
    }

    private RunnableFuture<V> newTaskFor(Runnable runnable, V v2) {
        if (this.aes == null) {
            return new FutureTask(runnable, v2);
        }
        return this.aes.newTaskFor(runnable, v2);
    }

    public ExecutorCompletionService(Executor executor) {
        if (executor == null) {
            throw new NullPointerException();
        }
        this.executor = executor;
        this.aes = executor instanceof AbstractExecutorService ? (AbstractExecutorService) executor : null;
        this.completionQueue = new LinkedBlockingQueue();
    }

    public ExecutorCompletionService(Executor executor, BlockingQueue<Future<V>> blockingQueue) {
        if (executor == null || blockingQueue == null) {
            throw new NullPointerException();
        }
        this.executor = executor;
        this.aes = executor instanceof AbstractExecutorService ? (AbstractExecutorService) executor : null;
        this.completionQueue = blockingQueue;
    }

    @Override // java.util.concurrent.CompletionService
    public Future<V> submit(Callable<V> callable) {
        if (callable == null) {
            throw new NullPointerException();
        }
        RunnableFuture<V> runnableFutureNewTaskFor = newTaskFor(callable);
        this.executor.execute(new QueueingFuture(runnableFutureNewTaskFor));
        return runnableFutureNewTaskFor;
    }

    @Override // java.util.concurrent.CompletionService
    public Future<V> submit(Runnable runnable, V v2) {
        if (runnable == null) {
            throw new NullPointerException();
        }
        RunnableFuture<V> runnableFutureNewTaskFor = newTaskFor(runnable, v2);
        this.executor.execute(new QueueingFuture(runnableFutureNewTaskFor));
        return runnableFutureNewTaskFor;
    }

    @Override // java.util.concurrent.CompletionService
    public Future<V> take() throws InterruptedException {
        return this.completionQueue.take2();
    }

    @Override // java.util.concurrent.CompletionService
    public Future<V> poll() {
        return this.completionQueue.poll();
    }

    @Override // java.util.concurrent.CompletionService
    public Future<V> poll(long j2, TimeUnit timeUnit) throws InterruptedException {
        return this.completionQueue.poll2(j2, timeUnit);
    }
}
