package com.sun.scenario.effect.impl.prism.ps;

import com.sun.javafx.geom.Rectangle;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.prism.Texture;
import com.sun.prism.ps.Shader;
import com.sun.prism.ps.ShaderGraphics;
import com.sun.scenario.effect.FilterContext;
import com.sun.scenario.effect.ImageData;
import com.sun.scenario.effect.impl.Renderer;
import com.sun.scenario.effect.impl.prism.PrTexture;
import com.sun.scenario.effect.impl.state.RenderState;

/* loaded from: jfxrt.jar:com/sun/scenario/effect/impl/prism/ps/PPSOneSamplerPeer.class */
public abstract class PPSOneSamplerPeer<T extends RenderState> extends PPSEffectPeer<T> {
    private Shader shader;

    protected PPSOneSamplerPeer(FilterContext fctx, Renderer r2, String shaderName) {
        super(fctx, r2, shaderName);
    }

    @Override // com.sun.scenario.effect.impl.prism.ps.PPSEffectPeer, com.sun.scenario.effect.impl.EffectPeer
    public void dispose() {
        if (this.shader != null) {
            this.shader.dispose();
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.sun.scenario.effect.impl.prism.ps.PPSEffectPeer
    ImageData filterImpl(ImageData... inputs) {
        PrTexture srcTex = (PrTexture) inputs[0].getUntransformedImage();
        Rectangle srcBounds = inputs[0].getUntransformedBounds();
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
        BaseTransform srcTransform = inputs[0].getTransform();
        setInputBounds(0, srcBounds);
        setInputTransform(0, srcTransform);
        setInputNativeBounds(0, srcTex.getNativeBounds());
        float[] srcRect = new float[8];
        int srcCoords = getTextureCoordinates(0, srcRect, srcBounds.f11913x, srcBounds.f11914y, r0.getPhysicalWidth(), r0.getPhysicalHeight(), dstBounds, srcTransform);
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
        Texture ptex = srcTex.getTextureObject();
        if (ptex == null) {
            renderer.markLost();
            return new ImageData(getFilterContext(), dst, dstBounds);
        }
        float txoff = ptex.getContentX() / ptex.getPhysicalWidth();
        float tyoff = ptex.getContentY() / ptex.getPhysicalHeight();
        float tx11 = txoff + srcRect[0];
        float ty11 = tyoff + srcRect[1];
        float tx22 = txoff + srcRect[2];
        float ty22 = tyoff + srcRect[3];
        if (srcCoords < 8) {
            g2.drawTextureRaw(ptex, 0.0f, 0.0f, dx2, dy2, tx11, ty11, tx22, ty22);
        } else {
            float tx21 = txoff + srcRect[4];
            float ty21 = tyoff + srcRect[5];
            float tx12 = txoff + srcRect[6];
            float ty12 = tyoff + srcRect[7];
            g2.drawMappedTextureRaw(ptex, 0.0f, 0.0f, dx2, dy2, tx11, ty11, tx21, ty21, tx12, ty12, tx22, ty22);
        }
        g2.setExternalShader(null);
        return new ImageData(getFilterContext(), dst, dstBounds);
    }
}
