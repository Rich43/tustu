package javafx.scene.control;

import javafx.scene.Node;
import javafx.scene.control.TreeCellBuilder;

@Deprecated
/* loaded from: jfxrt.jar:javafx/scene/control/TreeCellBuilder.class */
public class TreeCellBuilder<T, B extends TreeCellBuilder<T, B>> extends IndexedCellBuilder<T, B> {
    private boolean __set;
    private Node disclosureNode;

    protected TreeCellBuilder() {
    }

    public static <T> TreeCellBuilder<T, ?> create() {
        return new TreeCellBuilder<>();
    }

    public void applyTo(TreeCell<T> x2) {
        super.applyTo((Cell) x2);
        if (this.__set) {
            x2.setDisclosureNode(this.disclosureNode);
        }
    }

    public B disclosureNode(Node x2) {
        this.disclosureNode = x2;
        this.__set = true;
        return this;
    }

    @Override // javafx.scene.control.IndexedCellBuilder, javafx.scene.control.CellBuilder, javafx.util.Builder
    /* renamed from: build */
    public TreeCell<T> build2() {
        TreeCell<T> x2 = new TreeCell<>();
        applyTo((TreeCell) x2);
        return x2;
    }
}
