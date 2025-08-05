package javafx.scene.layout;

import javafx.scene.Node;
import javafx.scene.layout.BorderPaneBuilder;

@Deprecated
/* loaded from: jfxrt.jar:javafx/scene/layout/BorderPaneBuilder.class */
public class BorderPaneBuilder<B extends BorderPaneBuilder<B>> extends PaneBuilder<B> {
    private int __set;
    private Node bottom;
    private Node center;
    private Node left;
    private Node right;
    private Node top;

    protected BorderPaneBuilder() {
    }

    public static BorderPaneBuilder<?> create() {
        return new BorderPaneBuilder<>();
    }

    public void applyTo(BorderPane x2) {
        super.applyTo((Pane) x2);
        int set = this.__set;
        if ((set & 1) != 0) {
            x2.setBottom(this.bottom);
        }
        if ((set & 2) != 0) {
            x2.setCenter(this.center);
        }
        if ((set & 4) != 0) {
            x2.setLeft(this.left);
        }
        if ((set & 8) != 0) {
            x2.setRight(this.right);
        }
        if ((set & 16) != 0) {
            x2.setTop(this.top);
        }
    }

    public B bottom(Node x2) {
        this.bottom = x2;
        this.__set |= 1;
        return this;
    }

    public B center(Node x2) {
        this.center = x2;
        this.__set |= 2;
        return this;
    }

    public B left(Node x2) {
        this.left = x2;
        this.__set |= 4;
        return this;
    }

    public B right(Node x2) {
        this.right = x2;
        this.__set |= 8;
        return this;
    }

    public B top(Node x2) {
        this.top = x2;
        this.__set |= 16;
        return this;
    }

    @Override // javafx.scene.layout.PaneBuilder, javafx.scene.layout.RegionBuilder, javafx.util.Builder
    /* renamed from: build */
    public BorderPane build2() {
        BorderPane x2 = new BorderPane();
        applyTo(x2);
        return x2;
    }
}
