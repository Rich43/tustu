package com.sun.javafx.runtime.async;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import javafx.application.Platform;

/* loaded from: jfxrt.jar:com/sun/javafx/runtime/async/AbstractAsyncOperation.class */
public abstract class AbstractAsyncOperation<V> implements AsyncOperation, Callable<V> {
    protected final FutureTask<V> future;
    protected final AsyncOperationListener listener;
    private int progressGranularity = 100;
    private int progressMax;
    private int lastProgress;
    private int progressIncrement;
    private int nextProgress;
    private int bytesRead;

    protected AbstractAsyncOperation(final AsyncOperationListener<V> listener) {
        this.listener = listener;
        Callable<V> callable = () -> {
            return call();
        };
        final Runnable completionRunnable = new Runnable() { // from class: com.sun.javafx.runtime.async.AbstractAsyncOperation.1
            @Override // java.lang.Runnable
            public void run() {
                if (AbstractAsyncOperation.this.future.isCancelled()) {
                    listener.onCancel();
                    return;
                }
                try {
                    listener.onCompletion(AbstractAsyncOperation.this.future.get());
                } catch (InterruptedException e2) {
                    listener.onCancel();
                } catch (ExecutionException e3) {
                    listener.onException(e3);
                }
            }
        };
        this.future = new FutureTask<V>(callable) { // from class: com.sun.javafx.runtime.async.AbstractAsyncOperation.2
            @Override // java.util.concurrent.FutureTask
            protected void done() {
                try {
                    Platform.runLater(completionRunnable);
                } finally {
                    super.done();
                }
            }
        };
    }

    @Override // com.sun.javafx.runtime.async.AsyncOperation
    public boolean isCancelled() {
        return this.future.isCancelled();
    }

    @Override // com.sun.javafx.runtime.async.AsyncOperation
    public boolean isDone() {
        return this.future.isDone();
    }

    @Override // com.sun.javafx.runtime.async.AsyncOperation
    public void cancel() {
        this.future.cancel(true);
    }

    @Override // com.sun.javafx.runtime.async.AsyncOperation
    public void start() {
        BackgroundExecutor.getExecutor().execute(this.future);
    }

    protected void notifyProgress() {
        int last = this.lastProgress;
        int max = this.progressMax;
        Platform.runLater(() -> {
            this.listener.onProgress(last, max);
        });
    }

    protected void addProgress(int amount) {
        this.bytesRead += amount;
        if (this.bytesRead > this.nextProgress) {
            this.lastProgress = this.bytesRead;
            notifyProgress();
            this.nextProgress = ((this.lastProgress / this.progressIncrement) + 1) * this.progressIncrement;
        }
    }

    protected int getProgressMax() {
        return this.progressMax;
    }

    protected void setProgressMax(int progressMax) {
        if (progressMax == 0 || progressMax == -1) {
            this.progressIncrement = this.progressGranularity;
        } else {
            this.progressMax = progressMax;
            this.progressIncrement = progressMax / this.progressGranularity;
            if (this.progressIncrement < 1) {
                this.progressIncrement = 1;
            }
        }
        this.nextProgress = ((this.lastProgress / this.progressIncrement) + 1) * this.progressIncrement;
        notifyProgress();
    }

    protected int getProgressGranularity() {
        return this.progressGranularity;
    }

    protected void setProgressGranularity(int progressGranularity) {
        this.progressGranularity = progressGranularity;
        this.progressIncrement = this.progressMax / progressGranularity;
        this.nextProgress = ((this.lastProgress / this.progressIncrement) + 1) * this.progressIncrement;
        notifyProgress();
    }
}
