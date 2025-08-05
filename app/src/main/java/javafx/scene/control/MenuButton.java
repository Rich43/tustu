package javafx.scene.control;

import com.sun.javafx.scene.control.skin.MenuButtonSkin;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ObjectPropertyBase;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.ReadOnlyBooleanWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.css.PseudoClass;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventType;
import javafx.geometry.Side;
import javafx.scene.AccessibleAction;
import javafx.scene.AccessibleRole;
import javafx.scene.Node;

/* loaded from: jfxrt.jar:javafx/scene/control/MenuButton.class */
public class MenuButton extends ButtonBase {
    private final ObservableList<MenuItem> items;
    private ReadOnlyBooleanWrapper showing;
    private ObjectProperty<Side> popupSide;
    private static final String DEFAULT_STYLE_CLASS = "menu-button";
    public static final EventType<Event> ON_SHOWING = new EventType<>(Event.ANY, "MENU_BUTTON_ON_SHOWING");
    public static final EventType<Event> ON_SHOWN = new EventType<>(Event.ANY, "MENU_BUTTON_ON_SHOWN");
    public static final EventType<Event> ON_HIDING = new EventType<>(Event.ANY, "MENU_BUTTON_ON_HIDING");
    public static final EventType<Event> ON_HIDDEN = new EventType<>(Event.ANY, "MENU_BUTTON_ON_HIDDEN");
    private static final PseudoClass PSEUDO_CLASS_OPENVERTICALLY = PseudoClass.getPseudoClass("openvertically");
    private static final PseudoClass PSEUDO_CLASS_SHOWING = PseudoClass.getPseudoClass("showing");

    public MenuButton() {
        this(null, null);
    }

    public MenuButton(String text) {
        this(text, null);
    }

    public MenuButton(String text, Node graphic) {
        this(text, graphic, (MenuItem[]) null);
    }

    public MenuButton(String text, Node graphic, MenuItem... items) {
        this.items = FXCollections.observableArrayList();
        this.showing = new ReadOnlyBooleanWrapper(this, "showing", false) { // from class: javafx.scene.control.MenuButton.1
            @Override // javafx.beans.property.BooleanPropertyBase
            protected void invalidated() {
                MenuButton.this.pseudoClassStateChanged(MenuButton.PSEUDO_CLASS_SHOWING, get());
                super.invalidated();
            }
        };
        if (text != null) {
            setText(text);
        }
        if (graphic != null) {
            setGraphic(graphic);
        }
        if (items != null) {
            getItems().addAll(items);
        }
        getStyleClass().setAll(DEFAULT_STYLE_CLASS);
        setAccessibleRole(AccessibleRole.MENU_BUTTON);
        setMnemonicParsing(true);
        pseudoClassStateChanged(PSEUDO_CLASS_OPENVERTICALLY, true);
    }

    public final ObservableList<MenuItem> getItems() {
        return this.items;
    }

    private void setShowing(boolean value) {
        Event.fireEvent(this, value ? new Event(ComboBoxBase.ON_SHOWING) : new Event(ComboBoxBase.ON_HIDING));
        this.showing.set(value);
        Event.fireEvent(this, value ? new Event(ComboBoxBase.ON_SHOWN) : new Event(ComboBoxBase.ON_HIDDEN));
    }

    public final boolean isShowing() {
        return this.showing.get();
    }

    public final ReadOnlyBooleanProperty showingProperty() {
        return this.showing.getReadOnlyProperty();
    }

    public final void setPopupSide(Side value) {
        popupSideProperty().set(value);
    }

    public final Side getPopupSide() {
        return this.popupSide == null ? Side.BOTTOM : this.popupSide.get();
    }

    public final ObjectProperty<Side> popupSideProperty() {
        if (this.popupSide == null) {
            this.popupSide = new ObjectPropertyBase<Side>(Side.BOTTOM) { // from class: javafx.scene.control.MenuButton.2
                @Override // javafx.beans.property.ObjectPropertyBase
                protected void invalidated() {
                    Side side = get();
                    boolean active = side == Side.TOP || side == Side.BOTTOM;
                    MenuButton.this.pseudoClassStateChanged(MenuButton.PSEUDO_CLASS_OPENVERTICALLY, active);
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return MenuButton.this;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return "popupSide";
                }
            };
        }
        return this.popupSide;
    }

    public void show() {
        if (!isDisabled() && !this.showing.isBound()) {
            setShowing(true);
        }
    }

    public void hide() {
        if (!this.showing.isBound()) {
            setShowing(false);
        }
    }

    @Override // javafx.scene.control.ButtonBase
    public void fire() {
        if (!isDisabled()) {
            fireEvent(new ActionEvent());
        }
    }

    @Override // javafx.scene.control.Control
    protected Skin<?> createDefaultSkin() {
        return new MenuButtonSkin(this);
    }

    @Override // javafx.scene.control.ButtonBase, javafx.scene.control.Control, javafx.scene.Node
    public void executeAccessibleAction(AccessibleAction action, Object... parameters) {
        switch (action) {
            case FIRE:
                if (isShowing()) {
                    hide();
                    break;
                } else {
                    show();
                    break;
                }
            default:
                super.executeAccessibleAction(action, new Object[0]);
                break;
        }
    }
}
