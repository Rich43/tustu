package javafx.scene.control;

import java.util.Arrays;
import java.util.Collection;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.control.SplitPaneBuilder;
import javafx.util.Builder;

@Deprecated
/* loaded from: jfxrt.jar:javafx/scene/control/SplitPaneBuilder.class */
public class SplitPaneBuilder<B extends SplitPaneBuilder<B>> extends ControlBuilder<B> implements Builder<SplitPane> {
    private int __set;
    private double[] dividerPositions;
    private Collection<? extends Node> items;
    private Orientation orientation;

    protected SplitPaneBuilder() {
    }

    public static SplitPaneBuilder<?> create() {
        return new SplitPaneBuilder<>();
    }

    public void applyTo(SplitPane x2) {
        super.applyTo((Control) x2);
        int set = this.__set;
        if ((set & 1) != 0) {
            x2.setDividerPositions(this.dividerPositions);
        }
        if ((set & 2) != 0) {
            x2.getItems().addAll(this.items);
        }
        if ((set & 4) != 0) {
            x2.setOrientation(this.orientation);
        }
    }

    public B dividerPositions(double[] x2) {
        this.dividerPositions = x2;
        this.__set |= 1;
        return this;
    }

    public B items(Collection<? extends Node> x2) {
        this.items = x2;
        this.__set |= 2;
        return this;
    }

    public B items(Node... nodeArr) {
        return (B) items(Arrays.asList(nodeArr));
    }

    public B orientation(Orientation x2) {
        this.orientation = x2;
        this.__set |= 4;
        return this;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // javafx.util.Builder
    /* renamed from: build */
    public SplitPane build2() {
        SplitPane x2 = new SplitPane();
        applyTo(x2);
        return x2;
    }
}
