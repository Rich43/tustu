package javafx.scene.control;

import java.util.Arrays;
import java.util.Collection;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.control.ToolBarBuilder;
import javafx.util.Builder;

@Deprecated
/* loaded from: jfxrt.jar:javafx/scene/control/ToolBarBuilder.class */
public class ToolBarBuilder<B extends ToolBarBuilder<B>> extends ControlBuilder<B> implements Builder<ToolBar> {
    private int __set;
    private Collection<? extends Node> items;
    private Orientation orientation;

    protected ToolBarBuilder() {
    }

    public static ToolBarBuilder<?> create() {
        return new ToolBarBuilder<>();
    }

    public void applyTo(ToolBar x2) {
        super.applyTo((Control) x2);
        int set = this.__set;
        if ((set & 1) != 0) {
            x2.getItems().addAll(this.items);
        }
        if ((set & 2) != 0) {
            x2.setOrientation(this.orientation);
        }
    }

    public B items(Collection<? extends Node> x2) {
        this.items = x2;
        this.__set |= 1;
        return this;
    }

    public B items(Node... nodeArr) {
        return (B) items(Arrays.asList(nodeArr));
    }

    public B orientation(Orientation x2) {
        this.orientation = x2;
        this.__set |= 2;
        return this;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // javafx.util.Builder
    /* renamed from: build */
    public ToolBar build2() {
        ToolBar x2 = new ToolBar();
        applyTo(x2);
        return x2;
    }
}
