package com.sun.scenario.effect;

import com.sun.javafx.geom.Point2D;
import com.sun.scenario.effect.Blend;

/* loaded from: jfxrt.jar:com/sun/scenario/effect/Bloom.class */
public class Bloom extends DelegateEffect {
    private final Brightpass brightpass;
    private final GaussianBlur blur;
    private final Blend blend;

    public Bloom() {
        this(DefaultInput);
    }

    public Bloom(Effect input) {
        super(input);
        this.brightpass = new Brightpass(input);
        this.blur = new GaussianBlur(10.0f, this.brightpass);
        Crop crop = new Crop(this.blur, input);
        this.blend = new Blend(Blend.Mode.ADD, input, crop);
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
        this.brightpass.setInput(input);
        this.blend.setBottomInput(input);
    }

    public float getThreshold() {
        return this.brightpass.getThreshold();
    }

    public void setThreshold(float threshold) {
        this.brightpass.getThreshold();
        this.brightpass.setThreshold(threshold);
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
