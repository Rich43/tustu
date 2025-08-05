package com.sun.scenario.effect.impl.prism.ps;

import com.sun.javafx.geom.Rectangle;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.prism.ps.Shader;
import com.sun.scenario.effect.FilterContext;
import com.sun.scenario.effect.PerspectiveTransform;
import com.sun.scenario.effect.impl.Renderer;
import com.sun.scenario.effect.impl.state.AccessHelper;
import com.sun.scenario.effect.impl.state.PerspectiveTransformState;
import java.util.HashMap;

/* loaded from: jfxrt.jar:com/sun/scenario/effect/impl/prism/ps/PPSPerspectiveTransformPeer.class */
public class PPSPerspectiveTransformPeer extends PPSOneSamplerPeer {
    public PPSPerspectiveTransformPeer(FilterContext fctx, Renderer r2, String shaderName) {
        super(fctx, r2, shaderName);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.sun.scenario.effect.impl.EffectPeer
    public final PerspectiveTransform getEffect() {
        return (PerspectiveTransform) super.getEffect();
    }

    private float[][] getITX() {
        PerspectiveTransformState state = (PerspectiveTransformState) AccessHelper.getState(getEffect());
        return state.getITX();
    }

    private float[] getTx0() {
        Rectangle ib = getInputBounds(0);
        Rectangle nb = getInputNativeBounds(0);
        float scalex = ib.width / nb.width;
        float[] itx0 = getITX()[0];
        return new float[]{itx0[0] * scalex, itx0[1] * scalex, itx0[2] * scalex};
    }

    private float[] getTx1() {
        Rectangle ib = getInputBounds(0);
        Rectangle nb = getInputNativeBounds(0);
        float scaley = ib.height / nb.height;
        float[] itx1 = getITX()[1];
        return new float[]{itx1[0] * scaley, itx1[1] * scaley, itx1[2] * scaley};
    }

    private float[] getTx2() {
        return getITX()[2];
    }

    @Override // com.sun.scenario.effect.impl.EffectPeer
    public int getTextureCoordinates(int inputIndex, float[] coords, float srcX, float srcY, float srcNativeWidth, float srcNativeHeight, Rectangle dstBounds, BaseTransform transform) {
        coords[0] = dstBounds.f11913x;
        coords[1] = dstBounds.f11914y;
        coords[2] = dstBounds.f11913x + dstBounds.width;
        coords[3] = dstBounds.f11914y + dstBounds.height;
        return 4;
    }

    @Override // com.sun.scenario.effect.impl.prism.ps.PPSEffectPeer
    protected boolean isSamplerLinear(int i2) {
        switch (i2) {
            case 0:
                return true;
            default:
                return false;
        }
    }

    @Override // com.sun.scenario.effect.impl.prism.ps.PPSEffectPeer
    protected Shader createShader() {
        HashMap<String, Integer> samplers = new HashMap<>();
        samplers.put("baseImg", 0);
        HashMap<String, Integer> params = new HashMap<>();
        params.put("tx1", 1);
        params.put("tx0", 0);
        params.put("tx2", 2);
        return getRenderer().createShader(getShaderName(), samplers, params, false);
    }

    @Override // com.sun.scenario.effect.impl.prism.ps.PPSEffectPeer
    protected void updateShader(Shader shader) {
        float[] tx1_tmp = getTx1();
        shader.setConstant("tx1", tx1_tmp[0], tx1_tmp[1], tx1_tmp[2]);
        float[] tx0_tmp = getTx0();
        shader.setConstant("tx0", tx0_tmp[0], tx0_tmp[1], tx0_tmp[2]);
        float[] tx2_tmp = getTx2();
        shader.setConstant("tx2", tx2_tmp[0], tx2_tmp[1], tx2_tmp[2]);
    }
}
