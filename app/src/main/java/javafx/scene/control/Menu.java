package javafx.scene.control;

import com.sun.javafx.collections.TrackableObservableList;
import com.sun.javafx.scene.control.Logging;
import javafx.beans.DefaultProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ObjectPropertyBase;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.ReadOnlyBooleanWrapper;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.event.EventDispatchChain;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.scene.Node;

@DefaultProperty("items")
/* loaded from: jfxrt.jar:javafx/scene/control/Menu.class */
public class Menu extends MenuItem {
    public static final EventType<Event> ON_SHOWING = new EventType<>(Event.ANY, "MENU_ON_SHOWING");
    public static final EventType<Event> ON_SHOWN = new EventType<>(Event.ANY, "MENU_ON_SHOWN");
    public static final EventType<Event> ON_HIDING = new EventType<>(Event.ANY, "MENU_ON_HIDING");
    public static final EventType<Event> ON_HIDDEN = new EventType<>(Event.ANY, "MENU_ON_HIDDEN");
    private ReadOnlyBooleanWrapper showing;
    private ObjectProperty<EventHandler<Event>> onShowing;
    private ObjectProperty<EventHandler<Event>> onShown;
    private ObjectProperty<EventHandler<Event>> onHiding;
    private ObjectProperty<EventHandler<Event>> onHidden;
    private final ObservableList<MenuItem> items;
    private static final String DEFAULT_STYLE_CLASS = "menu";
    private static final String STYLE_CLASS_SHOWING = "showing";

    public Menu() {
        this("");
    }

    public Menu(String text) {
        this(text, null);
    }

    public Menu(String text, Node graphic) {
        this(text, graphic, (MenuItem[]) null);
    }

    public Menu(String text, Node graphic, MenuItem... items) {
        super(text, graphic);
        this.onShowing = new ObjectPropertyBase<EventHandler<Event>>() { // from class: javafx.scene.control.Menu.2
            @Override // javafx.beans.property.ObjectPropertyBase
            protected void invalidated() {
                Menu.this.eventHandlerManager.setEventHandler(Menu.ON_SHOWING, get());
            }

            @Override // javafx.beans.property.ReadOnlyProperty
            public Object getBean() {
                return Menu.this;
            }

            @Override // javafx.beans.property.ReadOnlyProperty
            public String getName() {
                return "onShowing";
            }
        };
        this.onShown = new ObjectPropertyBase<EventHandler<Event>>() { // from class: javafx.scene.control.Menu.3
            @Override // javafx.beans.property.ObjectPropertyBase
            protected void invalidated() {
                Menu.this.eventHandlerManager.setEventHandler(Menu.ON_SHOWN, get());
            }

            @Override // javafx.beans.property.ReadOnlyProperty
            public Object getBean() {
                return Menu.this;
            }

            @Override // javafx.beans.property.ReadOnlyProperty
            public String getName() {
                return "onShown";
            }
        };
        this.onHiding = new ObjectPropertyBase<EventHandler<Event>>() { // from class: javafx.scene.control.Menu.4
            @Override // javafx.beans.property.ObjectPropertyBase
            protected void invalidated() {
                Menu.this.eventHandlerManager.setEventHandler(Menu.ON_HIDING, get());
            }

            @Override // javafx.beans.property.ReadOnlyProperty
            public Object getBean() {
                return Menu.this;
            }

            @Override // javafx.beans.property.ReadOnlyProperty
            public String getName() {
                return "onHiding";
            }
        };
        this.onHidden = new ObjectPropertyBase<EventHandler<Event>>() { // from class: javafx.scene.control.Menu.5
            @Override // javafx.beans.property.ObjectPropertyBase
            protected void invalidated() {
                Menu.this.eventHandlerManager.setEventHandler(Menu.ON_HIDDEN, get());
            }

            @Override // javafx.beans.property.ReadOnlyProperty
            public Object getBean() {
                return Menu.this;
            }

            @Override // javafx.beans.property.ReadOnlyProperty
            public String getName() {
                return "onHidden";
            }
        };
        this.items = new TrackableObservableList<MenuItem>() { // from class: javafx.scene.control.Menu.6
            @Override // com.sun.javafx.collections.TrackableObservableList
            protected void onChanged(ListChangeListener.Change<MenuItem> c2) {
                while (c2.next()) {
                    for (MenuItem item : c2.getRemoved()) {
                        item.setParentMenu(null);
                        item.setParentPopup(null);
                    }
                    for (MenuItem item2 : c2.getAddedSubList()) {
                        if (item2.getParentMenu() != null) {
                            Logging.getControlsLogger().warning("Adding MenuItem " + item2.getText() + " that has already been added to " + item2.getParentMenu().getText());
                            item2.getParentMenu().getItems().remove(item2);
                        }
                        item2.setParentMenu(Menu.this);
                        item2.setParentPopup(Menu.this.getParentPopup());
                    }
                }
                if (Menu.this.getItems().size() == 0 && Menu.this.isShowing()) {
                    Menu.this.showingPropertyImpl().set(false);
                }
            }
        };
        getStyleClass().add(DEFAULT_STYLE_CLASS);
        if (items != null) {
            getItems().addAll(items);
        }
        parentPopupProperty().addListener(observable -> {
            for (int i2 = 0; i2 < getItems().size(); i2++) {
                MenuItem item = getItems().get(i2);
                item.setParentPopup(getParentPopup());
            }
        });
    }

    private void setShowing(boolean value) {
        if (getItems().size() != 0) {
            if (value && isShowing()) {
                return;
            }
            if (value) {
                if (getOnMenuValidation() != null) {
                    Event.fireEvent(this, new Event(MENU_VALIDATION_EVENT));
                    for (MenuItem m2 : getItems()) {
                        if (!(m2 instanceof Menu) && m2.getOnMenuValidation() != null) {
                            Event.fireEvent(m2, new Event(MenuItem.MENU_VALIDATION_EVENT));
                        }
                    }
                }
                Event.fireEvent(this, new Event(ON_SHOWING));
            } else {
                Event.fireEvent(this, new Event(ON_HIDING));
            }
            showingPropertyImpl().set(value);
            Event.fireEvent(this, value ? new Event(ON_SHOWN) : new Event(ON_HIDDEN));
        }
    }

    public final boolean isShowing() {
        if (this.showing == null) {
            return false;
        }
        return this.showing.get();
    }

    public final ReadOnlyBooleanProperty showingProperty() {
        return showingPropertyImpl().getReadOnlyProperty();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public ReadOnlyBooleanWrapper showingPropertyImpl() {
        if (this.showing == null) {
            this.showing = new ReadOnlyBooleanWrapper() { // from class: javafx.scene.control.Menu.1
                @Override // javafx.beans.property.BooleanPropertyBase
                protected void invalidated() {
                    get();
                    if (Menu.this.isShowing()) {
                        Menu.this.getStyleClass().add(Menu.STYLE_CLASS_SHOWING);
                    } else {
                        Menu.this.getStyleClass().remove(Menu.STYLE_CLASS_SHOWING);
                    }
                }

                @Override // javafx.beans.property.SimpleBooleanProperty, javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return Menu.this;
                }

                @Override // javafx.beans.property.SimpleBooleanProperty, javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return Menu.STYLE_CLASS_SHOWING;
                }
            };
        }
        return this.showing;
    }

    public final ObjectProperty<EventHandler<Event>> onShowingProperty() {
        return this.onShowing;
    }

    public final void setOnShowing(EventHandler<Event> value) {
        onShowingProperty().set(value);
    }

    public final EventHandler<Event> getOnShowing() {
        return onShowingProperty().get();
    }

    public final ObjectProperty<EventHandler<Event>> onShownProperty() {
        return this.onShown;
    }

    public final void setOnShown(EventHandler<Event> value) {
        onShownProperty().set(value);
    }

    public final EventHandler<Event> getOnShown() {
        return onShownProperty().get();
    }

    public final ObjectProperty<EventHandler<Event>> onHidingProperty() {
        return this.onHiding;
    }

    public final void setOnHiding(EventHandler<Event> value) {
        onHidingProperty().set(value);
    }

    public final EventHandler<Event> getOnHiding() {
        return onHidingProperty().get();
    }

    public final ObjectProperty<EventHandler<Event>> onHiddenProperty() {
        return this.onHidden;
    }

    public final void setOnHidden(EventHandler<Event> value) {
        onHiddenProperty().set(value);
    }

    public final EventHandler<Event> getOnHidden() {
        return onHiddenProperty().get();
    }

    public final ObservableList<MenuItem> getItems() {
        return this.items;
    }

    public void show() {
        if (isDisable()) {
            return;
        }
        setShowing(true);
    }

    public void hide() {
        if (isShowing()) {
            for (MenuItem i2 : getItems()) {
                if (i2 instanceof Menu) {
                    Menu m2 = (Menu) i2;
                    m2.hide();
                }
            }
            setShowing(false);
        }
    }

    @Override // javafx.scene.control.MenuItem
    public <E extends Event> void addEventHandler(EventType<E> eventType, EventHandler<E> eventHandler) {
        this.eventHandlerManager.addEventHandler(eventType, eventHandler);
    }

    @Override // javafx.scene.control.MenuItem
    public <E extends Event> void removeEventHandler(EventType<E> eventType, EventHandler<E> eventHandler) {
        this.eventHandlerManager.removeEventHandler(eventType, eventHandler);
    }

    @Override // javafx.scene.control.MenuItem, javafx.event.EventTarget
    public EventDispatchChain buildEventDispatchChain(EventDispatchChain tail) {
        return tail.prepend(this.eventHandlerManager);
    }
}
