package com.sun.scenario.effect.impl.prism.ps;

import com.sun.javafx.geom.Rectangle;
import com.sun.prism.paint.Color;
import com.sun.prism.ps.Shader;
import com.sun.prism.ps.ShaderGraphics;
import com.sun.scenario.effect.FilterContext;
import com.sun.scenario.effect.ImageData;
import com.sun.scenario.effect.impl.Renderer;

/* loaded from: jfxrt.jar:com/sun/scenario/effect/impl/prism/ps/PPSZeroSamplerPeer.class */
public abstract class PPSZeroSamplerPeer extends PPSEffectPeer {
    private Shader shader;

    protected PPSZeroSamplerPeer(FilterContext fctx, Renderer r2, String shaderName) {
        super(fctx, r2, shaderName);
    }

    @Override // com.sun.scenario.effect.impl.prism.ps.PPSEffectPeer, com.sun.scenario.effect.impl.EffectPeer
    public void dispose() {
        if (this.shader != null) {
            this.shader.dispose();
        }
    }

    @Override // com.sun.scenario.effect.impl.prism.ps.PPSEffectPeer
    ImageData filterImpl(ImageData... inputs) {
        Rectangle dstBounds = getDestBounds();
        int dstw = dstBounds.width;
        int dsth = dstBounds.height;
        PPSRenderer renderer = getRenderer();
        PPSDrawable dst = renderer.getCompatibleImage(dstw, dsth);
        if (dst == null) {
            renderer.markLost();
            return new ImageData(getFilterContext(), dst, dstBounds);
        }
        setDestNativeBounds(dst.getPhysicalWidth(), dst.getPhysicalHeight());
        ShaderGraphics g2 = dst.createGraphics();
        if (g2 == null) {
            renderer.markLost();
            return new ImageData(getFilterContext(), dst, dstBounds);
        }
        if (this.shader == null) {
            this.shader = createShader();
        }
        if (this.shader == null || !this.shader.isValid()) {
            renderer.markLost();
            return new ImageData(getFilterContext(), dst, dstBounds);
        }
        g2.setExternalShader(this.shader);
        updateShader(this.shader);
        float dx2 = dstw;
        float dy2 = dsth;
        g2.setPaint(Color.WHITE);
        g2.fillQuad(0.0f, 0.0f, dx2, dy2);
        g2.setExternalShader(null);
        return new ImageData(getFilterContext(), dst, dstBounds);
    }
}
