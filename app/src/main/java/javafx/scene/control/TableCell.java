package javafx.scene.control;

import com.sun.javafx.scene.control.skin.TableCellSkin;
import java.lang.ref.WeakReference;
import javafx.beans.InvalidationListener;
import javafx.beans.WeakInvalidationListener;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.collections.WeakListChangeListener;
import javafx.css.PseudoClass;
import javafx.event.Event;
import javafx.scene.AccessibleAction;
import javafx.scene.AccessibleAttribute;
import javafx.scene.AccessibleRole;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

/* loaded from: jfxrt.jar:javafx/scene/control/TableCell.class */
public class TableCell<S, T> extends IndexedCell<T> {
    private ReadOnlyObjectWrapper<TableView<S>> tableView;
    private WeakReference<S> oldRowItemRef;
    private static final String DEFAULT_STYLE_CLASS = "table-cell";
    private static final PseudoClass PSEUDO_CLASS_LAST_VISIBLE = PseudoClass.getPseudoClass("last-visible");
    boolean lockItemOnEdit = false;
    private boolean itemDirty = false;
    private ListChangeListener<TablePosition> selectedListener = c2 -> {
        while (c2.next()) {
            if (c2.wasAdded() || c2.wasRemoved()) {
                updateSelection();
            }
        }
    };
    private final InvalidationListener focusedListener = value -> {
        updateFocus();
    };
    private final InvalidationListener tableRowUpdateObserver = value -> {
        this.itemDirty = true;
        requestLayout();
    };
    private final InvalidationListener editingListener = value -> {
        updateEditing();
    };
    private ListChangeListener<TableColumn<S, ?>> visibleLeafColumnsListener = c2 -> {
        updateColumnIndex();
    };
    private ListChangeListener<String> columnStyleClassListener = c2 -> {
        while (c2.next()) {
            if (c2.wasRemoved()) {
                getStyleClass().removeAll(c2.getRemoved());
            }
            if (c2.wasAdded()) {
                getStyleClass().addAll(c2.getAddedSubList());
            }
        }
    };
    private final InvalidationListener columnStyleListener = value -> {
        if (getTableColumn() != null) {
            possiblySetStyle(getTableColumn().getStyle());
        }
    };
    private final InvalidationListener columnIdListener = value -> {
        if (getTableColumn() != null) {
            possiblySetId(getTableColumn().getId());
        }
    };
    private final WeakListChangeListener<TablePosition> weakSelectedListener = new WeakListChangeListener<>(this.selectedListener);
    private final WeakInvalidationListener weakFocusedListener = new WeakInvalidationListener(this.focusedListener);
    private final WeakInvalidationListener weaktableRowUpdateObserver = new WeakInvalidationListener(this.tableRowUpdateObserver);
    private final WeakInvalidationListener weakEditingListener = new WeakInvalidationListener(this.editingListener);
    private final WeakInvalidationListener weakColumnStyleListener = new WeakInvalidationListener(this.columnStyleListener);
    private final WeakInvalidationListener weakColumnIdListener = new WeakInvalidationListener(this.columnIdListener);
    private final WeakListChangeListener<TableColumn<S, ?>> weakVisibleLeafColumnsListener = new WeakListChangeListener<>(this.visibleLeafColumnsListener);
    private final WeakListChangeListener<String> weakColumnStyleClassListener = new WeakListChangeListener<>(this.columnStyleClassListener);
    private ReadOnlyObjectWrapper<TableColumn<S, T>> tableColumn = new ReadOnlyObjectWrapper<TableColumn<S, T>>() { // from class: javafx.scene.control.TableCell.1
        @Override // javafx.beans.property.ObjectPropertyBase
        protected void invalidated() {
            TableCell.this.updateColumnIndex();
        }

        @Override // javafx.beans.property.SimpleObjectProperty, javafx.beans.property.ReadOnlyProperty
        public Object getBean() {
            return TableCell.this;
        }

        @Override // javafx.beans.property.SimpleObjectProperty, javafx.beans.property.ReadOnlyProperty
        public String getName() {
            return "tableColumn";
        }
    };
    private ReadOnlyObjectWrapper<TableRow> tableRow = new ReadOnlyObjectWrapper<>(this, "tableRow");
    private boolean isLastVisibleColumn = false;
    private int columnIndex = -1;
    private boolean updateEditingIndex = true;
    private ObservableValue<T> currentObservableValue = null;
    private boolean isFirstRun = true;

    public TableCell() {
        getStyleClass().addAll(DEFAULT_STYLE_CLASS);
        setAccessibleRole(AccessibleRole.TABLE_CELL);
        updateColumnIndex();
    }

    public final ReadOnlyObjectProperty<TableColumn<S, T>> tableColumnProperty() {
        return this.tableColumn.getReadOnlyProperty();
    }

    private void setTableColumn(TableColumn<S, T> value) {
        this.tableColumn.set(value);
    }

    public final TableColumn<S, T> getTableColumn() {
        return this.tableColumn.get();
    }

    private void setTableView(TableView<S> value) {
        tableViewPropertyImpl().set(value);
    }

    public final TableView<S> getTableView() {
        if (this.tableView == null) {
            return null;
        }
        return this.tableView.get();
    }

    public final ReadOnlyObjectProperty<TableView<S>> tableViewProperty() {
        return tableViewPropertyImpl().getReadOnlyProperty();
    }

    private ReadOnlyObjectWrapper<TableView<S>> tableViewPropertyImpl() {
        if (this.tableView == null) {
            this.tableView = new ReadOnlyObjectWrapper<TableView<S>>() { // from class: javafx.scene.control.TableCell.2
                private WeakReference<TableView<S>> weakTableViewRef;

                @Override // javafx.beans.property.ObjectPropertyBase
                protected void invalidated() {
                    if (this.weakTableViewRef != null) {
                        TableCell.this.cleanUpTableViewListeners(this.weakTableViewRef.get());
                    }
                    if (get() != null) {
                        TableView.TableViewSelectionModel<S> sm = get().getSelectionModel();
                        if (sm != null) {
                            sm.getSelectedCells().addListener(TableCell.this.weakSelectedListener);
                        }
                        TableView.TableViewFocusModel<S> fm = get().getFocusModel();
                        if (fm != null) {
                            fm.focusedCellProperty().addListener(TableCell.this.weakFocusedListener);
                        }
                        get().editingCellProperty().addListener(TableCell.this.weakEditingListener);
                        get().getVisibleLeafColumns().addListener(TableCell.this.weakVisibleLeafColumnsListener);
                        this.weakTableViewRef = new WeakReference<>(get());
                    }
                    TableCell.this.updateColumnIndex();
                }

                @Override // javafx.beans.property.SimpleObjectProperty, javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return TableCell.this;
                }

                @Override // javafx.beans.property.SimpleObjectProperty, javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return "tableView";
                }
            };
        }
        return this.tableView;
    }

    private void setTableRow(TableRow value) {
        this.tableRow.set(value);
    }

    public final TableRow getTableRow() {
        return this.tableRow.get();
    }

    public final ReadOnlyObjectProperty<TableRow> tableRowProperty() {
        return this.tableRow;
    }

    @Override // javafx.scene.control.Cell
    public void startEdit() {
        TableView<S> table = getTableView();
        TableColumn<S, T> column = getTableColumn();
        if (isEditable()) {
            if (table == null || table.isEditable()) {
                if (column != null && !getTableColumn().isEditable()) {
                    return;
                }
                if (!this.lockItemOnEdit) {
                    updateItem(-1);
                }
                super.startEdit();
                if (column != null) {
                    TableColumn.CellEditEvent<S, ?> editEvent = new TableColumn.CellEditEvent<>(table, table.getEditingCell(), TableColumn.editStartEvent(), null);
                    Event.fireEvent(column, editEvent);
                }
            }
        }
    }

    @Override // javafx.scene.control.Cell
    public void commitEdit(T newValue) {
        if (isEditing()) {
            TableView<S> table = getTableView();
            if (table != null) {
                TableColumn.CellEditEvent editEvent = new TableColumn.CellEditEvent(table, table.getEditingCell(), TableColumn.editCommitEvent(), newValue);
                Event.fireEvent(getTableColumn(), editEvent);
            }
            super.commitEdit(newValue);
            updateItem(newValue, false);
            if (table != null) {
                table.edit(-1, null);
                ControlUtils.requestFocusOnControlOnlyIfCurrentFocusOwnerIsChild(table);
            }
        }
    }

    @Override // javafx.scene.control.Cell
    public void cancelEdit() {
        if (isEditing()) {
            TableView<S> table = getTableView();
            super.cancelEdit();
            if (table != null) {
                TablePosition<S, ?> editingCell = table.getEditingCell();
                if (this.updateEditingIndex) {
                    table.edit(-1, null);
                }
                ControlUtils.requestFocusOnControlOnlyIfCurrentFocusOwnerIsChild(table);
                TableColumn.CellEditEvent<S, ?> editEvent = new TableColumn.CellEditEvent<>(table, editingCell, TableColumn.editCancelEvent(), null);
                Event.fireEvent(getTableColumn(), editEvent);
            }
        }
    }

    @Override // javafx.scene.control.Cell
    public void updateSelected(boolean selected) {
        if (getTableRow() == null || getTableRow().isEmpty()) {
            return;
        }
        setSelected(selected);
    }

    @Override // javafx.scene.control.Control
    protected Skin<?> createDefaultSkin() {
        return new TableCellSkin(this);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void cleanUpTableViewListeners(TableView<S> tableView) {
        if (tableView != null) {
            TableView.TableViewSelectionModel<S> sm = tableView.getSelectionModel();
            if (sm != null) {
                sm.getSelectedCells().removeListener(this.weakSelectedListener);
            }
            TableView.TableViewFocusModel<S> fm = tableView.getFocusModel();
            if (fm != null) {
                fm.focusedCellProperty().removeListener(this.weakFocusedListener);
            }
            tableView.editingCellProperty().removeListener(this.weakEditingListener);
            tableView.getVisibleLeafColumns().removeListener(this.weakVisibleLeafColumnsListener);
        }
    }

    @Override // javafx.scene.control.IndexedCell
    void indexChanged(int oldIndex, int newIndex) {
        super.indexChanged(oldIndex, newIndex);
        updateItem(oldIndex);
        updateSelection();
        updateFocus();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateColumnIndex() {
        TableView<S> tv = getTableView();
        TableColumn<S, ?> tableColumn = getTableColumn();
        this.columnIndex = (tv == null || tableColumn == null) ? -1 : tv.getVisibleLeafIndex(tableColumn);
        this.isLastVisibleColumn = (getTableColumn() == null || this.columnIndex == -1 || this.columnIndex != getTableView().getVisibleLeafColumns().size() - 1) ? false : true;
        pseudoClassStateChanged(PSEUDO_CLASS_LAST_VISIBLE, this.isLastVisibleColumn);
    }

    private void updateSelection() {
        if (isEmpty()) {
            return;
        }
        boolean isSelected = isSelected();
        if (!isInCellSelectionMode()) {
            if (isSelected) {
                updateSelected(false);
                return;
            }
            return;
        }
        TableView<S> tableView = getTableView();
        if (getIndex() == -1 || tableView == null) {
            return;
        }
        TableSelectionModel<S> sm = tableView.getSelectionModel();
        if (sm == null) {
            updateSelected(false);
            return;
        }
        boolean isSelectedNow = sm.isSelected(getIndex(), getTableColumn());
        if (isSelected == isSelectedNow) {
            return;
        }
        updateSelected(isSelectedNow);
    }

    private void updateFocus() {
        boolean isFocused = isFocused();
        if (!isInCellSelectionMode()) {
            if (isFocused) {
                setFocused(false);
                return;
            }
            return;
        }
        TableView<S> tableView = getTableView();
        TableRow<S> tableRow = getTableRow();
        int index = getIndex();
        if (index == -1 || tableView == null || tableRow == null) {
            return;
        }
        TableView.TableViewFocusModel<S> fm = tableView.getFocusModel();
        if (fm == null) {
            setFocused(false);
        } else {
            boolean isFocusedNow = fm != null && fm.isFocused(index, (TableColumn) getTableColumn());
            setFocused(isFocusedNow);
        }
    }

    private void updateEditing() {
        if (getIndex() == -1 || getTableView() == null) {
            return;
        }
        TablePosition<S, ?> editCell = getTableView().getEditingCell();
        boolean match = match(editCell);
        if (match && !isEditing()) {
            startEdit();
        } else if (!match && isEditing()) {
            this.updateEditingIndex = false;
            cancelEdit();
            this.updateEditingIndex = true;
        }
    }

    private boolean match(TablePosition<S, ?> pos) {
        return pos != null && pos.getRow() == getIndex() && pos.getTableColumn() == getTableColumn();
    }

    private boolean isInCellSelectionMode() {
        TableSelectionModel<S> sm;
        TableView<S> tableView = getTableView();
        return (tableView == null || (sm = tableView.getSelectionModel()) == null || !sm.isCellSelectionEnabled()) ? false : true;
    }

    /* JADX WARN: Removed duplicated region for block: B:60:0x0113  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private void updateItem(int r6) {
        /*
            Method dump skipped, instructions count: 317
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: javafx.scene.control.TableCell.updateItem(int):void");
    }

    @Override // javafx.scene.control.Control, javafx.scene.Parent
    protected void layoutChildren() {
        if (this.itemDirty) {
            updateItem(-1);
            this.itemDirty = false;
        }
        super.layoutChildren();
    }

    public final void updateTableView(TableView tv) {
        setTableView(tv);
    }

    public final void updateTableRow(TableRow tableRow) {
        setTableRow(tableRow);
    }

    public final void updateTableColumn(TableColumn col) {
        TableColumn<S, T> oldCol = getTableColumn();
        if (oldCol != null) {
            oldCol.getStyleClass().removeListener(this.weakColumnStyleClassListener);
            getStyleClass().removeAll(oldCol.getStyleClass());
            oldCol.idProperty().removeListener(this.weakColumnIdListener);
            oldCol.styleProperty().removeListener(this.weakColumnStyleListener);
            String id = getId();
            String style = getStyle();
            if (id != null && id.equals(oldCol.getId())) {
                setId(null);
            }
            if (style != null && style.equals(oldCol.getStyle())) {
                setStyle("");
            }
        }
        setTableColumn(col);
        if (col != null) {
            getStyleClass().addAll(col.getStyleClass());
            col.getStyleClass().addListener(this.weakColumnStyleClassListener);
            col.idProperty().addListener(this.weakColumnIdListener);
            col.styleProperty().addListener(this.weakColumnStyleListener);
            possiblySetId(col.getId());
            possiblySetStyle(col.getStyle());
        }
    }

    private void possiblySetId(String idCandidate) {
        if (getId() == null || getId().isEmpty()) {
            setId(idCandidate);
        }
    }

    private void possiblySetStyle(String styleCandidate) {
        if (getStyle() == null || getStyle().isEmpty()) {
            setStyle(styleCandidate);
        }
    }

    @Override // javafx.scene.control.Control, javafx.scene.Parent, javafx.scene.Node
    public Object queryAccessibleAttribute(AccessibleAttribute attribute, Object... parameters) {
        switch (attribute) {
            case ROW_INDEX:
                return Integer.valueOf(getIndex());
            case COLUMN_INDEX:
                return Integer.valueOf(this.columnIndex);
            case SELECTED:
                return Boolean.valueOf(isInCellSelectionMode() ? isSelected() : getTableRow().isSelected());
            default:
                return super.queryAccessibleAttribute(attribute, parameters);
        }
    }

    @Override // javafx.scene.control.Control, javafx.scene.Node
    public void executeAccessibleAction(AccessibleAction action, Object... parameters) {
        TableView.TableViewFocusModel<S> fm;
        switch (action) {
            case REQUEST_FOCUS:
                TableView<S> tableView = getTableView();
                if (tableView != null && (fm = tableView.getFocusModel()) != null) {
                    fm.focus(getIndex(), (TableColumn) getTableColumn());
                    break;
                }
                break;
            default:
                super.executeAccessibleAction(action, parameters);
                break;
        }
    }
}
