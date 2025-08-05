package com.sun.scenario.effect.light;

import com.sun.scenario.effect.Color4f;

/* loaded from: jfxrt.jar:com/sun/scenario/effect/light/Light.class */
public abstract class Light {
    private final Type type;
    private Color4f color;

    /* loaded from: jfxrt.jar:com/sun/scenario/effect/light/Light$Type.class */
    public enum Type {
        DISTANT,
        POINT,
        SPOT
    }

    public abstract float[] getNormalizedLightPosition();

    Light(Type type) {
        this(type, Color4f.WHITE);
    }

    Light(Type type, Color4f color) {
        if (type == null) {
            throw new InternalError("Light type must be non-null");
        }
        this.type = type;
        setColor(color);
    }

    public Type getType() {
        return this.type;
    }

    public Color4f getColor() {
        return this.color;
    }

    public void setColor(Color4f color) {
        if (color == null) {
            throw new IllegalArgumentException("Color must be non-null");
        }
        this.color = color;
    }
}
