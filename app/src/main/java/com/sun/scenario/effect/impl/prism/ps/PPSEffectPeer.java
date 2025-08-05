package com.sun.scenario.effect.impl.prism.ps;

import com.sun.javafx.geom.Rectangle;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.prism.ps.Shader;
import com.sun.scenario.effect.Effect;
import com.sun.scenario.effect.FilterContext;
import com.sun.scenario.effect.ImageData;
import com.sun.scenario.effect.impl.EffectPeer;
import com.sun.scenario.effect.impl.Renderer;
import com.sun.scenario.effect.impl.state.RenderState;

/* loaded from: jfxrt.jar:com/sun/scenario/effect/impl/prism/ps/PPSEffectPeer.class */
public abstract class PPSEffectPeer<T extends RenderState> extends EffectPeer<T> {
    abstract ImageData filterImpl(ImageData... imageDataArr);

    protected abstract boolean isSamplerLinear(int i2);

    protected abstract Shader createShader();

    protected abstract void updateShader(Shader shader);

    @Override // com.sun.scenario.effect.impl.EffectPeer
    public abstract void dispose();

    protected PPSEffectPeer(FilterContext fctx, Renderer r2, String shaderName) {
        super(fctx, r2, shaderName);
    }

    @Override // com.sun.scenario.effect.impl.EffectPeer
    public final ImageData filter(Effect effect, T renderState, BaseTransform transform, Rectangle outputClip, ImageData... inputs) {
        setEffect(effect);
        setRenderState(renderState);
        setDestBounds(getResultBounds(transform, outputClip, inputs));
        return filterImpl(inputs);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.sun.scenario.effect.impl.EffectPeer
    public final PPSRenderer getRenderer() {
        return (PPSRenderer) super.getRenderer();
    }

    protected final String getShaderName() {
        return getUniqueName();
    }
}
