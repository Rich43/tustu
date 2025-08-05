package javafx.scene.control;

import com.sun.javafx.beans.IDProperty;
import com.sun.javafx.event.EventHandlerManager;
import com.sun.javafx.scene.control.ControlAcceleratorSupport;
import com.sun.javafx.scene.control.skin.Utils;
import com.sun.media.jfxmedia.MetadataParser;
import com.sun.org.apache.xalan.internal.templates.Constants;
import java.lang.ref.WeakReference;
import java.text.Collator;
import java.util.Comparator;
import java.util.HashMap;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.beans.property.ReadOnlyDoubleWrapper;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.collections.ObservableSet;
import javafx.css.PseudoClass;
import javafx.css.Styleable;
import javafx.event.Event;
import javafx.event.EventDispatchChain;
import javafx.event.EventHandler;
import javafx.event.EventTarget;
import javafx.event.EventType;
import javafx.scene.Node;
import javax.swing.JTree;

@IDProperty("id")
/* loaded from: jfxrt.jar:javafx/scene/control/TableColumnBase.class */
public abstract class TableColumnBase<S, T> implements EventTarget, Styleable {
    static final double DEFAULT_WIDTH = 80.0d;
    static final double DEFAULT_MIN_WIDTH = 10.0d;
    static final double DEFAULT_MAX_WIDTH = 5000.0d;
    final EventHandlerManager eventHandlerManager;
    private StringProperty text;
    private BooleanProperty visible;
    private ReadOnlyObjectWrapper<TableColumnBase<S, ?>> parentColumn;
    private ObjectProperty<ContextMenu> contextMenu;
    private StringProperty id;
    private StringProperty style;
    private final ObservableList<String> styleClass;
    private ObjectProperty<Node> graphic;
    private ObjectProperty<Node> sortNode;
    private ReadOnlyDoubleWrapper width;
    private DoubleProperty minWidth;
    private final DoubleProperty prefWidth;
    private DoubleProperty maxWidth;
    private BooleanProperty resizable;
    private BooleanProperty sortable;
    private BooleanProperty reorderable;
    private BooleanProperty fixed;
    private ObjectProperty<Comparator<T>> comparator;
    private BooleanProperty editable;
    private ObservableMap<Object, Object> properties;
    public static final Comparator DEFAULT_COMPARATOR = (obj1, obj2) -> {
        if (obj1 == null && obj2 == null) {
            return 0;
        }
        if (obj1 == null) {
            return -1;
        }
        if (obj2 == null) {
            return 1;
        }
        if ((obj1 instanceof Comparable) && (obj1.getClass() == obj2.getClass() || obj1.getClass().isAssignableFrom(obj2.getClass()))) {
            return obj1 instanceof String ? Collator.getInstance().compare(obj1, obj2) : ((Comparable) obj1).compareTo(obj2);
        }
        return Collator.getInstance().compare(obj1.toString(), obj2.toString());
    };
    private static final Object USER_DATA_KEY = new Object();

    public abstract ObservableList<? extends TableColumnBase<S, ?>> getColumns();

    public abstract ObservableValue<T> getCellObservableValue(int i2);

    public abstract ObservableValue<T> getCellObservableValue(S s2);

    protected TableColumnBase() {
        this("");
    }

    protected TableColumnBase(String text) {
        this.eventHandlerManager = new EventHandlerManager(this);
        this.text = new SimpleStringProperty(this, "text", "");
        this.visible = new SimpleBooleanProperty(this, "visible", true) { // from class: javafx.scene.control.TableColumnBase.1
            @Override // javafx.beans.property.BooleanPropertyBase
            protected void invalidated() {
                for (TableColumnBase<S, ?> col : TableColumnBase.this.getColumns()) {
                    col.setVisible(TableColumnBase.this.isVisible());
                }
            }
        };
        this.styleClass = FXCollections.observableArrayList();
        this.sortNode = new SimpleObjectProperty(this, "sortNode");
        this.width = new ReadOnlyDoubleWrapper(this, MetadataParser.WIDTH_TAG_NAME, DEFAULT_WIDTH);
        this.prefWidth = new SimpleDoubleProperty(this, "prefWidth", DEFAULT_WIDTH) { // from class: javafx.scene.control.TableColumnBase.4
            @Override // javafx.beans.property.DoublePropertyBase
            protected void invalidated() {
                TableColumnBase.this.impl_setWidth(TableColumnBase.this.getPrefWidth());
            }
        };
        this.maxWidth = new SimpleDoubleProperty(this, "maxWidth", DEFAULT_MAX_WIDTH) { // from class: javafx.scene.control.TableColumnBase.5
            @Override // javafx.beans.property.DoublePropertyBase
            protected void invalidated() {
                TableColumnBase.this.impl_setWidth(TableColumnBase.this.getWidth());
            }
        };
        setText(text);
    }

    public final StringProperty textProperty() {
        return this.text;
    }

    public final void setText(String value) {
        this.text.set(value);
    }

    public final String getText() {
        return this.text.get();
    }

    public final void setVisible(boolean value) {
        visibleProperty().set(value);
    }

    public final boolean isVisible() {
        return this.visible.get();
    }

    public final BooleanProperty visibleProperty() {
        return this.visible;
    }

    void setParentColumn(TableColumnBase<S, ?> value) {
        parentColumnPropertyImpl().set(value);
    }

    public final TableColumnBase<S, ?> getParentColumn() {
        if (this.parentColumn == null) {
            return null;
        }
        return this.parentColumn.get();
    }

    public final ReadOnlyObjectProperty<TableColumnBase<S, ?>> parentColumnProperty() {
        return parentColumnPropertyImpl().getReadOnlyProperty();
    }

    private ReadOnlyObjectWrapper<TableColumnBase<S, ?>> parentColumnPropertyImpl() {
        if (this.parentColumn == null) {
            this.parentColumn = new ReadOnlyObjectWrapper<>(this, "parentColumn");
        }
        return this.parentColumn;
    }

    public final void setContextMenu(ContextMenu value) {
        contextMenuProperty().set(value);
    }

    public final ContextMenu getContextMenu() {
        if (this.contextMenu == null) {
            return null;
        }
        return this.contextMenu.get();
    }

    public final ObjectProperty<ContextMenu> contextMenuProperty() {
        if (this.contextMenu == null) {
            this.contextMenu = new SimpleObjectProperty<ContextMenu>(this, "contextMenu") { // from class: javafx.scene.control.TableColumnBase.2
                private WeakReference<ContextMenu> contextMenuRef;

                @Override // javafx.beans.property.ObjectPropertyBase
                protected void invalidated() {
                    ContextMenu oldMenu = this.contextMenuRef == null ? null : this.contextMenuRef.get();
                    if (oldMenu != null) {
                        ControlAcceleratorSupport.removeAcceleratorsFromScene(oldMenu.getItems(), (TableColumnBase<?, ?>) TableColumnBase.this);
                    }
                    ContextMenu ctx = get();
                    this.contextMenuRef = new WeakReference<>(ctx);
                    if (ctx != null) {
                        ControlAcceleratorSupport.addAcceleratorsIntoScene(ctx.getItems(), (TableColumnBase<?, ?>) TableColumnBase.this);
                    }
                }
            };
        }
        return this.contextMenu;
    }

    public final void setId(String value) {
        idProperty().set(value);
    }

    @Override // javafx.css.Styleable
    public final String getId() {
        if (this.id == null) {
            return null;
        }
        return this.id.get();
    }

    public final StringProperty idProperty() {
        if (this.id == null) {
            this.id = new SimpleStringProperty(this, "id");
        }
        return this.id;
    }

    public final void setStyle(String value) {
        styleProperty().set(value);
    }

    @Override // javafx.css.Styleable
    public final String getStyle() {
        return this.style == null ? "" : this.style.get();
    }

    public final StringProperty styleProperty() {
        if (this.style == null) {
            this.style = new SimpleStringProperty(this, Constants.ATTRNAME_STYLE);
        }
        return this.style;
    }

    @Override // javafx.css.Styleable
    public ObservableList<String> getStyleClass() {
        return this.styleClass;
    }

    public final void setGraphic(Node value) {
        graphicProperty().set(value);
    }

    public final Node getGraphic() {
        if (this.graphic == null) {
            return null;
        }
        return this.graphic.get();
    }

    public final ObjectProperty<Node> graphicProperty() {
        if (this.graphic == null) {
            this.graphic = new SimpleObjectProperty(this, "graphic");
        }
        return this.graphic;
    }

    public final void setSortNode(Node value) {
        sortNodeProperty().set(value);
    }

    public final Node getSortNode() {
        return this.sortNode.get();
    }

    public final ObjectProperty<Node> sortNodeProperty() {
        return this.sortNode;
    }

    public final ReadOnlyDoubleProperty widthProperty() {
        return this.width.getReadOnlyProperty();
    }

    public final double getWidth() {
        return this.width.get();
    }

    void setWidth(double value) {
        this.width.set(value);
    }

    public final void setMinWidth(double value) {
        minWidthProperty().set(value);
    }

    public final double getMinWidth() {
        if (this.minWidth == null) {
            return 10.0d;
        }
        return this.minWidth.get();
    }

    public final DoubleProperty minWidthProperty() {
        if (this.minWidth == null) {
            this.minWidth = new SimpleDoubleProperty(this, "minWidth", 10.0d) { // from class: javafx.scene.control.TableColumnBase.3
                @Override // javafx.beans.property.DoublePropertyBase
                protected void invalidated() {
                    if (TableColumnBase.this.getMinWidth() < 0.0d) {
                        TableColumnBase.this.setMinWidth(0.0d);
                    }
                    TableColumnBase.this.impl_setWidth(TableColumnBase.this.getWidth());
                }
            };
        }
        return this.minWidth;
    }

    public final DoubleProperty prefWidthProperty() {
        return this.prefWidth;
    }

    public final void setPrefWidth(double value) {
        prefWidthProperty().set(value);
    }

    public final double getPrefWidth() {
        return this.prefWidth.get();
    }

    public final DoubleProperty maxWidthProperty() {
        return this.maxWidth;
    }

    public final void setMaxWidth(double value) {
        maxWidthProperty().set(value);
    }

    public final double getMaxWidth() {
        return this.maxWidth.get();
    }

    public final BooleanProperty resizableProperty() {
        if (this.resizable == null) {
            this.resizable = new SimpleBooleanProperty(this, "resizable", true);
        }
        return this.resizable;
    }

    public final void setResizable(boolean value) {
        resizableProperty().set(value);
    }

    public final boolean isResizable() {
        if (this.resizable == null) {
            return true;
        }
        return this.resizable.get();
    }

    public final BooleanProperty sortableProperty() {
        if (this.sortable == null) {
            this.sortable = new SimpleBooleanProperty(this, "sortable", true);
        }
        return this.sortable;
    }

    public final void setSortable(boolean value) {
        sortableProperty().set(value);
    }

    public final boolean isSortable() {
        if (this.sortable == null) {
            return true;
        }
        return this.sortable.get();
    }

    @Deprecated
    public final BooleanProperty impl_reorderableProperty() {
        if (this.reorderable == null) {
            this.reorderable = new SimpleBooleanProperty(this, "reorderable", true);
        }
        return this.reorderable;
    }

    @Deprecated
    public final void impl_setReorderable(boolean value) {
        impl_reorderableProperty().set(value);
    }

    @Deprecated
    public final boolean impl_isReorderable() {
        if (this.reorderable == null) {
            return true;
        }
        return this.reorderable.get();
    }

    @Deprecated
    public final BooleanProperty impl_fixedProperty() {
        if (this.fixed == null) {
            this.fixed = new SimpleBooleanProperty(this, "fixed", false);
        }
        return this.fixed;
    }

    @Deprecated
    public final void impl_setFixed(boolean value) {
        impl_fixedProperty().set(value);
    }

    @Deprecated
    public final boolean impl_isFixed() {
        if (this.fixed == null) {
            return false;
        }
        return this.fixed.get();
    }

    public final ObjectProperty<Comparator<T>> comparatorProperty() {
        if (this.comparator == null) {
            this.comparator = new SimpleObjectProperty(this, "comparator", DEFAULT_COMPARATOR);
        }
        return this.comparator;
    }

    public final void setComparator(Comparator<T> value) {
        comparatorProperty().set(value);
    }

    public final Comparator<T> getComparator() {
        return this.comparator == null ? DEFAULT_COMPARATOR : this.comparator.get();
    }

    public final void setEditable(boolean value) {
        editableProperty().set(value);
    }

    public final boolean isEditable() {
        if (this.editable == null) {
            return true;
        }
        return this.editable.get();
    }

    public final BooleanProperty editableProperty() {
        if (this.editable == null) {
            this.editable = new SimpleBooleanProperty(this, JTree.EDITABLE_PROPERTY, true);
        }
        return this.editable;
    }

    public final ObservableMap<Object, Object> getProperties() {
        if (this.properties == null) {
            this.properties = FXCollections.observableMap(new HashMap());
        }
        return this.properties;
    }

    public boolean hasProperties() {
        return (this.properties == null || this.properties.isEmpty()) ? false : true;
    }

    public void setUserData(Object value) {
        getProperties().put(USER_DATA_KEY, value);
    }

    public Object getUserData() {
        return getProperties().get(USER_DATA_KEY);
    }

    public final T getCellData(int index) {
        ObservableValue<T> result = getCellObservableValue(index);
        if (result == null) {
            return null;
        }
        return result.getValue2();
    }

    public final T getCellData(S item) {
        ObservableValue<T> result = getCellObservableValue((TableColumnBase<S, T>) item);
        if (result == null) {
            return null;
        }
        return result.getValue2();
    }

    @Override // javafx.event.EventTarget
    public EventDispatchChain buildEventDispatchChain(EventDispatchChain tail) {
        return tail.prepend(this.eventHandlerManager);
    }

    public <E extends Event> void addEventHandler(EventType<E> eventType, EventHandler<E> eventHandler) {
        this.eventHandlerManager.addEventHandler(eventType, eventHandler);
    }

    public <E extends Event> void removeEventHandler(EventType<E> eventType, EventHandler<E> eventHandler) {
        this.eventHandlerManager.removeEventHandler(eventType, eventHandler);
    }

    @Deprecated
    public void impl_setWidth(double width) {
        setWidth(Utils.boundedSize(width, getMinWidth(), getMaxWidth()));
    }

    void updateColumnWidths() {
        if (!getColumns().isEmpty()) {
            double _minWidth = 0.0d;
            double _prefWidth = 0.0d;
            double _maxWidth = 0.0d;
            for (TableColumnBase<S, ?> col : getColumns()) {
                col.setParentColumn(this);
                _minWidth += col.getMinWidth();
                _prefWidth += col.getPrefWidth();
                _maxWidth += col.getMaxWidth();
            }
            setMinWidth(_minWidth);
            setPrefWidth(_prefWidth);
            setMaxWidth(_maxWidth);
        }
    }

    @Override // javafx.css.Styleable
    public final ObservableSet<PseudoClass> getPseudoClassStates() {
        return FXCollections.emptyObservableSet();
    }
}
