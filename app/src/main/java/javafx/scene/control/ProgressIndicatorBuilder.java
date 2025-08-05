package javafx.scene.control;

import javafx.scene.control.ProgressIndicatorBuilder;
import javafx.util.Builder;

@Deprecated
/* loaded from: jfxrt.jar:javafx/scene/control/ProgressIndicatorBuilder.class */
public class ProgressIndicatorBuilder<B extends ProgressIndicatorBuilder<B>> extends ControlBuilder<B> implements Builder<ProgressIndicator> {
    private boolean __set;
    private double progress;

    protected ProgressIndicatorBuilder() {
    }

    public static ProgressIndicatorBuilder<?> create() {
        return new ProgressIndicatorBuilder<>();
    }

    public void applyTo(ProgressIndicator x2) {
        super.applyTo((Control) x2);
        if (this.__set) {
            x2.setProgress(this.progress);
        }
    }

    public B progress(double x2) {
        this.progress = x2;
        this.__set = true;
        return this;
    }

    @Override // javafx.util.Builder
    public ProgressIndicator build() {
        ProgressIndicator x2 = new ProgressIndicator();
        applyTo(x2);
        return x2;
    }
}
