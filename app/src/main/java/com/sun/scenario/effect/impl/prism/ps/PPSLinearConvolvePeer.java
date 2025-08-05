package com.sun.scenario.effect.impl.prism.ps;

import com.sun.javafx.geom.Rectangle;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.prism.ps.Shader;
import com.sun.scenario.effect.Effect;
import com.sun.scenario.effect.FilterContext;
import com.sun.scenario.effect.ImageData;
import com.sun.scenario.effect.impl.Renderer;
import com.sun.scenario.effect.impl.state.LinearConvolveRenderState;
import java.nio.FloatBuffer;
import java.util.HashMap;

/* loaded from: jfxrt.jar:com/sun/scenario/effect/impl/prism/ps/PPSLinearConvolvePeer.class */
public class PPSLinearConvolvePeer extends PPSOneSamplerPeer<LinearConvolveRenderState> {
    public PPSLinearConvolvePeer(FilterContext fctx, Renderer r2, String shaderName) {
        super(fctx, r2, shaderName);
    }

    @Override // com.sun.scenario.effect.impl.EffectPeer
    protected final Effect getEffect() {
        return super.getEffect();
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.sun.scenario.effect.impl.EffectPeer
    public Rectangle getResultBounds(BaseTransform transform, Rectangle outputClip, ImageData... inputDatas) {
        Rectangle r2 = inputDatas[0].getTransformedBounds(null);
        return ((LinearConvolveRenderState) getRenderState()).getPassResultBounds(r2, outputClip);
    }

    /* JADX WARN: Multi-variable type inference failed */
    private int getCount() {
        return (((LinearConvolveRenderState) getRenderState()).getPassKernelSize() + 3) / 4;
    }

    /* JADX WARN: Multi-variable type inference failed */
    private float[] getOffset() {
        return ((LinearConvolveRenderState) getRenderState()).getPassVector();
    }

    /* JADX WARN: Multi-variable type inference failed */
    private FloatBuffer getWeights() {
        return ((LinearConvolveRenderState) getRenderState()).getPassWeights();
    }

    /* JADX WARN: Multi-variable type inference failed */
    private int getWeightsArrayLength() {
        return ((LinearConvolveRenderState) getRenderState()).getPassWeightsArrayLength();
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
        samplers.put("img", 0);
        HashMap<String, Integer> params = new HashMap<>();
        params.put("offset", 1);
        params.put("count", 0);
        params.put("weights", 2);
        return getRenderer().createShader(getShaderName(), samplers, params, false);
    }

    @Override // com.sun.scenario.effect.impl.prism.ps.PPSEffectPeer
    protected void updateShader(Shader shader) {
        float[] offset_tmp = getOffset();
        shader.setConstant("offset", offset_tmp[0], offset_tmp[1], offset_tmp[2], offset_tmp[3]);
        shader.setConstant("count", getCount());
        shader.setConstants("weights", getWeights(), 0, getWeightsArrayLength());
    }
}
