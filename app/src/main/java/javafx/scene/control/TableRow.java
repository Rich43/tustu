package javafx.scene.control;

import com.sun.javafx.scene.control.skin.TableRowSkin;
import java.lang.ref.WeakReference;
import java.util.List;
import javafx.beans.InvalidationListener;
import javafx.beans.WeakInvalidationListener;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.ListChangeListener;
import javafx.collections.WeakListChangeListener;
import javafx.scene.AccessibleAttribute;
import javafx.scene.AccessibleRole;
import javafx.scene.control.TableView;

/* loaded from: jfxrt.jar:javafx/scene/control/TableRow.class */
public class TableRow<T> extends IndexedCell<T> {
    private ReadOnlyObjectWrapper<TableView<T>> tableView;
    private static final String DEFAULT_STYLE_CLASS = "table-row-cell";
    private ListChangeListener<TablePosition> selectedListener = c2 -> {
        updateSelection();
    };
    private final InvalidationListener focusedListener = valueModel -> {
        updateFocus();
    };
    private final InvalidationListener editingListener = valueModel -> {
        updateEditing();
    };
    private final WeakListChangeListener<TablePosition> weakSelectedListener = new WeakListChangeListener<>(this.selectedListener);
    private final WeakInvalidationListener weakFocusedListener = new WeakInvalidationListener(this.focusedListener);
    private final WeakInvalidationListener weakEditingListener = new WeakInvalidationListener(this.editingListener);
    private boolean isFirstRun = true;

    public TableRow() {
        getStyleClass().addAll(DEFAULT_STYLE_CLASS);
        setAccessibleRole(AccessibleRole.TABLE_ROW);
    }

    private void setTableView(TableView<T> value) {
        tableViewPropertyImpl().set(value);
    }

    public final TableView<T> getTableView() {
        if (this.tableView == null) {
            return null;
        }
        return this.tableView.get();
    }

    public final ReadOnlyObjectProperty<TableView<T>> tableViewProperty() {
        return tableViewPropertyImpl().getReadOnlyProperty();
    }

    private ReadOnlyObjectWrapper<TableView<T>> tableViewPropertyImpl() {
        if (this.tableView == null) {
            this.tableView = new ReadOnlyObjectWrapper<TableView<T>>() { // from class: javafx.scene.control.TableRow.1
                private WeakReference<TableView<T>> weakTableViewRef;

                @Override // javafx.beans.property.ObjectPropertyBase
                protected void invalidated() {
                    if (this.weakTableViewRef != null) {
                        TableView<T> oldTableView = this.weakTableViewRef.get();
                        if (oldTableView != null) {
                            TableView.TableViewSelectionModel<T> sm = oldTableView.getSelectionModel();
                            if (sm != null) {
                                sm.getSelectedCells().removeListener(TableRow.this.weakSelectedListener);
                            }
                            TableView.TableViewFocusModel<T> fm = oldTableView.getFocusModel();
                            if (fm != null) {
                                fm.focusedCellProperty().removeListener(TableRow.this.weakFocusedListener);
                            }
                            oldTableView.editingCellProperty().removeListener(TableRow.this.weakEditingListener);
                        }
                        this.weakTableViewRef = null;
                    }
                    TableView<T> tableView = TableRow.this.getTableView();
                    if (tableView != null) {
                        TableView.TableViewSelectionModel<T> sm2 = tableView.getSelectionModel();
                        if (sm2 != null) {
                            sm2.getSelectedCells().addListener(TableRow.this.weakSelectedListener);
                        }
                        TableView.TableViewFocusModel<T> fm2 = tableView.getFocusModel();
                        if (fm2 != null) {
                            fm2.focusedCellProperty().addListener(TableRow.this.weakFocusedListener);
                        }
                        tableView.editingCellProperty().addListener(TableRow.this.weakEditingListener);
                        this.weakTableViewRef = new WeakReference<>(get());
                    }
                }

                @Override // javafx.beans.property.SimpleObjectProperty, javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return TableRow.this;
                }

                @Override // javafx.beans.property.SimpleObjectProperty, javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return "tableView";
                }
            };
        }
        return this.tableView;
    }

    @Override // javafx.scene.control.Control
    protected Skin<?> createDefaultSkin() {
        return new TableRowSkin(this);
    }

    @Override // javafx.scene.control.IndexedCell
    void indexChanged(int oldIndex, int newIndex) {
        super.indexChanged(oldIndex, newIndex);
        updateItem(oldIndex);
        updateSelection();
        updateFocus();
    }

    private void updateItem(int oldIndex) {
        TableView<T> tv = getTableView();
        if (tv == null || tv.getItems() == null) {
            return;
        }
        List<T> items = tv.getItems();
        int itemCount = items == null ? -1 : items.size();
        int newIndex = getIndex();
        boolean valid = newIndex >= 0 && newIndex < itemCount;
        T oldValue = getItem();
        boolean isEmpty = isEmpty();
        if (valid) {
            T newValue = items.get(newIndex);
            if (oldIndex != newIndex || isItemChanged(oldValue, newValue)) {
                updateItem(newValue, false);
                return;
            }
            return;
        }
        if ((!isEmpty && oldValue != null) || this.isFirstRun) {
            updateItem(null, true);
            this.isFirstRun = false;
        }
    }

    private void updateSelection() {
        if (getIndex() == -1) {
            return;
        }
        TableView<T> table = getTableView();
        boolean isSelected = (table == null || table.getSelectionModel() == null || table.getSelectionModel().isCellSelectionEnabled() || !table.getSelectionModel().isSelected(getIndex())) ? false : true;
        updateSelected(isSelected);
    }

    private void updateFocus() {
        TableView<T> table;
        if (getIndex() == -1 || (table = getTableView()) == null) {
            return;
        }
        TableView.TableViewSelectionModel<T> sm = table.getSelectionModel();
        TableView.TableViewFocusModel<T> fm = table.getFocusModel();
        if (sm == null || fm == null) {
            return;
        }
        boolean isFocused = !sm.isCellSelectionEnabled() && fm.isFocused(getIndex());
        setFocused(isFocused);
    }

    private void updateEditing() {
        TableView<T> table;
        TableView.TableViewSelectionModel<T> sm;
        if (getIndex() == -1 || (table = getTableView()) == null || (sm = table.getSelectionModel()) == null || sm.isCellSelectionEnabled()) {
            return;
        }
        TablePosition<T, ?> editCell = table.getEditingCell();
        if (editCell != null && editCell.getTableColumn() != null) {
            return;
        }
        boolean z2 = editCell != null && editCell.getRow() == getIndex();
        boolean rowMatch = z2;
        if (!isEditing() && rowMatch) {
            startEdit();
        } else if (isEditing() && !rowMatch) {
            cancelEdit();
        }
    }

    public final void updateTableView(TableView<T> tv) {
        setTableView(tv);
    }

    @Override // javafx.scene.control.Control, javafx.scene.Parent, javafx.scene.Node
    public Object queryAccessibleAttribute(AccessibleAttribute attribute, Object... parameters) {
        switch (attribute) {
            case INDEX:
                return Integer.valueOf(getIndex());
            default:
                return super.queryAccessibleAttribute(attribute, parameters);
        }
    }
}
