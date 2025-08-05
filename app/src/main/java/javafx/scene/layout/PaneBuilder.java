package javafx.scene.layout;

import java.util.Arrays;
import java.util.Collection;
import javafx.scene.Node;
import javafx.scene.layout.PaneBuilder;

@Deprecated
/* loaded from: jfxrt.jar:javafx/scene/layout/PaneBuilder.class */
public class PaneBuilder<B extends PaneBuilder<B>> extends RegionBuilder<B> {
    private boolean __set;
    private Collection<? extends Node> children;

    protected PaneBuilder() {
    }

    public static PaneBuilder<?> create() {
        return new PaneBuilder<>();
    }

    public void applyTo(Pane x2) {
        super.applyTo((Region) x2);
        if (this.__set) {
            x2.getChildren().addAll(this.children);
        }
    }

    public B children(Collection<? extends Node> x2) {
        this.children = x2;
        this.__set = true;
        return this;
    }

    public B children(Node... nodeArr) {
        return (B) children(Arrays.asList(nodeArr));
    }

    @Override // javafx.scene.layout.RegionBuilder, javafx.util.Builder
    /* renamed from: build */
    public Pane build2() {
        Pane x2 = new Pane();
        applyTo(x2);
        return x2;
    }
}
