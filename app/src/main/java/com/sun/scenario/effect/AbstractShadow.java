package com.sun.scenario.effect;

/* loaded from: jfxrt.jar:com/sun/scenario/effect/AbstractShadow.class */
public abstract class AbstractShadow extends LinearConvolveCoreEffect {

    /* loaded from: jfxrt.jar:com/sun/scenario/effect/AbstractShadow$ShadowMode.class */
    public enum ShadowMode {
        ONE_PASS_BOX,
        TWO_PASS_BOX,
        THREE_PASS_BOX,
        GAUSSIAN
    }

    public abstract ShadowMode getMode();

    public abstract AbstractShadow implFor(ShadowMode shadowMode);

    public abstract float getGaussianRadius();

    public abstract void setGaussianRadius(float f2);

    public abstract float getGaussianWidth();

    public abstract void setGaussianWidth(float f2);

    public abstract float getGaussianHeight();

    public abstract void setGaussianHeight(float f2);

    public abstract float getSpread();

    public abstract void setSpread(float f2);

    public abstract Color4f getColor();

    public abstract void setColor(Color4f color4f);

    public abstract Effect getInput();

    public abstract void setInput(Effect effect);

    public AbstractShadow(Effect input) {
        super(input);
    }
}
