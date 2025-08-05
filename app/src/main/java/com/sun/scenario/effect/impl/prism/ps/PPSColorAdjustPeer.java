package com.sun.scenario.effect.impl.prism.ps;

import com.sun.prism.ps.Shader;
import com.sun.scenario.effect.ColorAdjust;
import com.sun.scenario.effect.FilterContext;
import com.sun.scenario.effect.impl.Renderer;
import java.util.HashMap;

/* loaded from: jfxrt.jar:com/sun/scenario/effect/impl/prism/ps/PPSColorAdjustPeer.class */
public class PPSColorAdjustPeer extends PPSOneSamplerPeer {
    public PPSColorAdjustPeer(FilterContext fctx, Renderer r2, String shaderName) {
        super(fctx, r2, shaderName);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.sun.scenario.effect.impl.EffectPeer
    public final ColorAdjust getEffect() {
        return (ColorAdjust) super.getEffect();
    }

    private float getHue() {
        return getEffect().getHue() / 2.0f;
    }

    private float getSaturation() {
        return getEffect().getSaturation() + 1.0f;
    }

    private float getBrightness() {
        return getEffect().getBrightness() + 1.0f;
    }

    private float getContrast() {
        float c2 = getEffect().getContrast();
        if (c2 > 0.0f) {
            c2 *= 3.0f;
        }
        return c2 + 1.0f;
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
        params.put("saturation", 1);
        params.put("brightness", 2);
        params.put("contrast", 3);
        params.put("hue", 0);
        return getRenderer().createShader(getShaderName(), samplers, params, false);
    }

    @Override // com.sun.scenario.effect.impl.prism.ps.PPSEffectPeer
    protected void updateShader(Shader shader) {
        shader.setConstant("saturation", getSaturation());
        shader.setConstant("brightness", getBrightness());
        shader.setConstant("contrast", getContrast());
        shader.setConstant("hue", getHue());
    }
}
