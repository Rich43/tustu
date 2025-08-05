package com.sun.prism.d3d;

import com.sun.glass.ui.Screen;
import com.sun.javafx.geom.Rectangle;
import com.sun.prism.CompositeMode;
import com.sun.prism.Graphics;
import com.sun.prism.Presentable;
import com.sun.prism.PresentableState;
import com.sun.prism.RTTexture;
import com.sun.prism.d3d.D3DResource;

/* loaded from: jfxrt.jar:com/sun/prism/d3d/D3DSwapChain.class */
class D3DSwapChain extends D3DResource implements D3DRenderTarget, Presentable, D3DContextSource {
    private final D3DRTTexture texBackBuffer;
    private final float pixelScaleFactor;

    private static native int nPresent(long j2, long j3);

    D3DSwapChain(D3DContext context, long pResource, D3DRTTexture rtt, float pixelScale) {
        super(new D3DResource.D3DRecord(context, pResource));
        this.texBackBuffer = rtt;
        this.pixelScaleFactor = pixelScale;
    }

    @Override // com.sun.prism.d3d.D3DResource, com.sun.prism.impl.BaseGraphicsResource, com.sun.prism.GraphicsResource
    public void dispose() {
        this.texBackBuffer.dispose();
        super.dispose();
    }

    @Override // com.sun.prism.Presentable
    public boolean prepare(Rectangle dirtyregion) {
        D3DContext context = getContext();
        context.flushVertexBuffer();
        D3DGraphics g2 = (D3DGraphics) D3DGraphics.create(this, context);
        if (g2 == null) {
            return false;
        }
        int sw = this.texBackBuffer.getContentWidth();
        int sh = this.texBackBuffer.getContentHeight();
        int dw = getContentWidth();
        int dh = getContentHeight();
        if (isMSAA()) {
            context.flushVertexBuffer();
            g2.blit(this.texBackBuffer, null, 0, 0, sw, sh, 0, 0, dw, dh);
        } else {
            g2.setCompositeMode(CompositeMode.SRC);
            g2.drawTexture(this.texBackBuffer, 0.0f, 0.0f, dw, dh, 0.0f, 0.0f, sw, sh);
        }
        context.flushVertexBuffer();
        this.texBackBuffer.unlock();
        return true;
    }

    @Override // com.sun.prism.Presentable
    public boolean present() {
        D3DContext context = getContext();
        if (context.isDisposed()) {
            return false;
        }
        int res = nPresent(context.getContextHandle(), this.d3dResRecord.getResource());
        return context.validatePresent(res);
    }

    @Override // com.sun.prism.d3d.D3DRenderTarget
    public long getResourceHandle() {
        return this.d3dResRecord.getResource();
    }

    @Override // com.sun.prism.Surface
    public int getPhysicalWidth() {
        return D3DResourceFactory.nGetTextureWidth(this.d3dResRecord.getResource());
    }

    @Override // com.sun.prism.Surface
    public int getPhysicalHeight() {
        return D3DResourceFactory.nGetTextureHeight(this.d3dResRecord.getResource());
    }

    @Override // com.sun.prism.Surface
    public int getContentWidth() {
        return getPhysicalWidth();
    }

    @Override // com.sun.prism.Surface
    public int getContentHeight() {
        return getPhysicalHeight();
    }

    @Override // com.sun.prism.Surface
    public int getContentX() {
        return 0;
    }

    @Override // com.sun.prism.Surface
    public int getContentY() {
        return 0;
    }

    @Override // com.sun.prism.d3d.D3DContextSource
    public D3DContext getContext() {
        return this.d3dResRecord.getContext();
    }

    @Override // com.sun.prism.Presentable
    public boolean lockResources(PresentableState pState) {
        if (pState.getRenderWidth() != this.texBackBuffer.getContentWidth() || pState.getRenderHeight() != this.texBackBuffer.getContentHeight() || pState.getRenderScale() != this.pixelScaleFactor) {
            return true;
        }
        this.texBackBuffer.lock();
        return this.texBackBuffer.isSurfaceLost();
    }

    @Override // com.sun.prism.RenderTarget
    public Graphics createGraphics() {
        Graphics g2 = D3DGraphics.create(this.texBackBuffer, getContext());
        g2.scale(this.pixelScaleFactor, this.pixelScaleFactor);
        return g2;
    }

    public RTTexture getRTTBackBuffer() {
        return this.texBackBuffer;
    }

    @Override // com.sun.prism.RenderTarget
    public Screen getAssociatedScreen() {
        return getContext().getAssociatedScreen();
    }

    @Override // com.sun.prism.Presentable
    public float getPixelScaleFactor() {
        return this.pixelScaleFactor;
    }

    @Override // com.sun.prism.RenderTarget
    public boolean isOpaque() {
        return this.texBackBuffer.isOpaque();
    }

    @Override // com.sun.prism.RenderTarget
    public void setOpaque(boolean opaque) {
        this.texBackBuffer.setOpaque(opaque);
    }

    @Override // com.sun.prism.RenderTarget
    public boolean isMSAA() {
        if (this.texBackBuffer != null) {
            return this.texBackBuffer.isMSAA();
        }
        return false;
    }
}
