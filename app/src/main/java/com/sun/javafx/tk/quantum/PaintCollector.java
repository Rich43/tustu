package com.sun.javafx.tk.quantum;

import com.sun.glass.ui.Application;
import com.sun.glass.ui.Window;
import com.sun.javafx.PlatformUtil;
import com.sun.javafx.logging.PulseLogger;
import com.sun.javafx.tk.CompletionListener;
import com.sun.javafx.tk.RenderJob;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/* loaded from: jfxrt.jar:com/sun/javafx/tk/quantum/PaintCollector.class */
final class PaintCollector implements CompletionListener {
    private static volatile PaintCollector collector;
    private static final Comparator<GlassScene> DIRTY_SCENE_SORTER;
    private final List<GlassScene> dirtyScenes = new ArrayList();
    private volatile CountDownLatch allWorkCompletedLatch = new CountDownLatch(0);
    private volatile boolean hasDirty;
    private final QuantumToolkit toolkit;
    private volatile boolean needsHint;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !PaintCollector.class.desiredAssertionStatus();
        DIRTY_SCENE_SORTER = (o1, o2) -> {
            int i1 = o1.isSynchronous() ? 1 : 0;
            int i2 = o2.isSynchronous() ? 1 : 0;
            return i1 - i2;
        };
    }

    static PaintCollector createInstance(QuantumToolkit toolkit) {
        PaintCollector paintCollector = new PaintCollector(toolkit);
        collector = paintCollector;
        return paintCollector;
    }

    static PaintCollector getInstance() {
        return collector;
    }

    private PaintCollector(QuantumToolkit qt) {
        this.toolkit = qt;
    }

    void waitForRenderingToComplete() {
        while (true) {
            try {
                this.allWorkCompletedLatch.await();
                return;
            } catch (InterruptedException e2) {
            }
        }
    }

    final boolean hasDirty() {
        return this.hasDirty;
    }

    final void addDirtyScene(GlassScene scene) {
        if (!$assertionsDisabled && Thread.currentThread() != QuantumToolkit.getFxUserThread()) {
            throw new AssertionError();
        }
        if (!$assertionsDisabled && scene == null) {
            throw new AssertionError();
        }
        if (QuantumToolkit.verbose) {
            System.err.println("PC.addDirtyScene: " + System.nanoTime() + ((Object) scene));
        }
        if (!this.dirtyScenes.contains(scene)) {
            this.dirtyScenes.add(scene);
            this.hasDirty = true;
        }
    }

    final void removeDirtyScene(GlassScene scene) {
        if (!$assertionsDisabled && Thread.currentThread() != QuantumToolkit.getFxUserThread()) {
            throw new AssertionError();
        }
        if (!$assertionsDisabled && scene == null) {
            throw new AssertionError();
        }
        if (QuantumToolkit.verbose) {
            System.err.println("PC.removeDirtyScene: " + ((Object) scene));
        }
        this.dirtyScenes.remove(scene);
        this.hasDirty = !this.dirtyScenes.isEmpty();
    }

    final CompletionListener getRendered() {
        return this;
    }

    @Override // com.sun.javafx.tk.CompletionListener
    public void done(RenderJob job) {
        if (!$assertionsDisabled && Thread.currentThread() == QuantumToolkit.getFxUserThread()) {
            throw new AssertionError();
        }
        if (!(job instanceof PaintRenderJob)) {
            throw new IllegalArgumentException("PaintCollector: invalid RenderJob");
        }
        PaintRenderJob paintjob = (PaintRenderJob) job;
        GlassScene scene = paintjob.getScene();
        if (scene == null) {
            throw new IllegalArgumentException("PaintCollector: null scene");
        }
        scene.frameRendered();
        if (this.allWorkCompletedLatch.getCount() == 1) {
            if (this.needsHint && !this.toolkit.hasNativeSystemVsync()) {
                this.toolkit.vsyncHint();
            }
            Application.GetApplication().notifyRenderingFinished();
            if (PulseLogger.PULSE_LOGGING_ENABLED) {
                PulseLogger.renderEnd();
            }
        }
        this.allWorkCompletedLatch.countDown();
    }

    final void liveRepaintRenderJob(ViewScene scene) {
        ViewPainter viewPainter = scene.getPainter();
        QuantumToolkit quantum = (QuantumToolkit) QuantumToolkit.getToolkit();
        quantum.pulse(false);
        CountDownLatch latch = new CountDownLatch(1);
        QuantumToolkit.runWithoutRenderLock(() -> {
            quantum.addRenderJob(new RenderJob(viewPainter, rj -> {
                latch.countDown();
            }));
            try {
                latch.await();
                return null;
            } catch (InterruptedException e2) {
                return null;
            }
        });
    }

    final void renderAll() {
        if (!$assertionsDisabled && Thread.currentThread() != QuantumToolkit.getFxUserThread()) {
            throw new AssertionError();
        }
        if (QuantumToolkit.pulseDebug) {
            System.err.println("PC.renderAll(" + this.dirtyScenes.size() + "): " + System.nanoTime());
        }
        if (!this.hasDirty) {
            return;
        }
        if (!$assertionsDisabled && this.dirtyScenes.isEmpty()) {
            throw new AssertionError();
        }
        Collections.sort(this.dirtyScenes, DIRTY_SCENE_SORTER);
        this.hasDirty = false;
        this.needsHint = false;
        if (PulseLogger.PULSE_LOGGING_ENABLED) {
            PulseLogger.renderStart();
        }
        if (!Application.GetApplication().hasWindowManager()) {
            List<Window> glassWindowList = Window.getWindows();
            this.allWorkCompletedLatch = new CountDownLatch(glassWindowList.size());
            int n2 = glassWindowList.size();
            for (int i2 = 0; i2 < n2; i2++) {
                Window w2 = glassWindowList.get(i2);
                WindowStage ws = WindowStage.findWindowStage(w2);
                if (ws != null) {
                    ViewScene vs = ws.getViewScene();
                    if (this.dirtyScenes.indexOf(vs) != -1 && !this.needsHint) {
                        this.needsHint = vs.isSynchronous();
                    }
                    if (!PlatformUtil.useEGL() || i2 == n2 - 1) {
                        vs.setDoPresent(true);
                    } else {
                        vs.setDoPresent(false);
                    }
                    try {
                        vs.repaint();
                    } catch (Throwable t2) {
                        t2.printStackTrace();
                    }
                }
            }
        } else {
            this.allWorkCompletedLatch = new CountDownLatch(this.dirtyScenes.size());
            for (GlassScene gs : this.dirtyScenes) {
                if (!this.needsHint) {
                    this.needsHint = gs.isSynchronous();
                }
                gs.setDoPresent(true);
                try {
                    gs.repaint();
                } catch (Throwable t3) {
                    t3.printStackTrace();
                }
            }
        }
        this.dirtyScenes.clear();
        if (this.toolkit.shouldWaitForRenderingToComplete()) {
            waitForRenderingToComplete();
        }
    }
}
