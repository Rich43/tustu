package com.sun.scenario.effect.light;

import com.sun.scenario.effect.Color4f;
import com.sun.scenario.effect.light.Light;

/* loaded from: jfxrt.jar:com/sun/scenario/effect/light/SpotLight.class */
public class SpotLight extends PointLight {
    private float pointsAtX;
    private float pointsAtY;
    private float pointsAtZ;
    private float specularExponent;

    public SpotLight() {
        this(0.0f, 0.0f, 0.0f, Color4f.WHITE);
    }

    public SpotLight(float x2, float y2, float z2, Color4f color) {
        super(Light.Type.SPOT, x2, y2, z2, color);
        this.pointsAtX = 0.0f;
        this.pointsAtY = 0.0f;
        this.pointsAtZ = 0.0f;
        this.specularExponent = 1.0f;
    }

    public float getPointsAtX() {
        return this.pointsAtX;
    }

    public void setPointsAtX(float pointsAtX) {
        this.pointsAtX = pointsAtX;
    }

    public float getPointsAtY() {
        return this.pointsAtY;
    }

    public void setPointsAtY(float pointsAtY) {
        float f2 = this.pointsAtY;
        this.pointsAtY = pointsAtY;
    }

    public float getPointsAtZ() {
        return this.pointsAtZ;
    }

    public void setPointsAtZ(float pointsAtZ) {
        this.pointsAtZ = pointsAtZ;
    }

    public float getSpecularExponent() {
        return this.specularExponent;
    }

    public void setSpecularExponent(float specularExponent) {
        if (specularExponent < 0.0f || specularExponent > 4.0f) {
            throw new IllegalArgumentException("Specular exponent must be in the range [0,4]");
        }
        this.specularExponent = specularExponent;
    }

    @Override // com.sun.scenario.effect.light.PointLight, com.sun.scenario.effect.light.Light
    public float[] getNormalizedLightPosition() {
        float x2 = getX();
        float y2 = getY();
        float z2 = getZ();
        float len = (float) Math.sqrt((x2 * x2) + (y2 * y2) + (z2 * z2));
        if (len == 0.0f) {
            len = 1.0f;
        }
        float[] pos = {x2 / len, y2 / len, z2 / len};
        return pos;
    }

    public float[] getNormalizedLightDirection() {
        float sx = this.pointsAtX - getX();
        float sy = this.pointsAtY - getY();
        float sz = this.pointsAtZ - getZ();
        float len = (float) Math.sqrt((sx * sx) + (sy * sy) + (sz * sz));
        if (len == 0.0f) {
            len = 1.0f;
        }
        float[] vec = {sx / len, sy / len, sz / len};
        return vec;
    }
}
