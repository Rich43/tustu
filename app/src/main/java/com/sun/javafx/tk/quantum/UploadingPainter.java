package com.sun.javafx.tk.quantum;

import com.sun.glass.ui.Pixels;
import com.sun.prism.Graphics;
import com.sun.prism.GraphicsPipeline;
import com.sun.prism.RTTexture;
import com.sun.prism.Texture;
import com.sun.prism.impl.Disposer;
import com.sun.prism.impl.QueuedPixelSource;
import java.nio.IntBuffer;

/* loaded from: jfxrt.jar:com/sun/javafx/tk/quantum/UploadingPainter.class */
final class UploadingPainter extends ViewPainter implements Runnable {
    private RTTexture rttexture;
    private RTTexture resolveRTT;
    private QueuedPixelSource pixelSource;
    private float penScale;

    UploadingPainter(GlassScene view) {
        super(view);
        this.resolveRTT = null;
        this.pixelSource = new QueuedPixelSource(true);
    }

    void disposeRTTexture() {
        if (this.rttexture != null) {
            this.rttexture.dispose();
            this.rttexture = null;
        }
        if (this.resolveRTT != null) {
            this.resolveRTT.dispose();
            this.resolveRTT = null;
        }
    }

    @Override // com.sun.javafx.tk.quantum.ViewPainter
    public float getPixelScaleFactor() {
        return this.sceneState.getRenderScale();
    }

    @Override // java.lang.Runnable
    public void run() {
        renderLock.lock();
        try {
            try {
                if (!validateStageGraphics()) {
                    if (QuantumToolkit.verbose) {
                        System.err.println("UploadingPainter: validateStageGraphics failed");
                    }
                    paintImpl(null);
                    if (this.rttexture != null && this.rttexture.isLocked()) {
                        this.rttexture.unlock();
                    }
                    if (this.resolveRTT != null && this.resolveRTT.isLocked()) {
                        this.resolveRTT.unlock();
                    }
                    Disposer.cleanUp();
                    this.sceneState.getScene().setPainting(false);
                    if (this.factory != null) {
                        this.factory.getTextureResourcePool().freeDisposalRequestedAndCheckResources(false);
                    }
                    renderLock.unlock();
                    return;
                }
                if (this.factory == null) {
                    this.factory = GraphicsPipeline.getDefaultResourceFactory();
                }
                if (this.factory == null || !this.factory.isDeviceReady()) {
                    this.factory = null;
                    if (this.rttexture != null && this.rttexture.isLocked()) {
                        this.rttexture.unlock();
                    }
                    if (this.resolveRTT != null && this.resolveRTT.isLocked()) {
                        this.resolveRTT.unlock();
                    }
                    Disposer.cleanUp();
                    this.sceneState.getScene().setPainting(false);
                    if (this.factory != null) {
                        this.factory.getTextureResourcePool().freeDisposalRequestedAndCheckResources(false);
                    }
                    renderLock.unlock();
                    return;
                }
                float scale = getPixelScaleFactor();
                int bufWidth = this.sceneState.getRenderWidth();
                int bufHeight = this.sceneState.getRenderHeight();
                boolean needsReset = (this.penScale == scale && this.penWidth == this.viewWidth && this.penHeight == this.viewHeight && this.rttexture != null && this.rttexture.getContentWidth() == bufWidth && this.rttexture.getContentHeight() == bufHeight) ? false : true;
                if (!needsReset) {
                    this.rttexture.lock();
                    if (this.rttexture.isSurfaceLost()) {
                        this.rttexture.unlock();
                        this.sceneState.getScene().entireSceneNeedsRepaint();
                        needsReset = true;
                    }
                }
                if (needsReset) {
                    disposeRTTexture();
                    this.rttexture = this.factory.createRTTexture(bufWidth, bufHeight, Texture.WrapMode.CLAMP_NOT_NEEDED, this.sceneState.isMSAA());
                    if (this.rttexture == null) {
                        if (this.rttexture != null && this.rttexture.isLocked()) {
                            this.rttexture.unlock();
                        }
                        if (this.resolveRTT != null && this.resolveRTT.isLocked()) {
                            this.resolveRTT.unlock();
                        }
                        Disposer.cleanUp();
                        this.sceneState.getScene().setPainting(false);
                        if (this.factory != null) {
                            this.factory.getTextureResourcePool().freeDisposalRequestedAndCheckResources(false);
                        }
                        renderLock.unlock();
                        return;
                    }
                    this.penScale = scale;
                    this.penWidth = this.viewWidth;
                    this.penHeight = this.viewHeight;
                    this.freshBackBuffer = true;
                }
                Graphics g2 = this.rttexture.createGraphics();
                if (g2 == null) {
                    disposeRTTexture();
                    this.sceneState.getScene().entireSceneNeedsRepaint();
                    if (this.rttexture != null && this.rttexture.isLocked()) {
                        this.rttexture.unlock();
                    }
                    if (this.resolveRTT != null && this.resolveRTT.isLocked()) {
                        this.resolveRTT.unlock();
                    }
                    Disposer.cleanUp();
                    this.sceneState.getScene().setPainting(false);
                    if (this.factory != null) {
                        this.factory.getTextureResourcePool().freeDisposalRequestedAndCheckResources(false);
                    }
                    renderLock.unlock();
                    return;
                }
                g2.scale(scale, scale);
                paintImpl(g2);
                this.freshBackBuffer = false;
                int outWidth = this.sceneState.getOutputWidth();
                int outHeight = this.sceneState.getOutputHeight();
                float outScale = this.sceneState.getOutputScale();
                RTTexture rtt = (!this.rttexture.isMSAA() && outWidth == bufWidth && outHeight == bufHeight) ? this.rttexture : resolveRenderTarget(g2, outWidth, outHeight);
                Pixels pix = this.pixelSource.getUnusedPixels(outWidth, outHeight, outScale);
                IntBuffer bits = (IntBuffer) pix.getPixels();
                int[] rawbits = rtt.getPixels();
                if (rawbits != null) {
                    bits.put(rawbits, 0, outWidth * outHeight);
                } else if (!rtt.readPixels(bits)) {
                    this.sceneState.getScene().entireSceneNeedsRepaint();
                    disposeRTTexture();
                    pix = null;
                }
                if (this.rttexture != null) {
                    this.rttexture.unlock();
                }
                if (pix != null) {
                    this.pixelSource.enqueuePixels(pix);
                    this.sceneState.uploadPixels(this.pixelSource);
                }
                if (this.rttexture != null && this.rttexture.isLocked()) {
                    this.rttexture.unlock();
                }
                if (this.resolveRTT != null && this.resolveRTT.isLocked()) {
                    this.resolveRTT.unlock();
                }
                Disposer.cleanUp();
                this.sceneState.getScene().setPainting(false);
                if (this.factory != null) {
                    this.factory.getTextureResourcePool().freeDisposalRequestedAndCheckResources(false);
                }
                renderLock.unlock();
            } catch (Throwable th) {
                th.printStackTrace(System.err);
                if (this.rttexture != null && this.rttexture.isLocked()) {
                    this.rttexture.unlock();
                }
                if (this.resolveRTT != null && this.resolveRTT.isLocked()) {
                    this.resolveRTT.unlock();
                }
                Disposer.cleanUp();
                this.sceneState.getScene().setPainting(false);
                if (this.factory != null) {
                    this.factory.getTextureResourcePool().freeDisposalRequestedAndCheckResources(true);
                }
                renderLock.unlock();
            }
        } catch (Throwable th2) {
            if (this.rttexture != null && this.rttexture.isLocked()) {
                this.rttexture.unlock();
            }
            if (this.resolveRTT != null && this.resolveRTT.isLocked()) {
                this.resolveRTT.unlock();
            }
            Disposer.cleanUp();
            this.sceneState.getScene().setPainting(false);
            if (this.factory != null) {
                this.factory.getTextureResourcePool().freeDisposalRequestedAndCheckResources(false);
            }
            renderLock.unlock();
            throw th2;
        }
    }

    private RTTexture resolveRenderTarget(Graphics g2, int width, int height) {
        if (this.resolveRTT != null) {
            this.resolveRTT.lock();
            if (this.resolveRTT.isSurfaceLost() || this.resolveRTT.getContentWidth() != width || this.resolveRTT.getContentHeight() != height) {
                this.resolveRTT.unlock();
                this.resolveRTT.dispose();
                this.resolveRTT = null;
            }
        }
        if (this.resolveRTT == null) {
            this.resolveRTT = g2.getResourceFactory().createRTTexture(width, height, Texture.WrapMode.CLAMP_NOT_NEEDED, false);
        }
        int srcw = this.rttexture.getContentWidth();
        int srch = this.rttexture.getContentHeight();
        g2.blit(this.rttexture, this.resolveRTT, 0, 0, srcw, srch, 0, 0, width, height);
        return this.resolveRTT;
    }
}
