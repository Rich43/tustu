package javafx.animation;

import javafx.scene.Node;
import javafx.util.Builder;
import javafx.util.Duration;

@Deprecated
/* loaded from: jfxrt.jar:javafx/animation/FadeTransitionBuilder.class */
public final class FadeTransitionBuilder extends TransitionBuilder<FadeTransitionBuilder> implements Builder<FadeTransition> {
    private int __set;
    private double byValue;
    private Duration duration;
    private double fromValue;
    private Node node;
    private double toValue;

    protected FadeTransitionBuilder() {
    }

    public static FadeTransitionBuilder create() {
        return new FadeTransitionBuilder();
    }

    public void applyTo(FadeTransition x2) {
        super.applyTo((Transition) x2);
        int set = this.__set;
        if ((set & 1) != 0) {
            x2.setByValue(this.byValue);
        }
        if ((set & 2) != 0) {
            x2.setDuration(this.duration);
        }
        if ((set & 4) != 0) {
            x2.setFromValue(this.fromValue);
        }
        if ((set & 8) != 0) {
            x2.setNode(this.node);
        }
        if ((set & 16) != 0) {
            x2.setToValue(this.toValue);
        }
    }

    public FadeTransitionBuilder byValue(double x2) {
        this.byValue = x2;
        this.__set |= 1;
        return this;
    }

    public FadeTransitionBuilder duration(Duration x2) {
        this.duration = x2;
        this.__set |= 2;
        return this;
    }

    public FadeTransitionBuilder fromValue(double x2) {
        this.fromValue = x2;
        this.__set |= 4;
        return this;
    }

    public FadeTransitionBuilder node(Node x2) {
        this.node = x2;
        this.__set |= 8;
        return this;
    }

    public FadeTransitionBuilder toValue(double x2) {
        this.toValue = x2;
        this.__set |= 16;
        return this;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // javafx.util.Builder
    /* renamed from: build */
    public FadeTransition build2() {
        FadeTransition x2 = new FadeTransition();
        applyTo(x2);
        return x2;
    }
}
