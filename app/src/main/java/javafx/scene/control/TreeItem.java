package javafx.scene.control;

import com.sun.javafx.event.EventHandlerManager;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.BooleanPropertyBase;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ObjectPropertyBase;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.ReadOnlyBooleanWrapper;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.event.EventDispatchChain;
import javafx.event.EventHandler;
import javafx.event.EventTarget;
import javafx.event.EventType;
import javafx.scene.Node;

/* loaded from: jfxrt.jar:javafx/scene/control/TreeItem.class */
public class TreeItem<T> implements EventTarget {
    private static final EventType<?> TREE_NOTIFICATION_EVENT = new EventType<>(Event.ANY, "TreeNotificationEvent");
    private static final EventType<?> EXPANDED_ITEM_COUNT_CHANGE_EVENT = new EventType<>(treeNotificationEvent(), "ExpandedItemCountChangeEvent");
    private static final EventType<?> BRANCH_EXPANDED_EVENT = new EventType<>(expandedItemCountChangeEvent(), "BranchExpandedEvent");
    private static final EventType<?> BRANCH_COLLAPSED_EVENT = new EventType<>(expandedItemCountChangeEvent(), "BranchCollapsedEvent");
    private static final EventType<?> CHILDREN_MODIFICATION_EVENT = new EventType<>(expandedItemCountChangeEvent(), "ChildrenModificationEvent");
    private static final EventType<?> VALUE_CHANGED_EVENT = new EventType<>(treeNotificationEvent(), "ValueChangedEvent");
    private static final EventType<?> GRAPHIC_CHANGED_EVENT = new EventType<>(treeNotificationEvent(), "GraphicChangedEvent");
    private final EventHandler<TreeModificationEvent<Object>> itemListener;
    private boolean ignoreSortUpdate;
    private boolean expandedDescendentCountDirty;
    ObservableList<TreeItem<T>> children;
    private final EventHandlerManager eventHandlerManager;
    private int expandedDescendentCount;
    int previousExpandedDescendentCount;
    Comparator<TreeItem<T>> lastComparator;
    TreeSortMode lastSortMode;
    private int parentLinkCount;
    private ListChangeListener<TreeItem<T>> childrenListener;
    private ObjectProperty<T> value;
    private ObjectProperty<Node> graphic;
    private BooleanProperty expanded;
    private ReadOnlyBooleanWrapper leaf;
    private ReadOnlyObjectWrapper<TreeItem<T>> parent;

    public static <T> EventType<TreeModificationEvent<T>> treeNotificationEvent() {
        return (EventType<TreeModificationEvent<T>>) TREE_NOTIFICATION_EVENT;
    }

    public static <T> EventType<TreeModificationEvent<T>> expandedItemCountChangeEvent() {
        return (EventType<TreeModificationEvent<T>>) EXPANDED_ITEM_COUNT_CHANGE_EVENT;
    }

    public static <T> EventType<TreeModificationEvent<T>> branchExpandedEvent() {
        return (EventType<TreeModificationEvent<T>>) BRANCH_EXPANDED_EVENT;
    }

    public static <T> EventType<TreeModificationEvent<T>> branchCollapsedEvent() {
        return (EventType<TreeModificationEvent<T>>) BRANCH_COLLAPSED_EVENT;
    }

    public static <T> EventType<TreeModificationEvent<T>> childrenModificationEvent() {
        return (EventType<TreeModificationEvent<T>>) CHILDREN_MODIFICATION_EVENT;
    }

    public static <T> EventType<TreeModificationEvent<T>> valueChangedEvent() {
        return (EventType<TreeModificationEvent<T>>) VALUE_CHANGED_EVENT;
    }

    public static <T> EventType<TreeModificationEvent<T>> graphicChangedEvent() {
        return (EventType<TreeModificationEvent<T>>) GRAPHIC_CHANGED_EVENT;
    }

    public TreeItem() {
        this(null);
    }

    public TreeItem(T value) {
        this(value, (Node) null);
    }

    public TreeItem(T value, Node graphic) {
        this.itemListener = new EventHandler<TreeModificationEvent<Object>>() { // from class: javafx.scene.control.TreeItem.1
            @Override // javafx.event.EventHandler
            public void handle(TreeModificationEvent<Object> event) {
                TreeItem.this.expandedDescendentCountDirty = true;
            }
        };
        this.ignoreSortUpdate = false;
        this.expandedDescendentCountDirty = true;
        this.eventHandlerManager = new EventHandlerManager(this);
        this.expandedDescendentCount = 1;
        this.previousExpandedDescendentCount = 1;
        this.lastComparator = null;
        this.lastSortMode = null;
        this.parentLinkCount = 0;
        this.childrenListener = c2 -> {
            this.expandedDescendentCountDirty = true;
            updateChildren(c2);
        };
        this.parent = new ReadOnlyObjectWrapper<>(this, "parent");
        setValue(value);
        setGraphic(graphic);
        addEventHandler(expandedItemCountChangeEvent(), this.itemListener);
    }

    public final void setValue(T value) {
        valueProperty().setValue(value);
    }

    public final T getValue() {
        if (this.value == null) {
            return null;
        }
        return this.value.getValue2();
    }

    public final ObjectProperty<T> valueProperty() {
        if (this.value == null) {
            this.value = new ObjectPropertyBase<T>() { // from class: javafx.scene.control.TreeItem.2
                @Override // javafx.beans.property.ObjectPropertyBase
                protected void invalidated() {
                    TreeItem.this.fireEvent(new TreeModificationEvent((EventType<? extends Event>) TreeItem.VALUE_CHANGED_EVENT, TreeItem.this, get()));
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return TreeItem.this;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return "value";
                }
            };
        }
        return this.value;
    }

    public final void setGraphic(Node value) {
        graphicProperty().setValue(value);
    }

    public final Node getGraphic() {
        if (this.graphic == null) {
            return null;
        }
        return this.graphic.getValue2();
    }

    public final ObjectProperty<Node> graphicProperty() {
        if (this.graphic == null) {
            this.graphic = new ObjectPropertyBase<Node>() { // from class: javafx.scene.control.TreeItem.3
                @Override // javafx.beans.property.ObjectPropertyBase
                protected void invalidated() {
                    TreeItem.this.fireEvent(new TreeModificationEvent(TreeItem.GRAPHIC_CHANGED_EVENT, TreeItem.this));
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return TreeItem.this;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return "graphic";
                }
            };
        }
        return this.graphic;
    }

    public final void setExpanded(boolean value) {
        if (value || this.expanded != null) {
            expandedProperty().setValue(Boolean.valueOf(value));
        }
    }

    public final boolean isExpanded() {
        if (this.expanded == null) {
            return false;
        }
        return this.expanded.getValue2().booleanValue();
    }

    public final BooleanProperty expandedProperty() {
        if (this.expanded == null) {
            this.expanded = new BooleanPropertyBase() { // from class: javafx.scene.control.TreeItem.4
                @Override // javafx.beans.property.BooleanPropertyBase
                protected void invalidated() {
                    if (TreeItem.this.isLeaf()) {
                        return;
                    }
                    EventType<?> evtType = TreeItem.this.isExpanded() ? TreeItem.BRANCH_EXPANDED_EVENT : TreeItem.BRANCH_COLLAPSED_EVENT;
                    TreeItem.this.fireEvent(new TreeModificationEvent((EventType<? extends Event>) evtType, TreeItem.this, TreeItem.this.isExpanded()));
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return TreeItem.this;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return "expanded";
                }
            };
        }
        return this.expanded;
    }

    private void setLeaf(boolean value) {
        if (value && this.leaf == null) {
            return;
        }
        if (this.leaf == null) {
            this.leaf = new ReadOnlyBooleanWrapper(this, "leaf", true);
        }
        this.leaf.setValue(Boolean.valueOf(value));
    }

    public boolean isLeaf() {
        if (this.leaf == null) {
            return true;
        }
        return this.leaf.getValue2().booleanValue();
    }

    public final ReadOnlyBooleanProperty leafProperty() {
        if (this.leaf == null) {
            this.leaf = new ReadOnlyBooleanWrapper(this, "leaf", true);
        }
        return this.leaf.getReadOnlyProperty();
    }

    private void setParent(TreeItem<T> value) {
        this.parent.setValue(value);
    }

    public final TreeItem<T> getParent() {
        if (this.parent == null) {
            return null;
        }
        return this.parent.getValue2();
    }

    public final ReadOnlyObjectProperty<TreeItem<T>> parentProperty() {
        return this.parent.getReadOnlyProperty();
    }

    public ObservableList<TreeItem<T>> getChildren() {
        if (this.children == null) {
            this.children = FXCollections.observableArrayList();
            this.children.addListener(this.childrenListener);
        }
        if (this.children.isEmpty()) {
            return this.children;
        }
        if (!this.ignoreSortUpdate) {
            checkSortState();
        }
        return this.children;
    }

    public TreeItem<T> previousSibling() {
        return previousSibling(this);
    }

    public TreeItem<T> previousSibling(TreeItem<T> beforeNode) {
        if (getParent() == null || beforeNode == null) {
            return null;
        }
        List<TreeItem<T>> parentChildren = getParent().getChildren();
        int childCount = parentChildren.size();
        for (int i2 = 0; i2 < childCount; i2++) {
            if (beforeNode.equals(parentChildren.get(i2))) {
                int pos = i2 - 1;
                if (pos < 0) {
                    return null;
                }
                return parentChildren.get(pos);
            }
        }
        return null;
    }

    public TreeItem<T> nextSibling() {
        return nextSibling(this);
    }

    public TreeItem<T> nextSibling(TreeItem<T> afterNode) {
        if (getParent() == null || afterNode == null) {
            return null;
        }
        List<TreeItem<T>> parentChildren = getParent().getChildren();
        int childCount = parentChildren.size();
        for (int i2 = 0; i2 < childCount; i2++) {
            if (afterNode.equals(parentChildren.get(i2))) {
                int pos = i2 + 1;
                if (pos >= childCount) {
                    return null;
                }
                return parentChildren.get(pos);
            }
        }
        return null;
    }

    public String toString() {
        return "TreeItem [ value: " + ((Object) getValue()) + " ]";
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void fireEvent(TreeModificationEvent<T> evt) {
        Event.fireEvent(this, evt);
    }

    @Override // javafx.event.EventTarget
    public EventDispatchChain buildEventDispatchChain(EventDispatchChain tail) {
        if (getParent() != null) {
            getParent().buildEventDispatchChain(tail);
        }
        return tail.append(this.eventHandlerManager);
    }

    public <E extends Event> void addEventHandler(EventType<E> eventType, EventHandler<E> eventHandler) {
        this.eventHandlerManager.addEventHandler(eventType, eventHandler);
    }

    public <E extends Event> void removeEventHandler(EventType<E> eventType, EventHandler<E> eventHandler) {
        this.eventHandlerManager.removeEventHandler(eventType, eventHandler);
    }

    void sort() {
        sort(this.children, this.lastComparator, this.lastSortMode);
    }

    private void sort(ObservableList<TreeItem<T>> children, Comparator<TreeItem<T>> comparator, TreeSortMode sortMode) {
        if (comparator == null) {
            return;
        }
        runSort(children, comparator, sortMode);
        if (getParent() == null) {
            TreeModificationEvent<T> e2 = new TreeModificationEvent<>(childrenModificationEvent(), this);
            ((TreeModificationEvent) e2).wasPermutated = true;
            fireEvent(e2);
        }
    }

    private void checkSortState() {
        TreeItem<T> rootNode = getRoot();
        TreeSortMode sortMode = rootNode.lastSortMode;
        Comparator<TreeItem<T>> comparator = rootNode.lastComparator;
        if (comparator != null && comparator != this.lastComparator) {
            this.lastComparator = comparator;
            runSort(this.children, comparator, sortMode);
        }
    }

    private void runSort(ObservableList<TreeItem<T>> children, Comparator<TreeItem<T>> comparator, TreeSortMode sortMode) {
        if (sortMode == TreeSortMode.ALL_DESCENDANTS) {
            doSort(children, comparator);
        } else if (sortMode == TreeSortMode.ONLY_FIRST_LEVEL && getParent() == null) {
            doSort(children, comparator);
        }
    }

    private TreeItem<T> getRoot() {
        TreeItem<T> parent = getParent();
        if (parent == null) {
            return this;
        }
        while (true) {
            TreeItem<T> newParent = parent.getParent();
            if (newParent == null) {
                return parent;
            }
            parent = newParent;
        }
    }

    private void doSort(ObservableList<TreeItem<T>> children, Comparator<TreeItem<T>> comparator) {
        if (!isLeaf() && isExpanded()) {
            FXCollections.sort(children, comparator);
        }
    }

    int getExpandedDescendentCount(boolean reset) {
        if (reset || this.expandedDescendentCountDirty) {
            updateExpandedDescendentCount(reset);
            this.expandedDescendentCountDirty = false;
        }
        return this.expandedDescendentCount;
    }

    private void updateExpandedDescendentCount(boolean reset) {
        this.previousExpandedDescendentCount = this.expandedDescendentCount;
        this.expandedDescendentCount = 1;
        this.ignoreSortUpdate = true;
        if (!isLeaf() && isExpanded()) {
            for (TreeItem<T> child : getChildren()) {
                if (child != null) {
                    this.expandedDescendentCount += child.isExpanded() ? child.getExpandedDescendentCount(reset) : 1;
                }
            }
        }
        this.ignoreSortUpdate = false;
    }

    private void updateChildren(ListChangeListener.Change<? extends TreeItem<T>> c2) {
        setLeaf(this.children.isEmpty());
        List<TreeItem<T>> added = new ArrayList<>();
        List<TreeItem<T>> removed = new ArrayList<>();
        while (c2.next()) {
            added.addAll(c2.getAddedSubList());
            removed.addAll(c2.getRemoved());
        }
        updateChildrenParent(removed, null);
        updateChildrenParent(added, this);
        c2.reset();
        fireEvent(new TreeModificationEvent<>(CHILDREN_MODIFICATION_EVENT, this, added, removed, c2));
    }

    private static <T> void updateChildrenParent(List<? extends TreeItem<T>> treeItems, TreeItem<T> newParent) {
        if (treeItems == null) {
            return;
        }
        for (TreeItem<T> treeItem : treeItems) {
            if (treeItem != null) {
                TreeItem<T> currentParent = treeItem.getParent();
                if (((TreeItem) treeItem).parentLinkCount == 0) {
                    treeItem.setParent(newParent);
                }
                boolean parentMatch = currentParent != null && currentParent.equals(newParent);
                if (parentMatch) {
                    if (newParent == null) {
                        ((TreeItem) treeItem).parentLinkCount--;
                    } else {
                        ((TreeItem) treeItem).parentLinkCount++;
                    }
                }
            }
        }
    }

    /* loaded from: jfxrt.jar:javafx/scene/control/TreeItem$TreeModificationEvent.class */
    public static class TreeModificationEvent<T> extends Event {
        private static final long serialVersionUID = 4741889985221719579L;
        public static final EventType<?> ANY = TreeItem.TREE_NOTIFICATION_EVENT;
        private final transient TreeItem<T> treeItem;
        private final T newValue;
        private final List<? extends TreeItem<T>> added;
        private final List<? extends TreeItem<T>> removed;
        private final ListChangeListener.Change<? extends TreeItem<T>> change;
        private final boolean wasExpanded;
        private final boolean wasCollapsed;
        private boolean wasPermutated;

        public TreeModificationEvent(EventType<? extends Event> eventType, TreeItem<T> treeItem) {
            this(eventType, treeItem, (Object) null);
        }

        public TreeModificationEvent(EventType<? extends Event> eventType, TreeItem<T> treeItem, T newValue) {
            super(eventType);
            this.treeItem = treeItem;
            this.newValue = newValue;
            this.added = null;
            this.removed = null;
            this.change = null;
            this.wasExpanded = false;
            this.wasCollapsed = false;
        }

        public TreeModificationEvent(EventType<? extends Event> eventType, TreeItem<T> treeItem, boolean expanded) {
            super(eventType);
            this.treeItem = treeItem;
            this.newValue = null;
            this.added = null;
            this.removed = null;
            this.change = null;
            this.wasExpanded = expanded;
            this.wasCollapsed = !expanded;
        }

        public TreeModificationEvent(EventType<? extends Event> eventType, TreeItem<T> treeItem, List<? extends TreeItem<T>> added, List<? extends TreeItem<T>> removed) {
            this(eventType, treeItem, added, removed, null);
        }

        private TreeModificationEvent(EventType<? extends Event> eventType, TreeItem<T> treeItem, List<? extends TreeItem<T>> added, List<? extends TreeItem<T>> removed, ListChangeListener.Change<? extends TreeItem<T>> change) {
            super(eventType);
            this.treeItem = treeItem;
            this.newValue = null;
            this.added = added;
            this.removed = removed;
            this.change = change;
            this.wasExpanded = false;
            this.wasCollapsed = false;
            this.wasPermutated = added != null && removed != null && added.size() == removed.size() && added.containsAll(removed);
        }

        @Override // java.util.EventObject
        public TreeItem<T> getSource() {
            return this.treeItem;
        }

        public TreeItem<T> getTreeItem() {
            return this.treeItem;
        }

        public T getNewValue() {
            return this.newValue;
        }

        public List<? extends TreeItem<T>> getAddedChildren() {
            return this.added == null ? Collections.emptyList() : this.added;
        }

        public List<? extends TreeItem<T>> getRemovedChildren() {
            return this.removed == null ? Collections.emptyList() : this.removed;
        }

        public int getRemovedSize() {
            return getRemovedChildren().size();
        }

        public int getAddedSize() {
            return getAddedChildren().size();
        }

        public boolean wasExpanded() {
            return this.wasExpanded;
        }

        public boolean wasCollapsed() {
            return this.wasCollapsed;
        }

        public boolean wasAdded() {
            return getAddedSize() > 0;
        }

        public boolean wasRemoved() {
            return getRemovedSize() > 0;
        }

        public boolean wasPermutated() {
            return this.wasPermutated;
        }

        int getFrom() {
            if (this.change == null) {
                return -1;
            }
            return this.change.getFrom();
        }

        int getTo() {
            if (this.change == null) {
                return -1;
            }
            return this.change.getTo();
        }

        ListChangeListener.Change<? extends TreeItem<T>> getChange() {
            return this.change;
        }
    }
}
