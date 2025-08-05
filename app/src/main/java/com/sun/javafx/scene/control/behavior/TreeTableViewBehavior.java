package com.sun.javafx.scene.control.behavior;

import java.util.ArrayList;
import java.util.List;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.WeakChangeListener;
import javafx.collections.ObservableList;
import javafx.geometry.NodeOrientation;
import javafx.scene.control.TableColumnBase;
import javafx.scene.control.TableFocusModel;
import javafx.scene.control.TablePositionBase;
import javafx.scene.control.TableSelectionModel;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTablePosition;
import javafx.scene.control.TreeTableView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.util.Callback;

/* loaded from: jfxrt.jar:com/sun/javafx/scene/control/behavior/TreeTableViewBehavior.class */
public class TreeTableViewBehavior<T> extends TableViewBehaviorBase<TreeTableView<T>, TreeItem<T>, TreeTableColumn<T, ?>> {
    protected static final List<KeyBinding> TREE_TABLE_VIEW_BINDINGS = new ArrayList();
    private final ChangeListener<TreeTableView.TreeTableViewSelectionModel<T>> selectionModelListener;
    private final WeakChangeListener<TreeTableView.TreeTableViewSelectionModel<T>> weakSelectionModelListener;

    static {
        TREE_TABLE_VIEW_BINDINGS.add(new KeyBinding(KeyCode.LEFT, "CollapseRow"));
        TREE_TABLE_VIEW_BINDINGS.add(new KeyBinding(KeyCode.KP_LEFT, "CollapseRow"));
        TREE_TABLE_VIEW_BINDINGS.add(new KeyBinding(KeyCode.RIGHT, "ExpandRow"));
        TREE_TABLE_VIEW_BINDINGS.add(new KeyBinding(KeyCode.KP_RIGHT, "ExpandRow"));
        TREE_TABLE_VIEW_BINDINGS.add(new KeyBinding(KeyCode.MULTIPLY, "ExpandAll"));
        TREE_TABLE_VIEW_BINDINGS.add(new KeyBinding(KeyCode.ADD, "ExpandRow"));
        TREE_TABLE_VIEW_BINDINGS.add(new KeyBinding(KeyCode.SUBTRACT, "CollapseRow"));
        TREE_TABLE_VIEW_BINDINGS.addAll(TABLE_VIEW_BINDINGS);
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.sun.javafx.scene.control.behavior.BehaviorBase
    protected String matchActionForEvent(KeyEvent e2) {
        String action = super.matchActionForEvent(e2);
        if (((TreeTableView) getControl()).getEffectiveNodeOrientation() == NodeOrientation.RIGHT_TO_LEFT) {
            if ("CollapseRow".equals(action) && (e2.getCode() == KeyCode.LEFT || e2.getCode() == KeyCode.KP_LEFT)) {
                action = "ExpandRow";
            } else if ("ExpandRow".equals(action) && (e2.getCode() == KeyCode.RIGHT || e2.getCode() == KeyCode.KP_RIGHT)) {
                action = "CollapseRow";
            }
        }
        return action;
    }

    @Override // com.sun.javafx.scene.control.behavior.TableViewBehaviorBase, com.sun.javafx.scene.control.behavior.BehaviorBase
    protected void callAction(String name) {
        if (!"ExpandRow".equals(name)) {
            if (!"CollapseRow".equals(name)) {
                if (!"ExpandAll".equals(name)) {
                    super.callAction(name);
                    return;
                } else {
                    expandAll();
                    return;
                }
            }
            leftArrowPressed();
            return;
        }
        rightArrowPressed();
    }

    public TreeTableViewBehavior(TreeTableView<T> control) {
        super(control, TREE_TABLE_VIEW_BINDINGS);
        this.selectionModelListener = (observable, oldValue, newValue) -> {
            if (oldValue != null) {
                oldValue.getSelectedCells().removeListener(this.weakSelectedCellsListener);
            }
            if (newValue != null) {
                newValue.getSelectedCells().addListener(this.weakSelectedCellsListener);
            }
        };
        this.weakSelectionModelListener = new WeakChangeListener<>(this.selectionModelListener);
        control.selectionModelProperty().addListener(this.weakSelectionModelListener);
        if (getSelectionModel() != null) {
            control.getSelectionModel().getSelectedCells().addListener(this.selectedCellsListener);
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.sun.javafx.scene.control.behavior.TableViewBehaviorBase
    protected int getItemCount() {
        return ((TreeTableView) getControl()).getExpandedItemCount();
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.sun.javafx.scene.control.behavior.TableViewBehaviorBase
    protected TableFocusModel getFocusModel() {
        return ((TreeTableView) getControl()).getFocusModel();
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.sun.javafx.scene.control.behavior.TableViewBehaviorBase
    protected TableSelectionModel<TreeItem<T>> getSelectionModel() {
        return ((TreeTableView) getControl()).getSelectionModel();
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.sun.javafx.scene.control.behavior.TableViewBehaviorBase
    protected ObservableList<TreeTablePosition<T, ?>> getSelectedCells() {
        return ((TreeTableView) getControl()).getSelectionModel().getSelectedCells();
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.sun.javafx.scene.control.behavior.TableViewBehaviorBase
    protected TablePositionBase getFocusedCell() {
        return ((TreeTableView) getControl()).getFocusModel().getFocusedCell();
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.sun.javafx.scene.control.behavior.TableViewBehaviorBase
    protected int getVisibleLeafIndex(TableColumnBase tc) {
        return ((TreeTableView) getControl()).getVisibleLeafIndex((TreeTableColumn) tc);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.sun.javafx.scene.control.behavior.TableViewBehaviorBase
    public TreeTableColumn getVisibleLeafColumn(int index) {
        return ((TreeTableView) getControl()).getVisibleLeafColumn(index);
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.sun.javafx.scene.control.behavior.TableViewBehaviorBase
    protected void editCell(int row, TableColumnBase tc) {
        ((TreeTableView) getControl()).edit(row, (TreeTableColumn) tc);
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.sun.javafx.scene.control.behavior.TableViewBehaviorBase
    protected ObservableList<TreeTableColumn<T, ?>> getVisibleLeafColumns() {
        return ((TreeTableView) getControl()).getVisibleLeafColumns();
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.sun.javafx.scene.control.behavior.TableViewBehaviorBase
    protected TablePositionBase<TreeTableColumn<T, ?>> getTablePosition(int row, TableColumnBase<TreeItem<T>, ?> tc) {
        return new TreeTablePosition((TreeTableView) getControl(), row, (TreeTableColumn) tc);
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.sun.javafx.scene.control.behavior.TableViewBehaviorBase
    protected void selectAllToFocus(boolean setAnchorToFocusIndex) {
        if (((TreeTableView) getControl()).getEditingCell() != null) {
            return;
        }
        super.selectAllToFocus(setAnchorToFocusIndex);
    }

    /* JADX WARN: Multi-variable type inference failed */
    private void rightArrowPressed() {
        if (((TreeTableView) getControl()).getSelectionModel().isCellSelectionEnabled()) {
            if (isRTL()) {
                selectLeftCell();
                return;
            } else {
                selectRightCell();
                return;
            }
        }
        expandRow();
    }

    /* JADX WARN: Multi-variable type inference failed */
    private void leftArrowPressed() {
        if (((TreeTableView) getControl()).getSelectionModel().isCellSelectionEnabled()) {
            if (isRTL()) {
                selectRightCell();
                return;
            } else {
                selectLeftCell();
                return;
            }
        }
        collapseRow();
    }

    /* JADX WARN: Multi-variable type inference failed */
    private void expandRow() {
        Callback<TreeItem<T>, Integer> getIndex = p2 -> {
            return Integer.valueOf(((TreeTableView) getControl()).getRow(p2));
        };
        TreeViewBehavior.expandRow(((TreeTableView) getControl()).getSelectionModel(), getIndex);
    }

    /* JADX WARN: Multi-variable type inference failed */
    private void expandAll() {
        TreeViewBehavior.expandAll(((TreeTableView) getControl()).getRoot());
    }

    /* JADX WARN: Multi-variable type inference failed */
    private void collapseRow() {
        TreeTableView<T> control = (TreeTableView) getControl();
        TreeViewBehavior.collapseRow(control.getSelectionModel(), control.getRoot(), control.isShowRoot());
    }
}
