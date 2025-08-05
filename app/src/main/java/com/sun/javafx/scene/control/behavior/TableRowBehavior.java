package com.sun.javafx.scene.control.behavior;

import javafx.collections.ObservableList;
import javafx.scene.control.FocusModel;
import javafx.scene.control.TablePositionBase;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableSelectionModel;
import javafx.scene.control.TableView;

/* loaded from: jfxrt.jar:com/sun/javafx/scene/control/behavior/TableRowBehavior.class */
public class TableRowBehavior<T> extends TableRowBehaviorBase<TableRow<T>> {
    public TableRowBehavior(TableRow<T> control) {
        super(control);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.sun.javafx.scene.control.behavior.TableRowBehaviorBase, com.sun.javafx.scene.control.behavior.CellBehaviorBase
    public TableSelectionModel<T> getSelectionModel() {
        return getCellContainer().getSelectionModel();
    }

    @Override // com.sun.javafx.scene.control.behavior.TableRowBehaviorBase
    protected TablePositionBase<?> getFocusedCell() {
        return getCellContainer().getFocusModel().getFocusedCell();
    }

    @Override // com.sun.javafx.scene.control.behavior.CellBehaviorBase
    protected FocusModel<T> getFocusModel() {
        return getCellContainer().getFocusModel();
    }

    @Override // com.sun.javafx.scene.control.behavior.TableRowBehaviorBase
    protected ObservableList getVisibleLeafColumns() {
        return getCellContainer().getVisibleLeafColumns();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.sun.javafx.scene.control.behavior.CellBehaviorBase
    public TableView<T> getCellContainer() {
        return ((TableRow) getControl()).getTableView();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.sun.javafx.scene.control.behavior.CellBehaviorBase
    public void edit(TableRow<T> cell) {
    }
}
