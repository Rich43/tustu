package com.sun.javafx.scene.control.behavior;

import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumnBase;
import javafx.scene.control.TablePositionBase;
import javafx.scene.control.TableView;

/* loaded from: jfxrt.jar:com/sun/javafx/scene/control/behavior/TableCellBehavior.class */
public class TableCellBehavior<S, T> extends TableCellBehaviorBase<S, T, TableColumn<S, ?>, TableCell<S, T>> {
    public TableCellBehavior(TableCell<S, T> control) {
        super(control);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.sun.javafx.scene.control.behavior.CellBehaviorBase
    public TableView<S> getCellContainer() {
        return ((TableCell) getControl()).getTableView();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.sun.javafx.scene.control.behavior.TableCellBehaviorBase
    public TableColumn<S, T> getTableColumn() {
        return ((TableCell) getControl()).getTableColumn();
    }

    @Override // com.sun.javafx.scene.control.behavior.TableCellBehaviorBase
    protected int getItemCount() {
        return getCellContainer().getItems().size();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.sun.javafx.scene.control.behavior.TableCellBehaviorBase, com.sun.javafx.scene.control.behavior.CellBehaviorBase
    public TableView.TableViewSelectionModel<S> getSelectionModel() {
        return getCellContainer().getSelectionModel();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.sun.javafx.scene.control.behavior.TableCellBehaviorBase, com.sun.javafx.scene.control.behavior.CellBehaviorBase
    public TableView.TableViewFocusModel<S> getFocusModel() {
        return getCellContainer().getFocusModel();
    }

    @Override // com.sun.javafx.scene.control.behavior.TableCellBehaviorBase
    protected TablePositionBase getFocusedCell() {
        return getCellContainer().getFocusModel().getFocusedCell();
    }

    @Override // com.sun.javafx.scene.control.behavior.TableCellBehaviorBase
    protected boolean isTableRowSelected() {
        return ((TableCell) getControl()).getTableRow().isSelected();
    }

    @Override // com.sun.javafx.scene.control.behavior.TableCellBehaviorBase
    protected int getVisibleLeafIndex(TableColumnBase tc) {
        return getCellContainer().getVisibleLeafIndex((TableColumn) tc);
    }

    @Override // com.sun.javafx.scene.control.behavior.TableCellBehaviorBase
    protected void focus(int row, TableColumnBase tc) {
        getFocusModel().focus(row, (TableColumn) tc);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.sun.javafx.scene.control.behavior.CellBehaviorBase
    public void edit(TableCell<S, T> cell) {
        if (cell == null) {
            getCellContainer().edit(-1, null);
        } else {
            getCellContainer().edit(cell.getIndex(), cell.getTableColumn());
        }
    }
}
