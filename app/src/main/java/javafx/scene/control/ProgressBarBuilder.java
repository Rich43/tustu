package javafx.scene.control;

import javafx.scene.control.ProgressBarBuilder;

@Deprecated
/* loaded from: jfxrt.jar:javafx/scene/control/ProgressBarBuilder.class */
public class ProgressBarBuilder<B extends ProgressBarBuilder<B>> extends ProgressIndicatorBuilder<B> {
    protected ProgressBarBuilder() {
    }

    public static ProgressBarBuilder<?> create() {
        return new ProgressBarBuilder<>();
    }

    @Override // javafx.scene.control.ProgressIndicatorBuilder, javafx.util.Builder
    public ProgressBar build() {
        ProgressBar x2 = new ProgressBar();
        applyTo((ProgressIndicator) x2);
        return x2;
    }
}
