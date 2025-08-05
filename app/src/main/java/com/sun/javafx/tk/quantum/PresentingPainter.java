package com.sun.javafx.tk.quantum;

import com.sun.javafx.logging.PulseLogger;
import com.sun.prism.Graphics;
import com.sun.prism.GraphicsPipeline;
import com.sun.prism.impl.Disposer;

/* loaded from: jfxrt.jar:com/sun/javafx/tk/quantum/PresentingPainter.class */
final class PresentingPainter extends ViewPainter {
    PresentingPainter(ViewScene view) {
        super(view);
    }

    @Override // java.lang.Runnable
    public void run() {
        renderLock.lock();
        try {
            try {
                boolean valid = validateStageGraphics();
                if (!valid) {
                    if (QuantumToolkit.verbose) {
                        System.err.println("PresentingPainter: validateStageGraphics failed");
                    }
                    paintImpl(null);
                    Disposer.cleanUp();
                    if (0 != 0) {
                        this.sceneState.unlock();
                    }
                    ViewScene viewScene = (ViewScene) this.sceneState.getScene();
                    viewScene.setPainting(false);
                    if (this.factory != null) {
                        this.factory.getTextureResourcePool().freeDisposalRequestedAndCheckResources(false);
                    }
                    renderLock.unlock();
                    return;
                }
                this.sceneState.lock();
                if (this.factory == null) {
                    this.factory = GraphicsPipeline.getDefaultResourceFactory();
                }
                if (this.factory == null || !this.factory.isDeviceReady()) {
                    this.sceneState.getScene().entireSceneNeedsRepaint();
                    this.factory = null;
                    Disposer.cleanUp();
                    if (1 != 0) {
                        this.sceneState.unlock();
                    }
                    ViewScene viewScene2 = (ViewScene) this.sceneState.getScene();
                    viewScene2.setPainting(false);
                    if (this.factory != null) {
                        this.factory.getTextureResourcePool().freeDisposalRequestedAndCheckResources(false);
                    }
                    renderLock.unlock();
                    return;
                }
                if (this.presentable != null && this.presentable.lockResources(this.sceneState)) {
                    disposePresentable();
                }
                if (this.presentable == null) {
                    this.presentable = this.factory.createPresentable(this.sceneState);
                    this.penWidth = this.viewWidth;
                    this.penHeight = this.viewHeight;
                    this.freshBackBuffer = true;
                }
                if (this.presentable != null) {
                    Graphics g2 = this.presentable.createGraphics();
                    ViewScene vs = (ViewScene) this.sceneState.getScene();
                    if (g2 != null) {
                        paintImpl(g2);
                        this.freshBackBuffer = false;
                    }
                    if (PulseLogger.PULSE_LOGGING_ENABLED) {
                        PulseLogger.newPhase("Presenting");
                    }
                    if (!this.presentable.prepare(null)) {
                        disposePresentable();
                        this.sceneState.getScene().entireSceneNeedsRepaint();
                        Disposer.cleanUp();
                        if (1 != 0) {
                            this.sceneState.unlock();
                        }
                        ViewScene viewScene3 = (ViewScene) this.sceneState.getScene();
                        viewScene3.setPainting(false);
                        if (this.factory != null) {
                            this.factory.getTextureResourcePool().freeDisposalRequestedAndCheckResources(false);
                        }
                        renderLock.unlock();
                        return;
                    }
                    if (vs.getDoPresent() && !this.presentable.present()) {
                        disposePresentable();
                        this.sceneState.getScene().entireSceneNeedsRepaint();
                    }
                }
                Disposer.cleanUp();
                if (1 != 0) {
                    this.sceneState.unlock();
                }
                ViewScene viewScene4 = (ViewScene) this.sceneState.getScene();
                viewScene4.setPainting(false);
                if (this.factory != null) {
                    this.factory.getTextureResourcePool().freeDisposalRequestedAndCheckResources(false);
                }
                renderLock.unlock();
            } catch (Throwable th) {
                th.printStackTrace(System.err);
                Disposer.cleanUp();
                if (0 != 0) {
                    this.sceneState.unlock();
                }
                ViewScene viewScene5 = (ViewScene) this.sceneState.getScene();
                viewScene5.setPainting(false);
                if (this.factory != null) {
                    this.factory.getTextureResourcePool().freeDisposalRequestedAndCheckResources(true);
                }
                renderLock.unlock();
            }
        } catch (Throwable th2) {
            Disposer.cleanUp();
            if (0 != 0) {
                this.sceneState.unlock();
            }
            ViewScene viewScene6 = (ViewScene) this.sceneState.getScene();
            viewScene6.setPainting(false);
            if (this.factory != null) {
                this.factory.getTextureResourcePool().freeDisposalRequestedAndCheckResources(false);
            }
            renderLock.unlock();
            throw th2;
        }
    }
}
