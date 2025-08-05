package com.sun.javafx.scene.control.behavior;

import java.util.Collections;
import javafx.scene.control.FocusModel;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.MultipleSelectionModel;

/* loaded from: jfxrt.jar:com/sun/javafx/scene/control/behavior/ListCellBehavior.class */
public class ListCellBehavior<T> extends CellBehaviorBase<ListCell<T>> {
    public ListCellBehavior(ListCell<T> control) {
        super(control, Collections.emptyList());
    }

    @Override // com.sun.javafx.scene.control.behavior.CellBehaviorBase
    protected MultipleSelectionModel<T> getSelectionModel() {
        return getCellContainer().getSelectionModel();
    }

    @Override // com.sun.javafx.scene.control.behavior.CellBehaviorBase
    protected FocusModel<T> getFocusModel() {
        return getCellContainer().getFocusModel();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.sun.javafx.scene.control.behavior.CellBehaviorBase
    public ListView<T> getCellContainer() {
        return ((ListCell) getControl()).getListView();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.sun.javafx.scene.control.behavior.CellBehaviorBase
    public void edit(ListCell<T> cell) {
        int index = cell == null ? -1 : cell.getIndex();
        getCellContainer().edit(index);
    }
}
