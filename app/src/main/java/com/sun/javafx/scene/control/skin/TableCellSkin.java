package com.sun.javafx.scene.control.skin;

import com.sun.javafx.scene.control.behavior.TableCellBehavior;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;

/* loaded from: jfxrt.jar:com/sun/javafx/scene/control/skin/TableCellSkin.class */
public class TableCellSkin<S, T> extends TableCellSkinBase<TableCell<S, T>, TableCellBehavior<S, T>> {
    private final TableColumn<S, T> tableColumn;

    public TableCellSkin(TableCell<S, T> tableCell) {
        super(tableCell, new TableCellBehavior(tableCell));
        this.tableColumn = tableCell.getTableColumn();
        super.init(tableCell);
    }

    @Override // com.sun.javafx.scene.control.skin.TableCellSkinBase
    protected BooleanProperty columnVisibleProperty() {
        return this.tableColumn.visibleProperty();
    }

    @Override // com.sun.javafx.scene.control.skin.TableCellSkinBase
    protected ReadOnlyDoubleProperty columnWidthProperty() {
        return this.tableColumn.widthProperty();
    }
}
