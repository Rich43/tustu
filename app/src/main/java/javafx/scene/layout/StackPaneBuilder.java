package javafx.scene.layout;

import javafx.geometry.Pos;
import javafx.scene.layout.StackPaneBuilder;

@Deprecated
/* loaded from: jfxrt.jar:javafx/scene/layout/StackPaneBuilder.class */
public class StackPaneBuilder<B extends StackPaneBuilder<B>> extends PaneBuilder<B> {
    private boolean __set;
    private Pos alignment;

    protected StackPaneBuilder() {
    }

    public static StackPaneBuilder<?> create() {
        return new StackPaneBuilder<>();
    }

    public void applyTo(StackPane x2) {
        super.applyTo((Pane) x2);
        if (this.__set) {
            x2.setAlignment(this.alignment);
        }
    }

    public B alignment(Pos x2) {
        this.alignment = x2;
        this.__set = true;
        return this;
    }

    @Override // javafx.scene.layout.PaneBuilder, javafx.scene.layout.RegionBuilder, javafx.util.Builder
    /* renamed from: build */
    public StackPane build2() {
        StackPane x2 = new StackPane();
        applyTo(x2);
        return x2;
    }
}
