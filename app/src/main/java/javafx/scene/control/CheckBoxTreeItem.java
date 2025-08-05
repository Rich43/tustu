package javafx.scene.control;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.event.Event;
import javafx.event.EventType;
import javafx.scene.Node;
import javax.swing.JInternalFrame;

/* loaded from: jfxrt.jar:javafx/scene/control/CheckBoxTreeItem.class */
public class CheckBoxTreeItem<T> extends TreeItem<T> {
    private final ChangeListener<Boolean> stateChangeListener;
    private final BooleanProperty selected;
    private final BooleanProperty indeterminate;
    private final BooleanProperty independent;
    private static final EventType<? extends Event> CHECK_BOX_SELECTION_CHANGED_EVENT = new EventType<>(TreeModificationEvent.ANY, "checkBoxSelectionChangedEvent");
    private static boolean updateLock = false;

    public static <T> EventType<TreeModificationEvent<T>> checkBoxSelectionChangedEvent() {
        return (EventType<TreeModificationEvent<T>>) CHECK_BOX_SELECTION_CHANGED_EVENT;
    }

    public CheckBoxTreeItem() {
        this(null);
    }

    public CheckBoxTreeItem(T value) {
        this(value, null, false);
    }

    public CheckBoxTreeItem(T value, Node graphic) {
        this(value, graphic, false);
    }

    public CheckBoxTreeItem(T value, Node graphic, boolean selected) {
        this(value, graphic, selected, false);
    }

    public CheckBoxTreeItem(T value, Node graphic, boolean selected, boolean independent) {
        super(value, graphic);
        this.stateChangeListener = (ov, oldVal, newVal) -> {
            updateState();
        };
        this.selected = new SimpleBooleanProperty(this, JInternalFrame.IS_SELECTED_PROPERTY, false) { // from class: javafx.scene.control.CheckBoxTreeItem.1
            @Override // javafx.beans.property.BooleanPropertyBase
            protected void invalidated() {
                super.invalidated();
                CheckBoxTreeItem.this.fireEvent(CheckBoxTreeItem.this, true);
            }
        };
        this.indeterminate = new SimpleBooleanProperty(this, "indeterminate", false) { // from class: javafx.scene.control.CheckBoxTreeItem.2
            @Override // javafx.beans.property.BooleanPropertyBase
            protected void invalidated() {
                super.invalidated();
                CheckBoxTreeItem.this.fireEvent(CheckBoxTreeItem.this, false);
            }
        };
        this.independent = new SimpleBooleanProperty(this, "independent", false);
        setSelected(selected);
        setIndependent(independent);
        selectedProperty().addListener(this.stateChangeListener);
        indeterminateProperty().addListener(this.stateChangeListener);
    }

    public final void setSelected(boolean value) {
        selectedProperty().setValue(Boolean.valueOf(value));
    }

    public final boolean isSelected() {
        return this.selected.getValue2().booleanValue();
    }

    public final BooleanProperty selectedProperty() {
        return this.selected;
    }

    public final void setIndeterminate(boolean value) {
        indeterminateProperty().setValue(Boolean.valueOf(value));
    }

    public final boolean isIndeterminate() {
        return this.indeterminate.getValue2().booleanValue();
    }

    public final BooleanProperty indeterminateProperty() {
        return this.indeterminate;
    }

    public final BooleanProperty independentProperty() {
        return this.independent;
    }

    public final void setIndependent(boolean value) {
        independentProperty().setValue(Boolean.valueOf(value));
    }

    public final boolean isIndependent() {
        return this.independent.getValue2().booleanValue();
    }

    private void updateState() {
        if (isIndependent()) {
            return;
        }
        boolean firstLock = !updateLock;
        updateLock = true;
        updateUpwards();
        if (firstLock) {
            updateLock = false;
        }
        if (updateLock) {
            return;
        }
        updateDownwards();
    }

    private void updateUpwards() {
        if (getParent() instanceof CheckBoxTreeItem) {
            CheckBoxTreeItem<?> parent = (CheckBoxTreeItem) getParent();
            int selectCount = 0;
            int indeterminateCount = 0;
            for (TreeItem<T> treeItem : parent.getChildren()) {
                if (treeItem instanceof CheckBoxTreeItem) {
                    CheckBoxTreeItem<?> cbti = (CheckBoxTreeItem) treeItem;
                    selectCount += (!cbti.isSelected() || cbti.isIndeterminate()) ? 0 : 1;
                    indeterminateCount += cbti.isIndeterminate() ? 1 : 0;
                }
            }
            if (selectCount == parent.getChildren().size()) {
                parent.setSelected(true);
                parent.setIndeterminate(false);
            } else if (selectCount == 0 && indeterminateCount == 0) {
                parent.setSelected(false);
                parent.setIndeterminate(false);
            } else {
                parent.setIndeterminate(true);
            }
        }
    }

    private void updateDownwards() {
        if (!isLeaf()) {
            for (TreeItem<T> child : getChildren()) {
                if (child instanceof CheckBoxTreeItem) {
                    CheckBoxTreeItem<T> cbti = (CheckBoxTreeItem) child;
                    cbti.setSelected(isSelected());
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void fireEvent(CheckBoxTreeItem<T> item, boolean selectionChanged) {
        Event evt = new TreeModificationEvent(CHECK_BOX_SELECTION_CHANGED_EVENT, item, selectionChanged);
        Event.fireEvent(this, evt);
    }

    /* loaded from: jfxrt.jar:javafx/scene/control/CheckBoxTreeItem$TreeModificationEvent.class */
    public static class TreeModificationEvent<T> extends Event {
        private static final long serialVersionUID = -8445355590698862999L;
        private final transient CheckBoxTreeItem<T> treeItem;
        private final boolean selectionChanged;
        public static final EventType<Event> ANY = new EventType<>(Event.ANY, "TREE_MODIFICATION");

        public TreeModificationEvent(EventType<? extends Event> eventType, CheckBoxTreeItem<T> treeItem, boolean selectionChanged) {
            super(eventType);
            this.treeItem = treeItem;
            this.selectionChanged = selectionChanged;
        }

        public CheckBoxTreeItem<T> getTreeItem() {
            return this.treeItem;
        }

        public boolean wasSelectionChanged() {
            return this.selectionChanged;
        }

        public boolean wasIndeterminateChanged() {
            return !this.selectionChanged;
        }
    }
}
