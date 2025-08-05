package javafx.scene.control;

import com.sun.javafx.scene.control.skin.TableViewSkinBase;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.beans.value.WritableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.WeakListChangeListener;
import javafx.css.CssMetaData;
import javafx.css.Styleable;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventTarget;
import javafx.event.EventType;
import javafx.scene.Node;
import javafx.util.Callback;

/* loaded from: jfxrt.jar:javafx/scene/control/TreeTableColumn.class */
public class TreeTableColumn<S, T> extends TableColumnBase<TreeItem<S>, T> implements EventTarget {
    private static final EventType<?> EDIT_ANY_EVENT = new EventType<>(Event.ANY, "TREE_TABLE_COLUMN_EDIT");
    private static final EventType<?> EDIT_START_EVENT = new EventType<>(editAnyEvent(), "EDIT_START");
    private static final EventType<?> EDIT_CANCEL_EVENT = new EventType<>(editAnyEvent(), "EDIT_CANCEL");
    private static final EventType<?> EDIT_COMMIT_EVENT = new EventType<>(editAnyEvent(), "EDIT_COMMIT");
    public static final Callback<TreeTableColumn<?, ?>, TreeTableCell<?, ?>> DEFAULT_CELL_FACTORY = new Callback<TreeTableColumn<?, ?>, TreeTableCell<?, ?>>() { // from class: javafx.scene.control.TreeTableColumn.1
        @Override // javafx.util.Callback
        public TreeTableCell<?, ?> call(TreeTableColumn<?, ?> param) {
            return new TreeTableCell() { // from class: javafx.scene.control.TreeTableColumn.1.1
                @Override // javafx.scene.control.Cell
                protected void updateItem(Object item, boolean empty) {
                    if (item == getItem()) {
                        return;
                    }
                    super.updateItem(item, empty);
                    if (item == null) {
                        super.setText(null);
                        super.setGraphic(null);
                    } else if (item instanceof Node) {
                        super.setText(null);
                        super.setGraphic((Node) item);
                    } else {
                        super.setText(item.toString());
                        super.setGraphic(null);
                    }
                }
            };
        }
    };
    private EventHandler<CellEditEvent<S, T>> DEFAULT_EDIT_COMMIT_HANDLER;
    private ListChangeListener<TreeTableColumn<S, ?>> columnsListener;
    private WeakListChangeListener<TreeTableColumn<S, ?>> weakColumnsListener;
    private final ObservableList<TreeTableColumn<S, ?>> columns;
    private ReadOnlyObjectWrapper<TreeTableView<S>> treeTableView;
    private ObjectProperty<Callback<CellDataFeatures<S, T>, ObservableValue<T>>> cellValueFactory;
    private final ObjectProperty<Callback<TreeTableColumn<S, T>, TreeTableCell<S, T>>> cellFactory;
    private ObjectProperty<SortType> sortType;
    private ObjectProperty<EventHandler<CellEditEvent<S, T>>> onEditStart;
    private ObjectProperty<EventHandler<CellEditEvent<S, T>>> onEditCommit;
    private ObjectProperty<EventHandler<CellEditEvent<S, T>>> onEditCancel;
    private static final String DEFAULT_STYLE_CLASS = "table-column";

    /* loaded from: jfxrt.jar:javafx/scene/control/TreeTableColumn$SortType.class */
    public enum SortType {
        ASCENDING,
        DESCENDING
    }

    public static <S, T> EventType<CellEditEvent<S, T>> editAnyEvent() {
        return (EventType<CellEditEvent<S, T>>) EDIT_ANY_EVENT;
    }

    public static <S, T> EventType<CellEditEvent<S, T>> editStartEvent() {
        return (EventType<CellEditEvent<S, T>>) EDIT_START_EVENT;
    }

    public static <S, T> EventType<CellEditEvent<S, T>> editCancelEvent() {
        return (EventType<CellEditEvent<S, T>>) EDIT_CANCEL_EVENT;
    }

    public static <S, T> EventType<CellEditEvent<S, T>> editCommitEvent() {
        return (EventType<CellEditEvent<S, T>>) EDIT_COMMIT_EVENT;
    }

    public TreeTableColumn() {
        this.DEFAULT_EDIT_COMMIT_HANDLER = t2 -> {
            ObservableValue<T> ov = getCellObservableValue((TreeItem) t2.getRowValue());
            if (ov instanceof WritableValue) {
                ((WritableValue) ov).setValue(t2.getNewValue());
            }
        };
        this.columnsListener = new ListChangeListener<TreeTableColumn<S, ?>>() { // from class: javafx.scene.control.TreeTableColumn.2
            @Override // javafx.collections.ListChangeListener
            public void onChanged(ListChangeListener.Change<? extends TreeTableColumn<S, ?>> c2) {
                while (c2.next()) {
                    for (TreeTableColumn<S, ?> tc : c2.getRemoved()) {
                        if (!TreeTableColumn.this.getColumns().contains(tc)) {
                            tc.setTreeTableView(null);
                            tc.setParentColumn(null);
                        }
                    }
                    Iterator<? extends TreeTableColumn<S, ?>> it = c2.getAddedSubList().iterator();
                    while (it.hasNext()) {
                        it.next().setTreeTableView(TreeTableColumn.this.getTreeTableView());
                    }
                    TreeTableColumn.this.updateColumnWidths();
                }
            }
        };
        this.weakColumnsListener = new WeakListChangeListener<>(this.columnsListener);
        this.columns = FXCollections.observableArrayList();
        this.treeTableView = new ReadOnlyObjectWrapper<>(this, "treeTableView");
        this.cellFactory = new SimpleObjectProperty<Callback<TreeTableColumn<S, T>, TreeTableCell<S, T>>>(this, "cellFactory", DEFAULT_CELL_FACTORY) { // from class: javafx.scene.control.TreeTableColumn.3
            @Override // javafx.beans.property.ObjectPropertyBase
            protected void invalidated() {
                TreeTableView<S> table = TreeTableColumn.this.getTreeTableView();
                if (table == null) {
                    return;
                }
                Map<Object, Object> properties = table.getProperties();
                if (properties.containsKey(TableViewSkinBase.RECREATE)) {
                    properties.remove(TableViewSkinBase.RECREATE);
                }
                properties.put(TableViewSkinBase.RECREATE, Boolean.TRUE);
            }
        };
        getStyleClass().add(DEFAULT_STYLE_CLASS);
        setOnEditCommit(this.DEFAULT_EDIT_COMMIT_HANDLER);
        getColumns().addListener(this.weakColumnsListener);
        treeTableViewProperty().addListener(observable -> {
            for (TreeTableColumn<S, ?> tc : getColumns()) {
                tc.setTreeTableView(getTreeTableView());
            }
        });
    }

    public TreeTableColumn(String text) {
        this();
        setText(text);
    }

    public final ReadOnlyObjectProperty<TreeTableView<S>> treeTableViewProperty() {
        return this.treeTableView.getReadOnlyProperty();
    }

    final void setTreeTableView(TreeTableView<S> value) {
        this.treeTableView.set(value);
    }

    public final TreeTableView<S> getTreeTableView() {
        return this.treeTableView.get();
    }

    public final void setCellValueFactory(Callback<CellDataFeatures<S, T>, ObservableValue<T>> value) {
        cellValueFactoryProperty().set(value);
    }

    public final Callback<CellDataFeatures<S, T>, ObservableValue<T>> getCellValueFactory() {
        if (this.cellValueFactory == null) {
            return null;
        }
        return this.cellValueFactory.get();
    }

    public final ObjectProperty<Callback<CellDataFeatures<S, T>, ObservableValue<T>>> cellValueFactoryProperty() {
        if (this.cellValueFactory == null) {
            this.cellValueFactory = new SimpleObjectProperty(this, "cellValueFactory");
        }
        return this.cellValueFactory;
    }

    public final void setCellFactory(Callback<TreeTableColumn<S, T>, TreeTableCell<S, T>> value) {
        this.cellFactory.set(value);
    }

    public final Callback<TreeTableColumn<S, T>, TreeTableCell<S, T>> getCellFactory() {
        return this.cellFactory.get();
    }

    public final ObjectProperty<Callback<TreeTableColumn<S, T>, TreeTableCell<S, T>>> cellFactoryProperty() {
        return this.cellFactory;
    }

    public final ObjectProperty<SortType> sortTypeProperty() {
        if (this.sortType == null) {
            this.sortType = new SimpleObjectProperty(this, "sortType", SortType.ASCENDING);
        }
        return this.sortType;
    }

    public final void setSortType(SortType value) {
        sortTypeProperty().set(value);
    }

    public final SortType getSortType() {
        return this.sortType == null ? SortType.ASCENDING : this.sortType.get();
    }

    public final void setOnEditStart(EventHandler<CellEditEvent<S, T>> value) {
        onEditStartProperty().set(value);
    }

    public final EventHandler<CellEditEvent<S, T>> getOnEditStart() {
        if (this.onEditStart == null) {
            return null;
        }
        return this.onEditStart.get();
    }

    public final ObjectProperty<EventHandler<CellEditEvent<S, T>>> onEditStartProperty() {
        if (this.onEditStart == null) {
            this.onEditStart = new SimpleObjectProperty<EventHandler<CellEditEvent<S, T>>>(this, "onEditStart") { // from class: javafx.scene.control.TreeTableColumn.4
                @Override // javafx.beans.property.ObjectPropertyBase
                protected void invalidated() {
                    TreeTableColumn.this.eventHandlerManager.setEventHandler(TreeTableColumn.editStartEvent(), get());
                }
            };
        }
        return this.onEditStart;
    }

    public final void setOnEditCommit(EventHandler<CellEditEvent<S, T>> value) {
        onEditCommitProperty().set(value);
    }

    public final EventHandler<CellEditEvent<S, T>> getOnEditCommit() {
        if (this.onEditCommit == null) {
            return null;
        }
        return this.onEditCommit.get();
    }

    public final ObjectProperty<EventHandler<CellEditEvent<S, T>>> onEditCommitProperty() {
        if (this.onEditCommit == null) {
            this.onEditCommit = new SimpleObjectProperty<EventHandler<CellEditEvent<S, T>>>(this, "onEditCommit") { // from class: javafx.scene.control.TreeTableColumn.5
                @Override // javafx.beans.property.ObjectPropertyBase
                protected void invalidated() {
                    TreeTableColumn.this.eventHandlerManager.setEventHandler(TreeTableColumn.editCommitEvent(), get());
                }
            };
        }
        return this.onEditCommit;
    }

    public final void setOnEditCancel(EventHandler<CellEditEvent<S, T>> value) {
        onEditCancelProperty().set(value);
    }

    public final EventHandler<CellEditEvent<S, T>> getOnEditCancel() {
        if (this.onEditCancel == null) {
            return null;
        }
        return this.onEditCancel.get();
    }

    public final ObjectProperty<EventHandler<CellEditEvent<S, T>>> onEditCancelProperty() {
        if (this.onEditCancel == null) {
            this.onEditCancel = new SimpleObjectProperty<EventHandler<CellEditEvent<S, T>>>(this, "onEditCancel") { // from class: javafx.scene.control.TreeTableColumn.6
                @Override // javafx.beans.property.ObjectPropertyBase
                protected void invalidated() {
                    TreeTableColumn.this.eventHandlerManager.setEventHandler(TreeTableColumn.editCancelEvent(), get());
                }
            };
        }
        return this.onEditCancel;
    }

    @Override // javafx.scene.control.TableColumnBase
    public final ObservableList<TreeTableColumn<S, ?>> getColumns() {
        return this.columns;
    }

    @Override // javafx.scene.control.TableColumnBase
    public final ObservableValue<T> getCellObservableValue(int index) {
        TreeTableView<S> table;
        if (index < 0 || (table = getTreeTableView()) == null || index >= table.getExpandedItemCount()) {
            return null;
        }
        TreeItem<S> item = table.getTreeItem(index);
        return getCellObservableValue((TreeItem) item);
    }

    @Override // javafx.scene.control.TableColumnBase
    public final ObservableValue<T> getCellObservableValue(TreeItem<S> item) {
        TreeTableView<S> table;
        Callback<CellDataFeatures<S, T>, ObservableValue<T>> factory = getCellValueFactory();
        if (factory == null || (table = getTreeTableView()) == null) {
            return null;
        }
        CellDataFeatures<S, T> cdf = new CellDataFeatures<>(table, this, item);
        return factory.call(cdf);
    }

    @Override // javafx.css.Styleable
    public String getTypeSelector() {
        return "TreeTableColumn";
    }

    @Override // javafx.css.Styleable
    public Styleable getStyleableParent() {
        return getTreeTableView();
    }

    @Override // javafx.css.Styleable
    public List<CssMetaData<? extends Styleable, ?>> getCssMetaData() {
        return getClassCssMetaData();
    }

    public static List<CssMetaData<? extends Styleable, ?>> getClassCssMetaData() {
        return Collections.emptyList();
    }

    @Deprecated
    public Node impl_styleableGetNode() {
        return null;
    }

    /* loaded from: jfxrt.jar:javafx/scene/control/TreeTableColumn$CellDataFeatures.class */
    public static class CellDataFeatures<S, T> {
        private final TreeTableView<S> treeTableView;
        private final TreeTableColumn<S, T> tableColumn;
        private final TreeItem<S> value;

        public CellDataFeatures(TreeTableView<S> treeTableView, TreeTableColumn<S, T> tableColumn, TreeItem<S> value) {
            this.treeTableView = treeTableView;
            this.tableColumn = tableColumn;
            this.value = value;
        }

        public TreeItem<S> getValue() {
            return this.value;
        }

        public TreeTableColumn<S, T> getTreeTableColumn() {
            return this.tableColumn;
        }

        public TreeTableView<S> getTreeTableView() {
            return this.treeTableView;
        }
    }

    /* loaded from: jfxrt.jar:javafx/scene/control/TreeTableColumn$CellEditEvent.class */
    public static class CellEditEvent<S, T> extends Event {
        private static final long serialVersionUID = -609964441682677579L;
        public static final EventType<?> ANY = TreeTableColumn.EDIT_ANY_EVENT;
        private final T newValue;
        private final transient TreeTablePosition<S, T> pos;

        public CellEditEvent(TreeTableView<S> table, TreeTablePosition<S, T> pos, EventType<CellEditEvent<S, T>> eventType, T newValue) {
            super(table, Event.NULL_SOURCE_TARGET, eventType);
            if (table == null) {
                throw new NullPointerException("TableView can not be null");
            }
            this.pos = pos;
            this.newValue = newValue;
        }

        public TreeTableView<S> getTreeTableView() {
            return this.pos.getTreeTableView();
        }

        public TreeTableColumn<S, T> getTableColumn() {
            return this.pos.getTableColumn();
        }

        public TreeTablePosition<S, T> getTreeTablePosition() {
            return this.pos;
        }

        public T getNewValue() {
            return this.newValue;
        }

        public T getOldValue() {
            TreeItem<S> rowData = getRowValue();
            if (rowData == null || this.pos.getTableColumn() == null) {
                return null;
            }
            return this.pos.getTableColumn().getCellData((TreeTableColumn<S, T>) rowData);
        }

        public TreeItem<S> getRowValue() {
            TreeTableView<S> treeTable = getTreeTableView();
            int row = this.pos.getRow();
            if (row < 0 || row >= treeTable.getExpandedItemCount()) {
                return null;
            }
            return treeTable.getTreeItem(row);
        }
    }
}
