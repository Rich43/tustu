package javafx.scene.control;

import java.util.Arrays;
import java.util.Collection;
import javafx.scene.Node;
import javafx.scene.control.TreeItemBuilder;
import javafx.util.Builder;

@Deprecated
/* loaded from: jfxrt.jar:javafx/scene/control/TreeItemBuilder.class */
public class TreeItemBuilder<T, B extends TreeItemBuilder<T, B>> implements Builder<TreeItem<T>> {
    private int __set;
    private Collection<? extends TreeItem<T>> children;
    private boolean expanded;
    private Node graphic;
    private T value;

    protected TreeItemBuilder() {
    }

    public static <T> TreeItemBuilder<T, ?> create() {
        return new TreeItemBuilder<>();
    }

    public void applyTo(TreeItem<T> x2) {
        int set = this.__set;
        if ((set & 1) != 0) {
            x2.getChildren().addAll(this.children);
        }
        if ((set & 2) != 0) {
            x2.setExpanded(this.expanded);
        }
        if ((set & 4) != 0) {
            x2.setGraphic(this.graphic);
        }
        if ((set & 8) != 0) {
            x2.setValue(this.value);
        }
    }

    public B children(Collection<? extends TreeItem<T>> x2) {
        this.children = x2;
        this.__set |= 1;
        return this;
    }

    public B children(TreeItem<T>... treeItemArr) {
        return (B) children(Arrays.asList(treeItemArr));
    }

    public B expanded(boolean x2) {
        this.expanded = x2;
        this.__set |= 2;
        return this;
    }

    public B graphic(Node x2) {
        this.graphic = x2;
        this.__set |= 4;
        return this;
    }

    public B value(T x2) {
        this.value = x2;
        this.__set |= 8;
        return this;
    }

    @Override // javafx.util.Builder
    /* renamed from: build */
    public TreeItem<T> build2() {
        TreeItem<T> x2 = new TreeItem<>();
        applyTo(x2);
        return x2;
    }
}
