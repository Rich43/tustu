package javafx.scene.control;

import com.sun.javafx.scene.control.skin.ListCellSkin;
import java.lang.ref.WeakReference;
import java.util.List;
import javafx.beans.InvalidationListener;
import javafx.beans.WeakInvalidationListener;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.beans.value.WeakChangeListener;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.WeakListChangeListener;
import javafx.scene.AccessibleAction;
import javafx.scene.AccessibleAttribute;
import javafx.scene.AccessibleRole;
import javafx.scene.control.ListView;

/* loaded from: jfxrt.jar:javafx/scene/control/ListCell.class */
public class ListCell<T> extends IndexedCell<T> {
    private final InvalidationListener editingListener = value -> {
        updateEditing();
    };
    private boolean updateEditingIndex = true;
    private final ListChangeListener<Integer> selectedListener = c2 -> {
        updateSelection();
    };
    private final ChangeListener<MultipleSelectionModel<T>> selectionModelPropertyListener = new ChangeListener<MultipleSelectionModel<T>>() { // from class: javafx.scene.control.ListCell.1
        @Override // javafx.beans.value.ChangeListener
        public void changed(ObservableValue<? extends MultipleSelectionModel<T>> observable, MultipleSelectionModel<T> oldValue, MultipleSelectionModel<T> newValue) {
            if (oldValue != null) {
                oldValue.getSelectedIndices().removeListener(ListCell.this.weakSelectedListener);
            }
            if (newValue != null) {
                newValue.getSelectedIndices().addListener(ListCell.this.weakSelectedListener);
            }
            ListCell.this.updateSelection();
        }
    };
    private final ListChangeListener<T> itemsListener = c2 -> {
        boolean doUpdate;
        boolean z2 = false;
        while (true) {
            doUpdate = z2;
            if (!c2.next()) {
                break;
            }
            int currentIndex = getIndex();
            ListView<T> lv = getListView();
            List<T> items = lv == null ? null : lv.getItems();
            int itemCount = items == null ? 0 : items.size();
            boolean indexAfterChangeFromIndex = currentIndex >= c2.getFrom();
            boolean indexBeforeChangeToIndex = currentIndex < c2.getTo() || currentIndex == itemCount;
            boolean indexInRange = indexAfterChangeFromIndex && indexBeforeChangeToIndex;
            z2 = indexInRange || (indexAfterChangeFromIndex && !c2.wasReplaced() && (c2.wasRemoved() || c2.wasAdded()));
        }
        if (doUpdate) {
            updateItem(-1);
        }
    };
    private final ChangeListener<ObservableList<T>> itemsPropertyListener = new ChangeListener<ObservableList<T>>() { // from class: javafx.scene.control.ListCell.2
        @Override // javafx.beans.value.ChangeListener
        public void changed(ObservableValue<? extends ObservableList<T>> observable, ObservableList<T> oldValue, ObservableList<T> newValue) {
            if (oldValue != null) {
                oldValue.removeListener(ListCell.this.weakItemsListener);
            }
            if (newValue != null) {
                newValue.addListener(ListCell.this.weakItemsListener);
            }
            ListCell.this.updateItem(-1);
        }
    };
    private final InvalidationListener focusedListener = value -> {
        updateFocus();
    };
    private final ChangeListener<FocusModel<T>> focusModelPropertyListener = new ChangeListener<FocusModel<T>>() { // from class: javafx.scene.control.ListCell.3
        @Override // javafx.beans.value.ChangeListener
        public void changed(ObservableValue<? extends FocusModel<T>> observable, FocusModel<T> oldValue, FocusModel<T> newValue) {
            if (oldValue != null) {
                oldValue.focusedIndexProperty().removeListener(ListCell.this.weakFocusedListener);
            }
            if (newValue != null) {
                newValue.focusedIndexProperty().addListener(ListCell.this.weakFocusedListener);
            }
            ListCell.this.updateFocus();
        }
    };
    private final WeakInvalidationListener weakEditingListener = new WeakInvalidationListener(this.editingListener);
    private final WeakListChangeListener<Integer> weakSelectedListener = new WeakListChangeListener<>(this.selectedListener);
    private final WeakChangeListener<MultipleSelectionModel<T>> weakSelectionModelPropertyListener = new WeakChangeListener<>(this.selectionModelPropertyListener);
    private final WeakListChangeListener<T> weakItemsListener = new WeakListChangeListener<>(this.itemsListener);
    private final WeakChangeListener<ObservableList<T>> weakItemsPropertyListener = new WeakChangeListener<>(this.itemsPropertyListener);
    private final WeakInvalidationListener weakFocusedListener = new WeakInvalidationListener(this.focusedListener);
    private final WeakChangeListener<FocusModel<T>> weakFocusModelPropertyListener = new WeakChangeListener<>(this.focusModelPropertyListener);
    private ReadOnlyObjectWrapper<ListView<T>> listView = new ReadOnlyObjectWrapper<ListView<T>>(this, "listView") { // from class: javafx.scene.control.ListCell.4
        private WeakReference<ListView<T>> weakListViewRef = new WeakReference<>(null);

        @Override // javafx.beans.property.ObjectPropertyBase
        protected void invalidated() {
            ListView<T> currentListView = get();
            ListView<T> oldListView = this.weakListViewRef.get();
            if (currentListView == oldListView) {
                return;
            }
            if (oldListView != null) {
                MultipleSelectionModel<T> sm = oldListView.getSelectionModel();
                if (sm != null) {
                    sm.getSelectedIndices().removeListener(ListCell.this.weakSelectedListener);
                }
                FocusModel<T> fm = oldListView.getFocusModel();
                if (fm != null) {
                    fm.focusedIndexProperty().removeListener(ListCell.this.weakFocusedListener);
                }
                ObservableList<T> items = oldListView.getItems();
                if (items != null) {
                    items.removeListener(ListCell.this.weakItemsListener);
                }
                oldListView.editingIndexProperty().removeListener(ListCell.this.weakEditingListener);
                oldListView.itemsProperty().removeListener(ListCell.this.weakItemsPropertyListener);
                oldListView.focusModelProperty().removeListener(ListCell.this.weakFocusModelPropertyListener);
                oldListView.selectionModelProperty().removeListener(ListCell.this.weakSelectionModelPropertyListener);
            }
            if (currentListView != null) {
                MultipleSelectionModel<T> sm2 = currentListView.getSelectionModel();
                if (sm2 != null) {
                    sm2.getSelectedIndices().addListener(ListCell.this.weakSelectedListener);
                }
                FocusModel<T> fm2 = currentListView.getFocusModel();
                if (fm2 != null) {
                    fm2.focusedIndexProperty().addListener(ListCell.this.weakFocusedListener);
                }
                ObservableList<T> items2 = currentListView.getItems();
                if (items2 != null) {
                    items2.addListener(ListCell.this.weakItemsListener);
                }
                currentListView.editingIndexProperty().addListener(ListCell.this.weakEditingListener);
                currentListView.itemsProperty().addListener(ListCell.this.weakItemsPropertyListener);
                currentListView.focusModelProperty().addListener(ListCell.this.weakFocusModelPropertyListener);
                currentListView.selectionModelProperty().addListener(ListCell.this.weakSelectionModelPropertyListener);
                this.weakListViewRef = new WeakReference<>(currentListView);
            }
            ListCell.this.updateItem(-1);
            ListCell.this.updateSelection();
            ListCell.this.updateFocus();
            ListCell.this.requestLayout();
        }
    };
    private boolean firstRun = true;
    private static final String DEFAULT_STYLE_CLASS = "list-cell";

    public ListCell() {
        getStyleClass().addAll(DEFAULT_STYLE_CLASS);
        setAccessibleRole(AccessibleRole.LIST_ITEM);
    }

    private void setListView(ListView<T> value) {
        this.listView.set(value);
    }

    public final ListView<T> getListView() {
        return this.listView.get();
    }

    public final ReadOnlyObjectProperty<ListView<T>> listViewProperty() {
        return this.listView.getReadOnlyProperty();
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

    @Override // javafx.scene.control.Control
    protected Skin<?> createDefaultSkin() {
        return new ListCellSkin(this);
    }

    @Override // javafx.scene.control.Cell
    public void startEdit() {
        ListView<T> list = getListView();
        if (isEditable()) {
            if (list != null && !list.isEditable()) {
                return;
            }
            super.startEdit();
            if (list != null) {
                list.fireEvent(new ListView.EditEvent(list, ListView.editStartEvent(), null, list.getEditingIndex()));
                list.edit(getIndex());
                list.requestFocus();
            }
        }
    }

    @Override // javafx.scene.control.Cell
    public void commitEdit(T newValue) {
        if (isEditing()) {
            ListView<T> list = getListView();
            if (list != null) {
                list.fireEvent(new ListView.EditEvent(list, ListView.editCommitEvent(), newValue, list.getEditingIndex()));
            }
            super.commitEdit(newValue);
            updateItem(newValue, false);
            if (list != null) {
                list.edit(-1);
                ControlUtils.requestFocusOnControlOnlyIfCurrentFocusOwnerIsChild(list);
            }
        }
    }

    @Override // javafx.scene.control.Cell
    public void cancelEdit() {
        if (isEditing()) {
            ListView<T> list = getListView();
            super.cancelEdit();
            if (list != null) {
                int editingIndex = list.getEditingIndex();
                if (this.updateEditingIndex) {
                    list.edit(-1);
                }
                ControlUtils.requestFocusOnControlOnlyIfCurrentFocusOwnerIsChild(list);
                list.fireEvent(new ListView.EditEvent(list, ListView.editCancelEvent(), null, editingIndex));
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateItem(int oldIndex) {
        ListView<T> lv = getListView();
        List<T> items = lv == null ? null : lv.getItems();
        int index = getIndex();
        int itemCount = items == null ? -1 : items.size();
        boolean valid = items != null && index >= 0 && index < itemCount;
        T oldValue = getItem();
        boolean isEmpty = isEmpty();
        if (valid) {
            T newValue = items.get(index);
            if (oldIndex != index || isItemChanged(oldValue, newValue)) {
                updateItem(newValue, false);
                return;
            }
            return;
        }
        if ((!isEmpty && oldValue != null) || this.firstRun) {
            updateItem(null, true);
            this.firstRun = false;
        }
    }

    public final void updateListView(ListView<T> listView) {
        setListView(listView);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateSelection() {
        if (isEmpty()) {
            return;
        }
        int index = getIndex();
        ListView<T> listView = getListView();
        if (index == -1 || listView == null) {
            return;
        }
        SelectionModel<T> sm = listView.getSelectionModel();
        if (sm == null) {
            updateSelected(false);
            return;
        }
        boolean isSelected = sm.isSelected(index);
        if (isSelected() == isSelected) {
            return;
        }
        updateSelected(isSelected);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateFocus() {
        int index = getIndex();
        ListView<T> listView = getListView();
        if (index == -1 || listView == null) {
            return;
        }
        FocusModel<T> fm = listView.getFocusModel();
        if (fm == null) {
            setFocused(false);
        } else {
            setFocused(fm.isFocused(index));
        }
    }

    private void updateEditing() {
        int index = getIndex();
        ListView<T> list = getListView();
        int editIndex = list == null ? -1 : list.getEditingIndex();
        boolean editing = isEditing();
        if (index != -1 && list != null) {
            if (index == editIndex && !editing) {
                startEdit();
            } else if (index != editIndex && editing) {
                this.updateEditingIndex = false;
                cancelEdit();
                this.updateEditingIndex = true;
            }
        }
    }

    @Override // javafx.scene.control.Control, javafx.scene.Parent, javafx.scene.Node
    public Object queryAccessibleAttribute(AccessibleAttribute attribute, Object... parameters) {
        switch (attribute) {
            case INDEX:
                return Integer.valueOf(getIndex());
            case SELECTED:
                return Boolean.valueOf(isSelected());
            default:
                return super.queryAccessibleAttribute(attribute, parameters);
        }
    }

    @Override // javafx.scene.control.Control, javafx.scene.Node
    public void executeAccessibleAction(AccessibleAction action, Object... parameters) {
        FocusModel<T> fm;
        switch (action) {
            case REQUEST_FOCUS:
                ListView<T> listView = getListView();
                if (listView != null && (fm = listView.getFocusModel()) != null) {
                    fm.focus(getIndex());
                    break;
                }
                break;
            default:
                super.executeAccessibleAction(action, parameters);
                break;
        }
    }
}
