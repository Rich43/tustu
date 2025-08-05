package com.sun.scenario.effect.light;

import com.sun.scenario.effect.Color4f;
import com.sun.scenario.effect.light.Light;

/* loaded from: jfxrt.jar:com/sun/scenario/effect/light/PointLight.class */
public class PointLight extends Light {

    /* renamed from: x, reason: collision with root package name */
    private float f12039x;

    /* renamed from: y, reason: collision with root package name */
    private float f12040y;

    /* renamed from: z, reason: collision with root package name */
    private float f12041z;

    public PointLight() {
        this(0.0f, 0.0f, 0.0f, Color4f.WHITE);
    }

    public PointLight(float x2, float y2, float z2, Color4f color) {
        this(Light.Type.POINT, x2, y2, z2, color);
    }

    PointLight(Light.Type type, float x2, float y2, float z2, Color4f color) {
        super(type, color);
        this.f12039x = x2;
        this.f12040y = y2;
        this.f12041z = z2;
    }

    public float getX() {
        return this.f12039x;
    }

    public void setX(float x2) {
        this.f12039x = x2;
    }

    public float getY() {
        return this.f12040y;
    }

    public void setY(float y2) {
        this.f12040y = y2;
    }

    public float getZ() {
        return this.f12041z;
    }

    public void setZ(float z2) {
        this.f12041z = z2;
    }

    @Override // com.sun.scenario.effect.light.Light
    public float[] getNormalizedLightPosition() {
        float len = (float) Math.sqrt((this.f12039x * this.f12039x) + (this.f12040y * this.f12040y) + (this.f12041z * this.f12041z));
        if (len == 0.0f) {
            len = 1.0f;
        }
        float[] pos = {this.f12039x / len, this.f12040y / len, this.f12041z / len};
        return pos;
    }
}
