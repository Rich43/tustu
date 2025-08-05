package javafx.animation;

import java.util.Arrays;
import java.util.Collection;
import javafx.util.Builder;

@Deprecated
/* loaded from: jfxrt.jar:javafx/animation/TimelineBuilder.class */
public final class TimelineBuilder extends AnimationBuilder<TimelineBuilder> implements Builder<Timeline> {
    private boolean __set;
    private Collection<? extends KeyFrame> keyFrames;
    private double targetFramerate;

    protected TimelineBuilder() {
    }

    public static TimelineBuilder create() {
        return new TimelineBuilder();
    }

    public void applyTo(Timeline x2) {
        super.applyTo((Animation) x2);
        if (this.__set) {
            x2.getKeyFrames().addAll(this.keyFrames);
        }
    }

    public TimelineBuilder keyFrames(Collection<? extends KeyFrame> x2) {
        this.keyFrames = x2;
        this.__set = true;
        return this;
    }

    public TimelineBuilder keyFrames(KeyFrame... x2) {
        return keyFrames(Arrays.asList(x2));
    }

    @Override // javafx.animation.AnimationBuilder
    public TimelineBuilder targetFramerate(double x2) {
        this.targetFramerate = x2;
        return this;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // javafx.util.Builder
    /* renamed from: build */
    public Timeline build2() {
        Timeline x2 = new Timeline(this.targetFramerate);
        applyTo(x2);
        return x2;
    }
}
