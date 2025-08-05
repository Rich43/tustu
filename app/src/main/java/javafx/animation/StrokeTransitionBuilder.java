package javafx.animation;

import javafx.scene.paint.Color;
import javafx.scene.shape.Shape;
import javafx.util.Builder;
import javafx.util.Duration;

@Deprecated
/* loaded from: jfxrt.jar:javafx/animation/StrokeTransitionBuilder.class */
public final class StrokeTransitionBuilder extends TransitionBuilder<StrokeTransitionBuilder> implements Builder<StrokeTransition> {
    private int __set;
    private Duration duration;
    private Color fromValue;
    private Shape shape;
    private Color toValue;

    protected StrokeTransitionBuilder() {
    }

    public static StrokeTransitionBuilder create() {
        return new StrokeTransitionBuilder();
    }

    public void applyTo(StrokeTransition x2) {
        super.applyTo((Transition) x2);
        int set = this.__set;
        if ((set & 1) != 0) {
            x2.setDuration(this.duration);
        }
        if ((set & 2) != 0) {
            x2.setFromValue(this.fromValue);
        }
        if ((set & 4) != 0) {
            x2.setShape(this.shape);
        }
        if ((set & 8) != 0) {
            x2.setToValue(this.toValue);
        }
    }

    public StrokeTransitionBuilder duration(Duration x2) {
        this.duration = x2;
        this.__set |= 1;
        return this;
    }

    public StrokeTransitionBuilder fromValue(Color x2) {
        this.fromValue = x2;
        this.__set |= 2;
        return this;
    }

    public StrokeTransitionBuilder shape(Shape x2) {
        this.shape = x2;
        this.__set |= 4;
        return this;
    }

    public StrokeTransitionBuilder toValue(Color x2) {
        this.toValue = x2;
        this.__set |= 8;
        return this;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // javafx.util.Builder
    /* renamed from: build */
    public StrokeTransition build2() {
        StrokeTransition x2 = new StrokeTransition();
        applyTo(x2);
        return x2;
    }
}
