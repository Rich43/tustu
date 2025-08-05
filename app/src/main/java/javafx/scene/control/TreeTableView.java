package javafx.scene.control;

import com.sun.javafx.collections.MappingChange;
import com.sun.javafx.collections.NonIterableChange;
import com.sun.javafx.css.converters.SizeConverter;
import com.sun.javafx.scene.control.ReadOnlyUnbackedObservableList;
import com.sun.javafx.scene.control.SelectedCellsMap;
import com.sun.javafx.scene.control.TableColumnComparatorBase;
import com.sun.javafx.scene.control.behavior.TableCellBehaviorBase;
import com.sun.javafx.scene.control.behavior.TreeTableCellBehavior;
import com.sun.javafx.scene.control.skin.TableViewSkinBase;
import com.sun.javafx.scene.control.skin.TreeTableViewSkin;
import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;
import javafx.application.Platform;
import javafx.beans.DefaultProperty;
import javafx.beans.InvalidationListener;
import javafx.beans.WeakInvalidationListener;
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
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.WeakListChangeListener;
import javafx.css.CssMetaData;
import javafx.css.PseudoClass;
import javafx.css.Styleable;
import javafx.css.StyleableDoubleProperty;
import javafx.css.StyleableProperty;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.event.WeakEventHandler;
import javafx.scene.AccessibleAttribute;
import javafx.scene.AccessibleRole;
import javafx.scene.Node;
import javafx.scene.control.MultipleSelectionModelBase;
import javafx.scene.control.TableUtil;
import javafx.scene.control.TreeItem;
import javafx.util.Callback;
import javax.swing.JTree;
import org.icepdf.core.util.PdfOps;

@DefaultProperty("root")
/* loaded from: jfxrt.jar:javafx/scene/control/TreeTableView.class */
public class TreeTableView<S> extends Control {
    private boolean expandedItemCountDirty;
    private Map<Integer, SoftReference<TreeItem<S>>> treeItemCacheMap;
    private final ObservableList<TreeTableColumn<S, ?>> columns;
    private final ObservableList<TreeTableColumn<S, ?>> visibleLeafColumns;
    private final ObservableList<TreeTableColumn<S, ?>> unmodifiableVisibleLeafColumns;
    private ObservableList<TreeTableColumn<S, ?>> sortOrder;
    double contentWidth;
    private boolean isInited;
    private final EventHandler<TreeItem.TreeModificationEvent<S>> rootEvent;
    private final ListChangeListener<TreeTableColumn<S, ?>> columnsObserver;
    private final WeakHashMap<TreeTableColumn<S, ?>, Integer> lastKnownColumnIndex;
    private final InvalidationListener columnVisibleObserver;
    private final InvalidationListener columnSortableObserver;
    private final InvalidationListener columnSortTypeObserver;
    private final InvalidationListener columnComparatorObserver;
    private final InvalidationListener cellSelectionModelInvalidationListener;
    private WeakEventHandler<TreeItem.TreeModificationEvent<S>> weakRootEventListener;
    private final WeakInvalidationListener weakColumnVisibleObserver;
    private final WeakInvalidationListener weakColumnSortableObserver;
    private final WeakInvalidationListener weakColumnSortTypeObserver;
    private final WeakInvalidationListener weakColumnComparatorObserver;
    private final WeakListChangeListener<TreeTableColumn<S, ?>> weakColumnsObserver;
    private final WeakInvalidationListener weakCellSelectionModelInvalidationListener;
    private ObjectProperty<TreeItem<S>> root;
    private BooleanProperty showRoot;
    private ObjectProperty<TreeTableColumn<S, ?>> treeColumn;
    private ObjectProperty<TreeTableViewSelectionModel<S>> selectionModel;
    private ObjectProperty<TreeTableViewFocusModel<S>> focusModel;
    private ReadOnlyIntegerWrapper expandedItemCount;
    private BooleanProperty editable;
    private ReadOnlyObjectWrapper<TreeTablePosition<S, ?>> editingCell;
    private BooleanProperty tableMenuButtonVisible;
    private ObjectProperty<Callback<ResizeFeatures, Boolean>> columnResizePolicy;
    private ObjectProperty<Callback<TreeTableView<S>, TreeTableRow<S>>> rowFactory;
    private ObjectProperty<Node> placeholder;
    private DoubleProperty fixedCellSize;
    private ObjectProperty<TreeSortMode> sortMode;
    private ReadOnlyObjectWrapper<Comparator<TreeItem<S>>> comparator;
    private ObjectProperty<Callback<TreeTableView<S>, Boolean>> sortPolicy;
    private ObjectProperty<EventHandler<SortEvent<TreeTableView<S>>>> onSort;
    private ObjectProperty<EventHandler<ScrollToEvent<Integer>>> onScrollTo;
    private ObjectProperty<EventHandler<ScrollToEvent<TreeTableColumn<S, ?>>>> onScrollToColumn;
    private boolean sortingInProgress;
    private boolean sortLock;
    private TableUtil.SortEventType lastSortEventType;
    private Object[] lastSortEventSupportInfo;
    private static final String DEFAULT_STYLE_CLASS = "tree-table-view";
    private static final EventType<?> EDIT_ANY_EVENT = new EventType<>(Event.ANY, "TREE_TABLE_VIEW_EDIT");
    private static final EventType<?> EDIT_START_EVENT = new EventType<>(editAnyEvent(), "EDIT_START");
    private static final EventType<?> EDIT_CANCEL_EVENT = new EventType<>(editAnyEvent(), "EDIT_CANCEL");
    private static final EventType<?> EDIT_COMMIT_EVENT = new EventType<>(editAnyEvent(), "EDIT_COMMIT");
    public static final Callback<ResizeFeatures, Boolean> UNCONSTRAINED_RESIZE_POLICY = new Callback<ResizeFeatures, Boolean>() { // from class: javafx.scene.control.TreeTableView.1
        public String toString() {
            return "unconstrained-resize";
        }

        @Override // javafx.util.Callback
        public Boolean call(ResizeFeatures prop) {
            double result = TableUtil.resize(prop.getColumn(), prop.getDelta().doubleValue());
            return Boolean.valueOf(Double.compare(result, 0.0d) == 0);
        }
    };
    public static final Callback<ResizeFeatures, Boolean> CONSTRAINED_RESIZE_POLICY = new Callback<ResizeFeatures, Boolean>() { // from class: javafx.scene.control.TreeTableView.2
        private boolean isFirstRun = true;

        public String toString() {
            return "constrained-resize";
        }

        @Override // javafx.util.Callback
        public Boolean call(ResizeFeatures prop) {
            TreeTableView<S> table = prop.getTable();
            List<? extends TableColumnBase<?, ?>> visibleLeafColumns = table.getVisibleLeafColumns();
            Boolean result = Boolean.valueOf(TableUtil.constrainedResize(prop, this.isFirstRun, table.contentWidth, visibleLeafColumns));
            boolean z2 = this.isFirstRun && !result.booleanValue();
            this.isFirstRun = z2;
            return result;
        }
    };
    public static final Callback<TreeTableView, Boolean> DEFAULT_SORT_POLICY = new Callback<TreeTableView, Boolean>() { // from class: javafx.scene.control.TreeTableView.3
        @Override // javafx.util.Callback
        public Boolean call(TreeTableView table) {
            try {
                TreeItem rootItem = table.getRoot();
                if (rootItem == null || rootItem.getChildren().isEmpty()) {
                    return false;
                }
                TreeSortMode sortMode = table.getSortMode();
                if (sortMode == null) {
                    return false;
                }
                rootItem.lastSortMode = sortMode;
                rootItem.lastComparator = table.getComparator();
                rootItem.sort();
                return true;
            } catch (UnsupportedOperationException e2) {
                return false;
            }
        }
    };
    private static final PseudoClass PSEUDO_CLASS_CELL_SELECTION = PseudoClass.getPseudoClass("cell-selection");
    private static final PseudoClass PSEUDO_CLASS_ROW_SELECTION = PseudoClass.getPseudoClass("row-selection");

    public TreeTableView() {
        this(null);
    }

    public TreeTableView(TreeItem<S> root) {
        this.expandedItemCountDirty = true;
        this.treeItemCacheMap = new HashMap();
        this.columns = FXCollections.observableArrayList();
        this.visibleLeafColumns = FXCollections.observableArrayList();
        this.unmodifiableVisibleLeafColumns = FXCollections.unmodifiableObservableList(this.visibleLeafColumns);
        this.sortOrder = FXCollections.observableArrayList();
        this.isInited = false;
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
        this.columnsObserver = new ListChangeListener<TreeTableColumn<S, ?>>() { // from class: javafx.scene.control.TreeTableView.4
            /* JADX WARN: Multi-variable type inference failed */
            @Override // javafx.collections.ListChangeListener
            public void onChanged(ListChangeListener.Change<? extends TreeTableColumn<S, ?>> c2) {
                int matchingColumnIndex;
                List<TreeTableColumn<S, ?>> columns = TreeTableView.this.getColumns();
                while (c2.next()) {
                    if (c2.wasAdded()) {
                        List<TreeTableColumn<S, ?>> duplicates = new ArrayList<>();
                        for (TreeTableColumn<S, ?> addedColumn : c2.getAddedSubList()) {
                            if (addedColumn != null) {
                                int count = 0;
                                for (TreeTableColumn<S, ?> column : columns) {
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
                            for (TreeTableColumn<S, ?> dupe : duplicates) {
                                titleList = titleList + PdfOps.SINGLE_QUOTE_TOKEN + dupe.getText() + "', ";
                            }
                            throw new IllegalStateException("Duplicate TreeTableColumns detected in TreeTableView columns list with titles " + titleList);
                        }
                    }
                }
                c2.reset();
                TreeTableView.this.updateVisibleLeafColumns();
                List<TreeTableColumn<S, ?>> toRemove = new ArrayList<>();
                while (c2.next()) {
                    List<? extends TreeTableColumn<S, ?>> removed = c2.getRemoved();
                    List<? extends TreeTableColumn<S, ?>> added = c2.getAddedSubList();
                    if (c2.wasRemoved()) {
                        toRemove.addAll(removed);
                        Iterator<? extends TreeTableColumn<S, ?>> it = removed.iterator();
                        while (it.hasNext()) {
                            it.next().setTreeTableView(null);
                        }
                    }
                    if (c2.wasAdded()) {
                        toRemove.removeAll(added);
                        Iterator<? extends TreeTableColumn<S, ?>> it2 = added.iterator();
                        while (it2.hasNext()) {
                            it2.next().setTreeTableView(TreeTableView.this);
                        }
                    }
                    TableUtil.removeColumnsListener(removed, TreeTableView.this.weakColumnsObserver);
                    TableUtil.addColumnsListener(added, TreeTableView.this.weakColumnsObserver);
                    TableUtil.removeTableColumnListener(c2.getRemoved(), TreeTableView.this.weakColumnVisibleObserver, TreeTableView.this.weakColumnSortableObserver, TreeTableView.this.weakColumnSortTypeObserver, TreeTableView.this.weakColumnComparatorObserver);
                    TableUtil.addTableColumnListener(c2.getAddedSubList(), TreeTableView.this.weakColumnVisibleObserver, TreeTableView.this.weakColumnSortableObserver, TreeTableView.this.weakColumnSortTypeObserver, TreeTableView.this.weakColumnComparatorObserver);
                }
                TreeTableView.this.sortOrder.removeAll(toRemove);
                TreeTableViewFocusModel<S> fm = TreeTableView.this.getFocusModel();
                TreeTableViewSelectionModel<S> sm = TreeTableView.this.getSelectionModel();
                c2.reset();
                List<TreeTableColumn<S, ?>> removed2 = new ArrayList<>();
                List<TreeTableColumn<S, ?>> added2 = new ArrayList<>();
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
                    TreeTablePosition<S, ?> focusedCell = fm.getFocusedCell();
                    boolean match = false;
                    Iterator<TreeTableColumn<S, ?>> it3 = removed2.iterator();
                    while (it3.hasNext()) {
                        match = focusedCell != null && focusedCell.getTableColumn() == it3.next();
                        if (match) {
                            break;
                        }
                    }
                    if (match) {
                        int matchingColumnIndex2 = ((Integer) TreeTableView.this.lastKnownColumnIndex.getOrDefault(focusedCell.getTableColumn(), 0)).intValue();
                        int newFocusColumnIndex = matchingColumnIndex2 == 0 ? 0 : Math.min(TreeTableView.this.getVisibleLeafColumns().size() - 1, matchingColumnIndex2 - 1);
                        fm.focus(focusedCell.getRow(), (TreeTableColumn) TreeTableView.this.getVisibleLeafColumn(newFocusColumnIndex));
                    }
                }
                if (sm != null) {
                    List<TreeTablePosition> selectedCells = new ArrayList<>(sm.getSelectedCells());
                    Iterator<TreeTablePosition> it4 = selectedCells.iterator();
                    while (it4.hasNext()) {
                        TreeTablePosition selectedCell = it4.next();
                        boolean match2 = false;
                        Iterator<TreeTableColumn<S, ?>> it5 = removed2.iterator();
                        while (it5.hasNext()) {
                            match2 = selectedCell != null && selectedCell.getTableColumn() == it5.next();
                            if (match2) {
                                break;
                            }
                        }
                        if (match2 && (matchingColumnIndex = ((Integer) TreeTableView.this.lastKnownColumnIndex.getOrDefault(selectedCell.getTableColumn(), -1)).intValue()) != -1) {
                            if (sm instanceof TreeTableViewArrayListSelectionModel) {
                                TreeTablePosition<S, ?> fixedTablePosition = new TreeTablePosition<>(TreeTableView.this, selectedCell.getRow(), selectedCell.getTableColumn());
                                fixedTablePosition.fixedColumnIndex = matchingColumnIndex;
                                ((TreeTableViewArrayListSelectionModel) sm).clearSelection(fixedTablePosition);
                            } else {
                                sm.clearSelection(selectedCell.getRow(), selectedCell.getTableColumn());
                            }
                        }
                    }
                }
                TreeTableView.this.lastKnownColumnIndex.clear();
                for (TreeTableColumn<S, ?> tc : TreeTableView.this.getColumns()) {
                    int index = TreeTableView.this.getVisibleLeafIndex(tc);
                    if (index > -1) {
                        TreeTableView.this.lastKnownColumnIndex.put(tc, Integer.valueOf(index));
                    }
                }
            }
        };
        this.lastKnownColumnIndex = new WeakHashMap<>();
        this.columnVisibleObserver = valueModel -> {
            updateVisibleLeafColumns();
        };
        this.columnSortableObserver = valueModel2 -> {
            TreeTableColumn col = (TreeTableColumn) ((BooleanProperty) valueModel2).getBean();
            if (getSortOrder().contains(col)) {
                doSort(TableUtil.SortEventType.COLUMN_SORTABLE_CHANGE, col);
            }
        };
        this.columnSortTypeObserver = valueModel3 -> {
            TreeTableColumn col = (TreeTableColumn) ((ObjectProperty) valueModel3).getBean();
            if (getSortOrder().contains(col)) {
                doSort(TableUtil.SortEventType.COLUMN_SORT_TYPE_CHANGE, col);
            }
        };
        this.columnComparatorObserver = valueModel4 -> {
            TreeTableColumn col = (TreeTableColumn) ((SimpleObjectProperty) valueModel4).getBean();
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
        this.root = new SimpleObjectProperty<TreeItem<S>>(this, "root") { // from class: javafx.scene.control.TreeTableView.5
            private WeakReference<TreeItem<S>> weakOldItem;

            @Override // javafx.beans.property.ObjectPropertyBase
            protected void invalidated() {
                TreeItem<S> oldTreeItem = this.weakOldItem == null ? null : this.weakOldItem.get();
                if (oldTreeItem != null && TreeTableView.this.weakRootEventListener != null) {
                    oldTreeItem.removeEventHandler(TreeItem.treeNotificationEvent(), TreeTableView.this.weakRootEventListener);
                }
                TreeItem<S> root2 = TreeTableView.this.getRoot();
                if (root2 != null) {
                    TreeTableView.this.weakRootEventListener = new WeakEventHandler(TreeTableView.this.rootEvent);
                    TreeTableView.this.getRoot().addEventHandler(TreeItem.treeNotificationEvent(), TreeTableView.this.weakRootEventListener);
                    this.weakOldItem = new WeakReference<>(root2);
                }
                TreeTableView.this.getSortOrder().clear();
                TreeTableView.this.expandedItemCountDirty = true;
                TreeTableView.this.updateRootExpanded();
            }
        };
        this.expandedItemCount = new ReadOnlyIntegerWrapper(this, "expandedItemCount", 0);
        this.sortLock = false;
        this.lastSortEventType = null;
        this.lastSortEventSupportInfo = null;
        getStyleClass().setAll(DEFAULT_STYLE_CLASS);
        setAccessibleRole(AccessibleRole.TREE_TABLE_VIEW);
        setRoot(root);
        updateExpandedItemCount(root);
        setSelectionModel(new TreeTableViewArrayListSelectionModel(this));
        setFocusModel(new TreeTableViewFocusModel<>(this));
        getColumns().addListener(this.weakColumnsObserver);
        getSortOrder().addListener(c2 -> {
            doSort(TableUtil.SortEventType.SORT_ORDER_CHANGE, c2);
        });
        getProperties().addListener(c3 -> {
            if (c3.wasAdded() && "TableView.contentWidth".equals(c3.getKey())) {
                if (c3.getValueAdded() instanceof Number) {
                    setContentWidth(((Double) c3.getValueAdded()).doubleValue());
                }
                getProperties().remove("TableView.contentWidth");
            }
        });
        this.isInited = true;
    }

    public static <S> EventType<EditEvent<S>> editAnyEvent() {
        return (EventType<EditEvent<S>>) EDIT_ANY_EVENT;
    }

    public static <S> EventType<EditEvent<S>> editStartEvent() {
        return (EventType<EditEvent<S>>) EDIT_START_EVENT;
    }

    public static <S> EventType<EditEvent<S>> editCancelEvent() {
        return (EventType<EditEvent<S>>) EDIT_CANCEL_EVENT;
    }

    public static <S> EventType<EditEvent<S>> editCommitEvent() {
        return (EventType<EditEvent<S>>) EDIT_COMMIT_EVENT;
    }

    @Deprecated
    public static int getNodeLevel(TreeItem<?> node) {
        return TreeView.getNodeLevel(node);
    }

    public final void setRoot(TreeItem<S> value) {
        rootProperty().set(value);
    }

    public final TreeItem<S> getRoot() {
        if (this.root == null) {
            return null;
        }
        return this.root.get();
    }

    public final ObjectProperty<TreeItem<S>> rootProperty() {
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
            this.showRoot = new SimpleBooleanProperty(this, "showRoot", true) { // from class: javafx.scene.control.TreeTableView.6
                @Override // javafx.beans.property.BooleanPropertyBase
                protected void invalidated() {
                    TreeTableView.this.updateRootExpanded();
                    TreeTableView.this.updateExpandedItemCount(TreeTableView.this.getRoot());
                }
            };
        }
        return this.showRoot;
    }

    public final ObjectProperty<TreeTableColumn<S, ?>> treeColumnProperty() {
        if (this.treeColumn == null) {
            this.treeColumn = new SimpleObjectProperty(this, "treeColumn", null);
        }
        return this.treeColumn;
    }

    public final void setTreeColumn(TreeTableColumn<S, ?> value) {
        treeColumnProperty().set(value);
    }

    public final TreeTableColumn<S, ?> getTreeColumn() {
        if (this.treeColumn == null) {
            return null;
        }
        return this.treeColumn.get();
    }

    public final void setSelectionModel(TreeTableViewSelectionModel<S> value) {
        selectionModelProperty().set(value);
    }

    public final TreeTableViewSelectionModel<S> getSelectionModel() {
        if (this.selectionModel == null) {
            return null;
        }
        return this.selectionModel.get();
    }

    public final ObjectProperty<TreeTableViewSelectionModel<S>> selectionModelProperty() {
        if (this.selectionModel == null) {
            this.selectionModel = new SimpleObjectProperty<TreeTableViewSelectionModel<S>>(this, "selectionModel") { // from class: javafx.scene.control.TreeTableView.7
                TreeTableViewSelectionModel<S> oldValue = null;

                @Override // javafx.beans.property.ObjectPropertyBase
                protected void invalidated() {
                    if (this.oldValue != null) {
                        this.oldValue.cellSelectionEnabledProperty().removeListener(TreeTableView.this.weakCellSelectionModelInvalidationListener);
                    }
                    this.oldValue = get();
                    if (this.oldValue != null) {
                        this.oldValue.cellSelectionEnabledProperty().addListener(TreeTableView.this.weakCellSelectionModelInvalidationListener);
                        TreeTableView.this.weakCellSelectionModelInvalidationListener.invalidated(this.oldValue.cellSelectionEnabledProperty());
                    }
                }
            };
        }
        return this.selectionModel;
    }

    public final void setFocusModel(TreeTableViewFocusModel<S> value) {
        focusModelProperty().set(value);
    }

    public final TreeTableViewFocusModel<S> getFocusModel() {
        if (this.focusModel == null) {
            return null;
        }
        return this.focusModel.get();
    }

    public final ObjectProperty<TreeTableViewFocusModel<S>> focusModelProperty() {
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

    private void setEditingCell(TreeTablePosition<S, ?> value) {
        editingCellPropertyImpl().set(value);
    }

    public final TreeTablePosition<S, ?> getEditingCell() {
        if (this.editingCell == null) {
            return null;
        }
        return this.editingCell.get();
    }

    public final ReadOnlyObjectProperty<TreeTablePosition<S, ?>> editingCellProperty() {
        return editingCellPropertyImpl().getReadOnlyProperty();
    }

    private ReadOnlyObjectWrapper<TreeTablePosition<S, ?>> editingCellPropertyImpl() {
        if (this.editingCell == null) {
            this.editingCell = new ReadOnlyObjectWrapper<>(this, "editingCell");
        }
        return this.editingCell;
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
            this.columnResizePolicy = new SimpleObjectProperty<Callback<ResizeFeatures, Boolean>>(this, "columnResizePolicy", UNCONSTRAINED_RESIZE_POLICY) { // from class: javafx.scene.control.TreeTableView.8
                private Callback<ResizeFeatures, Boolean> oldPolicy;

                @Override // javafx.beans.property.ObjectPropertyBase
                protected void invalidated() {
                    if (TreeTableView.this.isInited) {
                        get().call(new ResizeFeatures(TreeTableView.this, null, Double.valueOf(0.0d)));
                        if (this.oldPolicy != null) {
                            PseudoClass state = PseudoClass.getPseudoClass(this.oldPolicy.toString());
                            TreeTableView.this.pseudoClassStateChanged(state, false);
                        }
                        if (get() != null) {
                            PseudoClass state2 = PseudoClass.getPseudoClass(get().toString());
                            TreeTableView.this.pseudoClassStateChanged(state2, true);
                        }
                        this.oldPolicy = get();
                    }
                }
            };
        }
        return this.columnResizePolicy;
    }

    public final ObjectProperty<Callback<TreeTableView<S>, TreeTableRow<S>>> rowFactoryProperty() {
        if (this.rowFactory == null) {
            this.rowFactory = new SimpleObjectProperty(this, "rowFactory");
        }
        return this.rowFactory;
    }

    public final void setRowFactory(Callback<TreeTableView<S>, TreeTableRow<S>> value) {
        rowFactoryProperty().set(value);
    }

    public final Callback<TreeTableView<S>, TreeTableRow<S>> getRowFactory() {
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
            this.fixedCellSize = new StyleableDoubleProperty(-1.0d) { // from class: javafx.scene.control.TreeTableView.9
                @Override // javafx.css.StyleableProperty
                public CssMetaData<? extends Styleable, Number> getCssMetaData() {
                    return StyleableProperties.FIXED_CELL_SIZE;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return TreeTableView.this;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return "fixedCellSize";
                }
            };
        }
        return this.fixedCellSize;
    }

    public final ObjectProperty<TreeSortMode> sortModeProperty() {
        if (this.sortMode == null) {
            this.sortMode = new SimpleObjectProperty(this, "sortMode", TreeSortMode.ALL_DESCENDANTS);
        }
        return this.sortMode;
    }

    public final void setSortMode(TreeSortMode value) {
        sortModeProperty().set(value);
    }

    public final TreeSortMode getSortMode() {
        return this.sortMode == null ? TreeSortMode.ALL_DESCENDANTS : this.sortMode.get();
    }

    private void setComparator(Comparator<TreeItem<S>> value) {
        comparatorPropertyImpl().set(value);
    }

    public final Comparator<TreeItem<S>> getComparator() {
        if (this.comparator == null) {
            return null;
        }
        return this.comparator.get();
    }

    public final ReadOnlyObjectProperty<Comparator<TreeItem<S>>> comparatorProperty() {
        return comparatorPropertyImpl().getReadOnlyProperty();
    }

    private ReadOnlyObjectWrapper<Comparator<TreeItem<S>>> comparatorPropertyImpl() {
        if (this.comparator == null) {
            this.comparator = new ReadOnlyObjectWrapper<>(this, "comparator");
        }
        return this.comparator;
    }

    public final void setSortPolicy(Callback<TreeTableView<S>, Boolean> callback) {
        sortPolicyProperty().set(callback);
    }

    public final Callback<TreeTableView<S>, Boolean> getSortPolicy() {
        return this.sortPolicy == null ? DEFAULT_SORT_POLICY : this.sortPolicy.get();
    }

    public final ObjectProperty<Callback<TreeTableView<S>, Boolean>> sortPolicyProperty() {
        if (this.sortPolicy == null) {
            this.sortPolicy = new SimpleObjectProperty<Callback<TreeTableView<S>, Boolean>>(this, "sortPolicy", DEFAULT_SORT_POLICY) { // from class: javafx.scene.control.TreeTableView.10
                @Override // javafx.beans.property.ObjectPropertyBase
                protected void invalidated() {
                    TreeTableView.this.sort();
                }
            };
        }
        return this.sortPolicy;
    }

    public void setOnSort(EventHandler<SortEvent<TreeTableView<S>>> value) {
        onSortProperty().set(value);
    }

    public EventHandler<SortEvent<TreeTableView<S>>> getOnSort() {
        if (this.onSort != null) {
            return this.onSort.get();
        }
        return null;
    }

    public ObjectProperty<EventHandler<SortEvent<TreeTableView<S>>>> onSortProperty() {
        if (this.onSort == null) {
            this.onSort = new ObjectPropertyBase<EventHandler<SortEvent<TreeTableView<S>>>>() { // from class: javafx.scene.control.TreeTableView.11
                @Override // javafx.beans.property.ObjectPropertyBase
                protected void invalidated() {
                    EventType<SortEvent<TreeTableView<S>>> eventType = SortEvent.sortEvent();
                    EventHandler<SortEvent<TreeTableView<S>>> eventHandler = get();
                    TreeTableView.this.setEventHandler(eventType, eventHandler);
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return TreeTableView.this;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return "onSort";
                }
            };
        }
        return this.onSort;
    }

    @Override // javafx.scene.control.Control, javafx.scene.Parent
    protected void layoutChildren() {
        if (this.expandedItemCountDirty) {
            updateExpandedItemCount(getRoot());
        }
        super.layoutChildren();
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
            this.onScrollTo = new ObjectPropertyBase<EventHandler<ScrollToEvent<Integer>>>() { // from class: javafx.scene.control.TreeTableView.12
                @Override // javafx.beans.property.ObjectPropertyBase
                protected void invalidated() {
                    TreeTableView.this.setEventHandler(ScrollToEvent.scrollToTopIndex(), get());
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return TreeTableView.this;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return "onScrollTo";
                }
            };
        }
        return this.onScrollTo;
    }

    public void scrollToColumn(TreeTableColumn<S, ?> column) {
        ControlUtils.scrollToColumn(this, column);
    }

    public void scrollToColumnIndex(int columnIndex) {
        if (getColumns() != null) {
            ControlUtils.scrollToColumn(this, getColumns().get(columnIndex));
        }
    }

    public void setOnScrollToColumn(EventHandler<ScrollToEvent<TreeTableColumn<S, ?>>> value) {
        onScrollToColumnProperty().set(value);
    }

    public EventHandler<ScrollToEvent<TreeTableColumn<S, ?>>> getOnScrollToColumn() {
        if (this.onScrollToColumn != null) {
            return this.onScrollToColumn.get();
        }
        return null;
    }

    public ObjectProperty<EventHandler<ScrollToEvent<TreeTableColumn<S, ?>>>> onScrollToColumnProperty() {
        if (this.onScrollToColumn == null) {
            this.onScrollToColumn = new ObjectPropertyBase<EventHandler<ScrollToEvent<TreeTableColumn<S, ?>>>>() { // from class: javafx.scene.control.TreeTableView.13
                @Override // javafx.beans.property.ObjectPropertyBase
                protected void invalidated() {
                    EventType<ScrollToEvent<TreeTableColumn<S, ?>>> type = ScrollToEvent.scrollToColumn();
                    TreeTableView.this.setEventHandler(type, get());
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return TreeTableView.this;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return "onScrollToColumn";
                }
            };
        }
        return this.onScrollToColumn;
    }

    public int getRow(TreeItem<S> item) {
        return TreeUtil.getRow(item, getRoot(), this.expandedItemCountDirty, isShowRoot());
    }

    public TreeItem<S> getTreeItem(int row) {
        if (row < 0) {
            return null;
        }
        int _row = isShowRoot() ? row : row + 1;
        if (this.expandedItemCountDirty) {
            updateExpandedItemCount(getRoot());
        } else if (this.treeItemCacheMap.containsKey(Integer.valueOf(_row))) {
            SoftReference<TreeItem<S>> treeItemRef = this.treeItemCacheMap.get(Integer.valueOf(_row));
            TreeItem<S> treeItem = treeItemRef.get();
            if (treeItem != null) {
                return treeItem;
            }
        }
        TreeItem<S> treeItem2 = TreeUtil.getItem(getRoot(), _row, this.expandedItemCountDirty);
        this.treeItemCacheMap.put(Integer.valueOf(_row), new SoftReference<>(treeItem2));
        return treeItem2;
    }

    public int getTreeItemLevel(TreeItem<?> node) {
        TreeItem<S> root = getRoot();
        if (node == null) {
            return -1;
        }
        if (node == root) {
            return 0;
        }
        int level = 0;
        TreeItem<S> parent = node.getParent();
        while (true) {
            TreeItem<S> treeItem = parent;
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

    public final ObservableList<TreeTableColumn<S, ?>> getColumns() {
        return this.columns;
    }

    public final ObservableList<TreeTableColumn<S, ?>> getSortOrder() {
        return this.sortOrder;
    }

    public boolean resizeColumn(TreeTableColumn<S, ?> column, double delta) {
        if (column == null || Double.compare(delta, 0.0d) == 0) {
            return false;
        }
        boolean allowed = getColumnResizePolicy().call(new ResizeFeatures(this, column, Double.valueOf(delta))).booleanValue();
        return allowed;
    }

    public void edit(int row, TreeTableColumn<S, ?> column) {
        if (isEditable()) {
            if (column != null && !column.isEditable()) {
                return;
            }
            if (row < 0 && column == null) {
                setEditingCell(null);
            } else {
                setEditingCell(new TreeTablePosition<>(this, row, column));
            }
        }
    }

    public ObservableList<TreeTableColumn<S, ?>> getVisibleLeafColumns() {
        return this.unmodifiableVisibleLeafColumns;
    }

    public int getVisibleLeafIndex(TreeTableColumn<S, ?> column) {
        return getVisibleLeafColumns().indexOf(column);
    }

    public TreeTableColumn<S, ?> getVisibleLeafColumn(int column) {
        if (column < 0 || column >= this.visibleLeafColumns.size()) {
            return null;
        }
        return this.visibleLeafColumns.get(column);
    }

    boolean isSortingInProgress() {
        return this.sortingInProgress;
    }

    public void sort() {
        this.sortingInProgress = true;
        ObservableList<TreeTableColumn<S, ?>> sortOrder = getSortOrder();
        Comparator<TreeItem<S>> oldComparator = getComparator();
        setComparator(sortOrder.isEmpty() ? null : new TableColumnComparatorBase.TreeTableColumnComparator(sortOrder));
        SortEvent<TreeTableView<S>> sortEvent = new SortEvent<>(this, this);
        fireEvent(sortEvent);
        if (sortEvent.isConsumed()) {
            this.sortingInProgress = false;
            return;
        }
        List<TreeTablePosition<S, ?>> prevState = new ArrayList<>(getSelectionModel().getSelectedCells());
        int itemCount = prevState.size();
        getSelectionModel().startAtomic();
        Callback<TreeTableView<S>, Boolean> sortPolicy = getSortPolicy();
        if (sortPolicy == null) {
            this.sortingInProgress = false;
            return;
        }
        Boolean success = sortPolicy.call(this);
        if (getSortMode() == TreeSortMode.ALL_DESCENDANTS) {
            Set<TreeItem<S>> sortedParents = new HashSet<>();
            for (TreeTablePosition<S, ?> selectedPosition : prevState) {
                if (selectedPosition.getTreeItem() != null) {
                    TreeItem<S> parent = selectedPosition.getTreeItem().getParent();
                    while (true) {
                        TreeItem<S> parent2 = parent;
                        if (parent2 == null || !sortedParents.add(parent2)) {
                            break;
                        }
                        parent2.getChildren();
                        parent = parent2.getParent();
                    }
                }
            }
        }
        getSelectionModel().stopAtomic();
        if (success == null || !success.booleanValue()) {
            this.sortLock = true;
            TableUtil.handleSortFailure(sortOrder, this.lastSortEventType, this.lastSortEventSupportInfo);
            setComparator(oldComparator);
            this.sortLock = false;
        } else {
            if (getSelectionModel() instanceof TreeTableViewArrayListSelectionModel) {
                TreeTableViewArrayListSelectionModel<S> sm = (TreeTableViewArrayListSelectionModel) getSelectionModel();
                ObservableList<TreeTablePosition<S, ?>> newState = sm.getSelectedCells();
                List<TreeTablePosition<S, ?>> removed = new ArrayList<>();
                for (int i2 = 0; i2 < itemCount; i2++) {
                    TreeTablePosition<S, ?> prevItem = prevState.get(i2);
                    if (!newState.contains(prevItem)) {
                        removed.add(prevItem);
                    }
                }
                if (!removed.isEmpty()) {
                    ListChangeListener.Change<TreeTablePosition<S, ?>> c2 = new NonIterableChange.GenericAddRemoveChange<>(0, itemCount, removed, newState);
                    sm.handleSelectedCellsListChangeEvent(c2);
                }
            }
            getSelectionModel().setSelectedIndex(getRow(getSelectionModel().getSelectedItem()));
            getFocusModel().focus(getSelectionModel().getSelectedIndex());
        }
        this.sortingInProgress = false;
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
    public void updateExpandedItemCount(TreeItem<S> treeItem) {
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

    private void setContentWidth(double contentWidth) {
        this.contentWidth = contentWidth;
        if (this.isInited) {
            getColumnResizePolicy().call(new ResizeFeatures(this, null, Double.valueOf(0.0d)));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateVisibleLeafColumns() {
        List<TreeTableColumn<S, ?>> cols = new ArrayList<>();
        buildVisibleLeafColumns(getColumns(), cols);
        this.visibleLeafColumns.setAll(cols);
        getColumnResizePolicy().call(new ResizeFeatures(this, null, Double.valueOf(0.0d)));
    }

    private void buildVisibleLeafColumns(List<TreeTableColumn<S, ?>> cols, List<TreeTableColumn<S, ?>> vlc) {
        for (TreeTableColumn<S, ?> c2 : cols) {
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

    /* loaded from: jfxrt.jar:javafx/scene/control/TreeTableView$StyleableProperties.class */
    private static class StyleableProperties {
        private static final CssMetaData<TreeTableView<?>, Number> FIXED_CELL_SIZE = new CssMetaData<TreeTableView<?>, Number>("-fx-fixed-cell-size", SizeConverter.getInstance(), Double.valueOf(-1.0d)) { // from class: javafx.scene.control.TreeTableView.StyleableProperties.1
            @Override // javafx.css.CssMetaData
            public Double getInitialValue(TreeTableView<?> node) {
                return Double.valueOf(node.getFixedCellSize());
            }

            @Override // javafx.css.CssMetaData
            public boolean isSettable(TreeTableView<?> n2) {
                return ((TreeTableView) n2).fixedCellSize == null || !((TreeTableView) n2).fixedCellSize.isBound();
            }

            @Override // javafx.css.CssMetaData
            public StyleableProperty<Number> getStyleableProperty(TreeTableView<?> n2) {
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

    @Override // javafx.scene.control.Control
    protected Skin<?> createDefaultSkin() {
        return new TreeTableViewSkin(this);
    }

    @Override // javafx.scene.control.Control, javafx.scene.Parent, javafx.scene.Node
    public Object queryAccessibleAttribute(AccessibleAttribute attribute, Object... parameters) {
        switch (attribute) {
            case ROW_COUNT:
                return Integer.valueOf(getExpandedItemCount());
            case COLUMN_COUNT:
                return Integer.valueOf(getVisibleLeafColumns().size());
            case SELECTED_ITEMS:
                ObservableList<TreeTableRow<S>> rows = (ObservableList) super.queryAccessibleAttribute(attribute, parameters);
                List<Node> selection = new ArrayList<>();
                Iterator<TreeTableRow<S>> it = rows.iterator();
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
                TreeTableRow<S> row2 = (TreeTableRow) super.queryAccessibleAttribute(attribute, parameters);
                if (row2 != null) {
                    return row2.queryAccessibleAttribute(attribute, parameters);
                }
                return null;
            case MULTIPLE_SELECTION:
                TreeTableViewSelectionModel<S> sm = getSelectionModel();
                return Boolean.valueOf(sm != null && sm.getSelectionMode() == SelectionMode.MULTIPLE);
            default:
                return super.queryAccessibleAttribute(attribute, parameters);
        }
    }

    /* loaded from: jfxrt.jar:javafx/scene/control/TreeTableView$ResizeFeatures.class */
    public static class ResizeFeatures<S> extends ResizeFeaturesBase<TreeItem<S>> {
        private TreeTableView<S> treeTable;

        public ResizeFeatures(TreeTableView<S> treeTable, TreeTableColumn<S, ?> column, Double delta) {
            super(column, delta);
            this.treeTable = treeTable;
        }

        @Override // javafx.scene.control.ResizeFeaturesBase
        public TreeTableColumn<S, ?> getColumn() {
            return (TreeTableColumn) super.getColumn();
        }

        public TreeTableView<S> getTable() {
            return this.treeTable;
        }
    }

    /* loaded from: jfxrt.jar:javafx/scene/control/TreeTableView$EditEvent.class */
    public static class EditEvent<S> extends Event {
        private static final long serialVersionUID = -4437033058917528976L;
        public static final EventType<?> ANY = TreeTableView.EDIT_ANY_EVENT;
        private final S oldValue;
        private final S newValue;
        private final transient TreeItem<S> treeItem;

        public EditEvent(TreeTableView<S> source, EventType<? extends EditEvent> eventType, TreeItem<S> treeItem, S oldValue, S newValue) {
            super(source, Event.NULL_SOURCE_TARGET, eventType);
            this.oldValue = oldValue;
            this.newValue = newValue;
            this.treeItem = treeItem;
        }

        @Override // java.util.EventObject
        public TreeTableView<S> getSource() {
            return (TreeTableView) super.getSource();
        }

        public TreeItem<S> getTreeItem() {
            return this.treeItem;
        }

        public S getNewValue() {
            return this.newValue;
        }

        public S getOldValue() {
            return this.oldValue;
        }
    }

    /* loaded from: jfxrt.jar:javafx/scene/control/TreeTableView$TreeTableViewSelectionModel.class */
    public static abstract class TreeTableViewSelectionModel<S> extends TableSelectionModel<TreeItem<S>> {
        private final TreeTableView<S> treeTableView;

        public abstract ObservableList<TreeTablePosition<S, ?>> getSelectedCells();

        public TreeTableViewSelectionModel(TreeTableView<S> treeTableView) {
            if (treeTableView == null) {
                throw new NullPointerException("TreeTableView can not be null");
            }
            this.treeTableView = treeTableView;
        }

        public TreeTableView<S> getTreeTableView() {
            return this.treeTableView;
        }

        @Override // javafx.scene.control.MultipleSelectionModelBase
        public TreeItem<S> getModelItem(int index) {
            return this.treeTableView.getTreeItem(index);
        }

        @Override // javafx.scene.control.MultipleSelectionModelBase
        protected int getItemCount() {
            return this.treeTableView.getExpandedItemCount();
        }

        @Override // javafx.scene.control.MultipleSelectionModelBase
        public void focus(int row) {
            focus(row, null);
        }

        @Override // javafx.scene.control.MultipleSelectionModelBase
        public int getFocusedIndex() {
            return getFocusedCell().getRow();
        }

        @Override // javafx.scene.control.TableSelectionModel
        public void selectRange(int minRow, TableColumnBase<TreeItem<S>, ?> minColumn, int maxRow, TableColumnBase<TreeItem<S>, ?> maxColumn) {
            int minColumnIndex = this.treeTableView.getVisibleLeafIndex((TreeTableColumn) minColumn);
            int maxColumnIndex = this.treeTableView.getVisibleLeafIndex((TreeTableColumn) maxColumn);
            for (int _row = minRow; _row <= maxRow; _row++) {
                for (int _col = minColumnIndex; _col <= maxColumnIndex; _col++) {
                    select(_row, this.treeTableView.getVisibleLeafColumn(_col));
                }
            }
        }

        private void focus(int row, TreeTableColumn<S, ?> column) {
            focus(new TreeTablePosition<>(getTreeTableView(), row, column));
        }

        private void focus(TreeTablePosition<S, ?> pos) {
            if (getTreeTableView().getFocusModel() == null) {
                return;
            }
            getTreeTableView().getFocusModel().focus(pos.getRow(), (TreeTableColumn) pos.getTableColumn());
        }

        private TreeTablePosition<S, ?> getFocusedCell() {
            if (this.treeTableView.getFocusModel() == null) {
                return new TreeTablePosition<>(this.treeTableView, -1, null);
            }
            return this.treeTableView.getFocusModel().getFocusedCell();
        }
    }

    /* loaded from: jfxrt.jar:javafx/scene/control/TreeTableView$TreeTableViewArrayListSelectionModel.class */
    static class TreeTableViewArrayListSelectionModel<S> extends TreeTableViewSelectionModel<S> {
        private final MappingChange.Map<TreeTablePosition<S, ?>, TreeItem<S>> cellToItemsMap;
        private final MappingChange.Map<TreeTablePosition<S, ?>, Integer> cellToIndicesMap;
        private TreeTableView<S> treeTableView;
        private ChangeListener<TreeItem<S>> rootPropertyListener;
        private EventHandler<TreeItem.TreeModificationEvent<S>> treeItemListener;
        private WeakChangeListener<TreeItem<S>> weakRootPropertyListener;
        private WeakEventHandler<TreeItem.TreeModificationEvent<S>> weakTreeItemListener;
        private final SelectedCellsMap<TreeTablePosition<S, ?>> selectedCellsMap;
        private final ReadOnlyUnbackedObservableList<TreeItem<S>> selectedItems;
        private final ReadOnlyUnbackedObservableList<TreeTablePosition<S, ?>> selectedCellsSeq;

        public TreeTableViewArrayListSelectionModel(TreeTableView<S> treeTableView) {
            super(treeTableView);
            this.cellToItemsMap = f2 -> {
                return getModelItem(f2.getRow());
            };
            this.cellToIndicesMap = f3 -> {
                return Integer.valueOf(f3.getRow());
            };
            this.treeTableView = null;
            this.rootPropertyListener = (observable, oldValue, newValue) -> {
                updateDefaultSelection();
                updateTreeEventListener(oldValue, newValue);
            };
            this.treeItemListener = new EventHandler<TreeItem.TreeModificationEvent<S>>() { // from class: javafx.scene.control.TreeTableView.TreeTableViewArrayListSelectionModel.4
                @Override // javafx.event.EventHandler
                public void handle(TreeItem.TreeModificationEvent<S> e2) {
                    TreeItem<S> treeItem;
                    if ((TreeTableViewArrayListSelectionModel.this.getSelectedIndex() == -1 && TreeTableViewArrayListSelectionModel.this.getSelectedItem() == null) || (treeItem = e2.getTreeItem()) == null) {
                        return;
                    }
                    int oldSelectedIndex = TreeTableViewArrayListSelectionModel.this.getSelectedIndex();
                    TreeTableViewArrayListSelectionModel.this.getSelectedItem();
                    TreeTableViewArrayListSelectionModel.this.treeTableView.expandedItemCountDirty = true;
                    int startRow = TreeTableViewArrayListSelectionModel.this.treeTableView.getRow(treeItem);
                    int shift = 0;
                    ListChangeListener.Change<? extends TreeItem<S>> change = e2.getChange();
                    if (change != null) {
                        change.next();
                    }
                    do {
                        int addedSize = change == null ? 0 : change.getAddedSize();
                        int removedSize = change == null ? 0 : change.getRemovedSize();
                        if (e2.wasExpanded()) {
                            shift += treeItem.getExpandedDescendentCount(false) - 1;
                            startRow++;
                        } else if (e2.wasCollapsed()) {
                            treeItem.getExpandedDescendentCount(false);
                            int count = treeItem.previousExpandedDescendentCount;
                            int selectedIndex = TreeTableViewArrayListSelectionModel.this.getSelectedIndex();
                            boolean wasPrimarySelectionInChild = selectedIndex >= startRow + 1 && selectedIndex < startRow + count;
                            boolean wasAnyChildSelected = false;
                            boolean isCellSelectionMode = TreeTableViewArrayListSelectionModel.this.isCellSelectionEnabled();
                            ObservableList<TreeTableColumn<S, ?>> columns = TreeTableViewArrayListSelectionModel.this.getTreeTableView().getVisibleLeafColumns();
                            if (!isCellSelectionMode) {
                                TreeTableViewArrayListSelectionModel.this.startAtomic();
                            }
                            int from = startRow + 1;
                            int to = startRow + count;
                            List<Integer> removed = new ArrayList<>();
                            TreeTableColumn<S, ?> selectedColumn = null;
                            for (int i2 = from; i2 < to; i2++) {
                                if (isCellSelectionMode) {
                                    for (int column = 0; column < columns.size(); column++) {
                                        TreeTableColumn<S, ?> col = columns.get(column);
                                        if (TreeTableViewArrayListSelectionModel.this.isSelected(i2, col)) {
                                            wasAnyChildSelected = true;
                                            TreeTableViewArrayListSelectionModel.this.clearSelection(i2, col);
                                            selectedColumn = col;
                                        }
                                    }
                                } else if (TreeTableViewArrayListSelectionModel.this.isSelected(i2)) {
                                    wasAnyChildSelected = true;
                                    TreeTableViewArrayListSelectionModel.this.clearSelection(i2);
                                    removed.add(Integer.valueOf(i2));
                                }
                            }
                            if (!isCellSelectionMode) {
                                TreeTableViewArrayListSelectionModel.this.stopAtomic();
                            }
                            if (wasPrimarySelectionInChild && wasAnyChildSelected) {
                                TreeTableViewArrayListSelectionModel.this.select(startRow, selectedColumn);
                            } else if (!isCellSelectionMode) {
                                ListChangeListener.Change newChange = new NonIterableChange.GenericAddRemoveChange(from, from, removed, TreeTableViewArrayListSelectionModel.this.selectedIndicesSeq);
                                TreeTableViewArrayListSelectionModel.this.selectedIndicesSeq.callObservers(newChange);
                            }
                            shift += (-count) + 1;
                            startRow++;
                        } else if (e2.wasPermutated()) {
                            List<TreeTablePosition<S, ?>> currentSelection = new ArrayList<>(TreeTableViewArrayListSelectionModel.this.selectedCellsMap.getSelectedCells());
                            ArrayList arrayList = new ArrayList();
                            boolean selectionIndicesChanged = false;
                            for (TreeTablePosition<S, ?> selectedCell : currentSelection) {
                                int newRow = TreeTableViewArrayListSelectionModel.this.treeTableView.getRow(selectedCell.getTreeItem());
                                if (selectedCell.getRow() != newRow) {
                                    selectionIndicesChanged = true;
                                }
                                arrayList.add(new TreeTablePosition(selectedCell, newRow));
                            }
                            if (selectionIndicesChanged) {
                                if (TreeTableViewArrayListSelectionModel.this.treeTableView.isSortingInProgress()) {
                                    TreeTableViewArrayListSelectionModel.this.startAtomic();
                                    TreeTableViewArrayListSelectionModel.this.selectedCellsMap.setAll(arrayList);
                                    TreeTableViewArrayListSelectionModel.this.stopAtomic();
                                } else {
                                    TreeTableViewArrayListSelectionModel.this.startAtomic();
                                    TreeTableViewArrayListSelectionModel.this.quietClearSelection();
                                    TreeTableViewArrayListSelectionModel.this.stopAtomic();
                                    TreeTableViewArrayListSelectionModel.this.selectedCellsMap.setAll(arrayList);
                                    int selectedIndex2 = TreeTableViewArrayListSelectionModel.this.treeTableView.getRow(TreeTableViewArrayListSelectionModel.this.getSelectedItem());
                                    TreeTableViewArrayListSelectionModel.this.setSelectedIndex(selectedIndex2);
                                    TreeTableViewArrayListSelectionModel.this.focus(selectedIndex2);
                                }
                            }
                        } else if (e2.wasAdded()) {
                            shift += treeItem.isExpanded() ? addedSize : 0;
                            startRow = TreeTableViewArrayListSelectionModel.this.treeTableView.getRow(e2.getChange().getAddedSubList().get(0));
                            TreeTablePosition<S, ?> anchor = (TreeTablePosition) TreeTableCellBehavior.getAnchor(TreeTableViewArrayListSelectionModel.this.treeTableView, null);
                            if (anchor != null) {
                                boolean isAnchorSelected = TreeTableViewArrayListSelectionModel.this.isSelected(anchor.getRow(), anchor.getTableColumn());
                                if (isAnchorSelected) {
                                    TreeTablePosition<S, ?> newAnchor = new TreeTablePosition<>(TreeTableViewArrayListSelectionModel.this.treeTableView, anchor.getRow() + shift, anchor.getTableColumn());
                                    TreeTableCellBehavior.setAnchor(TreeTableViewArrayListSelectionModel.this.treeTableView, newAnchor, false);
                                }
                            }
                        } else if (e2.wasRemoved()) {
                            shift += treeItem.isExpanded() ? -removedSize : 0;
                            startRow += e2.getFrom() + 1;
                            List<Integer> selectedIndices = TreeTableViewArrayListSelectionModel.this.getSelectedIndices();
                            List<TreeItem<S>> selectedItems = TreeTableViewArrayListSelectionModel.this.getSelectedItems();
                            TreeItem<S> selectedItem = TreeTableViewArrayListSelectionModel.this.getSelectedItem();
                            List<? extends TreeItem<S>> removedChildren = e2.getChange().getRemoved();
                            for (int i3 = 0; i3 < selectedIndices.size() && !selectedItems.isEmpty(); i3++) {
                                int index = selectedIndices.get(i3).intValue();
                                if (index > selectedItems.size()) {
                                    break;
                                }
                                if (removedChildren.size() == 1 && selectedItems.size() == 1 && selectedItem != null && selectedItem.equals(removedChildren.get(0)) && oldSelectedIndex < TreeTableViewArrayListSelectionModel.this.getItemCount()) {
                                    int previousRow = oldSelectedIndex == 0 ? 0 : oldSelectedIndex - 1;
                                    TreeItem<S> newSelectedItem = TreeTableViewArrayListSelectionModel.this.getModelItem(previousRow);
                                    if (!selectedItem.equals(newSelectedItem)) {
                                        TreeTableViewArrayListSelectionModel.this.clearAndSelect(previousRow);
                                    }
                                }
                            }
                        }
                        if (e2.getChange() == null) {
                            break;
                        }
                    } while (e2.getChange().next());
                    if (shift != 0) {
                        TreeTableViewArrayListSelectionModel.this.shiftSelection(startRow, shift, new Callback<MultipleSelectionModelBase.ShiftParams, Void>() { // from class: javafx.scene.control.TreeTableView.TreeTableViewArrayListSelectionModel.4.1
                            @Override // javafx.util.Callback
                            public Void call(MultipleSelectionModelBase.ShiftParams param) {
                                TreeTableViewArrayListSelectionModel.this.startAtomic();
                                int clearIndex = param.getClearIndex();
                                int setIndex = param.getSetIndex();
                                TreeTablePosition<S, ?> oldTP = null;
                                if (clearIndex > -1) {
                                    for (int i4 = 0; i4 < TreeTableViewArrayListSelectionModel.this.selectedCellsMap.size(); i4++) {
                                        TreeTablePosition<S, ?> tp = (TreeTablePosition) TreeTableViewArrayListSelectionModel.this.selectedCellsMap.get(i4);
                                        if (tp.getRow() == clearIndex) {
                                            oldTP = tp;
                                            TreeTableViewArrayListSelectionModel.this.selectedCellsMap.remove(tp);
                                        } else if (tp.getRow() == setIndex && !param.isSelected()) {
                                            TreeTableViewArrayListSelectionModel.this.selectedCellsMap.remove(tp);
                                        }
                                    }
                                }
                                if (oldTP != null && param.isSelected()) {
                                    TreeTablePosition<S, ?> newTP = new TreeTablePosition<>(TreeTableViewArrayListSelectionModel.this.treeTableView, param.getSetIndex(), oldTP.getTableColumn());
                                    TreeTableViewArrayListSelectionModel.this.selectedCellsMap.add(newTP);
                                }
                                TreeTableViewArrayListSelectionModel.this.stopAtomic();
                                return null;
                            }
                        });
                    }
                }
            };
            this.weakRootPropertyListener = new WeakChangeListener<>(this.rootPropertyListener);
            this.treeTableView = treeTableView;
            this.treeTableView.rootProperty().addListener(this.weakRootPropertyListener);
            this.treeTableView.showRootProperty().addListener(o2 -> {
                shiftSelection(0, treeTableView.isShowRoot() ? 1 : -1, null);
            });
            updateTreeEventListener(null, treeTableView.getRoot());
            this.selectedCellsMap = new SelectedCellsMap<TreeTablePosition<S, ?>>(c2 -> {
                handleSelectedCellsListChangeEvent(c2);
            }) { // from class: javafx.scene.control.TreeTableView.TreeTableViewArrayListSelectionModel.1
                @Override // com.sun.javafx.scene.control.SelectedCellsMap
                public boolean isCellSelectionEnabled() {
                    return TreeTableViewArrayListSelectionModel.this.isCellSelectionEnabled();
                }
            };
            this.selectedItems = new ReadOnlyUnbackedObservableList<TreeItem<S>>() { // from class: javafx.scene.control.TreeTableView.TreeTableViewArrayListSelectionModel.2
                /* JADX WARN: Multi-variable type inference failed */
                @Override // com.sun.javafx.scene.control.ReadOnlyUnbackedObservableList, java.util.List
                public TreeItem<S> get(int i2) {
                    return TreeTableViewArrayListSelectionModel.this.getModelItem(((Integer) TreeTableViewArrayListSelectionModel.this.getSelectedIndices().get(i2)).intValue());
                }

                @Override // com.sun.javafx.scene.control.ReadOnlyUnbackedObservableList, java.util.List, java.util.Collection, java.util.Set
                public int size() {
                    return TreeTableViewArrayListSelectionModel.this.getSelectedIndices().size();
                }
            };
            this.selectedCellsSeq = new ReadOnlyUnbackedObservableList<TreeTablePosition<S, ?>>() { // from class: javafx.scene.control.TreeTableView.TreeTableViewArrayListSelectionModel.3
                @Override // com.sun.javafx.scene.control.ReadOnlyUnbackedObservableList, java.util.List
                public TreeTablePosition<S, ?> get(int i2) {
                    return (TreeTablePosition) TreeTableViewArrayListSelectionModel.this.selectedCellsMap.get(i2);
                }

                @Override // com.sun.javafx.scene.control.ReadOnlyUnbackedObservableList, java.util.List, java.util.Collection, java.util.Set
                public int size() {
                    return TreeTableViewArrayListSelectionModel.this.selectedCellsMap.size();
                }
            };
            updateDefaultSelection();
            cellSelectionEnabledProperty().addListener(o3 -> {
                updateDefaultSelection();
                TableCellBehaviorBase.setAnchor(treeTableView, getFocusedCell(), true);
            });
        }

        private void updateTreeEventListener(TreeItem<S> oldRoot, TreeItem<S> newRoot) {
            if (oldRoot != null && this.weakTreeItemListener != null) {
                oldRoot.removeEventHandler(TreeItem.expandedItemCountChangeEvent(), this.weakTreeItemListener);
            }
            if (newRoot != null) {
                this.weakTreeItemListener = new WeakEventHandler<>(this.treeItemListener);
                newRoot.addEventHandler(TreeItem.expandedItemCountChangeEvent(), this.weakTreeItemListener);
            }
        }

        @Override // javafx.scene.control.TableSelectionModel, javafx.scene.control.MultipleSelectionModelBase, javafx.scene.control.MultipleSelectionModel
        public ObservableList<TreeItem<S>> getSelectedItems() {
            return this.selectedItems;
        }

        @Override // javafx.scene.control.TreeTableView.TreeTableViewSelectionModel
        public ObservableList<TreeTablePosition<S, ?>> getSelectedCells() {
            return this.selectedCellsSeq;
        }

        @Override // javafx.scene.control.TableSelectionModel, javafx.scene.control.MultipleSelectionModelBase, javafx.scene.control.SelectionModel
        public void clearAndSelect(int row) {
            clearAndSelect(row, null);
        }

        @Override // javafx.scene.control.TableSelectionModel
        public void clearAndSelect(int row, TableColumnBase<TreeItem<S>, ?> column) {
            ListChangeListener.Change<TreeTablePosition<S, ?>> change;
            if (row < 0 || row >= getItemCount()) {
                return;
            }
            TreeTablePosition<S, ?> newTablePosition = new TreeTablePosition<>(getTreeTableView(), row, (TreeTableColumn) column);
            boolean isCellSelectionEnabled = isCellSelectionEnabled();
            TreeTableCellBehavior.setAnchor(this.treeTableView, newTablePosition, false);
            if (isCellSelectionEnabled && column == null) {
                return;
            }
            boolean wasSelected = isSelected(row, column);
            List<TreeTablePosition<S, ?>> previousSelection = new ArrayList<>(this.selectedCellsMap.getSelectedCells());
            if (wasSelected && previousSelection.size() == 1) {
                TreeTablePosition<S, ?> selectedCell = getSelectedCells().get(0);
                if (getSelectedItem() == getModelItem(row) && selectedCell.getRow() == row && selectedCell.getTableColumn() == column) {
                    return;
                }
            }
            startAtomic();
            clearSelection();
            select(row, column);
            stopAtomic();
            if (isCellSelectionEnabled) {
                previousSelection.remove(newTablePosition);
            } else {
                Iterator<TreeTablePosition<S, ?>> it = previousSelection.iterator();
                while (true) {
                    if (!it.hasNext()) {
                        break;
                    }
                    TreeTablePosition<S, ?> tp = it.next();
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
            select(row, null);
        }

        @Override // javafx.scene.control.TableSelectionModel
        public void select(int row, TableColumnBase<TreeItem<S>, ?> column) {
            if (row < 0 || row >= getRowCount()) {
                return;
            }
            if (isCellSelectionEnabled() && column == null) {
                List<TreeTableColumn<S, ?>> columns = getTreeTableView().getVisibleLeafColumns();
                for (int i2 = 0; i2 < columns.size(); i2++) {
                    select(row, columns.get(i2));
                }
                return;
            }
            TreeTablePosition<S, ?> pos = new TreeTablePosition<>(getTreeTableView(), row, (TreeTableColumn) column);
            if (getSelectionMode() == SelectionMode.SINGLE) {
                startAtomic();
                quietClearSelection();
                stopAtomic();
            }
            if (TreeTableCellBehavior.hasDefaultAnchor(this.treeTableView)) {
                TreeTableCellBehavior.removeAnchor(this.treeTableView);
            }
            this.selectedCellsMap.add(pos);
            updateSelectedIndex(row);
            focus(row, (TreeTableColumn) column);
        }

        @Override // javafx.scene.control.TableSelectionModel, javafx.scene.control.MultipleSelectionModelBase, javafx.scene.control.SelectionModel
        public void select(TreeItem<S> obj) {
            if (obj == null && getSelectionMode() == SelectionMode.SINGLE) {
                clearSelection();
                return;
            }
            int firstIndex = this.treeTableView.getRow(obj);
            if (firstIndex > -1) {
                if (isSelected(firstIndex)) {
                    return;
                }
                if (getSelectionMode() == SelectionMode.SINGLE) {
                    quietClearSelection();
                }
                select(firstIndex);
                return;
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
            int rowCount = getRowCount();
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
            Set<TreeTablePosition<S, ?>> positions = new LinkedHashSet<>();
            if (row >= 0 && row < rowCount) {
                if (isCellSelectionEnabled()) {
                    List<TreeTableColumn<S, ?>> columns = getTreeTableView().getVisibleLeafColumns();
                    for (int column = 0; column < columns.size(); column++) {
                        if (!this.selectedCellsMap.isSelected(row, column)) {
                            positions.add(new TreeTablePosition<>(getTreeTableView(), row, columns.get(column)));
                        }
                    }
                } else {
                    boolean match = this.selectedCellsMap.isSelected(row, -1);
                    if (!match) {
                        positions.add(new TreeTablePosition<>(getTreeTableView(), row, null));
                    }
                }
                lastIndex = row;
            }
            for (int index2 : rows) {
                if (index2 >= 0 && index2 < rowCount) {
                    lastIndex = index2;
                    if (isCellSelectionEnabled()) {
                        List<TreeTableColumn<S, ?>> columns2 = getTreeTableView().getVisibleLeafColumns();
                        for (int column2 = 0; column2 < columns2.size(); column2++) {
                            if (!this.selectedCellsMap.isSelected(index2, column2)) {
                                positions.add(new TreeTablePosition<>(getTreeTableView(), index2, columns2.get(column2)));
                                lastIndex = index2;
                            }
                        }
                    } else if (!this.selectedCellsMap.isSelected(index2, -1)) {
                        positions.add(new TreeTablePosition<>(getTreeTableView(), index2, null));
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
                List<TreeTablePosition<S, ?>> indices = new ArrayList<>();
                TreeTablePosition<S, ?> tp = null;
                for (int col = 0; col < getTreeTableView().getVisibleLeafColumns().size(); col++) {
                    TreeTableColumn<S, ?> column = getTreeTableView().getVisibleLeafColumns().get(col);
                    for (int row = 0; row < getRowCount(); row++) {
                        tp = new TreeTablePosition<>(getTreeTableView(), row, column);
                        indices.add(tp);
                    }
                }
                this.selectedCellsMap.setAll(indices);
                if (tp != null) {
                    select(tp.getRow(), tp.getTableColumn());
                    focus(tp.getRow(), tp.getTableColumn());
                    return;
                }
                return;
            }
            List<TreeTablePosition<S, ?>> indices2 = new ArrayList<>();
            for (int i2 = 0; i2 < getRowCount(); i2++) {
                indices2.add(new TreeTablePosition<>(getTreeTableView(), i2, null));
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

        @Override // javafx.scene.control.TreeTableView.TreeTableViewSelectionModel, javafx.scene.control.TableSelectionModel
        public void selectRange(int minRow, TableColumnBase<TreeItem<S>, ?> minColumn, int maxRow, TableColumnBase<TreeItem<S>, ?> maxColumn) {
            if (getSelectionMode() == SelectionMode.SINGLE) {
                quietClearSelection();
                select(maxRow, maxColumn);
                return;
            }
            startAtomic();
            int itemCount = getItemCount();
            boolean isCellSelectionEnabled = isCellSelectionEnabled();
            int minColumnIndex = this.treeTableView.getVisibleLeafIndex((TreeTableColumn) minColumn);
            int maxColumnIndex = this.treeTableView.getVisibleLeafIndex((TreeTableColumn) maxColumn);
            int _minColumnIndex = Math.min(minColumnIndex, maxColumnIndex);
            int _maxColumnIndex = Math.max(minColumnIndex, maxColumnIndex);
            int _minRow = Math.min(minRow, maxRow);
            int _maxRow = Math.max(minRow, maxRow);
            List<TreeTablePosition<S, ?>> cellsToSelect = new ArrayList<>();
            for (int _row = _minRow; _row <= _maxRow; _row++) {
                if (_row >= 0 && _row < itemCount) {
                    if (!isCellSelectionEnabled) {
                        cellsToSelect.add(new TreeTablePosition<>(this.treeTableView, _row, (TreeTableColumn) minColumn));
                    } else {
                        for (int _col = _minColumnIndex; _col <= _maxColumnIndex; _col++) {
                            TreeTableColumn<S, ?> column = this.treeTableView.getVisibleLeafColumn(_col);
                            if (column != null || !isCellSelectionEnabled) {
                                cellsToSelect.add(new TreeTablePosition<>(this.treeTableView, _row, column));
                            }
                        }
                    }
                }
            }
            cellsToSelect.removeAll(getSelectedCells());
            this.selectedCellsMap.addAll(cellsToSelect);
            stopAtomic();
            updateSelectedIndex(maxRow);
            focus(maxRow, (TreeTableColumn) maxColumn);
            TreeTableColumn<S, ?> startColumn = (TreeTableColumn) minColumn;
            TreeTableColumn<S, ?> endColumn = isCellSelectionEnabled ? (TreeTableColumn) maxColumn : startColumn;
            int startChangeIndex = this.selectedCellsMap.indexOf(new TreeTablePosition(this.treeTableView, minRow, startColumn));
            int endChangeIndex = this.selectedCellsMap.indexOf(new TreeTablePosition(this.treeTableView, maxRow, endColumn));
            if (startChangeIndex > -1 && endChangeIndex > -1) {
                int startIndex = Math.min(startChangeIndex, endChangeIndex);
                int endIndex = Math.max(startChangeIndex, endChangeIndex);
                ListChangeListener.Change c2 = new NonIterableChange.SimpleAddChange(startIndex, endIndex + 1, this.selectedCellsSeq);
                handleSelectedCellsListChangeEvent(c2);
            }
        }

        @Override // javafx.scene.control.TableSelectionModel, javafx.scene.control.MultipleSelectionModelBase, javafx.scene.control.SelectionModel
        public void clearSelection(int index) {
            clearSelection(index, null);
        }

        @Override // javafx.scene.control.TableSelectionModel
        public void clearSelection(int row, TableColumnBase<TreeItem<S>, ?> column) {
            clearSelection(new TreeTablePosition<>(getTreeTableView(), row, (TreeTableColumn) column));
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void clearSelection(TreeTablePosition<S, ?> tp) {
            boolean csMode = isCellSelectionEnabled();
            int row = tp.getRow();
            Iterator<TreeTablePosition<S, ?>> it = getSelectedCells().iterator();
            while (true) {
                if (!it.hasNext()) {
                    break;
                }
                TreeTablePosition<S, ?> pos = it.next();
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
            final List<TreeTablePosition<S, ?>> removed = new ArrayList<>(getSelectedCells());
            quietClearSelection();
            if (!isAtomic()) {
                updateSelectedIndex(-1);
                focus(-1);
                ListChangeListener.Change<TreeTablePosition<S, ?>> c2 = new NonIterableChange<TreeTablePosition<S, ?>>(0, 0, this.selectedCellsSeq) { // from class: javafx.scene.control.TreeTableView.TreeTableViewArrayListSelectionModel.5
                    @Override // javafx.collections.ListChangeListener.Change
                    public List<TreeTablePosition<S, ?>> getRemoved() {
                        return removed;
                    }
                };
                handleSelectedCellsListChangeEvent(c2);
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void quietClearSelection() {
            startAtomic();
            this.selectedCellsMap.clear();
            stopAtomic();
        }

        @Override // javafx.scene.control.TableSelectionModel, javafx.scene.control.MultipleSelectionModelBase, javafx.scene.control.SelectionModel
        public boolean isSelected(int index) {
            return isSelected(index, null);
        }

        @Override // javafx.scene.control.TableSelectionModel
        public boolean isSelected(int row, TableColumnBase<TreeItem<S>, ?> column) {
            boolean isCellSelectionEnabled = isCellSelectionEnabled();
            if (isCellSelectionEnabled && column == null) {
                return false;
            }
            int columnIndex = (!isCellSelectionEnabled || column == null) ? -1 : this.treeTableView.getVisibleLeafIndex((TreeTableColumn) column);
            return this.selectedCellsMap.isSelected(row, columnIndex);
        }

        @Override // javafx.scene.control.TableSelectionModel, javafx.scene.control.MultipleSelectionModelBase, javafx.scene.control.SelectionModel
        public boolean isEmpty() {
            return this.selectedCellsMap.isEmpty();
        }

        @Override // javafx.scene.control.TableSelectionModel, javafx.scene.control.MultipleSelectionModelBase, javafx.scene.control.SelectionModel
        public void selectPrevious() {
            if (isCellSelectionEnabled()) {
                TreeTablePosition<S, ?> pos = getFocusedCell();
                if (pos.getColumn() - 1 >= 0) {
                    select(pos.getRow(), getTableColumn(pos.getTableColumn(), -1));
                    return;
                } else {
                    if (pos.getRow() < getRowCount() - 1) {
                        select(pos.getRow() - 1, getTableColumn(getTreeTableView().getVisibleLeafColumns().size() - 1));
                        return;
                    }
                    return;
                }
            }
            int focusIndex = getFocusedIndex();
            if (focusIndex == -1) {
                select(getRowCount() - 1);
            } else if (focusIndex > 0) {
                select(focusIndex - 1);
            }
        }

        @Override // javafx.scene.control.TableSelectionModel, javafx.scene.control.MultipleSelectionModelBase, javafx.scene.control.SelectionModel
        public void selectNext() {
            if (isCellSelectionEnabled()) {
                TreeTablePosition<S, ?> pos = getFocusedCell();
                if (pos.getColumn() + 1 < getTreeTableView().getVisibleLeafColumns().size()) {
                    select(pos.getRow(), getTableColumn(pos.getTableColumn(), 1));
                    return;
                } else {
                    if (pos.getRow() < getRowCount() - 1) {
                        select(pos.getRow() + 1, getTableColumn(0));
                        return;
                    }
                    return;
                }
            }
            int focusIndex = getFocusedIndex();
            if (focusIndex == -1) {
                select(0);
            } else if (focusIndex < getRowCount() - 1) {
                select(focusIndex + 1);
            }
        }

        @Override // javafx.scene.control.TableSelectionModel
        public void selectAboveCell() {
            TreeTablePosition<S, ?> pos = getFocusedCell();
            if (pos.getRow() == -1) {
                select(getRowCount() - 1);
            } else if (pos.getRow() > 0) {
                select(pos.getRow() - 1, pos.getTableColumn());
            }
        }

        @Override // javafx.scene.control.TableSelectionModel
        public void selectBelowCell() {
            TreeTablePosition<S, ?> pos = getFocusedCell();
            if (pos.getRow() == -1) {
                select(0);
            } else if (pos.getRow() < getRowCount() - 1) {
                select(pos.getRow() + 1, pos.getTableColumn());
            }
        }

        @Override // javafx.scene.control.TableSelectionModel, javafx.scene.control.MultipleSelectionModelBase, javafx.scene.control.MultipleSelectionModel, javafx.scene.control.SelectionModel
        public void selectFirst() {
            TreeTablePosition<S, ?> focusedCell = getFocusedCell();
            if (getSelectionMode() == SelectionMode.SINGLE) {
                quietClearSelection();
            }
            if (getRowCount() > 0) {
                if (isCellSelectionEnabled()) {
                    select(0, focusedCell.getTableColumn());
                } else {
                    select(0);
                }
            }
        }

        @Override // javafx.scene.control.TableSelectionModel, javafx.scene.control.MultipleSelectionModelBase, javafx.scene.control.MultipleSelectionModel, javafx.scene.control.SelectionModel
        public void selectLast() {
            TreeTablePosition<S, ?> focusedCell = getFocusedCell();
            if (getSelectionMode() == SelectionMode.SINGLE) {
                quietClearSelection();
            }
            int numItems = getRowCount();
            if (numItems > 0 && getSelectedIndex() < numItems - 1) {
                if (isCellSelectionEnabled()) {
                    select(numItems - 1, focusedCell.getTableColumn());
                } else {
                    select(numItems - 1);
                }
            }
        }

        @Override // javafx.scene.control.TableSelectionModel
        public void selectLeftCell() {
            if (isCellSelectionEnabled()) {
                TreeTablePosition<S, ?> pos = getFocusedCell();
                if (pos.getColumn() - 1 >= 0) {
                    select(pos.getRow(), getTableColumn(pos.getTableColumn(), -1));
                }
            }
        }

        @Override // javafx.scene.control.TableSelectionModel
        public void selectRightCell() {
            if (isCellSelectionEnabled()) {
                TreeTablePosition<S, ?> pos = getFocusedCell();
                if (pos.getColumn() + 1 < getTreeTableView().getVisibleLeafColumns().size()) {
                    select(pos.getRow(), getTableColumn(pos.getTableColumn(), 1));
                }
            }
        }

        private void updateDefaultSelection() {
            clearSelection();
            int newFocusIndex = getItemCount() > 0 ? 0 : -1;
            focus(newFocusIndex, isCellSelectionEnabled() ? getTableColumn(0) : null);
        }

        private TreeTableColumn<S, ?> getTableColumn(int pos) {
            return getTreeTableView().getVisibleLeafColumn(pos);
        }

        private TreeTableColumn<S, ?> getTableColumn(TreeTableColumn<S, ?> column, int offset) {
            int columnIndex = getTreeTableView().getVisibleLeafIndex(column);
            int newColumnIndex = columnIndex + offset;
            return getTreeTableView().getVisibleLeafColumn(newColumnIndex);
        }

        private void updateSelectedIndex(int row) {
            setSelectedIndex(row);
            setSelectedItem(getModelItem(row));
        }

        @Override // javafx.scene.control.TreeTableView.TreeTableViewSelectionModel, javafx.scene.control.MultipleSelectionModelBase
        public void focus(int row) {
            focus(row, null);
        }

        private void focus(int row, TreeTableColumn<S, ?> column) {
            focus(new TreeTablePosition<>(getTreeTableView(), row, column));
        }

        private void focus(TreeTablePosition<S, ?> pos) {
            if (getTreeTableView().getFocusModel() == null) {
                return;
            }
            getTreeTableView().getFocusModel().focus(pos.getRow(), (TreeTableColumn) pos.getTableColumn());
            getTreeTableView().notifyAccessibleAttributeChanged(AccessibleAttribute.FOCUS_ITEM);
        }

        @Override // javafx.scene.control.TreeTableView.TreeTableViewSelectionModel, javafx.scene.control.MultipleSelectionModelBase
        public int getFocusedIndex() {
            return getFocusedCell().getRow();
        }

        private TreeTablePosition<S, ?> getFocusedCell() {
            if (this.treeTableView.getFocusModel() == null) {
                return new TreeTablePosition<>(this.treeTableView, -1, null);
            }
            return this.treeTableView.getFocusModel().getFocusedCell();
        }

        private int getRowCount() {
            return this.treeTableView.getExpandedItemCount();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void handleSelectedCellsListChangeEvent(ListChangeListener.Change<? extends TreeTablePosition<S, ?>> c2) {
            boolean fireChangeEvent;
            List<Integer> newlySelectedRows = new ArrayList<>();
            List<Integer> newlyUnselectedRows = new ArrayList<>();
            while (c2.next()) {
                if (c2.wasRemoved()) {
                    List<? extends TreeTablePosition<S, ?>> removed = c2.getRemoved();
                    for (int i2 = 0; i2 < removed.size(); i2++) {
                        TreeTablePosition<S, ?> tp = removed.get(i2);
                        int row = tp.getRow();
                        if (this.selectedIndices.get(row)) {
                            this.selectedIndices.clear(row);
                            newlyUnselectedRows.add(Integer.valueOf(row));
                        }
                    }
                }
                if (c2.wasAdded()) {
                    List<? extends TreeTablePosition<S, ?>> added = c2.getAddedSubList();
                    for (int i3 = 0; i3 < added.size(); i3++) {
                        TreeTablePosition<S, ?> tp2 = added.get(i3);
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
            if (c2.wasReplaced()) {
                int removedSize = c2.getRemovedSize();
                int addedSize = c2.getAddedSize();
                if (removedSize != addedSize) {
                    fireChangeEvent = true;
                } else {
                    int i4 = 0;
                    while (true) {
                        if (i4 < removedSize) {
                            TreeItem<S> removedTreeItem = c2.getRemoved().get(i4).getTreeItem();
                            if (removedTreeItem != null) {
                                TreeItem<S> addedTreeItem = c2.getAddedSubList().get(i4).getTreeItem();
                                if (!removedTreeItem.equals(addedTreeItem)) {
                                    fireChangeEvent = true;
                                    break;
                                }
                            }
                            i4++;
                        } else {
                            fireChangeEvent = false;
                            break;
                        }
                    }
                }
            } else {
                fireChangeEvent = true;
            }
            if (fireChangeEvent) {
                if (this.treeTableView.isSortingInProgress()) {
                    this.selectedItems.callObservers(new MappingChange(c2, f2 -> {
                        return f2.getTreeItem();
                    }, this.selectedItems));
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

    /* loaded from: jfxrt.jar:javafx/scene/control/TreeTableView$TreeTableViewFocusModel.class */
    public static class TreeTableViewFocusModel<S> extends TableFocusModel<TreeItem<S>, TreeTableColumn<S, ?>> {
        private final TreeTableView<S> treeTableView;
        private final TreeTablePosition EMPTY_CELL;
        private final ChangeListener<TreeItem<S>> rootPropertyListener = (observable, oldValue, newValue) -> {
            updateTreeEventListener(oldValue, newValue);
        };
        private final WeakChangeListener<TreeItem<S>> weakRootPropertyListener = new WeakChangeListener<>(this.rootPropertyListener);
        private EventHandler<TreeItem.TreeModificationEvent<S>> treeItemListener = new EventHandler<TreeItem.TreeModificationEvent<S>>() { // from class: javafx.scene.control.TreeTableView.TreeTableViewFocusModel.1
            @Override // javafx.event.EventHandler
            public void handle(TreeItem.TreeModificationEvent<S> e2) {
                if (TreeTableViewFocusModel.this.getFocusedIndex() == -1) {
                    return;
                }
                int shift = 0;
                if (e2.getChange() != null) {
                    e2.getChange().next();
                }
                do {
                    int row = TreeTableViewFocusModel.this.treeTableView.getRow(e2.getTreeItem());
                    if (e2.wasExpanded()) {
                        if (row < TreeTableViewFocusModel.this.getFocusedIndex()) {
                            shift += e2.getTreeItem().getExpandedDescendentCount(false) - 1;
                        }
                    } else if (e2.wasCollapsed()) {
                        if (row < TreeTableViewFocusModel.this.getFocusedIndex()) {
                            shift += (-e2.getTreeItem().previousExpandedDescendentCount) + 1;
                        }
                    } else if (e2.wasAdded()) {
                        TreeItem<S> eventTreeItem = e2.getTreeItem();
                        if (eventTreeItem.isExpanded()) {
                            for (int i2 = 0; i2 < e2.getAddedChildren().size(); i2++) {
                                TreeItem<S> item = e2.getAddedChildren().get(i2);
                                int row2 = TreeTableViewFocusModel.this.treeTableView.getRow(item);
                                if (item != null && row2 <= TreeTableViewFocusModel.this.getFocusedIndex()) {
                                    shift += item.getExpandedDescendentCount(false);
                                }
                            }
                        }
                    } else if (e2.wasRemoved()) {
                        int row3 = row + e2.getFrom() + 1;
                        for (int i3 = 0; i3 < e2.getRemovedChildren().size(); i3++) {
                            TreeItem<S> item2 = e2.getRemovedChildren().get(i3);
                            if (item2 != null && item2.equals(TreeTableViewFocusModel.this.getFocusedItem())) {
                                TreeTableViewFocusModel.this.focus(Math.max(0, TreeTableViewFocusModel.this.getFocusedIndex() - 1));
                                return;
                            }
                        }
                        if (row3 <= TreeTableViewFocusModel.this.getFocusedIndex()) {
                            shift += e2.getTreeItem().isExpanded() ? -e2.getRemovedSize() : 0;
                        }
                    }
                    if (e2.getChange() == null) {
                        break;
                    }
                } while (e2.getChange().next());
                if (shift != 0) {
                    TreeTablePosition<S, ?> focusedCell = TreeTableViewFocusModel.this.getFocusedCell();
                    int newFocus = focusedCell.getRow() + shift;
                    if (newFocus >= 0) {
                        Platform.runLater(() -> {
                            TreeTableViewFocusModel.this.focus(newFocus, (TreeTableColumn) focusedCell.getTableColumn());
                        });
                    }
                }
            }
        };
        private WeakEventHandler<TreeItem.TreeModificationEvent<S>> weakTreeItemListener;
        private ReadOnlyObjectWrapper<TreeTablePosition<S, ?>> focusedCell;

        public TreeTableViewFocusModel(TreeTableView<S> treeTableView) {
            if (treeTableView == null) {
                throw new NullPointerException("TableView can not be null");
            }
            this.treeTableView = treeTableView;
            this.EMPTY_CELL = new TreeTablePosition(treeTableView, -1, null);
            this.treeTableView.rootProperty().addListener(this.weakRootPropertyListener);
            updateTreeEventListener(null, treeTableView.getRoot());
            int focusRow = getItemCount() > 0 ? 0 : -1;
            TreeTablePosition<S, ?> pos = new TreeTablePosition<>(treeTableView, focusRow, null);
            setFocusedCell(pos);
            treeTableView.showRootProperty().addListener(o2 -> {
                if (isFocused(0)) {
                    focus(-1);
                    focus(0);
                }
            });
        }

        private void updateTreeEventListener(TreeItem<S> oldRoot, TreeItem<S> newRoot) {
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
            return this.treeTableView.getExpandedItemCount();
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // javafx.scene.control.FocusModel
        public TreeItem<S> getModelItem(int index) {
            if (index < 0 || index >= getItemCount()) {
                return null;
            }
            return this.treeTableView.getTreeItem(index);
        }

        public final ReadOnlyObjectProperty<TreeTablePosition<S, ?>> focusedCellProperty() {
            return focusedCellPropertyImpl().getReadOnlyProperty();
        }

        private void setFocusedCell(TreeTablePosition<S, ?> value) {
            focusedCellPropertyImpl().set(value);
        }

        public final TreeTablePosition<S, ?> getFocusedCell() {
            return this.focusedCell == null ? this.EMPTY_CELL : this.focusedCell.get();
        }

        private ReadOnlyObjectWrapper<TreeTablePosition<S, ?>> focusedCellPropertyImpl() {
            if (this.focusedCell == null) {
                this.focusedCell = new ReadOnlyObjectWrapper<TreeTablePosition<S, ?>>(this.EMPTY_CELL) { // from class: javafx.scene.control.TreeTableView.TreeTableViewFocusModel.2
                    private TreeTablePosition<S, ?> old;

                    @Override // javafx.beans.property.ObjectPropertyBase
                    protected void invalidated() {
                        if (get() == null) {
                            return;
                        }
                        if (this.old == null || !this.old.equals(get())) {
                            TreeTableViewFocusModel.this.setFocusedIndex(get().getRow());
                            TreeTableViewFocusModel.this.setFocusedItem(TreeTableViewFocusModel.this.getModelItem(getValue2().getRow()));
                            this.old = get();
                        }
                    }

                    @Override // javafx.beans.property.SimpleObjectProperty, javafx.beans.property.ReadOnlyProperty
                    public Object getBean() {
                        return TreeTableViewFocusModel.this;
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
        public void focus(int row, TreeTableColumn<S, ?> column) {
            if (row < 0 || row >= getItemCount()) {
                setFocusedCell(this.EMPTY_CELL);
                return;
            }
            TreeTablePosition<S, ?> oldFocusCell = getFocusedCell();
            TreeTablePosition<S, ?> newFocusCell = new TreeTablePosition<>(this.treeTableView, row, column);
            setFocusedCell(newFocusCell);
            if (newFocusCell.equals(oldFocusCell)) {
                setFocusedIndex(row);
                setFocusedItem(getModelItem(row));
            }
        }

        public void focus(TreeTablePosition<S, ?> pos) {
            if (pos == null) {
                return;
            }
            focus(pos.getRow(), (TreeTableColumn) pos.getTableColumn());
        }

        @Override // javafx.scene.control.TableFocusModel
        public boolean isFocused(int row, TreeTableColumn<S, ?> column) {
            if (row < 0 || row >= getItemCount()) {
                return false;
            }
            TreeTablePosition<S, ?> cell = getFocusedCell();
            boolean columnMatch = column == null || column.equals(cell.getTableColumn());
            return cell.getRow() == row && columnMatch;
        }

        @Override // javafx.scene.control.FocusModel
        public void focus(int index) {
            if (((TreeTableView) this.treeTableView).expandedItemCountDirty) {
                this.treeTableView.updateExpandedItemCount(this.treeTableView.getRoot());
            }
            if (index < 0 || index >= getItemCount()) {
                setFocusedCell(this.EMPTY_CELL);
            } else {
                setFocusedCell(new TreeTablePosition<>(this.treeTableView, index, null));
            }
        }

        @Override // javafx.scene.control.TableFocusModel
        public void focusAboveCell() {
            TreeTablePosition<S, ?> cell = getFocusedCell();
            if (getFocusedIndex() == -1) {
                focus(getItemCount() - 1, (TreeTableColumn) cell.getTableColumn());
            } else if (getFocusedIndex() > 0) {
                focus(getFocusedIndex() - 1, (TreeTableColumn) cell.getTableColumn());
            }
        }

        @Override // javafx.scene.control.TableFocusModel
        public void focusBelowCell() {
            TreeTablePosition<S, ?> cell = getFocusedCell();
            if (getFocusedIndex() == -1) {
                focus(0, (TreeTableColumn) cell.getTableColumn());
            } else if (getFocusedIndex() != getItemCount() - 1) {
                focus(getFocusedIndex() + 1, (TreeTableColumn) cell.getTableColumn());
            }
        }

        @Override // javafx.scene.control.TableFocusModel
        public void focusLeftCell() {
            TreeTablePosition<S, ?> cell = getFocusedCell();
            if (cell.getColumn() <= 0) {
                return;
            }
            focus(cell.getRow(), (TreeTableColumn) getTableColumn(cell.getTableColumn(), -1));
        }

        @Override // javafx.scene.control.TableFocusModel
        public void focusRightCell() {
            TreeTablePosition<S, ?> cell = getFocusedCell();
            if (cell.getColumn() == getColumnCount() - 1) {
                return;
            }
            focus(cell.getRow(), (TreeTableColumn) getTableColumn(cell.getTableColumn(), 1));
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

        private int getColumnCount() {
            return this.treeTableView.getVisibleLeafColumns().size();
        }

        private TreeTableColumn<S, ?> getTableColumn(TreeTableColumn<S, ?> column, int offset) {
            int columnIndex = this.treeTableView.getVisibleLeafIndex(column);
            int newColumnIndex = columnIndex + offset;
            return this.treeTableView.getVisibleLeafColumn(newColumnIndex);
        }
    }
}
