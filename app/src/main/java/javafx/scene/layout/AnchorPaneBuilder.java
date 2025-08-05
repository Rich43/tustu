package javafx.scene.layout;

import javafx.scene.layout.AnchorPaneBuilder;

@Deprecated
/* loaded from: jfxrt.jar:javafx/scene/layout/AnchorPaneBuilder.class */
public class AnchorPaneBuilder<B extends AnchorPaneBuilder<B>> extends PaneBuilder<B> {
    protected AnchorPaneBuilder() {
    }

    public static AnchorPaneBuilder<?> create() {
        return new AnchorPaneBuilder<>();
    }

    @Override // javafx.scene.layout.PaneBuilder, javafx.scene.layout.RegionBuilder, javafx.util.Builder
    /* renamed from: build */
    public AnchorPane build2() {
        AnchorPane x2 = new AnchorPane();
        applyTo((Pane) x2);
        return x2;
    }
}
