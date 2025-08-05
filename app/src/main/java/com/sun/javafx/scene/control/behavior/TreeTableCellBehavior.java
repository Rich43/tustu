package com.sun.javafx.scene.control.behavior;

import java.util.Iterator;
import javafx.scene.Node;
import javafx.scene.control.TableColumnBase;
import javafx.scene.control.TablePositionBase;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableCell;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableView;
import javafx.scene.input.MouseButton;

/* loaded from: jfxrt.jar:com/sun/javafx/scene/control/behavior/TreeTableCellBehavior.class */
public class TreeTableCellBehavior<S, T> extends TableCellBehaviorBase<TreeItem<S>, T, TreeTableColumn<S, ?>, TreeTableCell<S, T>> {
    public TreeTableCellBehavior(TreeTableCell<S, T> control) {
        super(control);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.sun.javafx.scene.control.behavior.CellBehaviorBase
    public TreeTableView<S> getCellContainer() {
        return ((TreeTableCell) getControl()).getTreeTableView();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.sun.javafx.scene.control.behavior.TableCellBehaviorBase
    public TreeTableColumn<S, T> getTableColumn() {
        return ((TreeTableCell) getControl()).getTableColumn();
    }

    @Override // com.sun.javafx.scene.control.behavior.TableCellBehaviorBase
    protected int getItemCount() {
        return getCellContainer().getExpandedItemCount();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.sun.javafx.scene.control.behavior.TableCellBehaviorBase, com.sun.javafx.scene.control.behavior.CellBehaviorBase
    public TreeTableView.TreeTableViewSelectionModel<S> getSelectionModel() {
        return getCellContainer().getSelectionModel();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.sun.javafx.scene.control.behavior.TableCellBehaviorBase, com.sun.javafx.scene.control.behavior.CellBehaviorBase
    public TreeTableView.TreeTableViewFocusModel<S> getFocusModel() {
        return getCellContainer().getFocusModel();
    }

    @Override // com.sun.javafx.scene.control.behavior.TableCellBehaviorBase
    protected TablePositionBase getFocusedCell() {
        return getCellContainer().getFocusModel().getFocusedCell();
    }

    @Override // com.sun.javafx.scene.control.behavior.TableCellBehaviorBase
    protected boolean isTableRowSelected() {
        return ((TreeTableCell) getControl()).getTreeTableRow().isSelected();
    }

    @Override // com.sun.javafx.scene.control.behavior.TableCellBehaviorBase
    protected int getVisibleLeafIndex(TableColumnBase tc) {
        return getCellContainer().getVisibleLeafIndex((TreeTableColumn) tc);
    }

    @Override // com.sun.javafx.scene.control.behavior.TableCellBehaviorBase
    protected void focus(int row, TableColumnBase tc) {
        getFocusModel().focus(row, (TreeTableColumn) tc);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.sun.javafx.scene.control.behavior.CellBehaviorBase
    public void edit(TreeTableCell<S, T> cell) {
        if (cell == null) {
            getCellContainer().edit(-1, null);
        } else {
            getCellContainer().edit(cell.getIndex(), cell.getTableColumn());
        }
    }

    @Override // com.sun.javafx.scene.control.behavior.CellBehaviorBase
    protected boolean handleDisclosureNode(double d2, double d3) {
        Node disclosureNode;
        TreeTableColumn<S, ?> next;
        TreeItem<S> treeItem = ((TreeTableCell) getControl()).getTreeTableRow().getTreeItem();
        TreeTableView<S> treeTableView = ((TreeTableCell) getControl()).getTreeTableView();
        TreeTableColumn<S, T> tableColumn = getTableColumn();
        TreeTableColumn<S, T> visibleLeafColumn = treeTableView.getTreeColumn() == null ? treeTableView.getVisibleLeafColumn(0) : treeTableView.getTreeColumn();
        if (tableColumn == visibleLeafColumn && (disclosureNode = ((TreeTableCell) getControl()).getTreeTableRow().getDisclosureNode()) != null) {
            double width = 0.0d;
            Iterator<TreeTableColumn<S, ?>> it = treeTableView.getVisibleLeafColumns().iterator();
            while (it.hasNext() && (next = it.next()) != visibleLeafColumn) {
                width += next.getWidth();
            }
            if (d2 < disclosureNode.getBoundsInParent().getMaxX() - width) {
                if (treeItem != null) {
                    treeItem.setExpanded(!treeItem.isExpanded());
                    return true;
                }
                return true;
            }
            return false;
        }
        return false;
    }

    @Override // com.sun.javafx.scene.control.behavior.CellBehaviorBase
    protected void handleClicks(MouseButton mouseButton, int i2, boolean z2) {
        TreeItem<S> treeItem = ((TreeTableCell) getControl()).getTreeTableRow().getTreeItem();
        if (mouseButton == MouseButton.PRIMARY) {
            if (i2 == 1 && z2) {
                edit((TreeTableCell) getControl());
                return;
            }
            if (i2 == 1) {
                edit((TreeTableCell) null);
                return;
            }
            if (i2 == 2 && treeItem.isLeaf()) {
                edit((TreeTableCell) getControl());
            } else if (i2 % 2 == 0) {
                treeItem.setExpanded(!treeItem.isExpanded());
            }
        }
    }
}
