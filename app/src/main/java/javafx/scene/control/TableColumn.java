package javafx.scene.control;

import com.sun.javafx.scene.control.skin.NestedTableColumnHeader;
import com.sun.javafx.scene.control.skin.TableColumnHeader;
import com.sun.javafx.scene.control.skin.TableHeaderRow;
import com.sun.javafx.scene.control.skin.TableViewSkin;
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

/* loaded from: jfxrt.jar:javafx/scene/control/TableColumn.class */
public class TableColumn<S, T> extends TableColumnBase<S, T> implements EventTarget {
    private static final EventType<?> EDIT_ANY_EVENT = new EventType<>(Event.ANY, "TABLE_COLUMN_EDIT");
    private static final EventType<?> EDIT_START_EVENT = new EventType<>(editAnyEvent(), "EDIT_START");
    private static final EventType<?> EDIT_CANCEL_EVENT = new EventType<>(editAnyEvent(), "EDIT_CANCEL");
    private static final EventType<?> EDIT_COMMIT_EVENT = new EventType<>(editAnyEvent(), "EDIT_COMMIT");
    public static final Callback<TableColumn<?, ?>, TableCell<?, ?>> DEFAULT_CELL_FACTORY = new Callback<TableColumn<?, ?>, TableCell<?, ?>>() { // from class: javafx.scene.control.TableColumn.1
        @Override // javafx.util.Callback
        public TableCell<?, ?> call(TableColumn<?, ?> param) {
            return new TableCell<Object, Object>() { // from class: javafx.scene.control.TableColumn.1.1
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
    private ListChangeListener<TableColumn<S, ?>> columnsListener;
    private WeakListChangeListener<TableColumn<S, ?>> weakColumnsListener;
    private final ObservableList<TableColumn<S, ?>> columns;
    private ReadOnlyObjectWrapper<TableView<S>> tableView;
    private ObjectProperty<Callback<CellDataFeatures<S, T>, ObservableValue<T>>> cellValueFactory;
    private final ObjectProperty<Callback<TableColumn<S, T>, TableCell<S, T>>> cellFactory;
    private ObjectProperty<SortType> sortType;
    private ObjectProperty<EventHandler<CellEditEvent<S, T>>> onEditStart;
    private ObjectProperty<EventHandler<CellEditEvent<S, T>>> onEditCommit;
    private ObjectProperty<EventHandler<CellEditEvent<S, T>>> onEditCancel;
    private static final String DEFAULT_STYLE_CLASS = "table-column";

    /* loaded from: jfxrt.jar:javafx/scene/control/TableColumn$SortType.class */
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

    public TableColumn() {
        this.DEFAULT_EDIT_COMMIT_HANDLER = t2 -> {
            int index = t2.getTablePosition().getRow();
            List<S> list = t2.getTableView().getItems();
            if (list == null || index < 0 || index >= list.size()) {
                return;
            }
            S rowData = list.get(index);
            ObservableValue<T> ov = getCellObservableValue((TableColumn<S, T>) rowData);
            if (ov instanceof WritableValue) {
                ((WritableValue) ov).setValue(t2.getNewValue());
            }
        };
        this.columnsListener = c2 -> {
            while (c2.next()) {
                for (TableColumn<S, ?> tc : c2.getRemoved()) {
                    if (!getColumns().contains(tc)) {
                        tc.setTableView(null);
                        tc.setParentColumn(null);
                    }
                }
                Iterator it = c2.getAddedSubList().iterator();
                while (it.hasNext()) {
                    ((TableColumn) it.next()).setTableView(getTableView());
                }
                updateColumnWidths();
            }
        };
        this.weakColumnsListener = new WeakListChangeListener<>(this.columnsListener);
        this.columns = FXCollections.observableArrayList();
        this.tableView = new ReadOnlyObjectWrapper<>(this, "tableView");
        this.cellFactory = new SimpleObjectProperty<Callback<TableColumn<S, T>, TableCell<S, T>>>(this, "cellFactory", DEFAULT_CELL_FACTORY) { // from class: javafx.scene.control.TableColumn.2
            @Override // javafx.beans.property.ObjectPropertyBase
            protected void invalidated() {
                TableView<S> table = TableColumn.this.getTableView();
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
        tableViewProperty().addListener(observable -> {
            for (TableColumn<S, ?> tc : getColumns()) {
                tc.setTableView(getTableView());
            }
        });
    }

    public TableColumn(String text) {
        this();
        setText(text);
    }

    public final ReadOnlyObjectProperty<TableView<S>> tableViewProperty() {
        return this.tableView.getReadOnlyProperty();
    }

    final void setTableView(TableView<S> value) {
        this.tableView.set(value);
    }

    public final TableView<S> getTableView() {
        return this.tableView.get();
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

    public final void setCellFactory(Callback<TableColumn<S, T>, TableCell<S, T>> value) {
        this.cellFactory.set(value);
    }

    public final Callback<TableColumn<S, T>, TableCell<S, T>> getCellFactory() {
        return this.cellFactory.get();
    }

    public final ObjectProperty<Callback<TableColumn<S, T>, TableCell<S, T>>> cellFactoryProperty() {
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
            this.onEditStart = new SimpleObjectProperty<EventHandler<CellEditEvent<S, T>>>(this, "onEditStart") { // from class: javafx.scene.control.TableColumn.3
                @Override // javafx.beans.property.ObjectPropertyBase
                protected void invalidated() {
                    TableColumn.this.eventHandlerManager.setEventHandler(TableColumn.editStartEvent(), get());
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
            this.onEditCommit = new SimpleObjectProperty<EventHandler<CellEditEvent<S, T>>>(this, "onEditCommit") { // from class: javafx.scene.control.TableColumn.4
                @Override // javafx.beans.property.ObjectPropertyBase
                protected void invalidated() {
                    TableColumn.this.eventHandlerManager.setEventHandler(TableColumn.editCommitEvent(), get());
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
            this.onEditCancel = new SimpleObjectProperty<EventHandler<CellEditEvent<S, T>>>(this, "onEditCancel") { // from class: javafx.scene.control.TableColumn.5
                @Override // javafx.beans.property.ObjectPropertyBase
                protected void invalidated() {
                    TableColumn.this.eventHandlerManager.setEventHandler(TableColumn.editCancelEvent(), get());
                }
            };
        }
        return this.onEditCancel;
    }

    @Override // javafx.scene.control.TableColumnBase
    public final ObservableList<TableColumn<S, ?>> getColumns() {
        return this.columns;
    }

    @Override // javafx.scene.control.TableColumnBase
    public final ObservableValue<T> getCellObservableValue(int index) {
        TableView<S> table;
        if (index < 0 || (table = getTableView()) == null || table.getItems() == null) {
            return null;
        }
        List<S> items = table.getItems();
        if (index >= items.size()) {
            return null;
        }
        S rowData = items.get(index);
        return getCellObservableValue((TableColumn<S, T>) rowData);
    }

    @Override // javafx.scene.control.TableColumnBase
    public final ObservableValue<T> getCellObservableValue(S item) {
        TableView<S> table;
        Callback<CellDataFeatures<S, T>, ObservableValue<T>> factory = getCellValueFactory();
        if (factory == null || (table = getTableView()) == null) {
            return null;
        }
        CellDataFeatures<S, T> cdf = new CellDataFeatures<>(table, this, item);
        return factory.call(cdf);
    }

    @Override // javafx.css.Styleable
    public String getTypeSelector() {
        return "TableColumn";
    }

    @Override // javafx.css.Styleable
    public Styleable getStyleableParent() {
        return getTableView();
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
        if (!(getTableView().getSkin() instanceof TableViewSkin)) {
            return null;
        }
        TableViewSkin<?> skin = (TableViewSkin) getTableView().getSkin();
        TableHeaderRow tableHeader = skin.getTableHeaderRow();
        NestedTableColumnHeader rootHeader = tableHeader.getRootHeader();
        return scan(rootHeader);
    }

    private TableColumnHeader scan(TableColumnHeader header) {
        if (equals(header.getTableColumn())) {
            return header;
        }
        if (header instanceof NestedTableColumnHeader) {
            NestedTableColumnHeader parent = (NestedTableColumnHeader) header;
            for (int i2 = 0; i2 < parent.getColumnHeaders().size(); i2++) {
                TableColumnHeader result = scan(parent.getColumnHeaders().get(i2));
                if (result != null) {
                    return result;
                }
            }
            return null;
        }
        return null;
    }

    /* loaded from: jfxrt.jar:javafx/scene/control/TableColumn$CellDataFeatures.class */
    public static class CellDataFeatures<S, T> {
        private final TableView<S> tableView;
        private final TableColumn<S, T> tableColumn;
        private final S value;

        public CellDataFeatures(TableView<S> tableView, TableColumn<S, T> tableColumn, S value) {
            this.tableView = tableView;
            this.tableColumn = tableColumn;
            this.value = value;
        }

        public S getValue() {
            return this.value;
        }

        public TableColumn<S, T> getTableColumn() {
            return this.tableColumn;
        }

        public TableView<S> getTableView() {
            return this.tableView;
        }
    }

    /* loaded from: jfxrt.jar:javafx/scene/control/TableColumn$CellEditEvent.class */
    public static class CellEditEvent<S, T> extends Event {
        private static final long serialVersionUID = -609964441682677579L;
        public static final EventType<?> ANY = TableColumn.EDIT_ANY_EVENT;
        private final T newValue;
        private final transient TablePosition<S, T> pos;

        public CellEditEvent(TableView<S> table, TablePosition<S, T> pos, EventType<CellEditEvent<S, T>> eventType, T newValue) {
            super(table, Event.NULL_SOURCE_TARGET, eventType);
            if (table == null) {
                throw new NullPointerException("TableView can not be null");
            }
            this.pos = pos;
            this.newValue = newValue;
        }

        public TableView<S> getTableView() {
            return this.pos.getTableView();
        }

        public TableColumn<S, T> getTableColumn() {
            return this.pos.getTableColumn();
        }

        public TablePosition<S, T> getTablePosition() {
            return this.pos;
        }

        public T getNewValue() {
            return this.newValue;
        }

        public T getOldValue() {
            S rowData = getRowValue();
            if (rowData == null || this.pos.getTableColumn() == null) {
                return null;
            }
            return this.pos.getTableColumn().getCellData((TableColumn<S, T>) rowData);
        }

        public S getRowValue() {
            int row;
            List<S> items = getTableView().getItems();
            if (items != null && (row = this.pos.getRow()) >= 0 && row < items.size()) {
                return items.get(row);
            }
            return null;
        }
    }
}
