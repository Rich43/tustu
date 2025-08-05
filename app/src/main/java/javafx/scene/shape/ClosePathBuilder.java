package javafx.scene.shape;

import javafx.scene.shape.ClosePathBuilder;
import javafx.util.Builder;

@Deprecated
/* loaded from: jfxrt.jar:javafx/scene/shape/ClosePathBuilder.class */
public class ClosePathBuilder<B extends ClosePathBuilder<B>> extends PathElementBuilder<B> implements Builder<ClosePath> {
    protected ClosePathBuilder() {
    }

    public static ClosePathBuilder<?> create() {
        return new ClosePathBuilder<>();
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // javafx.util.Builder
    public ClosePath build() {
        ClosePath x2 = new ClosePath();
        applyTo(x2);
        return x2;
    }
}
