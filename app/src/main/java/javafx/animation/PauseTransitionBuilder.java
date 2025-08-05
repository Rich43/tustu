package javafx.animation;

import javafx.util.Builder;
import javafx.util.Duration;

@Deprecated
/* loaded from: jfxrt.jar:javafx/animation/PauseTransitionBuilder.class */
public final class PauseTransitionBuilder extends TransitionBuilder<PauseTransitionBuilder> implements Builder<PauseTransition> {
    private boolean __set;
    private Duration duration;

    protected PauseTransitionBuilder() {
    }

    public static PauseTransitionBuilder create() {
        return new PauseTransitionBuilder();
    }

    public void applyTo(PauseTransition x2) {
        super.applyTo((Transition) x2);
        if (this.__set) {
            x2.setDuration(this.duration);
        }
    }

    public PauseTransitionBuilder duration(Duration x2) {
        this.duration = x2;
        this.__set = true;
        return this;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // javafx.util.Builder
    public PauseTransition build() {
        PauseTransition x2 = new PauseTransition();
        applyTo(x2);
        return x2;
    }
}
