package com.sun.javafx.scene.control.behavior;

import java.util.Collections;
import java.util.List;
import javafx.collections.ObservableList;
import javafx.scene.control.Cell;
import javafx.scene.control.Control;
import javafx.scene.control.TableColumnBase;
import javafx.scene.control.TablePositionBase;
import javafx.scene.control.TableSelectionModel;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

/* loaded from: jfxrt.jar:com/sun/javafx/scene/control/behavior/TableRowBehaviorBase.class */
public abstract class TableRowBehaviorBase<T extends Cell> extends CellBehaviorBase<T> {
    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.sun.javafx.scene.control.behavior.CellBehaviorBase
    public abstract TableSelectionModel<?> getSelectionModel();

    protected abstract TablePositionBase<?> getFocusedCell();

    protected abstract ObservableList getVisibleLeafColumns();

    public TableRowBehaviorBase(T control) {
        super(control, Collections.emptyList());
    }

    @Override // com.sun.javafx.scene.control.behavior.CellBehaviorBase, com.sun.javafx.scene.control.behavior.BehaviorBase
    public void mousePressed(MouseEvent e2) {
        if (isClickPositionValid(e2.getX(), e2.getY())) {
            super.mousePressed(e2);
        }
    }

    @Override // com.sun.javafx.scene.control.behavior.CellBehaviorBase
    protected void doSelect(double x2, double y2, MouseButton button, int clickCount, boolean shiftDown, boolean shortcutDown) {
        TableSelectionModel<?> sm;
        Control table = getCellContainer();
        if (table == null || handleDisclosureNode(x2, y2) || (sm = getSelectionModel()) == null || sm.isCellSelectionEnabled()) {
            return;
        }
        int index = getIndex();
        boolean isAlreadySelected = sm.isSelected(index);
        if (clickCount == 1) {
            if (isClickPositionValid(x2, y2)) {
                if (isAlreadySelected && shortcutDown) {
                    sm.clearSelection(index);
                    return;
                }
                if (shortcutDown) {
                    sm.select(getIndex());
                    return;
                } else {
                    if (shiftDown) {
                        TablePositionBase<?> anchor = (TablePositionBase) TableCellBehavior.getAnchor(table, getFocusedCell());
                        int anchorRow = anchor.getRow();
                        selectRows(anchorRow, index);
                        return;
                    }
                    simpleSelect(button, clickCount, shortcutDown);
                    return;
                }
            }
            return;
        }
        simpleSelect(button, clickCount, shortcutDown);
    }

    @Override // com.sun.javafx.scene.control.behavior.CellBehaviorBase
    protected boolean isClickPositionValid(double x2, double y2) {
        List<TableColumnBase<T, ?>> columns = getVisibleLeafColumns();
        double width = 0.0d;
        for (int i2 = 0; i2 < columns.size(); i2++) {
            width += columns.get(i2).getWidth();
        }
        return x2 > width;
    }
}
