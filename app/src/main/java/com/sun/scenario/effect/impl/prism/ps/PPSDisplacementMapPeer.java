package com.sun.scenario.effect.impl.prism.ps;

import com.sun.javafx.geom.Rectangle;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.prism.ps.Shader;
import com.sun.scenario.effect.DisplacementMap;
import com.sun.scenario.effect.FilterContext;
import com.sun.scenario.effect.impl.Renderer;
import java.util.HashMap;

/* loaded from: jfxrt.jar:com/sun/scenario/effect/impl/prism/ps/PPSDisplacementMapPeer.class */
public class PPSDisplacementMapPeer extends PPSTwoSamplerPeer {
    public PPSDisplacementMapPeer(FilterContext fctx, Renderer r2, String shaderName) {
        super(fctx, r2, shaderName);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.sun.scenario.effect.impl.EffectPeer
    public final DisplacementMap getEffect() {
        return (DisplacementMap) super.getEffect();
    }

    private float[] getSampletx() {
        return new float[]{getEffect().getOffsetX(), getEffect().getOffsetY(), getEffect().getScaleX(), getEffect().getScaleY()};
    }

    private float[] getImagetx() {
        float inset = getEffect().getWrap() ? 0.5f : 0.0f;
        return new float[]{inset / getInputNativeBounds(0).width, inset / getInputNativeBounds(0).height, (getInputBounds(0).width - (2.0f * inset)) / getInputNativeBounds(0).width, (getInputBounds(0).height - (2.0f * inset)) / getInputNativeBounds(0).height};
    }

    private float getWrap() {
        return getEffect().getWrap() ? 1.0f : 0.0f;
    }

    @Override // com.sun.scenario.effect.impl.EffectPeer
    protected Object getSamplerData(int i2) {
        return getEffect().getMapData();
    }

    @Override // com.sun.scenario.effect.impl.EffectPeer
    public int getTextureCoordinates(int inputIndex, float[] coords, float srcX, float srcY, float srcNativeWidth, float srcNativeHeight, Rectangle dstBounds, BaseTransform transform) {
        coords[1] = 0.0f;
        coords[0] = 0.0f;
        coords[3] = 1.0f;
        coords[2] = 1.0f;
        return 4;
    }

    @Override // com.sun.scenario.effect.impl.prism.ps.PPSEffectPeer
    protected boolean isSamplerLinear(int i2) {
        switch (i2) {
            case 0:
                break;
            case 1:
                break;
        }
        return true;
    }

    @Override // com.sun.scenario.effect.impl.prism.ps.PPSEffectPeer
    protected Shader createShader() {
        HashMap<String, Integer> samplers = new HashMap<>();
        samplers.put("origImg", 0);
        samplers.put("mapImg", 1);
        HashMap<String, Integer> params = new HashMap<>();
        params.put("imagetx", 1);
        params.put("wrap", 2);
        params.put("sampletx", 0);
        return getRenderer().createShader(getShaderName(), samplers, params, false);
    }

    @Override // com.sun.scenario.effect.impl.prism.ps.PPSEffectPeer
    protected void updateShader(Shader shader) {
        float[] imagetx_tmp = getImagetx();
        shader.setConstant("imagetx", imagetx_tmp[0], imagetx_tmp[1], imagetx_tmp[2], imagetx_tmp[3]);
        shader.setConstant("wrap", getWrap());
        float[] sampletx_tmp = getSampletx();
        shader.setConstant("sampletx", sampletx_tmp[0], sampletx_tmp[1], sampletx_tmp[2], sampletx_tmp[3]);
    }
}
