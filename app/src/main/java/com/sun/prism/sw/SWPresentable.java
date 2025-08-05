package com.sun.prism.sw;

import com.sun.glass.ui.Pixels;
import com.sun.javafx.geom.Rectangle;
import com.sun.prism.Presentable;
import com.sun.prism.PresentableState;
import com.sun.prism.impl.QueuedPixelSource;
import java.nio.IntBuffer;

/* loaded from: jfxrt.jar:com/sun/prism/sw/SWPresentable.class */
final class SWPresentable extends SWRTTexture implements Presentable {
    private final PresentableState pState;
    private Pixels pixels;
    private QueuedPixelSource pixelSource;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !SWPresentable.class.desiredAssertionStatus();
    }

    public SWPresentable(PresentableState pState, SWResourceFactory factory) {
        super(factory, pState.getRenderWidth(), pState.getRenderHeight());
        this.pixelSource = new QueuedPixelSource(false);
        this.pState = pState;
    }

    @Override // com.sun.prism.Presentable
    public boolean lockResources(PresentableState pState) {
        return (getPhysicalWidth() == pState.getRenderWidth() && getPhysicalHeight() == pState.getRenderHeight()) ? false : true;
    }

    @Override // com.sun.prism.Presentable
    public boolean prepare(Rectangle dirtyregion) {
        if (!this.pState.isViewClosed()) {
            int w2 = getPhysicalWidth();
            int h2 = getPhysicalHeight();
            this.pixels = this.pixelSource.getUnusedPixels(w2, h2, 1.0f);
            IntBuffer pixBuf = (IntBuffer) this.pixels.getPixels();
            IntBuffer buf = getSurface().getDataIntBuffer();
            if (!$assertionsDisabled && !buf.hasArray()) {
                throw new AssertionError();
            }
            System.arraycopy(buf.array(), 0, pixBuf.array(), 0, w2 * h2);
            return true;
        }
        return false;
    }

    @Override // com.sun.prism.Presentable
    public boolean present() {
        this.pixelSource.enqueuePixels(this.pixels);
        this.pState.uploadPixels(this.pixelSource);
        return true;
    }

    @Override // com.sun.prism.Presentable
    public float getPixelScaleFactor() {
        return this.pState.getRenderScale();
    }

    @Override // com.sun.prism.sw.SWTexture, com.sun.prism.Texture
    public int getContentWidth() {
        return this.pState.getOutputWidth();
    }

    @Override // com.sun.prism.sw.SWTexture, com.sun.prism.Texture
    public int getContentHeight() {
        return this.pState.getOutputHeight();
    }

    @Override // com.sun.prism.sw.SWRTTexture, com.sun.prism.RenderTarget
    public boolean isMSAA() {
        return super.isMSAA();
    }
}
