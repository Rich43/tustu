package javafx.scene.control;

import com.sun.javafx.collections.NonIterableChange;
import com.sun.javafx.css.converters.SizeConverter;
import com.sun.javafx.scene.control.behavior.TreeCellBehavior;
import com.sun.javafx.scene.control.skin.TreeViewSkin;
import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javafx.application.Platform;
import javafx.beans.DefaultProperty;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ObjectPropertyBase;
import javafx.beans.property.ReadOnlyIntegerProperty;
import javafx.beans.property.ReadOnlyIntegerWrapper;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.WeakChangeListener;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.css.CssMetaData;
import javafx.css.Styleable;
import javafx.css.StyleableDoubleProperty;
import javafx.css.StyleableProperty;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.event.WeakEventHandler;
import javafx.scene.AccessibleAttribute;
import javafx.scene.AccessibleRole;
import javafx.scene.control.TreeItem;
import javafx.util.Callback;
import javax.swing.JTree;

@DefaultProperty("root")
/* loaded from: jfxrt.jar:javafx/scene/control/TreeView.class */
public class TreeView<T> extends Control {
    private static final EventType<?> EDIT_ANY_EVENT = new EventType<>(Event.ANY, "TREE_VIEW_EDIT");
    private static final EventType<?> EDIT_START_EVENT = new EventType<>(editAnyEvent(), "EDIT_START");
    private static final EventType<?> EDIT_CANCEL_EVENT = new EventType<>(editAnyEvent(), "EDIT_CANCEL");
    private static final EventType<?> EDIT_COMMIT_EVENT = new EventType<>(editAnyEvent(), "EDIT_COMMIT");
    private boolean expandedItemCountDirty;
    private Map<Integer, SoftReference<TreeItem<T>>> treeItemCacheMap;
    private final EventHandler<TreeItem.TreeModificationEvent<T>> rootEvent;
    private WeakEventHandler<TreeItem.TreeModificationEvent<T>> weakRootEventListener;
    private ObjectProperty<Callback<TreeView<T>, TreeCell<T>>> cellFactory;
    private ObjectProperty<TreeItem<T>> root;
    private BooleanProperty showRoot;
    private ObjectProperty<MultipleSelectionModel<TreeItem<T>>> selectionModel;
    private ObjectProperty<FocusModel<TreeItem<T>>> focusModel;
    private ReadOnlyIntegerWrapper expandedItemCount;
    private DoubleProperty fixedCellSize;
    private BooleanProperty editable;
    private ReadOnlyObjectWrapper<TreeItem<T>> editingItem;
    private ObjectProperty<EventHandler<EditEvent<T>>> onEditStart;
    private ObjectProperty<EventHandler<EditEvent<T>>> onEditCommit;
    private ObjectProperty<EventHandler<EditEvent<T>>> onEditCancel;
    private ObjectProperty<EventHandler<ScrollToEvent<Integer>>> onScrollTo;
    private static final String DEFAULT_STYLE_CLASS = "tree-view";

    public static <T> EventType<EditEvent<T>> editAnyEvent() {
        return (EventType<EditEvent<T>>) EDIT_ANY_EVENT;
    }

    public static <T> EventType<EditEvent<T>> editStartEvent() {
        return (EventType<EditEvent<T>>) EDIT_START_EVENT;
    }

    public static <T> EventType<EditEvent<T>> editCancelEvent() {
        return (EventType<EditEvent<T>>) EDIT_CANCEL_EVENT;
    }

    public static <T> EventType<EditEvent<T>> editCommitEvent() {
        return (EventType<EditEvent<T>>) EDIT_COMMIT_EVENT;
    }

    @Deprecated
    public static int getNodeLevel(TreeItem<?> node) {
        if (node == null) {
            return -1;
        }
        int level = 0;
        TreeItem<?> parent = node.getParent();
        while (true) {
            TreeItem<?> parent2 = parent;
            if (parent2 != null) {
                level++;
                parent = parent2.getParent();
            } else {
                return level;
            }
        }
    }

    public TreeView() {
        this(null);
    }

    public TreeView(TreeItem<T> root) {
        this.expandedItemCountDirty = true;
        this.treeItemCacheMap = new HashMap();
        this.rootEvent = e2 -> {
            EventType<?> eventType = e2.getEventType();
            boolean match = false;
            while (true) {
                if (eventType == null) {
                    break;
                }
                if (eventType.equals(TreeItem.expandedItemCountChangeEvent())) {
                    match = true;
                    break;
                }
                eventType = eventType.getSuperType();
            }
            if (match) {
                this.expandedItemCountDirty = true;
                requestLayout();
            }
        };
        this.root = new SimpleObjectProperty<TreeItem<T>>(this, "root") { // from class: javafx.scene.control.TreeView.1
            private WeakReference<TreeItem<T>> weakOldItem;

            @Override // javafx.beans.property.ObjectPropertyBase
            protected void invalidated() {
                TreeItem<T> oldTreeItem = this.weakOldItem == null ? null : this.weakOldItem.get();
                if (oldTreeItem != null && TreeView.this.weakRootEventListener != null) {
                    oldTreeItem.removeEventHandler(TreeItem.treeNotificationEvent(), TreeView.this.weakRootEventListener);
                }
                TreeItem<T> root2 = TreeView.this.getRoot();
                if (root2 != null) {
                    TreeView.this.weakRootEventListener = new WeakEventHandler(TreeView.this.rootEvent);
                    TreeView.this.getRoot().addEventHandler(TreeItem.treeNotificationEvent(), TreeView.this.weakRootEventListener);
                    this.weakOldItem = new WeakReference<>(root2);
                }
                TreeView.this.edit(null);
                TreeView.this.expandedItemCountDirty = true;
                TreeView.this.updateRootExpanded();
            }
        };
        this.expandedItemCount = new ReadOnlyIntegerWrapper(this, "expandedItemCount", 0);
        getStyleClass().setAll(DEFAULT_STYLE_CLASS);
        setAccessibleRole(AccessibleRole.TREE_VIEW);
        setRoot(root);
        updateExpandedItemCount(root);
        MultipleSelectionModel<TreeItem<T>> sm = new TreeViewBitSetSelectionModel<>(this);
        setSelectionModel(sm);
        setFocusModel(new TreeViewFocusModel(this));
    }

    public final void setCellFactory(Callback<TreeView<T>, TreeCell<T>> value) {
        cellFactoryProperty().set(value);
    }

    public final Callback<TreeView<T>, TreeCell<T>> getCellFactory() {
        if (this.cellFactory == null) {
            return null;
        }
        return this.cellFactory.get();
    }

    public final ObjectProperty<Callback<TreeView<T>, TreeCell<T>>> cellFactoryProperty() {
        if (this.cellFactory == null) {
            this.cellFactory = new SimpleObjectProperty(this, "cellFactory");
        }
        return this.cellFactory;
    }

    public final void setRoot(TreeItem<T> value) {
        rootProperty().set(value);
    }

    public final TreeItem<T> getRoot() {
        if (this.root == null) {
            return null;
        }
        return this.root.get();
    }

    public final ObjectProperty<TreeItem<T>> rootProperty() {
        return this.root;
    }

    public final void setShowRoot(boolean value) {
        showRootProperty().set(value);
    }

    public final boolean isShowRoot() {
        if (this.showRoot == null) {
            return true;
        }
        return this.showRoot.get();
    }

    public final BooleanProperty showRootProperty() {
        if (this.showRoot == null) {
            this.showRoot = new SimpleBooleanProperty(this, "showRoot", true) { // from class: javafx.scene.control.TreeView.2
                @Override // javafx.beans.property.BooleanPropertyBase
                protected void invalidated() {
                    TreeView.this.updateRootExpanded();
                    TreeView.this.updateExpandedItemCount(TreeView.this.getRoot());
                }
            };
        }
        return this.showRoot;
    }

    public final void setSelectionModel(MultipleSelectionModel<TreeItem<T>> value) {
        selectionModelProperty().set(value);
    }

    public final MultipleSelectionModel<TreeItem<T>> getSelectionModel() {
        if (this.selectionModel == null) {
            return null;
        }
        return this.selectionModel.get();
    }

    public final ObjectProperty<MultipleSelectionModel<TreeItem<T>>> selectionModelProperty() {
        if (this.selectionModel == null) {
            this.selectionModel = new SimpleObjectProperty(this, "selectionModel");
        }
        return this.selectionModel;
    }

    public final void setFocusModel(FocusModel<TreeItem<T>> value) {
        focusModelProperty().set(value);
    }

    public final FocusModel<TreeItem<T>> getFocusModel() {
        if (this.focusModel == null) {
            return null;
        }
        return this.focusModel.get();
    }

    public final ObjectProperty<FocusModel<TreeItem<T>>> focusModelProperty() {
        if (this.focusModel == null) {
            this.focusModel = new SimpleObjectProperty(this, "focusModel");
        }
        return this.focusModel;
    }

    public final ReadOnlyIntegerProperty expandedItemCountProperty() {
        return this.expandedItemCount.getReadOnlyProperty();
    }

    private void setExpandedItemCount(int value) {
        this.expandedItemCount.set(value);
    }

    public final int getExpandedItemCount() {
        if (this.expandedItemCountDirty) {
            updateExpandedItemCount(getRoot());
        }
        return this.expandedItemCount.get();
    }

    public final void setFixedCellSize(double value) {
        fixedCellSizeProperty().set(value);
    }

    public final double getFixedCellSize() {
        if (this.fixedCellSize == null) {
            return -1.0d;
        }
        return this.fixedCellSize.get();
    }

    public final DoubleProperty fixedCellSizeProperty() {
        if (this.fixedCellSize == null) {
            this.fixedCellSize = new StyleableDoubleProperty(-1.0d) { // from class: javafx.scene.control.TreeView.3
                @Override // javafx.css.StyleableProperty
                public CssMetaData<? extends Styleable, Number> getCssMetaData() {
                    return StyleableProperties.FIXED_CELL_SIZE;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return TreeView.this;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return "fixedCellSize";
                }
            };
        }
        return this.fixedCellSize;
    }

    public final void setEditable(boolean value) {
        editableProperty().set(value);
    }

    public final boolean isEditable() {
        if (this.editable == null) {
            return false;
        }
        return this.editable.get();
    }

    public final BooleanProperty editableProperty() {
        if (this.editable == null) {
            this.editable = new SimpleBooleanProperty(this, JTree.EDITABLE_PROPERTY, false);
        }
        return this.editable;
    }

    private void setEditingItem(TreeItem<T> value) {
        editingItemPropertyImpl().set(value);
    }

    public final TreeItem<T> getEditingItem() {
        if (this.editingItem == null) {
            return null;
        }
        return this.editingItem.get();
    }

    public final ReadOnlyObjectProperty<TreeItem<T>> editingItemProperty() {
        return editingItemPropertyImpl().getReadOnlyProperty();
    }

    private ReadOnlyObjectWrapper<TreeItem<T>> editingItemPropertyImpl() {
        if (this.editingItem == null) {
            this.editingItem = new ReadOnlyObjectWrapper<>(this, "editingItem");
        }
        return this.editingItem;
    }

    public final void setOnEditStart(EventHandler<EditEvent<T>> value) {
        onEditStartProperty().set(value);
    }

    public final EventHandler<EditEvent<T>> getOnEditStart() {
        if (this.onEditStart == null) {
            return null;
        }
        return this.onEditStart.get();
    }

    public final ObjectProperty<EventHandler<EditEvent<T>>> onEditStartProperty() {
        if (this.onEditStart == null) {
            this.onEditStart = new SimpleObjectProperty<EventHandler<EditEvent<T>>>(this, "onEditStart") { // from class: javafx.scene.control.TreeView.4
                @Override // javafx.beans.property.ObjectPropertyBase
                protected void invalidated() {
                    TreeView.this.setEventHandler(TreeView.editStartEvent(), get());
                }
            };
        }
        return this.onEditStart;
    }

    public final void setOnEditCommit(EventHandler<EditEvent<T>> value) {
        onEditCommitProperty().set(value);
    }

    public final EventHandler<EditEvent<T>> getOnEditCommit() {
        if (this.onEditCommit == null) {
            return null;
        }
        return this.onEditCommit.get();
    }

    public final ObjectProperty<EventHandler<EditEvent<T>>> onEditCommitProperty() {
        if (this.onEditCommit == null) {
            this.onEditCommit = new SimpleObjectProperty<EventHandler<EditEvent<T>>>(this, "onEditCommit") { // from class: javafx.scene.control.TreeView.5
                @Override // javafx.beans.property.ObjectPropertyBase
                protected void invalidated() {
                    TreeView.this.setEventHandler(TreeView.editCommitEvent(), get());
                }
            };
        }
        return this.onEditCommit;
    }

    public final void setOnEditCancel(EventHandler<EditEvent<T>> value) {
        onEditCancelProperty().set(value);
    }

    public final EventHandler<EditEvent<T>> getOnEditCancel() {
        if (this.onEditCancel == null) {
            return null;
        }
        return this.onEditCancel.get();
    }

    public final ObjectProperty<EventHandler<EditEvent<T>>> onEditCancelProperty() {
        if (this.onEditCancel == null) {
            this.onEditCancel = new SimpleObjectProperty<EventHandler<EditEvent<T>>>(this, "onEditCancel") { // from class: javafx.scene.control.TreeView.6
                @Override // javafx.beans.property.ObjectPropertyBase
                protected void invalidated() {
                    TreeView.this.setEventHandler(TreeView.editCancelEvent(), get());
                }
            };
        }
        return this.onEditCancel;
    }

    @Override // javafx.scene.control.Control, javafx.scene.Parent
    protected void layoutChildren() {
        if (this.expandedItemCountDirty) {
            updateExpandedItemCount(getRoot());
        }
        super.layoutChildren();
    }

    public void edit(TreeItem<T> item) {
        if (isEditable()) {
            setEditingItem(item);
        }
    }

    public void scrollTo(int index) {
        ControlUtils.scrollToIndex(this, index);
    }

    public void setOnScrollTo(EventHandler<ScrollToEvent<Integer>> value) {
        onScrollToProperty().set(value);
    }

    public EventHandler<ScrollToEvent<Integer>> getOnScrollTo() {
        if (this.onScrollTo != null) {
            return this.onScrollTo.get();
        }
        return null;
    }

    public ObjectProperty<EventHandler<ScrollToEvent<Integer>>> onScrollToProperty() {
        if (this.onScrollTo == null) {
            this.onScrollTo = new ObjectPropertyBase<EventHandler<ScrollToEvent<Integer>>>() { // from class: javafx.scene.control.TreeView.7
                @Override // javafx.beans.property.ObjectPropertyBase
                protected void invalidated() {
                    TreeView.this.setEventHandler(ScrollToEvent.scrollToTopIndex(), get());
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return TreeView.this;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return "onScrollTo";
                }
            };
        }
        return this.onScrollTo;
    }

    public int getRow(TreeItem<T> item) {
        return TreeUtil.getRow(item, getRoot(), this.expandedItemCountDirty, isShowRoot());
    }

    public TreeItem<T> getTreeItem(int row) {
        if (row < 0) {
            return null;
        }
        int _row = isShowRoot() ? row : row + 1;
        if (this.expandedItemCountDirty) {
            updateExpandedItemCount(getRoot());
        } else if (this.treeItemCacheMap.containsKey(Integer.valueOf(_row))) {
            SoftReference<TreeItem<T>> treeItemRef = this.treeItemCacheMap.get(Integer.valueOf(_row));
            TreeItem<T> treeItem = treeItemRef.get();
            if (treeItem != null) {
                return treeItem;
            }
        }
        TreeItem<T> treeItem2 = TreeUtil.getItem(getRoot(), _row, this.expandedItemCountDirty);
        this.treeItemCacheMap.put(Integer.valueOf(_row), new SoftReference<>(treeItem2));
        return treeItem2;
    }

    public int getTreeItemLevel(TreeItem<?> node) {
        TreeItem<T> root = getRoot();
        if (node == null) {
            return -1;
        }
        if (node == root) {
            return 0;
        }
        int level = 0;
        TreeItem<T> parent = node.getParent();
        while (true) {
            TreeItem<T> treeItem = parent;
            if (treeItem == null) {
                break;
            }
            level++;
            if (treeItem == root) {
                break;
            }
            parent = treeItem.getParent();
        }
        return level;
    }

    @Override // javafx.scene.control.Control
    protected Skin<?> createDefaultSkin() {
        return new TreeViewSkin(this);
    }

    public void refresh() {
        getProperties().put(TreeViewSkin.RECREATE, Boolean.TRUE);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateExpandedItemCount(TreeItem<T> treeItem) {
        setExpandedItemCount(TreeUtil.updateExpandedItemCount(treeItem, this.expandedItemCountDirty, isShowRoot()));
        if (this.expandedItemCountDirty) {
            this.treeItemCacheMap.clear();
        }
        this.expandedItemCountDirty = false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateRootExpanded() {
        if (!isShowRoot() && getRoot() != null && !getRoot().isExpanded()) {
            getRoot().setExpanded(true);
        }
    }

    /* loaded from: jfxrt.jar:javafx/scene/control/TreeView$StyleableProperties.class */
    private static class StyleableProperties {
        private static final CssMetaData<TreeView<?>, Number> FIXED_CELL_SIZE = new CssMetaData<TreeView<?>, Number>("-fx-fixed-cell-size", SizeConverter.getInstance(), Double.valueOf(-1.0d)) { // from class: javafx.scene.control.TreeView.StyleableProperties.1
            @Override // javafx.css.CssMetaData
            public Double getInitialValue(TreeView<?> node) {
                return Double.valueOf(node.getFixedCellSize());
            }

            @Override // javafx.css.CssMetaData
            public boolean isSettable(TreeView<?> n2) {
                return ((TreeView) n2).fixedCellSize == null || !((TreeView) n2).fixedCellSize.isBound();
            }

            @Override // javafx.css.CssMetaData
            public StyleableProperty<Number> getStyleableProperty(TreeView<?> n2) {
                return (StyleableProperty) n2.fixedCellSizeProperty();
            }
        };
        private static final List<CssMetaData<? extends Styleable, ?>> STYLEABLES;

        private StyleableProperties() {
        }

        static {
            List<CssMetaData<? extends Styleable, ?>> styleables = new ArrayList<>(Control.getClassCssMetaData());
            styleables.add(FIXED_CELL_SIZE);
            STYLEABLES = Collections.unmodifiableList(styleables);
        }
    }

    public static List<CssMetaData<? extends Styleable, ?>> getClassCssMetaData() {
        return StyleableProperties.STYLEABLES;
    }

    @Override // javafx.scene.control.Control
    public List<CssMetaData<? extends Styleable, ?>> getControlCssMetaData() {
        return getClassCssMetaData();
    }

    @Override // javafx.scene.control.Control, javafx.scene.Parent, javafx.scene.Node
    public Object queryAccessibleAttribute(AccessibleAttribute attribute, Object... parameters) {
        switch (attribute) {
            case MULTIPLE_SELECTION:
                MultipleSelectionModel<TreeItem<T>> sm = getSelectionModel();
                return Boolean.valueOf(sm != null && sm.getSelectionMode() == SelectionMode.MULTIPLE);
            case ROW_COUNT:
                return Integer.valueOf(getExpandedItemCount());
            default:
                return super.queryAccessibleAttribute(attribute, parameters);
        }
    }

    /* loaded from: jfxrt.jar:javafx/scene/control/TreeView$EditEvent.class */
    public static class EditEvent<T> extends Event {
        private static final long serialVersionUID = -4437033058917528976L;
        public static final EventType<?> ANY = TreeView.EDIT_ANY_EVENT;
        private final T oldValue;
        private final T newValue;
        private final transient TreeItem<T> treeItem;

        public EditEvent(TreeView<T> source, EventType<? extends EditEvent> eventType, TreeItem<T> treeItem, T oldValue, T newValue) {
            super(source, Event.NULL_SOURCE_TARGET, eventType);
            this.oldValue = oldValue;
            this.newValue = newValue;
            this.treeItem = treeItem;
        }

        @Override // java.util.EventObject
        public TreeView<T> getSource() {
            return (TreeView) super.getSource();
        }

        public TreeItem<T> getTreeItem() {
            return this.treeItem;
        }

        public T getNewValue() {
            return this.newValue;
        }

        public T getOldValue() {
            return this.oldValue;
        }
    }

    /* loaded from: jfxrt.jar:javafx/scene/control/TreeView$TreeViewBitSetSelectionModel.class */
    static class TreeViewBitSetSelectionModel<T> extends MultipleSelectionModelBase<TreeItem<T>> {
        private TreeView<T> treeView;
        private ChangeListener<TreeItem<T>> rootPropertyListener = (observable, oldValue, newValue) -> {
            updateDefaultSelection();
            updateTreeEventListener(oldValue, newValue);
        };
        private EventHandler<TreeItem.TreeModificationEvent<T>> treeItemListener = treeModificationEvent -> {
            TreeItem<T> treeItem;
            Integer num;
            if ((getSelectedIndex() == -1 && getSelectedItem() == null) || (treeItem = treeModificationEvent.getTreeItem()) == null) {
                return;
            }
            ((TreeView) this.treeView).expandedItemCountDirty = true;
            int row = this.treeView.getRow(treeItem);
            int expandedDescendentCount = 0;
            ListChangeListener.Change<? extends TreeItem<T>> change = treeModificationEvent.getChange();
            if (change != null) {
                change.next();
            }
            do {
                int addedSize = change == null ? 0 : change.getAddedSize();
                int removedSize = change == null ? 0 : change.getRemovedSize();
                if (treeModificationEvent.wasExpanded()) {
                    expandedDescendentCount += treeItem.getExpandedDescendentCount(false) - 1;
                    row++;
                } else if (treeModificationEvent.wasCollapsed()) {
                    treeItem.getExpandedDescendentCount(false);
                    int i2 = treeItem.previousExpandedDescendentCount;
                    int selectedIndex = getSelectedIndex();
                    boolean z2 = selectedIndex >= row + 1 && selectedIndex < row + i2;
                    boolean z3 = false;
                    startAtomic();
                    int i3 = row + 1;
                    int i4 = row + i2;
                    ArrayList arrayList = new ArrayList();
                    for (int i5 = i3; i5 < i4; i5++) {
                        if (isSelected(i5)) {
                            z3 = true;
                            clearSelection(i5);
                            arrayList.add(Integer.valueOf(i5));
                        }
                    }
                    stopAtomic();
                    if (z2 && z3) {
                        select(row);
                    } else {
                        this.selectedIndicesSeq.callObservers(new NonIterableChange.GenericAddRemoveChange(i3, i3, arrayList, this.selectedIndicesSeq));
                    }
                    expandedDescendentCount += (-i2) + 1;
                    row++;
                } else if (!treeModificationEvent.wasPermutated()) {
                    if (treeModificationEvent.wasAdded()) {
                        expandedDescendentCount += treeItem.isExpanded() ? addedSize : 0;
                        row = this.treeView.getRow(treeModificationEvent.getChange().getAddedSubList().get(0));
                    } else if (treeModificationEvent.wasRemoved()) {
                        expandedDescendentCount += treeItem.isExpanded() ? -removedSize : 0;
                        row += treeModificationEvent.getFrom() + 1;
                        ObservableList<Integer> selectedIndices = getSelectedIndices();
                        int selectedIndex2 = getSelectedIndex();
                        ObservableList<T> selectedItems = getSelectedItems();
                        TreeItem treeItem2 = (TreeItem) getSelectedItem();
                        List<? extends TreeItem<T>> removed = treeModificationEvent.getChange().getRemoved();
                        for (int i6 = 0; i6 < selectedIndices.size() && !selectedItems.isEmpty() && selectedIndices.get(i6).intValue() <= selectedItems.size(); i6++) {
                            if (removed.size() == 1 && selectedItems.size() == 1 && treeItem2 != null && treeItem2.equals(removed.get(0)) && selectedIndex2 < getItemCount()) {
                                TreeItem<T> modelItem = getModelItem(selectedIndex2 == 0 ? 0 : selectedIndex2 - 1);
                                if (!treeItem2.equals(modelItem)) {
                                    select((TreeItem) modelItem);
                                }
                            }
                        }
                    }
                }
                if (treeModificationEvent.getChange() == null) {
                    break;
                }
            } while (treeModificationEvent.getChange().next());
            shiftSelection(row, expandedDescendentCount, null);
            if ((treeModificationEvent.wasAdded() || treeModificationEvent.wasRemoved()) && (num = (Integer) TreeCellBehavior.getAnchor(this.treeView, null)) != null && isSelected(num.intValue() + expandedDescendentCount)) {
                TreeCellBehavior.setAnchor(this.treeView, Integer.valueOf(num.intValue() + expandedDescendentCount), false);
            }
        };
        private WeakChangeListener<TreeItem<T>> weakRootPropertyListener = new WeakChangeListener<>(this.rootPropertyListener);
        private WeakEventHandler<TreeItem.TreeModificationEvent<T>> weakTreeItemListener;

        public TreeViewBitSetSelectionModel(TreeView<T> treeView) {
            this.treeView = null;
            if (treeView == null) {
                throw new IllegalArgumentException("TreeView can not be null");
            }
            this.treeView = treeView;
            this.treeView.rootProperty().addListener(this.weakRootPropertyListener);
            this.treeView.showRootProperty().addListener(o2 -> {
                shiftSelection(0, treeView.isShowRoot() ? 1 : -1, null);
            });
            updateTreeEventListener(null, treeView.getRoot());
            updateDefaultSelection();
        }

        private void updateTreeEventListener(TreeItem<T> oldRoot, TreeItem<T> newRoot) {
            if (oldRoot != null && this.weakTreeItemListener != null) {
                oldRoot.removeEventHandler(TreeItem.expandedItemCountChangeEvent(), this.weakTreeItemListener);
            }
            if (newRoot != null) {
                this.weakTreeItemListener = new WeakEventHandler<>(this.treeItemListener);
                newRoot.addEventHandler(TreeItem.expandedItemCountChangeEvent(), this.weakTreeItemListener);
            }
        }

        @Override // javafx.scene.control.MultipleSelectionModelBase, javafx.scene.control.MultipleSelectionModel
        public void selectAll() {
            int anchor = ((Integer) TreeCellBehavior.getAnchor(this.treeView, -1)).intValue();
            super.selectAll();
            TreeCellBehavior.setAnchor(this.treeView, Integer.valueOf(anchor), false);
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // javafx.scene.control.MultipleSelectionModelBase, javafx.scene.control.SelectionModel
        public void select(TreeItem<T> treeItem) {
            if (treeItem == 0 && getSelectionMode() == SelectionMode.SINGLE) {
                clearSelection();
                return;
            }
            if (treeItem != 0) {
                TreeItem<?> parent = treeItem.getParent();
                while (true) {
                    TreeItem<?> item = parent;
                    if (item == null) {
                        break;
                    }
                    item.setExpanded(true);
                    parent = item.getParent();
                }
            }
            this.treeView.updateExpandedItemCount(this.treeView.getRoot());
            int row = this.treeView.getRow(treeItem);
            if (row == -1) {
                setSelectedIndex(-1);
                setSelectedItem(treeItem);
            } else {
                select(row);
            }
        }

        @Override // javafx.scene.control.MultipleSelectionModelBase, javafx.scene.control.SelectionModel
        public void clearAndSelect(int row) {
            TreeCellBehavior.setAnchor(this.treeView, Integer.valueOf(row), false);
            super.clearAndSelect(row);
        }

        @Override // javafx.scene.control.MultipleSelectionModelBase
        protected void focus(int itemIndex) {
            if (this.treeView.getFocusModel() != null) {
                this.treeView.getFocusModel().focus(itemIndex);
            }
            this.treeView.notifyAccessibleAttributeChanged(AccessibleAttribute.FOCUS_ITEM);
        }

        @Override // javafx.scene.control.MultipleSelectionModelBase
        protected int getFocusedIndex() {
            if (this.treeView.getFocusModel() == null) {
                return -1;
            }
            return this.treeView.getFocusModel().getFocusedIndex();
        }

        @Override // javafx.scene.control.MultipleSelectionModelBase
        protected int getItemCount() {
            if (this.treeView == null) {
                return 0;
            }
            return this.treeView.getExpandedItemCount();
        }

        @Override // javafx.scene.control.MultipleSelectionModelBase
        public TreeItem<T> getModelItem(int index) {
            if (this.treeView != null && index >= 0 && index < this.treeView.getExpandedItemCount()) {
                return this.treeView.getTreeItem(index);
            }
            return null;
        }

        private void updateDefaultSelection() {
            clearSelection();
            focus(getItemCount() > 0 ? 0 : -1);
        }
    }

    /* loaded from: jfxrt.jar:javafx/scene/control/TreeView$TreeViewFocusModel.class */
    static class TreeViewFocusModel<T> extends FocusModel<TreeItem<T>> {
        private final TreeView<T> treeView;
        private final ChangeListener<TreeItem<T>> rootPropertyListener = (observable, oldValue, newValue) -> {
            updateTreeEventListener(oldValue, newValue);
        };
        private final WeakChangeListener<TreeItem<T>> weakRootPropertyListener = new WeakChangeListener<>(this.rootPropertyListener);
        private EventHandler<TreeItem.TreeModificationEvent<T>> treeItemListener = new EventHandler<TreeItem.TreeModificationEvent<T>>() { // from class: javafx.scene.control.TreeView.TreeViewFocusModel.1
            @Override // javafx.event.EventHandler
            public void handle(TreeItem.TreeModificationEvent<T> e2) {
                int newFocus;
                if (TreeViewFocusModel.this.getFocusedIndex() == -1) {
                    return;
                }
                int row = TreeViewFocusModel.this.treeView.getRow(e2.getTreeItem());
                int shift = 0;
                if (e2.getChange() != null) {
                    e2.getChange().next();
                }
                do {
                    if (e2.wasExpanded()) {
                        if (row < TreeViewFocusModel.this.getFocusedIndex()) {
                            shift += e2.getTreeItem().getExpandedDescendentCount(false) - 1;
                        }
                    } else if (e2.wasCollapsed()) {
                        if (row < TreeViewFocusModel.this.getFocusedIndex()) {
                            shift += (-e2.getTreeItem().previousExpandedDescendentCount) + 1;
                        }
                    } else if (e2.wasAdded()) {
                        TreeItem<T> eventTreeItem = e2.getTreeItem();
                        if (eventTreeItem.isExpanded()) {
                            for (int i2 = 0; i2 < e2.getAddedChildren().size(); i2++) {
                                TreeItem<T> item = e2.getAddedChildren().get(i2);
                                row = TreeViewFocusModel.this.treeView.getRow(item);
                                if (item != null && row <= TreeViewFocusModel.this.getFocusedIndex()) {
                                    shift += item.getExpandedDescendentCount(false);
                                }
                            }
                        }
                    } else if (e2.wasRemoved()) {
                        row += e2.getFrom() + 1;
                        for (int i3 = 0; i3 < e2.getRemovedChildren().size(); i3++) {
                            TreeItem<T> item2 = e2.getRemovedChildren().get(i3);
                            if (item2 != null && item2.equals(TreeViewFocusModel.this.getFocusedItem())) {
                                TreeViewFocusModel.this.focus(Math.max(0, TreeViewFocusModel.this.getFocusedIndex() - 1));
                                return;
                            }
                        }
                        if (row <= TreeViewFocusModel.this.getFocusedIndex()) {
                            shift += e2.getTreeItem().isExpanded() ? -e2.getRemovedSize() : 0;
                        }
                    }
                    if (e2.getChange() == null) {
                        break;
                    }
                } while (e2.getChange().next());
                if (shift != 0 && (newFocus = TreeViewFocusModel.this.getFocusedIndex() + shift) >= 0) {
                    Platform.runLater(() -> {
                        TreeViewFocusModel.this.focus(newFocus);
                    });
                }
            }
        };
        private WeakEventHandler<TreeItem.TreeModificationEvent<T>> weakTreeItemListener;

        public TreeViewFocusModel(TreeView<T> treeView) {
            this.treeView = treeView;
            this.treeView.rootProperty().addListener(this.weakRootPropertyListener);
            updateTreeEventListener(null, treeView.getRoot());
            if (treeView.getExpandedItemCount() > 0) {
                focus(0);
            }
            treeView.showRootProperty().addListener(o2 -> {
                if (isFocused(0)) {
                    focus(-1);
                    focus(0);
                }
            });
        }

        private void updateTreeEventListener(TreeItem<T> oldRoot, TreeItem<T> newRoot) {
            if (oldRoot != null && this.weakTreeItemListener != null) {
                oldRoot.removeEventHandler(TreeItem.expandedItemCountChangeEvent(), this.weakTreeItemListener);
            }
            if (newRoot != null) {
                this.weakTreeItemListener = new WeakEventHandler<>(this.treeItemListener);
                newRoot.addEventHandler(TreeItem.expandedItemCountChangeEvent(), this.weakTreeItemListener);
            }
        }

        @Override // javafx.scene.control.FocusModel
        protected int getItemCount() {
            if (this.treeView == null) {
                return -1;
            }
            return this.treeView.getExpandedItemCount();
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // javafx.scene.control.FocusModel
        public TreeItem<T> getModelItem(int index) {
            if (this.treeView != null && index >= 0 && index < this.treeView.getExpandedItemCount()) {
                return this.treeView.getTreeItem(index);
            }
            return null;
        }

        @Override // javafx.scene.control.FocusModel
        public void focus(int index) {
            if (((TreeView) this.treeView).expandedItemCountDirty) {
                this.treeView.updateExpandedItemCount(this.treeView.getRoot());
            }
            super.focus(index);
        }
    }
}
