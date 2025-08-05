package javafx.scene.control;

import com.sun.javafx.scene.control.skin.TreeTableCellSkin;
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
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableView;

/* loaded from: jfxrt.jar:javafx/scene/control/TreeTableCell.class */
public class TreeTableCell<S, T> extends IndexedCell<T> {
    private ReadOnlyObjectWrapper<TreeTableView<S>> treeTableView;
    private WeakReference<S> oldRowItemRef;
    private static final String DEFAULT_STYLE_CLASS = "tree-table-cell";
    private static final PseudoClass PSEUDO_CLASS_LAST_VISIBLE = PseudoClass.getPseudoClass("last-visible");
    boolean lockItemOnEdit = false;
    private boolean itemDirty = false;
    private ListChangeListener<TreeTablePosition<S, ?>> selectedListener = c2 -> {
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
    private ListChangeListener<TreeTableColumn<S, ?>> visibleLeafColumnsListener = c2 -> {
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
    private final InvalidationListener rootPropertyListener = observable -> {
        updateItem(-1);
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
    private final WeakListChangeListener<TreeTablePosition<S, ?>> weakSelectedListener = new WeakListChangeListener<>(this.selectedListener);
    private final WeakInvalidationListener weakFocusedListener = new WeakInvalidationListener(this.focusedListener);
    private final WeakInvalidationListener weaktableRowUpdateObserver = new WeakInvalidationListener(this.tableRowUpdateObserver);
    private final WeakInvalidationListener weakEditingListener = new WeakInvalidationListener(this.editingListener);
    private final WeakListChangeListener<TreeTableColumn<S, ?>> weakVisibleLeafColumnsListener = new WeakListChangeListener<>(this.visibleLeafColumnsListener);
    private final WeakListChangeListener<String> weakColumnStyleClassListener = new WeakListChangeListener<>(this.columnStyleClassListener);
    private final WeakInvalidationListener weakColumnStyleListener = new WeakInvalidationListener(this.columnStyleListener);
    private final WeakInvalidationListener weakColumnIdListener = new WeakInvalidationListener(this.columnIdListener);
    private final WeakInvalidationListener weakRootPropertyListener = new WeakInvalidationListener(this.rootPropertyListener);
    private ReadOnlyObjectWrapper<TreeTableColumn<S, T>> treeTableColumn = new ReadOnlyObjectWrapper<TreeTableColumn<S, T>>(this, "treeTableColumn") { // from class: javafx.scene.control.TreeTableCell.1
        @Override // javafx.beans.property.ObjectPropertyBase
        protected void invalidated() {
            TreeTableCell.this.updateColumnIndex();
        }
    };
    private ReadOnlyObjectWrapper<TreeTableRow<S>> treeTableRow = new ReadOnlyObjectWrapper<>(this, "treeTableRow");
    private boolean isLastVisibleColumn = false;
    private int columnIndex = -1;
    private boolean updateEditingIndex = true;
    private ObservableValue<T> currentObservableValue = null;
    private boolean isFirstRun = true;

    public TreeTableCell() {
        getStyleClass().addAll(DEFAULT_STYLE_CLASS);
        setAccessibleRole(AccessibleRole.TREE_TABLE_CELL);
        updateColumnIndex();
    }

    public final ReadOnlyObjectProperty<TreeTableColumn<S, T>> tableColumnProperty() {
        return this.treeTableColumn.getReadOnlyProperty();
    }

    private void setTableColumn(TreeTableColumn<S, T> value) {
        this.treeTableColumn.set(value);
    }

    public final TreeTableColumn<S, T> getTableColumn() {
        return this.treeTableColumn.get();
    }

    private void setTreeTableView(TreeTableView<S> value) {
        treeTableViewPropertyImpl().set(value);
    }

    public final TreeTableView<S> getTreeTableView() {
        if (this.treeTableView == null) {
            return null;
        }
        return this.treeTableView.get();
    }

    public final ReadOnlyObjectProperty<TreeTableView<S>> treeTableViewProperty() {
        return treeTableViewPropertyImpl().getReadOnlyProperty();
    }

    private ReadOnlyObjectWrapper<TreeTableView<S>> treeTableViewPropertyImpl() {
        if (this.treeTableView == null) {
            this.treeTableView = new ReadOnlyObjectWrapper<TreeTableView<S>>(this, "treeTableView") { // from class: javafx.scene.control.TreeTableCell.2
                private WeakReference<TreeTableView<S>> weakTableViewRef;

                @Override // javafx.beans.property.ObjectPropertyBase
                protected void invalidated() {
                    TreeTableView<S> oldTableView;
                    if (this.weakTableViewRef != null && (oldTableView = this.weakTableViewRef.get()) != null) {
                        TreeTableView.TreeTableViewSelectionModel<S> sm = oldTableView.getSelectionModel();
                        if (sm != null) {
                            sm.getSelectedCells().removeListener(TreeTableCell.this.weakSelectedListener);
                        }
                        TreeTableView.TreeTableViewFocusModel<S> fm = oldTableView.getFocusModel();
                        if (fm != null) {
                            fm.focusedCellProperty().removeListener(TreeTableCell.this.weakFocusedListener);
                        }
                        oldTableView.editingCellProperty().removeListener(TreeTableCell.this.weakEditingListener);
                        oldTableView.getVisibleLeafColumns().removeListener(TreeTableCell.this.weakVisibleLeafColumnsListener);
                        oldTableView.rootProperty().removeListener(TreeTableCell.this.weakRootPropertyListener);
                    }
                    TreeTableView<S> newTreeTableView = get();
                    if (newTreeTableView != null) {
                        TreeTableView.TreeTableViewSelectionModel<S> sm2 = newTreeTableView.getSelectionModel();
                        if (sm2 != null) {
                            sm2.getSelectedCells().addListener(TreeTableCell.this.weakSelectedListener);
                        }
                        TreeTableView.TreeTableViewFocusModel<S> fm2 = newTreeTableView.getFocusModel();
                        if (fm2 != null) {
                            fm2.focusedCellProperty().addListener(TreeTableCell.this.weakFocusedListener);
                        }
                        newTreeTableView.editingCellProperty().addListener(TreeTableCell.this.weakEditingListener);
                        newTreeTableView.getVisibleLeafColumns().addListener(TreeTableCell.this.weakVisibleLeafColumnsListener);
                        newTreeTableView.rootProperty().addListener(TreeTableCell.this.weakRootPropertyListener);
                        this.weakTableViewRef = new WeakReference<>(newTreeTableView);
                    }
                    TreeTableCell.this.updateColumnIndex();
                }
            };
        }
        return this.treeTableView;
    }

    private void setTreeTableRow(TreeTableRow<S> value) {
        this.treeTableRow.set(value);
    }

    public final TreeTableRow<S> getTreeTableRow() {
        return this.treeTableRow.get();
    }

    public final ReadOnlyObjectProperty<TreeTableRow<S>> tableRowProperty() {
        return this.treeTableRow;
    }

    @Override // javafx.scene.control.Cell
    public void startEdit() {
        if (isEditing()) {
            return;
        }
        TreeTableView<S> table = getTreeTableView();
        TreeTableColumn<S, T> column = getTableColumn();
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
                    TreeTableColumn.CellEditEvent editEvent = new TreeTableColumn.CellEditEvent(table, table.getEditingCell(), TreeTableColumn.editStartEvent(), null);
                    Event.fireEvent(column, editEvent);
                }
            }
        }
    }

    @Override // javafx.scene.control.Cell
    public void commitEdit(T newValue) {
        if (isEditing()) {
            TreeTableView<S> table = getTreeTableView();
            if (table != null) {
                TreeTableColumn.CellEditEvent<S, T> editEvent = new TreeTableColumn.CellEditEvent<>(table, table.getEditingCell(), TreeTableColumn.editCommitEvent(), newValue);
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
            TreeTableView<S> table = getTreeTableView();
            super.cancelEdit();
            if (table != null) {
                TreeTablePosition<S, ?> editingCell = table.getEditingCell();
                if (this.updateEditingIndex) {
                    table.edit(-1, null);
                }
                ControlUtils.requestFocusOnControlOnlyIfCurrentFocusOwnerIsChild(table);
                TreeTableColumn.CellEditEvent<S, T> editEvent = new TreeTableColumn.CellEditEvent<>(table, editingCell, TreeTableColumn.editCancelEvent(), null);
                Event.fireEvent(getTableColumn(), editEvent);
            }
        }
    }

    @Override // javafx.scene.control.Cell
    public void updateSelected(boolean selected) {
        if (getTreeTableRow() == null || getTreeTableRow().isEmpty()) {
            return;
        }
        setSelected(selected);
    }

    @Override // javafx.scene.control.IndexedCell
    void indexChanged(int oldIndex, int newIndex) {
        super.indexChanged(oldIndex, newIndex);
        if (!isEditing() || newIndex != oldIndex) {
            updateItem(oldIndex);
            updateSelection();
            updateFocus();
            updateEditing();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateColumnIndex() {
        TreeTableView<S> tv = getTreeTableView();
        TreeTableColumn<S, ?> tableColumn = getTableColumn();
        this.columnIndex = (tv == null || tableColumn == null) ? -1 : tv.getVisibleLeafIndex(tableColumn);
        this.isLastVisibleColumn = (getTableColumn() == null || this.columnIndex == -1 || this.columnIndex != tv.getVisibleLeafColumns().size() - 1) ? false : true;
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
        TreeTableView<S> tv = getTreeTableView();
        if (getIndex() == -1 || tv == null) {
            return;
        }
        TreeTableView.TreeTableViewSelectionModel<S> sm = tv.getSelectionModel();
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
        TreeTableView<S> tv = getTreeTableView();
        if (getIndex() == -1 || tv == null) {
            return;
        }
        TreeTableView.TreeTableViewFocusModel<S> fm = tv.getFocusModel();
        if (fm == null) {
            setFocused(false);
        } else {
            boolean isFocusedNow = fm != null && fm.isFocused(getIndex(), (TreeTableColumn) getTableColumn());
            setFocused(isFocusedNow);
        }
    }

    private void updateEditing() {
        TreeTableView<S> tv = getTreeTableView();
        if (getIndex() == -1 || tv == null) {
            return;
        }
        TreeTablePosition<S, ?> editCell = tv.getEditingCell();
        boolean match = match(editCell);
        if (match && !isEditing()) {
            startEdit();
        } else if (!match && isEditing()) {
            this.updateEditingIndex = false;
            cancelEdit();
            this.updateEditingIndex = true;
        }
    }

    private boolean match(TreeTablePosition pos) {
        return pos != null && pos.getRow() == getIndex() && pos.getTableColumn() == getTableColumn();
    }

    private boolean isInCellSelectionMode() {
        TreeTableView.TreeTableViewSelectionModel<S> sm;
        TreeTableView<S> tv = getTreeTableView();
        return (tv == null || (sm = tv.getSelectionModel()) == null || !sm.isCellSelectionEnabled()) ? false : true;
    }

    /* JADX WARN: Removed duplicated region for block: B:58:0x0108  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private void updateItem(int r6) {
        /*
            Method dump skipped, instructions count: 306
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: javafx.scene.control.TreeTableCell.updateItem(int):void");
    }

    @Override // javafx.scene.control.Control, javafx.scene.Parent
    protected void layoutChildren() {
        if (this.itemDirty) {
            updateItem(-1);
            this.itemDirty = false;
        }
        super.layoutChildren();
    }

    public final void updateTreeTableView(TreeTableView<S> tv) {
        setTreeTableView(tv);
    }

    public final void updateTreeTableRow(TreeTableRow<S> treeTableRow) {
        setTreeTableRow(treeTableRow);
    }

    public final void updateTreeTableColumn(TreeTableColumn<S, T> col) {
        TreeTableColumn<S, T> oldCol = getTableColumn();
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

    @Override // javafx.scene.control.Control
    protected Skin<?> createDefaultSkin() {
        return new TreeTableCellSkin(this);
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
                return Boolean.valueOf(isInCellSelectionMode() ? isSelected() : getTreeTableRow().isSelected());
            default:
                return super.queryAccessibleAttribute(attribute, parameters);
        }
    }

    @Override // javafx.scene.control.Control, javafx.scene.Node
    public void executeAccessibleAction(AccessibleAction action, Object... parameters) {
        TreeTableView.TreeTableViewFocusModel<S> fm;
        switch (action) {
            case REQUEST_FOCUS:
                TreeTableView<S> treeTableView = getTreeTableView();
                if (treeTableView != null && (fm = treeTableView.getFocusModel()) != null) {
                    fm.focus(getIndex(), (TreeTableColumn) getTableColumn());
                    break;
                }
                break;
            default:
                super.executeAccessibleAction(action, parameters);
                break;
        }
    }
}
