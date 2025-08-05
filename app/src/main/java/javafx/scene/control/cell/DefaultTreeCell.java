package javafx.scene.control.cell;

import java.lang.ref.WeakReference;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.WeakInvalidationListener;
import javafx.scene.Node;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.layout.HBox;

/* loaded from: jfxrt.jar:javafx/scene/control/cell/DefaultTreeCell.class */
class DefaultTreeCell<T> extends TreeCell<T> {
    private HBox hbox;
    private WeakReference<TreeItem<T>> treeItemRef;
    private InvalidationListener treeItemGraphicListener = observable -> {
        updateDisplay(getItem(), isEmpty());
    };
    private InvalidationListener treeItemListener = new InvalidationListener() { // from class: javafx.scene.control.cell.DefaultTreeCell.1
        @Override // javafx.beans.InvalidationListener
        public void invalidated(Observable observable) {
            TreeItem<T> treeItem = DefaultTreeCell.this.treeItemRef == null ? null : (TreeItem) DefaultTreeCell.this.treeItemRef.get();
            if (treeItem != null) {
                treeItem.graphicProperty().removeListener(DefaultTreeCell.this.weakTreeItemGraphicListener);
            }
            TreeItem<T> treeItem2 = DefaultTreeCell.this.getTreeItem();
            if (treeItem2 != null) {
                treeItem2.graphicProperty().addListener(DefaultTreeCell.this.weakTreeItemGraphicListener);
                DefaultTreeCell.this.treeItemRef = new WeakReference(treeItem2);
            }
        }
    };
    private WeakInvalidationListener weakTreeItemGraphicListener = new WeakInvalidationListener(this.treeItemGraphicListener);
    private WeakInvalidationListener weakTreeItemListener = new WeakInvalidationListener(this.treeItemListener);

    public DefaultTreeCell() {
        treeItemProperty().addListener(this.weakTreeItemListener);
        if (getTreeItem() != null) {
            getTreeItem().graphicProperty().addListener(this.weakTreeItemGraphicListener);
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    void updateDisplay(T t2, boolean empty) {
        if (t2 == 0 || empty) {
            this.hbox = null;
            setText(null);
            setGraphic(null);
            return;
        }
        TreeItem<T> treeItem = getTreeItem();
        if (treeItem != null && treeItem.getGraphic() != null) {
            if (t2 instanceof Node) {
                setText(null);
                if (this.hbox == null) {
                    this.hbox = new HBox(3.0d);
                }
                this.hbox.getChildren().setAll(treeItem.getGraphic(), (Node) t2);
                setGraphic(this.hbox);
                return;
            }
            this.hbox = null;
            setText(t2.toString());
            setGraphic(treeItem.getGraphic());
            return;
        }
        this.hbox = null;
        if (t2 instanceof Node) {
            setText(null);
            setGraphic((Node) t2);
        } else {
            setText(t2.toString());
            setGraphic(null);
        }
    }

    @Override // javafx.scene.control.Cell
    public void updateItem(T item, boolean empty) {
        super.updateItem(item, empty);
        updateDisplay(item, empty);
    }
}
