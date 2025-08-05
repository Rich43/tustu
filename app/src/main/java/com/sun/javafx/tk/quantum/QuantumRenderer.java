package com.sun.javafx.tk.quantum;

import com.sun.glass.ui.Application;
import com.sun.glass.ui.Screen;
import com.sun.glass.ui.View;
import com.sun.javafx.tk.CompletionListener;
import com.sun.javafx.tk.RenderJob;
import com.sun.prism.GraphicsPipeline;
import com.sun.prism.GraphicsResource;
import com.sun.prism.Presentable;
import com.sun.prism.ResourceFactory;
import com.sun.prism.impl.PrismSettings;
import com.sun.scenario.effect.impl.Renderer;
import com.sun.scenario.effect.impl.prism.PrFilterContext;
import java.security.AccessController;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RunnableFuture;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

/* loaded from: jfxrt.jar:com/sun/javafx/tk/quantum/QuantumRenderer.class */
final class QuantumRenderer extends ThreadPoolExecutor {
    private static boolean usePurgatory;
    private static final AtomicReference<QuantumRenderer> instanceReference;
    private Thread _renderer;
    private Throwable _initThrowable;
    private CountDownLatch initLatch;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !QuantumRenderer.class.desiredAssertionStatus();
        usePurgatory = ((Boolean) AccessController.doPrivileged(() -> {
            return Boolean.valueOf(Boolean.getBoolean("decora.purgatory"));
        })).booleanValue();
        instanceReference = new AtomicReference<>(null);
    }

    private QuantumRenderer() {
        super(1, 1, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue());
        this._initThrowable = null;
        this.initLatch = new CountDownLatch(1);
        setThreadFactory(new QuantumThreadFactory());
    }

    protected Throwable initThrowable() {
        return this._initThrowable;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setInitThrowable(Throwable th) {
        this._initThrowable = th;
    }

    /* loaded from: jfxrt.jar:com/sun/javafx/tk/quantum/QuantumRenderer$PipelineRunnable.class */
    private class PipelineRunnable implements Runnable {
        private Runnable work;

        public PipelineRunnable(Runnable runner) {
            this.work = runner;
        }

        public void init() {
            try {
                if (GraphicsPipeline.createPipeline() == null) {
                    System.err.println("Error initializing QuantumRenderer: no suitable pipeline found");
                    throw new RuntimeException("Error initializing QuantumRenderer: no suitable pipeline found");
                }
                Map device = GraphicsPipeline.getPipeline().getDeviceDetails();
                if (device == null) {
                    device = new HashMap();
                }
                device.put(View.Capability.kHiDPIAwareKey, Boolean.valueOf(PrismSettings.allowHiDPIScaling));
                Map map = Application.getDeviceDetails();
                if (map != null) {
                    device.putAll(map);
                }
                Application.setDeviceDetails(device);
            } catch (Throwable th) {
                QuantumRenderer.this.setInitThrowable(th);
            } finally {
                QuantumRenderer.this.initLatch.countDown();
            }
        }

        public void cleanup() {
            GraphicsPipeline pipeline = GraphicsPipeline.getPipeline();
            if (pipeline != null) {
                pipeline.dispose();
            }
        }

        @Override // java.lang.Runnable
        public void run() {
            try {
                init();
                this.work.run();
            } finally {
                cleanup();
            }
        }
    }

    /* loaded from: jfxrt.jar:com/sun/javafx/tk/quantum/QuantumRenderer$QuantumThreadFactory.class */
    private class QuantumThreadFactory implements ThreadFactory {
        final AtomicInteger threadNumber;
        static final /* synthetic */ boolean $assertionsDisabled;

        private QuantumThreadFactory() {
            this.threadNumber = new AtomicInteger(0);
        }

        static {
            $assertionsDisabled = !QuantumRenderer.class.desiredAssertionStatus();
        }

        @Override // java.util.concurrent.ThreadFactory
        public Thread newThread(Runnable r2) {
            PipelineRunnable pipeline = QuantumRenderer.this.new PipelineRunnable(r2);
            QuantumRenderer.this._renderer = (Thread) AccessController.doPrivileged(() -> {
                Thread th = new Thread(pipeline);
                th.setName("QuantumRenderer-" + this.threadNumber.getAndIncrement());
                th.setDaemon(true);
                th.setUncaughtExceptionHandler((t2, thr) -> {
                    System.err.println(t2.getName() + " uncaught: " + thr.getClass().getName());
                    thr.printStackTrace();
                });
                return th;
            });
            if ($assertionsDisabled || this.threadNumber.get() == 1) {
                return QuantumRenderer.this._renderer;
            }
            throw new AssertionError();
        }
    }

    protected void createResourceFactory() {
        CountDownLatch createLatch = new CountDownLatch(1);
        CompletionListener createDone = job -> {
            createLatch.countDown();
        };
        Runnable factoryCreator = () -> {
            ResourceFactory factory = GraphicsPipeline.getDefaultResourceFactory();
            if (!$assertionsDisabled && factory == null) {
                throw new AssertionError();
            }
        };
        RenderJob job2 = new RenderJob(factoryCreator, createDone);
        submit(job2);
        try {
            createLatch.await();
        } catch (Throwable th) {
            th.printStackTrace(System.err);
        }
    }

    protected void disposePresentable(Presentable presentable) {
        if (!$assertionsDisabled && Thread.currentThread().equals(this._renderer)) {
            throw new AssertionError();
        }
        if (presentable instanceof GraphicsResource) {
            GraphicsResource resource = (GraphicsResource) presentable;
            Runnable presentableDisposer = () -> {
                resource.dispose();
            };
            RenderJob job = new RenderJob(presentableDisposer, null);
            submit(job);
        }
    }

    protected void stopRenderer() {
        AccessController.doPrivileged(() -> {
            shutdown();
            return null;
        });
        if (PrismSettings.verbose) {
            System.out.println("QuantumRenderer: shutdown");
        }
        if (!$assertionsDisabled && !isShutdown()) {
            throw new AssertionError();
        }
        instanceReference.set(null);
    }

    @Override // java.util.concurrent.AbstractExecutorService
    protected <T> RunnableFuture<T> newTaskFor(Runnable runnable, T value) {
        return (RenderJob) runnable;
    }

    protected Future submitRenderJob(RenderJob r2) {
        return submit(r2);
    }

    @Override // java.util.concurrent.ThreadPoolExecutor
    public void afterExecute(Runnable r2, Throwable t2) {
        super.afterExecute(r2, t2);
        if (usePurgatory) {
            Screen screen = Screen.getMainScreen();
            Renderer renderer = Renderer.getRenderer(PrFilterContext.getInstance(screen));
            renderer.releasePurgatory();
        }
    }

    void checkRendererIdle() {
        if (PrismSettings.threadCheck) {
            PaintCollector collector = PaintCollector.getInstance();
            boolean busy = ViewPainter.renderLock.isLocked() && !ViewPainter.renderLock.isHeldByCurrentThread();
            if (busy) {
                System.err.println("ERROR: PrismPen / FX threads co-running: DIRTY: " + collector.hasDirty());
                for (StackTraceElement s2 : QuantumToolkit.getFxUserThread().getStackTrace()) {
                    System.err.println("FX: " + ((Object) s2));
                }
                for (StackTraceElement q2 : this._renderer.getStackTrace()) {
                    System.err.println("QR: " + ((Object) q2));
                }
            }
        }
    }

    public static synchronized QuantumRenderer getInstance() {
        if (instanceReference.get() == null) {
            synchronized (QuantumRenderer.class) {
                QuantumRenderer newTk = null;
                try {
                    newTk = new QuantumRenderer();
                    newTk.prestartCoreThread();
                    newTk.initLatch.await();
                } catch (Throwable t2) {
                    if (newTk != null) {
                        newTk.setInitThrowable(t2);
                    }
                    if (PrismSettings.verbose) {
                        t2.printStackTrace();
                    }
                }
                if (newTk != null && newTk.initThrowable() != null) {
                    if (PrismSettings.noFallback) {
                        System.err.println("Cannot initialize a graphics pipeline, and Prism fallback is disabled");
                        throw new InternalError("Could not initialize prism toolkit, and the fallback is disabled.");
                    }
                    throw new RuntimeException(newTk.initThrowable());
                }
                instanceReference.set(newTk);
            }
        }
        return instanceReference.get();
    }
}
