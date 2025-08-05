package javafx.scene.control;

import com.sun.javafx.scene.control.skin.TreeTableRowSkin;
import java.lang.ref.WeakReference;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.WeakInvalidationListener;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ListChangeListener;
import javafx.collections.WeakListChangeListener;
import javafx.css.PseudoClass;
import javafx.scene.AccessibleAction;
import javafx.scene.AccessibleAttribute;
import javafx.scene.AccessibleRole;
import javafx.scene.Node;
import javafx.scene.control.TreeTableView;

/* loaded from: jfxrt.jar:javafx/scene/control/TreeTableRow.class */
public class TreeTableRow<T> extends IndexedCell<T> {
    private boolean oldExpanded;
    private static final String DEFAULT_STYLE_CLASS = "tree-table-row-cell";
    private static final PseudoClass EXPANDED_PSEUDOCLASS_STATE = PseudoClass.getPseudoClass("expanded");
    private static final PseudoClass COLLAPSED_PSEUDOCLASS_STATE = PseudoClass.getPseudoClass("collapsed");
    private final ListChangeListener<Integer> selectedListener = c2 -> {
        updateSelection();
    };
    private final InvalidationListener focusedListener = valueModel -> {
        updateFocus();
    };
    private final InvalidationListener editingListener = valueModel -> {
        updateEditing();
    };
    private final InvalidationListener leafListener = new InvalidationListener() { // from class: javafx.scene.control.TreeTableRow.1
        @Override // javafx.beans.InvalidationListener
        public void invalidated(Observable valueModel) {
            TreeItem<T> treeItem = TreeTableRow.this.getTreeItem();
            if (treeItem != null) {
                TreeTableRow.this.requestLayout();
            }
        }
    };
    private final InvalidationListener treeItemExpandedInvalidationListener = o2 -> {
        boolean expanded = ((BooleanProperty) o2).get();
        pseudoClassStateChanged(EXPANDED_PSEUDOCLASS_STATE, expanded);
        pseudoClassStateChanged(COLLAPSED_PSEUDOCLASS_STATE, !expanded);
        if (expanded != this.oldExpanded) {
            notifyAccessibleAttributeChanged(AccessibleAttribute.EXPANDED);
        }
        this.oldExpanded = expanded;
    };
    private final WeakListChangeListener<Integer> weakSelectedListener = new WeakListChangeListener<>(this.selectedListener);
    private final WeakInvalidationListener weakFocusedListener = new WeakInvalidationListener(this.focusedListener);
    private final WeakInvalidationListener weakEditingListener = new WeakInvalidationListener(this.editingListener);
    private final WeakInvalidationListener weakLeafListener = new WeakInvalidationListener(this.leafListener);
    private final WeakInvalidationListener weakTreeItemExpandedInvalidationListener = new WeakInvalidationListener(this.treeItemExpandedInvalidationListener);
    private ReadOnlyObjectWrapper<TreeItem<T>> treeItem = new ReadOnlyObjectWrapper<TreeItem<T>>(this, "treeItem") { // from class: javafx.scene.control.TreeTableRow.2
        TreeItem<T> oldValue = null;

        @Override // javafx.beans.property.ObjectPropertyBase
        protected void invalidated() {
            if (this.oldValue != null) {
                this.oldValue.expandedProperty().removeListener(TreeTableRow.this.weakTreeItemExpandedInvalidationListener);
            }
            this.oldValue = get();
            if (this.oldValue != null) {
                TreeTableRow.this.oldExpanded = this.oldValue.isExpanded();
                this.oldValue.expandedProperty().addListener(TreeTableRow.this.weakTreeItemExpandedInvalidationListener);
                TreeTableRow.this.weakTreeItemExpandedInvalidationListener.invalidated(this.oldValue.expandedProperty());
            }
        }
    };
    private ObjectProperty<Node> disclosureNode = new SimpleObjectProperty(this, "disclosureNode");
    private ReadOnlyObjectWrapper<TreeTableView<T>> treeTableView = new ReadOnlyObjectWrapper<TreeTableView<T>>(this, "treeTableView") { // from class: javafx.scene.control.TreeTableRow.3
        private WeakReference<TreeTableView<T>> weakTreeTableViewRef;

        @Override // javafx.beans.property.ObjectPropertyBase
        protected void invalidated() {
            if (this.weakTreeTableViewRef != null) {
                TreeTableView<T> oldTreeTableView = this.weakTreeTableViewRef.get();
                if (oldTreeTableView != null) {
                    TreeTableView.TreeTableViewSelectionModel<T> sm = oldTreeTableView.getSelectionModel();
                    if (sm != null) {
                        sm.getSelectedIndices().removeListener(TreeTableRow.this.weakSelectedListener);
                    }
                    TreeTableView.TreeTableViewFocusModel<T> fm = oldTreeTableView.getFocusModel();
                    if (fm != null) {
                        fm.focusedIndexProperty().removeListener(TreeTableRow.this.weakFocusedListener);
                    }
                    oldTreeTableView.editingCellProperty().removeListener(TreeTableRow.this.weakEditingListener);
                }
                this.weakTreeTableViewRef = null;
            }
            if (get() != null) {
                TreeTableView.TreeTableViewSelectionModel<T> sm2 = get().getSelectionModel();
                if (sm2 != null) {
                    sm2.getSelectedIndices().addListener(TreeTableRow.this.weakSelectedListener);
                }
                TreeTableView.TreeTableViewFocusModel<T> fm2 = get().getFocusModel();
                if (fm2 != null) {
                    fm2.focusedIndexProperty().addListener(TreeTableRow.this.weakFocusedListener);
                }
                get().editingCellProperty().addListener(TreeTableRow.this.weakEditingListener);
                this.weakTreeTableViewRef = new WeakReference<>(get());
            }
            TreeTableRow.this.updateItem();
            TreeTableRow.this.requestLayout();
        }
    };
    private int index = -1;
    private boolean isFirstRun = true;

    public TreeTableRow() {
        getStyleClass().addAll(DEFAULT_STYLE_CLASS);
        setAccessibleRole(AccessibleRole.TREE_TABLE_ROW);
    }

    private void setTreeItem(TreeItem<T> value) {
        this.treeItem.set(value);
    }

    public final TreeItem<T> getTreeItem() {
        return this.treeItem.get();
    }

    public final ReadOnlyObjectProperty<TreeItem<T>> treeItemProperty() {
        return this.treeItem.getReadOnlyProperty();
    }

    public final void setDisclosureNode(Node value) {
        disclosureNodeProperty().set(value);
    }

    public final Node getDisclosureNode() {
        return this.disclosureNode.get();
    }

    public final ObjectProperty<Node> disclosureNodeProperty() {
        return this.disclosureNode;
    }

    private void setTreeTableView(TreeTableView<T> value) {
        this.treeTableView.set(value);
    }

    public final TreeTableView<T> getTreeTableView() {
        return this.treeTableView.get();
    }

    public final ReadOnlyObjectProperty<TreeTableView<T>> treeTableViewProperty() {
        return this.treeTableView.getReadOnlyProperty();
    }

    @Override // javafx.scene.control.IndexedCell
    void indexChanged(int oldIndex, int newIndex) {
        this.index = getIndex();
        updateItem();
        updateSelection();
        updateFocus();
    }

    @Override // javafx.scene.control.Cell
    public void startEdit() {
        TreeTableView<T> treeTable = getTreeTableView();
        if (isEditable()) {
            if (treeTable != null && !treeTable.isEditable()) {
                return;
            }
            super.startEdit();
            if (treeTable != null) {
                treeTable.fireEvent(new TreeTableView.EditEvent(treeTable, TreeTableView.editStartEvent(), getTreeItem(), getItem(), null));
                treeTable.requestFocus();
            }
        }
    }

    @Override // javafx.scene.control.Cell
    public void commitEdit(T newValue) {
        if (isEditing()) {
            TreeItem<T> treeItem = getTreeItem();
            TreeTableView<T> treeTable = getTreeTableView();
            if (treeTable != null) {
                treeTable.fireEvent(new TreeTableView.EditEvent(treeTable, TreeTableView.editCommitEvent(), treeItem, getItem(), newValue));
            }
            if (treeItem != null) {
                treeItem.setValue(newValue);
                updateTreeItem(treeItem);
                updateItem(newValue, false);
            }
            super.commitEdit(newValue);
            if (treeTable != null) {
                treeTable.edit(-1, null);
                treeTable.requestFocus();
            }
        }
    }

    @Override // javafx.scene.control.Cell
    public void cancelEdit() {
        if (isEditing()) {
            TreeTableView<T> treeTable = getTreeTableView();
            if (treeTable != null) {
                treeTable.fireEvent(new TreeTableView.EditEvent(treeTable, TreeTableView.editCancelEvent(), getTreeItem(), getItem(), null));
            }
            super.cancelEdit();
            if (treeTable != null) {
                treeTable.edit(-1, null);
                treeTable.requestFocus();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateItem() {
        TreeTableView<T> tv = getTreeTableView();
        if (tv == null) {
            return;
        }
        boolean valid = this.index >= 0 && this.index < tv.getExpandedItemCount();
        TreeItem<T> oldTreeItem = getTreeItem();
        boolean isEmpty = isEmpty();
        if (valid) {
            TreeItem<T> newTreeItem = tv.getTreeItem(this.index);
            T newValue = newTreeItem == null ? null : newTreeItem.getValue();
            updateTreeItem(newTreeItem);
            updateItem(newValue, false);
            return;
        }
        if ((!isEmpty && oldTreeItem != null) || this.isFirstRun) {
            updateTreeItem(null);
            updateItem(null, true);
            this.isFirstRun = false;
        }
    }

    private void updateSelection() {
        boolean isSelected;
        if (isEmpty() || this.index == -1 || getTreeTableView() == null || getTreeTableView().getSelectionModel() == null || isSelected() == (isSelected = getTreeTableView().getSelectionModel().isSelected(this.index))) {
            return;
        }
        updateSelected(isSelected);
    }

    private void updateFocus() {
        if (getIndex() == -1 || getTreeTableView() == null || getTreeTableView().getFocusModel() == null) {
            return;
        }
        setFocused(getTreeTableView().getFocusModel().isFocused(getIndex()));
    }

    private void updateEditing() {
        if (getIndex() == -1 || getTreeTableView() == null || getTreeItem() == null) {
            return;
        }
        TreeTablePosition<T, ?> editingCell = getTreeTableView().getEditingCell();
        if (editingCell != null && editingCell.getTableColumn() != null) {
            return;
        }
        TreeItem<T> editItem = editingCell == null ? null : editingCell.getTreeItem();
        if (!isEditing() && getTreeItem().equals(editItem)) {
            startEdit();
        } else if (isEditing() && !getTreeItem().equals(editItem)) {
            cancelEdit();
        }
    }

    public final void updateTreeTableView(TreeTableView<T> treeTable) {
        setTreeTableView(treeTable);
    }

    public final void updateTreeItem(TreeItem<T> treeItem) {
        TreeItem<T> _treeItem = getTreeItem();
        if (_treeItem != null) {
            _treeItem.leafProperty().removeListener(this.weakLeafListener);
        }
        setTreeItem(treeItem);
        if (treeItem != null) {
            treeItem.leafProperty().addListener(this.weakLeafListener);
        }
    }

    @Override // javafx.scene.control.Control
    protected Skin<?> createDefaultSkin() {
        return new TreeTableRowSkin(this);
    }

    @Override // javafx.scene.control.Control, javafx.scene.Parent, javafx.scene.Node
    public Object queryAccessibleAttribute(AccessibleAttribute attribute, Object... parameters) {
        int index;
        TreeItem<T> child;
        TreeItem<T> parent;
        TreeItem<T> treeItem = getTreeItem();
        TreeTableView<T> treeTableView = getTreeTableView();
        switch (attribute) {
            case TREE_ITEM_PARENT:
                if (treeItem == null || (parent = treeItem.getParent()) == null) {
                    return null;
                }
                int parentIndex = treeTableView.getRow(parent);
                return treeTableView.queryAccessibleAttribute(AccessibleAttribute.ROW_AT_INDEX, Integer.valueOf(parentIndex));
            case TREE_ITEM_COUNT:
                if (treeItem != null && treeItem.isExpanded()) {
                    return Integer.valueOf(treeItem.getChildren().size());
                }
                return 0;
            case TREE_ITEM_AT_INDEX:
                if (treeItem == null || !treeItem.isExpanded() || (index = ((Integer) parameters[0]).intValue()) >= treeItem.getChildren().size() || (child = treeItem.getChildren().get(index)) == null) {
                    return null;
                }
                int childIndex = treeTableView.getRow(child);
                return treeTableView.queryAccessibleAttribute(AccessibleAttribute.ROW_AT_INDEX, Integer.valueOf(childIndex));
            case LEAF:
                return Boolean.valueOf(treeItem == null ? true : treeItem.isLeaf());
            case EXPANDED:
                return Boolean.valueOf(treeItem == null ? false : treeItem.isExpanded());
            case INDEX:
                return Integer.valueOf(getIndex());
            case DISCLOSURE_LEVEL:
                return Integer.valueOf(treeTableView == null ? 0 : treeTableView.getTreeItemLevel(treeItem));
            default:
                return super.queryAccessibleAttribute(attribute, parameters);
        }
    }

    @Override // javafx.scene.control.Control, javafx.scene.Node
    public void executeAccessibleAction(AccessibleAction action, Object... parameters) {
        switch (action) {
            case EXPAND:
                TreeItem<T> treeItem = getTreeItem();
                if (treeItem != null) {
                    treeItem.setExpanded(true);
                    break;
                }
                break;
            case COLLAPSE:
                TreeItem<T> treeItem2 = getTreeItem();
                if (treeItem2 != null) {
                    treeItem2.setExpanded(false);
                    break;
                }
                break;
            default:
                super.executeAccessibleAction(action, new Object[0]);
                break;
        }
    }
}
