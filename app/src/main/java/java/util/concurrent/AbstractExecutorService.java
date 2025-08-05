package java.util.concurrent;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/* loaded from: rt.jar:java/util/concurrent/AbstractExecutorService.class */
public abstract class AbstractExecutorService implements ExecutorService {
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !AbstractExecutorService.class.desiredAssertionStatus();
    }

    protected <T> RunnableFuture<T> newTaskFor(Runnable runnable, T t2) {
        return new FutureTask(runnable, t2);
    }

    protected <T> RunnableFuture<T> newTaskFor(Callable<T> callable) {
        return new FutureTask(callable);
    }

    @Override // java.util.concurrent.ExecutorService
    public Future<?> submit(Runnable runnable) {
        if (runnable == null) {
            throw new NullPointerException();
        }
        RunnableFuture runnableFutureNewTaskFor = newTaskFor(runnable, null);
        execute(runnableFutureNewTaskFor);
        return runnableFutureNewTaskFor;
    }

    @Override // java.util.concurrent.ExecutorService
    public <T> Future<T> submit(Runnable runnable, T t2) {
        if (runnable == null) {
            throw new NullPointerException();
        }
        RunnableFuture<T> runnableFutureNewTaskFor = newTaskFor(runnable, t2);
        execute(runnableFutureNewTaskFor);
        return runnableFutureNewTaskFor;
    }

    @Override // java.util.concurrent.ExecutorService
    public <T> Future<T> submit(Callable<T> callable) {
        if (callable == null) {
            throw new NullPointerException();
        }
        RunnableFuture<T> runnableFutureNewTaskFor = newTaskFor(callable);
        execute(runnableFutureNewTaskFor);
        return runnableFutureNewTaskFor;
    }

    private <T> T doInvokeAny(Collection<? extends Callable<T>> collection, boolean z2, long j2) throws ExecutionException, InterruptedException, TimeoutException {
        long jNanoTime;
        if (collection == null) {
            throw new NullPointerException();
        }
        int size = collection.size();
        if (size == 0) {
            throw new IllegalArgumentException();
        }
        ArrayList arrayList = new ArrayList(size);
        ExecutorCompletionService executorCompletionService = new ExecutorCompletionService(this);
        ExecutionException executionException = null;
        if (z2) {
            try {
                jNanoTime = System.nanoTime() + j2;
            } catch (Throwable th) {
                int size2 = arrayList.size();
                for (int i2 = 0; i2 < size2; i2++) {
                    ((Future) arrayList.get(i2)).cancel(true);
                }
                throw th;
            }
        } else {
            jNanoTime = 0;
        }
        long j3 = jNanoTime;
        Iterator<? extends Callable<T>> it = collection.iterator();
        arrayList.add(executorCompletionService.submit(it.next()));
        int i3 = size - 1;
        int i4 = 1;
        while (true) {
            Future futurePoll = executorCompletionService.poll();
            if (futurePoll == null) {
                if (i3 > 0) {
                    i3--;
                    arrayList.add(executorCompletionService.submit(it.next()));
                    i4++;
                } else {
                    if (i4 == 0) {
                        if (executionException == null) {
                            executionException = new ExecutionException();
                        }
                        throw executionException;
                    }
                    if (z2) {
                        futurePoll = executorCompletionService.poll(j2, TimeUnit.NANOSECONDS);
                        if (futurePoll == null) {
                            throw new TimeoutException();
                        }
                        j2 = j3 - System.nanoTime();
                    } else {
                        futurePoll = executorCompletionService.take();
                    }
                }
            }
            if (futurePoll != null) {
                i4--;
                try {
                    T t2 = (T) futurePoll.get();
                    int size3 = arrayList.size();
                    for (int i5 = 0; i5 < size3; i5++) {
                        ((Future) arrayList.get(i5)).cancel(true);
                    }
                    return t2;
                } catch (RuntimeException e2) {
                    executionException = new ExecutionException(e2);
                } catch (ExecutionException e3) {
                    executionException = e3;
                }
            }
        }
    }

    @Override // java.util.concurrent.ExecutorService
    public <T> T invokeAny(Collection<? extends Callable<T>> collection) throws ExecutionException, InterruptedException {
        try {
            return (T) doInvokeAny(collection, false, 0L);
        } catch (TimeoutException e2) {
            if ($assertionsDisabled) {
                return null;
            }
            throw new AssertionError();
        }
    }

    @Override // java.util.concurrent.ExecutorService
    public <T> T invokeAny(Collection<? extends Callable<T>> collection, long j2, TimeUnit timeUnit) throws ExecutionException, InterruptedException, TimeoutException {
        return (T) doInvokeAny(collection, true, timeUnit.toNanos(j2));
    }

    @Override // java.util.concurrent.ExecutorService
    public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> collection) throws InterruptedException {
        if (collection == null) {
            throw new NullPointerException();
        }
        ArrayList arrayList = new ArrayList(collection.size());
        try {
            Iterator<? extends Callable<T>> it = collection.iterator();
            while (it.hasNext()) {
                RunnableFuture<T> runnableFutureNewTaskFor = newTaskFor(it.next());
                arrayList.add(runnableFutureNewTaskFor);
                execute(runnableFutureNewTaskFor);
            }
            int size = arrayList.size();
            for (int i2 = 0; i2 < size; i2++) {
                Future future = (Future) arrayList.get(i2);
                if (!future.isDone()) {
                    try {
                        future.get();
                    } catch (CancellationException e2) {
                    } catch (ExecutionException e3) {
                    }
                }
            }
            if (1 == 0) {
                int size2 = arrayList.size();
                for (int i3 = 0; i3 < size2; i3++) {
                    ((Future) arrayList.get(i3)).cancel(true);
                }
            }
            return arrayList;
        } catch (Throwable th) {
            if (0 == 0) {
                int size3 = arrayList.size();
                for (int i4 = 0; i4 < size3; i4++) {
                    ((Future) arrayList.get(i4)).cancel(true);
                }
            }
            throw th;
        }
    }

    @Override // java.util.concurrent.ExecutorService
    public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> collection, long j2, TimeUnit timeUnit) throws InterruptedException {
        if (collection == null) {
            throw new NullPointerException();
        }
        long nanos = timeUnit.toNanos(j2);
        ArrayList arrayList = new ArrayList(collection.size());
        try {
            Iterator<? extends Callable<T>> it = collection.iterator();
            while (it.hasNext()) {
                arrayList.add(newTaskFor(it.next()));
            }
            long jNanoTime = System.nanoTime() + nanos;
            int size = arrayList.size();
            for (int i2 = 0; i2 < size; i2++) {
                execute((Runnable) arrayList.get(i2));
                nanos = jNanoTime - System.nanoTime();
                if (nanos <= 0) {
                    return arrayList;
                }
            }
            for (int i3 = 0; i3 < size; i3++) {
                Future future = (Future) arrayList.get(i3);
                if (!future.isDone()) {
                    if (nanos <= 0) {
                        if (0 == 0) {
                            int size2 = arrayList.size();
                            for (int i4 = 0; i4 < size2; i4++) {
                                ((Future) arrayList.get(i4)).cancel(true);
                            }
                        }
                        return arrayList;
                    }
                    try {
                        future.get(nanos, TimeUnit.NANOSECONDS);
                    } catch (CancellationException e2) {
                    } catch (ExecutionException e3) {
                    } catch (TimeoutException e4) {
                        if (0 == 0) {
                            int size3 = arrayList.size();
                            for (int i5 = 0; i5 < size3; i5++) {
                                ((Future) arrayList.get(i5)).cancel(true);
                            }
                        }
                        return arrayList;
                    }
                    nanos = jNanoTime - System.nanoTime();
                }
            }
            if (1 == 0) {
                int size4 = arrayList.size();
                for (int i6 = 0; i6 < size4; i6++) {
                    ((Future) arrayList.get(i6)).cancel(true);
                }
            }
            return arrayList;
        } finally {
            if (0 == 0) {
                int size5 = arrayList.size();
                for (int i7 = 0; i7 < size5; i7++) {
                    ((Future) arrayList.get(i7)).cancel(true);
                }
            }
        }
    }
}
