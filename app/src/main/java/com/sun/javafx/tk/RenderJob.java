package com.sun.javafx.tk;

import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

/* loaded from: jfxrt.jar:com/sun/javafx/tk/RenderJob.class */
public class RenderJob extends FutureTask {
    private CompletionListener listener;
    private Object futureReturn;

    public RenderJob(Runnable pen) {
        super(pen, null);
    }

    public RenderJob(Runnable pen, CompletionListener cl) {
        super(pen, null);
        setCompletionListener(cl);
    }

    public CompletionListener getCompletionListener() {
        return this.listener;
    }

    public void setCompletionListener(CompletionListener cl) {
        this.listener = cl;
    }

    @Override // java.util.concurrent.FutureTask, java.util.concurrent.RunnableFuture, java.lang.Runnable
    public void run() {
        if (super.runAndReset()) {
            if (this.listener != null) {
                try {
                    this.listener.done(this);
                    return;
                } catch (Throwable th) {
                    th.printStackTrace();
                    return;
                }
            }
            return;
        }
        try {
            Object value = super.get();
            System.err.println("RenderJob.run: failed no exception: " + value);
        } catch (CancellationException e2) {
            System.err.println("RenderJob.run: task cancelled");
        } catch (ExecutionException ee) {
            System.err.println("RenderJob.run: internal exception");
            ee.getCause().printStackTrace();
        } catch (Throwable th2) {
            th2.printStackTrace();
        }
    }

    @Override // java.util.concurrent.FutureTask, java.util.concurrent.Future
    public Object get() {
        return this.futureReturn;
    }

    public void setFutureReturn(Object o2) {
        this.futureReturn = o2;
    }
}
