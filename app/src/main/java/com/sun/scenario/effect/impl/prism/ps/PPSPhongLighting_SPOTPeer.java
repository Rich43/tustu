package com.sun.scenario.effect.impl.prism.ps;

import com.sun.javafx.geom.Rectangle;
import com.sun.prism.ps.Shader;
import com.sun.scenario.effect.FilterContext;
import com.sun.scenario.effect.PhongLighting;
import com.sun.scenario.effect.impl.BufferUtil;
import com.sun.scenario.effect.impl.Renderer;
import com.sun.scenario.effect.light.PointLight;
import com.sun.scenario.effect.light.SpotLight;
import java.nio.FloatBuffer;
import java.util.HashMap;

/* loaded from: jfxrt.jar:com/sun/scenario/effect/impl/prism/ps/PPSPhongLighting_SPOTPeer.class */
public class PPSPhongLighting_SPOTPeer extends PPSTwoSamplerPeer {
    private FloatBuffer kvals;

    public PPSPhongLighting_SPOTPeer(FilterContext fctx, Renderer r2, String shaderName) {
        super(fctx, r2, shaderName);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.sun.scenario.effect.impl.EffectPeer
    public final PhongLighting getEffect() {
        return (PhongLighting) super.getEffect();
    }

    private float getSurfaceScale() {
        return getEffect().getSurfaceScale();
    }

    private float getDiffuseConstant() {
        return getEffect().getDiffuseConstant();
    }

    private float getSpecularConstant() {
        return getEffect().getSpecularConstant();
    }

    private float getSpecularExponent() {
        return getEffect().getSpecularExponent();
    }

    private float[] getNormalizedLightPosition() {
        return getEffect().getLight().getNormalizedLightPosition();
    }

    private float[] getLightPosition() {
        PointLight plight = (PointLight) getEffect().getLight();
        return new float[]{plight.getX(), plight.getY(), plight.getZ()};
    }

    private float[] getLightColor() {
        return getEffect().getLight().getColor().getPremultipliedRGBComponents();
    }

    private float getLightSpecularExponent() {
        return ((SpotLight) getEffect().getLight()).getSpecularExponent();
    }

    private float[] getNormalizedLightDirection() {
        return ((SpotLight) getEffect().getLight()).getNormalizedLightDirection();
    }

    private FloatBuffer getKvals() {
        Rectangle bumpImgBounds = getInputNativeBounds(0);
        float xoff = 1.0f / bumpImgBounds.width;
        float yoff = 1.0f / bumpImgBounds.height;
        float[] kx = {-1.0f, 0.0f, 1.0f, -2.0f, 0.0f, 2.0f, -1.0f, 0.0f, 1.0f};
        float[] ky = {-1.0f, -2.0f, -1.0f, 0.0f, 0.0f, 0.0f, 1.0f, 2.0f, 1.0f};
        if (this.kvals == null) {
            this.kvals = BufferUtil.newFloatBuffer(32);
        }
        this.kvals.clear();
        int kidx = 0;
        float factor = (-getSurfaceScale()) * 0.25f;
        for (int i2 = -1; i2 <= 1; i2++) {
            for (int j2 = -1; j2 <= 1; j2++) {
                if (i2 != 0 || j2 != 0) {
                    this.kvals.put(j2 * xoff);
                    this.kvals.put(i2 * yoff);
                    this.kvals.put(kx[kidx] * factor);
                    this.kvals.put(ky[kidx] * factor);
                }
                kidx++;
            }
        }
        this.kvals.rewind();
        return this.kvals;
    }

    private int getKvalsArrayLength() {
        return 8;
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
        samplers.put("bumpImg", 0);
        samplers.put("origImg", 1);
        HashMap<String, Integer> params = new HashMap<>();
        params.put("lightPosition", 13);
        params.put("lightSpecularExponent", 15);
        params.put("specularExponent", 2);
        params.put("kvals", 4);
        params.put("diffuseConstant", 0);
        params.put("lightColor", 3);
        params.put("normalizedLightDirection", 14);
        params.put("specularConstant", 1);
        params.put("surfaceScale", 12);
        return getRenderer().createShader(getShaderName(), samplers, params, true);
    }

    @Override // com.sun.scenario.effect.impl.prism.ps.PPSEffectPeer
    protected void updateShader(Shader shader) {
        float[] lightPosition_tmp = getLightPosition();
        shader.setConstant("lightPosition", lightPosition_tmp[0], lightPosition_tmp[1], lightPosition_tmp[2]);
        shader.setConstant("lightSpecularExponent", getLightSpecularExponent());
        shader.setConstant("specularExponent", getSpecularExponent());
        shader.setConstants("kvals", getKvals(), 0, getKvalsArrayLength());
        shader.setConstant("diffuseConstant", getDiffuseConstant());
        float[] lightColor_tmp = getLightColor();
        shader.setConstant("lightColor", lightColor_tmp[0], lightColor_tmp[1], lightColor_tmp[2]);
        float[] normalizedLightDirection_tmp = getNormalizedLightDirection();
        shader.setConstant("normalizedLightDirection", normalizedLightDirection_tmp[0], normalizedLightDirection_tmp[1], normalizedLightDirection_tmp[2]);
        shader.setConstant("specularConstant", getSpecularConstant());
        shader.setConstant("surfaceScale", getSurfaceScale());
    }
}
