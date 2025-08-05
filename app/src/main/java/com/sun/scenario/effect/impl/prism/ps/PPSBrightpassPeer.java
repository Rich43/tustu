package com.sun.scenario.effect.impl.prism.ps;

import com.sun.prism.ps.Shader;
import com.sun.scenario.effect.Brightpass;
import com.sun.scenario.effect.FilterContext;
import com.sun.scenario.effect.impl.Renderer;
import java.util.HashMap;
import jdk.jfr.Threshold;

/* loaded from: jfxrt.jar:com/sun/scenario/effect/impl/prism/ps/PPSBrightpassPeer.class */
public class PPSBrightpassPeer extends PPSOneSamplerPeer {
    public PPSBrightpassPeer(FilterContext fctx, Renderer r2, String shaderName) {
        super(fctx, r2, shaderName);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.sun.scenario.effect.impl.EffectPeer
    public final Brightpass getEffect() {
        return (Brightpass) super.getEffect();
    }

    private float getThreshold() {
        return getEffect().getThreshold();
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
        samplers.put("baseImg", 0);
        HashMap<String, Integer> params = new HashMap<>();
        params.put(Threshold.NAME, 0);
        return getRenderer().createShader(getShaderName(), samplers, params, false);
    }

    @Override // com.sun.scenario.effect.impl.prism.ps.PPSEffectPeer
    protected void updateShader(Shader shader) {
        shader.setConstant(Threshold.NAME, getThreshold());
    }
}
