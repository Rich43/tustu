package com.sun.javafx.scene.control.behavior;

import com.sun.javafx.scene.control.skin.Utils;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.WeakChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumnBase;
import javafx.scene.control.TableFocusModel;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TablePositionBase;
import javafx.scene.control.TableSelectionModel;
import javafx.scene.control.TableView;

/* loaded from: jfxrt.jar:com/sun/javafx/scene/control/behavior/TableViewBehavior.class */
public class TableViewBehavior<T> extends TableViewBehaviorBase<TableView<T>, T, TableColumn<T, ?>> {
    private final ChangeListener<TableView.TableViewSelectionModel<T>> selectionModelListener;
    private final WeakChangeListener<TableView.TableViewSelectionModel<T>> weakSelectionModelListener;
    private TwoLevelFocusBehavior tlFocus;

    public TableViewBehavior(TableView<T> control) {
        super(control);
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
        TableView.TableViewSelectionModel<T> sm = control.getSelectionModel();
        if (sm != null) {
            sm.getSelectedCells().addListener(this.selectedCellsListener);
        }
        if (Utils.isTwoLevelFocus()) {
            this.tlFocus = new TwoLevelFocusBehavior(control);
        }
    }

    @Override // com.sun.javafx.scene.control.behavior.BehaviorBase
    public void dispose() {
        if (this.tlFocus != null) {
            this.tlFocus.dispose();
        }
        super.dispose();
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.sun.javafx.scene.control.behavior.TableViewBehaviorBase
    protected int getItemCount() {
        if (((TableView) getControl()).getItems() == null) {
            return 0;
        }
        return ((TableView) getControl()).getItems().size();
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.sun.javafx.scene.control.behavior.TableViewBehaviorBase
    protected TableFocusModel getFocusModel() {
        return ((TableView) getControl()).getFocusModel();
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.sun.javafx.scene.control.behavior.TableViewBehaviorBase
    protected TableSelectionModel<T> getSelectionModel() {
        return ((TableView) getControl()).getSelectionModel();
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.sun.javafx.scene.control.behavior.TableViewBehaviorBase
    protected ObservableList<TablePosition> getSelectedCells() {
        return ((TableView) getControl()).getSelectionModel().getSelectedCells();
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.sun.javafx.scene.control.behavior.TableViewBehaviorBase
    protected TablePositionBase getFocusedCell() {
        return ((TableView) getControl()).getFocusModel().getFocusedCell();
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.sun.javafx.scene.control.behavior.TableViewBehaviorBase
    protected int getVisibleLeafIndex(TableColumnBase tc) {
        return ((TableView) getControl()).getVisibleLeafIndex((TableColumn) tc);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.sun.javafx.scene.control.behavior.TableViewBehaviorBase
    public TableColumn<T, ?> getVisibleLeafColumn(int index) {
        return ((TableView) getControl()).getVisibleLeafColumn(index);
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.sun.javafx.scene.control.behavior.TableViewBehaviorBase
    protected void editCell(int row, TableColumnBase tc) {
        ((TableView) getControl()).edit(row, (TableColumn) tc);
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.sun.javafx.scene.control.behavior.TableViewBehaviorBase
    protected ObservableList<TableColumn<T, ?>> getVisibleLeafColumns() {
        return ((TableView) getControl()).getVisibleLeafColumns();
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.sun.javafx.scene.control.behavior.TableViewBehaviorBase
    protected TablePositionBase<TableColumn<T, ?>> getTablePosition(int row, TableColumnBase<T, ?> tc) {
        return new TablePosition((TableView) getControl(), row, (TableColumn) tc);
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.sun.javafx.scene.control.behavior.TableViewBehaviorBase
    protected void selectAllToFocus(boolean setAnchorToFocusIndex) {
        if (((TableView) getControl()).getEditingCell() != null) {
            return;
        }
        super.selectAllToFocus(setAnchorToFocusIndex);
    }
}
