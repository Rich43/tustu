package com.sun.javafx.webkit.prism;

import com.sun.javafx.application.PlatformImpl;
import com.sun.javafx.tk.RenderJob;
import com.sun.javafx.tk.Toolkit;
import com.sun.webkit.Invoker;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.logging.Logger;

/* loaded from: jfxrt.jar:com/sun/javafx/webkit/prism/PrismInvoker.class */
public final class PrismInvoker extends Invoker {
    private static final Logger log = Logger.getLogger(PrismInvoker.class.getName());

    @Override // com.sun.webkit.Invoker
    protected boolean lock(ReentrantLock lock) {
        return false;
    }

    @Override // com.sun.webkit.Invoker
    protected boolean unlock(ReentrantLock lock) {
        return false;
    }

    @Override // com.sun.webkit.Invoker
    protected boolean isEventThread() {
        return isEventThreadPrivate();
    }

    private static boolean isEventThreadPrivate() {
        return Toolkit.getToolkit().isFxUserThread();
    }

    @Override // com.sun.webkit.Invoker
    public void checkEventThread() {
        Toolkit.getToolkit().checkFxUserThread();
    }

    @Override // com.sun.webkit.Invoker
    public void invokeOnEventThread(Runnable r2) {
        if (isEventThread()) {
            r2.run();
        } else {
            PlatformImpl.runLater(r2);
        }
    }

    @Override // com.sun.webkit.Invoker
    public void postOnEventThread(Runnable r2) {
        PlatformImpl.runLater(r2);
    }

    static void invokeOnRenderThread(Runnable r2) {
        Toolkit.getToolkit().addRenderJob(new RenderJob(r2));
    }

    static void runOnRenderThread(Runnable r2) {
        if (Thread.currentThread().getName().startsWith("QuantumRenderer")) {
            r2.run();
            return;
        }
        FutureTask<Void> f2 = new FutureTask<>(r2, null);
        Toolkit.getToolkit().addRenderJob(new RenderJob(f2));
        try {
            f2.get();
        } catch (InterruptedException | ExecutionException ex) {
            log.log(Level.SEVERE, "RenderJob error", (Throwable) ex);
        }
    }
}
