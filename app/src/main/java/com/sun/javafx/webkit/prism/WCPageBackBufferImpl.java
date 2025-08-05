package com.sun.javafx.webkit.prism;

import com.sun.prism.Graphics;
import com.sun.prism.GraphicsPipeline;
import com.sun.prism.RTTexture;
import com.sun.prism.ResourceFactory;
import com.sun.prism.ResourceFactoryListener;
import com.sun.prism.Texture;
import com.sun.webkit.graphics.WCCamera;
import com.sun.webkit.graphics.WCGraphicsContext;
import com.sun.webkit.graphics.WCGraphicsManager;
import com.sun.webkit.graphics.WCPageBackBuffer;
import java.lang.ref.WeakReference;
import java.util.logging.Logger;

/* loaded from: jfxrt.jar:com/sun/javafx/webkit/prism/WCPageBackBufferImpl.class */
final class WCPageBackBufferImpl extends WCPageBackBuffer implements ResourceFactoryListener {
    private RTTexture texture;
    private WeakReference<ResourceFactory> registeredWithFactory = null;
    private boolean firstValidate = true;
    private float pixelScale;
    private static final Logger log = Logger.getLogger(WCPageBackBufferImpl.class.getName());

    WCPageBackBufferImpl(float pixelScale) {
        this.pixelScale = pixelScale;
    }

    private static RTTexture createTexture(int w2, int h2) {
        return GraphicsPipeline.getDefaultResourceFactory().createRTTexture(w2, h2, Texture.WrapMode.CLAMP_NOT_NEEDED);
    }

    @Override // com.sun.webkit.graphics.WCPageBackBuffer
    public WCGraphicsContext createGraphics() {
        Graphics g2 = this.texture.createGraphics();
        g2.setCamera(WCCamera.INSTANCE);
        g2.scale(this.pixelScale, this.pixelScale);
        return WCGraphicsManager.getGraphicsManager().createGraphicsContext(g2);
    }

    @Override // com.sun.webkit.graphics.WCPageBackBuffer
    public void disposeGraphics(WCGraphicsContext gc) {
        gc.dispose();
    }

    @Override // com.sun.webkit.graphics.WCPageBackBuffer
    public void flush(WCGraphicsContext gc, int x2, int y2, int w2, int h2) {
        int x22 = x2 + w2;
        int y22 = y2 + h2;
        ((Graphics) gc.getPlatformGraphics()).drawTexture(this.texture, x2, y2, x22, y22, x2 * this.pixelScale, y2 * this.pixelScale, x22 * this.pixelScale, y22 * this.pixelScale);
        this.texture.unlock();
    }

    @Override // com.sun.webkit.graphics.WCPageBackBuffer
    protected void copyArea(int x2, int y2, int w2, int h2, int dx, int dy) {
        int x3 = (int) (x2 * this.pixelScale);
        int y3 = (int) (y2 * this.pixelScale);
        int w3 = (int) Math.ceil(w2 * this.pixelScale);
        int h3 = (int) Math.ceil(h2 * this.pixelScale);
        int dx2 = (int) (dx * this.pixelScale);
        int dy2 = (int) (dy * this.pixelScale);
        RTTexture aux = createTexture(w3, h3);
        aux.createGraphics().drawTexture(this.texture, 0.0f, 0.0f, w3, h3, x3, y3, x3 + w3, y3 + h3);
        this.texture.createGraphics().drawTexture(aux, x3 + dx2, y3 + dy2, x3 + w3 + dx2, y3 + h3 + dy2, 0.0f, 0.0f, w3, h3);
        aux.dispose();
    }

    @Override // com.sun.webkit.graphics.WCPageBackBuffer
    public boolean validate(int width, int height) {
        ResourceFactory factory = GraphicsPipeline.getDefaultResourceFactory();
        if (factory == null || factory.isDisposed()) {
            log.fine("WCPageBackBufferImpl::validate : device disposed or not ready");
            return false;
        }
        int width2 = (int) Math.ceil(width * this.pixelScale);
        int height2 = (int) Math.ceil(height * this.pixelScale);
        if (this.texture != null) {
            this.texture.lock();
            if (this.texture.isSurfaceLost()) {
                this.texture.dispose();
                this.texture = null;
            }
        }
        if (this.texture == null) {
            this.texture = createTexture(width2, height2);
            this.texture.contentsUseful();
            if (this.registeredWithFactory == null || this.registeredWithFactory.get() != factory) {
                factory.addFactoryListener(this);
                this.registeredWithFactory = new WeakReference<>(factory);
            }
            if (this.firstValidate) {
                this.firstValidate = false;
                return true;
            }
            this.texture.unlock();
            return false;
        }
        int tw = this.texture.getContentWidth();
        int th = this.texture.getContentHeight();
        if (tw != width2 || th != height2) {
            RTTexture newTexture = createTexture(width2, height2);
            newTexture.contentsUseful();
            newTexture.createGraphics().drawTexture(this.texture, 0.0f, 0.0f, Math.min(width2, tw), Math.min(height2, th));
            this.texture.dispose();
            this.texture = newTexture;
            return true;
        }
        return true;
    }

    @Override // com.sun.prism.ResourceFactoryListener
    public void factoryReset() {
        if (this.texture != null) {
            this.texture.dispose();
            this.texture = null;
        }
    }

    @Override // com.sun.prism.ResourceFactoryListener
    public void factoryReleased() {
        log.fine("WCPageBackBufferImpl: resource factory released");
        if (this.texture != null) {
            this.texture.dispose();
            this.texture = null;
        }
    }
}
