package com.sun.javafx.scene.control.behavior;

import java.util.Collections;
import javafx.scene.Node;
import javafx.scene.control.FocusModel;
import javafx.scene.control.MultipleSelectionModel;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.input.MouseButton;

/* loaded from: jfxrt.jar:com/sun/javafx/scene/control/behavior/TreeCellBehavior.class */
public class TreeCellBehavior<T> extends CellBehaviorBase<TreeCell<T>> {
    public TreeCellBehavior(TreeCell<T> control) {
        super(control, Collections.emptyList());
    }

    @Override // com.sun.javafx.scene.control.behavior.CellBehaviorBase
    protected MultipleSelectionModel<TreeItem<T>> getSelectionModel() {
        return getCellContainer().getSelectionModel();
    }

    @Override // com.sun.javafx.scene.control.behavior.CellBehaviorBase
    protected FocusModel<TreeItem<T>> getFocusModel() {
        return getCellContainer().getFocusModel();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.sun.javafx.scene.control.behavior.CellBehaviorBase
    public TreeView<T> getCellContainer() {
        return ((TreeCell) getControl()).getTreeView();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.sun.javafx.scene.control.behavior.CellBehaviorBase
    public void edit(TreeCell<T> cell) {
        TreeItem<T> treeItem = cell == null ? null : cell.getTreeItem();
        getCellContainer().edit(treeItem);
    }

    @Override // com.sun.javafx.scene.control.behavior.CellBehaviorBase
    protected void handleClicks(MouseButton mouseButton, int i2, boolean z2) {
        TreeItem<T> treeItem = ((TreeCell) getControl()).getTreeItem();
        if (mouseButton == MouseButton.PRIMARY) {
            if (i2 == 1 && z2) {
                edit((TreeCell) getControl());
                return;
            }
            if (i2 == 1) {
                edit((TreeCell) null);
                return;
            }
            if (i2 == 2 && treeItem.isLeaf()) {
                edit((TreeCell) getControl());
            } else if (i2 % 2 == 0) {
                treeItem.setExpanded(!treeItem.isExpanded());
            }
        }
    }

    @Override // com.sun.javafx.scene.control.behavior.CellBehaviorBase
    protected boolean handleDisclosureNode(double d2, double d3) {
        TreeCell treeCell = (TreeCell) getControl();
        Node disclosureNode = treeCell.getDisclosureNode();
        if (disclosureNode != null && disclosureNode.getBoundsInParent().contains(d2, d3)) {
            if (treeCell.getTreeItem() != null) {
                treeCell.getTreeItem().setExpanded(!treeCell.getTreeItem().isExpanded());
                return true;
            }
            return true;
        }
        return false;
    }
}
