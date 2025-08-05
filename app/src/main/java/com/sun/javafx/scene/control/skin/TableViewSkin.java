package com.sun.javafx.scene.control.skin;

import com.sun.javafx.scene.control.behavior.TableViewBehavior;
import java.util.ArrayList;
import java.util.List;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.AccessibleAction;
import javafx.scene.AccessibleAttribute;
import javafx.scene.Node;
import javafx.scene.control.ResizeFeaturesBase;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableSelectionModel;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Region;
import javafx.util.Callback;

/* loaded from: jfxrt.jar:com/sun/javafx/scene/control/skin/TableViewSkin.class */
public class TableViewSkin<T> extends TableViewSkinBase<T, T, TableView<T>, TableViewBehavior<T>, TableRow<T>, TableColumn<T, ?>> {
    private final TableView<T> tableView;

    /* JADX WARN: Multi-variable type inference failed */
    public TableViewSkin(TableView<T> tableView) {
        super(tableView, new TableViewBehavior(tableView));
        this.tableView = tableView;
        this.flow.setFixedCellSize(tableView.getFixedCellSize());
        super.init(tableView);
        EventHandler<MouseEvent> ml = event -> {
            if (tableView.getEditingCell() != null) {
                tableView.edit(-1, null);
            }
            if (tableView.isFocusTraversable()) {
                tableView.requestFocus();
            }
        };
        this.flow.getVbar().addEventFilter(MouseEvent.MOUSE_PRESSED, ml);
        this.flow.getHbar().addEventFilter(MouseEvent.MOUSE_PRESSED, ml);
        TableViewBehavior<T> behavior = (TableViewBehavior) getBehavior();
        behavior.setOnFocusPreviousRow(() -> {
            onFocusPreviousCell();
        });
        behavior.setOnFocusNextRow(() -> {
            onFocusNextCell();
        });
        behavior.setOnMoveToFirstCell(() -> {
            onMoveToFirstCell();
        });
        behavior.setOnMoveToLastCell(() -> {
            onMoveToLastCell();
        });
        behavior.setOnScrollPageDown(isFocusDriven -> {
            return Integer.valueOf(onScrollPageDown(isFocusDriven.booleanValue()));
        });
        behavior.setOnScrollPageUp(isFocusDriven2 -> {
            return Integer.valueOf(onScrollPageUp(isFocusDriven2.booleanValue()));
        });
        behavior.setOnSelectPreviousRow(() -> {
            onSelectPreviousCell();
        });
        behavior.setOnSelectNextRow(() -> {
            onSelectNextCell();
        });
        behavior.setOnSelectLeftCell(() -> {
            onSelectLeftCell();
        });
        behavior.setOnSelectRightCell(() -> {
            onSelectRightCell();
        });
        registerChangeListener(tableView.fixedCellSizeProperty(), "FIXED_CELL_SIZE");
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.sun.javafx.scene.control.skin.TableViewSkinBase, com.sun.javafx.scene.control.skin.BehaviorSkinBase
    protected void handleControlPropertyChanged(String p2) {
        super.handleControlPropertyChanged(p2);
        if ("FIXED_CELL_SIZE".equals(p2)) {
            this.flow.setFixedCellSize(((TableView) getSkinnable()).getFixedCellSize());
        }
    }

    @Override // com.sun.javafx.scene.control.skin.TableViewSkinBase
    protected ObservableList<TableColumn<T, ?>> getVisibleLeafColumns() {
        return this.tableView.getVisibleLeafColumns();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.sun.javafx.scene.control.skin.TableViewSkinBase
    public int getVisibleLeafIndex(TableColumn<T, ?> tc) {
        return this.tableView.getVisibleLeafIndex(tc);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.sun.javafx.scene.control.skin.TableViewSkinBase
    public TableColumn<T, ?> getVisibleLeafColumn(int col) {
        return this.tableView.getVisibleLeafColumn(col);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.sun.javafx.scene.control.skin.TableViewSkinBase
    public TableView.TableViewFocusModel<T> getFocusModel() {
        return this.tableView.getFocusModel();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.sun.javafx.scene.control.skin.TableViewSkinBase
    public TablePosition<T, ?> getFocusedCell() {
        return this.tableView.getFocusModel().getFocusedCell();
    }

    @Override // com.sun.javafx.scene.control.skin.TableViewSkinBase
    protected TableSelectionModel<T> getSelectionModel() {
        return this.tableView.getSelectionModel();
    }

    @Override // com.sun.javafx.scene.control.skin.TableViewSkinBase
    protected ObjectProperty<Callback<TableView<T>, TableRow<T>>> rowFactoryProperty() {
        return this.tableView.rowFactoryProperty();
    }

    @Override // com.sun.javafx.scene.control.skin.TableViewSkinBase
    protected ObjectProperty<Node> placeholderProperty() {
        return this.tableView.placeholderProperty();
    }

    @Override // com.sun.javafx.scene.control.skin.TableViewSkinBase
    protected ObjectProperty<ObservableList<T>> itemsProperty() {
        return this.tableView.itemsProperty();
    }

    @Override // com.sun.javafx.scene.control.skin.TableViewSkinBase
    protected ObservableList<TableColumn<T, ?>> getColumns() {
        return this.tableView.getColumns();
    }

    @Override // com.sun.javafx.scene.control.skin.TableViewSkinBase
    protected BooleanProperty tableMenuButtonVisibleProperty() {
        return this.tableView.tableMenuButtonVisibleProperty();
    }

    @Override // com.sun.javafx.scene.control.skin.TableViewSkinBase
    protected ObjectProperty<Callback<ResizeFeaturesBase, Boolean>> columnResizePolicyProperty() {
        return this.tableView.columnResizePolicyProperty();
    }

    @Override // com.sun.javafx.scene.control.skin.TableViewSkinBase
    protected ObservableList<TableColumn<T, ?>> getSortOrder() {
        return this.tableView.getSortOrder();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.sun.javafx.scene.control.skin.TableViewSkinBase
    public boolean resizeColumn(TableColumn<T, ?> tc, double delta) {
        return this.tableView.resizeColumn(tc, delta);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.sun.javafx.scene.control.skin.TableViewSkinBase
    public void edit(int index, TableColumn<T, ?> column) {
        this.tableView.edit(index, column);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.sun.javafx.scene.control.skin.TableViewSkinBase
    public void resizeColumnToFitContent(TableColumn<T, ?> tc, int maxRows) {
        List<?> items;
        Callback cellFactory;
        TableCell<T, ?> cell;
        if (!tc.isResizable() || (items = itemsProperty().get()) == null || items.isEmpty() || (cellFactory = tc.getCellFactory()) == null || (cell = cellFactory.call(tc)) == null) {
            return;
        }
        cell.getProperties().put("deferToParentPrefWidth", Boolean.TRUE);
        double padding = 10.0d;
        Node n2 = cell.getSkin() == null ? null : cell.getSkin().getNode();
        if (n2 instanceof Region) {
            Region r2 = (Region) n2;
            padding = r2.snappedLeftInset() + r2.snappedRightInset();
        }
        int rows = maxRows == -1 ? items.size() : Math.min(items.size(), maxRows);
        double maxWidth = 0.0d;
        for (int row = 0; row < rows; row++) {
            cell.updateTableColumn(tc);
            cell.updateTableView(this.tableView);
            cell.updateIndex(row);
            if ((cell.getText() != null && !cell.getText().isEmpty()) || cell.getGraphic() != null) {
                getChildren().add(cell);
                cell.applyCss();
                maxWidth = Math.max(maxWidth, cell.prefWidth(-1.0d));
                getChildren().remove(cell);
            }
        }
        cell.updateIndex(-1);
        TableColumnHeader header = getTableHeaderRow().getColumnHeaderFor(tc);
        double headerTextWidth = Utils.computeTextWidth(header.label.getFont(), tc.getText(), -1.0d);
        Node graphic = header.label.getGraphic();
        double headerGraphicWidth = graphic == null ? 0.0d : graphic.prefWidth(-1.0d) + header.label.getGraphicTextGap();
        double headerWidth = headerTextWidth + headerGraphicWidth + 10.0d + header.snappedLeftInset() + header.snappedRightInset();
        double maxWidth2 = Math.max(maxWidth, headerWidth) + padding;
        if (this.tableView.getColumnResizePolicy() == TableView.CONSTRAINED_RESIZE_POLICY && this.tableView.getWidth() > 0.0d) {
            if (maxWidth2 > tc.getMaxWidth()) {
                maxWidth2 = tc.getMaxWidth();
            }
            int size = tc.getColumns().size();
            if (size > 0) {
                resizeColumnToFitContent((TableColumn) tc.getColumns().get(size - 1), maxRows);
                return;
            } else {
                resizeColumn((TableColumn) tc, Math.round(maxWidth2 - tc.getWidth()));
                return;
            }
        }
        tc.impl_setWidth(maxWidth2);
    }

    @Override // com.sun.javafx.scene.control.skin.VirtualContainerBase
    public int getItemCount() {
        if (this.tableView.getItems() == null) {
            return 0;
        }
        return this.tableView.getItems().size();
    }

    @Override // com.sun.javafx.scene.control.skin.VirtualContainerBase
    public TableRow<T> createCell() {
        TableRow<T> cell;
        if (this.tableView.getRowFactory() != null) {
            cell = this.tableView.getRowFactory().call(this.tableView);
        } else {
            cell = new TableRow<>();
        }
        cell.updateTableView(this.tableView);
        return cell;
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.sun.javafx.scene.control.skin.TableViewSkinBase
    protected void horizontalScroll() {
        super.horizontalScroll();
        if (((TableView) getSkinnable()).getFixedCellSize() > 0.0d) {
            this.flow.requestCellLayout();
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.sun.javafx.scene.control.skin.TableViewSkinBase, javafx.scene.control.SkinBase
    public Object queryAccessibleAttribute(AccessibleAttribute attribute, Object... parameters) {
        switch (attribute) {
            case SELECTED_ITEMS:
                List<Node> selection = new ArrayList<>();
                TableView.TableViewSelectionModel<T> sm = ((TableView) getSkinnable()).getSelectionModel();
                for (TablePosition<T, ?> pos : sm.getSelectedCells()) {
                    TableRow<T> row = (TableRow) this.flow.getPrivateCell(pos.getRow());
                    if (row != null) {
                        selection.add(row);
                    }
                }
                return FXCollections.observableArrayList(selection);
            default:
                return super.queryAccessibleAttribute(attribute, parameters);
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // javafx.scene.control.SkinBase
    protected void executeAccessibleAction(AccessibleAction action, Object... parameters) {
        TableSelectionModel<T> sm;
        switch (action) {
            case SHOW_ITEM:
                Node item = (Node) parameters[0];
                if (item instanceof TableCell) {
                    this.flow.show(((TableCell) item).getIndex());
                    break;
                }
                break;
            case SET_SELECTED_ITEMS:
                ObservableList<Node> items = (ObservableList) parameters[0];
                if (items != null && (sm = ((TableView) getSkinnable()).getSelectionModel()) != null) {
                    sm.clearSelection();
                    for (Node item2 : items) {
                        if (item2 instanceof TableCell) {
                            TableCell<T, ?> cell = (TableCell) item2;
                            sm.select(cell.getIndex(), cell.getTableColumn());
                        }
                    }
                    break;
                }
                break;
            default:
                super.executeAccessibleAction(action, parameters);
                break;
        }
    }
}
