package javafx.animation;

import javafx.animation.TransitionBuilder;

@Deprecated
/* loaded from: jfxrt.jar:javafx/animation/TransitionBuilder.class */
public abstract class TransitionBuilder<B extends TransitionBuilder<B>> extends AnimationBuilder<B> {
    private boolean __set;
    private Interpolator interpolator;
    private double targetFramerate;

    protected TransitionBuilder() {
    }

    public void applyTo(Transition x2) {
        super.applyTo((Animation) x2);
        if (this.__set) {
            x2.setInterpolator(this.interpolator);
        }
    }

    public B interpolator(Interpolator x2) {
        this.interpolator = x2;
        this.__set = true;
        return this;
    }

    @Override // javafx.animation.AnimationBuilder
    public B targetFramerate(double x2) {
        this.targetFramerate = x2;
        return this;
    }
}
