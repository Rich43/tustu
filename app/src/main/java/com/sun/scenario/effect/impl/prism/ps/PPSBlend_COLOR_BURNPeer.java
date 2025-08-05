package com.sun.scenario.effect.impl.prism.ps;

import com.sun.prism.ps.Shader;
import com.sun.scenario.effect.Blend;
import com.sun.scenario.effect.FilterContext;
import com.sun.scenario.effect.impl.Renderer;
import java.util.HashMap;

/* loaded from: jfxrt.jar:com/sun/scenario/effect/impl/prism/ps/PPSBlend_COLOR_BURNPeer.class */
public class PPSBlend_COLOR_BURNPeer extends PPSTwoSamplerPeer {
    public PPSBlend_COLOR_BURNPeer(FilterContext fctx, Renderer r2, String shaderName) {
        super(fctx, r2, shaderName);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.sun.scenario.effect.impl.EffectPeer
    public final Blend getEffect() {
        return (Blend) super.getEffect();
    }

    private float getOpacity() {
        return getEffect().getOpacity();
    }

    /* JADX WARN: Failed to find 'out' block for switch in B:3:0x0001. Please report as an issue. */
    @Override // com.sun.scenario.effect.impl.prism.ps.PPSEffectPeer
    protected boolean isSamplerLinear(int i2) {
        switch (i2) {
        }
        return false;
    }

    @Override // com.sun.scenario.effect.impl.prism.ps.PPSEffectPeer
    protected Shader createShader() {
        HashMap<String, Integer> samplers = new HashMap<>();
        samplers.put("botImg", 0);
        samplers.put("topImg", 1);
        HashMap<String, Integer> params = new HashMap<>();
        params.put("opacity", 0);
        return getRenderer().createShader(getShaderName(), samplers, params, false);
    }

    @Override // com.sun.scenario.effect.impl.prism.ps.PPSEffectPeer
    protected void updateShader(Shader shader) {
        shader.setConstant("opacity", getOpacity());
    }
}
