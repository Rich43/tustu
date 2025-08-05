package javafx.scene.control;

import com.sun.javafx.beans.IDProperty;
import com.sun.javafx.event.EventHandlerManager;
import com.sun.javafx.scene.control.ControlAcceleratorSupport;
import com.sun.org.apache.xalan.internal.templates.Constants;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import javafx.beans.DefaultProperty;
import javafx.beans.InvalidationListener;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.BooleanPropertyBase;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ObjectPropertyBase;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.ReadOnlyBooleanWrapper;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.collections.ObservableSet;
import javafx.css.CssMetaData;
import javafx.css.PseudoClass;
import javafx.css.Styleable;
import javafx.event.Event;
import javafx.event.EventDispatchChain;
import javafx.event.EventHandler;
import javafx.event.EventTarget;
import javafx.event.EventType;
import javafx.scene.Node;
import javax.swing.JInternalFrame;
import javax.swing.text.AbstractDocument;

@DefaultProperty(AbstractDocument.ContentElementName)
@IDProperty("id")
/* loaded from: jfxrt.jar:javafx/scene/control/Tab.class */
public class Tab implements EventTarget, Styleable {
    private StringProperty id;
    private StringProperty style;
    private ReadOnlyBooleanWrapper selected;
    private ReadOnlyObjectWrapper<TabPane> tabPane;
    private final InvalidationListener parentDisabledChangedListener;
    private StringProperty text;
    private ObjectProperty<Node> graphic;
    private ObjectProperty<Node> content;
    private ObjectProperty<ContextMenu> contextMenu;
    private BooleanProperty closable;
    private ObjectProperty<EventHandler<Event>> onSelectionChanged;
    private ObjectProperty<EventHandler<Event>> onClosed;
    private ObjectProperty<Tooltip> tooltip;
    private final ObservableList<String> styleClass;
    private BooleanProperty disable;
    private ReadOnlyBooleanWrapper disabled;
    private ObjectProperty<EventHandler<Event>> onCloseRequest;
    private ObservableMap<Object, Object> properties;
    private final EventHandlerManager eventHandlerManager;
    private static final String DEFAULT_STYLE_CLASS = "tab";
    public static final EventType<Event> SELECTION_CHANGED_EVENT = new EventType<>(Event.ANY, "SELECTION_CHANGED_EVENT");
    public static final EventType<Event> CLOSED_EVENT = new EventType<>(Event.ANY, "TAB_CLOSED");
    public static final EventType<Event> TAB_CLOSE_REQUEST_EVENT = new EventType<>(Event.ANY, "TAB_CLOSE_REQUEST_EVENT");
    private static final Object USER_DATA_KEY = new Object();

    public Tab() {
        this(null);
    }

    public Tab(String text) {
        this(text, null);
    }

    public Tab(String text, Node content) {
        this.parentDisabledChangedListener = valueModel -> {
            updateDisabled();
        };
        this.styleClass = FXCollections.observableArrayList();
        this.eventHandlerManager = new EventHandlerManager(this);
        setText(text);
        setContent(content);
        this.styleClass.addAll(DEFAULT_STYLE_CLASS);
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
        if (this.style == null) {
            return null;
        }
        return this.style.get();
    }

    public final StringProperty styleProperty() {
        if (this.style == null) {
            this.style = new SimpleStringProperty(this, Constants.ATTRNAME_STYLE);
        }
        return this.style;
    }

    final void setSelected(boolean value) {
        selectedPropertyImpl().set(value);
    }

    public final boolean isSelected() {
        if (this.selected == null) {
            return false;
        }
        return this.selected.get();
    }

    public final ReadOnlyBooleanProperty selectedProperty() {
        return selectedPropertyImpl().getReadOnlyProperty();
    }

    private ReadOnlyBooleanWrapper selectedPropertyImpl() {
        if (this.selected == null) {
            this.selected = new ReadOnlyBooleanWrapper() { // from class: javafx.scene.control.Tab.1
                @Override // javafx.beans.property.BooleanPropertyBase
                protected void invalidated() {
                    if (Tab.this.getOnSelectionChanged() != null) {
                        Event.fireEvent(Tab.this, new Event(Tab.SELECTION_CHANGED_EVENT));
                    }
                }

                @Override // javafx.beans.property.SimpleBooleanProperty, javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return Tab.this;
                }

                @Override // javafx.beans.property.SimpleBooleanProperty, javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return JInternalFrame.IS_SELECTED_PROPERTY;
                }
            };
        }
        return this.selected;
    }

    final void setTabPane(TabPane value) {
        tabPanePropertyImpl().set(value);
    }

    public final TabPane getTabPane() {
        if (this.tabPane == null) {
            return null;
        }
        return this.tabPane.get();
    }

    public final ReadOnlyObjectProperty<TabPane> tabPaneProperty() {
        return tabPanePropertyImpl().getReadOnlyProperty();
    }

    private ReadOnlyObjectWrapper<TabPane> tabPanePropertyImpl() {
        if (this.tabPane == null) {
            this.tabPane = new ReadOnlyObjectWrapper<TabPane>(this, "tabPane") { // from class: javafx.scene.control.Tab.2
                private WeakReference<TabPane> oldParent;

                @Override // javafx.beans.property.ObjectPropertyBase
                protected void invalidated() {
                    if (this.oldParent != null && this.oldParent.get() != null) {
                        this.oldParent.get().disabledProperty().removeListener(Tab.this.parentDisabledChangedListener);
                    }
                    Tab.this.updateDisabled();
                    TabPane newParent = get();
                    if (newParent != null) {
                        newParent.disabledProperty().addListener(Tab.this.parentDisabledChangedListener);
                    }
                    this.oldParent = new WeakReference<>(newParent);
                    super.invalidated();
                }
            };
        }
        return this.tabPane;
    }

    public final void setText(String value) {
        textProperty().set(value);
    }

    public final String getText() {
        if (this.text == null) {
            return null;
        }
        return this.text.get();
    }

    public final StringProperty textProperty() {
        if (this.text == null) {
            this.text = new SimpleStringProperty(this, "text");
        }
        return this.text;
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

    public final void setContent(Node value) {
        contentProperty().set(value);
    }

    public final Node getContent() {
        if (this.content == null) {
            return null;
        }
        return this.content.get();
    }

    public final ObjectProperty<Node> contentProperty() {
        if (this.content == null) {
            this.content = new SimpleObjectProperty(this, AbstractDocument.ContentElementName);
        }
        return this.content;
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
            this.contextMenu = new SimpleObjectProperty<ContextMenu>(this, "contextMenu") { // from class: javafx.scene.control.Tab.3
                private WeakReference<ContextMenu> contextMenuRef;

                @Override // javafx.beans.property.ObjectPropertyBase
                protected void invalidated() {
                    ContextMenu oldMenu = this.contextMenuRef == null ? null : this.contextMenuRef.get();
                    if (oldMenu != null) {
                        ControlAcceleratorSupport.removeAcceleratorsFromScene(oldMenu.getItems(), Tab.this);
                    }
                    ContextMenu ctx = get();
                    this.contextMenuRef = new WeakReference<>(ctx);
                    if (ctx != null) {
                        ControlAcceleratorSupport.addAcceleratorsIntoScene(ctx.getItems(), Tab.this);
                    }
                }
            };
        }
        return this.contextMenu;
    }

    public final void setClosable(boolean value) {
        closableProperty().set(value);
    }

    public final boolean isClosable() {
        if (this.closable == null) {
            return true;
        }
        return this.closable.get();
    }

    public final BooleanProperty closableProperty() {
        if (this.closable == null) {
            this.closable = new SimpleBooleanProperty(this, "closable", true);
        }
        return this.closable;
    }

    public final void setOnSelectionChanged(EventHandler<Event> value) {
        onSelectionChangedProperty().set(value);
    }

    public final EventHandler<Event> getOnSelectionChanged() {
        if (this.onSelectionChanged == null) {
            return null;
        }
        return this.onSelectionChanged.get();
    }

    public final ObjectProperty<EventHandler<Event>> onSelectionChangedProperty() {
        if (this.onSelectionChanged == null) {
            this.onSelectionChanged = new ObjectPropertyBase<EventHandler<Event>>() { // from class: javafx.scene.control.Tab.4
                @Override // javafx.beans.property.ObjectPropertyBase
                protected void invalidated() {
                    Tab.this.setEventHandler(Tab.SELECTION_CHANGED_EVENT, get());
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return Tab.this;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return "onSelectionChanged";
                }
            };
        }
        return this.onSelectionChanged;
    }

    public final void setOnClosed(EventHandler<Event> value) {
        onClosedProperty().set(value);
    }

    public final EventHandler<Event> getOnClosed() {
        if (this.onClosed == null) {
            return null;
        }
        return this.onClosed.get();
    }

    public final ObjectProperty<EventHandler<Event>> onClosedProperty() {
        if (this.onClosed == null) {
            this.onClosed = new ObjectPropertyBase<EventHandler<Event>>() { // from class: javafx.scene.control.Tab.5
                @Override // javafx.beans.property.ObjectPropertyBase
                protected void invalidated() {
                    Tab.this.setEventHandler(Tab.CLOSED_EVENT, get());
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return Tab.this;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return "onClosed";
                }
            };
        }
        return this.onClosed;
    }

    public final void setTooltip(Tooltip value) {
        tooltipProperty().setValue(value);
    }

    public final Tooltip getTooltip() {
        if (this.tooltip == null) {
            return null;
        }
        return this.tooltip.getValue2();
    }

    public final ObjectProperty<Tooltip> tooltipProperty() {
        if (this.tooltip == null) {
            this.tooltip = new SimpleObjectProperty(this, "tooltip");
        }
        return this.tooltip;
    }

    public final void setDisable(boolean value) {
        disableProperty().set(value);
    }

    public final boolean isDisable() {
        if (this.disable == null) {
            return false;
        }
        return this.disable.get();
    }

    public final BooleanProperty disableProperty() {
        if (this.disable == null) {
            this.disable = new BooleanPropertyBase(false) { // from class: javafx.scene.control.Tab.6
                @Override // javafx.beans.property.BooleanPropertyBase
                protected void invalidated() {
                    Tab.this.updateDisabled();
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return Tab.this;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return "disable";
                }
            };
        }
        return this.disable;
    }

    private final void setDisabled(boolean value) {
        disabledPropertyImpl().set(value);
    }

    public final boolean isDisabled() {
        if (this.disabled == null) {
            return false;
        }
        return this.disabled.get();
    }

    public final ReadOnlyBooleanProperty disabledProperty() {
        return disabledPropertyImpl().getReadOnlyProperty();
    }

    private ReadOnlyBooleanWrapper disabledPropertyImpl() {
        if (this.disabled == null) {
            this.disabled = new ReadOnlyBooleanWrapper() { // from class: javafx.scene.control.Tab.7
                @Override // javafx.beans.property.SimpleBooleanProperty, javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return Tab.this;
                }

                @Override // javafx.beans.property.SimpleBooleanProperty, javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return "disabled";
                }
            };
        }
        return this.disabled;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateDisabled() {
        boolean disabled = isDisable() || (getTabPane() != null && getTabPane().isDisabled());
        setDisabled(disabled);
        Node content = getContent();
        if (content != null) {
            content.setDisable(disabled);
        }
    }

    public final ObjectProperty<EventHandler<Event>> onCloseRequestProperty() {
        if (this.onCloseRequest == null) {
            this.onCloseRequest = new ObjectPropertyBase<EventHandler<Event>>() { // from class: javafx.scene.control.Tab.8
                @Override // javafx.beans.property.ObjectPropertyBase
                protected void invalidated() {
                    Tab.this.setEventHandler(Tab.TAB_CLOSE_REQUEST_EVENT, get());
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return Tab.this;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return "onCloseRequest";
                }
            };
        }
        return this.onCloseRequest;
    }

    public EventHandler<Event> getOnCloseRequest() {
        if (this.onCloseRequest == null) {
            return null;
        }
        return this.onCloseRequest.get();
    }

    public void setOnCloseRequest(EventHandler<Event> value) {
        onCloseRequestProperty().set(value);
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

    @Override // javafx.css.Styleable
    public ObservableList<String> getStyleClass() {
        return this.styleClass;
    }

    @Override // javafx.event.EventTarget
    public EventDispatchChain buildEventDispatchChain(EventDispatchChain tail) {
        return tail.prepend(this.eventHandlerManager);
    }

    protected <E extends Event> void setEventHandler(EventType<E> eventType, EventHandler<E> eventHandler) {
        this.eventHandlerManager.setEventHandler(eventType, eventHandler);
    }

    Node lookup(String selector) {
        if (selector == null) {
            return null;
        }
        Node n2 = null;
        if (getContent() != null) {
            n2 = getContent().lookup(selector);
        }
        if (n2 == null && getGraphic() != null) {
            n2 = getGraphic().lookup(selector);
        }
        return n2;
    }

    List<Node> lookupAll(String selector) {
        List<Node> results = new ArrayList<>();
        if (getContent() != null) {
            Set set = getContent().lookupAll(selector);
            if (!set.isEmpty()) {
                results.addAll(set);
            }
        }
        if (getGraphic() != null) {
            Set set2 = getGraphic().lookupAll(selector);
            if (!set2.isEmpty()) {
                results.addAll(set2);
            }
        }
        return results;
    }

    @Override // javafx.css.Styleable
    public String getTypeSelector() {
        return "Tab";
    }

    @Override // javafx.css.Styleable
    public Styleable getStyleableParent() {
        return getTabPane();
    }

    @Override // javafx.css.Styleable
    public final ObservableSet<PseudoClass> getPseudoClassStates() {
        return FXCollections.emptyObservableSet();
    }

    @Override // javafx.css.Styleable
    public List<CssMetaData<? extends Styleable, ?>> getCssMetaData() {
        return getClassCssMetaData();
    }

    public static List<CssMetaData<? extends Styleable, ?>> getClassCssMetaData() {
        return Collections.emptyList();
    }
}
