package com.sun.scenario.effect.impl.prism.ps;

import com.sun.org.apache.xalan.internal.templates.Constants;
import com.sun.prism.ps.Shader;
import com.sun.scenario.effect.FilterContext;
import com.sun.scenario.effect.SepiaTone;
import com.sun.scenario.effect.impl.Renderer;
import java.util.HashMap;

/* loaded from: jfxrt.jar:com/sun/scenario/effect/impl/prism/ps/PPSSepiaTonePeer.class */
public class PPSSepiaTonePeer extends PPSOneSamplerPeer {
    public PPSSepiaTonePeer(FilterContext fctx, Renderer r2, String shaderName) {
        super(fctx, r2, shaderName);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.sun.scenario.effect.impl.EffectPeer
    public final SepiaTone getEffect() {
        return (SepiaTone) super.getEffect();
    }

    private float getLevel() {
        return getEffect().getLevel();
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
        params.put(Constants.ATTRNAME_LEVEL, 0);
        return getRenderer().createShader(getShaderName(), samplers, params, false);
    }

    @Override // com.sun.scenario.effect.impl.prism.ps.PPSEffectPeer
    protected void updateShader(Shader shader) {
        shader.setConstant(Constants.ATTRNAME_LEVEL, getLevel());
    }
}
