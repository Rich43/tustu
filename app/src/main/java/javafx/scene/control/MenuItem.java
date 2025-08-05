package javafx.scene.control;

import com.sun.javafx.beans.IDProperty;
import com.sun.javafx.event.EventHandlerManager;
import com.sun.javafx.scene.control.skin.ContextMenuContent;
import com.sun.javafx.scene.control.skin.ContextMenuSkin;
import com.sun.org.apache.xalan.internal.templates.Constants;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ObjectPropertyBase;
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
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventDispatchChain;
import javafx.event.EventHandler;
import javafx.event.EventTarget;
import javafx.event.EventType;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.input.KeyCombination;

@IDProperty("id")
/* loaded from: jfxrt.jar:javafx/scene/control/MenuItem.class */
public class MenuItem implements EventTarget, Styleable {
    private final ObservableList<String> styleClass;
    final EventHandlerManager eventHandlerManager;
    private Object userData;
    private ObservableMap<Object, Object> properties;
    private StringProperty id;
    private StringProperty style;
    private ReadOnlyObjectWrapper<Menu> parentMenu;
    private ReadOnlyObjectWrapper<ContextMenu> parentPopup;
    private StringProperty text;
    private ObjectProperty<Node> graphic;
    private ObjectProperty<EventHandler<ActionEvent>> onAction;
    public static final EventType<Event> MENU_VALIDATION_EVENT = new EventType<>(Event.ANY, "MENU_VALIDATION_EVENT");
    private ObjectProperty<EventHandler<Event>> onMenuValidation;
    private BooleanProperty disable;
    private BooleanProperty visible;
    private ObjectProperty<KeyCombination> accelerator;
    private BooleanProperty mnemonicParsing;
    private static final String DEFAULT_STYLE_CLASS = "menu-item";

    public MenuItem() {
        this(null, null);
    }

    public MenuItem(String text) {
        this(text, null);
    }

    public MenuItem(String text, Node graphic) {
        this.styleClass = FXCollections.observableArrayList();
        this.eventHandlerManager = new EventHandlerManager(this);
        setText(text);
        setGraphic(graphic);
        this.styleClass.add(DEFAULT_STYLE_CLASS);
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

    protected final void setParentMenu(Menu value) {
        parentMenuPropertyImpl().set(value);
    }

    public final Menu getParentMenu() {
        if (this.parentMenu == null) {
            return null;
        }
        return this.parentMenu.get();
    }

    public final ReadOnlyObjectProperty<Menu> parentMenuProperty() {
        return parentMenuPropertyImpl().getReadOnlyProperty();
    }

    private ReadOnlyObjectWrapper<Menu> parentMenuPropertyImpl() {
        if (this.parentMenu == null) {
            this.parentMenu = new ReadOnlyObjectWrapper<>(this, "parentMenu");
        }
        return this.parentMenu;
    }

    protected final void setParentPopup(ContextMenu value) {
        parentPopupPropertyImpl().set(value);
    }

    public final ContextMenu getParentPopup() {
        if (this.parentPopup == null) {
            return null;
        }
        return this.parentPopup.get();
    }

    public final ReadOnlyObjectProperty<ContextMenu> parentPopupProperty() {
        return parentPopupPropertyImpl().getReadOnlyProperty();
    }

    private ReadOnlyObjectWrapper<ContextMenu> parentPopupPropertyImpl() {
        if (this.parentPopup == null) {
            this.parentPopup = new ReadOnlyObjectWrapper<>(this, "parentPopup");
        }
        return this.parentPopup;
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

    public final void setOnAction(EventHandler<ActionEvent> value) {
        onActionProperty().set(value);
    }

    public final EventHandler<ActionEvent> getOnAction() {
        if (this.onAction == null) {
            return null;
        }
        return this.onAction.get();
    }

    public final ObjectProperty<EventHandler<ActionEvent>> onActionProperty() {
        if (this.onAction == null) {
            this.onAction = new ObjectPropertyBase<EventHandler<ActionEvent>>() { // from class: javafx.scene.control.MenuItem.1
                @Override // javafx.beans.property.ObjectPropertyBase
                protected void invalidated() {
                    MenuItem.this.eventHandlerManager.setEventHandler(ActionEvent.ACTION, get());
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return MenuItem.this;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return "onAction";
                }
            };
        }
        return this.onAction;
    }

    public final void setOnMenuValidation(EventHandler<Event> value) {
        onMenuValidationProperty().set(value);
    }

    public final EventHandler<Event> getOnMenuValidation() {
        if (this.onMenuValidation == null) {
            return null;
        }
        return this.onMenuValidation.get();
    }

    public final ObjectProperty<EventHandler<Event>> onMenuValidationProperty() {
        if (this.onMenuValidation == null) {
            this.onMenuValidation = new ObjectPropertyBase<EventHandler<Event>>() { // from class: javafx.scene.control.MenuItem.2
                @Override // javafx.beans.property.ObjectPropertyBase
                protected void invalidated() {
                    MenuItem.this.eventHandlerManager.setEventHandler(MenuItem.MENU_VALIDATION_EVENT, get());
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return MenuItem.this;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return "onMenuValidation";
                }
            };
        }
        return this.onMenuValidation;
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
            this.disable = new SimpleBooleanProperty(this, "disable");
        }
        return this.disable;
    }

    public final void setVisible(boolean value) {
        visibleProperty().set(value);
    }

    public final boolean isVisible() {
        if (this.visible == null) {
            return true;
        }
        return this.visible.get();
    }

    public final BooleanProperty visibleProperty() {
        if (this.visible == null) {
            this.visible = new SimpleBooleanProperty(this, "visible", true);
        }
        return this.visible;
    }

    public final void setAccelerator(KeyCombination value) {
        acceleratorProperty().set(value);
    }

    public final KeyCombination getAccelerator() {
        if (this.accelerator == null) {
            return null;
        }
        return this.accelerator.get();
    }

    public final ObjectProperty<KeyCombination> acceleratorProperty() {
        if (this.accelerator == null) {
            this.accelerator = new SimpleObjectProperty(this, "accelerator");
        }
        return this.accelerator;
    }

    public final void setMnemonicParsing(boolean value) {
        mnemonicParsingProperty().set(value);
    }

    public final boolean isMnemonicParsing() {
        if (this.mnemonicParsing == null) {
            return true;
        }
        return this.mnemonicParsing.get();
    }

    public final BooleanProperty mnemonicParsingProperty() {
        if (this.mnemonicParsing == null) {
            this.mnemonicParsing = new SimpleBooleanProperty(this, "mnemonicParsing", true);
        }
        return this.mnemonicParsing;
    }

    @Override // javafx.css.Styleable
    public ObservableList<String> getStyleClass() {
        return this.styleClass;
    }

    public void fire() {
        Event.fireEvent(this, new ActionEvent(this, this));
    }

    public <E extends Event> void addEventHandler(EventType<E> eventType, EventHandler<E> eventHandler) {
        this.eventHandlerManager.addEventHandler(eventType, eventHandler);
    }

    public <E extends Event> void removeEventHandler(EventType<E> eventType, EventHandler<E> eventHandler) {
        this.eventHandlerManager.removeEventHandler(eventType, eventHandler);
    }

    @Override // javafx.event.EventTarget
    public EventDispatchChain buildEventDispatchChain(EventDispatchChain tail) {
        if (getParentPopup() != null) {
            getParentPopup().buildEventDispatchChain(tail);
        }
        if (getParentMenu() != null) {
            getParentMenu().buildEventDispatchChain(tail);
        }
        return tail.prepend(this.eventHandlerManager);
    }

    public Object getUserData() {
        return this.userData;
    }

    public void setUserData(Object value) {
        this.userData = value;
    }

    public ObservableMap<Object, Object> getProperties() {
        if (this.properties == null) {
            this.properties = FXCollections.observableMap(new HashMap());
        }
        return this.properties;
    }

    @Override // javafx.css.Styleable
    public String getTypeSelector() {
        return "MenuItem";
    }

    @Override // javafx.css.Styleable
    public Styleable getStyleableParent() {
        if (getParentMenu() == null) {
            return getParentPopup();
        }
        return getParentMenu();
    }

    @Override // javafx.css.Styleable
    public final ObservableSet<PseudoClass> getPseudoClassStates() {
        return FXCollections.emptyObservableSet();
    }

    @Override // javafx.css.Styleable
    public List<CssMetaData<? extends Styleable, ?>> getCssMetaData() {
        return Collections.emptyList();
    }

    @Deprecated
    public Node impl_styleableGetNode() {
        ContextMenu parentPopup = getParentPopup();
        if (parentPopup == null || !(parentPopup.getSkin() instanceof ContextMenuSkin)) {
            return null;
        }
        ContextMenuSkin skin = (ContextMenuSkin) parentPopup.getSkin();
        if (!(skin.getNode() instanceof ContextMenuContent)) {
            return null;
        }
        ContextMenuContent content = (ContextMenuContent) skin.getNode();
        Parent nodes = content.getItemsContainer();
        List<Node> childrenNodes = nodes.getChildrenUnmodifiable();
        for (int i2 = 0; i2 < childrenNodes.size(); i2++) {
            if (childrenNodes.get(i2) instanceof ContextMenuContent.MenuItemContainer) {
                ContextMenuContent.MenuItemContainer MenuRow = (ContextMenuContent.MenuItemContainer) childrenNodes.get(i2);
                if (equals(MenuRow.getItem())) {
                    return MenuRow;
                }
            }
        }
        return null;
    }

    public String toString() {
        StringBuilder sbuf = new StringBuilder(getClass().getSimpleName());
        boolean hasId = (this.id == null || "".equals(getId())) ? false : true;
        boolean hasStyleClass = !getStyleClass().isEmpty();
        if (!hasId) {
            sbuf.append('@');
            sbuf.append(Integer.toHexString(hashCode()));
        } else {
            sbuf.append("[id=");
            sbuf.append(getId());
            if (!hasStyleClass) {
                sbuf.append("]");
            }
        }
        if (hasStyleClass) {
            if (hasId) {
                sbuf.append(", ");
            } else {
                sbuf.append('[');
            }
            sbuf.append("styleClass=");
            sbuf.append((Object) getStyleClass());
            sbuf.append("]");
        }
        return sbuf.toString();
    }
}
