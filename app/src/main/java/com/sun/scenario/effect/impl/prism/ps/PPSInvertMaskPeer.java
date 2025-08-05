package com.sun.scenario.effect.impl.prism.ps;

import com.sun.prism.ps.Shader;
import com.sun.scenario.effect.FilterContext;
import com.sun.scenario.effect.InvertMask;
import com.sun.scenario.effect.impl.Renderer;
import java.util.HashMap;

/* loaded from: jfxrt.jar:com/sun/scenario/effect/impl/prism/ps/PPSInvertMaskPeer.class */
public class PPSInvertMaskPeer extends PPSOneSamplerPeer {
    public PPSInvertMaskPeer(FilterContext fctx, Renderer r2, String shaderName) {
        super(fctx, r2, shaderName);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.sun.scenario.effect.impl.EffectPeer
    public final InvertMask getEffect() {
        return (InvertMask) super.getEffect();
    }

    private float[] getOffset() {
        float xoff = getEffect().getOffsetX();
        float yoff = getEffect().getOffsetY();
        float[] offsets = {xoff, yoff};
        try {
            getInputTransform(0).inverseDeltaTransform(offsets, 0, offsets, 0, 1);
        } catch (Exception e2) {
        }
        offsets[0] = offsets[0] / getInputNativeBounds(0).width;
        offsets[1] = offsets[1] / getInputNativeBounds(0).height;
        return offsets;
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
        params.put("offset", 0);
        return getRenderer().createShader(getShaderName(), samplers, params, false);
    }

    @Override // com.sun.scenario.effect.impl.prism.ps.PPSEffectPeer
    protected void updateShader(Shader shader) {
        float[] offset_tmp = getOffset();
        shader.setConstant("offset", offset_tmp[0], offset_tmp[1]);
    }
}
