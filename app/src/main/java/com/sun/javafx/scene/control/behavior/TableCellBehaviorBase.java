package com.sun.javafx.scene.control.behavior;

import java.util.Collections;
import javafx.scene.control.Control;
import javafx.scene.control.IndexedCell;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumnBase;
import javafx.scene.control.TableFocusModel;
import javafx.scene.control.TablePositionBase;
import javafx.scene.control.TableSelectionModel;
import javafx.scene.input.MouseButton;

/* loaded from: jfxrt.jar:com/sun/javafx/scene/control/behavior/TableCellBehaviorBase.class */
public abstract class TableCellBehaviorBase<S, T, TC extends TableColumnBase<S, ?>, C extends IndexedCell<T>> extends CellBehaviorBase<C> {
    protected abstract TableColumnBase<S, T> getTableColumn();

    protected abstract int getItemCount();

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.sun.javafx.scene.control.behavior.CellBehaviorBase
    public abstract TableSelectionModel<S> getSelectionModel();

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.sun.javafx.scene.control.behavior.CellBehaviorBase
    public abstract TableFocusModel<S, TC> getFocusModel();

    protected abstract TablePositionBase getFocusedCell();

    protected abstract boolean isTableRowSelected();

    protected abstract int getVisibleLeafIndex(TableColumnBase<S, T> tableColumnBase);

    protected abstract void focus(int i2, TableColumnBase<S, T> tableColumnBase);

    public TableCellBehaviorBase(C control) {
        super(control, Collections.emptyList());
    }

    @Override // com.sun.javafx.scene.control.behavior.CellBehaviorBase
    protected void doSelect(double d2, double d3, MouseButton mouseButton, int i2, boolean z2, boolean z3) {
        Control cellContainer;
        TableSelectionModel<S> selectionModel;
        IndexedCell indexedCell = (IndexedCell) getControl();
        if (indexedCell.contains(d2, d3) && (cellContainer = getCellContainer()) != null) {
            if (indexedCell.getIndex() < getItemCount() && (selectionModel = getSelectionModel()) != null) {
                boolean zIsSelected = isSelected();
                int index = indexedCell.getIndex();
                int column = getColumn();
                TableColumnBase<S, T> tableColumn = getTableColumn();
                TableFocusModel<S, TC> focusModel = getFocusModel();
                if (focusModel == null) {
                    return;
                }
                TablePositionBase focusedCell = getFocusedCell();
                if (handleDisclosureNode(d2, d3)) {
                    return;
                }
                if (z2) {
                    if (!hasNonDefaultAnchor(cellContainer)) {
                        setAnchor(cellContainer, focusedCell, false);
                    }
                } else {
                    removeAnchor(cellContainer);
                }
                if (mouseButton == MouseButton.PRIMARY || (mouseButton == MouseButton.SECONDARY && !zIsSelected)) {
                    if (selectionModel.getSelectionMode() == SelectionMode.SINGLE) {
                        simpleSelect(mouseButton, i2, z3);
                        return;
                    }
                    if (z3) {
                        if (zIsSelected) {
                            selectionModel.clearSelection(index, tableColumn);
                            focusModel.focus(index, tableColumn);
                            return;
                        } else {
                            selectionModel.select(index, tableColumn);
                            return;
                        }
                    }
                    if (z2) {
                        TablePositionBase tablePositionBase = (TablePositionBase) getAnchor(cellContainer, focusedCell);
                        int row = tablePositionBase.getRow();
                        boolean z4 = row < index;
                        selectionModel.clearSelection();
                        int iMin = Math.min(row, index);
                        int iMax = Math.max(row, index);
                        TableColumnBase<S, T> tableColumn2 = tablePositionBase.getColumn() < column ? tablePositionBase.getTableColumn() : tableColumn;
                        TableColumnBase<S, T> tableColumn3 = tablePositionBase.getColumn() >= column ? tablePositionBase.getTableColumn() : tableColumn;
                        if (z4) {
                            selectionModel.selectRange(iMin, tableColumn2, iMax, tableColumn3);
                            return;
                        } else {
                            selectionModel.selectRange(iMax, tableColumn2, iMin, tableColumn3);
                            return;
                        }
                    }
                    simpleSelect(mouseButton, i2, z3);
                }
            }
        }
    }

    @Override // com.sun.javafx.scene.control.behavior.CellBehaviorBase
    protected void simpleSelect(MouseButton mouseButton, int i2, boolean z2) {
        TableSelectionModel<S> selectionModel = getSelectionModel();
        int index = ((IndexedCell) getControl()).getIndex();
        TableColumnBase<S, T> tableColumn = getTableColumn();
        boolean zIsSelected = selectionModel.isSelected(index, tableColumn);
        if (zIsSelected && z2) {
            selectionModel.clearSelection(index, tableColumn);
            getFocusModel().focus(index, tableColumn);
            zIsSelected = false;
        } else {
            selectionModel.clearAndSelect(index, tableColumn);
        }
        handleClicks(mouseButton, i2, zIsSelected);
    }

    private int getColumn() {
        if (getSelectionModel().isCellSelectionEnabled()) {
            TableColumnBase<S, T> tc = getTableColumn();
            return getVisibleLeafIndex(tc);
        }
        return -1;
    }

    @Override // com.sun.javafx.scene.control.behavior.CellBehaviorBase
    protected boolean isSelected() {
        TableSelectionModel<S> selectionModel = getSelectionModel();
        if (selectionModel == null) {
            return false;
        }
        if (selectionModel.isCellSelectionEnabled()) {
            return ((IndexedCell) getControl()).isSelected();
        }
        return isTableRowSelected();
    }
}
