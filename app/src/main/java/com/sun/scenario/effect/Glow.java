package com.sun.scenario.effect;

import com.sun.javafx.geom.Point2D;
import com.sun.scenario.effect.Blend;

/* loaded from: jfxrt.jar:com/sun/scenario/effect/Glow.class */
public class Glow extends DelegateEffect {
    private final GaussianBlur blur;
    private final Blend blend;

    public Glow() {
        this(DefaultInput);
    }

    public Glow(Effect input) {
        super(input);
        this.blur = new GaussianBlur(10.0f, input);
        Crop crop = new Crop(this.blur, input);
        this.blend = new Blend(Blend.Mode.ADD, input, crop);
        this.blend.setOpacity(0.3f);
    }

    @Override // com.sun.scenario.effect.DelegateEffect
    protected Effect getDelegate() {
        return this.blend;
    }

    public final Effect getInput() {
        return getInputs().get(0);
    }

    public void setInput(Effect input) {
        setInput(0, input);
        this.blur.setInput(input);
        this.blend.setBottomInput(input);
    }

    public float getLevel() {
        return this.blend.getOpacity();
    }

    public void setLevel(float level) {
        this.blend.getOpacity();
        this.blend.setOpacity(level);
    }

    @Override // com.sun.scenario.effect.DelegateEffect, com.sun.scenario.effect.Effect
    public Point2D transform(Point2D p2, Effect defaultInput) {
        return getDefaultedInput(0, defaultInput).transform(p2, defaultInput);
    }

    @Override // com.sun.scenario.effect.DelegateEffect, com.sun.scenario.effect.Effect
    public Point2D untransform(Point2D p2, Effect defaultInput) {
        return getDefaultedInput(0, defaultInput).untransform(p2, defaultInput);
    }
}
