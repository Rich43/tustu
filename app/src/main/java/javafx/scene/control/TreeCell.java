package javafx.scene.control;

import com.sun.javafx.scene.control.skin.TreeCellSkin;
import java.lang.ref.WeakReference;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.WeakInvalidationListener;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.beans.value.WeakChangeListener;
import javafx.collections.ListChangeListener;
import javafx.collections.WeakListChangeListener;
import javafx.css.PseudoClass;
import javafx.scene.AccessibleAction;
import javafx.scene.AccessibleAttribute;
import javafx.scene.AccessibleRole;
import javafx.scene.Node;
import javafx.scene.control.TreeView;

/* loaded from: jfxrt.jar:javafx/scene/control/TreeCell.class */
public class TreeCell<T> extends IndexedCell<T> {
    private boolean oldIsExpanded;
    private static final String DEFAULT_STYLE_CLASS = "tree-cell";
    private static final PseudoClass EXPANDED_PSEUDOCLASS_STATE = PseudoClass.getPseudoClass("expanded");
    private static final PseudoClass COLLAPSED_PSEUDOCLASS_STATE = PseudoClass.getPseudoClass("collapsed");
    private final ListChangeListener<Integer> selectedListener = c2 -> {
        updateSelection();
    };
    private final ChangeListener<MultipleSelectionModel<TreeItem<T>>> selectionModelPropertyListener = new ChangeListener<MultipleSelectionModel<TreeItem<T>>>() { // from class: javafx.scene.control.TreeCell.1
        @Override // javafx.beans.value.ChangeListener
        public void changed(ObservableValue<? extends MultipleSelectionModel<TreeItem<T>>> observable, MultipleSelectionModel<TreeItem<T>> oldValue, MultipleSelectionModel<TreeItem<T>> newValue) {
            if (oldValue != null) {
                oldValue.getSelectedIndices().removeListener(TreeCell.this.weakSelectedListener);
            }
            if (newValue != null) {
                newValue.getSelectedIndices().addListener(TreeCell.this.weakSelectedListener);
            }
            TreeCell.this.updateSelection();
        }
    };
    private final InvalidationListener focusedListener = valueModel -> {
        updateFocus();
    };
    private final ChangeListener<FocusModel<TreeItem<T>>> focusModelPropertyListener = new ChangeListener<FocusModel<TreeItem<T>>>() { // from class: javafx.scene.control.TreeCell.2
        @Override // javafx.beans.value.ChangeListener
        public void changed(ObservableValue<? extends FocusModel<TreeItem<T>>> observable, FocusModel<TreeItem<T>> oldValue, FocusModel<TreeItem<T>> newValue) {
            if (oldValue != null) {
                oldValue.focusedIndexProperty().removeListener(TreeCell.this.weakFocusedListener);
            }
            if (newValue != null) {
                newValue.focusedIndexProperty().addListener(TreeCell.this.weakFocusedListener);
            }
            TreeCell.this.updateFocus();
        }
    };
    private final InvalidationListener editingListener = valueModel -> {
        updateEditing();
    };
    private final InvalidationListener leafListener = new InvalidationListener() { // from class: javafx.scene.control.TreeCell.3
        @Override // javafx.beans.InvalidationListener
        public void invalidated(Observable valueModel) {
            TreeItem<T> treeItem = TreeCell.this.getTreeItem();
            if (treeItem != null) {
                TreeCell.this.requestLayout();
            }
        }
    };
    private final InvalidationListener treeItemExpandedInvalidationListener = new InvalidationListener() { // from class: javafx.scene.control.TreeCell.4
        @Override // javafx.beans.InvalidationListener
        public void invalidated(Observable o2) {
            boolean isExpanded = ((BooleanProperty) o2).get();
            TreeCell.this.pseudoClassStateChanged(TreeCell.EXPANDED_PSEUDOCLASS_STATE, isExpanded);
            TreeCell.this.pseudoClassStateChanged(TreeCell.COLLAPSED_PSEUDOCLASS_STATE, !isExpanded);
            if (isExpanded != TreeCell.this.oldIsExpanded) {
                TreeCell.this.notifyAccessibleAttributeChanged(AccessibleAttribute.EXPANDED);
            }
            TreeCell.this.oldIsExpanded = isExpanded;
        }
    };
    private final InvalidationListener rootPropertyListener = observable -> {
        updateItem(-1);
    };
    private final WeakListChangeListener<Integer> weakSelectedListener = new WeakListChangeListener<>(this.selectedListener);
    private final WeakChangeListener<MultipleSelectionModel<TreeItem<T>>> weakSelectionModelPropertyListener = new WeakChangeListener<>(this.selectionModelPropertyListener);
    private final WeakInvalidationListener weakFocusedListener = new WeakInvalidationListener(this.focusedListener);
    private final WeakChangeListener<FocusModel<TreeItem<T>>> weakFocusModelPropertyListener = new WeakChangeListener<>(this.focusModelPropertyListener);
    private final WeakInvalidationListener weakEditingListener = new WeakInvalidationListener(this.editingListener);
    private final WeakInvalidationListener weakLeafListener = new WeakInvalidationListener(this.leafListener);
    private final WeakInvalidationListener weakTreeItemExpandedInvalidationListener = new WeakInvalidationListener(this.treeItemExpandedInvalidationListener);
    private final WeakInvalidationListener weakRootPropertyListener = new WeakInvalidationListener(this.rootPropertyListener);
    private ReadOnlyObjectWrapper<TreeItem<T>> treeItem = new ReadOnlyObjectWrapper<TreeItem<T>>(this, "treeItem") { // from class: javafx.scene.control.TreeCell.5
        TreeItem<T> oldValue = null;

        @Override // javafx.beans.property.ObjectPropertyBase
        protected void invalidated() {
            if (this.oldValue != null) {
                this.oldValue.expandedProperty().removeListener(TreeCell.this.weakTreeItemExpandedInvalidationListener);
            }
            this.oldValue = get();
            if (this.oldValue != null) {
                TreeCell.this.oldIsExpanded = this.oldValue.isExpanded();
                this.oldValue.expandedProperty().addListener(TreeCell.this.weakTreeItemExpandedInvalidationListener);
                TreeCell.this.weakTreeItemExpandedInvalidationListener.invalidated(this.oldValue.expandedProperty());
            }
        }
    };
    private ObjectProperty<Node> disclosureNode = new SimpleObjectProperty(this, "disclosureNode");
    private ReadOnlyObjectWrapper<TreeView<T>> treeView = new ReadOnlyObjectWrapper<TreeView<T>>() { // from class: javafx.scene.control.TreeCell.6
        private WeakReference<TreeView<T>> weakTreeViewRef;

        @Override // javafx.beans.property.ObjectPropertyBase
        protected void invalidated() {
            if (this.weakTreeViewRef != null) {
                TreeView<T> oldTreeView = this.weakTreeViewRef.get();
                if (oldTreeView != null) {
                    MultipleSelectionModel<TreeItem<T>> sm = oldTreeView.getSelectionModel();
                    if (sm != null) {
                        sm.getSelectedIndices().removeListener(TreeCell.this.weakSelectedListener);
                    }
                    FocusModel<TreeItem<T>> fm = oldTreeView.getFocusModel();
                    if (fm != null) {
                        fm.focusedIndexProperty().removeListener(TreeCell.this.weakFocusedListener);
                    }
                    oldTreeView.editingItemProperty().removeListener(TreeCell.this.weakEditingListener);
                    oldTreeView.focusModelProperty().removeListener(TreeCell.this.weakFocusModelPropertyListener);
                    oldTreeView.selectionModelProperty().removeListener(TreeCell.this.weakSelectionModelPropertyListener);
                    oldTreeView.rootProperty().removeListener(TreeCell.this.weakRootPropertyListener);
                }
                this.weakTreeViewRef = null;
            }
            TreeView<T> treeView = get();
            if (treeView != null) {
                MultipleSelectionModel<TreeItem<T>> sm2 = treeView.getSelectionModel();
                if (sm2 != null) {
                    sm2.getSelectedIndices().addListener(TreeCell.this.weakSelectedListener);
                }
                FocusModel<TreeItem<T>> fm2 = treeView.getFocusModel();
                if (fm2 != null) {
                    fm2.focusedIndexProperty().addListener(TreeCell.this.weakFocusedListener);
                }
                treeView.editingItemProperty().addListener(TreeCell.this.weakEditingListener);
                treeView.focusModelProperty().addListener(TreeCell.this.weakFocusModelPropertyListener);
                treeView.selectionModelProperty().addListener(TreeCell.this.weakSelectionModelPropertyListener);
                treeView.rootProperty().addListener(TreeCell.this.weakRootPropertyListener);
                this.weakTreeViewRef = new WeakReference<>(treeView);
            }
            TreeCell.this.updateItem(-1);
            TreeCell.this.requestLayout();
        }

        @Override // javafx.beans.property.SimpleObjectProperty, javafx.beans.property.ReadOnlyProperty
        public Object getBean() {
            return TreeCell.this;
        }

        @Override // javafx.beans.property.SimpleObjectProperty, javafx.beans.property.ReadOnlyProperty
        public String getName() {
            return "treeView";
        }
    };
    private boolean isFirstRun = true;
    private boolean updateEditingIndex = true;

    public TreeCell() {
        getStyleClass().addAll(DEFAULT_STYLE_CLASS);
        setAccessibleRole(AccessibleRole.TREE_ITEM);
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

    private void setTreeView(TreeView<T> value) {
        this.treeView.set(value);
    }

    public final TreeView<T> getTreeView() {
        return this.treeView.get();
    }

    public final ReadOnlyObjectProperty<TreeView<T>> treeViewProperty() {
        return this.treeView.getReadOnlyProperty();
    }

    @Override // javafx.scene.control.Cell
    public void startEdit() {
        if (isEditing()) {
            return;
        }
        TreeView<T> tree = getTreeView();
        if (isEditable()) {
            if (tree != null && !tree.isEditable()) {
                return;
            }
            updateItem(-1);
            super.startEdit();
            if (tree != null) {
                tree.fireEvent(new TreeView.EditEvent(tree, TreeView.editStartEvent(), getTreeItem(), getItem(), null));
                tree.requestFocus();
            }
        }
    }

    @Override // javafx.scene.control.Cell
    public void commitEdit(T newValue) {
        if (isEditing()) {
            TreeItem<T> treeItem = getTreeItem();
            TreeView<T> tree = getTreeView();
            if (tree != null) {
                tree.fireEvent(new TreeView.EditEvent(tree, TreeView.editCommitEvent(), treeItem, getItem(), newValue));
            }
            super.commitEdit(newValue);
            if (treeItem != null) {
                treeItem.setValue(newValue);
                updateTreeItem(treeItem);
                updateItem(newValue, false);
            }
            if (tree != null) {
                tree.edit(null);
                ControlUtils.requestFocusOnControlOnlyIfCurrentFocusOwnerIsChild(tree);
            }
        }
    }

    @Override // javafx.scene.control.Cell
    public void cancelEdit() {
        if (isEditing()) {
            TreeView<T> tree = getTreeView();
            super.cancelEdit();
            if (tree != null) {
                if (this.updateEditingIndex) {
                    tree.edit(null);
                }
                ControlUtils.requestFocusOnControlOnlyIfCurrentFocusOwnerIsChild(tree);
                tree.fireEvent(new TreeView.EditEvent(tree, TreeView.editCancelEvent(), getTreeItem(), getItem(), null));
            }
        }
    }

    @Override // javafx.scene.control.Control
    protected Skin<?> createDefaultSkin() {
        return new TreeCellSkin(this);
    }

    @Override // javafx.scene.control.IndexedCell
    void indexChanged(int oldIndex, int newIndex) {
        super.indexChanged(oldIndex, newIndex);
        if (!isEditing() || newIndex != oldIndex) {
            updateItem(oldIndex);
            updateSelection();
            updateFocus();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateItem(int oldIndex) {
        TreeView<T> tv = getTreeView();
        if (tv == null) {
            return;
        }
        int index = getIndex();
        boolean valid = index >= 0 && index < tv.getExpandedItemCount();
        boolean isEmpty = isEmpty();
        TreeItem<T> oldTreeItem = getTreeItem();
        if (valid) {
            TreeItem<T> newTreeItem = tv.getTreeItem(index);
            T newValue = newTreeItem == null ? null : newTreeItem.getValue();
            T oldValue = oldTreeItem == null ? null : oldTreeItem.getValue();
            if (oldIndex != index || isItemChanged(oldValue, newValue)) {
                updateTreeItem(newTreeItem);
                updateItem(newValue, false);
                return;
            }
            return;
        }
        if ((!isEmpty && oldTreeItem != null) || this.isFirstRun) {
            updateTreeItem(null);
            updateItem(null, true);
            this.isFirstRun = false;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateSelection() {
        if (isEmpty() || getIndex() == -1 || getTreeView() == null) {
            return;
        }
        SelectionModel<TreeItem<T>> sm = getTreeView().getSelectionModel();
        if (sm == null) {
            updateSelected(false);
            return;
        }
        boolean isSelected = sm.isSelected(getIndex());
        if (isSelected() == isSelected) {
            return;
        }
        updateSelected(isSelected);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateFocus() {
        if (getIndex() == -1 || getTreeView() == null) {
            return;
        }
        FocusModel<TreeItem<T>> fm = getTreeView().getFocusModel();
        if (fm == null) {
            setFocused(false);
        } else {
            setFocused(fm.isFocused(getIndex()));
        }
    }

    private void updateEditing() {
        int index = getIndex();
        TreeView<T> tree = getTreeView();
        TreeItem<T> treeItem = getTreeItem();
        TreeItem<T> editItem = tree == null ? null : tree.getEditingItem();
        boolean editing = isEditing();
        if (index == -1 || tree == null || treeItem == null) {
            return;
        }
        boolean match = treeItem.equals(editItem);
        if (match && !editing) {
            startEdit();
        } else if (!match && editing) {
            this.updateEditingIndex = false;
            cancelEdit();
            this.updateEditingIndex = true;
        }
    }

    public final void updateTreeView(TreeView<T> tree) {
        setTreeView(tree);
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

    @Override // javafx.scene.control.Control, javafx.scene.Parent, javafx.scene.Node
    public Object queryAccessibleAttribute(AccessibleAttribute attribute, Object... parameters) {
        int index;
        TreeItem<T> child;
        TreeItem<T> parent;
        TreeItem<T> treeItem = getTreeItem();
        TreeView<T> treeView = getTreeView();
        switch (attribute) {
            case TREE_ITEM_PARENT:
                if (treeView == null || treeItem == null || (parent = treeItem.getParent()) == null) {
                    return null;
                }
                int parentIndex = treeView.getRow(parent);
                return treeView.queryAccessibleAttribute(AccessibleAttribute.ROW_AT_INDEX, Integer.valueOf(parentIndex));
            case TREE_ITEM_COUNT:
                if (treeItem != null && treeItem.isExpanded()) {
                    return Integer.valueOf(treeItem.getChildren().size());
                }
                return 0;
            case TREE_ITEM_AT_INDEX:
                if (treeItem == null || !treeItem.isExpanded() || (index = ((Integer) parameters[0]).intValue()) >= treeItem.getChildren().size() || (child = treeItem.getChildren().get(index)) == null) {
                    return null;
                }
                int childIndex = treeView.getRow(child);
                return treeView.queryAccessibleAttribute(AccessibleAttribute.ROW_AT_INDEX, Integer.valueOf(childIndex));
            case LEAF:
                return Boolean.valueOf(treeItem == null ? true : treeItem.isLeaf());
            case EXPANDED:
                return Boolean.valueOf(treeItem == null ? false : treeItem.isExpanded());
            case INDEX:
                return Integer.valueOf(getIndex());
            case SELECTED:
                return Boolean.valueOf(isSelected());
            case DISCLOSURE_LEVEL:
                return Integer.valueOf(treeView == null ? 0 : treeView.getTreeItemLevel(treeItem));
            default:
                return super.queryAccessibleAttribute(attribute, parameters);
        }
    }

    @Override // javafx.scene.control.Control, javafx.scene.Node
    public void executeAccessibleAction(AccessibleAction action, Object... parameters) {
        FocusModel<TreeItem<T>> fm;
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
            case REQUEST_FOCUS:
                TreeView<T> treeView = getTreeView();
                if (treeView != null && (fm = treeView.getFocusModel()) != null) {
                    fm.focus(getIndex());
                    break;
                }
                break;
            default:
                super.executeAccessibleAction(action, new Object[0]);
                break;
        }
    }
}
