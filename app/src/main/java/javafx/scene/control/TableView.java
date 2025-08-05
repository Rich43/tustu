package javafx.scene.control;

import com.sun.javafx.collections.MappingChange;
import com.sun.javafx.collections.NonIterableChange;
import com.sun.javafx.css.converters.SizeConverter;
import com.sun.javafx.scene.control.Logging;
import com.sun.javafx.scene.control.ReadOnlyUnbackedObservableList;
import com.sun.javafx.scene.control.SelectedCellsMap;
import com.sun.javafx.scene.control.TableColumnComparatorBase;
import com.sun.javafx.scene.control.behavior.TableCellBehavior;
import com.sun.javafx.scene.control.behavior.TableCellBehaviorBase;
import com.sun.javafx.scene.control.skin.TableViewSkin;
import com.sun.javafx.scene.control.skin.TableViewSkinBase;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.WeakHashMap;
import javafx.beans.DefaultProperty;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.WeakInvalidationListener;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ObjectPropertyBase;
import javafx.beans.property.Property;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableObjectValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.MapChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.WeakListChangeListener;
import javafx.collections.transformation.SortedList;
import javafx.css.CssMetaData;
import javafx.css.PseudoClass;
import javafx.css.Styleable;
import javafx.css.StyleableDoubleProperty;
import javafx.css.StyleableProperty;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.scene.AccessibleAttribute;
import javafx.scene.AccessibleRole;
import javafx.scene.Node;
import javafx.scene.control.TableUtil;
import javafx.util.Callback;
import javax.swing.JTree;
import org.icepdf.core.util.PdfOps;

@DefaultProperty("items")
/* loaded from: jfxrt.jar:javafx/scene/control/TableView.class */
public class TableView<S> extends Control {
    static final String SET_CONTENT_WIDTH = "TableView.contentWidth";
    private final ObservableList<TableColumn<S, ?>> columns;
    private final ObservableList<TableColumn<S, ?>> visibleLeafColumns;
    private final ObservableList<TableColumn<S, ?>> unmodifiableVisibleLeafColumns;
    private ObservableList<TableColumn<S, ?>> sortOrder;
    private double contentWidth;
    private boolean isInited;
    private final ListChangeListener<TableColumn<S, ?>> columnsObserver;
    private final WeakHashMap<TableColumn<S, ?>, Integer> lastKnownColumnIndex;
    private final InvalidationListener columnVisibleObserver;
    private final InvalidationListener columnSortableObserver;
    private final InvalidationListener columnSortTypeObserver;
    private final InvalidationListener columnComparatorObserver;
    private final InvalidationListener cellSelectionModelInvalidationListener;
    private final WeakInvalidationListener weakColumnVisibleObserver;
    private final WeakInvalidationListener weakColumnSortableObserver;
    private final WeakInvalidationListener weakColumnSortTypeObserver;
    private final WeakInvalidationListener weakColumnComparatorObserver;
    private final WeakListChangeListener<TableColumn<S, ?>> weakColumnsObserver;
    private final WeakInvalidationListener weakCellSelectionModelInvalidationListener;
    private ObjectProperty<ObservableList<S>> items;
    private BooleanProperty tableMenuButtonVisible;
    private ObjectProperty<Callback<ResizeFeatures, Boolean>> columnResizePolicy;
    private ObjectProperty<Callback<TableView<S>, TableRow<S>>> rowFactory;
    private ObjectProperty<Node> placeholder;
    private ObjectProperty<TableViewSelectionModel<S>> selectionModel;
    private ObjectProperty<TableViewFocusModel<S>> focusModel;
    private BooleanProperty editable;
    private DoubleProperty fixedCellSize;
    private ReadOnlyObjectWrapper<TablePosition<S, ?>> editingCell;
    private ReadOnlyObjectWrapper<Comparator<S>> comparator;
    private ObjectProperty<Callback<TableView<S>, Boolean>> sortPolicy;
    private ObjectProperty<EventHandler<SortEvent<TableView<S>>>> onSort;
    private ObjectProperty<EventHandler<ScrollToEvent<Integer>>> onScrollTo;
    private ObjectProperty<EventHandler<ScrollToEvent<TableColumn<S, ?>>>> onScrollToColumn;
    private boolean sortLock;
    private TableUtil.SortEventType lastSortEventType;
    private Object[] lastSortEventSupportInfo;
    private static final String DEFAULT_STYLE_CLASS = "table-view";
    public static final Callback<ResizeFeatures, Boolean> UNCONSTRAINED_RESIZE_POLICY = new Callback<ResizeFeatures, Boolean>() { // from class: javafx.scene.control.TableView.1
        public String toString() {
            return "unconstrained-resize";
        }

        @Override // javafx.util.Callback
        public Boolean call(ResizeFeatures prop) {
            double result = TableUtil.resize(prop.getColumn(), prop.getDelta().doubleValue());
            return Boolean.valueOf(Double.compare(result, 0.0d) == 0);
        }
    };
    public static final Callback<ResizeFeatures, Boolean> CONSTRAINED_RESIZE_POLICY = new Callback<ResizeFeatures, Boolean>() { // from class: javafx.scene.control.TableView.2
        private boolean isFirstRun = true;

        public String toString() {
            return "constrained-resize";
        }

        @Override // javafx.util.Callback
        public Boolean call(ResizeFeatures prop) {
            TableView<S> table = prop.getTable();
            List<? extends TableColumnBase<?, ?>> visibleLeafColumns = table.getVisibleLeafColumns();
            Boolean result = Boolean.valueOf(TableUtil.constrainedResize(prop, this.isFirstRun, ((TableView) table).contentWidth, visibleLeafColumns));
            boolean z2 = this.isFirstRun && !result.booleanValue();
            this.isFirstRun = z2;
            return result;
        }
    };
    public static final Callback<TableView, Boolean> DEFAULT_SORT_POLICY = new Callback<TableView, Boolean>() { // from class: javafx.scene.control.TableView.3
        @Override // javafx.util.Callback
        public Boolean call(TableView table) {
            try {
                ObservableList<S> items = table.getItems();
                if (items instanceof SortedList) {
                    SortedList sortedList = (SortedList) items;
                    boolean comparatorsBound = sortedList.comparatorProperty().isEqualTo((ObservableObjectValue<?>) table.comparatorProperty()).get();
                    if (!comparatorsBound && Logging.getControlsLogger().isEnabled()) {
                        Logging.getControlsLogger().info("TableView items list is a SortedList, but the SortedList comparator should be bound to the TableView comparator for sorting to be enabled (e.g. sortedList.comparatorProperty().bind(tableView.comparatorProperty());).");
                    }
                    return Boolean.valueOf(comparatorsBound);
                }
                if (items == null || items.isEmpty()) {
                    return true;
                }
                Comparator comparator = table.getComparator();
                if (comparator == null) {
                    return true;
                }
                FXCollections.sort(items, comparator);
                return true;
            } catch (UnsupportedOperationException e2) {
                return false;
            }
        }
    };
    private static final PseudoClass PSEUDO_CLASS_CELL_SELECTION = PseudoClass.getPseudoClass("cell-selection");
    private static final PseudoClass PSEUDO_CLASS_ROW_SELECTION = PseudoClass.getPseudoClass("row-selection");

    public TableView() {
        this(FXCollections.observableArrayList());
    }

    public TableView(ObservableList<S> items) {
        this.columns = FXCollections.observableArrayList();
        this.visibleLeafColumns = FXCollections.observableArrayList();
        this.unmodifiableVisibleLeafColumns = FXCollections.unmodifiableObservableList(this.visibleLeafColumns);
        this.sortOrder = FXCollections.observableArrayList();
        this.isInited = false;
        this.columnsObserver = new ListChangeListener<TableColumn<S, ?>>() { // from class: javafx.scene.control.TableView.5
            /* JADX WARN: Multi-variable type inference failed */
            @Override // javafx.collections.ListChangeListener
            public void onChanged(ListChangeListener.Change<? extends TableColumn<S, ?>> c2) {
                int matchingColumnIndex;
                List<TableColumn<S, ?>> columns = TableView.this.getColumns();
                while (c2.next()) {
                    if (c2.wasAdded()) {
                        List<TableColumn<S, ?>> duplicates = new ArrayList<>();
                        for (TableColumn<S, ?> addedColumn : c2.getAddedSubList()) {
                            if (addedColumn != null) {
                                int count = 0;
                                for (TableColumn<S, ?> column : columns) {
                                    if (addedColumn == column) {
                                        count++;
                                    }
                                }
                                if (count > 1) {
                                    duplicates.add(addedColumn);
                                }
                            }
                        }
                        if (!duplicates.isEmpty()) {
                            String titleList = "";
                            for (TableColumn<S, ?> dupe : duplicates) {
                                titleList = titleList + PdfOps.SINGLE_QUOTE_TOKEN + dupe.getText() + "', ";
                            }
                            throw new IllegalStateException("Duplicate TableColumns detected in TableView columns list with titles " + titleList);
                        }
                    }
                }
                c2.reset();
                TableView.this.updateVisibleLeafColumns();
                List<TableColumn<S, ?>> toRemove = new ArrayList<>();
                while (c2.next()) {
                    List<? extends TableColumn<S, ?>> removed = c2.getRemoved();
                    List<? extends TableColumn<S, ?>> added = c2.getAddedSubList();
                    if (c2.wasRemoved()) {
                        toRemove.addAll(removed);
                        Iterator<? extends TableColumn<S, ?>> it = removed.iterator();
                        while (it.hasNext()) {
                            it.next().setTableView(null);
                        }
                    }
                    if (c2.wasAdded()) {
                        toRemove.removeAll(added);
                        Iterator<? extends TableColumn<S, ?>> it2 = added.iterator();
                        while (it2.hasNext()) {
                            it2.next().setTableView(TableView.this);
                        }
                    }
                    TableUtil.removeColumnsListener(removed, TableView.this.weakColumnsObserver);
                    TableUtil.addColumnsListener(added, TableView.this.weakColumnsObserver);
                    TableUtil.removeTableColumnListener(c2.getRemoved(), TableView.this.weakColumnVisibleObserver, TableView.this.weakColumnSortableObserver, TableView.this.weakColumnSortTypeObserver, TableView.this.weakColumnComparatorObserver);
                    TableUtil.addTableColumnListener(c2.getAddedSubList(), TableView.this.weakColumnVisibleObserver, TableView.this.weakColumnSortableObserver, TableView.this.weakColumnSortTypeObserver, TableView.this.weakColumnComparatorObserver);
                }
                TableView.this.sortOrder.removeAll(toRemove);
                TableViewFocusModel<S> fm = TableView.this.getFocusModel();
                TableViewSelectionModel<S> sm = TableView.this.getSelectionModel();
                c2.reset();
                List<TableColumn<S, ?>> removed2 = new ArrayList<>();
                List<TableColumn<S, ?>> added2 = new ArrayList<>();
                while (c2.next()) {
                    if (c2.wasRemoved()) {
                        removed2.addAll(c2.getRemoved());
                    }
                    if (c2.wasAdded()) {
                        added2.addAll(c2.getAddedSubList());
                    }
                }
                removed2.removeAll(added2);
                if (fm != null) {
                    TablePosition<S, ?> focusedCell = fm.getFocusedCell();
                    boolean match = false;
                    Iterator<TableColumn<S, ?>> it3 = removed2.iterator();
                    while (it3.hasNext()) {
                        match = focusedCell != null && focusedCell.getTableColumn() == it3.next();
                        if (match) {
                            break;
                        }
                    }
                    if (match) {
                        int matchingColumnIndex2 = ((Integer) TableView.this.lastKnownColumnIndex.getOrDefault(focusedCell.getTableColumn(), 0)).intValue();
                        int newFocusColumnIndex = matchingColumnIndex2 == 0 ? 0 : Math.min(TableView.this.getVisibleLeafColumns().size() - 1, matchingColumnIndex2 - 1);
                        fm.focus(focusedCell.getRow(), (TableColumn) TableView.this.getVisibleLeafColumn(newFocusColumnIndex));
                    }
                }
                if (sm != null) {
                    List<TablePosition> selectedCells = new ArrayList<>(sm.getSelectedCells());
                    Iterator<TablePosition> it4 = selectedCells.iterator();
                    while (it4.hasNext()) {
                        TablePosition selectedCell = it4.next();
                        boolean match2 = false;
                        Iterator<TableColumn<S, ?>> it5 = removed2.iterator();
                        while (it5.hasNext()) {
                            match2 = selectedCell != null && selectedCell.getTableColumn() == it5.next();
                            if (match2) {
                                break;
                            }
                        }
                        if (match2 && (matchingColumnIndex = ((Integer) TableView.this.lastKnownColumnIndex.getOrDefault(selectedCell.getTableColumn(), -1)).intValue()) != -1) {
                            if (sm instanceof TableViewArrayListSelectionModel) {
                                TablePosition<S, ?> fixedTablePosition = new TablePosition<>(TableView.this, selectedCell.getRow(), selectedCell.getTableColumn());
                                fixedTablePosition.fixedColumnIndex = matchingColumnIndex;
                                ((TableViewArrayListSelectionModel) sm).clearSelection(fixedTablePosition);
                            } else {
                                sm.clearSelection(selectedCell.getRow(), (TableColumn) selectedCell.getTableColumn());
                            }
                        }
                    }
                }
                TableView.this.lastKnownColumnIndex.clear();
                for (TableColumn<S, ?> tc : TableView.this.getColumns()) {
                    int index = TableView.this.getVisibleLeafIndex(tc);
                    if (index > -1) {
                        TableView.this.lastKnownColumnIndex.put(tc, Integer.valueOf(index));
                    }
                }
            }
        };
        this.lastKnownColumnIndex = new WeakHashMap<>();
        this.columnVisibleObserver = valueModel -> {
            updateVisibleLeafColumns();
        };
        this.columnSortableObserver = valueModel2 -> {
            Object col = ((Property) valueModel2).getBean();
            if (getSortOrder().contains(col)) {
                doSort(TableUtil.SortEventType.COLUMN_SORTABLE_CHANGE, col);
            }
        };
        this.columnSortTypeObserver = valueModel3 -> {
            Object col = ((Property) valueModel3).getBean();
            if (getSortOrder().contains(col)) {
                doSort(TableUtil.SortEventType.COLUMN_SORT_TYPE_CHANGE, col);
            }
        };
        this.columnComparatorObserver = valueModel4 -> {
            Object col = ((Property) valueModel4).getBean();
            if (getSortOrder().contains(col)) {
                doSort(TableUtil.SortEventType.COLUMN_COMPARATOR_CHANGE, col);
            }
        };
        this.cellSelectionModelInvalidationListener = o2 -> {
            boolean isCellSelection = ((BooleanProperty) o2).get();
            pseudoClassStateChanged(PSEUDO_CLASS_CELL_SELECTION, isCellSelection);
            pseudoClassStateChanged(PSEUDO_CLASS_ROW_SELECTION, !isCellSelection);
        };
        this.weakColumnVisibleObserver = new WeakInvalidationListener(this.columnVisibleObserver);
        this.weakColumnSortableObserver = new WeakInvalidationListener(this.columnSortableObserver);
        this.weakColumnSortTypeObserver = new WeakInvalidationListener(this.columnSortTypeObserver);
        this.weakColumnComparatorObserver = new WeakInvalidationListener(this.columnComparatorObserver);
        this.weakColumnsObserver = new WeakListChangeListener<>(this.columnsObserver);
        this.weakCellSelectionModelInvalidationListener = new WeakInvalidationListener(this.cellSelectionModelInvalidationListener);
        this.items = new SimpleObjectProperty<ObservableList<S>>(this, "items") { // from class: javafx.scene.control.TableView.6
            WeakReference<ObservableList<S>> oldItemsRef;

            @Override // javafx.beans.property.ObjectPropertyBase
            protected void invalidated() {
                ObservableList<S> oldItems = this.oldItemsRef == null ? null : this.oldItemsRef.get();
                ObservableList<S> newItems = TableView.this.getItems();
                if (newItems != null && newItems == oldItems) {
                    return;
                }
                if (!(newItems instanceof SortedList)) {
                    TableView.this.getSortOrder().clear();
                }
                this.oldItemsRef = new WeakReference<>(newItems);
            }
        };
        this.selectionModel = new SimpleObjectProperty<TableViewSelectionModel<S>>(this, "selectionModel") { // from class: javafx.scene.control.TableView.8
            TableViewSelectionModel<S> oldValue = null;

            @Override // javafx.beans.property.ObjectPropertyBase
            protected void invalidated() {
                if (this.oldValue != null) {
                    this.oldValue.cellSelectionEnabledProperty().removeListener(TableView.this.weakCellSelectionModelInvalidationListener);
                }
                this.oldValue = get();
                if (this.oldValue != null) {
                    this.oldValue.cellSelectionEnabledProperty().addListener(TableView.this.weakCellSelectionModelInvalidationListener);
                    TableView.this.weakCellSelectionModelInvalidationListener.invalidated(this.oldValue.cellSelectionEnabledProperty());
                }
            }
        };
        this.sortLock = false;
        this.lastSortEventType = null;
        this.lastSortEventSupportInfo = null;
        getStyleClass().setAll(DEFAULT_STYLE_CLASS);
        setAccessibleRole(AccessibleRole.TABLE_VIEW);
        setItems(items);
        setSelectionModel(new TableViewArrayListSelectionModel(this));
        setFocusModel(new TableViewFocusModel<>(this));
        getColumns().addListener(this.weakColumnsObserver);
        getSortOrder().addListener(c2 -> {
            doSort(TableUtil.SortEventType.SORT_ORDER_CHANGE, c2);
        });
        getProperties().addListener(new MapChangeListener<Object, Object>() { // from class: javafx.scene.control.TableView.4
            @Override // javafx.collections.MapChangeListener
            public void onChanged(MapChangeListener.Change<? extends Object, ? extends Object> c3) {
                if (c3.wasAdded() && TableView.SET_CONTENT_WIDTH.equals(c3.getKey())) {
                    if (c3.getValueAdded() instanceof Number) {
                        TableView.this.setContentWidth(((Double) c3.getValueAdded()).doubleValue());
                    }
                    TableView.this.getProperties().remove(TableView.SET_CONTENT_WIDTH);
                }
            }
        });
        this.isInited = true;
    }

    public final ObjectProperty<ObservableList<S>> itemsProperty() {
        return this.items;
    }

    public final void setItems(ObservableList<S> value) {
        itemsProperty().set(value);
    }

    public final ObservableList<S> getItems() {
        return this.items.get();
    }

    public final BooleanProperty tableMenuButtonVisibleProperty() {
        if (this.tableMenuButtonVisible == null) {
            this.tableMenuButtonVisible = new SimpleBooleanProperty(this, "tableMenuButtonVisible");
        }
        return this.tableMenuButtonVisible;
    }

    public final void setTableMenuButtonVisible(boolean value) {
        tableMenuButtonVisibleProperty().set(value);
    }

    public final boolean isTableMenuButtonVisible() {
        if (this.tableMenuButtonVisible == null) {
            return false;
        }
        return this.tableMenuButtonVisible.get();
    }

    public final void setColumnResizePolicy(Callback<ResizeFeatures, Boolean> callback) {
        columnResizePolicyProperty().set(callback);
    }

    public final Callback<ResizeFeatures, Boolean> getColumnResizePolicy() {
        return this.columnResizePolicy == null ? UNCONSTRAINED_RESIZE_POLICY : this.columnResizePolicy.get();
    }

    public final ObjectProperty<Callback<ResizeFeatures, Boolean>> columnResizePolicyProperty() {
        if (this.columnResizePolicy == null) {
            this.columnResizePolicy = new SimpleObjectProperty<Callback<ResizeFeatures, Boolean>>(this, "columnResizePolicy", UNCONSTRAINED_RESIZE_POLICY) { // from class: javafx.scene.control.TableView.7
                private Callback<ResizeFeatures, Boolean> oldPolicy;

                @Override // javafx.beans.property.ObjectPropertyBase
                protected void invalidated() {
                    if (TableView.this.isInited) {
                        get().call(new ResizeFeatures(TableView.this, null, Double.valueOf(0.0d)));
                        if (this.oldPolicy != null) {
                            PseudoClass state = PseudoClass.getPseudoClass(this.oldPolicy.toString());
                            TableView.this.pseudoClassStateChanged(state, false);
                        }
                        if (get() != null) {
                            PseudoClass state2 = PseudoClass.getPseudoClass(get().toString());
                            TableView.this.pseudoClassStateChanged(state2, true);
                        }
                        this.oldPolicy = get();
                    }
                }
            };
        }
        return this.columnResizePolicy;
    }

    public final ObjectProperty<Callback<TableView<S>, TableRow<S>>> rowFactoryProperty() {
        if (this.rowFactory == null) {
            this.rowFactory = new SimpleObjectProperty(this, "rowFactory");
        }
        return this.rowFactory;
    }

    public final void setRowFactory(Callback<TableView<S>, TableRow<S>> value) {
        rowFactoryProperty().set(value);
    }

    public final Callback<TableView<S>, TableRow<S>> getRowFactory() {
        if (this.rowFactory == null) {
            return null;
        }
        return this.rowFactory.get();
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

    public final ObjectProperty<TableViewSelectionModel<S>> selectionModelProperty() {
        return this.selectionModel;
    }

    public final void setSelectionModel(TableViewSelectionModel<S> value) {
        selectionModelProperty().set(value);
    }

    public final TableViewSelectionModel<S> getSelectionModel() {
        return this.selectionModel.get();
    }

    public final void setFocusModel(TableViewFocusModel<S> value) {
        focusModelProperty().set(value);
    }

    public final TableViewFocusModel<S> getFocusModel() {
        if (this.focusModel == null) {
            return null;
        }
        return this.focusModel.get();
    }

    public final ObjectProperty<TableViewFocusModel<S>> focusModelProperty() {
        if (this.focusModel == null) {
            this.focusModel = new SimpleObjectProperty(this, "focusModel");
        }
        return this.focusModel;
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
            this.fixedCellSize = new StyleableDoubleProperty(-1.0d) { // from class: javafx.scene.control.TableView.9
                @Override // javafx.css.StyleableProperty
                public CssMetaData<? extends Styleable, Number> getCssMetaData() {
                    return StyleableProperties.FIXED_CELL_SIZE;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return TableView.this;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return "fixedCellSize";
                }
            };
        }
        return this.fixedCellSize;
    }

    private void setEditingCell(TablePosition<S, ?> value) {
        editingCellPropertyImpl().set(value);
    }

    public final TablePosition<S, ?> getEditingCell() {
        if (this.editingCell == null) {
            return null;
        }
        return this.editingCell.get();
    }

    public final ReadOnlyObjectProperty<TablePosition<S, ?>> editingCellProperty() {
        return editingCellPropertyImpl().getReadOnlyProperty();
    }

    private ReadOnlyObjectWrapper<TablePosition<S, ?>> editingCellPropertyImpl() {
        if (this.editingCell == null) {
            this.editingCell = new ReadOnlyObjectWrapper<>(this, "editingCell");
        }
        return this.editingCell;
    }

    private void setComparator(Comparator<S> value) {
        comparatorPropertyImpl().set(value);
    }

    public final Comparator<S> getComparator() {
        if (this.comparator == null) {
            return null;
        }
        return this.comparator.get();
    }

    public final ReadOnlyObjectProperty<Comparator<S>> comparatorProperty() {
        return comparatorPropertyImpl().getReadOnlyProperty();
    }

    private ReadOnlyObjectWrapper<Comparator<S>> comparatorPropertyImpl() {
        if (this.comparator == null) {
            this.comparator = new ReadOnlyObjectWrapper<>(this, "comparator");
        }
        return this.comparator;
    }

    public final void setSortPolicy(Callback<TableView<S>, Boolean> callback) {
        sortPolicyProperty().set(callback);
    }

    public final Callback<TableView<S>, Boolean> getSortPolicy() {
        return this.sortPolicy == null ? DEFAULT_SORT_POLICY : this.sortPolicy.get();
    }

    public final ObjectProperty<Callback<TableView<S>, Boolean>> sortPolicyProperty() {
        if (this.sortPolicy == null) {
            this.sortPolicy = new SimpleObjectProperty<Callback<TableView<S>, Boolean>>(this, "sortPolicy", DEFAULT_SORT_POLICY) { // from class: javafx.scene.control.TableView.10
                @Override // javafx.beans.property.ObjectPropertyBase
                protected void invalidated() {
                    TableView.this.sort();
                }
            };
        }
        return this.sortPolicy;
    }

    public void setOnSort(EventHandler<SortEvent<TableView<S>>> value) {
        onSortProperty().set(value);
    }

    public EventHandler<SortEvent<TableView<S>>> getOnSort() {
        if (this.onSort != null) {
            return this.onSort.get();
        }
        return null;
    }

    public ObjectProperty<EventHandler<SortEvent<TableView<S>>>> onSortProperty() {
        if (this.onSort == null) {
            this.onSort = new ObjectPropertyBase<EventHandler<SortEvent<TableView<S>>>>() { // from class: javafx.scene.control.TableView.11
                @Override // javafx.beans.property.ObjectPropertyBase
                protected void invalidated() {
                    EventType<SortEvent<TableView<S>>> eventType = SortEvent.sortEvent();
                    EventHandler<SortEvent<TableView<S>>> eventHandler = get();
                    TableView.this.setEventHandler(eventType, eventHandler);
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return TableView.this;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return "onSort";
                }
            };
        }
        return this.onSort;
    }

    public final ObservableList<TableColumn<S, ?>> getColumns() {
        return this.columns;
    }

    public final ObservableList<TableColumn<S, ?>> getSortOrder() {
        return this.sortOrder;
    }

    public void scrollTo(int index) {
        ControlUtils.scrollToIndex(this, index);
    }

    public void scrollTo(S object) {
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
            this.onScrollTo = new ObjectPropertyBase<EventHandler<ScrollToEvent<Integer>>>() { // from class: javafx.scene.control.TableView.12
                @Override // javafx.beans.property.ObjectPropertyBase
                protected void invalidated() {
                    TableView.this.setEventHandler(ScrollToEvent.scrollToTopIndex(), get());
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return TableView.this;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return "onScrollTo";
                }
            };
        }
        return this.onScrollTo;
    }

    public void scrollToColumn(TableColumn<S, ?> column) {
        ControlUtils.scrollToColumn(this, column);
    }

    public void scrollToColumnIndex(int columnIndex) {
        if (getColumns() != null) {
            ControlUtils.scrollToColumn(this, getColumns().get(columnIndex));
        }
    }

    public void setOnScrollToColumn(EventHandler<ScrollToEvent<TableColumn<S, ?>>> value) {
        onScrollToColumnProperty().set(value);
    }

    public EventHandler<ScrollToEvent<TableColumn<S, ?>>> getOnScrollToColumn() {
        if (this.onScrollToColumn != null) {
            return this.onScrollToColumn.get();
        }
        return null;
    }

    public ObjectProperty<EventHandler<ScrollToEvent<TableColumn<S, ?>>>> onScrollToColumnProperty() {
        if (this.onScrollToColumn == null) {
            this.onScrollToColumn = new ObjectPropertyBase<EventHandler<ScrollToEvent<TableColumn<S, ?>>>>() { // from class: javafx.scene.control.TableView.13
                @Override // javafx.beans.property.ObjectPropertyBase
                protected void invalidated() {
                    EventType<ScrollToEvent<TableColumn<S, ?>>> type = ScrollToEvent.scrollToColumn();
                    TableView.this.setEventHandler(type, get());
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return TableView.this;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return "onScrollToColumn";
                }
            };
        }
        return this.onScrollToColumn;
    }

    public boolean resizeColumn(TableColumn<S, ?> column, double delta) {
        if (column == null || Double.compare(delta, 0.0d) == 0) {
            return false;
        }
        boolean allowed = getColumnResizePolicy().call(new ResizeFeatures(this, column, Double.valueOf(delta))).booleanValue();
        return allowed;
    }

    public void edit(int row, TableColumn<S, ?> column) {
        if (isEditable()) {
            if (column != null && !column.isEditable()) {
                return;
            }
            if (row < 0 && column == null) {
                setEditingCell(null);
            } else {
                setEditingCell(new TablePosition<>(this, row, column));
            }
        }
    }

    public ObservableList<TableColumn<S, ?>> getVisibleLeafColumns() {
        return this.unmodifiableVisibleLeafColumns;
    }

    public int getVisibleLeafIndex(TableColumn<S, ?> column) {
        return this.visibleLeafColumns.indexOf(column);
    }

    public TableColumn<S, ?> getVisibleLeafColumn(int column) {
        if (column < 0 || column >= this.visibleLeafColumns.size()) {
            return null;
        }
        return this.visibleLeafColumns.get(column);
    }

    @Override // javafx.scene.control.Control
    protected Skin<?> createDefaultSkin() {
        return new TableViewSkin(this);
    }

    public void sort() {
        ObservableList<? extends TableColumnBase<S, ?>> sortOrder = getSortOrder();
        Comparator<S> oldComparator = getComparator();
        setComparator(sortOrder.isEmpty() ? null : new TableColumnComparatorBase.TableColumnComparator(sortOrder));
        SortEvent<TableView<S>> sortEvent = new SortEvent<>(this, this);
        fireEvent(sortEvent);
        if (sortEvent.isConsumed()) {
            return;
        }
        List<TablePosition> prevState = new ArrayList<>(getSelectionModel().getSelectedCells());
        int itemCount = prevState.size();
        getSelectionModel().startAtomic();
        Callback<TableView<S>, Boolean> sortPolicy = getSortPolicy();
        if (sortPolicy == null) {
            return;
        }
        Boolean success = sortPolicy.call(this);
        getSelectionModel().stopAtomic();
        if (success == null || !success.booleanValue()) {
            this.sortLock = true;
            TableUtil.handleSortFailure(sortOrder, this.lastSortEventType, this.lastSortEventSupportInfo);
            setComparator(oldComparator);
            this.sortLock = false;
            return;
        }
        if (getSelectionModel() instanceof TableViewArrayListSelectionModel) {
            TableViewArrayListSelectionModel<S> sm = (TableViewArrayListSelectionModel) getSelectionModel();
            ObservableList<TablePosition<S, ?>> newState = sm.getSelectedCells();
            List<TablePosition<S, ?>> removed = new ArrayList<>();
            for (int i2 = 0; i2 < itemCount; i2++) {
                TablePosition<S, ?> prevItem = prevState.get(i2);
                if (!newState.contains(prevItem)) {
                    removed.add(prevItem);
                }
            }
            if (!removed.isEmpty()) {
                ListChangeListener.Change<TablePosition<S, ?>> c2 = new NonIterableChange.GenericAddRemoveChange<>(0, itemCount, removed, newState);
                sm.handleSelectedCellsListChangeEvent(c2);
            }
        }
    }

    public void refresh() {
        getProperties().put(TableViewSkinBase.RECREATE, Boolean.TRUE);
    }

    private void doSort(TableUtil.SortEventType sortEventType, Object... supportInfo) {
        if (this.sortLock) {
            return;
        }
        this.lastSortEventType = sortEventType;
        this.lastSortEventSupportInfo = supportInfo;
        sort();
        this.lastSortEventType = null;
        this.lastSortEventSupportInfo = null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setContentWidth(double contentWidth) {
        this.contentWidth = contentWidth;
        if (this.isInited) {
            getColumnResizePolicy().call(new ResizeFeatures(this, null, Double.valueOf(0.0d)));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateVisibleLeafColumns() {
        List<TableColumn<S, ?>> cols = new ArrayList<>();
        buildVisibleLeafColumns(getColumns(), cols);
        this.visibleLeafColumns.setAll(cols);
        getColumnResizePolicy().call(new ResizeFeatures(this, null, Double.valueOf(0.0d)));
    }

    private void buildVisibleLeafColumns(List<TableColumn<S, ?>> cols, List<TableColumn<S, ?>> vlc) {
        for (TableColumn<S, ?> c2 : cols) {
            if (c2 != null) {
                boolean hasChildren = !c2.getColumns().isEmpty();
                if (hasChildren) {
                    buildVisibleLeafColumns(c2.getColumns(), vlc);
                } else if (c2.isVisible()) {
                    vlc.add(c2);
                }
            }
        }
    }

    /* loaded from: jfxrt.jar:javafx/scene/control/TableView$StyleableProperties.class */
    private static class StyleableProperties {
        private static final CssMetaData<TableView<?>, Number> FIXED_CELL_SIZE = new CssMetaData<TableView<?>, Number>("-fx-fixed-cell-size", SizeConverter.getInstance(), Double.valueOf(-1.0d)) { // from class: javafx.scene.control.TableView.StyleableProperties.1
            @Override // javafx.css.CssMetaData
            public Double getInitialValue(TableView<?> node) {
                return Double.valueOf(node.getFixedCellSize());
            }

            @Override // javafx.css.CssMetaData
            public boolean isSettable(TableView<?> n2) {
                return ((TableView) n2).fixedCellSize == null || !((TableView) n2).fixedCellSize.isBound();
            }

            @Override // javafx.css.CssMetaData
            public StyleableProperty<Number> getStyleableProperty(TableView<?> n2) {
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
            case COLUMN_COUNT:
                return Integer.valueOf(getVisibleLeafColumns().size());
            case ROW_COUNT:
                return Integer.valueOf(getItems().size());
            case SELECTED_ITEMS:
                ObservableList<TableRow<S>> rows = (ObservableList) super.queryAccessibleAttribute(attribute, parameters);
                List<Node> selection = new ArrayList<>();
                Iterator<TableRow<S>> it = rows.iterator();
                while (it.hasNext()) {
                    ObservableList<Node> cells = (ObservableList) it.next().queryAccessibleAttribute(attribute, parameters);
                    if (cells != null) {
                        selection.addAll(cells);
                    }
                }
                return FXCollections.observableArrayList(selection);
            case FOCUS_ITEM:
                Node row = (Node) super.queryAccessibleAttribute(attribute, parameters);
                if (row == null) {
                    return null;
                }
                Node cell = (Node) row.queryAccessibleAttribute(attribute, parameters);
                return cell != null ? cell : row;
            case CELL_AT_ROW_COLUMN:
                TableRow<S> row2 = (TableRow) super.queryAccessibleAttribute(attribute, parameters);
                if (row2 != null) {
                    return row2.queryAccessibleAttribute(attribute, parameters);
                }
                return null;
            case MULTIPLE_SELECTION:
                MultipleSelectionModel<S> sm = getSelectionModel();
                return Boolean.valueOf(sm != null && sm.getSelectionMode() == SelectionMode.MULTIPLE);
            default:
                return super.queryAccessibleAttribute(attribute, parameters);
        }
    }

    /* loaded from: jfxrt.jar:javafx/scene/control/TableView$ResizeFeatures.class */
    public static class ResizeFeatures<S> extends ResizeFeaturesBase<S> {
        private TableView<S> table;

        public ResizeFeatures(TableView<S> table, TableColumn<S, ?> column, Double delta) {
            super(column, delta);
            this.table = table;
        }

        @Override // javafx.scene.control.ResizeFeaturesBase
        public TableColumn<S, ?> getColumn() {
            return (TableColumn) super.getColumn();
        }

        public TableView<S> getTable() {
            return this.table;
        }
    }

    /* loaded from: jfxrt.jar:javafx/scene/control/TableView$TableViewSelectionModel.class */
    public static abstract class TableViewSelectionModel<S> extends TableSelectionModel<S> {
        private final TableView<S> tableView;
        boolean blockFocusCall = false;

        public abstract ObservableList<TablePosition> getSelectedCells();

        public abstract boolean isSelected(int i2, TableColumn<S, ?> tableColumn);

        public abstract void select(int i2, TableColumn<S, ?> tableColumn);

        public abstract void clearAndSelect(int i2, TableColumn<S, ?> tableColumn);

        public abstract void clearSelection(int i2, TableColumn<S, ?> tableColumn);

        public TableViewSelectionModel(TableView<S> tableView) {
            if (tableView == null) {
                throw new NullPointerException("TableView can not be null");
            }
            this.tableView = tableView;
        }

        @Override // javafx.scene.control.TableSelectionModel
        public boolean isSelected(int row, TableColumnBase<S, ?> column) {
            return isSelected(row, (TableColumn) column);
        }

        @Override // javafx.scene.control.TableSelectionModel
        public void select(int row, TableColumnBase<S, ?> column) {
            select(row, (TableColumn) column);
        }

        @Override // javafx.scene.control.TableSelectionModel
        public void clearAndSelect(int row, TableColumnBase<S, ?> column) {
            clearAndSelect(row, (TableColumn) column);
        }

        @Override // javafx.scene.control.TableSelectionModel
        public void clearSelection(int row, TableColumnBase<S, ?> column) {
            clearSelection(row, (TableColumn) column);
        }

        @Override // javafx.scene.control.TableSelectionModel
        public void selectRange(int minRow, TableColumnBase<S, ?> minColumn, int maxRow, TableColumnBase<S, ?> maxColumn) {
            int minColumnIndex = this.tableView.getVisibleLeafIndex((TableColumn) minColumn);
            int maxColumnIndex = this.tableView.getVisibleLeafIndex((TableColumn) maxColumn);
            for (int _row = minRow; _row <= maxRow; _row++) {
                for (int _col = minColumnIndex; _col <= maxColumnIndex; _col++) {
                    select(_row, (TableColumn) this.tableView.getVisibleLeafColumn(_col));
                }
            }
        }

        public TableView<S> getTableView() {
            return this.tableView;
        }

        protected List<S> getTableModel() {
            return this.tableView.getItems();
        }

        @Override // javafx.scene.control.MultipleSelectionModelBase
        protected S getModelItem(int index) {
            if (index < 0 || index >= getItemCount()) {
                return null;
            }
            return this.tableView.getItems().get(index);
        }

        @Override // javafx.scene.control.MultipleSelectionModelBase
        protected int getItemCount() {
            return getTableModel().size();
        }

        @Override // javafx.scene.control.MultipleSelectionModelBase
        public void focus(int row) {
            focus(row, null);
        }

        @Override // javafx.scene.control.MultipleSelectionModelBase
        public int getFocusedIndex() {
            return getFocusedCell().getRow();
        }

        void focus(int row, TableColumn<S, ?> column) {
            focus(new TablePosition<>(getTableView(), row, column));
            getTableView().notifyAccessibleAttributeChanged(AccessibleAttribute.FOCUS_ITEM);
        }

        void focus(TablePosition<S, ?> pos) {
            if (this.blockFocusCall || getTableView().getFocusModel() == null) {
                return;
            }
            getTableView().getFocusModel().focus(pos.getRow(), (TableColumn) pos.getTableColumn());
        }

        TablePosition<S, ?> getFocusedCell() {
            if (getTableView().getFocusModel() == null) {
                return new TablePosition<>(getTableView(), -1, null);
            }
            return getTableView().getFocusModel().getFocusedCell();
        }
    }

    /* loaded from: jfxrt.jar:javafx/scene/control/TableView$TableViewArrayListSelectionModel.class */
    static class TableViewArrayListSelectionModel<S> extends TableViewSelectionModel<S> {
        private int itemCount;
        private final MappingChange.Map<TablePosition<S, ?>, S> cellToItemsMap;
        private final MappingChange.Map<TablePosition<S, ?>, Integer> cellToIndicesMap;
        private final TableView<S> tableView;
        final ListChangeListener<S> itemsContentListener;
        final WeakListChangeListener<S> weakItemsContentListener;
        private final SelectedCellsMap<TablePosition<S, ?>> selectedCellsMap;
        private final ReadOnlyUnbackedObservableList<S> selectedItems;
        private final ReadOnlyUnbackedObservableList<TablePosition<S, ?>> selectedCellsSeq;
        private int previousModelSize;

        public TableViewArrayListSelectionModel(final TableView<S> tableView) {
            super(tableView);
            this.itemCount = 0;
            this.cellToItemsMap = f2 -> {
                return getModelItem(f2.getRow());
            };
            this.cellToIndicesMap = f3 -> {
                return Integer.valueOf(f3.getRow());
            };
            this.itemsContentListener = c2 -> {
                updateItemCount();
                List<S> items1 = getTableModel();
                while (c2.next()) {
                    if (c2.wasReplaced() || c2.getAddedSize() == getItemCount()) {
                        this.selectedItemChange = c2;
                        updateDefaultSelection();
                        this.selectedItemChange = null;
                        return;
                    }
                    S selectedItem = getSelectedItem();
                    int selectedIndex = getSelectedIndex();
                    if (items1 == null || items1.isEmpty()) {
                        clearSelection();
                    } else if (getSelectedIndex() == -1 && getSelectedItem() != null) {
                        int newIndex = items1.indexOf(getSelectedItem());
                        if (newIndex != -1) {
                            setSelectedIndex(newIndex);
                        }
                    } else if (c2.wasRemoved() && c2.getRemovedSize() == 1 && !c2.wasAdded() && selectedItem != null && selectedItem.equals(c2.getRemoved().get(0)) && getSelectedIndex() < getItemCount()) {
                        int previousRow = selectedIndex == 0 ? 0 : selectedIndex - 1;
                        S newSelectedItem = getModelItem(previousRow);
                        if (!selectedItem.equals(newSelectedItem)) {
                            clearAndSelect(previousRow);
                        }
                    }
                }
                updateSelection(c2);
            };
            this.weakItemsContentListener = new WeakListChangeListener<>(this.itemsContentListener);
            this.previousModelSize = 0;
            this.tableView = tableView;
            this.tableView.itemsProperty().addListener(new InvalidationListener() { // from class: javafx.scene.control.TableView.TableViewArrayListSelectionModel.1
                private WeakReference<ObservableList<S>> weakItemsRef;

                {
                    this.weakItemsRef = new WeakReference<>(tableView.getItems());
                }

                @Override // javafx.beans.InvalidationListener
                public void invalidated(Observable observable) {
                    ObservableList<S> oldItems = this.weakItemsRef.get();
                    this.weakItemsRef = new WeakReference<>(tableView.getItems());
                    TableViewArrayListSelectionModel.this.updateItemsObserver(oldItems, tableView.getItems());
                }
            });
            this.selectedCellsMap = new SelectedCellsMap<TablePosition<S, ?>>(c3 -> {
                handleSelectedCellsListChangeEvent(c3);
            }) { // from class: javafx.scene.control.TableView.TableViewArrayListSelectionModel.2
                @Override // com.sun.javafx.scene.control.SelectedCellsMap
                public boolean isCellSelectionEnabled() {
                    return TableViewArrayListSelectionModel.this.isCellSelectionEnabled();
                }
            };
            this.selectedItems = new ReadOnlyUnbackedObservableList<S>() { // from class: javafx.scene.control.TableView.TableViewArrayListSelectionModel.3
                /* JADX WARN: Multi-variable type inference failed */
                @Override // com.sun.javafx.scene.control.ReadOnlyUnbackedObservableList, java.util.List
                public S get(int i2) {
                    return TableViewArrayListSelectionModel.this.getModelItem(((Integer) TableViewArrayListSelectionModel.this.getSelectedIndices().get(i2)).intValue());
                }

                @Override // com.sun.javafx.scene.control.ReadOnlyUnbackedObservableList, java.util.List, java.util.Collection, java.util.Set
                public int size() {
                    return TableViewArrayListSelectionModel.this.getSelectedIndices().size();
                }
            };
            this.selectedCellsSeq = new ReadOnlyUnbackedObservableList<TablePosition<S, ?>>() { // from class: javafx.scene.control.TableView.TableViewArrayListSelectionModel.4
                @Override // com.sun.javafx.scene.control.ReadOnlyUnbackedObservableList, java.util.List
                public TablePosition<S, ?> get(int i2) {
                    return (TablePosition) TableViewArrayListSelectionModel.this.selectedCellsMap.get(i2);
                }

                @Override // com.sun.javafx.scene.control.ReadOnlyUnbackedObservableList, java.util.List, java.util.Collection, java.util.Set
                public int size() {
                    return TableViewArrayListSelectionModel.this.selectedCellsMap.size();
                }
            };
            ObservableList<S> items = getTableView().getItems();
            if (items != null) {
                items.addListener(this.weakItemsContentListener);
            }
            updateItemCount();
            updateDefaultSelection();
            cellSelectionEnabledProperty().addListener(o2 -> {
                updateDefaultSelection();
                TableCellBehaviorBase.setAnchor(tableView, getFocusedCell(), true);
            });
        }

        @Override // javafx.scene.control.TableSelectionModel, javafx.scene.control.MultipleSelectionModelBase, javafx.scene.control.MultipleSelectionModel
        public ObservableList<S> getSelectedItems() {
            return this.selectedItems;
        }

        @Override // javafx.scene.control.TableView.TableViewSelectionModel
        public ObservableList<TablePosition> getSelectedCells() {
            return this.selectedCellsSeq;
        }

        private void updateSelection(ListChangeListener.Change<? extends S> c2) {
            c2.reset();
            int shift = 0;
            int startRow = -1;
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
                    startRow = c2.getFrom();
                    shift += c2.wasAdded() ? c2.getAddedSize() : -c2.getRemovedSize();
                } else if (c2.wasPermutated()) {
                    startAtomic();
                    int oldSelectedIndex = getSelectedIndex();
                    int length = c2.getTo() - c2.getFrom();
                    HashMap<Integer, Integer> pMap = new HashMap<>(length);
                    for (int i2 = c2.getFrom(); i2 < c2.getTo(); i2++) {
                        pMap.put(Integer.valueOf(i2), Integer.valueOf(c2.getPermutation(i2)));
                    }
                    List<TablePosition<S, ?>> selectedIndices = new ArrayList<>(getSelectedCells());
                    List<TablePosition<S, ?>> newIndices = new ArrayList<>(selectedIndices.size());
                    boolean selectionIndicesChanged = false;
                    for (int i3 = 0; i3 < selectedIndices.size(); i3++) {
                        TablePosition<S, ?> oldIndex = selectedIndices.get(i3);
                        int oldRow = oldIndex.getRow();
                        if (pMap.containsKey(Integer.valueOf(oldRow))) {
                            int newIndex = pMap.get(Integer.valueOf(oldRow)).intValue();
                            selectionIndicesChanged = selectionIndicesChanged || newIndex != oldRow;
                            newIndices.add(new TablePosition<>(oldIndex.getTableView(), newIndex, oldIndex.getTableColumn()));
                        }
                    }
                    if (selectionIndicesChanged) {
                        quietClearSelection();
                        stopAtomic();
                        this.selectedCellsMap.setAll(newIndices);
                        if (oldSelectedIndex >= 0 && oldSelectedIndex < this.itemCount) {
                            int newIndex2 = c2.getPermutation(oldSelectedIndex);
                            setSelectedIndex(newIndex2);
                            focus(newIndex2);
                        }
                    } else {
                        stopAtomic();
                    }
                }
            }
            if (shift != 0 && startRow >= 0) {
                List<TablePosition<S, ?>> newIndices2 = new ArrayList<>(this.selectedCellsMap.size());
                for (int i4 = 0; i4 < this.selectedCellsMap.size(); i4++) {
                    TablePosition<S, ?> old = (TablePosition) this.selectedCellsMap.get(i4);
                    int oldRow2 = old.getRow();
                    int newRow = Math.max(0, oldRow2 < startRow ? oldRow2 : oldRow2 + shift);
                    if (oldRow2 >= startRow) {
                        if (oldRow2 == 0 && shift == -1) {
                            newIndices2.add(new TablePosition<>(getTableView(), 0, old.getTableColumn()));
                        } else {
                            newIndices2.add(new TablePosition<>(getTableView(), newRow, old.getTableColumn()));
                        }
                    }
                }
                int newIndicesSize = newIndices2.size();
                if ((c2.wasRemoved() || c2.wasAdded()) && newIndicesSize > 0) {
                    TablePosition<S, ?> anchor = (TablePosition) TableCellBehavior.getAnchor(this.tableView, null);
                    if (anchor != null) {
                        boolean isAnchorSelected = isSelected(anchor.getRow(), (TableColumn) anchor.getTableColumn());
                        if (isAnchorSelected) {
                            TablePosition<S, ?> newAnchor = new TablePosition<>(this.tableView, anchor.getRow() + shift, anchor.getTableColumn());
                            TableCellBehavior.setAnchor(this.tableView, newAnchor, false);
                        }
                    }
                    quietClearSelection();
                    this.blockFocusCall = true;
                    for (int i5 = 0; i5 < newIndicesSize; i5++) {
                        TablePosition<S, ?> tp = newIndices2.get(i5);
                        select(tp.getRow(), (TableColumn) tp.getTableColumn());
                    }
                    this.blockFocusCall = false;
                }
            }
            this.previousModelSize = getItemCount();
        }

        @Override // javafx.scene.control.TableSelectionModel, javafx.scene.control.MultipleSelectionModelBase, javafx.scene.control.SelectionModel
        public void clearAndSelect(int row) {
            clearAndSelect(row, (TableColumn) null);
        }

        @Override // javafx.scene.control.TableView.TableViewSelectionModel
        public void clearAndSelect(int row, TableColumn<S, ?> column) {
            ListChangeListener.Change<TablePosition<S, ?>> change;
            if (row < 0 || row >= getItemCount()) {
                return;
            }
            TablePosition<S, ?> newTablePosition = new TablePosition<>(getTableView(), row, column);
            boolean isCellSelectionEnabled = isCellSelectionEnabled();
            TableCellBehavior.setAnchor(this.tableView, newTablePosition, false);
            if (isCellSelectionEnabled && column == null) {
                return;
            }
            boolean wasSelected = isSelected(row, (TableColumn) column);
            List<TablePosition<S, ?>> previousSelection = new ArrayList<>(this.selectedCellsMap.getSelectedCells());
            if (wasSelected && previousSelection.size() == 1) {
                TablePosition<S, ?> selectedCell = getSelectedCells().get(0);
                if (getSelectedItem() == getModelItem(row) && selectedCell.getRow() == row && selectedCell.getTableColumn() == column) {
                    return;
                }
            }
            startAtomic();
            clearSelection();
            select(row, (TableColumn) column);
            stopAtomic();
            if (isCellSelectionEnabled) {
                previousSelection.remove(newTablePosition);
            } else {
                Iterator<TablePosition<S, ?>> it = previousSelection.iterator();
                while (true) {
                    if (!it.hasNext()) {
                        break;
                    }
                    TablePosition<S, ?> tp = it.next();
                    if (tp.getRow() == row) {
                        previousSelection.remove(tp);
                        break;
                    }
                }
            }
            if (wasSelected) {
                change = ControlUtils.buildClearAndSelectChange(this.selectedCellsSeq, previousSelection, row);
            } else {
                int changeIndex = this.selectedCellsSeq.indexOf(newTablePosition);
                change = new NonIterableChange.GenericAddRemoveChange<>(changeIndex, changeIndex + 1, previousSelection, this.selectedCellsSeq);
            }
            handleSelectedCellsListChangeEvent(change);
        }

        @Override // javafx.scene.control.TableSelectionModel, javafx.scene.control.MultipleSelectionModelBase, javafx.scene.control.SelectionModel
        public void select(int row) {
            select(row, (TableColumn) null);
        }

        @Override // javafx.scene.control.TableView.TableViewSelectionModel
        public void select(int row, TableColumn<S, ?> column) {
            if (row < 0 || row >= getItemCount()) {
                return;
            }
            if (isCellSelectionEnabled() && column == null) {
                List<TableColumn<S, ?>> columns = getTableView().getVisibleLeafColumns();
                for (int i2 = 0; i2 < columns.size(); i2++) {
                    select(row, (TableColumn) columns.get(i2));
                }
                return;
            }
            TablePosition<S, ?> pos = new TablePosition<>(getTableView(), row, column);
            if (getSelectionMode() == SelectionMode.SINGLE) {
                startAtomic();
                quietClearSelection();
                stopAtomic();
            }
            if (TableCellBehavior.hasDefaultAnchor(this.tableView)) {
                TableCellBehavior.removeAnchor(this.tableView);
            }
            this.selectedCellsMap.add(pos);
            updateSelectedIndex(row);
            focus(row, column);
        }

        @Override // javafx.scene.control.TableSelectionModel, javafx.scene.control.MultipleSelectionModelBase, javafx.scene.control.SelectionModel
        public void select(S obj) {
            if (obj == null && getSelectionMode() == SelectionMode.SINGLE) {
                clearSelection();
                return;
            }
            for (int i2 = 0; i2 < getItemCount(); i2++) {
                S rowObj = getModelItem(i2);
                if (rowObj != null && rowObj.equals(obj)) {
                    if (isSelected(i2)) {
                        return;
                    }
                    if (getSelectionMode() == SelectionMode.SINGLE) {
                        quietClearSelection();
                    }
                    select(i2);
                    return;
                }
            }
            setSelectedIndex(-1);
            setSelectedItem(obj);
        }

        @Override // javafx.scene.control.TableSelectionModel, javafx.scene.control.MultipleSelectionModelBase, javafx.scene.control.MultipleSelectionModel
        public void selectIndices(int row, int... rows) {
            if (rows == null) {
                select(row);
                return;
            }
            int rowCount = getItemCount();
            if (getSelectionMode() == SelectionMode.SINGLE) {
                quietClearSelection();
                int i2 = rows.length - 1;
                while (true) {
                    if (i2 < 0) {
                        break;
                    }
                    int index = rows[i2];
                    if (index >= 0 && index < rowCount) {
                        select(index);
                        break;
                    }
                    i2--;
                }
                if (this.selectedCellsMap.isEmpty() && row > 0 && row < rowCount) {
                    select(row);
                    return;
                }
                return;
            }
            int lastIndex = -1;
            Set<TablePosition<S, ?>> positions = new LinkedHashSet<>();
            if (row >= 0 && row < rowCount) {
                if (isCellSelectionEnabled()) {
                    List<TableColumn<S, ?>> columns = getTableView().getVisibleLeafColumns();
                    for (int column = 0; column < columns.size(); column++) {
                        if (!this.selectedCellsMap.isSelected(row, column)) {
                            positions.add(new TablePosition<>(getTableView(), row, columns.get(column)));
                        }
                    }
                } else {
                    boolean match = this.selectedCellsMap.isSelected(row, -1);
                    if (!match) {
                        positions.add(new TablePosition<>(getTableView(), row, null));
                    }
                }
                lastIndex = row;
            }
            for (int index2 : rows) {
                if (index2 >= 0 && index2 < rowCount) {
                    lastIndex = index2;
                    if (isCellSelectionEnabled()) {
                        List<TableColumn<S, ?>> columns2 = getTableView().getVisibleLeafColumns();
                        for (int column2 = 0; column2 < columns2.size(); column2++) {
                            if (!this.selectedCellsMap.isSelected(index2, column2)) {
                                positions.add(new TablePosition<>(getTableView(), index2, columns2.get(column2)));
                                lastIndex = index2;
                            }
                        }
                    } else if (!this.selectedCellsMap.isSelected(index2, -1)) {
                        positions.add(new TablePosition<>(getTableView(), index2, null));
                    }
                }
            }
            this.selectedCellsMap.addAll(positions);
            if (lastIndex != -1) {
                select(lastIndex);
            }
        }

        @Override // javafx.scene.control.TableSelectionModel, javafx.scene.control.MultipleSelectionModelBase, javafx.scene.control.MultipleSelectionModel
        public void selectAll() {
            if (getSelectionMode() == SelectionMode.SINGLE) {
                return;
            }
            if (isCellSelectionEnabled()) {
                List<TablePosition<S, ?>> indices = new ArrayList<>();
                TablePosition<S, ?> tp = null;
                for (int col = 0; col < getTableView().getVisibleLeafColumns().size(); col++) {
                    TableColumn<S, ?> column = getTableView().getVisibleLeafColumns().get(col);
                    for (int row = 0; row < getItemCount(); row++) {
                        tp = new TablePosition<>(getTableView(), row, column);
                        indices.add(tp);
                    }
                }
                this.selectedCellsMap.setAll(indices);
                if (tp != null) {
                    select(tp.getRow(), (TableColumn) tp.getTableColumn());
                    focus(tp.getRow(), tp.getTableColumn());
                    return;
                }
                return;
            }
            List<TablePosition<S, ?>> indices2 = new ArrayList<>();
            for (int i2 = 0; i2 < getItemCount(); i2++) {
                indices2.add(new TablePosition<>(getTableView(), i2, null));
            }
            this.selectedCellsMap.setAll(indices2);
            int focusedIndex = getFocusedIndex();
            if (focusedIndex == -1) {
                int itemCount = getItemCount();
                if (itemCount > 0) {
                    select(itemCount - 1);
                    focus(indices2.get(indices2.size() - 1));
                    return;
                }
                return;
            }
            select(focusedIndex);
            focus(focusedIndex);
        }

        @Override // javafx.scene.control.TableView.TableViewSelectionModel, javafx.scene.control.TableSelectionModel
        public void selectRange(int minRow, TableColumnBase<S, ?> minColumn, int maxRow, TableColumnBase<S, ?> maxColumn) {
            if (getSelectionMode() == SelectionMode.SINGLE) {
                quietClearSelection();
                select(maxRow, maxColumn);
                return;
            }
            startAtomic();
            int itemCount = getItemCount();
            boolean isCellSelectionEnabled = isCellSelectionEnabled();
            int minColumnIndex = this.tableView.getVisibleLeafIndex((TableColumn) minColumn);
            int maxColumnIndex = this.tableView.getVisibleLeafIndex((TableColumn) maxColumn);
            int _minColumnIndex = Math.min(minColumnIndex, maxColumnIndex);
            int _maxColumnIndex = Math.max(minColumnIndex, maxColumnIndex);
            int _minRow = Math.min(minRow, maxRow);
            int _maxRow = Math.max(minRow, maxRow);
            List<TablePosition<S, ?>> cellsToSelect = new ArrayList<>();
            for (int _row = _minRow; _row <= _maxRow; _row++) {
                if (_row >= 0 && _row < itemCount) {
                    if (!isCellSelectionEnabled) {
                        cellsToSelect.add(new TablePosition<>(this.tableView, _row, (TableColumn) minColumn));
                    } else {
                        for (int _col = _minColumnIndex; _col <= _maxColumnIndex; _col++) {
                            TableColumn<S, ?> column = this.tableView.getVisibleLeafColumn(_col);
                            if (column != null || !isCellSelectionEnabled) {
                                cellsToSelect.add(new TablePosition<>(this.tableView, _row, column));
                            }
                        }
                    }
                }
            }
            cellsToSelect.removeAll(getSelectedCells());
            this.selectedCellsMap.addAll(cellsToSelect);
            stopAtomic();
            updateSelectedIndex(maxRow);
            focus(maxRow, (TableColumn) maxColumn);
            TableColumn<S, ?> startColumn = (TableColumn) minColumn;
            TableColumn<S, ?> endColumn = isCellSelectionEnabled ? (TableColumn) maxColumn : startColumn;
            int startChangeIndex = this.selectedCellsMap.indexOf(new TablePosition(this.tableView, minRow, startColumn));
            int endChangeIndex = this.selectedCellsMap.indexOf(new TablePosition(this.tableView, maxRow, endColumn));
            if (startChangeIndex > -1 && endChangeIndex > -1) {
                int startIndex = Math.min(startChangeIndex, endChangeIndex);
                int endIndex = Math.max(startChangeIndex, endChangeIndex);
                ListChangeListener.Change c2 = new NonIterableChange.SimpleAddChange(startIndex, endIndex + 1, this.selectedCellsSeq);
                handleSelectedCellsListChangeEvent(c2);
            }
        }

        @Override // javafx.scene.control.TableSelectionModel, javafx.scene.control.MultipleSelectionModelBase, javafx.scene.control.SelectionModel
        public void clearSelection(int index) {
            clearSelection(index, (TableColumn) null);
        }

        @Override // javafx.scene.control.TableView.TableViewSelectionModel
        public void clearSelection(int row, TableColumn<S, ?> column) {
            clearSelection(new TablePosition<>(getTableView(), row, column));
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void clearSelection(TablePosition<S, ?> tp) {
            boolean csMode = isCellSelectionEnabled();
            int row = tp.getRow();
            Iterator<TablePosition> it = getSelectedCells().iterator();
            while (true) {
                if (!it.hasNext()) {
                    break;
                }
                TablePosition pos = it.next();
                if (!csMode) {
                    if (pos.getRow() == row) {
                        this.selectedCellsMap.remove(pos);
                        break;
                    }
                } else if (pos.equals(tp)) {
                    this.selectedCellsMap.remove(tp);
                    break;
                }
            }
            if (isEmpty() && !isAtomic()) {
                updateSelectedIndex(-1);
                this.selectedCellsMap.clear();
            }
        }

        @Override // javafx.scene.control.TableSelectionModel, javafx.scene.control.MultipleSelectionModelBase, javafx.scene.control.SelectionModel
        public void clearSelection() {
            final List<TablePosition<S, ?>> removed = new ArrayList<>(getSelectedCells());
            quietClearSelection();
            if (!isAtomic()) {
                updateSelectedIndex(-1);
                focus(-1);
                if (!removed.isEmpty()) {
                    ListChangeListener.Change<TablePosition<S, ?>> c2 = new NonIterableChange<TablePosition<S, ?>>(0, 0, this.selectedCellsSeq) { // from class: javafx.scene.control.TableView.TableViewArrayListSelectionModel.5
                        @Override // javafx.collections.ListChangeListener.Change
                        public List<TablePosition<S, ?>> getRemoved() {
                            return removed;
                        }
                    };
                    handleSelectedCellsListChangeEvent(c2);
                }
            }
        }

        private void quietClearSelection() {
            startAtomic();
            this.selectedCellsMap.clear();
            stopAtomic();
        }

        @Override // javafx.scene.control.TableSelectionModel, javafx.scene.control.MultipleSelectionModelBase, javafx.scene.control.SelectionModel
        public boolean isSelected(int index) {
            return isSelected(index, (TableColumn) null);
        }

        @Override // javafx.scene.control.TableView.TableViewSelectionModel
        public boolean isSelected(int row, TableColumn<S, ?> column) {
            boolean isCellSelectionEnabled = isCellSelectionEnabled();
            if (isCellSelectionEnabled && column == null) {
                return false;
            }
            int columnIndex = (!isCellSelectionEnabled || column == null) ? -1 : this.tableView.getVisibleLeafIndex(column);
            return this.selectedCellsMap.isSelected(row, columnIndex);
        }

        @Override // javafx.scene.control.TableSelectionModel, javafx.scene.control.MultipleSelectionModelBase, javafx.scene.control.SelectionModel
        public boolean isEmpty() {
            return this.selectedCellsMap.isEmpty();
        }

        @Override // javafx.scene.control.TableSelectionModel, javafx.scene.control.MultipleSelectionModelBase, javafx.scene.control.SelectionModel
        public void selectPrevious() {
            if (isCellSelectionEnabled()) {
                TablePosition<S, ?> pos = getFocusedCell();
                if (pos.getColumn() - 1 >= 0) {
                    select(pos.getRow(), (TableColumn) getTableColumn(pos.getTableColumn(), -1));
                    return;
                } else {
                    if (pos.getRow() < getItemCount() - 1) {
                        select(pos.getRow() - 1, (TableColumn) getTableColumn(getTableView().getVisibleLeafColumns().size() - 1));
                        return;
                    }
                    return;
                }
            }
            int focusIndex = getFocusedIndex();
            if (focusIndex == -1) {
                select(getItemCount() - 1);
            } else if (focusIndex > 0) {
                select(focusIndex - 1);
            }
        }

        @Override // javafx.scene.control.TableSelectionModel, javafx.scene.control.MultipleSelectionModelBase, javafx.scene.control.SelectionModel
        public void selectNext() {
            if (isCellSelectionEnabled()) {
                TablePosition<S, ?> pos = getFocusedCell();
                if (pos.getColumn() + 1 < getTableView().getVisibleLeafColumns().size()) {
                    select(pos.getRow(), (TableColumn) getTableColumn(pos.getTableColumn(), 1));
                    return;
                } else {
                    if (pos.getRow() < getItemCount() - 1) {
                        select(pos.getRow() + 1, (TableColumn) getTableColumn(0));
                        return;
                    }
                    return;
                }
            }
            int focusIndex = getFocusedIndex();
            if (focusIndex == -1) {
                select(0);
            } else if (focusIndex < getItemCount() - 1) {
                select(focusIndex + 1);
            }
        }

        @Override // javafx.scene.control.TableSelectionModel
        public void selectAboveCell() {
            TablePosition<S, ?> pos = getFocusedCell();
            if (pos.getRow() == -1) {
                select(getItemCount() - 1);
            } else if (pos.getRow() > 0) {
                select(pos.getRow() - 1, (TableColumn) pos.getTableColumn());
            }
        }

        @Override // javafx.scene.control.TableSelectionModel
        public void selectBelowCell() {
            TablePosition<S, ?> pos = getFocusedCell();
            if (pos.getRow() == -1) {
                select(0);
            } else if (pos.getRow() < getItemCount() - 1) {
                select(pos.getRow() + 1, (TableColumn) pos.getTableColumn());
            }
        }

        @Override // javafx.scene.control.TableSelectionModel, javafx.scene.control.MultipleSelectionModelBase, javafx.scene.control.MultipleSelectionModel, javafx.scene.control.SelectionModel
        public void selectFirst() {
            TablePosition<S, ?> focusedCell = getFocusedCell();
            if (getSelectionMode() == SelectionMode.SINGLE) {
                quietClearSelection();
            }
            if (getItemCount() > 0) {
                if (isCellSelectionEnabled()) {
                    select(0, (TableColumn) focusedCell.getTableColumn());
                } else {
                    select(0);
                }
            }
        }

        @Override // javafx.scene.control.TableSelectionModel, javafx.scene.control.MultipleSelectionModelBase, javafx.scene.control.MultipleSelectionModel, javafx.scene.control.SelectionModel
        public void selectLast() {
            TablePosition<S, ?> focusedCell = getFocusedCell();
            if (getSelectionMode() == SelectionMode.SINGLE) {
                quietClearSelection();
            }
            int numItems = getItemCount();
            if (numItems > 0 && getSelectedIndex() < numItems - 1) {
                if (isCellSelectionEnabled()) {
                    select(numItems - 1, (TableColumn) focusedCell.getTableColumn());
                } else {
                    select(numItems - 1);
                }
            }
        }

        @Override // javafx.scene.control.TableSelectionModel
        public void selectLeftCell() {
            if (isCellSelectionEnabled()) {
                TablePosition<S, ?> pos = getFocusedCell();
                if (pos.getColumn() - 1 >= 0) {
                    select(pos.getRow(), (TableColumn) getTableColumn(pos.getTableColumn(), -1));
                }
            }
        }

        @Override // javafx.scene.control.TableSelectionModel
        public void selectRightCell() {
            if (isCellSelectionEnabled()) {
                TablePosition<S, ?> pos = getFocusedCell();
                if (pos.getColumn() + 1 < getTableView().getVisibleLeafColumns().size()) {
                    select(pos.getRow(), (TableColumn) getTableColumn(pos.getTableColumn(), 1));
                }
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void updateItemsObserver(ObservableList<S> oldList, ObservableList<S> newList) {
            if (oldList != null) {
                oldList.removeListener(this.weakItemsContentListener);
            }
            if (newList != null) {
                newList.addListener(this.weakItemsContentListener);
            }
            updateItemCount();
            updateDefaultSelection();
        }

        private void updateDefaultSelection() {
            int newSelectionIndex = -1;
            int newFocusIndex = -1;
            if (this.tableView.getItems() != null) {
                S selectedItem = getSelectedItem();
                if (selectedItem != null) {
                    newSelectionIndex = this.tableView.getItems().indexOf(selectedItem);
                }
                if (-1 == -1) {
                    newFocusIndex = this.tableView.getItems().size() > 0 ? 0 : -1;
                }
            }
            clearSelection();
            select(newSelectionIndex, (TableColumn) (isCellSelectionEnabled() ? getTableColumn(0) : null));
            focus(newFocusIndex, isCellSelectionEnabled() ? getTableColumn(0) : null);
        }

        private TableColumn<S, ?> getTableColumn(int pos) {
            return getTableView().getVisibleLeafColumn(pos);
        }

        private TableColumn<S, ?> getTableColumn(TableColumn<S, ?> column, int offset) {
            int columnIndex = getTableView().getVisibleLeafIndex(column);
            int newColumnIndex = columnIndex + offset;
            return getTableView().getVisibleLeafColumn(newColumnIndex);
        }

        private void updateSelectedIndex(int row) {
            setSelectedIndex(row);
            setSelectedItem(getModelItem(row));
        }

        @Override // javafx.scene.control.TableView.TableViewSelectionModel, javafx.scene.control.MultipleSelectionModelBase
        protected int getItemCount() {
            return this.itemCount;
        }

        private void updateItemCount() {
            if (this.tableView == null) {
                this.itemCount = -1;
            } else {
                List<S> items = getTableModel();
                this.itemCount = items == null ? -1 : items.size();
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void handleSelectedCellsListChangeEvent(ListChangeListener.Change<? extends TablePosition<S, ?>> c2) {
            boolean fireChangeEvent;
            int removedSize;
            int addedSize;
            List<Integer> newlySelectedRows = new ArrayList<>();
            List<Integer> newlyUnselectedRows = new ArrayList<>();
            while (c2.next()) {
                if (c2.wasRemoved()) {
                    List<? extends TablePosition<S, ?>> removed = c2.getRemoved();
                    for (int i2 = 0; i2 < removed.size(); i2++) {
                        TablePosition<S, ?> tp = removed.get(i2);
                        int row = tp.getRow();
                        if (this.selectedIndices.get(row)) {
                            this.selectedIndices.clear(row);
                            newlyUnselectedRows.add(Integer.valueOf(row));
                        }
                    }
                }
                if (c2.wasAdded()) {
                    List<? extends TablePosition<S, ?>> added = c2.getAddedSubList();
                    for (int i3 = 0; i3 < added.size(); i3++) {
                        TablePosition<S, ?> tp2 = added.get(i3);
                        int row2 = tp2.getRow();
                        if (!this.selectedIndices.get(row2)) {
                            this.selectedIndices.set(row2);
                            newlySelectedRows.add(Integer.valueOf(row2));
                        }
                    }
                }
            }
            c2.reset();
            this.selectedIndicesSeq.reset();
            if (isAtomic()) {
                return;
            }
            c2.next();
            if (!c2.wasReplaced() || (removedSize = c2.getRemovedSize()) != (addedSize = c2.getAddedSize())) {
                fireChangeEvent = true;
            } else {
                int i4 = 0;
                while (true) {
                    if (i4 < removedSize) {
                        S removedItem = c2.getRemoved().get(i4).getItem();
                        boolean matchFound = false;
                        int j2 = 0;
                        while (true) {
                            if (j2 >= addedSize) {
                                break;
                            }
                            S addedItem = c2.getAddedSubList().get(j2).getItem();
                            if (!removedItem.equals(addedItem)) {
                                j2++;
                            } else {
                                matchFound = true;
                                break;
                            }
                        }
                        if (matchFound) {
                            i4++;
                        } else {
                            fireChangeEvent = true;
                            break;
                        }
                    } else {
                        fireChangeEvent = false;
                        break;
                    }
                }
            }
            if (fireChangeEvent) {
                if (this.selectedItemChange != null) {
                    this.selectedItems.callObservers(this.selectedItemChange);
                } else {
                    this.selectedItems.callObservers(new MappingChange(c2, this.cellToItemsMap, this.selectedItems));
                }
            }
            c2.reset();
            if (this.selectedItems.isEmpty() && getSelectedItem() != null) {
                setSelectedItem(null);
            }
            ReadOnlyUnbackedObservableList<Integer> selectedIndicesSeq = (ReadOnlyUnbackedObservableList) getSelectedIndices();
            if (!newlySelectedRows.isEmpty() && newlyUnselectedRows.isEmpty()) {
                ListChangeListener.Change<Integer> change = createRangeChange(selectedIndicesSeq, newlySelectedRows, false);
                selectedIndicesSeq.callObservers(change);
            } else {
                selectedIndicesSeq.callObservers(new MappingChange<>(c2, this.cellToIndicesMap, selectedIndicesSeq));
                c2.reset();
            }
            this.selectedCellsSeq.callObservers(new MappingChange(c2, MappingChange.NOOP_MAP, this.selectedCellsSeq));
            c2.reset();
        }
    }

    /* loaded from: jfxrt.jar:javafx/scene/control/TableView$TableViewFocusModel.class */
    public static class TableViewFocusModel<S> extends TableFocusModel<S, TableColumn<S, ?>> {
        private final TableView<S> tableView;
        private final TablePosition<S, ?> EMPTY_CELL;
        private final ListChangeListener<S> itemsContentListener = c2 -> {
            int removedSize;
            c2.next();
            TablePosition<S, ?> focusedCell = getFocusedCell();
            int focusedIndex = focusedCell.getRow();
            if (focusedIndex == -1 || c2.getFrom() > focusedIndex) {
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
                if (addedSize < c2.getList().size()) {
                    focus(Math.min(getItemCount() - 1, getFocusedIndex() + addedSize), (TableColumn) focusedCell.getTableColumn());
                }
            } else if (!added && removed) {
                int newFocusIndex = Math.max(0, getFocusedIndex() - removedSize);
                if (newFocusIndex < 0) {
                    focus(0, (TableColumn) focusedCell.getTableColumn());
                } else {
                    focus(newFocusIndex, (TableColumn) focusedCell.getTableColumn());
                }
            }
        };
        private WeakListChangeListener<S> weakItemsContentListener = new WeakListChangeListener<>(this.itemsContentListener);
        private ReadOnlyObjectWrapper<TablePosition> focusedCell;

        public TableViewFocusModel(final TableView<S> tableView) {
            if (tableView == null) {
                throw new NullPointerException("TableView can not be null");
            }
            this.tableView = tableView;
            this.EMPTY_CELL = new TablePosition<>(tableView, -1, null);
            if (tableView.getItems() != null) {
                this.tableView.getItems().addListener(this.weakItemsContentListener);
            }
            this.tableView.itemsProperty().addListener(new InvalidationListener() { // from class: javafx.scene.control.TableView.TableViewFocusModel.1
                private WeakReference<ObservableList<S>> weakItemsRef;

                {
                    this.weakItemsRef = new WeakReference<>(tableView.getItems());
                }

                @Override // javafx.beans.InvalidationListener
                public void invalidated(Observable observable) {
                    ObservableList<S> oldItems = this.weakItemsRef.get();
                    this.weakItemsRef = new WeakReference<>(tableView.getItems());
                    TableViewFocusModel.this.updateItemsObserver(oldItems, tableView.getItems());
                }
            });
            updateDefaultFocus();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void updateItemsObserver(ObservableList<S> oldList, ObservableList<S> newList) {
            if (oldList != null) {
                oldList.removeListener(this.weakItemsContentListener);
            }
            if (newList != null) {
                newList.addListener(this.weakItemsContentListener);
            }
            updateDefaultFocus();
        }

        @Override // javafx.scene.control.FocusModel
        protected int getItemCount() {
            if (this.tableView.getItems() == null) {
                return -1;
            }
            return this.tableView.getItems().size();
        }

        @Override // javafx.scene.control.FocusModel
        protected S getModelItem(int index) {
            if (this.tableView.getItems() != null && index >= 0 && index < getItemCount()) {
                return this.tableView.getItems().get(index);
            }
            return null;
        }

        public final ReadOnlyObjectProperty<TablePosition> focusedCellProperty() {
            return focusedCellPropertyImpl().getReadOnlyProperty();
        }

        private void setFocusedCell(TablePosition value) {
            focusedCellPropertyImpl().set(value);
        }

        public final TablePosition getFocusedCell() {
            return this.focusedCell == null ? this.EMPTY_CELL : this.focusedCell.get();
        }

        private ReadOnlyObjectWrapper<TablePosition> focusedCellPropertyImpl() {
            if (this.focusedCell == null) {
                this.focusedCell = new ReadOnlyObjectWrapper<TablePosition>(this.EMPTY_CELL) { // from class: javafx.scene.control.TableView.TableViewFocusModel.2
                    private TablePosition old;

                    @Override // javafx.beans.property.ObjectPropertyBase
                    protected void invalidated() {
                        if (get() == null) {
                            return;
                        }
                        if (this.old == null || !this.old.equals(get())) {
                            TableViewFocusModel.this.setFocusedIndex(get().getRow());
                            TableViewFocusModel.this.setFocusedItem(TableViewFocusModel.this.getModelItem(getValue2().getRow()));
                            this.old = get();
                        }
                    }

                    @Override // javafx.beans.property.SimpleObjectProperty, javafx.beans.property.ReadOnlyProperty
                    public Object getBean() {
                        return TableViewFocusModel.this;
                    }

                    @Override // javafx.beans.property.SimpleObjectProperty, javafx.beans.property.ReadOnlyProperty
                    public String getName() {
                        return "focusedCell";
                    }
                };
            }
            return this.focusedCell;
        }

        @Override // javafx.scene.control.TableFocusModel
        public void focus(int row, TableColumn<S, ?> column) {
            if (row < 0 || row >= getItemCount()) {
                setFocusedCell(this.EMPTY_CELL);
                return;
            }
            TablePosition<S, ?> oldFocusCell = getFocusedCell();
            TablePosition<S, ?> newFocusCell = new TablePosition<>(this.tableView, row, column);
            setFocusedCell(newFocusCell);
            if (newFocusCell.equals(oldFocusCell)) {
                setFocusedIndex(row);
                setFocusedItem(getModelItem(row));
            }
        }

        public void focus(TablePosition pos) {
            if (pos == null) {
                return;
            }
            focus(pos.getRow(), (TableColumn) pos.getTableColumn());
        }

        @Override // javafx.scene.control.TableFocusModel
        public boolean isFocused(int row, TableColumn<S, ?> column) {
            if (row < 0 || row >= getItemCount()) {
                return false;
            }
            TablePosition cell = getFocusedCell();
            boolean columnMatch = column == null || column.equals(cell.getTableColumn());
            return cell.getRow() == row && columnMatch;
        }

        @Override // javafx.scene.control.FocusModel
        public void focus(int index) {
            if (index < 0 || index >= getItemCount()) {
                setFocusedCell(this.EMPTY_CELL);
            } else {
                setFocusedCell(new TablePosition(this.tableView, index, null));
            }
        }

        @Override // javafx.scene.control.TableFocusModel
        public void focusAboveCell() {
            TablePosition cell = getFocusedCell();
            if (getFocusedIndex() == -1) {
                focus(getItemCount() - 1, (TableColumn) cell.getTableColumn());
            } else if (getFocusedIndex() > 0) {
                focus(getFocusedIndex() - 1, (TableColumn) cell.getTableColumn());
            }
        }

        @Override // javafx.scene.control.TableFocusModel
        public void focusBelowCell() {
            TablePosition cell = getFocusedCell();
            if (getFocusedIndex() == -1) {
                focus(0, (TableColumn) cell.getTableColumn());
            } else if (getFocusedIndex() != getItemCount() - 1) {
                focus(getFocusedIndex() + 1, (TableColumn) cell.getTableColumn());
            }
        }

        @Override // javafx.scene.control.TableFocusModel
        public void focusLeftCell() {
            TablePosition cell = getFocusedCell();
            if (cell.getColumn() <= 0) {
                return;
            }
            focus(cell.getRow(), (TableColumn) getTableColumn(cell.getTableColumn(), -1));
        }

        @Override // javafx.scene.control.TableFocusModel
        public void focusRightCell() {
            TablePosition cell = getFocusedCell();
            if (cell.getColumn() == getColumnCount() - 1) {
                return;
            }
            focus(cell.getRow(), (TableColumn) getTableColumn(cell.getTableColumn(), 1));
        }

        @Override // javafx.scene.control.FocusModel
        public void focusPrevious() {
            if (getFocusedIndex() == -1) {
                focus(0);
            } else if (getFocusedIndex() > 0) {
                focusAboveCell();
            }
        }

        @Override // javafx.scene.control.FocusModel
        public void focusNext() {
            if (getFocusedIndex() == -1) {
                focus(0);
            } else if (getFocusedIndex() != getItemCount() - 1) {
                focusBelowCell();
            }
        }

        private void updateDefaultFocus() {
            int newValueIndex = -1;
            if (this.tableView.getItems() != null) {
                S focusedItem = getFocusedItem();
                if (focusedItem != null) {
                    newValueIndex = this.tableView.getItems().indexOf(focusedItem);
                }
                if (newValueIndex == -1) {
                    newValueIndex = this.tableView.getItems().size() > 0 ? 0 : -1;
                }
            }
            TablePosition<S, ?> focusedCell = getFocusedCell();
            TableColumn<S, ?> focusColumn = (focusedCell == null || this.EMPTY_CELL.equals(focusedCell)) ? this.tableView.getVisibleLeafColumn(0) : focusedCell.getTableColumn();
            focus(newValueIndex, (TableColumn) focusColumn);
        }

        private int getColumnCount() {
            return this.tableView.getVisibleLeafColumns().size();
        }

        private TableColumn<S, ?> getTableColumn(TableColumn<S, ?> column, int offset) {
            int columnIndex = this.tableView.getVisibleLeafIndex(column);
            int newColumnIndex = columnIndex + offset;
            return this.tableView.getVisibleLeafColumn(newColumnIndex);
        }
    }
}
