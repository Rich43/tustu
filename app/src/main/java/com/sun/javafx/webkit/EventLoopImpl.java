package com.sun.javafx.webkit;

import com.sun.javafx.tk.Toolkit;
import com.sun.webkit.EventLoop;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import javafx.application.Platform;

/* loaded from: jfxrt.jar:com/sun/javafx/webkit/EventLoopImpl.class */
public final class EventLoopImpl extends EventLoop {
    private static final long DELAY = 20;
    private static final ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();

    @Override // com.sun.webkit.EventLoop
    protected void cycle() {
        Object key = new Object();
        executor.schedule(() -> {
            Platform.runLater(new Runnable() { // from class: com.sun.javafx.webkit.EventLoopImpl.1
                @Override // java.lang.Runnable
                public void run() {
                    Toolkit.getToolkit().exitNestedEventLoop(key, null);
                }
            });
        }, 20L, TimeUnit.MILLISECONDS);
        Toolkit.getToolkit().enterNestedEventLoop(key);
    }
}
