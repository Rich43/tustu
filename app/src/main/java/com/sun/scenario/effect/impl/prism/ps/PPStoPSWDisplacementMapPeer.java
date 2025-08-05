package com.sun.scenario.effect.impl.prism.ps;

import com.sun.javafx.geom.Rectangle;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.prism.RTTexture;
import com.sun.scenario.effect.Effect;
import com.sun.scenario.effect.FilterContext;
import com.sun.scenario.effect.ImageData;
import com.sun.scenario.effect.impl.EffectPeer;
import com.sun.scenario.effect.impl.Renderer;
import com.sun.scenario.effect.impl.prism.PrDrawable;
import com.sun.scenario.effect.impl.prism.PrRenderer;
import com.sun.scenario.effect.impl.prism.PrTexture;
import com.sun.scenario.effect.impl.state.RenderState;

/* loaded from: jfxrt.jar:com/sun/scenario/effect/impl/prism/ps/PPStoPSWDisplacementMapPeer.class */
public class PPStoPSWDisplacementMapPeer extends EffectPeer {
    PrRenderer softwareRenderer;
    EffectPeer softwarePeer;

    public PPStoPSWDisplacementMapPeer(FilterContext fctx, Renderer r2, String shaderName) {
        super(fctx, r2, shaderName);
        this.softwareRenderer = (PrRenderer) Renderer.getRenderer(fctx);
        this.softwarePeer = this.softwareRenderer.getPeerInstance(fctx, "DisplacementMap", 0);
    }

    @Override // com.sun.scenario.effect.impl.EffectPeer
    public ImageData filter(Effect effect, RenderState rstate, BaseTransform transform, Rectangle outputClip, ImageData... inputs) {
        ImageData input = inputs[0];
        PrTexture srcTex = (PrTexture) input.getUntransformedImage();
        RTTexture srcRT = (RTTexture) srcTex.getTextureObject();
        PrDrawable srcDrawable = this.softwareRenderer.createDrawable(srcRT);
        ImageData heapinput = new ImageData(getFilterContext(), srcDrawable, input.getUntransformedBounds());
        ImageData ret = this.softwarePeer.filter(effect, rstate, transform, outputClip, heapinput.transform(input.getTransform()));
        return ret;
    }
}
