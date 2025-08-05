package com.sun.scenario.effect.light;

import com.sun.scenario.effect.Color4f;
import com.sun.scenario.effect.light.Light;

/* loaded from: jfxrt.jar:com/sun/scenario/effect/light/DistantLight.class */
public class DistantLight extends Light {
    private float azimuth;
    private float elevation;

    public DistantLight() {
        this(0.0f, 0.0f, Color4f.WHITE);
    }

    public DistantLight(float azimuth, float elevation, Color4f color) {
        super(Light.Type.DISTANT, color);
        this.azimuth = azimuth;
        this.elevation = elevation;
    }

    public float getAzimuth() {
        return this.azimuth;
    }

    public void setAzimuth(float azimuth) {
        this.azimuth = azimuth;
    }

    public float getElevation() {
        return this.elevation;
    }

    public void setElevation(float elevation) {
        this.elevation = elevation;
    }

    @Override // com.sun.scenario.effect.light.Light
    public float[] getNormalizedLightPosition() {
        double a2 = Math.toRadians(this.azimuth);
        double e2 = Math.toRadians(this.elevation);
        float x2 = (float) (Math.cos(a2) * Math.cos(e2));
        float y2 = (float) (Math.sin(a2) * Math.cos(e2));
        float z2 = (float) Math.sin(e2);
        float len = (float) Math.sqrt((x2 * x2) + (y2 * y2) + (z2 * z2));
        if (len == 0.0f) {
            len = 1.0f;
        }
        float[] pos = {x2 / len, y2 / len, z2 / len};
        return pos;
    }
}
