package javafx.scene.control;

import java.lang.ref.WeakReference;
import javafx.beans.NamedArg;

/* loaded from: jfxrt.jar:javafx/scene/control/TreeTablePosition.class */
public class TreeTablePosition<S, T> extends TablePositionBase<TreeTableColumn<S, T>> {
    private final WeakReference<TreeTableView<S>> controlRef;
    private final WeakReference<TreeItem<S>> treeItemRef;
    int fixedColumnIndex;
    private final int nonFixedColumnIndex;

    /* JADX WARN: Multi-variable type inference failed */
    public TreeTablePosition(@NamedArg("treeTableView") TreeTableView<S> treeTableView, @NamedArg("row") int row, @NamedArg("tableColumn") TreeTableColumn<S, T> treeTableColumn) {
        super(row, treeTableColumn);
        this.fixedColumnIndex = -1;
        this.controlRef = new WeakReference<>(treeTableView);
        this.treeItemRef = new WeakReference<>(treeTableView.getTreeItem(row));
        this.nonFixedColumnIndex = (treeTableView == null || treeTableColumn == 0) ? -1 : treeTableView.getVisibleLeafIndex(treeTableColumn);
    }

    TreeTablePosition(@NamedArg("treeTableView") TreeTablePosition<S, T> pos, @NamedArg("row") int row) {
        super(row, pos.getTableColumn());
        this.fixedColumnIndex = -1;
        this.controlRef = new WeakReference<>(pos.getTreeTableView());
        this.treeItemRef = new WeakReference<>(pos.getTreeItem());
        this.nonFixedColumnIndex = pos.getColumn();
    }

    @Override // javafx.scene.control.TablePositionBase
    public int getColumn() {
        if (this.fixedColumnIndex > -1) {
            return this.fixedColumnIndex;
        }
        return this.nonFixedColumnIndex;
    }

    public final TreeTableView<S> getTreeTableView() {
        return this.controlRef.get();
    }

    @Override // javafx.scene.control.TablePositionBase
    public final TreeTableColumn<S, T> getTableColumn() {
        return (TreeTableColumn) super.getTableColumn();
    }

    public final TreeItem<S> getTreeItem() {
        return this.treeItemRef.get();
    }
}
