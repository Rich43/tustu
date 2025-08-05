package com.sun.javafx.scene.control.skin;

import com.sun.javafx.scene.control.behavior.CellBehaviorBase;
import com.sun.javafx.scene.control.behavior.TableRowBehavior;
import java.lang.ref.Reference;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.AccessibleAttribute;
import javafx.scene.Node;
import javafx.scene.control.Control;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumnBase;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;

/* loaded from: jfxrt.jar:com/sun/javafx/scene/control/skin/TableRowSkin.class */
public class TableRowSkin<T> extends TableRowSkinBase<T, TableRow<T>, CellBehaviorBase<TableRow<T>>, TableCell<T, ?>> {
    private TableView<T> tableView;
    private TableViewSkin<T> tableViewSkin;

    public TableRowSkin(TableRow<T> tableRow) {
        super(tableRow, new TableRowBehavior(tableRow));
        this.tableView = tableRow.getTableView();
        updateTableViewSkin();
        super.init(tableRow);
        registerChangeListener(tableRow.tableViewProperty(), "TABLE_VIEW");
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.sun.javafx.scene.control.skin.TableRowSkinBase, com.sun.javafx.scene.control.skin.LabeledSkinBase, com.sun.javafx.scene.control.skin.BehaviorSkinBase
    protected void handleControlPropertyChanged(String p2) {
        super.handleControlPropertyChanged(p2);
        if ("TABLE_VIEW".equals(p2)) {
            updateTableViewSkin();
            int max = this.cells.size();
            for (int i2 = 0; i2 < max; i2++) {
                Node n2 = (Node) this.cells.get(i2);
                if (n2 instanceof TableCell) {
                    ((TableCell) n2).updateTableView(((TableRow) getSkinnable()).getTableView());
                }
            }
            this.tableView = ((TableRow) getSkinnable()).getTableView();
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.sun.javafx.scene.control.skin.TableRowSkinBase
    public TableCell<T, ?> getCell(TableColumnBase tcb) {
        TableColumn tableColumn = (TableColumn) tcb;
        TableCell cell = (TableCell) tableColumn.getCellFactory().call(tableColumn);
        cell.updateTableColumn(tableColumn);
        cell.updateTableView(tableColumn.getTableView());
        cell.updateTableRow((TableRow) getSkinnable());
        return cell;
    }

    @Override // com.sun.javafx.scene.control.skin.TableRowSkinBase
    protected ObservableList<TableColumn<T, ?>> getVisibleLeafColumns() {
        return this.tableView.getVisibleLeafColumns();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.sun.javafx.scene.control.skin.TableRowSkinBase
    public void updateCell(TableCell<T, ?> cell, TableRow<T> row) {
        cell.updateTableRow(row);
    }

    @Override // com.sun.javafx.scene.control.skin.TableRowSkinBase
    protected DoubleProperty fixedCellSizeProperty() {
        return this.tableView.fixedCellSizeProperty();
    }

    @Override // com.sun.javafx.scene.control.skin.TableRowSkinBase
    protected boolean isColumnPartiallyOrFullyVisible(TableColumnBase tc) {
        if (this.tableViewSkin == null) {
            return false;
        }
        return this.tableViewSkin.isColumnPartiallyOrFullyVisible((TableColumn) tc);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.sun.javafx.scene.control.skin.TableRowSkinBase
    public TableColumn<T, ?> getTableColumnBase(TableCell<T, ?> cell) {
        return cell.getTableColumn();
    }

    @Override // com.sun.javafx.scene.control.skin.TableRowSkinBase
    protected ObjectProperty<Node> graphicProperty() {
        return null;
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.sun.javafx.scene.control.skin.TableRowSkinBase
    protected Control getVirtualFlowOwner() {
        return ((TableRow) getSkinnable()).getTableView();
    }

    /* JADX WARN: Multi-variable type inference failed */
    private void updateTableViewSkin() {
        TableView<T> tableView = ((TableRow) getSkinnable()).getTableView();
        if (tableView.getSkin() instanceof TableViewSkin) {
            this.tableViewSkin = (TableViewSkin) tableView.getSkin();
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.sun.javafx.scene.control.skin.LabeledSkinBase, javafx.scene.control.SkinBase
    protected Object queryAccessibleAttribute(AccessibleAttribute attribute, Object... parameters) {
        switch (attribute) {
            case SELECTED_ITEMS:
                List<Node> selection = new ArrayList<>();
                int index = ((TableRow) getSkinnable()).getIndex();
                Iterator<TablePosition> it = this.tableView.getSelectionModel().getSelectedCells().iterator();
                if (it.hasNext()) {
                    TablePosition<T, ?> pos = it.next();
                    if (pos.getRow() == index) {
                        Object column = pos.getTableColumn();
                        if (column == null) {
                            column = this.tableView.getVisibleLeafColumn(0);
                        }
                        TableCell<T, ?> cell = (TableCell) ((Reference) this.cellsMap.get(column)).get();
                        if (cell != null) {
                            selection.add(cell);
                        }
                    }
                    return FXCollections.observableArrayList(selection);
                }
                break;
            case CELL_AT_ROW_COLUMN:
                break;
            case FOCUS_ITEM:
                TableView.TableViewFocusModel<T> fm = this.tableView.getFocusModel();
                TablePosition<T, ?> focusedCell = fm.getFocusedCell();
                Object column2 = focusedCell.getTableColumn();
                if (column2 == null) {
                    column2 = this.tableView.getVisibleLeafColumn(0);
                }
                if (this.cellsMap.containsKey(column2)) {
                    return ((Reference) this.cellsMap.get(column2)).get();
                }
                return null;
            default:
                return super.queryAccessibleAttribute(attribute, parameters);
        }
        int colIndex = ((Integer) parameters[1]).intValue();
        Object column3 = this.tableView.getVisibleLeafColumn(colIndex);
        if (this.cellsMap.containsKey(column3)) {
            return ((Reference) this.cellsMap.get(column3)).get();
        }
        return null;
    }
}
