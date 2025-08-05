package com.sun.javafx.scene.control.behavior;

import javafx.collections.ObservableList;
import javafx.scene.control.TableFocusModel;
import javafx.scene.control.TablePositionBase;
import javafx.scene.control.TableSelectionModel;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableRow;
import javafx.scene.control.TreeTableView;
import javafx.scene.input.MouseButton;

/* loaded from: jfxrt.jar:com/sun/javafx/scene/control/behavior/TreeTableRowBehavior.class */
public class TreeTableRowBehavior<T> extends TableRowBehaviorBase<TreeTableRow<T>> {
    public TreeTableRowBehavior(TreeTableRow<T> control) {
        super(control);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.sun.javafx.scene.control.behavior.TableRowBehaviorBase, com.sun.javafx.scene.control.behavior.CellBehaviorBase
    public TableSelectionModel<TreeItem<T>> getSelectionModel() {
        return getCellContainer().getSelectionModel();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.sun.javafx.scene.control.behavior.CellBehaviorBase
    public TableFocusModel<TreeItem<T>, ?> getFocusModel() {
        return getCellContainer().getFocusModel();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.sun.javafx.scene.control.behavior.CellBehaviorBase
    public TreeTableView<T> getCellContainer() {
        return ((TreeTableRow) getControl()).getTreeTableView();
    }

    @Override // com.sun.javafx.scene.control.behavior.TableRowBehaviorBase
    protected TablePositionBase<?> getFocusedCell() {
        return getCellContainer().getFocusModel().getFocusedCell();
    }

    @Override // com.sun.javafx.scene.control.behavior.TableRowBehaviorBase
    protected ObservableList getVisibleLeafColumns() {
        return getCellContainer().getVisibleLeafColumns();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.sun.javafx.scene.control.behavior.CellBehaviorBase
    public void edit(TreeTableRow<T> cell) {
    }

    @Override // com.sun.javafx.scene.control.behavior.CellBehaviorBase
    protected void handleClicks(MouseButton mouseButton, int i2, boolean z2) {
        TreeItem<T> treeItem = ((TreeTableRow) getControl()).getTreeItem();
        if (mouseButton == MouseButton.PRIMARY) {
            if (i2 == 1 && z2) {
                edit((TreeTableRow) getControl());
                return;
            }
            if (i2 == 1) {
                edit((TreeTableRow) null);
                return;
            }
            if (i2 == 2 && treeItem.isLeaf()) {
                edit((TreeTableRow) getControl());
            } else if (i2 % 2 == 0) {
                treeItem.setExpanded(!treeItem.isExpanded());
            }
        }
    }
}
