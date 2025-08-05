package javafx.scene.control;

import com.sun.javafx.css.converters.EnumConverter;
import com.sun.javafx.css.converters.SizeConverter;
import com.sun.javafx.scene.control.behavior.ListCellBehavior;
import com.sun.javafx.scene.control.skin.ListViewSkin;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import javafx.beans.DefaultProperty;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.WeakInvalidationListener;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ObjectPropertyBase;
import javafx.beans.property.ReadOnlyIntegerProperty;
import javafx.beans.property.ReadOnlyIntegerWrapper;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.WeakListChangeListener;
import javafx.css.CssMetaData;
import javafx.css.PseudoClass;
import javafx.css.Styleable;
import javafx.css.StyleableDoubleProperty;
import javafx.css.StyleableObjectProperty;
import javafx.css.StyleableProperty;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.geometry.Orientation;
import javafx.scene.AccessibleAttribute;
import javafx.scene.AccessibleRole;
import javafx.scene.Node;
import javafx.util.Callback;
import javax.swing.JTree;

@DefaultProperty("items")
/* loaded from: jfxrt.jar:javafx/scene/control/ListView.class */
public class ListView<T> extends Control {
    private boolean selectFirstRowByDefault;
    private EventHandler<EditEvent<T>> DEFAULT_EDIT_COMMIT_HANDLER;
    private ObjectProperty<ObservableList<T>> items;
    private ObjectProperty<Node> placeholder;
    private ObjectProperty<MultipleSelectionModel<T>> selectionModel;
    private ObjectProperty<FocusModel<T>> focusModel;
    private ObjectProperty<Orientation> orientation;
    private ObjectProperty<Callback<ListView<T>, ListCell<T>>> cellFactory;
    private DoubleProperty fixedCellSize;
    private BooleanProperty editable;
    private ReadOnlyIntegerWrapper editingIndex;
    private ObjectProperty<EventHandler<EditEvent<T>>> onEditStart;
    private ObjectProperty<EventHandler<EditEvent<T>>> onEditCommit;
    private ObjectProperty<EventHandler<EditEvent<T>>> onEditCancel;
    private ObjectProperty<EventHandler<ScrollToEvent<Integer>>> onScrollTo;
    private static final String DEFAULT_STYLE_CLASS = "list-view";
    private static final EventType<?> EDIT_ANY_EVENT = new EventType<>(Event.ANY, "LIST_VIEW_EDIT");
    private static final EventType<?> EDIT_START_EVENT = new EventType<>(editAnyEvent(), "EDIT_START");
    private static final EventType<?> EDIT_CANCEL_EVENT = new EventType<>(editAnyEvent(), "EDIT_CANCEL");
    private static final EventType<?> EDIT_COMMIT_EVENT = new EventType<>(editAnyEvent(), "EDIT_COMMIT");
    private static final PseudoClass PSEUDO_CLASS_VERTICAL = PseudoClass.getPseudoClass("vertical");
    private static final PseudoClass PSEUDO_CLASS_HORIZONTAL = PseudoClass.getPseudoClass("horizontal");

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

    public ListView() {
        this(FXCollections.observableArrayList());
    }

    public ListView(ObservableList<T> items) {
        this.selectFirstRowByDefault = true;
        this.DEFAULT_EDIT_COMMIT_HANDLER = editEvent -> {
            int index = editEvent.getIndex();
            ObservableList<T> items2 = getItems();
            if (index < 0 || index >= items2.size()) {
                return;
            }
            items2.set(index, editEvent.getNewValue());
        };
        this.selectionModel = new SimpleObjectProperty(this, "selectionModel");
        getStyleClass().setAll(DEFAULT_STYLE_CLASS);
        setAccessibleRole(AccessibleRole.LIST_VIEW);
        setItems(items);
        setSelectionModel(new ListViewBitSetSelectionModel(this));
        setFocusModel(new ListViewFocusModel(this));
        setOnEditCommit(this.DEFAULT_EDIT_COMMIT_HANDLER);
        getProperties().addListener(change -> {
            Boolean _selectFirstRowByDefault;
            if (change.wasAdded() && "selectFirstRowByDefault".equals(change.getKey()) && (_selectFirstRowByDefault = (Boolean) change.getValueAdded()) != null) {
                this.selectFirstRowByDefault = _selectFirstRowByDefault.booleanValue();
            }
        });
    }

    public final void setItems(ObservableList<T> value) {
        itemsProperty().set(value);
    }

    public final ObservableList<T> getItems() {
        if (this.items == null) {
            return null;
        }
        return this.items.get();
    }

    public final ObjectProperty<ObservableList<T>> itemsProperty() {
        if (this.items == null) {
            this.items = new SimpleObjectProperty(this, "items");
        }
        return this.items;
    }

    public final ObjectProperty<Node> placeholderProperty() {
        if (this.placeholder == null) {
            this.placeholder = new SimpleObjectProperty(this, "placeholder");
        }
        return this.placeholder;
    }

    public final void setPlaceholder(Node value) {
        placeholderProperty().set(value);
    }

    public final Node getPlaceholder() {
        if (this.placeholder == null) {
            return null;
        }
        return this.placeholder.get();
    }

    public final void setSelectionModel(MultipleSelectionModel<T> value) {
        selectionModelProperty().set(value);
    }

    public final MultipleSelectionModel<T> getSelectionModel() {
        if (this.selectionModel == null) {
            return null;
        }
        return this.selectionModel.get();
    }

    public final ObjectProperty<MultipleSelectionModel<T>> selectionModelProperty() {
        return this.selectionModel;
    }

    public final void setFocusModel(FocusModel<T> value) {
        focusModelProperty().set(value);
    }

    public final FocusModel<T> getFocusModel() {
        if (this.focusModel == null) {
            return null;
        }
        return this.focusModel.get();
    }

    public final ObjectProperty<FocusModel<T>> focusModelProperty() {
        if (this.focusModel == null) {
            this.focusModel = new SimpleObjectProperty(this, "focusModel");
        }
        return this.focusModel;
    }

    public final void setOrientation(Orientation value) {
        orientationProperty().set(value);
    }

    public final Orientation getOrientation() {
        return this.orientation == null ? Orientation.VERTICAL : this.orientation.get();
    }

    public final ObjectProperty<Orientation> orientationProperty() {
        if (this.orientation == null) {
            this.orientation = new StyleableObjectProperty<Orientation>(Orientation.VERTICAL) { // from class: javafx.scene.control.ListView.1
                @Override // javafx.beans.property.ObjectPropertyBase
                public void invalidated() {
                    boolean active = get() == Orientation.VERTICAL;
                    ListView.this.pseudoClassStateChanged(ListView.PSEUDO_CLASS_VERTICAL, active);
                    ListView.this.pseudoClassStateChanged(ListView.PSEUDO_CLASS_HORIZONTAL, !active);
                }

                @Override // javafx.css.StyleableProperty
                public CssMetaData<ListView<?>, Orientation> getCssMetaData() {
                    return StyleableProperties.ORIENTATION;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return ListView.this;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return "orientation";
                }
            };
        }
        return this.orientation;
    }

    public final void setCellFactory(Callback<ListView<T>, ListCell<T>> value) {
        cellFactoryProperty().set(value);
    }

    public final Callback<ListView<T>, ListCell<T>> getCellFactory() {
        if (this.cellFactory == null) {
            return null;
        }
        return this.cellFactory.get();
    }

    public final ObjectProperty<Callback<ListView<T>, ListCell<T>>> cellFactoryProperty() {
        if (this.cellFactory == null) {
            this.cellFactory = new SimpleObjectProperty(this, "cellFactory");
        }
        return this.cellFactory;
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
            this.fixedCellSize = new StyleableDoubleProperty(-1.0d) { // from class: javafx.scene.control.ListView.2
                @Override // javafx.css.StyleableProperty
                public CssMetaData<? extends Styleable, Number> getCssMetaData() {
                    return StyleableProperties.FIXED_CELL_SIZE;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return ListView.this;
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

    private void setEditingIndex(int value) {
        editingIndexPropertyImpl().set(value);
    }

    public final int getEditingIndex() {
        if (this.editingIndex == null) {
            return -1;
        }
        return this.editingIndex.get();
    }

    public final ReadOnlyIntegerProperty editingIndexProperty() {
        return editingIndexPropertyImpl().getReadOnlyProperty();
    }

    private ReadOnlyIntegerWrapper editingIndexPropertyImpl() {
        if (this.editingIndex == null) {
            this.editingIndex = new ReadOnlyIntegerWrapper(this, "editingIndex", -1);
        }
        return this.editingIndex;
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
            this.onEditStart = new ObjectPropertyBase<EventHandler<EditEvent<T>>>() { // from class: javafx.scene.control.ListView.3
                @Override // javafx.beans.property.ObjectPropertyBase
                protected void invalidated() {
                    ListView.this.setEventHandler(ListView.editStartEvent(), get());
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return ListView.this;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return "onEditStart";
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
            this.onEditCommit = new ObjectPropertyBase<EventHandler<EditEvent<T>>>() { // from class: javafx.scene.control.ListView.4
                @Override // javafx.beans.property.ObjectPropertyBase
                protected void invalidated() {
                    ListView.this.setEventHandler(ListView.editCommitEvent(), get());
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return ListView.this;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return "onEditCommit";
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
            this.onEditCancel = new ObjectPropertyBase<EventHandler<EditEvent<T>>>() { // from class: javafx.scene.control.ListView.5
                @Override // javafx.beans.property.ObjectPropertyBase
                protected void invalidated() {
                    ListView.this.setEventHandler(ListView.editCancelEvent(), get());
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return ListView.this;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return "onEditCancel";
                }
            };
        }
        return this.onEditCancel;
    }

    public void edit(int itemIndex) {
        if (isEditable()) {
            setEditingIndex(itemIndex);
        }
    }

    public void scrollTo(int index) {
        ControlUtils.scrollToIndex(this, index);
    }

    public void scrollTo(T object) {
        int idx;
        if (getItems() != null && (idx = getItems().indexOf(object)) >= 0) {
            ControlUtils.scrollToIndex(this, idx);
        }
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
            this.onScrollTo = new ObjectPropertyBase<EventHandler<ScrollToEvent<Integer>>>() { // from class: javafx.scene.control.ListView.6
                @Override // javafx.beans.property.ObjectPropertyBase
                protected void invalidated() {
                    ListView.this.setEventHandler(ScrollToEvent.scrollToTopIndex(), get());
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return ListView.this;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return "onScrollTo";
                }
            };
        }
        return this.onScrollTo;
    }

    @Override // javafx.scene.control.Control
    protected Skin<?> createDefaultSkin() {
        return new ListViewSkin(this);
    }

    public void refresh() {
        getProperties().put(ListViewSkin.RECREATE, Boolean.TRUE);
    }

    /* loaded from: jfxrt.jar:javafx/scene/control/ListView$StyleableProperties.class */
    private static class StyleableProperties {
        private static final CssMetaData<ListView<?>, Orientation> ORIENTATION = new CssMetaData<ListView<?>, Orientation>("-fx-orientation", new EnumConverter(Orientation.class), Orientation.VERTICAL) { // from class: javafx.scene.control.ListView.StyleableProperties.1
            @Override // javafx.css.CssMetaData
            public Orientation getInitialValue(ListView<?> node) {
                return node.getOrientation();
            }

            @Override // javafx.css.CssMetaData
            public boolean isSettable(ListView<?> n2) {
                return ((ListView) n2).orientation == null || !((ListView) n2).orientation.isBound();
            }

            @Override // javafx.css.CssMetaData
            public StyleableProperty<Orientation> getStyleableProperty(ListView<?> n2) {
                return (StyleableProperty) n2.orientationProperty();
            }
        };
        private static final CssMetaData<ListView<?>, Number> FIXED_CELL_SIZE = new CssMetaData<ListView<?>, Number>("-fx-fixed-cell-size", SizeConverter.getInstance(), Double.valueOf(-1.0d)) { // from class: javafx.scene.control.ListView.StyleableProperties.2
            @Override // javafx.css.CssMetaData
            public Double getInitialValue(ListView<?> node) {
                return Double.valueOf(node.getFixedCellSize());
            }

            @Override // javafx.css.CssMetaData
            public boolean isSettable(ListView<?> n2) {
                return ((ListView) n2).fixedCellSize == null || !((ListView) n2).fixedCellSize.isBound();
            }

            @Override // javafx.css.CssMetaData
            public StyleableProperty<Number> getStyleableProperty(ListView<?> n2) {
                return (StyleableProperty) n2.fixedCellSizeProperty();
            }
        };
        private static final List<CssMetaData<? extends Styleable, ?>> STYLEABLES;

        private StyleableProperties() {
        }

        static {
            List<CssMetaData<? extends Styleable, ?>> styleables = new ArrayList<>(Control.getClassCssMetaData());
            styleables.add(ORIENTATION);
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
                MultipleSelectionModel<T> sm = getSelectionModel();
                return Boolean.valueOf(sm != null && sm.getSelectionMode() == SelectionMode.MULTIPLE);
            default:
                return super.queryAccessibleAttribute(attribute, parameters);
        }
    }

    /* loaded from: jfxrt.jar:javafx/scene/control/ListView$EditEvent.class */
    public static class EditEvent<T> extends Event {
        private final T newValue;
        private final int editIndex;
        private static final long serialVersionUID = 20130724;
        public static final EventType<?> ANY = ListView.EDIT_ANY_EVENT;

        public EditEvent(ListView<T> source, EventType<? extends EditEvent<T>> eventType, T newValue, int editIndex) {
            super(source, Event.NULL_SOURCE_TARGET, eventType);
            this.editIndex = editIndex;
            this.newValue = newValue;
        }

        @Override // java.util.EventObject
        public ListView<T> getSource() {
            return (ListView) super.getSource();
        }

        public int getIndex() {
            return this.editIndex;
        }

        public T getNewValue() {
            return this.newValue;
        }

        @Override // java.util.EventObject
        public String toString() {
            return "ListViewEditEvent [ newValue: " + ((Object) getNewValue()) + ", ListView: " + ((Object) getSource()) + " ]";
        }
    }

    /* loaded from: jfxrt.jar:javafx/scene/control/ListView$ListViewBitSetSelectionModel.class */
    static class ListViewBitSetSelectionModel<T> extends MultipleSelectionModelBase<T> {
        private final InvalidationListener itemsObserver;
        private final ListView<T> listView;
        private final ListChangeListener<T> itemsContentObserver = new ListChangeListener<T>() { // from class: javafx.scene.control.ListView.ListViewBitSetSelectionModel.2
            /* JADX WARN: Multi-variable type inference failed */
            @Override // javafx.collections.ListChangeListener
            public void onChanged(ListChangeListener.Change<? extends T> c2) {
                ListViewBitSetSelectionModel.this.updateItemCount();
                while (c2.next()) {
                    T selectedItem = ListViewBitSetSelectionModel.this.getSelectedItem();
                    int selectedIndex = ListViewBitSetSelectionModel.this.getSelectedIndex();
                    if (ListViewBitSetSelectionModel.this.listView.getItems() == null || ListViewBitSetSelectionModel.this.listView.getItems().isEmpty()) {
                        ListViewBitSetSelectionModel.this.selectedItemChange = c2;
                        ListViewBitSetSelectionModel.this.clearSelection();
                        ListViewBitSetSelectionModel.this.selectedItemChange = null;
                    } else if (selectedIndex == -1 && selectedItem != null) {
                        int newIndex = ListViewBitSetSelectionModel.this.listView.getItems().indexOf(selectedItem);
                        if (newIndex != -1) {
                            ListViewBitSetSelectionModel.this.setSelectedIndex(newIndex);
                        }
                    } else if (c2.wasRemoved() && c2.getRemovedSize() == 1 && !c2.wasAdded() && selectedItem != null && selectedItem.equals(c2.getRemoved().get(0)) && ListViewBitSetSelectionModel.this.getSelectedIndex() < ListViewBitSetSelectionModel.this.getItemCount()) {
                        int previousRow = selectedIndex == 0 ? 0 : selectedIndex - 1;
                        Object modelItem = ListViewBitSetSelectionModel.this.getModelItem(previousRow);
                        if (!selectedItem.equals(modelItem)) {
                            ListViewBitSetSelectionModel.this.startAtomic();
                            ListViewBitSetSelectionModel.this.clearSelection(selectedIndex);
                            ListViewBitSetSelectionModel.this.stopAtomic();
                            ListViewBitSetSelectionModel.this.select((ListViewBitSetSelectionModel) modelItem);
                        }
                    }
                }
                ListViewBitSetSelectionModel.this.updateSelection(c2);
            }
        };
        private WeakListChangeListener<T> weakItemsContentObserver = new WeakListChangeListener<>(this.itemsContentObserver);
        private int itemCount = 0;
        private int previousModelSize = 0;

        public ListViewBitSetSelectionModel(final ListView<T> listView) {
            if (listView == null) {
                throw new IllegalArgumentException("ListView can not be null");
            }
            this.listView = listView;
            this.itemsObserver = new InvalidationListener() { // from class: javafx.scene.control.ListView.ListViewBitSetSelectionModel.1
                private WeakReference<ObservableList<T>> weakItemsRef;

                {
                    this.weakItemsRef = new WeakReference<>(listView.getItems());
                }

                @Override // javafx.beans.InvalidationListener
                public void invalidated(Observable observable) {
                    ObservableList<T> oldItems = this.weakItemsRef.get();
                    this.weakItemsRef = new WeakReference<>(listView.getItems());
                    ListViewBitSetSelectionModel.this.updateItemsObserver(oldItems, listView.getItems());
                }
            };
            this.listView.itemsProperty().addListener(new WeakInvalidationListener(this.itemsObserver));
            if (listView.getItems() != null) {
                this.listView.getItems().addListener(this.weakItemsContentObserver);
            }
            updateItemCount();
            updateDefaultSelection();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void updateSelection(ListChangeListener.Change<? extends T> c2) {
            c2.reset();
            int shift = 0;
            while (c2.next()) {
                if (c2.wasReplaced()) {
                    if (c2.getList().isEmpty()) {
                        clearSelection();
                    } else {
                        int index = getSelectedIndex();
                        if (this.previousModelSize == c2.getRemovedSize()) {
                            clearSelection();
                        } else if (index < getItemCount() && index >= 0) {
                            startAtomic();
                            clearSelection(index);
                            stopAtomic();
                            select(index);
                        } else {
                            clearSelection();
                        }
                    }
                } else if (c2.wasAdded() || c2.wasRemoved()) {
                    shift += c2.wasAdded() ? c2.getAddedSize() : -c2.getRemovedSize();
                } else if (c2.wasPermutated()) {
                    int length = c2.getTo() - c2.getFrom();
                    HashMap<Integer, Integer> pMap = new HashMap<>(length);
                    for (int i2 = c2.getFrom(); i2 < c2.getTo(); i2++) {
                        pMap.put(Integer.valueOf(i2), Integer.valueOf(c2.getPermutation(i2)));
                    }
                    List<Integer> selectedIndices = new ArrayList<>(getSelectedIndices());
                    clearSelection();
                    List<Integer> newIndices = new ArrayList<>(getSelectedIndices().size());
                    for (int i3 = 0; i3 < selectedIndices.size(); i3++) {
                        int oldIndex = selectedIndices.get(i3).intValue();
                        if (pMap.containsKey(Integer.valueOf(oldIndex))) {
                            Integer newIndex = pMap.get(Integer.valueOf(oldIndex));
                            newIndices.add(newIndex);
                        }
                    }
                    if (!newIndices.isEmpty()) {
                        if (newIndices.size() == 1) {
                            select(newIndices.get(0).intValue());
                        } else {
                            int[] ints = new int[newIndices.size() - 1];
                            for (int i4 = 0; i4 < newIndices.size() - 1; i4++) {
                                ints[i4] = newIndices.get(i4 + 1).intValue();
                            }
                            selectIndices(newIndices.get(0).intValue(), ints);
                        }
                    }
                }
            }
            if (shift != 0) {
                shiftSelection(c2.getFrom(), shift, null);
            }
            this.previousModelSize = getItemCount();
        }

        @Override // javafx.scene.control.MultipleSelectionModelBase, javafx.scene.control.MultipleSelectionModel
        public void selectAll() {
            int anchor = ((Integer) ListCellBehavior.getAnchor(this.listView, -1)).intValue();
            super.selectAll();
            ListCellBehavior.setAnchor(this.listView, Integer.valueOf(anchor), false);
        }

        @Override // javafx.scene.control.MultipleSelectionModelBase, javafx.scene.control.SelectionModel
        public void clearAndSelect(int row) {
            ListCellBehavior.setAnchor(this.listView, Integer.valueOf(row), false);
            super.clearAndSelect(row);
        }

        @Override // javafx.scene.control.MultipleSelectionModelBase
        protected void focus(int row) {
            if (this.listView.getFocusModel() == null) {
                return;
            }
            this.listView.getFocusModel().focus(row);
            this.listView.notifyAccessibleAttributeChanged(AccessibleAttribute.FOCUS_ITEM);
        }

        @Override // javafx.scene.control.MultipleSelectionModelBase
        protected int getFocusedIndex() {
            if (this.listView.getFocusModel() == null) {
                return -1;
            }
            return this.listView.getFocusModel().getFocusedIndex();
        }

        @Override // javafx.scene.control.MultipleSelectionModelBase
        protected int getItemCount() {
            return this.itemCount;
        }

        @Override // javafx.scene.control.MultipleSelectionModelBase
        protected T getModelItem(int index) {
            List<T> items = this.listView.getItems();
            if (items != null && index >= 0 && index < this.itemCount) {
                return items.get(index);
            }
            return null;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void updateItemCount() {
            if (this.listView == null) {
                this.itemCount = -1;
            } else {
                List<T> items = this.listView.getItems();
                this.itemCount = items == null ? -1 : items.size();
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void updateItemsObserver(ObservableList<T> oldList, ObservableList<T> newList) {
            if (oldList != null) {
                oldList.removeListener(this.weakItemsContentObserver);
            }
            if (newList != null) {
                newList.addListener(this.weakItemsContentObserver);
            }
            updateItemCount();
            updateDefaultSelection();
        }

        private void updateDefaultSelection() {
            int newSelectionIndex = -1;
            int newFocusIndex = -1;
            if (this.listView.getItems() != null) {
                T selectedItem = getSelectedItem();
                if (selectedItem != null) {
                    newSelectionIndex = this.listView.getItems().indexOf(selectedItem);
                    newFocusIndex = newSelectionIndex;
                }
                if (((ListView) this.listView).selectFirstRowByDefault && newFocusIndex == -1) {
                    newFocusIndex = this.listView.getItems().size() > 0 ? 0 : -1;
                }
            }
            clearSelection();
            select(newSelectionIndex);
            focus(newFocusIndex);
        }
    }

    /* loaded from: jfxrt.jar:javafx/scene/control/ListView$ListViewFocusModel.class */
    static class ListViewFocusModel<T> extends FocusModel<T> {
        private final ListView<T> listView;
        private final InvalidationListener itemsObserver;
        private int itemCount = 0;
        private final ListChangeListener<T> itemsContentListener = c2 -> {
            int removedSize;
            updateItemCount();
            while (c2.next()) {
                int from = c2.getFrom();
                if (getFocusedIndex() == -1 || from > getFocusedIndex()) {
                    return;
                }
                c2.reset();
                boolean added = false;
                boolean removed = false;
                int addedSize = 0;
                int removedSize2 = 0;
                while (true) {
                    removedSize = removedSize2;
                    if (!c2.next()) {
                        break;
                    }
                    added |= c2.wasAdded();
                    removed |= c2.wasRemoved();
                    addedSize += c2.getAddedSize();
                    removedSize2 = removedSize + c2.getRemovedSize();
                }
                if (added && !removed) {
                    focus(Math.min(getItemCount() - 1, getFocusedIndex() + addedSize));
                } else if (!added && removed) {
                    focus(Math.max(0, getFocusedIndex() - removedSize));
                }
            }
        };
        private WeakListChangeListener<T> weakItemsContentListener = new WeakListChangeListener<>(this.itemsContentListener);

        public ListViewFocusModel(final ListView<T> listView) {
            if (listView == null) {
                throw new IllegalArgumentException("ListView can not be null");
            }
            this.listView = listView;
            this.itemsObserver = new InvalidationListener() { // from class: javafx.scene.control.ListView.ListViewFocusModel.1
                private WeakReference<ObservableList<T>> weakItemsRef;

                {
                    this.weakItemsRef = new WeakReference<>(listView.getItems());
                }

                @Override // javafx.beans.InvalidationListener
                public void invalidated(Observable observable) {
                    ObservableList<T> oldItems = this.weakItemsRef.get();
                    this.weakItemsRef = new WeakReference<>(listView.getItems());
                    ListViewFocusModel.this.updateItemsObserver(oldItems, listView.getItems());
                }
            };
            this.listView.itemsProperty().addListener(new WeakInvalidationListener(this.itemsObserver));
            if (listView.getItems() != null) {
                this.listView.getItems().addListener(this.weakItemsContentListener);
            }
            updateItemCount();
            if (this.itemCount > 0) {
                focus(0);
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void updateItemsObserver(ObservableList<T> oldList, ObservableList<T> newList) {
            if (oldList != null) {
                oldList.removeListener(this.weakItemsContentListener);
            }
            if (newList != null) {
                newList.addListener(this.weakItemsContentListener);
            }
            updateItemCount();
        }

        @Override // javafx.scene.control.FocusModel
        protected int getItemCount() {
            return this.itemCount;
        }

        @Override // javafx.scene.control.FocusModel
        protected T getModelItem(int index) {
            if (!isEmpty() && index >= 0 && index < this.itemCount) {
                return this.listView.getItems().get(index);
            }
            return null;
        }

        private boolean isEmpty() {
            return this.itemCount == -1;
        }

        private void updateItemCount() {
            if (this.listView == null) {
                this.itemCount = -1;
            } else {
                List<T> items = this.listView.getItems();
                this.itemCount = items == null ? -1 : items.size();
            }
        }
    }
}
