package com.sun.scenario.effect.impl.prism.ps;

import com.sun.javafx.geom.Rectangle;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.prism.Texture;
import com.sun.prism.ps.Shader;
import com.sun.prism.ps.ShaderGraphics;
import com.sun.scenario.effect.FilterContext;
import com.sun.scenario.effect.FloatMap;
import com.sun.scenario.effect.ImageData;
import com.sun.scenario.effect.impl.Renderer;
import com.sun.scenario.effect.impl.prism.PrTexture;

/* loaded from: jfxrt.jar:com/sun/scenario/effect/impl/prism/ps/PPSTwoSamplerPeer.class */
public abstract class PPSTwoSamplerPeer extends PPSEffectPeer {
    private Shader shader;

    protected PPSTwoSamplerPeer(FilterContext fctx, Renderer r2, String shaderName) {
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
        PrTexture src1Tex;
        int src1Coords;
        float t0x21;
        float t0y21;
        float t0x12;
        float t0y12;
        float t1x21;
        float t1y21;
        float t1x12;
        float t1y12;
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
        PrTexture src0Tex = (PrTexture) inputs[0].getUntransformedImage();
        Rectangle src0Bounds = inputs[0].getUntransformedBounds();
        BaseTransform src0Transform = inputs[0].getTransform();
        setInputBounds(0, src0Bounds);
        setInputTransform(0, src0Transform);
        setInputNativeBounds(0, src0Tex.getNativeBounds());
        float[] src1Rect = new float[8];
        if (inputs.length > 1) {
            src1Tex = (PrTexture) inputs[1].getUntransformedImage();
            if (src1Tex == null) {
                renderer.markLost();
                return new ImageData(getFilterContext(), dst, dstBounds);
            }
            Rectangle src1Bounds = inputs[1].getUntransformedBounds();
            BaseTransform src1Transform = inputs[1].getTransform();
            setInputBounds(1, src1Bounds);
            setInputTransform(1, src1Transform);
            setInputNativeBounds(1, src1Tex.getNativeBounds());
            src1Coords = getTextureCoordinates(1, src1Rect, src1Bounds.f11913x, src1Bounds.f11914y, r0.getPhysicalWidth(), r0.getPhysicalHeight(), dstBounds, src1Transform);
        } else {
            FloatMap map = (FloatMap) getSamplerData(1);
            src1Tex = (PrTexture) map.getAccelData(getFilterContext());
            if (src1Tex == null) {
                renderer.markLost();
                return new ImageData(getFilterContext(), dst, dstBounds);
            }
            Rectangle b2 = new Rectangle(map.getWidth(), map.getHeight());
            Rectangle nb = src1Tex.getNativeBounds();
            setInputBounds(1, b2);
            setInputNativeBounds(1, nb);
            src1Rect[1] = 0.0f;
            src1Rect[0] = 0.0f;
            src1Rect[2] = b2.width / nb.width;
            src1Rect[3] = b2.height / nb.height;
            src1Coords = 4;
        }
        float[] src0Rect = new float[8];
        int src0Coords = getTextureCoordinates(0, src0Rect, src0Bounds.f11913x, src0Bounds.f11914y, r0.getPhysicalWidth(), r0.getPhysicalHeight(), dstBounds, src0Transform);
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
        Texture ptex0 = src0Tex.getTextureObject();
        if (ptex0 == null) {
            renderer.markLost();
            return new ImageData(getFilterContext(), dst, dstBounds);
        }
        Texture ptex1 = src1Tex.getTextureObject();
        if (ptex1 == null) {
            renderer.markLost();
            return new ImageData(getFilterContext(), dst, dstBounds);
        }
        float t0xoff = ptex0.getContentX() / ptex0.getPhysicalWidth();
        float t0yoff = ptex0.getContentY() / ptex0.getPhysicalHeight();
        float t0x11 = t0xoff + src0Rect[0];
        float t0y11 = t0yoff + src0Rect[1];
        float t0x22 = t0xoff + src0Rect[2];
        float t0y22 = t0yoff + src0Rect[3];
        float t1xoff = ptex1.getContentX() / ptex1.getPhysicalWidth();
        float t1yoff = ptex1.getContentY() / ptex1.getPhysicalHeight();
        float t1x11 = t1xoff + src1Rect[0];
        float t1y11 = t1yoff + src1Rect[1];
        float t1x22 = t1xoff + src1Rect[2];
        float t1y22 = t1yoff + src1Rect[3];
        if (src0Coords < 8 && src1Coords < 8) {
            g2.drawTextureRaw2(ptex0, ptex1, 0.0f, 0.0f, dx2, dy2, t0x11, t0y11, t0x22, t0y22, t1x11, t1y11, t1x22, t1y22);
        } else {
            if (src0Coords < 8) {
                t0x21 = t0x22;
                t0y21 = t0y11;
                t0x12 = t0x11;
                t0y12 = t0y22;
            } else {
                t0x21 = t0xoff + src0Rect[4];
                t0y21 = t0yoff + src0Rect[5];
                t0x12 = t0xoff + src0Rect[6];
                t0y12 = t0yoff + src0Rect[7];
            }
            if (src1Coords < 8) {
                t1x21 = t1x22;
                t1y21 = t1y11;
                t1x12 = t1x11;
                t1y12 = t1y22;
            } else {
                t1x21 = t1xoff + src1Rect[4];
                t1y21 = t1yoff + src1Rect[5];
                t1x12 = t1xoff + src1Rect[6];
                t1y12 = t1yoff + src1Rect[7];
            }
            g2.drawMappedTextureRaw2(ptex0, ptex1, 0.0f, 0.0f, dx2, dy2, t0x11, t0y11, t0x21, t0y21, t0x12, t0y12, t0x22, t0y22, t1x11, t1y11, t1x21, t1y21, t1x12, t1y12, t1x22, t1y22);
        }
        g2.setExternalShader(null);
        if (inputs.length <= 1) {
            src1Tex.unlock();
        }
        return new ImageData(getFilterContext(), dst, dstBounds);
    }
}
