package javafx.scene.control;

import javafx.scene.Node;
import javafx.scene.control.TitledPaneBuilder;
import javafx.util.Builder;

@Deprecated
/* loaded from: jfxrt.jar:javafx/scene/control/TitledPaneBuilder.class */
public class TitledPaneBuilder<B extends TitledPaneBuilder<B>> extends LabeledBuilder<B> implements Builder<TitledPane> {
    private int __set;
    private boolean animated;
    private boolean collapsible;
    private Node content;
    private boolean expanded;

    protected TitledPaneBuilder() {
    }

    public static TitledPaneBuilder<?> create() {
        return new TitledPaneBuilder<>();
    }

    public void applyTo(TitledPane x2) {
        super.applyTo((Labeled) x2);
        int set = this.__set;
        if ((set & 1) != 0) {
            x2.setAnimated(this.animated);
        }
        if ((set & 2) != 0) {
            x2.setCollapsible(this.collapsible);
        }
        if ((set & 4) != 0) {
            x2.setContent(this.content);
        }
        if ((set & 8) != 0) {
            x2.setExpanded(this.expanded);
        }
    }

    public B animated(boolean x2) {
        this.animated = x2;
        this.__set |= 1;
        return this;
    }

    public B collapsible(boolean x2) {
        this.collapsible = x2;
        this.__set |= 2;
        return this;
    }

    public B content(Node x2) {
        this.content = x2;
        this.__set |= 4;
        return this;
    }

    public B expanded(boolean x2) {
        this.expanded = x2;
        this.__set |= 8;
        return this;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // javafx.util.Builder
    /* renamed from: build */
    public TitledPane build2() {
        TitledPane x2 = new TitledPane();
        applyTo(x2);
        return x2;
    }
}
