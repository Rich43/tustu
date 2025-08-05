package com.sun.scenario.effect;

/* loaded from: jfxrt.jar:com/sun/scenario/effect/Color4f.class */
public final class Color4f {
    public static final Color4f BLACK = new Color4f(0.0f, 0.0f, 0.0f, 1.0f);
    public static final Color4f WHITE = new Color4f(1.0f, 1.0f, 1.0f, 1.0f);

    /* renamed from: r, reason: collision with root package name */
    private final float f12033r;

    /* renamed from: g, reason: collision with root package name */
    private final float f12034g;

    /* renamed from: b, reason: collision with root package name */
    private final float f12035b;

    /* renamed from: a, reason: collision with root package name */
    private final float f12036a;

    public Color4f(float r2, float g2, float b2, float a2) {
        this.f12033r = r2;
        this.f12034g = g2;
        this.f12035b = b2;
        this.f12036a = a2;
    }

    public float getRed() {
        return this.f12033r;
    }

    public float getGreen() {
        return this.f12034g;
    }

    public float getBlue() {
        return this.f12035b;
    }

    public float getAlpha() {
        return this.f12036a;
    }

    public float[] getPremultipliedRGBComponents() {
        float[] comps = {this.f12033r * this.f12036a, this.f12034g * this.f12036a, this.f12035b * this.f12036a, this.f12036a};
        return comps;
    }
}
